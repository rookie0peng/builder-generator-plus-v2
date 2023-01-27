package com.peng.idea.plugin.builder.util;

import java.util.UUID;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2022/4/30 18:19
 * </pre>
 */
public class UUIDUtil {

    /**
     * 生成随机uuid，不含连接符'-'
     * @return uuid
     */
    public static String randomUUID() {
        String uuid = UUID.randomUUID().toString();
        StringBuilder stringBuilder = new StringBuilder(36);
        char c;
        for (int i = 0; i < uuid.length(); i++) {
            if ((c = uuid.charAt(i)) == '-')
                continue;
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(randomUUID());
        }

//        System.out.println(randomUUID());
    }
}
