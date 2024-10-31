package com.sps.ofbiz.translator.models;

public class TranslationItem {

	//The translation item key
	private String key;

	//Default language english
	private String en;

	//The local iso code.
	private String otherLangIsoCode;

	//Language we want to translate to.
	private String otherLangText;


	//Used for UI only ( it will be auto generated ).
	private String id;

	private String style = "color:black;";

	//The html item direction;
	private String direction;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getOtherLangIsoCode() {
		return otherLangIsoCode;
	}

	public void setOtherLangIsoCode(String otherLangIsoCode) {
		this.otherLangIsoCode = otherLangIsoCode;
	}

	public String getOtherLangText() {
		return otherLangText;
	}

	public String getDirection() {
		if(this.otherLangIsoCode != null && this.otherLangIsoCode.startsWith("ar")){
			this.direction = "rtl";
		}
		else {
			this.direction = "ltr";
		}
		return direction;
	}

	public void setOtherLangText(String otherLangText) {
		this.otherLangText = otherLangText;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}


}
