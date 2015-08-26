<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<c:choose>
    	<c:when test="${project.id == 0}">
    	<title>Create Project</title>
    	</c:when>
    	<c:when test="${project.id > 0}">
		<title>Edit Project</title>
    	</c:when>
    </c:choose>
	
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
		/*TODO Estimation tables. Needed?*/
		#concrete-estimation-summary-table, #concrete-table, #form-estimate-cost {
			font-size: 11px;
		}
		/*TODO Toggle visibility of columns. Do we still need this?*/
		.toggle-vis {
			cursor: pointer;
			font-size: 12px !important;
		}
	</style>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<script src="<c:url value="/resources/lib/moment.min.js" />"></script>
	<script src="<c:url value="/resources/lib/fullcalendar.min.js" />"></script>

	<!-- Modal -->
	<div id="deleteModal" class="modal fade" role="dialog">
		<!-- Modal content-->
		<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">Confirmation</h4>
			</div>
			<div class="modal-body">
				<p>Do you really want to delete this project?</p>
			</div>
			<div class="modal-footer">
	            <c:url var="urlProjectDelete" value="/project/delete/${project.id}"/>
				<a href="${urlProjectDelete}">
				<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
				</a>
				<button type="button" class="btn btn-default btn-flat btn-sm" data-dismiss="modal">Close</button>
			</div>
		</div>
		</div>
	</div>

	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>	
	            	<c:choose>
	            		<c:when test="${project.id == 0}">
	            			New Project
			                <small>Create Project 
			                <c:url var="clearCache" value="/project/clear/cache/${project.id}"/>
			                <a href="${clearCache}">Clear Cache</a> </small>
	            		</c:when>

	            		<c:when test="${project.id != 0}">
	            			${project.name}
			                <small>Edit Project 
			                <c:url var="clearCache" value="/project/clear/cache/${project.id}"/>
			                <a href="${clearCache}">Clear Cache</a> </small>
	            		</c:when>
	            	</c:choose>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-md-12">
                    	${uiParamAlert}
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs" id="myTab">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                                <c:choose>
                                	<c:when test="${project.id != 0}">
                                		<li><a href="#tab_staff" data-toggle="tab">Staff</a></li>
                                		<li><a href="#tab_project_estimate" data-toggle="tab">Estimate</a></li>
		                                <li><a href="#tab_timeline" data-toggle="tab">Program of Works</a></li>
										<li><a href="#tab_inventory" data-toggle="tab">Inventory</a></li>
		                                <li><a href="#tab_payroll" data-toggle="tab">Payroll</a></li>
		                                <!-- <li><a href="#tab_calendar" data-toggle="tab">TODO Calendar</a></li> -->
                                	</c:when>
                                </c:choose>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Basic</h3>
                   								</div>
                   								<div class="box-body">
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
				                                    	</form:form>
			                                    	<c:choose>
		                                            	<c:when test="${project.id == 0}">
		                                            		<button class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${project.id > 0}">
		                                            		<button class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
															<button class="btn btn-cebedo-delete btn-flat btn-sm" data-toggle="modal" data-target="#deleteModal">Delete</button>
		                                            	</c:when>
		                                            </c:choose>
			                                        </div>
                   								</div>
                   							</div>
                   						</div>
                   						<c:choose>
                   						<c:when test="${project.id != 0}">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<c:set var="projectFields" value="${project.assignedFields}"/>
                   								<c:choose>
                   								<c:when test="${empty projectFields}">
                   								<div class="box-header">
                   									<h3 class="box-title">Add More Information</h3>
                   								</div>
                   								</c:when>
                   								
                   								<c:when test="${!empty projectFields}">
                   								<div class="box-header">
                   									<h3 class="box-title">More Information</h3>
                   								</div>
                   								</c:when>
                   								</c:choose>
                   								
                   								<div class="box-body">
                   									<div class="form-group">
               												
                   											<c:if test="${!empty projectFields}">
   															<div class="form-group" id="fieldsDivViewer">
	               												<c:forEach var="field" items="${projectFields}"  varStatus="loop">
               														<!-- More Information Output -->
	       															<label><c:out value="${field.label}"/></label>
	               														<c:url var="urlEditProjectField" value="/project/field/edit/${field.field.id}-3edc-${field.label}-3edc-${field.value}"/>
						                                            	(
		                                								<a class="general-link" href="${urlEditProjectField}">
						                                            	Edit
	               														</a>
						                                            	)
						                                            <br/>
						                                            <c:out value="${field.value}"/>
	       															<br/>
	       															<br/>
																</c:forEach>
   															</div>
   															</c:if>
   															<c:if test="${!empty projectFields}">
   															<h4>Add More Information</h4>
   															</c:if>
															<form:form modelAttribute="field"
																id="fieldsForm" 
																method="post" 
																action="${contextPath}/project/assign/field">
																
																<label>Label</label><br/>
																<form:input type="text" path="label" id="label" class="form-control"
																	placeholder="Sample: SSS, Building Permit No., Sub-contractor"/>
																	<p class="help-block">Add a label for this information</p>
																
																<label>Information</label><br/>
																<form:textarea class="form-control"
																	rows="3" id="value" path="value"
																	placeholder="Sample: 000-123-456, AEE-123, OneForce Construction"></form:textarea>
																	<p class="help-block">Enter the information to be added</p>
															
															</form:form>
	                                           				<button class="btn btn-cebedo-create btn-flat btn-sm" onclick="submitForm('fieldsForm')">Add Information</button>
	                                           				<c:if test="${!empty projectFields}">
																<c:url var="urlProjectUnassignFieldAll" value="/project/unassign/field/all"/>
	                               								<a href="${urlProjectUnassignFieldAll}">
																	<button class="btn btn-cebedo-delete btn-flat btn-sm">Remove All</button>
	       														</a>
															</c:if>
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
                   				<div class="tab-pane" id="tab_project_estimate">

                   					<c:if test="${empty estimationOutputList}">
                   						<c:set value="hide" var="estimateVisibility"/>
                   					</c:if>

              						<div class="row ${estimateVisibility}">
                   						<div class="col-md-12">
		                                	<div class="box box-body box-default">
<!-- 				                                		<div class="box-header"> -->
<!-- 			              									<h3 class="box-title">Staff Members</h3> -->
<!-- 			              								</div> -->
				                                <div class="box-body">
				                                    <table id="estimate-output-table" class="table table-bordered table-striped is-data-table">	
				                                    	<thead>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                                <th>Name</th>
				                                                <th>Remarks</th>
				                                                <th>Allowance</th>
				                                                <th>Time Computed</th>
				                                            </tr>
		                                        		</thead>
				                                        <tbody>
					                                		<c:forEach items="${estimationOutputList}" var="estimationOutput">
				                                            <tr>
				                                            	<td>
				                                            		<center>
				                                            			<c:url var="urlView" value="/project/view/estimation/${estimationOutput.getKey()}-end"/>
				                                            			<a href="${urlView}">
								                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
				                                            			</a>
									                                    <c:url value="/project/delete/estimation/${estimationOutput.getKey()}-end" var="urlDelete"/>
									                                    <a href="${urlDelete}">
		                   													<button class="btn btn-cebedo-unassign btn-flat btn-sm">Delete</button>
									                                    </a>
																	</center>
																</td>
			                                                	<td>${estimationOutput.name}</td>
			                                                	<td>${estimationOutput.remarks}</td>
			                                                	<td>${estimationOutput.estimationAllowance.getLabel()}</td>
			                                                	<fmt:formatDate pattern="yyyy/MM/dd hh:mm:ss a" value="${estimationOutput.lastComputed}" var="timeComputed"/>
			                                                	<td>${timeComputed}</td>
				                                            </tr>
				                                            </c:forEach>
					                                    </tbody>
					                                </table>
				                                </div><!-- /.box-body -->
				                             </div>
				                        </div>
				                   	</div> <!-- End of Row -->

				                   	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Estimation Input</h3>
                   								</div>
                   								<div class="box-body">
                   								<form:form modelAttribute="estimationInput"
													action="${contextPath}/project/create/estimate"
													method="post"
													enctype="multipart/form-data">
