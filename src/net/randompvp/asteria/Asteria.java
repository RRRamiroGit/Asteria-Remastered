package net.randompvp.asteria;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.codehaus.plexus.util.FileUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.randompvp.asteria.listeners.BlockListeners;
import net.randompvp.asteria.listeners.EntityListeners;
import net.randompvp.asteria.listeners.InventoryListeners;
import net.randompvp.asteria.listeners.PlayerListeners;
import net.randompvp.asteria.powers.HostileConvergencePlayer;
import net.randompvp.asteria.utils.PowerUtils;

public class Asteria extends JavaPlugin {
	Random r = new Random();
	static File dataFolder = new File("plugins", "AsteriaSMP");
	public static UUID hany = UUID.fromString("22f40a79-3ed4-4c90-b922-105ce4f8d473");
	public static UUID ed = UUID.fromString("700fd2d3-3bb6-47a8-b9dd-aeefc47ee159");
	public static UUID saby = UUID.fromString("63e461d7-9450-4148-9a21-ad205d48015e");
	public static UUID ramiro = UUID.fromString("e03b7c40-7ee0-4228-b558-7b251729ec53");
	public static UUID jonathan = UUID.fromString("ea41a057-5cf7-47f4-b7cf-e67174516f2f");
	public static UUID jose = UUID.fromString("c42d81de-11e0-494c-95f2-35049d994483");
	public static UUID eli = UUID.fromString("bfb97cfe-e7c6-4486-903c-d20fa7c08c51");
	public static UUID brent = UUID.fromString("54b8f69f-5992-46ae-b76d-208dfd82bebd");
	public static UUID oz = UUID.fromString("dd5f36b8-643e-40f5-b0fa-5f0a5287e004");
	static String[] powerNamesar = { "§6Wither Turret / Dash", "§bParalysis", "§6Alert / Double Jump", "§6Soul's of the Damned", "§6Royal Guard", "§6Dimensional Rooms", "§6Dimension Jump", "§bDimensional Shift", "§6Spatial Manipulation", "§610th Dimensional Physiology", "§6Excalibur", "§bFire Breath", "§bNight Terror", "§6Energy Convergence", "§6Rift", "§6Shooting Star", "§6Distortion", "§6Eruption", "§6Supernova", "§6Star Shield", "§6Wrath of Asteria", "§6Bane of Darkness", "§6Withered Allegiance", "§6Caliburn", "§6Neptune", "§6Raging Storm", "§bStomp", "§bCalling Conch", "§bClimate Change", "§bAqua Blast", "§bWrath of the Sea", "§bArea Counter", "§6Screech", "§6Dark Shift", "§6Blood Bite", "§6Return", "§6Poisonous Breath", "§6Black Hole", "§6Phantom Burst", "§bSnowstorm", "§6Hellstorm", "§6Spectral Shift", "§6Conjuring Shot", "§6Divine Light", "§6Time Manipulation", "§6Sorcerer's State", "§bParalysis", "§6Electrokinesis", "§bUmbrakinesis", "§bDawn of Light", "§bShape Shift", "§bBlades of Exile", "§bLeviathan Axe", "§6Magic Seal", "§bAlmighty Push", "§bFull Counter", "§bCatastrophic Seal", "§bThe End Gates", "§bStygius", "§bHostile Convergence", "§bFortuna Flip" };
	public static List<String> powerNames = Arrays.asList(powerNamesar);
	public static boolean canQueue = true;
	public static boolean isRestarting = false;
	public static HashSet<Integer> restartTasks = new HashSet<>();
	public static HashMap<String, String> reply = new HashMap<String, String>();
	public static HashMap<Integer, HashMap<Location, BlockData>> obbyBl = new HashMap<Integer, HashMap<Location, BlockData>>();
	public static int obbyBlNum = 0;
	public static HashMap<UUID, Integer> wither = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Byte> witherCooldownCount = new HashMap<UUID, Byte>();
	public static HashSet<UUID> witherEmbraceMove = new HashSet<UUID>();
	public static HashSet<UUID> stomp = new HashSet<UUID>();
	public static HashMap<UUID, Integer> invis = new HashMap<UUID, Integer>();
	public static HashMap<Integer, HashSet<UUID>> frozen = new HashMap<Integer, HashSet<UUID>>();
	public static int frozenNum = 0;
	public static HashMap<UUID, List<ItemStack>> deathItems = new HashMap<UUID, List<ItemStack>>();
	public static HashMap<String, String> cooldownStrengthsword = new HashMap<String, String>();
	public static HashMap<String, String> cooldownWither = coolDownFileReader("wither");
	public static HashMap<String, String> cooldownStomp = coolDownFileReader("stomp");
	public static HashMap<String, String> cooldownParalysis = coolDownFileReader("paralysis");
	public static HashMap<String, String> cooldownFirebreath = coolDownFileReader("firebreath");
	public static Long cooldownAlert = 0L;
	public static Long cooldownEnergyConvergence = 0L;
	public static Long cooldownRift = 0L;
	public static Long cooldownStarShot = 0L;
	public static Long cooldownDistortion = 0L;
	public static Long cooldownEruption = 0L;
	public static Long cooldownSupernova = 0L;
	public static Long cooldownStarshield = 0L;
	public static Long cooldownWrath = 0L;
	public static Long cooldownRagingStorm = 0L;
	public static Long cooldownCallingConch = 0L;
	public static Long cooldownClimateChange = 0L;
	public static Long cooldownAquaBlast = 0L;
	public static Long cooldownWrathSea = 0L;
	public static Long cooldownRiftSpec = 0L;
	public static Long cooldownRiftOverworld = 0L;
	public static Long cooldown10thDimensional = 0L;
	public static Long cooldownAreaReversal = 0L;
	public static Long cooldownScreech = 0L;
	public static Long cooldownDarkShift = 0L;
	public static Long cooldownBloodBite = 0L;
	public static Long cooldownReturn = 0L;
	public static Long cooldownPoisonBreath = 0L;
	public static Long cooldownBlackHole = 0L;
	public static Long cooldownPhantomBurst = 0L;
	public static Long cooldownElectro = 0L;
	public static Long cooldownUmbrakinesis = 0L;
	public static Long cooldownDawnOfLight = 0L;
	public static Long cooldownShapeShift = 0L;
	public static Long cooldownBladesofExile = 0L;
	public static Long cooldownLeviathanAxe = 0L;
	public static Long cooldownMagicSeal = 0L;
	public static Long cooldownAlmightlyPush = 0L;
	public static Long cooldownFullCounter = 0L;
	public static Long cooldownCatastrophicSeal = 0L;
	public static Long cooldownEndGates = 0L;
	public static Long cooldownBerserker = 0L;
	public static Long cooldownStygius = 0L;
	public static Long cooldownSnowstorm = 0L;
	public static Long cooldownHellstorm = 0L;
	public static Long cooldownSpectralShift = 0L;
	public static Long cooldownConjuringShot = 0L;
	public static Long cooldownDivineLight = 0L;
	public static Long cooldownTimeManipulation = 0L;
	public static Long cooldownRagnarok = readPower("ragnarok");
	public static HashMap<LivingEntity, Integer> witherai = new HashMap<LivingEntity, Integer>();
	public static HashMap<UUID, Location> dimensionalRoom = new HashMap<UUID, Location>();
	public static HashSet<UUID> dimensionalRoomQueue = new HashSet<UUID>();
	public static HashSet<UUID> dimensionalWarpQueue = new HashSet<UUID>();
	public static boolean doesPotionAffect = false;
	public static Location lastSpatialBlock;
	public static boolean canSpatialBlockPlace = false;
	public static boolean isSpatialBlockActive = false;
	public static HashMap<UUID, Integer> toTeleportToHany = new HashMap<UUID, Integer>();
	public static ArrayList<HashMap<Location, BlockData>> warpBlocks = new ArrayList<HashMap<Location, BlockData>>();
	public static HashSet<UUID> inFightWithHany = new HashSet<UUID>();
	public static ArrayList<Double> witherSkeletonQueue = new ArrayList<Double>();
	public static ArrayList<Integer> witherSkeletonQueueId = new ArrayList<Integer>();
	public static HashMap<UUID, Location> tenthdimensionLocations = new HashMap<UUID, Location>();
	public static ArrayList<Double> witherQueue = new ArrayList<Double>();
	public static ArrayList<Integer> witherQueueId = new ArrayList<Integer>();
	public static long whenStopDimensional = 0;
	public static int dimensionId = 0;
	public static HashSet<Integer> dimensionIds = new HashSet<Integer>();
	public static HashSet<UUID> alertPlayers = new HashSet<UUID>();
	public static ArrayList<ItemStack> tenthDimensionItems = new ArrayList<ItemStack>();
	public static boolean secondChanceCooldown = false;
	public static boolean isTakeDamageDoubleJump = false;
	public static boolean isTakeDamageWitherDash = false;
	public static HashMap<UUID, Location> riftLocations = new HashMap<UUID, Location>();
	public static HashMap<UUID, UUID> riftTarget = new HashMap<UUID, UUID>();
	public static int riftId = 0;
	public static int starShotCooldownCount = 0;
	public static boolean overdriveCooldown = false;
	public static boolean starshieldActive = false;
	public static int wrathId = 0;
	public static int baneId = 0;
	public static String whatMobHanySelected = "zombie";
	public static EntityType mobHanySelected = EntityType.ZOMBIE;
	public static boolean cancelSpawning = false;
	public static boolean hasUsedBane = false;
	public static boolean hasUsedBaneAfter = false;
	public static boolean hasthirtysecpassed = false;
	public static int thirtySecId = 0;
	public static HashMap<EntityType, ArrayList<Double>> withersallegianceQueue = new HashMap<EntityType, ArrayList<Double>>();
	public static HashMap<EntityType, ArrayList<Integer>> withersallegianceQueueId = new HashMap<EntityType, ArrayList<Integer>>();
	public static boolean isOneShotWither = false;
	public static boolean stopExplosionHany = false;
	public static boolean shootWitherSkulls = true;
	public static boolean canAllegianceTeleportToHany = true;
	public static boolean canWitherTeleportToHany = true;
	public static Double reversalDamage = readReversal();
	public static boolean isReversalActive = false;
	public static int clicksForReversal = 0;
	public static int cancelClicksId = 0;
	public static boolean isWrathSeaActive = false;
	public static boolean joseInvincible = false;
	public static boolean wrathSeaStrikeCooldown = false;
	public static boolean riftInSpec = false;
	public static boolean canUseOpRift = true;
	public static int canUseOpRiftId = 0;
	public static int riftSpecId = 0;
	public static boolean riftInOverworld = false;
	public static int riftOverworldId = 0;
	public static boolean disableRegen = false;
	public static boolean isReversalAreaSuicide = false;
	public static ArrayList<UUID> halfDamage = new ArrayList<UUID>();
	public static Location poisonBreath = null;
	public static boolean isBlackHoleActive = false;
	public static boolean isPhantomBurstActive = false;
	public static boolean isHellActive = false;
	public static boolean isSorcererActive = false;
	public static int SorcererId = 0;
	public static HashMap<UUID, Integer> reversalDamageId = new HashMap<UUID, Integer>();
	public static boolean sorcererRayCooldown = false;
	public static boolean isDawnOfLightActive = false;
	public static boolean isShapeShiftActive = false;
	public static boolean isMagicSealActive = false;
	public static UUID playerOnMagicSeal = null;
	public static int starShieldId = 0;
	public static int hellStormId = 0;
	public static int hellStormEndId = 0;
	public static int wrathSeaId = 0;
	public static int invicibleId = 0;
	public static int poisonBreathId = 0;
	public static int blackHoleId = 0;
	public static int blackHoleEndId = 0;
	public static int phantomBurstEndId = 0;
	public static int phantomBurstId = 0;
	public static int dawnOfLightId = 0;
	public static int dawnOfLightEndId = 0;
	public static boolean isFullCounterActive = false;
	public static int fullCounterId = 0;
	public static int fullCounterEndId = 0;
	public static double fullCounterDamage = 0;
	public static boolean isFinalFullCounterActive = false;
	public static int fullCounterFinalEndId = 0;
	public static int shotsSinceFullCounter = 0;
	public static boolean isEndGatesActive = false;
	public static boolean isEndGatesFullyActive = false;
	public static ArrayList<Location> endGateLocations = new ArrayList<Location>();
	public static boolean isBerserkerActive = false;
	public static int berserkerId = 0;
	public static int berserkerEndId = 0;
	public static Location locOfCatastrophicSeal = null;
	public static UUID catastrophicSealPlayer = null;
	public static int catastrophicSealId = 0;
	public static boolean isHostileConvergenceActive = false;
	public static ArrayList<HostileConvergencePlayer> hostileConvergencePlayers = new ArrayList<HostileConvergencePlayer>();
	public static double hanyPowerMultiplier = 1;
	public static int chanceForFortune = readFortune();
	public static int fortuneTask = 0;
	public static HashSet<UUID> endAdvancements = readArray("endAdvancements");

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerListeners(Asteria.getPlugin(this.getClass())), this);
		pm.registerEvents(new InventoryListeners(Asteria.getPlugin(this.getClass())), this);
		pm.registerEvents(new EntityListeners(Asteria.getPlugin(this.getClass())), this);
		pm.registerEvents(new BlockListeners(Asteria.getPlugin(this.getClass())), this);
		scheduleRestart();
		PowerUtils.scheduleCooldown(cooldownWither);
		PowerUtils.scheduleCooldown(cooldownStomp);
		PowerUtils.scheduleCooldown(cooldownParalysis);
		PowerUtils.scheduleCooldown(cooldownFirebreath);
		enableLoops();
		if (new File("./rift").exists()) {
			try {
				FileUtils.deleteDirectory("./rift");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		WorldCreator dm = new WorldCreator("dimensional_room");
		dm.environment(Environment.THE_END);
		Bukkit.createWorld(dm);
		WorldCreator tenth = new WorldCreator("10thdimension");
		tenth.environment(Environment.THE_END);
		Bukkit.createWorld(tenth);
		WorldCreator twelfth = new WorldCreator("12thdimension");
		twelfth.environment(Environment.THE_END);
		Bukkit.createWorld(twelfth);
	}

	@Override
	public void onDisable() {
		writeToFile(cooldownWither, "wither");
		writeToFile(cooldownStomp, "stomp");
		writeToFile(cooldownParalysis, "paralysis");
		writeToFile(cooldownFirebreath, "firebreath");
		for (HashMap.Entry<Integer, HashMap<Location, BlockData>> blit : obbyBl.entrySet()) {
			for (HashMap.Entry<Location, BlockData> blitt : blit.getValue().entrySet())
				blitt.getKey().getBlock().setBlockData(blitt.getValue());
		}
		writeToFileNew(new File(getDataFolder(), "reversalDamage"), reversalDamage.toString());
		writeToFileNew(new File(getDataFolder(), "fortuneChance"), String.valueOf(cooldownRagnarok));
		writeToFileNew(new File(getDataFolder(), "ragnarok"), String.valueOf(cooldownRagnarok));
		String endAdvancementsString = ",";
		for (UUID u : endAdvancements)
			endAdvancementsString = endAdvancementsString + u.toString() + ",";
		writeToFileNew(new File(getDataFolder(), "endAdvancements"), endAdvancementsString);
		for (Entry<UUID, Integer> iv : invis.entrySet()) {
			Player p = Bukkit.getPlayer(iv.getKey());
			p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			p.removePotionEffect(PotionEffectType.INVISIBILITY);
			for (Player a : Bukkit.getOnlinePlayers())
				a.showPlayer(this, p);
		}
		for (HashMap<Location, BlockData> blocks : warpBlocks) {
			for (Entry<Location, BlockData> blockss : blocks.entrySet())
				blockss.getKey().getBlock().setBlockData(blockss.getValue());
		}
		if (isSpatialBlockActive) {
			isSpatialBlockActive = false;
			Bukkit.getPlayer(ed).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
			canSpatialBlockPlace = false;
			if (lastSpatialBlock != null) {
				lastSpatialBlock.getBlock().setType(Material.AIR);
				lastSpatialBlock = null;
			}
		}
		for (Entry<UUID, Location> ul : tenthdimensionLocations.entrySet())
			Bukkit.getPlayer(ul.getKey()).teleport(ul.getValue());
		for (ItemStack is : tenthDimensionItems)
			tenthdimensionLocations.get(ed).getWorld().dropItem(tenthdimensionLocations.get(ed), is);
		if (wrathId != 0) {
			Player p = Bukkit.getPlayer(jonathan);
			if (thirtySecId != 0) {
				Bukkit.getScheduler().cancelTask(thirtySecId);
				thirtySecId = 0;
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
			hasUsedBane = false;
			hasUsedBaneAfter = false;
			hasthirtysecpassed = false;
			if (baneId != 0) {
				Bukkit.getScheduler().cancelTask(baneId);
				baneId = 0;
			}
			wrathId = 0;
		}
		if (riftLocations.size() != 0)
			PowerUtils.endRift();
		if (Bukkit.getPlayer(hany) != null && isBerserkerActive) {
			Bukkit.getScheduler().cancelTask(berserkerEndId);
			PowerUtils.endBerserker();
		}
	}

	void scheduleRestart() {
		long secondsMidnight = Duration.between(ZonedDateTime.now().toLocalDate().atStartOfDay(ZonedDateTime.now().getZone()).toInstant(), Instant.now()).getSeconds();
		long restartTime;
		if (secondsMidnight > 10800)
			restartTime = 86400 - secondsMidnight + 10800 + 86400;
		else
			restartTime = 10800 - secondsMidnight + 86400;
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> scheduleRestartCountdown(), restartTime * 20);
	}

	void scheduleRestartCountdown() {
		isRestarting = true;
		Bukkit.broadcastMessage("§bServer restarting in 10 minutes.");
		restartTasks.add(Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.broadcastMessage("§bServer restarting in 5 minutes."), 6000L));
		restartTasks.add(Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.broadcastMessage("§bServer restarting in 1 minute."), 10800L));
		restartTasks.add(Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.broadcastMessage("§bServer restarting in 30 seconds."), 11400L));
		restartTasks.add(Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.broadcastMessage("§bServer restarting in 15 seconds."), 11700L));
		restartTasks.add(Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.broadcastMessage("§bServer restarting in 10 seconds."), 11800L));
		restartTasks.add(Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.broadcastMessage("§bServer restarting in 5 seconds."), 11900L));
		restartTasks.add(Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.broadcastMessage("§bServer restarting in 3 seconds."), 11940L));
		restartTasks.add(Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.broadcastMessage("§bServer restarting in 2 seconds."), 11960L));
		restartTasks.add(Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.broadcastMessage("§bServer restarting in 1 second."), 11980L));
		restartTasks.add(Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
			for (Player a : Bukkit.getOnlinePlayers())
				a.kickPlayer("Server is restarting, rejoin soon!");
			Bukkit.getServer().shutdown();
		}, 12000L));
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("r")) {
			if (args.length == 0) {
				p.sendMessage("Usage: /r <message>");
			} else {
				if (reply.containsKey(p.getName())) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < args.length; i++) {
						sb.append(args[i]).append(" ");
					}
					String allArgs = sb.toString().trim();
					p.performCommand("w " + reply.get(p.getName()) + " " + allArgs);
				} else {
					p.sendMessage("You aren't talking to anybody.");
				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("cancelrestart")) {
			if (!p.isOp())
				p.sendMessage(ChatColor.RED + "You can't do this!");
			else {
				Bukkit.broadcastMessage("Operator has canceled automatic restart. Another restart scheduled in 20 minutes.");
				isRestarting = false;
				Iterator<Integer> it1 = restartTasks.iterator();
				while (it1.hasNext()) {
					Bukkit.getScheduler().cancelTask(it1.next());
				}
				restartTasks.clear();
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> scheduleRestartCountdown(), 12000L);
			}
		}
		if (cmd.getName().equalsIgnoreCase("shrug")) {
			if (args.length == 0) {
				p.chat("¯\\_(ツ)_/¯");
			} else {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < args.length; i++) {
					sb.append(args[i]).append(" ");
				}
				String allArgs = sb.toString().trim();
				p.chat(allArgs + " ¯\\_(ツ)_/¯");
			}
		}
		return false;
	}

	public static HashMap<String, String> coolDownFileReader(String fileName) {
		File myObj = new File(".//plugins/Powers/cooldown" + fileName + ".txt");
		File dir = new File(".//plugins/Powers");
		try {
			Scanner myReader = new Scanner(myObj);
			String data = myReader.nextLine();
			myReader.close();
			data = data.substring(1, data.length() - 1);
			String[] keyValuePairs = data.split(",");
			HashMap<String, String> map = new HashMap<String, String>();
			for (String pair : keyValuePairs) {
				String[] entry = pair.split("=");
				if (entry.length == 2)
					map.put(entry[0].trim(), entry[1].trim());
			}
			return map;
		} catch (FileNotFoundException e) {
			dir.mkdirs();
			try {
				myObj.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			writeToFile(new HashMap<String, String>(), fileName);
		}
		return new HashMap<String, String>();
	}

	public static String formatTime(Long s) {
		if (s < 60)
			return s + "s";
		else if (s < 3600) {
			if (s % 60 == 0)
				return s / 60 + "m";
			else
				return s / 60 + "m " + s % 60 + "s";
		} else {
			if ((s / 60 % 60 == 0) && (s % 60 == 0))
				return s / 3600 + "h";
			else if (s % 60 == 0)
				return s / 3600 + "h " + s % 3600 / 60 + "m";
			else
				return s / 3600 + "h " + s % 3600 / 60 + "m " + s % 3600 % 60 + "s";
		}
	}

	public void enableLoops() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for (World w : Bukkit.getWorlds()) {
				for (Entity en : w.getEntities()) {
					if (en.getType().equals(EntityType.WITHER_SKULL)) {
						WitherSkull ws = (WitherSkull) en;
						if (ws.getShooter() != null && ws.getShooter() instanceof Wither) {
							Wither wither = (Wither) ws.getShooter();
							if (wither.getName().equals("§6§lRoyal Guard") && wither.getTarget() == null)
								ws.remove();
						}
					}
				}
			}
		}, 0, 1);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for (Player a : Bukkit.getOnlinePlayers()) {
				if (a.getUniqueId().equals(hany) && (a.getLocation().getBlock().getType().equals(Material.SOUL_SAND) || a.getLocation().getBlock().getType().equals(Material.SOUL_SOIL) || a.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.SOUL_SAND) || a.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.SOUL_SOIL)))
					a.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, 1));
				else if (a.getUniqueId().equals(jonathan) && wrathId != 0) {
					a.getWorld().spawnParticle(Particle.WHITE_ASH, a.getLocation(), 20, 0.2, 0, 0.2, 0.9);
				} else if (a.getUniqueId().equals(ed) && hasAdvancement(a, "story/mine_diamond")) {
					String bold = "";
					if (reversalDamage / 75 > 100)
						bold = "§l";
					String cooldownText = PowerUtils.isOnCooldownNew(cooldownEnergyConvergence, a) ? formatTime(cooldownEnergyConvergence - Instant.now().getEpochSecond()) : "§aNo cooldown!";
					a.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§e§lCounter Damage: §6" + bold + String.valueOf(new DecimalFormat("#.##").format(reversalDamage / 0.75)) + "% §r| §e§lEC Status: §6" + cooldownText));
					if (!invis.containsKey(ed) && !a.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
						DustOptions pa = new Particle.DustOptions(Color.fromRGB(114, 137, 218), 1);
						Double dis = reversalDamage / 220;
						if (dis > 0.3)
							dis = 0.3D;
						int ptcles = (int) Math.round(reversalDamage) * 5;
						if (ptcles > 350)
							ptcles = 350;
						for (Player aa : Bukkit.getOnlinePlayers()) {
							if (aa.getWorld().getName().equals(a.getWorld().getName()) && a.getLocation().distance(aa.getLocation()) < 100 && !a.getUniqueId().equals(aa.getUniqueId()))
								aa.spawnParticle(Particle.REDSTONE, a.getLocation(), ptcles, dis, dis, dis, 1, pa);
						}
					}
				} else if (a.getUniqueId().equals(jose)) {
					double maxhealth = hasAdvancement(a, "story/enter_the_end") ? 28 : 20;
					if ((a.getLocation().getBlock().getType().equals(Material.WATER) || a.getLocation().getBlock().getRelative(BlockFace.UP).getType().equals(Material.WATER)) || (!a.getWorld().isClearWeather() && !PowerUtils.isUnderSomething(a))) {
						if (hasAdvancement(a, "nether/brew_potion"))
							a.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 40, 0));
						if (hasAdvancement(a, "story/obtain_armor")) {
							if (a.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() != maxhealth + 4)
								a.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxhealth + 4);
							a.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 1));
						}
					} else if (a.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() != maxhealth)
						a.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxhealth);
				}

			}
		}, 0, 2);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for (Player a : Bukkit.getOnlinePlayers()) {
				if (a.getUniqueId().equals(hany)) {
					a.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 0));
					if (a.getHealth() < 6) {
						a.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30, (int) (1 * hanyPowerMultiplier)));
						a.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30, hanyPowerMultiplier == 2 ? 1 : 0));
						a.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, (int) (1 * hanyPowerMultiplier)));
					} else if (hasAdvancement(a, "nether/brew_potion"))
						a.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, hanyPowerMultiplier == 2 ? 1 : 0));
				} else if (a.getUniqueId().equals(saby)) {
					a.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 1, false, false));
					if (a.getLocation().getBlock().getLightLevel() < 6 && !a.getWorld().getEnvironment().equals(Environment.THE_END))
						a.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 0, false, false));
				} else if (a.getUniqueId().equals(ramiro)) {
					a.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 60, 0));
					if (hasAdvancement(a, "story/enter_the_nether"))
						a.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 0));
					if (hasAdvancement(a, "story/shiny_gear"))
						a.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 0));
					if (hasAdvancement(a, "story/enter_the_end"))
						a.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 0));
					if (hasAdvancement(a, "nether/explore_nether")) {
						for (Player aa : Bukkit.getOnlinePlayers()) {
							if (aa.getUniqueId().equals(a.getUniqueId()))
								continue;
							boolean contains = alertPlayers.contains(aa.getUniqueId());
							if (!aa.getWorld().getName().equals(a.getWorld().getName()) && contains) {
								alertPlayers.remove(aa.getUniqueId());
								a.sendMessage(ChatColor.RED + aa.getName() + " isn't within your premise anymore!");
							} else if (aa.getWorld().getName().equals(a.getWorld().getName()) && aa.getLocation().distance(a.getLocation()) < 30 && !contains) {
								alertPlayers.add(aa.getUniqueId());
								a.sendMessage(ChatColor.GREEN + aa.getName() + " is within your premise!");
							} else if (aa.getWorld().getName().equals(a.getWorld().getName()) && aa.getLocation().distance(a.getLocation()) >= 30 && contains) {
								alertPlayers.remove(aa.getUniqueId());
								a.sendMessage(ChatColor.RED + aa.getName() + " isn't within your premise anymore!");
							}
						}
					}
				} else if (a.getUniqueId().equals(jonathan)) {
					a.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
					a.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 0));
					if (hasAdvancement(a, "story/shiny_gear"))
						a.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 1));
				} else if (a.getUniqueId().equals(eli) && hasAdvancement(a, "story/shiny_gear")) {
					a.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 1));
					a.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1));
				} else if (a.getUniqueId().equals(oz)) {
					boolean hasSniper = hasAdvancement(a, "story/shiny_gear");
					if (hasSniper)
						a.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 0));
					a.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, hasSniper ? 1 : 0));
					a.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, hasSniper ? 1 : 0));
					if (hasAdvancement(a, "nether/return_to_sender"))
						a.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 60, 0));
					if (hasAdvancement(a, "story/enchant_item"))
						a.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 300, 0));
				} else if (a.getUniqueId().equals(jose) && endAdvancements.contains(a.getUniqueId())) {
					a.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 1));
					a.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1));
					a.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
				}
			}
		}, 0, 5);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for (World wo : Bukkit.getWorlds()) {
				for (LivingEntity le : wo.getLivingEntities()) {
					if (le.getType().equals(EntityType.WITHER) && le.getCustomName() != null && le.getCustomName().equals("§6§lRoyal Guard") && ((Wither) le).getTarget() != null) {
						Wither w = (Wither) le;
						Vector fire = w.getTarget().getLocation().toVector().subtract(w.getLocation().toVector()).multiply(0.2);
						for (int i = 0; i < 3; i++) {
							Bukkit.getScheduler().scheduleSyncDelayedTask(Asteria.getPlugin(Asteria.class), () -> {
								WitherSkull sw = (WitherSkull) w.getWorld().spawnEntity(w.getLocation(), EntityType.WITHER_SKULL);
								sw.setShooter(w);
								sw.setVelocity(fire);
							}, i);
						}
					}
				}
			}
			if (poisonBreath != null) {
				HashSet<LivingEntity> toHurt = new HashSet<LivingEntity>();
				for (int x = poisonBreath.getBlockX() - 5; x < poisonBreath.getBlockX() + 6; x++) {
					for (int z = poisonBreath.getBlockZ() - 5; z < poisonBreath.getBlockZ() + 6; z++) {
						Block b = poisonBreath.getWorld().getBlockAt(x, poisonBreath.getBlockY(), z);
						DustOptions pa = new Particle.DustOptions(Color.fromRGB(92, 242, 22), 1);
						b.getWorld().spawnParticle(Particle.REDSTONE, b.getLocation(), 25, 0.6, 0.6, 0.6, 1, pa, true);
						for (LivingEntity le : poisonBreath.getWorld().getLivingEntities()) {
							if ((le.getLocation().distance(b.getLocation()) < 0.8 || le.getEyeLocation().distance(b.getLocation()) < 0.8) && !le.getUniqueId().equals(brent))
								toHurt.add(le);
						}
					}
				}
				for (LivingEntity le : toHurt) {
					le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 30, 9));
					le.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 90, 4));
				}
			}
		}, 0, 15);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for (Player a : Bukkit.getOnlinePlayers()) {
				if (a.getUniqueId().equals(brent) && a.getWorld().getTime() < 23850 && a.getWorld().getTime() > 12300)
					a.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 300, 0));
			}
			for (World wo : Bukkit.getWorlds()) {
				for (LivingEntity le : wo.getLivingEntities()) {
					if (le.getType().equals(EntityType.WITHER_SKELETON) && le.getCustomName() != null && le.getCustomName().equals("§6Withered Knights")) {
						WitherSkeleton w = (WitherSkeleton) le;
						boolean hasPlayer = false;
						for (Player aa : Bukkit.getOnlinePlayers()) {
							if (w.getTarget() == null && w.getWorld().getName().equals(aa.getWorld().getName()) && aa.getLocation().distance(w.getLocation()) < 25 && !aa.getUniqueId().equals(hany) && !invis.containsKey(aa.getUniqueId()) && inFightWithHany.contains(aa.getUniqueId())) {
								w.setTarget(aa);
								hasPlayer = true;
							}
						}
						Double minDistance = 25D;
						LivingEntity mob = null;
						if (!hasPlayer) {
							for (LivingEntity lee : wo.getLivingEntities()) {
								if (w.getTarget() == null && lee.getLocation().distance(w.getLocation()) < 25 && (lee.getCustomName() == null || (lee.getCustomName() != null && !lee.getCustomName().equals("§6Withered Allegiance"))) && !lee.getType().equals(EntityType.WITHER_SKELETON) && !(lee instanceof Player) && !(lee.getType().equals(EntityType.PIG) || lee.getType().equals(EntityType.COW) || lee.getType().equals(EntityType.SHEEP) || lee.getType().equals(EntityType.CHICKEN) || lee.getType().equals(EntityType.RABBIT) || lee.getType().equals(EntityType.BAT) || lee.getType().equals(EntityType.HORSE) || lee.getType().equals(EntityType.DONKEY) || lee.getType().equals(EntityType.MULE) || lee.getType().equals(EntityType.CAT) || lee.getType().equals(EntityType.OCELOT) || lee.getType().equals(EntityType.PARROT) || lee.getType().equals(EntityType.SALMON) || lee.getType().equals(EntityType.DOLPHIN) || lee.getType().equals(EntityType.SQUID) || lee.getType().equals(EntityType.FOX) || lee.getType().equals(EntityType.WOLF) || lee.getType().equals(EntityType.PANDA) || lee.getType().equals(EntityType.IRON_GOLEM) || lee.getType().equals(EntityType.VILLAGER) || lee.getType().equals(EntityType.LLAMA) || lee.getType().equals(EntityType.COD) || lee.getType().equals(EntityType.TROPICAL_FISH) || lee.getType().equals(EntityType.ZOMBIE_HORSE) || lee.getType().equals(EntityType.PANDA)
										|| lee.getType().equals(EntityType.POLAR_BEAR) || lee.getType().equals(EntityType.SKELETON_HORSE) || lee.getType().equals(EntityType.TRADER_LLAMA) || lee.getType().equals(EntityType.TURTLE) || lee.getType().equals(EntityType.WANDERING_TRADER) || lee.getType().equals(EntityType.BEE) || lee.getType().equals(EntityType.MUSHROOM_COW) || lee.getType().equals(EntityType.SNOWMAN) || lee.getType().equals(EntityType.AXOLOTL) || lee.getType().equals(EntityType.GLOW_SQUID) || lee.getType().equals(EntityType.GOAT)) && lee.getLocation().distance(w.getLocation()) < minDistance) {
									if (lee.getType().equals(EntityType.ZOMBIFIED_PIGLIN)) {
										PigZombie zp = (PigZombie) lee;
										if (zp.getTarget() != null && zp.getTarget().getUniqueId().equals(hany)) {
											mob = lee;
											minDistance = lee.getLocation().distance(w.getLocation());
										}
									} else if (lee.getType().equals(EntityType.ENDERMAN)) {
										Enderman em = (Enderman) lee;
										if (em.getTarget() != null && em.getTarget().getUniqueId().equals(hany)) {
											mob = lee;
											minDistance = lee.getLocation().distance(w.getLocation());
										}
									} else if (lee.getType().equals(EntityType.WITHER)) {
										Wither em = (Wither) lee;
										if (em.getTarget() != null && em.getTarget().getUniqueId().equals(hany)) {
											mob = lee;
											minDistance = lee.getLocation().distance(w.getLocation());
										}
									} else {
										mob = lee;
										minDistance = lee.getLocation().distance(w.getLocation());
									}
								}
							}
						}
						if (mob != null) {
							w.setTarget(mob);
							final LivingEntity mobs = mob;
							witherai.put(mobs, Bukkit.getScheduler().scheduleSyncDelayedTask(Asteria.getPlugin(Asteria.class), () -> {
								if (!mobs.isDead()) {
									w.setTarget(null);
									witherai.remove(mobs);
								}
							}, 30 * 20));
						}
						if (w.getTarget() == null && !toTeleportToHany.containsKey(le.getUniqueId())) {
							toTeleportToHany.put(le.getUniqueId(), Bukkit.getScheduler().scheduleSyncDelayedTask(Asteria.getPlugin(Asteria.class), () -> {
								if (w.getTarget() == null) {
									if (Bukkit.getPlayer(hany) != null && !Bukkit.getPlayer(hany).isDead() && canWitherTeleportToHany && !PowerUtils.isInDimension(Bukkit.getPlayer(hany).getWorld().getName())) {
										w.teleport(Bukkit.getPlayer(hany).getLocation());
									}
								}
								toTeleportToHany.remove(le.getUniqueId());
							}, 200));
						}
					}
					if (le.getType().equals(EntityType.WITHER) && le.getCustomName() != null && le.getCustomName().equals("§6§lRoyal Guard")) {
						Wither w = (Wither) le;
						boolean hasPlayer = false;
						for (Player aa : Bukkit.getOnlinePlayers()) {
							if (w.getTarget() == null && w.getWorld().getName().equals(aa.getWorld().getName()) && aa.getLocation().distance(w.getLocation()) < 25 && !aa.getUniqueId().equals(hany) && !invis.containsKey(aa.getUniqueId()) && inFightWithHany.contains(aa.getUniqueId())) {
								w.setTarget(aa);
								hasPlayer = true;
							}
						}
						Double minDistance = 25D;
						LivingEntity mob = null;
						if (!hasPlayer) {
							for (LivingEntity lee : wo.getLivingEntities()) {
								if (w.getTarget() == null && lee.getLocation().distance(w.getLocation()) < 25 && (lee.getCustomName() == null || (lee.getCustomName() != null && !lee.getCustomName().equals("§6Withered Allegiance"))) && !lee.getType().equals(EntityType.WITHER_SKELETON) && !(lee instanceof Player) && !(lee.getType().equals(EntityType.PIG) || lee.getType().equals(EntityType.COW) || lee.getType().equals(EntityType.SHEEP) || lee.getType().equals(EntityType.CHICKEN) || lee.getType().equals(EntityType.RABBIT) || lee.getType().equals(EntityType.BAT) || lee.getType().equals(EntityType.HORSE) || lee.getType().equals(EntityType.DONKEY) || lee.getType().equals(EntityType.MULE) || lee.getType().equals(EntityType.CAT) || lee.getType().equals(EntityType.OCELOT) || lee.getType().equals(EntityType.PARROT) || lee.getType().equals(EntityType.SALMON) || lee.getType().equals(EntityType.DOLPHIN) || lee.getType().equals(EntityType.SQUID) || lee.getType().equals(EntityType.FOX) || lee.getType().equals(EntityType.WOLF) || lee.getType().equals(EntityType.PANDA) || lee.getType().equals(EntityType.IRON_GOLEM) || lee.getType().equals(EntityType.VILLAGER) || lee.getType().equals(EntityType.LLAMA) || lee.getType().equals(EntityType.COD) || lee.getType().equals(EntityType.TROPICAL_FISH) || lee.getType().equals(EntityType.ZOMBIE_HORSE) || lee.getType().equals(EntityType.PANDA)
										|| lee.getType().equals(EntityType.POLAR_BEAR) || lee.getType().equals(EntityType.SKELETON_HORSE) || lee.getType().equals(EntityType.TRADER_LLAMA) || lee.getType().equals(EntityType.TURTLE) || lee.getType().equals(EntityType.WANDERING_TRADER) || lee.getType().equals(EntityType.BEE) || lee.getType().equals(EntityType.MUSHROOM_COW) || lee.getType().equals(EntityType.SNOWMAN) || lee.getType().equals(EntityType.AXOLOTL) || lee.getType().equals(EntityType.GLOW_SQUID) || lee.getType().equals(EntityType.GOAT)) && lee.getLocation().distance(w.getLocation()) < minDistance) {
									if (lee.getType().equals(EntityType.ZOMBIFIED_PIGLIN)) {
										PigZombie zp = (PigZombie) lee;
										if (zp.getTarget() != null && zp.getTarget().getUniqueId().equals(hany)) {
											mob = lee;
											minDistance = lee.getLocation().distance(w.getLocation());
										}
									} else if (lee.getType().equals(EntityType.ENDERMAN)) {
										Enderman em = (Enderman) lee;
										if (em.getTarget() != null && em.getTarget().getUniqueId().equals(hany)) {
											mob = lee;
											minDistance = lee.getLocation().distance(w.getLocation());
										}
									} else if (lee.getType().equals(EntityType.WITHER)) {
										Wither em = (Wither) lee;
										if (em.getTarget() != null && em.getTarget().getUniqueId().equals(hany)) {
											mob = lee;
											minDistance = lee.getLocation().distance(w.getLocation());
										}
									} else {
										mob = lee;
										minDistance = lee.getLocation().distance(w.getLocation());
									}
								}
							}
						}
						if (mob != null) {
							w.setTarget(mob);
							final LivingEntity mobs = mob;
							witherai.put(mobs, Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
								if (!mobs.isDead()) {
									w.setTarget(null);
									witherai.remove(mobs);
								}
							}, 30 * 20));
						}
						if (w.getTarget() == null && !toTeleportToHany.containsKey(le.getUniqueId())) {
							toTeleportToHany.put(le.getUniqueId(), Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
								if (w.getTarget() == null && Bukkit.getPlayer(hany) != null && !Bukkit.getPlayer(hany).isDead() && canWitherTeleportToHany && !PowerUtils.isInDimension(Bukkit.getPlayer(hany).getWorld().getName())) {
									w.teleport(Bukkit.getPlayer(hany).getLocation());
								}
								toTeleportToHany.remove(le.getUniqueId());
							}, 200));
						}
					}
					if (le instanceof Mob && !(le instanceof AbstractHorse) && le.getCustomName() != null && le.getCustomName().equals("§6Withered Allegiance")) {
						Mob w = (Mob) le;
						boolean hasPlayer = false;
						for (Player aa : Bukkit.getOnlinePlayers()) {
							if (w.getTarget() == null && w.getWorld().getName().equals(aa.getWorld().getName()) && aa.getLocation().distance(w.getLocation()) < 25 && !aa.getUniqueId().equals(hany) && !invis.containsKey(aa.getUniqueId()) && inFightWithHany.contains(aa.getUniqueId())) {
								w.setTarget(aa);
								hasPlayer = true;
							}
						}
						Double minDistance = 25D;
						LivingEntity mob = null;
						if (!hasPlayer) {
							for (LivingEntity lee : wo.getLivingEntities()) {
								if (w.getTarget() == null && lee.getLocation().distance(w.getLocation()) < 25 && (lee.getCustomName() == null || (lee.getCustomName() != null && !lee.getCustomName().equals("§6Withered Allegiance"))) && !lee.getType().equals(EntityType.WITHER_SKELETON) && !(lee instanceof Player) && !(lee.getType().equals(EntityType.PIG) || lee.getType().equals(EntityType.COW) || lee.getType().equals(EntityType.SHEEP) || lee.getType().equals(EntityType.CHICKEN) || lee.getType().equals(EntityType.RABBIT) || lee.getType().equals(EntityType.BAT) || lee.getType().equals(EntityType.HORSE) || lee.getType().equals(EntityType.DONKEY) || lee.getType().equals(EntityType.MULE) || lee.getType().equals(EntityType.CAT) || lee.getType().equals(EntityType.OCELOT) || lee.getType().equals(EntityType.PARROT) || lee.getType().equals(EntityType.SALMON) || lee.getType().equals(EntityType.DOLPHIN) || lee.getType().equals(EntityType.SQUID) || lee.getType().equals(EntityType.FOX) || lee.getType().equals(EntityType.WOLF) || lee.getType().equals(EntityType.PANDA) || lee.getType().equals(EntityType.IRON_GOLEM) || lee.getType().equals(EntityType.VILLAGER) || lee.getType().equals(EntityType.LLAMA) || lee.getType().equals(EntityType.COD) || lee.getType().equals(EntityType.TROPICAL_FISH) || lee.getType().equals(EntityType.ZOMBIE_HORSE) || lee.getType().equals(EntityType.PANDA)
										|| lee.getType().equals(EntityType.POLAR_BEAR) || lee.getType().equals(EntityType.SKELETON_HORSE) || lee.getType().equals(EntityType.TRADER_LLAMA) || lee.getType().equals(EntityType.TURTLE) || lee.getType().equals(EntityType.WANDERING_TRADER) || lee.getType().equals(EntityType.BEE) || lee.getType().equals(EntityType.MUSHROOM_COW) || lee.getType().equals(EntityType.SNOWMAN) || lee.getType().equals(EntityType.AXOLOTL) || lee.getType().equals(EntityType.GLOW_SQUID) || lee.getType().equals(EntityType.GOAT)) && lee.getLocation().distance(w.getLocation()) < minDistance) {
									if (lee.getType().equals(EntityType.ZOMBIFIED_PIGLIN)) {
										PigZombie zp = (PigZombie) lee;
										if (zp.getTarget() != null && zp.getTarget().getUniqueId().equals(hany)) {
											mob = lee;
											minDistance = lee.getLocation().distance(w.getLocation());
										}
									} else if (lee.getType().equals(EntityType.ENDERMAN)) {
										Enderman em = (Enderman) lee;
										if (em.getTarget() != null && em.getTarget().getUniqueId().equals(hany)) {
											mob = lee;
											minDistance = lee.getLocation().distance(w.getLocation());
										}
									} else if (lee.getType().equals(EntityType.WITHER)) {
										Wither em = (Wither) lee;
										if (em.getTarget() != null && em.getTarget().getUniqueId().equals(hany)) {
											mob = lee;
											minDistance = lee.getLocation().distance(w.getLocation());
										}
									} else {
										mob = lee;
										minDistance = lee.getLocation().distance(w.getLocation());
									}
								}
							}
						}
						if (mob != null) {
							w.setTarget(mob);
							final LivingEntity mobs = mob;
							witherai.put(mobs, Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
								if (!mobs.isDead()) {
									w.setTarget(null);
									witherai.remove(mobs);
								}
							}, 30 * 20));
						}
						if (w.getTarget() == null && !toTeleportToHany.containsKey(le.getUniqueId())) {
							toTeleportToHany.put(le.getUniqueId(), Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
								if (w.getTarget() == null && Bukkit.getPlayer(hany) != null && !Bukkit.getPlayer(hany).isDead() && canAllegianceTeleportToHany && !PowerUtils.isInDimension(Bukkit.getPlayer(hany).getWorld().getName()))
									w.teleport(Bukkit.getPlayer(hany).getLocation());
								toTeleportToHany.remove(le.getUniqueId());
							}, 200));
						}
					}
				}
			}
		}, 0, 20);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for (Player a : Bukkit.getOnlinePlayers()) {
				if (a.getUniqueId().equals(hany)) {
					if (a.getWorld().getEnvironment().equals(Environment.NETHER) && hasAdvancement(a, "nether/obtain_blaze_rod"))
						a.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 90, 1));
					else
						a.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 90, 0));
				} else if (a.getUniqueId().equals(ed) && !disableRegen)
					a.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 90, 1));
				else if (a.getUniqueId().equals(saby))
					a.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 90, 0, false, false));
				else if (a.getUniqueId().equals(ramiro) && hasAdvancement(a, "nether/explore_nether")) {
					a.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 90, 1));
				} else if (a.getUniqueId().equals(jose)) {
					if (hasAdvancement(a, "story/enter_the_end"))
						a.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 90, 0));
					else if (hasAdvancement(a, "nether/brew_potion"))
						a.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 90, 0));
					if (a.getLocation().getBlock().getType().equals(Material.WATER) || a.getLocation().getBlock().getRelative(BlockFace.UP).getType().equals(Material.WATER))
						a.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 90, 1));
				}
				if (a.getWorld().getName().equals("dimensional_room"))
					a.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 90, 1));
			}
		}, 0, 80);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for (Player a : Bukkit.getOnlinePlayers()) {
				if (a.getUniqueId().equals(jonathan)) {
					a.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
					if (hasAdvancement(a, "story/shiny_gear"))
						a.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 3));
				}
			}
		}, 0, 2400);
	}

	public static void writeToFile(HashMap<String, String> cd, String s) {
		try (FileWriter filewriter = new FileWriter(".//plugins/Powers/cooldown" + s + ".txt")) {
			filewriter.write(cd.toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	void writeToFileNew(File f, String s) {
		try (FileWriter filewriter = new FileWriter(f)) {
			filewriter.write(s);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void addPowers(Player p) {
		UUID u = p.getUniqueId();
		if (u.equals(hany)) {
			ItemStack a;
			if (hasAdvancement(p, "nether/summon_wither")) {
				a = itemWithName(Material.NETHERITE_SWORD, "§bStygius");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.LOOT_BONUS_MOBS, 3, true);
				ab.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
				ab.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
				ab.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
				a.setItemMeta(ab);
			} else if (hasAdvancement(p, "nether/brew_potion")) {
				a = itemWithName(Material.NETHERITE_SWORD, "§bStygius");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.LOOT_BONUS_MOBS, 2, true);
				ab.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
				ab.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
				ab.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
				a.setItemMeta(ab);
			} else if (hasAdvancement(p, "nether/obtain_blaze_rod")) {
				a = itemWithName(Material.DIAMOND_SWORD, "§bStygius");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.LOOT_BONUS_MOBS, 2, true);
				ab.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
				ab.addEnchant(Enchantment.SWEEPING_EDGE, 2, true);
				ab.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
				a.setItemMeta(ab);
			} else if (hasAdvancement(p, "nether/get_wither_skull")) {
				a = itemWithName(Material.IRON_SWORD, "§bStygius");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.LOOT_BONUS_MOBS, 1, true);
				ab.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
				ab.addEnchant(Enchantment.SWEEPING_EDGE, 2, true);
				ab.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
				a.setItemMeta(ab);
			} else if (hasAdvancement(p, "nether/find_fortress")) {
				a = itemWithName(Material.IRON_SWORD, "§bStygius");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
				ab.addEnchant(Enchantment.SWEEPING_EDGE, 1, true);
				ab.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
				a.setItemMeta(ab);
			} else if (hasAdvancement(p, "story/enter_the_nether")) {
				a = itemWithName(Material.STONE_SWORD, "§bStygius");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
				ab.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
				a.setItemMeta(ab);
			} else {
				a = itemWithName(Material.WOODEN_SWORD, "§bStygius");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
				a.setItemMeta(ab);
			}
			p.getInventory().addItem(a);
			if (hasAdvancement(p, "nether/find_fortress")) {
				p.getInventory().addItem(itemWithName(Material.WITHER_ROSE, "§6Wither Turret / Dash"));
				p.getInventory().addItem(itemWithName(Material.SUNFLOWER, "§6Withered Allegiance"));
			}
			if (hasAdvancement(p, "nether/get_wither_skull"))
				p.getInventory().addItem(itemWithName(Material.WITHER_SKELETON_SKULL, "§6Soul's of the Damned"));
			if (hasAdvancement(p, "nether/obtain_blaze_rod"))
				p.getInventory().addItem(itemWithName(Material.NETHERITE_INGOT, "§bFortuna Flip"));
			if (hasAdvancement(p, "nether/summon_wither"))
				p.getInventory().addItem(itemWithName(Material.NETHER_STAR, "§6Royal Guard"));
		} else if (u.equals(saby)) {
			if (hasAdvancement(p, "end/dragon_breath")) {
				ItemStack a = itemWithName(Material.NETHERITE_SWORD, "§6Caliburn");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.LOOT_BONUS_MOBS, 3, true);
				ab.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
				ab.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
				ab.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
				a.setItemMeta(ab);
				p.getInventory().addItem(a);
			} else if (hasAdvancement(p, "story/enchant_item")) {
				ItemStack a = itemWithName(Material.NETHERITE_SWORD, "§6Caliburn");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.LOOT_BONUS_MOBS, 2, true);
				ab.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
				ab.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
				ab.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
				a.setItemMeta(ab);
				p.getInventory().addItem(a);
			} else if (hasAdvancement(p, "husbandry/make_a_sign_glow")) {
				ItemStack a = itemWithName(Material.DIAMOND_SWORD, "§6Caliburn");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.LOOT_BONUS_MOBS, 2, true);
				ab.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
				ab.addEnchant(Enchantment.SWEEPING_EDGE, 2, true);
				ab.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
				a.setItemMeta(ab);
				p.getInventory().addItem(a);
			} else if (hasAdvancement(p, "adventure/shoot_arrow")) {
				ItemStack a = itemWithName(Material.IRON_SWORD, "§6Caliburn");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.LOOT_BONUS_MOBS, 1, true);
				ab.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
				ab.addEnchant(Enchantment.SWEEPING_EDGE, 2, true);
				ab.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
				a.setItemMeta(ab);
				p.getInventory().addItem(a);
			} else if (hasAdvancement(p, "adventure/voluntary_exile")) {
				ItemStack a = itemWithName(Material.IRON_SWORD, "§6Caliburn");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
				ab.addEnchant(Enchantment.SWEEPING_EDGE, 1, true);
				ab.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
				a.setItemMeta(ab);
				p.getInventory().addItem(a);
			} else if (hasAdvancement(p, "story/lava_bucket")) {
				ItemStack a = itemWithName(Material.STONE_SWORD, "§6Caliburn");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
				ab.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
				a.setItemMeta(ab);
				p.getInventory().addItem(a);
			} else {
				ItemStack a = itemWithName(Material.WOODEN_SWORD, "§6Caliburn");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
				a.setItemMeta(ab);
				p.getInventory().addItem(a);
			}
			p.getInventory().addItem(itemWithName(Material.BLUE_ICE, "§bSnowstorm"));
			if (hasAdvancement(p, "story/lava_bucket")) {
				p.getInventory().addItem(itemWithName(Material.BLAZE_ROD, "§6Hellstorm"));
				p.getInventory().addItem(itemWithName(Material.GLOWSTONE_DUST, "§bFire Breath"));
			}
			if (hasAdvancement(p, "adventure/voluntary_exile")) {
				p.getInventory().addItem(itemWithName(Material.TOTEM_OF_UNDYING, "§6Spectral Shift"));
				p.getInventory().addItem(itemWithName(Material.GOLD_NUGGET, "§bParalysis"));
			}
			if (hasAdvancement(p, "adventure/shoot_arrow"))
				p.getInventory().addItem(itemWithName(Material.ARROW, "§6Conjuring Shot"));
			if (hasAdvancement(p, "husbandry/make_a_sign_glow"))
				p.getInventory().addItem(itemWithName(Material.GLOWSTONE, "§6Divine Light"));
			if (hasAdvancement(p, "story/enchant_item"))
				p.getInventory().addItem(itemWithName(Material.CLOCK, "§6Time Manipulation"));
			if (hasAdvancement(p, "end/dragon_breath"))
				p.getInventory().addItem(itemWithName(Material.DRAGON_BREATH, "§6Sorcerer's State"));
		} else if (u.equals(jonathan)) {
			p.getInventory().addItem(itemWithName(Material.YELLOW_DYE, "§6Shooting Star"));
			if (hasAdvancement(p, "story/lava_bucket"))
				p.getInventory().addItem(itemWithName(Material.DIAMOND, "§6Distortion"));
			if (hasAdvancement(p, "nether/obtain_blaze_rod"))
				p.getInventory().addItem(itemWithName(Material.BLAZE_POWDER, "§6Eruption"));
			if (endAdvancements.contains(p.getUniqueId()))
				p.getInventory().addItem(itemWithName(Material.TNT, "§6Supernova"));
			if (hasAdvancement(p, "end/elytra"))
				p.getInventory().addItem(itemWithName(Material.NETHER_STAR, "§6Star Shield"));
			if (hasAdvancement(p, "end/respawn_dragon"))
				p.getInventory().addItem(itemWithName(Material.END_CRYSTAL, "§6Wrath of Asteria"));
		} else if (u.equals(ramiro)) {
			if (hasAdvancement(p, "story/mine_diamond"))
				p.getInventory().addItem(itemWithName(Material.RABBIT_FOOT, "§6Alert / Double Jump"));
			if (hasAdvancement(p, "story/enter_the_nether"))
				p.getInventory().addItem(itemWithName(Material.LIME_DYE, "§bStomp"));
		} else if (u.equals(ed)) {
			p.getInventory().addItem(itemWithName(Material.GRAY_GLAZED_TERRACOTTA, "§bDimensional Shift"));
			if (hasAdvancement(p, "adventure/kill_a_mob"))
				p.getInventory().addItem(itemWithName(Material.REDSTONE_BLOCK, "§bHostile Convergence"));
			if (hasAdvancement(p, "story/enter_the_nether"))
				p.getInventory().addItem(itemWithName(Material.RED_STAINED_GLASS_PANE, "§6Dimension Jump"));
			if (hasAdvancement(p, "story/mine_diamond"))
				p.getInventory().addItem(itemWithName(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "§6Spatial Manipulation"));
			if (hasAdvancement(p, "story/shiny_gear")) {
				p.getInventory().addItem(itemWithName(Material.BLUE_GLAZED_TERRACOTTA, "§bArea Counter"));
				p.getInventory().addItem(itemWithName(Material.WHITE_STAINED_GLASS_PANE, "§6Dimensional Rooms"));
			}
			if (hasAdvancement(p, "end/kill_dragon"))
				p.getInventory().addItem(itemWithName(Material.BLACK_STAINED_GLASS_PANE, "§610th Dimensional Physiology"));
			if (hasAdvancement(p, "end/find_end_city")) {
				p.getInventory().addItem(itemWithName(Material.PURPLE_STAINED_GLASS_PANE, "§6Rift"));
				p.getInventory().addItem(itemWithName(Material.YELLOW_STAINED_GLASS_PANE, "§6Energy Convergence"));
			}

		} else if (u.equals(jose)) {
			ItemStack a = itemWithName(Material.TRIDENT, "§6Neptune");
			ItemMeta ab = a.getItemMeta();
			ab.addEnchant(Enchantment.IMPALING, hasAdvancement(p, "nether/brew_potion") ? 5 : hasAdvancement(p, "adventure/very_very_frightening") ? 4 : hasAdvancement(p, "husbandry/kill_axolotl_target") ? 3 : hasAdvancement(p, "story/obtain_armor") ? 2 : 1, true);
			if (hasAdvancement(p, "husbandry/kill_axolotl_target"))
				ab.addEnchant(Enchantment.CHANNELING, 1, true);
			a.setItemMeta(ab);
			p.getInventory().addItem(a);
			p.getInventory().addItem(itemWithName(Material.GOLD_INGOT, "§6Raging Storm"));
			if (hasAdvancement(p, "adventure/kill_a_mob"))
				p.getInventory().addItem(itemWithName(Material.NAUTILUS_SHELL, "§bCalling Conch"));
			if (hasAdvancement(p, "story/obtain_armor"))
				p.getInventory().addItem(itemWithName(Material.HEART_OF_THE_SEA, "§bClimate Change"));
			if (hasAdvancement(p, "husbandry/kill_axolotl_target"))
				p.getInventory().addItem(itemWithName(Material.DIAMOND, "§bAqua Blast"));
			if (endAdvancements.contains(p.getUniqueId()))
				p.getInventory().addItem(itemWithName(Material.CONDUIT, "§bWrath of the Sea"));
		} else if (u.equals(brent)) {
			p.getInventory().addItem(itemWithName(Material.COAL, "§6Screech"));
			if (hasAdvancement(p, "adventure/kill_a_mob"))
				p.getInventory().addItem(itemWithName(Material.NETHERITE_INGOT, "§6Dark Shift"));
			if (hasAdvancement(p, "story/deflect_arrow"))
				p.getInventory().addItem(itemWithName(Material.REDSTONE, "§6Blood Bite"));
			if (hasAdvancement(p, "nether/return_to_sender"))
				p.getInventory().addItem(itemWithName(Material.GHAST_TEAR, "§6Return"));
			if (hasAdvancement(p, "story/follow_ender_eye"))
				p.getInventory().addItem(itemWithName(Material.SLIME_BALL, "§6Poisonous Breath"));
			if (hasAdvancement(p, "end/dragon_egg"))
				p.getInventory().addItem(itemWithName(Material.DRAGON_EGG, "§6Black Hole"));
			if (hasAdvancement(p, "end/elytra"))
				p.getInventory().addItem(itemWithName(Material.PHANTOM_MEMBRANE, "§6Phantom Burst"));
		} else if (u.equals(eli)) {
			p.getInventory().addItem(itemWithName(Material.BLAZE_ROD, "§6Electrokinesis"));
			if (hasAdvancement(p, "story/obtain_armor"))
				p.getInventory().addItem(itemWithName(Material.BLACK_DYE, "§bUmbrakinesis"));
			if (hasAdvancement(p, "story/enter_the_nether"))
				p.getInventory().addItem(itemWithName(Material.GLOWSTONE, "§bDawn of Light"));
			if (hasAdvancement(p, "story/shiny_gear"))
				p.getInventory().addItem(itemWithName(Material.DIAMOND, "§bShape Shift"));
			if (endAdvancements.contains(p.getUniqueId())) {
				ItemStack a = itemWithName(Material.NETHERITE_SWORD, "§bBlades of Exile");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
				ab.addEnchant(Enchantment.KNOCKBACK, 5, true);
				ab.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
				a.setItemMeta(ab);
				p.getInventory().addItem(a);
			}
			if (hasAdvancement(p, "end/find_end_city")) {
				ItemStack aa = itemWithName(Material.NETHERITE_AXE, "§bLeviathan Axe");
				ItemMeta aab = aa.getItemMeta();
				aab.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
				aa.setItemMeta(aab);
				p.getInventory().addItem(aa);
			}
		} else if (u.equals(oz)) {
			p.getInventory().addItem(itemWithName(Material.GOLD_NUGGET, "§6Magic Seal"));
			if (hasAdvancement(p, "adventure/sniper_duel"))
				p.getInventory().addItem(itemWithName(Material.GOLD_INGOT, "§bAlmighty Push"));
			if (hasAdvancement(p, "nether/return_to_sender"))
				p.getInventory().addItem(itemWithName(Material.GOLD_BLOCK, "§bFull Counter"));
			if (hasAdvancement(p, "story/enchant_item"))
				p.getInventory().addItem(itemWithName(Material.OBSIDIAN, "§bCatastrophic Seal"));
			if (endAdvancements.contains(p.getUniqueId()))
				p.getInventory().addItem(itemWithName(Material.YELLOW_STAINED_GLASS_PANE, "§bThe End Gates"));
		}
	}

	public static ItemStack itemWithName(Material m, String n) {
		ItemStack a = new ItemStack(m);
		ItemMeta ab = a.getItemMeta();
		ab.setDisplayName(n);
		a.setItemMeta(ab);
		return a;
	}

	public ItemStack excaliburNetherite() {
		ItemStack a = new ItemStack(Material.NETHERITE_SWORD);
		ItemMeta ab = a.getItemMeta();
		ab.setDisplayName("§6Excalibur");
		ab.addEnchant(Enchantment.LOOT_BONUS_MOBS, 3, true);
		ab.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
		ab.addEnchant(Enchantment.DAMAGE_UNDEAD, 5, true);
		ab.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
		a.setItemMeta(ab);
		return a;
	}

	static Double readReversal() {
		if (!dataFolder.exists())
			dataFolder.mkdir();
		File f = new File(dataFolder, "reversalDamage");
		try {
			if (f.createNewFile())
				return 0D;
			else {
				Scanner myReader = new Scanner(f);
				String data = myReader.nextLine();
				myReader.close();
				return Double.valueOf(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0D;
	}

	static long readPower(String s) {
		if (!dataFolder.exists())
			dataFolder.mkdir();
		File f = new File(dataFolder, s);
		try {
			if (f.createNewFile())
				return 0L;
			else {
				Scanner myReader = new Scanner(f);
				String data = myReader.nextLine();
				myReader.close();
				return Long.valueOf(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0L;
	}

	static int readFortune() {
		if (!dataFolder.exists())
			dataFolder.mkdir();
		File f = new File(dataFolder, "fortuneChance");
		try {
			if (f.createNewFile())
				return 5;
			else {
				Scanner myReader = new Scanner(f);
				String data = myReader.nextLine();
				myReader.close();
				return Integer.parseInt(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 5;
	}

	static HashSet<UUID> readArray(String s) {
		if (!dataFolder.exists())
			dataFolder.mkdir();
		File f = new File(dataFolder, s);
		try {
			if (f.createNewFile())
				return new HashSet<UUID>();
			else {
				Scanner myReader = new Scanner(f);
				String data = myReader.nextLine();
				myReader.close();
				HashSet<UUID> temp = new HashSet<UUID>();
				for (String ss : data.split(","))
					temp.add(UUID.fromString(ss));
				return temp;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new HashSet<UUID>();
	}

	public static boolean hasAdvancement(Player player, String name) {
		name = "minecraft:" + name;
		Iterator<Advancement> it = Bukkit.getServer().advancementIterator();
		while (it.hasNext()) {
			Advancement a = it.next();
			if (a.getKey().toString().equals(name)) {
				AdvancementProgress pr = player.getAdvancementProgress(a);
				return pr.isDone();
			}
		}
		Bukkit.getLogger().warning("No advancement found for: " + name);
		return false;
	}

	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
		for (Entry<T, E> entry : map.entrySet()) {
			if (Objects.equals(value, entry.getValue()))
				return entry.getKey();
		}
		return null;
	}

}