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
                   								<div class="box-body">
                   									<c:choose>
                   									<c:when  test="${empty projectStructManagers}">
                   									<i>Cannot create Payroll without a Project Manager.<br/>
                   									Only assigned Project Managers are allowed to approve/reject a Payroll.</i>
                   									</c:when>
                   									<c:when  test="${!empty projectStructManagers}">
                   									<form:form modelAttribute="projectPayroll"
														id="detailsForm"
														method="post"
														action="${contextPath}/project/create/payroll">
				                                        <div class="form-group">
				                                        
				                                        	<!-- List of all staff as Project Manager -->
				                                            <label>Approver</label>
				                                            <form:select class="form-control" path="approverID">
				                                            	<c:forEach items="${projectStructManagers}" var="manager">
				                                            	<form:option class="form-control" value="${manager.id}" label="${manager.getFullName()}"/>
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
				                                            <form:input type="text" class="form-control date-picker" path="startDate"/><br/>
				                                            <label>End Date</label>
				                                            <form:input type="text" class="form-control date-picker" path="endDate"/>
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
                   								<div class="box-body">
                   									<form:form modelAttribute="projectPayroll"
														id="detailsForm"
														method="post"
														action="${contextPath}/project/create/payroll">
				                                        <div class="form-group">

															<label>Managers</label><br/>
															<c:choose>
															<c:when test="${empty projectStructManagers}">
															<i>No manager assigned.</i><br/><br/>
															</c:when>
															<c:when test="${!empty projectStructManagers}">
			                                            	<table class="table table-bordered table-striped">
															<thead>
					                                    		<tr>
						                                            <th>Include</th>
						                                            <th>Manager</th>
						                                        </tr>
					                                    	</thead>
															<tbody>
															<c:forEach items="${projectStructManagers}" var="manager">
																<tr>
																	<td align="center">
								                                		<form:checkbox class="form-control include-checkbox manager-checkboxes" 
								                                			path="staffIDs" 
								                                			value="${manager.id}"
								                                			/>
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
															
															
															<label>Teams</label><br/>
															<c:choose>
															<c:when test="${empty projectStructTeams}">
															<i>No team assigned.</i><br/><br/>
															</c:when>
															<c:when test="${!empty projectStructTeams}">
			                                            	<table class="table table-bordered table-striped">
															<thead>
					                                    		<tr>
						                                            <th>Include</th>
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
								                                		<form:checkbox class="form-control include-checkbox team-checkboxes" 
								                                			path="staffIDs" 
								                                			value="${teamMember.id}"
								                                			/>
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
															
															
															<label>Tasks</label><br/>
															<c:choose>
															<c:when test="${empty projectStructTasks}">
															<i>No task assigned.</i><br/><br/>
															</c:when>
															<c:when test="${!empty projectStructTasks}">
															
			                                            	<table class="table table-bordered table-striped">
															<thead>
					                                    		<tr>
						                                            <th>Include</th>
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
								                                		<form:checkbox class="form-control include-checkbox task-checkboxes" 
								                                			path="staffIDs" 
								                                			value="${staff.id}"
								                                			/>
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
															
															
															
															<label>Deliveries</label><br/>
															<c:choose>
															<c:when test="${empty projectStructDeliveries}">
															<i>No deliveries.</i><br/>
															</c:when>
															<c:when test="${!empty projectStructDeliveries}">
			                                            	<table class="table table-bordered table-striped">
															<thead>
					                                    		<tr>
						                                            <th>Include</th>
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
								                                		<form:checkbox class="form-control include-checkbox delivery-checkboxes" 
								                                			path="staffIDs" 
								                                			value="${deliveryStaff.id}"
								                                			/>
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
				                                        <c:if test="${!empty projectStructManagers || !empty projectStructTeams || !empty projectStructTasks || !empty projectStructDeliveries}">
	                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton">Update</button>
				                                        </c:if>
				                                    </form:form>
				                                    <c:if test="${!empty projectStructManagers || !empty projectStructTeams || !empty projectStructTasks || !empty projectStructDeliveries}">
				                                    <br/>
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
              						<div class="row">
                   						<div class="col-xs-12">
                   							<div class="box box-default">
                   								<div class="box-body">
                   									<table id="treegrid1"></table>
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
		
		function selectAll(checkboxClass) {
			$('.'+checkboxClass).attr('checked', true);
			return false;
		}
		
		$(document).ready(function() {
			$('.date-picker').datepicker({
			    format: 'yyyy/mm/dd'
			});
		});
		
// 		$(document).ready(function() {
// 		    $('#selecctall').click(function(event) {  //on click 
// 		        if(this.checked) { // check select status
// 		            $('.checkbox1').each(function() { //loop through each checkbox
// 		                this.checked = true;  //select all checkboxes with class "checkbox1"               
// 		            });
// 		        }else{
// 		            $('.checkbox1').each(function() { //loop through each checkbox
// 		                this.checked = false; //deselect all checkboxes with class "checkbox1"                       
// 		            });         
// 		        }
// 		    });
		    
// 		});
	</script>
</body>
</html>