package com.sps.ofbiz.translator.models;

import java.util.ArrayList;
import java.util.List;

public class TranslationItemsModel {

    private String key;
    private List<KeyValuePairModel> translations = new ArrayList<>();



    public void addTranslation(String lang, String value){
        KeyValuePairModel trans = new KeyValuePairModel(lang,value);
        this.translations.add(trans);
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<KeyValuePairModel> getTranslations() {
        return translations;
    }

    public void setTranslations(List<KeyValuePairModel> translations) {
        this.translations = translations;
    }
}
