package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.factory.AlertBoxFactory;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.ExcelHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.impl.DeliveryValueRepoImpl;
import com.cebedo.pmsys.repository.impl.MaterialValueRepoImpl;
import com.cebedo.pmsys.repository.impl.ProjectAuxValueRepoImpl;
import com.cebedo.pmsys.repository.impl.PullOutValueRepoImpl;
import com.cebedo.pmsys.service.MaterialService;
import com.cebedo.pmsys.service.ProjectAuxService;
import com.cebedo.pmsys.validator.MaterialValidator;

@Service
public class MaterialServiceImpl implements MaterialService {

    private static final int EXCEL_COLUMN_NAME = 1;
    private static final int EXCEL_COLUMN_QUANTITY = 2;
    private static final int EXCEL_COLUMN_UNIT = 3;
    private static final int EXCEL_COLUMN_COST_PER_UNIT = 4;
    private static final int EXCEL_COLUMN_REMARKS = 5;

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private ValidationHelper validationHelper = new ValidationHelper();
    private ExcelHelper excelHelper = new ExcelHelper();

    private MaterialValueRepoImpl materialValueRepo;
    private DeliveryValueRepoImpl deliveryValueRepo;
    private ProjectAuxService projectAuxService;
    private PullOutValueRepoImpl pullOutValueRepo;
    private ProjectAuxValueRepoImpl projectAuxValueRepo;

    @Autowired
    @Qualifier(value = "projectAuxValueRepo")
    public void setProjectAuxValueRepo(ProjectAuxValueRepoImpl projectAuxValueRepo) {
	this.projectAuxValueRepo = projectAuxValueRepo;
    }

    public void setPullOutValueRepo(PullOutValueRepoImpl pullOutValueRepo) {
	this.pullOutValueRepo = pullOutValueRepo;
    }

    public void setProjectAuxService(ProjectAuxService projectAuxService) {
	this.projectAuxService = projectAuxService;
    }

    public void setDeliveryValueRepo(DeliveryValueRepoImpl deliveryValueRepo) {
	this.deliveryValueRepo = deliveryValueRepo;
    }

    public void setMaterialValueRepo(MaterialValueRepoImpl materialValueRepo) {
	this.materialValueRepo = materialValueRepo;
    }

    @Autowired
    MaterialValidator materialValidator;

