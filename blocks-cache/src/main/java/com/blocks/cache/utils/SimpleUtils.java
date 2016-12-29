package com.blocks.cache.utils;

import java.io.*;

/**
 * Created by lotus on 2016/12/26.
 */
public class SimpleUtils {

    public static byte[] serialize(Object obj){
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object unserialize(byte[] bytes){
        if (null != bytes){
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            try {
                // 序列化
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
