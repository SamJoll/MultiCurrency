package main.ru.mncw.DataBases;

import main.ru.mncw.Commands.ShowBalanceCommand;
import main.ru.mncw.MultiCurrency;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;

public class PlayersWalletDB {
    MultiCurrency plugin;

    String dataBasePath = "jdbc:sqlite:plugins/MultiCurrency/PlayersWallet.db";

    public PlayersWalletDB(MultiCurrency plugin) throws Exception {
        this.plugin = plugin;

        Class.forName("org.sqlite.JDBC").newInstance();

        Connection dbConc = getConnection();
        Statement dbState = dbConc.createStatement();

        dbState.executeUpdate("CREATE TABLE IF NOT EXISTS playerBalanceInfo ('nickname' TEXT, 'balance' DOUBLE, UNIQUE ('nickname'))");

        dbState.close();
        dbConc.close();
    }

//    Функция подключения к базе данных
    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(dataBasePath);
    }

//    Добавление нового игрока
    public boolean AddNewPlayer(Player player) {
        try {
            String playerName = player.getName();

            Connection dbConc = getConnection();
            Statement dbState = dbConc.createStatement();

            dbState.executeUpdate("INSERT OR IGNORE INTO playerBalanceInfo VALUES('"+ playerName +"', 0)");

            dbState.close();
            dbConc.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }
//    Функция показа баланса игрока
    public double ShowBalance(Player player) {
        try {
            String playerName = player.getName();

            Connection dbConc = getConnection();
            Statement dbState = dbConc.createStatement();

            ResultSet recordBalance = dbState.executeQuery("SELECT balance FROM playerBalanceInfo WHERE nickname = '"+ playerName +"'");
            double playerBalance = recordBalance.getDouble(1);

            dbState.close();
            dbConc.close();

            return playerBalance;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
//    Добавление денег на счет
    public boolean AddMoney(Player targetPlayer, double amount) {
        try {
            String playerName = targetPlayer.getName();

            Connection dbConc = getConnection();
            Statement dbState = dbConc.createStatement();

            ResultSet playerBalanceRecord = dbState.executeQuery("SELECT balance FROM playerBalanceInfo WHERE nickname = '"+ playerName +"'");
            double currentPlayerBalance = playerBalanceRecord.getDouble(1);

            if(amount > 0) {
                double newPlayerBalance = currentPlayerBalance + amount;

                dbState.executeUpdate("UPDATE playerBalanceInfo SET balance = " + newPlayerBalance + " WHERE nickname = '"+ playerName +"'");

                dbState.close();
                dbConc.close();

                return true;
            } else {

                dbState.close();
                dbConc.close();

                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
//    Вычетание денег со счета
    public boolean SubtractMoney(Player targetPlayer, double amount) {
        try {
            String playerName = targetPlayer.getName();

            Connection dbConc = getConnection();
            Statement dbState = dbConc.createStatement();

            ResultSet playerBalanceRecord = dbState.executeQuery("SELECT balance FROM playerBalanceInfo WHERE nickname = '"+ playerName +"'");
            double currentPlayerBalance = playerBalanceRecord.getDouble(1);

            if(amount > 0 && currentPlayerBalance >= amount) {
                double newPlayerBalance = currentPlayerBalance - amount;

                dbState.executeUpdate("UPDATE playerBalanceInfo SET balance = "+ newPlayerBalance +" WHERE nickname = '"+ playerName +"'");

                dbState.close();
                dbConc.close();

                return true;
            } else {
                dbState.close();
                dbConc.close();

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }
//    Перечисляет деньги на счет другого игрока
    public boolean SendMoney(Player player, Player targetPlayer, double amount) {
        try {
            if(amount > 0 && ShowBalance(player) >= amount) {

                SubtractMoney(player, amount);
                AddMoney(targetPlayer, amount);

                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }
}
