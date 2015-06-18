<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<c:set value="${false}" var="isUpdating"/>
	<c:choose>
   	<c:when test="${empty delivery.uuid}">
    	<title>Delivery Create</title>
   	</c:when>
   	<c:when test="${!empty delivery.uuid}">
		<title>Delivery Edit</title>
		<c:set value="${true}" var="isUpdating"/>
   	</c:when>
   	</c:choose>
   	<link href="<c:url value="/resources/lib/datetimepicker/jquery.datetimepicker.css" />"rel="stylesheet" type="text/css" />
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
		            	New Delivery
		                <small>Create Delivery</small>
	            	</c:when>
	            	<c:when test="${isUpdating}">
	            		${delivery.name}
		                <small>Edit Delivery</small>
	            	</c:when>
	            	</c:choose>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                    	<c:url var="urlBack" value="/project/edit/${delivery.project.id}" />
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
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Details</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="callout callout-info callout-cebedo">
									                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
									                </div>
                   									<form:form modelAttribute="delivery"
														id="detailsForm"
														method="post"
														action="${contextPath}/project/create/delivery">
				                                        <div class="form-group">
				                                            <label>Name</label>
				                                            <form:input type="text" class="form-control" path="name"/><br/>
				                                            <label>Date and Time</label>
				                                            <form:input type="text" class="form-control" id="date-picker" path="datetime"/><br/>
				                                            <label>Description</label>
				                                            <form:input type="text" class="form-control" path="description"/>
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
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Add Materials</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="callout callout-info callout-cebedo">
									                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
									                </div>
                   									<form:form modelAttribute="material"
														id="materialForm"
														method="post"
														action="${contextPath}/project/add/material">
				                                        <div class="form-group">
				                                            <label>Name</label>
				                                            <form:input type="text" class="form-control" path="name"/><br/>
				                                            <label>Quantity</label>
				                                            <form:input type="text" class="form-control" path="quantity"/><br/>
				                                            <label>Unit</label>
				                                            <form:input type="text" class="form-control" path="unit"/><br/>
				                                            <label>Cost (Per Unit)</label>
				                                            <form:input type="text" class="form-control" path="costPerUnitMaterial"/><br/>
				                                            <label>Remarks</label>
				                                            <form:input type="text" class="form-control" path="remarks"/>
				                                        </div>
				                                    </form:form>
                                            		<button onclick="submitForm('materialForm')" class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton">Add</button>
                   								</div>
                   							</div>
                   						</div>
                   						</c:if>
              						</div>
              						<c:if test="${isUpdating}">
              						<div class="row">
                   						<div class="col-xs-12">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">List of Materials</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="callout callout-info callout-cebedo">
									                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
									                </div>
									                <div class="pull-right">
									                <h3>Grand Total <b><u>
				                                	${delivery.getGrandTotalOfMaterialsAsString()}
													</u></b></h3>
									                </div>
                                            		<table id="material-table" class="table table-bordered table-striped">
				                                    	<thead>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                                <th>Name</th>
				                                                <th>Used</th>
				                                                <th>Available</th>
				                                            	<th>Quantity</th>
				                                                <th>Cost (Per Unit)</th>
				                                                <th>Total Cost</th>
				                                                <th>Unit</th>
				                                                <th>Remarks</th>
				                                            </tr>
		                                        		</thead>
				                                        <tbody>
					                                		<c:forEach items="${materialList}" var="row">
				                                            <tr>
				                                            	<td>
				                                            		<center>
				                                            			<c:url var="urlEdit" value="/project/edit/material/${row.getKey()}-end"/>
				                                            			<a href="${urlEdit}">
								                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
				                                            			</a>
									                                    <c:url var="urlDelete" value="/project/delete/material/${row.getKey()}-end"/>
									                                    <a href="${urlDelete}">
		                   													<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
									                                    </a>
																	</center>
																</td>
																<td>${row.name}</td>
																<td>${row.used}</td>
																<td>${row.available}</td>
																<td>${row.quantity}</td>
																<td align="right">${row.getCostPerUnitMaterialAsString()}</td>
																<td align="right">${row.getTotalCostPerUnitMaterialAsString()}</td>
																<td>${row.unit}</td>
																<td>${row.remarks}</td>
				                                            </tr>
			                                            	</c:forEach>
					                                    </tbody>
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
<script src="${contextPath}/resources/lib/datetimepicker/jquery.datetimepicker.js" type="text/javascript"></script>
<script>
$(function () {
	$('#date-picker').datetimepicker();
	$('#material-table').dataTable();
});
function submitForm(id) {
	$('#'+id).submit();
}
</script>
</html>