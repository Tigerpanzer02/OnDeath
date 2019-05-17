/*
 * OnDeath - Your Server Death Plugin
 *
 *      With this plugin, deaths are
 *      unique on your server
 *
 *
 *    Maintained by Tigerpanzer_02
 */

package at.tigerpanzer.ondeath.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import at.tigerpanzer.ondeath.Main;
import at.tigerpanzer.ondeath.util.Storage;
import at.tigerpanzer.ondeath.util.Utils;

public class DeathExecuteCommand implements Listener {

  private Main plugin;

  public DeathExecuteCommand(Main plugin) {
    this.plugin = plugin;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onDeathExecuteCommand(PlayerDeathEvent e) {
    Player p = e.getEntity().getPlayer();
    Player k = e.getEntity().getKiller();
    List<String> commands;
    if (plugin.firstDeath() && Storage.getFirstDeath(p)) {
      if (!p.hasPermission("OnDeath.FirstDeath.ExecuteCommand")) {
        return;
      }
      if (!plugin.getConfig().getBoolean("FirstDeath.ExecuteCommand.CommandOn")) {
        return;
      }
      commands = plugin.getConfig().getStringList("FirstDeath.ExecuteCommand.Commands");
    } else {
      if (!p.hasPermission("OnDeath.ExecuteCommand")) {
        return;
      }
      if (!plugin.getConfig().getBoolean("ExecuteCommand.CommandOn")) {
        return;
      }
      commands = plugin.getConfig().getStringList("ExecuteCommand.Commands");
    }
    Utils.debugmessage("[D] Trying to execute commands" + commands);
    for (String command : commands) {
      String[] parts = command.split(";");
      String sender = parts[0];
      String cmd = Utils.setPlaceholders(p, parts[1]);
      switch (sender) {
        case "console":
          Utils.debugmessage("[D] Executed console command" + cmd);
          Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        case "killer":
          k.performCommand(cmd);
          Utils.debugmessage("[D] Executed killer (" + k.getName() + ") command" + cmd);
        case "player":
          p.performCommand(cmd);
          Utils.debugmessage("[D] Executed player (" + p.getName() + ") command" + cmd);
        default:
          System.out.println("[D] Error ExecuteCommand 01 | You can only use killer, player or console as sender!");
      }
    }
  }

  @EventHandler
  public void onRespawnExecuteCommand(PlayerRespawnEvent e) {
    Player p = e.getPlayer();
    List<String> commands;
    if (plugin.firstDeath() && Storage.getFirstDeath(p)) {
      if (!p.hasPermission("OnDeath.FirstDeath.ExecuteCommand.Respawn")) {
        return;
      }
      if (!plugin.getConfig().getBoolean("FirstDeath.ExecuteCommand.Respawn.CommandOn")) {
        return;
      }
      commands = plugin.getConfig().getStringList("FirstDeath.ExecuteCommand.Respawn.Commands");
    } else {
      if (!p.hasPermission("OnDeath.ExecuteCommand.Respawn")) {
        return;
      }
      if (!plugin.getConfig().getBoolean("ExecuteCommand.Respawn.CommandOn")) {
        return;
      }
      commands = plugin.getConfig().getStringList("ExecuteCommand.Respawn.Commands");
    }
    Utils.debugmessage("[R] Trying to execute commands" + commands);
    for (String command : commands) {
      String[] parts = command.split(";");
      String sender = parts[0];
      String cmd = Utils.setPlaceholders(p, parts[1]);
      switch (sender) {
        case "console":
          Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
          Utils.debugmessage("[R] Executed console command" + cmd);
        case "player":
          p.performCommand(cmd);
          Utils.debugmessage("[R] Executed player (" + p.getName() + ") command" + cmd);
        default:
          System.out.println("[R] Error ExecuteCommand 02 | You can only use player or console as sender!");
      }
    }
  }
}