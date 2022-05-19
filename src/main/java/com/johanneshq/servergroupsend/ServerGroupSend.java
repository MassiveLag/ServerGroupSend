package com.johanneshq.servergroupsend;

import com.johanneshq.servergroupsend.config.Settings;
import com.johanneshq.servergroupsend.listeners.PluginMessageListener;
import nl.chimpgamer.networkmanager.api.extensions.NMExtension;
import nl.chimpgamer.networkmanager.api.utils.PlatformType;

public class ServerGroupSend extends NMExtension {

    private static ServerGroupSend eventMessages;

    public Settings settings = new Settings(this);

    @Override
    protected void onConfigsReload() {
        settings.reload();
    }

    @Override
    protected void onDisable() {

    }

    @Override
    protected void onEnable() {
        eventMessages = this;
        if (networkManagerPlugin.getPlatformType() != PlatformType.BUNGEECORD) {
            getLogger().severe("This extension only works for BungeeCord");
            return;
        }

        settings.reload();

        getNetworkManager().registerListener(new PluginMessageListener());
    }

    public static ServerGroupSend get() {
        return eventMessages;
    }
}
