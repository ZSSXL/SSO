package com.zss.sso.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/20 11:31
 * @desc 序列化工具
 */
@Slf4j
@SuppressWarnings("unused")
public class SerializableUtil {

    /**
     * 对象序列化
     *
     * @param o 对象
     * @return 序列化结果
     */
    public static String serializable(Object o) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(o);
            return byteArrayOutputStream.toString("ISO-8859-1");
        } catch (IOException e) {
            log.error("序列化对象失败: [{}]", e.getMessage());
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    log.error("关闭对象输出流失败: [{}]", e.getMessage());
                }
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                log.error("关闭字节输出流失败: [{}]", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 反序列化
     *
     * @param str 反序列化对象
     * @return 对象
     */
    public static Object serializeToObject(String str) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes(StandardCharsets.ISO_8859_1));
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("序列化对象失败: [{}]", e.getMessage());
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    log.error("关闭对象输出流失败: [{}]", e.getMessage());
                }
            }
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                log.error("关闭字节输出流失败: [{}]", e.getMessage());
            }
        }
        return null;
    }
}
