package com.johanneshq.servergroupsend.listeners;

import com.johanneshq.servergroupsend.ServerGroupSend;
import com.johanneshq.servergroupsend.utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import nl.chimpgamer.networkmanager.api.models.servers.balancer.BalanceMethodType;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PluginMessageListener implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if ( !event.getTag().equals("BungeeCord") ) return;

        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(event.getReceiver().toString());
        if (proxiedPlayer == null)
            return;

        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String action = dataInputStream.readUTF();
            String target = dataInputStream.readUTF();

            //format: "group:GROUP:RANDOM"
           if (action.equalsIgnoreCase("connect")) {
               ServerGroupSend.get().debug("ACTION: " + action);
               ServerGroupSend.get().debug("TARGET: " + target);
               String[] split = target.split(":");
               if (split[0].equalsIgnoreCase("group")) {
                   Utils.movePlayerToGroup(proxiedPlayer, split[1], split[2] == null ? BalanceMethodType.RANDOM : parseBalanceMethod(split[2]));
                   event.setCancelled(true);
               }
            }

        } catch (IOException e) { /* ignored */}

    }

    private BalanceMethodType parseBalanceMethod(String method) {
        ServerGroupSend.get().debug("parseBalanceMethod: " + method);
        BalanceMethodType balanceMethodType = BalanceMethodType.RANDOM; //Default
        try {
            balanceMethodType = BalanceMethodType.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            ServerGroupSend.get().getLogger().severe("The BalanceMethode does not exist! Method: " + method.toUpperCase() + " using the default one instead: " + balanceMethodType.name());
            ServerGroupSend.get().debug("parseBalanceMethod Catch Type: " + balanceMethodType.name());
            return balanceMethodType;
        }
        ServerGroupSend.get().debug("parseBalanceMethod Type: " + balanceMethodType.name());
        return balanceMethodType;
    }
}