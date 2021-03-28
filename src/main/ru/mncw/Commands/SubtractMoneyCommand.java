package main.ru.mncw.Commands;

import main.ru.mncw.DataBases.PlayersWalletDB;
import main.ru.mncw.MultiCurrency;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubtractMoneyCommand implements CommandExecutor {
    MultiCurrency plugin;
    PlayersWalletDB PlayersWDB;

    public SubtractMoneyCommand(MultiCurrency plugin) {
        this.plugin = plugin;

        try {
            PlayersWDB = new PlayersWalletDB(plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender playerSender, Command comm, String label, String args[]) {
        if(playerSender instanceof Player) {
            if(args.length == 2 && PlayersWDB.SubtractMoney(Bukkit.getPlayer(args[0]), Double.valueOf(args[1]))) {
                Bukkit.getPlayer(args[0]).sendMessage(plugin.getConfig().getString("messages.actions.player-raisemoney").replace("%amount%", args[1]));
                Bukkit.getPlayer(playerSender.getName()).sendMessage(plugin.getConfig().getString("messages.actions.player-subtractmoney").replace("%amount%", args[1]).replace("%player%", args[0]));

                return true;
            } else if(args.length == 1 && PlayersWDB.SubtractMoney((Player) playerSender, Double.valueOf(args[0]))) {
                Bukkit.getPlayer(playerSender.getName()).sendMessage(plugin.getConfig().getString("messages.actions.player-raisemoney").replace("%amount%", args[0]));

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
