package net.randompvp.asteria.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.randompvp.asteria.Asteria;
import net.randompvp.asteria.utils.PowerUtils;
import net.randompvp.asteria.utils.Utils;

public class Menu {

	public static Inventory jump(Player p) {
		int inv_rows = 9;
		Inventory inv = Bukkit.createInventory(null, inv_rows);
		Inventory toReturn = Bukkit.createInventory(null, inv_rows, "§lDimension Jump");

		if (p.getWorld().getName().equals("world") || PowerUtils.isInDimension(p.getWorld().getName()) || p.getWorld().getName().equals("rift"))
			Utils.createItem(inv, Material.BEDROCK, 1, 2, "§8§lCan't use this", "You can't use this power currently!");
		else
			Utils.createItem(inv, Material.GRASS_BLOCK, 1, 2, "§a§lOverworld", "Click to warp to the overworld");
		if (p.getWorld().getName().equals("world_nether") || PowerUtils.isInDimension(p.getWorld().getName()) || p.getWorld().getName().equals("rift"))
			Utils.createItem(inv, Material.BEDROCK, 1, 5, "§8§lCan't use this", "You can't use this power currently!");
		else
			Utils.createItem(inv, Material.NETHERRACK, 1, 5, "§c§lNether", "Click to warp to the nether");
		if (p.getWorld().getName().equals("world_the_end") || PowerUtils.isInDimension(p.getWorld().getName()) || p.getWorld().getName().equals("rift")
				|| !Asteria.hasAdvancement(p, "minecraft:story/enter_the_end"))
			Utils.createItem(inv, Material.BEDROCK, 1, 8, "§8§lCan't use this", "You can't use this power currently!");
		else
			Utils.createItem(inv, Material.END_STONE, 1, 8, "§e§lEnd", "Click to warp to the end");

		toReturn.setContents(inv.getContents());
		return toReturn;
	}

	public static Inventory rift(Player p) {
		int inv_rows = 9;
		Inventory inv = Bukkit.createInventory(null, inv_rows);
		Inventory toReturn = Bukkit.createInventory(null, inv_rows, "§lRift Menu");
		String desc1 = "Click to go back to your last location for 4 minutes.";
		if (Asteria.riftInOverworld)
			desc1 = "Click to go back to the Rift Dimension.";
		Utils.createItem(inv, Material.GRASS_BLOCK, 1, 2, "§aOverworld", desc1);
		String desc2 = "Click to spectate for 4 minutes.";
		if (Asteria.riftInSpec)
			desc2 = "Click to cancel your spectator.";
		Utils.createItem(inv, Material.GLASS, 1, 5, "§bSpectate", desc2);
		Utils.createItem(inv, Material.RED_WOOL, 1, 8, "§bCancel", "Cancel this entire power.");

		toReturn.setContents(inv.getContents());
		return toReturn;
	}

