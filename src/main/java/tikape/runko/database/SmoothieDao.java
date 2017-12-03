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
import java.util.List;
import tikape.runko.domain.Smoothie;

public class SmoothieDao implements Dao<Smoothie, Integer> {

    private Database database;

    public SmoothieDao(Database database) {
        this.database = database;
    }

    @Override
    public Smoothie findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Smoothie s = new Smoothie(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return s;
    }

    @Override
    public Smoothie saveOrUpdate(Smoothie smoothie) throws SQLException {

        Smoothie byName = findByName(smoothie.getNimi());

        if (byName != null) {
            return byName;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Annos (nimi) VALUES (?)");
            stmt.setString(1, smoothie.getNimi());
            stmt.executeUpdate();
        }

        return findByName(smoothie.getNimi());
    }

    private Smoothie findByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM Annos WHERE nimi = ?");
            stmt.setString(1, name);

            try (ResultSet result = stmt.executeQuery()) {
                if (!result.next()) {
                    return null;
                }
                return createFromRow(result);
            }
        }
    }

    public Smoothie createFromRow(ResultSet resultSet) throws SQLException {
        return new Smoothie(resultSet.getInt("id"), resultSet.getString("nimi"));
    }

    @Override
    public List<Smoothie> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos");

        ResultSet rs = stmt.executeQuery();
        List<Smoothie> smoothiet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            smoothiet.add(new Smoothie(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return smoothiet;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Annos WHERE id = ?");
        stmt.setObject(1, key);
        stmt.execute();
    }

}
