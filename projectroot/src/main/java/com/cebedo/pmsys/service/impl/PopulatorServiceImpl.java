package com.cebedo.pmsys.service.impl;

import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cebedo.pmsys.domain.BlockLayingMixture;
import com.cebedo.pmsys.domain.EstimationAllowance;
import com.cebedo.pmsys.domain.Unit;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.BlockLayingMixtureService;
import com.cebedo.pmsys.service.EstimationAllowanceService;
import com.cebedo.pmsys.service.PopulatorService;
import com.cebedo.pmsys.service.UnitService;

@Service
public class PopulatorServiceImpl implements PopulatorService {

    private AuthHelper authHelper = new AuthHelper();
    private EstimationAllowanceService estimationAllowanceService;
    private UnitService unitService;
    private BlockLayingMixtureService blockLayingMixtureService;

    public void setBlockLayingMixtureService(
	    BlockLayingMixtureService blockLayingMixtureService) {
	this.blockLayingMixtureService = blockLayingMixtureService;
    }

    public void setUnitService(UnitService unitService) {
	this.unitService = unitService;
    }

    public void setEstimationAllowanceService(
	    EstimationAllowanceService estimationAllowanceService) {
	this.estimationAllowanceService = estimationAllowanceService;
    }

    @Transactional
    @Override
    public void populateEstimationAllowance() {
	// Delete all.
	Company myCom = this.authHelper.getAuth().getCompany();
	String pattern = EstimationAllowance.constructPattern(myCom);
	this.estimationAllowanceService.delete(this.estimationAllowanceService
		.keys(pattern));

	// Construct.
	EstimationAllowance allowance = new EstimationAllowance();
	allowance.setCompany(myCom);

	// Populate.
	allowance.setUuid(UUID.randomUUID());
	allowance.setDescription("8 percent estimation allowace.");
	allowance.setEstimationAllowance(0.08);
	allowance.setName("8 Percent");
	this.estimationAllowanceService.set(allowance);

	// Populate.
	allowance.setUuid(UUID.randomUUID());
	allowance.setDescription("9 percent estimation allowace.");
	allowance.setEstimationAllowance(0.09);
	allowance.setName("9 Percent");
	this.estimationAllowanceService.set(allowance);

	// Populate.
	allowance.setUuid(UUID.randomUUID());
	allowance.setDescription("10 percent estimation allowace.");
	allowance.setEstimationAllowance(0.1);
	allowance.setName("10 Percent");
	this.estimationAllowanceService.set(allowance);

	// Populate.
	allowance.setUuid(UUID.randomUUID());
	allowance.setDescription("11 percent estimation allowace.");
	allowance.setEstimationAllowance(0.11);
	allowance.setName("11 Percent");
	this.estimationAllowanceService.set(allowance);

	// Populate.
	allowance.setUuid(UUID.randomUUID());
	allowance.setDescription("12 percent estimation allowace.");
	allowance.setEstimationAllowance(0.12);
	allowance.setName("12 Percent");
	this.estimationAllowanceService.set(allowance);

	// Populate.
	allowance.setUuid(UUID.randomUUID());
	allowance.setDescription("13 percent estimation allowace.");
	allowance.setEstimationAllowance(0.13);
	allowance.setName("13 Percent");
	this.estimationAllowanceService.set(allowance);

	// Populate.
	allowance.setUuid(UUID.randomUUID());
	allowance.setDescription("14 percent estimation allowace.");
	allowance.setEstimationAllowance(0.14);
	allowance.setName("14 Percent");
	this.estimationAllowanceService.set(allowance);

	// Populate.
	allowance.setUuid(UUID.randomUUID());
	allowance.setDescription("15 percent estimation allowace.");
	allowance.setEstimationAllowance(0.15);
	allowance.setName("15 Percent");
	this.estimationAllowanceService.set(allowance);
    }

    @Transactional
    @Override
    public void populateUnitsOfMeasure() {
	// Delete all.
	Company myCom = this.authHelper.getAuth().getCompany();
	String pattern = Unit.constructPattern(myCom);
	Set<String> keys = this.unitService.keys(pattern);
	this.unitService.delete(keys);

	// Construct.
	Unit unit = new Unit(myCom);

	// Populate.
	unit.setName("40kg Bags");
	unit.setDetails("40-kilo bag typically used in measuring cement.");
	unit.setUuid(UUID.randomUUID());
	this.unitService.set(unit);

	// Populate.
	unit.setName("50kg Bags");
	unit.setDetails("50-kilo bag typically used in measuring cement.");
	unit.setUuid(UUID.randomUUID());
	this.unitService.set(unit);

	// Populate.
	unit.setName("Cubic Meters");
	unit.setDetails("Common unit of measure.");
	unit.setUuid(UUID.randomUUID());
	this.unitService.set(unit);
    }

