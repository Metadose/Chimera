package com.cebedo.pmsys.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cebedo.pmsys.enums.TableEstimationAllowance;

public class EstimationOutputBean implements Serializable {

    private static final long serialVersionUID = -1414234153517026207L;

    private TableEstimationAllowance estimationAllowance;
    private List<EstimationOutputRowBean> rowList = new ArrayList<EstimationOutputRowBean>();

    public TableEstimationAllowance getEstimationAllowance() {
	return estimationAllowance;
    }

    public void setEstimationAllowance(
	    TableEstimationAllowance estimationAllowance) {
	this.estimationAllowance = estimationAllowance;
    }

    public List<EstimationOutputRowBean> getRowList() {
	return rowList;
    }

    public void setRowList(List<EstimationOutputRowBean> rowList) {
	this.rowList = rowList;
    }

}
