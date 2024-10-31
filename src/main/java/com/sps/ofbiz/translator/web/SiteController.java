package com.sps.ofbiz.translator.web;

import com.sps.ofbiz.translator.services.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sps.ofbiz.translator.services.FMTemplateUtil;
import com.sps.ofbiz.translator.utils.StyleGenUtil;
import com.sps.ofbiz.translator.models.RangeTranslationModel;
import com.sps.ofbiz.translator.models.TranslationFileModel;
import com.sps.ofbiz.translator.models.TranslationItem;


@Controller
@RequestMapping("/")
public class SiteController {
	
	private Logger logger = LoggerFactory.getLogger(SiteController.class);


	@Autowired
	private TranslationService translationService;

	@GetMapping
	public String index(Model model) {
		this.translationService.clear();
		model.addAttribute("translations", this.translationService.getTranslations());
		model.addAttribute("totalItems", this.translationService.getTotalItems());
		model.addAttribute("locales", this.translationService.getLocales());
		model.addAttribute("attachment", new TranslationFileModel());
		model.addAttribute("rModel", new RangeTranslationModel());
		return "index";
	}
	
	@PostMapping
	public String loadFile(TranslationFileModel attachment, Model model) {
		this.translationService.setTranslationFileModel(attachment);
		this.translationService.loadTranslations();
		var totalItems = this.translationService.getTotalItems();
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("translations", this.translationService.getTranslations());
		model.addAttribute("attachment", attachment);
		model.addAttribute("locales", this.translationService.getLocales());

		RangeTranslationModel rModel = new RangeTranslationModel();
		rModel.setPath(attachment.getPath());
		rModel.setOtherLangIsoCode(attachment.getOtherLangIsoCode());
		rModel.setOtherLangName(attachment.getOtherLangName());
		model.addAttribute("rModel",rModel);
		return "index";
	}
	
	@PostMapping("/gpt")
	public String gpt(TranslationItem trans, Model model) {
		this.translationService.translateItemWithGPT(trans);
        model.addAttribute("trans", trans);
        return "fragments :: gptItem";
	
	}

	
	@PostMapping("/update")
	public String update(TranslationItem trans, Model model) {
	  
		if(this.translationService.updateOriginalData(trans)){
			FMTemplateUtil.saveTranslations(this.translationService.getOriginalData(),
					this.translationService.getTranslationFileModel().getPath());
		}
		trans.setStyle(StyleGenUtil.getRandomStyle());
        model.addAttribute("trans", trans);
        return "fragments :: gptItem";
	}


	@PostMapping("/trans-range")
	public String translateRange(RangeTranslationModel rModel, Model model){

		var updated = this.translationService.translateRange(rModel);

		model.addAttribute("translations", this.translationService.getTranslations());
		model.addAttribute("totalItems", this.translationService.getTotalItems());
		model.addAttribute("attachment", updated);
		model.addAttribute("rModel", updated);
		model.addAttribute("locales", this.translationService.getLocales());
		return "index";
	}


}
