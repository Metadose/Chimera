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
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Details</h3>
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
	 		                                    			
	 		                                    			<label>Estimate</label><br/>
	 		                                    			<c:forEach items="${estimateTypes}" var="type">
	 		                                    			<form:checkbox path="estimateType" class="form-control" value="${type.id()}" />&nbsp;<label>${type.label()}</label><br/>
	 		                                    			</c:forEach>
	 		                                    			<p class="help-block">Specify the type of estimation</p>
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
                   									<h3 class="box-title">Inputs</h3>
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
									                <td><label>Formula</label></td>
									                <td>${estimate.shape.formula}</td>
									                </tr>
									                </table>
			                                            
			                                        <br/>
			                                        <h4>Formula Variables</h4>
                   									<form:form modelAttribute="estimate"
														method="post"
														action="${contextPath}/project/compute/estimate">
				                                        <div class="form-group">
				                                        
				                                        <!-- Formula Inputs -->
			                                            <c:forEach items="${estimate.shape.variableNames}" var="variableName">
			                                            <label>${variableName}</label>
			                                            <form:input type="text" class="form-control" path="formulaInputs['${variableName}']"></form:input>
			                                            <form:select class="form-control" path="formulaInputsUnits['${variableName}']"> 
                                     						<c:forEach items="${commonUnitsList}" var="commonUnit"> 
                                     							<form:option value="${commonUnit}" label="${commonUnit.label()}"/> 
                                     						</c:forEach> 
 		                                    			</form:select>
			                                            </c:forEach>
			                                            
				                                        <p class="help-block">Input the value for each variable in the shape formula</p>
				                                        
				                                        <c:if test="${estimate.willComputeConcrete()}">
				                                        <label>
										                <c:url var="urlLink" value="/concreteproportion/list"/>
										                <a href="${urlLink}" class="general-link">
										                Concrete Proportion
										                </a>
										                </label>
										                
										                <c:choose>
														<c:when test="${empty estimate.lastComputed}">
	                                   						<c:forEach items="${concreteProportionList}" var="ratio"> 
	                                   							<form:checkbox path="concreteProportionKeys" value="${ratio.getKey()}"/>
	                                   							&nbsp;${ratio.getDisplayName()}
	                                   							<br/>
	                                   						</c:forEach> 
	 		                                    			<p class="help-block">Choose the ratio of cement, sand and gravel for the concrete</p>
														</c:when>
														
														<c:when test="${!empty estimate.lastComputed}">
															<label>${estimate.concreteProportion.getDisplayName()}</label>
														</c:when>
														</c:choose>
										                
				                                        </c:if>
				                                        
				                                        </div>
			                                            <button class="btn btn-cebedo-create btn-flat btn-sm">Compute</button>
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
              						<c:if test="${!empty estimate.lastComputed}">
              						<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Results</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="callout callout-info callout-cebedo">
									                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
									                </div>
									                
									                <h4>Cement</h4>
									                <table class="table table-bordered table-striped">
									                <tr>
									                <td><label>Component</label></td>
									                <td><label>Estimate</label></td>
									                <td><label>Unit of Measure</label></td>
									                </tr>
									                <tr>
										                <td><label>Cement (40kg)</label></td>
										                <td align="right">${estimate.resultEstimateConcrete.cement40kg}</td>
										                <td>${estimate.concreteProportion.unitCement40kg.name}</td>
									                </tr>
									                <tr>
										                <td><label>Cement (50kg)</label></td>
										                <td align="right">${estimate.resultEstimateConcrete.cement50kg}</td>
										                <td>${estimate.concreteProportion.unitCement50kg.name}</td>
									                </tr>
									                <tr>
										                <td><label>Sand</label></td>
										                <td align="right">${estimate.resultEstimateConcrete.sand}</td>
										                <td>${estimate.concreteProportion.unitSand.name}</td>
									                </tr>
									                <tr>
										                <td><label>Gravel</label></td>
										                <td align="right">${estimate.resultEstimateConcrete.gravel}</td>
										                <td>${estimate.concreteProportion.unitGravel.name}</td>
									                </tr>
									                </table>
									                
                   								</div>
                   							</div>
                   						</div>
              						</div>
              						</c:if>
                                </div><!-- /.tab-pane -->
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