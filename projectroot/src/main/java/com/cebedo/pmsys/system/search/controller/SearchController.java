package com.cebedo.pmsys.system.search.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.system.search.model.SearchResult;
import com.cebedo.pmsys.system.search.service.SearchService;

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
	public @ResponseBody List<SearchResult> search(
			@RequestParam(PARAM_SEARCH_INPUT) String searchInput) {
		List<SearchResult> result = new ArrayList<SearchResult>();
		for (SearchResult searchResult : this.searchService.getData()) {
			if (searchResult == null || searchResult.getText().isEmpty()) {
				continue;
			}
			String resultText = searchResult.getText();
			if (resultText.contains(searchInput)) {
				result.add(searchResult);
			}
		}
		return result;
	}
}