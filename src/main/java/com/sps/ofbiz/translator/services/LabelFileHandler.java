package com.sps.ofbiz.translator.services;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sps.ofbiz.translator.models.TranslationItemsModel;

public class LabelFileHandler extends DefaultHandler {

	 private TranslationItemsModel currentItem;
	    private List<TranslationItemsModel> allTranslations = new ArrayList<>();
	    private boolean insideTrans = false;
	    private String currentKey = "";
	    private String currentText = "";
	    private Set<String> currentLocales = new HashSet<>();
	    
		@Override
	    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	        if (qName.equals("property")) {
	            this.currentItem = new TranslationItemsModel();
	            this.currentItem.setKey(attributes.getValue("key"));
	            insideTrans = false;
	        } else if (qName.equals("value")) {
	            currentKey = attributes.getValue("xml:lang");
	            insideTrans = true;
	            this.currentLocales.add(currentKey);
	        } else {
	            insideTrans = false;
	        }

	    }

	    @Override
	    public void characters(char[] chars, int start, int length) {
	        if (this.insideTrans) {
	            currentText = new String(chars, start, length);
	        }
	    }

	    @Override
	    public void endElement(String uri, String localName, String qName) throws SAXException {
	        if (qName.equals("property")) {
	            this.allTranslations.add(this.currentItem);
	        } else if (qName.equals("value")) {
	            this.currentItem.addTranslation(this.currentKey, this.currentText);
	            // System.out.println("Trans: " + currentTrans.getValue());
	        }
	        insideTrans = false;
	    } 
	    
	     List<TranslationItemsModel> getAllTrans(){
	    	return this.allTranslations;
	    }
	    
	    public static List<TranslationItemsModel>  loadTranslations(String path) throws Exception {
			//FMTemplateUtil.createConfiguration();
			//String path = "/home/omar/source/mine_ofbiz/ofbiz-framework/applications/accounting/config/AccountingUiLabels.xml";
	    	LabelFileHandler lh = new LabelFileHandler();
			 SAXParser sax = SAXParserFactory.newDefaultInstance().newSAXParser();
	         var is = new FileInputStream(path);
	         
	         sax.parse(is, lh);
	         is.close();
			var translations = lh.getAllTrans();
			translations.sort((x,y)-> x.getKey().compareTo(y.getKey()));
			
			System.out.println("Total translations " + translations.size());
			
		
			return lh.getAllTrans();
			
			
			//System.out.println(getOption());
			//sendMessageToChatGPT("Problem getting PaymentApplication(s) for Invoice ID", "Arabic", "Problem getting PaymentApplication(s) for Invoice ID".length());
		}
}
