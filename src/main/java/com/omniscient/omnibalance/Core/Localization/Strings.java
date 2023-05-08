package com.omniscient.omnibalance.Core.Localization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omniscient.omnibalance.Core.Core;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;

public class Strings {
    public static String defaultLanguage = "EN";
    public static List<Language> languages = new ArrayList<>();
    public static Map<String, Map<String, String>> strings = new HashMap<>();

    public static void load(String jsonFile) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(jsonFile);
            defaultLanguage = mapper.readValue(json.get("defaultLanguage").traverse(), new TypeReference<>(){});
            languages = mapper.readValue(json.get("languages").traverse(), new TypeReference<>(){});
            strings = mapper.readValue(json.get("strings").traverse(), new TypeReference<>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Language fromCode(String code){
        return Strings.languages.stream()
                .filter(l -> l.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseGet(() -> fromCode(Strings.defaultLanguage));
    }

    public static String make(String stringID, Language lang, String... replacements){
        Map<String, String> translations = strings.get(stringID);
        if(translations == null || translations.isEmpty()) return StringUtils.chop(Arrays.stream(replacements).reduce(stringID+":", (a, b) -> a+b+","));
        String string = translations.get(lang.getCode());
        if(string == null) return replaceWith(translations.get(translations.keySet().stream().findFirst().orElse("")), replacements);
        return Core.color(replaceWith(string, replacements));
    }
    public static String make(String stringID, String... replacements){
        return make(stringID, fromCode(defaultLanguage), replacements);
    }

    public static String replaceWith(String string, String... replacements){
        for(String replacement : replacements)
            string = string.replaceFirst("%%", replacement);
        return string;
    }
}
