package tikape.runko;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.DrinkkiDao;
import tikape.runko.database.AineDao;
import tikape.runko.database.DrinkkiAineDao;
import tikape.runko.domain.Drinkki;
import tikape.runko.domain.DrinkkiRaakaAine;
import tikape.runko.domain.RaakaAine;
import tikape.runko.domain.*;

public class Main {

    public static void main(String[] args) throws Exception {

        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }

        staticFileLocation("/templates");

        Database database = new Database("jdbc:sqlite:Drinkkiarkisto.db");

        DrinkkiDao drinkit = new DrinkkiDao(database);
        AineDao aineet = new AineDao(database);
        DrinkkiAineDao ohjeet = new DrinkkiAineDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();

            map.put("drinkit", drinkit.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/drinkit", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("drinkit", drinkit.findAll());
            map.put("aineet", aineet.findAll());

            return new ModelAndView(map, "drinkit");
        }, new ThymeleafTemplateEngine());

        get("/drinkit/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int drinkkiId = Integer.parseInt(req.params(":id"));
            map.put("drinkki", drinkit.findOne(drinkkiId));
            map.put("ohje", ohjeet.findOhje(drinkkiId));

            return new ModelAndView(map, "drinkki");
        }, new ThymeleafTemplateEngine());

        get("/aineet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int raakaAineId = Integer.parseInt(req.params(":id"));
            map.put("aine", aineet.findOne(raakaAineId));
            map.put("drinkit", drinkit.findDrinkit(raakaAineId));

            return new ModelAndView(map, "aine");
        }, new ThymeleafTemplateEngine());

        get("/aineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("aineet", aineet.findAll());

            return new ModelAndView(map, "aineet");
        }, new ThymeleafTemplateEngine());

        Spark.post("/aineet", (req, res) -> {
            RaakaAine aine = new RaakaAine(-1, req.queryParams("name"));
            aineet.saveOrUpdate(aine);

            res.redirect("/aineet");
            return "";
        });

        Spark.post("/drinkit", (req, res) -> {
            Drinkki drinkki = new Drinkki(-1, req.queryParams("name"));
            drinkit.saveOrUpdate(drinkki);

            res.redirect("/drinkit");
            return "";
        });
        
        Spark.post("/uusiaine", (req, res) -> {
            RaakaAine aine = new RaakaAine(-1, req.queryParams("name"));
            aineet.saveOrUpdate(aine);

            res.redirect("/drinkit");
            return "";
        });

        Spark.post("/lisaa", (req, res) -> {

            int aineId = Integer.parseInt(req.queryParams("aine"));
            int drinkkiId = Integer.parseInt(req.queryParams("drinkki"));
            int jarjestys = Integer.parseInt(req.queryParams("jarj"));
            String maara = req.queryParams("maara");
            String ohje = req.queryParams("ohje");

            RaakaAine raakaAine = aineet.findById(aineId);

            DrinkkiRaakaAine aine = new DrinkkiRaakaAine(raakaAine.getNimi(), drinkkiId, aineId, jarjestys, maara, ohje);
            DrinkkiRaakaAine test = ohjeet.saveOrUpdate(aine);

            res.redirect("/drinkit");
            return "";
        });

        Spark.post("/drinkit/:id/delete", (req, res) -> {

            int drinkkiId = Integer.parseInt(req.params(":id"));
            drinkit.delete(drinkkiId);

            res.redirect("/drinkit");
            return "";
        });

        Spark.post("/aineet/:id/delete", (req, res) -> {

            int aineId = Integer.parseInt(req.params(":id"));
            aineet.delete(aineId);

            res.redirect("/aineet");
            return "";
        });

        Spark.post("/drinkit/:id1/:id2/delete", (req, res) -> {
            int drinkkiId = Integer.parseInt(req.params(":id1"));
            int aineId = Integer.parseInt(req.params(":id2"));

            ohjeet.deleteByIds(drinkkiId, aineId);

            res.redirect("/drinkit/" + drinkkiId);
            return "";

        });
    }
}
