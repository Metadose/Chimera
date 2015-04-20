<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Team ${action}</title>
	
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
	                ${team.name}
	                <small>${action} Team</small>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                                <li><a href="#tab_3" data-toggle="tab">Members</a></li>
                                <li><a href="#tab_2" data-toggle="tab">Tasks</a></li>
                                <li><a href="#tab_7" data-toggle="tab">Projects</a></li>
                                <li><a href="#tab_timeline" data-toggle="tab">Timeline</a></li>
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
                   									<c:set var="detailsFormURL" value="${contextPath}/team/create"/>
				                                    <c:if test="${!empty origin && !empty originID}">
				                                    	<c:set var="detailsFormURL" value="${contextPath}/team/create/from/origin"/>
				                                    </c:if>
                   									<form role="form" name="detailsForm" id="detailsForm" method="post" action="${detailsFormURL}">
                   										<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                   										<c:if test="${!empty origin && !empty originID}">
				                                    	<input type="hidden" name="origin" value="${origin}"/>
				                                    	<input type="hidden" name="originID" value="${originID}"/>
					                                    </c:if>
				                                        <div class="form-group">
				                                        	<input type="hidden" name="id" value="${team.id}"/>
				                                            <label>Name</label>
				                                            <input type="text" class="form-control" name="name" value="${team.name}"/><br/>
				                                        </div>
				                                    </form>
				                                    <c:choose>
		                                            	<c:when test="${team.id == 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${team.id > 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<sec:authorize access="hasRole('ROLE_TEAM_EDITOR')">
		                                            		<a href="${contextPath}/team/delete/${team.id}">
																<button class="btn btn-default btn-flat btn-sm">Delete This Team</button>
															</a>
															</sec:authorize>
		                                            	</c:when>
		                                            </c:choose>
                   								</div>
                   							</div>
                   						</div>
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Fields</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="form-group">
                   										<table>
															<tr>
																<td style="padding-bottom: 3px;">
																	<input type="text" class="form-control" value="BIR Number">
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<input type="text" class="form-control" value="202-123-345-123">
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<button class="btn btn-default btn-flat btn-sm">Remove</button>
																</td>
															</tr>
															<tr>
																<td style="padding-bottom: 3px;">
																	<input type="text" class="form-control" value="Manpower">
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<input type="text" class="form-control" value="ABC Services Inc.">
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<button class="btn btn-default btn-flat btn-sm">Remove</button>
																</td>
															</tr>
														</table>
														<br/>
														<button class="btn btn-default btn-flat btn-sm">Clear All</button>
														<br/>
														<br/>
														<h4>Assign More Fields</h4>
														<table>
															<tr>
																<td style="padding-right: 3px;">
																	<label>Label</label>
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<input type="text" class="form-control" placeholder="Example: SSS, Building Permit No., Sub-contractor, etc...">
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
																	<input type="text" class="form-control" placeholder="Example: 000-123-456, AEE-123, OneForce Construction, etc...">
																</td>
															</tr>
														</table>
                                            			<button class="btn btn-default btn-flat btn-sm">Assign</button>
			                                        </div>
                   								</div>
                   							</div>
                   						</div>
              						</div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_3">
                                	<div class="box">
		                                <div class="box-body table-responsive">
		                                	<table>
		                                    	<tr>
		                                    		<sec:authorize access="hasRole('ROLE_STAFF_EDITOR')">
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/staff/edit/0">
		                                    			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				                                    	<button class="btn btn-default btn-flat btn-sm">Create Staff</button>
					                                    </form>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		</sec:authorize>
		                                    		<form method="post" action="${contextPath}/staff/assign/team">
		                                    		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		                                    		<td>
		                                    			<select class="form-control" name="staff_id">
		                                    				<c:choose>
		                                    					<c:when test="${!empty staffList}">
		                                    						<c:forEach items="${staffList}" var="staff">
		                                    							<c:set var="staffName" value="${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}"/>
		                                    							<option value="${staff.id}">${staffName}</option>
		                                    						</c:forEach>
		                                    					</c:when>
		                                    				</c:choose>
		                                    			</select>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<input type="hidden" name="origin" value="team"/>
		                                    			<input type="hidden" name="originID" value="${team.id}"/>
		                                    			<input type="hidden" name="team_id" value="${team.id}"/>
														<button class="btn btn-default btn-flat btn-sm">Assign</button>
		                                    		</td>
		                                    		</form>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/team/unassign/staff/all">
		                                    			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
														<input type="hidden" name="team_id" value="${team.id}"/>
														<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
														</form>
		                                    		</td>
		                                    	</tr>
		                                    </table><br/>
		                                    <table id="members-table" class="table table-bordered table-striped">
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
			                                        <c:set var="assignmentList" value="${team.members}"/>
				                                	<c:if test="${!empty assignmentList}">
				                                		<c:forEach items="${assignmentList}" var="member">
			                                            <tr>
			                                            	<td>
			                                            		<center>
																	<a href="${contextPath}/staff/edit/${member.id}">
																		<button class="btn btn-default btn-flat btn-sm">View</button>
																	</a>
																	<form method="post" action="${contextPath}/staff/unassign/team">
																		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
																		<input type="hidden" name="staff_id" value="${member.id}"/>
																		<input type="hidden" name="team_id" value="${team.id}"/>
																		<input type="hidden" name="origin" value="team"/>
																		<button class="btn btn-default btn-flat btn-sm">Unassign</button>
																	</form>
																</center>
															</td>
			                                                <td>
			                                                	<div class="user-panel">
													            <div class="pull-left image">
													                <c:choose>
		                                                			<c:when test="${!empty member.thumbnailURL}">
		                                                				<img src="${contextPath}/image/display/staff/profile/?staff_id=${member.id}" class="img-circle"/>
		                                                			</c:when>
		                                                			<c:when test="${empty member.thumbnailURL}">
		                                                				<img src="${contextPath}/resources/img/avatar5.png" class="img-circle">
		                                                			</c:when>
			                                                		</c:choose>
													            </div>
														        </div>
			                                                </td>
			                                                <td>${member.prefix} ${member.firstName} ${member.middleName} ${member.lastName} ${member.suffix}</td>
			                                                <td>${member.companyPosition}</td>
			                                                <td>${member.email}</td>
			                                                <td>${member.contactNumber}</td>
			                                            </tr>
		                                            </c:forEach>
	                                        		</c:if>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_7">
                                	<div class="box">
		                                <div class="box-body table-responsive">
		                                	<table>
		                                    	<tr>
		                                    		<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/project/edit/0">
		                                    			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				                                    	<button class="btn btn-default btn-flat btn-sm">Create Project</button>
					                                    </form>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		</sec:authorize>
		                                    		<form method="post" action="${contextPath}/team/assign/project">
		                                    		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		                                    		<td>
		                                    			<select class="form-control" name="project_id">
		                                    				<c:choose>
		                                    					<c:when test="${!empty projectList}">
		                                    						<c:forEach items="${projectList}" var="project">
		                                    							<option value="${project.id}">${project.name}</option>
		                                    						</c:forEach>
		                                    					</c:when>
		                                    				</c:choose>
		                                    			</select>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<input type="hidden" name="origin" value="team"/>
		                                    			<input type="hidden" name="originID" value="${team.id}"/>
		                                    			<input type="hidden" name="team_id" value="${team.id}"/>
														<button class="btn btn-default btn-flat btn-sm">Assign</button>
		                                    		</td>
		                                    		</form>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/team/unassign/all/project">
		                                    			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
														<input type="hidden" name="team_id" value="${team.id}"/>
														<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
														</form>
		                                    		</td>
		                                    	</tr>
		                                    </table><br/>
		                                	<table id="project-table" class="table table-bordered table-striped">
		                                    	<thead>
		                                    		<tr>
			                                        	<th>&nbsp;</th>
		                                                <th>Project</th>
		                                                <th>Location</th>
		                                                <th>Notes</th>
			                                        </tr>
		                                    	</thead>
		                                        <tbody>
			                                        <c:set var="assignmentList" value="${team.projects}"/>
				                                	<c:if test="${!empty assignmentList}">
				                                		<c:forEach items="${assignmentList}" var="project">
			                                            <tr>
			                                            	<td>
			                                            		<center>
																	<a href="${contextPath}/project/edit/${member.id}">
																		<button class="btn btn-default btn-flat btn-sm">View</button>
																	</a>
																	<form method="post" action="${contextPath}/team/unassign/project">
																		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
																		<input type="hidden" name="project_id" value="${project.id}"/>
																		<input type="hidden" name="team_id" value="${team.id}"/>
																		<input type="hidden" name="origin" value="team"/>
																		<input type="hidden" name="originID" value="${team.id}"/>
																		<button class="btn btn-default btn-flat btn-sm">Unassign</button>
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
		                                	<a href="${contextPath}/task/assign/team/${team.id}">
		                                		<button class="btn btn-default btn-flat btn-sm">Create Task</button>
		                                	</a>
		                                	<a href="${contextPath}/project/edit/0">
		                                		<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
		                                	</a><br/><br/>
		                                	<table id="task-table" class="table table-bordered table-striped">
		                                    	<thead>
		                                    		<tr>
			                                        	<th>&nbsp;</th>
			                                            <th>Status</th>
			                                            <th>Content</th>
			                                            <th>Project</th>
			                                            <th>Start</th>
			                                            <th>Duration</th>
			                                        </tr>
		                                    	</thead>
		                                        <tbody>
			                                        <c:set var="taskList" value="${team.tasks}"/>
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
							                                                <li><a href="${contextPath}/task/mark/team/?team_id=${team.id}&task_id=${task.id}&status=0">New</a></li>
							                                                <li><a href="${contextPath}/task/mark/team/?team_id=${team.id}&task_id=${task.id}&status=1">Ongoing</a></li>
							                                                <li><a href="${contextPath}/task/mark/team/?team_id=${team.id}&task_id=${task.id}&status=2">Completed</a></li>
							                                                <li><a href="${contextPath}/task/mark/team/?team_id=${team.id}&task_id=${task.id}&status=3">Failed</a></li>
							                                                <li><a href="${contextPath}/task/mark/team/?team_id=${team.id}&task_id=${task.id}&status=4">Cancelled</a></li>
							                                            </ul>
							                                        </div>
							                                        <a href="${contextPath}/task/edit/${task.id}">
					                                            		<button class="btn btn-default btn-flat btn-sm">View</button>
					                                            	</a>
					                                            	<sec:authorize access="hasRole('ROLE_TEAM_EDITOR')">
					                                            	<a href="${contextPath}/task/delete/${task.id}">
					                                            		<button class="btn btn-default btn-flat btn-sm">Delete</button>
					                                            	</a>
					                                            	</sec:authorize>
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
				                                            			<a href="${contextPath}/project/edit/from/team/?${task.project.id}">
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
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
	
	<script src="<c:url value="/resources/js/common.js" />"type="text/javascript"></script>
	
	<c:if test="${team.id != 0}">
	<!-- Generate the data to be used by the gantt. -->
	<c:set var="ganttData" value="'data':[{id:'${team.id}', text:'${fn:escapeXml(team.name)}', open: true, duration:0},"/>
    <c:if test="${!empty team.tasks}">
    	<c:forEach var="task" items="${team.tasks}">
    		<fmt:formatDate pattern="dd-MM-yyyy" value="${task.dateStart}" var="taskDateStart"/>
    		<c:set var="taskRow" value="{id:'${task.id}', status:${task.status}, text:'${fn:escapeXml(task.title)}', content:'${fn:escapeXml(task.content)}', start_date:'${taskDateStart}', open: true, duration:${task.duration}, parent:'${team.id}'},"/>
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
		
		$(document).ready(function() {
			$("#members-table").dataTable();
			$("#project-table").dataTable();
			$("#task-table").dataTable();
	    });
	</script>
</body>
</html>