/*
 * OnDeath - Your Server Death Plugin
 *
 *      With this plugin, deaths are
 *      unique on your server
 *
 *
 *    Maintained by Tigerpanzer_02
 */

package at.tigerpanzer.ondeath.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.tigerpanzer.ondeath.Main;
import at.tigerpanzer.ondeath.handlers.LanguageManager;
import at.tigerpanzer.ondeath.util.Utils;


public class DeathCommand implements CommandExecutor {

  private Main plugin;

  public DeathCommand(Main plugin) {
    this.plugin = plugin;
    plugin.getCommand("ondeath").setExecutor(this);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(Utils.colorMessage("NoPlayer"));
      return true;
    }
    Player player = (Player) sender;
    if (!player.hasPermission("OnDeath.config")) {
      player.sendMessage(Utils.setPlaceholders(player, Utils.colorMessage("Permissionfail")));
      return true;
    }
    if (args.length == 0) {
      List<String> HelpText = LanguageManager.getLanguageList("Help.HelpText");
      for (String msg : HelpText) {
        player.sendMessage(Utils.setPlaceholders(player, msg));
      }
      return true;
    } else if (args.length == 2) {
      if (args[0].equalsIgnoreCase("locale")) {
        if (args[1].equalsIgnoreCase("de")) {
          plugin.getConfig().set("locale", "de");
          plugin.saveConfig();
          player.sendMessage(Utils.color(Utils.colorMessage("Prefix") + LanguageManager.getLanguageMessage("Help.LanguageSwitch")));
        } else if (args[1].equalsIgnoreCase("default")) {
          plugin.getConfig().set("locale", "default");
          plugin.saveConfig();
          player.sendMessage(Utils.color(Utils.colorMessage("Prefix") + LanguageManager.getLanguageMessage("Help.LanguageSwitch")));
        }
      }
      return true;

    } else if (args.length == 1) {
      if (args[0].equalsIgnoreCase("reloadconfig")) {
        plugin.reloadConfig();
        player.sendMessage(Utils.setPlaceholders(player, Utils.colorMessage("Prefix") + Utils.colorMessage("Help.OutConfigLoad")));
      }
      if (args[0].equalsIgnoreCase("setrespawn")) {
        plugin.getConfig().set("SpawnLocation.World", player.getLocation().getWorld().getName());
        plugin.getConfig().set("SpawnLocation.XCoord", player.getLocation().getX());
        plugin.getConfig().set("SpawnLocation.YCoord", player.getLocation().getY());
        plugin.getConfig().set("SpawnLocation.ZCoord", player.getLocation().getZ());
        plugin.getConfig().set("SpawnLocation.Yaw", player.getLocation().getYaw());
        plugin.getConfig().set("SpawnLocation.Pitch", player.getLocation().getPitch());
        plugin.getConfig().set("SpawnLocation.SpawnLocationEnable", true);
        player.sendMessage(Utils.colorMessage("SpawnLocation.SetSpawnMessageSetTo") + player.getLocation().getWorld().getName() + ", " + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ());
        player.sendMessage(Utils.colorMessage("SpawnLocation.SetSpawnMessageYaw") + player.getLocation().getYaw());
        player.sendMessage(Utils.colorMessage("SpawnLocation.SetSpawnMessagePitch") + player.getLocation().getPitch());
        plugin.saveConfig();
      }
      if (args[0].equalsIgnoreCase("setfirstrespawn")) {
        plugin.getConfig().set("FirstDeath.SpawnLocation.World", player.getLocation().getWorld().getName());
        plugin.getConfig().set("FirstDeath.SpawnLocation.XCoord", player.getLocation().getX());
        plugin.getConfig().set("FirstDeath.SpawnLocation.YCoord", player.getLocation().getY());
        plugin.getConfig().set("FirstDeath.SpawnLocation.ZCoord", player.getLocation().getZ());
        plugin.getConfig().set("FirstDeath.SpawnLocation.Yaw", player.getLocation().getYaw());
        plugin.getConfig().set("FirstDeath.SpawnLocation.Pitch", player.getLocation().getPitch());
        plugin.getConfig().set("FirstDeath.SpawnLocation.SpawnLocationEnable", true);
        player.sendMessage(Utils.colorMessage("SpawnLocation.SetSpawnMessageSetTo") + player.getLocation().getWorld().getName() + ", " + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ());
        player.sendMessage(Utils.colorMessage("SpawnLocation.SetSpawnMessageYaw") + player.getLocation().getYaw());
        player.sendMessage(Utils.colorMessage("SpawnLocation.SetSpawnMessagePitch") + player.getLocation().getPitch());
        plugin.saveConfig();
      }
      return true;

    } else {
      player.sendMessage(Utils.setPlaceholders(player, Utils.colorMessage("Prefix") + "Use /OnDeath"));
      return true;
    }
  }
}