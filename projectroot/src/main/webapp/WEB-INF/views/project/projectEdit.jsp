<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="action" value="Create"/>
<c:if test="${project.id > 0}">
	<c:set var="action" value="Edit"/>
</c:if>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Project ${action}</title>
	
	<link href="<c:url value="/resources/css/gantt-custom.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/fullcalendar.css" />"rel="stylesheet" type="text/css" />
	
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
	</style>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<script src="<c:url value="/resources/lib/moment.min.js" />"></script>
	<script src="<c:url value="/resources/lib/fullcalendar.min.js" />"></script>
	
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
	            			${fn:escapeXml(project.name)}
	            		</c:when>
	            	</c:choose>
	                <small>${action} Project <a href="${contextPath}/project/clear/cache/${project.id}">Clear Cache</a> </small>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                    	${uiParamAlert}
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs" id="myTab">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                                <c:choose>
                                	<c:when test="${project.id != 0}">
                                		<li><a href="#tab_managers" data-toggle="tab">Managers</a></li>
                                		<li><a href="#tab_staff" data-toggle="tab">Staff</a></li>
		                                <li><a href="#tab_timeline" data-toggle="tab">Timeline</a></li>
		                                <li><a href="#tab_calendar" data-toggle="tab">Calendar</a></li>
		                                <li><a href="#tab_expenses" data-toggle="tab">Expenses</a></li>
										<li><a href="#tab_inventory" data-toggle="tab">Inventory</a></li>
		                                <li><a href="#tab_payroll" data-toggle="tab">Payroll</a></li>
                                	</c:when>
                                </c:choose>
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
                   									<div class="callout callout-info callout-cebedo">
									                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
									                </div>
                   									<c:choose>
                                						<c:when test="${project.id != 0}">
                                							<c:choose>
		                   										<c:when test="${!empty project.thumbnailURL}">
		                   											<img src="${contextPath}/image/display/project/profile/?project_id=${project.id}"/>
		                   											<br/>
		                   											<br/>
		                   										</c:when>
		                   										<c:when test="${empty project.thumbnailURL}">
		                   											<div class="callout callout-warning">
													                    <p>No photo uploaded.</p>
													                </div>
		                   										</c:when>
		                   									</c:choose>
		                   									<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
		                   									<div class="form-group">
		                   										<form:form id="uploadPhotoForm"
	                   												modelAttribute="profilePhoto"
																	action="${contextPath}/project/upload/profile"
																	method="post"
																	enctype="multipart/form-data">
			                   										<table>
			                   											<tr>
			                   												<td style="vertical-align: top">
			                   													<label for="exampleInputFile">Update Photo</label>
			                   												</td>
			                   												<td>
			                   													&nbsp;&nbsp;
			                   												</td> 
			                   												<td>
			                   													<input type="file" id="file" name="file"/>
			                   													<p class="help-block">Upload a photo of the project site</p>
			                   												</td>
			                   											</tr>
			                   										</table>
						                                        </form:form>
						                                        <table>
						                                        <tr>
						                                        <td>
						                                        <button onclick="submitForm('uploadPhotoForm')" class="btn btn-cebedo-upload btn-flat btn-sm">Upload</button>
						                                        </td>
						                                        <c:if test="${!empty project.thumbnailURL}">
						                                        <td>
						                                        &nbsp;
						                                        </td>
						                                        <td>
						                                        <c:url var="urlProjectProfileDelete" value="/project/profile/delete"/>
                                								<a href="${urlProjectProfileDelete}">
						                                        <button class="btn btn-cebedo-delete btn-flat btn-sm">Delete Photo</button>
          														</a>
						                                        </td>
						                                        </c:if>
						                                        </tr>
						                                        </table>
						                                    </div>
						                                    </sec:authorize>
                                						</c:when>
                              						</c:choose>
				                                    <br/>
				                                    <c:if test="${project.id != 0}">
				                                    <!-- Read only Output -->
				                                    <div class="form-group" id="detailsDivViewer">
			                                            <label>Name</label><br/>
			                                            <c:out value="${project.name}"/><br/><br/>
			                                            
			                                            <label>Status</label><br/>
			                                            <c:set value="${project.getStatusEnum().css()}" var="css"></c:set>
														<span class="label ${css}">${project.getStatusEnum()}</span><br/><br/>
			                                            
			                                            <label>Location</label><br/>
			                                            <c:out value="${project.location}"/><br/><br/>
			                                            
			                                            <label>Notes</label><br/>
			                                            <c:out value="${project.notes}"/><br/><br/>
			                                            
			                                            <sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
			                                            <button class="btn btn-cebedo-edit btn-flat btn-sm" onclick="switchDisplay(detailsDivViewer, detailsDivEditor)">Edit Details</button>
			                                            <p class="help-block">Edit the details above</p>
			                                            </sec:authorize>
			                                        </div>
			                                        </c:if>
				                                    <sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
			                                        <div class="form-group" id="detailsDivEditor">
			                                        
			                                        	<!-- Update form Input -->
                  										<form:form id="detailsForm"
                  											modelAttribute="project"
                  											method="post"
                  											action="${contextPath}/project/create">
				                                            
				                                            <label>Name</label>
				                                            <form:input type="text" placeholder="Sample: Mr. Brown Building" class="form-control" path="name"/>
				                                            <p class="help-block">Enter the name of the project</p>
				                                            
				                                            <label>Status</label>
				                                            <form:select class="form-control" id="project_status" path="status">
						                                    	<form:option value="0" label="New"/>
						                                    	<form:option value="1" label="Ongoing"/>
						                                    	<form:option value="2" label="Completed"/>
						                                    	<form:option value="3" label="Failed"/>
						                                    	<form:option value="4" label="Cancelled"/>
				                                            </form:select>
				                                            <p class="help-block">Choose the status of this project</p>
				                                            
				                                            <label>Location</label>
				                                            <form:input type="text" placeholder="Sample: 123 Brown Avenue, New York City" class="form-control" path="location"/>
				                                            <p class="help-block">Enter the project location</p>
				                                            
				                                            <label>Notes</label>
				                                            <form:input type="text" placeholder="Sample: This is only the first phase" class="form-control" path="notes"/>
				                                            <p class="help-block">Add additional notes and remarks</p>
				                                            <br/>
				                                    	</form:form>
			                                    	<c:choose>
		                                            	<c:when test="${project.id == 0}">
		                                            		<button class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${project.id > 0}">
		                                            		<button class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<c:url var="urlProjectDelete" value="/project/delete/${project.id}"/>
                               								<a href="${urlProjectDelete}">
																<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete This Project</button>
       														</a>
		                                            	</c:when>
		                                            </c:choose>
		                                            <c:if test="${project.id != 0}">
		                                            <button class="btn btn-cebedo-edit btn-flat btn-sm" onclick="switchDisplay(detailsDivEditor, detailsDivViewer)">Done Editing</button>
		                                            </c:if>
			                                        </div>
		                                            </sec:authorize>
                   								</div>
                   							</div>
                   						</div>
                   						<c:choose>
                   						<c:when test="${project.id != 0}">
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">More Information</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="callout callout-info callout-cebedo">
									                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
									                </div>
                   									<div class="form-group">
                   											<c:set var="projectFields" value="${project.assignedFields}"/>
               												
                   											<c:if test="${!empty projectFields}">
   															<div class="form-group" id="fieldsDivViewer">
	               												<c:forEach var="field" items="${projectFields}"  varStatus="loop">
               														<!-- More Information Output -->
	       															<label><c:out value="${field.label}"/></label>
	       															<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
	               														<c:url var="urlEditProjectField" value="/project/field/edit/${field.field.id}-3edc-${field.label}-3edc-${field.value}"/>
						                                            	(
		                                								<a class="general-link" href="${urlEditProjectField}">
						                                            	Edit
	               														</a>
						                                            	)
						                                            </sec:authorize>
						                                            <br/>
						                                            <c:out value="${field.value}"/>
	       															<br/>
	       															<br/>
																</c:forEach>
   															</div>
   															</c:if>
   															<c:if test="${empty projectFields}">
   															<div class="callout callout-warning">
											                    <p>No extra information added.</p>
											                </div>
   															</c:if>
   															<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
   															<h4>Add More Information</h4>
															<form:form modelAttribute="field"
																id="fieldsForm" 
																method="post" 
																action="${contextPath}/project/assign/field">
																
																<label>Label</label><br/>
																<form:input type="text" path="label" id="label" class="form-control"
																	placeholder="Sample: SSS, Building Permit No., Sub-contractor, etc."/>
																	<p class="help-block">Add a label for this information</p>
																
																<label>Information</label><br/>
																<form:textarea class="form-control"
																	rows="3" id="value" path="value"
																	placeholder="Sample: 000-123-456, AEE-123, OneForce Construction, etc."></form:textarea>
																	<p class="help-block">Enter the information to be added</p>
															
															</form:form>
															<br/>
	                                           				<button class="btn btn-cebedo-create btn-flat btn-sm" onclick="submitForm('fieldsForm')">Add Information</button>
	                                           				<c:if test="${!empty projectFields}">
	                                           				<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
																<c:url var="urlProjectUnassignFieldAll" value="/project/unassign/field/all"/>
	                               								<a href="${urlProjectUnassignFieldAll}">
																	<button class="btn btn-cebedo-delete btn-flat btn-sm">Remove All</button>
	       														</a>
															</sec:authorize>
															</c:if>
	                                           				</sec:authorize>
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
                                <div class="tab-pane" id="tab_timeline">
                                	<div class="row">
                   						<div class="col-xs-12">
		                                	<div class="box box-default">
	              								<div class="box-header">
	              									<h3 class="box-title">Timeline</h3>
	              								</div>
				                                <div class="box-body">
				                                <div class="callout callout-info callout-cebedo">
								                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
								                </div>
								                <c:if test="${!empty project.assignedTasks}">
				                                <table>
               										<tr>
           											<td>Legend:
           											</td>
           											<td>&nbsp;</td>
           											<td>
													<c:forEach items="${ganttElemTypeList}" var="ganttElem">
														<c:set value="" var="border"></c:set>
														<c:if test="${ganttElem.className().contains(\"btn-default\")}">
															<c:set value="border: 1px solid #999999;" var="border"></c:set>
														</c:if>
														<span class="label ${ganttElem.className()}"
														style="
														color: ${ganttElem.color()};
														background-color: ${ganttElem.backgroundColor()};
														${border};
														">
														${ganttElem.label()}
														</span>
														&nbsp;
													</c:forEach>
           											</td>
               										</tr>
               									</table>
               									<br/>
               									</c:if>
				                                <c:choose>
				                                	<c:when test="${!empty project.assignedTasks}">
						                                <div id="gantt-chart" class="gantt-holder">
						                                </div><!-- /.box-body -->
				                                	</c:when>
				                                	<c:when test="${empty project.assignedTasks}">
				                                		<div id="gantt-chart" class="gantt-holder">
				                                			<div class="callout callout-warning">
											                    <p>No tasks in this project.</p>
											                </div>
						                                </div><!-- /.box-body -->
				                                	</c:when>
				                                </c:choose>
				                                </div>
				                            </div>
			                            </div>
		                            </div>
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Milestones</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="callout callout-info callout-cebedo">
									                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
									                </div>
                   									<button class="btn btn-cebedo-create btn-flat btn-sm" id="createMilestone">Create Milestone</button>
                   									<br/>
                   									<br/>
                   									<table id="milestones-table" class="table table-bordered table-striped">
				                                    	<thead>
				                                            <tr>
	              											<th>&nbsp;</th>
	              											<th>Milestone</th>
	              											<th>Status</th>
	              											<th>New Task</th>
	              											<th>Ongoing Task</th>
	              											<th>Done Task</th>
	              											</tr>
		                                        		</thead>
					                                    <tbody>
															<c:forEach items="${milestoneSummary}" var="milestoneMap">
															<c:set value="${milestoneMap.key}" var="milestone"/>
						                                	<c:set value="${milestoneMap.value}" var="msCount"/>
															<tr>
																<td>
																<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
																<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
																</td>
																<td>${milestone.name}</td>
																<td>
																<c:set value="${msCount.get(\"Status\").css()}" var="css"></c:set>
																<span class="label ${css}">${msCount.get("Status")}</span>
																</td>
																<td>${msCount.get("NOT YET STARTED")}</td>
																<td>${msCount.get("ONGOING")}</td>
																<td>${msCount.get("DONE")}</td>
															</tr>
															</c:forEach>
														</tbody>
					                                </table>
                   									<br/>
													<b>Total Tasks Assigned to Milestones:</b> ${timelineSummaryMap.get("Total Tasks Assigned to Milestones")}<br/>
													<b>Total Milestones:</b> ${timelineSummaryMap.get("Total Milestones")}<br/>
													<b>Breakdown</b> of Total Milestones by Milestone Status:<br/><br/>
													<table id="milestone-breakdown-table" class="table table-bordered table-striped">
													<thead>
			                                    		<tr>
				                                            <th>Milestone Status</th>
				                                            <th>Count</th>
				                                        </tr>
			                                    	</thead>
													<tbody>
														<tr>
															<c:set value="${idToMilestoneMap.get(\"NOT YET STARTED\").css()}" var="css"></c:set>
															<td><span class="label ${css}">${idToMilestoneMap.get("NOT YET STARTED").label()}</span></td>
															<td>${timelineSummaryMap.get("Total Milestones (Not Yet Started)")}</td>
														</tr>
														<tr>
															<c:set value="${idToMilestoneMap.get(\"ONGOING\").css()}" var="css"></c:set>
															<td><span class="label ${css}">${idToMilestoneMap.get("ONGOING")}</span></td>
															<td>${timelineSummaryMap.get("Total Milestones (Ongoing)")}</td>
														</tr>
														<tr>
															<c:set value="${idToMilestoneMap.get(\"DONE\").css()}" var="css"></c:set>
															<td><span class="label ${css}">${idToMilestoneMap.get("DONE")}</span></td>
															<td>${timelineSummaryMap.get("Total Milestones (Done)")}</td>
														</tr>
													</tbody>
													</table>
													
                   								</div>
                   							</div>
                   						</div>
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Summary of Tasks</h3>
                   								</div>
                   								<div class="box-body">
                   								<div class="callout callout-info callout-cebedo">
								                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
								                </div>
								                
                   								<b>Total Tasks:</b> ${timelineSummaryMap.get("Total Tasks")}<br/>
                   								<b>Breakdown</b> of Total Tasks by Task Status:<br/><br/>
                   								
												<table id="task-status-table" class="table table-bordered table-striped">
												<thead>
		                                    		<tr>
			                                            <th>Task Status</th>
			                                            <th>Count</th>
			                                        </tr>
		                                    	</thead>
												<tbody>
												<c:forEach items="${taskStatusMap}" var="statusEntry">
												<c:set value="${statusEntry.key}" var="entryKey"/>
												<c:set value="${statusEntry.value}" var="entryValue"/>
													<tr>
														<td>
				                                            <span class="label ${entryKey.css()}">${entryKey}</span>
														</td>
														<td>
															${entryValue}
														</td>
													</tr>
												</c:forEach>
												</tbody>
												</table>
                   								</div>
                   							</div>
                   						</div>
              						</div>
		                            <div class="row">
                   						<div class="col-xs-12">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Tasks</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="callout callout-info callout-cebedo">
									                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
									                </div>
			                                    		<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
					                                	<table>
					                                    	<tr>
					                                    		<td>
					                                    			<c:url value="/task/create/from/project" var="urlAddTask"/>
					                                    			<a href="${urlAddTask}">
							                                    	<button class="btn btn-cebedo-create btn-flat btn-sm">Create Task</button>
					                                    			</a>
					                                    		</td>
					                                    		<c:if test="${!empty project.assignedTasks}">
					                                    		<td>
					                                    			&nbsp;
					                                    		</td>
					                                    		<td>
					                                    			<form method="post" action="${contextPath}/task/unassign/project/all">
					                                    				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			                											<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
			                											<button class="btn btn-cebedo-unassign-all btn-flat btn-sm">Unassign All</button>
			               											</form>
					                                    		</td>
					                                    		</c:if>
					                                    	</tr>
					                                    </table><br/>
			                                    		</sec:authorize>
					                                    <table id="tasks-table" class="table table-bordered table-striped">
					                                    	<thead>
					                                            <tr>
						                                        	<th>&nbsp;</th>
						                                            <th>Status</th>
						                                            <th>Start</th>
						                                            <th>End</th>
						                                            <th>Duration</th>
						                                            <th>Title</th>
						                                            <th>Content</th>
						                                            <th>Staff</th>
						                                        </tr>
			                                        		</thead>
					                                        <tbody>
						                                        <c:set var="taskList" value="${project.assignedTasks}"/>
							                                	<c:if test="${!empty taskList}">
					                                        		<c:forEach items="${taskList}" var="task">
					                                        			<tr>
					                                        				<td>
					                                        					<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
					                                        					<div class="btn-group">
										                                            <button type="button" class="btn btn-cebedo-update btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">
										                                                Mark As&nbsp;
										                                                <span class="caret"></span>
										                                            </button>
										                                            <ul class="dropdown-menu">
										                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=0&${_csrf.parameterName}=${_csrf.token}">New</a></li>
										                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=1&${_csrf.parameterName}=${_csrf.token}">Ongoing</a></li>
										                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=2&${_csrf.parameterName}=${_csrf.token}">Completed</a></li>
										                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=3&${_csrf.parameterName}=${_csrf.token}">Failed</a></li>
										                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=4&${_csrf.parameterName}=${_csrf.token}">Cancelled</a></li>
			<!-- 							                                                <li class="divider"></li> -->
			<!-- 							                                                <li><a href="#">Separated link</a></li> -->
										                                            </ul>
										                                        </div>
										                                        </sec:authorize>
										                                        <c:url value="/task/edit/${task.id}/from/project/${project.id}" var="urlViewTask"/>
										                                        <a href="${urlViewTask}">
								                                            	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
										                                        </a>
								                                            	<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
								                                            	<form method="post" action="${contextPath}/task/unassign/from/project">
								                                            	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
								                                            	<input type="hidden" name="task_id" value="${task.id}"/>
								                                            	<input type="hidden" name="project_id" value="${project.id}"/>
								                                            	<button class="btn btn-cebedo-unassign btn-flat btn-sm">Unassign</button>
								                                            	</form> 
								                                            	</sec:authorize>
					                                        				</td>
								                                            <td style="vertical-align: middle;">
									                                            <c:set value="${task.getStatusEnum().css()}" var="css"></c:set>
																				<span class="label ${css}">${task.getStatusEnum()}</span>
								                                            </td>
								                                            <td>${task.dateStart}</td>
								                                            <fmt:formatDate pattern="yyyy-MM-dd" value="${task.getEndDate()}" var="taskEndDate"/>
								                                            <td>${taskEndDate}</td>
								                                            <td>${task.duration}</td>
								                                            <td>
								                                            ${task.title}
								                                            </td>
								                                            <td>
								                                            ${task.content}
								                                            </td>
								                                            <td>
								                                            	<c:choose>
								                                            		<c:when test="${!empty task.staff}">
								                                            			<c:forEach items="${task.staff}" var="taskStaff">
								                                            			<c:set var="taskStaffName" value="${taskStaff.getFullName()}"/>
								                                            			<a class="general-link" href="${contextPath}/staff/edit/from/project/?${taskStaff.id}">
										                                            	${taskStaffName}
										                                            	</a>
										                                            	<br/><br/>
								                                            			</c:forEach>
								                                            		</c:when>
								                                            		<c:when test="${empty task.staff}">
								                                            			No staff assigned.
								                                            		</c:when>
								                                            	</c:choose>					                                            
								                                            </td>
								                                        </tr>
					                                        		</c:forEach>
				                                        		</c:if>
						                                    </tbody>
						                                </table>
					                                </div><!-- /.box-body -->
					                            </div>
				                           	</div>
			                           	</div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_calendar">
                               	<div class="row">
               						<div class="col-xs-12">
               							<div class="box box-default">
               								<div class="box-header">
