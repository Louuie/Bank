package louie.bank.BankAPI;

import louie.bank.Bank;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static louie.bank.Database.Database.initializeDatabaseConnection;
import static louie.bank.Database.Database.insertNewPlayer;

// I added this
public class BankService implements BankAPI {
    static Bank bankClass = Bank.getInstance();

    /**
     * Returns the players balance.
     * 
     * @param player Players UUID
     * @return Players current Balance
     */
    @Override
    public CompletableFuture<Integer> getPlayersBalance(UUID player) {
        CompletableFuture<Integer> result = new CompletableFuture<>();
        checkForNullUserThenInsert(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            String postgresql = "SELECT balance FROM BANK_API WHERE uuid=?";
                            Connection databaseConnection = initializeDatabaseConnection();
                            PreparedStatement stmt = databaseConnection.prepareStatement(postgresql);
                            stmt.setString(1, player.toString());
                            ResultSet rs = stmt.executeQuery();
                            if (rs.next()) {
                                int balance = rs.getInt("balance");
                                result.complete(balance);
                            }
                            databaseConnection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }.runTaskAsynchronously(bankClass);
            }
        }.runTaskLater(bankClass, 20 / 2 / 2);
        return result;
    }

    /**
     * FULLY Updates Players Balance.
     * DO NOT USE THIS TO ADD OR REMOVE MONEY THERE ARE METHODS FOR THAT,
     * SO BEWARE THIS WILL AND CAN RESET THE PLAYERS BALANCE IF NOT CAREFUL ENOUGH!
     * 
     * @param player Players UUID
     * @param value Value that replaces the players current balance with
     * @return If it successfully updated the players balance
     */
    @Override
    public CompletableFuture<Boolean> updatePlayersBalance(UUID player, int value) {
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String postgresql = "UPDATE BANK_API SET balance = ? WHERE uuid = ?";
                    Connection databaseConnection = initializeDatabaseConnection();
                    PreparedStatement stmt = databaseConnection.prepareStatement(postgresql);
                    stmt.setInt(1, value);
                    stmt.setString(2, player.toString());
                    stmt.executeUpdate();
                    result.complete(true);
                    databaseConnection.close();
                } catch (SQLException e) {
                    result.complete(false);
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(bankClass);
        return result;
    }

    /**
     * Adds money to the players balance without having to fetch the balance then
     * add it,
     * so please DO NOT fetch the balance then add that using this method that will
     * add x6 the amount you intended to!
     * 
     * @param player Players UUID
     * @param value Value that adds to the players current balance
     * @return If it successfully added the value to the players balance
     */
    @Override
    public CompletableFuture<Boolean> addMoney(UUID player, int value) {
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        getPlayersBalance(player).thenAccept(balance -> {
            updatePlayersBalance(player, balance + value).thenAccept(result::complete);
        });
        return result;
    }

    /**
     * Removes money to the players balance without having to fetch the balance then
     * remove it.
     * so please DO NOT fetch the balance then add that using this method that will
     * remove x6 the amount you intended to!
     * 
     * @param player Players UUID
     * @param value Value that removes from the players current balance
     * @return If it successfully removed the value from the players balance
     */
    @Override
    public CompletableFuture<Boolean> removeMoney(UUID player, int value) {
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        getPlayersBalance(player).thenAccept(balance -> {
            updatePlayersBalance(player, balance - value).thenAccept(result::complete);
        });
        return result;
    }

    /**
     * This method checks if a player with the given UUID exists in the BANK_API
     * database table.
     * If the player does not exist, it inserts a new record for the player into the
     * table.
     *
     * @param player The UUID of the player to check and insert if necessary.
     */
    private void checkForNullUserThenInsert(UUID player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String postgres = "SELECT uuid FROM BANK_API WHERE uuid=?";
                    Connection databaseConnection = initializeDatabaseConnection();
                    PreparedStatement stmt = databaseConnection.prepareStatement(postgres);
                    stmt.setString(1, player.toString());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        Bukkit.getLogger().info("Database: " + player + " user exists!");
                    } else {
                        Bukkit.getLogger().info(
                                "Database: " + player + " user DOES NOT exists, inserting them into the database!");
                        insertNewPlayer(player);
                    }
                    databaseConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(bankClass);
    }

}