    @Override
    @Transactional
    public List<Material> convertExcelToMaterials(MultipartFile multipartFile, Project project) {

	// Security check.
	if (!this.authHelper.hasAccess(project)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
	    return null;
	}

	// Service layer form validation.
	boolean valid = this.validationHelper.fileIsNotNullOrEmpty(multipartFile);
	if (!valid) {
	    return null;
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_CONVERT_FILE,
		Project.OBJECT_NAME, project.getId(), MultipartFile.class.getName());

	try {

	    // Create Workbook instance holding reference to .xls file
	    // Get first/desired sheet from the workbook.
	    HSSFWorkbook workbook = new HSSFWorkbook(multipartFile.getInputStream());
	    HSSFSheet sheet = workbook.getSheetAt(0);

	    // Iterate through each rows one by one.
	    Iterator<Row> rowIterator = sheet.iterator();

	    // Construct estimate containers.
	    List<Material> materials = new ArrayList<Material>();
	    while (rowIterator.hasNext()) {

		Row row = rowIterator.next();
		int rowCountDisplay = row.getRowNum() + 1;

		// Skip first line.
		if (rowCountDisplay <= 1) {
		    continue;
		}

		// For each row, iterate through all the columns
		Iterator<Cell> cellIterator = row.cellIterator();

		// Every row, is a Staff object.
		Material material = new Material();

		while (cellIterator.hasNext()) {

		    // Cell in this row and column.
		    Cell cell = cellIterator.next();
		    int colCountDisplay = cell.getColumnIndex() + 1;

		    switch (colCountDisplay) {

		    case EXCEL_COLUMN_NAME:
			String name = (String) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? ""
					: this.excelHelper.getValueAsExpected(workbook, cell));
			material.setName(name);
			continue;

		    case EXCEL_COLUMN_QUANTITY:
			Double quantity = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			material.setQuantity(quantity);
			continue;

		    case EXCEL_COLUMN_UNIT:
			String unit = (String) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? ""
					: this.excelHelper.getValueAsExpected(workbook, cell));
			material.setUnitOfMeasure(unit);
			continue;

		    case EXCEL_COLUMN_COST_PER_UNIT:
			Double costPerUnit = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			material.setCostPerUnitMaterial(costPerUnit);
			continue;

		    case EXCEL_COLUMN_REMARKS:
			String remarks = (String) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? ""
					: this.excelHelper.getValueAsExpected(workbook, cell));
			material.setRemarks(remarks);
			continue;
		    }
		}
		materials.add(material);
	    }
	    return materials;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    @Override
    @Transactional
    public String create(Material obj, BindingResult result) {

	// If company is null.
	if (obj.getCompany() == null) {
	    obj.setCompany(this.authHelper.getAuth().getCompany());
	}

	else if (!this.authHelper.hasAccess(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_MATERIAL, obj.getKey());
	    return AlertBoxFactory.ERROR;
	}

	// Service layer form validation.
	this.materialValidator.validate(obj, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// If we're creating.
	if (obj.getUuid() == null) {

	    // Set the UUID.
	    obj.setUuid(UUID.randomUUID());

	    // Set available = quantity.
	    obj.setAvailable(obj.getQuantity());

	    // Set the total cost.
	    // total = quantity * cost per unit.
	    double totalCost = obj.getQuantity() * obj.getCostPerUnitMaterial();
	    obj.setTotalCostPerUnitMaterial(totalCost);

	    // Do actual service.
	    this.materialValueRepo.set(obj);

	    // Add the grand total to the delivery.
	    // Update the delivery object.
	    Delivery delivery = obj.getDelivery();
	    double newGrandTotal = delivery.getGrandTotalOfMaterials() + totalCost;
	    delivery.setGrandTotalOfMaterials(newGrandTotal);
	    this.deliveryValueRepo.set(delivery);

	    // Update the project auxillary.
	    // Add material total to project grand total.
	    ProjectAux projectAux = this.projectAuxService.get(delivery);
	    double projectTotalDelivery = projectAux.getGrandTotalDelivery() + totalCost;
	    projectAux.setGrandTotalDelivery(projectTotalDelivery);
	    this.projectAuxValueRepo.set(projectAux);

	    // Log.
	    Project proj = obj.getProject();
	    this.messageHelper.auditableKey(AuditAction.ACTION_CREATE, Project.OBJECT_NAME, proj.getId(),
		    ConstantsRedis.OBJECT_MATERIAL, obj.getKey(), proj, obj.getName());

	    // Return.
	    return AlertBoxFactory.SUCCESS.generateAdd(ConstantsRedis.OBJECT_MATERIAL, obj.getName());
	}

	// This service used only for adding.
	// Not updating.
	return AlertBoxFactory.ERROR;
    }

    @Override
    @Transactional
    public Material get(String key) {

	Material obj = this.materialValueRepo.get(key);

	// Security check.
	if (!this.authHelper.hasAccess(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_MATERIAL, obj.getKey());
	    return new Material();
	}

	// Log.
	this.messageHelper.nonAuditableKeyNoAssoc(AuditAction.ACTION_GET, ConstantsRedis.OBJECT_MATERIAL,
		obj.getKey());

	return obj;
    }

    @Override
    @Transactional
    public List<Material> listDesc(Delivery delivery) {
	// Security check.
	if (!this.authHelper.hasAccess(delivery)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_DELIVERY, delivery.getKey());
	    return new ArrayList<Material>();
	}

	// Log.
	this.messageHelper.nonAuditableListWithAssoc(AuditAction.ACTION_LIST,
		ConstantsRedis.OBJECT_DELIVERY, delivery.getKey(), ConstantsRedis.OBJECT_MATERIAL);

	String pattern = Material.constructPattern(delivery);
	Set<String> keys = this.materialValueRepo.keys(pattern);
	List<Material> materials = this.materialValueRepo.multiGet(keys);
	Collections.sort(materials, new Comparator<Material>() {
	    @Override
	    public int compare(Material aObj, Material bObj) {
		Date aStart = aObj.getDelivery().getDatetime();
		Date bStart = bObj.getDelivery().getDatetime();

		// To sort in descending.
		return !(aStart.before(bStart)) ? -1 : !(aStart.after(bStart)) ? 1 : 0;
	    }
	});
	return materials;
    }

    /**
     * Delete a material. Argument projectId for cache evict.
     */
    @Transactional
    @Override
    public String delete(String key, long projectId) {

	// Get the material.
	Material material = this.materialValueRepo.get(key);

	// Security check.
	if (!this.authHelper.hasAccess(material)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_MATERIAL, material.getKey());
	    return AlertBoxFactory.ERROR;
	}

	// Log.
	Project proj = material.getProject();
	this.messageHelper.auditableKey(AuditAction.ACTION_DELETE, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_MATERIAL, material.getKey(), proj, material.getName());

	// Get the updated version of the objects.
	Delivery delivery = this.deliveryValueRepo.get(material.getDelivery().getKey());
	ProjectAux projectAux = this.projectAuxService.get(material.getProject());

	// If the object will be deleted,
	// remove this material's total from delivery,
	// remove this material's total from project aux.
	double materialTotal = material.getTotalCostPerUnitMaterial();
	double deliveryTotal = delivery.getGrandTotalOfMaterials();
	double grandTotal = projectAux.getGrandTotalDelivery();

	// Set the values,
	// then set it to repo.
	delivery.setGrandTotalOfMaterials(deliveryTotal - materialTotal);
	projectAux.setGrandTotalDelivery(grandTotal - materialTotal);
	this.deliveryValueRepo.set(delivery);
	this.projectAuxValueRepo.set(projectAux);

	// Do the delete.
	this.materialValueRepo.delete(key);

	// Delete also all related pull-outs.
	String pattern = PullOut.constructPattern(material);
	Set<String> keys = this.pullOutValueRepo.keys(pattern);
	this.pullOutValueRepo.delete(keys);

	// Return success.
	return AlertBoxFactory.SUCCESS.generateDelete(ConstantsRedis.OBJECT_MATERIAL,
		material.getName());
    }

    @Override
    public List<Material> listDesc(Project proj) {
	return listDesc(proj, false);
    }

    /**
     * List all materials in project.
     */
    @Transactional
    @Override
    public List<Material> listDesc(Project proj, boolean override) {

	// Security check.
	if (!override && !this.authHelper.hasAccess(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<Material>();
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_LIST, Project.OBJECT_NAME,
		proj.getId(), ConstantsRedis.OBJECT_MATERIAL);

	String pattern = Material.constructPattern(proj);
	Set<String> keys = this.materialValueRepo.keys(pattern);
	List<Material> materials = this.materialValueRepo.multiGet(keys);

	Collections.sort(materials, new Comparator<Material>() {
	    @Override
	    public int compare(Material aObj, Material bObj) {
		Date aStart = aObj.getDelivery().getDatetime();
		Date bStart = bObj.getDelivery().getDatetime();

		// To sort in descending.
		return -1 * (aStart.compareTo(bStart));
	    }
	});

	return materials;
    }

    @Override
    @Transactional
    public String update(Material material, BindingResult result) {

	// Security check.
	if (!this.authHelper.hasAccess(material)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_MATERIAL, material.getKey());
	    return AlertBoxFactory.ERROR;
	}

	// Service layer form validation.
	this.materialValidator.validate(material, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// Log.
	Project proj = material.getProject();
	this.messageHelper.auditableKey(AuditAction.ACTION_UPDATE, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_MATERIAL, material.getKey(), proj, material.getName());

	// Set the material.
	this.materialValueRepo.set(material);
	return AlertBoxFactory.SUCCESS.generateUpdate(ConstantsRedis.OBJECT_MATERIAL,
		material.getName());
    }

}
