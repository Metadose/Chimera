<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Task ${action}</title>
	
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
	            		<c:when test="${task.id == 0}">New Task</c:when>
	            		<c:when test="${task.id > 0}">${task.content}</c:when>
	            	</c:choose>
	                <small>${action} Task</small>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                    	${uiParamAlert}
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                                <li><a href="#tab_expenses" data-toggle="tab">Expenses</a></li>
                                <li><a href="#tab_assigned_staff" data-toggle="tab">Staff</a></li>
                                <li><a href="#tab_assigned_teams" data-toggle="tab">Teams</a></li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
                   									<c:set var="formAction" value="${contextPath}/task/create"/>
                   									<c:if test="${!empty origin && !empty originID}">
                   										<c:set var="formAction" value="${contextPath}/task/create/from/${origin}/${originID}"/>
                   									</c:if>
                   									<form:form modelAttribute="task" role="form" name="detailsForm" id="detailsForm" method="post" action="${formAction}">
				                                        <div class="form-group">
				                                        	<label>Status</label>
				                                            <form:select class="form-control" id="task_status" path="status">
						                                    	<form:option value="0" label="New"/>
						                                    	<form:option value="1" label="Ongoing"/>
						                                    	<form:option value="2" label="Completed"/>
						                                    	<form:option value="3" label="Failed"/>
						                                    	<form:option value="4" label="Cancelled"/>
				                                            </form:select><br/>
				                                            <label>Title</label>
				                                            <form:input type="text" class="form-control" path="title"/><br/>
				                                            
				                                            <label>Content</label>
				                                            <form:input type="text" class="form-control" path="content"/><br/>
				                                            
				                                            <label>Start</label>
					                                        <div class="input-group">
					                                            <div class="input-group-addon">
					                                                <i class="fa fa-calendar"></i>
					                                            </div>
