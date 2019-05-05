package at.tigerpanzer.ondeath;

import me.clip.placeholderapi.PlaceholderAPIPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import at.tigerpanzer.ondeath.command.DeathCommand;
import at.tigerpanzer.ondeath.events.DeathExecuteCommand;
import at.tigerpanzer.ondeath.events.DeathFirework;
import at.tigerpanzer.ondeath.events.DeathRespawnListener;
import at.tigerpanzer.ondeath.handlers.LanguageManager;
import at.tigerpanzer.ondeath.handlers.LanguageMigrator;
import at.tigerpanzer.ondeath.util.MessageUtils;
import at.tigerpanzer.ondeath.util.MySQL;
import at.tigerpanzer.ondeath.util.UpdateChecker;
import at.tigerpanzer.ondeath.util.Utils;


public class Main extends JavaPlugin {

  private boolean needUpdateJoin;
  private boolean placeholderAPI;
  private boolean mySQLEnabled;
  private boolean firstDeathEnabled;
  private boolean usedbefore2;
  private String consolePrefix;
  public static MySQL mysql;

  @Override
  public void onEnable() {
    LanguageManager.init(this);
    saveDefaultConfig();
    LanguageMigrator.configUpdate();
    LanguageMigrator.languageFileUpdate();
    consolePrefix = Utils.color(LanguageManager.getLanguageMessage("Console.PrefixConsole"));
    needUpdateJoin = false;
    mySQLEnabled = false;
    firstDeathEnabled = false;
    Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &cWird &aGESTARTET &7| &cis &aSTARTING"));
    register();
    if (getConfig().getBoolean("MySQL.Enabled", false)) {
      connectMySQL();
      mySQLEnabled = true;
    }
    if (getConfig().getBoolean("Death.UpdateMessageOn", true)) {
      update();
    }
    if (getConfig().getBoolean("FirstDeath.Enabled", false)) {
      firstDeathEnabled = true;
    }
    Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &7=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="));
    Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &cPlugin version: &e" + getDescription().getVersion()));
    Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &cPlugin author: &e" + getDescription().getAuthors()));
    Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &cPlugin status: &aaktiviert &c| &aenabled"));
    if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
      Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " §a✔ §ePlaceholderAPI §7| §aVersion§7:§e " + PlaceholderAPIPlugin.getInstance().getDescription().getVersion()));
      placeholderAPI = true;
    } else {
      Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " §c✖ §4PlaceholderAPI"));
      placeholderAPI = false;
    }
    if (mySQLEnabled) {
      Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " §a✔ §eMySQL"));
    } else {
      Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " §c✖ §4MySQL"));
    }
    Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &7=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="));
  }

  @Override
  public void onDisable() {
    if (mySQLEnabled) {
      mysql.Disconnect();
    }
    Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &7=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="));
    Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &cPlugin version: &e" + getDescription().getVersion()));
    Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &cPlugin author: &e" + getDescription().getAuthors()));
    Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &cPlugin status: &4deaktiviert &c| &4disabled"));
    Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &7=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="));
  }

  private void update() {
    UpdateChecker.init(this, 63773).requestUpdateCheck().whenComplete((result, exception) -> {
      if (result.requiresUpdate()) {
        if (result.getNewestVersion().contains("b")) {
          if (getConfig().getBoolean("Update-Notifier.Notify-Beta-Versions", true)) {
            Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &7=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="));
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[OnDeath] Your software is ready for update! However it's a BETA VERSION. Proceed with caution.");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[OnDeath] Current version %old%, latest version %new%".replace("%old%", getDescription().getVersion()).replace("%new%",
                result.getNewestVersion()));
            Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &7=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="));
          }
          return;
        }
        needUpdateJoin = true;
        Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &7=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="));
        MessageUtils.updateIsHere();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Your OnDeath plugin is outdated! Download it to keep with latest changes and fixes.");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Disable this option in config.yml if you wish.");
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Current version: " + ChatColor.RED + getDescription().getVersion() + ChatColor.YELLOW + " Latest version: " + ChatColor.GREEN + result.getNewestVersion());
        Bukkit.getConsoleSender().sendMessage(Utils.color(consolePrefix + " &7=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="));
      }
    });
  }

  private void register() {
    new DeathCommand(this);
    new DeathExecuteCommand(this);
    new DeathFirework(this);
    new DeathRespawnListener(this);
  }

  private void connectMySQL() {
    Main.mysql = new MySQL(getConfig().getString("MySQL.Host"), getConfig().getString("MySQL.Database"), getConfig().getString("MySQL.Username"), getConfig().getString("MySQL.Password"), getConfig().getInt("MySQL.Port", 3306));
    if (getConfig().getBoolean("MySQL.AutoReconnect")) {
      Bukkit.getScheduler().runTaskTimer(this, () -> {
        if (mySQLEnabled) {
          mysql.Reconnect();
        }
      }, 20L * 2700, 20L * 2700);
    }
  }

  public String getConsolePrefix() {
    return consolePrefix;
  }

  public boolean needUpdateJoin() {
    return needUpdateJoin;
  }

  public boolean firstDeath() {
    return firstDeathEnabled;
  }

  public boolean mySQLEnabled() {
    return mySQLEnabled;
  }

  public void setNeedUpdateJoin(boolean needUpdateJoin) {
    this.needUpdateJoin = needUpdateJoin;
  }

  public boolean isPlaceholderAPIEnabled() {
    return placeholderAPI;
  }

  public static MySQL getMysql() {
    return mysql;
  }
}
