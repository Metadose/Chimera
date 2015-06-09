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
	<c:choose>
   	<c:when test="${!projectPayroll.saved}">
    	<title>Payroll Create</title>
   	</c:when>
   	<c:when test="${projectPayroll.saved}">
		<title>Payroll Edit</title>
   	</c:when>
   	</c:choose>
	
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
	            	<c:when test="${!projectPayroll.saved}">
		            	New Payroll
		                <small>Create Payroll</small>
	            	</c:when>
	            	<c:when test="${projectPayroll.saved}">
	            		<fmt:formatDate pattern="yyyy/MM/dd" value="${projectPayroll.startDate}" var="startDate"/>
	            		<fmt:formatDate pattern="yyyy/MM/dd" value="${projectPayroll.endDate}" var="endDate"/>
		            	${startDate} to ${endDate}
		                <small>Edit Payroll</small>
	            	</c:when>
	            	</c:choose>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <c:url var="urlBack" value="/project/edit/${projectPayroll.projectID}" />
	                    <a href="${urlBack}">
							<button class="btn btn-default btn-flat btn-sm">Back to Project</button>
						</a><br/><br/>
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                                <c:if test="${!empty projectStructManagers && !empty projectPayroll.staffIDs && !empty payrollJSON}">
                                <li><a href="#tab_computation" data-toggle="tab">Computation</a></li>
                                </c:if>
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
                   									<c:choose>
                   									<c:when  test="${empty payrollApproverOptions}">
                   									<i>Cannot create Payroll without a Project Manager.<br/>
                   									Only assigned Project Managers are allowed to approve or reject a Payroll.</i>
                   									</c:when>
                   									<c:when  test="${!empty payrollApproverOptions}">
                   									<form:form modelAttribute="projectPayroll"
														id="detailsForm"
														method="post"
														action="${contextPath}/project/create/payroll">
				                                        <div class="form-group">
				                                        
				                                        	<!-- List of all staff as Project Manager -->
				                                            <label>Approver</label>
				                                            <form:select class="form-control" path="approverID">
				                                            	<c:forEach items="${payrollApproverOptions}" var="manager">
				                                            	<form:option class="form-control" value="${manager.user.id}" label="${manager.getFullName()}"/>
				                                            	</c:forEach>
				                                            </form:select>
				                                            <br/>
				                                            
				                                            <!-- List of all in PayrollStatus enum -->
				                                            <label>Status</label>
				                                            <form:select class="form-control" path="statusID">
				                                            	<c:forEach items="${payrollStatusArr}" var="payrollStatus">
				                                            	<form:option class="form-control" value="${payrollStatus.id()}" label="${payrollStatus.label()}"/>
				                                            	</c:forEach>
				                                            </form:select>
				                                            <br/>
				                                            
				                                            <!-- Date pickers -->
				                                            <label>Start Date</label>
				                                            <form:input type="text" class="form-control date-picker" path="startDate" value="${startDate}"/><br/>
				                                            <label>End Date</label>
				                                            <form:input type="text" class="form-control date-picker" path="endDate" value="${endDate}"/>
				                                        </div>
				                                        <c:if test="${projectPayroll.saved}">
	                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton">Update</button>
				                                        </c:if>
				                                        <c:if test="${!projectPayroll.saved}">
	                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton">Create</button>
				                                        </c:if>
				                                    </form:form>
				                                    </c:when>
                   									</c:choose>
                   								</div>
                   							</div>
                   						</div>
                   						<c:if test="${projectPayroll.saved}">
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Add to Payroll</h3>
                   								</div>
                   								<div class="box-body">
                   									<label>All</label>&nbsp;
													<a href="#" onclick="checkAll('include-checkbox')" class="general-link">Check All</a>&nbsp;
													<a href="#" onclick="uncheckAll('include-checkbox')" class="general-link">Uncheck All</a>
													<br/>
													<br/>
													<c:set value="" var="alreadyRendered"/>
                   									<form:form modelAttribute="projectPayroll"
														id="checkboxesForm"
														method="post"
														action="${contextPath}/project/create/payroll/clear/computation">
				                                        <div class="form-group">



															<label>Managers</label>&nbsp;
															<a href="#" onclick="checkAll('manager-checkboxes')" class="general-link">Check All</a>&nbsp;
															<a href="#" onclick="uncheckAll('manager-checkboxes')" class="general-link">Uncheck All</a>
															<br/>
															<c:choose>
															<c:when test="${empty projectStructManagers}">
															<i>No manager assigned.</i><br/><br/>
															</c:when>
															<c:when test="${!empty projectStructManagers}">
			                                            	<table class="table table-bordered table-striped">
															<thead>
					                                    		<tr>
						                                            <th>Add</th>
						                                            <th>Manager</th>
						                                        </tr>
					                                    	</thead>
															<tbody>
															<c:forEach items="${projectStructManagers}" var="manager">
																<tr>
																	<td align="center">
																		<c:if test="${!fn:contains(alreadyRendered, manager.id)}">
																		<c:set value="${alreadyRendered}-${manager.id}-" var="alreadyRendered"/>
								                                		<form:checkbox class="form-control include-checkbox manager-checkboxes" 
								                                			path="staffIDs" 
								                                			value="${manager.id}"
								                                			/>
																		</c:if>
																	</td>
																	<td>
																		${manager.getFullName()}
																	</td>
																</tr>
															</c:forEach>
															</tbody>
															</table><br/>
															</c:when>
															</c:choose>
															
															
															
															<label>Teams</label>&nbsp;
															<a href="#" onclick="checkAll('team-checkboxes')" class="general-link">Check All</a>&nbsp;
															<a href="#" onclick="uncheckAll('team-checkboxes')" class="general-link">Uncheck All</a>
															<br/>
															<c:choose>
															<c:when test="${empty projectStructTeams}">
															<i>No team assigned.</i><br/><br/>
															</c:when>
															<c:when test="${!empty projectStructTeams}">
			                                            	<table class="table table-bordered table-striped">
															<thead>
					                                    		<tr>
						                                            <th>Add</th>
						                                            <th>Team</th>
						                                            <th>Staff</th>
						                                        </tr>
					                                    	</thead>
															<tbody>
															<c:forEach items="${projectStructTeams}" var="teamStaffMap">
																<c:set value="${teamStaffMap.key}" var="team"/>
						                                		<c:set value="${teamStaffMap.value}" var="staffList"/>
						                                		<c:forEach items="${staffList}" var="teamMember">
																<tr>
																	<td align="center">
																		<c:if test="${!fn:contains(alreadyRendered, teamMember.id)}">
																		<c:set value="${alreadyRendered}-${teamMember.id}-" var="alreadyRendered"/>
								                                		<form:checkbox class="form-control include-checkbox team-checkboxes" 
								                                			path="staffIDs" 
								                                			value="${teamMember.id}"
								                                			/>
																		</c:if>
																	</td>
																	<td>
																		${team.name}
																	</td>
																	<td>
																		${teamMember.getFullName()}
																	</td>
																</tr>
						                                		</c:forEach>
															</c:forEach>
															</tbody>
															</table><br/>
															</c:when>
															</c:choose>
															


															<label>Tasks</label>&nbsp;
															<a href="#" onclick="checkAll('task-checkboxes')" class="general-link">Check All</a>&nbsp;
															<a href="#" onclick="uncheckAll('task-checkboxes')" class="general-link">Uncheck All</a>
															<br/>
															<c:choose>
															<c:when test="${empty projectStructTasks}">
															<i>No task assigned.</i><br/><br/>
															</c:when>
															<c:when test="${!empty projectStructTasks}">
															
			                                            	<table class="table table-bordered table-striped">
															<thead>
					                                    		<tr>
						                                            <th>Add</th>
						                                            <th>Task</th>
						                                            <th>Staff</th>
						                                        </tr>
					                                    	</thead>
															<tbody>
															<c:forEach items="${projectStructTasks}" var="taskStaffMap">
																<c:set value="${taskStaffMap.key}" var="task"/>
						                                		<c:set value="${taskStaffMap.value}" var="staffList"/>
						                                		<c:forEach items="${staffList}" var="staff">
																<tr>
																	<td align="center">
																		<c:if test="${!fn:contains(alreadyRendered, staff.id)}">
																		<c:set value="${alreadyRendered}-${staff.id}-" var="alreadyRendered"/>
								                                		<form:checkbox class="form-control include-checkbox task-checkboxes" 
								                                			path="staffIDs" 
								                                			value="${staff.id}"
								                                			/>
																		</c:if>
																	</td>
																	<td>
																		${task.title}
																	</td>
																	<td>
																		${staff.getFullName()}
																	</td>
																</tr>
						                                		</c:forEach>
															</c:forEach>
															</tbody>
															</table><br/>
															</c:when>
															</c:choose>
															
															
															
															<label>Deliveries</label>&nbsp;
															<a href="#" onclick="checkAll('delivery-checkboxes')" class="general-link">Check All</a>&nbsp;
															<a href="#" onclick="uncheckAll('delivery-checkboxes')" class="general-link">Uncheck All</a>
															<br/>
															<c:choose>
															<c:when test="${empty projectStructDeliveries}">
															<i>No deliveries.</i><br/>
															</c:when>
															<c:when test="${!empty projectStructDeliveries}">
			                                            	<table class="table table-bordered table-striped">
															<thead>
					                                    		<tr>
						                                            <th>Add</th>
						                                            <th>Delivery</th>
						                                            <th>Staff</th>
						                                        </tr>
					                                    	</thead>
															<tbody>
															<c:forEach items="${projectStructDeliveries}" var="deliveryStaffMap">
																<c:set value="${deliveryStaffMap.key}" var="delivery"/>
						                                		<c:set value="${deliveryStaffMap.value}" var="deliveryStaffList"/>
						                                		<c:forEach items="${deliveryStaffList}" var="deliveryStaff">
																<tr>
																	<td align="center">	
																		<c:if test="${!fn:contains(alreadyRendered, deliveryStaff.id)}">
																		<c:set value="${alreadyRendered}-${deliveryStaff.id}-" var="alreadyRendered"/>
								                                		<form:checkbox class="form-control include-checkbox delivery-checkboxes" 
								                                			path="staffIDs" 
								                                			value="${deliveryStaff.id}"
								                                			/>
																		</c:if>
																	</td>
																	<td>
																		${delivery.name}
																	</td>
																	<td>
																		${deliveryStaff.getFullName()}
																	</td>
																</tr>
						                                		</c:forEach>
															</c:forEach>
															</tbody>
															</table>
															</c:when>
															</c:choose>
				                                        </div>
				                                    </form:form>
				                                    
			                                        <c:if test="${!empty projectStructManagers || !empty projectStructTeams || !empty projectStructTasks || !empty projectStructDeliveries}">
                                            		<button onclick="submitForm('checkboxesForm')" class="btn btn-default btn-flat btn-sm" id="detailsButton">Update</button>
			                                        </c:if>
			                                        
				                                    <c:if test="${(!empty projectStructManagers || !empty projectStructTeams || !empty projectStructTasks || !empty projectStructDeliveries) && !empty projectPayroll.staffIDs}">
                                            		<c:url var="urlCompute" value="/project/compute/payroll" />
                                            		<a href="${urlCompute}">
														<button class="btn btn-default btn-flat btn-sm">Compute Payroll</button>
													</a>
													</c:if>
													
                   								</div>
                   							</div>
                   						</div>
                   						</c:if>
              						</div>
                                </div><!-- /.tab-pane -->
                                <c:if test="${!empty projectStructManagers && !empty projectPayroll.staffIDs && !empty payrollJSON}">
                                <div class="tab-pane" id="tab_computation">
              						<div class="row">
                   						<div class="col-xs-12">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Computation</h3>
                   								</div>
                   								<div class="box-body">
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
        $("#treegrid1").igTreeGrid({
            dataSource: flatDS,
            primaryKey: "primaryKey",
            foreignKey: "foreignKey",
            columns: [
				{ headerText: "primaryKey", key: "primaryKey", dataType: "number", hidden: true },
				{ headerText: "foreignKey", key: "foreignKey", dataType: "number", hidden: true },
                { headerText: "Name", key: "name", dataType: "string" },
                { headerText: "Payroll", key: "value", dataType: "string" }
            ]
        });
	});
	</script>
	</c:if>
	
	<script>
		function submitForm(id) {
			$('#'+id).submit();
		}
		
		function checkAll(checkboxClass) {
			$('.'+checkboxClass).each(function() { //loop through each checkbox
	             this.checked = true;  //select all checkboxes with class "checkbox1"
	             $(this).parent().attr('class', 'icheckbox_minimal checked');
	        });
			return false;
		}
		
		function uncheckAll(checkboxClass) {
			$('.'+checkboxClass).each(function() { //loop through each checkbox
	             this.checked = false;  //select all checkboxes with class "checkbox1"
	             $(this).parent().attr('class', 'icheckbox_minimal');
	        });
			return false;
		}
		
		$(document).ready(function() {
			$('.date-picker').datepicker({
			    format: 'yyyy/mm/dd'
			});
		});
	</script>
</body>
</html>