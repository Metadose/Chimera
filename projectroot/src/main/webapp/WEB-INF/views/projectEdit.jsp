<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Project ${action}</title>
	<c:import url="/resources/css-includes.jsp" />
	<link href="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.css" />"rel="stylesheet" type="text/css" />
	<style type="text/css">
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
		html, body { height: 100%; padding:0px; margin:0px; overflow: hidden; }
		.gantt_task_cell.day_end, .gantt_task_cell.no_work_hour.day_start{
			border-right-color: #C7DFFF;
		}
		.gantt_task_cell.week_end.day_end, .gantt_task_cell.week_end.day_start{
			border-right-color: #E2E1E1;
		}
		
		.gantt_task_cell.week_end, .gantt_task_cell.no_work_hour{
			background-color: #F5F5F5;
		}
		.gantt_task_row.gantt_selected .gantt_task_cell.week_end{
			background-color: #F8EC9C;
		}
		html, body{ height:100%; padding:0px; margin:0px; overflow: hidden;}
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
	            		<c:when test="${project.id == 0}">
	            			New Project
	            		</c:when>
	            		<c:when test="${project.id != 0}">
	            			${project.name}
	            		</c:when>
	            	</c:choose>
	                <small>${action} Project</small>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                                <c:choose>
                                	<c:when test="${project.id != 0}">
                                		<li><a href="#tab_managers" data-toggle="tab">Managers</a></li>
                                		<li><a href="#tab_teams" data-toggle="tab">Teams</a></li>
                                		<li><a href="#tab_2" data-toggle="tab">Tasks</a></li>
		                                <li><a href="#tab_6" data-toggle="tab">Calendar</a></li>
		                                <li><a href="#tab_timeline" data-toggle="tab">Timeline</a></li>
		                                <li><a href="#tab_3" data-toggle="tab">Files</a></li>
		                                <li><a href="#tab_4" data-toggle="tab">Photos</a></li>
<!-- 		                                <li><a href="#tab_7" data-toggle="tab">Map</a></li> -->
                                	</c:when>
                                </c:choose>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<h2 class="page-header">Information</h2>
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Details</h3>
                   								</div>
                   								<div class="box-body">
                   									<c:choose>
                                						<c:when test="${project.id != 0}">
                                							<c:choose>
		                   										<c:when test="${!empty project.thumbnailURL}">
		                   											<img src="${contextPath}/image/display/project/profile/?project_id=${project.id}"/>
		                   										</c:when>
		                   										<c:when test="${empty project.thumbnailURL}">
		                   											No photo uploaded.
		                   										</c:when>
		                   									</c:choose>
		                   									<br/><br/>
		                   									<div class="form-group">
		                   										<form action="${contextPath}/photo/upload/project/profile" method="post" enctype="multipart/form-data">	
		                   											<input type="hidden" value="${project.id}" id="project_id" name="project_id"/>
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
						                                        </form>
						                                        &nbsp;
						                                        <form action="${contextPath}/photo/delete/project/profile/?project_id=${project.id}" method="post">
						                                        	<button class="btn btn-default btn-flat btn-sm">Delete Photo</button>
						                                        </form>
						                                    </div>
                                						</c:when>
                              						</c:choose>
				                                    <br/>
                   									<form role="form" name="detailsForm" id="detailsForm" method="post" action="${contextPath}/project/create">
				                                        <div class="form-group">
				                                        	<input type="hidden" name="id" value="${project.id}"/>
				                                            <label>Name</label>
				                                            <input type="text" class="form-control" name="name" value="${project.name}"/><br/>
				                                            <label>Status</label>
				                                            <select class="form-control" id="project_status" name="status">
						                                    	<option value="0">New</option>
						                                    	<option value="1">Ongoing</option>
						                                    	<option value="2">Completed</option>
						                                    	<option value="3">Failed</option>
						                                    	<option value="4">Cancelled</option>
				                                            </select><br/>
				                                            <label>Location</label>
				                                            <input type="text" class="form-control" name="location" value="${project.location}"/><br/>
				                                            <label>Notes</label>
				                                            <input type="text" class="form-control" name="notes" value="${project.notes}"/><br/>
				                                        </div>
				                                    </form>
				                                    <c:choose>
		                                            	<c:when test="${project.id == 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${project.id > 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<a href="${contextPath}/project/delete/${project.id}">
																<button class="btn btn-default btn-flat btn-sm">Delete This Project</button>
															</a>
		                                            	</c:when>
		                                            </c:choose>
                   								</div>
                   							</div>
                   						</div>
                   						<c:choose>
                   						<c:when test="${project.id != 0}">
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Fields</h3>
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
															<c:when test="${!empty projectFields}">
																<form role="form" name="fieldsUnassignForm" id="fieldsUnassignForm" method="post" action="${contextPath}/field/unassign/project/all">
																	<input type="hidden" name="project_id" value="${project.id}"/>
																	<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
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
                                           				<button class="btn btn-default btn-flat btn-sm" onclick="submitForm('fieldsForm')">Assign</button>
			                                        </div>
                   								</div>
                   							</div>
                   						</div>
                   						</c:when>
                   						</c:choose>
              						</div>
              						<c:choose>
                   					<c:when test="${project.id != 0}">
               						</c:when>
               						</c:choose>
                                </div><!-- /.tab-pane -->
                                <c:choose>
                   				<c:when test="${project.id != 0}">
