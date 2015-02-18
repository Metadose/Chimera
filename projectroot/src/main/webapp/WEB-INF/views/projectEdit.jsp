<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Project ${action}</title>
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
	                ${project.name}
	                <small>${action} Project</small>
	            </h1>
	            <ol class="breadcrumb">
	                <li><a href="${contextPath}/dashboard/">Home</a></li>
	                <li><a href="${contextPath}/project/list">Projects</a></li>
	                <li class="active">${project.name}</li>
	            </ol>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                                <li><a href="#tab_2" data-toggle="tab">Tasks</a></li>
                                <li><a href="#tab_6" data-toggle="tab">Calendar</a></li>
                                <li><a href="#tab_5" data-toggle="tab">Timeline</a></li>
                                <li><a href="#tab_3" data-toggle="tab">Files</a></li>
                                <li><a href="#tab_4" data-toggle="tab">Photos</a></li>
                                <li><a href="#tab_7" data-toggle="tab">Map</a></li>
<!--                                 <li class="dropdown"> -->
<!--                                     <a class="dropdown-toggle" data-toggle="dropdown" href="#"> -->
<!--                                         Dropdown <span class="caret"></span> -->
<!--                                     </a> -->
<!--                                     <ul class="dropdown-menu"> -->
<!--                                         <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Action</a></li> -->
<!--                                         <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Another action</a></li> -->
<!--                                         <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Something else here</a></li> -->
<!--                                         <li role="presentation" class="divider"></li> -->
<!--                                         <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Separated link</a></li> -->
<!--                                     </ul> -->
<!--                                 </li> -->
<!--                                 <li class="pull-right"><a href="#" class="text-muted"><i class="fa fa-gear"></i></a></li> -->
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
                   									[Photo]
                   									<div class="form-group">
                   										<table>
                   											<tr>
                   												<td>
                   													<label for="exampleInputFile">Update Photo</label>
                   												</td>
                   												<td>
                   													&nbsp;&nbsp;
                   												</td>
                   												<td>
                   													<input type="file" id="exampleInputFile">
                   												</td>
                   											</tr>
                   										</table>
                   										<br/>
				                                        <button class="btn btn-warning btn-sm">Upload</button>
				                                        <button class="btn btn-danger btn-sm">Delete Photo</button>
				                                    </div>
				                                    <br/>
                   									<form role="form" name="detailsForm" id="detailsForm" method="post" action="${contextPath}/project/create">
				                                        <div class="form-group">
				                                        	<input type="hidden" name="id" value="${project.id}"/>
				                                            <label>Name</label>
				                                            <input type="text" class="form-control" name="name" value="${project.name}"/><br/>
				                                            <label>Status</label>
				                                            <input type="text" class="form-control" name="status" value="${project.status}"/><br/>
				                                            <label>Location</label>
				                                            <input type="text" class="form-control" name="location" value="${project.location}"/><br/>
				                                            <label>Notes</label>
				                                            <input type="text" class="form-control" name="notes" value="${project.notes}"/><br/>
				                                        </div>
				                                    </form>
				                                    <c:choose>
		                                            	<c:when test="${project.id == 0}">
		                                            		<button class="btn btn-success btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${project.id > 0}">
		                                            		<button class="btn btn-warning btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<a href="${contextPath}/project/delete/${project.id}">
																<button class="btn btn-danger btn-sm">Delete This Project</button>
															</a>
		                                            	</c:when>
		                                            </c:choose>
                   								</div>
                   							</div>
                   						</div>
                   						<div class="col-md-6">
                   							<div class="box box-primary">
                   								<div class="box-header">
                   									<h3 class="box-title">Info</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="form-group">
                   										<table>
                   											<c:set var="projectFields" value="${project.assignedFields}"/>
                   											<c:if test="${!empty projectFields}">
                   												<c:set var="fieldFormID" value="${0}"/>
                   												<c:forEach var="field" items="${projectFields}">
                   													<tr>
	                   													<form role="form" name="field_unassign_${fieldFormID}" id="field_unassign_${fieldFormID}" method="post" action="${contextPath}/field/unassign/project">
																			<input type="hidden" name="project_id" value="${project.id}"/>
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
																			<button class="btn btn-warning btn-sm" onclick="submitAjax('field_unassign_${fieldFormID}')">Update</button>
																		</td>
																		<td style="padding-bottom: 3px;">
																			&nbsp;
																		</td>
																		<td style="padding-bottom: 3px;">
																			<button class="btn btn-danger btn-sm" onclick="submitForm('field_unassign_${fieldFormID}')">Unassign</button>
																		</td>
																	</tr>
																	<c:set var="fieldFormID" value="${fieldFormID + 1}"/>
																</c:forEach>
															</c:if>
														</table>
														<br/>
														<c:choose>
															<c:when test="${!empty projectFields}">
																<form role="form" name="fieldsUnassignForm" id="fieldsUnassignForm" method="post" action="${contextPath}/field/unassign/project/all">
																	<input type="hidden" name="project_id" value="${project.id}"/>
																	<button class="btn btn-danger btn-sm">Unassign All</button>
																</form>
															</c:when>
															<c:when test="${empty projectFields}">
																<h5>No field assigned.</h5>
															</c:when>
														</c:choose>
														<br/>
														<br/>
														<h4>Assign Fields</h4>
														<form role="form" name="fieldsForm" id="fieldsForm" method="post" action="${contextPath}/field/assign/project">
															<input type="hidden" name="project_id" value="${project.id}"/>
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
														</form>
														<br/>
                                           				<button class="btn btn-primary btn-sm" onclick="submitForm('fieldsForm')">Assign</button>
			                                        </div>
                   								</div>
                   							</div>
                   						</div>
              						</div>
              						<h2 class="page-header">Staff</h2>
              						<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-primary">
                   								<div class="box-header">
                   									<h3 class="box-title">Managers</h3>
                   								</div>
                   								<div class="box-body">
                   									<table>
                   										<c:set value="${project.managerAssignments}" var="managerAssignments"/>
                   										<c:forEach items="${managerAssignments}" var="managerAssignment">
	                                                		<c:set var="staff" value="${managerAssignment.manager}"/>
	                                                		<c:set var="manName" value="${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}"/>
		                                                	<tr>
	                   											<td>
	                   												<div class="user-panel">
															            <div class="pull-left image">
															                <img src="/pmsys/resources/img/avatar2.png" class="img-circle" alt="User Image">
															            </div>
															            <div class="pull-left info">
															            	${staff.thumbnailURL}
															                <p>${manName}</p>
															                <h6>${managerAssignment.projectPosition}</h6>
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
	                   												<button class="btn btn-danger btn-sm" style="padding: 3px; margin-bottom: 3px">Unassign</button>
	                   												<a href="${contextPath}/staff/edit/${staff.id}">
	                   													<button class="btn btn-info btn-sm" style="padding: 3px; margin-bottom: 3px">View Staff</button>
	                   												</a>
	                   											</td>
	                   										</tr>
	                                                	</c:forEach>
                   									</table>
                   									<c:choose>
                   										<c:when test="${!empty managerAssignments}">
                   											<button class="btn btn-danger btn-sm">Unassign All</button>
                   										</c:when>
                   										<c:when test="${empty managerAssignments}">
                   											<h5>No manager assigned.</h5>
                   										</c:when>
                   									</c:choose>
													<br/>
													<br/>
													<h4>Assign Managers</h4>
													<table>
														<tr>
															<td style="padding-right: 3px;">
																<label>Staff </label>
															</td>
															<td style="padding-bottom: 3px;">
																&nbsp;
															</td>
															<td style="padding-bottom: 3px;">
																<select class="form-control">
																	<c:if test="${!empty staffList}">
																		<c:forEach items="${staffList}" var="staff">
																			<c:set var="staffName" value="${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}"/>
																			<option>${staffName}</option>
																		</c:forEach>
																	</c:if>
					                                            </select>
															</td>
														</tr>
														<tr>
															<td style="padding-right: 3px;">
																<label>Project Position</label>
															</td>
															<td style="padding-bottom: 3px;">
																&nbsp;
															</td>
															<td style="padding-bottom: 3px;">
																<input type="text" class="form-control" placeholder="Example: Project Manager, Foreman, etc...">
															</td>
														</tr>
													</table>
													<br/>
                                           			<button class="btn btn-primary btn-sm">Assign</button>													
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
                   										<c:set var="projTeams" value="${project.assignedTeams}"/>
                   										<c:if test="${!empty projTeams}">
                   											<c:forEach var="team" items="${projTeams}">
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
		                   												<button class="btn btn-danger btn-sm" style="padding: 3px; margin-bottom: 3px">Unassign</button>
		                   												<a href="${contextPath}/team/edit/${team.id}">
		                   													<button class="btn btn-info btn-sm" style="padding: 3px; margin-bottom: 3px">View Team</button>
		                   												</a>
		                   											</td>
		                   										</tr>
                   											</c:forEach>
                   										</c:if>
                   									</table>
                   									<c:choose>
                   										<c:when test="${!empty projTeams}">
                   											<button class="btn btn-danger btn-sm">Unassign All</button>
                   										</c:when>
                   										<c:when test="${empty projTeams}">
                   											<h5>No team assigned.</h5>
                   										</c:when>
                   									</c:choose>
													<br/>
													<br/>
													<h4>Assign Teams</h4>
													<table>
														<tr>
															<td style="padding-right: 3px;">
																<label>Teams </label>
															</td>
															<td style="padding-bottom: 3px;">
																&nbsp;
															</td>
															<td style="padding-bottom: 3px;">
																<select class="form-control">
																	<c:if test="${!empty teamList}">
																		<c:forEach items="${teamList}" var="team">
																			<option>${team.name}</option>
																		</c:forEach>
																	</c:if>
					                                            </select>
															</td>
														</tr>
													</table>
													<br/>
                                           			<button class="btn btn-primary btn-sm">Assign</button>
                   								</div>
                   							</div>
                   						</div>
               						</div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_7">
                                	<div class="box">
                                		google map location:<br/>
	                                    - location of bunk house<br/>
	                                    - gravel supplier<br/>
	                                    - actual site location<br/>
                                	</div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_6">
                                	<div class="row">
				                        <div class="col-md-3">
				                            <div class="box box-primary">
				                                <div class="box-header">
				                                    <h4 class="box-title">Draggable Events</h4>
				                                </div>
				                                <div class="box-body">
				                                    <!-- the events -->
				                                    <div id='external-events'>
				                                        <div class='external-event bg-green'>Lunch</div>
				                                        <div class='external-event bg-red'>Go home</div>
				                                        <div class='external-event bg-aqua'>Do homework</div>
				                                        <div class='external-event bg-yellow'>Work on UI design</div>
				                                        <div class='external-event bg-navy'>Sleep tight</div>
				                                        <p>
				                                            <input type='checkbox' id='drop-remove' /> <label for='drop-remove'>remove after drop</label>
				                                        </p>
				                                    </div>
				                                </div><!-- /.box-body -->
				                            </div><!-- /. box -->
				                            <div class="box box-primary">
				                                <div class="box-header">
				                                    <h3 class="box-title">Create Event</h3>
				                                </div>
				                                <div class="box-body">
				                                    <div class="btn-group" style="width: 100%; margin-bottom: 10px;">
				                                        <button type="button" id="color-chooser-btn" class="btn btn-danger btn-block btn-sm dropdown-toggle" data-toggle="dropdown">Color <span class="caret"></span></button>
				                                        <ul class="dropdown-menu" id="color-chooser">
				                                            <li><a class="text-green" href="#"><i class="fa fa-square"></i> Green</a></li>
				                                            <li><a class="text-blue" href="#"><i class="fa fa-square"></i> Blue</a></li>
				                                            <li><a class="text-navy" href="#"><i class="fa fa-square"></i> Navy</a></li>
				                                            <li><a class="text-yellow" href="#"><i class="fa fa-square"></i> Yellow</a></li>
				                                            <li><a class="text-orange" href="#"><i class="fa fa-square"></i> Orange</a></li>
				                                            <li><a class="text-aqua" href="#"><i class="fa fa-square"></i> Aqua</a></li>
				                                            <li><a class="text-red" href="#"><i class="fa fa-square"></i> Red</a></li>
				                                            <li><a class="text-fuchsia" href="#"><i class="fa fa-square"></i> Fuchsia</a></li>
				                                            <li><a class="text-purple" href="#"><i class="fa fa-square"></i> Purple</a></li>
				                                        </ul>
				                                    </div><!-- /btn-group -->
				                                    <div class="input-group">
				                                        <input id="new-event" type="text" class="form-control" placeholder="Event Title">
				                                        <div class="input-group-btn">
				                                            <button id="add-new-event" type="button" class="btn btn-default btn-flat">Add</button>
				                                        </div><!-- /btn-group -->
				                                    </div><!-- /input-group -->
				                                </div>
				                            </div>
				                        </div><!-- /.col -->
				                        <div class="col-md-9">
				                            <div class="box box-primary">
				                                <div class="box-body no-padding">
				                                    <!-- THE CALENDAR -->
				                                    <div id="calendar"></div>
				                                </div><!-- /.box-body -->
				                            </div><!-- /. box -->
				                        </div><!-- /.col -->
				                    </div><!-- /.row -->
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_2">
                                	<div class="box">
		                                <div class="box-header">
		                                    <h3 class="box-title">Assigned Tasks</h3>
		                                </div><!-- /.box-header -->
		                                <div class="box-body no-padding">
		                                    <table class="table table-striped">
		                                        <tbody>
			                                        <tr>
			                                        	<th>&nbsp;</th>
			                                            <th>Status</th>
			                                            <th>Content</th>
			                                            <th>Team</th>
			                                            <th>Staff</th>
			                                            <th>Start</th>
			                                            <th>End</th>
			                                        </tr>
			                                        <c:set var="taskList" value="${project.assignedTasks}"/>
				                                	<c:if test="${!empty taskList}">
		                                        		<c:forEach items="${taskList}" var="task">
		                                        			<tr>
		                                        				<td>
		                                        					<div class="btn-group">
							                                            <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">
							                                                Mark As&nbsp;
							                                                <span class="caret"></span>
							                                            </button>
							                                            <ul class="dropdown-menu">
							                                                <li><a href="#">New</a></li>
							                                                <li><a href="#">Ongoing</a></li>
							                                                <li><a href="#">Completed</a></li>
							                                                <li><a href="#">Failed</a></li>
							                                                <li><a href="#">Cancelled</a></li>
