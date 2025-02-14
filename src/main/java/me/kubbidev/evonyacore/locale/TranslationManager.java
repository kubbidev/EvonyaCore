package me.kubbidev.evonyacore.locale;

import me.kubbidev.nexuspowered.util.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class TranslationManager {
    private TranslationManager() {}

    private static final Map<String, String> translations = new ConcurrentHashMap<>();

    private static final char PLACEHOLDER_OPENING = '<';
    private static final char PLACEHOLDER_CLOSING = '>';

    public static @Nullable String addTranslation(String key, String translation) {
        Objects.requireNonNull(key, "key");
        return TranslationManager.translations.put(key, translation);
    }

    public static @Nullable String removeTranslation(String key) {
        return translations.remove(Objects.requireNonNull(key, "key"));
    }

    public static void reloadTranslations(Map<String, String> translations) {
        TranslationManager.translations.clear();
        TranslationManager.translations.putAll(translations);
    }

    public static void addTranslations(@NotNull Map<String, String> translations) {
        TranslationManager.translations.putAll(translations);
    }

    public static @NotNull Set<String> keySet() {
        return translations.keySet();
    }

    @FunctionalInterface
    public interface TranslationConsumer {
        void accept(@NotNull String key, @NotNull String translation);
    }

    public static void forEach(@NotNull TranslationConsumer action) {
        translations.forEach(action::accept);
    }

    public static @Nullable String getTranslation(String key) {
        return translations.get(Objects.requireNonNull(key, "key"));
    }

    @Contract("_, !null -> !null")
    public static @Nullable String getTranslationDefault(String key, @Nullable String defaultValue) {
        return translations.getOrDefault(Objects.requireNonNull(key, "key"), defaultValue);
    }

    @Contract("!null -> !null")
    public static @Nullable String render(String key) {
        if (key == null) return null;
        String translated = translations.get(key);
        return translated == null ? key : Text.colorize(translated);
    }

    @Contract("!null, _ -> !null")
    public static @Nullable String render(String key, Object... args) {
        if (key == null) return null;
        String translated = translations.get(key);
        if (translated == null) return key;

        int i = 0;
        while ((i + 1) < args.length) {
            Object k = Objects.requireNonNull(args[i++], "arg key");
            translated = applyPlaceholders(translated, k, args[i++]);
        }
        return Text.colorize(translated);
    }

    private static String applyPlaceholders(@NotNull String text, Object key, Object value) {
        return text.replace(PLACEHOLDER_OPENING + key.toString() + PLACEHOLDER_CLOSING, Text.colorize(Objects.toString(value)));
    }
}
