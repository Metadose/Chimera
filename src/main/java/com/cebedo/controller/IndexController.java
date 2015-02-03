package com.cebedo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cebedo.constants.BasecodeConstants;

@Controller
public class IndexController {
	
	@RequestMapping(value={BasecodeConstants.URL_ROOT, BasecodeConstants.URL_INDEX})
	public String index() {
		return BasecodeConstants.SCR_INDEX;
	}
}