<!--	 allowanceList -->
<!--     private TableEstimationAllowance estimationAllowance; -->
<!--     private MultipartFile estimationFile; -->
			                                        <div class="form-group">
			                                        
													<label>Name</label>
													<form:input placeholder="Sample: Project estimate, Estimation" class="form-control" path="name"/>
													<p class="help-block">Enter the name of this estimate</p>
	
			                                        <label>Excel File</label>
													<form:input type="file" class="form-control" path="estimationFile"/>
													<p class="help-block">Choose the Excel file which contains the inputs</p>
													
	                                                <label>Estimation Allowance</label>
	                                                <form:select class="form-control" path="estimationAllowance"> 
                                   						<c:forEach items="${allowanceList}" var="allowance"> 
                                   							<form:option value="${allowance}" label="${allowance.getLabel()}"/> 
                                   						</c:forEach> 
	                                    			</form:select>
	                                    			<p class="help-block">Alot allowance for wastage</p>
	                                    			
													<label>Remarks</label>
													<form:input placeholder="Sample: This is the official estimate" class="form-control" path="remarks"/>
													<p class="help-block">Add additional information</p>
	                                    			
	                                    			<button class="btn btn-cebedo-create btn-flat btn-sm">Estimate</button>
			                                        </div>
		                                        </form:form>
                   								</div>
                   							</div>
                   						</div>
              						</div>

                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_timeline">
									<c:choose>
	                                	<c:when test="${!empty project.assignedTasks}">
	                                		<c:set value="active" var="timelineVisibility"></c:set>
	                                	</c:when>
	                                	<c:when test="${empty project.assignedTasks}">
	                                		<c:set value="hide" var="timelineVisibility"></c:set>
	                                		<c:set value="active" var="tasksVisibility"></c:set>
	                                		<c:set value="hide" var="tasksSummaryVisibility"></c:set>
	                                	</c:when>
	                                </c:choose>
                                	
                                	<div class="nav-tabs-custom">
									<ul class="nav nav-tabs" id="subtabs-timeline">
										<li class="${timelineVisibility}"><a href="#subtab_chart" data-toggle="tab">Chart</a></li>
										<li class="${tasksVisibility}"><a href="#subtab_tasks" data-toggle="tab">Tasks</a></li>
									</ul>
									<div class="tab-content">

									
										<div class="tab-pane ${timelineVisibility}" id="subtab_chart">
											<div class="row">
		                   						<div class="col-md-12">
				                                	<div class="box box-body box-default">
			              								<div class="box-header">
			              									<h3 class="box-title">Timeline</h3>
			              								</div>
						                                <div class="box-body">
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
						                                <div id="gantt-chart" class="gantt-holder">
						                                </div><!-- /.box-body -->
						                                </div>
						                            </div>
					                            </div>
				                            </div>
										</div>
										
										<div class="tab-pane ${tasksVisibility}" id="subtab_tasks">
											<div class="row ${tasksSummaryVisibility}">
		                   						<div class="col-md-6">
		                   							<div class="box box-body box-default">
		                   								<div class="box-header">
		                   									<h3 class="box-title">Summary</h3>
		                   								</div>
		                   								<div class="box-body">
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
		                   						<div class="col-md-6">
		                   							<div class="box box-body box-default">
		                   								<div class="box-header">
		                   									<h3 class="box-title">Graph</h3>
		                   								</div>
		                   								<div class="box-body">
		                   								Pie chart sa division sa summary (awa sa left side)
		                   								</div>
		                   							</div>
		                   						</div>
		              						</div>
	              						<div class="row">
	                   						<div class="col-md-12">
	                   							<div class="box box-body box-default">
	                   								<div class="box-body">
										                	<form:form modelAttribute="massUploadStaffBean"
																action="${contextPath}/project/mass/upload-and-assign/task"
																method="post"
																enctype="multipart/form-data">
															
																<div class="form-group">
																<label>Excel File</label>
																<form:input type="file" class="form-control" path="file"/><br/>
																<button class="btn btn-cebedo-create btn-flat btn-sm">Upload Tasks</button>
																</div>
															</form:form>
						                                	<table>
						                                    	<tr>
						                                    		<td>
						                                    			<c:url value="/project/edit/task/0" var="urlAddTask"/>
						                                    			<a href="${urlAddTask}">
								                                    	<button class="btn btn-cebedo-create btn-flat btn-sm">Create Task</button>
						                                    			</a>
						                                    		</td>
						                                    		<c:if test="${!empty project.assignedTasks}">
						                                    		<td>&nbsp;</td>
						                                    		<td>
				               											<!-- Delete All button -->
								                                        <c:url value="/project/delete/task/all" var="urlButton"/>
								                                        <a href="${urlButton}">
				                										<button class="btn btn-cebedo-unassign-all btn-flat btn-sm">Delete All</button>
								                                        </a>
						                                    		</td>
						                                    		</c:if>
						                                    	</tr>
						                                    </table><br/>
						                                    <table id="tasks-table" class="table table-bordered table-striped">
						                                    	<thead>
						                                            <tr>
							                                        	<th>&nbsp;</th>
							                                            <th>Status</th>
							                                            <th>Start Date</th>
							                                            <th>End Date</th>
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
						                                        					<div class="btn-group">
											                                            <button type="button" class="btn btn-cebedo-update btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">
											                                                Mark As&nbsp;
											                                                <span class="caret"></span>
											                                            </button>
											                                            <ul class="dropdown-menu">
											                                                <li><a href="<c:url value="/project/mark/task/?task_id=${task.id}&status=0"/>">New</a></li>
											                                                <li><a href="<c:url value="/project/mark/task/?task_id=${task.id}&status=1"/>">Ongoing</a></li>
											                                                <li><a href="<c:url value="/project/mark/task/?task_id=${task.id}&status=2"/>">Completed</a></li>
											                                                <li><a href="<c:url value="/project/mark/task/?task_id=${task.id}&status=3"/>">Failed</a></li>
											                                                <li><a href="<c:url value="/project/mark/task/?task_id=${task.id}&status=4"/>">Cancelled</a></li>
											                                            </ul>
											                                        </div>
											                                        
											                                        <!-- View button -->
											                                        <c:url value="/project/edit/task/${task.id}" var="urlViewTask"/>
											                                        <a href="${urlViewTask}">
									                                            	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
											                                        </a>

											                                        <!-- Delete button -->
											                                        <a href="<c:url value="/project/delete/task/${task.id}"/>">
									                                            	<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
											                                        </a>
						                                        				</td>
									                                            <td>
										                                            <c:set value="${task.getStatusEnum().css()}" var="css"></c:set>
																					<span class="label ${css}">${task.getStatusEnum()}</span>
									                                            </td>
									                                            <fmt:formatDate pattern="yyyy/MM/dd" value="${task.dateStart}" var="taskStartDate"/>
									                                            <td>${taskStartDate}</td>
									                                            <fmt:formatDate pattern="yyyy/MM/dd" value="${task.getEndDate()}" var="taskEndDate"/>
									                                            <td>${taskEndDate}</td>
									                                            <td>${task.duration}</td>
									                                            <td>${task.title}</td>
									                                            <td>${task.content}</td>
									                                            <td>
									                                            	<c:choose>
									                                            		<c:when test="${!empty task.staff}">
									                                            			<c:forEach items="${task.staff}" var="taskStaff">
									                                            			<c:set var="taskStaffName" value="${taskStaff.getFullName()}"/>
									                                            			<a class="general-link" href="<c:url value="/project/edit/staff/${taskStaff.id}"/>">
											                                            	${taskStaffName}
											                                            	</a>
											                                            	<br/>
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
										</div>
									</div>
									</div>
                                
                                	
                                	
		                            
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_calendar">
                               	<div class="row">
               						<div class="col-md-12">
               							<div class="box box-body box-default">
               								<div class="box-header">
