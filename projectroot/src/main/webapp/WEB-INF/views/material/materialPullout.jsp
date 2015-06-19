<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>${pullout.material.name} Pull-Out</title>
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
	            <h1>
	            	${pullout.project.name} <i class="fa fa-angle-double-right"></i>
	            	${pullout.delivery.name} <i class="fa fa-angle-double-right"></i>
	            	${pullout.material.name}
		            <small>Pull-Out Materials</small>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
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
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Pull-Out Materials</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="callout callout-info callout-cebedo">
									                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
									                </div>
									                <table>
									                <tr>
									                	<td><label>Delivery Name:</label></td>
									                	<td>&nbsp;</td>
									                	<td>${pullout.delivery.name}</td>
									                </tr>
									                <tr>
									                	<td><label>Material Name:</label></td>
									                	<td>&nbsp;</td>
									                	<td>${pullout.material.name}</td>
									                </tr>
									                <tr>
									                	<td>&nbsp;</td>
									                	<td>&nbsp;</td>
									                	<td>&nbsp;</td>
									                </tr>
									                <tr>
									                	<td><label>Available:</label></td>
									                	<td>&nbsp;</td>
									                	<td align="right">${pullout.material.available}
									                	</td>
									                </tr>
									                <tr>
									                	<td><label>Used:</label></td>
									                	<td>&nbsp;</td>
									                	<td align="right">${pullout.material.used}</td>
									                </tr>
									                <tr>
									                	<td><label>Total Quantity:</label></td>
									                	<td>&nbsp;</td>
									                	<td align="right">${pullout.material.quantity}</td>
									                </tr>
									                <tr>
									                	<td><label>Units:</label></td>
									                	<td>&nbsp;</td>
									                	<td align="right">${pullout.material.unit}</td>
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
									                <br/>
                   									<form:form modelAttribute="pullout"
														id="pulloutForm"
														method="post"
														action="${contextPath}/project/do-pullout/material">
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
				                                            <label>Date and Time</label>
				                                            <form:input type="text" placeholder="Sample: 2015/06/24 08:15" class="form-control" id="date-picker" path="datetime"/>
				                                            <p class="help-block">Choose the date and time of the pull-out</p>
				                                            <label>Remarks</label>
				                                            <form:input type="text" placeholder="Sample: The pull-out was delayed due to rain." class="form-control" path="remarks"/>
				                                            <p class="help-block">Add additional remarks</p>
				                                        </div>
				                                    </form:form>
                                            		<button onclick="submitForm('pulloutForm')" class="btn btn-cebedo-pullout btn-flat btn-sm" id="detailsButton">Pull-Out</button>
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