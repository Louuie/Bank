package louie.bank.Database;

import louie.bank.Bank;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.UUID;

public class Database {

    private static Bank bankAPIClass = Bank.getInstance();

    public static Connection initializeDatabaseConnection() {
        String url = "jdbc:postgresql://localhost:5445/BankAPI";
        String user = "bankapiUser";
        String password = "bankapiDB123";
        Connection connection = null;

        try {
            // Load the PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // Establish the connection
            connection = DriverManager.getConnection(url, user, password);

            if (connection != null) {
                Bukkit.getLogger().info("Connected to the database");
            } else
                Bukkit.getLogger().info("Failed to connect to the database");

        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().info("Error: PostgreSQL driver not found.");
        } catch (SQLException e) {
            Bukkit.getLogger().info("Error: Unable to connect to the database.");
            e.printStackTrace();
        }

        return connection;
    }

    public static void createTable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection databaseConnection = initializeDatabaseConnection();
                    String postgresql = "CREATE TABLE IF NOT EXISTS BANK_API (uuid VARCHAR NOT NULL, balance INTEGER NOT NULL, PRIMARY KEY(uuid))";
                    PreparedStatement stmt = databaseConnection.prepareStatement(postgresql);
                    stmt.executeUpdate();
                    databaseConnection.close();
                    Bukkit.getLogger().info("Successfully created table BANK_API");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(bankAPIClass);
    }

    public static void insertNewPlayer(UUID player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection databaseConnection = initializeDatabaseConnection();
                    String postgresql = "INSERT INTO BANK_API VALUES (?, ?)";
                    PreparedStatement stmt = databaseConnection.prepareStatement(postgresql);
                    stmt.setString(1, player.toString());
                    stmt.setInt(2, 100);
                    stmt.executeUpdate();
                    databaseConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(bankAPIClass);
    }
}
