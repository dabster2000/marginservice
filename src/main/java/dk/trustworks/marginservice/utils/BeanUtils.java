package dk.trustworks.marginservice.utils;

import io.vertx.core.json.JsonObject;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BeanUtils {
    public BeanUtils() {
    }

    public static void populateFields(Object o, JsonObject json) {
        for (Field field : o.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (type.equals(String.class)) {
                try {
                    field.set(o, json.getString(field.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (type.equals(Boolean.TYPE)) {
                try {
                    field.set(o, json.getBoolean(field.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (type.equals(LocalDate.class)) {
                try {
                    if (json.getString(field.getName()) == null || json.getString(field.getName()).trim().length() < 10)
                        continue;
                    field.set(o, LocalDate.parse(json.getString(field.getName()).substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (type.equals(Integer.TYPE)) {
                try {
                    field.set(o, json.getInteger(field.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (type.equals(Integer.class)) {
                try {
                    field.set(o, json.getInteger(field.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (type.equals(Long.TYPE)) {
                try {
                    field.set(o, json.getLong(field.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (type.equals(Double.TYPE)) {
                try {
                    field.set(o, json.getDouble(field.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (type.isEnum()) {
                try {
                    if(json.getString(field.getName())==null) continue;
                    field.set(o, Enum.valueOf((Class<Enum>) field.getType(), json.getString(field.getName())));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}