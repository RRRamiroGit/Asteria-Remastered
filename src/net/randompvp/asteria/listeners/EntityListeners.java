package net.randompvp.asteria.listeners;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftTrident;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.randompvp.asteria.Asteria;
import net.randompvp.asteria.powers.HostileConvergencePlayer;
import net.randompvp.asteria.utils.PowerUtils;

public class EntityListeners implements Listener {
	Random r = new Random();

	Plugin plugin;

	public EntityListeners(Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		if (e.getEntity().getType().equals(EntityType.PHANTOM))
			e.getEntity().setCustomName("Corona");
		if (e.getEntity().getType().equals(EntityType.ENDER_DRAGON) && (e.getEntity().getWorld().getName().equals("10thdimension") || e.getEntity().getWorld().getName().equals("dimensional_room") || e.getEntity().getWorld().getName().equals("12thdimension")))
			e.setCancelled(true);
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		if (e.getEntityType().equals(EntityType.SMALL_FIREBALL) && e.getEntity().hasMetadata("fromPower"))
			e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 2, false, false, e.getEntity());
		if (e.getEntityType().equals(EntityType.WITHER_SKULL) && e.getEntity().hasMetadata("fromPower")) {
			float explosion = 4;
			if (Asteria.isBerserkerActive)
				explosion = 6;
			e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), (float) (explosion * Asteria.hanyPowerMultiplier), false, true, e.getEntity());
		}
		if (e.getEntityType().equals(EntityType.WITHER_SKULL) && e.getEntity().hasMetadata("fromPower1")) {
			Asteria.stopExplosionHany = true;
			e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), (float) (8 * Asteria.hanyPowerMultiplier), false, true, e.getEntity());
			Asteria.stopExplosionHany = false;
		}
		if (e.getEntityType().equals(EntityType.WITHER_SKULL) && e.getEntity().hasMetadata("fromPower2")) {
			float explosion = 1F;
			if (Asteria.isBerserkerActive)
				explosion = 3F;
			e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), (float) (explosion * Asteria.hanyPowerMultiplier), false, true, e.getEntity());
		}
		if (e.getEntityType().equals(EntityType.TRIDENT)) {
			ItemStack thrownTrident = CraftItemStack.asBukkitCopy(((CraftTrident) e.getEntity()).getHandle().aq);
			if (!e.getEntity().getWorld().isThundering() && thrownTrident.getItemMeta().getDisplayName() != null && thrownTrident.getItemMeta().getDisplayName().equals("§6Neptune") && e.getEntity().getShooter() instanceof Player && Asteria.hasAdvancement((Player) e.getEntity().getShooter(), "adventure/very_very_frightening") && ((e.getHitBlock() != null && e.getHitBlock().getType().equals(Material.LIGHTNING_ROD)) || e.getHitEntity() != null))
				e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation());
		}
	}

	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (Asteria.powerNames.contains(e.getConsumable().getItemMeta().getDisplayName())) {
				p.sendMessage(ChatColor.RED + "Hmm, it seems you tried to shoot your power, please put your regular arrows in your hotbar");
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		// changing/canceling damage is priority
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (Asteria.stopExplosionHany && p.getUniqueId().equals(Asteria.hany) && (e.getCause().equals(DamageCause.ENTITY_EXPLOSION) || e.getCause().equals(DamageCause.BLOCK_EXPLOSION)))
				e.setDamage(0);
			if ((Asteria.invis.containsKey(p.getUniqueId()) && p.getUniqueId().equals(Asteria.ed)) || (((p.getUniqueId().equals(Asteria.hany) && Asteria.hasAdvancement(p, "story/enter_the_nether")) || p.getUniqueId().equals(Asteria.ramiro) || p.getUniqueId().equals(Asteria.ed) || (p.getUniqueId().equals(Asteria.oz) && Asteria.endAdvancements.contains(p.getUniqueId()))) && e.getCause().equals(DamageCause.FALL)) || p.getWorld().getName().equals("dimensional_room") || p.getWorld().getName().equals("12thdimension") || (p.getUniqueId().equals(Asteria.jose) && Asteria.joseInvincible) || (Asteria.riftInSpec && p.getUniqueId().equals(Asteria.ed))) {
				e.setDamage(0);
				e.setCancelled(true);
				return;
			}
			if (p.getUniqueId().equals(Asteria.oz) && Asteria.shotsSinceFullCounter < 2 && Asteria.isFullCounterActive) {
				e.setCancelled(true);
				Asteria.shotsSinceFullCounter++;
			}
		}
		if (e instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent) e;
			if (ee.getDamager() instanceof Projectile && e.getEntity() instanceof LivingEntity) {
				Projectile pr = (Projectile) ee.getDamager();
				LivingEntity le = (LivingEntity) e.getEntity();
				if (e.getEntity() instanceof Player) {
					Player p = (Player) e.getEntity();
					if ((p.getUniqueId().equals(Asteria.hany) && p.getHealth() < 11) || (p.getUniqueId().equals(Asteria.brent) && Asteria.isPhantomBurstActive)) {
						e.setCancelled(true);
						e.setDamage(0);
						return;
					}
				}
				if (pr.getCustomName() != null && pr.getCustomName().equals("§6Conjuring Shot")) {
					PotionEffectType[] negativeEffects = { PotionEffectType.BLINDNESS, PotionEffectType.CONFUSION, PotionEffectType.HUNGER, PotionEffectType.POISON, PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.WEAKNESS, PotionEffectType.WITHER };
					le.addPotionEffect(new PotionEffect(negativeEffects[r.nextInt(negativeEffects.length)], 300, 2));
					le.sendMessage(ChatColor.RED + "You have been hit with §6Conjuring Shot" + ChatColor.RED + "!");
				}
			}
			if (Asteria.halfDamage.contains(ee.getDamager().getUniqueId()))
				e.setDamage(e.getDamage() / 2);
			if (ee.getDamager() instanceof Projectile && e.getEntity().getType().isAlive()) {
				Projectile pp = (Projectile) ee.getDamager();
				if (pp.getShooter() instanceof Player) {
					Player p = (Player) pp.getShooter();
					LivingEntity entity = (LivingEntity) e.getEntity();
					if (p.getUniqueId().equals(Asteria.hany) && !e.getCause().equals(DamageCause.ENTITY_EXPLOSION) && e.getFinalDamage() > 0 && e.getFinalDamage() > 0) {
						PowerUtils.hanyDamage(p, entity, e.getFinalDamage());
					}
				}
			}
			if (ee.getDamager() instanceof Player && e.getEntity().getType().isAlive()) {
				Player p = (Player) ee.getDamager();
				LivingEntity entity = (LivingEntity) e.getEntity();
				if ((Asteria.witherEmbraceMove.contains(p.getUniqueId()) && p.getUniqueId().equals(entity.getUniqueId())) || (Asteria.riftInSpec && p.getUniqueId().equals(Asteria.ed))) {
					e.setCancelled(true);
					e.setDamage(0);
					return;
				}
				if (p.getUniqueId().equals(Asteria.hany) && !e.getCause().equals(DamageCause.ENTITY_EXPLOSION) && e.getFinalDamage() > 0) {
					PowerUtils.hanyDamage(p, entity, e.getFinalDamage());
					if (Asteria.shootWitherSkulls) {
						Location l = p.getEyeLocation().add(p.getLocation().getDirection().multiply(0.05));
						WitherSkull sk = (WitherSkull) p.getWorld().spawnEntity(l, EntityType.WITHER_SKULL);
						sk.setShooter(p);
						sk.setMetadata("fromPower2", new FixedMetadataValue(plugin, "yes!"));
						sk.setVelocity(p.getLocation().getDirection().multiply(0.3));
						if (p.getHealth() < 10)
							sk.setCharged(true);
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> sk.remove(), 260);
					}
				}
				if (entity.hasMetadata("fromPower") || (entity.getType().equals(EntityType.WITHER) && entity.getCustomName() != null && entity.getCustomName().equals("§6§lRoyal Guard")))
					Asteria.inFightWithHany.add(p.getUniqueId());
				if (p.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_SWORD) && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§6Bane of Darkness")) {
					entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
					entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
				}
				if (p.getUniqueId().equals(Asteria.ed) && Asteria.isReversalActive) {
					if (Asteria.reversalDamage < 7.5)
						p.sendMessage(ChatColor.RED + "You can't use Revenge Counter with less than 10% damage!");
					else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
					else if (PowerUtils.shouldDisablePowerBySeal(p.getUniqueId()))
						p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Magic Seal");
					else {
						Asteria.isReversalActive = false;
						if (Asteria.halfDamage.contains(p.getUniqueId()))
							Asteria.reversalDamage = Asteria.reversalDamage / 2;
						p.sendMessage(ChatColor.BLUE + "You have used your Revenge Counter with a total of " + ChatColor.DARK_BLUE + String.valueOf(new DecimalFormat("#.##").format(Asteria.reversalDamage)) + ChatColor.BLUE + " damage!");
						Vector vel = p.getLocation().getDirection().multiply(7).setY(2);
						DustOptions pa = new Particle.DustOptions(Color.fromRGB(114, 137, 218), 1);
						for (int n = 0; n < 4; n++) {
							final int nn = n;
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
								for (int o = -1; o < 2; o++) {
									for (int i = 0; i < 10; i++) {
										Location l = entity.getLocation();
										for (int k = 0; k < nn * 4; k++) {
											l.setPitch(o * 40);
											l.setYaw(i * 24);
											l.add(l.getDirection().multiply(0.7));
											if (nn == 3)
												l.getWorld().spawnParticle(Particle.REDSTONE, l, 16, 0.3, 0.3, 0.3, 5, new Particle.DustOptions(Color.fromRGB(78, 93, 148), 1), true);
											else
												l.getWorld().spawnParticle(Particle.REDSTONE, l, 14, 0.1, 0.1, 0.1, 5, pa, true);
										}
									}
								}
							}, n * 2);
						}
						Bukkit.getScheduler().runTask(plugin, () -> entity.setVelocity(vel));
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> entity.setVelocity(vel), 2);
						Double damage = e.getDamage() + Asteria.reversalDamage;
						e.setDamage(0);
						Asteria.reversalDamageId.put(entity.getUniqueId(), Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							entity.damage(damage);
							entity.sendMessage(ChatColor.RED + "You have been hit with " + ChatColor.GOLD + String.valueOf(new DecimalFormat("#.##").format(damage)) + ChatColor.RED + " damage!");
							Asteria.reversalDamageId.remove(entity.getUniqueId());
						}, 9));
						Asteria.reversalDamage = 0D;
					}
				}
				if (p.getUniqueId().equals(Asteria.oz) && Asteria.isFinalFullCounterActive) {
					if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
						p.sendMessage(ChatColor.RED + "You may not use Full Counter right now due to §6Sorcerer's State§c!");
					else {
						Asteria.isFinalFullCounterActive = false;
						if (Asteria.halfDamage.contains(p.getUniqueId()))
							Asteria.fullCounterDamage = Asteria.fullCounterDamage / 2;
						p.sendMessage(ChatColor.BLUE + "You have used your Full Counter with a total of " + ChatColor.DARK_BLUE + String.valueOf(new DecimalFormat("#.##").format(Asteria.fullCounterDamage)) + ChatColor.BLUE + " damage!");
						e.setDamage(e.getDamage() + Asteria.fullCounterDamage);
						entity.sendMessage(ChatColor.RED + "You have been hit with " + ChatColor.GOLD + String.valueOf(new DecimalFormat("#.##").format(Asteria.fullCounterDamage)) + ChatColor.RED + " damage!");
						Asteria.shotsSinceFullCounter = 0;
						Bukkit.getScheduler().cancelTask(Asteria.fullCounterId);
						Bukkit.getScheduler().cancelTask(Asteria.fullCounterFinalEndId);
						Asteria.fullCounterDamage = 0D;
					}
				}
				if (entity instanceof Player) {
					Player pl = (Player) entity;
					if (p.getInventory().getItemInMainHand().getType().equals(Material.WHITE_STAINED_GLASS_PANE) && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§6Dimensional Rooms") && !Asteria.dimensionalRoom.containsKey(p.getUniqueId())) {
						if (Asteria.dimensionalRoomQueue.contains(entity.getUniqueId())) {
							Asteria.dimensionalRoomQueue.remove(entity.getUniqueId());
							p.sendMessage(ChatColor.RED + entity.getName() + " will no longer be warping with you to the Dimensional Room!");
						} else {
							int max = Asteria.endAdvancements.contains(p.getUniqueId()) ? 4 : 3;
							if (Asteria.dimensionalRoomQueue.size() < max) {
								Asteria.dimensionalRoomQueue.add(entity.getUniqueId());
								p.sendMessage(ChatColor.GREEN + entity.getName() + " will now warp with you to the Dimensional Room!");
							} else
								p.sendMessage(ChatColor.DARK_RED + "You may not add any more people to the queue!");
						}
					}
					if (p.getInventory().getItemInMainHand().getType().equals(Material.RED_STAINED_GLASS_PANE) && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§6Dimension Jump")) {
						if (Asteria.dimensionalWarpQueue.contains(entity.getUniqueId())) {
							Asteria.dimensionalWarpQueue.remove(entity.getUniqueId());
							p.sendMessage(ChatColor.RED + entity.getName() + " will no longer be warping with you between dimensions!");
						} else {
							int max = Asteria.endAdvancements.contains(p.getUniqueId()) ? 4 : 3;
							if (Asteria.dimensionalWarpQueue.size() < max) {
								Asteria.dimensionalWarpQueue.add(entity.getUniqueId());
								p.sendMessage(ChatColor.GREEN + entity.getName() + " will now warp with you between dimensions!");
							} else
								p.sendMessage(ChatColor.DARK_RED + "You may not add any more people to the queue!");
						}
					}
					if (p.getUniqueId().equals(Asteria.hany))
						Asteria.inFightWithHany.add(pl.getUniqueId());
					if (pl.getUniqueId().equals(Asteria.hany))
						Asteria.inFightWithHany.add(p.getUniqueId());
					if (p.getInventory().getItemInMainHand().getType().equals(Material.PURPLE_STAINED_GLASS_PANE) && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§6Rift") && Asteria.riftLocations.containsKey(p.getUniqueId())) {
						e.setCancelled(true);
						entity.sendMessage(ChatColor.GREEN + p.getName() + " has warped you out of the Rift Dimension!");
						entity.teleport(Asteria.riftLocations.get(entity.getUniqueId()));
						if (Asteria.riftTarget.size() > 2) {
							Bukkit.getPlayer(Asteria.getKeyByValue(Asteria.riftTarget, entity.getUniqueId())).sendMessage(ChatColor.RED + "Your target was warped back, your new target is: " + Bukkit.getPlayer(Asteria.riftTarget.get(entity.getUniqueId())).getName());
							Asteria.riftTarget.put(Asteria.getKeyByValue(Asteria.riftTarget, entity.getUniqueId()), Asteria.riftTarget.get(entity.getUniqueId()));
							p.sendMessage(ChatColor.DARK_GREEN + String.valueOf(Asteria.riftTarget.size() - 1) + " players remain");
						} else if (Asteria.riftTarget.size() == 2) {
							Bukkit.getPlayer(Asteria.getKeyByValue(Asteria.riftTarget, entity.getUniqueId())).sendMessage(ChatColor.RED + "Your target was warped back, you are the only player left, you will now be warped back!!");
							Bukkit.getScheduler().cancelTask(Asteria.riftId);
							p.sendMessage(ChatColor.RED + "Rift has ended as there is only one player left!");
							Asteria.riftLocations.remove(entity.getUniqueId());
							PowerUtils.endRift();
						} else {
							Bukkit.getScheduler().cancelTask(Asteria.riftId);
							p.sendMessage(ChatColor.RED + "Rift has ended as there is no players left!");
							Asteria.riftLocations.remove(entity.getUniqueId());
							PowerUtils.endRift();
						}
						Asteria.riftTarget.remove(p.getUniqueId());
						Asteria.riftLocations.remove(p.getUniqueId());
					}
					if ((entity.getUniqueId().equals(Asteria.ed) || p.getUniqueId().equals(Asteria.ed)) && Asteria.riftLocations.containsKey(p.getUniqueId())) {
						Asteria.canUseOpRift = false;
						if (Asteria.canUseOpRiftId != 0) {
							Bukkit.getScheduler().cancelTask(Asteria.canUseOpRiftId);
							Asteria.canUseOpRiftId = 0;
						}
						Asteria.canUseOpRiftId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Asteria.canUseOpRift = true;
							Asteria.canUseOpRiftId = 0;
						}, 600);
					}
					if (p.getUniqueId().equals(Asteria.oz)) {
						if (Asteria.isMagicSealActive && ((Asteria.playerOnMagicSeal != null && !Asteria.playerOnMagicSeal.equals(pl.getUniqueId())) || Asteria.playerOnMagicSeal == null)) {
							if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
								p.sendMessage(ChatColor.RED + "You may not use §6Magic Seal§c right now due to §6Sorcerer's State§c!");
							else {
								Player old = Bukkit.getPlayer(Asteria.playerOnMagicSeal);
								if (old != null)
									old.sendMessage(ChatColor.GREEN + "You are no longer affected by §6Magic Seal§a!");
								Asteria.playerOnMagicSeal = pl.getUniqueId();
								pl.sendMessage(ChatColor.RED + "You have been hit with §6Magic Seal§c!");
								PowerUtils.endPowersWithSeal(p, pl, "§6Magic Seal");
							}
						} else if (p.getInventory().getItemInMainHand().getType().equals(Material.OBSIDIAN) && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§bCatastrophic Seal")) {
							if (PowerUtils.isOnCooldownNew(Asteria.cooldownCatastrophicSeal, p))
								PowerUtils.sendCDMsgNew(Asteria.cooldownCatastrophicSeal, p);
							else if (PowerUtils.shouldDisablePowerBySorcerer(p.getLocation()))
								p.sendMessage(ChatColor.RED + "You may not use this power right now due to §6Sorcerer's State§c!");
							else {
								for (int i = 0; i < 5; i++) {
									final int ii = 5 - i;
									Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
										p.sendMessage(ChatColor.GREEN + pl.getName() + " is about be trapped in " + ii + " seconds!");
										pl.sendMessage(ChatColor.RED + "You're about be trapped in " + ii + " seconds!");
									}, i * 20);
								}
								Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
									if (!pl.isOnline()) {
										p.sendMessage(ChatColor.RED + "It appears your target is not online!");
										Asteria.cooldownCatastrophicSeal = 0L;
									} else if (PowerUtils.isInDimension(pl.getWorld().getName())) {
										p.sendMessage(ChatColor.RED + "The dimension your target is in may not be affected by " + "power name" + ChatColor.RED + "!");
										pl.sendMessage(ChatColor.BLUE + "You may not be trapped in this dimension!");
										Asteria.cooldownCatastrophicSeal = 0L;
									} else {
										Asteria.obbyBl.put(Asteria.obbyBlNum, new HashMap<Location, BlockData>());
										int num = Asteria.obbyBlNum;
										Asteria.obbyBlNum++;
										Location l = pl.getLocation();
										int lx = l.getBlockX();
										int ly = l.getBlockY();
										int lz = l.getBlockZ();
										for (int y = ly - 11; y < ly + 11; y++) {
											for (int x = lx - 11; x < lx + 11; x++) {
												for (int z = lz - 11; z < lz + 11; z++) {
													Block b = l.getWorld().getBlockAt(x, y, z);
													Double dis = b.getLocation().distance(pl.getLocation().getBlock().getLocation());
													if (dis >= 2.5 && dis <= 10.5 && !b.getType().equals(Material.BEDROCK) && !b.getType().equals(Material.END_PORTAL_FRAME)) {
														Asteria.obbyBl.get(num).put(b.getLocation(), b.getBlockData());
														Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> b.setType(Material.OBSIDIAN), (Math.round(dis) * 5) - 12);
													}
												}
											}
										}
										Asteria.locOfCatastrophicSeal = l;
										PowerUtils.endPowersWithSeal(p, pl, "§bCatastrophic Seal");
										Player old = Bukkit.getPlayer(Asteria.catastrophicSealPlayer);
										if (old != null) {
											old.sendMessage(ChatColor.GREEN + "You are no longer being affected by §bCatastrophic Seal§a!");
											p.sendMessage(ChatColor.RED + "You are no longer affecting " + old.getName() + " with §bCatastrophic Seal§c!");
										}
										Asteria.catastrophicSealPlayer = pl.getUniqueId();
										Asteria.catastrophicSealId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
											Asteria.catastrophicSealPlayer = null;
											Asteria.locOfCatastrophicSeal = null;
											old.sendMessage(ChatColor.GREEN + "You are no longer being affected by §bCatastrophic Seal§a!");
											p.sendMessage(ChatColor.RED + "You are no longer affecting " + old.getName() + " with §bCatastrophic Seal §cbecause the time ran out!");
										});
										Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
											HashMap<Location, BlockData> bl = Asteria.obbyBl.get(num);
											for (HashMap.Entry<Location, BlockData> blit : bl.entrySet())
												blit.getKey().getBlock().setBlockData((BlockData) blit.getValue());
											Asteria.obbyBl.remove(num);
										}, 180 * 20);
										p.sendMessage(ChatColor.GREEN + "Your target has been encased in obsidian!");
										pl.sendMessage(ChatColor.RED + "You have been encased in obsidian!");
									}
								}, 100);
								Asteria.cooldownCatastrophicSeal = Instant.now().getEpochSecond() + PowerUtils.handleCooldown((24000 - Bukkit.getWorld("world").getTime()) / 20); // till the end of the day
							}
						}
					}
					if (p.getUniqueId().equals(Asteria.ed) && Asteria.isHostileConvergenceActive) {
						HostileConvergencePlayer hpl = PowerUtils.getHostileConvergencePlayer(pl.getUniqueId());
						if (hpl == null) {
							p.sendMessage(ChatColor.BLUE + "You have begun to hit " + pl.getName() + "!");
							pl.sendMessage(ChatColor.RED + "You have begun to get affected by §bHostile Convergence§c!");
							Asteria.hostileConvergencePlayers.add(new HostileConvergencePlayer(pl.getUniqueId(), Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
								Asteria.hostileConvergencePlayers.remove(PowerUtils.getHostileConvergencePlayer(pl.getUniqueId()));
								p.sendMessage(ChatColor.RED + pl.getName() + "'s timer has run out for §bHostile Convergence§c!");
								pl.sendMessage(ChatColor.GREEN + "Your timer has run out for §bHostile Convergence§a!");
							}, 400)));
						} else {
							hpl.setId(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
								Asteria.hostileConvergencePlayers.remove(PowerUtils.getHostileConvergencePlayer(pl.getUniqueId()));
								p.sendMessage(ChatColor.RED + pl.getName() + "'s timer has run out for §bHostile Convergence§c!");
								pl.sendMessage(ChatColor.GREEN + "Your timer has run out for §bHostile Convergence§a!");
							}, 400));
							final int hit = hpl.getHits();
							final int time = 12000;
							boolean sendmsg = true;
							if (hit == 5)
								pl.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, time, 0));
							else if (hit == 10)
								pl.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, time, 1));
							else if (hit == 20) {
								pl.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, time, 2));
								pl.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time, 0));
							} else if (hit == 30) {
								pl.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, time, 2));
								pl.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time, 1));
							} else if (hit == 40)
								p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, time, 1));
							else if (hit == 50)
								p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, time, 1));
							else if (hit == 60) {
								pl.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, time, 3));
								pl.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time, 3));
								pl.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, time, 1));
								p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, time, 1));
							} else if (hit == 100) {
								pl.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, time, 3));
								pl.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, time, 5));
								pl.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time, 3));
								pl.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, time, 0));
								p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, time, 3));
								p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, time, 2));
							} else
								sendmsg = false;
							if (sendmsg) {
								p.sendMessage(ChatColor.GREEN + pl.getName() + "' has been hit " + hit + " times and got some effects!");
								pl.sendMessage(ChatColor.RED + "You have been hit with §bHostile Convergence §c" + hit + " times and got some effects!");
								pl.getWorld().playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 1F);
							}
						}
					}
				}
			}
			if (ee.getDamager().getType().isAlive() && e.getEntity() instanceof Player && e.getEntity().getUniqueId().equals(Asteria.jonathan) && Asteria.starshieldActive)
				((LivingEntity) ee.getDamager()).damage(e.getFinalDamage(), (Player) e.getEntity());
		}
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (p.getUniqueId().equals(Asteria.hany)) {
				if (!Asteria.witherEmbraceMove.contains(p.getUniqueId()) && p.getHealth() - e.getFinalDamage() <= 6 && p.getHealth() > 6 && p.getHealth() - e.getFinalDamage() > 0.1) {
					Asteria.witherEmbraceMove.add(p.getUniqueId());
					Location l = p.getLocation();
					l.setY(l.getY() + 1.65);
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 6));
					int lx = l.getBlockX();
					int lz = l.getBlockZ();
					for (int x = lx - 6; x < lx + 6; x++) {
						for (int z = lz - 6; z < lz + 6; z++) {
							Block b = l.getWorld().getBlockAt(x, (int) l.getY(), z);
							if (b.getLocation().distance(l) <= 5)
								l.getWorld().spawnParticle(Particle.SOUL, b.getLocation(), 50);
						}
					}
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
						l.getWorld().createExplosion(l, (float) (8 * Asteria.hanyPowerMultiplier), false, true, p);
						Asteria.witherEmbraceMove.remove(p.getUniqueId());
					}, 30);
				}
				if (Asteria.hasAdvancement(p, "nether/get_wither_skull") && (e.getCause().equals(DamageCause.WITHER) || e.getCause().equals(DamageCause.MAGIC)))
					e.setDamage(e.getDamage() / 4);
			}
			if (p.getUniqueId().equals(Asteria.jonathan) && p.getHealth() - e.getFinalDamage() <= 4 && p.getHealth() > 4 && p.getHealth() - e.getFinalDamage() > 0.1 && !Asteria.overdriveCooldown && Asteria.endAdvancements.contains(p.getUniqueId())) {
				Location l = p.getLocation();
				l.setY(l.getY() + 1.65);
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 0));
				l.getWorld().createExplosion(l, 6, true, true, p);
				Asteria.overdriveCooldown = true;
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Asteria.overdriveCooldown = false, 6000);
			}
			if (p.getUniqueId().equals(Asteria.saby) && p.getHealth() - e.getFinalDamage() < 0.01 && r.nextInt(20) == 0 && Asteria.secondChanceCooldown) {
				p.getWorld().spawnParticle(Particle.END_ROD, p.getEyeLocation(), 500, 0.5, 0.5, 0.5, 0.5);
				e.setDamage(0);
				p.setHealth(20);
				Asteria.secondChanceCooldown = true;
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Asteria.secondChanceCooldown = false, 18000);
			}
			if (p.getUniqueId().equals(Asteria.eli) && p.getHealth() - e.getFinalDamage() < 0.01 && r.nextInt(20) == 0) {
				p.getWorld().spawnParticle(Particle.SLIME, p.getEyeLocation(), 500, 0.6, 0.6, 0.6, 0.5);
				e.setDamage(0);
				p.setHealth(10);
			}
			if (p.getUniqueId().equals(Asteria.ed)) {
				if (p.getHealth() >= 2 && p.getHealth() - e.getFinalDamage() < 2 && (e.getCause().equals(DamageCause.ENTITY_EXPLOSION) || e.getCause().equals(DamageCause.BLOCK_EXPLOSION))) {
					e.setDamage(0);
					if (Asteria.hasAdvancement(p, "story/mine_diamond"))
						Asteria.reversalDamage = Asteria.reversalDamage + (p.getHealth() - 2);
					p.setHealth(2);
					if (!Asteria.disableRegen) {
						if (p.hasPotionEffect(PotionEffectType.REGENERATION))
							p.removePotionEffect(PotionEffectType.REGENERATION);
						Asteria.disableRegen = true;
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Asteria.disableRegen = false;
							p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 90, 1));
						}, 100);
					}
				} else {
					if (Asteria.hasAdvancement(p, "story/mine_diamond"))
						Asteria.reversalDamage = Asteria.reversalDamage + (e.getFinalDamage() * 0.9);
					e.setDamage(e.getDamage() * 0.8);
				}
			} else if (p.getUniqueId().equals(Asteria.oz) && Asteria.isFullCounterActive)
				Asteria.fullCounterDamage = Asteria.fullCounterDamage + (e.getFinalDamage() * 4);
			else if (p.getUniqueId().equals(Asteria.eli) && Asteria.isShapeShiftActive)
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> p.setVelocity(new Vector()), 1);
		}
		if (e.getEntity() instanceof Item && (e.getCause().equals(DamageCause.ENTITY_EXPLOSION) || e.getCause().equals(DamageCause.BLOCK_EXPLOSION)))
			e.setCancelled(true);
	}

	@EventHandler
	public void onEntityTarget(EntityTargetEvent e) {
		if (e.getTarget() != null) {
			if (e.getEntityType() == EntityType.WITHER_SKELETON && (e.getTarget().getUniqueId().equals(Asteria.hany) || (e.getEntity().hasMetadata("fromPower") && !Asteria.inFightWithHany.contains(e.getTarget().getUniqueId()))))
				e.setCancelled(true);
			if (e.getEntityType() == EntityType.WITHER && e.getEntity().getCustomName() != null && e.getEntity().getCustomName().equals("§6§lRoyal Guard"))
				e.setCancelled(true);
			if (e.getEntity().getCustomName() != null && e.getEntity().getCustomName().equals("§6Withered Allegiance"))
				e.setCancelled(true);
			if (e.getTarget().getUniqueId().equals(Asteria.jose) && e.getEntity().getCustomName() != null && (e.getEntity().getCustomName().equals("§bGuards of the Abyss") || e.getEntity().getCustomName().equals("§bLeviathans") || e.getEntity().getCustomName().equals("§bCreatures of the Deep")))
				e.setCancelled(true);
			if (e.getTarget().getUniqueId().equals(Asteria.saby) && e.getEntity().getCustomName() != null && (e.getEntity().getCustomName().equals("§bGhastly Knights") || e.getEntity().getCustomName().equals("§6Flaming Knights")))
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityPotionEffect(EntityPotionEffectEvent e) {
		if (e.getEntity() instanceof Player && e.getEntity().getUniqueId().equals(Asteria.hany) && !Asteria.doesPotionAffect && e.getNewEffect() != null && Asteria.hasAdvancement((Player) e.getEntity(), "nether/brew_potion") && (e.getNewEffect().getType().equals(PotionEffectType.BLINDNESS) || e.getNewEffect().getType().equals(PotionEffectType.CONFUSION) || e.getNewEffect().getType().equals(PotionEffectType.HUNGER) || e.getNewEffect().getType().equals(PotionEffectType.SLOW_DIGGING) || e.getNewEffect().getType().equals(PotionEffectType.POISON) || e.getNewEffect().getType().equals(PotionEffectType.SLOW) || e.getNewEffect().getType().equals(PotionEffectType.WEAKNESS))) {
			e.setCancelled(true);
			int i = e.getNewEffect().getAmplifier() / 4;
			Asteria.doesPotionAffect = true;
			((Player) e.getEntity()).addPotionEffect(new PotionEffect(e.getNewEffect().getType(), e.getNewEffect().getDuration(), i));
			Asteria.doesPotionAffect = false;
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if (e.getEntity() instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) e.getEntity();
			if (Asteria.witherai.containsKey(le)) {
				Bukkit.getScheduler().cancelTask(Asteria.witherai.get(le));
				Asteria.witherai.remove(le);
			}
			for (World wo : Bukkit.getWorlds()) {
				for (LivingEntity lee : wo.getLivingEntities()) {
					if (lee.getType().equals(EntityType.WITHER) && lee.getCustomName() != null && lee.getCustomName().equals("§6§lRoyal Guard")) {
						Wither w = (Wither) lee;
						if (w.getTarget() != null && w.getTarget().getUniqueId().equals(le.getUniqueId()))
							w.setTarget(null);
					}
					if (lee.getType().equals(EntityType.WITHER_SKELETON) && lee.hasMetadata("fromPower")) {
						WitherSkeleton w = (WitherSkeleton) lee;
						if (w.getTarget() != null && w.getTarget().getUniqueId().equals(le.getUniqueId()))
							w.setTarget(null);
					}
				}
			}
			if (le instanceof AbstractHorse) {
				AbstractHorse h = (AbstractHorse) le;
				if (h.getCustomName() != null && h.getCustomName().equals("§6Withered Allegiance"))
					e.getDrops().remove(h.getInventory().getSaddle());
			}
			if (e.getEntity().getCustomName() != null && e.getEntity().getCustomName().equals("§6Withered Knights")) {
				ArrayList<ItemStack> a = new ArrayList<ItemStack>();
				for (ItemStack b : e.getDrops()) {
					if (b != null && b.getItemMeta() != null && b.getItemMeta().getDisplayName() != null && b.getItemMeta().getDisplayName().startsWith("§6") && r.nextInt(100) != 0)
						a.add(b);
				}
				for (ItemStack b : a)
					e.getDrops().remove(b);
			}
			if (le.getKiller() != null && le.getKiller().getUniqueId().equals(Asteria.brent) && r.nextBoolean() && !PowerUtils.isInDimension(le.getKiller().getWorld().getName())) {
				Player p = le.getKiller();
				Phantom ph = (Phantom) p.getWorld().spawnEntity(p.getLocation(), EntityType.PHANTOM);
				p.sendMessage(ChatColor.BLUE + "A phantom has been summoned to fight for you!");
				ph.setCustomName("§eBrent's Phantom");
			}
		}
		if (e.getEntity().getCustomName() != null && e.getEntity().getCustomName().equals("§bGuards of the Abyss")) {
			ArrayList<ItemStack> a = new ArrayList<ItemStack>();
			for (ItemStack b : e.getDrops()) {
				if (b != null && (b.getType().equals(Material.GOLDEN_HELMET) || b.getType().equals(Material.GOLDEN_CHESTPLATE) || b.getType().equals(Material.GOLDEN_LEGGINGS) || b.getType().equals(Material.GOLDEN_BOOTS) || b.getType().equals(Material.TRIDENT)))
					a.add(b);
			}
			for (ItemStack b : a)
				e.getDrops().remove(b);
		}
	}

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent e) {
		if (e.getEntityType().equals(EntityType.TRIDENT)) {
			ItemStack thrownTrident = CraftItemStack.asBukkitCopy(((CraftTrident) e.getEntity()).getHandle().aq);
			if (thrownTrident.getItemMeta().getDisplayName() != null && thrownTrident.getItemMeta().getDisplayName().equals("§6Neptune") && e.getEntity().getShooter() instanceof Player) {
				Player p = (Player) e.getEntity().getShooter();
				Trident trident = (Trident) e.getEntity();
				trident.setPickupStatus(PickupStatus.DISALLOWED);
				ItemStack a = Asteria.itemWithName(Material.TRIDENT, "§6Neptune");
				ItemMeta ab = a.getItemMeta();
				ab.addEnchant(Enchantment.IMPALING, Asteria.hasAdvancement(p, "nether/brew_potion") ? 5 : Asteria.hasAdvancement(p, "adventure/very_very_frightening") ? 4 : Asteria.hasAdvancement(p, "husbandry/kill_axolotl_target") ? 3 : Asteria.hasAdvancement(p, "story/obtain_armor") ? 2 : 1, true);
				if (Asteria.hasAdvancement(p, "husbandry/kill_axolotl_target"))
					ab.addEnchant(Enchantment.CHANNELING, 1, true);
				a.setItemMeta(ab);
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> p.getInventory().addItem(a), 1);
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
					if (trident.isOnGround())
						trident.remove();
					else
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> trident.remove(), 1200);
				}, 1200);
				if (Asteria.isWrathSeaActive) {
					Location l = p.getEyeLocation();
					l.add(l.getDirection());
					Vector v = e.getEntity().getVelocity();
					for (int i = 0; i < 4; i++) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							Trident t = (Trident) p.getWorld().spawnEntity(l, EntityType.TRIDENT);
							t.setVelocity(v);
							t.setPickupStatus(PickupStatus.DISALLOWED);
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> t.remove(), 2000);
						}, i);
					}
				}
			}
		}
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		if (PowerUtils.isInDimension(e.getEntity().getWorld().getName()))
			e.blockList().clear();
	}

	@EventHandler
	public void onEntityResurrect(EntityResurrectEvent e) {
		if (e.getEntity() instanceof Player) {
			PlayerInventory inv = ((Player) e.getEntity()).getInventory();
			if ((inv.getItemInMainHand().hasItemMeta() && inv.getItemInMainHand().getItemMeta().getDisplayName() != null && Asteria.powerNames.contains(inv.getItemInMainHand().getItemMeta().getDisplayName())) || (inv.getItemInOffHand().hasItemMeta() && inv.getItemInOffHand().getItemMeta().getDisplayName() != null && Asteria.powerNames.contains(inv.getItemInOffHand().getItemMeta().getDisplayName())))
				e.setCancelled(true);
		}
	}

}
