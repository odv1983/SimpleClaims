package com.buuz135.simpleclaims.util;

import com.hypixel.hytale.server.core.Message;

public final class TranslationHelper {

    private TranslationHelper() {
    }

    public static String rawTextOrEnglish(String translationKey, String englishLangKey) {
        String rawText = resolveRawText(translationKey);
        if (rawText != null) {
            return rawText;
        }

        // Some call sites/APIs resolve "ui.ui.*", others "ui.*": try both forms.
        if (translationKey.startsWith("ui.ui.")) {
            String altKey = "ui." + translationKey.substring("ui.ui.".length());
            rawText = resolveRawText(altKey);
            if (rawText != null) {
                return rawText;
            }
        } else if (translationKey.startsWith("ui.")) {
            String altKey = "ui.ui." + translationKey.substring("ui.".length());
            rawText = resolveRawText(altKey);
            if (rawText != null) {
                return rawText;
            }
        }
        // If native translation lookup fails, use configured language files first.
        String configuredLangValue = Lang.get(englishLangKey);
        if (configuredLangValue != null && !configuredLangValue.isBlank() && !configuredLangValue.equals(englishLangKey)) {
            return configuredLangValue;
        }
        return Lang.get("en-US", englishLangKey);
    }

    private static String resolveRawText(String translationKey) {
        Message translated = Message.translation(translationKey);
        if (translated == null) {
            return null;
        }
        String rawText = translated.getRawText();
        if (rawText == null || rawText.isBlank()) {
            return null;
        }
        // If unresolved, some implementations return the key itself.
        return rawText.equals(translationKey) ? null : rawText;
    }
}
