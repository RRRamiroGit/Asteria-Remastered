package net.randompvp.asteria.powers;

import java.util.UUID;

import org.bukkit.Bukkit;

public class HostileConvergencePlayer {

	UUID uuid;
	int id;
	int hits = 1;

	public HostileConvergencePlayer(UUID u, int id) {
		this.uuid = u;
		this.id = id;
	}

	public int getHits() {
		return hits;
	}
	
	public UUID getUUID() {
		return uuid;
	}

	public void setId(int id) {
		Bukkit.getScheduler().cancelTask(this.id);
		this.id = id;
		hits++;
	}

}
