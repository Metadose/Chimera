<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<c:set value="${false}" var="isUpdating"/>
	<c:choose>
   	<c:when test="${empty estimate.uuid}">
    	<title>Estimate Create</title>
   	</c:when>
   	<c:when test="${!empty estimate.uuid}">
		<title>Estimate Edit</title>
		<c:set value="${true}" var="isUpdating"/>
   	</c:when>
   	</c:choose>
	<style>
	  ul {         
	      padding:0 0 0 0;
	      margin:0 0 0 0;
	  }
	  ul li {     
	      list-style:none;
	      margin-bottom:25px;           
	  }
	  ul li img {
	      cursor: pointer;
	  }
	</style>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	            	<c:choose>
	            	<c:when test="${!isUpdating}">
		            	New Estimate
		                <small>Create Estimate</small>
	            	</c:when>
	            	<c:when test="${isUpdating}">
	            		${estimate.name}
		                <small>Edit Estimate</small>
	            	</c:when>
	            	</c:choose>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-md-12">
                    	<c:url var="urlBack" value="/project/edit/${estimate.project.id}" />
	                    <a href="${urlBack}">
							<button class="btn btn-cebedo-back btn-flat btn-sm">Back to Project</button>
						</a><br/><br/>
                    	${uiParamAlert}
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                                <c:if test="${!empty estimate.lastComputed}">
                                <li><a href="#tab_results" data-toggle="tab">Quantity Estimation
                                <span class="badge bg-blue">Results</span>
                                </a></li>
                                </c:if>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">
                   									<span class="badge bg-green">Step 1</span>
                   									Basic Details</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="callout callout-info callout-cebedo">
									                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
									                </div>
                   									<form:form modelAttribute="estimate"
														id="detailsForm"
														method="post"
														action="${contextPath}/project/create/estimate">
				                                        <div class="form-group">
				                                            
				                                            <label>Name</label>
				                                            <form:input type="text" placeholder="Sample: Fence, Wall 1, Wall 2" class="form-control" path="name"/>
				                                            <p class="help-block">Enter a name for this estimate</p>
				                                            
				                                            <label>Remarks</label>
				                                            <form:input type="text" placeholder="Sample: Estimating concrete hollowblocks needed for the fence" class="form-control" path="remarks"/>
				                                            <p class="help-block">Add extra information</p>
				                                            
				                                            <label>
											                <c:url var="urlLink" value="/shape/list"/>
											                <a href="${urlLink}" class="general-link">
											                Shape
											                </a>
											                </label>
				                                            <form:select class="form-control" path="shapeKey"> 
	                                     						<c:forEach items="${shapeList}" var="shape"> 
	                                     							<form:option value="${shape.getKey()}" label="${shape.name}"/> 
	                                     						</c:forEach> 
	 		                                    			</form:select>
	 		                                    			<p class="help-block">Specify the shape of the object to be estimated</p>
	 		                                    			
	 		                                    			<label>Estimate</label>
	 		                                    			<table class="table table-bordered table-striped">
	 		                                    			<!-- Headers -->
	 		                                    			<tr>
	 		                                    				<td><label>Check / Uncheck</label></td>
	 		                                    				<td><label>Type of Estimate</label></td>
	 		                                    			</tr>
	 		                                    			
	 		                                    			<!-- Checkboxes -->
	 		                                    			<c:forEach items="${estimateTypes}" var="type">
	 		                                    			<tr>
	 		                                    				<td align="center"><form:checkbox path="estimateType" class="form-control" value="${type.id()}" /></td>
	 		                                    				<td>${type.label()}</td>
	 		                                    			</tr>
	 		                                    			</c:forEach>
	 		                                    			</table>
	 		                                    			<p class="help-block">Specify the type of estimation</p>
	 		                                    			
	 		                                    			<!-- Allowance -->
	 		                                    			<label>
											                <c:url var="urlLink" value="/estimationallowance/list"/>
											                <a href="${urlLink}" class="general-link">
											                Estimation Allowance
											                </a>
											                </label>
				                                            <form:select class="form-control" path="estimationAllowanceKey"> 
	                                     						<c:forEach items="${allowanceList}" var="allowance"> 
	                                     							<form:option value="${allowance.getKey()}" label="${allowance.name} (${allowance.getEstimationAllowanceAsString()})"/> 
	                                     						</c:forEach> 
	 		                                    			</form:select>
	 		                                    			<p class="help-block">Specify the shape of the object to be estimated</p>
	 		                                    			
				                                        </div>
				                                    </form:form>
			                                        <c:if test="${isUpdating}">
                                            		<button onclick="submitForm('detailsForm')" class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton">Update</button>
			                                        </c:if>
			                                        <c:if test="${!isUpdating}">
                                            		<button onclick="submitForm('detailsForm')" class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton">Create</button>
			                                        </c:if>
                   								</div>
                   							</div>
                   						</div>
                   						<c:if test="${isUpdating}">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">
                   									<span class="badge bg-green">Step 2</span>
                   									Formula & Inputs</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="callout callout-info callout-cebedo">
									                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
									                </div>
									                
									                <table class="table table-bordered table-striped">
									                <tr>
									                <td><label>Shape</label></td>
									                <td>
									                <c:url var="urlLink" value="/shape/edit/${estimate.shape.getKey()}-end"/>
									                <a href="${urlLink}" class="general-link">
									                ${estimate.shape.name}
									                </a>
									                </td>
									                </tr>
									                <tr>
									                <td><label>Area Formula</label></td>
									                <td>${estimate.shape.areaFormula}</td>
									                </tr>
									                <tr>
									                <td><label>Volume Formula</label></td>
									                <td>${estimate.shape.volumeFormula}</td>
									                </tr>
									                </table>
									                <p class="help-block">Area and Volume formula of the shape</p>
                   									<form:form modelAttribute="estimate"
														method="post"
														action="${contextPath}/project/compute/estimate">
				                                        <div class="form-group">
				                                        
				                                        <c:if test="${estimate.willComputeMasonry()}">
				                                        <br/>
				                                        <!-- Area Formula Inputs -->
				                                        <h4>Area Formula Inputs</h4>
			                                            <c:forEach items="${estimate.shape.areaVariableNames}" var="variableName">
			                                            <label>${variableName}</label>
			                                            <form:input type="text" class="form-control" path="areaFormulaInputs['${variableName}']"></form:input>
			                                            <form:select class="form-control" path="areaFormulaInputsUnits['${variableName}']"> 
                                     						<c:forEach items="${commonUnitsList}" var="commonUnit"> 
                                     							<form:option value="${commonUnit}" label="${commonUnit.label()}"/> 
                                     						</c:forEach> 
 		                                    			</form:select>
			                                            </c:forEach>
				                                        <p class="help-block">Input the value for each variable in the area formula</p>
				                                        </c:if>
				                                        
				                                        <c:if test="${estimate.willComputeConcrete()}">
				                                        <br/>
				                                        <!-- Volume Formula Inputs -->
				                                        <h4>Volume Formula Inputs</h4>
			                                            <c:forEach items="${estimate.shape.volumeVariableNames}" var="variableName">
			                                            <label>${variableName}</label>
			                                            <form:input type="text" class="form-control" path="volumeFormulaInputs['${variableName}']"></form:input>
			                                            <form:select class="form-control" path="volumeFormulaInputsUnits['${variableName}']"> 
                                     						<c:forEach items="${commonUnitsList}" var="commonUnit"> 
                                     							<form:option value="${commonUnit}" label="${commonUnit.label()}"/> 
                                     						</c:forEach> 
 		                                    			</form:select>
			                                            </c:forEach>
				                                        <p class="help-block">Input the value for each variable in the volume formula</p>
				                                        
				                                        
				                                        <!-- Label & hyperlink -->
				                                        <br/>
				                                        <h4>Concrete Estimation Inputs</h4>
				                                        <label>
										                <c:url var="urlLink" value="/concreteproportion/list"/>
										                <a href="${urlLink}" class="general-link">
										                Concrete Proportion
										                </a>
										                </label>
										                
				                                        <table class="table table-bordered table-striped">
				                                        <tr>
				                                        	<td><label>Check / Uncheck</label></td>
				                                        	<td><label>Concrete Proportion / Class</label></td>
				                                        </tr>
				                                        
	<!-- Map<ConcreteProportion, ConcreteEstimateResults> resultMapConcrete = new HashMap<ConcreteProportion, ConcreteEstimateResults>();
    Map<CHB, MasonryEstimateResults> resultMapMasonry = new HashMap<CHB, MasonryEstimateResults>(); -->
				                                        
                                   						<c:forEach items="${concreteProportionList}" var="ratio"> 
				                                        <tr>
				                                        <td align="center"><form:checkbox path="concreteProportionKeys" value="${ratio.getKey()}"/></td>
				                                        <td>
				                                        <c:url var="urlLink" value="/concreteproportion/edit/${ratio.getKey()}-end"/>
										                <a href="${urlLink}" class="general-link">
				                                        ${ratio.getDisplayName()}
										                </a>
				                                        </td>
				                                        </tr>
                                   						</c:forEach> 
				                                        </table>
 		                                    			<p class="help-block">Choose the ratio of cement, sand and gravel for the concrete</p>
														
				                                        </c:if> <!-- End of "if will compute Concrete" -->
				                                        
				                                        <c:if test="${estimate.willComputeMasonry()}">
				                                        <br/>
				                                        <h4>Masonry Estimation Inputs</h4>
				                                        <label>
										                <c:url var="urlLink" value="/chb/list"/>
										                <a href="${urlLink}" class="general-link">
										                CHB Measurements
										                </a>
										                </label>
										                
										                <table class="table table-bordered table-striped">
										                	<tr>
										                	<td><label>Check / Uncheck</label></td>
										                	<td><label>Proportion Name</label></td>
										                	</tr>
										                	
										                	<c:forEach items="${chbList}" var="chb">
										                	<tr>
										                	<td align="center"><form:checkbox path="chbMeasurementKeys" class="form-control" value="${chb.getKey()}"/></td>
										                	<td>
										                	<c:url var="urlLink" value="/chb/edit/${chb.getKey()}-end"/>
											                <a href="${urlLink}" class="general-link">
										                	${chb.name}
											                </a>
										                	</td>
										                	</tr>
										                	</c:forEach>
										                </table>
 		                                    			<p class="help-block">Specify the shape of the object to be estimated</p>
				                                        </c:if> <!-- End of "if will compute Masonry" -->
				                                        
				                                        </div>
			                                            <button class="btn btn-cebedo-create btn-flat btn-sm">Create Estimate</button>
				                                    </form:form>
				                                    <br/>
				                                    <c:choose>
													<c:when test="${empty estimate.lastComputed}">
														<div class="callout callout-info">
										                    <p>Not yet computed.</p>
										                </div>
													</c:when>
													
													<c:when test="${!empty estimate.lastComputed}">
														<fmt:formatDate pattern="yyyy/MM/dd hh:mm:ss a" value="${estimate.lastComputed}" var="lastComputed"/>
														<div class="callout callout-info">
										                    <p>Last Computed: ${lastComputed}</p>
										                </div>
													</c:when>
													</c:choose>
													
                   								</div>
                   							</div>
                   						</div>
                   						</c:if>
              						</div>
                                </div><!-- /.tab-pane -->
                   				<c:if test="${!empty estimate.lastComputed}">
                                <div class="tab-pane" id="tab_results">
                                	<div class="row">
									    <c:if test="${!empty estimate.resultMapConcrete}">
                   						<div class="col-md-6">
               								<div class="box box-body box-default">
               								<div class="box-header">
               									<h3 class="box-title">Concrete</h3>
               								</div>
               								<div class="box-body">
               									<div class="callout callout-info callout-cebedo">
						                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
							                </div>
							                
							                <!-- Result of concrete estimation -->
							                <table class="table table-bordered table-striped">
							                <tr>
							                <td><label>Concrete Proportion</label></td>
							                <td><label>Component</label></td>
							                <td><label>Quantity Estimate</label></td>
							                <td><label>Unit of Measure</label></td>
							                </tr>
							                
