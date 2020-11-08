package me.darkeyedragon.randomtp.common.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AddonClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b = loadClassFromFile(name);
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassFromFile(String fileName) throws ClassNotFoundException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName.replace('.', File.separatorChar)+".class");
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        if(inputStream == null) throw new ClassNotFoundException();
        int nextValue = 0;
        try {
            while((nextValue = inputStream.read()) != -1){
                byteStream.write(nextValue);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return byteStream.toByteArray();
    }
}
