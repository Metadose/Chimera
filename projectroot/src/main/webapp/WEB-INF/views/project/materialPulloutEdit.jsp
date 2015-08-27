<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<c:set value="${true}" var="isUpdating" />
	<c:if test="${empty pullout.uuid}">
		<c:set value="${false}" var="isUpdating" />
	</c:if>
	
	<c:choose>
	<c:when test="${isUpdating}">
	<title>${pullout.material.name} | Edit Pull-Out</title>
	</c:when>
	<c:when test="${!isUpdating}">
	<title>Create New Pull-Out</title>
	</c:when>
	</c:choose>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
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
	            <c:choose>
				<c:when test="${isUpdating}">
	            <h1>
	            	${pullout.material.name}
		            <small>Edit Pull-Out</small>
	            </h1>
				</c:when>

				<c:when test="${!isUpdating}">
				<h1>
	            	${pullout.material.name}
		            <small>Create Pull-Out</small>
	            </h1>
				</c:when>
				</c:choose>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-md-12">
                    	<c:url var="urlBack" value="/project/edit/${pullout.project.id}" />
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
                   								<div class="box-body">
									                <c:if test="${!empty staffList}">
									                <table class="table table-bordered table-striped">
									                <tr>
									                	<td><label>Delivery</label></td>
									                	<td>
									                	<c:url var="urlLink" value="/project/edit/delivery/${pullout.delivery.getKey()}-end"/>
									                	<a class="general-link" href="${urlLink}">
									                	${pullout.delivery.name}
									                	</a>
									                	</td>
									                </tr>
									                <tr>
									                	<td><label>Material Category</label></td>
									                	<td>${pullout.material.materialCategory.getLabel()}</td>
									                </tr>
									                <tr>
									                	<td><label>Specific Name</label></td>
									                	<td>
									                		<c:url var="urlLink" value="/project/edit/material/${pullout.material.getKey()}-end"/>
						                                    <a href="${urlLink}" class="general-link">
									                		${pullout.material.name}
						                                    </a>
									                	</td>
									                </tr>
									                <tr>
									                	<td><label>Units</label></td>
									                	<td>${pullout.material.getUnitName()}</td>
									                </tr>
									                <tr>
									                	<td><label>Available</label></td>
									                	<td align="right">${pullout.material.available}
									                	</td>
									                </tr>
									                <tr>
									                	<td><label>Used</label></td>
									                	<td align="right">${pullout.material.used}</td>
									                </tr>
									                <tr>
									                	<td><label>Total Quantity</label></td>
									                	<td align="right">${pullout.material.quantity}</td>
									                </tr>
									                </table>
									                <br/>
									                <div class="progress">
														<div class="progress-bar progress-bar-${pullout.material.getAvailableCSS()} progress-bar-striped" 
														    role="progressbar" 
														    aria-valuenow="${pullout.material.available}" 
														    aria-valuemin="0" 
														    aria-valuemax="${pullout.material.quantity}"
														    style="width:${pullout.material.getAvailableAsPercentage()}">
														    <c:if test="${pullout.material.available <= 0}">
														    	Out of Stock
														    </c:if>
														    <c:if test="${pullout.material.available > 0}">
														    	${pullout.material.available} out of ${pullout.material.quantity} (${pullout.material.getAvailableAsPercentageForDisplay()})
														    </c:if>
													    </div>
													</div>
									                
													<c:set value="${contextPath}/project/do-pullout/material" var="formURL" />
													<c:if test="${isUpdating}">
														<c:set value="${contextPath}/project/update/pullout" var="formURL" />
													</c:if>
													
                   									<form:form modelAttribute="pullout"
														id="pulloutForm"
														method="post"
														action="${formURL}">
				                                        <div class="form-group">
				                                            <label>Quantity</label>
				                                            <form:input type="text" class="form-control" path="quantity"/>
				                                            <p class="help-block">Enter the number of units that was pulled-out</p>
				                                            <label>Staff</label>
				                                            <form:select class="form-control" path="staffID"> 
	                                     						<c:forEach items="${staffList}" var="staff"> 
	                                     							<form:option value="${staff.id}" label="${staff.getFullName()}"/> 
	                                     						</c:forEach> 
	 		                                    			</form:select>
	 		                                    			<p class="help-block">Choose the staff who pulled-out the material</p>

	 		                                    			<fmt:formatDate pattern="yyyy/MM/dd hh:mm a" value="${pullout.datetime}" var="pulloutDateTime"/>
				                                            <label>Date and Time</label>
				                                            <form:input type="text" placeholder="Sample: 2015/06/24 08:15" class="form-control" id="date-picker" path="datetime" value="${pulloutDateTime}"/>
				                                            <p class="help-block">Choose the date and time of the pull-out</p>
				                                            <label>Remarks</label>
				                                            <form:input type="text" placeholder="Sample: The pull-out was delayed due to rain." class="form-control" path="remarks"/>
				                                            <p class="help-block">Add additional remarks</p>
				                                        </div>
				                                    </form:form>
				                                    <c:choose>
													<c:when test="${isUpdating}">
													<button onclick="submitForm('pulloutForm')" class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton">Update</button>
													<c:url value="/project/delete/pullout/${pullout.getKey()}-end" var="urlDelete"/>
				                                    <a href="${urlDelete}">
       													<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
				                                    </a>
													</c:when>
													<c:when test="${!isUpdating}">
                                            		<button onclick="submitForm('pulloutForm')" class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton">Create</button>
													</c:when>
													</c:choose>
                                            		</c:if>
                   								</div>
                   							</div>
                   						</div>
              						</div>
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
});
function submitForm(id) {
	$('#'+id).submit();
}
</script>
</html>