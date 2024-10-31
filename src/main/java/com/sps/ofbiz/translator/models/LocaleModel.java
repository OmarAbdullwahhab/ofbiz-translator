package com.sps.ofbiz.translator.models;

import java.util.Locale;

public class LocaleModel {

    private String key;

    private String name;

    private String language;

    public LocaleModel(){

    }

    public LocaleModel(Locale locale){
        this.key = locale.getLanguage();
        if(locale.getCountry() != null && !locale.getCountry().isEmpty()){
            this.key += "-" + locale.getCountry();
        }
        this.name = locale.getDisplayName();
        this.language = locale.getDisplayLanguage();
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
