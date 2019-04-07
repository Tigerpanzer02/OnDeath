package at.tigerpanzer.ondeath.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import at.tigerpanzer.ondeath.Main;
import at.tigerpanzer.ondeath.handlers.LanguageManager;
import at.tigerpanzer.ondeath.util.Storage;
import at.tigerpanzer.ondeath.util.Utils;

public class DeathRespawnListener implements Listener {

  private Main plugin;

  public DeathRespawnListener(Main plugin) {
    this.plugin = plugin;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }


  @EventHandler
  public void OnJoin(PlayerJoinEvent e) {
    Player p = e.getPlayer();
    Storage.DataOnJoin(p);
    if (plugin.needUpdateJoin()) {
      if ((p.hasPermission("OnDeath.UpdateMessage")) || (p.hasPermission("OnDeath.*"))) {
        if (plugin.getConfig().getBoolean("Death.UpdateMessageOn")) {
          List<String> UpdateMessageText = LanguageManager.getLanguageList("Death.UpdateMessageText");
          for (String msg : UpdateMessageText) {
            p.sendMessage(Utils.setPlaceholders(p, msg));
          }
        }
      }
    }
  }

  @EventHandler
  public void OnQuit(PlayerQuitEvent e) {
    if (plugin.mySQLEnabled()) {
      Storage.DataOnQuit(e.getPlayer());
    }
  }

