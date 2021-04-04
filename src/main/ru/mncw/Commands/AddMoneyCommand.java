package main.ru.mncw.Commands;

import main.ru.mncw.DataBases.PlayersDB;
import main.ru.mncw.DataBases.PlayersTXDB;
import main.ru.mncw.MultiCurrency;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddMoneyCommand implements CommandExecutor {
//    Класс плагина
    MultiCurrency plugin;
//    Класс БД игроков
    PlayersDB PlayersDB;
//    Класс БД транзакций
    PlayersTXDB PlayersTXDB;

//    Конструктор класса
//    Создание экземпляра классов выше
    public AddMoneyCommand(MultiCurrency plugin) {

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
//        Игрок ли отправил команду?
        if(playerSender instanceof Player) {
//            ===========/addmoney PLAYER AMOUNT===========
            if(args.length == 2 && PlayersDB.AddMoney(Bukkit.getPlayer(args[0]), Double.valueOf(args[1]))) {
                Bukkit.getPlayer(playerSender.getName()).sendMessage(plugin.getConfig().getString("messages.actions.player-addmoney").replace("%player%", args[0]).replace("%amount%", args[1]));
                Bukkit.getPlayer(args[0]).sendMessage(plugin.getConfig().getString("messages.actions.player-getmoney").replace("%amount%", args[1]));

//                Фиксируем операцию в транзакциях
                PlayersTXDB.WriteTransaction(
                        Bukkit.getPlayer(args[0]),
                        plugin.getConfig().getString("transactions.addmoney").replace("%amount%", args[1]),
                        plugin.getConfig().getString("transactions.status.success")
                );

                return true;
            }
//            ===========/addmoney AMOUNT===========
            else if(args.length == 1 && PlayersDB.AddMoney((Player)playerSender, Double.valueOf(args[0]))) {
                Bukkit.getPlayer(playerSender.getName()).sendMessage(plugin.getConfig().getString("messages.actions.player-getmoney").replace("%amount%", args[0]));

//                Фиксируем операцию в транзакциях
                PlayersTXDB.WriteTransaction(
                        Bukkit.getPlayer(playerSender.getName()),
                        plugin.getConfig().getString("transactions.addmoney").replace("%amount%", args[0]),
                        plugin.getConfig().getString("transactions.status.success")
                );

                return true;
            }
            else if(args.length == 2 && !PlayersDB.AddMoney(Bukkit.getPlayer(args[0]), Double.valueOf(args[1]))) {
                Bukkit.getPlayer(playerSender.getName()).sendMessage(plugin.getConfig().getString("messages.errors.bank-error-notify"));

//                Фиксируем операцию в транзакциях
                PlayersTXDB.WriteTransaction(
                        Bukkit.getPlayer(args[0]),
                        plugin.getConfig().getString("transactions.addmoney").replace("%amount%", args[1]),
                        plugin.getConfig().getString("transactions.status.not-success")
                );

                return true;
            }
            else if(args.length == 1 && PlayersDB.AddMoney((Player)playerSender, Double.valueOf(args[0]))) {
                Bukkit.getPlayer(playerSender.getName()).sendMessage(plugin.getConfig().getString("messages.errors.bank-error-notify"));

//                Фиксируем операцию в транзакциях
                PlayersTXDB.WriteTransaction(
                        Bukkit.getPlayer(playerSender.getName()),
                        plugin.getConfig().getString("transactions.addmoney").replace("%amount%", args[0]),
                        plugin.getConfig().getString("transactions.status.not-success")
                );

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
