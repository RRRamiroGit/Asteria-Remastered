package net.randompvp.asteria.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.dbassett.skullcreator.SkullCreator;

public class Utils {
	
	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public static org.bukkit.inventory.ItemStack createItem(Inventory inv, Material material, int amount, int invSlot,
			String displayName, String... loreString) {

		org.bukkit.inventory.ItemStack item;
		List<String> lore = new ArrayList<String>();

		item = new ItemStack(material, amount);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.chat(displayName));
		for (String s : loreString) {
			lore.add(Utils.chat(s));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);

		inv.setItem(invSlot - 1, item);
		return item;
	}
	
	public static org.bukkit.inventory.ItemStack createHead(Inventory inv, String base64, int invSlot,
			String displayName, String... loreString) {
		
		org.bukkit.inventory.ItemStack item;
		List<String> lore = new ArrayList<String>();

		item = SkullCreator.itemFromBase64(base64);;
		

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.chat(displayName));
		for (String s : loreString) {
			lore.add(Utils.chat(s));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);

		inv.setItem(invSlot - 1, item);
		return item;
	}

}
