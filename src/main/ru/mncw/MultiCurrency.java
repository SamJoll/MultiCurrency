package main.ru.mncw;

import main.ru.mncw.Commands.*;
import main.ru.mncw.Handlers.InventoryHandler;
import main.ru.mncw.Handlers.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

import tool.ColorCode;

public class MultiCurrency extends JavaPlugin {

//    Путь до папки плагина
    final String pluginFolderPath = "plugins/MultiCurrency";
//    Путь до конфиг файла
    final String pluginCustomConfigPath = pluginFolderPath + "/config.yml";

//    Меню с транзакциями
    public Inventory transactionsMenu;

//    Консоль майнкрафта
    Logger log = Logger.getLogger("Minecraft");

//    Создание папки плагина
    void CreatePluginFolder() {
        File pluginFolder = new File(pluginFolderPath);

        boolean isFolderCreated = pluginFolder.mkdir();

        if(isFolderCreated) log.info(ColorCode.ANSI_CYAN + "[MultiCurrency] Plugin's folder is created!" + ColorCode.ANSI_RESET);
        else log.info(ColorCode.ANSI_RED + "[MultiCurrency] Plugin's folder isn't created!" + ColorCode.ANSI_RESET);
    }
//    Создание внешнего конфиг файла
    void CreateCustomConfig() {
//        Файл внешнего конфиг файла
        File customConfigFile = new File(pluginCustomConfigPath);
//        Внешний конфиг файл
        FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        if(!customConfigFile.exists()) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }

//    Иницилизация меню с транзакциями
    void InitTransactionsMenu() {
        transactionsMenu = Bukkit.createInventory(null, 27, getConfig().getString("transactions-title"));
    }
    @Override
    public void onEnable() {
        CreatePluginFolder();
        CreateCustomConfig();

        InitTransactionsMenu();

        Bukkit.getPluginManager().registerEvents(new PlayerHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryHandler(this), this);

        getCommand("showbalance").setExecutor(new ShowBalanceCommand(this));
        getCommand("addmoney").setExecutor(new AddMoneyCommand(this));
        getCommand("subtractmoney").setExecutor(new SubtractMoneyCommand(this));
        getCommand("sendmoney").setExecutor(new SendMoneyCommand(this));
        getCommand("transactions").setExecutor(new ShowTransactionsCommand(this));

        log.info(ColorCode.ANSI_CYAN + "[MultiCurrency] Plugin is working!" + ColorCode.ANSI_RESET);
    }

    @Override
    public void onDisable() {
        log.info(ColorCode.ANSI_RED + "[MultiCurrency] Plugin is disabled..." + ColorCode.ANSI_RESET);
    }
}
