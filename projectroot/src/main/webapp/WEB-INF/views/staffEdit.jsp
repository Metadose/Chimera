<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="staffName" value="${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Staff ${action}</title>
	
	<link href="<c:url value="/resources/css/gantt-custom.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.css" />"rel="stylesheet" type="text/css" />
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
	            	<c:when test="${staff.id == 0}">
	            		New Staff
	            	</c:when>
	            	<c:when test="${staff.id > 0}">
	            		${staffName}
	            	</c:when>
	            	</c:choose>
	                <small>${action} Staff</small>
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
                                <c:if test="${staff.id != 0}">
                                <li><a href="#tab_2" data-toggle="tab">Tasks</a></li>
                                <li><a href="#tab_7" data-toggle="tab">Projects</a></li>
                                <li><a href="#tab_timeline" data-toggle="tab">Timeline</a></li>
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
                   									<c:if test="${staff.id != 0}">
                   									<c:choose>
                   										<c:when test="${!empty staff.thumbnailURL}">
                   											<img src="${contextPath}/image/display/staff/profile/?staff_id=${staff.id}"/>
                   										</c:when>
                   										<c:when test="${empty staff.thumbnailURL}">
                   											No photo uploaded.
                   										</c:when>
                   									</c:choose>
                   									<br/><br/>
                   									<div class="form-group">
                   										<form action="${contextPath}/photo/upload/staff/profile" method="post" enctype="multipart/form-data">	
                   											<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	                										<input type="hidden" value="${staff.id}" id="staff_id" name="staff_id"/>
	                   										<table>
	                   											<tr>
	                   												<td>
	                   													<label for="exampleInputFile">Update Photo</label>
	                   												</td>
	                   												<td>
	                   													&nbsp;&nbsp;
	                   												</td>
	                   												<td>
	                   													<input type="file" id="file" name="file"/>
	                   												</td>
	                   											</tr>
	                   										</table>
	                   										<br/>
					                                        <button class="btn btn-default btn-flat btn-sm">Upload</button>
					                                        <button class="btn btn-default btn-flat btn-sm">Delete</button>
				                                        </form>
                   									</div>
				                                    <br/>
				                                    </c:if>
				                                    <c:set var="detailsFormURL" value="${contextPath}/staff/create"/>
				                                    <c:if test="${!empty origin && !empty originID}">
				                                    	<c:set var="detailsFormURL" value="${contextPath}/staff/create/from/${origin}/${originID}"/>
				                                    </c:if>
                   									<form:form modelAttribute="staff" id="detailsForm" method="post" action="${detailsFormURL}">
				                                        <div class="form-group">
				                                            <label>Prefix</label>
				                                            <form:input type="text" class="form-control" path="prefix"/><br/>
				                                            
				                                            <label>First</label>
				                                            <form:input type="text" class="form-control" path="firstName"/><br/>
				                                            
				                                            <label>Middle</label>
				                                            <form:input type="text" class="form-control" path="middleName"/><br/>
				                                            
				                                            <label>Last</label>
				                                            <form:input type="text" class="form-control" path="lastName"/><br/>
				                                            
				                                            <label>Suffix</label>
				                                            <form:input type="text" class="form-control" path="suffix"/><br/>
				                                            
				                                            <label>Position</label>
				                                            <form:input type="text" class="form-control" path="companyPosition"/><br/>
				                                            
				                                            <label>E-Mail</label>
				                                            <form:input type="text" class="form-control" path="email"/><br/>
				                                            
				                                            <label>Contact Number</label>
				                                            <form:input type="text" class="form-control" path="contactNumber"/><br/>
				                                        </div>
				                                    </form:form>
				                                    <c:choose>
		                                            	<c:when test="${staff.id == 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${staff.id > 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<form:form action="${contextPath}/staff/delete" method="post">
																<button class="btn btn-default btn-flat btn-sm">Delete This Staff</button>
		                                            		</form:form>
		                                            	</c:when>
		                                            </c:choose>
                   								</div>
                   							</div>
                   						</div>
                   						<c:if test="${staff.id != 0}">
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">More Info</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="form-group">
                   										<table>
                   											<c:set var="fields" value="${staff.fieldAssignments}"/>
                   											<c:if test="${!empty fields}">
                   												<c:set var="fieldFormID" value="${0}"/>
                   												<c:forEach var="field" items="${fields}">
                   													<tr>
	                   													<form id="field_unassign_${fieldFormID}" method="post" action="${contextPath}/field/unassign/staff">
	                   														<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
																			<input type="hidden" name="staff_id" value="${staff.id}"/>
																			<input type="hidden" name="field_id" value="${field.field.id}"/>
																			<input type="hidden" id="old_label" name="old_label" value="${field.label}"/>
																			<input type="hidden" id="old_value" name="old_value" value="${field.value}"/>
																			<td style="padding-bottom: 3px;">
																				<input type="text" class="form-control" id="label" name="label" value="${field.label}">
																			</td>
																			<td style="padding-bottom: 3px;">
																				&nbsp;
																			</td>
																			<td style="padding-bottom: 3px;">
																				<input type="text" class="form-control" id="value" name="value" value="${field.value}">
																			</td>
																			<td style="padding-bottom: 3px;">
																				&nbsp;
																			</td>
																		</form>
																		<td style="padding-bottom: 3px;">
																			<button class="btn btn-default btn-flat btn-sm" onclick="submitAjax('field_unassign_${fieldFormID}')">Update</button>
																		</td>
																		<td style="padding-bottom: 3px;">
																			&nbsp;
																		</td>
																		<td style="padding-bottom: 3px;">
																			<button class="btn btn-default btn-flat btn-sm" onclick="submitForm('field_unassign_${fieldFormID}')">Unassign</button>
																		</td>
																	</tr>
																	<c:set var="fieldFormID" value="${fieldFormID + 1}"/>
																</c:forEach>
															</c:if>
														</table>
														<br/>
														<c:choose>
														<c:when test="${!empty fields}">
															<form method="post" action="${contextPath}/field/unassign/staff/all">
																<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
																<input type="hidden" name="staff_id" value="${staff.id}"/>
																<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
															</form>
														</c:when>
														<c:when test="${empty fields}">
															<h5>No field assigned.</h5>
														</c:when>
														</c:choose>
														<br/>
														<br/>
														<h4>Assign Fields</h4>
														<form role="form" name="fieldsForm" id="fieldsForm" method="post" action="${contextPath}/field/assign/staff">
															<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
															<input type="hidden" name="staff_id" value="${staff.id}"/>
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
																		<input type="text" name="label" id="label" class="form-control" placeholder="Example: SSS, Building Permit No., Sub-contractor, etc...">
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
																		<input type="text" name="value" id="value" class="form-control" placeholder="Example: 000-123-456, AEE-123, OneForce Construction, etc...">
																	</td>
																</tr>
															</table>
															<br/>
															<button class="btn btn-default btn-flat btn-sm">Assign</button>
														</form>
			                                        </div>
                   								</div>
                   							</div>
                   						</div>
                   						</c:if>
              						</div>
              						<c:if test="${staff.id != 0}">
              						<h2 class="page-header">Assignments</h2>
              						<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Teams</h3>
                   								</div>
                   								<div class="box-body">
                   									<table>
                   										<c:choose>
               											<c:when test="${!empty staff.teams}">
               												<c:forEach items="${staff.teams}" var="team">
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
	                   												<form method="post" action="${contextPath}/staff/unassign/team">
	                   													<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	                   													<input type="hidden" id="staff_id" name="staff_id" value="${staff.id}"/>
	                   													<input type="hidden" id="team_id" name="team_id" value="${team.id}"/>
	                   													<button class="btn btn-default btn-flat btn-sm" style="padding: 3px; margin-bottom: 3px">Unassign</button>
	                   												</form>
	                   												<a href="${contextPath}/team/edit/${team.id}">
	                   													<button class="btn btn-default btn-flat btn-sm" style="padding: 3px; margin-bottom: 3px">View Team</button>
	                   												</a>
	                   											</td>
		                   										</tr>
               												</c:forEach>
               											</c:when>
               											</c:choose>
                   									</table>
                   									<c:choose>
                   										<c:when test="${!empty staff.teams}">
                   											<form method="post" action="${contextPath}/staff/unassign/team/all">
                   												<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                   												<input type="hidden" id="staff_id" name="staff_id" value="${staff.id}"/>
                   												<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
                   											</form>
                   										</c:when>
                   										<c:when test="${empty staff.teams}">
                   											<h5>No team assigned.</h5>
                   										</c:when>
                   									</c:choose>
													<br/>
													<br/>
													<h4>Assign Teams&nbsp;
													<a href="${contextPath}/team/edit/0">
                   										<button class="btn btn-default btn-flat btn-sm" style="padding: 3px; margin-bottom: 3px">Create Team</button>
                   									</a>
													</h4>
													<form method="post" action="${contextPath}/staff/assign/team">
													<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
													<input type="hidden" id="staff_id" name="staff_id" value="${staff.id}"/>
													<table>
														<tr>
															<td style="padding-right: 3px;">
																<label>Teams </label>
															</td>
															<td style="padding-bottom: 3px;">
																&nbsp;
															</td>
															<td style="padding-bottom: 3px;">
																<select id="team_id" name="team_id" class="form-control">
																	<c:if test="${!empty teamList}">
																		<c:forEach items="${teamList}" var="team">
																			<option value="${team.id}">${team.name}</option>
																		</c:forEach>
																	</c:if>
					                                            </select>
															</td>
														</tr>
													</table>
													<br/>
													<button class="btn btn-default btn-flat btn-sm">Assign</button>
													</form>
                   								</div>
                   							</div>
                   						</div>
               						</div>
               						</c:if>
                                </div><!-- /.tab-pane -->
                                <c:if test="${staff.id != 0}">
                                <div class="tab-pane" id="tab_7">
                                	<div class="box">
		                                <div class="box-body table-responsive">
		                                	<a href="${contextPath}/project/edit/0">
		                                		<button class="btn btn-default btn-flat btn-sm">Create Project</button>
		                                	</a><br/><br/>
		                                    <table id="project-table" class="table table-bordered table-striped">
		                                    	<thead>
		                                    		<tr>
		                                            	<th>&nbsp;</th>
		                                            	<th>Status</th>
		                                                <th>Project</th>
		                                                <th>Location</th>
		                                                <th>Notes</th>
		                                            </tr>
		                                    	</thead>
		                                        <tbody>
			                                        <c:set var="assignmentList" value="${staff.assignedManagers}"/>
				                                	<c:if test="${!empty assignmentList}">
				                                		<c:forEach items="${assignmentList}" var="projectAssignment">
				                                		<c:set var="project" value="${projectAssignment.project}"/>	
			                                            <tr>
		                                            	<td>
		                                            		<center>
																<form action="${contextPath}/project/edit/${project.id}" method="post">
																	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
																	<button class="btn btn-default btn-flat btn-sm">View</button>
																</form>&nbsp;
																<form action="${contextPath}/project/delete/${project.id}" method="post">
																	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
																	<button class="btn btn-default btn-flat btn-sm">Delete</button>
																</form>
															</center>
														</td>
														<td>
		                                                	<c:choose>
				                                            	<c:when test="${project.status == 0}">
				                                            		<span class="label label-info">New</span>
				                                            	</c:when>
				                                            	<c:when test="${project.status == 1}">
				                                            		<span class="label label-primary">Ongoing</span>
				                                            	</c:when>
				                                            	<c:when test="${project.status == 2}">
				                                            		<span class="label label-success">Completed</span>
				                                            	</c:when>
				                                            	<c:when test="${project.status == 3}">
				                                            		<span class="label label-danger">Failed</span>
				                                            	</c:when>
				                                            	<c:when test="${project.status == 4}">
				                                            		<span class="label label">Cancelled</span>
				                                            	</c:when>
				                                            </c:choose>
		                                                </td>
		                                                <td>
				                                            ${project.name}<br/><br/>
		                                                	<c:choose>
		                                                		<c:when test="${!empty project.thumbnailURL}">
		                                                			<img style="width: 100%" src="${contextPath}/image/display/project/profile/?project_id=${project.id}"/>
		                                                		</c:when>
		                                                		<c:when test="${empty project.thumbnailURL}">
		                                                			<h5>No photo uploaded.</h5>
		                                                		</c:when>
		                                                	</c:choose>
		                                                </td>
		                                                <td>${project.location}</td>
		                                                <td>${project.notes}</td>
		                                            </tr>
		                                            </c:forEach>
	                                        		</c:if>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_2">
                                	<div class="box">
		                                <div class="box-body table-responsive">	
		                               		<a href="${contextPath}/task/assign/staff/${staff.id}">
		                                		<button class="btn btn-default btn-flat btn-sm">Create Task</button>
		                                	</a><br/><br/>
		                                    <table id="task-table" class="table table-bordered table-striped">
		                                    	<thead>
		                                    		<tr>
			                                        	<th>&nbsp;</th>
			                                            <th>Status</th>
			                                            <th>Content</th>
			                                            <th>Project</th>
			                                            <th>Team</th>
			                                            <th>Start</th>
			                                            <th>Duration</th>
			                                        </tr>
		                                    	</thead>
		                                        <tbody>
			                                        <c:set var="taskList" value="${staff.tasks}"/>
				                                	<c:if test="${!empty taskList}">
		                                        		<c:forEach items="${taskList}" var="task">
		                                        			<tr>
		                                        				<td>
		                                        					<div class="btn-group">
							                                            <button type="button" class="btn btn-default btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">
							                                                Mark As&nbsp;
							                                                <span class="caret"></span>
							                                            </button>
							                                            <ul class="dropdown-menu">
							                                                <li><a href="${contextPath}/task/mark/staff/?staff_id=${staff.id}&task_id=${task.id}&status=0">New</a></li>
							                                                <li><a href="${contextPath}/task/mark/staff/?staff_id=${staff.id}&task_id=${task.id}&status=1">Ongoing</a></li>
							                                                <li><a href="${contextPath}/task/mark/staff/?staff_id=${staff.id}&task_id=${task.id}&status=2">Completed</a></li>
							                                                <li><a href="${contextPath}/task/mark/staff/?staff_id=${staff.id}&task_id=${task.id}&status=3">Failed</a></li>
							                                                <li><a href="${contextPath}/task/mark/staff/?staff_id=${staff.id}&task_id=${task.id}&status=4">Cancelled</a></li>
							                                            </ul>
							                                        </div>
							                                        <a href="${contextPath}/task/edit/${task.id}">
					                                            		<button class="btn btn-default btn-flat btn-sm">View</button>
					                                            	</a>
					                                            	<a href="${contextPath}/task/delete/${task.id}">
					                                            		<button class="btn btn-default btn-flat btn-sm">Delete</button>
					                                            	</a>
		                                        				</td>
					                                            <td style="vertical-align: middle;">
					                                            	<c:choose>
					                                            	<c:when test="${task.status == 0}">
					                                            		<span class="label label-info">New</span>
					                                            	</c:when>
					                                            	<c:when test="${task.status == 1}">
					                                            		<span class="label label-primary">Ongoing</span>
					                                            	</c:when>
					                                            	<c:when test="${task.status == 2}">
					                                            		<span class="label label-success">Completed</span>
					                                            	</c:when>
					                                            	<c:when test="${task.status == 3}">
					                                            		<span class="label label-danger">Failed</span>
					                                            	</c:when>
					                                            	<c:when test="${task.status == 4}">
					                                            		<h6>Cancelled</h6>
					                                            	</c:when>
						                                            </c:choose>
					                                            </td>
					                                            <td>${task.content}</td>
					                                            <td>
					                                            	<c:choose>
				                                            		<c:when test="${!empty task.project}">
				                                            			<a href="${contextPath}/project/edit/from/staff/?${task.project.id}">
						                                            		<button class="btn btn-default btn-flat btn-sm">View</button>&nbsp;&nbsp;
						                                            	</a>
						                                            	${task.project.name}
						                                            	<br/>
				                                            		</c:when>
				                                            		<c:when test="${empty task.project}">
				                                            			<h5>No project assigned.</h5>
				                                            		</c:when>
					                                            	</c:choose>					                                            
					                                            </td>
					                                            <td>
					                                            	<c:choose>
				                                            		<c:when test="${!empty task.teams}">
				                                            			<c:forEach items="${task.teams}" var="taskTeam">
				                                            			<a href="${contextPath}/team/edit/${taskTeam.id}">
						                                            		<button class="btn btn-default btn-flat btn-sm">View</button>&nbsp;&nbsp;
						                                            	</a>
						                                            	${taskTeam.name}
						                                            	<br/>
				                                            			</c:forEach>
				                                            		</c:when>
				                                            		<c:when test="${empty task.teams}">
				                                            			<h5>No team assigned.</h5>
				                                            		</c:when>
					                                            	</c:choose>
					                                            </td>
					                                            <td>${task.dateStart}</td>
					                                            <td>${task.duration}</td>
					                                        </tr>
		                                        		</c:forEach>
	                                        		</c:if>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_timeline">
                                	<div class="box box-default">
		                                <div class="box-body">
		                                <div id="gantt-chart" style='width:1000px; height:400px;'>