<!--                                 <div class="tab-pane" id="tab_7"> -->
<!--                                 	<div class="box"> -->
<!--                                 		google map location:<br/> -->
<!-- 	                                    - location of bunk house<br/> -->
<!-- 	                                    - gravel supplier<br/> -->
<!-- 	                                    - actual site location<br/> -->
<!--                                 	</div> -->
<!--                                 </div>/.tab-pane -->
                                <div class="tab-pane" id="tab_6">
                                	<div class="row">
				                        <div class="col-md-3">
				                            <div class="box box-default">
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
				                            <div class="box box-default">
				                                <div class="box-header">
				                                    <h3 class="box-title">Create Event</h3>
				                                </div>
				                                <div class="box-body">
				                                    <div class="btn-group" style="width: 100%; margin-bottom: 10px;">
				                                        <button type="button" id="color-chooser-btn" class="btn btn-default btn-flat btn-block btn-sm dropdown-toggle" data-toggle="dropdown">Color <span class="caret"></span></button>
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
				                            <div class="box box-default">
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
		                                	<h3 class="box-title">Tasks&nbsp;
		                                    <table>
		                                    	<tr>
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/task/assign/from/project/">
		                                    			<input type="hidden" name="project_id" value="${project.id}"/>
		                                    			<input type="hidden" name="origin" value="project"/>
		                                    			<input type="hidden" name="originID" value="${project.id}"/>
				                                    	<button class="btn btn-default btn-flat btn-sm">Add Task</button>
					                                    </form>
		                                    		</td>
		                                    		<c:if test="${!empty project.assignedTasks}">
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/task/unassign/project/all">
                											<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
                											<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
               											</form>
		                                    		</td>
		                                    		</c:if>
		                                    	</tr>
		                                    </table>
		                                    </h3>
		                                </div><!-- /.box-header -->
		                                <div class="box-body table-responsive">
		                                    <table id="tasks-table" class="table table-bordered table-striped">
		                                    	<thead>
		                                            <tr>
			                                        	<th>&nbsp;</th>
			                                            <th>Status</th>
			                                            <th>Content</th>
			                                            <th>Team</th>
			                                            <th>Staff</th>
			                                            <th>Start</th>
			                                            <th>End</th>
			                                        </tr>
                                        		</thead>
		                                        <tbody>
			                                        <c:set var="taskList" value="${project.assignedTasks}"/>
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
							                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=0">New</a></li>
							                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=1">Ongoing</a></li>
							                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=2">Completed</a></li>
							                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=3">Failed</a></li>
							                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=4">Cancelled</a></li>
