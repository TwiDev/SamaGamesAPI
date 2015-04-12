package net.samagames.core.api.stats;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.stats.PlayerStat;
import net.samagames.api.stats.StatsManager;
import net.samagames.core.APIPlugin;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class StatsManagerDB extends StatsManager {

	public StatsManagerDB(String game, APIPlugin plugin) {
		super(game, plugin);
	}

	@Override
	public void increase(final UUID player, final String stat, final int amount) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			Jedis j = APIPlugin.getApi().getResource();
			j.zincrby("gamestats:" + game + ":" + stat, amount, player.toString());
			j.close();
		});
	}

	@Override
	public void setValue(UUID player, String stat, int value) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			Jedis j = APIPlugin.getApi().getResource();
			j.zadd("gamestats:" + game + ":" + stat, value, player.toString());
			j.close();
		});
	}

	@Override
	public double getStatValue(UUID player, String stat) {
		Jedis j = APIPlugin.getApi().getResource();
		double value = j.zscore("gamestats:"+game+":"+stat, player.toString());
		j.close();

		return value;
	}

	@Override
	public ArrayList<PlayerStat> getLeaderboard(String stat)
	{
		ArrayList<PlayerStat> leaderboard = new ArrayList<>();
		Jedis jedis = SamaGamesAPI.get().getResource();
		Set<String> ids = jedis.zrange("gamestats:" + game + ":" + stat, 0, 2);
		jedis.close();

		for (String id : ids)
		{
			PlayerStat playerStat = new PlayerStat(UUID.fromString(id), this.game, stat);
			playerStat.fill();

			leaderboard.add(playerStat);
		}

		return leaderboard;
	}
}
