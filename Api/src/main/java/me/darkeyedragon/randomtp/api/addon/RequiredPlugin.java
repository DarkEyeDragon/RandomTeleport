package me.darkeyedragon.randomtp.api.addon;

public interface RequiredPlugin {
    String getMinVersion();
    String getMaxVersion();
    boolean isLoaded();
    void setLoaded(boolean b);
    String getName();

}
