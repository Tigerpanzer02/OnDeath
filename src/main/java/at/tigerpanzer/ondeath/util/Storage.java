/*
 * OnDeath - Your Server Death Plugin
 *
 *      With this plugin, deaths are
 *      unique on your server
 *
 *
 *    Maintained by Tigerpanzer_02
 */

package at.tigerpanzer.ondeath.util;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import at.tigerpanzer.ondeath.Main;

public class Storage {

  private static Main plugin = JavaPlugin.getPlugin(Main.class);

  private static String path = "plugins//OnDeath//Data//%s.yml";

  private static void SetDataToFile(Player playerName, boolean FirstDeath, String Date) {
    String UUID = playerName.getUniqueId().toString();
    File file = new File(String.format(path, UUID));
    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    //PlayerData
    cfg.set("PlayerName", playerName);
    cfg.set("UUID", UUID);
    cfg.set("FirstDeath", FirstDeath);
    cfg.set("Date", Date);
    try {
      cfg.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public String getDataFile(String name, String data) {
    File file = new File(String.format(path, name));
    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    //PlayerData
    if (data.equalsIgnoreCase("PlayerName")) {
      return cfg.getString("PlayerName");
    }
    if (data.equalsIgnoreCase("UUID")) {
      return cfg.getString("UUID");
    }
    if (data.equalsIgnoreCase("Date")) {
      return cfg.getString("Date");
    }
    return "null";
  }

  public static boolean getFirstDeath(Player p) {
    File file = new File(String.format(path, p.getUniqueId().toString()));
    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    return cfg.getBoolean("FirstDeath");
  }

  public static void setFirstDeath(Player p, boolean data) {
    File file = new File(String.format(path, p.getUniqueId().toString()));
    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    cfg.set("FirstDeath", data);
    try {
      cfg.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public static void SetDataFile(String uuid, String data, String DataName) {
    File file = new File(String.format(path, uuid));
    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    //PlayerData
    if (DataName.equalsIgnoreCase("PlayerName")) {
      cfg.set("PlayerName", data);
    }
    if (DataName.equalsIgnoreCase("UUID")) {
      cfg.set("UUID", data);
    }
    if (DataName.equalsIgnoreCase("FirstDeath")) {
      cfg.set("FirstDeath", data);
    }
    if (DataName.equalsIgnoreCase("Date")) {
      cfg.set("Date", data);
    }
    try {
      cfg.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void DataOnJoin(Player p) {
    String name = p.getUniqueId().toString();
    File file = new File(String.format(path, name));
    Date date = new Date(System.currentTimeMillis());
    //GetPlayerData
    if (plugin.mySQLEnabled()) {
      try {
        ResultSet rs = Main.mysql.getResultSet("SELECT * FROM FirstJoin WHERE UUID='" + p.getUniqueId() + "'");
        if (!rs.next()) {
          Main.mysql.ExecuteCommand("INSERT INTO FirstDeath (PlayerName, UUID, FirstDeath, Date) VALUES ('" + p.getName() + "','" + p.getUniqueId() + "','true','" + getDate() + "')");
        } else {
          Main.mysql.ExecuteCommand("UPDATE FirstDeath SET Spielername='" + p.getName() + "' WHERE UUID='" + p.getUniqueId() + "'");
          boolean FirstJoin = rs.getBoolean("FirstDeath");
          String Date = rs.getString("Date");
          SetDataToFile(p, FirstJoin, Date);
          return;
        }
      } catch (Exception ex2) {
        ex2.printStackTrace();
      }
    } else {
      if (file.exists()) {
        return;
      } else {
        String Date = getDate();
        SetDataToFile(p, true, Date);
      }
    }
  }

  public static void DataOnQuit(Player p) {
    if (plugin.mySQLEnabled()) {
      String name = p.getUniqueId().toString();
      File file = new File(String.format(path, name));
      YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
      //SetPlayerData
      try {
        boolean FirstJoin = cfg.getBoolean("FirstDeath");
        String Date = cfg.getString("Date");

        Main.mysql.ExecuteCommand("UPDATE FirstJoin SET FirstDeath='" + FirstJoin + "', Date='" + Date + "' WHERE UUID='" + name + "'");
      } catch (Exception ex2) {
        ex2.printStackTrace();
      }
    }
  }

  private static String getDate() {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    return simpleDateFormat.format(cal.getTime());
  }
}
