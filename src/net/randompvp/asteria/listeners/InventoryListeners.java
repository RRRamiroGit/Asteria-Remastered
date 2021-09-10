package net.randompvp.asteria.listeners;

import java.time.Instant;
import java.util.ArrayList;
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
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import net.randompvp.asteria.Asteria;
import net.randompvp.asteria.utils.PowerUtils;
import net.randompvp.asteria.utils.Utils;

public class InventoryListeners implements Listener {
	Random r = new Random();

	Plugin plugin;

	public InventoryListeners(Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		ItemStack i = e.getCurrentItem();
		if ((e.getClick().isShiftClick() && e.getClickedInventory() == p.getInventory() && i != null && i.getItemMeta() != null && Asteria.powerNames.contains(i.getItemMeta().getDisplayName())) || (e.getClick().isKeyboardClick() && e.getClickedInventory() != p.getInventory() && e.getHotbarButton() > -1 && p.getInventory().getItem(e.getHotbarButton()) != null && Asteria.powerNames.contains(p.getInventory().getItem(e.getHotbarButton()).getItemMeta().getDisplayName())) || (e.getClickedInventory() != p.getInventory() && e.getCursor() != null && !e.getCursor().getType().equals(Material.AIR) && Asteria.powerNames.contains(e.getCursor().getItemMeta().getDisplayName())))
			e.setCancelled(true);
		if (e.getView().getTitle().equals("§lDimension Jump")) {
			e.setCancelled(true);
			if (i != null && i.getItemMeta() != null && i.getItemMeta().getDisplayName() != null) {
				int x = p.getLocation().getBlockX();
				int z = p.getLocation().getBlockZ();
				if (i.getItemMeta().getDisplayName().equals("§a§lOverworld")) {
					if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						if (p.getWorld().getEnvironment().equals(Environment.NETHER)) {
							x = x * 8;
							z = z * 8;
						}
						for (int y = 255; y > 0; y--) {
							Block b = Bukkit.getWorld("world").getBlockAt(x, y, z);
							if (!b.getType().isAir()) {
								Location l = b.getLocation();
								l.setX(l.getX() + 0.5);
								l.setZ(l.getZ() + 0.5);
								l.setY(l.getY() + 1);
								p.teleport(l);
								for (UUID u : Asteria.dimensionalWarpQueue) {
									if (!Bukkit.getPlayer(u).isDead())
										Bukkit.getPlayer(u).teleport(l);
								}
								Asteria.dimensionalWarpQueue.clear();
								PowerUtils.setBlocksForWarp(b);
								p.damage(6);
								p.sendMessage(ChatColor.GREEN + "Warped to the Overworld!");
								return;
							}
						}
						Block b = Bukkit.getWorld("world").getBlockAt(x, 80, z);
						Location l = b.getLocation();
						l.setX(l.getX() + 0.5);
						l.setZ(l.getZ() + 0.5);
						l.setY(l.getY() + 1);
						p.teleport(l);
						for (UUID u : Asteria.dimensionalWarpQueue) {
							if (!Bukkit.getPlayer(u).isDead())
								Bukkit.getPlayer(u).teleport(l);
						}
						Asteria.dimensionalWarpQueue.clear();
						PowerUtils.setBlocksForWarp(b);
						p.damage(6);
						p.sendMessage(ChatColor.GREEN + "Warped to the Overworld!");
					}
				} else if (i.getItemMeta().getDisplayName().equals("§c§lNether")) {
					if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						x = x / 8;
						z = z / 8;
						for (int y = 0; y < 127; y++) {
							Block b = Bukkit.getWorld("world_nether").getBlockAt(x, y, z);
							if (b.getType().isAir()) {
								b = Bukkit.getWorld("world_nether").getBlockAt(x, y - 1, z);
								Location l = b.getLocation();
								l.setX(l.getX() + 0.5);
								l.setZ(l.getZ() + 0.5);
								l.setY(l.getY() + 1);
								p.teleport(l);
								for (UUID u : Asteria.dimensionalWarpQueue) {
									if (!Bukkit.getPlayer(u).isDead())
										Bukkit.getPlayer(u).teleport(l);
								}
								Asteria.dimensionalWarpQueue.clear();
								PowerUtils.setBlocksForWarp(b);
								p.damage(6);
								p.sendMessage(ChatColor.GREEN + "Warped to the Nether!");
								return;
							}
						}
						Block b = Bukkit.getWorld("world_nether").getBlockAt(x, 70, z);
						Location l = b.getLocation();
						l.setX(l.getX() + 0.5);
						l.setZ(l.getZ() + 0.5);
						l.setY(l.getY() + 1);
						p.teleport(l);
						for (UUID u : Asteria.dimensionalWarpQueue) {
							if (!Bukkit.getPlayer(u).isDead())
								Bukkit.getPlayer(u).teleport(l);
						}
						Asteria.dimensionalWarpQueue.clear();
						PowerUtils.setBlocksForWarp(b);
						p.damage(6);
						p.sendMessage(ChatColor.GREEN + "Warped to the Nether!");
					}
				} else if (i.getItemMeta().getDisplayName().equals("§e§lEnd")) {
					if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						if (p.getWorld().getEnvironment().equals(Environment.NETHER)) {
							x = x * 8;
							z = z * 8;
						}
						for (int y = 255; y > 0; y--) {
							Block b = Bukkit.getWorld("world_the_end").getBlockAt(x, y, z);
							if (!b.getType().isAir()) {
								Location l = b.getLocation();
								l.setX(l.getX() + 0.5);
								l.setZ(l.getZ() + 0.5);
								l.setY(l.getY() + 1);
								p.teleport(l);
								for (UUID u : Asteria.dimensionalWarpQueue) {
									if (!Bukkit.getPlayer(u).isDead())
										Bukkit.getPlayer(u).teleport(l);
								}
								Asteria.dimensionalWarpQueue.clear();
								PowerUtils.setBlocksForWarp(b);
								p.damage(6);
								p.sendMessage(ChatColor.GREEN + "Warped to the End!");
								return;
							}
						}
						Block b = Bukkit.getWorld("world_the_end").getBlockAt(x, 80, z);
						Location l = b.getLocation();
						l.setX(l.getX() + 0.5);
						l.setZ(l.getZ() + 0.5);
						l.setY(l.getY() + 1);
						p.teleport(l);
						for (UUID u : Asteria.dimensionalWarpQueue) {
							if (!Bukkit.getPlayer(u).isDead())
								Bukkit.getPlayer(u).teleport(l);
						}
						Asteria.dimensionalWarpQueue.clear();
						PowerUtils.setBlocksForWarp(b);
						p.damage(6);
						p.sendMessage(ChatColor.GREEN + "Warped to the End!");
					}
				}
			}
		} else if (e.getView().getTitle().equals("§lRift Menu")) {
			e.setCancelled(true);
			if (i != null && i.getItemMeta() != null && i.getItemMeta().getDisplayName() != null) {
				if (i.getItemMeta().getDisplayName().equals("§aOverworld")) {
					p.closeInventory();
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownRiftOverworld, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownRiftOverworld, p);
					else if (Asteria.riftInSpec)
						p.sendMessage(ChatColor.RED + "You may not use this while you are in spectator mode!");
					else if (!Asteria.canUseOpRift)
						p.sendMessage(ChatColor.RED + "You may not use this during combat!");
					else if (Asteria.riftInOverworld) {
						Asteria.riftInOverworld = false;
						Location l = p.getLocation();
						p.teleport(Asteria.riftLocations.get(p.getUniqueId()));
						Asteria.riftLocations.put(p.getUniqueId(), l);
						p.sendMessage(ChatColor.RED + "You have been teleported to the Rift Dimenison!");
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (a.getWorld().getName().equals("rift"))
								a.sendMessage(ChatColor.DARK_GREEN + p.getName() + " has gone back into the Rift Dimension!");
						}
						Bukkit.getScheduler().cancelTask(Asteria.riftOverworldId);
						Asteria.riftOverworldId = 0;
						Asteria.cooldownRiftOverworld = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(120); // 2 minutes
					} else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Asteria.riftInOverworld = true;
						Location l = p.getLocation();
						p.teleport(Asteria.riftLocations.get(p.getUniqueId()));
						Asteria.riftLocations.put(p.getUniqueId(), l);
						p.sendMessage(ChatColor.GREEN + "You have teleported from the Rift Dimenison!");
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (a.getWorld().getName().equals("rift"))
								a.sendMessage(ChatColor.DARK_GREEN + p.getName() + " has gone out of the Rift Dimension!");
						}
						Asteria.riftOverworldId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Asteria.riftInOverworld = false;
							Location k = p.getLocation();
							p.teleport(Asteria.riftLocations.get(p.getUniqueId()));
							Asteria.riftLocations.put(p.getUniqueId(), k);
							p.sendMessage(ChatColor.RED + "You have run out have time, and were teleported to the Rift Dimenison!");
							for (Player a : Bukkit.getOnlinePlayers()) {
								if (a.getWorld().getName().equals("rift"))
									a.sendMessage(ChatColor.DARK_GREEN + p.getName() + " has gone back into the Rift Dimension!");
							}
							Asteria.cooldownRiftOverworld = Instant.now().getEpochSecond() + 120; // 2 minutes
						}, 4800);
					}
				} else if (i.getItemMeta().getDisplayName().equals("§bSpectate")) {
					p.closeInventory();
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownRiftSpec, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownRiftSpec, p);
					else if (Asteria.riftInOverworld)
						p.sendMessage(ChatColor.RED + "You may not use this while you aren't in the Rift dimension!");
					else if (!Asteria.canUseOpRift)
						p.sendMessage(ChatColor.RED + "You may not use this during combat!");
					else if (Asteria.riftInSpec) {
						Asteria.riftInSpec = false;
						p.setFlying(false);
						p.setAllowFlight(false);
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (a.getWorld().getName().equals("rift"))
								a.sendMessage(ChatColor.DARK_GREEN + p.getName() + " has gone out of spectator mode!");
						}
						p.removePotionEffect(PotionEffectType.INVISIBILITY);
						p.sendMessage(ChatColor.RED + "You have canceled your spectator!!");
						Bukkit.getScheduler().cancelTask(Asteria.riftSpecId);
						Asteria.riftSpecId = 0;
						Asteria.cooldownRiftSpec = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(120); // 2 minutes
					}
					if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Asteria.riftInSpec = true;
						p.setAllowFlight(true);
						p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
						for (Player a : Bukkit.getOnlinePlayers()) {
							if (a.getWorld().getName().equals("rift"))
								a.sendMessage(ChatColor.DARK_GREEN + p.getName() + " has gone into spectator mode!");
						}
						p.sendMessage(ChatColor.GREEN + "You have enabled spectator! You can now fly and are invincible");
						Asteria.riftSpecId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Asteria.riftInSpec = false;
							p.setFlying(false);
							p.setAllowFlight(false);
							for (Player a : Bukkit.getOnlinePlayers()) {
								if (a.getWorld().getName().equals("rift"))
									a.sendMessage(ChatColor.DARK_GREEN + p.getName() + " has gone out of spectator mode!");
							}
							p.removePotionEffect(PotionEffectType.INVISIBILITY);
							p.sendMessage(ChatColor.RED + "Your spectator has run out!");
							Asteria.cooldownRiftSpec = Instant.now().getEpochSecond() + 120; // 2 minutes
						}, 4800);
					}
				} else if (i.getItemMeta().getDisplayName().equals("§bCancel")) {
					for (Player a : Bukkit.getOnlinePlayers()) {
						if (a.getWorld().getName().equals("rift"))
							a.sendMessage(ChatColor.AQUA + p.getName() + " has ended rift, you will be warped back!");
					}
					Bukkit.getScheduler().cancelTask(Asteria.riftId);
					PowerUtils.endRift();
				}
			}
		} else if (e.getView().getTitle().equals("§lChoose your variation")) {
			e.setCancelled(true);
			if (i != null && i.getItemMeta() != null && i.getItemMeta().getDisplayName() != null) {
				if (i.getItemMeta().getDisplayName().equals("§bZombie")) {
					p.sendMessage("§2You have set your mob to spawn to §aZombie");
					Asteria.mobHanySelected = EntityType.ZOMBIE;
				} else if (i.getItemMeta().getDisplayName().equals("§bZombie Villager")) {
					p.sendMessage("§2You have set your mob to spawn to §aZombie Villager");
					Asteria.mobHanySelected = EntityType.ZOMBIE_VILLAGER;
				} else if (i.getItemMeta().getDisplayName().equals("§bHusk")) {
					p.sendMessage("§2You have set your mob to spawn to §aHusk");
					Asteria.mobHanySelected = EntityType.HUSK;
				} else if (i.getItemMeta().getDisplayName().equals("§bDrowned")) {
					p.sendMessage("§2You have set your mob to spawn to §aDrowned");
					Asteria.mobHanySelected = EntityType.DROWNED;
				} else if (i.getItemMeta().getDisplayName().equals("§bSkeleton")) {
					p.sendMessage("§2You have set your mob to spawn to §aSkeleton");
					Asteria.mobHanySelected = EntityType.SKELETON;
				} else if (i.getItemMeta().getDisplayName().equals("§bStray")) {
					p.sendMessage("§2You have set your mob to spawn to §aStray");
					Asteria.mobHanySelected = EntityType.STRAY;
				} else if (i.getItemMeta().getDisplayName().equals("§bZombified Piglin")) {
					p.sendMessage("§2You have set your mob to spawn to §aZombified Piglin");
					Asteria.mobHanySelected = EntityType.ZOMBIFIED_PIGLIN;
				} else if (i.getItemMeta().getDisplayName().equals("§bZoglin")) {
					p.sendMessage("§2You have set your mob to spawn to §aZoglin");
					Asteria.mobHanySelected = EntityType.ZOGLIN;
				} else if (i.getItemMeta().getDisplayName().equals("§bZombie Horse")) {
					p.sendMessage("§2You have set your mob to spawn to §aZombie Horse");
					Asteria.mobHanySelected = EntityType.ZOMBIE_HORSE;
				} else if (i.getItemMeta().getDisplayName().equals("§bSkeleton Horse")) {
					p.sendMessage("§2You have set your mob to spawn to §aSkeleton Horse");
					Asteria.mobHanySelected = EntityType.SKELETON_HORSE;
				} else if (i.getItemMeta().getDisplayName().equals("§bPhantom")) {
					p.sendMessage("§2You have set your mob to spawn to §aPhantom");
					Asteria.mobHanySelected = EntityType.PHANTOM;
				} else if (i.getItemMeta().getDisplayName().equals("§4Desummon")) {
					ArrayList<Double> de = new ArrayList<Double>();
					for (World wo : Bukkit.getWorlds()) {
						for (LivingEntity le : wo.getLivingEntities()) {
							if (le.getType().equals(Asteria.mobHanySelected) && le.getCustomName() != null && le.getCustomName().equals("§6Withered Allegiance")) {
								final Double d = le.getHealth();
								if (!Asteria.withersallegianceQueue.containsKey(Asteria.mobHanySelected))
									Asteria.withersallegianceQueue.put(Asteria.mobHanySelected, new ArrayList<Double>());
								if (!Asteria.withersallegianceQueueId.containsKey(Asteria.mobHanySelected))
									Asteria.withersallegianceQueueId.put(Asteria.mobHanySelected, new ArrayList<Integer>());
								Asteria.withersallegianceQueue.get(Asteria.mobHanySelected).add(d);
								le.remove();
								de.add(d);
							}
						}
					}
					final EntityType hanyMob = Asteria.mobHanySelected;
					for (Double dee : de) {
						ArrayList<Integer> temp = new ArrayList<Integer>();
						temp.add(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Asteria.withersallegianceQueue.get(hanyMob).remove(dee);
							Asteria.withersallegianceQueueId.get(hanyMob).remove(temp.get(0));
						}, de.size() * 100));
						Asteria.withersallegianceQueueId.get(hanyMob).add(temp.get(0));
					}
					if (de.size() != 0)
						p.sendMessage(ChatColor.GREEN + "You have desummoned all of your mobs!");
					else
						p.sendMessage(ChatColor.RED + "You couldn't desummon anything!");
				} else if (i.getItemMeta().getDisplayName().equals("§bToggle Teleporting")) {
					if (Asteria.canAllegianceTeleportToHany) {
						Asteria.canAllegianceTeleportToHany = false;
						p.sendMessage(ChatColor.RED + "Turned off teleporting for allegance");
					} else {
						Asteria.canAllegianceTeleportToHany = true;
						p.sendMessage(ChatColor.GREEN + "Turned on teleporting for allegance");
					}
					ItemStack item = new ItemStack(Material.ENDER_PEARL);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(Utils.chat("§bToggle Teleporting"));
					List<String> lore = new ArrayList<String>();
					lore.add("Click to toggle the teleporting of all your allegiances");
					String color = "§a";
					if (!Asteria.canAllegianceTeleportToHany)
						color = "§c";
					lore.add("Currently set to: " + color + Asteria.canAllegianceTeleportToHany);
					meta.setLore(lore);
					item.setItemMeta(meta);
					p.getOpenInventory().getTopInventory().setItem(7, item);
				}
			}
		} else if (e.getView().getTitle().equals("§6Withered Allegiance"))
			e.setCancelled(true);
		else if (e.getView().getTitle().equals("§lCalling Conch")) {
			e.setCancelled(true);
			if (PowerUtils.isInDimension(p.getWorld().getName())) {
				p.closeInventory();
				p.sendMessage(ChatColor.RED + "You may not use this power right now!");
			} else if (i != null) {
				if (PowerUtils.isOnCooldownNew(Asteria.cooldownCallingConch, p))
					PowerUtils.sendCDMsgNew(Asteria.cooldownCallingConch, p);
				else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
					p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
				else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
					p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
				else {
					EntityType mob = null;
					int amountOfMobs = 4;
					if (i.getItemMeta().getDisplayName().equals("§bDrowned")) {
						mob = EntityType.DROWNED;
					} else if (i.getItemMeta().getDisplayName().equals("§bAxolotl")) {
						mob = EntityType.AXOLOTL;
						amountOfMobs = 3;
					} else if (i.getItemMeta().getDisplayName().equals("§bGuardian")) {
						mob = EntityType.GUARDIAN;
					}
					if (mob != null) {
						if (Asteria.endAdvancements.contains(p.getUniqueId()))
							amountOfMobs = 5;
						boolean hasBrew = Asteria.hasAdvancement(p, "nether/brew_potion");
						boolean hasFriend = Asteria.hasAdvancement(p, "husbandry/kill_axolotl_target");
						for (int distance = 0; distance < 50; distance++) {
							Location l = p.getEyeLocation();
							l.add(l.getDirection().multiply(distance + 1));
							if (!l.getBlock().getType().isAir()) {
								Location ll = p.getEyeLocation();
								ll.add(ll.getDirection().multiply(distance));
								for (int ii = 0; ii < amountOfMobs; ii++) {
									EntityType mobb = mob;
									if (mobb.equals(EntityType.GUARDIAN) && r.nextInt(4) == 0)
										mobb = EntityType.ELDER_GUARDIAN;
									LivingEntity d = (LivingEntity) p.getWorld().spawnEntity(new Location(ll.getWorld(), ll.getX() + (r.nextDouble() * 2 - 1), ll.getY(), ll.getZ() + (r.nextDouble() * 2 - 1)), mobb);
									d.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
									d.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
									d.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
									if (mobb.equals(EntityType.DROWNED)) {
										d.setCustomName("§bGuards of the Abyss");
										if (hasBrew) {
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
										} else {
											d.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
											d.getEquipment().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
											if (hasFriend) {
												d.getEquipment().setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
												d.getEquipment().setBoots(new ItemStack(Material.GOLDEN_BOOTS));
											}
										}
									} else if (mobb.equals(EntityType.GUARDIAN)) {
										d.setCustomName("§bCreatures of the Deep");
									} else if (mobb.equals(EntityType.ELDER_GUARDIAN)) {
										d.setCustomName("§bLeviathans");
									}
								}
								Asteria.cooldownCallingConch = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(300);
								break;
							} else if (distance == 49)
								p.sendMessage(ChatColor.RED + "You are not looking at a block! (Or you are too far away)");
						}
					}
				}
			}
		} else if (e.getView().getTitle().equals("§lClimate Change")) {
			e.setCancelled(true);
			if (i != null && i.getItemMeta() != null && i.getItemMeta().getDisplayName() != null) {
				if (PowerUtils.isOnCooldownNew(Asteria.cooldownClimateChange, p))
					PowerUtils.sendCDMsgNew(Asteria.cooldownClimateChange, p);
				else if (!p.getWorld().getEnvironment().equals(Environment.NORMAL))
					p.sendMessage(ChatColor.RED + "You must be in the overworld to change the climate!");
				else {
					if (i.getItemMeta().getDisplayName().equals("§bRain")) {
						if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
							p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
						else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
							p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
						else if (!p.getWorld().isThundering() && !p.getWorld().isClearWeather())
							p.sendMessage(ChatColor.RED + "It is already raining!");
						else {
							p.sendMessage(ChatColor.GREEN + "It is now raining!");
							p.getWorld().setStorm(true);
							p.getWorld().setThundering(false);
							p.getWorld().setWeatherDuration(6000);
						}
					} else if (i.getItemMeta().getDisplayName().equals("§bThunderStorm")) {
						if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
							p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
						else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
							p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
						else if (p.getWorld().isThundering() && p.getWorld().hasStorm())
							p.sendMessage(ChatColor.RED + "It is already thundering!");
						else {
							p.sendMessage(ChatColor.GREEN + "It is now thundering!");
							p.getWorld().setStorm(true);
							p.getWorld().setThundering(true);
							p.getWorld().setThunderDuration(6000);
						}
					}
					Asteria.cooldownClimateChange = Instant.now().getEpochSecond() + 600;
				}
			}
		} else if (e.getView().getTitle().equals("§lArea Counter")) {
			e.setCancelled(true);
			if (i != null && i.getItemMeta() != null && i.getItemMeta().getDisplayName() != null) {
				if (i.getItemMeta().getDisplayName().equals("§bNormal")) {
					Asteria.isReversalAreaSuicide = false;
					p.sendMessage(ChatColor.AQUA + "You have set your Area Counter to " + ChatColor.BLUE + "Normal!");
				} else if (i.getItemMeta().getDisplayName().equals("§bSuicide")) {
					Asteria.isReversalAreaSuicide = true;
					p.sendMessage(ChatColor.AQUA + "You have set your Area Counter to " + ChatColor.BLUE + "Suicide!");
				}
			}
		} else if (e.getView().getTitle().equals("§lDimensional Rooms")) {
			e.setCancelled(true);
			if (i != null && i.getItemMeta() != null && i.getItemMeta().getDisplayName() != null) {
				if (i.getItemMeta().getDisplayName().equals("§bDimensional Room")) {
					if (p.getWorld().getName().equals("rift") || p.getWorld().getName().equals("10thdimension") || p.getWorld().getName().equals("12thdimension") || Asteria.riftLocations.containsKey(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now!");
					else if (Asteria.dimensionalRoom.containsKey(p.getUniqueId())) {
						for (Entry<UUID, Location> ul : Asteria.dimensionalRoom.entrySet()) {
							Bukkit.getPlayer(ul.getKey()).teleport(ul.getValue());
							Bukkit.getPlayer(ul.getKey()).sendMessage(ChatColor.BLUE + "You have been warped back!");
						}
						Asteria.dimensionalRoom.clear();
					} else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Asteria.dimensionalRoom.put(p.getUniqueId(), p.getLocation());
						p.teleport(new Location(Bukkit.getWorld("dimensional_room"), 0.5, 81, 0.5));
						for (UUID u : Asteria.dimensionalRoomQueue) {
							if (!Bukkit.getPlayer(u).isDead()) {
								Asteria.dimensionalRoom.put(u, Bukkit.getPlayer(u).getLocation());
								Bukkit.getPlayer(u).teleport(new Location(Bukkit.getWorld("dimensional_room"), 0.5, 81, 0.5));
							}
						}
						Asteria.dimensionalRoomQueue.clear();
					}
				} else if (i.getItemMeta().getDisplayName().equals("§b12th Dimensional Physiology")) {
					if (p.getWorld().getName().equals("rift") || p.getWorld().getName().equals("10thdimension") || p.getWorld().getName().equals("dimensional_room") || Asteria.riftLocations.containsKey(p.getUniqueId()) || Asteria.riftLocations.containsKey(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now!");
					else if (!Asteria.hasAdvancement(p, "end/respawn_dragon"))
						p.sendMessage(ChatColor.RED + "You must unlock The End... Again? for this power!");
					else if (Asteria.dimensionalRoom.containsKey(p.getUniqueId())) {
						Location to = Asteria.dimensionalRoom.get(p.getUniqueId());
						int x = to.getBlockX() + (p.getLocation().getBlockX() * 50);
						int z = to.getBlockZ() + (p.getLocation().getBlockZ() * 50);
						Location tpTo = null;
						if (to.getWorld().getEnvironment().equals(Environment.NORMAL) || to.getWorld().getEnvironment().equals(Environment.THE_END)) {
							for (int y = to.getWorld().getMaxHeight(); y > 0; y--) {
								Block b = to.getWorld().getBlockAt(x, y, z);
								if (!b.getType().isAir()) {
									Location l = b.getLocation();
									l.setX(l.getX() + 0.5);
									l.setZ(l.getZ() + 0.5);
									l.setY(l.getY() + 1);
									tpTo = l;
									break;
								} else if (y == 1) {
									Location l = to.getWorld().getBlockAt(x, 80, z).getLocation();
									l.setX(l.getX() + 0.5);
									l.setZ(l.getZ() + 0.5);
									l.setY(l.getY() + 1);
									l.getBlock().getRelative(BlockFace.DOWN).setType(Material.DIRT);
									tpTo = l;
								}
							}
						} else {
							for (int y = 0; y < to.getWorld().getMaxHeight(); y++) {
								Block b = to.getWorld().getBlockAt(x, y, z);
								if (b.getType().isAir()) {
									b = to.getWorld().getBlockAt(x, y - 1, z);
									Location l = b.getLocation();
									l.setX(l.getX() + 0.5);
									l.setZ(l.getZ() + 0.5);
									l.setY(l.getY() + 1);
									tpTo = l;
									break;
								} else if (y == to.getWorld().getMaxHeight() - 1) {
									Location l = to.getWorld().getBlockAt(x, 70, z).getLocation();
									l.setX(l.getX() + 0.5);
									l.setZ(l.getZ() + 0.5);
									l.setY(l.getY() + 1);
									l.getBlock().getRelative(BlockFace.DOWN).setType(Material.DIRT);
									tpTo = l;
								}
							}
						}
						for (Entry<UUID, Location> ul : Asteria.dimensionalRoom.entrySet()) {
							Bukkit.getPlayer(ul.getKey()).teleport(tpTo);
							Bukkit.getPlayer(ul.getKey()).sendMessage(ChatColor.BLUE + "You have been teleported!");
						}
						Asteria.dimensionalRoom.clear();
					} else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Asteria.dimensionalRoom.put(p.getUniqueId(), p.getLocation());
						p.teleport(new Location(Bukkit.getWorld("12thdimension"), 0.5, 81, 0.5, p.getLocation().getYaw(), p.getLocation().getPitch()));
						for (UUID u : Asteria.dimensionalRoomQueue) {
							if (!Bukkit.getPlayer(u).isDead()) {
								Asteria.dimensionalRoom.put(u, Bukkit.getPlayer(u).getLocation());
								Bukkit.getPlayer(u).teleport(new Location(Bukkit.getWorld("12thdimension"), 0.5, 81, 0.5));
							}
						}
						Asteria.dimensionalRoomQueue.clear();
					}
				}
			}
		} else if (e.getView().getTitle().equals("§lTime Manipulation")) {
			e.setCancelled(true);
			if (i != null && i.getItemMeta() != null && i.getItemMeta().getDisplayName() != null) {
				if (i.getItemMeta().getDisplayName().equals("§bTime Slow")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownTimeManipulation, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownTimeManipulation, p);
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						boolean wasThereEntity = false;
						for (LivingEntity le : p.getWorld().getLivingEntities()) {
							if (!le.getUniqueId().equals(p.getUniqueId()) && le.getLocation().distance(p.getLocation()) < 20.5) {
								wasThereEntity = true;
								le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, 2));
								le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 600, 2));
								le.sendMessage(ChatColor.RED + "You have been given Slowness and Mining Fatigue!");
							}
						}
						if (wasThereEntity) {
							DustOptions pa = new Particle.DustOptions(Color.fromRGB(2, 2, 2), 1);
							p.getWorld().spawnParticle(Particle.REDSTONE, p.getEyeLocation(), 3000, 12, 12, 12, 1, pa, true);
							p.sendMessage(ChatColor.GRAY + "You have used §bTime Slow");
							Asteria.cooldownTimeManipulation = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(25); // 25 seconds
						} else
							p.sendMessage(ChatColor.RED + "There was no entities in a 20 block radius of you!");
					}
				} else if (i.getItemMeta().getDisplayName().equals("§bTime Speed")) {
					if (PowerUtils.isOnCooldownNew(Asteria.cooldownTimeManipulation, p))
						PowerUtils.sendCDMsgNew(Asteria.cooldownTimeManipulation, p);
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 600, 2));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 2));
						p.sendMessage(ChatColor.GRAY + "You have used §bTime Speed");
						Asteria.cooldownTimeManipulation = Instant.now().getEpochSecond() + PowerUtils.handleCooldown(25); // 25 seconds
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		if (Asteria.powerNames.contains(e.getOldCursor().getItemMeta().getDisplayName())) {
			int inventorySize = e.getInventory().getSize();
			for (int i : e.getRawSlots()) {
				if (i < inventorySize) {
					e.setCancelled(true);
					break;
				}
			}
		}
	}

}
