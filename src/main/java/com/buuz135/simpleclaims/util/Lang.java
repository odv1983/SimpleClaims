package com.buuz135.simpleclaims.util;

import com.buuz135.simpleclaims.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Lang {

    private static final String DEFAULT_LANGUAGE = "en-US";
    private static final Map<String, Map<String, String>> CACHE = new ConcurrentHashMap<>();

    private Lang() {
    }

    public static String get(String key) {
        return get(Main.CONFIG.get().getLanguage(), key);
    }

    public static String get(String language, String key) {
        String normalizedLanguage = normalizeLanguage(language);
        String value = getLanguageMap(normalizedLanguage).get(key);
        if (value != null) return value;

        if (!DEFAULT_LANGUAGE.equals(normalizedLanguage)) {
            value = getLanguageMap(DEFAULT_LANGUAGE).get(key);
            if (value != null) return value;
        }
        return key;
    }

    private static Map<String, String> getLanguageMap(String language) {
        return CACHE.computeIfAbsent(language, Lang::loadLanguageFile);
    }

    private static String normalizeLanguage(String language) {
        if (language == null || language.isBlank()) return DEFAULT_LANGUAGE;
        return language;
    }

    private static Map<String, String> loadLanguageFile(String language) {
        Map<String, String> map = new HashMap<>();
        map.putAll(loadLangFile(language, "ui.lang"));
        map.putAll(loadLangFile(language, "commands.lang"));
        return map;
    }

    private static Map<String, String> loadLangFile(String language, String fileName) {
        String path = "Server/Languages/" + language + "/" + fileName;
        InputStream is = Lang.class.getClassLoader().getResourceAsStream(path);
        if (is == null) return Collections.emptyMap();

        Map<String, String> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;

                int separatorIndex = trimmed.indexOf('=');
                if (separatorIndex <= 0) continue;

                String key = trimmed.substring(0, separatorIndex).trim();
                String value = trimmed.substring(separatorIndex + 1).trim();
                if (!key.isEmpty()) {
                    map.put(key, value);
                }
            }
        } catch (IOException ignored) {
            return Collections.emptyMap();
        }
        return map;
    }
}
