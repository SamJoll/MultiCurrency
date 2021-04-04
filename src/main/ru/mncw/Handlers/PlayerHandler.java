package main.ru.mncw.Handlers;

import main.ru.mncw.DataBases.PlayersDB;
import main.ru.mncw.MultiCurrency;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerHandler implements Listener {

//    Класс плагина
    MultiCurrency plugin;
//    Класс БД игроков
    PlayersDB PlayersDB;

//    Конструктор класса
//    Создаем экземпляры классов выше
    public PlayerHandler(MultiCurrency plugin) {
        this.plugin = plugin;

        try {
            PlayersDB = new PlayersDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if(PlayersDB.AddNewPlayer(player)) {
            Bukkit.getPlayer(player.getUniqueId()).sendMessage(plugin.getConfig().getString("messages.actions.player-wallet-created"));
        } else {
            Bukkit.getPlayer(player.getUniqueId()).sendMessage(plugin.getConfig().getString("messages.errors.player-wallet-created-error"));
        }
    }
}
