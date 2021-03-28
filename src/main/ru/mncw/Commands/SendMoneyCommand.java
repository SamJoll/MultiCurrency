package main.ru.mncw.Commands;

import main.ru.mncw.DataBases.PlayersWalletDB;
import main.ru.mncw.MultiCurrency;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendMoneyCommand implements CommandExecutor {

    MultiCurrency plugin;
    PlayersWalletDB PlayersWDB;

    public SendMoneyCommand(MultiCurrency plugin) {
        this.plugin = plugin;

        try {
            PlayersWDB = new PlayersWalletDB(plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender playerSender, Command comm, String label, String[] args) {
        if(playerSender instanceof Player) {
            if(args.length == 2 && PlayersWDB.SendMoney((Player)playerSender, Bukkit.getPlayer(args[0]), Double.valueOf(args[1]))) {
                Bukkit.getPlayer(playerSender.getName()).sendMessage(plugin.getConfig().getString("messages.actions.player-sendmoney").replace("%player%", args[0]).replace("%amount%", args[1]));
                Bukkit.getPlayer(args[0]).sendMessage(plugin.getConfig().getString("messages.actions.player-getsendmoney").replace("%player%", playerSender.getName()).replace("%amount%", args[1]));

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
