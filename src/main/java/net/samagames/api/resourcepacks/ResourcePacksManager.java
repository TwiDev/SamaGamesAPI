package net.samagames.api.resourcepacks;

import org.bukkit.entity.Player;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public interface ResourcePacksManager {

	void forcePack(String name);

	/**
	 * Request the user to download a pack
	 * @param player player receiving the pack
	 * @param packName the resource pack name (without extension)
	 * @param callback callback
	 */
	public void sendResourcePack(Player player, String packName, ResourceCallback callback);

	public void sendResourcePack(Player player, String packName);

	public void resetResourcePack(Player player);
}
