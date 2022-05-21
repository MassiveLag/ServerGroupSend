package com.johanneshq.servergroupsend.utils;

import com.johanneshq.servergroupsend.ServerGroupSend;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import nl.chimpgamer.networkmanager.api.models.player.Player;
import nl.chimpgamer.networkmanager.api.models.servers.Server;
import nl.chimpgamer.networkmanager.api.models.servers.ServerGroup;
import nl.chimpgamer.networkmanager.api.models.servers.balancer.BalanceMethodType;

import java.util.Optional;
import java.util.stream.Stream;

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
            nmPlayer.get().sendMessage(Component.text(ServerGroupSend.get().getMessage(nmPlayer.get().getLanguage().getId(), Message.GROUP_NOT_EXIST).replace("%groupname%", groupName)));
            ServerGroupSend.get().debug("optionalServerGroup is empty!");
            return;
        }

        Stream<Server> onlineServers = ServerGroupSend.get().getNetworkManager().getCacheManager().getCachedServers().getServers().values()
                .stream().filter(server ->
                        server.getServerGroups()
                                .stream()
                                .anyMatch(id -> id.getId() == optionalServerGroup.get().getId()))
                .filter(Server::getOnline);

        if (onlineServers.count() <= 1) {
            Optional<Server> first = onlineServers.findFirst();
            ServerGroupSend.get().debug("onlineServers=" + onlineServers.count());
            if (first.isEmpty()) {
                nmPlayer.get().sendMessage(Component.text(ServerGroupSend.get().getMessage(nmPlayer.get().getLanguage().getId(), Message.NO_AVAILABLE_SERVER).replace("%groupname%", optionalServerGroup.get().getGroupName())));
                return;
            }

            movePlayerToServer(nmPlayer.get(), first.get());
            return;
        }

        Server randomServerFromServerGroup = ServerGroupSend.get().getNetworkManager().getCacheManager().getCachedServers().getServerFromGroup(nmPlayer.get(), optionalServerGroup.get(), balanceMethodType);
        if (randomServerFromServerGroup == null) {
            ServerGroupSend.get().debug("randomServerFromServerGroup is null");
            nmPlayer.get().sendMessage(Component.text(ServerGroupSend.get().getMessage(nmPlayer.get().getLanguage().getId(), Message.NO_AVAILABLE_SERVER).replace("%groupname%", optionalServerGroup.get().getGroupName())));
            return;
        }

        movePlayerToServer(nmPlayer.get(), randomServerFromServerGroup);
    }

    public static void movePlayerToServer(Player player, Server server) {
        ServerGroupSend.get().debug("movePlayerToServer=" + player.getName() + ", server=" + server.getServerName());
        player.connect(server);
        player.sendMessage(Component.text(ServerGroupSend.get().getNMMessage(player.getLanguage().getId(), nl.chimpgamer.networkmanager.api.values.Message.SERVER_CONNECTING).replace("%servername%", server.getDisplayName())));
    }

}
