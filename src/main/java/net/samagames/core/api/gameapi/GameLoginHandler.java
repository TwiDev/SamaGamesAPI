package net.samagames.core.api.gameapi;

import net.samagames.api.gameapi.Game;
import net.samagames.api.network.JoinHandler;
import net.samagames.api.network.JoinResponse;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GameLoginHandler implements JoinHandler {

    private final GameAPI api;

    GameLoginHandler(GameAPI api) {
        this.api = api;
    }

    @Override
    public JoinResponse onLogin(UUID player, JoinResponse response) {
        if (api.getGame() != null) {
            if (!response.isAllowed())
                return api.getGame().onLogin(player, response);

            Game info = api.getGame();
            if (info.getState() == Game.GameState.INGAME) {
                response.setResponseType(JoinResponse.ResponseType.DENY_INGAME);
            } else if (info.getState() == Game.GameState.NOT_READY) {
                response.setResponseType(JoinResponse.ResponseType.DENY_NOTREADY);
            } else if (info.getConnectedPlayers() > info.getTotalMaxPlayers() && !PermissionsBukkit.hasPermission(player, "games.joinfull")) {
                response.setResponseType(JoinResponse.ResponseType.DENY_SERVER_FULL);
            } else if (info.getConnectedPlayers() > info.getMaxPlayers() && !PermissionsBukkit.hasPermission(player, "games.joinvip")) {
                response.setResponseType(JoinResponse.ResponseType.DENY_ALMOST_FULL);
            }

            return api.getGame().onLogin(player, response);
        }
        return response;
    }

    @Override
    public JoinResponse onJoin(Player player, JoinResponse response) {
        if (api.getGame() != null) {
            if (!response.isAllowed())
                return api.getGame().onJoin(player, response);

            Game info = api.getGame();
            if (info.getState() == Game.GameState.INGAME) {
                response.setResponseType(JoinResponse.ResponseType.DENY_INGAME);
            } else if (info.getState() == Game.GameState.NOT_READY) {
                response.setResponseType(JoinResponse.ResponseType.DENY_NOTREADY);
            } else if (info.getConnectedPlayers() > info.getTotalMaxPlayers() && !PermissionsBukkit.hasPermission(player, "games.joinfull")) {
                response.setResponseType(JoinResponse.ResponseType.DENY_SERVER_FULL);
            } else if (info.getConnectedPlayers() > info.getMaxPlayers() && !PermissionsBukkit.hasPermission(player, "games.joinvip")) {
                response.setResponseType(JoinResponse.ResponseType.DENY_ALMOST_FULL);
            }

            response = api.getGame().onJoin(player, response);
            if (response.isAllowed()) {
                String message = ChatColor.YELLOW + "${PSEUDO}" + ChatColor.YELLOW + " a rejoint la partie ! " + ChatColor.DARK_GRAY + "[" + ChatColor.RED + "${JOUEURS}" + ChatColor.DARK_GRAY + "/" + ChatColor.RED + "${JOUEURS_MAX}" + ChatColor.DARK_GRAY + "]";
                message = message.replace("${PSEUDO}", player.getName());
                message = message.replace("${JOUEURS}", String.valueOf(info.getConnectedPlayers()));
                message = message.replace("${JOUEURS_MAX}", String.valueOf(info.getTotalMaxPlayers()));

                if(info.getConnectedPlayers() > info.getMaxPlayers()) {
                    message += ChatColor.GREEN + " [Slots VIP]";
                }

                api.getGame().displayMessage(message);
            }

            return response;
        }

        return response;
    }

    @Override
    public void onModerationJoin(Player player) {

    }

    @Override
    public void onLogout(Player player) {
        /*
        GameAPI api = this.api.getGameAPI();
		if (api.getGame() != null) {
			api.getGame().logout(event.getPlayer());
		}
         */
    }
}
