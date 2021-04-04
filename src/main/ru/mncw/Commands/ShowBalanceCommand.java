package main.ru.mncw.Commands;

import main.ru.mncw.DataBases.PlayersDB;
import main.ru.mncw.DataBases.PlayersTXDB;
import main.ru.mncw.MultiCurrency;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowBalanceCommand implements CommandExecutor {

//    Класс плагина
    MultiCurrency plugin;
//    Класс БД игроков
    PlayersDB PlayersDB;

//    Конструктор класса
//    Создание экземпляра классов
    public ShowBalanceCommand(MultiCurrency plugin) {
        this.plugin = plugin;

        try {
            PlayersDB = new PlayersDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender playerSender, Command comm, String label, String[] args) {
        if(playerSender instanceof Player) {
            if (PlayersDB.GetBalance((Player) playerSender) >= 0 && args.length <= 0) {
                Bukkit.getPlayer(
                        playerSender.getName()).sendMessage(
                                plugin.getConfig().getString("messages.actions.player-showbalance").replace("%balance%", String.valueOf(PlayersDB.GetBalance((Player)playerSender)
                        ))
                );

                return true;
            } else {
                Bukkit.getPlayer(playerSender.getName()).sendMessage(plugin.getConfig().getString("messages.errors.bank-error-notify"));

                return false;
            }
        } else {
            return false;
        }
    }
}