<!--                    									List<Map<String, String>> getEventTypePropertyMaps -->
               									<h3 class="box-title">Calendar</h3>
               								</div>
               								<div class="box-body">
               									<div class="callout callout-info callout-cebedo">
								                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
								                </div>
               									<table>
               										<tr>
           											<td>Legend:
           											</td>
           											<td>&nbsp;</td>
           											<td>
													<c:forEach items="${calendarEventTypes}" var="calendarEvent">
														<span class="label ${calendarEvent.css()}">
														${calendarEvent}
														</span>
														&nbsp;
													</c:forEach>
           											</td>
               										</tr>
               									</table><br/>
               									<div id='calendar'></div>
               								</div>
               							</div>
               						</div>
          						</div>
           						</div>
                                <div class="tab-pane" id="tab_payroll">
                                	<div class="row">
		                            <div class="col-xs-12">
               							<div class="box box-default">
               								<div class="box-header">
               									<h3 class="box-title">Payroll List</h3>
               								</div>
               								<div class="box-body">
               									<div class="callout callout-info callout-cebedo">
								                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
								                </div>
										  	  	<c:url var="urlCreateTeam" value="/project/edit/payroll/0-end"/>
		                                  		<a href="${urlCreateTeam}">
		                                    		<button class="btn btn-cebedo-create btn-flat btn-sm">Create Payroll</button>
		                                  		</a>
		                                  		<br/>
		                                  		<br/>
		                                  		<div class="pull-right">
		                                  		<h3>Grand Total <b><u>
			                                	${projectAux.getGrandTotalPayrollAsString()}
												</u></b></h3>
												</div>
			                                    <table id="payroll-table" class="table table-bordered table-striped">
			                                    	<thead>
			                                            <tr>
			                                            	<th>&nbsp;</th>
			                                                <th>Start Date</th>
			                                                <th>End Date</th>
			                                            	<th>Approver</th>
			                                                <th>Creator</th>
			                                                <th>Status</th>
			                                                <th>Payroll Total</th>
			                                                <th>Last Computed</th>
			                                            </tr>
	                                        		</thead>
			                                        <tbody>
				                                		<c:forEach items="${payrollList}" var="payrollRow">
														<fmt:formatDate pattern="yyyy.MM.dd" value="${payrollRow.startDate}" var="payrollStartDate"/>
														<fmt:formatDate pattern="yyyy.MM.dd" value="${payrollRow.endDate}" var="payrollEndDate"/>
				                                		<c:set value="${payrollRow.approver.id}-${payrollRow.creator.id}-${payrollRow.status.id()}-${payrollStartDate}-${payrollEndDate}"
				                                				var="payrollRow.getKey()"></c:set>
				                                		
			                                            <tr>
			                                            	<td>
			                                            		<center>
			                                            			<c:url var="urlEditPayroll" value="/project/edit/payroll/${payrollRow.getKey()}-end"/>
			                                            			<a href="${urlEditPayroll}">
							                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
			                                            			</a>
								                                    <c:url value="/project/delete/payroll/${payrollRow.getKey()}-end" var="urlDeletePayroll"/>
								                                    <a href="${urlDeletePayroll}">
	                   													<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
								                                    </a>
																</center>
															</td>
															<fmt:formatDate pattern="yyyy/MM/dd" value="${payrollRow.startDate}" var="payrollStartDate"/>
			                                                <td>${payrollStartDate}</td>
			                                                <fmt:formatDate pattern="yyyy/MM/dd" value="${payrollRow.endDate}" var="payrollEndDate"/>
			                                                <td>${payrollEndDate}</td>
			                                                <td>${payrollRow.approver.staff.getFullName()}</td>
			                                                <td>${payrollRow.creator.staff.getFullName()}</td>
			                                                <td>
			                                                <c:set value="${payrollRow.status}" var="payrollStatus"></c:set>
			                                                <c:set value="${payrollStatus.css()}" var="css"></c:set>
															<span class="label ${css}">${payrollStatus}</span>
			                                                </td>
			                                                <td>${payrollRow.payrollComputationResult.getOverallTotalOfStaffAsString()}</td>
			                                                <fmt:formatDate pattern="yyyy/MM/dd hh:mm:ss a" value="${payrollRow.lastComputed}" var="lastComputed"/>
			                                                <td>${lastComputed}</td>
			                                            </tr>
		                                            	</c:forEach>
				                                    </tbody>
				                                </table>
			                                </div><!-- /.box-body -->
               							</div>
               						</div>
               						</div>
		                            <div class="row">
		                            <div class="col-md-6">
               							<div class="box box-default">
               								<div class="box-header">
               									<h3 class="box-title">Graph</h3>
               								</div>
               								<div class="box-body">
               									<div class="callout callout-info callout-cebedo">
								                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
								                </div>
               									Line graph here of [release date, total]
               								</div>
               							</div>
               						</div>
               						</div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_inventory">
                                	<div class="row">
		                            <div class="col-md-6">
               							<div class="box box-default">
               								<div class="box-header">
               									<h3 class="box-title">Deliveries</h3>
               								</div>
               								<div class="box-body">
               									<div class="callout callout-info callout-cebedo">
								                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
								                </div>
										  	  	<c:url var="urlCreateDelivery" value="/project/edit/delivery/0-end"/>
		                                  		<a href="${urlCreateDelivery}">
		                                    		<button class="btn btn-cebedo-create btn-flat btn-sm">Create Delivery</button>
		                                  		</a>
		                                  		<br/>
		                                  		<br/>
			                                    <table id="delivery-table" class="table table-bordered table-striped">
			                                    	<thead>
			                                            <tr>
			                                            	<th>&nbsp;</th>
			                                                <th>Date and Time</th>
			                                                <th>Name</th>
			                                                <th>Description</th>
			                                                <th>Materials Cost</th>
			                                            </tr>
	                                        		</thead>
			                                        <tbody>
				                                		<c:forEach items="${deliveryList}" var="deliveryRow">
														<fmt:formatDate pattern="yyyy/MM/dd hh:mm a" value="${deliveryRow.datetime}" var="deliveryDateTime"/>
			                                            <tr>
			                                            	<td>
			                                            		<center>
			                                            			<c:url var="urlEditDelivery" value="/project/edit/delivery/${deliveryRow.getKey()}-end"/>
			                                            			<a href="${urlEditDelivery}">
							                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
			                                            			</a>
								                                    <c:url value="/project/delete/delivery/${deliveryRow.getKey()}-end" var="urlDeleteDelivery"/>
								                                    <a href="${urlDeleteDelivery}">
	                   													<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
								                                    </a>
																</center>
															</td>
															<td>${deliveryDateTime}</td>
															<td>${deliveryRow.name}</td>
															<td>${deliveryRow.description}</td>
															<td>${deliveryRow.getGrandTotalOfMaterialsAsString()}</td>
			                                            </tr>
		                                            	</c:forEach>
				                                    </tbody>
				                                </table>
			                                </div><!-- /.box-body -->
               							</div>
               						</div>
               						<div class="col-md-6">
               							<div class="box box-default">
               								<div class="box-header">
               									<h3 class="box-title">Pull-Outs</h3>
               								</div>
               								<div class="box-body">
               									<div class="callout callout-info callout-cebedo">
								                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
								                </div>
			                                    <table id="pull-out-table" class="table table-bordered table-striped">
			                                    	<thead>
			                                            <tr>
			                                            	<th>&nbsp;</th>
