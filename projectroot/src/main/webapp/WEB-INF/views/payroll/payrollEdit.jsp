<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Payroll Edit</title>
	
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
	            	TODO
	                <small>${action} Field</small>
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
															</table>
															
															
															
															<br/><label>Teams</label><br/>
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
															</table>
															
															
															
															<br/><label>Tasks</label><br/>
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
															</table>
															
															
															
															<!--     public static final String ATTR_PROJECT_STRUCT_DELIVERIES = "projectStructDeliveries"; -->
															<br/><label>Deliveries</label><br/>
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
						                                		<c:set value="${deliveryStaffMap.value}" var="staffList"/>
						                                		<c:forEach items="${staffList}" var="staff">
																<tr>
																	<td align="center">
								                                		<form:checkbox class="form-control include-checkbox delivery-checkboxes" 
								                                			path="staffIDs" 
								                                			value="${staff.id}"
								                                			/>
																	</td>
																	<td>
																		${delivery.name}
																	</td>
																	<td>
																		${staff.getFullName()}
																	</td>
																</tr>
						                                		</c:forEach>
															</c:forEach>
															</tbody>
															</table>
															
				                                            
				                                        </div>
	                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton">Update</button>
				                                    </form:form>
<%--                                             		<c:url var="urlDeleteField" value="/project/field/delete" /> --%>
<%--                                             		<a href="${urlDeleteField}"> --%>
<!-- 														<button class="btn btn-default btn-flat btn-sm">Remove This Field</button> -->
<!-- 													</a> -->
                   								</div>
                   							</div>
                   						</div>
                   						</c:if>
              						</div>
                                </div><!-- /.tab-pane -->
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
	
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