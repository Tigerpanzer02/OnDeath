

/*
 * OnDeath - Your Server Death Plugin
 *
 *      With this plugin, deaths are
 *      unique on your server
 *
 *
 *    Maintained by Tigerpanzer_02
 */

package at.tigerpanzer.ondeath.handlers;

import java.io.File;
import java.util.List;

import at.tigerpanzer.ondeath.Main;
import at.tigerpanzer.ondeath.util.Utils;


public class LanguageManager {

  private static Main plugin;

  public static void init(Main pl) {
    plugin = pl;
    if (!new File(plugin.getDataFolder() + File.separator + "language.yml").exists()) {
      plugin.saveResource("language.yml", false);
    }
    if (!new File(plugin.getDataFolder() + File.separator + "language_de.yml").exists()) {
      plugin.saveResource("language_de.yml", false);
    }
  }

  public static List<String> getLanguageList(String path) {
    if (plugin.getConfig().get("locale").equals("de")) {
      return Utils.getConfig(plugin, "language_de").getStringList(path);
    }
    return Utils.getConfig(plugin, "language").getStringList(path);
  }

  public static String getLanguageMessage(String message) {
    if (plugin.getConfig().get("locale").equals("de")) {
      return Utils.getConfig(plugin, "language_de").getString(message, "ERR_MESSAGE_NOT_FOUND");
    }
    return Utils.getConfig(plugin, "language").getString(message, "ERR_MESSAGE_NOT_FOUND");
  }
}
