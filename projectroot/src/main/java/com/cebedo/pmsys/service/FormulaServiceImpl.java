package com.cebedo.pmsys.service;

import java.util.ArrayList;
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
import com.cebedo.pmsys.utils.StringUtils;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

@Service
public class FormulaServiceImpl implements FormulaService {

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

    /**
     * Set the formula object.
     */
    @Override
    @Transactional
    public String set(Formula obj) {

	// If the object is not valid.
	if (!obj.isValid()) {

	    // If we're creating.
	    if (obj.getUuid() == null) {
		return AlertBoxFactory.FAILED.generateCreate(
			RedisConstants.OBJECT_FORMULA, obj.getName());
	    }
	    // Else, we're updating.
	    return AlertBoxFactory.FAILED.generateUpdate(
		    RedisConstants.OBJECT_FORMULA, obj.getName());
	}

	// If company is null,
	// set it based on auth.
	if (obj.getCompany() == null) {
	    obj.setCompany(this.authHelper.getAuth().getCompany());
	}

	// Set the variable names in this formula.
	obj.setVariableNames(getAllVariableNames(obj));

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
    public Formula get(String key) {
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
    public String getReadyToSolveEquation(Formula formula) {

	// Get all ingredients.
	String[] formulaInputs = formula.getFormulaInputs();
	List<String> variableNames = formula.getVariableNames();
	String formulaStr = formula.getFormula();

	for (int i = 0; i < variableNames.size(); i++) {

	    // Get the input.
	    // And the variable to replace.
	    String input = formulaInputs[i];
	    String variableName = variableNames.get(i);

	    // Replace the variable with the actual input.
	    formulaStr = formulaStr.replaceAll(variableName, input);
	}

	// Clean the string.
	formulaStr = org.apache.commons.lang.StringUtils.remove(formulaStr,
		Formula.DELIMITER_OPEN_VARIABLE);
	formulaStr = org.apache.commons.lang.StringUtils.remove(formulaStr,
		Formula.DELIMITER_CLOSE_VARIABLE);
	return formulaStr;
    }

    @Override
    @Transactional
    public String test(Formula formula) {

	// Get the input.
	// Configure the format.
	// Create the query.
	String input = getReadyToSolveEquation(formula);
	WAQuery query = this.wolframAlphaEngine.createQuery();

	// Set properties of the query.
	query.setInput(input);
	query.addFormat("plaintext");
	String returnStr = "";

	try {
	    // This sends the URL to the Wolfram|Alpha server,
	    // gets the XML result and parses it into an object hierarchy
	    // held by the WAQueryResult object.
	    WAQueryResult queryResult = this.wolframAlphaEngine
		    .performQuery(query);

	    if (queryResult.isError()) {
		String error = "";
		error += "<b>Error Code:</b> " + queryResult.getErrorCode()
			+ "<br/>";
		error += "<b>Error Message:</b> "
			+ queryResult.getErrorMessage();
		returnStr = AlertBoxFactory.FAILED.setMessage(error)
			.generateHTML();
	    }
	    // If general error, unknown cause.
	    else if (!queryResult.isSuccess()) {
		returnStr = AlertBoxFactory.FAILED.generateCompute(
			RedisConstants.OBJECT_FORMULA, formula.getName());
	    }
	    // If the query was a success.
	    else {

		// Top header.
		String successStr = "";

		for (WAPod pod : queryResult.getPods()) {
		    if (!pod.isError()) {

			if (pod.getTitle().equals("Number line")) {
			    continue;
			}

			// Pod title.
			successStr += "<b>" + pod.getTitle() + "</b><br/>";

			// Subpods.
			// And results.
			for (WASubpod subpod : pod.getSubpods()) {
			    for (Object element : subpod.getContents()) {
				if (element instanceof WAPlainText) {
				    WAPlainText text = ((WAPlainText) element);
				    successStr += text.getText() + "<br/>";
				}
			    }
			}

			// End of pod.
			successStr += "<br/>";
		    }
		}

		// Success string.
		returnStr = AlertBoxFactory.SUCCESS.setMessage(successStr)
			.generateHTML();
	    }
	} catch (WAException e) {
	    e.printStackTrace();
	}

	return returnStr;
    }

    /**
     * Get all variable names of this formula.
     * 
     * @return
     */
    @Transactional
    @Override
    public List<String> getAllVariableNames(Formula formula) {

	// Get all indices of open and close variables.
	List<Integer> openIndices = StringUtils.getAllIndicesOfSubstring(
		formula.getFormula(), Formula.DELIMITER_OPEN_VARIABLE);
	List<Integer> closeIndices = StringUtils.getAllIndicesOfSubstring(
		formula.getFormula(), Formula.DELIMITER_CLOSE_VARIABLE);

	// Proceed only if legal.
	if (openIndices.size() == closeIndices.size()) {

	    List<String> variableNames = new ArrayList<String>();

	    for (int i = 0; i < openIndices.size(); i++) {

		// Get the variable name.
		int indexOpen = openIndices.get(i);
		int indexClose = closeIndices.get(i);
		String variableName = formula.getFormula().substring(indexOpen,
			indexClose);

		// Clean the variable name.
		variableName = org.apache.commons.lang.StringUtils.remove(
			variableName, Formula.DELIMITER_OPEN_VARIABLE);
		variableName = org.apache.commons.lang.StringUtils.remove(
			variableName, Formula.DELIMITER_CLOSE_VARIABLE);

		// Add the name to the output list.
		variableNames.add(variableName);
	    }
	    return variableNames;
	}
	return new ArrayList<String>();
    }

    @Transactional
    @Override
    public void delete(String key) {
	this.formulaValueRepo.delete(key);
    }
}