    @Transactional
    @Override
    public void populateBlockLayingMixture() {
	// Delete all.
	Company myCom = this.authHelper.getAuth().getCompany();
	String pattern = BlockLayingMixture.constructPattern(myCom);
	Set<String> keys = this.blockLayingMixtureService.keys(pattern);
	this.blockLayingMixtureService.delete(keys);

	// Construct.
	BlockLayingMixture mix = new BlockLayingMixture(myCom);

	// Class A -----------------------------------------------------------
	// Populate.
	mix.setCementBags(0.792);
	mix.setSand(0.0435);
	mix.setName("Class A of CHB (10 x 20 x 40)");
	mix.setDescription("Class A mixture when using CHB (10 x 20 x 40).");
	mix.setUuid(UUID.randomUUID());
	this.blockLayingMixtureService.set(mix);

	// Populate.
	mix.setCementBags(1.526);
	mix.setSand(0.0844);
	mix.setName("Class A of CHB (15 x 20 x 40)");
	mix.setDescription("Class A mixture when using CHB (15 x 20 x 40).");
	mix.setUuid(UUID.randomUUID());
	this.blockLayingMixtureService.set(mix);

	// Populate.
	mix.setCementBags(2.260);
	mix.setSand(0.1250);
	mix.setName("Class A of CHB (20 x 20 x 40)");
	mix.setDescription("Class A mixture when using CHB (20 x 20 x 40).");
	mix.setUuid(UUID.randomUUID());
	this.blockLayingMixtureService.set(mix);

	// Class B -----------------------------------------------------------
	// Populate.
	mix.setCementBags(0.522);
	mix.setSand(0.0435);
	mix.setName("Class B of CHB (10 x 20 x 40)");
	mix.setDescription("Class B mixture when using CHB (10 x 20 x 40).");
	mix.setUuid(UUID.randomUUID());
	this.blockLayingMixtureService.set(mix);

	// Populate.
	mix.setCementBags(1.018);
	mix.setSand(0.0844);
	mix.setName("Class B of CHB (15 x 20 x 40)");
	mix.setDescription("Class B mixture when using CHB (15 x 20 x 40).");
	mix.setUuid(UUID.randomUUID());
	this.blockLayingMixtureService.set(mix);

	// Populate.
	mix.setCementBags(1.500);
	mix.setSand(0.1250);
	mix.setName("Class B of CHB (20 x 20 x 40)");
	mix.setDescription("Class B mixture when using CHB (20 x 20 x 40).");
	mix.setUuid(UUID.randomUUID());
	this.blockLayingMixtureService.set(mix);

	// Class C -----------------------------------------------------------
	// Populate.
	mix.setCementBags(0.394);
	mix.setSand(0.0435);
	mix.setName("Class C of CHB (10 x 20 x 40)");
	mix.setDescription("Class C mixture when using CHB (10 x 20 x 40).");
	mix.setUuid(UUID.randomUUID());
	this.blockLayingMixtureService.set(mix);

	// Populate.
	mix.setCementBags(0.763);
	mix.setSand(0.0844);
	mix.setName("Class C of CHB (15 x 20 x 40)");
	mix.setDescription("Class C mixture when using CHB (15 x 20 x 40).");
	mix.setUuid(UUID.randomUUID());
	this.blockLayingMixtureService.set(mix);

	// Populate.
	mix.setCementBags(1.125);
	mix.setSand(0.1250);
	mix.setName("Class C of CHB (20 x 20 x 40)");
	mix.setDescription("Class C mixture when using CHB (20 x 20 x 40).");
	mix.setUuid(UUID.randomUUID());
	this.blockLayingMixtureService.set(mix);

	// Class D -----------------------------------------------------------
	// Populate.
	mix.setCementBags(0.328);
	mix.setSand(0.0435);
	mix.setName("Class D of CHB (10 x 20 x 40)");
	mix.setDescription("Class D mixture when using CHB (10 x 20 x 40).");
	mix.setUuid(UUID.randomUUID());
	this.blockLayingMixtureService.set(mix);

	// Populate.
	mix.setCementBags(0.633);
	mix.setSand(0.0844);
	mix.setName("Class D of CHB (15 x 20 x 40)");
	mix.setDescription("Class D mixture when using CHB (15 x 20 x 40).");
	mix.setUuid(UUID.randomUUID());
	this.blockLayingMixtureService.set(mix);

	// Populate.
	mix.setCementBags(0.938);
	mix.setSand(0.1250);
	mix.setName("Class D of CHB (20 x 20 x 40)");
	mix.setDescription("Class D mixture when using CHB (20 x 20 x 40).");
	mix.setUuid(UUID.randomUUID());
	this.blockLayingMixtureService.set(mix);
    }

    @Transactional
    @Override
    public void populateCHB() {
	// TODO Auto-generated method stub

    }

    @Transactional
    @Override
    public void populateCHBFootingDimension() {
	// TODO Auto-generated method stub

    }

    @Transactional
    @Override
    public void populateCHBFootingMixture() {
	// TODO Auto-generated method stub

    }

    @Transactional
    @Override
    public void populateConcreteProportion() {
	// TODO Auto-generated method stub

    }

    @Transactional
    @Override
    public void populateMaterialCategory() {
	// TODO Auto-generated method stub

    }

    @Transactional
    @Override
    public void populatePlasterMixture() {
	// TODO Auto-generated method stub

    }

    @Transactional
    @Override
    public void populateShape() {
	// TODO Auto-generated method stub

    }

    @Transactional
    @Override
    public void populateAll() {
	// TODO Auto-generated method stub

    }

}
