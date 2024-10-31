package com.sps.ofbiz.translator.models;


public class TranslationFileModel {

	private String path;
	private String otherLangIsoCode;
	private String otherLangName;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getOtherLangIsoCode() {
		return otherLangIsoCode;
	}

	public void setOtherLangIsoCode(String otherLangIsoCode) {
		this.otherLangIsoCode = otherLangIsoCode;
	}

	public String getOtherLangName() {
		return otherLangName;
	}

	public void setOtherLangName(String otherLangName) {
		this.otherLangName = otherLangName;
	}
}
