package net.randompvp.asteria.utils;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.libs.org.codehaus.plexus.util.FileUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import net.randompvp.asteria.Asteria;
import net.randompvp.asteria.powers.HostileConvergencePlayer;

public class PowerUtils {

	public static void setBlocksForWarp(Block b) {
		Block[] toStone = { b, b.getRelative(BlockFace.NORTH), b.getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.EAST), b.getRelative(BlockFace.WEST), b.getRelative(BlockFace.NORTH_EAST), b.getRelative(BlockFace.NORTH_WEST), b.getRelative(BlockFace.SOUTH_EAST), b.getRelative(BlockFace.SOUTH_WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.NORTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.NORTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.NORTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.NORTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.NORTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.NORTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST) };
		Block[] toAir = { b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST) };
		HashMap<Location, BlockData> temp = new HashMap<Location, BlockData>();
		for (Block bb : toStone) {
			if (!bb.getType().equals(Material.STONE) && !bb.getType().equals(Material.BEDROCK) && !bb.getType().equals(Material.END_PORTAL_FRAME)) {
				temp.put(bb.getLocation(), bb.getBlockData());
				bb.setType(Material.STONE);
			}
		}
		for (Block bb : toAir) {
			if (!bb.getType().equals(Material.AIR) && !bb.getType().equals(Material.BEDROCK) && !bb.getType().equals(Material.END_PORTAL_FRAME)) {
				temp.put(bb.getLocation(), bb.getBlockData());
				bb.setType(Material.AIR);
			}
		}
		Asteria.warpBlocks.add(temp);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Asteria.getPlugin(Asteria.class), () -> {
			for (Entry<Location, BlockData> blocks : temp.entrySet())
				blocks.getKey().getBlock().setBlockData(blocks.getValue());
			Asteria.warpBlocks.remove(temp);
		}, 600);
	}

	public static boolean isUnderSomething(Player p) {
		for (int i = p.getWorld().getMaxHeight(); i > p.getEyeLocation().getBlockY(); i--) {
			if (p.getWorld().getBlockAt(p.getLocation().getBlockX(), i, p.getLocation().getBlockZ()).getType().isSolid())
				return true;
		}
		return false;
	}

	public static void scheduleCooldown(HashMap<String, String> s) {
		for (HashMap.Entry<String, String> cd : s.entrySet()) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Asteria.getPlugin(Asteria.class), () -> s.remove(cd.getKey()), (Long.parseLong(cd.getValue()) - Instant.now().getEpochSecond()) * 20);
		}
	}

	public static void addCooldown(HashMap<String, String> s, Player p, long time) {
		s.put(p.getUniqueId().toString(), String.valueOf(Instant.now().getEpochSecond() + time));
		Bukkit.getScheduler().scheduleSyncDelayedTask(Asteria.getPlugin(Asteria.class), () -> s.remove(p.getUniqueId().toString()), time * 20);
	}

	public static long handleCooldown(long time) {
		return time;
	}

	public static boolean isOnCooldown(HashMap<String, String> cd, Player p) {
		return cd.containsKey(p.getUniqueId().toString()) && Long.parseLong(cd.get(p.getUniqueId().toString())) - Instant.now().getEpochSecond() > 0;
	}

	public static void sendCDMsg(HashMap<String, String> cd, Player p) {
		p.sendMessage("§cYou are on cooldown for " + Asteria.formatTime(Long.parseLong(cd.get(p.getUniqueId().toString())) - Instant.now().getEpochSecond()) + " more!");
	}

	public static boolean isOnCooldownNew(Long cd, Player p) {
		return cd - Instant.now().getEpochSecond() > 0;
	}

	public static void sendCDMsgNew(Long cd, Player p) {
		p.sendMessage("§cYou are on cooldown for " + Asteria.formatTime(cd - Instant.now().getEpochSecond()) + " more!");
	}

	public static boolean isInDimension(String name) {
		return name.equals("10thdimension") || name.equals("dimensional_room") || name.equals("12thdimension");
	}

	public static boolean shouldDisablePowerBySorcerer(Location l) {
		if (!Asteria.isSorcererActive)
			return false;
		Player p = Bukkit.getPlayer(Asteria.saby);
		if (p == null || (!l.getWorld().getName().equals(p.getWorld().getName()) && l.distance(p.getLocation()) > 30.5))
			return false;
		return true;
	}

	public static List<LivingEntity> getNearbyEntities(Location l, double size, UUID excempt) {
		List<LivingEntity> list = new ArrayList<LivingEntity>();
		for (LivingEntity le : l.getWorld().getLivingEntities()) {
			if (!le.getUniqueId().equals(excempt) && (le.getLocation().distance(l) < size || le.getEyeLocation().distance(l) < size))
				list.add(le);
		}
		return list;
	}

	public static void endBerserker() {
		Player p = Bukkit.getPlayer(Asteria.hany);
		p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20 * Asteria.hanyPowerMultiplier);
		p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		p.removePotionEffect(PotionEffectType.REGENERATION);
		p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
		p.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0);
		p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
		Asteria.isBerserkerActive = false;
		Bukkit.getScheduler().cancelTask(Asteria.berserkerId);
	}

	public static void endPowersWithSeal(Player p, Player pl, String name) {
		if (pl.getUniqueId().equals(Asteria.hany) && Asteria.isBerserkerActive) {
			pl.sendMessage(ChatColor.RED + "Your §bBerserker§c has been canceled by " + name + "§c!");
			p.sendMessage(ChatColor.GREEN + "You deactivated §bBerserker§a with " + name + "§c!");
			Bukkit.getScheduler().cancelTask(Asteria.berserkerEndId);
			endBerserker();
		} else if (pl.getUniqueId().equals(Asteria.jonathan)) {
			if (Asteria.starshieldActive) {
				pl.sendMessage(ChatColor.RED + "Your §6Star Shield§c has been canceled by " + name + "§c!");
				p.sendMessage(ChatColor.GREEN + "You deactivated §6Star Shield§a with " + name + "§c!");
				Asteria.starshieldActive = false;
				Bukkit.getScheduler().cancelTask(Asteria.starShieldId);
			} else if (Asteria.wrathId != 0) {
				pl.sendMessage(ChatColor.RED + "Your §6Wrath of Asteria§c has been canceled by " + name + "§c!");
				p.sendMessage(ChatColor.GREEN + "You deactivated §6Wrath of Asteria§a with " + name + "§c!");
				Bukkit.getScheduler().cancelTask(Asteria.wrathId);
				if (Asteria.thirtySecId != 0) {
					Bukkit.getScheduler().cancelTask(Asteria.thirtySecId);
					Asteria.thirtySecId = 0;
				}
				boolean foundItem = false;
				for (ItemStack iss : p.getInventory().getContents()) {
					if (iss != null && iss.getItemMeta().getDisplayName() != null && iss.getItemMeta().getDisplayName().equals("§6Bane of Darkness")) {
						pl.getInventory().remove(iss);
						foundItem = true;
					}
				}
				if (!foundItem && pl.getItemOnCursor() != null && pl.getItemOnCursor().getItemMeta() != null && pl.getItemOnCursor().getItemMeta().getDisplayName() != null && pl.getItemOnCursor().getItemMeta().getDisplayName().equals("§6Bane of Darkness")) {
					pl.getInventory().remove(pl.getItemOnCursor());
					foundItem = true;
				}
				Asteria.hasUsedBane = false;
				Asteria.hasUsedBaneAfter = false;
				Asteria.hasthirtysecpassed = false;
				if (!foundItem)
					Bukkit.getLogger().warning("Could not remove Bane of Darkness from " + pl.getName() + "'s inventory");
				if (Asteria.baneId != 0) {
					Bukkit.getScheduler().cancelTask(Asteria.baneId);
					Asteria.baneId = 0;
				}
				Asteria.thirtySecId = 0;
				Asteria.wrathId = 0;
			}
		} else if (pl.getUniqueId().equals(Asteria.ed) && Asteria.isSpatialBlockActive) {
			pl.sendMessage(ChatColor.RED + "Your §6Spatial Manipulation§c has been canceled by " + name + "§c!");
			p.sendMessage(ChatColor.GREEN + "You deactivated §6Spatial Manipulation§a with " + name + "§c!");
			Asteria.isSpatialBlockActive = false;
			pl.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
			Asteria.canSpatialBlockPlace = false;
			if (Asteria.lastSpatialBlock != null) {
				Asteria.lastSpatialBlock.getBlock().setType(Material.AIR);
				Asteria.lastSpatialBlock = null;
			}
		} else if (pl.getUniqueId().equals(Asteria.saby) && Asteria.isHellActive) {
			pl.sendMessage(ChatColor.RED + "Your §6Hellstorm§c has been canceled by " + name + "§c!");
			p.sendMessage(ChatColor.GREEN + "You deactivated §6Hellstorm§a with " + name + "§c!");
			Bukkit.getScheduler().cancelTask(Asteria.hellStormId);
			Bukkit.getScheduler().cancelTask(Asteria.hellStormEndId);
			Asteria.isHellActive = false;
		} else if (pl.getUniqueId().equals(Asteria.jose) && Asteria.isWrathSeaActive) {
			Bukkit.getScheduler().cancelTask(Asteria.invicibleId);
			Bukkit.getScheduler().cancelTask(Asteria.wrathSeaId);
			Asteria.isWrathSeaActive = false;
			pl.sendMessage(ChatColor.RED + "Your §bWrath of the Sea§c has been canceled by " + name + "§c!");
			p.sendMessage(ChatColor.GREEN + "You deactivated §bWrath of the Sea§a with " + name + "§c!");
		} else if (pl.getUniqueId().equals(Asteria.brent)) {
			if (Asteria.poisonBreath != null) {
				Bukkit.getScheduler().cancelTask(Asteria.poisonBreathId);
				Asteria.poisonBreath = null;
				pl.sendMessage(ChatColor.RED + "Your §6Poisonous Breath§c has been canceled by " + name + "§c!");
				p.sendMessage(ChatColor.GREEN + "You deactivated §6Poisonous Breath§a with " + name + "§c!");
			} else if (Asteria.isBlackHoleActive) {
				Bukkit.getScheduler().cancelTask(Asteria.blackHoleEndId);
				Bukkit.getScheduler().cancelTask(Asteria.blackHoleId);
				Asteria.isBlackHoleActive = false;
				pl.sendMessage(ChatColor.RED + "Your §6Black Hole§c has been canceled by " + name + "§c!");
				p.sendMessage(ChatColor.GREEN + "You deactivated §6Black Hole§a with " + name + "§c!");
			} else if (Asteria.isPhantomBurstActive) {
				Bukkit.getScheduler().cancelTask(Asteria.phantomBurstEndId);
				Bukkit.getScheduler().cancelTask(Asteria.phantomBurstId);
				Asteria.isPhantomBurstActive = false;
				pl.sendMessage(ChatColor.RED + "Your §6Phantom Burst§c has been canceled by " + name + "§c!");
				p.sendMessage(ChatColor.GREEN + "You deactivated §6Phantom Burst§a with " + name + "§c!");
			}
		} else if (pl.getUniqueId().equals(Asteria.eli) && Asteria.isDawnOfLightActive) {
			Bukkit.getScheduler().cancelTask(Asteria.dawnOfLightEndId);
			Bukkit.getScheduler().cancelTask(Asteria.dawnOfLightId);
			Asteria.isDawnOfLightActive = false;
			pl.sendMessage(ChatColor.RED + "Your §bDawn of Light§c has been canceled by " + name + "§c!");
			p.sendMessage(ChatColor.GREEN + "You deactivated §bDawn of Light§a with " + name + "§c!");
		}
	}

	public static boolean shouldDisablePowerBySeal(UUID u) {
		if ((Asteria.playerOnMagicSeal != null && Asteria.playerOnMagicSeal.equals(u)) || (Asteria.catastrophicSealPlayer != null && Asteria.catastrophicSealPlayer.equals(u)))
			return true;
		return false;
	}

	public static HostileConvergencePlayer getHostileConvergencePlayer(UUID u) {
		for (HostileConvergencePlayer hpl : Asteria.hostileConvergencePlayers) {
			if (hpl.getUUID().equals(u))
				return hpl;
		}
		return null;
	}

	public static void hanyDamage(Player hany, LivingEntity target, double finalDamage) {
		if (Asteria.hasAdvancement(hany, "story/enter_the_nether")) {
			int length = Asteria.hasAdvancement(hany, "nether/brew_potion") ? 200 : 100;
			if (Asteria.isBerserkerActive)
				length = (int) (length * 2.5);
			target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, (int) (length * Asteria.hanyPowerMultiplier), (int) (3 * Asteria.hanyPowerMultiplier)));
		}
		if (hany.getHealth() + finalDamage + 1 > hany.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())
			hany.setHealth(hany.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		else
			hany.setHealth(hany.getHealth() + finalDamage + 1);
	}

	public static void endRift() {
		Player pl = Bukkit.getPlayer(Asteria.ed);
		Asteria.riftId = 0;
		for (Entry<UUID, Location> ul : Asteria.riftLocations.entrySet()) {
			Player p = Bukkit.getPlayer(ul.getKey());
			if (!p.getUniqueId().equals(Asteria.ed) || (p.getUniqueId().equals(Asteria.ed) && !Asteria.riftInOverworld)) {
				p.teleport(ul.getValue());
				p.sendMessage(ChatColor.DARK_PURPLE + "You have been warped back!");
			}
		}
		Asteria.riftLocations.clear();
		Asteria.riftTarget.clear();
		if (Asteria.riftInSpec) {
			Asteria.riftInSpec = false;
			pl.removePotionEffect(PotionEffectType.INVISIBILITY);
			pl.setFlying(false);
			pl.setAllowFlight(false);
			Bukkit.getScheduler().cancelTask(Asteria.riftSpecId);
			Asteria.riftSpecId = 0;
		} else if (Asteria.riftInOverworld) {
			Asteria.riftInOverworld = false;
			Bukkit.getScheduler().cancelTask(Asteria.riftOverworldId);
			Asteria.riftOverworldId = 0;
		}
		if (Bukkit.unloadWorld("rift", false)) {
			try {
				FileUtils.deleteDirectory("./rift");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else
			Bukkit.getLogger().warning("Rift was unable to unlod, will try to unload next time Ed uses his power");
	}

	public static String getCDirection(float f) {
		if (f > 0) {
			if (f >= 135 && f <= 225)
				return "N";
			else if (f >= 225 && f <= 315)
				return "E";
			else if (f >= 315 || f <= 45)
				return "S";
			else
				return "W";
		} else {
			if (f <= -135 && f >= -225)
				return "N";
			else if (f <= -225 && f >= -315)
				return "W";
			else if (f <= -315 || f >= -45)
				return "S";
			else
				return "E";
		}
	}
	
	public static int hanyWitherCount() {
		int i = 0;
		for (World wo : Bukkit.getWorlds()) {
			for (LivingEntity le : wo.getLivingEntities()) {
				if (le.getCustomName() != null && (le.getCustomName().equals("§6Withered Knights") || le.getCustomName().equals("§6§lRoyal Guard")))
					i++;
			}
		}
		return i;
	}

}
