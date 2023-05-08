package com.omniscient.omnibalance.Core.Localization;

public class Language {
    private String code;
    private String name;
    private String[] countries;

    public Language(){}
    public Language(String code, String name, String... countries){
        this.code = code;
        this.name = name;
        this.countries = countries;
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public String[] getCountries() {
        return countries;
    }
}
