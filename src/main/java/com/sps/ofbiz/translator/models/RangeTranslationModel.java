package com.sps.ofbiz.translator.models;

public class RangeTranslationModel extends TranslationFileModel {
    

    private String range;
    private boolean skipTranslated;

    public String getRange() {
        return range;
    }
    public void setRange(String range) {
        this.range = range;
    }


    public boolean isSkipTranslated() {
        return skipTranslated;
    }

    public void setSkipTranslated(boolean skipTranslated) {
        this.skipTranslated = skipTranslated;
    }


}
