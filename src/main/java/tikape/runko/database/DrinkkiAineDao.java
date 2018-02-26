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
import tikape.runko.domain.DrinkkiRaakaAine;

public class DrinkkiAineDao implements Dao<DrinkkiRaakaAine, Integer> {

    private Database database;

    public DrinkkiAineDao(Database database) {
        this.database = database;
    }

    @Override
    public DrinkkiRaakaAine findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
        /*Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        DrinkkiRaakaAine o = new DrinkkiRaakaAine(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return o;*/
    }

    public List<DrinkkiRaakaAine> findOhje(Integer DrinkkiId) throws SQLException {

        Connection connection = database.getConnection();

        PreparedStatement stmt = connection.prepareStatement("SELECT RaakaAine.nimi, DrinkkiRaakaAine.drinkki_id, DrinkkiRaakaAine.raakaaine_id, "
                + "DrinkkiRaakaAine.jarjestys, DrinkkiRaakaAine.maara, DrinkkiRaakaAine.ohje FROM DrinkkiRaakaAine, Drinkki, RaakaAine "
                + "WHERE Drinkki.id = DrinkkiRaakaAine.drinkki_id "
                + "AND RaakaAine.id = DrinkkiRaakaAine.raakaaine_id "
                + "AND DrinkkiRaakaAine.drinkki_id = ?");

        stmt.setInt(1, DrinkkiId);

        ResultSet rs = stmt.executeQuery();

        List<DrinkkiRaakaAine> aineet = new ArrayList<>();

        while (rs.next()) {
            String nimi = rs.getString("nimi");
            int drinkkiId = rs.getInt("drinkki_id");
            int aineId = rs.getInt("raakaaine_id");
            int jarjestys = rs.getInt("jarjestys");
            String maara = rs.getString("maara");
            String ohje = rs.getString("ohje");

            aineet.add(new DrinkkiRaakaAine(nimi, drinkkiId, aineId, jarjestys, maara, ohje));
        }

        rs.close();
        stmt.close();
        connection.close();

        Collections.sort(aineet);

        return aineet;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

    public void deleteByIds(Integer key1, Integer key2) throws SQLException {

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM DrinkkiRaakaAine WHERE drinkki_id = ? AND raakaaine_id = ?");
            stmt.setInt(1, key1);
            stmt.setInt(2, key2);
            stmt.executeUpdate();
        }

    }

    @Override
    public List<DrinkkiRaakaAine> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DrinkkiRaakaAine saveOrUpdate(DrinkkiRaakaAine aine) throws SQLException {

        DrinkkiRaakaAine check = findByIds(aine.getDrinkkiId(), aine.getRaakaAineId());

        if (check != null) {
            return null;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO DrinkkiRaakaAine (drinkki_id, raakaaine_id, jarjestys, "
                    + "maara, ohje) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, aine.getDrinkkiId());
            stmt.setInt(2, aine.getRaakaAineId());
            stmt.setInt(3, aine.getJarjestys());
            stmt.setString(4, aine.getMaara());
            stmt.setString(5, aine.getOhje());
            stmt.executeUpdate();
        }

        return findByIds(aine.getDrinkkiId(), aine.getRaakaAineId());

    }

    private DrinkkiRaakaAine findByIds(Integer drinkkiId, Integer raakaAineId) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT RaakaAine.nimi, DrinkkiRaakaAine.drinkki_id, DrinkkiRaakaAine.raakaaine_id, "
                    + "DrinkkiRaakaAine.jarjestys, DrinkkiRaakaAine.maara, DrinkkiRaakaAine.ohje FROM DrinkkiRaakaAine, Drinkki, RaakaAine "
                    + "WHERE Drinkki.id = DrinkkiRaakaAine.drinkki_id "
                    + "AND RaakaAine.id = DrinkkiRaakaAine.raakaaine_id "
                    + "AND DrinkkiRaakaAine.raakaaine_id = ? "
                    + "AND DrinkkiRaakaAine.drinkki_id = ?");

            stmt.setInt(1, raakaAineId);
            stmt.setInt(2, drinkkiId);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            String nimi = result.getString("nimi");
            int jarjestys = result.getInt("jarjestys");
            String maara = result.getString("maara");
            String ohje = result.getString("ohje");

            return new DrinkkiRaakaAine(nimi, drinkkiId, raakaAineId, jarjestys, maara, ohje);
        }
        
        
    }
    
    
    

}
