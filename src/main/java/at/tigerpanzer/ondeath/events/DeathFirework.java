package at.tigerpanzer.ondeath.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import at.tigerpanzer.ondeath.Main;
import at.tigerpanzer.ondeath.util.Storage;

public class DeathFirework implements Listener {

  private Main plugin;

  public DeathFirework(Main plugin) {
    this.plugin = plugin;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onDeathFirework(PlayerDeathEvent e) {
    Player p = e.getEntity().getPlayer();
    int fireworkamount;
    List<String> lore;
    List<String> lore2;
    int fireworkhight;
    boolean fireworkflicker;
    boolean fireworktrail;
    String fireworkftype;
    boolean fireworkinstantexplode;
    int fireworkpower;
    if (plugin.firstDeath() && Storage.getFirstDeath(p)) {
      if (!p.hasPermission("OnDeath.FirstDeath.Firework") || !p.hasPermission("OnDeath.*")) {
        return;
      }
      if (!plugin.getConfig().getBoolean("FirstDeath.Death.FireworkOn")) {
        return;
      }
      fireworkamount = plugin.getConfig().getInt("FirstDeath.Death.Firework.Amount");
      lore = plugin.getConfig().getStringList("FirstDeath.Death.Firework.Colors");
      lore2 = plugin.getConfig().getStringList("FirstDeath.Death.Firework.Fade");
      fireworkhight = plugin.getConfig().getInt("FirstDeath.Death.Firework.Firework-Height");
      fireworkflicker = plugin.getConfig().getBoolean("FirstDeath.Death.Firework.Flicker");
      fireworktrail = plugin.getConfig().getBoolean("FirstDeath.Death.Firework.Trail");
      fireworkftype = plugin.getConfig().getString("FirstDeath.Death.Firework.Type");
      fireworkinstantexplode = plugin.getConfig().getBoolean("FirstDeath.Death.Firework.InstantExplode");
      fireworkpower = plugin.getConfig().getInt("FirstDeath.Death.Firework.Power");
    } else {
      if (!p.hasPermission("OnDeath.Firework") || !p.hasPermission("OnDeath.*")) {
        return;
      }
      if (!plugin.getConfig().getBoolean("Death.FireworkOn")) {
        return;
      }
      fireworkamount = plugin.getConfig().getInt("Death.Firework.Amount");
      lore = plugin.getConfig().getStringList("Death.Firework.Colors");
      lore2 = plugin.getConfig().getStringList("Death.Firework.Fade");
      fireworkhight = plugin.getConfig().getInt("Death.Firework.Firework-Height");
      fireworkflicker = plugin.getConfig().getBoolean("Death.Firework.Flicker");
      fireworktrail = plugin.getConfig().getBoolean("Death.Firework.Trail");
      fireworkftype = plugin.getConfig().getString("Death.Firework.Type");
      fireworkinstantexplode = plugin.getConfig().getBoolean("Death.Firework.InstantExplode");
      fireworkpower = plugin.getConfig().getInt("Death.Firework.Power");
    }
    for (int i = 1; i < fireworkamount; i++) {
      List<Color> colors = new ArrayList<>();
      List<Color> fade = new ArrayList<>();
      for (String l : lore) {
        colors.add(getColor(l));
      }
      for (String l : lore2) {
        fade.add(getColor(l));
      }
      final Firework f = e.getEntity().getPlayer().getWorld().spawn(
          p.getLocation().add(0.5D, fireworkhight, 0.5D),
          Firework.class);
      FireworkMeta fm = f.getFireworkMeta();
      fm.addEffect(FireworkEffect.builder().flicker(fireworkflicker).trail(fireworktrail).with(FireworkEffect.Type.valueOf(fireworkftype)).withColor(colors).withFade(fade).build());
      if (!fireworkinstantexplode) {
        fm.setPower(fireworkpower);
      }
      f.setFireworkMeta(fm);
      if (fireworkinstantexplode) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, f::detonate, 1L);
      }
    }
  }

  private Color getColor(String color) {
    switch (color.toUpperCase()) {
      case "AQUA":
        return Color.AQUA;
      case "BLACK":
        return Color.BLACK;
      case "BLUE":
        return Color.BLUE;
      case "FUCHSIA":
        return Color.FUCHSIA;
      case "GRAY":
        return Color.GRAY;
      case "GREEN":
        return Color.GREEN;
      case "LIME":
        return Color.LIME;
      case "MAROON":
        return Color.MAROON;
      case "NAVY":
        return Color.NAVY;
      case "OLIVE":
        return Color.OLIVE;
      case "ORANGE":
        return Color.ORANGE;
      case "PURPLE":
        return Color.PURPLE;
      case "RED":
        return Color.RED;
      case "SILVER":
        return Color.SILVER;
      case "TEAL":
        return Color.TEAL;
      case "WHITE":
        return Color.WHITE;
      case "YELLOW":
        return Color.YELLOW;
      default:
        return Color.BLACK;
    }
  }
}