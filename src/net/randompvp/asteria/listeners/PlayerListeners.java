package net.randompvp.asteria.listeners;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.libs.org.codehaus.plexus.util.FileUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.randompvp.asteria.Asteria;
import net.randompvp.asteria.menu.Menu;
import net.randompvp.asteria.utils.PowerUtils;

public class PlayerListeners implements Listener {
	Random r = new Random();
	String[] allowedcmds = { "/w", "/msg", "/tell", "/help", "/r", "/shrug", "/claimexplosions", "/abandonallclaims", "/abandonclaim", "/trust", "/claimlist", "/extendclaim", "/untrust", "/trustlist" };
	String[] messagecmds = { "/w", "/msg", "/tell" };

	Plugin plugin;

	public PlayerListeners(Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getItem() != null) {
			Material type = e.getItem().getType();
			String displayName = e.getItem().getItemMeta().getDisplayName();
			if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if (p.getUniqueId().equals(Asteria.jose) && p.isSneaking() && Asteria.isWrathSeaActive && !Asteria.wrathSeaStrikeCooldown) {
					for (int i = 0; i < 500; i++) {
						Location l = p.getEyeLocation();
						l.add(l.getDirection().multiply((Double.valueOf(i) / 10) + 1));
						if (!l.getBlock().getType().isAir()) {
							l.getWorld().strikeLightning(l);
							for (LivingEntity le : l.getWorld().getLivingEntities()) {
								if (le.getLocation().distance(l) < 1.2 && !le.isDead()) {
									le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
									le.damage(12, p);
								}
							}
							Asteria.wrathSeaStrikeCooldown = true;
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Asteria.wrathSeaStrikeCooldown = false, 100);
							break;
						}
					}
				}
				if (p.getUniqueId().equals(Asteria.saby) && p.isSneaking() && Asteria.isSorcererActive && !Asteria.sorcererRayCooldown) {
					HashSet<Integer> ids = new HashSet<Integer>();
					Location l = p.getLocation();
					DustOptions pa = new Particle.DustOptions(Color.fromRGB(230, 152, 7), 1);
					for (int i = 0; i < 600; i++) {
						final Double ii = (double) i / 1.75;
						ids.add(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Location ll = l.clone();
							ll.add(ll.getDirection().multiply(ii));
							ll.getWorld().spawnParticle(Particle.REDSTONE, ll, 11, 0.3, 0.3, 0.3, 1, pa, true);
							boolean isThereEntity = false;
							for (LivingEntity le : ll.getWorld().getLivingEntities()) {
								if (!le.getUniqueId().equals(p.getUniqueId()) && (ll.distance(le.getLocation()) < 1.85 || ll.distance(le.getEyeLocation()) < 1.85)) {
									isThereEntity = true;
									le.damage(12, p);
									le.setVelocity(ll.getDirection().multiply(1.3).setY(0.8));
									le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
									le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
								}
							}
							if (isThereEntity) {
								for (int toCancel : ids)
									Bukkit.getScheduler().cancelTask(toCancel);
								ll.getWorld().playSound(ll, Sound.BLOCK_STONE_BREAK, 1, 1);
							}
						}, Math.round(ii)));
					}
					Asteria.sorcererRayCooldown = true;
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Asteria.sorcererRayCooldown = false, 10);
				}
				if (type.equals(Material.WITHER_ROSE) && displayName.equals("§6Wither Turret / Dash")) {
					if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else if (!Asteria.hasAdvancement(p, "nether/get_wither_skull"))
						p.sendMessage(ChatColor.RED + "Sorry, but you need Spooky Scary Skeletons to use this power");
					else {
						p.setVelocity(p.getLocation().getDirection().multiply(5));
						Asteria.isTakeDamageWitherDash = true;
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Asteria.isTakeDamageWitherDash = false;
							p.damage(2);
						}, 20);
						HashSet<UUID> u = new HashSet<UUID>();
						int b = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
							for (LivingEntity le : p.getWorld().getLivingEntities()) {
								if (le.getLocation().distance(p.getLocation()) < 2.4 && !le.getUniqueId().equals(p.getUniqueId()) && !u.contains(le.getUniqueId())) {
									le.damage(7, p);
									u.add(le.getUniqueId());
								}
							}
						}, 0, 1);
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.getScheduler().cancelTask(b), 30);
					}
				} else if (type.equals(Material.RABBIT_FOOT) && displayName.equals("§6Alert / Double Jump")) {
					if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Asteria.isTakeDamageDoubleJump = true;
						double jump = 2D;
						double damage = 6D;
						if (Asteria.endAdvancements.contains(p.getUniqueId()))
							jump = 6D;
						else if (Asteria.hasAdvancement(p, "nether/explore_nether")) {
							jump = 5D;
							if (!Asteria.endAdvancements.contains(p.getUniqueId()))
								damage = 8D;
						} else if (Asteria.hasAdvancement(p, "story/shiny_gear"))
							jump = 4D;
						else if (Asteria.hasAdvancement(p, "story/enter_the_nether"))
							jump = 3D;
						final double damagee = damage;
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							p.damage(damagee);
							Asteria.isTakeDamageDoubleJump = false;
						}, 20);
						Vector v = p.getLocation().getDirection().multiply(jump).setY(jump / 2);
						p.setVelocity(p.getLocation().getDirection().setY(jump));
						int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
							p.setVelocity(v);
							p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation(), 50);
						}, 2, 2);
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.getScheduler().cancelTask(task), 18);
					}
				} else if (type.equals(Material.LIME_DYE) && displayName.equals("§bStomp")) {
					e.setCancelled(true);
					if (PowerUtils.isOnCooldown(Asteria.cooldownStomp, p))
						PowerUtils.sendCDMsg(Asteria.cooldownStomp, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300, 1));
						p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 0));
						p.setVelocity(p.getLocation().getDirection().multiply(0).setY(1.8));
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							if (p.isOnline())
								Asteria.stomp.add(p.getUniqueId());
						}, 8);
						PowerUtils.addCooldown(Asteria.cooldownStomp, p, 60);
					}
				} else if (type.equals(Material.GRAY_GLAZED_TERRACOTTA) && displayName.equals("§bDimensional Shift")) {
					e.setCancelled(true);
					if (Asteria.invis.containsKey(p.getUniqueId()))
						p.sendMessage("§cYou are already invisible!");
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else if (p.getHealth() < 4)
						p.sendMessage(ChatColor.RED + "Your health is too low to use §bDimensional Shift" + ChatColor.RED + "!");
					else {
						p.damage(4);
						p.sendMessage("§bYou are now invisible!");
						p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 3600, 0));
						p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3600, 0));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3600, 1));
						for (Player a : Bukkit.getOnlinePlayers())
							a.hidePlayer(plugin, p);
						Asteria.invis.put(p.getUniqueId(), Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							for (Player a : Bukkit.getOnlinePlayers())
								a.showPlayer(Asteria.getPlugin(Asteria.class), p);
							p.sendMessage("§cYour invisibility has run out!");
							Asteria.invis.remove(p.getUniqueId());
							p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
							p.removePotionEffect(PotionEffectType.INVISIBILITY);
						}, 1000));
					}
				} else if (type.equals(Material.GOLD_NUGGET) && displayName.equals("§bParalysis")) {
					if (PowerUtils.isOnCooldown(Asteria.cooldownParalysis, p))
						PowerUtils.sendCDMsg(Asteria.cooldownParalysis, p);
					else {
						p.sendMessage("§aYou have paralyzed surrounding people!");
						int num = Asteria.frozenNum;
						Asteria.frozenNum++;
						Asteria.frozen.put(num, new HashSet<UUID>());
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (a.getWorld().equals(p.getWorld()) && a.getLocation().distance(p.getLocation()) < 15 && !a.equals(p)) {
								Asteria.frozen.get(num).add(a.getUniqueId());
								a.sendMessage("§6You have been paralyzed by " + p.getName());
								a.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 3));
							}
						}
						Location l = p.getLocation();
						int lx = l.getBlockX();
						int ly = l.getBlockY();
						int lz = l.getBlockZ();
						for (int x = lx - 16; x < lx + 16; x++) {
							for (int y = ly - 16; y < ly + 16; y++) {
								for (int z = lz - 16; z < lz + 16; z++) {
									Block b = l.getWorld().getBlockAt(x, y, z);
									if (b.getLocation().distance(p.getLocation()) < 15) {
										l.getWorld().spawnParticle(Particle.LANDING_HONEY, b.getLocation(), 1);
									}
								}
							}
						}
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Asteria.frozen.remove(num), 400);
						PowerUtils.addCooldown(Asteria.cooldownParalysis, p, 960);
					}
				} else if (type.equals(Material.GLOWSTONE_DUST) && displayName.equals("§bFire Breath")) {
					if (PowerUtils.isOnCooldown(Asteria.cooldownFirebreath, p)) {
						PowerUtils.sendCDMsg(Asteria.cooldownFirebreath, p);
					} else {
						int t = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
							for (int i = -3; i < 4; i++) {
								for (int o = 0; o < 7; o++) {
									Location l = p.getEyeLocation();
									l.setYaw(l.getYaw() + (i * 10));
									l.add(l.getDirection().multiply((o * 1.2) + 1.5));
									p.getWorld().spawnParticle(Particle.FLAME, l, 5, 0, 0, 0, 0);
									p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, l, 0, 0, 0, 0, 0);
									for (LivingEntity le : p.getWorld().getLivingEntities()) {
										if (le.getEyeLocation().distance(l) <= 1) {
											le.damage(3, p);
											le.setFireTicks(50);
										}
									}
								}
							}
						}, 0, 3);
						Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getScheduler().cancelTask(t), 120);
						PowerUtils.addCooldown(Asteria.cooldownFirebreath, p, 20);
					}
				} else if (type.equals(Material.WITHER_SKELETON_SKULL) && displayName.equals("§6Soul's of the Damned")) {
					if (PowerUtils.isInDimension(p.getWorld().getName()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now!");
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						boolean hasFire = Asteria.hasAdvancement(p, "nether/obtain_blaze_rod");
						boolean hasBrewery = Asteria.hasAdvancement(p, "nether/brew_potion");
						boolean hasHeights = Asteria.hasAdvancement(p, "nether/summon_wither");
						boolean usedQueue = false;
						for (int i = 0; i < 51; i++) {
							Location l = p.getEyeLocation();
							l.add(l.getDirection().multiply(i + 1));
							if (!l.getBlock().getType().isAir() || i == 50) {
								Location ll = p.getEyeLocation();
								ll.add(ll.getDirection().multiply(i));
								for (int ii = 0; ii < 4 * Asteria.hanyPowerMultiplier; ii++) {
									WitherSkeleton ws = (WitherSkeleton) p.getWorld().spawnEntity(ll, EntityType.WITHER_SKELETON);
									ItemStack helmet;
									ItemStack chestplate;
									ItemStack leggings;
									ItemStack boots;
									ItemStack sword;
									if (hasHeights) {
										helmet = new ItemStack(Material.NETHERITE_HELMET);
										ItemMeta helmetEnchants = helmet.getItemMeta();
										helmetEnchants.addEnchant(Enchantment.WATER_WORKER, 1, false);
										helmetEnchants.addEnchant(Enchantment.MENDING, 1, false);
										helmetEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false);
										helmetEnchants.addEnchant(Enchantment.OXYGEN, 3, false);
										helmetEnchants.addEnchant(Enchantment.DURABILITY, 3, false);
										helmetEnchants.setDisplayName("§6Unholy Regalia");
										helmet.setItemMeta(helmetEnchants);
										chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
										ItemMeta chestplateEnchants = chestplate.getItemMeta();
										chestplateEnchants.addEnchant(Enchantment.MENDING, 1, false);
										chestplateEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false);
										chestplateEnchants.addEnchant(Enchantment.DURABILITY, 3, false);
										chestplateEnchants.setDisplayName("§6Unholy Regalia");
										chestplate.setItemMeta(chestplateEnchants);
										leggings = new ItemStack(Material.NETHERITE_LEGGINGS);
										ItemMeta leggingsEnchants = leggings.getItemMeta();
										leggingsEnchants.addEnchant(Enchantment.MENDING, 1, false);
										leggingsEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false);
										leggingsEnchants.addEnchant(Enchantment.DURABILITY, 3, false);
										leggingsEnchants.setDisplayName("§6Unholy Regalia");
										leggings.setItemMeta(leggingsEnchants);
										boots = new ItemStack(Material.NETHERITE_BOOTS);
										ItemMeta bootsEnchants = boots.getItemMeta();
										bootsEnchants.addEnchant(Enchantment.MENDING, 1, false);
										bootsEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false);
										bootsEnchants.addEnchant(Enchantment.DURABILITY, 3, false);
										bootsEnchants.addEnchant(Enchantment.DEPTH_STRIDER, 3, false);
										bootsEnchants.addEnchant(Enchantment.PROTECTION_FALL, 4, false);
										bootsEnchants.setDisplayName("§6Unholy Regalia");
										boots.setItemMeta(bootsEnchants);
										sword = new ItemStack(Material.NETHERITE_SWORD);
										ItemMeta swordEnchants = sword.getItemMeta();
										swordEnchants.addEnchant(Enchantment.MENDING, 1, false);
										swordEnchants.addEnchant(Enchantment.DAMAGE_ALL, 5, false);
										swordEnchants.addEnchant(Enchantment.DURABILITY, 3, false);
										swordEnchants.addEnchant(Enchantment.SWEEPING_EDGE, 3, false);
										swordEnchants.addEnchant(Enchantment.LOOT_BONUS_MOBS, 3, false);
										swordEnchants.addEnchant(Enchantment.FIRE_ASPECT, 2, false);
										swordEnchants.setDisplayName("§6Withered Blade");
										sword.setItemMeta(swordEnchants);
										ws.getEquipment().setItemInMainHand(sword);
									} else if (hasBrewery) {
										helmet = new ItemStack(Material.DIAMOND_HELMET);
										ItemMeta helmetEnchants = helmet.getItemMeta();
										helmetEnchants.addEnchant(Enchantment.WATER_WORKER, 1, false);
										helmetEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, false);
										helmetEnchants.addEnchant(Enchantment.OXYGEN, 2, false);
										helmetEnchants.addEnchant(Enchantment.DURABILITY, 2, false);
										helmetEnchants.setDisplayName("§6Unholy Regalia");
										helmet.setItemMeta(helmetEnchants);
										chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
										ItemMeta chestplateEnchants = chestplate.getItemMeta();
										chestplateEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, false);
										chestplateEnchants.addEnchant(Enchantment.DURABILITY, 2, false);
										chestplateEnchants.setDisplayName("§6Unholy Regalia");
										chestplate.setItemMeta(chestplateEnchants);
										leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
										ItemMeta leggingsEnchants = leggings.getItemMeta();
										leggingsEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, false);
										leggingsEnchants.addEnchant(Enchantment.DURABILITY, 2, false);
										leggingsEnchants.setDisplayName("§6Unholy Regalia");
										leggings.setItemMeta(leggingsEnchants);
										boots = new ItemStack(Material.DIAMOND_BOOTS);
										ItemMeta bootsEnchants = boots.getItemMeta();
										bootsEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, false);
										bootsEnchants.addEnchant(Enchantment.DURABILITY, 2, false);
										bootsEnchants.addEnchant(Enchantment.DEPTH_STRIDER, 2, false);
										bootsEnchants.setDisplayName("§6Unholy Regalia");
										boots.setItemMeta(bootsEnchants);
										sword = new ItemStack(Material.DIAMOND_SWORD);
										ItemMeta swordEnchants = sword.getItemMeta();
										swordEnchants.addEnchant(Enchantment.DAMAGE_ALL, 4, false);
										swordEnchants.addEnchant(Enchantment.SWEEPING_EDGE, 3, false);
										swordEnchants.addEnchant(Enchantment.LOOT_BONUS_MOBS, 3, false);
										swordEnchants.addEnchant(Enchantment.FIRE_ASPECT, 2, false);
										bootsEnchants.addEnchant(Enchantment.DURABILITY, 3, false);
										swordEnchants.setDisplayName("§6Withered Blade");
										sword.setItemMeta(swordEnchants);
									} else if (hasFire) {
										helmet = new ItemStack(Material.IRON_HELMET);
										ItemMeta helmetEnchants = helmet.getItemMeta();
										helmetEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, false);
										helmetEnchants.addEnchant(Enchantment.OXYGEN, 1, false);
										helmetEnchants.addEnchant(Enchantment.DURABILITY, 1, false);
										helmetEnchants.setDisplayName("§6Unholy Regalia");
										helmet.setItemMeta(helmetEnchants);
										chestplate = new ItemStack(Material.IRON_CHESTPLATE);
										ItemMeta chestplateEnchants = chestplate.getItemMeta();
										chestplateEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, false);
										chestplateEnchants.addEnchant(Enchantment.DURABILITY, 1, false);
										chestplateEnchants.setDisplayName("§6Unholy Regalia");
										chestplate.setItemMeta(chestplateEnchants);
										leggings = new ItemStack(Material.IRON_LEGGINGS);
										ItemMeta leggingsEnchants = leggings.getItemMeta();
										leggingsEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, false);
										leggingsEnchants.addEnchant(Enchantment.DURABILITY, 1, false);
										leggingsEnchants.setDisplayName("§6Unholy Regalia");
										leggings.setItemMeta(leggingsEnchants);
										boots = new ItemStack(Material.IRON_BOOTS);
										ItemMeta bootsEnchants = boots.getItemMeta();
										bootsEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, false);
										bootsEnchants.addEnchant(Enchantment.DURABILITY, 1, false);
										bootsEnchants.addEnchant(Enchantment.DEPTH_STRIDER, 1, false);
										bootsEnchants.setDisplayName("§6Unholy Regalia");
										boots.setItemMeta(bootsEnchants);
										sword = new ItemStack(Material.IRON_SWORD);
										ItemMeta swordEnchants = sword.getItemMeta();
										swordEnchants.addEnchant(Enchantment.DAMAGE_ALL, 3, false);
										swordEnchants.addEnchant(Enchantment.SWEEPING_EDGE, 3, false);
										swordEnchants.addEnchant(Enchantment.LOOT_BONUS_MOBS, 2, false);
										bootsEnchants.addEnchant(Enchantment.DURABILITY, 3, false);
										swordEnchants.addEnchant(Enchantment.FIRE_ASPECT, 1, false);
										swordEnchants.setDisplayName("§6Withered Blade");
										sword.setItemMeta(swordEnchants);
									} else {
										helmet = new ItemStack(Material.CHAINMAIL_HELMET);
										ItemMeta helmetEnchants = helmet.getItemMeta();
										helmetEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
										helmetEnchants.setDisplayName("§6Unholy Regalia");
										helmet.setItemMeta(helmetEnchants);
										chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
										ItemMeta chestplateEnchants = chestplate.getItemMeta();
										chestplateEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
										chestplateEnchants.setDisplayName("§6Unholy Regalia");
										chestplate.setItemMeta(chestplateEnchants);
										leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
										ItemMeta leggingsEnchants = leggings.getItemMeta();
										leggingsEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
										leggingsEnchants.setDisplayName("§6Unholy Regalia");
										leggings.setItemMeta(leggingsEnchants);
										boots = new ItemStack(Material.CHAINMAIL_BOOTS);
										ItemMeta bootsEnchants = boots.getItemMeta();
										bootsEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
										bootsEnchants.setDisplayName("§6Unholy Regalia");
										boots.setItemMeta(bootsEnchants);
										sword = new ItemStack(Material.STONE_SWORD);
										ItemMeta swordEnchants = sword.getItemMeta();
										swordEnchants.addEnchant(Enchantment.DAMAGE_ALL, 2, false);
										swordEnchants.addEnchant(Enchantment.SWEEPING_EDGE, 1, false);
										swordEnchants.addEnchant(Enchantment.LOOT_BONUS_MOBS, 1, false);
										bootsEnchants.addEnchant(Enchantment.DURABILITY, 1, false);
										swordEnchants.setDisplayName("§6Withered Blade");
										sword.setItemMeta(swordEnchants);
									}
									ws.getEquipment().setHelmet(helmet);
									ws.getEquipment().setChestplate(chestplate);
									ws.getEquipment().setLeggings(leggings);
									ws.getEquipment().setBoots(boots);
									ws.getEquipment().setItemInMainHand(sword);
									ws.setCustomName("§6Withered Knights");
									ws.setMetadata("fromPower", new FixedMetadataValue(Asteria.getPlugin(Asteria.class), "yes!"));
									if (Asteria.witherSkeletonQueue.size() > 0) {
										usedQueue = true;
										ws.setHealth(Asteria.witherSkeletonQueue.get(0));
										Asteria.witherSkeletonQueue.remove(0);
										Bukkit.getScheduler().cancelTask(Asteria.witherSkeletonQueueId.get(0));
										Asteria.witherSkeletonQueueId.remove(0);
									}
								}
								break;
							}
						}
						if (!usedQueue)
							p.damage(4 / Asteria.hanyPowerMultiplier);
					}
				} else if (type.equals(Material.NETHER_STAR) && displayName.equals("§6Royal Guard")) {
					if (PowerUtils.isInDimension(p.getWorld().getName()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now!");
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						boolean usedQueue = false;
						for (int i = 0; i < 50; i++) {
							Location l = p.getEyeLocation();
							l.add(l.getDirection().multiply(i + 1));
							if (!l.getBlock().getType().isAir()) {
								Location ll = p.getEyeLocation();
								ll.add(ll.getDirection().multiply(i));
								if (Asteria.hanyPowerMultiplier == 2) {
									Wither w = (Wither) p.getWorld().spawnEntity(ll, EntityType.WITHER);
									w.setCustomName("§6§lRoyal Guard");
									double maxHealth = 200 * Asteria.hanyPowerMultiplier;
									w.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
									w.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(w.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 1.5);
									w.setHealth(200 * Asteria.hanyPowerMultiplier);
									if (Asteria.witherQueue.size() > 0) {
										usedQueue = true;
										double oldHealth = Asteria.witherQueue.get(0);
										w.setHealth(oldHealth > maxHealth ? maxHealth : oldHealth);
										Asteria.witherQueue.remove(0);
										Bukkit.getScheduler().cancelTask(Asteria.witherQueueId.get(0));
										Asteria.witherQueueId.remove(0);
									}
								}
								Wither w = (Wither) p.getWorld().spawnEntity(ll, EntityType.WITHER);
								w.setCustomName("§6§lRoyal Guard");
								double maxHealth = 200 * Asteria.hanyPowerMultiplier;
								w.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
								w.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(w.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 1.5);
								w.setHealth(200 * Asteria.hanyPowerMultiplier);
								if (Asteria.witherQueue.size() > 0) {
									usedQueue = true;
									double oldHealth = Asteria.witherQueue.get(0);
									w.setHealth(oldHealth > maxHealth ? maxHealth : oldHealth);
									Asteria.witherQueue.remove(0);
									Bukkit.getScheduler().cancelTask(Asteria.witherQueueId.get(0));
									Asteria.witherQueueId.remove(0);
								}
								break;
							}
						}
						if (!usedQueue)
							p.damage(10 / Asteria.hanyPowerMultiplier);
					}
				} else if (type.equals(Material.WHITE_STAINED_GLASS_PANE) && displayName.equals("§6Dimensional Rooms")) {
					p.openInventory(Menu.dimensionalRooms(p));
				} else if (type.equals(Material.RED_STAINED_GLASS_PANE) && displayName.equals("§6Dimension Jump"))
					p.openInventory(Menu.jump(p));
				else if (type.equals(Material.LIGHT_BLUE_STAINED_GLASS_PANE) && displayName.equals("§6Spatial Manipulation") && p.getUniqueId().equals(Asteria.ed)) {
					if (Asteria.isSpatialBlockActive) {
						p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
						Asteria.isSpatialBlockActive = false;
						p.sendMessage(ChatColor.RED + "Disabled Spatial Manipulation!");
						Asteria.canSpatialBlockPlace = false;
						if (Asteria.lastSpatialBlock != null) {
							Asteria.lastSpatialBlock.getBlock().setType(Material.AIR);
							Asteria.lastSpatialBlock = null;
						}
					} else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Asteria.isSpatialBlockActive = true;
						p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(14);
						p.sendMessage(ChatColor.GREEN + "Enabled Spatial Manipulation!");
					}
				} else if (type.equals(Material.BLACK_STAINED_GLASS_PANE) && displayName.equals("§610th Dimensional Physiology")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldown10thDimensional, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldown10thDimensional, p);
					else if (p.getWorld().getName().equals("rift") || p.getWorld().getName().equals("dimensional_room") || p.getWorld().getName().equals("12thdimension") || Asteria.riftLocations.containsKey(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now!");
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else if (Asteria.tenthdimensionLocations.size() == 0) {
						boolean isTherePlayer = false;
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (a.getWorld().getName().equals(p.getWorld().getName()) && p.getLocation().distance(a.getLocation()) < 10 && !p.getUniqueId().equals(a.getUniqueId())) {
								isTherePlayer = true;
								break;
							}
						}
						if (isTherePlayer) {
							Location l = p.getLocation();
							for (Player a : Bukkit.getOnlinePlayers()) {
								if (a.getWorld().getName().equals(l.getWorld().getName()) && l.distance(a.getLocation()) < 10) {
									Asteria.tenthdimensionLocations.put(a.getUniqueId(), a.getLocation());
									Location ll = new Location(Bukkit.getWorld("10thdimension"), 0.5, 81, 0.5);
									ll.setX(r.nextDouble() * 20 - 10);
									ll.setZ(r.nextDouble() * 20 - 10);
									a.teleport(ll);
									a.sendMessage("§4§lYou have been warped to the 10th Dimension...");
									a.sendMessage("§4§l§oBeware of " + p.getName());
								}
							}
							p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100000, 0));
							p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100000, 1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 0));
							p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100000, 1));
							Asteria.dimensionIds.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
								for (int i = 0; i < 15; i++) {
									Location k = new Location(Bukkit.getWorld("10thdimension"), 0.5, 76, 0.5);
									k.setYaw(i * 24);
									k.add(k.getDirection().multiply(18));
									k.getWorld().strikeLightning(k);
								}
							}, 0, 25));
							Asteria.whenStopDimensional = Instant.now().getEpochSecond() + 20;
							Asteria.dimensionId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
								for (Integer id : Asteria.dimensionIds)
									Bukkit.getScheduler().cancelTask(id);
								Asteria.dimensionIds.clear();
								for (Entry<UUID, Location> u : Asteria.tenthdimensionLocations.entrySet()) {
									if (Bukkit.getPlayer(u.getKey()) != null) {
										Bukkit.getPlayer(u.getKey()).teleport(u.getValue());
										Bukkit.getPlayer(u.getKey()).sendMessage(ChatColor.GREEN + "You have been warped back from the 10th Dimension");
									}
								}
								Bukkit.getPlayer(Asteria.ed).sendMessage(ChatColor.GREEN + "Your time has run out! All of the items have been dropped on the floor");
								for (ItemStack is : Asteria.tenthDimensionItems)
									Bukkit.getPlayer(Asteria.ed).getWorld().dropItem(Bukkit.getPlayer(Asteria.ed).getLocation(), is);
								Asteria.tenthDimensionItems.clear();
								Asteria.tenthdimensionLocations.clear();
								p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
								p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
								p.removePotionEffect(PotionEffectType.SPEED);
								p.removePotionEffect(PotionEffectType.REGENERATION);
							}, (Asteria.whenStopDimensional - Instant.now().getEpochSecond()) * 20);
							Asteria.cooldown10thDimensional = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(300); // 5 minutes
						} else
							p.sendMessage(ChatColor.RED + "No one is in a 10 block radius from you!");
					} else
						p.sendMessage(ChatColor.RED + "You are already in the 10th dimension!");
				} else if (type.equals(Material.YELLOW_STAINED_GLASS_PANE) && displayName.equals("§6Energy Convergence")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownEnergyConvergence, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownEnergyConvergence, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 2));
						p.sendMessage(ChatColor.GREEN + "You have converged energy from the 11th dimension into absorption!");
						Asteria.cooldownEnergyConvergence = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(25); // 25 seconds
					}
				} else if (type.equals(Material.PURPLE_STAINED_GLASS_PANE) && displayName.equals("§6Rift")) {
					boolean canProceed = true;
					if (PowerUtils.isInDimension(p.getWorld().getName()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now!");
					else if (Asteria.riftLocations.containsKey(p.getUniqueId()))
						p.openInventory(Menu.rift(p));
					else if (PowerUtils.isOnCooldownNew(Asteria.cooldownRift, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownRift, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else if (Bukkit.getWorld("rift") != null) {
						if (Bukkit.unloadWorld("rift", false)) {
							try {
								FileUtils.deleteDirectory("./rift");
							} catch (IOException ex) {
								canProceed = false;
								ex.printStackTrace();
							}
						} else
							canProceed = false;
					} else if (!canProceed)
						p.sendMessage(ChatColor.RED + "Error while trying to use this power! Please try again later");
					else {
						int amountOfPlayers = 0;
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (p.getWorld().getName().equals(a.getWorld().getName()) && p.getLocation().distance(a.getLocation()) < 60 && !a.getUniqueId().equals(p.getUniqueId()))
								amountOfPlayers++;
						}
						if (amountOfPlayers > 1) {
							WorldCreator wc = new WorldCreator("rift");
							wc.environment(Environment.NORMAL);
							World w = Bukkit.createWorld(wc);
							WorldBorder wb = w.getWorldBorder();
							wb.setCenter(0, 0);
							wb.setSize(200);
							w.setAutoSave(false);
							Location l = p.getLocation();
							for (Player a : Bukkit.getOnlinePlayers()) {
								if (l.getWorld().getName().equals(a.getWorld().getName()) && l.distance(a.getLocation()) < 60) {
									Asteria.riftLocations.put(a.getUniqueId(), a.getLocation());
									int x = r.nextInt(200) - 100;
									int z = r.nextInt(200) - 100;
									a.teleport(new Location(w, x, w.getHighestBlockYAt(x, z) + 1, z));
								}
							}
							ArrayList<UUID> players = new ArrayList<UUID>();
							for (Entry<UUID, Location> entry : Asteria.riftLocations.entrySet()) {
								if (!entry.getKey().equals(Asteria.ed))
									players.add(entry.getKey());
							}
							for (int i = 0; i < players.size(); i++) {
								if (i != players.size() - 1)
									Asteria.riftTarget.put(players.get(i), players.get(i + 1));
								else
									Asteria.riftTarget.put(players.get(i), players.get(0));
								Bukkit.getPlayer(players.get(i)).sendMessage(ChatColor.GREEN + "Your target is " + ChatColor.DARK_GREEN + Bukkit.getPlayer(Asteria.riftTarget.get(players.get(i))).getName() + "!");
							}
							Asteria.riftId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> PowerUtils.endRift(), 24000);
							Asteria.cooldownRift = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(1800); // 30 minutes
						} else
							p.sendMessage(ChatColor.RED + "There was not enough people in a 60 block radius from you!");
					}
				} else if (type.equals(Material.YELLOW_DYE) && displayName.equals("§6Shooting Star")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownStarShot, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownStarShot, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						HashSet<Integer> ids = new HashSet<Integer>();
						outerloop: for (int i = 0; i < 400; i++) {
							final Location l = p.getEyeLocation();
							l.add(l.getDirection().multiply((i / 2) + 1.7));
							for (LivingEntity le : l.getWorld().getLivingEntities()) {
								if (l.getBlock().getType().isSolid() || le.getEyeLocation().distance(l) < 1.3) {
									int iii = 0;
									if (Asteria.hasAdvancement(p, "story/lava_bucket")) {
										Asteria.starShotCooldownCount++;
										if (Asteria.starShotCooldownCount > 2) {
											Asteria.cooldownStarShot = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(20);
											Asteria.starShotCooldownCount = 0;
										}
									} else
										Asteria.cooldownStarShot = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(20);
									for (int ii = l.getBlockY() + 60; ii > l.getWorld().getMinHeight(); ii--) {
										iii++;
										int iiii = ii;
										ids.add(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
											final Location ll = new Location(l.getWorld(), l.getX(), iiii, l.getZ());
											int lx = ll.getBlockX();
											int ly = ll.getBlockY();
											int lz = ll.getBlockZ();
											for (int x = lx - 2; x < lx + 2; x++) {
												for (int y = ly - 2; y < ly + 2; y++) {
													for (int z = lz - 2; z < lz + 2; z++) {
														Block b = l.getWorld().getBlockAt(x, y, z);
														if (b.getLocation().distance(ll) < 1.8) {
															DustOptions pa = new Particle.DustOptions(Color.fromRGB(247, 225, 22), 1);
															l.getWorld().spawnParticle(Particle.REDSTONE, b.getLocation(), 16, 0.6, 0.6, 0.6, 1, pa, true);
														}
													}
												}
											}
											if (ll.getBlock().getType().isSolid() || le.getEyeLocation().distance(ll) < 1.3 || l.getWorld().getMinHeight() + 1 == iiii) {
												Float power = 1.7F;
												if (Asteria.hasAdvancement(p, "end/elytra"))
													power = 2.3F;
												l.getWorld().createExplosion(ll, power, true, true, p);
												for (int o : ids)
													Bukkit.getScheduler().cancelTask(o);
											}
										}, iii / 3));
									}
									break outerloop;
								}
							}
						}
					}
				} else if (type.equals(Material.DIAMOND) && displayName.equals("§6Distortion")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownDistortion, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownDistortion, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						boolean isTherePlayer = false;
						for (LivingEntity le : p.getWorld().getLivingEntities()) {
							if (le.getLocation().distance(p.getLocation()) < 7 && !le.getUniqueId().equals(p.getUniqueId())) {
								double damage = 10;
								if (Asteria.hasAdvancement(p, "end/elytra"))
									damage = 12;
								le.damage(damage, p);
								le.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 200, 0));
								le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 400, 1));
								le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 2));
								le.sendMessage(ChatColor.YELLOW + "You have been distorted!");
								isTherePlayer = true;
							}
						}
						if (isTherePlayer) {
							Location l = p.getLocation();
							int lx = l.getBlockX();
							int ly = l.getBlockY();
							int lz = l.getBlockZ();
							for (int x = lx - 8; x < lx + 8; x++) {
								for (int y = ly - 8; y < ly + 8; y++) {
									for (int z = lz - 8; z < lz + 8; z++) {
										Block b = l.getWorld().getBlockAt(x, y, z);
										if (b.getLocation().distance(p.getLocation()) < 7)
											l.getWorld().spawnParticle(Particle.SPELL_WITCH, b.getLocation(), 2, 0.5, 0.5, 0.5);
									}
								}
							}
							PotionEffectType[] effects = { PotionEffectType.ABSORPTION, PotionEffectType.DAMAGE_RESISTANCE, PotionEffectType.DOLPHINS_GRACE, PotionEffectType.FAST_DIGGING, PotionEffectType.FIRE_RESISTANCE, PotionEffectType.HEAL, PotionEffectType.HEALTH_BOOST, PotionEffectType.HERO_OF_THE_VILLAGE, PotionEffectType.INCREASE_DAMAGE, PotionEffectType.JUMP, PotionEffectType.LUCK, PotionEffectType.NIGHT_VISION, PotionEffectType.REGENERATION, PotionEffectType.SATURATION, PotionEffectType.SLOW_FALLING, PotionEffectType.SPEED, PotionEffectType.WATER_BREATHING };
							ArrayList<PotionEffectType> positiveEffects = new ArrayList<PotionEffectType>();
							for (PotionEffectType pet : effects)
								positiveEffects.add(pet);
							int power = 1;
							if (Asteria.hasAdvancement(p, "nether/obtain_blaze_rod")) {
								power = 2;
								int random = r.nextInt(positiveEffects.size());
								p.addPotionEffect(new PotionEffect(positiveEffects.get(random), 600, power));
								positiveEffects.remove(random);
							}
							int random = r.nextInt(positiveEffects.size());
							p.addPotionEffect(new PotionEffect(positiveEffects.get(random), 600, power));
							positiveEffects.remove(random);
							int random1 = r.nextInt(positiveEffects.size());
							p.addPotionEffect(new PotionEffect(positiveEffects.get(random1), 600, power));
							p.sendMessage(ChatColor.GREEN + "You have distorted everyone in a 7 block radius from you!");
							Asteria.cooldownDistortion = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(120); // 2 minutes
						} else
							p.sendMessage(ChatColor.RED + "There was no entity in a 7 block radius from you!");
					}
				} else if (type.equals(Material.BLAZE_POWDER) && displayName.equals("§6Eruption")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownEruption, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownEruption, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						for (int i = 0; i < 50; i++) {
							Location l = p.getEyeLocation();
							l.add(l.getDirection().multiply(i + 1));
							l.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, l, 3, 0.2, 0.2, 0.2);
							if (!l.getBlock().getType().isAir()) {
								for (LivingEntity le : l.getWorld().getLivingEntities()) {
									if (le.getLocation().distance(l) < 9 && !p.getUniqueId().equals(le.getUniqueId())) {
										if (Asteria.hasAdvancement(p, "story/shiny_gear")) {
											le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
											le.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
										}
										double damage = 12;
										if (Asteria.hasAdvancement(p, "end/elytra"))
											damage = 14;
										le.damage(damage, p);
										le.setVelocity(le.getLocation().getDirection().multiply(0).setY(1.35));
									}
								}
								for (int x = l.getBlockX() - 4; x < l.getBlockX() + 4; x++) {
									for (int z = l.getBlockZ() - 4; z < l.getBlockZ() + 4; z++)
										l.getWorld().spawnParticle(Particle.SMALL_FLAME, l, 6, 0.5, 0.5, 0.5, 0.2);
								}
								Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> l.getWorld().createExplosion(l, 5, true, true, p), 8);
								Asteria.cooldownEruption = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(180); // 3 minutes
								return;
							}
						}
						p.sendMessage(ChatColor.RED + "You didn't hit anything!");
					}
				} else if (type.equals(Material.TNT) && displayName.equals("§6Supernova")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownSupernova, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownSupernova, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Location l = p.getLocation();
						l.setY(l.getY() + 1.65);
						p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0));
						p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 0));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1200, 0));
						l.getWorld().createExplosion(l, 8, true, true, p);
						p.damage(10);
						Asteria.cooldownSupernova = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(480); // 8 minutes
					}
				} else if (type.equals(Material.NETHER_STAR) && displayName.equals("§6Star Shield")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownStarshield, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownStarshield, p);
					else if (Asteria.starshieldActive)
						p.sendMessage(ChatColor.RED + "You already have this active!");
					else {
						p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 4));
						p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
						Asteria.starshieldActive = true;
						Asteria.starShieldId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Asteria.starshieldActive = false, 200);
						Asteria.cooldownStarshield = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(600); // 10 minutes
					}
				} else if (type.equals(Material.END_CRYSTAL) && displayName.equals("§6Wrath of Asteria")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownWrath, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownWrath, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						p.setHealth(28);
						p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 3));
						p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1200, 1));
						p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 1));
						p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 3));
						ItemStack is = new ItemStack(Material.NETHERITE_SWORD);
						ItemMeta im = is.getItemMeta();
						im.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
						im.setDisplayName("§6Bane of Darkness");
						is.setItemMeta(im);
						p.getInventory().addItem(is);
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (a.getWorld().getName().equals(p.getWorld().getName()) && p.getLocation().distance(a.getLocation()) < 30 && p.getUniqueId().equals(a.getUniqueId()))
								a.sendMessage(ChatColor.YELLOW + "The Star God calls upon his wrath!");
						}
						Asteria.thirtySecId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Asteria.hasthirtysecpassed = true;
							Asteria.thirtySecId = 0;
						}, 600);
						Asteria.wrathId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							boolean foundItem = false;
							for (ItemStack iss : p.getInventory().getContents()) {
								if (iss != null && iss.getItemMeta().getDisplayName() != null && iss.getItemMeta().getDisplayName().equals("§6Bane of Darkness")) {
									p.getInventory().remove(iss);
									foundItem = true;
								}
							}
							if (!foundItem && p.getItemOnCursor() != null && p.getItemOnCursor().getItemMeta() != null && p.getItemOnCursor().getItemMeta().getDisplayName() != null && p.getItemOnCursor().getItemMeta().getDisplayName().equals("§6Bane of Darkness")) {
								p.getInventory().remove(p.getItemOnCursor());
								foundItem = true;
							}
							Asteria.hasUsedBane = false;
							Asteria.hasUsedBaneAfter = false;
							Asteria.hasthirtysecpassed = false;
							if (!foundItem)
								Bukkit.getLogger().warning("Could not remove Bane of Darkness from " + p.getName() + "'s inventory");
							if (Asteria.baneId != 0) {
								Bukkit.getScheduler().cancelTask(Asteria.baneId);
								Asteria.baneId = 0;
							}
							Asteria.thirtySecId = 0;
							Asteria.wrathId = 0;
						}, 1200);
						Asteria.cooldownWrath = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(900); // 15 minutes
					}
				} else if (type.equals(Material.NETHERITE_SWORD) && displayName.equals("§6Bane of Darkness")) {
					if (Asteria.baneId == 0) {
						if ((!Asteria.hasUsedBane && !Asteria.hasthirtysecpassed) || (Asteria.hasUsedBane && !Asteria.hasUsedBaneAfter && Asteria.hasthirtysecpassed) || (!Asteria.hasUsedBane && Asteria.hasthirtysecpassed)) {
							if (Asteria.hasthirtysecpassed)
								Asteria.hasUsedBaneAfter = true;
							else
								Asteria.hasUsedBane = true;
							Asteria.baneId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
								for (int i = 0; i < 50; i++) {
									Location l = p.getEyeLocation();
									l.add(l.getDirection().multiply(i + 1));
									DustOptions pa = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1);
									l.getWorld().spawnParticle(Particle.REDSTONE, l, 60, 0.2, 0.2, 0.2, pa);
									if (!l.getBlock().getType().isAir() || i == 49) {
										for (LivingEntity le : l.getWorld().getLivingEntities()) {
											if (le.getLocation().distance(l) < 3)
												le.damage(6, p);
										}
										l.getWorld().createExplosion(l, 3, true, true, p);
										return;
									}
								}
							}, 0, 3);
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
								Bukkit.getScheduler().cancelTask(Asteria.baneId);
								Asteria.baneId = 0;
							}, 200);
						}
					}
				} else if (type.equals(Material.SUNFLOWER) && displayName.equals("§6Withered Allegiance")) {
					if (PowerUtils.isInDimension(p.getWorld().getName()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now!");
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						for (int i = 0; i < 50; i++) {
							Location l = p.getEyeLocation();
							l.add(l.getDirection().multiply(i + 1));
							if (!l.getBlock().getType().isAir()) {
								boolean usedQueue = false;
								int o = 0;
								if (Asteria.mobHanySelected.equals(EntityType.PHANTOM))
									o = 4;
								else if (Asteria.whatMobHanySelected.equals("horse"))
									o = 1;
								else
									o = 2;
								for (int oo = 0; oo < o * Asteria.hanyPowerMultiplier; oo++) {
									Location ll = p.getEyeLocation();
									ll.add(ll.getDirection().multiply(i));
									LivingEntity w = (LivingEntity) p.getWorld().spawnEntity(ll, Asteria.mobHanySelected);
									w.setCustomName("§6Withered Allegiance");
									if (w instanceof AbstractHorse) {
										AbstractHorse h = (AbstractHorse) w;
										h.getInventory().setSaddle(new ItemStack(Material.SADDLE));
										h.setOwner(p);
									}
									if (!Asteria.withersallegianceQueue.containsKey(Asteria.mobHanySelected))
										Asteria.withersallegianceQueue.put(Asteria.mobHanySelected, new ArrayList<Double>());
									if (!Asteria.withersallegianceQueueId.containsKey(Asteria.mobHanySelected))
										Asteria.withersallegianceQueueId.put(Asteria.mobHanySelected, new ArrayList<Integer>());
									if (Asteria.withersallegianceQueue.get(Asteria.mobHanySelected).size() > 0) {
										usedQueue = true;
										w.setHealth(Asteria.withersallegianceQueue.get(Asteria.mobHanySelected).get(0));
										Asteria.withersallegianceQueue.get(Asteria.mobHanySelected).remove(0);
										Bukkit.getScheduler().cancelTask(Asteria.withersallegianceQueueId.get(Asteria.mobHanySelected).get(0));
										Asteria.withersallegianceQueueId.get(Asteria.mobHanySelected).remove(0);
									}
								}
								if (!usedQueue) {
									if (Asteria.whatMobHanySelected.equals("horse") || Asteria.mobHanySelected.equals(EntityType.PHANTOM))
										p.damage(1 / Asteria.hanyPowerMultiplier);
									else
										p.damage(2 / Asteria.hanyPowerMultiplier);
								}
								break;
							}
						}
					}
				} else if (p.getUniqueId().equals(Asteria.ed) && Asteria.hasAdvancement(p, "story/mine_diamond") && (type.name().endsWith("_PICKAXE") || type.name().endsWith("_SWORD") || type.name().endsWith("_SHOVEL") || type.name().endsWith("_HOE") || type.name().endsWith("_AXE"))) {
					if (Asteria.isReversalActive) {
						Asteria.isReversalActive = false;
						p.sendMessage(ChatColor.RED + "You have deactivated your Revenge Counter!");
					} else if (Asteria.clicksForReversal == 2) {
						if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
							p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
						else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
							p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
						else if (Asteria.reversalDamage < 7.5)
							p.sendMessage(ChatColor.RED + "You can't use Revenge Counter with less than 10% damage!");
						else if (!Asteria.hasAdvancement(p, "story/shiny_gear"))
							p.sendMessage(ChatColor.RED + "You must unlock Cover me With Diamonds to use this!");
						else {
							Asteria.clicksForReversal = 0;
							Bukkit.getScheduler().cancelTask(Asteria.cancelClicksId);
							HashSet<LivingEntity> toHurt = new HashSet<LivingEntity>();
							for (LivingEntity le : p.getWorld().getLivingEntities()) {
								EntityType eType = le.getType();
								if (p.getLocation().distance(le.getLocation()) < 6 && !p.getUniqueId().equals(le.getUniqueId()) && (eType.equals(EntityType.BLAZE) || eType.equals(EntityType.CAVE_SPIDER) || eType.equals(EntityType.CREEPER) || eType.equals(EntityType.DROWNED) || eType.equals(EntityType.ELDER_GUARDIAN) || eType.equals(EntityType.ENDERMAN) || eType.equals(EntityType.EVOKER) || eType.equals(EntityType.GHAST) || eType.equals(EntityType.GUARDIAN) || eType.equals(EntityType.HOGLIN) || eType.equals(EntityType.HUSK) || eType.equals(EntityType.MAGMA_CUBE) || eType.equals(EntityType.PHANTOM) || eType.equals(EntityType.PIGLIN) || eType.equals(EntityType.PIGLIN_BRUTE) || eType.equals(EntityType.PILLAGER) || eType.equals(EntityType.RAVAGER) || eType.equals(EntityType.SKELETON) || eType.equals(EntityType.SLIME) || eType.equals(EntityType.SPIDER) || eType.equals(EntityType.STRAY) || eType.equals(EntityType.VEX) || eType.equals(EntityType.VINDICATOR) || eType.equals(EntityType.WITCH) || eType.equals(EntityType.WITHER_SKELETON) || eType.equals(EntityType.WITHER) || eType.equals(EntityType.ZOGLIN) || eType.equals(EntityType.ZOMBIE) || eType.equals(EntityType.ZOMBIE_VILLAGER) || eType.equals(EntityType.ZOMBIFIED_PIGLIN) || eType.equals(EntityType.ENDER_DRAGON) || eType.equals(EntityType.GLOW_SQUID) || eType.equals(EntityType.PLAYER)))
									toHurt.add(le);
							}
							if (toHurt.size() == 0)
								p.sendMessage(ChatColor.RED + "There was no one in a 6 block radius from you!");
							else {
								DustOptions pb = new Particle.DustOptions(Color.fromRGB(114, 137, 218), 1);
								for (int o = -1; o < 1; o++) {
									for (int i = 0; i < 10; i++) {
										Location l = p.getLocation();
										for (int k = 0; k < 15; k++) {
											l.setPitch(o * 40);
											l.setYaw(i * 24);
											l.add(l.getDirection().multiply(0.7));
											l.getWorld().spawnParticle(Particle.REDSTONE, l, 16, 0.15, 0.15, 0.15, 1, pb, true);
										}
									}
								}
								if (Asteria.halfDamage.contains(p.getUniqueId()))
									Asteria.reversalDamage = Asteria.reversalDamage / 2;
								Double damageToEach = Asteria.reversalDamage / toHurt.size();
								DustOptions pa = new Particle.DustOptions(Color.fromRGB(252, 186, 3), 1);
								String msg = ChatColor.DARK_AQUA + p.getName() + " use suicidal Area Counter!";
								if (Asteria.isReversalAreaSuicide) {
									pa = new Particle.DustOptions(Color.fromRGB(252, 23, 3), 1); // change the color of the particles
									damageToEach = damageToEach * 5;
									if (p.getHealth() > 2) {
										p.damage(0);
										p.setHealth(2);
									}
								}
								for (LivingEntity le : toHurt) {
									le.sendMessage(msg);
									Location l = p.getEyeLocation();
									le.damage(damageToEach, p);
									le.setVelocity(le.getEyeLocation().toVector().subtract(p.getEyeLocation().toVector()).multiply(4));
									while (l.distance(le.getEyeLocation()) > 0.12) {
										l.add(le.getEyeLocation().toVector().subtract(p.getEyeLocation().toVector()).multiply(0.1));
										l.getWorld().spawnParticle(Particle.REDSTONE, l, 10, 0.1, 0.1, 0.1, 0.1, pa, true);
									}
									le.sendMessage(ChatColor.RED + "You have been hit with " + ChatColor.GOLD + String.valueOf(new DecimalFormat("#.##").format(damageToEach)) + ChatColor.RED + " damage!");
								}
								p.sendMessage(ChatColor.BLUE + "You have used your Area Counter that hit " + ChatColor.DARK_BLUE + toHurt.size() + ChatColor.BLUE + " entities and hit each with " + ChatColor.DARK_BLUE + String.valueOf(new DecimalFormat("#.##").format(damageToEach)) + ChatColor.BLUE + " damage!");
								Asteria.reversalDamage = 0D;
							}
						}
					} else if (Asteria.clicksForReversal == 1) {
						Asteria.clicksForReversal++;
						Bukkit.getScheduler().cancelTask(Asteria.cancelClicksId);
						Asteria.cancelClicksId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							if (Asteria.reversalDamage > 1) {
								Asteria.isReversalActive = true;
								p.sendMessage(ChatColor.AQUA + "You have activated your Revenge Counter power!");
							}
							Asteria.clicksForReversal = 0;
						}, 4);
					} else {
						Asteria.clicksForReversal++;
						Asteria.cancelClicksId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Asteria.clicksForReversal = 0, 4);
					}
				} else if (type.equals(Material.GOLD_INGOT) && displayName.equals("§6Raging Storm")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownRagingStorm, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownRagingStorm, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						boolean isTherePlayer = false;
						double damage = Asteria.hasAdvancement(p, "nether/brew_potion") ? 12 : 10;
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (a.getWorld().getName().equals(p.getWorld().getName()) && a.getLocation().distance(p.getLocation()) < 10 && !a.getUniqueId().equals(p.getUniqueId())) {
								a.getWorld().strikeLightning(a.getLocation());
								a.damage(damage, p);
								a.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
								isTherePlayer = true;
							}
						}
						if (isTherePlayer) {
							for (int i = 0; i < 180; i++) {
								Location l = p.getLocation();
								l.setPitch(0);
								l.setYaw(i * 2);
								l.add(l.getDirection().multiply(10));
								l.getWorld().spawnParticle(Particle.SNEEZE, l, 15, 0.5, 0.5, 0.5, 0.05, null, true);
							}
							Asteria.cooldownRagingStorm = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(120); // 2 minutes
						} else
							p.sendMessage(ChatColor.RED + "There was no one in a 10 block radius from you!");
					}
				} else if (type.equals(Material.NAUTILUS_SHELL) && displayName.equals("§bCalling Conch"))
					p.openInventory(Menu.callingConch(p));
				else if (type.equals(Material.HEART_OF_THE_SEA) && displayName.equals("§bClimate Change"))
					p.openInventory(Menu.climateChange(p));
				else if (type.equals(Material.DIAMOND) && displayName.equals("§bAqua Blast")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownAquaBlast, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownAquaBlast, p);
					else {
						HashSet<Integer> ids = new HashSet<Integer>();
						Location l = p.getEyeLocation();
						double damage = Asteria.hasAdvancement(p, "nether/brew_potion") ? 14 : 12;
						for (int i = 0; i < 600; i++) {
							final int ii = i;
							ids.add(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
								Location ll = l.clone();
								ll.add(ll.getDirection().multiply((double) ii / 2));
								DustOptions pa = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1);
								ll.getWorld().spawnParticle(Particle.REDSTONE, ll, 15, 0.4, 0.4, 0.4, 1, pa, true);
								boolean isThereEntity = false;
								for (LivingEntity le : ll.getWorld().getLivingEntities()) {
									if (!le.getUniqueId().equals(p.getUniqueId()) && (ll.distance(le.getLocation()) < 1.8 || ll.distance(le.getEyeLocation()) < 1.8)) {
										isThereEntity = true;
										le.damage(damage, p);
										le.setVelocity(ll.getDirection().multiply(2).setY(1.2));
									}
								}
								if (isThereEntity) {
									for (int toCancel : ids)
										Bukkit.getScheduler().cancelTask(toCancel);
									ll.getWorld().playSound(ll, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
									Location lll = ll.getBlock().getLocation();
									int x = lll.getBlockX();
									int y = lll.getBlockY();
									int z = lll.getBlockZ();
									for (int iii = 1; iii < 5; iii++) {
										final int iiii = iii;
										Bukkit.getScheduler().scheduleSyncDelayedTask(Asteria.getPlugin(Asteria.class), () -> {
											for (int xx = x - iiii; xx < x + iiii; xx++) {
												for (int yy = y - iiii; yy < y + iiii; yy++) {
													for (int zz = z - iiii; zz < z + iiii; zz++) {
														Block b = lll.getWorld().getBlockAt(xx, yy, zz);
														if (b.getLocation().distance(lll) > iiii - 1 && b.getLocation().distance(lll) < iiii)
															lll.getWorld().spawnParticle(Particle.REDSTONE, b.getLocation(), 15, 0.5, 0.5, 0.5, 1, pa, true);
													}
												}
											}
										}, iii * 2);
									}
								}
							}, i / 2));
						}
						Asteria.cooldownAquaBlast = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(60); // 1 minute
					}
				} else if (type.equals(Material.CONDUIT) && displayName.equals("§bWrath of the Sea")) {
					if (Asteria.isWrathSeaActive)
						p.sendMessage(ChatColor.RED + "Your Wrath of the Sea is already active!");
					else if (PowerUtils.isOnCooldownNew(Asteria.cooldownWrathSea, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownWrathSea, p);
					else if (PowerUtils.isInDimension(p.getWorld().getName()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now!");
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						p.sendMessage(ChatColor.GREEN + "You have activated your Wrath of the Sea!");
						Asteria.joseInvincible = true;
						Asteria.isWrathSeaActive = true;
						Asteria.invicibleId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Asteria.joseInvincible = false, 200);
						Asteria.wrathSeaId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Asteria.isWrathSeaActive = false;
							p.sendMessage(ChatColor.RED + "Your Wrath of the Sea has run out!");
						}, 800);
						for (int distance = 0; distance < 50; distance++) {
							Location l = p.getEyeLocation();
							l.add(l.getDirection().multiply(distance + 1));
							if (!l.getBlock().getType().isAir() || distance == 49) {
								Location ll = p.getEyeLocation();
								ll.add(ll.getDirection().multiply(distance));
								for (int ii = 0; ii < 10; ii++) {
									Drowned d = (Drowned) p.getWorld().spawnEntity(new Location(ll.getWorld(), ll.getX() + (r.nextDouble() * 2 - 1), ll.getY(), ll.getZ() + (r.nextDouble() * 2 - 1)), EntityType.DROWNED);
									d.setCustomName("§bGuards of the Abyss");
									ItemStack helmet = new ItemStack(Material.GOLDEN_HELMET);
									ItemMeta helmetEnchants = helmet.getItemMeta();
									helmetEnchants.addEnchant(Enchantment.WATER_WORKER, 1, false);
									helmetEnchants.addEnchant(Enchantment.MENDING, 1, false);
									helmetEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false);
									helmetEnchants.addEnchant(Enchantment.OXYGEN, 3, false);
									helmetEnchants.addEnchant(Enchantment.DURABILITY, 3, false);
									helmet.setItemMeta(helmetEnchants);
									d.getEquipment().setHelmet(helmet);
									ItemStack chestplate = new ItemStack(Material.GOLDEN_CHESTPLATE);
									ItemMeta chestplateEnchants = chestplate.getItemMeta();
									chestplateEnchants.addEnchant(Enchantment.MENDING, 1, false);
									chestplateEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false);
									chestplateEnchants.addEnchant(Enchantment.DURABILITY, 3, false);
									chestplate.setItemMeta(chestplateEnchants);
									d.getEquipment().setChestplate(chestplate);
									ItemStack leggings = new ItemStack(Material.GOLDEN_LEGGINGS);
									ItemMeta leggingsEnchants = leggings.getItemMeta();
									leggingsEnchants.addEnchant(Enchantment.MENDING, 1, false);
									leggingsEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false);
									leggingsEnchants.addEnchant(Enchantment.DURABILITY, 3, false);
									leggings.setItemMeta(leggingsEnchants);
									d.getEquipment().setLeggings(leggings);
									ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS);
									ItemMeta bootsEnchants = boots.getItemMeta();
									bootsEnchants.addEnchant(Enchantment.MENDING, 1, false);
									bootsEnchants.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false);
									bootsEnchants.addEnchant(Enchantment.DURABILITY, 3, false);
									bootsEnchants.addEnchant(Enchantment.DEPTH_STRIDER, 3, false);
									bootsEnchants.addEnchant(Enchantment.PROTECTION_FALL, 4, false);
									boots.setItemMeta(bootsEnchants);
									d.getEquipment().setBoots(boots);
									ItemStack trident = new ItemStack(Material.TRIDENT);
									ItemMeta tridentEnchants = trident.getItemMeta();
									tridentEnchants.addEnchant(Enchantment.IMPALING, 5, false);
									tridentEnchants.addEnchant(Enchantment.DURABILITY, 3, false);
									tridentEnchants.addEnchant(Enchantment.CHANNELING, 1, false);
									tridentEnchants.addEnchant(Enchantment.MENDING, 1, false);
									trident.setItemMeta(tridentEnchants);
									d.getEquipment().setItemInMainHand(trident);
								}
								break;
							}
						}
						Asteria.cooldownWrathSea = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(600); // 10 minutes
					}
				} else if (type.equals(Material.BLUE_GLAZED_TERRACOTTA) && displayName.equals("§bArea Counter"))
					p.openInventory(Menu.areaReversal());
				else if (type.equals(Material.COAL) && displayName.equals("§6Screech")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownScreech, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownScreech, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						HashSet<LivingEntity> entities = new HashSet<LivingEntity>();
						for (LivingEntity le : p.getWorld().getLivingEntities()) {
							if (le.getLocation().distance(p.getLocation()) < 15 && !le.getUniqueId().equals(p.getUniqueId()))
								entities.add(le);
						}
						if (entities.isEmpty()) {
							p.sendMessage(ChatColor.RED + "There were no entities in a 15 block radius you could affect!");
						} else {
							for (LivingEntity l : entities) {
								l.sendMessage(ChatColor.RED + "You have been given slowness and now only have half your strength for 10 seconds!");
								l.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 2));
								if (l instanceof Player) {
									int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> ((Player) l).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§4§lYou are only doing half your damage!")), 0, 10);
									Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.getScheduler().cancelTask(task), 190);
								}
								Asteria.halfDamage.add(l.getUniqueId());
								Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Asteria.halfDamage.remove(l.getUniqueId()), 200);
							}
							p.sendMessage(ChatColor.GREEN + "You have hit " + ChatColor.DARK_GREEN + entities.size() + ChatColor.GREEN + " entities with §6Screech" + ChatColor.GREEN + "!");
							Asteria.cooldownScreech = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(60); // 1 minute
						}
					}
				} else if (type.equals(Material.NETHERITE_INGOT) && displayName.equals("§6Dark Shift")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownDarkShift, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownDarkShift, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Location l = p.getLocation();
						l.setPitch(0);
						l.add(l.getDirection().multiply(5));
						p.teleport(l);
						DustOptions pa = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1);
						l.getWorld().spawnParticle(Particle.REDSTONE, l, 100, 0.2, 0.2, 0.2, 1, pa, true);
						for (LivingEntity le : l.getWorld().getLivingEntities()) {
							if (le.getLocation().distance(l) < 5.5 && !le.getUniqueId().equals(p.getUniqueId())) {
								le.sendMessage(ChatColor.of("#070707") + "You have been given Blindness and Nausea for 5 seconds!");
								le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
								le.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
								le.damage(12);
							}
						}
						p.sendMessage(ChatColor.GREEN + "You have warped 5 blocks in front of you!");
						Asteria.cooldownDarkShift = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(120); // 2 minutes
					}
				} else if (type.equals(Material.GHAST_TEAR) && displayName.equals("§6Return")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownReturn, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownReturn, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else if (PowerUtils.isInDimension(p.getWorld().getName()) || p.getWorld().getName().equals("rift"))
						p.sendMessage(ChatColor.RED + "You may not use §6Return " + ChatColor.RED + "at this moment!");
					else {
						Location l = Bukkit.getWorld("world").getSpawnLocation();
						if (p.getBedSpawnLocation() != null)
							l = p.getBedSpawnLocation();
						if (l.getWorld().getName().equals(p.getEyeLocation().getWorld().getName()) && l.distance(p.getEyeLocation()) < 7)
							p.sendMessage(ChatColor.RED + "You can't warp that close to your spawnpoint!");
						else {
							Location old = p.getEyeLocation();
							p.teleport(l);
							p.sendMessage(ChatColor.LIGHT_PURPLE + "You have teleported to your spawnpoint!");
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1200, 2));
							p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 2));
							for (Player a : Bukkit.getOnlinePlayers()) {
								if (a.getWorld().getName().equals(old.getWorld().getName()) && a.getLocation().distance(old) < 7)
									a.sendMessage(ChatColor.DARK_GREEN + p.getName() + " has warped to their spawnpoint!");
							}
							for (int i = 0; i < 5; i++)
								old.getWorld().spawnEntity(old, EntityType.PHANTOM);
							Asteria.cooldownReturn = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(1800); // 30 minutes
						}
					}
				} else if (type.equals(Material.SLIME_BALL) && displayName.equals("§6Poisonous Breath")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownPoisonBreath, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownPoisonBreath, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else if (Asteria.poisonBreath != null)
						p.sendMessage(ChatColor.RED + "There can only be one location with §6Poisonous Breath " + ChatColor.RED + "at a time!");
					else {
						Location l = p.getLocation();
						l.setY(l.getY() + 0.1);
						l = l.getBlock().getLocation();
						Asteria.poisonBreath = l;
						p.sendMessage(ChatColor.GREEN + "You have breathed poison under your feet that will last for 30 seconds!");
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (a.getWorld().getName().equals(l.getWorld().getName()) && a.getLocation().distance(l) < 8 && !a.getUniqueId().equals(p.getUniqueId()))
								a.sendMessage(ChatColor.DARK_GREEN + p.getName() + " has breathed poison under their feet near you!");
						}
						Asteria.poisonBreathId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Asteria.poisonBreath = null, 600);
						Asteria.cooldownPoisonBreath = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(240); // 4 minutes
					}
				} else if (type.equals(Material.DRAGON_EGG) && displayName.equals("§6Black Hole")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownBlackHole, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownBlackHole, p);
					else if (Asteria.isBlackHoleActive)
						p.sendMessage(ChatColor.RED + "You can't use " + displayName + ChatColor.RED + " when its already active!");
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Asteria.isBlackHoleActive = true;
						p.sendMessage(ChatColor.GREEN + "You have used " + displayName + ChatColor.GREEN + "!");
						long endTime = Instant.now().getEpochSecond() + 10;
						ArrayList<Integer> num = new ArrayList<Integer>();
						HashSet<UUID> entitiesCantTakeDamage = new HashSet<UUID>();
						Asteria.blackHoleId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
							if (p.isOnline()) {
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eTime left for §6Black Hole§e: " + (endTime - Instant.now().getEpochSecond())));
								for (int i = 0; i < 50; i++) {
									Location l = p.getEyeLocation();
									l.add(l.getDirection().multiply(i + 1));
									if (!l.getBlock().getType().isAir() || i == 49) {
										if (num.isEmpty())
											num.add(0);
										if (num.get(0) == 5) {
											num.set(0, 0);
											l.getWorld().createExplosion(l, 4, true, true, p);
											DustOptions pa = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1);
											p.getWorld().spawnParticle(Particle.REDSTONE, l, 300, 2, 2, 2, 1, pa, true);
										} else
											num.set(0, num.get(0) + 1);
										for (LivingEntity le : l.getWorld().getLivingEntities()) {
											if (le.getLocation().distance(l) < 10 && !le.getUniqueId().equals(p.getUniqueId())) {
												Vector fire = l.toVector().subtract(le.getLocation().toVector()).multiply(0.06);
												le.setVelocity(fire);
											}
											if (le.getLocation().distance(l) < 3.5 && !le.getUniqueId().equals(p.getUniqueId())) {
												le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 3));
												if (!entitiesCantTakeDamage.contains(le.getUniqueId())) {
													entitiesCantTakeDamage.add(le.getUniqueId());
													le.damage(2, p);
												}
											}
										}
										break;
									}
								}
							}
						}, 0, 1);
						Asteria.blackHoleEndId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Bukkit.getScheduler().cancelTask(Asteria.blackHoleId);
							Asteria.isBlackHoleActive = false;
						}, 200);
						Asteria.cooldownBlackHole = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(360); // 6 minutes
					}
				} else if (type.equals(Material.PHANTOM_MEMBRANE) && displayName.equals("§6Phantom Burst")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownPhantomBurst, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownPhantomBurst, p);
					else if (PowerUtils.isInDimension(p.getWorld().getName()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now!");
					else if (Asteria.isPhantomBurstActive)
						p.sendMessage(ChatColor.RED + "You may not use §6Phantom Burst " + ChatColor.RED + "while it is active!");
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Asteria.isPhantomBurstActive = true;
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (!a.getUniqueId().equals(p.getUniqueId()) && a.getLocation().distance(p.getLocation()) < 7)
								a.sendMessage(ChatColor.GOLD + p.getName() + " has activated §6Phantom Burst " + ChatColor.GOLD + "near you!");
						}
						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 600, 1));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 2));
						p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 3));
						long endTime = Instant.now().getEpochSecond() + 30;
						Location l = p.getEyeLocation();
						l.setY(l.getY() + 1);
						for (int i = 0; i < 10; i++) {
							Phantom ph = (Phantom) p.getWorld().spawnEntity(l, EntityType.PHANTOM);
							ph.setCustomName("§eBrent's Phantom");
						}
						p.sendMessage(ChatColor.BLUE + "You have activated Phantom Burst, and 10 phantoms has been summoned to fight for you!");
						Asteria.phantomBurstId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
							if (p.isOnline()) {
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eTime left for §6Phantom Burst§e: " + (endTime - Instant.now().getEpochSecond())));
								for (int i = 0; i < 90; i++) {
									Location m = p.getLocation();
									m.setPitch(0);
									m.setYaw(i * 4);
									m.add(m.getDirection().multiply(5));
									DustOptions pa = new Particle.DustOptions(Color.fromRGB(152, 25, 194), 1);
									m.getWorld().spawnParticle(Particle.REDSTONE, m, 12, 0.45, 0.45, 0.45, 1.5, pa, true);
								}
								for (LivingEntity le : p.getWorld().getLivingEntities()) {
									if (le.getLocation().distance(p.getLocation()) < 5.7 && !le.getUniqueId().equals(p.getUniqueId()) && (le.getCustomName() == null || (le.getCustomName() != null && le.getCustomName().equals("§eBrent's Phantom")))) {
										le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20, 3));
										le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 210, 3));
									}
								}
							}
						}, 0, 3);
						Asteria.phantomBurstEndId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Bukkit.getScheduler().cancelTask(Asteria.phantomBurstId);
							Asteria.isPhantomBurstActive = false;
						}, 600);
						Asteria.cooldownPhantomBurst = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(600); // 10 minutes
					}
				} else if (type.equals(Material.BLUE_ICE) && displayName.equals("§bSnowstorm")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownSnowstorm, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownSnowstorm, p);
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						int k = r.nextInt(3) + 8;
						DustOptions pa = new Particle.DustOptions(Color.fromRGB(3, 186, 252), 1);
						int max = Asteria.hasAdvancement(p, "adventure/voluntary_exile") ? 3 : 1;
						double damage = Asteria.hasAdvancement(p, "husbandry/make_a_sign_glow") ? 8 : 4;
						for (int v = 0; v < max; v++) {
							Location l = p.getEyeLocation();
							if (v == 1)
								l.setYaw(l.getYaw() - 10);
							else if (v == 2)
								l.setYaw(l.getYaw() + 10);
							for (int j = 0; j < k; j++) {
								HashSet<Integer> ids = new HashSet<Integer>();
								for (int i = 0; i < 700; i++) {
									final Double ii = (double) i / 1.75;
									ids.add(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
										Location ll = l.clone();
										ll.add(ll.getDirection().multiply(ii));
										ll.getWorld().spawnParticle(Particle.REDSTONE, ll, 11, 0.3, 0.3, 0.3, 1, pa, true);
										boolean isThereEntity = false;
										for (LivingEntity le : ll.getWorld().getLivingEntities()) {
											if (!le.getUniqueId().equals(p.getUniqueId()) && (ll.distance(le.getLocation()) < 1.85 || ll.distance(le.getEyeLocation()) < 1.85)) {
												isThereEntity = true;
												le.damage(damage, p);
												le.setVelocity(ll.getDirection().multiply(1.6).setY(1));
												if (r.nextBoolean()) {
													if (le.hasPotionEffect(PotionEffectType.SLOW))
														le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, le.getPotionEffect(PotionEffectType.SLOW).getAmplifier() + 1));
													else
														le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 0));
												}
											}
										}
										if (isThereEntity) {
											for (int toCancel : ids)
												Bukkit.getScheduler().cancelTask(toCancel);
											ll.getWorld().playSound(ll, Sound.BLOCK_GLASS_BREAK, 1, 1);
										}
									}, Math.round(ii) + (j * 6)));
								}
							}
						}
						Asteria.cooldownSnowstorm = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(10); // 10 seconds
					}
				} else if (type.equals(Material.BLAZE_ROD) && displayName.equals("§6Hellstorm")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownHellstorm, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownHellstorm, p);
					else if (Asteria.isHellActive)
						p.sendMessage(ChatColor.RED + "You can't use §6Hellstorm " + ChatColor.RED + "while its active!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Asteria.isHellActive = true;
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (!a.getUniqueId().equals(p.getUniqueId()) && a.getWorld().getName().equals(p.getWorld().getName()) && a.getLocation().distance(p.getLocation()) < 20)
								a.sendMessage(ChatColor.YELLOW + p.getName() + " has activated §6Hellstorm " + ChatColor.YELLOW + "near you!");
						}
						if (Asteria.hasAdvancement(p, "adventure/shoot_arrow")) {
							Location blaze = p.getEyeLocation();
							blaze.setY(blaze.getY() + 1);
							for (int i = 0; i < 3; i++) {
								Blaze b = (Blaze) blaze.getWorld().spawnEntity(blaze, EntityType.BLAZE);
								b.setCustomName("§6Flaming Knights");
							}
						}
						long endTime = Instant.now().getEpochSecond() + 30;
						p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 0));
						double damage = Asteria.hasAdvancement(p, "husbandry/make_a_sign_glow") ? 12 : 8;
						Asteria.hellStormId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
							if (p.isOnline()) {
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eTime left for §6Hellstorm§e: " + (endTime - Instant.now().getEpochSecond())));
								Location l = p.getLocation();
								l.setY(l.getY() + 20);
								int lx = l.getBlockX();
								int lz = l.getBlockZ();
								for (int x = lx - 25; x < lx + 26; x++) {
									for (int z = lz - 26; z < lz + 26; z++) {
										Block b = l.getWorld().getBlockAt(x, (int) l.getY(), z);
										if (b.getLocation().distance(l) < 25.5)
											b.getLocation().getWorld().spawnParticle(Particle.FALLING_LAVA, b.getLocation(), 0, 0.4, 0, 0.4, 1, null, true);
									}
								}
								Bukkit.getScheduler().scheduleSyncDelayedTask(Asteria.getPlugin(Asteria.class), () -> {
									for (LivingEntity le : l.getWorld().getLivingEntities()) {
										Location entityLocation = le.getLocation();
										entityLocation.setY(l.getY());
										if (entityLocation.distance(l) < 25.5 && !le.getUniqueId().equals(p.getUniqueId()) && (le.getCustomName() == null || (le.getCustomName() != null && !le.getCustomName().equals("§6Flaming Knights")))) {
											le.damage(damage, p);
											le.setFireTicks(le.getFireTicks() + 160);
										}
									}
								}, 28);
							}
						}, 0, 8);
						Asteria.hellStormEndId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Asteria.isHellActive = false;
							Bukkit.getScheduler().cancelTask(Asteria.hellStormId);
						}, 600);
						Asteria.cooldownHellstorm = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(50); // 50 seconds
					}
				} else if (type.equals(Material.TOTEM_OF_UNDYING) && displayName.equals("§6Spectral Shift")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownSpectralShift, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownSpectralShift, p);
					else if (Asteria.invis.containsKey(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use §6Spectral Shift " + ChatColor.RED + "as you are already invisble!");
					else if (PowerUtils.isInDimension(p.getWorld().getName()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						p.sendMessage("§bYou are now invisible!");
						p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0));
						for (Player a : Bukkit.getOnlinePlayers()) {
							a.hidePlayer(plugin, p);
							if (a.getWorld().getName().equals(p.getWorld().getName()) && a.getLocation().distance(p.getLocation()) < 10 && !a.getUniqueId().equals(p.getUniqueId()))
								a.sendMessage(ChatColor.YELLOW + p.getName() + " has used §6Spectral Shift " + ChatColor.YELLOW + "near you!");
						}
						Location l = p.getEyeLocation();
						l.setY(l.getY() + 1);
						for (int i = 0; i < 5; i++) {
							Vex v = (Vex) p.getWorld().spawnEntity(l, EntityType.VEX);
							v.setCustomName("§bGhastly Knights");
						}
						Asteria.invis.put(p.getUniqueId(), Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							for (Player a : Bukkit.getOnlinePlayers())
								a.showPlayer(Asteria.getPlugin(Asteria.class), p);
							p.sendMessage("§cYour §6Spectral Shift §chas run out!");
							Asteria.invis.remove(p.getUniqueId());
						}, 600));
						Asteria.cooldownSpectralShift = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(45); // 45 seconds
					}
				} else if (type.equals(Material.ARROW) && displayName.equals("§6Conjuring Shot")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownConjuringShot, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownConjuringShot, p);
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						int max = Asteria.hasAdvancement(p, "story/enchant_item") ? 5 : 1;
						for (int i = 0; i < max; i++) {
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
								Location l = p.getEyeLocation().add(p.getEyeLocation().getDirection());
								Arrow a = (Arrow) p.getWorld().spawnEntity(l, EntityType.ARROW);
								a.setVelocity(l.getDirection().multiply(2.5));
								a.setShooter(p);
								a.setPickupStatus(PickupStatus.DISALLOWED);
								a.setTicksLived(1000);
								a.setCustomName("§6Conjuring Shot");
							}, i * 3);
						}
						Asteria.cooldownConjuringShot = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(5); // 5 seconds
					}
				} else if (type.equals(Material.GLOWSTONE) && displayName.equals("§6Divine Light")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownDivineLight, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownDivineLight, p);
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						for (LivingEntity le : p.getWorld().getLivingEntities()) {
							if (!le.getUniqueId().equals(p.getUniqueId()) && le.getLocation().distance(p.getLocation()) < 30.5) {
								le.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 600, 0));
								le.sendMessage(ChatColor.YELLOW + "You have been given the glow effect!");
							}
						}
						Location l = p.getLocation();
						int lx = l.getBlockX();
						int ly = l.getBlockY();
						int lz = l.getBlockZ();
						for (int x = lx - 31; x < lx + 31; x++) {
							for (int y = ly - 31; y < ly + 31; y++) {
								for (int z = lz - 31; z < lz + 31; z++) {
									Block b = l.getWorld().getBlockAt(x, y, z);
									if (b.getLocation().distance(p.getLocation()) < 30 && b.getLocation().distance(p.getLocation()) > 28.5 && (b.getLocation().getZ() % 3 == 0 && b.getLocation().getX() % 3 == 0 && b.getLocation().getY() % 3 == 0))
										l.getWorld().spawnParticle(Particle.FLASH, b.getLocation(), 0, 1, 1, 1, 2, null, true);
								}
							}
						}
						p.sendMessage(ChatColor.GREEN + "You have been given Regeneration and Resistance from §6Divine Light" + ChatColor.GREEN + "!");
						p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 1));
						p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 1));
						Asteria.cooldownDivineLight = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(60); // 1 minute
					}
				} else if (type.equals(Material.CLOCK) && displayName.equals("§6Time Manipulation"))
					p.openInventory(Menu.timeManipulation());
				else if (type.equals(Material.DRAGON_BREATH) && displayName.equals("§6Sorcerer's State")) {
					if (Asteria.isSorcererActive)
						p.sendMessage(ChatColor.RED + "You may not use §6Sorcerer's State §cwhile its active!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (a.getWorld().getName().equals(p.getWorld().getName()) && a.getLocation().distance(p.getLocation()) < 50 && !a.getUniqueId().equals(p.getUniqueId()))
								a.sendMessage(ChatColor.RED + p.getName() + " has activated §6Sorcerer's State §cnear you!");
						}
						Asteria.isSorcererActive = true;
						p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 1));
						p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 1200, 4));
						p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1200, 2));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 2));
						Asteria.SorcererId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Asteria.isSorcererActive = false;
							p.sendMessage(ChatColor.RED + "Your Sorcerer has run out!");
						}, 1200);
					}
				} else if (type.equals(Material.BLAZE_ROD) && displayName.equals("§6Electrokinesis")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownElectro, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownElectro, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						for (int i = 0; i < 500; i++) {
							Location l = p.getEyeLocation();
							l.add(l.getDirection().multiply((Double.valueOf(i) / 5) + 1));
							if (!l.getBlock().getType().isAir()) {
								l.getWorld().strikeLightning(l);
								for (LivingEntity le : l.getWorld().getLivingEntities()) {
									if (le.getLocation().distance(l) < 1.2 && !le.isDead())
										le.damage(12, p);
								}
								l.setY(l.getY() + 0.1);
								l = l.getBlock().getLocation();
								final Location ll = l;
								final DustOptions pa = new Particle.DustOptions(Color.fromRGB(252, 245, 28), 1);
								int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
									HashSet<LivingEntity> toHurt = new HashSet<LivingEntity>();
									for (int x = ll.getBlockX() - 5; x < ll.getBlockX() + 6; x++) {
										for (int z = ll.getBlockZ() - 5; z < ll.getBlockZ() + 6; z++) {
											Block b = ll.getWorld().getBlockAt(x, ll.getBlockY(), z);
											b.getWorld().spawnParticle(Particle.REDSTONE, b.getLocation(), 25, 0.6, 0.6, 0.6, 1, pa, true);
											for (LivingEntity le : ll.getWorld().getLivingEntities()) {
												if ((le.getLocation().distance(b.getLocation()) < 0.8 || le.getEyeLocation().distance(b.getLocation()) < 0.8) && !le.getUniqueId().equals(Asteria.eli))
													toHurt.add(le);
											}
										}
									}
									for (LivingEntity le : toHurt) {
										le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 3));
										le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 3));
									}
								}, 0, 15);
								Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.getScheduler().cancelTask(task), 600);
								Asteria.cooldownElectro = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(120); // 2 minutes
								break;
							} else if (i == 499)
								p.sendMessage(ChatColor.RED + "You weren't looking at anything!");
						}
					}
				} else if (displayName.equals("§6Excalibur")) {
					HashSet<UUID> u = new HashSet<UUID>();
					for (int i = -40; i < 41; i = i + 20) {
						Location l = p.getEyeLocation();
						l.setYaw(l.getYaw() + i);
						for (int o = 0; o < 10; o++) {
							l.add(l.getDirection());
							l.getWorld().spawnParticle(Particle.SWEEP_ATTACK, l, 0, 0, 0, 0, 1, null, true);
							for (LivingEntity en : l.getWorld().getLivingEntities()) {
								if (en.getLocation().distance(l) < 2 && !en.getUniqueId().equals(p.getUniqueId()) && !u.contains(en.getUniqueId())) {
									en.damage(10, p);
									u.add(en.getUniqueId());
								}
							}
						}
					}
				} else if (displayName.equals("§6Caliburn")) {
					HashSet<UUID> u = new HashSet<UUID>();
					for (int i = -40; i < 41; i = i + 20) {
						Location l = p.getEyeLocation();
						l.setYaw(l.getYaw() + i);
						for (int o = 0; o < 10; o++) {
							l.add(l.getDirection());
							l.getWorld().spawnParticle(Particle.FLAME, l, 10, 0.1, 0.1, 0.1, 0.1, null, true);
							for (LivingEntity en : l.getWorld().getLivingEntities()) {
								if (en.getLocation().distance(l) < 2 && !en.getUniqueId().equals(p.getUniqueId()) && !u.contains(en.getUniqueId())) {
									if (Asteria.hasAdvancement(p, "end/dragon_breath")) {
										en.setFireTicks(880);
										en.damage(14, p);
									} else
										en.setFireTicks(220);
									u.add(en.getUniqueId());
								}
							}
						}
					}
				} else if (type.equals(Material.BLACK_DYE) && displayName.equals("§bUmbrakinesis")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownUmbrakinesis, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownUmbrakinesis, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						boolean isEntity = false;
						for (LivingEntity le : p.getWorld().getLivingEntities()) {
							if (!le.getUniqueId().equals(p.getUniqueId()) && le.getLocation().distance(p.getLocation()) < 10.5) {
								isEntity = true;
								le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
								le.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0));
								le.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 0));
							}
						}
						if (isEntity) {
							Asteria.cooldownUmbrakinesis = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(60); // 1 minute
							p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 12000, 10, 10, 10, 1, new Particle.DustOptions(Color.fromRGB(30, 61, 77), 1), true);
						} else
							p.sendMessage(ChatColor.RED + "There wasn't any entities in a 10 block radius from you!");
					}
				} else if (type.equals(Material.GLOWSTONE) && displayName.equals("§bDawn of Light")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownDawnOfLight, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownDawnOfLight, p);
					else if (Asteria.isDawnOfLightActive)
						p.sendMessage(ChatColor.RED + "You may not use " + displayName + " §cwhile its active!");
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Asteria.isDawnOfLightActive = true;
						p.sendMessage(ChatColor.GREEN + "You have activated " + displayName + ChatColor.GREEN + "!");
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (!a.getUniqueId().equals(p.getUniqueId()) && a.getWorld().getName().equals(p.getWorld().getName()) && a.getLocation().distance(p.getLocation()) < 13)
								a.sendMessage(ChatColor.RED + p.getName() + " has activated " + displayName + " §cnear you!");
						}
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
						p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 200, 1));
						final DustOptions pa = new Particle.DustOptions(Color.fromRGB(255, 255, 153), 1);
						HashSet<UUID> noHurt = new HashSet<UUID>();
						long endTime = Instant.now().getEpochSecond() + 3;
						Asteria.dawnOfLightId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
							if (p.isOnline()) {
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eTime left for §bDawn of Light§e: " + (endTime - Instant.now().getEpochSecond())));
								for (int i = 0; i < 60; i++) {
									final Double ii = (double) i / 1.5;
									Location l = p.getEyeLocation();
									l.add(l.getDirection().multiply(ii + 2));
									l.getWorld().spawnParticle(Particle.REDSTONE, l, 9, 0.25, 0.25, 0.25, 1, pa, true);
									for (LivingEntity le : l.getWorld().getLivingEntities()) {
										if (!le.getUniqueId().equals(p.getUniqueId()) && (l.distance(le.getLocation()) < 1.85 || l.distance(le.getEyeLocation()) < 1.85) && !noHurt.contains(le.getUniqueId())) {
											noHurt.add(le.getUniqueId());
											le.damage(6, p);
											le.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 600, 0));
											Bukkit.getScheduler().scheduleSyncDelayedTask(Asteria.getPlugin(Asteria.class), () -> noHurt.remove(le.getUniqueId()), 10);
										}
									}
								}
							}
						}, 0, 1);
						Asteria.dawnOfLightEndId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Bukkit.getScheduler().cancelTask(Asteria.dawnOfLightId);
							Asteria.isDawnOfLightActive = false;
							p.sendMessage(ChatColor.RED + "Your " + displayName + " §cran out!");
						}, 60);
						Asteria.cooldownDawnOfLight = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(180); // 3 minutes
					}
				} else if (type.equals(Material.DIAMOND) && displayName.equals("§bShape Shift")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownShapeShift, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownShapeShift, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else if (Asteria.isShapeShiftActive)
						p.sendMessage(ChatColor.RED + "You may not use " + displayName + " §cwhile its active!");
					else {
						Asteria.isShapeShiftActive = true;
						long endTime = Instant.now().getEpochSecond() + 20;
						int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
							if (p.isOnline())
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eTime left for " + displayName + "§e: " + (endTime - Instant.now().getEpochSecond())));
						}, 0, 4);
						p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 400, 4));
						p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 400, 3));
						p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 2));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 0));
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Bukkit.getScheduler().cancelTask(task);
							Asteria.isShapeShiftActive = false;
						}, 400);
						Asteria.cooldownShapeShift = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(240); // 4 minutes
					}
				} else if (type.equals(Material.NETHERITE_SWORD) && displayName.equals("§bBlades of Exile") && p.isSneaking()) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownBladesofExile, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownBladesofExile, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Location l = p.getLocation();
						l.setY(l.getY() + 1.1);
						DustOptions pa = new Particle.DustOptions(Color.fromRGB(67, 140, 171), 1);
						HashSet<Integer> tasks = new HashSet<Integer>();
						l.getWorld().spawnParticle(Particle.SMOKE_NORMAL, l, 100, 0.2, 0.2, 0.2, 0.1, null, true);
						for (int i = 0; i < 110; i++) {
							final int ii = i;
							tasks.add(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
								l.add(l.getDirection().multiply(0.1));
								Location ll = l.clone();
								ll.setY(ll.getY() - 0.7);
								p.teleport(ll);
								if (PowerUtils.getNearbyEntities(l, 1.2, p.getUniqueId()).size() > 0 || l.getBlock().getType().isSolid()) {
									for (Integer id : tasks)
										Bukkit.getScheduler().cancelTask(id);
									for (LivingEntity le : PowerUtils.getNearbyEntities(l, 1.35, p.getUniqueId())) {
										le.damage(14, p);
										le.setVelocity(l.getDirection().multiply(1.6).setY(0.8));
									}
									l.getWorld().spawnParticle(Particle.REDSTONE, l, 400, 0.3, 0.3, 0.3, 1, pa, true);
								} else if (ii == 109)
									l.getWorld().spawnParticle(Particle.REDSTONE, l, 400, 0.3, 0.3, 0.3, 1, pa, true);
							}, i / 19));
						}
						Asteria.cooldownBladesofExile = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(180); // 3 minutes
					}
				} else if (type.equals(Material.NETHERITE_AXE) && displayName.equals("§bLeviathan Axe") && p.isSneaking()) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownLeviathanAxe, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownLeviathanAxe, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						List<LivingEntity> near = PowerUtils.getNearbyEntities(p.getLocation(), 10.5, p.getUniqueId());
						if (near.size() == 0)
							p.sendMessage(ChatColor.RED + "There were no entities in a 10 block radius from you!");
						else {
							for (LivingEntity le : near) {
								le.sendMessage(ChatColor.RED + p.getName() + " has used " + displayName + ChatColor.RED + " on you! Your health has been cut in half!");
								le.damage(0, p);
								le.setHealth(le.getHealth() / 2);
								le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 3));
								le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 3));
							}
							DustOptions pa = new Particle.DustOptions(Color.fromRGB(93, 252, 199), 1);
							p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 12000, 8, 8, 8, pa);
							Asteria.cooldownLeviathanAxe = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(300); // 5 minutes
						}
					}
				} else if (type.equals(Material.GOLD_NUGGET) && displayName.equals("§6Magic Seal")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownMagicSeal, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownMagicSeal, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (Asteria.isMagicSealActive)
						p.sendMessage(ChatColor.RED + "You may not use " + displayName + "§c while it's active!");
					else {
						Asteria.isMagicSealActive = true;
						long endTime = Instant.now().getEpochSecond() + 30;
						int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
							if (p.isOnline())
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eTime left for " + displayName + "§e: " + (endTime - Instant.now().getEpochSecond())));
						}, 0, 3);
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Bukkit.getScheduler().cancelTask(task);
							Asteria.isMagicSealActive = false;
							p.sendMessage(displayName + ChatColor.RED + " has run out!");
							Asteria.playerOnMagicSeal = null;
						}, 600);
						p.damage(4);
						Asteria.cooldownMagicSeal = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(60); // 1 minute
					}
				} else if (type.equals(Material.GOLD_INGOT) && displayName.equals("§bAlmighty Push")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownAlmightlyPush, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownAlmightlyPush, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else {
						boolean isBlock = false;
						for (int i = 0; i < 70; i++) {
							Location l = p.getEyeLocation();
							l.add(l.getDirection().multiply(i + 1));
							l.getWorld().spawnParticle(Particle.CRIMSON_SPORE, l, 2);
							if (!l.getBlock().getType().isAir()) {
								p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 160, 3));
								String s = PowerUtils.getCDirection(l.getYaw());
								isBlock = true;
								int lx = l.getBlockX();
								int ly = l.getBlockY();
								int lz = l.getBlockZ();
								if (s == "N") {
									for (int it = 0; it < 50; it++) {
										final int lzz = lz - it;
										Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
											for (int x = lx - 4; x < lx + 4; x++) {
												for (int y = ly - 0; y < ly + 3; y++)
													l.getWorld().createExplosion(l.getWorld().getBlockAt(x, y, lzz).getLocation(), 3, false, true, p);
											}
											Location ll = l.getWorld().getBlockAt(lx, ly, lzz).getLocation();
											for (Player a : Bukkit.getOnlinePlayers()) {
												if (a.getWorld().equals(ll.getWorld()) && a.getLocation().distance(ll) < 5 && !a.getUniqueId().equals(p.getUniqueId())) {
													a.damage(12, p);
													a.setVelocity(a.getLocation().toVector().subtract(ll.toVector()).multiply(4).setY(2));
												}
											}
										}, it * 2);
									}
								}
								if (s == "S") {
									for (int it = 0; it < 50; it++) {
										final int lzz = lz + it;
										Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
											for (int x = lx - 4; x < lx + 4; x++) {
												for (int y = ly - 0; y < ly + 3; y++)
													l.getWorld().createExplosion(l.getWorld().getBlockAt(x, y, lzz).getLocation(), 3, false, true, p);
											}
											Location ll = l.getWorld().getBlockAt(lx, ly, lzz).getLocation();
											for (Player a : Bukkit.getOnlinePlayers()) {
												if (a.getWorld().equals(ll.getWorld()) && a.getLocation().distance(ll) < 5 && !a.getUniqueId().equals(p.getUniqueId())) {
													a.damage(12, p);
													a.setVelocity(a.getLocation().toVector().subtract(ll.toVector()).multiply(4).setY(2));
												}
											}
										}, it * 2);
									}
								}
								if (s == "E") {
									for (int it = 0; it < 50; it++) {
										final int lxx = lx + it;
										Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
											for (int z = lz - 4; z < lz + 4; z++) {
												for (int y = ly - 0; y < ly + 3; y++)
													l.getWorld().createExplosion(l.getWorld().getBlockAt(lxx, y, z).getLocation(), 3, false, true, p);
											}
											Location ll = l.getWorld().getBlockAt(lxx, ly, lz).getLocation();
											for (Player a : Bukkit.getOnlinePlayers()) {
												if (a.getWorld().equals(ll.getWorld()) && a.getLocation().distance(ll) < 5 && !a.getUniqueId().equals(p.getUniqueId())) {
													a.damage(12, p);
													a.setVelocity(a.getLocation().toVector().subtract(ll.toVector()).multiply(4).setY(2));
												}
											}
										}, it * 2);
									}
								}
								if (s == "W") {
									for (int it = 0; it < 40; it++) {
										final int lxx = lx - it;
										Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
											for (int z = lz - 4; z < lz + 4; z++) {
												for (int y = ly - 0; y < ly + 3; y++)
													l.getWorld().createExplosion(l.getWorld().getBlockAt(lxx, y, z).getLocation(), 3, false, true, p);
											}
											Location ll = l.getWorld().getBlockAt(lxx, ly, lz).getLocation();
											for (Player a : Bukkit.getOnlinePlayers()) {
												if (a.getWorld().equals(ll.getWorld()) && a.getLocation().distance(ll) < 5 && !a.getUniqueId().equals(p.getUniqueId())) {
													a.damage(12, p);
													a.setVelocity(a.getLocation().toVector().subtract(ll.toVector()).multiply(4).setY(2));
												}
											}
										}, it * 2);
									}
								}
								break;
							}
						}
						if (isBlock)
							Asteria.cooldownAlmightlyPush = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(180); // 3 minutes
						else
							p.sendMessage(ChatColor.RED + "You were not looking at a block (or it was too far away)!");
					}
				} else if (type.equals(Material.GOLD_BLOCK) && displayName.equals("§bFull Counter")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownFullCounter, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownFullCounter, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (Asteria.isFullCounterActive)
						p.sendMessage(ChatColor.RED + "You may not use " + displayName + ChatColor.RED + " while it is active!");
					else {
						Asteria.isFullCounterActive = true;
						long endTime = Instant.now().getEpochSecond() + 20;
						Asteria.fullCounterId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
							if (p.isOnline())
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eTime left for " + displayName + "§e: " + (endTime - Instant.now().getEpochSecond()) + " | §eFull Counter Damage: " + String.valueOf(new DecimalFormat("#.##").format(Asteria.fullCounterDamage))));
						}, 0, 2);
						Asteria.fullCounterEndId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Asteria.isFullCounterActive = false;
							Asteria.isFinalFullCounterActive = true;
							p.sendMessage(ChatColor.AQUA + "You now have 10 seconds to damage someone to deal damage with " + displayName + ChatColor.AQUA + "!");
						}, 200);
						Asteria.fullCounterFinalEndId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Bukkit.getScheduler().cancelTask(Asteria.fullCounterId);
							Asteria.isFinalFullCounterActive = false;
							Asteria.shotsSinceFullCounter = 0;
							Asteria.fullCounterDamage = 0;
							p.sendMessage(ChatColor.RED + displayName + ChatColor.RED + " has run out!");
						}, 400);
						Asteria.cooldownFullCounter = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(20); // 20 seconds
					}
				} else if (type.equals(Material.YELLOW_STAINED_GLASS_PANE) && displayName.equals("§bThe End Gates")) {
					if (Asteria.isEndGatesActive)
						p.sendMessage(ChatColor.RED + "You can't use this while its active, try left clicking it!");
					else if (PowerUtils.isOnCooldownNew(Asteria.cooldownEndGates, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownEndGates, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else {
						p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 58, 3));
						Asteria.isEndGatesActive = true;
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							if (p.isOnline()) {
								p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 1));
								for (Player a : Bukkit.getOnlinePlayers()) {
									if (!a.getUniqueId().equals(p.getUniqueId()) && a.getWorld().getName().equals(p.getWorld().getName()) && a.getLocation().distance(p.getLocation()) < 17)
										a.sendMessage(ChatColor.RED + p.getName() + " has activated " + displayName + ChatColor.RED + " near you!");
								}
								if (!p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid())
									p.getLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.GLASS);
								Location l = p.getEyeLocation();
								long endTime = Instant.now().getEpochSecond() + 60;
								ArrayList<Location> locations = new ArrayList<Location>();
								DustOptions pa = new Particle.DustOptions(Color.fromRGB(155, 17, 209), 1);
								for (double u = 0.5; u < 10; u = u + 0.65) {
									final double uu = u;
									Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
										ArrayList<Location> pickRandom = new ArrayList<Location>();
										for (int o = -1; o < 2; o = o + 2) {
											for (int i = -90; i < 15; i++) {
												Location ll = l.clone();
												ll.setYaw(ll.getYaw() + (o * 90));
												ll.setPitch(i);
												ll.add(ll.getDirection().multiply(uu));
												ll.getWorld().spawnParticle(Particle.REDSTONE, ll, 18, 0.4, 0.4, 0.4, 1, pa, true);
												pickRandom.add(ll);
											}
										}
										if (uu > 1.7) {
											for (int y = 0; y < Math.round(uu); y++) {
												int random = r.nextInt(pickRandom.size());
												locations.add(pickRandom.get(random));
												pickRandom.remove(random);
											}
										}
									}, Math.round(u * 2.2));
								}
								int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
									for (Location loc : locations)
										loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 180, 0.2, 0.2, 0.2, 1, pa, true);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eTime left for " + displayName + "§e: " + (endTime - Instant.now().getEpochSecond())));
								}, 2, 6);
								Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
									p.sendMessage(ChatColor.GREEN + "You may now start shooting " + displayName + ChatColor.GREEN + "!");
									Asteria.isEndGatesFullyActive = true;
									Asteria.endGateLocations = locations;
								}, 22);
								Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
									Bukkit.getScheduler().cancelTask(task);
									Asteria.isEndGatesActive = false;
									Asteria.isEndGatesFullyActive = false;
								}, 1200);
							}
						}, 58);
						Asteria.cooldownEndGates = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(480); // 8 minutes
					}
				} else if (displayName.equals("§bStygius")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownStygius, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownStygius, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						HashSet<UUID> u = new HashSet<UUID>();
						boolean hasBlazeRod = Asteria.hasAdvancement(p, "nether/obtain_blaze_rod");
						double damage = hasBlazeRod ? 10 : 8;
						long cooldown = 2;
						if (Asteria.isBerserkerActive) {
							damage = 12;
							cooldown = 10;
						}
						int max = hasBlazeRod ? 6 : 4;
						for (int i = -180; i < 180; i = i + 20) {
							Location l = p.getEyeLocation();
							l.setYaw(l.getYaw() + i);
							for (int o = 0; o < max * Asteria.hanyPowerMultiplier; o++) {
								l.add(l.getDirection());
								l.getWorld().spawnParticle(Particle.SWEEP_ATTACK, l, 0, 0, 0, 0, 1, null, true);
								for (LivingEntity en : l.getWorld().getLivingEntities()) {
									if (en.getLocation().distance(l) < 2 && !en.getUniqueId().equals(p.getUniqueId()) && !u.contains(en.getUniqueId())) {
										en.damage(damage * Asteria.hanyPowerMultiplier, p);
										u.add(en.getUniqueId());
									}
								}
							}
						}
						Asteria.cooldownStygius = Instant.now().getEpochSecond() + PowerUtils.handleCooldown((long) (cooldown / Asteria.hanyPowerMultiplier)); // 5 or
						// 20
						// seconds
					}
				} else if (type.equals(Material.REDSTONE_BLOCK) && displayName.equals("§bHostile Convergence")) {
					if (Asteria.isHostileConvergenceActive) {
						p.sendMessage(ChatColor.BLUE + "You have disabled " + displayName + ChatColor.BLUE + "!");
						Asteria.isHostileConvergenceActive = false;
						Asteria.hostileConvergencePlayers.clear();
					} else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Asteria.isHostileConvergenceActive = true;
						p.sendMessage(ChatColor.GREEN + "You have enabled " + displayName + ChatColor.GREEN + "!");
					}
				} else if (type.equals(Material.NETHERITE_INGOT) && displayName.equals("§bFortuna Flip")) {
					if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else if (Asteria.hanyPowerMultiplier != 1)
						p.sendMessage(ChatColor.RED + "You already have a multiplier!");
					else {
						boolean higher = r.nextInt(10) < Asteria.chanceForFortune;
						long endTime = Instant.now().getEpochSecond() + 45;
						Asteria.fortuneTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eTime left for " + displayName + "§e: " + (endTime - Instant.now().getEpochSecond()))), 0, 16);
						if (higher) {
							Asteria.chanceForFortune--;
							p.sendMessage(ChatColor.GREEN + "You got good fortune! Everything is now multiplied by 2");
							Asteria.hanyPowerMultiplier = 2;
							p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Asteria.isBerserkerActive ? 24 : 40);
						} else {
							Asteria.chanceForFortune++;
							p.sendMessage(ChatColor.RED + "You got bad fortune! Everything is now multiplied by 1/2");
							Asteria.hanyPowerMultiplier = 0.5;
							p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Asteria.isBerserkerActive ? 6 : 10);
						}
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Player player = Bukkit.getPlayer(p.getUniqueId());
							if (player != null) {
								player.sendMessage(ChatColor.AQUA + "Your fortune has run out!");
								player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Asteria.isBerserkerActive ? 12 : 20);
							}
							Asteria.hanyPowerMultiplier = 1;
							Bukkit.getScheduler().cancelTask(Asteria.fortuneTask);
						}, 900);
					}
				}
			} else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				if (type.equals(Material.GRAY_GLAZED_TERRACOTTA) && displayName.equals("§bDimensional Shift") && Asteria.invis.containsKey(p.getUniqueId())) {
					e.setCancelled(true);
					p.sendMessage("§aYou have canceled your Dimensional Shift!");
					p.removePotionEffect(PotionEffectType.BLINDNESS);
					p.removePotionEffect(PotionEffectType.SPEED);
					p.removePotionEffect(PotionEffectType.INVISIBILITY);
					Bukkit.getScheduler().cancelTask(Asteria.invis.get(p.getUniqueId()));
					Asteria.invis.remove(p.getUniqueId());
					for (Player a : Bukkit.getOnlinePlayers())
						a.showPlayer(Asteria.getPlugin(Asteria.class), p);
				} else if (type.equals(Material.WITHER_SKELETON_SKULL) && displayName.equals("§6Soul's of the Damned")) {
					if (Asteria.cancelSpawning)
						Asteria.cancelSpawning = false;
					else {
						ArrayList<Double> de = new ArrayList<Double>();
						for (World wo : Bukkit.getWorlds()) {
							for (LivingEntity le : wo.getLivingEntities()) {
								if (le.getType().equals(EntityType.WITHER_SKELETON) && le.hasMetadata("fromPower")) {
									final Double d = le.getHealth();
									Asteria.witherSkeletonQueue.add(d);
									le.remove();
									de.add(d);
								}
							}
						}
						for (Double dee : de) {
							ArrayList<Integer> temp = new ArrayList<Integer>();
							temp.add(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
								Asteria.witherSkeletonQueue.remove(dee);
								Asteria.witherSkeletonQueueId.remove(temp.get(0));
							}, de.size() * 100));
							Asteria.witherSkeletonQueueId.add(temp.get(0));
						}
						if (de.size() == 0)
							p.sendMessage(ChatColor.GREEN + "You have desummoned all of your wither skeletons!");
					}
				} else if (type.equals(Material.NETHER_STAR) && displayName.equals("§6Royal Guard")) {
					if (Asteria.cancelSpawning)
						Asteria.cancelSpawning = false;
					else {
						ArrayList<Double> de = new ArrayList<Double>();
						for (World wo : Bukkit.getWorlds()) {
							for (LivingEntity le : wo.getLivingEntities()) {
								if (le.getType().equals(EntityType.WITHER) && le.getCustomName() != null && le.getCustomName().equals("§6§lRoyal Guard")) {
									final Double d = le.getHealth();
									Asteria.witherQueue.add(d);
									le.remove();
									de.add(d);
								}
							}
						}
						for (Double dee : de) {
							ArrayList<Integer> temp = new ArrayList<Integer>();
							temp.add(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
								Asteria.witherQueue.remove(dee);
								Asteria.witherQueueId.remove(temp.get(0));
							}, de.size() * 600));
							Asteria.witherQueueId.add(temp.get(0));
						}
						if (de.size() == 0)
							p.sendMessage(ChatColor.GREEN + "You have desummoned all of your Royal Guards!");
					}
				} else if (type.equals(Material.WITHER_ROSE) && displayName.equals("§6Wither Turret / Dash")) {
					if (Asteria.cancelSpawning)
						Asteria.cancelSpawning = false;
					else if (PowerUtils.isOnCooldown(Asteria.cooldownWither, p))
						PowerUtils.sendCDMsg(Asteria.cooldownWither, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						if (!Asteria.witherCooldownCount.containsKey(p.getUniqueId()))
							Asteria.witherCooldownCount.put(p.getUniqueId(), (byte) 0);
						if (!Asteria.isOneShotWither) {
							for (int i = 0; i < 40 * Asteria.hanyPowerMultiplier; i++) {
								Location l = p.getEyeLocation().add(p.getLocation().getDirection().multiply(i * 0.4));
								WitherSkull sk = (WitherSkull) p.getWorld().spawnEntity(l, EntityType.WITHER_SKULL);
								sk.setShooter(p);
								sk.setMetadata("fromPower", new FixedMetadataValue(Asteria.getPlugin(Asteria.class), "yes!"));
								sk.setVelocity(p.getLocation().getDirection().multiply(0.1));
								if (p.getHealth() < 10)
									sk.setCharged(true);
								Bukkit.getScheduler().scheduleSyncDelayedTask(Asteria.getPlugin(Asteria.class), () -> sk.remove(), 160);
							}
						} else {
							Location l = p.getEyeLocation().add(p.getLocation().getDirection().multiply(2));
							WitherSkull sk = (WitherSkull) p.getWorld().spawnEntity(l, EntityType.WITHER_SKULL);
							sk.setShooter(p);
							sk.setMetadata("fromPower1", new FixedMetadataValue(Asteria.getPlugin(Asteria.class), "yes!"));
							sk.setVelocity(p.getLocation().getDirection().multiply(0.1));
							if (p.getHealth() < 10)
								sk.setCharged(true);
							Bukkit.getScheduler().scheduleSyncDelayedTask(Asteria.getPlugin(Asteria.class), () -> sk.remove(), 160);
						}
						Asteria.witherCooldownCount.put(p.getUniqueId(), (byte) (Asteria.witherCooldownCount.get(p.getUniqueId()) + 1));
						if ((Asteria.witherCooldownCount.get(p.getUniqueId()) > 2 * Asteria.hanyPowerMultiplier)) {
							Asteria.witherCooldownCount.remove(p.getUniqueId());
							PowerUtils.addCooldown(Asteria.cooldownWither, p, (long) (60 / Asteria.hanyPowerMultiplier));
						} else {
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
								if (!PowerUtils.isOnCooldown(Asteria.cooldownWither, p) && Asteria.witherCooldownCount.containsKey(p.getUniqueId())) {
									Asteria.witherCooldownCount.remove(p.getUniqueId());
								}
							}, 400);
						}
					}
				} else if (type.equals(Material.RABBIT_FOOT) && displayName.equals("§6Alert / Double Jump")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownAlert, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownAlert, p);
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else if (!Asteria.hasAdvancement(p, "story/shiny_gear"))
						p.sendMessage(ChatColor.RED + "You must unlock Cover me With Diamonds!");
					else {
						boolean isTherePlayer = false;
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (a.getWorld().getName().equals(p.getWorld().getName()) && a.getLocation().distance(p.getLocation()) < 30 && !a.getUniqueId().equals(p.getUniqueId())) {
								a.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0));
								a.sendMessage(ChatColor.YELLOW + "You have been given the glowing effect!");
								isTherePlayer = true;
							}
						}
						if (isTherePlayer) {
							Location l = p.getLocation();
							int lx = l.getBlockX();
							int ly = l.getBlockY();
							int lz = l.getBlockZ();
							for (int x = lx - 31; x < lx + 31; x++) {
								for (int y = ly - 31; y < ly + 31; y++) {
									for (int z = lz - 31; z < lz + 31; z++) {
										Block b = l.getWorld().getBlockAt(x, y, z);
										if (b.getLocation().distance(p.getLocation()) < 30 && (b.getLocation().getZ() % 5 == 0 && b.getLocation().getX() % 5 == 0 && b.getLocation().getY() % 5 == 0))
											l.getWorld().spawnParticle(Particle.FLASH, b.getLocation(), 0, 2, 2, 2, 1, null, true);
									}
								}
							}
							p.sendMessage(ChatColor.GREEN + "You have given everyone in a 30 block radius the glowing effect!");
							Asteria.cooldownAlert = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(90);
						} else
							p.sendMessage(ChatColor.RED + "There was no one in a 30 block radius for you to give the glowing effect!");
					}
				} else if (type.equals(Material.SUNFLOWER) && displayName.equals("§6Withered Allegiance")) {
					if (Asteria.cancelSpawning)
						Asteria.cancelSpawning = false;
					else
						p.openInventory(Menu.mobMenu());
				} else if (type.equals(Material.YELLOW_STAINED_GLASS_PANE) && displayName.equals("§bThe End Gates")) {
					if (Asteria.isEndGatesActive && !Asteria.isEndGatesFullyActive)
						p.sendMessage(ChatColor.RED + "You must wait until " + displayName + ChatColor.RED + " is fully active!");
					else if (Asteria.isEndGatesFullyActive) {
						for (int i = 1; i < 50; i++) {
							Location l = p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(i));
							if (!l.getBlock().getType().isAir() || i == 49) {
								Location particle = Asteria.endGateLocations.get(r.nextInt(Asteria.endGateLocations.size()));
								Vector fire = l.toVector().subtract(particle.toVector()).normalize().multiply(2.5);
								Fireball df = (Fireball) particle.getWorld().spawnEntity(particle, EntityType.FIREBALL);
								df.setVelocity(fire);
								df.setDirection(fire);
								df.setShooter(p);
								df.setYield(4);
								l.getWorld().spawnParticle(Particle.REDSTONE, l, 230, 0.7, 0.7, 0.7, 1, new Particle.DustOptions(Color.fromRGB(219, 15, 36), 1), true);
								break;
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		String msg = e.getMessage().toLowerCase();
		if (msg.contains("nigger") || msg.contains("nigga") || msg.contains("n igga") || msg.contains("n igger") || msg.contains("ni gga") || msg.contains("ni gger") || msg.contains("nig ga") || msg.contains("nig ger") || msg.contains("nigg a") || msg.contains("nigg er") || msg.contains("nigge r")) {
			e.setCancelled(true);
			Bukkit.getScheduler().runTask(Asteria.getPlugin(Asteria.class), () -> e.getPlayer().kickPlayer("Word usage"));
			return;
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		String[] args = e.getMessage().split(" ");
		if (!Arrays.stream(allowedcmds).anyMatch(args[0]::equalsIgnoreCase) && !e.getPlayer().isOp()) {
			e.setCancelled(true);
			e.getPlayer().sendMessage("Unknown command. Type \"/help\" for help.");
		}
		if (args[0].equalsIgnoreCase("/help")) {
			e.setCancelled(true);
			e.getPlayer().sendMessage("§b--------- §rHelp §b---------\n§6/msg §r- Message somebody\n§6/w §r- Alias of /msg\n§6/tell §r- Alias of /msg\n§6/r §r- Reply to somebody\n§6/shrug §r- Shrugs for you\n§6/help §r- Bring up this menu");
		}
		if (Arrays.stream(messagecmds).anyMatch(args[0]::equalsIgnoreCase)) {
			if (args.length > 1) {
				Asteria.reply.put(e.getPlayer().getName(), args[1]);
				if (Bukkit.getPlayer(args[1]) != null) {
					Asteria.reply.put(Bukkit.getPlayer(args[1]).getName(), e.getPlayer().getName());
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerCommandSend(PlayerCommandSendEvent e) {
		if (!e.getPlayer().isOp()) {
			e.getCommands().clear();
			for (String s : allowedcmds)
				e.getCommands().add(s.substring(1));
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (Asteria.powerNames.contains(e.getItemDrop().getItemStack().getItemMeta().getDisplayName())) {
			e.setCancelled(true);
			if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("§6Withered Allegiance")) {
				Asteria.cancelSpawning = true;
				if (Asteria.whatMobHanySelected.equals("zombie")) {
					Asteria.whatMobHanySelected = "skeleton";
					Asteria.mobHanySelected = EntityType.SKELETON;
					p.sendMessage("§2You have changed your mob type to §aSkeleton");
				} else if (Asteria.whatMobHanySelected.equals("skeleton")) {
					Asteria.whatMobHanySelected = "piglin";
					Asteria.mobHanySelected = EntityType.ZOMBIFIED_PIGLIN;
					p.sendMessage("§2You have changed your mob type to §aPiglin");
				} else if (Asteria.whatMobHanySelected.equals("piglin")) {
					Asteria.whatMobHanySelected = "horse";
					Asteria.mobHanySelected = EntityType.ZOMBIE_HORSE;
					p.sendMessage("§2You have changed your mob type to §aHorse");
				} else if (Asteria.whatMobHanySelected.equals("horse")) {
					Asteria.whatMobHanySelected = "phantom";
					Asteria.mobHanySelected = EntityType.PHANTOM;
					p.sendMessage("§2You have changed your mob type to §aPhantom");
				} else {
					Asteria.whatMobHanySelected = "zombie";
					Asteria.mobHanySelected = EntityType.ZOMBIE;
					p.sendMessage("§2You have changed your mob type to §aZombie");
				}
			} else if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("§6Royal Guard") || e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("§bFortuna Flip")) {
				Asteria.cancelSpawning = true;
				if (Asteria.canWitherTeleportToHany) {
					Asteria.canWitherTeleportToHany = false;
					p.sendMessage(ChatColor.RED + "Your Royal Guards and Withered Knights wont teleport to you anymore!");
				} else {
					Asteria.canWitherTeleportToHany = true;
					p.sendMessage(ChatColor.GREEN + "Your Royal Guards and Withered Knights will teleport to you now!");
				}
			} else if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("§6Soul's of the Damned")) {
				Asteria.cancelSpawning = true;
				if (Asteria.shootWitherSkulls) {
					Asteria.shootWitherSkulls = false;
					p.sendMessage(ChatColor.RED + "You will no longer shoot wither skulls!");
				} else {
					Asteria.shootWitherSkulls = true;
					p.sendMessage(ChatColor.GREEN + "You will now shoot wither skulls!");
				}
			} else if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("§6Wither Turret / Dash")) {
				Asteria.cancelSpawning = true;
				if (Asteria.isOneShotWither) {
					Asteria.isOneShotWither = false;
					p.sendMessage("§2You now shoot bursts of wither skulls!");
				} else if (!Asteria.isBerserkerActive && Asteria.hasAdvancement(p, "nether/obtain_blaze_rod")) {
					Asteria.isOneShotWither = true;
					p.sendMessage("§2You now shoot a single wither skull!");
				}
			} else if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("§bStygius") && Asteria.hasAdvancement(p, "nether/summon_wither")) {
				String powerName = "§bBerserker";
				if (Asteria.isBerserkerActive) {
					p.sendMessage(ChatColor.RED + "You have disabled " + powerName + ChatColor.RED + "!");
					Bukkit.getScheduler().cancelTask(Asteria.berserkerEndId);
					PowerUtils.endBerserker();
				} else if (PowerUtils.isOnCooldownNew(Asteria.cooldownBerserker, p))
					PowerUtils.sendCDMsgNew(Asteria.cooldownBerserker, p);
				else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
					p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
				else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
					p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
				else {
					for (Player a : Bukkit.getOnlinePlayers()) {
						if (!a.getUniqueId().equals(p.getUniqueId()) && a.getWorld().getName().equals(p.getWorld().getName()) && a.getLocation().distance(p.getLocation()) < 12)
							a.sendMessage(ChatColor.RED + p.getName() + " has activated " + powerName + ChatColor.RED + " near you!");
					}
					p.sendMessage(ChatColor.GREEN + "You have activated " + powerName + ChatColor.GREEN + "!");
					long endTime = (long) (Instant.now().getEpochSecond() + (90 * Asteria.hanyPowerMultiplier));
					Asteria.isBerserkerActive = true;
					p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(12 * Asteria.hanyPowerMultiplier);
					p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) (1800 * Asteria.hanyPowerMultiplier), (int) (2 * Asteria.hanyPowerMultiplier)));
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int) (1800 * Asteria.hanyPowerMultiplier), (int) (1 * Asteria.hanyPowerMultiplier)));
					p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (1800 * Asteria.hanyPowerMultiplier), (int) (1 * Asteria.hanyPowerMultiplier)));
					p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(5);
					p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(8 * Asteria.hanyPowerMultiplier);
					p.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(20 * Asteria.hanyPowerMultiplier);
					Asteria.isOneShotWither = false;
					Asteria.berserkerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eTime left for " + powerName + "§e: " + (endTime - Instant.now().getEpochSecond())));
					}, 0, 15);
					Asteria.berserkerEndId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> PowerUtils.endBerserker(), (long) (1800 * Asteria.hanyPowerMultiplier));
					p.damage(12);
					Asteria.cooldownBerserker = Instant.now().getEpochSecond() + PowerUtils.handleCooldown((long) (120 / Asteria.hanyPowerMultiplier)); // 2 minutes
				}
			} else
				p.sendMessage("§cYou may not drop your power!");
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (Asteria.wither.containsKey(p.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(Asteria.wither.get(p.getUniqueId()));
			Asteria.wither.remove(p.getUniqueId());
		}
		if (Asteria.invis.containsKey(p.getUniqueId())) {
			p.sendMessage("§cYour invisibility has been canceled!");
			Bukkit.getScheduler().cancelTask(Asteria.invis.get(p.getUniqueId()));
			Asteria.invis.remove(p.getUniqueId());
			for (Player a : Bukkit.getOnlinePlayers())
				a.showPlayer(plugin, p);
		}
		ArrayList<ItemStack> removePower = new ArrayList<ItemStack>();
		for (ItemStack is : e.getDrops()) {
			if (Asteria.powerNames.contains(is.getItemMeta().getDisplayName()))
				removePower.add(is);
		}
		for (ItemStack rP : removePower)
			e.getDrops().remove(rP);
		ArrayList<ItemStack> removeHalf = new ArrayList<ItemStack>();
		for (ItemStack is : e.getDrops()) {
			if (r.nextInt(4) != 0) {
				if (!Asteria.deathItems.containsKey(p.getUniqueId()))
					Asteria.deathItems.put(p.getUniqueId(), new ArrayList<ItemStack>());
				Asteria.deathItems.get(p.getUniqueId()).add(is);
				removeHalf.add(is);
			} else if (Asteria.tenthdimensionLocations.containsKey(p.getUniqueId())) {
				Asteria.tenthDimensionItems.add(is);
				removeHalf.add(is);
			}
		}
		for (ItemStack rH : removeHalf)
			e.getDrops().remove(rH);
		if (Asteria.inFightWithHany.contains(p.getUniqueId()))
			Asteria.inFightWithHany.remove(p.getUniqueId());
		if (p.getUniqueId().equals(Asteria.ed) && Asteria.isSpatialBlockActive) {
			Asteria.isSpatialBlockActive = false;
			p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
			Asteria.canSpatialBlockPlace = false;
			if (Asteria.lastSpatialBlock != null) {
				Asteria.lastSpatialBlock.getBlock().setType(Material.AIR);
				Asteria.lastSpatialBlock = null;
			}
		}
		if (Asteria.tenthdimensionLocations.containsKey(p.getUniqueId())) {
			if (p.getUniqueId().equals(Asteria.ed)) {
				for (Player a : Bukkit.getOnlinePlayers()) {
					if (a.getWorld().getName().equals("10thdimension"))
						a.sendMessage("§4§l" + p.getName() + " has died! You will be warped back!");
				}
				Bukkit.getScheduler().cancelTask(Asteria.dimensionId);
				for (Integer id : Asteria.dimensionIds)
					Bukkit.getScheduler().cancelTask(id);
				Asteria.dimensionIds.clear();
				for (Entry<UUID, Location> u : Asteria.tenthdimensionLocations.entrySet()) {
					if (Bukkit.getPlayer(u.getKey()) != null) {
						Bukkit.getPlayer(u.getKey()).teleport(u.getValue());
						Bukkit.getPlayer(u.getKey()).sendMessage(ChatColor.GREEN + "You have been warped back from the 10th Dimension");
					}
				}
				p.sendMessage(ChatColor.RED + "You have died! All of the items have been dropped on the floor where you warped everyone");
				for (ItemStack is : Asteria.tenthDimensionItems)
					Asteria.tenthdimensionLocations.get(p.getUniqueId()).getWorld().dropItem(Asteria.tenthdimensionLocations.get(p.getUniqueId()), is);
				Asteria.tenthDimensionItems.clear();
				Asteria.tenthdimensionLocations.clear();
			} else {
				if (Asteria.tenthdimensionLocations.size() > 2) {
					for (Player a : Bukkit.getOnlinePlayers()) {
						if (a.getWorld().getName().equals("10thdimension"))
							a.sendMessage("§4§l" + p.getName() + " has died! The timer has been extended by 8 seconds!");
					}
					Asteria.tenthdimensionLocations.remove(p.getUniqueId());
					Bukkit.getScheduler().cancelTask(Asteria.dimensionId);
					Asteria.whenStopDimensional = Asteria.whenStopDimensional + 8;
					Asteria.dimensionId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
						for (Integer id : Asteria.dimensionIds)
							Bukkit.getScheduler().cancelTask(id);
						Asteria.dimensionIds.clear();
						for (Entry<UUID, Location> u : Asteria.tenthdimensionLocations.entrySet()) {
							if (Bukkit.getPlayer(u.getKey()) != null) {
								Bukkit.getPlayer(u.getKey()).teleport(u.getValue());
								Bukkit.getPlayer(u.getKey()).sendMessage(ChatColor.GREEN + "You have been warped back from the 10th Dimension");
							}
						}
						Player edd = Bukkit.getPlayer(Asteria.ed);
						edd.sendMessage(ChatColor.GREEN + "Your time has run out! All of the items have been dropped on the floor");
						for (ItemStack is : Asteria.tenthDimensionItems)
							edd.getWorld().dropItem(edd.getLocation(), is);
						Asteria.tenthDimensionItems.clear();
						Asteria.tenthdimensionLocations.clear();
						edd.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
						edd.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
						edd.removePotionEffect(PotionEffectType.SPEED);
						edd.removePotionEffect(PotionEffectType.REGENERATION);
					}, (Asteria.whenStopDimensional - Instant.now().getEpochSecond()) * 20);
				} else {
					Bukkit.getScheduler().cancelTask(Asteria.dimensionId);
					for (Integer id : Asteria.dimensionIds)
						Bukkit.getScheduler().cancelTask(id);
					Asteria.dimensionIds.clear();
					Player edd = Bukkit.getPlayer(Asteria.ed);
					edd.sendMessage(ChatColor.GREEN + "You have killed everyone! All of the items have been dropped on the floor");
					Bukkit.getPlayer(Asteria.ed).teleport(Asteria.tenthdimensionLocations.get(Asteria.ed));
					for (ItemStack is : Asteria.tenthDimensionItems)
						edd.getWorld().dropItem(edd.getLocation(), is);
					Asteria.tenthDimensionItems.clear();
					Asteria.tenthdimensionLocations.clear();
					edd.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
					edd.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
					edd.removePotionEffect(PotionEffectType.SPEED);
					edd.removePotionEffect(PotionEffectType.REGENERATION);
				}
			}
		}
		if (e.getDeathMessage().equals("death.attack.witherSkull.item"))
			e.setDeathMessage(p.getName() + " was killed by §6Wither Turret");
		if (p.getUniqueId().equals(Asteria.hany)) {
			for (World wo : Bukkit.getWorlds()) {
				for (LivingEntity le : wo.getLivingEntities()) {
					if ((le.getType().equals(EntityType.WITHER_SKELETON) || le.hasMetadata("fromPower")) || (le.getType().equals(EntityType.WITHER) && le.getCustomName() != null && le.getCustomName().equals("§6§lRoyal Guard"))) {
						le.setHealth(0);
					}
				}
			}
			if (Asteria.isBerserkerActive) {
				Bukkit.getScheduler().cancelTask(Asteria.berserkerEndId);
				PowerUtils.endBerserker();
			}
		}
		if (Asteria.riftLocations.containsKey(p.getUniqueId())) {
			if (Asteria.riftTarget.containsKey(p.getUniqueId())) { // isn't ed
				Bukkit.getPlayer(Asteria.ed).sendMessage(ChatColor.DARK_GREEN + p.getName() + " has died! Their location of death is " + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ());
				p.sendMessage(ChatColor.RED + "You have failed to kill your target!");
				if (p.getKiller() != null && p.getKiller() instanceof Player && Asteria.riftTarget.containsKey(p.getKiller().getUniqueId()) && Asteria.riftTarget.get(p.getKiller().getUniqueId()).equals(p.getUniqueId())) { // killer had player as target
					Player killer = p.getKiller();
					killer.sendMessage(ChatColor.DARK_GREEN + "You have killed your target! You may now return");
					killer.teleport(Asteria.riftLocations.get(killer.getUniqueId()));
					Asteria.riftTarget.remove(killer.getUniqueId());
					Asteria.riftLocations.remove(killer.getUniqueId());
					if (Asteria.riftTarget.size() > 3) {
						Bukkit.getPlayer(Asteria.getKeyByValue(Asteria.riftTarget, killer.getUniqueId())).sendMessage(ChatColor.RED + "Your target has killed their target, your new target is: " + Bukkit.getPlayer(Asteria.riftTarget.get(p.getUniqueId())).getName());
						Asteria.riftTarget.put(Asteria.getKeyByValue(Asteria.riftTarget, killer.getUniqueId()), Asteria.riftTarget.get(p.getUniqueId()));
						Bukkit.getPlayer(Asteria.ed).sendMessage(ChatColor.DARK_GREEN + String.valueOf(Asteria.riftTarget.size() - 1) + " players remain");
					} else if (Asteria.riftTarget.size() == 3) {
						Bukkit.getPlayer(Asteria.getKeyByValue(Asteria.riftTarget, killer.getUniqueId())).sendMessage(ChatColor.RED + "Your target has killed their target, you are the only player left, you will now be warped back!!");
						Bukkit.getScheduler().cancelTask(Asteria.riftId);
						Bukkit.getPlayer(Asteria.ed).sendMessage(ChatColor.RED + "Rift has ended as there is only one player left!");
						Asteria.riftLocations.remove(p.getUniqueId());
						PowerUtils.endRift();
					} else {
						Bukkit.getScheduler().cancelTask(Asteria.riftId);
						Bukkit.getPlayer(Asteria.ed).sendMessage(ChatColor.RED + "Rift has ended as there is no players left!");
						Asteria.riftLocations.remove(p.getUniqueId());
						PowerUtils.endRift();
					}
				} else { // they died but not to the person who had them as a target
					if (Asteria.riftTarget.size() > 2) {
						Bukkit.getPlayer(Asteria.getKeyByValue(Asteria.riftTarget, p.getUniqueId())).sendMessage(ChatColor.RED + "Your target has died, your new target is: " + Bukkit.getPlayer(Asteria.riftTarget.get(p.getUniqueId())).getName());
						Asteria.riftTarget.put(Asteria.getKeyByValue(Asteria.riftTarget, p.getUniqueId()), Asteria.riftTarget.get(p.getUniqueId()));
						Bukkit.getPlayer(Asteria.ed).sendMessage(ChatColor.DARK_GREEN + String.valueOf(Asteria.riftTarget.size() - 1) + " players remain");
					} else if (Asteria.riftTarget.size() == 2) {
						Bukkit.getPlayer(Asteria.getKeyByValue(Asteria.riftTarget, p.getUniqueId())).sendMessage(ChatColor.RED + "Your target died, you are the only player left, you will now be warped back!!");
						Bukkit.getScheduler().cancelTask(Asteria.riftId);
						Bukkit.getPlayer(Asteria.ed).sendMessage(ChatColor.RED + "Rift has ended as there is only one player left!");
						Asteria.riftLocations.remove(p.getUniqueId());
						PowerUtils.endRift();
					} else {
						Bukkit.getScheduler().cancelTask(Asteria.riftId);
						Bukkit.getPlayer(Asteria.ed).sendMessage(ChatColor.RED + "Rift has ended as there is no players left!");
						Asteria.riftLocations.remove(p.getUniqueId());
						PowerUtils.endRift();
					}
				}
				Asteria.riftTarget.remove(p.getUniqueId());
				Asteria.riftLocations.remove(p.getUniqueId());
			} else { // this is ed
				for (Player a : Bukkit.getOnlinePlayers()) {
					if (a.getWorld().getName().equals("rift") && !a.getUniqueId().equals(Asteria.ed))
						a.sendMessage(ChatColor.GREEN + Bukkit.getPlayer(Asteria.ed).getName() + " has died, you will now be warped back!");
				}
				Bukkit.getScheduler().cancelTask(Asteria.riftId);
				PowerUtils.endRift();
			}
		}
		if (p.getUniqueId().equals(Asteria.jonathan) && Asteria.wrathId != 0) {
			if (Asteria.thirtySecId != 0) {
				Bukkit.getScheduler().cancelTask(Asteria.thirtySecId);
				Asteria.thirtySecId = 0;
			}
			Bukkit.getScheduler().cancelTask(Asteria.wrathId);
			if (Asteria.baneId != 0) {
				Bukkit.getScheduler().cancelTask(Asteria.baneId);
				Asteria.baneId = 0;
			}
			Asteria.hasUsedBane = false;
			Asteria.hasUsedBaneAfter = false;
			Asteria.hasthirtysecpassed = false;
			Asteria.wrathId = 0;
		}
		if (p.getUniqueId().equals(Asteria.ed))
			Asteria.reversalDamage = 0D;
		DustOptions pa = new Particle.DustOptions(Color.fromRGB(125, 9, 20), 1);
		p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 500, 0.5, 0.5, 0.5, pa);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		UUID u = p.getUniqueId();
		if (Asteria.wither.containsKey(u)) {
			Bukkit.getScheduler().cancelTask(Asteria.wither.get(u));
			Asteria.wither.remove(u);
		}
		if (Asteria.stomp.contains(u))
			Asteria.stomp.remove(u);
		if (Asteria.invis.containsKey(u)) {
			if (u.equals(Asteria.ed)) {
				p.removePotionEffect(PotionEffectType.BLINDNESS);
				p.removePotionEffect(PotionEffectType.SPEED);
			}
			p.removePotionEffect(PotionEffectType.INVISIBILITY);
			Bukkit.getScheduler().cancelTask(Asteria.invis.get(u));
			Asteria.invis.remove(u);
			for (Player a : Bukkit.getOnlinePlayers())
				a.showPlayer(plugin, p);
		}
		for (Entry<UUID, Integer> iv : Asteria.invis.entrySet())
			p.showPlayer(plugin, Bukkit.getPlayer(iv.getKey()));
		if (Asteria.dimensionalRoom.containsKey(u)) {
			String worldName = p.getWorld().getName();
			p.teleport(Asteria.dimensionalRoom.get(u));
			if (u.equals(Asteria.ed)) {
				for (Entry<UUID, Location> dim : Asteria.dimensionalRoom.entrySet()) {
					if (Bukkit.getPlayer(dim.getKey()) != null) {
						Bukkit.getPlayer(dim.getKey()).teleport(dim.getValue());
						Bukkit.getPlayer(dim.getKey()).sendMessage(ChatColor.BLUE + "You have been warped back!");
					}
				}
				Asteria.dimensionalRoom.clear();
			} else if (worldName.equals("12thdimension"))
				Asteria.dimensionalRoom.remove(u);
		}
		if (Asteria.dimensionalWarpQueue.contains(u))
			Asteria.dimensionalWarpQueue.remove(u);
		if (Asteria.dimensionalRoomQueue.contains(u))
			Asteria.dimensionalRoomQueue.remove(u);
		if (Asteria.inFightWithHany.contains(u))
			Asteria.inFightWithHany.remove(u);
		for (World wo : Bukkit.getWorlds()) {
			for (LivingEntity le : wo.getLivingEntities()) {
				if (le.getType().equals(EntityType.WITHER_SKELETON)) {
					WitherSkeleton w = (WitherSkeleton) le;
					if (w.getTarget() != null && w.getTarget().getUniqueId().equals(u))
						w.setTarget(null);
				}
				if (le.getType().equals(EntityType.WITHER)) {
					Wither w = (Wither) le;
					if (w.getTarget() != null && w.getTarget().getUniqueId().equals(u))
						w.setTarget(null);
				}
			}
		}
		if (u.equals(Asteria.ed) && Asteria.isSpatialBlockActive) {
			Asteria.isSpatialBlockActive = false;
			p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
			Asteria.canSpatialBlockPlace = false;
			if (Asteria.lastSpatialBlock != null) {
				Asteria.lastSpatialBlock.getBlock().setType(Material.AIR);
				Asteria.lastSpatialBlock = null;
			}
		}
		if (u.equals(Asteria.ramiro))
			Asteria.alertPlayers.clear();
		if (Asteria.alertPlayers.contains(u)) {
			Asteria.alertPlayers.remove(u);
			Bukkit.getPlayer(Asteria.ramiro).sendMessage(ChatColor.RED + p.getName() + " isn't within your premise anymore!");
		}
		if (u.equals(Asteria.ramiro) && Asteria.isTakeDamageDoubleJump) {
			p.damage(6);
			Asteria.isTakeDamageDoubleJump = false;
		}
		if (u.equals(Asteria.hany)) {
			if (Asteria.isTakeDamageWitherDash) {
				p.damage(2);
				Asteria.isTakeDamageWitherDash = false;
			}
			if (Asteria.isBerserkerActive) {
				Bukkit.getScheduler().cancelTask(Asteria.berserkerEndId);
				PowerUtils.endBerserker();
			}
			if (Asteria.hanyPowerMultiplier != 1)
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
		}
		if (Asteria.tenthdimensionLocations.containsKey(u))
			p.setHealth(0);
		if (u.equals(Asteria.jonathan) && Asteria.wrathId != 0) {
			if (Asteria.thirtySecId != 0) {
				Bukkit.getScheduler().cancelTask(Asteria.thirtySecId);
				Asteria.thirtySecId = 0;
			}
			boolean foundItem = false;
			for (ItemStack is : p.getInventory().getContents()) {
				if (is != null && is.getItemMeta().getDisplayName() != null && is.getItemMeta().getDisplayName().equals("§6Bane of Darkness")) {
					p.getInventory().remove(is);
					foundItem = true;
				}
			}
			if (!foundItem && p.getItemOnCursor() != null && p.getItemOnCursor().getItemMeta().getDisplayName() != null && p.getItemOnCursor().getItemMeta().getDisplayName().equals("§6Bane of Darkness")) {
				p.getInventory().remove(p.getItemOnCursor());
				foundItem = true;
			}
			Asteria.hasUsedBane = false;
			Asteria.hasUsedBaneAfter = false;
			Asteria.hasthirtysecpassed = false;
			if (Asteria.baneId != 0) {
				Bukkit.getScheduler().cancelTask(Asteria.baneId);
				Asteria.baneId = 0;
			}
			Asteria.wrathId = 0;
		}
		if (Asteria.riftLocations.containsKey(u))
			p.setHealth(0);
		if (Asteria.reversalDamageId.containsKey(u)) {
			Bukkit.getScheduler().cancelTask(Asteria.reversalDamageId.get(u));
			p.setHealth(0);
			Asteria.reversalDamageId.remove(u);
		}
		if (Asteria.isSorcererActive && u.equals(Asteria.saby)) {
			p.removePotionEffect(PotionEffectType.REGENERATION);
			p.removePotionEffect(PotionEffectType.HEALTH_BOOST);
			p.removePotionEffect(PotionEffectType.JUMP);
			p.removePotionEffect(PotionEffectType.SPEED);
			Asteria.isSorcererActive = false;
			Bukkit.getScheduler().cancelTask(Asteria.SorcererId);
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (Asteria.witherEmbraceMove.contains(p.getUniqueId()) && (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()))
			e.setCancelled(true);
		if (Asteria.stomp.contains(p.getUniqueId())) {
			if (p.isSneaking())
				p.setVelocity(p.getLocation().getDirection().multiply(0).setY(-2));
			if (!p.getLocation().getBlock().getRelative(BlockFace.DOWN).isPassable() || !p.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).isPassable()) {
				for (Player a : Bukkit.getOnlinePlayers()) {
					if (a.getWorld().equals(p.getWorld()) && a.getLocation().distance(p.getLocation()) < 8 && !a.equals(p)) {
						a.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 600, 1));
					}
				}
				p.getWorld().createExplosion(p.getLocation(), 8, true, true, e.getPlayer());
				Asteria.stomp.remove(p.getUniqueId());
			}
		}
		for (Entry<Integer, HashSet<UUID>> fit : Asteria.frozen.entrySet()) {
			if (fit.getValue().contains(p.getUniqueId()) && (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ() || e.getFrom().getBlockY() != e.getTo().getBlockY()))
				e.setCancelled(true);
		}
		if (p.getWorld().getName().equals("dimensional_room") && p.getLocation().getY() < 60)
			p.teleport(new Location(Bukkit.getWorld("dimensional_room"), 0.5, 81, 0.5));
		if (p.getUniqueId().equals(Asteria.ed) && p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir() && !Asteria.canSpatialBlockPlace && Asteria.isSpatialBlockActive) {
			Material[] m = { Material.BLUE_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.PURPLE_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS };
			if (Asteria.lastSpatialBlock == null) {
				p.getLocation().getBlock().getRelative(BlockFace.DOWN).setType(m[r.nextInt(m.length)]);
				Asteria.lastSpatialBlock = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();
			} else {
				final Location l = Asteria.lastSpatialBlock;
				Asteria.canSpatialBlockPlace = true;
				ArrayList<Location> temp = new ArrayList<Location>();
				int i = r.nextInt(3);
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> temp.add(p.getLocation()), 3 + i);
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
					l.getBlock().setType(Material.AIR);
					Location locForBlock = p.getLocation();
					locForBlock.setX(locForBlock.getX() + ((p.getLocation().getX() - temp.get(0).getX()) * 1.7));
					locForBlock.setZ(locForBlock.getZ() + ((p.getLocation().getZ() - temp.get(0).getZ()) * 1.7));
					Asteria.canSpatialBlockPlace = false;
					if (locForBlock.getBlock().getRelative(BlockFace.DOWN).getType().isAir()) {
						locForBlock.getBlock().getRelative(BlockFace.DOWN).setType(m[r.nextInt(m.length)]);
						Asteria.lastSpatialBlock = locForBlock.getBlock().getRelative(BlockFace.DOWN).getLocation();
					} else
						Asteria.lastSpatialBlock = null;
				}, 4 + i);
			}
		}
		if (Asteria.catastrophicSealPlayer != null && Asteria.catastrophicSealPlayer.equals(p.getUniqueId()) && (!Asteria.locOfCatastrophicSeal.getWorld().getName().equals(p.getWorld().getName()) || (Asteria.locOfCatastrophicSeal.getWorld().getName().equals(p.getWorld().getName()) && Asteria.locOfCatastrophicSeal.distance(p.getLocation()) > 10.5))) {
			Player o = Bukkit.getPlayer(Asteria.oz);
			if (o != null)
				o.sendMessage(ChatColor.RED + p.getName() + " is no longer being affected by Catastrophic Seal because they got out of the obsidian!");
			p.sendMessage(ChatColor.GREEN + "You are no longer being affected by Catastrophic Seal!");
			Asteria.catastrophicSealPlayer = null;
			Asteria.locOfCatastrophicSeal = null;
			Bukkit.getScheduler().cancelTask(Asteria.catastrophicSealId);
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (Asteria.deathItems.containsKey(p.getUniqueId())) {
			for (ItemStack is : Asteria.deathItems.get(p.getUniqueId()))
				p.getInventory().addItem(is);
			Asteria.deathItems.remove(p.getUniqueId());
			p.sendMessage("§2You died and dropped a quarter of your items!");
		}
		ArrayList<ItemStack> removePower = new ArrayList<ItemStack>();
		for (ItemStack is : p.getInventory().getContents()) {
			if (is != null && Asteria.powerNames.contains(is.getItemMeta().getDisplayName()))
				removePower.add(is);
		}
		for (ItemStack rP : removePower)
			p.getInventory().remove(rP);
		Asteria.addPowers(p);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		ArrayList<ItemStack> removePower = new ArrayList<ItemStack>();
		for (ItemStack is : p.getInventory().getContents()) {
			if (is != null && Asteria.powerNames.contains(is.getItemMeta().getDisplayName()))
				removePower.add(is);
		}
		for (ItemStack rP : removePower)
			p.getInventory().remove(rP);
		Asteria.addPowers(p);
		for (Entry<UUID, Integer> iv : Asteria.invis.entrySet()) {
			p.hidePlayer(plugin, Bukkit.getPlayer(iv.getKey()));
		}
		if (Asteria.dimensionalRoom.containsKey(p.getUniqueId()))
			p.teleport(new Location(Bukkit.getWorld("dimensional_room"), 0.5, 81, 0.5));
		if (!p.hasPlayedBefore()) {
			p.sendMessage(ChatColor.YELLOW + "Welcome " + p.getName() + " to AsteriaRemastered!\nFinally! it's done! After months we're finally playing this smp, aren't you hyped too!?! Anyway yea, powers n stuff, pretty cool huh, and you'll unlock more than what you have rn later so thats even more cool. Go and get your other powers! But not too quickly because then it'll kill the smp, atleast have fun tho!");
			p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.FIREWORK);
			if (p.getUniqueId().equals(Asteria.saby))
				p.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(1);
		}
		if (p.getUniqueId().equals(Asteria.hany) && Asteria.hanyPowerMultiplier != 1)
			p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Asteria.hanyPowerMultiplier == 2 ? 40 : 10);
	}

	@EventHandler
	public void onPlayerConsume(PlayerItemConsumeEvent e) {
		if (Asteria.powerNames.contains(e.getItem().getItemMeta().getDisplayName()))
			e.setCancelled(true);
		if (Asteria.invis.containsKey(e.getPlayer().getUniqueId()) && e.getItem().getType().equals(Material.MILK_BUCKET))
			e.setCancelled(true);
		if (e.getPlayer().getUniqueId().equals(Asteria.jose) && e.getItem().getType().equals(Material.POTION) && ((PotionMeta) e.getItem().getItemMeta()).getBasePotionData().getType().equals(PotionType.WATER) && Asteria.hasAdvancement(e.getPlayer(), "nether/brew_potion")) {
			PotionEffectType[] effects = { PotionEffectType.ABSORPTION, PotionEffectType.DAMAGE_RESISTANCE, PotionEffectType.DOLPHINS_GRACE, PotionEffectType.FAST_DIGGING, PotionEffectType.FIRE_RESISTANCE, PotionEffectType.HEAL, PotionEffectType.HEALTH_BOOST, PotionEffectType.HERO_OF_THE_VILLAGE, PotionEffectType.INCREASE_DAMAGE, PotionEffectType.JUMP, PotionEffectType.LUCK, PotionEffectType.NIGHT_VISION, PotionEffectType.REGENERATION, PotionEffectType.SATURATION, PotionEffectType.SLOW_FALLING, PotionEffectType.SPEED, PotionEffectType.WATER_BREATHING };
			ArrayList<PotionEffectType> positiveEffects = new ArrayList<PotionEffectType>();
			for (PotionEffectType pet : effects)
				positiveEffects.add(pet);
			int random = r.nextInt(positiveEffects.size());
			e.getPlayer().addPotionEffect(new PotionEffect(positiveEffects.get(random), 600, 1));
			positiveEffects.remove(random);
			int random1 = r.nextInt(positiveEffects.size());
			e.getPlayer().addPotionEffect(new PotionEffect(positiveEffects.get(random1), 600, 1));
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		ItemStack i = e.getPlayer().getInventory().getItem(e.getHand());
		if (i.hasItemMeta() && Asteria.powerNames.contains(i.getItemMeta().getDisplayName()))
			e.setCancelled(true);
		if (e.getRightClicked() instanceof AbstractHorse) {
			AbstractHorse h = (AbstractHorse) e.getRightClicked();
			if (h.getCustomName() != null && h.getCustomName().equals("§6Withered Allegiance") && i.getType().equals(Material.NAME_TAG))
				e.setCancelled(true);
		}
		if (i.hasItemMeta() && i.getItemMeta().getDisplayName().equals("§6Blood Bite") && e.getRightClicked() instanceof LivingEntity) {
			if (PowerUtils.isOnCooldownNew(Asteria.cooldownBloodBite, e.getPlayer()))
				PowerUtils.sendCDMsgNew(Asteria.cooldownBloodBite, e.getPlayer());
			else {
				LivingEntity le = (LivingEntity) e.getRightClicked();
				le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 3));
				le.damage(8, e.getPlayer());
				if (e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() < e.getPlayer().getHealth() + 8)
					e.getPlayer().setHealth(e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				else
					e.getPlayer().setHealth(e.getPlayer().getHealth() + 8);
				le.sendMessage(ChatColor.RED + "You have been bit by " + e.getPlayer().getName() + " and lost some health!");
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
				e.getPlayer().sendMessage(ChatColor.DARK_PURPLE + "You have bit an enemy and you got regened some health!");
				Asteria.cooldownBloodBite = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(180); // 3 minutes
			}
		}
	}

	@EventHandler
	public void onPlayerItemDamage(PlayerItemDamageEvent e) {
		if (e.getItem().getItemMeta().getDisplayName() != null && Asteria.powerNames.contains(e.getItem().getItemMeta().getDisplayName()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		if (!Asteria.endAdvancements.contains(p.getUniqueId()) && p.getWorld().getName().equals("world_the_end")) {
			Asteria.endAdvancements.add(p.getUniqueId());
			UUID u = p.getUniqueId();
			if (u.equals(Asteria.ed))
				e.getPlayer().sendMessage("§aYou now have §6End Jump§a!");
			else if (u.equals(Asteria.ramiro))
				e.getPlayer().sendMessage("§aCongrats! You now have resistance 1, and §6Rabbit Jump§a has upgraded!");
			else if (u.equals(Asteria.jonathan)) {
				p.sendMessage("§aYou now have §6Supernova§a, and you have been given more hearts!");
				if (!Asteria.hasAdvancement(p, "end/respawn_dragon"))
					p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(24);
			} else if (u.equals(Asteria.jose))
				p.sendMessage("§aCongrats! You now have §6Wrath of the Sea§a, some permanent effects, and four extra hearts!");
			else if (u.equals(Asteria.eli))
				p.sendMessage("§aYou now have §6Blades of Exile§a!");
			else if (u.equals(Asteria.oz))
				p.sendMessage("§aCongrats! You now take no fall damage, and have §6The End Gates§a!");
		}
		if (p.getUniqueId().equals(Asteria.ed) || Bukkit.getPlayer(Asteria.ed) == null)
			return;
		Environment from = e.getFrom().getEnvironment();
		String fromString;
		if (from.equals(Environment.NORMAL))
			fromString = "§aThe Overworld";
		else if (from.equals(Environment.NETHER))
			fromString = "§cThe Nether";
		else if (from.equals(Environment.THE_END))
			fromString = "§eThe End";
		else
			fromString = "§rUnknown";
		String toString;
		Environment to = p.getWorld().getEnvironment();
		if (to.equals(Environment.NORMAL))
			toString = "§aThe Overworld";
		else if (to.equals(Environment.NETHER))
			toString = "§cThe Nether";
		else if (to.equals(Environment.THE_END))
			toString = "§eThe End";
		else
			toString = "§rUnknown";
		Bukkit.getPlayer(Asteria.ed).sendMessage(ChatColor.DARK_GREEN + p.getName() + ChatColor.GREEN + " has switched from " + fromString + ChatColor.GREEN + " to " + toString);
	}

	@EventHandler
	public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent e) {
		Player p = e.getPlayer();
		String name = e.getAdvancement().getKey().getKey();
		ArrayList<ItemStack> removePower = new ArrayList<ItemStack>();
		for (ItemStack is : p.getInventory().getContents()) {
			if (is != null && Asteria.powerNames.contains(is.getItemMeta().getDisplayName()))
				removePower.add(is);
		}
		for (ItemStack rP : removePower)
			p.getInventory().remove(rP);
		Asteria.addPowers(p);
		if (p.getUniqueId().equals(Asteria.ramiro)) {
			if (name.equals("story/mine_diamond"))
				p.sendMessage("§aYou now have §6Rabbit Jump§a!");
			else if (name.equals("story/enter_the_nether"))
				p.sendMessage("§aYou now have §bStomp§a, speed 1, and §6Rabbit Jump§a has upgraded!");
			else if (name.equals("story/shiny_gear"))
				p.sendMessage("§aYou now have strength 1, §6Alert§a, and §6Rabbit Jump§a has upgraded!");
			else if (name.equals("nether/explore_nether"))
				p.sendMessage("§aYou now have haste 2, Alert, and §6Rabbit Jump§a has upgraded!");
		} else if (p.getUniqueId().equals(Asteria.jonathan)) {
			if (name.equals("story/lava_bucket"))
				p.sendMessage("§aYou now have §6Distortion§a, and §6Star Shot§a has upgraded!");
			else if (name.equals("nether/obtain_blaze_rod"))
				p.sendMessage("§aYou now have §6Eruption§a, and §6Distortion§a has upgraded!");
			else if (name.equals("story/shiny_gear"))
				p.sendMessage("§aYou now have strength 2, and §6Eruption§a has upgraded!");
			else if (name.equals("end/elytra"))
				p.sendMessage("§aYou now have §6Star Shield§a, and §6Distortion§a, §6Eruption§a, and §6Star Shot§a has upgraded!");
			else if (name.equals("end/respawn_dragon")) {
				p.sendMessage("§aCongrats! You now have §6Wrath of Asteria§a, and you have been given more hearts!");
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(28);
			}
		} else if (p.getUniqueId().equals(Asteria.ed)) {
			if (name.equals("adventure/kill_a_mob"))
				p.sendMessage("§aYou now have §6Hostile Convergence§a!");
			else if (name.equals("story/enter_the_nether"))
				p.sendMessage("§aYou now have §6Nether Jump§a!");
			else if (name.equals("story/mine_diamond"))
				p.sendMessage("§aYou now have §6Spatial Manipulation§a, and §6Revenge Counter§a!");
			else if (name.equals("story/shiny_gear"))
				p.sendMessage("§aYou now have §6Area Counter§a, and §6Dimensional Room§a!");
			else if (name.equals("end/kill_dragon"))
				p.sendMessage("§aYou now have §610th Dimensional Physiology§a!");
			else if (name.equals("end/find_end_city"))
				p.sendMessage("§aYou now have §611th Energy Convergence§a, and §6Rift§a, §6Eruption§a!");
			else if (name.equals("end/respawn_dragon"))
				p.sendMessage("§aCongrats! §aYou now have §612th Dimensional Physiology§a!");
		} else if (p.getUniqueId().equals(Asteria.jose)) {
			if (name.equals("adventure/kill_a_mob"))
				p.sendMessage("§aYou now have §6Calling Conch§a!");
			else if (name.equals("story/obtain_armor"))
				p.sendMessage("§aYou now have §6Climate Change§a, and §6Neptune§a has upgraded!");
			else if (name.equals("husbandry/kill_axolotl_target"))
				p.sendMessage("§aYou now have §6Aqua Blast§a, and §6Neptune§a and §6Calling Conch§a has upgraded!");
			else if (name.equals("adventure/very_very_frightening"))
				p.sendMessage("§aYou now have §6Neptune§a and §6Calling Conch§a has upgraded!");
			else if (name.equals("nether/brew_potion"))
				p.sendMessage("§aYou now have effects when drinking a water bottle and in water, and §6Neptune§a, §6Raging Storm§a, §6Aqua Blast§a, and §6Calling Conch§a has upgraded!");
		} else if (p.getUniqueId().equals(Asteria.eli)) {
			if (name.equals("story/obtain_armor"))
				p.sendMessage("§aYou now have §6Umbrakinesis§a!");
			else if (name.equals("story/enter_the_nether"))
				p.sendMessage("§aYou now have §6Dawn of Light§a!");
			else if (name.equals("story/shiny_gear"))
				p.sendMessage("§aYou now have strength 2 and resistance 2, and §6Shape Shift§a!");
			else if (name.equals("end/find_end_city"))
				p.sendMessage("§aCongrats! You now have §6Leviathan Axe§a!");
		} else if (p.getUniqueId().equals(Asteria.brent)) {
			if (name.equals("adventure/kill_a_mob"))
				p.sendMessage("§aYou now have §6Dark Shift§a!");
			else if (name.equals("story/deflect_arrow"))
				p.sendMessage("§aYou now have §6Blood Bite§a!");
			else if (name.equals("nether/return_to_sender"))
				p.sendMessage("§aYou now have §6Return§a!");
			else if (name.equals("story/follow_ender_eye"))
				p.sendMessage("§aYou now have §6Poisonous Breath§a!");
			else if (name.equals("end/dragon_egg"))
				p.sendMessage("§aYou now have §6Black Hole§a!");
			else if (name.equals("end/elytra"))
				p.sendMessage("§aCongrats! You now have §6Phantom Burst§a!");
		} else if (p.getUniqueId().equals(Asteria.oz)) {
			if (name.equals("adventure/sniper_duel"))
				p.sendMessage("§aYou now have resistance 1, more speed and strength, and §6Almightly Push§a!");
			else if (name.equals("nether/return_to_sender"))
				p.sendMessage("§aYou now have haste 1, and §6Full Counter§a!");
			else if (name.equals("story/enchant_item"))
				p.sendMessage("§aYou now have night vision, and §6Catastrophic Seal§a!");
		} else if (p.getUniqueId().equals(Asteria.saby)) {
			if (name.equals("story/lava_bucket"))
				p.sendMessage("§aYou now have §6Hellstorm§a, and §bFire Breath§a!");
			else if (name.equals("adventure/voluntary_exile"))
				p.sendMessage("§aYou now have §6Spectral Shift§a and §bParalysis§a, and §bSnowStorm§a has upgraded!");
			else if (name.equals("adventure/shoot_arrow"))
				p.sendMessage("§aYou now have §6Conjuring Shot§a, and §bHellstorm§a has upgraded!");
			else if (name.equals("husbandry/make_a_sign_glow"))
				p.sendMessage("§aYou now have §6Divine Light§a, and §bSnowStorm§a and §bHellstorm§a has upgraded!");
			else if (name.equals("story/enchant_item"))
				p.sendMessage("§aYou now have §6Time Manipulation§a, and §6Conjuring Shot§a has upgraded!");
			else if (name.equals("end/dragon_breath"))
				p.sendMessage("§aCongrats! You now have §6Sorcerer's State§a!");
		} else if (p.getUniqueId().equals(Asteria.hany)) {
			if (name.equals("story/enter_the_nether"))
				p.sendMessage("§aYou now have §6Wither Punch§a, and no fall damage!");
			else if (name.equals("nether/find_fortress"))
				p.sendMessage("§aYou now have §6Wither Turret§a and §6Withered Allegiance§a!");
			else if (name.equals("nether/get_wither_skull"))
				p.sendMessage("§aYou now have §6Soul's of the Damned§a, and §bWither Dash§a!");
			else if (name.equals("nether/obtain_blaze_rod"))
				p.sendMessage("§aYou now have regeneration in the nether §bFortuna Flip§a, and §6Soul's of the Damned§a, §bStygius§a and §bWither Turret§a has upgraded!");
			else if (name.equals("nether/brew_potion"))
				p.sendMessage("§aYou now have strength 1, and §6Wither Punch§a and §6Soul's of the Damned§a has upgraded!");
			else if (name.equals("nether/summon_wither"))
				p.sendMessage("§aCongrats! You now have §6Royal Guard§a!");
		}
	}

}
