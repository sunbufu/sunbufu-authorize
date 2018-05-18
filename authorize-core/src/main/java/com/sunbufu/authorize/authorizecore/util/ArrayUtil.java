package com.sunbufu.authorize.authorizecore.util;

import org.springframework.stereotype.Component;

/**
 * 数组工具类
 *
 * @author sunbufu
 */
@Component
public class ArrayUtil {

    /**
     * 合并
     *
     * @param a
     * @param b
     * @return
     */
    public String[] concat(String[] a, String[] b) {
        if(a == null){
            a = new String[0];
        }
        if(b == null){
            b = new String[0];
        }

        String[] c = new String[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
}
