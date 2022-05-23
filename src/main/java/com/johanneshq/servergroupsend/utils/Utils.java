package com.johanneshq.servergroupsend.utils;

import com.johanneshq.servergroupsend.ServerGroupSend;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import nl.chimpgamer.networkmanager.api.models.player.Player;
import nl.chimpgamer.networkmanager.api.models.servers.Server;
import nl.chimpgamer.networkmanager.api.models.servers.ServerGroup;
import nl.chimpgamer.networkmanager.api.models.servers.balancer.BalanceMethodType;

import java.util.Collections;
import java.util.Optional;

public class Utils {

    /**
     * Moves a player into a server group with specified load balancing method.
     * @param player The player to move.
     * @param groupName The group name to move the player into.
     * @param balanceMethodType The load balancing method to use.
     */
    public static void movePlayerToGroup(ProxiedPlayer player, String groupName, BalanceMethodType balanceMethodType) {
        Optional<Player> nmPlayer = ServerGroupSend.get().getNetworkManager().getPlayerSafe(player.getUniqueId());
        if (nmPlayer.isEmpty()) {
            ServerGroupSend.get().debug("nmPlayer is empty!");
            return;
        }

        Optional<ServerGroup> optionalServerGroup = ServerGroupSend.get().getNetworkManager().getCacheManager().getCachedServers().getServerGroupSafe(groupName);
        if (optionalServerGroup.isEmpty()) {
            Message.GROUP_NOT_EXIST.send(nmPlayer.get(), Collections.singletonMap("<groupname>", groupName));
            ServerGroupSend.get().debug("optionalServerGroup is empty!");
            return;
        }

        Server randomServerFromServerGroup = ServerGroupSend.get().getNetworkManager().getCacheManager().getCachedServers().getServerFromGroup(nmPlayer.get(), optionalServerGroup.get(), balanceMethodType);
        if (randomServerFromServerGroup == null) {
            ServerGroupSend.get().debug("randomServerFromServerGroup is null");
            Message.NO_AVAILABLE_SERVER.send(nmPlayer.get(), Collections.singletonMap("<groupname>", optionalServerGroup.get().getGroupName()));
            return;
        }

        movePlayerToServer(nmPlayer.get(), randomServerFromServerGroup);
    }

    public static void movePlayerToServer(Player player, Server server) {
        ServerGroupSend.get().debug("movePlayerToServer=" + player.getName() + ", server=" + server.getServerName());
        player.connect(server);
        nl.chimpgamer.networkmanager.api.values.Message.SERVER_CONNECTING.send(player, Collections.singletonMap("%servername%", server.getDisplayName()));
    }

}