<!--                    									List<Map<String, String>> getEventTypePropertyMaps -->
               									<h3 class="box-title">Calendar</h3>
               								</div>
               								<div class="box-body">
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
		                            <div class="col-md-12">
               							<div class="box box-body box-default">
               								<div class="box-body">
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
			                                                
			                                                <td>
			                                                <c:url var="urlLink" value="/project/edit/staff/${payrollRow.creator.staff.id}"/>
						                                    <a href="${urlLink}" class="general-link">
			                                                ${payrollRow.creator.staff.getFullName()}
						                                    </a>
			                                                </td>
			                                                
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
               							<div class="box box-body box-default">
               								<div class="box-header">
               									<h3 class="box-title">Graph</h3>
               								</div>
               								<div class="box-body">
               									Line graph here of [release date, total]
               								</div>
               							</div>
               						</div>
               						</div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_inventory">
                                
                                	<c:choose>
                                	<c:when test="${empty materialList}">
                                		<c:set value="hide" var="materialsVisibility"/>
                                		<c:set value="active" var="deliveriesVisibility"/>
                                	</c:when>
                                	<c:when test="${!empty materialList}">
                                		<c:set value="active" var="materialsVisibility"/>
                                	</c:when>
                                	</c:choose>

                                	<c:if test="${empty pullOutList}">
                                		<c:set value="hide" var="pulloutsVisibility"/>
                                	</c:if>

                                	
                                	<div class="nav-tabs-custom">
		                            <ul class="nav nav-tabs" id="subtabs-inventory">
		                                <li class="${materialsVisibility}"><a href="#subtab_inventory" data-toggle="tab">Materials</a></li>
                                		<li class="${deliveriesVisibility}"><a href="#subtab_delivery" data-toggle="tab">Deliveries</a></li>
		                                <li class="${pulloutsVisibility}"><a href="#subtab_pullout" data-toggle="tab">Pull-Outs</a></li>
		                            </ul>
		                            <div class="tab-content">
		                                <div class="tab-pane ${materialsVisibility}" id="subtab_inventory">
	                                	<div class="row">
			                            <div class="col-md-12">
	               							<div class="box box-body box-default">
	               								<div class="box-body box-default">
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
				                                            	<th>Material Category</th>
				                                                <th>Specific Name</th>
				                                                <th>Unit</th>
				                                                <th>Available</th>
				                                                <th>Used / Pulled-Out</th>
				                                            	<th>Total Quantity</th>
				                                                <th>Cost (Per Unit)</th>
				                                                <th>Total Cost</th>
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
				                                            			<c:if test="${row.available > 0}">
				                                            			<c:url var="urlPullout" value="/project/pullout/material/${row.getKey()}-end"/>
									                                    <a href="${urlPullout}">
		                   													<button class="btn btn-cebedo-pullout btn-flat btn-sm">Pull-Out</button>
									                                    </a>
									                                    </c:if>
									                                    <c:url var="urlDelete" value="/project/delete/material/${row.getKey()}-end"/>
									                                    <a href="${urlDelete}">
		                   													<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
									                                    </a>
																	</center>
																</td>
																<td>
																<c:url var="urlLink" value="/project/edit/delivery/${row.delivery.getKey()}-end"/>
							                                    <a href="${urlLink}" class="general-link">
																${row.delivery.name}
							                                    </a>
																</td>
																<td>${row.materialCategory.getLabel()}</td>

																<td>
																	<c:url var="urlLink" value="/project/edit/material/${row.getKey()}-end"/>
								                                    <a href="${urlLink}" class="general-link">
																	${row.name}
								                                    </a>
							                                	</td>

																<td>${row.getUnitName()}</td>
																
																
																<td align="center">
																<div class="progress">
																	<div class="progress-bar progress-bar-${row.getAvailableCSS()} progress-bar-striped" 
																	    role="progressbar" 
																	    aria-valuenow="${row.available}" 
																	    aria-valuemin="0" 
																	    aria-valuemax="${row.quantity}"
																	    style="width:${row.getAvailableAsPercentage()}">
																	    <c:if test="${row.available <= 0}">
																	    	Out of Stock
																	    </c:if>
																    </div>
																</div>
															    <c:if test="${row.available > 0}">
															      ${row.available} (${row.getAvailableAsPercentage()})
															    </c:if>
																</td>
																
																<td align="right">${row.used}</td>
																
																<td align="right">${row.quantity}</td>
																<td align="right">${row.getCostPerUnitMaterialAsString()}</td>
																<td align="right">${row.getTotalCostPerUnitMaterialAsString()}</td>
				                                            </tr>
			                                            	</c:forEach>
					                                    </tbody>
					                                </table>
				                                </div><!-- /.box-body -->
	               							</div>
	               						</div>
	               						</div>
		                                </div>
		                                
		                                
		                                <div class="tab-pane ${deliveriesVisibility}" id="subtab_delivery">
		                                <div class="row">
			                            <div class="col-md-12">
	               							<div class="box box-body box-default">
	               								<div class="box-body">
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
				                                                <th>Delivery</th>
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
																<td align="right">${deliveryRow.getGrandTotalOfMaterialsAsString()}</td>
				                                            </tr>
			                                            	</c:forEach>
					                                    </tbody>
					                                </table>
				                                </div><!-- /.box-body -->
	               							</div>
	               						</div>
	               						</div>
		                                </div>
		                                
		                                
		                                <div class="tab-pane ${pulloutsVisibility}" id="subtab_pullout">
		                                <div class="row">
	               						<div class="col-md-12">
	               							<div class="box box-body box-default">
	               								<div class="box-body">
				                                    <table id="pull-out-table" class="table table-bordered table-striped">
				                                    	<thead>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                                <th>Date and Time</th>
				                                                <th>Delivery</th>
																<th>Material Category</th>
																<th>Specific Name</th>
				                                                <th>Unit</th>
				                                            	<th>Staff</th>
				                                                <th>Quantity</th>
				                                            </tr>
		                                        		</thead>
				                                        <tbody>
					                                		<c:forEach items="${pullOutList}" var="row">
				                                            <tr>
				                                            	<td>
				                                            		<center>
				                                            			<c:url var="urlEdit" value="/project/edit/pullout/${row.getKey()}-end"/>
				                                            			<a href="${urlEdit}">
								                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
				                                            			</a>
									                                    <c:url value="/project/delete/pullout/${row.getKey()}-end" var="urlDelete"/>
									                                    <a href="${urlDelete}">
		                   													<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
									                                    </a>
																	</center>
																</td>
																<fmt:formatDate pattern="yyyy/MM/dd hh:mm a" value="${row.datetime}" var="rowDatetime"/>
																<td>${rowDatetime}</td>
																<td>
																<c:url var="urlLink" value="/project/edit/delivery/${row.delivery.getKey()}-end"/>
							                                    <a href="${urlLink}" class="general-link">
																${row.delivery.name}
							                                    </a>
																</td>
																
																<td>${row.material.materialCategory.getLabel()}</td>
																
																<td>
																<c:url var="urlLink" value="/project/edit/material/${row.material.getKey()}-end"/>
							                                    <a href="${urlLink}" class="general-link">
																${row.material.name}
							                                    </a>
																</td>
																
																<td>${row.material.getUnitName()}</td>
																
																<td>
																<c:url var="urlLink" value="/project/edit/staff/${row.staff.id}"/>
							                                    <a href="${urlLink}" class="general-link">
																${row.staff.getFullName()}
							                                    </a>
																</td>
																
																<td align="right">${row.quantity}</td>
																
				                                            </tr>
			                                            	</c:forEach>
					                                    </tbody>
					                                </table>
				                                </div><!-- /.box-body -->
	               							</div>
	               						</div>
	               						</div>
		                                </div>
		                            </div>
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_estimate">
                                	<div class="box">