  @EventHandler
  public void OnRespawn(PlayerRespawnEvent e) {
    Player p = e.getPlayer();
    boolean chatclearon;
    boolean spawnlocationenable;
    String spawnlocationworld;
    double spawnlocationx;
    double spawnlocationy;
    double spawnlocationz;
    int spawnlocationyaw;
    int spawnlocationpitch;
    boolean titleOnDeath;
    String title1;
    String subtitle1;
    String subtitle2;
    boolean actionbarOnDeath;
    String actionbar1;
    String actionbar2;
    boolean DeathMessageTextOn;
    List<String> DeathMessageText;
    if (plugin.firstDeath() && Storage.getFirstDeath(p)) {
      chatclearon = plugin.getConfig().getBoolean("FirstDeath.Death.ChatClearOn");
      spawnlocationenable = plugin.getConfig().getBoolean("FirstDeath.SpawnLocation.SpawnLocationEnable");
      spawnlocationworld = plugin.getConfig().getString("FirstDeath.SpawnLocation.World");
      spawnlocationx = plugin.getConfig().getDouble("FirstDeath.SpawnLocation.XCoord");
      spawnlocationy = plugin.getConfig().getDouble("FirstDeath.SpawnLocation.YCoord");
      spawnlocationz = plugin.getConfig().getDouble("FirstDeath.SpawnLocation.ZCoord");
      spawnlocationyaw = plugin.getConfig().getInt("FirstDeath.SpawnLocation.Yaw");
      spawnlocationpitch = plugin.getConfig().getInt("FirstDeath.SpawnLocation.Pitch");
      if ((p.hasPermission("FirstDeath.OnDeath.Heal"))) {
        if (plugin.getConfig().getBoolean("FirstDeath.Heal.HealOnWithPermission")) {
          p.setHealth(plugin.getConfig().getInt("FirstDeath.Heal.HealthWithPermission"));
          p.setFoodLevel(plugin.getConfig().getInt("FirstDeath.Heal.FoodLevelWithPermission"));
          if (plugin.getConfig().getBoolean("FirstDeath.Heal.ClearPotionEffectsWithPermission")) {
            p.getActivePotionEffects().clear();
          }
        }
      } else if (plugin.getConfig().getString("FirstDeath.Heal.HealOn").contains("true")) {
        p.setHealth(plugin.getConfig().getInt("FirstDeath.Heal.Health"));
        p.setFoodLevel(plugin.getConfig().getInt("FirstDeath.Heal.FoodLevel"));
        if (plugin.getConfig().getBoolean("FirstDeath.Heal.ClearPotionEffects")) {
          p.getActivePotionEffects().clear();
        }
      }
      titleOnDeath = plugin.getConfig().getBoolean("FirstDeath.Title.TitleOnDeath");
      title1 = Utils.colorMessage("FirstDeath.Title.Title1");
      subtitle1 = Utils.colorMessage("FirstDeath.Title.SubTitle1");
      subtitle2 = Utils.colorMessage("FirstDeath.Title.SubTitle2");
      actionbarOnDeath = plugin.getConfig().getBoolean("FirstDeath.Actionbar.ActionbarOnDeath");
      actionbar1 = Utils.colorMessage("FirstDeath.Actionbar.Actionbar1");
      actionbar2 = Utils.colorMessage("FirstDeath.Actionbar.Actionbar2");
      DeathMessageTextOn = plugin.getConfig().getBoolean("FirstDeath.WelcomeMessage.DeathMessageTextOn");
      DeathMessageText = LanguageManager.getLanguageList("FirstDeath.WelcomeMessage.DeathMessageText");
    } else {
      chatclearon = plugin.getConfig().getBoolean("Death.ChatClearOn");
      spawnlocationenable = plugin.getConfig().getBoolean("SpawnLocation.SpawnLocationEnable");
      spawnlocationworld = plugin.getConfig().getString("SpawnLocation.World");
      spawnlocationx = plugin.getConfig().getDouble("SpawnLocation.XCoord");
      spawnlocationy = plugin.getConfig().getDouble("SpawnLocation.YCoord");
      spawnlocationz = plugin.getConfig().getDouble("SpawnLocation.ZCoord");
      spawnlocationyaw = plugin.getConfig().getInt("SpawnLocation.Yaw");
      spawnlocationpitch = plugin.getConfig().getInt("SpawnLocation.Pitch");
      if ((p.hasPermission("OnDeath.Heal"))) {
        if (plugin.getConfig().getBoolean("Heal.HealOnWithPermission")) {
          p.setHealth(plugin.getConfig().getInt("Heal.HealthWithPermission"));
          p.setFoodLevel(plugin.getConfig().getInt("Heal.FoodLevelWithPermission"));
          if (plugin.getConfig().getBoolean("Heal.ClearPotionEffectsWithPermission")) {
            p.getActivePotionEffects().clear();
          }
        }
      } else if (plugin.getConfig().getString("Heal.HealOn").contains("true")) {
        p.setHealth(plugin.getConfig().getInt("Heal.Health"));
        p.setFoodLevel(plugin.getConfig().getInt("Heal.FoodLevel"));
        if (plugin.getConfig().getBoolean("Heal.ClearPotionEffects")) {
          p.getActivePotionEffects().clear();
        }
      }
      titleOnDeath = plugin.getConfig().getBoolean("Title.TitleOnDeath");
      title1 = Utils.colorMessage("Title.Title1");
      subtitle1 = Utils.colorMessage("Title.SubTitle1");
      subtitle2 = Utils.colorMessage("Title.SubTitle2");
      actionbarOnDeath = plugin.getConfig().getBoolean("Actionbar.ActionbarOnDeath");
      actionbar1 = Utils.colorMessage("Actionbar.Actionbar1");
      actionbar2 = Utils.colorMessage("Actionbar.Actionbar2");
      DeathMessageTextOn = plugin.getConfig().getBoolean("WelcomeMessage.DeathMessageTextOn");
      DeathMessageText = LanguageManager.getLanguageList("WelcomeMessage.DeathMessageText");
    }
    if (chatclearon) {
      for (int i = 0; i < 200; i++) {
        p.sendMessage(" ");
      }
    }
    if (spawnlocationenable) {
      final Location SpawnLocation = new Location(Bukkit.getServer().getWorld(spawnlocationworld), spawnlocationx, spawnlocationy, spawnlocationz, (float) spawnlocationyaw, (float) spawnlocationpitch);
      p.teleport(SpawnLocation);
    }
    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      if (titleOnDeath) {
        Utils.sendTitle(p, Utils.setPlaceholders(p, title1), 25, 90, 0);
        Utils.sendSubTitle(p, Utils.setPlaceholders(p, subtitle1), 25, 90, 0);

        if (actionbarOnDeath) {
          Utils.sendActionBar(p, Utils.setPlaceholders(p, actionbar1));
        }
        if (DeathMessageTextOn) {
          for (String msg : DeathMessageText) {
            p.sendMessage(Utils.setPlaceholders(e.getPlayer(), msg));
          }
        }
      }
    }, 2L);

    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      if (titleOnDeath) {
        Utils.sendSubTitle(p, Utils.setPlaceholders(p, subtitle2), 0, 90, 0);
      }
      if (actionbarOnDeath) {
        Utils.sendActionBar(p, Utils.setPlaceholders(p, actionbar2));
      }
    }, 65L);
    if (plugin.needUpdateJoin()) {
      if ((p.hasPermission("OnDeath.UpdateMessage"))) {
        if (plugin.getConfig().getBoolean("Death.UpdateMessageOn")) {
          List<String> UpdateMessageText = LanguageManager.getLanguageList("Death.UpdateMessageText");
          for (String msg : UpdateMessageText) {
            p.sendMessage(Utils.setPlaceholders(p, msg));
          }
        }
      }
    }
  }

  @EventHandler
  public void onQuit(PlayerDeathEvent e) {
    Player p = e.getEntity().getPlayer();
    boolean Deathsoundon;
    String Deathsound;
    boolean Deathmessageon;
    String Deathmessage;
    if (plugin.firstDeath() && Storage.getFirstDeath(p)) {
      Deathsoundon = plugin.getConfig().getBoolean("FirstDeath.Death.DeathSoundOn");
      Deathsound = plugin.getConfig().getString("FirstDeath.Death.DeathSound");
      Deathmessageon = plugin.getConfig().getBoolean("FirstDeath.Death.DeathMessageOn");
      Deathmessage = Utils.colorMessage("FirstDeath.Death.DeathMessage");
      Storage.setFirstDeath(p, false);
    } else {
      Deathsoundon = plugin.getConfig().getBoolean("Death.DeathSoundOn");
      Deathsound = plugin.getConfig().getString("Death.DeathSound");
      Deathmessageon = plugin.getConfig().getBoolean("Death.DeathMessageOn");
      Deathmessage = Utils.colorMessage("Death.DeathMessage");
    }
    if (Deathsoundon) {
      p.playSound(p.getLocation(), Sound.valueOf(Deathsound), 3, 1);
    }
    if (Deathmessageon) {
      e.setDeathMessage(Utils.setPlaceholders(p, Deathmessage));
    } else {
      e.setDeathMessage("");
    }
  }
}
