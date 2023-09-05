package louie.bank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public final class Bank extends JavaPlugin {

    private static Bank instance;

    public static Bank getInstance(){
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        Bukkit.getConsoleSender().sendMessage("§7[§aBank_v.01§7]§a is now starting up!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage("§7[§aBank_v.01§7]§c is now starting up!");
    }
}
