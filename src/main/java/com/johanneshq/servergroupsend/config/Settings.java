package com.johanneshq.servergroupsend.config;

import com.johanneshq.servergroupsend.ServerGroupSend;
import nl.chimpgamer.networkmanager.api.utils.FileUtils;

public class Settings extends FileUtils {
    public Settings(ServerGroupSend eventMessages) {
        super(eventMessages.getDataFolder().getPath(), "settings.yml");
        setupFile(eventMessages.getResource("settings.yml"));
    }

    @Override
    public void reload() {
        super.reload();
    }

}
