package net.randompvp.asteria.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import net.randompvp.asteria.Asteria;
import net.randompvp.asteria.utils.PowerUtils;

public class BlockListeners implements Listener {

	Plugin plugin;

	public BlockListeners(Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (Asteria.powerNames.contains(e.getItemInHand().getItemMeta().getDisplayName()))
			e.setCancelled(true);
		if (e.getPlayer().getWorld().getName().equals("dimensional_room") && (e.getBlock().getX() < -5 || e.getBlock().getX() > 5 || e.getBlock().getZ() < -5 || e.getBlock().getZ() > 5))
			e.setCancelled(true);
		if (e.getPlayer().getWorld().getName().equals("10thdimension"))
			e.setCancelled(true);
		if (e.getPlayer().getWorld().getName().equals("rift"))
			e.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.getPlayer().getWorld().getName().equals("dimensional_room") && e.getBlock().getY() == 80)
			e.setCancelled(true);
		if (e.getPlayer().getWorld().getName().equals("10thdimension") || e.getPlayer().getWorld().getName().equals("12thdimension"))
			e.setCancelled(true);
		if (e.getPlayer().getWorld().getName().equals("rift"))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent e) {
		if (PowerUtils.isInDimension(e.getBlock().getWorld().getName()))
			e.blockList().clear();
	}

}
