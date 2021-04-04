package main.ru.mncw.Commands;

import main.ru.mncw.DataBases.PlayersDB;
import main.ru.mncw.DataBases.PlayersTXDB;
import main.ru.mncw.MultiCurrency;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendMoneyCommand implements CommandExecutor {

//    Класс плагина
    MultiCurrency plugin;
//    Класс БД игроков
    PlayersDB PlayersDB;
//    Класс БД с транзакциями
    PlayersTXDB PlayersTXDB;

//    Конструктор класса
//    Создание экземпляра класса
    public SendMoneyCommand(MultiCurrency plugin) {
        this.plugin = plugin;

        try {
            PlayersDB = new PlayersDB();
            PlayersTXDB = new PlayersTXDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender playerSender, Command comm, String label, String[] args) {
        if(playerSender instanceof Player) {
            if(args.length == 2 && PlayersDB.TransferMoney((Player)playerSender, Bukkit.getPlayer(args[0]), Double.valueOf(args[1]))) {
                Bukkit.getPlayer(playerSender.getName()).sendMessage(plugin.getConfig().getString("messages.actions.player-sendmoney").replace("%player%", args[0]).replace("%amount%", args[1]));
                Bukkit.getPlayer(args[0]).sendMessage(plugin.getConfig().getString("messages.actions.player-getsendmoney").replace("%player%", playerSender.getName()).replace("%amount%", args[1]));

//                =============ФИКСАЦИЯ ОПЕРАЦИИ=============
                PlayersTXDB.WriteTransaction(
                        Bukkit.getPlayer(playerSender.getName()),
                        plugin.getConfig().getString("transactions.sendmoney").replace("%target-player%", args[0]).replace("%player%", playerSender.getName()).replace("%amount%", args[1]),
                        plugin.getConfig().getString("transactions.status.success")
                );

                PlayersTXDB.WriteTransaction(
                        Bukkit.getPlayer(args[0]),
                        plugin.getConfig().getString("transactions.sendmoney").replace("%target-player%", args[0]).replace("%player%", playerSender.getName()).replace("%amount%", args[1]),
                        plugin.getConfig().getString("transactions.status.success")
                );
//                =============================================

                return true;
            }
            else if(args.length == 2 && !PlayersDB.TransferMoney((Player)playerSender, Bukkit.getPlayer(args[0]), Double.valueOf(args[1]))) {
                Bukkit.getPlayer(playerSender.getName()).sendMessage(plugin.getConfig().getString("messages.errors.bank-error-notify"));

//                =============ФИКСАЦИЯ ОПЕРАЦИИ=============
                PlayersTXDB.WriteTransaction(
                        Bukkit.getPlayer(playerSender.getName()),
                        plugin.getConfig().getString("transactions.sendmoney").replace("%target-player%", args[0]).replace("%player%", playerSender.getName()).replace("%amount%", args[1]),
                        plugin.getConfig().getString("transactions.status.not-success")
                );

                PlayersTXDB.WriteTransaction(
                        Bukkit.getPlayer(args[0]),
                        plugin.getConfig().getString("transactions.sendmoney").replace("%target-player%", args[0]).replace("%player%", playerSender.getName()).replace("%amount%", args[1]),
                        plugin.getConfig().getString("transactions.status.not-success")
                );
//                =============================================

                return true;
            }
            else {
                Bukkit.getPlayer(playerSender.getName()).sendMessage(plugin.getConfig().getString("messages.errors.bank-error-notify"));

                return false;
            }
        } else {
            return false;
        }
    }
}
