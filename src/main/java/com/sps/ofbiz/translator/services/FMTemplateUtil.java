package com.sps.ofbiz.translator.services;

import com.sps.ofbiz.translator.OfbizTranslatorApplication;
import com.sps.ofbiz.translator.models.KeyValuePairModel;
import com.sps.ofbiz.translator.models.TranslationItemsModel;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.LoggerFactory;

/**
 * Freemarker template utilities
 */
public class FMTemplateUtil {

    public static Configuration FREEMARKER_CONFIG;

    private static String generateOutput(String templatePath, Object model){
        try {
            var template = FREEMARKER_CONFIG.getTemplate(templatePath);
            StringWriter sw = new StringWriter();
            template.process(model,sw);
            return  sw.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
    private static List<TranslationItemsModel> getFreeMarkerModels(
            List<TranslationItemsModel> allTranslations) {

        List<TranslationItemsModel> items = new ArrayList<>();

        for (var rowModel : allTranslations) {
            rowModel.getTranslations().sort(Comparator.comparing(KeyValuePairModel::getKey));
            items.add(rowModel);
        }

        items.sort(Comparator.comparing(TranslationItemsModel::getKey));
        return items;
    }
	
 public static void saveTranslations(List<TranslationItemsModel> allTranslations, String path) {
	try {
        var template = FREEMARKER_CONFIG.getTemplate("ofbiz/uilabels.ftl");
        Map<String, List<TranslationItemsModel>> data = new HashMap<>();
        data.put("data", getFreeMarkerModels(allTranslations));
        template.process(data, new FileWriter(path));
        LoggerFactory.getLogger(FMTemplateUtil.class).info("Items saved to the file successfully..");
    } catch (IOException e) {
        throw new RuntimeException(e);
    } catch (TemplateException e) {
        throw new RuntimeException(e);
    }
}


    public static void createConfiguration(){
        // Create your Configuration instance, and specify if up to what FreeMarker
// version (here 2.3.32) do you want to apply the fixes that are not 100%
// backward-compatible. See the Configuration JavaDoc for details.
        FREEMARKER_CONFIG = new Configuration(Configuration.VERSION_2_3_33);

// Specify the source where the template files come from. Here I set a
// plain directory for it, but non-file-system sources are possible too:

        FREEMARKER_CONFIG.setClassForTemplateLoading(OfbizTranslatorApplication.class, "/templates");


// From here we will set the settings recommended for new projects. These
// aren't the defaults for backward compatibilty.

// Set the preferred charset template files are stored in. UTF-8 is
// a good choice in most applications:
        FREEMARKER_CONFIG.setDefaultEncoding("UTF-8");

// Sets how errors will appear.
// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        FREEMARKER_CONFIG.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
        FREEMARKER_CONFIG.setLogTemplateExceptions(false);

// Wrap unchecked exceptions thrown during template processing into TemplateException-s:
        FREEMARKER_CONFIG.setWrapUncheckedExceptions(true);

// Do not fall back to higher scopes when reading a null loop variable:
        FREEMARKER_CONFIG.setFallbackOnNullLoopVariable(false);

// To accomodate to how JDBC returns values; see Javadoc!
        FREEMARKER_CONFIG.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
    }

}
