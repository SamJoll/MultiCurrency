package main.ru.mncw.Commands;

import main.ru.mncw.DataBases.PlayersWalletDB;
import main.ru.mncw.MultiCurrency;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowBalanceCommand implements CommandExecutor {

    MultiCurrency plugin;
    PlayersWalletDB PlayerWDB;

    public ShowBalanceCommand(MultiCurrency plugin) {
        this.plugin = plugin;

        try {
            PlayerWDB = new PlayersWalletDB(plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender playerSender, Command comm, String label, String[] args) {
        if(playerSender instanceof Player) {
            if (PlayerWDB.ShowBalance((Player) playerSender) >= 0 && args.length <= 0) {
                Bukkit.getPlayer(
                        playerSender.getName()).sendMessage(
                                plugin.getConfig().getString("messages.actions.player-showbalance").replace("%balance%", String.valueOf(PlayerWDB.ShowBalance((Player)playerSender)
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
