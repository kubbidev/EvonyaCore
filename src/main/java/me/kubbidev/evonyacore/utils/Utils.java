package me.kubbidev.evonyacore.utils;

import me.kubbidev.nexuspowered.time.DurationFormatter;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class Utils {

    public static String formatBoolean(boolean bool) {
        return bool ? "&aActivé" : "&cDésactivé";
    }

    public static String formatStringList(Collection<String> list) {
        return list.stream().collect(Collectors.joining("\n", "", ""));
    }

    public static String formatStringArray(String[] arr) {
        return Arrays.stream(arr).collect(Collectors.joining("\n", "", ""));
    }

    public static String getFormattedTime(long time) {
        return DurationFormatter.CONCISE_LOW_ACCURACY.format(Duration.ofSeconds(time));
    }
}
