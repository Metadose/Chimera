package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.DeliveryValueRepo;
import com.cebedo.pmsys.repository.MaterialValueRepo;
import com.cebedo.pmsys.repository.ProjectAuxValueRepo;
import com.cebedo.pmsys.repository.PullOutValueRepo;
import com.cebedo.pmsys.service.DeliveryService;
import com.cebedo.pmsys.service.MaterialService;
import com.cebedo.pmsys.service.PullOutService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DateUtils;
import com.cebedo.pmsys.validator.DeliveryValidator;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    private DeliveryValueRepo deliveryValueRepo;
    private ProjectAuxValueRepo projectAuxValueRepo;
    private MaterialValueRepo materialValueRepo;
    private MaterialService materialService;
    private PullOutValueRepo pullOutValueRepo;
    private PullOutService pullOutService;
    private ProjectDAO projectDAO;

    @Autowired
    @Qualifier(value = "pullOutService")
    public void setPullOutService(PullOutService pullOutService) {
	this.pullOutService = pullOutService;
    }

    @Autowired
    @Qualifier(value = "materialService")
    public void setMaterialService(MaterialService materialService) {
	this.materialService = materialService;
    }

    @Autowired
    @Qualifier(value = "projectDAO")
    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    public void setMaterialValueRepo(MaterialValueRepo materialValueRepo) {
	this.materialValueRepo = materialValueRepo;
    }

    public void setPullOutValueRepo(PullOutValueRepo pullOutValueRepo) {
	this.pullOutValueRepo = pullOutValueRepo;
    }

    public void setProjectAuxValueRepo(ProjectAuxValueRepo projectAuxValueRepo) {
	this.projectAuxValueRepo = projectAuxValueRepo;
    }

    public void setDeliveryValueRepo(DeliveryValueRepo deliveryValueRepo) {
	this.deliveryValueRepo = deliveryValueRepo;
    }

    @Autowired
    DeliveryValidator deliveryValidator;

    @Override
    @Transactional
    public HSSFWorkbook exportXLS(long projID) {

	Project proj = this.projectDAO.getByIDWithAllCollections(projID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new HSSFWorkbook();
	}
	this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_EXPORT, "Inventory", projID);

	HSSFWorkbook wb = new HSSFWorkbook();

	// Deliveries.
	constructSheetDelivery(wb, proj);

	// Materials.
	constructSheetMaterials(wb, proj);

	// Pull-outs.
	constructSheetPullOuts(wb, proj);

	return wb;
    }

    private void constructSheetPullOuts(HSSFWorkbook wb, Project proj) {

	List<PullOut> pullOuts = this.pullOutService.listDesc(proj);

	HSSFSheet sheet = wb.createSheet("Pull-Outs");
	int rowIndex = 0;
	HSSFRow row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Date and Time");
	row.createCell(1).setCellValue("Staff");
	row.createCell(2).setCellValue("Delivery");
	row.createCell(3).setCellValue("Material Category");
	row.createCell(4).setCellValue("Specific Name");
	row.createCell(5).setCellValue("Unit");
	row.createCell(6).setCellValue("Quantity");
	rowIndex++;

	for (PullOut pullOut : pullOuts) {
	    HSSFRow pullOutRow = sheet.createRow(rowIndex);

	    Material material = pullOut.getMaterial();
	    pullOutRow.createCell(0).setCellValue(
		    DateUtils.formatDate(pullOut.getDatetime(), DateUtils.PATTERN_DATE_TIME));
	    pullOutRow.createCell(1).setCellValue(pullOut.getStaff().getFullNameWithMiddleName());
	    pullOutRow.createCell(2).setCellValue(pullOut.getDelivery().getName());
	    pullOutRow.createCell(3).setCellValue(material.getMaterialCategory().getLabel());
	    pullOutRow.createCell(4).setCellValue(material.getName());
	    pullOutRow.createCell(5).setCellValue(material.getUnitName());
	    pullOutRow.createCell(6).setCellValue(pullOut.getQuantity());

	    rowIndex++;
	}
    }

    private void constructSheetMaterials(HSSFWorkbook wb, Project proj) {
	List<Material> materials = this.materialService.listDesc(proj);
	HSSFSheet materialsSheet = wb.createSheet("Materials");
	int materialsRowIndex = 0;
	HSSFRow row = materialsSheet.createRow(materialsRowIndex);
	row.createCell(0).setCellValue("Delivery");
	row.createCell(1).setCellValue("Material Category");
	row.createCell(2).setCellValue("Specific Name");
	row.createCell(3).setCellValue("Unit");
	row.createCell(4).setCellValue("Available");
	row.createCell(5).setCellValue("Used / Pulled-Out");
	row.createCell(6).setCellValue("Total Quantity");
	row.createCell(7).setCellValue("Cost (Per Unit)");
	row.createCell(8).setCellValue("Total Cost");
	materialsRowIndex++;

	for (Material material : materials) {
	    HSSFRow materialsRow = materialsSheet.createRow(materialsRowIndex);

	    materialsRow.createCell(0).setCellValue(material.getDelivery().getName());
	    materialsRow.createCell(1).setCellValue(material.getMaterialCategory().getLabel());
	    materialsRow.createCell(2).setCellValue(material.getName());
	    materialsRow.createCell(3).setCellValue(material.getUnitName());
	    materialsRow.createCell(4).setCellValue(material.getAvailable());
	    materialsRow.createCell(5).setCellValue(material.getUsed());
	    materialsRow.createCell(6).setCellValue(material.getQuantity());
	    materialsRow.createCell(7).setCellValue(material.getCostPerUnitMaterial());
	    materialsRow.createCell(8).setCellValue(material.getTotalCostPerUnitMaterial());

	    materialsRowIndex++;
	}
    }

    private void constructSheetDelivery(HSSFWorkbook wb, Project proj) {
	HSSFSheet deliverySheet = wb.createSheet("Deliveries");

	// For grand total.
	int deliveryRowIndex = 0;
	HSSFRow row = deliverySheet.createRow(deliveryRowIndex);
	row.createCell(0).setCellValue("Grand Total");
	ProjectAux aux = this.projectAuxValueRepo.get(ProjectAux.constructKey(proj));
	row.createCell(1).setCellValue(aux.getGrandTotalDelivery());
	deliveryRowIndex++;
	deliveryRowIndex++;

	// For headers.
	row = deliverySheet.createRow(deliveryRowIndex);
	deliveryRowIndex++;

	// Create a cell and put a value in it.
	row.createCell(0).setCellValue("Date and Time");
	row.createCell(1).setCellValue("Delivery");
	row.createCell(2).setCellValue("Description");
	row.createCell(3).setCellValue("Materials Cost");

	// Setup the table.
	// Delivery list data.
	List<Delivery> deliveries = listDesc(proj);
	for (Delivery delivery : deliveries) {
	    HSSFRow deliveryRow = deliverySheet.createRow(deliveryRowIndex);

	    deliveryRow.createCell(0).setCellValue(
		    DateUtils.formatDate(delivery.getDatetime(), DateUtils.PATTERN_DATE_TIME));
	    deliveryRow.createCell(1).setCellValue(delivery.getName());
	    deliveryRow.createCell(2).setCellValue(delivery.getDescription());
	    deliveryRow.createCell(3).setCellValue(delivery.getGrandTotalOfMaterials());

	    deliveryRowIndex++;
	}
    }

    @Override
    @Transactional
    public String set(Delivery obj, BindingResult result) {

	// If company is null.
	if (obj.getCompany() == null) {
	    obj.setCompany(this.authHelper.getAuth().getCompany());
	}

	else if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_DELIVERY, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// Service layer form validation.
	this.deliveryValidator.validate(obj, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// If we're creating.
	boolean isCreate = false;
	if (obj.getUuid() == null) {
	    isCreate = true;
	    obj.setUuid(UUID.randomUUID());
	}

	// Do the action.
	// Return success.
	this.deliveryValueRepo.set(obj);

	Project proj = obj.getProject();
	if (isCreate) {
	    this.messageHelper.auditableKey(AuditAction.ACTION_CREATE, Project.OBJECT_NAME, proj.getId(),
		    ConstantsRedis.OBJECT_DELIVERY, obj.getKey(), proj, obj.getName());
	    return AlertBoxGenerator.SUCCESS.generateCreate(ConstantsRedis.OBJECT_DELIVERY,
		    obj.getName());
	}
	this.messageHelper.auditableKey(AuditAction.ACTION_UPDATE, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_DELIVERY, obj.getKey(), proj, obj.getName());
	return AlertBoxGenerator.SUCCESS.generateUpdate(ConstantsRedis.OBJECT_DELIVERY, obj.getName());
    }

    @Override
    @Transactional
    public Delivery get(String key) {
	Delivery obj = this.deliveryValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_DELIVERY, obj.getKey());
	    return new Delivery();
	}

	// Log.
	this.messageHelper.nonAuditableKeyNoAssoc(AuditAction.ACTION_GET, ConstantsRedis.OBJECT_DELIVERY,
		obj.getKey());

	return obj;
    }

    @Override
    @Transactional
    public List<Delivery> listDesc(Project proj) {
	return listDesc(proj, null, null);
    }

    @Override
    @Transactional
    public String delete(String key) {

	Delivery delivery = this.deliveryValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(delivery)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_DELIVERY, delivery.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	Project proj = delivery.getProject();
	this.messageHelper.auditableKey(AuditAction.ACTION_DELETE, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_DELIVERY, delivery.getKey(), proj, delivery.getName());

	// Get the necessary objects.
	ProjectAux projAux = this.projectAuxValueRepo.get(ProjectAux.constructKey(proj));

	// Solve for the old grand total.
	double deliveryTotal = delivery.getGrandTotalOfMaterials();
	double grandTotal = projAux.getGrandTotalDelivery();
	double newTotal = grandTotal - deliveryTotal;

	// Revert the grand total.
	projAux.setGrandTotalDelivery(newTotal);
	this.projectAuxValueRepo.set(projAux);

	// If we're deleting this delivery,
	// delete also all materials inside,
	// and all pullouts involved.
	String materialsPattern = Material.constructPattern(delivery);
	String pulloutPattern = PullOut.constructPattern(delivery);

	// Get all keys.
	// Delete all keys.
	Set<String> materialKeys = this.materialValueRepo.keys(materialsPattern);
	Set<String> pulloutKeys = this.pullOutValueRepo.keys(pulloutPattern);
	this.materialValueRepo.delete(materialKeys);
	this.pullOutValueRepo.delete(pulloutKeys);

	// Delete this object.
	this.deliveryValueRepo.delete(key);

	return AlertBoxGenerator.SUCCESS.generateDelete(ConstantsRedis.OBJECT_DELIVERY,
		delivery.getName());
    }

    @Transactional
    @Override
    public List<Delivery> listAsc(Project proj) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<Delivery>();
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_LIST, Project.OBJECT_NAME,
		proj.getId(), ConstantsRedis.OBJECT_DELIVERY);

	String pattern = Delivery.constructPattern(proj);
	Set<String> keys = this.deliveryValueRepo.keys(pattern);

	List<Delivery> deliveries = this.deliveryValueRepo.multiGet(keys);

	// Sort the list in descending order.
	Collections.sort(deliveries, new Comparator<Delivery>() {
	    @Override
	    public int compare(Delivery aObj, Delivery bObj) {
		Date aStart = aObj.getDatetime();
		Date bStart = bObj.getDatetime();

		// To sort in ascending,
		return aStart.before(bStart) ? -1 : aStart.after(bStart) ? 1 : 0;
	    }
	});
	return deliveries;
    }

    @Override
    @Transactional
    public List<Delivery> listDesc(Project proj, Date startDate, Date endDate) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<Delivery>();
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_LIST, Project.OBJECT_NAME,
		proj.getId(), ConstantsRedis.OBJECT_DELIVERY);

	String pattern = Delivery.constructPattern(proj);
	Set<String> keys = this.deliveryValueRepo.keys(pattern);

	List<Delivery> deliveries = this.deliveryValueRepo.multiGet(keys);

	// If we are getting a specific range.
	boolean isRange = startDate != null && endDate != null;
	if (isRange) {
	    List<Delivery> toInclude = new ArrayList<Delivery>();
	    for (Delivery obj : deliveries) {
		Date objDate = obj.getDatetime();

		// If the date is equal to the start or end,
		// if date is between start and end.
		// Add to payrolls to include.
		if (objDate.equals(startDate) || objDate.equals(endDate)
			|| (objDate.after(startDate) && objDate.before(endDate))) {
		    toInclude.add(obj);
		}
	    }
	    deliveries = toInclude;
	}

	// Sort the list in descending order.
	Collections.sort(deliveries, new Comparator<Delivery>() {
	    @Override
	    public int compare(Delivery aObj, Delivery bObj) {
		Date aStart = aObj.getDatetime();
		Date bStart = bObj.getDatetime();

		// To sort in ascending,
		// remove Not's.
		return !(aStart.before(bStart)) ? -1 : !(aStart.after(bStart)) ? 1 : 0;
	    }
	});
	return deliveries;
    }

    @Override
    @Transactional
    public double analyzeTotal(List<Delivery> deliveries) {

	double total = 0;
	Project proj = null;
	for (Delivery obj : deliveries) {

	    // Security check.
	    if (!this.authHelper.isActionAuthorized(obj)) {
		this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_DELIVERY, obj.getKey());
		return 0.0;
	    }
	    proj = proj == null ? obj.getProject() : proj;
	    total += obj.getGrandTotalOfMaterials();
	}
	// Log.
	this.messageHelper.nonAuditableIDWithAssocWithKey(AuditAction.ACTION_GET, Project.OBJECT_NAME,
		proj.getId(), ConstantsRedis.OBJECT_DELIVERY, "Grand Total");
	return total;
    }

    @Override
    @Transactional
    public List<Delivery> analyzeMax(List<Delivery> objs) {
	double greatest = 0;
	Project proj = null;
	List<Delivery> max = new ArrayList<Delivery>();

	// Identify the greatest value.
	for (Delivery obj : objs) {
	    if (!this.authHelper.isActionAuthorized(obj)) {
		this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_DELIVERY, obj.getKey());
		return new ArrayList<Delivery>();
	    }
	    proj = proj == null ? obj.getProject() : proj;
	    double total = obj.getGrandTotalOfMaterials();
	    if (total > greatest) {
		greatest = total;
	    }
	}

	// Get all objects that are equal with the greatest value.
	for (Delivery obj : objs) {
	    double total = obj.getGrandTotalOfMaterials();
	    if (total == greatest) {
		max.add(obj);
	    }
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocWithKey(AuditAction.ACTION_ANALYZE,
		Project.OBJECT_NAME, proj.getId(), ConstantsRedis.OBJECT_DELIVERY, "Max");
	return max;
    }

    @Override
    @Transactional
    public List<Delivery> analyzeMin(List<Delivery> objs) {
	Double least = null;
	Project proj = null;
	List<Delivery> min = new ArrayList<Delivery>();

	// Identify the greatest value.
	for (Delivery obj : objs) {
	    if (!this.authHelper.isActionAuthorized(obj)) {
		this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_DELIVERY, obj.getKey());
		return new ArrayList<Delivery>();
	    }
	    proj = proj == null ? obj.getProject() : proj;
	    double total = obj.getGrandTotalOfMaterials();
	    if (least == null) {
		least = total;
		continue;
	    }
	    if (total < least) {
		least = total;
	    }
	}

	// Get all objects that are equal with the greatest value.
	for (Delivery obj : objs) {
	    double total = obj.getGrandTotalOfMaterials();
	    if (total == least) {
		min.add(obj);
	    }
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocWithKey(AuditAction.ACTION_ANALYZE,
		Project.OBJECT_NAME, proj.getId(), ConstantsRedis.OBJECT_DELIVERY, "Min");
	return min;
    }

    @Override
    @Transactional
    public double analyzeMean(Project proj, List<Delivery> objs) {
	double total = analyzeTotal(objs);
	this.messageHelper.nonAuditableIDWithAssocWithKey(AuditAction.ACTION_ANALYZE,
		Project.OBJECT_NAME, proj.getId(), ConstantsRedis.OBJECT_DELIVERY, "Mean");
	return total / objs.size();
    }

}
