package com.sps.ofbiz.translator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sps.ofbiz.translator.services.FMTemplateUtil;



@SpringBootApplication
public class OfbizTranslatorApplication {

	public static void main(String[] args) {
		FMTemplateUtil.createConfiguration();
		SpringApplication.run(OfbizTranslatorApplication.class, args);
	}

}