<!-- 			                                                <th>Start Date</th> -->
<!-- 			                                                <th>End Date</th> -->
<!-- 			                                            	<th>Approver</th> -->
<!-- 			                                                <th>Creator</th> -->
<!-- 			                                                <th>Status</th> -->
<!-- 			                                                <th>Payroll Total</th> -->
<!-- 			                                                <th>Last Computed</th> -->
			                                            </tr>
	                                        		</thead>
			                                        <tbody>
				                                		<c:forEach items="${payrollList}" var="payrollRow">
														<fmt:formatDate pattern="yyyy.MM.dd" value="${payrollRow.startDate}" var="payrollStartDate"/>
														<fmt:formatDate pattern="yyyy.MM.dd" value="${payrollRow.endDate}" var="payrollEndDate"/>
			                                            <tr>
			                                            	<td>
			                                            		<center>
			                                            			<c:url var="urlEditPayroll" value="/project/edit/payroll/${payrollRow.getKey()}-end"/>
			                                            			<a href="${urlEditPayroll}">
							                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
			                                            			</a>
								                                    <c:url value="/project/delete/payroll/${payrollRow.getKey()}-end" var="urlDeletePayroll"/>
								                                    <a href="${urlDeletePayroll}">
	                   													<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
								                                    </a>
																</center>
															</td>
			                                            </tr>
		                                            	</c:forEach>
				                                    </tbody>
				                                </table>
			                                </div><!-- /.box-body -->
               							</div>
               						</div>
               						</div>
                                	<div class="row">
		                            <div class="col-xs-12">
               							<div class="box box-default">
               								<div class="box-header">
               									<h3 class="box-title">Inventory</h3>
               								</div>
               								<div class="box-body box-default">
               									<div class="callout callout-info callout-cebedo">
								                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
								                </div>
								                <div class="pull-right">
		                                  		<h3>Grand Total <b><u>
			                                	${projectAux.getGrandTotalDeliveryAsString()}
												</u></b></h3>
												</div>
			                                    <table id="material-table" class="table table-bordered table-striped">
			                                    	<thead>
			                                            <tr>
			                                            	<th>&nbsp;</th>
			                                            	<th>Delivery</th>
			                                                <th>Name</th>
			                                                <th>Used / Pulled-Out</th>
			                                                <th>Available</th>
			                                            	<th>Quantity</th>
			                                                <th>Unit</th>
			                                                <th>Cost (Per Unit)</th>
			                                                <th>Total Cost</th>
			                                                <th>Remarks</th>
			                                            </tr>
	                                        		</thead>
			                                        <tbody>
				                                		<c:forEach items="${materialList}" var="row">
			                                            <tr>
			                                            	<td>
			                                            		<center>
			                                            			<c:url var="urlEdit" value="/project/edit/material/${row.getKey()}-end"/>
			                                            			<a href="${urlEdit}">
							                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
			                                            			</a>
			                                            			<c:url var="urlPullout" value="/project/pullout/material/${row.getKey()}-end"/>
								                                    <a href="${urlPullout}">
	                   													<button class="btn btn-cebedo-pullout btn-flat btn-sm">Pull-Out</button>
								                                    </a>
								                                    <c:url var="urlDelete" value="/project/delete/material/${row.getKey()}-end"/>
								                                    <a href="${urlDelete}">
	                   													<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
								                                    </a>
																</center>
															</td>
															<td>${row.delivery.name}</td>
															<td>${row.name}</td>
															<td align="right">${row.used}</td>
															<td align="right">${row.available}</td>
															<td align="right">${row.quantity}</td>
															<td>${row.unit}</td>
															<td align="right">${row.getCostPerUnitMaterialAsString()}</td>
															<td align="right">${row.getTotalCostPerUnitMaterialAsString()}</td>
															<td>${row.remarks}</td>
			                                            </tr>
		                                            	</c:forEach>
				                                    </tbody>
				                                </table>
			                                </div><!-- /.box-body -->
               							</div>
               						</div>
               						</div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_managers">
                                	<div class="box">
                                		<div class="box-header">
          									<h3 class="box-title">Managers</h3>
          								</div>
		                                <div class="box-body table-responsive">
          									<div class="callout callout-info callout-cebedo">
							                    <p>Managers are system Users who have the previledge to edit/update this project TODO.</p>
							                </div>
		                                	<c:set var="displayBreakManager" value="${false}"/>
		                                	<table>
		                                    	<tr>
		                                    		<sec:authorize access="hasRole('ROLE_STAFF_EDITOR')">
		                                    		<td style="vertical-align: top;">
		                                    			<c:url var="urlCreateStaff" value="/staff/edit/0/from/project/${project.id}"/>
		                                    			<a href="${urlCreateStaff}">
				                                    	<button class="btn btn-cebedo-create btn-flat btn-sm">Create Staff</button>
		                                    			</a>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:set var="displayBreakManager" value="${true}"/>
		                                    		</sec:authorize>
		                                    		<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
		                                    		<c:if test="${!empty staffList}">
 		                                    		<form:form 
 		                                    		modelAttribute="staffPosition"  
 		                                    		method="post" 
 		                                    		action="${contextPath}/project/assign/staff"> 
 		                                    			<td>
 		                                    			<form:select class="form-control" path="staffID"> 
                                     						<c:forEach items="${staffList}" var="staff"> 
                                     							<form:option value="${staff.id}" label="${staff.getFullName()}"/> 
                                     						</c:forEach> 
 		                                    			</form:select>
 		                                    			
 		                                    			<p class="help-block">Choose the staff who will manage this project&nbsp;&nbsp;&nbsp;&nbsp;</p> 
 		                                    			</td>
 		                                    			<td>
 		                                    				&nbsp;
 		                                    			</td>
 		                                    			<td>
 		                                    			<form:input placeholder="Sample: Constructor, Timekeeper, Foreman, Liason, etc." 
 		                                    				type="text" 
 															class="form-control" 
 															path="position"/>
 														<p class="help-block">Enter the position or title of the staff in this project</p>
 		                                    			</td>
 		                                    			<td>
 		                                    				&nbsp;
 		                                    			</td>
 														<td style="vertical-align: top;">
 														<button class="btn btn-cebedo-assign btn-flat btn-sm">Assign</button>
 		                                    			</td> 
 		                                    		</form:form> 
		                                    		</c:if>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:if test="${!empty project.managerAssignments}">
		                                    		<td>
               											<c:url var="urlProjectUnassignStaffAll" value="/project/unassign/staff/all"/>
		                                    			<a href="${urlProjectUnassignStaffAll}">
                											<button class="btn btn-cebedo-unassign-all btn-flat btn-sm">Unassign All</button>
		                                    			</a>
		                                    		</td>
		                                    		</c:if>
		                                    		<c:set var="displayBreakManager" value="${true}"/>
		                                    		</sec:authorize>
		                                    	</tr>
		                                    </table>
		                                    <c:if test="${displayBreakManager}">
		                                    <br/>
		                                    </c:if>
		                                    <table id="managers-table" class="table table-bordered table-striped">
		                                    	<thead>
		                                            <tr>
		                                            	<th>&nbsp;</th>
		                                                <th>Photo</th>
		                                                <th>Full Name</th>
		                                                <th>Company Position</th>
		                                                <th>Project Position</th>
		                                                <th>Salary (Daily)</th>
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
			                                            			<c:url var="urlViewStaff" value="/staff/edit/${manager.id}/from/project/${project.id}" />
			                                            			<a href="${urlViewStaff}">
							                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
			                                            			</a>
	                   												<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
	                   												<c:url var="urlUnassignStaff" value="/project/unassign/staff/${manager.id}"/>
	                   												<a href="${urlUnassignStaff}">
																		<button class="btn btn-cebedo-unassign btn-flat btn-sm">Unassign</button>
	                   												</a>
	                   												</sec:authorize>
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
		                                                				<img src="${contextPath}/resources/img/avatar5.png" class="img-circle">
		                                                			</c:when>
			                                                		</c:choose>
													            </div>
														        </div>
			                                                </td>
			                                                <td>${manager.getFullName()}</td>
			                                                <td>${manager.companyPosition}</td>
			                                                <td>${assignment.projectPosition}</td>
			                                                <td>${manager.getWageAsString()}</td>
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
                                <div class="tab-pane" id="tab_expenses">
                                	<div class="box">
		                                <div class="box-body table-responsive">
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_staff">
                                	<div class="row">
                   						<div class="col-xs-12">
		                                	<div class="box box-default">
		                                		<div class="box-header">
	              									<h3 class="box-title">Staff Members</h3>
	              								</div>
				                                <div class="box-body table-responsive">
				                                	<div class="callout callout-info callout-cebedo">
									                    <p>Managers are system Users who have the previledge to edit/update this project TODO.</p>
									                </div>
				                                    <table id="assigned-staff-table" class="table table-bordered table-striped">
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
					                                        <c:set var="assignedStaff" value="${project.assignedStaff}"/>
						                                	<c:if test="${!empty assignedStaff}">
						                                		<c:forEach items="${assignedStaff}" var="assignedStaffMember">
					                                            <tr>
					                                            	<td>
					                                            		<center>
					                                            			<c:url var="urlViewStaff" value="/staff/edit/${assignedStaffMember.id}/from/project/${project.id}"/>
					                                            			<a href="${urlViewStaff}">
									                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
					                                            			</a>
										                                    <sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
										                                    <c:url value="/project/unassign/staff-member/${assignedStaffMember.id}" var="urlUnassignStaff"/>
										                                    <a href="${urlUnassignStaff}">
			                   													<button class="btn btn-cebedo-unassign btn-flat btn-sm">Unassign</button>
										                                    </a>
			                   												</sec:authorize>
																		</center>
																	</td>
				                                                	<td>${assignedStaffMember.getFullName()}</td>
				                                                	<td>${assignedStaffMember.companyPosition}</td>
				                                                	<td>${assignedStaffMember.getWageAsString()}</td>
				                                                	<td>${assignedStaffMember.email}</td>
				                                                	<td>${assignedStaffMember.contactNumber}</td>
					                                            </tr>
				                                            </c:forEach>
			                                        		</c:if>
					                                    </tbody>
					                                </table>
				                                </div><!-- /.box-body -->
				                             </div>
				                        </div>
				                   	</div>
				                   	<div class="row">
                   						<div class="col-xs-12">
		                                	<div class="box box-default">
	              								<div class="box-header">
	              									<h3 class="box-title">Staff Assignment Controls</h3>
	              								</div>
				                                <div class="box-body">
				                                	<div class="callout callout-info callout-cebedo">
									                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
									                </div>
		                                    		<sec:authorize access="hasRole('ROLE_STAFF_EDITOR')">
		                                    			<c:url var="urlCreateStaff" value="/staff/edit/0/from/project/${project.id}"/>
		                                    			<a href="${urlCreateStaff}">
				                                    	<button class="btn btn-cebedo-create btn-flat btn-sm">Create Staff</button>
		                                    			</a>
		                                    		</sec:authorize>
		                                    		<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
		                                    		<c:if test="${!empty availableStaffToAssign}">
		                                    		<button onclick="submitForm('assignStaffForm')" class="btn btn-cebedo-assign btn-flat btn-sm">Assign</button>
		                                    		</c:if>
		                                    		<c:if test="${!empty project.assignedStaff}">
              											<c:url value="/project/unassign/staff-member/all" var="urlUnassignStaffAll"/>
					                                    <a href="${urlUnassignStaffAll}">
              												<button class="btn btn-cebedo-unassign-all btn-flat btn-sm">Unassign All</button>
					                                    </a>
		                                    		</c:if>
		                                    		<c:if test="${!empty availableStaffToAssign}">
		                                    		&nbsp;&nbsp;
		                                    		<a href="#" onclick="checkAll('include-checkbox')" class="general-link">Check All</a>&nbsp;
													<a href="#" onclick="uncheckAll('include-checkbox')" class="general-link">Uncheck All</a>
		                                    		</c:if>
					                                <br/>
					                                <br/>
		                                    		</sec:authorize>
		                                    		<form:form modelAttribute="project" 
		                                    		method="post" 
		                                    		id="assignStaffForm"
		                                    		action="${contextPath}/project/assign/staff/mass">
		                                    		<table id="assign-staff-table" class="table table-bordered table-striped">
		                                    			<thead>
		                                    			<tr>
		                                    			<th>Check/Uncheck</th>
		                                    			<th>Full Name</th>
		                                                <th>Company Position</th>
		                                                <th>Salary (Daily)</th>
		                                                <th>E-Mail</th>
		                                                <th>Contact Number</th>
		                                    			</tr>
		                                    			</thead>
		                                    			<tbody>
		                                    			<c:forEach items="${availableStaffToAssign}" var="staff">
		                                    			<tr>
		                                    			<td align="center">
			                                    			<form:checkbox class="form-control include-checkbox" path="staffIDs" value="${staff.id}"/><br/>
		                                    			</td>
		                                    			<td>${staff.getFullName()}</td>
		                                    			<td>${staff.companyPosition}</td>
	                                                	<td>${staff.getWageAsString()}</td>
	                                                	<td>${staff.email}</td>
	                                                	<td>${staff.contactNumber}</td>
		                                    			</tr>
			                                    		</c:forEach>
		                                    			</tbody>
		                                    		</table>
		                                    		</form:form>
				                                </div>
				                             </div>
				                        </div>
				                   	</div>
				                </div>
                                </c:when>
                                </c:choose>
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
	
	<script src="<c:url value="/resources/js/common.js" />"type="text/javascript"></script>

	<c:if test="${project.id != 0 && !empty project.assignedTasks}">
   	<script src="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.js" />"type="text/javascript"></script>
    <script src="${contextPath}/resources/lib/dhtmlxGantt_v3.1.1_gpl/ext/dhtmlxgantt_tooltip.js" type="text/javascript"></script>
	<script src="<c:url value="/resources/js/gantt-custom.js" />"type="text/javascript"></script>
	
	<script type="text/javascript">
	    $(document).ready(function() {
	    	var ganttJSON = ${ganttJSON};
		    var tasks = {'data': ganttJSON};
			gantt.init("gantt-chart");
		    gantt.parse(tasks);
		    gantt.sort("start_date");
		 	
		    // TODO.
		    // On load of the page: switch to the currently selected tab.
		    var hash = window.name;
		    $('#myTab').find("[href='#"+ hash +"']").tab('show');
		});
	</script>
   	</c:if>
   	
   	
   	<c:if test="${project.id != 0}">
   	<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
   	<script type="text/javascript">
   	$(document).ready(function() {
		$('#detailsDivEditor').hide();
		
		var eventsJSON = ${calendarJSON};
		$('#calendar').fullCalendar({
			events: eventsJSON,
			dayClick: function(date, jsEvent, view) {
// 				$("#modalDate").val(date.format());
// 				$("#modalWage").val(staffWage);
// 				$("#myModal").modal('show');
		    },
		    eventClick: function(calEvent, jsEvent, view) {
// 		    	$("#modalDate").val(calEvent.start.format());
// 				$("#modalWage").val(staffWage);
// 				$("#myModal").modal('show');
				
// 				var statusValue = calEvent.attendanceStatus;
// 				$('#attendanceStatus').val(statusValue);
				
// 				if(statusValue == 2 || this.value == -1) {
// 					$('#modalWage').hide();
// 					$('#modalWageLabel').hide();
// 					$('#modalWageBreak').hide();
// 				} else {
// 					$('#modalWage').val(calEvent.attendanceWage);
// 					$('#modalWage').show();
// 					$('#modalWageLabel').show();
// 					$('#modalWageBreak').show();
// 				}
		    }
	    });
	});
   	</script>
	</sec:authorize>
	</c:if>
	
	<c:if test="${project.id != 0 && !empty project.assignedFields}">
   	<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
   	<script type="text/javascript">
   	$(document).ready(function() {
		$('#detailsDivEditor').hide();
		$('#fieldsDivEditor').hide();
	});
   	</script>
	</sec:authorize>
	</c:if>
	
	<script type="text/javascript">
	    // Photos event handler.
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
	        // Hide next button.
	        if(total === newNextIndex){
	            $('a.next').hide();
	        }else{
	            $('a.next').show()
	        }
	        
	        // Hide previous button.
	        if(newPrevIndex === 0){
	            $('a.previous').hide();
	        }else{
	            $('a.previous').show()
	        }
	        
	        return false;
	    });
		
		$(document).ready(function() {
			$("#material-table").dataTable();
			$("#pull-out-table").dataTable();
			$("#delivery-table").dataTable();
			$("#payroll-table").dataTable();
			$("#milestones-table").dataTable();
			$("#managers-table").dataTable();
			$("#assigned-staff-table").dataTable();
			$("#assign-staff-table").dataTable();
			$("#tasks-table").dataTable();
			
			// Event handler for photos.
			$('li img').on('click',function(){
                var src = $(this).attr('src');
                var img = '<img id="popupImage" src="' + src + '" class="img-responsive"/>';
                var index = $(this).parent('li').index();
                var html = '';
                html += img;                
                html += '<br/><div>';
               
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
	</script>
</body>
</html>