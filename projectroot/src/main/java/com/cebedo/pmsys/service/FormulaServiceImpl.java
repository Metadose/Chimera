package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.Formula;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.repository.FormulaValueRepo;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxFactory;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

@Service
public class FormulaServiceImpl implements FormulaService {

    private static final String WA_APP_ID = "AU7QGR-T7QT7QVGKY";
    private AuthHelper authHelper = new AuthHelper();
    private FormulaValueRepo formulaValueRepo;
    private WAEngine wolframAlphaEngine = new WAEngine();

    public void setFormulaValueRepo(FormulaValueRepo formulaValueRepo) {
	this.formulaValueRepo = formulaValueRepo;
    }

    @Override
    @Transactional
    public void rename(Formula obj, String newKey) {
	this.formulaValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, Formula> m) {
	this.formulaValueRepo.multiSet(m);
    }

    @Override
    @Transactional
    public String set(Formula obj) {

	if (!obj.isValid()) {

	}

	if (obj.getCompany() == null) {
	    obj.setCompany(this.authHelper.getAuth().getCompany());
	}

	// If we're creating.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.formulaValueRepo.set(obj);
	    return AlertBoxFactory.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_FORMULA, obj.getName());
	}
	this.formulaValueRepo.set(obj);
	return AlertBoxFactory.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_FORMULA, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.formulaValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(Formula obj) {
	this.formulaValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public Formula get(String uuid) {
	String key = Formula.constructKey(this.authHelper.getAuth()
		.getCompany(), uuid);
	return this.formulaValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.formulaValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<Formula> multiGet(Collection<String> keys) {
	return this.formulaValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public List<Formula> list() {
	AuthenticationToken auth = this.authHelper.getAuth();
	String pattern = Formula.constructPattern(auth.getCompany());
	Set<String> keys = this.formulaValueRepo.keys(pattern);
	List<Formula> fList = this.formulaValueRepo.multiGet(keys);
	return fList;
    }

    @Override
    @Transactional
    public String test(Formula formula) {

	String returnStr = "";
	String input = "(1/2)*5*6";

	// TODO These properties will be set in all the WAQuery objects created
	// from this WAEngine.
	this.wolframAlphaEngine.setAppID(WA_APP_ID);
	this.wolframAlphaEngine.addFormat("plaintext");

	// Create the query.
	WAQuery query = this.wolframAlphaEngine.createQuery();

	// Set properties of the query.
	query.setInput(input);

	try {
	    // For educational purposes, print out the URL we are about to send:
	    returnStr += "\n\n\n" + "Query URL:";
	    returnStr += "\n\n\n" + this.wolframAlphaEngine.toURL(query);

	    // This sends the URL to the Wolfram|Alpha server, gets the XML
	    // result
	    // and parses it into an object hierarchy held by the WAQueryResult
	    // object.
	    WAQueryResult queryResult = this.wolframAlphaEngine
		    .performQuery(query);

	    if (queryResult.isError()) {
		returnStr += "\n\n\n" + "Query error";
		returnStr += "\n\n\n" + "  error code: "
			+ queryResult.getErrorCode();
		returnStr += "\n\n\n" + "  error message: "
			+ queryResult.getErrorMessage();
	    } else if (!queryResult.isSuccess()) {
		System.out
			.println("Query was not understood; no results available.");
	    } else {
		// Got a result.
		returnStr += "\n\n\n" + "Successful query. Pods follow:\n";
		for (WAPod pod : queryResult.getPods()) {
		    if (!pod.isError()) {
			returnStr += "\n\n\n" + pod.getTitle();
			returnStr += "\n\n\n" + "------------";
			for (WASubpod subpod : pod.getSubpods()) {
			    for (Object element : subpod.getContents()) {
				if (element instanceof WAPlainText) {
				    returnStr += "\n\n\n"
					    + ((WAPlainText) element).getText();
				    returnStr += "\n\n\n" + "";
				}
			    }
			}
			returnStr += "\n\n\n" + "";
		    }
		}
		// We ignored many other types of Wolfram|Alpha output, such as
		// warnings, assumptions, etc.
		// These can be obtained by methods of WAQueryResult or objects
		// deeper in the hierarchy.
	    }
	} catch (WAException e) {
	    e.printStackTrace();
	}

	return returnStr;
    }
}
