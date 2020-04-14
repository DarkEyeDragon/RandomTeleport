package me.darkeyedragon.randomtp.config.data;

class ConfigMessage {

    private String initMessage;
    private String teleportMessage;
    private String blacklistMessage;

    public void setInitMessage(String initMessage) {
        this.initMessage = initMessage;
    }

    public void setTeleportMessage(String teleportMessage) {
        this.teleportMessage = teleportMessage;
    }

    public void setBlacklistMessage(String blacklistMessage) {
        this.blacklistMessage = blacklistMessage;
    }

    public String getInitMessage() {
        return initMessage;
    }

    public String getTeleportMessage() {
        return teleportMessage;
    }

    public String getBlacklistMessage() {
        return blacklistMessage;
    }
}
