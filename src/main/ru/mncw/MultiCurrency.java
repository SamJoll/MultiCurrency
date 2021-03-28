package main.ru.mncw;

import main.ru.mncw.Commands.AddMoneyCommand;
import main.ru.mncw.Commands.SendMoneyCommand;
import main.ru.mncw.Commands.ShowBalanceCommand;
import main.ru.mncw.Commands.SubtractMoneyCommand;
import main.ru.mncw.Handlers.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

import tool.ColorCode;

public class MultiCurrency extends JavaPlugin {

//    Путь до папки плагина
    final String pluginFolderPath = "plugins/MultiCurrency";
//    Путь до конфиг файла
    final String pluginCustomConfigPath = pluginFolderPath + "/config.yml";

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

    @Override
    public void onEnable() {
        CreatePluginFolder();
        CreateCustomConfig();

        Bukkit.getPluginManager().registerEvents(new PlayerHandler(this), this);

        getCommand("showbalance").setExecutor(new ShowBalanceCommand(this));
        getCommand("addmoney").setExecutor(new AddMoneyCommand(this));
        getCommand("subtractmoney").setExecutor(new SubtractMoneyCommand(this));
        getCommand("sendmoney").setExecutor(new SendMoneyCommand(this));

        log.info(ColorCode.ANSI_CYAN + "[MultiCurrency] Plugin is working!" + ColorCode.ANSI_RESET);
    }

    @Override
    public void onDisable() {
        log.info(ColorCode.ANSI_RED + "[MultiCurrency] Plugin is disabled..." + ColorCode.ANSI_RESET);
    }
}
