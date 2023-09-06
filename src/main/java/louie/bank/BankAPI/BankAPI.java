package louie.bank.BankAPI;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface BankAPI {


    /**
     * Returns the players balance.
     * 
     * @param player Players UUID
     * @return Players current Balance
     */
    CompletableFuture<Integer> getPlayersBalance(UUID player);


    /**
     * FULLY Updates Players Balance.
     * DO NOT USE THIS TO ADD OR REMOVE MONEY THERE ARE METHODS FOR THAT,
     * SO BEWARE THIS WILL AND CAN RESET THE PLAYERS BALANCE IF NOT CAREFUL ENOUGH!
     * 
     * @param player Players UUID
     * @param value Value that replaces the players current balance with
     * @return If it successfully updated the players balance
     */    
    CompletableFuture<Boolean> updatePlayersBalance(UUID player, int value);


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
    CompletableFuture<Boolean> addMoney(UUID player, int value);


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
    CompletableFuture<Boolean> removeMoney(UUID player, int value);
}
