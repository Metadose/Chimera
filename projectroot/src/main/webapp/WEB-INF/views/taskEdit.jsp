<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Task ${action}</title>
	<c:import url="/resources/css-includes.jsp" />
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
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<h2 class="page-header">Information</h2>
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-primary">
                   								<div class="box-header">
                   									<h3 class="box-title">Details</h3>
                   								</div>
                   								<div class="box-body">
                   									<c:set var="targetController" value="create"/>
                   									<c:choose>
                   										<c:when test="${!empty assignProjectID}">
                   											<c:set var="targetController" value="assign/project/${assignProjectID}"/>
                   										</c:when>
                   										<c:when test="${!empty assignStaffID}">
                   											<c:set var="targetController" value="assign/new/staff/${assignStaffID}"/>
                   										</c:when>
                   									</c:choose>
                   									<form role="form" name="detailsForm" id="detailsForm" method="post" action="${contextPath}/task/${targetController}">
				                                        <div class="form-group">
				                                        	<input type="hidden" name="id" value="${task.id}"/>
				                                        	<input type="hidden" name="project_id" value="${task.project.id}"/>
				                                        	<label>Status</label>
				                                            <select class="form-control" id="task_status" name="status">
						                                    	<option value="0">New</option>
						                                    	<option value="1">Ongoing</option>
						                                    	<option value="2">Completed</option>
						                                    	<option value="3">Failed</option>
						                                    	<option value="4">Cancelled</option>
				                                            </select><br/>
				                                            <label>Content</label>
				                                            <input type="text" class="form-control" name="content" value="${task.content}"/><br/>
				                                            
				                                            <label>Start</label>
					                                        <div class="input-group">
					                                            <div class="input-group-addon">
					                                                <i class="fa fa-calendar"></i>
					                                            </div>
					                                            <input type="text" id="date-mask" class="form-control" name="dateStart" data-inputmask="'alias': 'yyyy/mm/dd'" data-mask" value="${task.dateStart}"/>
					                                        </div>
					                                        <label>End</label>
					                                        <div class="input-group">
					                                            <div class="input-group-addon">
					                                                <i class="fa fa-calendar"></i>
					                                            </div>
					                                            <input type="text" id="date-mask2" class="form-control" name="dateEnd" data-inputmask="'alias': 'yyyy/mm/dd'" data-mask value="${task.dateEnd}"/>
					                                        </div>
				                                        </div>
				                                    </form>
				                                    <c:choose>
		                                            	<c:when test="${task.id == 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${task.id > 0}">
		                                            		<button class="btn btn-warning btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<a href="${contextPath}/task/delete/${task.id}">
																<button class="btn btn-danger btn-sm">Delete This Task</button>
															</a>
		                                            	</c:when>
		                                            </c:choose>
                   								</div>
                   							</div>
                   						</div>
                   						<c:if test="${task.id > 0}">
                   						<div class="col-md-6">
                   							<div class="box box-primary">
                   								<div class="box-header">
                   									<h3 class="box-title">Fields</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="form-group">
                   										<c:choose>
               											<c:when test="${!empty task.fields}">
               												<c:set var="fieldFormID" value="${0}"/>
               												<c:forEach items="${task.fields}" var="field">
               												<table>
																<tr>
																	<form id="field_unassign_${fieldFormID}" action="${contextPath}/field/unassign/task" method="post">
		               												<input type="hidden" name="task_id" value="${task.id}"/>
		               												<input type="hidden" name="field_id" value="${field.field.id}"/>
		               												<input type="hidden" class="form-control" name="old_label" value="${field.label}">
		               												<input type="hidden" class="form-control" name="old_value" value="${field.value}">
																	<td style="padding-bottom: 3px;">
																		<input type="text" class="form-control" name="label" value="${field.label}">
																	</td>
																	<td style="padding-bottom: 3px;">
																		&nbsp;
																	</td>
																	<td style="padding-bottom: 3px;">
																		<input type="text" class="form-control" name="value" value="${field.value}">
																	</td>
																	<td style="padding-bottom: 3px;">
																		&nbsp;
																	</td>
																	</form>
																	<td style="padding-bottom: 3px;">
																		<button class="btn btn-warning btn-sm" onclick="submitAjax('field_unassign_${fieldFormID}')">Update</button>
																	</td>
																	<td style="padding-bottom: 3px;">
																		&nbsp;
																	</td>
																	<td style="padding-bottom: 3px;">
																		<button class="btn btn-danger btn-sm" onclick="submitForm('field_unassign_${fieldFormID}')">Unassign</button>
																	</td>
																</tr>
															</table>
															<c:set var="fieldFormID" value="${fieldFormID + 1}"/>
															</c:forEach>
															<br/>
															<form action="${contextPath}/field/unassign/task/all" method="post">
																<input type="hidden" name="task_id" value="${task.id}"/>
																<button class="btn btn-danger btn-sm">Unassign All</button>
															</form>
               											</c:when>
               											<c:when test="${empty task.fields}">
               												<h6>No field assigned.</h6>
               											</c:when>
                   										</c:choose>
														<br/>
														<br/>
														<h4>Assign Fields</h4>
														<form action="${contextPath}/field/assign/task" method="post">
														<input type="hidden" name="task_id" value="${task.id}"/>
														<table>
															<tr>
																<td style="padding-right: 3px;">
																	<label>Field Type </label>
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<select class="form-control" id="field_id" name="field_id">
																		<c:if test="${!empty fieldList}">
																			<c:forEach items="${fieldList}" var="field">
								                                                <option value="${field.id}">${field.name}</option>
							                                                </c:forEach>
						                                                </c:if>
						                                            </select>
																</td>
															</tr>
															<tr>
																<td style="padding-right: 3px;">
																	<label>Label</label>
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<input type="text" id="label" name="label" class="form-control" placeholder="Example: SSS, Building Permit No., Sub-contractor, etc...">
																</td>
															</tr>
															<tr>
																<td style="padding-right: 3px;">
																	<label>Value</label>
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<input type="text" id="value" name="value" class="form-control" placeholder="Example: 000-123-456, AEE-123, OneForce Construction, etc...">
																</td>
															</tr>
														</table>
                                            			<button class="btn btn-primary btn-sm">Assign</button>
                                            			</form>
			                                        </div>
                   								</div>
                   							</div>
                   						</div>
                   						</c:if>
              						</div>
              						<c:if test="${task.id > 0}">
              						<h2 class="page-header">Assignments</h2>
              						<div class="row">
              							<div class="col-md-6">
                   							<div class="box box-primary">
                   								<div class="box-header">
                   									<h3 class="box-title">Staff</h3>
                   								</div>
                   								<div class="box-body">
                   									<table>
                   										<c:set value="${task.staff}" var="staffAssignments"/>
                   										<c:forEach items="${staffAssignments}" var="staff">
	                                                		<c:set var="staffName" value="${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}"/>
		                                                	<tr>
	                   											<td>
	                   												<div class="user-panel">
															            <div class="pull-left image">
															                <c:choose>
				                                                			<c:when test="${!empty staff.thumbnailURL}">
				                                                				<img src="${contextPath}/image/display/staff/profile/?staff_id=${staff.id}" class="img-circle"/>
				                                                			</c:when>
				                                                			<c:when test="${empty staff.thumbnailURL}">
				                                                				<img src="/pmsys/resources/img/avatar5.png" class="img-circle">
				                                                			</c:when>
					                                                		</c:choose>
															            </div>
															            <div class="pull-left info">
															                <p>${staffName}</p>
															                <h6>${staff.companyPosition}</h6>
															                <h6>${staff.email}</h6>
															                <h6>${staff.contactNumber}</h6>
															            </div>
															        </div>
	                   											</td>
	                   											<td style="padding-right: 5px">
	                   												&nbsp;
	                   											</td>
	                   											<td>
	                   												<form method="post" action="${contextPath}/task/unassign/staff">
																		<input type="hidden" id="task_id" name="task_id" value="${task.id}"/>
																		<input type="hidden" id="staff_id" name="staff_id" value="${staff.id}"/>
																		<button class="btn btn-danger btn-sm" style="padding: 3px; margin-bottom: 3px">Unassign</button>
	                   												</form>
	                   												<form action="${contextPath}/staff/edit/from/project" method="post">
	                   													<input type="hidden" name="staff_id" value="${staff.id}"/>
	                   													<input type="hidden" name="project_id" value="${staff.id}"/>
	                   													<button class="btn btn-info btn-sm" style="padding: 3px; margin-bottom: 3px">View Staff</button>
	                   												</form>
	                   											</td>
	                   										</tr>
	                                                	</c:forEach>
                   									</table>
                   									<c:choose>
                   										<c:when test="${!empty task.staff}">
                   											<form method="post" action="${contextPath}/task/unassign/staff/all">
                   												<input type="hidden" name="task_id" value="${task.id}"/>
                   												<button class="btn btn-danger btn-sm">Unassign All</button>
                   											</form>
                   										</c:when>
                   										<c:when test="${empty task.staff}">
                   											<h5>No manager assigned.</h5>
                   										</c:when>
                   									</c:choose>
													<br/>
													<br/>
													<h4>Assign Staff&nbsp;
													<a href="${contextPath}/staff/edit/from/project/?0">
				              							<button class="btn btn-default btn-flat btn-sm" style="padding: 3px; margin-bottom: 3px">Create Staff</button>
				              						</a>
													</h4>
													<form method="post" action="${contextPath}/task/assign/staff">
														<input type="hidden" name="task_id" value="${task.id}"/>
														<table>
															<tr>
																<td style="padding-right: 3px;">
																	<label>Staff </label>
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<select class="form-control" id="staff_id" name="staff_id">
																		<c:if test="${!empty staffList}">
																			<c:forEach items="${staffList}" var="staff">
																				<c:set var="staffName" value="${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}"/>
																				<option value="${staff.id}">${staffName}</option>
																			</c:forEach>
																		</c:if>
						                                            </select>
																</td>
															</tr>
														</table>
														<br/>
                                           				<button class="btn btn-primary btn-sm">Assign</button>
                                           			</form>
                   								</div>
                   							</div>
                   						</div>
                   						<div class="col-md-6">
                   							<div class="box box-primary">
                   								<div class="box-header">
                   									<h3 class="box-title">Teams</h3>
                   								</div>
                   								<div class="box-body">
                   									<table>
                   										<c:choose>
                  										<c:when test="${!empty task.teams}">
                  											<c:forEach items="${task.teams}" var="team">
                  												<tr style="padding-bottom: 5px">
		                   											<td>
		                   												<div class="user-panel">
		                   													<div class="pull-left info">
																                <p>${team.name}</p>
																                <h6>Maya Villanueva</h6>
																                <h6>(+63) 922 062 2345</h6>
																                <h6>5 Members</h6>
																            </div>
		                   												</div>
		                   											</td>
		                   											<td style="padding-right: 50px">
		                   												&nbsp;
		                   											</td>
		                   											<td>
		                   												<form action="${contextPath}/task/unassign/team" method="post">
		                   													<input type="hidden" name="task_id" value="${task.id}"/>
		                   													<input type="hidden" name="team_id" value="${team.id}"/>
		                   													<button class="btn btn-danger btn-sm" style="padding: 3px; margin-bottom: 3px">Unassign</button>
		                   												</form>
		                   												<form action="${contextPath}/team/edit/${team.id}" method="post">
		                   													<button class="btn btn-info btn-sm" style="padding: 3px; margin-bottom: 3px">View Team</button>
		                   												</form>
		                   											</td>
		                   										</tr>
                  											</c:forEach>
                  										</c:when>
                  										<c:when test="${empty task.teams}">
                  											<h6>No team assigned.</h6>
                  										</c:when>
                   										</c:choose>
                   									</table>
                   									<c:if test="${!empty task.teams}">
                   									<form action="${contextPath}/task/unassign/team/all" method="post">
                   										<input type="hidden" name="task_id" value="${task.id}"/>
                   										<button class="btn btn-danger btn-sm">Unassign All</button>
                   									</form>
                   									</c:if>
													<br/>
													<br/>
													<h4>Assign More Teams</h4>
													<form action="${contextPath}/task/assign/team" method="post">
													<table>
														<tr>
															<td style="padding-right: 3px;">
																<label>Tasks </label>
															</td>
															<td style="padding-bottom: 3px;">
																&nbsp;
															</td>
															<td style="padding-bottom: 3px;">
																<select class="form-control" name="team_id">
																	<c:forEach items="${teamList}" var="teamItem">
																		<option value="${teamItem.id}">${teamItem.name}</option>
																	</c:forEach>
					                                            </select>
															</td>
														</tr>
													</table>
													<input type="hidden" name="task_id" value="${task.id}"/>
                                           			<button class="btn btn-primary btn-sm">Assign</button>
                                           			</form>
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
	<c:import url="/resources/js-includes.jsp" />
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
			$("#date-mask2").inputmask("yyyy/mm/dd", {"placeholder": "yyyy/mm/dd"});
			$("#task_status").val("${task.status}");
	    });
	</script>
</body>
</html>