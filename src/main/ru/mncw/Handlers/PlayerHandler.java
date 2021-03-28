package main.ru.mncw.Handlers;

import main.ru.mncw.DataBases.PlayersWalletDB;
import main.ru.mncw.MultiCurrency;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerHandler implements Listener {

    MultiCurrency plugin;
    PlayersWalletDB PlayerWDB;

    public PlayerHandler(MultiCurrency plugin) {
        this.plugin = plugin;

        try {
            PlayerWDB = new PlayersWalletDB(plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent e) {
        if(PlayerWDB.AddNewPlayer(e.getPlayer())) {
            Bukkit.getPlayer(e.getPlayer().getName()).sendMessage(plugin.getConfig().getString("messages.actions.player-wallet-created"));
        } else {
            Bukkit.getPlayer(e.getPlayer().getName()).sendMessage(plugin.getConfig().getString("messages.actions.player-wallet-created-error"));
        }
    }
}