<%-- 					                                            <form:input type="text" id="date-mask" class="form-control" path="dateStart" data-inputmask="'alias': 'yyyy/mm/dd'" data-mask/> --%>
					                                            <form:input type="text" id="date-mask" class="form-control" path="dateStart"/>
					                                        </div>
					                                        <br/>
					                                        <label>Duration (Man Days)</label>
				                                            <form:input type="text" class="form-control" path="duration"/><br/>
				                                        </div>
				                                    </form:form>
				                                    <c:choose>
		                                            	<c:when test="${task.id == 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${task.id > 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<a href="${contextPath}/task/delete/${task.id}">
																<button class="btn btn-default btn-flat btn-sm">Delete This Task</button>
															</a>
		                                            	</c:when>
		                                            </c:choose>
                   								</div>
                   							</div>
                   						</div>
              						</div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_expenses">
	                               	<div class="row">
                   						<div class="col-xs-12">
                   							<div class="box box-body box-default">
                   							<div class="box-header">
                   								<table>
                   								<tr>
                   								<td><h3 class="box-title">Expenses</h3></td>
                   								<td>
                   									<c:url value="/edit/expense/0" var="addExpenseURL"/>
                   									<a href="${addExpenseURL}">
			                                		<button class="btn btn-default btn-flat btn-sm">Add Expense</button>
                   									</a>
                   								</td>
                   								</tr>
                   								</table>
               								</div>
               								<div class="box-body">
			                                    <table id="expenses-table" class="table table-bordered table-striped">
			                                    	<thead>
			                                    		<tr>
				                                        	<th>&nbsp;</th>
				                                        	<th>ID #</th>
				                                            <th>Name</th>
				                                            <th>Description</th>
				                                            <th>Value</th>
				                                            <th>Date and Time</th>
				                                        </tr>
			                                    	</thead>
			                                        <tbody>
		                                        		<c:forEach items="${task.expenses}" var="expense">
		                                        			<tr>
		                                        				<td>
		                                        					<c:url value="/task/edit/expense/${expense.id}" var="viewExpenseURL"/>
							                                        <a href="${viewExpenseURL}">
					                                            		<button class="btn btn-default btn-flat btn-sm">View</button>
					                                            	</a>
					                                            	<a href="${contextPath}/task/delete/XXXXX">
					                                            		<button class="btn btn-default btn-flat btn-sm">Delete</button>
					                                            	</a>
		                                        				</td>
					                                            <td>${expense.id}</td>
					                                            <td>${expense.name}</td>
					                                            <td>${expense.description}</td>
					                                            <td>${expense.value}</td>
					                                            <td>${expense.datetime}</td>
					                                        </tr>
		                                        		</c:forEach>
				                                    </tbody>
				                                </table>
			                                </div><!-- /.box-body -->
			                                </div>
                   						</div>
              						</div>
           						</div>
                                <div class="tab-pane" id="tab_assigned_staff">
                                	<div class="box">
		                                <div class="box-body table-responsive">
		                                	<table>
		                                    	<tr>
		                                    		<sec:authorize access="hasRole('ROLE_STAFF_EDITOR')">
		                                    		<td>
		                                    			<c:url var="urlCreateStaff" value="/staff/edit/0/from/task/${task.id}"/>
		                                    			<a href="${urlCreateStaff}">
				                                    	<button class="btn btn-default btn-flat btn-sm">Create Staff</button>
		                                    			</a>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		</sec:authorize>
		                                    		<sec:authorize access="hasRole('ROLE_TASK_EDITOR')">
		                                    		<c:if test="${!empty staffList}">
 		                                    		<form:form 
 		                                    		modelAttribute="staffAssignment"  
 		                                    		method="post" 
 		                                    		action="${contextPath}/task/assign/staff"> 
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
 														<button class="btn btn-default btn-flat btn-sm">Assign</button>
 		                                    			</td> 
 		                                    		</form:form> 
		                                    		</c:if>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:if test="${!empty task.staff}">
		                                    		<td>
               											<c:url var="urlTaskUnassignStaffAll" value="/task/unassign/staff/all"/>
		                                    			<a href="${urlTaskUnassignStaffAll}">
                											<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
		                                    			</a>
		                                    		</td>
		                                    		</c:if>
		                                    		</sec:authorize>
		                                    	</tr>
		                                    </table>
		                                    <table id="staff-table" class="table table-bordered table-striped">
		                                    	<thead>
		                                            <tr>
		                                            	<th>&nbsp;</th>
		                                                <th>Photo</th>
		                                                <th>Full Name</th>
		                                                <th>Position</th>
		                                                <th>E-Mail</th>
		                                                <th>Contact Number</th>
		                                            </tr>
                                        		</thead>
		                                        <tbody>
			                                		<c:forEach items="${task.staff}" var="staffAssign">
			                                            <tr>
			                                            	<td>
			                                            		<center>
			                                            			<c:url var="urlViewStaff" value="/staff/edit/${staffAssign.id}/from/task/${task.id}" />
			                                            			<a href="${urlViewStaff}">
							                                    	<button class="btn btn-default btn-flat btn-sm">View</button>
			                                            			</a>
	                   												<sec:authorize access="hasRole('ROLE_TASK_EDITOR')">
	                   												<c:url var="urlUnassignStaff" value="/task/unassign/staff/${staffAssign.id}"/>
	                   												<a href="${urlUnassignStaff}">
																		<button class="btn btn-default btn-flat btn-sm">Unassign</button>
	                   												</a>
	                   												</sec:authorize>
																</center>
															</td>
			                                                <td>
			                                                	<div class="user-panel">
													            <div class="pull-left image">
													                <c:choose>
		                                                			<c:when test="${!empty staffAssign.thumbnailURL}">
		                                                				<img src="${contextPath}/image/display/staff/profile/?staff_id=${staffAssign.id}" class="img-circle"/>
		                                                			</c:when>
		                                                			<c:when test="${empty staffAssign.thumbnailURL}">
		                                                				<img src="${contextPath}/resources/img/avatar5.png" class="img-circle">
		                                                			</c:when>
			                                                		</c:choose>
													            </div>
														        </div>
			                                                </td>
			                                                <td>${staffAssign.getFullName()}</td>
			                                                <td>${staffAssign.companyPosition}</td>
			                                                <td>${staffAssign.email}</td>
			                                                <td>${staffAssign.contactNumber}</td>
			                                            </tr>
		                                            </c:forEach>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_assigned_teams">
                                	<div class="box">
		                                <div class="box-body table-responsive">
		                                	<table>
		                                    	<tr>
		                                    		<sec:authorize access="hasRole('ROLE_TEAM_EDITOR')">
		                                    		<td>
		                                    			<c:url var="urlCreateTeam" value="/team/edit/0/from/task/${task.id}"/>
		                                    			<a href="${urlCreateTeam}">
				                                    	<button class="btn btn-default btn-flat btn-sm">Create Team</button>
		                                    			</a>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		</sec:authorize>
		                                    		<sec:authorize access="hasRole('ROLE_TASK_EDITOR')">
		                                    		<c:if test="${!empty teamList}">
 		                                    		<form:form 
 		                                    		modelAttribute="teamAssignment"  
 		                                    		method="post" 
 		                                    		action="${contextPath}/task/assign/team"> 
 		                                    			<td>
 		                                    			<form:select class="form-control" path="teamID"> 
                                     						<c:forEach items="${teamList}" var="team"> 
                                     							<form:option value="${team.id}" label="${team.name}"/> 
                                     						</c:forEach> 
 		                                    			</form:select> 
 		                                    			</td>
 		                                    			<td>
 		                                    				&nbsp;
 		                                    			</td>
 														<td>
 														<button class="btn btn-default btn-flat btn-sm">Assign</button>
 		                                    			</td> 
 		                                    		</form:form> 
		                                    		</c:if>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:if test="${!empty task.teams}">
		                                    		<td>
               											<c:url var="urlTaskUnassignTeamAll" value="/task/unassign/team/all"/>
		                                    			<a href="${urlTaskUnassignTeamAll}">
                											<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
		                                    			</a>
		                                    		</td>
		                                    		</c:if>
		                                    		</sec:authorize>
		                                    	</tr>
		                                    </table>
		                                    <table id="teams-table" class="table table-bordered table-striped">
		                                    	<thead>
		                                            <tr>
		                                            	<th>&nbsp;</th>
		                                                <th>#</th>
		                                                <th>Name</th>
		                                            </tr>
                                        		</thead>
		                                        <tbody>
			                                		<c:forEach items="${task.teams}" var="team">
			                                            <tr>
			                                            	<td>
			                                            		<center>
			                                            			<c:url var="urlViewTeam" value="/team/edit/${team.id}/from/task/${task.id}" />
			                                            			<a href="${urlViewTeam}">
							                                    	<button class="btn btn-default btn-flat btn-sm">View</button>
			                                            			</a>
	                   												<sec:authorize access="hasRole('ROLE_TASK_EDITOR')">
	                   												<c:url var="urlUnassignTeam" value="/task/unassign/team/${team.id}"/>
	                   												<a href="${urlUnassignTeam}">
																		<button class="btn btn-default btn-flat btn-sm">Unassign</button>
	                   												</a>
	                   												</sec:authorize>
																</center>
															</td>
			                                                <td>${team.id}</td>
			                                                <td>${team.name}</td>
			                                            </tr>
		                                            </c:forEach>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
	
	<!-- InputMask -->
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.date.extensions.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.extensions.js" type="text/javascript"></script>
	
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
			$("#date-mask").inputmask("yyyy/mm/dd", {"placeholder": "yyyy/mm/dd"});
			$("#task_status").val("${task.status}");
			$("#expenses-table").dataTable();
			$("#staff-table").dataTable();
			$("#teams-table").dataTable();
	    });
	</script>
</body>
</html>