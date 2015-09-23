package com.cebedo.pmsys.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.model.Project;

public interface EstimateCostService {

    public HSSFWorkbook exportXLS(long projID);

    public String createMassCosts(List<EstimateCost> costs, BindingResult result);

    public List<EstimateCost> convertExcelToCostList(MultipartFile multipartFile, Project project);

    public String delete(String key);

    public EstimateCost get(String uuid);

    public List<EstimateCost> list(Project proj);

    public String set(EstimateCost cost, BindingResult result);

}