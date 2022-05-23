package com.johanneshq.servergroupsend.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextReplacementConfig;
import nl.chimpgamer.networkmanager.api.models.message.TranslatableMessage;
import nl.chimpgamer.networkmanager.api.models.sender.Sender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public enum Message implements TranslatableMessage{

    NO_AVAILABLE_SERVER("servergroupsend.no_available_server", "<bold><red>(!) <red>There is no available server found to the group <yellow><groupname> <red>we're trying to send you to!", "1.0.1","servergroupsend"),
    GROUP_NOT_EXIST("servergroupsend.group_not_exist", "<bold><red>(!) <red>The group <yellow><groupname> <red>does not exist!", "1.0.1", "servergroupsend");

    final String key;
    final String message;
    final String version;
    final String pluginName;

    Message(String key, String message, String version, String pluginName) {
        this.key = key;
        this.message = message;
        this.version = version;
        this.pluginName = pluginName;
    }

    public @NotNull String getKey() {
        return key;
    }

    public @NotNull String getMessage() {
        return message;
    }

    public @NotNull String getVersion() {
        return version;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void send(Sender sender) {
        sender.sendMessage(sender.getNetworkManagerPlugin().getMessage(sender.getLanguage(), this));
    }

    public void send(Sender sender, Map<String, ?> replacements) {
        Component messageComponent = sender.getNetworkManagerPlugin().getMessageComponent(sender.getLanguage(), this);

        for (Map.Entry<String, ?> stringEntry : replacements.entrySet()) {
            TextReplacementConfig.Builder builder = TextReplacementConfig.builder().once().matchLiteral(stringEntry.getKey());
            if (stringEntry.getValue() instanceof ComponentLike) {
                builder.replacement(((ComponentLike) stringEntry.getValue()).asComponent());
            } else {
                builder.replacement(stringEntry.getValue().toString());
            }
            messageComponent = messageComponent.replaceText(builder.build());
        }
        sender.sendMessage(messageComponent);
    }
}
