package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tikape.runko.domain.SmoothieRaakaAine;
import java.util.List;
import java.util.ArrayList;
import tikape.runko.domain.RaakaAine;

public class SmoothieRaakaAineDao {

    private Database database;

    public SmoothieRaakaAineDao(Database database) {
        this.database = database;
    }

    public void saveOrUpdate(SmoothieRaakaAine smoothieRaakaAine, Integer annos) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO AnnosRaakaAine VALUES (?,?,?,?,?)");
            stmt.setInt(1, smoothieRaakaAine.getRaakaAine().getId());
            stmt.setInt(2, annos);
            stmt.setInt(3, smoothieRaakaAine.getJarjestys());
            stmt.setInt(4, smoothieRaakaAine.getMaara());
            stmt.setString(5, smoothieRaakaAine.getOhje());
            stmt.executeUpdate();
        }
    }

    public List<SmoothieRaakaAine> findAllWhereSmoothieIs(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE annos_id = ?");
        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        List<SmoothieRaakaAine> s = new ArrayList<>();
        while (rs.next()) {
            stmt = connection.prepareStatement("SELECT nimi FROM RaakaAine WHERE id = ?");
            stmt.setObject(1, rs.getInt("annos_id"));
            String nimi = stmt.executeQuery().getString(1);
            s.add(new SmoothieRaakaAine(new RaakaAine(rs.getInt("annos_id"), nimi), rs.getInt("jarjestys"), rs.getInt("maara"), rs.getString("ohje")));
        }

        rs.close();
        stmt.close();
        connection.close();

        return s;
    }

    public void deleteByAnnos(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM AnnosRaakaAine WHERE annos_id = ?");
        stmt.setObject(1, key);
        stmt.execute();
    }

    public void deleteByRaakaAine(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM AnnosRaakaAine WHERE raaka_aine_id = ?");
        stmt.setObject(1, key);
        stmt.execute();
    }
}