<!--                                 		<div class="box-header"> -->
<!--           									<h3 class="box-title">Estimates</h3> -->
<!--           								</div> -->
		                                <div class="box-body table-responsive">
                                   			<c:url var="urlCreate" value="/project/edit/estimate/0-end"/>
                                   			<a href="${urlCreate}">
	                                    	<button class="btn btn-cebedo-create btn-flat btn-sm">Estimate Quantity</button>
                                   			</a>
                                   			<span class="badge bg-green">Step A</span>
                                   			<br/>
                                   			<br/>
                                   			
                                   			<div class="nav-tabs-custom">
				                            <ul class="nav nav-tabs" id="subtabs-estimate">
				                                <li class="active"><a href="#subtab_concrete" data-toggle="tab">Concrete</a></li>
		                                		<li><a href="#subtab_masonry" data-toggle="tab">Masonry</a></li>
		                                		<li><a href="#subtab_cost-estimate-controls" data-toggle="tab">Estimate Cost
		                                		<span class="badge bg-green">Step B</span>
		                                		</a></li>
				                            </ul>
				                            <div class="tab-content">
				                                <div class="tab-pane active" id="subtab_concrete">
				                                <div class="row">
					                            <div class="col-md-12">
			               							<div class="box box-body box-default">
			               							
			               							
			               								<div class="nav-tabs-custom">
							                            <ul class="nav nav-tabs" id="subtabs-inventory">
							                                <li class="active"><a href="#subsubtab_summary" data-toggle="tab">
							                                Component Quantity & Cost
							                                <span class="badge">Cost Results</span>
							                                </a></li>
					                                		<li><a href="#subsubtab_breakdown" data-toggle="tab">
					                                		Component Breakdown
					                                		<span class="badge">Quantity Results</span>
					                                		</a></li>
							                            </ul>
							                            <div class="tab-content">
							                            
							                            	<!-- Subsubtab for Quantity & Cost -->
							                                <div class="tab-pane active" id="subsubtab_summary">
						                                	<div class="row">
								                            <div class="col-md-12">
						               							<div class="box box-body box-default">
