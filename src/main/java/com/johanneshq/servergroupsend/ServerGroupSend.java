package com.johanneshq.servergroupsend;

import com.johanneshq.servergroupsend.config.Settings;
import com.johanneshq.servergroupsend.listeners.PluginMessageListener;
import com.johanneshq.servergroupsend.utils.Message;
import nl.chimpgamer.networkmanager.api.extensions.NMExtension;
import nl.chimpgamer.networkmanager.api.utils.PlatformType;

public class ServerGroupSend extends NMExtension {

    private static ServerGroupSend INSTANCE;

    public Settings settings = new Settings(this);

    public boolean debug = false;

    @Override
    protected void onConfigsReload() {
        settings.reload();
        debug = settings.getBoolean("debug");
    }

    @Override
    protected void onDisable() {

    }

    @Override
    protected void onEnable() {
        INSTANCE = this;
        if (networkManagerPlugin.getPlatformType() != PlatformType.BUNGEECORD) {
            getLogger().severe("This extension only works for BungeeCord");
            return;
        }

        settings.reload();
        debug = settings.getBoolean("debug");

        for (Message value : Message.values()) {
            getNetworkManager().getStorage().getDao().getLanguagesDao().insertLanguageMessage(value.getKey(), value.getMessage(), value.getVersion(), value.getPluginName());
        }

        getNetworkManager().registerListener(new PluginMessageListener());
    }

    public static ServerGroupSend get() {
        return INSTANCE;
    }

    public void debug (String message) {
        if (debug) getLogger().warning("ServerGroupSend[debug]: " + message);
    }

}
