/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tikape.runko.domain.Drinkki;

public class DrinkkiDao implements Dao<Drinkki, Integer> {

    private Database database;

    public DrinkkiDao(Database database) {
        this.database = database;
    }

    @Override
    public Drinkki findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Drinkki WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Drinkki o = new Drinkki(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    @Override
    public List<Drinkki> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Drinkki");

        ResultSet rs = stmt.executeQuery();
        List<Drinkki> drinkit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            drinkit.add(new Drinkki(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return drinkit;
    }
    
    public List<Drinkki> findDrinkit(Integer raakaAineId) throws SQLException {

        Connection connection = database.getConnection();

        PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT Drinkki.id, Drinkki.nimi FROM DrinkkiRaakaAine, Drinkki "
                + "WHERE DrinkkiRaakaAine.raakaaine_id = ? "
                + "AND Drinkki.id = DrinkkiRAakaAine.drinkki_id");
        
        stmt.setInt(1, raakaAineId);

        ResultSet rs = stmt.executeQuery();
        
        List<Drinkki> drinkit = new ArrayList<>();
        
        while (rs.next()) {
            int id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            
            drinkit.add(new Drinkki(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();
        
        return drinkit;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

}