<!-- 						               								<div class="box-header"> -->
<!-- 						               									<h3 class="box-title">Cost Estimation</h3> -->
<!-- 						               								</div> -->
						               								<div class="box-body box-default">
													                <button class="btn btn-cebedo-delete btn-flat btn-sm" id="detailsButton">Delete</button>
													                <button class="btn btn-cebedo-compute btn-flat btn-sm" id="detailsButton">Compute</button>
													                <br/><br/>
					<label>Show/Hide Column</label>&nbsp;
					<a class="toggle-vis" data-column="2,3,4,5" data-table="concrete-estimation-summary-table">
					<span class="label btn-success">
					TOTAL QUANTITY / NO. OF UNITS
					</span>
					</a>
					&nbsp;
					<a class="toggle-vis" data-column="6,7,8,9" data-table="concrete-estimation-summary-table">
					<span class="label btn-primary">
					COST PER UNIT
					</span>
					</a>
					&nbsp;
					<a class="toggle-vis" data-column="10,11,12,13" data-table="concrete-estimation-summary-table">
					<span class="label btn-warning">
					TOTAL COST PER COMPONENT
					</span>
					</a>
					&nbsp;
					<a class="toggle-vis" data-column="14,15" data-table="concrete-estimation-summary-table">
					<span class="label btn-danger">
					TOTAL COST
					</span>
					</a>
					<p class="help-block">Click to show/hide</p>
					
													                <form:form modelAttribute="project"
																	id="detailsForm"
																	method="post"
																	action="${contextPath}/project/create/concreteestimationsummary">
													                <table id="concrete-estimation-summary-table" class="table table-bordered table-striped">
								                                        <thead>
								                                        	<tr>
								                                            	<th rowspan="1" colspan="2" style="text-align: center;">
								                                            	<span class="label btn-info">
								                                            	DETAILS
																				</span>
								                                            	</th>
								                                            	
								                                            	<th colspan="4" style="text-align: center;">
								                                            	<a class="toggle-vis" data-column="1,2,3,4" data-table="concrete-estimation-summary-table">
																				<span class="label btn-success">
																				TOTAL QUANTITY / NO. OF UNITS
																				</span>
																				</a>
								                                            	</th>
								                                            	
								                                                <th colspan="4" style="text-align: center;">
								                                                <a class="toggle-vis" data-column="5,6,7,8" data-table="concrete-estimation-summary-table">
																				<span class="label btn-primary">
																				COST PER UNIT
																				</span>
																				</a>
																				</th>
								                                                
								                                                <th colspan="4" style="text-align: center;">
								                                                <a class="toggle-vis" data-column="9,10,11,12" data-table="concrete-estimation-summary-table">
																				<span class="label btn-warning">
																				TOTAL COST PER COMPONENT
																				</span>
																				</a>
								                                                </th>
								                                                
								                                                <th colspan="2" style="text-align: center;">
								                                                <a class="toggle-vis" data-column="13,14" data-table="concrete-estimation-summary-table">
																				<span class="label btn-danger">
																				TOTAL COST
																				</span>
																				</a>
								                                                </th>
								                                            </tr>
								                                            <tr>
								                                            	<th>Name</th>
								                                            	<th>Proportion & Allowance</th>
								                                            	
								                                            	<th>Cement 40kg (bags)</th>
								                                                <th>Cement 50kg (bags)</th>
								                                                <th>Sand (cu.m.)</th>
								                                                <th>Gravel (cu.m.)</th>
								                                            
								                                                <th>Cement 40kg (&#8369;)</th>
								                                                <th>Cement 50kg (&#8369;)</th>
								                                                <th>Sand (&#8369;)</th>
								                                                <th>Gravel (&#8369;)</th>
								                                                
								                                                <th>Cement 40kg (&#8369;)</th>
								                                                <th>Cement 50kg (&#8369;)</th>
								                                                <th>Sand (&#8369;)</th>
								                                                <th>Gravel (&#8369;)</th>
								                                                
								                                                <th>Using Cement 40kg (&#8369;)</th>
								                                                <th>Using Cement 50kg (&#8369;)</th>
								                                            </tr>
								                                        </thead>
								                                        <tbody>
								                                        		<c:forEach items="${concreteEstimationSummaries}" var="summary">
										                                            <tr>
										                                            	<td>
										                                            		<center>
								                                								<form:checkbox path="staffIDs" value="0"/><br/>
										                                            			<c:url var="urlLink" value=""/>
								                                								<a class="general-link" href="${urlLink}">
												                                            		${summary.name}
								                                								</a>
																							</center>
																						</td>
																						
																						<td>
										                                            		<center>
								                                								<c:url var="urlLink" value="/concreteproportion/edit/${summary.concreteProportion.getKey()}-end"/>
								                                								<a class="general-link" href="${urlLink}">
								                                								${summary.concreteProportion.getDisplayName()}
								                                								</a>
								                                								<br/>
								                                								<c:url var="urlLink" value="/estimationallowance/edit/${summary.estimationAllowance.getKey()}-end"/>
								                                								<a class="general-link" href="${urlLink}">
								                                								${summary.estimationAllowance.getDisplayName()}
								                                								</a>
																							</center>
																						</td>
																						
																						<td align="right">${summary.getTotalUnitsCement40kgAsString()}</td>
										                                                <td align="right">${summary.getTotalUnitsCement50kgAsString()}</td>
										                                                <td align="right">${summary.getTotalUnitsSandAsString()}</td>
										                                                <td align="right">${summary.getTotalUnitsGravelAsString()}</td>
										                                                
										                                                <td align="right">${summary.getCostPerUnitCement40kgAsString()}</td>
										                                                <td align="right">${summary.getCostPerUnitCement50kgAsString()}</td>
										                                                <td align="right">${summary.getCostPerUnitSandAsString()}</td>
										                                                <td align="right">${summary.getCostPerUnitGravelAsString()}</td>
										                                                <td align="right">${summary.getTotalCostCement40kgAsString()}</td>
										                                                <td align="right">${summary.getTotalCostCement50kgAsString()}</td>
										                                                <td align="right">${summary.getTotalCostSandAsString()}</td>
										                                                <td align="right">${summary.getTotalCostGravelAsString()}</td>
										                                                <td align="right">${summary.getGrandTotalCostIf40kgAsString()}</td>
										                                                <td align="right">${summary.getGrandTotalCostIf50kgAsString()}</td>
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
						               						
						               						<!-- Subsubtab for Quantity Estimates -->
							                                <div class="tab-pane" id="subsubtab_breakdown">
						                                	<div class="row">
								                            <div class="col-md-12">
						               							<div class="box box-body box-default">
<!-- 						               								<div class="box-header"> -->
<!-- 						               									<h3 class="box-title">Quantity Estimates</h3> -->
<!-- 						               								</div> -->
						               								<div class="box-body box-default">
														                
														                <label>Show/Hide Column</label>&nbsp;
					<a class="toggle-vis" data-column="2,3,4,5" data-table="concrete-table">
					<span class="label btn-success">
					FORMULA INPUTS / ESTIMATION PARAMETERS
					</span>
					</a>
					&nbsp;
					<a class="toggle-vis" data-column="6,7,8,9" data-table="concrete-table">
					<span class="label btn-primary">
					TOTAL QUANTITY / NO. OF UNITS PER COMPONENT
					</span>
					</a>
					<p class="help-block">Click to show/hide</p>
														                
									                                    <table id="concrete-table" class="table table-bordered table-striped">
									                                    	<thead>
									                                    		<tr>
									                                            	<th colspan="2" style="text-align: center;">
									                                            	<span class="label btn-info">
									                                            	DETAILS
																					</span>
									                                            	</th>
									                                                <th colspan="4" style="text-align: center;">
									                                                <a class="toggle-vis" data-column="2,3,4,5" data-table="concrete-table">
																					<span class="label btn-success">
																					FORMULA INPUTS / ESTIMATION PARAMETERS
																					</span>
																					</a>
									                                                </th>
									                                                <th colspan="4" style="text-align: center;">
									                                                <a class="toggle-vis" data-column="6,7,8,9" data-table="concrete-table">
																					<span class="label btn-primary">
																					TOTAL QUANTITY / NO. OF UNITS PER COMPONENT
																					</span>
																					</a>
									                                                </th>
									                                            </tr>
									                                            <tr>
									                                            	<th>&nbsp;</th>
									                                            	<th>Estimation Name</th>
									                                                <th>Concrete Proportion</th>
									                                                <th>Estimation Allowance</th>
									                                                <th>Shape</th>
									                                                <th>Volume Inputs</th>
									                                                <th>Cement 40kg (bags)</th>
									                                                <th>Cement 50kg (bags)</th>
									                                                <th>Sand (cu.m.)</th>
									                                                <th>Gravel (cu.m.)</th>
									                                            </tr>
							                                        		</thead>
									                                        <tbody>
										                                		<c:forEach items="${concreteEstimateList}" var="concrete">
										                                			
										                                			<c:forEach items="${concrete.resultMapConcrete}" var="estimateEntry">
										                                			
										                                            <tr>
										                                            	<!-- Buttons -->
										                                            	<td>
										                                            		<center>
									                                            			<c:url var="urlLink" value="/project/edit/estimate/${concrete.getKey()}-end"/>
									                                            			<a href="${urlLink}">
													                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
									                                            			</a>
														                                    <c:url var="urlLink" value=""/>
														                                    <a href="${urlLink}">
							                   													<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
														                                    </a>
																							</center>
																						</td>
																						
																						<!-- Basic and Inputs -->
																						<td>${concrete.name}</td>
																						
	    																				<c:set value="${estimateEntry.key}" var="estimateKeyProportion"/>
											                                			<c:set value="${estimateEntry.value}" var="estimateValueResult"/>
									                                                	
									                                                	<!-- Multiple fields -->
									                                                	<td>
									                                                	<c:url var="urlLink" value="/concreteproportion/edit/${estimateKeyProportion.getKey()}-end"/>
								                                            			<a href="${urlLink}" class="general-link">
									                                                	${estimateKeyProportion.getDisplayName()}
								                                            			</a>
									                                                	</td>
									                                                	
									                                                	<!-- Estimation Allowance -->
									                                                	<td>
									                                                	<c:url var="urlLink" value="/estimationallowance/edit/${concrete.estimationAllowance.getKey()}-end"/>
								                                            			<a href="${urlLink}" class="general-link">
									                                                	${concrete.estimationAllowance.getDisplayName()}
								                                            			</a>
									                                                	</td>
									                                                	
									                                                	<!-- Shape -->
									                                                	<td>
									                                                	<c:url var="urlLink" value="/shape/edit/${concrete.shape.getKey()}-end"/>
								                                            			<a href="${urlLink}" class="general-link">
									                                                	${concrete.shape.name}
								                                            			</a>
								                                            			<br/>
								                                            			Volume = ${concrete.shape.volumeFormula}
									                                                	</td>
									                                                	
									                                                	<!-- Formula inputs -->
									                                                	<td align="right">
									                                                	<c:forEach items="${concrete.volumeFormulaInputs}" var="input">
									                                                	${input.key} = ${input.value} ${concrete.volumeFormulaInputsUnits.get(input.key).symbol()}<br/>
									                                                	</c:forEach>
									                                                	Volume = ${concrete.shape.volume} cu.m.
									                                                	</td>
									                                                	
	<!--
	Map<ConcreteProportion, ConcreteEstimateResults> resultMapConcrete = new HashMap<ConcreteProportion, ConcreteEstimateResults>();
    Map<CHB, MasonryEstimateResults> resultMapMasonryCHB = new HashMap<CHB, MasonryEstimateResults>(); -->
    
									                                                	
									                                                	<td align="right">${estimateValueResult.getCement40kgAsString()}</td>
									                                                	<td align="right">${estimateValueResult.getCement50kgAsString()}</td>
									                                                	<td align="right">${estimateValueResult.getSandAsString()}</td>
									                                                	<td align="right">${estimateValueResult.getGravelAsString()}</td>
									                                                	
										                                            </tr> <!-- End of one "Estimate" object -->
										                                			</c:forEach> <!-- Loop the "Estimate" according to "Results" -->
										                                            
									                                            </c:forEach> <!-- End of loop of all "Estimate" -->
										                                    </tbody>
										                                </table>
						               								</div>
						               							</div>
						               						</div>
						               						</div>
						               						</div>
						               						
						               					</div>
						               					</div>
			               							
			               							
			               							
				               								
			               							</div>
			               						</div>
			               						</div>
			               						
			               						
			               						
			               						</div>
			               						<div class="tab-pane" id="subtab_masonry">
			                                	<div class="row">
					                            <div class="col-md-12">
			               							<div class="box box-body box-default">
			               								<div class="nav-tabs-custom">
														<ul class="nav nav-tabs">
															<li class="active"><a href="#subsubtab_chb_summary" data-toggle="tab">CHB Quantity & Cost
															<span class="badge">Cost Results</span>
															</a></li>
															<li><a href="#subsubtab_chb" data-toggle="tab">
															CHB Breakdown
															<span class="badge">Quantity Results</span>
															</a></li>
															<li><a href="#subsubtab_block-laying" data-toggle="tab">Block Laying</a></li>
															<li><a href="#subsubtab_paster" data-toggle="tab">Plaster</a></li>
														</ul>
														<div class="tab-content">
															<div class="tab-pane active" id="subsubtab_chb_summary">
																<div class="row">
									                            <div class="col-md-12">
							               							<div class="box box-body box-default">
							               								<div class="box-header">
							               									<h3 class="box-title">Concrete Hollow Blocks (CHB) Quantity & Cost</h3>
							               								</div>
							               								<div class="box-body box-default">
							               								<table id="chb-cost-quantity-table" class="table table-bordered table-striped">
									                                    	<thead>
									                                    		<tr>
									                                            	<th rowspan="1" colspan="4" style="text-align: center;">
									                                            	<span class="label btn-info">
									                                            	DETAILS
									                                            	</span>
									                                            	</th>
									                                                <th colspan="1" style="text-align: center;">
									                                                <span class="label btn-success">
									                                                QUANTITY
									                                            	</span>
									                                                </th>
									                                                <th colspan="2" style="text-align: center;">
									                                                <span class="label btn-danger">
									                                                COST
									                                            	</span>
									                                                </th>
									                                            </tr>
									                                            <tr>
									                                            	<th>&nbsp;</th>
									                                            	<th>Name</th>
									                                            	<th>CHB</th>
									                                            	<th>Estimation Allowance</th>
									                                                <th>No. of Pieces</th>
									                                                <th>Cost per Piece (&#8369;)</th>
									                                                <th>Total Cost (&#8369;)</th>
									                                            </tr>
							                                        		</thead>
									                                        <tbody>
										                                		<c:forEach items="${masonryCHBEstimationSummaries}" var="thisEstimate">
										                                            <tr>
										                                            	<!-- Buttons -->
										                                            	<td>
										                                            		<center>
										                                            			<c:url var="urlLink" value="/project/edit/estimate/${thisEstimate.getKey()}-end"/>
										                                            			<a href="${urlLink}">
														                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
										                                            			</a>
															                                    <c:url var="urlLink" value=""/>
															                                    <a href="${urlLink}">
								                   													<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
															                                    </a>
																							</center>
																						</td>
																						
																						<!-- Name -->
																						<td>
																						${thisEstimate.name}
																						</td>
																						
																						<!-- CHB -->
																						<td>
																						<c:url var="urlLink" value="/chb/edit/${thisEstimate.chbMeasurement.getKey()}-end"/>
								                                            			<a href="${urlLink}" class="general-link">
																						${thisEstimate.chbMeasurement.name}
								                                            			</a><br/>
								                                            			(${thisEstimate.chbMeasurement.getPerSqM()} CHB/sq.m.)
																						</td>
																						
																						<!-- Estimation Allowance -->
																						<td>
																						<c:url var="urlLink" value="/estimationallowance/edit/${thisEstimate.estimationAllowance.getKey()}-end"/>
								                                            			<a href="${urlLink}" class="general-link">
																						${thisEstimate.estimationAllowance.getDisplayName()}
								                                            			</a>
																						</td>
																						
									                                                	<td align="right">${thisEstimate.getTotalPiecesCHBAsString()}</td>
									                                                	<td align="right">${thisEstimate.getCostPerPieceCHBAsString()}</td>
									                                                	<td align="right">${thisEstimate.getTotalCostOfCHBAsString()}</td>
    																					
										                                            </tr> <!-- End of one "Estimate" -->
									                                            </c:forEach> <!-- Loop all "Estimate" -->
										                                    </tbody>
										                                </table>
							               								</div>
							               							</div>
							               						</div>
							               						</div>
															</div>
															<div class="tab-pane" id="subsubtab_chb">
																<div class="row">
									                            <div class="col-md-12">
							               							<div class="box box-body box-default">
							               								<div class="box-header">
							               									<h3 class="box-title">Concrete Hollow Blocks (CHB) Quantity Estimate</h3>
							               								</div>
							               								<div class="box-body box-default">
							               								<table id="chb-quantity-table" class="table table-bordered table-striped">
									                                    	<thead>
									                                    		<tr>
									                                            	<th colspan="3" style="text-align: center;">
									                                            	<span class="label btn-info">
									                                            	DETAILS
									                                            	</span>
									                                            	</th>
									                                                <th colspan="4" style="text-align: center;">
									                                                <span class="label btn-success">
									                                                FORMULA INPUTS / ESTIMATION PARAMETERS
									                                            	</span>
									                                                </th>
									                                                <th colspan="1" style="text-align: center;">
									                                                <span class="label btn-primary">
									                                                TOTAL QUANTITY / NO. OF PIECES
									                                            	</span>
									                                                </th>
									                                            </tr>
									                                            <tr>
									                                            	<th>&nbsp;</th>
									                                            	<th>Name</th>
									                                            	<th>Remarks</th>
									                                                <th>Estimation Allowance</th>
									                                                <th>Shape</th>
									                                                <th>Area Inputs</th>
									                                                 <!-- private double totalCHB;
									                                                 private CHB chbMeasurement; -->
									                                                <th>CHB</th>
									                                                <th>No. of Pieces</th>
									                                            </tr>
							                                        		</thead>
									                                        <tbody>
										                                		<c:forEach items="${masonryEstimateList}" var="thisEstimate">
										                                			<c:forEach items="${thisEstimate.resultMapMasonryCHB}" var="masonryEntry">
										                                            <tr>
										                                            	<td>
										                                            		<center>
										                                            			<c:url var="urlLink" value="/project/edit/estimate/${thisEstimate.getKey()}-end"/>
										                                            			<a href="${urlLink}">
														                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
										                                            			</a>
															                                    <c:url var="urlLink" value=""/>
															                                    <a href="${urlLink}">
								                   													<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
															                                    </a>
																							</center>
																						</td>
																						<td>${thisEstimate.name}</td>
																						<td>${thisEstimate.remarks}</td>
																						
									                                                	<!-- Estimation Allowance -->
									                                                	<td>
									                                                	<c:url var="urlLink" value="/estimationallowance/edit/${thisEstimate.estimationAllowance.getKey()}-end"/>
								                                            			<a href="${urlLink}" class="general-link">
									                                                	${thisEstimate.estimationAllowance.getDisplayName()}
								                                            			</a>
									                                                	</td>
									                                                	
									                                                	<!-- Shape -->
									                                                	<td>
									                                                	<c:url var="urlLink" value="/shape/edit/${thisEstimate.shape.getKey()}-end"/>
								                                            			<a href="${urlLink}" class="general-link">
									                                                	${thisEstimate.shape.name}
								                                            			</a><br/>
								                                            			Area = ${thisEstimate.shape.areaFormula}
									                                                	</td>
									                                                	
									                                                	<!-- Formula inputs -->
									                                                	<td align="right">
									                                                	<c:forEach items="${thisEstimate.areaFormulaInputs}" var="input">
									                                                	${input.key} = ${input.value} ${thisEstimate.areaFormulaInputsUnits.get(input.key).symbol()}<br/>
									                                                	</c:forEach> <!-- End of loop of all formula inputs -->
									                                                	Area = ${thisEstimate.shape.area} sq.m.
									                                                	</td>
									                                                	
    <!-- Map<CHB, MasonryEstimateResults> resultMapMasonryCHB = new HashMap<CHB, MasonryEstimateResults>(); -->
    																					<c:set value="${masonryEntry.key}" var="estimateKeyCHB"></c:set>
    																					<c:set value="${masonryEntry.value}" var="estimateValueResult"></c:set>
									                                                	<td>
									                                                	<c:url var="urlLink" value="/chb/edit/${estimateKeyCHB.getKey()}-end"/>
								                                            			<a href="${urlLink}" class="general-link">
									                                                	${estimateKeyCHB.name}
								                                            			</a><br/>
								                                            			(${estimateKeyCHB.getPerSqM()} CHB/sq.m.)
									                                                	</td>
									                                                	<td align="right">${estimateValueResult.getTotalCHBAsString()}</td>
    																					
										                                            </tr> <!-- End of one "Estimate" -->
   																					</c:forEach> <!-- Loop all "Estimate" according to "Result" -->
									                                            </c:forEach> <!-- Loop all "Estimate" -->
										                                    </tbody>
										                                </table>
							               								</div>
							               							</div>
							               						</div>
							               						</div>
															</div>
															<div class="tab-pane" id="subsubtab_block-laying">
																<div class="row">
									                            <div class="col-md-12">
							               							<div class="box box-body box-default">
							               								<div class="box-body box-default">
							               								
							               								
							               								</div>
							               							</div>
							               						</div>
							               						</div>
															</div>
															<div class="tab-pane" id="subsubtab_paster">
																<div class="row">
									                            <div class="col-md-12">
							               							<div class="box box-body box-default">
							               								<div class="box-body box-default">
							               								
							               								
							               								</div>
							               							</div>
							               						</div>
							               						</div>
															</div>
														</div>
														</div>
			               							</div>
			               						</div>
			               						</div>
			               						</div>
			               						
			               						
			               						<!-- // Basic details.
											    private String name;
											    private String description;
											    private String[] estimationToCompute;
											
											    // Concrete estimation.
											    private double costPerUnitCement40kg;
											    private double costPerUnitCement50kg;
											    private double costPerUnitSand;
											    private double costPerUnitGravel;
											
											    // Masonry estimation.
											    private double costPerPieceCHB; -->
			               						
			               						<div class="tab-pane" id="subtab_cost-estimate-controls">
<%-- 			               						<form modelAttribute="costEstimationBean" --%>
<%-- 														id="detailsForm" --%>
<%-- 														method="post" --%>
<%-- 														action="${contextPath}/project/create/costestimation"> --%>
<!-- 			                                	<div class="row"> -->
<!-- 					                            <div class="col-md-4"> -->
<!-- 			               							<div class="box box-body box-default"> -->
<!-- 			               								<div class="box-header"> -->
<!-- 			               									<h3 class="box-title"> -->
<!-- 			               									<span class="badge bg-blue">Input</span> -->
<!-- 			               									Basic Details</h3> -->
<!-- 			               								</div> -->
<!-- 			               								<div class="box-body"> -->
<!-- 				                                        <div class="form-group"> -->
				                                        
<!-- 			                                            <label>Name</label> -->
<%-- 			                                            <form:input type="text" placeholder="Sample: Summary of all Class C Estimates" class="form-control" path="name"/> --%>
<!-- 			                                            <p class="help-block">Enter the name of this estimate summary</p> -->
			                                            
<!-- 			                                            <label>Description</label> -->
<%-- 			                                            <form:input type="text" placeholder="Sample: To be used for comparison" class="form-control" path="description"/> --%>
<!-- 					                                    <p class="help-block">Add a description to this summary</p> -->
					                                    
<!-- 				                                        </div> -->
<!-- 			               								</div> -->
<!-- 			               							</div> -->
<!-- 			               						</div> -->
<!-- 			               						<div class="col-md-4"> -->
<!-- 			               							<div class="box box-body box-default"> -->
			               								
<!-- 			               								<div class="box-header"> -->
<!-- 			               									<h3 class="box-title"> -->
<!-- 			               									<span class="badge bg-blue">Input</span> -->
<!-- 			               									Concrete Cost</h3> -->
<!-- 			               								</div> -->
<!-- 			               								<div class="box-body"> -->
<!-- 					                                        <div class="form-group"> -->
<!-- 						                                    <label>Cost per Unit (Cement 40kg)</label> -->
<%-- 				                                            <form:input type="text" placeholder="Sample: 200, 220, 250, 300" class="form-control" path="costPerUnitCement40kg"/> --%>
<!-- 						                                    <p class="help-block">Specify the cost per 40kg bag of Cement</p> -->
						                                    
<!-- 						                                    <label>Cost per Unit (Cement 50kg)</label> -->
<%-- 				                                            <form:input type="text" placeholder="Sample: 200, 220, 250, 300" class="form-control" path="costPerUnitCement50kg"/> --%>
<!-- 						                                    <p class="help-block">Specify the cost per 50kg bag of Cement</p> -->
						                                    
<!-- 						                                    <label>Cost per Unit (Sand)</label> -->
<%-- 				                                            <form:input type="text" placeholder="Sample: 400, 500, 550, 650" class="form-control" path="costPerUnitSand"/> --%>
<!-- 						                                    <p class="help-block">Specify the cost per unit of Sand</p> -->
						                                    
<!-- 						                                    <label>Cost per Unit (Gravel)</label> -->
<%-- 				                                            <form:input type="text" placeholder="Sample: 400, 500, 550, 650" class="form-control" path="costPerUnitGravel"/> --%>
<!-- 						                                    <p class="help-block">Specify the cost per unit of Gravel</p> -->
						                                    
<!-- 					                                        </div> -->
<!-- 			               								</div> -->
			               								
			               								
<!-- 			               							</div> -->
<!-- 			               						</div> -->
<!-- 			               						<div class="col-md-4"> -->
<!-- 			               							<div class="box box-body box-default"> -->
<!-- 			               								<div class="box-header"> -->
<!-- 			               									<h3 class="box-title"> -->
<!-- 			               									<span class="badge bg-blue">Input</span> -->
<!-- 			               									Masonry Cost</h3> -->
<!-- 			               								</div> -->
<!-- 			               								<div class="box-body"> -->
<!-- 				                                        <div class="form-group"> -->
				                                        
<!-- 			                                            <label>Cost per Piece of Concrete Hollow Block (CHB)</label> -->
<%-- 			                                            <form:input type="text" placeholder="Sample: Summary of all Class C Estimates" class="form-control" path="costPerPieceCHB"/> --%>
<!-- 			                                            <p class="help-block">Enter the name of this estimate summary</p> -->
<!-- 				                                        </div> -->
<!-- 			               								</div> -->
<!-- 			               							</div> -->
<!-- 			               						</div> -->
<!-- 			               						</div> -->
			               						
<!-- 			               						<div class="row"> -->
<!-- 			               						<div class="col-md-12"> -->
<!-- 			               							<div class="box box-body box-default"> -->
<!-- 			               								<div class="box-header"> -->
<!-- 			               									<h3 class="box-title"> -->
<!-- 			               									<span class="badge bg-blue">Input</span> -->
<!-- 			               									Quantity Estimations</h3> -->
<!-- 			               								</div> -->
<!-- 			               								<div class="box-body"> -->
										                
<!-- 					                                        <div class="form-group"> -->
					                                        
<!-- 						                                    <table id="form-estimate-cost" class="table table-bordered table-striped"> -->
<!-- 						                                    <thead> -->
<!-- 						                                    <tr> -->
<!-- 											                	<th colspan="3" style="text-align: center;"> -->
<!-- 											                	<span class="label btn-info"> -->
<!-- 											                	DETAILS -->
<!-- 											                	</span> -->
<!-- 											                	</th> -->
											                	
<!-- 											                	<th colspan="3" style="text-align: center;"> -->
<!-- 											                	<span class="label btn-success"> -->
<!-- 																FORMULA INPUTS / ESTIMATION PARAMETERS -->
<!-- 																</span> -->
<!-- 											                	</th> -->
											                	
<!-- 											                	<th rowspan="2" colspan="1" style="text-align: center; vertical-align: middle;"> -->
<!-- 											                	<span class="label btn-danger"> -->
<!-- 											                	TYPES OF ESTIMATIONS -->
<!-- 											                	</span> -->
<!-- 											                	</th> -->
<!-- 											                </tr> -->
<!-- 											                <tr> -->
<!-- 											                	<th>Check / Uncheck</th> -->
<!-- 											                	<th>Name</th> -->
<!-- 											                	<th>Remarks</th> -->
<!-- 											                	<th>Shape</th> -->
<!-- 											                	<th>Area Inputs</th> -->
<!-- 											                	<th>Volume Inputs</th> -->
<!-- 											                </tr> -->
<!-- 						                                    </thead> -->
<!-- 						                                    <tbody> -->
<%--                                      						<c:forEach items="${combinedEstimateList}" var="quantityEstimate">  --%>
<!-- 						                                    <tr> -->
<!-- 						                                    	<td align="center"> -->
<%--                                      							<form:checkbox path="estimationToCompute" class="form-control" value="${quantityEstimate.getKey()}"/> --%>
<!-- 						                                    	</td> -->
<!-- 						                                    	<td> -->
<%-- 						                                    	<c:url var="urlLink" value="/project/edit/estimate/${quantityEstimate.getKey()}-end"/> --%>
<%-- 		                                            			<a href="${urlLink}" class="general-link"> --%>
<%-- 						                                    	${quantityEstimate.name} --%>
<!-- 		                                            			</a> -->
<!-- 						                                    	</td> -->
						                                    	
<!-- 						                                    	<td> -->
<%-- 						                                    	${quantityEstimate.remarks} --%>
<!-- 						                                    	</td> -->
						                                    	
<!-- 						                                    	<td> -->
<%-- 						                                    	<c:url var="urlLink" value="/shape/edit/${quantityEstimate.shape.getKey()}-end"/> --%>
<%-- 		                                            			<a href="${urlLink}" class="general-link"> --%>
<%-- 						                                    	${quantityEstimate.shape.name} --%>
<!-- 		                                            			</a><br/> -->
<%-- 		                                            			Area = ${quantityEstimate.shape.areaFormula} --%>
<!-- 		                                            			<br/> -->
<%-- 		                                            			Volume = ${quantityEstimate.shape.volumeFormula} --%>
<!-- 						                                    	</td> -->
						                                    	
<!-- 						                                    	<td align="right"> -->
<%-- 						                                    	<c:forEach items="${quantityEstimate.areaFormulaInputs}" var="input"> --%>
<%-- 			                                                	${input.key} = ${input.value} ${quantityEstimate.areaFormulaInputsUnits.get(input.key).symbol()}<br/> --%>
<%-- 			                                                	</c:forEach> --%>
<%-- 			                                                	Area = ${quantityEstimate.shape.area} sq.m. --%>
<!-- 						                                    	</td> -->
						                                    	
<!-- 						                                    	<td align="right"> -->
<%-- 						                                    	<c:forEach items="${quantityEstimate.volumeFormulaInputs}" var="input"> --%>
<%-- 			                                                	${input.key} = ${input.value} ${quantityEstimate.volumeFormulaInputsUnits.get(input.key).symbol()}<br/> --%>
<%-- 			                                                	</c:forEach> --%>
<%-- 			                                                	Volume = ${quantityEstimate.shape.volume} cu.m. --%>
<!-- 						                                    	</td> -->
						                                    	
<!-- 						                                    	<td> -->
<%-- 						                                    	<c:forEach items="${quantityEstimate.estimateTypes}" var="estimateType"> --%>
<%-- 						                                    	- ${estimateType.label()}<br/> --%>
<%-- 						                                    	</c:forEach> --%>
<!-- 						                                    	</td> -->
<!-- 						                                    </tr> -->
<%--                                      						</c:forEach>  --%>
<!-- 						                                    </tbody> -->
<!-- 											                </table> -->
						                                    
<!-- 		                                            		<button class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton">Estimate Costs</button> -->
<!-- 					                                        </div> -->
					                                    
<!-- 			               								</div> -->
			               								
			               								
<!-- 			               							</div> -->
<!-- 			               						</div> -->
<!-- 			               						</div> -->
<%-- 			               						</form> --%>
			               						</div>
			               					</div>
			               					</div>
                                   			
			                                
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_staff">

                                	<c:choose>
	                                	<c:when test="${!empty project.assignedStaff}">
	                                		<c:set value="active" var="membersVisibility"></c:set>
	                                	</c:when>
	                                	<c:when test="${empty project.assignedStaff}">
	                                		<c:set value="hide" var="membersVisibility"></c:set>
	                                		<c:set value="active" var="assignVisibility"></c:set>
	                                	</c:when>
	                                </c:choose>
                                
                                	<div class="nav-tabs-custom">
									<ul class="nav nav-tabs" id="subtabs-staff">
										<li class="${membersVisibility}"><a href="#subtab_members" data-toggle="tab">Members</a></li>
										<li class="${assignVisibility}"><a href="#subtab_controls" data-toggle="tab">Assign</a></li>
									</ul>
									<div class="tab-content">
										<div class="tab-pane ${membersVisibility}" id="subtab_members">
											<div class="row">
		                   						<div class="col-md-12">
				                                	<div class="box box-body box-default">
<!-- 				                                		<div class="box-header"> -->
<!-- 			              									<h3 class="box-title">Staff Members</h3> -->
<!-- 			              								</div> -->
						                                <div class="box-body">
											                <c:if test="${!empty project.assignedStaff}">
		              											<c:url value="/project/unassign/staff-member/all" var="urlUnassignStaffAll"/>
							                                    <a href="${urlUnassignStaffAll}">
		              												<button class="btn btn-cebedo-unassign-all btn-flat btn-sm">Unassign All</button>
							                                    </a>
																<br/>
																<br/>
				                                    		</c:if>

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
							                                            			<c:url var="urlViewStaff" value="/project/edit/staff/${assignedStaffMember.id}"/>
							                                            			<a href="${urlViewStaff}">
											                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
							                                            			</a>
												                                    <c:url value="/project/unassign/staff-member/${assignedStaffMember.id}" var="urlUnassignStaff"/>
												                                    <a href="${urlUnassignStaff}">
					                   													<button class="btn btn-cebedo-unassign btn-flat btn-sm">Unassign</button>
												                                    </a>
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
										</div>
										<div class="tab-pane ${assignVisibility}" id="subtab_controls">
						                   	<div class="row">
		                   						<div class="col-md-12">
				                                	<div class="box box-body box-default">
<!-- 			              								<div class="box-header"> -->
<!-- 			              									<h3 class="box-title">Staff Assignment Controls</h3> -->
<!-- 			              								</div> -->
						                                <div class="box-body">
				                                    		<form:form modelAttribute="massUploadStaffBean"
																action="${contextPath}/project/mass/upload-and-assign/staff"
																method="post"
																enctype="multipart/form-data">
															
																<div class="form-group">
																<label>Excel File</label>
																<form:input type="file" class="form-control" path="file"/><br/>
																<button class="btn btn-cebedo-create btn-flat btn-sm">Upload and Assign</button>
																</div>
															</form:form>
															
			                                    			<a href="<c:url value="/project/edit/staff/0"/>">
					                                    	<button class="btn btn-cebedo-create btn-flat btn-sm">Create Staff</button>
			                                    			</a>
				                                    		
				                                    		<c:if test="${!empty availableStaffToAssign}">
				                                    		<button onclick="submitForm('assignStaffForm')" class="btn btn-cebedo-assign btn-flat btn-sm">Assign</button>
				                                    		</c:if>

				                                    		<c:if test="${!empty availableStaffToAssign}">
				                                    		&nbsp;&nbsp;
				                                    		<a href="#" onclick="checkAll('include-checkbox')" class="general-link">Check All</a>&nbsp;
															<a href="#" onclick="uncheckAll('include-checkbox')" class="general-link">Uncheck All</a>
							                                <br/>
							                                <br/>

					                                    	<form:form modelAttribute="project" 
							                                    method="post" 
							                                    id="assignStaffForm"
							                                    action="${contextPath}/project/assign/staff/mass">
				                                    		<div class="form-group">
				                                    		<table class="table table-bordered table-striped">
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
						                                    			<td>
					                                            			<a href="<c:url value="/project/edit/staff/${staff.id}"/>" class="general-link">
						                                    				${staff.getFullName()}
					                                            			</a>
						                                    			</td>
						                                    			<td>${staff.companyPosition}</td>
					                                                	<td>${staff.getWageAsString()}</td>
					                                                	<td>${staff.email}</td>
					                                                	<td>${staff.contactNumber}</td>
						                                    			</tr>
						                                    		</c:forEach>
				                                    			</tbody>
				                                    		</table>
				                                    		</div>
						                                    </form:form>

				                                    		</c:if>

						                                </div>
						                             </div>
						                        </div>
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
<!-- 	<script src="https://cdn.datatables.net/1.10.7/js/jquery.dataTables.min.js" type="text/javascript"></script> -->

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
   	<script type="text/javascript">
   	$(document).ready(function() {
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
	</c:if>
	
	<script type="text/javascript">
	    
	    // TODO Not sure if we still need the below code.
		$(document).ready(function() {
		    var estimationTable = $('#concrete-estimation-summary-table').DataTable();
		    var concreteBreakdownTable = $("#concrete-table").DataTable();
		 
		 	// Hide/show data table columns.
		    $('a.toggle-vis').on( 'click', function (e) {
		        e.preventDefault();
		 
		        // Get the column API object.
		        // Loop through each ID in data-column attr.
		        // Hide each.
		        var dataColIDs = $(this).attr('data-column');
		        var colsToHide = dataColIDs.split(",");
		        var dataTable = $(this).attr('data-table');
		        var targetTable;

		        if(dataTable == "concrete-estimation-summary-table"){
		        	targetTable = estimationTable;
		        } else if(dataTable == "concrete-table"){
		        	targetTable = concreteBreakdownTable;
		        }
		        
		        for(var i = 0; i < colsToHide.length; i++){
			        var column = targetTable.column(colsToHide[i]);
			 
			        // Toggle the visibility
			        column.visible( ! column.visible() );
		        }
		        
		    });
		    
		    // Hide the column TOTAL QUANTITY / NO. OF UNITS.
		    var dataColIDs = "2,3,4,5";
	        var colsToHide = dataColIDs.split(",");
	        for(var i = 0; i < colsToHide.length; i++){
		        var column = estimationTable.column(colsToHide[i]);
		        column.visible( ! column.visible() );
	        }
		});
		
		// Data tables.
		$(document).ready(function() {
			$("#chb-cost-quantity-table").dataTable();
			$("#chb-quantity-table").dataTable();
			$("#form-estimate-cost").dataTable();
			$("#material-table").dataTable();
			$("#pull-out-table").dataTable();
			$("#delivery-table").dataTable();
			$("#payroll-table").dataTable();
			$("#managers-table").dataTable();
			$("#assigned-staff-table").dataTable();
			$("#tasks-table").dataTable();
			$(".is-data-table").dataTable();
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