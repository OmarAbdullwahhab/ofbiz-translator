package com.sps.ofbiz.translator.services;

import com.sps.ofbiz.translator.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class TranslationService {

    private List<TranslationItemsModel> originalData = new ArrayList<>();
    private List<TranslationItem> translations = new ArrayList<>();
    private TranslationFileModel translationFileModel;


    @Autowired
    private OpenAITranslator gptTranslator;

    public void clear(){
        this.originalData.clear();
        this.translations.clear();
    }
    public void loadTranslations() {

        try {
            this.translations.clear();
            this.originalData = LabelFileHandler.loadTranslations(this.translationFileModel.getPath());
            int index = 1;
            for(var item : originalData) {
                TranslationItem ti = new TranslationItem();
                ti.setOtherLangIsoCode(this.translationFileModel.getOtherLangIsoCode());
                ti.setKey(item.getKey());
                ti.setId(String.format("lbl_%d", index++));
                var en = item.getTranslations().stream().filter(x->x.getKey().equals("en")).findFirst();
                if(en.isPresent()) {
                    ti.setEn(en.get().getValue());
                }
                else {
                    ti.setEn("");
                }
                var otherLangText = item.getTranslations().stream().filter(x->x.getKey().equals(this.translationFileModel.getOtherLangIsoCode())).findFirst();
                if(otherLangText.isPresent()) {
                    ti.setOtherLangText(otherLangText.get().getValue());
                }else {
                    ti.setOtherLangText("");
                }
                this.translations.add(ti);
            }
            this.translations.sort((x,y) -> {
                Integer xid = Integer.parseInt(x.getId().replace("lbl_", ""));
                Integer yid = Integer.parseInt(y.getId().replace("lbl_", ""));
                return xid.compareTo(yid);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TranslationFileModel getTranslationFileModel() {
        return translationFileModel;
    }

    public void setTranslationFileModel(TranslationFileModel translationFileModel) {
        this.translationFileModel = translationFileModel;
    }

    public int getTotalItems() {
        return this.translations.size();
    }



    public List<TranslationItem> getTranslations() {
        return translations;
    }

    public boolean updateOriginalData(TranslationItem trans){
        var found = this.originalData.stream().filter(x->x.getKey().equals(trans.getKey())).findFirst();
        if(found.isPresent()) {
            var tr = found.get().getTranslations().stream().filter(x->trans.getOtherLangIsoCode().equalsIgnoreCase(x.getKey())).findFirst();
            if(tr.isPresent()) {
                tr.get().setValue(trans.getOtherLangText());
            }
            else {
                found.get().addTranslation(trans.getOtherLangIsoCode(), trans.getOtherLangText());
            }
            return true;
        }
        return false;
    }

    public List<TranslationItemsModel> getOriginalData() {
        return originalData;
    }

    public void translateItemWithGPT(TranslationItem trans){
        try {
            if(trans.getEn() == null || trans.getEn().isEmpty()){
                return;
            }
            int lengthOfOtherLangTokens = trans.getEn().length() + 10;
            var translated = this.gptTranslator.translate(trans.getEn(),this.getOtherLangName(trans.getOtherLangIsoCode()), lengthOfOtherLangTokens);
            trans.setOtherLangText(translated);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public RangeTranslationModel translateRange(RangeTranslationModel rModel) {
        this.setTranslationFileModel(rModel);
        this.loadTranslations();

        String[] items = rModel.getRange().split("-");
        if(items.length == 2){
            int start = Integer.parseInt(items[0]);
            int end  = Integer.parseInt(items[1]);
            this.translations.sort((x,y) -> {
                Integer xid = Integer.parseInt(x.getId().replace("lbl_", ""));
                Integer yid = Integer.parseInt(y.getId().replace("lbl_", ""));
                return xid.compareTo(yid);
            });
            for(var trans : this.translations){
                trans.setOtherLangIsoCode(rModel.getOtherLangIsoCode());
                if(rModel.isSkipTranslated()  && trans.getOtherLangText() != null && !trans.getOtherLangText().trim().isEmpty()){ //SKIP Translated already
                    continue;
                }
                int id = Integer.parseInt(trans.getId().replace("lbl_", ""));
                if(id >= start && id <= end){
                    this.translateItemWithGPT(trans);
                    this.updateOriginalData(trans);
                }
                if(id > end){ // previous sort has to be present for
                    break;    // this statment to work correctly
                }
            }
        }
        FMTemplateUtil.saveTranslations(originalData, rModel.getPath());

        return rModel;
    }

    public List<LocaleModel> getLocales(){
        List<LocaleModel> locales = Locale.availableLocales()
                .sorted(Comparator.comparing(Locale::getDisplayName)).map(x -> new LocaleModel(x)).toList();
        return locales;
    }

    public String getOtherLangName(String isoCode){
        var found = Locale.availableLocales().map(x -> new LocaleModel(x)).filter(x->x.getKey().equals(isoCode)).findFirst();
        if(found.isPresent()){
            return found.get().getLanguage();
        }
        return "English";
    }


}
