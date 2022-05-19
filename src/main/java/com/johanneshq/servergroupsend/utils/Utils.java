package com.johanneshq.servergroupsend.utils;

import com.johanneshq.servergroupsend.ServerGroupSend;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import nl.chimpgamer.networkmanager.api.models.player.Player;
import nl.chimpgamer.networkmanager.api.models.servers.Server;
import nl.chimpgamer.networkmanager.api.models.servers.ServerGroup;
import nl.chimpgamer.networkmanager.api.models.servers.balancer.BalanceMethodType;

import java.util.Optional;

public class Utils {

    /**
     * Moves a player into a server group with specified load balancing method.
     * @param player The player to move.
     * @param groupName The group name to move the player into.
     * @param balanceMethodType The load balancing method to use.
     */
    public static void movePlayerToGroup(ProxiedPlayer player, String groupName, BalanceMethodType balanceMethodType) {
        BalanceMethodType methodType = balanceMethodType;

        Optional<Player> nmPlayer = ServerGroupSend.get().getNetworkManager().getPlayerSafe(player.getUniqueId());
        if (nmPlayer.isEmpty()) {
            return;
        }

        Optional<ServerGroup> optionalServerGroup = ServerGroupSend.get().getNetworkManager().getCacheManager().getCachedServers().getServerGroupSafe(groupName);
        if (optionalServerGroup.isEmpty()) {
            ServerGroupSend.get().getLogger().info("We are trying to sent " + player.getName() + " to the group. But the group does not exist, make sure you have defined the right group name!");
            return;
        }

        long onlineServers = ServerGroupSend.get().getNetworkManager().getCacheManager().getCachedServers().getServers().values()
                .stream().filter(server ->
                        server.getServerGroups()
                                .stream()
                                .anyMatch(id -> id.getId() == optionalServerGroup.get().getId()))
                .filter(Server::getOnline).count();

        if (onlineServers <= 1) {
            methodType = BalanceMethodType.RANDOM;
        }

        Server randomServerFromServerGroup = ServerGroupSend.get().getNetworkManager().getCacheManager().getCachedServers().getServerFromGroup(nmPlayer.get(), optionalServerGroup.get(), methodType);
        if (randomServerFromServerGroup == null) {
            ServerGroupSend.get().getLogger().info("There is no available server found in group: " + optionalServerGroup.get().getGroupName());
            return;
        }

        movePlayerToServer(nmPlayer.get(), randomServerFromServerGroup);
    }

    public static void movePlayerToServer(Player player, Server server) {
        player.connect(server);
    }

}
