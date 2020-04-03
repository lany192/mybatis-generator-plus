package com.github.lany192.generator;

import com.github.lany192.generator.utils.OtherUtils;

public class Test {

    public static void main(String[] args) {
        System.out.println(byte[].class.getName());
        try {
            Class clazz = Class.forName("[B");
            System.out.println(byte[].class == clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("-------------------------------------");
        String name = "mmsMemberLevel";
        System.out.println(name + "转" + OtherUtils.hump2path(name));
        String name2 = "IntegrationChangeHistory";
        System.out.println(name2 + "转" + OtherUtils.hump2path(name2));
    }
}
