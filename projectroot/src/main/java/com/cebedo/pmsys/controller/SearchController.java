package com.cebedo.pmsys.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.model.SearchResult;
import com.cebedo.pmsys.service.SearchService;
import com.google.gson.Gson;

@Controller
@RequestMapping(SearchController.CONTROLLER_MAPPING)
public class SearchController {

	public static final String CONTROLLER_MAPPING = "search";
	private static final String PARAM_SEARCH_INPUT = "searchInput";

	private SearchService searchService;

	@Autowired(required = true)
	@Qualifier(value = "searchService")
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT })
	public @ResponseBody String search(
			@RequestParam(PARAM_SEARCH_INPUT) String searchInput) {
		List<SearchResult> result = new ArrayList<SearchResult>();
		for (SearchResult searchResult : this.searchService.getData()) {
			if (searchResult == null || searchResult.getText().isEmpty()) {
				continue;
			}
			String resultText = searchResult.getText();
			if (resultText.toLowerCase().contains(searchInput.toLowerCase())) {
				result.add(searchResult);
			}
		}
		Gson gson = new Gson();
		return gson.toJson(result);
	}
}