package com.johanneshq.servergroupsend.utils;

public enum Message {

    NO_AVAILABLE_SERVER("servergroupsend.no_available_server", "There is no available server found to the group %groupname% we're trying to send you to!", "1.0.1","servergroupsend"),
    GROUP_NOT_EXIST("servergroupsend.group_not_exist", "The group %groupname% does not exist!", "1.0.1", "servergroupsend");

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

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }

    public String getVersion() {
        return version;
    }

    public String getPluginName() {
        return pluginName;
    }
}
