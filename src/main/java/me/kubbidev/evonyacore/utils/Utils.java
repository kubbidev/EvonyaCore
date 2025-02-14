package me.kubbidev.evonyacore.utils;

import org.bukkit.event.Cancellable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {

    public static String convertBooleanToString(boolean b) {
        return b ? "&aActivé" : "&cDésactivé";
    }

    public static String convertListToString(Collection<String> list) {
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String strings : list) {
            String s;
            if (i > 0) {
                s = "\n" + Color.translate(strings);
            } else s = Color.translate(strings);
            sb.append(s);

            i++;
        }
        return sb.toString();
    }

    public static String getFormattedTime(long time) {
        int d = (int) time / 86400;
        time -= (d * 86400);

        int h = (int) time / 3600;
        time -= (h * 3600);

        int m = (int) time / 60;
        time -= (m * 60);

        if (d == 0) {
            if (h == 0) {
                if (m == 0)
                    return time + "s";
                return m + "m" + time + "s";
            }
            return h + "h" + m + "m" + time + "s";
        }
        return d + "d" + h + "h" + m + "m" + time + "s";
    }

    public static List<Field> getAnnotatedFields(Class<?> c, Class<? extends Annotation> annotation) {
        List<Field> fields = new ArrayList<>();
        for (Field field : c.getFields()) {
            if (field.isAnnotationPresent(annotation)) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotation)) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        return fields;
    }

    public static boolean isCancelled(Cancellable event, Cancellable subEvent) {
        if (subEvent.isCancelled()) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }
}
