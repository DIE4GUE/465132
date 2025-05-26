package com.example.english_learning.model;

import java.util.Map;

public enum CardType {
    LANGUAGE, TEXT;

    private final static Map<String, CardType> CARD_TYPE_MATCHER = Map.of(
            "Language", LANGUAGE,
            "Text answer", TEXT
    );

    public static CardType defaultType() {
        return LANGUAGE;
    }

    public static CardType getByPreview(String str) {
        return CARD_TYPE_MATCHER.get(str);
    }
}