<!-- Map<ConcreteProportion, ConcreteEstimateResults> resultMapConcrete = new HashMap<ConcreteProportion, ConcreteEstimateResults>(); -->
  
							                
							                <c:forEach items="${estimate.resultMapConcrete}" var="resultEntry">
							                <c:set value="${false}" var="renderedProportion"></c:set>
							                
							                <c:set var="entryConcreteProportion" value="${resultEntry.key}"></c:set>
							                <c:set var="entryResult" value="${resultEntry.value}"></c:set>
							                <tr>
							                
							                	<c:if test="${!renderedProportion}">
							                	<td rowspan="4"><label>
							                	<c:url var="urlLink" value="/concreteproportion/edit/${entryConcreteProportion.getKey()}-end"/>
								                <a href="${urlLink}" class="general-link">
							                	${entryConcreteProportion.getDisplayName()}
								                </a>
							                	</label></td>
							                	<c:set value="${true}" var="renderedProportion"></c:set>
							                	</c:if>
							                	
								                <td><label>Cement (40kg)</label></td>
								                <td align="right">${entryResult.getCement40kgAsString()}</td>
								                <td>${entryConcreteProportion.unitCement40kg.name}</td>
							                </tr>
							                <tr>
								                <td><label>Cement (50kg)</label></td>
								                <td align="right">${entryResult.getCement50kgAsString()}</td>
								                <td>${entryConcreteProportion.unitCement50kg.name}</td>
							                </tr>
							                <tr>
								                <td><label>Sand</label></td>
								                <td align="right">${entryResult.getSandAsString()}</td>
								                <td>${entryConcreteProportion.unitSand.name}</td>
							                </tr>
							                <tr>
								                <td><label>Gravel</label></td>
								                <td align="right">${entryResult.getGravelAsString()}</td>
								                <td>${entryConcreteProportion.unitGravel.name}</td>
							                </tr>
							                </c:forEach> <!-- End of "Result" entries loop -->
							                
							                </table>
                 								</div>
                 								</div>
									    </div>
									    </c:if>
									    
									    <c:if test="${!empty estimate.resultMapMasonry}">
									    <div class="col-md-6">
								    		<div class="box box-body box-default">
               								<div class="box-header">
               									<h3 class="box-title">Masonry</h3>
               								</div>
               								<div class="box-body">
               									<div class="callout callout-info callout-cebedo">
						                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
							                </div>
							                
							                <!-- Result of masonry estimation -->
							                <!-- Map<CHB, MasonryEstimateResults> resultMapMasonry = new HashMap<CHB, MasonryEstimateResults>(); -->
							                <table class="table table-bordered table-striped">
							                <tr>
							                <td><label>CHB Measurement</label></td>
							                <td><label>Pieces per Sq. Meter</label></td>
							                <td><label>Estimated No. of Pieces</label></td>
							                </tr>
							                
							                <c:forEach items="${estimate.resultMapMasonry}" var="resultEntry">
							                <c:set value="${resultEntry.key}" var="entryCHB"></c:set>
							                <c:set value="${resultEntry.value}" var="entryResult"></c:set>
							                <tr>
								                <td><label>
								                <c:url var="urlLink" value="/chb/edit/${entryCHB.getKey()}-end"/>
								                <a href="${urlLink}" class="general-link">
								                ${entryCHB.name}
								                </a>
								                </label></td>
								                <td align="right"><label>${entryCHB.getPerSqM()}</label></td>
								                <td align="right">${entryResult.getTotalCHBAsString()}</td>
							                </tr>
							                </c:forEach>
							                </table>
                 								</div>
                 								</div>
									    </div>
									    </c:if>
									</div>
								</div>
								</c:if>
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
</body>
<script>
function submitForm(id) {
	$('#'+id).submit();
}
</script>
</html>