	public static Inventory mobMenu() {
		int inv_rows = 9;
		Inventory inv = Bukkit.createInventory(null, inv_rows);
		Inventory toReturn = Bukkit.createInventory(null, inv_rows, "§lChoose your variation");
		if (Asteria.whatMobHanySelected.equals("zombie")) {
			Utils.createItem(inv, Material.ZOMBIE_HEAD, 1, 1, "§bZombie", "Click to select Zombie");
			Utils.createHead(inv,
					"e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc1MTU3YjRhNDhmMmE5NDk5MGJjZGY2Yjk2YTg4OGE3OThhOGNmZmM1YWViOWI1ZDA2YjkwYzZjOGY4MGYwIn19fQ==",
					2, "§bZombie Villager", "Click to select Zombie Villager");
			Utils.createHead(inv,
					"e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWY2NDNmZGRlZGE1M2JkMjM4ZGU2ZjVjYmFhYzY1ZGVmMGJhYjk1MjQyYzY3ZDM0MDEyZDc3NWNlMDFjYTkzIn19fQ==",
					3, "§bHusk", "Click to select Husk");
			Utils.createHead(inv,
					"e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQzMTFlZWI3ZTVhNDllZjA5MTgwN2JkNGUzMGQzOGEwM2YwMTMwNzE2YzkxMGU1NjZlYWEwMThhOGUyNWQ5MyJ9fX0=",
					4, "§bDrowned", "Click to select Drowned");
		} else if (Asteria.whatMobHanySelected.equals("skeleton")) {
			Utils.createItem(inv, Material.SKELETON_SKULL, 1, 1, "§bSkeleton", "Click to select Skeleton");
			Utils.createHead(inv,
					"e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjU3YmYwN2IzODg0N2FjMzA5ZDMzZTMyYTZiZjNiZDYwODc0MGNiZDljZjUwMmRjMWQ2NzBjM2VhMjZmOWRmNiJ9fX0=",
					2, "§bStray", "Click to select Stray");
		} else if (Asteria.whatMobHanySelected.equals("piglin")) {
			Utils.createHead(inv,
					"e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTdiYjhkNDM0ZGY1ZDVmNTM2MmY0ZmNkMWFkYmEyYjBhMDY2OGM2YzBhM2UzMWZlMWJlZGIwZmI3Y2YzMmIxNSJ9fX0=",
					1, "§bZombified Piglin", "Click to select Zombified Piglin");
			Utils.createHead(inv,
					"e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ2ZGUzM2UzZGJkZjM5YWU2MDBiMmFhZjdiY2Q5ODQ4MzU2YWIzYzIzOTAxMDg1NjIxODk5MzlkMTRmNzcyNSJ9fX0=",
					2, "§bZoglin", "Click to select Zoglin");
		} else if (Asteria.whatMobHanySelected.equals("horse")) {
			Utils.createItem(inv, Material.ZOMBIE_HORSE_SPAWN_EGG, 1, 1, "§bZombie Horse", "Click to select Zombie Horse");
			Utils.createItem(inv, Material.SKELETON_HORSE_SPAWN_EGG, 1, 2, "§bSkeleton Horse", "Click to select Skeleton Horse");
		} else {
			Utils.createItem(inv, Material.PHANTOM_SPAWN_EGG, 1, 1, "§bPhantom", "Click to select Phantom");
		}
		String color = "§a";
		if (!Asteria.canAllegianceTeleportToHany)
			color = "§c";
		Utils.createItem(inv, Material.ENDER_PEARL, 1, 8, "§bToggle Teleporting", "Click to toggle the teleporting of all your allegiances",
				"Currently set to: " + color + Asteria.canAllegianceTeleportToHany);
		Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 9, "§4Desummon", "Click to desummon");
		toReturn.setContents(inv.getContents());
		return toReturn;
	}

	public static Inventory callingConch(Player p) {
		int inv_rows = 9;
		Inventory inv = Bukkit.createInventory(null, inv_rows);
		Inventory toReturn = Bukkit.createInventory(null, inv_rows, "§lCalling Conch");

		Utils.createHead(inv,
				"e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQzMTFlZWI3ZTVhNDllZjA5MTgwN2JkNGUzMGQzOGEwM2YwMTMwNzE2YzkxMGU1NjZlYWEwMThhOGUyNWQ5MyJ9fX0=",
				1, "§bDrowned", "Click here to spawn drowned where you're looking!");
		Utils.createItem(inv, Material.AXOLOTL_BUCKET, 1, 2, "§bAxolotl", "Click here to spawn axolotl where you're looking!");
		Utils.createHead(inv,
				"e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTU2YjM3NzgzODYxZTZhNDRkNTM3ZTMyZDg2NTUzNzMxYWU0MzM3OTExNWRiMzRjMjY2ZTUyZmEzY2FiIn19fQ==",
				3, "§bGuardian", "Click here to spawn guardians where you're looking!");

		toReturn.setContents(inv.getContents());
		return toReturn;
	}

	public static Inventory climateChange(Player p) {
		int inv_rows = 9;
		Inventory inv = Bukkit.createInventory(null, inv_rows);
		Inventory toReturn = Bukkit.createInventory(null, inv_rows, "§lClimate Change");

		Utils.createItem(inv, Material.WATER_BUCKET, 1, 3, "§bRain", "Click to change the weather to rain!");
		Utils.createItem(inv, Material.BLUE_ICE, 1, 7, "§bThunderStorm", "Click to change the weather to thunder!");

		toReturn.setContents(inv.getContents());
		return toReturn;
	}

	public static Inventory areaReversal() {
		int inv_rows = 9;
		Inventory inv = Bukkit.createInventory(null, inv_rows);
		Inventory toReturn = Bukkit.createInventory(null, inv_rows, "§lArea Counter");

		Utils.createItem(inv, Material.BLUE_WOOL, 1, 3, "§bNormal", "Click to switch Area Counter to Normal!");
		Utils.createItem(inv, Material.RED_WOOL, 1, 7, "§bSuicide", "Click to switch Area Counter to Suicide!");

		toReturn.setContents(inv.getContents());
		return toReturn;
	}

	public static Inventory dimensionalRooms(Player p) {
		int inv_rows = 9;
		Inventory inv = Bukkit.createInventory(null, inv_rows);
		Inventory toReturn = Bukkit.createInventory(null, inv_rows, "§lDimensional Rooms");

		if (Asteria.dimensionalRoom.containsKey(p.getUniqueId()) && p.getWorld().getName().equals("dimensional_room"))
			Utils.createItem(inv, Material.LIGHT_BLUE_CONCRETE, 1, 3, "§bDimensional Room", "Click to warp out of your Dimensional Room!");
		else
			Utils.createItem(inv, Material.LIGHT_BLUE_CONCRETE, 1, 3, "§bDimensional Room", "Click to go to your Dimensional Room!");
		if (Asteria.dimensionalRoom.containsKey(p.getUniqueId()) && p.getWorld().getName().equals("12thdimension"))
			Utils.createItem(inv, Material.MAGENTA_CONCRETE, 1, 7, "§b12th Dimensional Physiology",
					"Click to warp out of the 12th Dimension!");
		else
			Utils.createItem(inv, Material.MAGENTA_CONCRETE, 1, 7, "§b12th Dimensional Physiology", "Click to go to the 12th Dimension!");

		toReturn.setContents(inv.getContents());
		return toReturn;
	}

	public static Inventory timeManipulation() {
		int inv_rows = 9;
		Inventory inv = Bukkit.createInventory(null, inv_rows);
		Inventory toReturn = Bukkit.createInventory(null, inv_rows, "§lTime Manipulation");

		Utils.createItem(inv, Material.NAUTILUS_SHELL, 1, 3, "§bTime Slow", "Click to slow everything around you down!");
		Utils.createItem(inv, Material.RABBIT_FOOT, 1, 7, "§bTime Speed", "Click to speed you up!");

		toReturn.setContents(inv.getContents());
		return toReturn;
	}

}
