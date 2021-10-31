package me.darkeyedragon.randomtp.common.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class AddonClassLoader extends URLClassLoader {

    public AddonClassLoader(URL url, ClassLoader classLoader) {
        super(new URL[]{url}, classLoader);
    }
}
