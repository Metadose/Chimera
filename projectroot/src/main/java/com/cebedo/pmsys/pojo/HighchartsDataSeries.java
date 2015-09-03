package com.cebedo.pmsys.pojo;

import java.util.List;

public class HighchartsDataSeries {

    private String name;
    private List<HighchartsDataPoint> data;

    public HighchartsDataSeries(String n, List<HighchartsDataPoint> d) {
	setName(n);
	setData(d);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<HighchartsDataPoint> getData() {
	return data;
    }

    public void setData(List<HighchartsDataPoint> data) {
	this.data = data;
    }

}
