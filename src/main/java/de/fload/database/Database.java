package de.fload.database;

import de.fload.core.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static de.fload.util.FUNCTION.outputException;

/**
 * Database class - for interaction with the database.
 */
public class Database {

    /**
     * Connects to the database.
     *
     * @return Returns a {@link Connection} to the database.
     */
    static Connection connect() {
        Connection con = null;
        try {
            String url = "jdbc:sqlite:" + Settings.getInstance().getDbPath();
            con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            outputException(e);
        }
        return con;
    }

    /**
     * Performs a simple statement on the database.
     *
     * @param statement String for the SQL-Statement.
     */
    static void commitStatement(String statement) {
        try (Connection con = connect();
             PreparedStatement pstmt = con.prepareStatement(statement)) {

            pstmt.execute();
        } catch (SQLException e) {
            outputException(e);
        }
    }
}