<!-- 		                                <div id="gantt-chart" class="box-body table-responsive"> -->
		                                </div><!-- /.box-body -->
		                                </div>
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
	
	<script src="<c:url value="/resources/js/common.js" />"type="text/javascript"></script>
	
	<c:if test="${staff.id != 0}">
	<!-- Generate the data to be used by the gantt. -->
	<c:set var="ganttData" value="'data':[{id:'${staff.id}', text:'${fn:escapeXml(staffName)}', open: true, duration:0},"/>
    <c:if test="${!empty staff.tasks}">
    	<c:forEach var="task" items="${staff.tasks}">
    		<fmt:formatDate pattern="dd-MM-yyyy" value="${task.dateStart}" var="taskDateStart"/>
    		<c:set var="taskRow" value="{id:'${task.id}', status:${task.status}, text:'${fn:escapeXml(task.title)}', content:'${fn:escapeXml(task.content)}', start_date:'${taskDateStart}', open: true, duration:${task.duration}, parent:'${staff.id}'},"/>
    		<c:set var="ganttData" value="${ganttData}${taskRow}"/>
    	</c:forEach>
    	<c:set var="ganttData" value="${fn:substring(ganttData, 0, fn:length(ganttData)-1)}"/>
    </c:if>
    <c:set var="ganttEnd" value="]"/>
   	<c:set var="ganttData" value="{${ganttData}${ganttEnd}}"/>
   	<script src="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/js/gantt-custom.js" />"type="text/javascript"></script>
	
	<script type="text/javascript">
	    var tasks = ${ganttData};
		gantt.init("gantt-chart");
	    gantt.parse(tasks);
	</script>
   	</c:if>
   	
	<script>
		function submitForm(id) {
			$('#'+id).submit();
		}
		
		function submitAjax(id) {
			var formObj = $('#'+id);
			var serializedData = formObj.serialize();
			$.ajax({
				type: "POST",
				url: '${contextPath}/field/update/assigned/staff',
				data: serializedData,
				success: function(response){
					location.reload();
				}
			});
		}
		
		$(document).ready(function() {
			$("#example-1").dataTable();
			$("#project-table").dataTable();
			$("#task-table").dataTable();
	    });
	</script>
</body>
</html>