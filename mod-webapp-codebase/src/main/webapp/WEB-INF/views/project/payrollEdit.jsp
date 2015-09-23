<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:formatDate pattern="yyyy/MM/dd" value="${projectPayroll.startDate}" var="startDate"/>
<fmt:formatDate pattern="yyyy/MM/dd" value="${projectPayroll.endDate}" var="endDate"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<c:choose>
   	<c:when test="${!projectPayroll.saved}">
    	<title>Create New Payroll</title>
   	</c:when>
   	<c:when test="${projectPayroll.saved}">
		<title>${startDate} to ${endDate} | Edit Payroll</title>
   	</c:when>
   	</c:choose>

   	<script src="<c:url value="/resources/js/accounting.min.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/js/accounting-aux.js" />"type="text/javascript"></script>
	
	<!-- Ignite UI Required Combined CSS Files -->
	<link href="<c:url value="/resources/lib/igniteui/infragistics.theme.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/igniteui/infragistics.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/igniteui/infragistics.ui.treegrid.css" />"rel="stylesheet" type="text/css" />
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
		.ui-widget-header {
			width: 100%;
		}
		span.ui-iggrid-headertext {
			font-size: smaller;
		}
	</style>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<!--  -->
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	            	<c:choose>
	            	<c:when test="${!projectPayroll.saved}">
		            	New Payroll
		                <small>Create Payroll</small>
	            	</c:when>
	            	<c:when test="${projectPayroll.saved}">
		            	${startDate} to ${endDate}
		                <small>Edit Payroll</small>
	            	</c:when>
	            	</c:choose>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-md-12">
                        <c:url var="urlBack" value="/project/edit/${projectPayroll.project.id}" />
	                    <a href="${urlBack}">
							<button class="btn btn-cebedo-back btn-flat btn-sm">Back to Project</button>
						</a><br/><br/>
                    	${uiParamAlert}
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                                <c:if test="${!empty payrollJSON}">
                                <li><a href="#tab_computation" data-toggle="tab">Computation</a></li>
                                </c:if>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
                   									<form:form modelAttribute="projectPayroll"
														id="detailsForm"
														method="post"
														action="${contextPath}/project/create/payroll">
				                                        <div class="form-group">
				                                            
				                                            <!-- List of all in PayrollStatus enum -->
				                                            <label>Status</label>
				                                            <form:select class="form-control" path="statusID">
				                                            	<c:forEach items="${payrollStatusArr}" var="payrollStatus">
				                                            	<form:option class="form-control" value="${payrollStatus.id()}" label="${payrollStatus.label()}"/>
				                                            	</c:forEach>
				                                            </form:select>
				                                            <p class="help-block">Set the status of this payroll</p>
				                                            
				                                            <!-- Date pickers -->
				                                            <label>Start Date</label>
				                                            <form:input type="text" placeholder="Sample: 2015/06/01, 2016/07/25, 2017/01/31" class="form-control date-picker" path="startDate" value="${startDate}"/>
				                                            <p class="help-block">Choose the starting coverage date of this payroll</p>
				                                            
				                                            <label>End Date</label>
				                                            <form:input type="text" placeholder="Sample: 2015/06/01, 2016/07/25, 2017/01/31" class="form-control date-picker" path="endDate" value="${endDate}"/>
				                                            <p class="help-block">Choose the end date of this payroll</p>
				                                            
				                                        </div>
				                                        <c:if test="${projectPayroll.saved}">
	                                            		<button class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton">
	                                            		Update
	                                            		</button>

	                                            		<div class="btn-group">
	                                            		<button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
	                                            		<ul class="dropdown-menu">
	                                            			<li>
	                                            				<c:url value="/project/delete/payroll/${projectPayroll.getKey()}-end" var="urlDeletePayroll"/>
	                                            				<a href="${urlDeletePayroll}" class="cebedo-dropdown-hover">
	                                            		    		Confirm Delete
	                                            		    	</a>
	                                            			</li>
	                                            		</ul>
	                                            		</div>
	                                            		
				                                        </c:if>
				                                        <c:if test="${!projectPayroll.saved}">
	                                            		<button class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton">Create</button>
				                                        </c:if>
				                                    </form:form>
                   								</div>
                   							</div>
                   							<c:if test="${projectPayroll.saved && !empty manualStaffList}">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Include to Payroll</h3>
                   								</div>
                   								<div class="box-body">
			                                        <table>
				                                    <form:form modelAttribute="payrollIncludeStaff"
														id="detailsForm"
														method="post"
														action="${contextPath}/project/edit/payroll/include/staff">
				                                        	<tr>
				                                        	<td><label>
				                                        	<c:url value="/staff/list" var="urlLink"/>
				                                        	<a href="${urlLink}" class="general-link">
				                                        	Staff
				                                        	</a>
				                                        	</label></td>
				                                        	<td>&nbsp;</td>
				                                        	<td style="width: 100%">
				                                            <form:select class="form-control" path="staffID">
				                                            	<c:forEach items="${manualStaffList}" var="staff">
				                                            	<form:option class="form-control" value="${staff.id}" label="${staff.getFullName()}"/>
				                                            	</c:forEach>
				                                            </form:select>
				                                        	</td>
				                                        	<td>&nbsp;</td>
				                                        	<td>
				                                        	<button class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton">Include</button>
				                                        	</td>
				                                        	</tr>
				                                    </form:form>
			                                        </table>
			                                        <p class="help-block">Include staff members who are not assigned to the project</p>
                   								</div>
                   							</div>
                   							</c:if>
                   						</div>
                   						<c:if test="${projectPayroll.saved}">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Payroll Checklist</h3>
                   								</div>  
                   								<div class="box-body">

													<c:set value="" var="alreadyRendered"/>
													
                   									<form:form modelAttribute="projectPayroll"
														id="checkboxesForm"
														method="post"
														action="${contextPath}/project/create/payroll/clear/computation">
				                                        <div class="form-group">
															

															<c:if test="${!empty staffList}">
															<a href="#" onclick="checkAll('staff-checkboxes')" class="general-link">Check All</a>&nbsp;
															<a href="#" onclick="uncheckAll('staff-checkboxes')" class="general-link">Uncheck All</a>
															<p class="help-block">Check or uncheck staff members</p>
															</c:if>

			                                            	<table class="table table-bordered table-striped">
															<thead>
					                                    		<tr>
						                                            <th>Add</th>
						                                            <th>Staff</th>
						                                        </tr>
					                                    	</thead>
															<tbody>
						                                		<c:forEach items="${staffList}" var="staff">
																<c:if test="${!fn:contains(alreadyRendered, staff.id)}">
																<tr>
																	<td align="center">
																		<c:set value="${alreadyRendered}-${staff.id}-" var="alreadyRendered"/>
								                                		<form:checkbox class="form-control include-checkbox staff-checkboxes" 
								                                			path="staffIDs" 
								                                			value="${staff.id}"
								                                			/>
																	</td>
																	<td>
																		<c:url var="staffLink" value="/project/edit/staff/${staff.id}"/>
																		<a href="${staffLink}" class="general-link">
																		${staff.getFullName()}
				                                            			</a>
																	</td>
																</tr>
																</c:if>
						                                		</c:forEach>
															</tbody>
															</table>
				                                        </div>
				                                    </form:form>
				                                    
			                                        <c:if test="${!empty staffList || !empty managerList}">
                                            		<button onclick="submitForm('checkboxesForm')" class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton">
                                            		Update
                                            		</button>
			                                        </c:if>
			                                        
				                                    <c:if test="${!empty projectPayroll.assignedStaffList && fn:length(projectPayroll.assignedStaffList) > 0}">
                                            		<c:url var="urlCompute" value="/project/compute/payroll" />
                                            		<a href="${urlCompute}">
														<button class="btn btn-cebedo-update btn-flat btn-sm">Compute Payroll</button>
													</a>
													
													<c:choose>
													<c:when test="${empty projectPayroll.lastComputed}">
														<p class="help-block">Not yet computed</p>
													</c:when>
													
													<c:when test="${!empty projectPayroll.lastComputed}">
														<fmt:formatDate pattern="yyyy/MM/dd hh:mm:ss a" value="${projectPayroll.lastComputed}" var="lastComputed"/>
														<p class="help-block">Last Computed: ${lastComputed}</p>
													</c:when>
													</c:choose>
													
													</c:if>
													
                   								</div>
                   							</div>
                   						</div>
                   						</c:if>
              						</div>
                                </div><!-- /.tab-pane -->
                                <c:if test="${!empty payrollJSON}">
                                <div class="tab-pane" id="tab_computation">
              						<div class="row">
                   						<div class="col-md-12">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
													<a href="<c:url value="/project/export-xls/payroll/${projectPayroll.getKey()}-end"/>">
		                                        		<button class="btn btn-cebedo-export btn-flat btn-sm">Export</button>
		                                        	</a>

                   									<div class="pull-right">
			                                  		<h3>Grand Total <b><u>
                   									${projectPayroll.getTotalAsString()}
													</u></b></h3>

                   									<fmt:formatDate pattern="yyyy/MM/dd hh:mm:ss a" value="${projectPayroll.lastComputed}" var="lastComputed"/>
                   									<p class="help-block">Computation as of ${lastComputed}</p>
													</div>

                   									<table id="treegrid1"></table>
                   								</div>
                							</div>
                						</div>
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
	
	<!-- Ignite UI Required Combined JavaScript Files -->
	<script src="<c:url value="/resources/lib/modernizr.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/lib/igniteui/infragistics.core.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/lib/igniteui/infragistics.lob.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/lib/igniteui/infragistics.ui.treegrid.js" />"type="text/javascript"></script>
	
	<c:if test="${!empty payrollJSON}">
	<script>
	$(document).ready(function() {
		
		// Tree grid.
		var flatDS = ${payrollJSON};
        $("#treegrid1").igGrid({
            dataSource: flatDS,
            primaryKey: "uuid",
            features:[
                { name: "MultiColumnHeaders" },
				{ name: "Summaries" }
            ],
            columns: [
               	{ headerText: "primaryKey", key: "uuid", dataType: "string", hidden: true },
               	{ headerText: "Name", key: "name", columnCssClass: "cebedo-payroll-staff-name", dataType: "string" },
               	{ headerText: "Total", key: "value", formatter: formatCurrency, columnCssClass: "cebedo-payroll-cell", dataType: "number" },
               	{ headerText: "Salary (Daily)", key: "wage", formatter: formatCurrency, columnCssClass: "cebedo-payroll-cell", dataType: "number" },

               	{ headerText: "Present", group: [
               			{ headerText: "Count", key: "breakdownPresentCount", columnCssClass: "cebedo-payroll-cell", dataType: "number" },
               			{ headerText: "Subtotal", key: "breakdownPresentWage", formatter: formatCurrency, columnCssClass: "cebedo-payroll-cell", dataType: "number" }
               		]},
               	{ headerText: "Overtime", group: [
               			{ headerText: "Count", key: "breakdownOvertimeCount", columnCssClass: "cebedo-payroll-cell", dataType: "number" },
               			{ headerText: "Subtotal", key: "breakdownOvertimeWage", formatter: formatCurrency, columnCssClass: "cebedo-payroll-cell", dataType: "number" }
               		]},
               	{ headerText: "Late", group: [
               			{ headerText: "Count", key: "breakdownLateCount", columnCssClass: "cebedo-payroll-cell", dataType: "number" },
               			{ headerText: "Subtotal", key: "breakdownLateWage", formatter: formatCurrency, columnCssClass: "cebedo-payroll-cell", dataType: "number" }
               		]},
               	{ headerText: "Half-day", group: [
               			{ headerText: "Count", key: "breakdownHalfdayCount", columnCssClass: "cebedo-payroll-cell", dataType: "number" },
               			{ headerText: "Subtotal", key: "breakdownHalfdayWage", formatter: formatCurrency, columnCssClass: "cebedo-payroll-cell", dataType: "number" }
               		]},
               	{ headerText: "Leave", group: [
               			{ headerText: "Count", key: "breakdownLeaveCount", columnCssClass: "cebedo-payroll-cell", dataType: "number" },
               			{ headerText: "Subtotal", key: "breakdownLeaveWage", formatter: formatCurrency, columnCssClass: "cebedo-payroll-cell", dataType: "number" }
               		]},
               	{ headerText: "Absent", group: [
               			{ headerText: "Count", key: "breakdownAbsentCount", columnCssClass: "cebedo-payroll-cell", dataType: "number" },
               			{ headerText: "Subtotal", key: "breakdownAbsentWage", formatter: formatCurrency, columnCssClass: "cebedo-payroll-cell", dataType: "number" }
               		]}

            ]
        });
	});

	$(document).ready(function() {
		$('#treegrid1_summaries_footer_row_count').hide();
		$('#treegrid1_summaries_footer_row_min').hide();
		$('#treegrid1_summaries_footer_row_max').hide();
		$('#treegrid1_summaries_footer_row_avg').hide();
	});
	</script>
	</c:if>
	
	<script>
		function submitForm(id) {
			$('#'+id).submit();
		}
		
		$(document).ready(function() {
			$('.date-picker').datepicker({
			    format: 'yyyy/mm/dd'
			});
		});
	</script>
</body>
</html>