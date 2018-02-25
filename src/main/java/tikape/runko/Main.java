package tikape.runko;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.DrinkkiDao;
import tikape.runko.database.AineDao;
import tikape.runko.database.DrinkkiAineDao;
import tikape.runko.domain.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:Drinkkiarkisto.db");
        
        DrinkkiDao drinkit = new DrinkkiDao(database);
        AineDao aineet  = new AineDao(database);
        DrinkkiAineDao ohje = new DrinkkiAineDao(database); 

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            
            map.put("drinkit", drinkit.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/drinkit", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("drinkit", drinkit.findAll());
            map.put("aineet", drinkit.findAll());

            return new ModelAndView(map, "drinkit");
        }, new ThymeleafTemplateEngine());

        get("/drinkit/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int drinkkiId = Integer.parseInt(req.params(":id"));
            map.put("drinkki", drinkit.findOne(drinkkiId));
            map.put("ohje", ohje.findOhje(drinkkiId));

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
        
        post("/lisaaAine", (req, res) -> {
           RaakaAine aine = new RaakaAine(-1, (req.queryParams("nimi")));
           aineet.save(aine);
           
           res.redirect("/aineet");
           return "";
        });
    }
}
