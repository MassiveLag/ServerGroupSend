package com.johanneshq.servergroupsend;

import com.johanneshq.servergroupsend.config.Settings;
import com.johanneshq.servergroupsend.listeners.PluginMessageListener;
import com.johanneshq.servergroupsend.utils.Message;
import nl.chimpgamer.networkmanager.api.exceptions.LanguageMessageNotFoundException;
import nl.chimpgamer.networkmanager.api.extensions.NMExtension;
import nl.chimpgamer.networkmanager.api.utils.PlatformType;

public class ServerGroupSend extends NMExtension {

    private static ServerGroupSend INSTANCE;

    public Settings settings = new Settings(this);

    public boolean debug = get().settings.getBoolean("debug");

    @Override
    protected void onConfigsReload() {
        settings.reload();
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

        for (Message value : Message.values()) {
            getNetworkManager().getStorage().getDao().getLanguagesDao().insertLanguageMessage(value.getKey(), value.getMessage(), value.getVersion(), value.getPluginName());
        }

        getNetworkManager().registerListener(new PluginMessageListener());
    }

    public static ServerGroupSend get() {
        return INSTANCE;
    }

    public void debug (String message) {
        if (!debug)
            return;

        getLogger().warning("ServerGroupSend[debug]: " + message);
    }

    public String getMessage(int languageId, Message message) {
        try {
            return getNetworkManager().getCacheManager().getCachedLanguages().getMessage(message.getPluginName(), languageId, message.getKey());
        } catch ( LanguageMessageNotFoundException ex) {
            return ex.getLocalizedMessage();
        }
    }

    public String getNMMessage(int languageId, nl.chimpgamer.networkmanager.api.values.Message message) {
        try {
            return getNetworkManager().getCacheManager().getCachedLanguages().getMessage(1, message.getKey());
        } catch ( LanguageMessageNotFoundException ex) {
            return ex.getLocalizedMessage();
        }
    }
}
