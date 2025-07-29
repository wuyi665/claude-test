package com.allinpay.checkout.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Convert {
    public static Map<String, String> convertToMap(Object obj) throws IllegalAccessException {
        Map<String, String> resultMap = new HashMap<>();

        // 获取对象的所有字段
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            // 允许访问私有字段
            field.setAccessible(true);
            String fieldName = field.getName();
            String fieldValue = (String) field.get(obj);
            if (fieldValue == null) {
                continue;
            }
            resultMap.put(fieldName, fieldValue);
        }

        return resultMap;
    }

    public static <T> T mapToBean(Map<String, String> map, Class<T> beanClass) throws Exception {
        T obj = beanClass.getDeclaredConstructor().newInstance();

        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (map.containsKey(fieldName)) {
                field.set(obj, map.get(fieldName));
            }
        }

        return obj;
    }
}
