package com.cebedo.pmsys.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cebedo.pmsys.bean.EstimateComputation;
import com.cebedo.pmsys.bean.EstimateComputationShape;
import com.cebedo.pmsys.enums.TableEstimationAllowance;
import com.cebedo.pmsys.service.EstimateService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:servlet-context.xml")
public class EstimateServiceImplTest {

    @Autowired
    private EstimateService estimateService;

    public void setEstimateService(EstimateService estimateService) {
	this.estimateService = estimateService;
    }

    @Test
    public void estimateConcreteTest() {
	EstimateServiceImpl impl = (EstimateServiceImpl) estimateService;
	EstimateComputation estimateComputationBean = new EstimateComputation();
	estimateComputationBean.setEstimationAllowance(TableEstimationAllowance.ALLOWANCE_0);

	EstimateComputationShape estimateComputationShape = new EstimateComputationShape();

	impl.estimateConcrete(estimateComputationBean, estimateComputationShape);
    }

}
