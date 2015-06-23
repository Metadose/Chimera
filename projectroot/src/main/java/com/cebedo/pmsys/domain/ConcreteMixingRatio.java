package com.cebedo.pmsys.domain;

import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.model.Company;

/**
 * C = number of bags of cement per cubic meter of concrete work (bag/m3)<br>
 * S = volume of sand per cubic meter of concrete work (m3 of sand /m3)<br>
 * G = volume of gravel per cubic meter of concrete work (m3 of gravel /m3)<br>
 * c,s,g = cement-sand-gravel ratio<br>
 * (relative amounts of solids by volume in a mixture)
 * 
 * 
 * Cement C = 55 / (c+s+g)<br>
 * Sand S   = 0.028*C*s<br>
 * Gravel G = 0.028*C*g
 * 
 * 
 * The classes of concrete mixture depends on the cement-sand-gravel ratio
 * (c:s:g).
 * 
 * 
 * Class A (1:2:4) = for beams, slabs, columns, all members subjected to bending<br>
 * Class B (1:2.5:5) = member not reinforced for bending stress<br>
 * Class C (1:3:6) = for footing (not under water)
 * 
 */
public class ConcreteMixingRatio implements IDomainObject {

    private static final long serialVersionUID = -2080215320887737760L;

    /**
     * Keys.
     */
    private Company company;
    private UUID uuid;

    /**
     * Specs.
     */
    private String name;
    private String description;

    private double ratioCement;
    private double ratioGravel;
    private double ratioSand;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    public UUID getUuid() {
	return uuid;
    }

    public void setUuid(UUID uuid) {
	this.uuid = uuid;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public double getRatioCement() {
	return ratioCement;
    }

    public void setRatioCement(double ratioCement) {
	this.ratioCement = ratioCement;
    }

    public double getRatioGravel() {
	return ratioGravel;
    }

    public void setRatioGravel(double ratioGravel) {
	this.ratioGravel = ratioGravel;
    }

    public double getRatioSand() {
	return ratioSand;
    }

    public void setRatioSand(double ratioSand) {
	this.ratioSand = ratioSand;
    }

    @Override
    public String getKey() {
	// TODO Auto-generated method stub
	return null;
    }

}
