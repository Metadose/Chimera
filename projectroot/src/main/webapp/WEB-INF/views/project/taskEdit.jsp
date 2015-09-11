<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<c:choose>
		<c:when test="${task.id == 0}">
			<title>Create New Task</title>
		</c:when>
		<c:when test="${task.id > 0}">
			<title>${task.title} | Edit Task</title>
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
		<!--  -->
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	            	<c:choose>
	            		<c:when test="${task.id == 0}">
	            			New Task
	                		<small>Create Task</small>
	            		</c:when>
	            		<c:when test="${task.id > 0}">
	            			${task.title}
	            			<small>Edit Task</small>
	            		</c:when>
	            	</c:choose>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-md-12">
                    	<c:url var="urlBack" value="/project/edit/${task.project.id}" />
	                    <a href="${urlBack}">
							<button class="btn btn-cebedo-back btn-flat btn-sm">Back to Project</button>
						</a><br/><br/>
                    	${uiParamAlert}
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                                <c:if test="${task.id > 0}">
                                <li><a href="#tab_assigned_staff" data-toggle="tab">Assign Staff</a></li>
                            	</c:if>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
                   									<form:form modelAttribute="task" role="form" name="detailsForm" id="detailsForm" method="post" action="${contextPath}/project/create/task">
				                                        <div class="form-group">
				                                        	<label>Status</label>
				                                            <form:select class="form-control" id="task_status" path="status">
					                                            <c:forEach items="${taskStatusList}" var="taskStatus">
						                                    		<form:option value="${taskStatus.id()}" label="${taskStatus.label()}"/>
				                                            	</c:forEach>
				                                            </form:select>
				                                            <p class="help-block">Choose the task status</p>

				                                            <label>Title</label>
				                                            <form:input type="text" class="form-control" path="title" placeholder="Sample: Site works, concrete works, setup of scaffolding"/>
				                                            <p class="help-block">Enter a title for the task</p>
				                                            
				                                            <label>Content</label>
				                                            <form:input type="text" class="form-control" path="content" placeholder="Sample: Initial clearing of the lot and misc preparations"/>
				                                            <p class="help-block">Provide more task details</p>
				                                            
				                                            <label>Planned Start Date</label>
					                                        <div class="input-group">
					                                            <div class="input-group-addon">
					                                                <i class="fa fa-calendar"></i>
					                                            </div>
					                                            <fmt:formatDate value="${task.dateStart}" var="dateString" pattern="yyyy/MM/dd" />
					                                            <form:input type="text" class="form-control date-picker" path="dateStart" placeholder="Sample: 2016/06/25" value="${dateString}"/>
					                                        </div>
				                                            <p class="help-block">Enter the task start date</p>

					                                        <label>Planned Duration (Man Days)</label>
				                                            <form:input type="text" class="form-control" path="duration" placeholder="Sample: 30, 40, 50"/>
				                                            <p class="help-block">Planned required man-days to complete the task</p>

				                                            <label>Actual Start Date</label>
					                                        <div class="input-group">
					                                            <div class="input-group-addon">
					                                                <i class="fa fa-calendar"></i>
					                                            </div>
					                                            <fmt:formatDate value="${task.actualDateStart}" var="dateString" pattern="yyyy/MM/dd" />
					                                            <form:input type="text" class="form-control date-picker" path="actualDateStart" placeholder="Sample: 2016/06/25" value="${dateString}"/>
					                                        </div>
				                                            <p class="help-block">Enter the actual start date</p>

					                                        <label>Actual Duration (Man Days)</label>
				                                            <form:input type="text" class="form-control" path="actualDuration" placeholder="Sample: 30, 40, 50"/>
				                                            <p class="help-block">Actual man-days to complete the task</p>
				                                        </div>
				                                    </form:form>
				                                    <c:choose>
		                                            	<c:when test="${task.id == 0}">
		                                            		<button class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${task.id > 0}">
		                                            		<button class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		
															<div class="btn-group">
															<button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
															<ul class="dropdown-menu">
																<li>
																	<a href="<c:url value="/project/delete/task/${task.id}"/>" class="cebedo-dropdown-hover">
															    		Confirm Delete
															    	</a>
																</li>
															</ul>
															</div>
		                                            	</c:when>
		                                            </c:choose>
                   								</div>
                   							</div>
                   						</div>
              						</div>
                                </div><!-- /.tab-pane -->
                                <c:if test="${task.id > 0}">
                                <div class="tab-pane" id="tab_assigned_staff">
                                	<div class="box">
		                                <div class="box-body">

			                                	<c:if test="${empty task.project.assignedStaff && empty task.staff}">
													<div class="callout callout-warning">
														<h4>Warning!</h4>
														<p>There are <b>no staff members</b> assigned to the <b>project</b>.</p>
													</div>
			                                	</c:if>

			                                	<c:if test="${!empty staffList || !empty task.staff}">
			                                	<table>
			                                    	<tr>
			                                    		<c:if test="${!empty staffList}">
	 		                                    		<form:form 
	 		                                    		modelAttribute="staffAssignment"  
	 		                                    		method="post" 
	 		                                    		action="${contextPath}/project/assign/task/staff"> 
	 		                                    			<td>
	 		                                    			<form:select class="form-control" path="staffID"> 
	                                     						<c:forEach items="${staffList}" var="staff"> 
	                                     							<form:option value="${staff.id}" label="${staff.getFullName()}"/> 
	                                     						</c:forEach> 
	 		                                    			</form:select> 
	 		                                    			</td>
	 		                                    			<td>
	 		                                    				&nbsp;
	 		                                    			</td>
	 														<td>
	 														<button class="btn btn-cebedo-assign btn-flat btn-sm">Assign</button>
	 		                                    			</td> 
	 		                                    		</form:form> 
			                                    		</c:if>
			                                    		<td> 
			                                    			&nbsp;
			                                    		</td>
			                                    		<c:if test="${!empty task.staff}">
			                                    		<td>

			                                    			<div class="btn-group">
			                                    			<button type="button" class="btn btn-cebedo-unassign-all btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Unassign All</button>
			                                    			<ul class="dropdown-menu">
			                                    				<li>
			               											<c:url var="urlTaskUnassignStaffAll" value="/project/unassign/task/staff/all"/>
					                                    			<a href="${urlTaskUnassignStaffAll}" class="cebedo-dropdown-hover">
			                                    			    		Confirm Unassign All
			                                    			    	</a>
			                                    				</li>
			                                    			</ul>
			                                    			</div>
			                                    		</td>
			                                    		</c:if>
			                                    	</tr>
			                                    </table>
			                                    <br/>
			                                    </c:if>
			                                    <table id="staff-table" class="table table-bordered table-striped">
			                                    	<thead>
			                                            <tr>
			                                            	<th>&nbsp;</th>
			                                                <th>Full Name</th>
			                                                <th>Company Position</th>
			                                                <th>Salary (Daily)</th>
			                                                <th>E-Mail</th>
			                                                <th>Contact Number</th>
			                                            </tr>
	                                        		</thead>
			                                        <tbody>
				                                		<c:forEach items="${task.staff}" var="staffAssign">
				                                            <tr>
				                                            	<td>
				                                            		<center>
				                                            			<a href="<c:url value="/project/edit/staff/${staffAssign.id}"/>">
								                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
				                                            			</a>

		                   												<div class="btn-group">
		                   												<button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Unassign</button>
		                   												<ul class="dropdown-menu">
		                   													<li>
		                   														<c:url var="urlUnassignStaff" value="/project/unassign/task/staff/${staffAssign.id}"/>
		                   														<a href="${urlUnassignStaff}" class="cebedo-dropdown-hover">
		                   												    		Confirm Unassign
		                   												    	</a>
		                   													</li>
		                   												</ul>
		                   												</div>
																	</center>
																</td>
				                                                <td>${staffAssign.getFullName()}</td>
				                                                <td>${staffAssign.companyPosition}</td>
				                                                <td style="text-align: right;">${staffAssign.getWageAsString()}</td>
				                                                <td>${staffAssign.email}</td>
				                                                <td><fmt:formatNumber type="number" pattern="###" value="${staffAssign.contactNumber}" /></td>
				                                            </tr>
			                                            </c:forEach>
				                                    </tbody>
				                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                            	</c:if>
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
		
		function submitAjax(id) {
			var formObj = $('#'+id);
			var serializedData = formObj.serialize();
			$.ajax({
				type: "POST",
				url: '${contextPath}/field/update/assigned/task',
				data: serializedData,
				success: function(response){
					location.reload();
				}
			});
		}
	
		$(document).ready(function() {
			$("#task_status").val("${task.status}");
			$("#staff-table").dataTable();
			$("#teams-table").dataTable();
	    });

	    $(document).ready(function() {
			$('.date-picker').datepicker({
			    format: 'yyyy/mm/dd'
			})
	    });
	</script>
</body>
</html>