<!-- 							                                                <li class="divider"></li> -->
<!-- 							                                                <li><a href="#">Separated link</a></li> -->
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
					                                            <td>
					                                            	<c:choose>
					                                            		<c:when test="${!empty task.staff}">
					                                            			<c:forEach items="${task.staff}" var="taskStaff">
					                                            			<c:set var="taskStaffName" value="${taskStaff.prefix} ${taskStaff.firstName} ${taskStaff.middleName} ${taskStaff.lastName} ${taskStaff.suffix}"/>
					                                            			<a href="${contextPath}/staff/edit/from/project/?${taskStaff.id}">
							                                            		<button class="btn btn-default btn-flat btn-sm">View</button>&nbsp;&nbsp;
							                                            	</a>
							                                            	${taskStaffName}
							                                            	<br/>
					                                            			</c:forEach>
					                                            		</c:when>
					                                            		<c:when test="${empty task.staff}">
					                                            			<h5>No manager assigned.</h5>
					                                            		</c:when>
					                                            	</c:choose>					                                            
					                                            </td>
					                                            <td>${task.dateStart}</td>
					                                            <td>${task.dateEnd}</td>
					                                        </tr>
		                                        		</c:forEach>
	                                        		</c:if>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_3">
                                    <div class="box-body table-responsive">
                                    	<form enctype="multipart/form-data" method="post" action="${contextPath}/projectfile/upload/file/project">
											<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
											<label for="exampleInputFile">File Upload (20MB Max)</label>
											<input type="file" id="file" name="file"/><br/>
											<label>Description</label>
											<input type="text" class="form-control" id="description" name="description"/><br/>
											<button class="btn btn-default btn-flat btn-sm" id="uploadButton">Upload</button>
										</form>
	                                    <br/>
	                                    <table id="example-1" class="table table-bordered table-striped">
	                                        <thead>
	                                            <tr>
	                                            	<th>&nbsp;</th>
	                                            	<th>#</th>
	                                                <th>Name</th>
	                                                <th>Description</th>
	                                                <th>Size</th>
	                                                <th>Uploader</th>
	                                                <th>Date Uploaded</th>
	                                            </tr>
	                                        </thead>
	                                        <tbody>
	                                        	<c:if test="${!empty project.files}">
	                                        		<c:forEach items="${project.files}" var="file">
	                                        			<c:set var="uploader" value="${file.uploader}"/>
	                                               		<c:set var="uploaderName" value="${uploader.prefix} ${uploader.firstName} ${uploader.middleName} ${uploader.lastName} ${uploader.suffix}"/>
	                                        			<tr>
			                                            	<td>
			                                            		<center>
			                                            		<form action="${contextPath}/projectfile/download/from/project/" method="post">
			                                            			<input type="hidden" name="project_id" value="${project.id}"/>
			                                            			<input type="hidden" name="projectfile_id" value="${file.id}"/>
			                                            			<button class="btn btn-default btn-flat btn-sm">Download</button>
			                                            		</form>
																<button class="btn btn-default btn-flat btn-sm">View Details</button>
																<form name="deleteFileForm" id="deleteFileForm" method="post" action="${contextPath}/projectfile/delete/from/project/">
																	<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
																	<input type="hidden" id="projectfile_id" name="projectfile_id" value="${file.id}"/>
																	<button class="btn btn-default btn-flat btn-sm">Delete</button>
																</form>
																</center>
															</td>
			                                                <td>${file.id}</td>
			                                                <td>${file.name}</td>
			                                                <td>${file.description}</td>
			                                                <c:choose>
		                                                	<c:when test="${file.size < 1000000}">
		                                                		<c:set var="fileSize" value="${file.size / 1000}"/>
		                                                		<td><fmt:formatNumber type="number" maxIntegerDigits="3" value="${fileSize}"/> KB</td>
		                                                	</c:when>
		                                                	<c:when test="${file.size >= 1000000}">
		                                                		<c:set var="fileSize" value="${file.size / 1000000}"/>
		                                                		<td><fmt:formatNumber type="number" maxIntegerDigits="3" value="${fileSize}"/> MB</td>
		                                                	</c:when>
			                                                </c:choose>
			                                                <td>${staffName}</td>
			                                                <td>${file.dateUploaded}</td>
			                                            </tr>
	                                        		</c:forEach>
	                                        	</c:if>
	                                        </tbody>
	                                        <tfoot>
	                                            <tr>
	                                            	<th>&nbsp;</th>
	                                            	<th>#</th>
	                                                <th>Name</th>
	                                                <th>Description</th>
	                                                <th>Size</th>
	                                                <th>Uploader</th>
	                                                <th>Date Uploaded</th>
	                                            </tr>
	                                        </tfoot>
	                                    </table>
	                                </div><!-- /.box-body -->
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_4">
                                    <form enctype="multipart/form-data" method="post" action="${contextPath}/photo/upload/project">
										<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
										<label for="exampleInputFile">Upload Photo (20MB Max)</label>
										<input type="file" id="file" name="file"/><br/>
										<label>Description</label>
										<input type="text" class="form-control" id="description" name="description"/><br/>
										<button class="btn btn-default btn-flat btn-sm" id="uploadButton">Upload</button>
									</form>
                                    <br/>
                                    <c:if test="${!empty project.photos}">
                                   	<div class="box box-default">
                                    	<div class="box-header">
           									<h3 class="box-title">Photos</h3>
           								</div>
           								<div class="box-body">
           									<ul class="row">
									     		<c:forEach items="${project.photos}" var="photo">
									     			<li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
														<img src="${contextPath}/image/display/?project_id=${project.id}&filename=${photo.name}"/><br/><br/>
														<form action="${contextPath}/photo/delete">
															<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
															<input type="hidden" id="photo_id" name="photo_id" value="${photo.id}"/>
															<h6>${photo.name}</h6>
															<h6>${photo.description}</h6>
															<br/>
															<h6>Uploaded ${photo.dateUploaded}</h6>
															
															<c:set var="photoUploader" value="${photo.uploader}"/>
															<c:set var="photoUploaderName" value="${photoUploader.prefix} ${photoUploader.firstName} ${photoUploader.middleName} ${photoUploader.lastName} ${photoUploader.suffix}"/>
															<h6>${photoUploaderName}</h6>
															<button class="btn btn-default btn-flat btn-sm" id="photoDeleteButton">Delete</button>
														</form>
													</li>
									     		</c:forEach>
										     </ul>
           								</div>
       								</div>
       								</c:if>
									<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
								      <div class="modal-dialog">
								        <div class="modal-content">         
								          <div class="modal-body">                
								          </div>
								        </div><!-- /.modal-content -->
								      </div><!-- /.modal-dialog -->
								    </div><!-- /.modal -->
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_timeline">
                                	<div class="box">
		                                <div class="box-header">
		                                	<h3 class="box-title">Timeline&nbsp;
		                                    </h3>
		                                </div><!-- /.box-header -->
		                                <div id="gantt-chart" style='width:1000px; height:400px;'>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_managers">
                                	<div class="box">
		                                <div class="box-header">
		                                	<h3 class="box-title">Managers&nbsp;
		                                    <table>
		                                    	<tr>
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/staff/edit/0">
				                                    	<button class="btn btn-default btn-flat btn-sm">Create Staff</button>
					                                    </form>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:if test="${!empty staffList}">
		                                    		<form role="form" method="post" action="${contextPath}/staff/assign/project">
		                                    		<td>
		                                    			<select class="form-control" name="staff_id">
                                    						<c:forEach items="${staffList}" var="staff">
                                    							<c:set var="staffName" value="${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}"/>
                                    							<option value="${staff.id}">${staffName}</option>
                                    						</c:forEach>
		                                    			</select>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<input type="text" class="form-control" name="project_position"/>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<input type="hidden" name="project_id" value="${project.id}"/>
														<button class="btn btn-default btn-flat btn-sm">Assign</button>
		                                    		</td>
		                                    		</form>
		                                    		</c:if>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:if test="${!empty project.managerAssignments}">
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/staff/unassign/project/all">
                											<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
                											<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
               											</form>
		                                    		</td>
		                                    		</c:if>
		                                    	</tr>
		                                    </table>
		                                    </h3>
		                                </div><!-- /.box-header -->
		                                <div class="box-body table-responsive">
		                                    <table id="managers-table" class="table table-bordered table-striped">
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
			                                        <c:set var="managerAssignments" value="${project.managerAssignments}"/>
				                                	<c:if test="${!empty managerAssignments}">
				                                		<c:forEach items="${managerAssignments}" var="assignment">
			                                			<c:set var="manager" value="${assignment.manager}"/>
			                                            <tr>
			                                            	<td>
			                                            		<center>
	                   												<form action="${contextPath}/staff/edit/${manager.id}" method="post">
	                   													<input type="hidden" name="staff_id" value="${manager.id}"/>
	                   													<button class="btn btn-default btn-flat btn-sm">View</button>
	                   												</form>
																	<form name="unassignStaffForm" id="unassignStaffForm" method="post" action="${contextPath}/staff/unassign/project">
																		<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
																		<input type="hidden" id="staff_id" name="staff_id" value="${manager.id}"/>
																		<button class="btn btn-default btn-flat btn-sm">Unassign</button>
	                   												</form>
																</center>
															</td>
			                                                <td>
			                                                	<div class="user-panel">
													            <div class="pull-left image">
													                <c:choose>
		                                                			<c:when test="${!empty manager.thumbnailURL}">
		                                                				<img src="${contextPath}/image/display/staff/profile/?staff_id=${manager.id}" class="img-circle"/>
		                                                			</c:when>
		                                                			<c:when test="${empty manager.thumbnailURL}">
		                                                				<img src="/pmsys/resources/img/avatar5.png" class="img-circle">
		                                                			</c:when>
			                                                		</c:choose>
													            </div>
														        </div>
			                                                </td>
			                                                <td>${manager.prefix} ${manager.firstName} ${manager.middleName} ${manager.lastName} ${manager.suffix}</td>
			                                                <td>${manager.companyPosition}</td>
			                                                <td>${manager.email}</td>
			                                                <td>${manager.contactNumber}</td>
			                                            </tr>
		                                            </c:forEach>
	                                        		</c:if>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_teams">
                                	<div class="box">
		                                <div class="box-header">
		                                	<h3 class="box-title">Teams&nbsp;
		                                    <table>
		                                    	<tr>
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/team/edit/0">
				                                    	<button class="btn btn-default btn-flat btn-sm">Create Team</button>
					                                    </form>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:if test="${!empty teamList}">
		                                    		<form role="form" method="post" action="${contextPath}/team/assign/project">
		                                    		<td>
		                                    			<select class="form-control" name="team_id">
                                    						<c:forEach items="${teamList}" var="team">
                                    							<option value="${team.id}">${team.name}</option>
                                    						</c:forEach>
		                                    			</select>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<input type="hidden" name="project_id" value="${project.id}"/>
		                                    			<input type="hidden" name="origin" value="project"/>
		                                    			<input type="hidden" name="originID" value="${project.id}"/>
														<button class="btn btn-default btn-flat btn-sm">Assign</button>
		                                    		</td>
		                                    		</form>
		                                    		</c:if>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:if test="${!empty project.assignedTeams}">
		                                    		<td>
		                                    			<form role="form" method="post" action="${contextPath}/team/unassign/project/all">
              												<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
              												<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
              											</form>
		                                    		</td>
		                                    		</c:if>
		                                    	</tr>
		                                    </table>
		                                    </h3>
		                                </div><!-- /.box-header -->
		                                <div class="box-body table-responsive">
		                                    <table id="teams-table" class="table table-bordered table-striped">
		                                    	<thead>
		                                            <tr>
		                                            	<th>&nbsp;</th>
		                                            	<th>#</th>
		                                                <th>Name</th>
		                                            </tr>
                                        		</thead>
		                                        <tbody>
			                                        <c:set var="teams" value="${project.assignedTeams}"/>
				                                	<c:if test="${!empty teams}">
				                                		<c:forEach items="${teams}" var="team">
			                                            <tr>
			                                            	<td>
			                                            		<center>
																	<a href="${contextPath}/team/edit/${team.id}">
																		<button class="btn btn-default btn-flat btn-sm">View</button>
																	</a>
																	<form role="form" method="post" action="${contextPath}/team/unassign/project">
	                   													<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
	                   													<input type="hidden" id="team_id" name="team_id" value="${team.id}"/>
	                   													<input type="hidden" name="origin" value="project"/>
		                                    							<input type="hidden" name="originID" value="${project.id}"/>
	                   													<button class="btn btn-default btn-flat btn-sm">Unassign</button>
	                   												</form>
																</center>
															</td>
			                                                <td>${team.id}</td>
		                                                	<td>${team.name}</td>
			                                            </tr>
		                                            </c:forEach>
	                                        		</c:if>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                </c:when>
                                </c:choose>
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
	<c:import url="/resources/js-includes.jsp" />
	<script src="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.js" />"type="text/javascript"></script>
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
			$("#managers-table").dataTable();
			$("#teams-table").dataTable();
			$("#tasks-table").dataTable();
			$("#project_status").val("${project.status}");
			
			$('li img').on('click',function(){
                var src = $(this).attr('src');
                var img = '<img src="' + src + '" class="img-responsive"/>';
                
                //start of new code new code
                var index = $(this).parent('li').index();   
                
                var html = '';
                html += img;                
                html += '<div style="clear:both;padding-top: 5px;display:block;">';
                
                // Previous button.
                html += '<a class="controls previous" href="' + (index) + '">';
                html += '<button class="btn btn-default btn-flat btn-sm">Previous</button>';
                html += '</a>';
                html += '&nbsp;';
                
                // Next button.
                html += '<a class="controls next" href="'+ (index+2) + '">';
                html += '<button class="btn btn-default btn-flat btn-sm">Next</button>';
                html += '</a>';
                
                html += '</div>';
                
                $('#myModal').modal();
                $('#myModal').on('shown.bs.modal', function(){
                    $('#myModal .modal-body').html(html);
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
    
    <script type="text/javascript">
    var demo_tasks = {
    		"data":[
    			{"id":11, "text":"Project #1", "start_date":"28-03-2013", "duration":"11", "progress": 0.6, "open": true},
    			{"id":1, "text":"Project #2", "start_date":"01-04-2013", "duration":"18", "progress": 0.4, "open": true},

    			{"id":2, "text":"Task #1", "start_date":"02-04-2013", "duration":"8", "parent":"1", "progress":0.5, "open": true},
    			{"id":3, "text":"Task #2", "start_date":"11-04-2013", "duration":"8", "parent":"1", "progress": 0.6, "open": true},
    			{"id":4, "text":"Task #3", "start_date":"13-04-2013", "duration":"6", "parent":"1", "progress": 0.5, "open": true},
    			{"id":5, "text":"Task #1.1", "start_date":"02-04-2013", "duration":"7", "parent":"2", "progress": 0.6, "open": true},
    			{"id":6, "text":"Task #1.2", "start_date":"03-04-2013", "duration":"7", "parent":"2", "progress": 0.6, "open": true},
    			{"id":7, "text":"Task #2.1", "start_date":"11-04-2013", "duration":"8", "parent":"3", "progress": 0.6, "open": true},
    			{"id":8, "text":"Task #3.1", "start_date":"14-04-2013", "duration":"5", "parent":"4", "progress": 0.5, "open": true},
    			{"id":9, "text":"Task #3.2", "start_date":"14-04-2013", "duration":"4", "parent":"4", "progress": 0.5, "open": true},
    			{"id":10, "text":"Task #3.3", "start_date":"14-04-2013", "duration":"3", "parent":"4", "progress": 0.5, "open": true},
    			
    			{"id":12, "text":"Task #1", "start_date":"03-04-2013", "duration":"5", "parent":"11", "progress": 1, "open": true},
    			{"id":13, "text":"Task #2", "start_date":"02-04-2013", "duration":"7", "parent":"11", "progress": 0.5, "open": true},
    			{"id":14, "text":"Task #3", "start_date":"02-04-2013", "duration":"6", "parent":"11", "progress": 0.8, "open": true},
    			{"id":15, "text":"Task #4", "start_date":"02-04-2013", "duration":"5", "parent":"11", "progress": 0.2, "open": true},
    			{"id":16, "text":"Task #5", "start_date":"02-04-2013", "duration":"7", "parent":"11", "progress": 0, "open": true},

    			{"id":17, "text":"Task #2.1", "start_date":"03-04-2013", "duration":"2", "parent":"13", "progress": 1, "open": true},
    			{"id":18, "text":"Task #2.2", "start_date":"06-04-2013", "duration":"3", "parent":"13", "progress": 0.8, "open": true},
    			{"id":19, "text":"Task #2.3", "start_date":"10-04-2013", "duration":"4", "parent":"13", "progress": 0.2, "open": true},
    			{"id":20, "text":"Task #2.4", "start_date":"10-04-2013", "duration":"4", "parent":"13", "progress": 0, "open": true},
    			{"id":21, "text":"Task #4.1", "start_date":"03-04-2013", "duration":"4", "parent":"15", "progress": 0.5, "open": true},
    			{"id":22, "text":"Task #4.2", "start_date":"03-04-2013", "duration":"4", "parent":"15", "progress": 0.1, "open": true},
    			{"id":23, "text":"Task #4.3", "start_date":"03-04-2013", "duration":"5", "parent":"15", "progress": 0, "open": true}
    		],
    		"links":[
    			{"id":"1","source":"1","target":"2","type":"1"},
    			{"id":"2","source":"2","target":"3","type":"0"},
    			{"id":"3","source":"3","target":"4","type":"0"},
    			{"id":"4","source":"2","target":"5","type":"2"},
    			{"id":"5","source":"2","target":"6","type":"2"},
    			{"id":"6","source":"3","target":"7","type":"2"},
    			{"id":"7","source":"4","target":"8","type":"2"},
    			{"id":"8","source":"4","target":"9","type":"2"},
    			{"id":"9","source":"4","target":"10","type":"2"},
    			{"id":"10","source":"11","target":"12","type":"1"},
    			{"id":"11","source":"11","target":"13","type":"1"},
    			{"id":"12","source":"11","target":"14","type":"1"},
    			{"id":"13","source":"11","target":"15","type":"1"},
    			{"id":"14","source":"11","target":"16","type":"1"},
    			{"id":"15","source":"13","target":"17","type":"1"},
    			{"id":"16","source":"17","target":"18","type":"0"},
    			{"id":"17","source":"18","target":"19","type":"0"},
    			{"id":"18","source":"19","target":"20","type":"0"},
    			{"id":"19","source":"15","target":"21","type":"2"},
    			{"id":"20","source":"15","target":"22","type":"2"},
    			{"id":"21","source":"15","target":"23","type":"2"}
    		]
    	};

    	var users_data = {
    		"data":[
    			{"id":1, "text":"Project #1", "start_date":"01-04-2013", "duration":"11", "progress": 0.6, "open": true, "users": ["John", "Mike", "Anna"], "priority": "2"},
    			{"id":2, "text":"Task #1", "start_date":"03-04-2013", "duration":"5", "parent":"1", "progress": 1, "open": true, "users": ["John", "Mike"], "priority": "1"},
    			{"id":3, "text":"Task #2", "start_date":"02-04-2013", "duration":"7", "parent":"1", "progress": 0.5, "open": true, "users": ["Anna"], "priority": "1"},
    			{"id":4, "text":"Task #3", "start_date":"02-04-2013", "duration":"6", "parent":"1", "progress": 0.8, "open": true, "users": ["Mike", "Anna"], "priority": "2"},
    			{"id":5, "text":"Task #4", "start_date":"02-04-2013", "duration":"5", "parent":"1", "progress": 0.2, "open": true, "users": ["John"], "priority": "3"},
    			{"id":6, "text":"Task #5", "start_date":"02-04-2013", "duration":"7", "parent":"1", "progress": 0, "open": true, "users": ["John"], "priority": "2"},
    			{"id":7, "text":"Task #2.1", "start_date":"03-04-2013", "duration":"2", "parent":"3", "progress": 1, "open": true, "users": ["Mike", "Anna"], "priority": "2"},
    			{"id":8, "text":"Task #2.2", "start_date":"06-04-2013", "duration":"3", "parent":"3", "progress": 0.8, "open": true, "users": ["Anna"], "priority": "3"},
    			{"id":9, "text":"Task #2.3", "start_date":"10-04-2013", "duration":"4", "parent":"3", "progress": 0.2, "open": true, "users": ["Mike", "Anna"], "priority": "1"},
    			{"id":10, "text":"Task #2.4", "start_date":"10-04-2013", "duration":"4", "parent":"3", "progress": 0, "open": true, "users": ["John", "Mike"], "priority": "1"},
    			{"id":11, "text":"Task #4.1", "start_date":"03-04-2013", "duration":"4", "parent":"5", "progress": 0.5, "open": true, "users": ["John", "Anna"], "priority": "3"},
    			{"id":12, "text":"Task #4.2", "start_date":"03-04-2013", "duration":"4", "parent":"5", "progress": 0.1, "open": true, "users": ["John"], "priority": "3"},
    			{"id":13, "text":"Task #4.3", "start_date":"03-04-2013", "duration":"5", "parent":"5", "progress": 0, "open": true, "users": ["Anna"], "priority": "3"}
    		],
    		"links":[
    			{"id":"10","source":"11","target":"12","type":"1"},
    			{"id":"11","source":"11","target":"13","type":"1"},
    			{"id":"12","source":"11","target":"14","type":"1"},
    			{"id":"13","source":"11","target":"15","type":"1"},
    			{"id":"14","source":"11","target":"16","type":"1"},

    			{"id":"15","source":"13","target":"17","type":"1"},
    			{"id":"16","source":"17","target":"18","type":"0"},
    			{"id":"17","source":"18","target":"19","type":"0"},
    			{"id":"18","source":"19","target":"20","type":"0"},
    			{"id":"19","source":"15","target":"21","type":"2"},
    			{"id":"20","source":"15","target":"22","type":"2"},
    			{"id":"21","source":"15","target":"23","type":"2"}
    		]
    	};
    	
    	
	gantt.config.work_time = true;
	gantt.setWorkTime({hours : [8, 12, 13, 17]});//global working hours. 8:00-12:00, 13:00-17:00

	gantt.config.scale_unit = "day";
	gantt.config.date_scale = "%l, %F %d";
	gantt.config.min_column_width = 20;
	gantt.config.duration_unit = "hour";
	gantt.config.scale_height = 20*3;

	gantt.templates.task_cell_class = function(task, date){
		var css = [];
		if(date.getHours() == 7){
			css.push("day_start");
		}
		if(date.getHours() == 16){
			css.push("day_end");
		}
		if(!gantt.isWorkTime(date, 'day')){
			css.push("week_end");
		}else if(!gantt.isWorkTime(date, 'hour')){
			css.push("no_work_hour");
		}
		return css.join(" ");
	};

	var weekScaleTemplate = function(date){
		var dateToStr = gantt.date.date_to_str("%d %M");
		var weekNum = gantt.date.date_to_str("(week %W)");
		var endDate = gantt.date.add(gantt.date.add(date, 1, "week"), -1, "day");
		return dateToStr(date) + " - " + dateToStr(endDate) + " " + weekNum(date);
	};

	gantt.config.subscales = [
		{unit:"week", step:1, template:weekScaleTemplate},
		{unit:"hour", step:1, date:"%G"}

	];

	function showAll(){
		gantt.ignore_time = null;
		gantt.render();
	}
	
	function hideWeekEnds(){
		gantt.ignore_time = function(date){
			return !gantt.isWorkTime(date, "day");
		};
		gantt.render();
	}
	
	function hideNotWorkingTime(){
		gantt.ignore_time = function(date){
			return !gantt.isWorkTime(date);
		};
		gantt.render();
	}

	gantt.init("gantt-chart");
	gantt.parse(demo_tasks);
</script>
</body>
</html>