<!-- 							                                                <li class="divider"></li> -->
<!-- 							                                                <li><a href="#">Separated link</a></li> -->
							                                            </ul>
							                                        </div>
							                                        <a href="${contextPath}/task/edit/${task.id}">
					                                            		<button class="btn btn-primary btn-sm">View</button>
					                                            	</a>
					                                            	<a href="${contextPath}/task/delete/${task.id}">
					                                            		<button class="btn btn-danger btn-sm">Delete</button>
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
						                                            		<span class="label label">Cancelled</span>
						                                            	</c:when>
						                                            </c:choose>
					                                            </td>
					                                            <td>${task.content}</td>
					                                            <td>
					                                            	<a href="${contextPath}/team/edit/${task.team.id}">
					                                            		<button class="btn btn-info btn-sm">View</button>&nbsp;&nbsp;
					                                            	</a>
					                                            	${task.team.name}
					                                            </td>
					                                            <c:set var="taskStaff" value="${task.staff}"/>
					                                            <c:set var="taskTeam" value="${task.team}"/>
					                                            <c:set var="taskStaffName" value="${taskStaff.prefix} ${taskStaff.firstName} ${taskStaff.middleName} ${taskStaff.lastName} ${taskStaff.suffix}"/>
					                                            <td>
					                                            	<a href="${contextPath}/staff/edit/${taskStaff.id}">
					                                            		<button class="btn btn-info btn-sm">View</button>&nbsp;&nbsp;
					                                            	</a>
					                                            	${taskStaffName}
					                                            </td>
					                                            <td>${task.dateStart}</td>
					                                            <td>${task.dateEnd}</td>
					                                        </tr>
		                                        		</c:forEach>
	                                        		</c:if>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
		                                <div class="box-footer clearfix no-border">
		                                	<a href="${contextPath}/task/assign/project/${project.id}">
		                                    	<button class="btn btn-default pull-right"><i class="fa fa-plus"></i> Add Task</button>
		                                    </a>
		                                </div>
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_3">
                                    <div class="box-body table-responsive">
                                    	<form role="form" name="uploadForm" id="uploadForm" method="post" action="${contextPath}/project/file/upload">
                                    		<div class="form-group">
		                                        <label for="exampleInputFile">File Upload</label>
		                                        <input type="file" id="inputFile">
		                                    </div>
                                    	</form>
                                    	<button class="btn btn-warning btn-sm" id="uploadButton" onclick="submitForm('uploadForm')">Upload</button>
	                                    <br/>
	                                    <table id="example-1" class="table table-bordered table-striped">
	                                        <thead>
	                                            <tr>
	                                            	<th>&nbsp;</th>
	                                                <th>Rendering engine</th>
	                                                <th>Browser</th>
	                                                <th>Platform(s)</th>
	                                                <th>Engine version</th>
	                                                <th>CSS grade</th>
	                                            </tr>
	                                        </thead>
	                                        <tbody>
	                                            <tr>
	                                            	<td>
	                                            		<center>
														<button class="btn btn-primary btn-sm">Download</button>
														<button class="btn btn-danger btn-sm">Delete</button>
														</center>
													</td>
	                                                <td>Trident</td>
	                                                <td>Internet
	                                                    Explorer 4.0</td>
	                                                <td>Win 95+</td>
	                                                <td> 4</td>
	                                                <td>X</td>
	                                            </tr>
	                                            <tr>
	                                            	<td>
	                                            		<center>
														<button class="btn btn-primary btn-sm">Download</button>
														<button class="btn btn-danger btn-sm">Delete</button>
														</center>
													</td>
	                                                <td>Trident</td>
	                                                <td>Internet
	                                                    Explorer 5.0</td>
	                                                <td>Win 95+</td>
	                                                <td>5</td>
	                                                <td>C</td>
	                                            </tr>
	                                            <tr>
	                                            	<td>
	                                            		<center>
														<button class="btn btn-primary btn-sm">Download</button>
														<button class="btn btn-danger btn-sm">Delete</button>
														</center>
													</td>
	                                                <td>Trident</td>
	                                                <td>Internet
	                                                    Explorer 5.5</td>
	                                                <td>Win 95+</td>
	                                                <td>5.5</td>
	                                                <td>A</td>
	                                            </tr>
	                                        </tbody>
	                                        <tfoot>
	                                            <tr>
	                                            	<th>&nbsp;</th>
	                                                <th>Rendering engine</th>
	                                                <th>Browser</th>
	                                                <th>Platform(s)</th>
	                                                <th>Engine version</th>
	                                                <th>CSS grade</th>
	                                            </tr>
	                                        </tfoot>
	                                    </table>
	                                </div><!-- /.box-body -->
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_4">
                                    <div class="form-group">
                                        <label for="exampleInputFile">Upload Photo</label>
                                        <input type="file" id="exampleInputFile"><br/>
                                        <label>Title</label>
                                        <input type="text" class="form-control" placeholder="Enter ..."/><br/>
                                        <label>Description</label>
                                        <input type="text" class="form-control" placeholder="Enter ..."/><br/>
										<button class="btn btn-primary btn-sm">Upload</button>
                                    </div>
                                    <br/>
                                    <div class="box">
                                    	 <br/>
									     <ul class="row">
									          <li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
									          	<img src="<c:url value="/temp/1.jpg"/> "/>
									          </li>
									          <li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
									          	<img src="<c:url value="/temp/1 (1).jpg"/> "/>
									          </li>
									          <li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
									          	<img src="<c:url value="/temp/1 (2).jpg"/> "/>
									          </li>
									          <li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
									          	<img src="<c:url value="/temp/1 (3).jpg"/> "/>
									          </li>
									          <li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
									          	<img src="<c:url value="/temp/1 (4).jpg"/> "/>
									          </li>
									          <li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
									          	<img src="<c:url value="/temp/1 (5).jpg"/> "/>
									          </li>
									     </ul>
									</div>
									<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
								      <div class="modal-dialog">
								        <div class="modal-content">         
								          <div class="modal-body">                
								          </div>
								        </div><!-- /.modal-content -->
								      </div><!-- /.modal-dialog -->
								    </div><!-- /.modal -->
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_5">
                                    <!-- The time line -->
		                            <ul class="timeline">
		                                <!-- timeline time label -->
		                                <li class="time-label">
		                                    <span class="bg-red">
		                                        10 Feb. 2014
		                                    </span>
		                                </li>
		                                <!-- /.timeline-label -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-envelope bg-blue"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 12:05</span>
		                                        <h3 class="timeline-header"><a href="#">Support Team</a> sent you and email</h3>
		                                        <div class="timeline-body">
		                                            Etsy doostang zoodles disqus groupon greplin oooj voxy zoodles,
		                                            weebly ning heekya handango imeem plugg dopplr jibjab, movity
		                                            jajah plickers sifteo edmodo ifttt zimbra. Babblely odeo kaboodle
		                                            quora plaxo ideeli hulu weebly balihoo...
		                                        </div>
		                                        <div class='timeline-footer'>
		                                            <a class="btn btn-primary btn-xs">Read more</a>
		                                            <a class="btn btn-danger btn-xs">Delete</a>
		                                        </div>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-user bg-aqua"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 5 mins ago</span>
		                                        <h3 class="timeline-header no-border"><a href="#">Sarah Young</a> accepted your friend request</h3>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-comments bg-yellow"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 27 mins ago</span>
		                                        <h3 class="timeline-header"><a href="#">Jay White</a> commented on your post</h3>
		                                        <div class="timeline-body">
		                                            Take me to your leader!
		                                            Switzerland is small and neutral!
		                                            We are more like Germany, ambitious and misunderstood!
		                                        </div>
		                                        <div class='timeline-footer'>
		                                            <a class="btn btn-warning btn-flat btn-xs">View comment</a>
		                                        </div>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <!-- timeline time label -->
		                                <li class="time-label">
		                                    <span class="bg-green">
		                                        3 Jan. 2014
		                                    </span>
		                                </li>
		                                <!-- /.timeline-label -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-camera bg-purple"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 2 days ago</span>
		                                        <h3 class="timeline-header"><a href="#">Mina Lee</a> uploaded new photos</h3>
		                                        <div class="timeline-body">
		                                            <img src="http://placehold.it/150x100" alt="..." class='margin' />
		                                            <img src="http://placehold.it/150x100" alt="..." class='margin'/>
		                                            <img src="http://placehold.it/150x100" alt="..." class='margin'/>
		                                            <img src="http://placehold.it/150x100" alt="..." class='margin'/>
		                                        </div>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-video-camera bg-maroon"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 5 days ago</span>
		                                        <h3 class="timeline-header"><a href="#">Mr. Doe</a> shared a video</h3>
		                                        <div class="timeline-body">
		                                            <iframe width="300" height="169" src="//www.youtube.com/embed/fLe_qO4AE-M" frameborder="0" allowfullscreen></iframe>
		                                        </div>
		                                        <div class="timeline-footer">
		                                            <a href="#" class="btn btn-xs bg-maroon">See comments</a>
		                                        </div>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <li>
		                                    <i class="fa fa-clock-o"></i>
		                                </li>
		                            </ul>
                                </div><!-- /.tab-pane -->
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
	<c:import url="/resources/js-includes.jsp" />
	<script>
		function submitAjax(id) {
			var formObj = $('#'+id);
			var serializedData = formObj.serialize();
			$.ajax({
				type: "POST",
				url: '${contextPath}/field/update/assigned/project',
				data: serializedData,
				success: function(response){
					location.reload();
				}
			});
		}	
	
		function submitForm(id) {
			$('#'+id).submit();
		}
	
		$(document).on('click', 'a.controls', function(){
	        var index = $(this).attr('href');
	        var src = $('ul.row li:nth-child('+ index +') img').attr('src');             
	        
	        $('.modal-body img').attr('src', src);
	        
	        var newPrevIndex = parseInt(index) - 1; 
	        var newNextIndex = parseInt(newPrevIndex) + 2; 
	        
	        if($(this).hasClass('previous')){               
	            $(this).attr('href', newPrevIndex); 
	            $('a.next').attr('href', newNextIndex);
	        }else{
	            $(this).attr('href', newNextIndex); 
	            $('a.previous').attr('href', newPrevIndex);
	        }
	        
	        var total = $('ul.row li').length + 1; 
	        //hide next button
	        if(total === newNextIndex){
	            $('a.next').hide();
	        }else{
	            $('a.next').show()
	        }            
	        //hide previous button
	        if(newPrevIndex === 0){
	            $('a.previous').hide();
	        }else{
	            $('a.previous').show()
	        }
	        
	        
	        return false;
	    });
		
		$(document).ready(function() {
			$("#example-1").dataTable();
			
			$('li img').on('click',function(){
                var src = $(this).attr('src');
                var img = '<img src="' + src + '" class="img-responsive"/>';
                
                //start of new code new code
                var index = $(this).parent('li').index();   
                
                var html = '';
                html += img;                
                html += '<div style="height:25px;clear:both;display:block;">';
                html += '<a class="controls next" href="'+ (index+2) + '">next &raquo;</a>';
                html += '<a class="controls previous" href="' + (index) + '">&laquo; prev</a>';
                html += '</div>';
                html += '<button class="btn btn-danger btn-sm">Delete</button>';
                
                $('#myModal').modal();
                $('#myModal').on('shown.bs.modal', function(){
                    $('#myModal .modal-body').html(html);
                    //new code
                    $('a.controls').trigger('click');
                })
                $('#myModal').on('hidden.bs.modal', function(){
                    $('#myModal .modal-body').html('');
                });
                
                
                
                
           });
	    });
	</script>
	<script type="text/javascript">
        $(function() {

            /* initialize the external events
             -----------------------------------------------------------------*/
            function ini_events(ele) {
                ele.each(function() {

                    // create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
                    // it doesn't need to have a start or end
                    var eventObject = {
                        title: $.trim($(this).text()) // use the element's text as the event title
                    };

                    // store the Event Object in the DOM element so we can get to it later
                    $(this).data('eventObject', eventObject);

                    // make the event draggable using jQuery UI
                    $(this).draggable({
                        zIndex: 1070,
                        revert: true, // will cause the event to go back to its
                        revertDuration: 0  //  original position after the drag
                    });

                });
            }
            ini_events($('#external-events div.external-event'));

            /* initialize the calendar
             -----------------------------------------------------------------*/
            //Date for the calendar events (dummy data)
            var date = new Date();
            var d = date.getDate(),
                    m = date.getMonth(),
                    y = date.getFullYear();
            $('#calendar').fullCalendar({
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                buttonText: {
                    today: 'today',
                    month: 'month',
                    week: 'week',
                    day: 'day'
                },
                //Random default events
                events: [
                    {
                        title: 'All Day Event',
                        start: new Date(y, m, 1),
                        backgroundColor: "#f56954", //red
                        borderColor: "#f56954" //red
                    },
                    {
                        title: 'Long Event',
                        start: new Date(y, m, d - 5),
                        end: new Date(y, m, d - 2),
                        backgroundColor: "#f39c12", //yellow
                        borderColor: "#f39c12" //yellow
                    },
                    {
                        title: 'Meeting',
                        start: new Date(y, m, d, 10, 30),
                        allDay: false,
                        backgroundColor: "#0073b7", //Blue
                        borderColor: "#0073b7" //Blue
                    },
                    {
                        title: 'Lunch',
                        start: new Date(y, m, d, 12, 0),
                        end: new Date(y, m, d, 14, 0),
                        allDay: false,
                        backgroundColor: "#00c0ef", //Info (aqua)
                        borderColor: "#00c0ef" //Info (aqua)
                    },
                    {
                        title: 'Birthday Party',
                        start: new Date(y, m, d + 1, 19, 0),
                        end: new Date(y, m, d + 1, 22, 30),
                        allDay: false,
                        backgroundColor: "#00a65a", //Success (green)
                        borderColor: "#00a65a" //Success (green)
                    },
                    {
                        title: 'Click for Google',
                        start: new Date(y, m, 28),
                        end: new Date(y, m, 29),
                        url: 'http://google.com/',
                        backgroundColor: "#3c8dbc", //Primary (light-blue)
                        borderColor: "#3c8dbc" //Primary (light-blue)
                    }
                ],
                editable: true,
                droppable: true, // this allows things to be dropped onto the calendar !!!
                drop: function(date, allDay) { // this function is called when something is dropped

                    // retrieve the dropped element's stored Event Object
                    var originalEventObject = $(this).data('eventObject');

                    // we need to copy it, so that multiple events don't have a reference to the same object
                    var copiedEventObject = $.extend({}, originalEventObject);

                    // assign it the date that was reported
                    copiedEventObject.start = date;
                    copiedEventObject.allDay = allDay;
                    copiedEventObject.backgroundColor = $(this).css("background-color");
                    copiedEventObject.borderColor = $(this).css("border-color");

                    // render the event on the calendar
                    // the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
                    $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

                    // is the "remove after drop" checkbox checked?
                    if ($('#drop-remove').is(':checked')) {
                        // if so, remove the element from the "Draggable Events" list
                        $(this).remove();
                    }

                }
            });

            /* ADDING EVENTS */
            var currColor = "#f56954"; //Red by default
            //Color chooser button
            var colorChooser = $("#color-chooser-btn");
            $("#color-chooser > li > a").click(function(e) {
                e.preventDefault();
                //Save color
                currColor = $(this).css("color");
                //Add color effect to button
                colorChooser
                        .css({"background-color": currColor, "border-color": currColor})
                        .html($(this).text()+' <span class="caret"></span>');
            });
            $("#add-new-event").click(function(e) {
                e.preventDefault();
                //Get value and make sure it is not null
                var val = $("#new-event").val();
                if (val.length == 0) {
                    return;
                }

                //Create events
                var event = $("<div />");
                event.css({"background-color": currColor, "border-color": currColor, "color": "#fff"}).addClass("external-event");
                event.html(val);
                $('#external-events').prepend(event);

                //Add draggable funtionality
                ini_events(event);

                //Remove event from text input
                $("#new-event").val("");
            });
        });
    </script>
</body>
</html>