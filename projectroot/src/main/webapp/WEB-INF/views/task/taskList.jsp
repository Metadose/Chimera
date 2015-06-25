<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Task ${action}</title>
	
	<link href="<c:url value="/resources/css/gantt-custom.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.css" />"rel="stylesheet" type="text/css" />
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                Task ${action}
	                <small>Complete list of all task members</small>
	            </h1>
	        </section>
	        <section class="content">
			<div class="row">
				<div class="col-md-12">
					${uiParamAlert}
					<!-- Custom Tabs -->
					<div class="nav-tabs-custom">
						<ul class="nav nav-tabs">
							<li class="active"><a href="#tab_list" data-toggle="tab">List</a></li>
							<li><a href="#tab_timeline" data-toggle="tab">Timeline</a></li>
						</ul>
						<div class="tab-content">
							<div class="tab-pane active" id="tab_list">
								<div class="row">
									<div class="col-md-12">
										<div class="box">
												<div class="box-header">
				<!--                                     <h3 class="box-title">Data Table With Full Features</h3> -->
												</div><!-- /.box-header -->
												<div class="box-body table-responsive">
													<sec:authorize access="hasRole('ROLE_TASK_EDITOR')">
													<c:url value="/task/edit/0" var="urlCreateTask"/>
				                                	<a href="${urlCreateTask}">
				                                		<button class="btn btn-default btn-flat btn-sm">Create Task</button>
				                                	</a>
				                                	<br/><br/>
				                                	</sec:authorize>
				                                    <table id="example-1" class="table table-bordered table-striped">
				                                        <thead>
				                                            <tr>
				                                            	<th>&nbsp;</th>
					                                            <th>Status</th>
					                                            <th>Content</th>
					                                            <th>Project</th>
					                                            <th>Team</th>
					                                            <th>Staff</th>
					                                            <th>Start</th>
					                                            <th>Duration</th>
				                                            </tr>
				                                        </thead>
				                                        <tbody>
						                                	<c:if test="${!empty taskList}">
				                                        		<c:forEach items="${taskList}" var="task">
				                                        			<tr>
				                                        				<td>
				                                        					<sec:authorize access="hasRole('ROLE_TASK_EDITOR')">
				                                        					<div class="btn-group">
									                                            <button type="button" class="btn btn-default btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">
									                                                Mark As&nbsp;
									                                                <span class="caret"></span>
									                                            </button>
									                                            <ul class="dropdown-menu">
									                                                <li><a href="${contextPath}/task/mark/?task_id=${task.id}&status=0">New</a></li>
									                                                <li><a href="${contextPath}/task/mark/?task_id=${task.id}&status=1">Ongoing</a></li>
									                                                <li><a href="${contextPath}/task/mark/?task_id=${task.id}&status=2">Completed</a></li>
									                                                <li><a href="${contextPath}/task/mark/?task_id=${task.id}&status=3">Failed</a></li>
									                                                <li><a href="${contextPath}/task/mark/?task_id=${task.id}&status=4">Cancelled</a></li>
									                                            </ul>
									                                        </div>
									                                        </sec:authorize>
									                                        <c:url value="/task/edit/${task.id}" var="viewTaskURL"/>
									                                        <a href="${viewTaskURL}">
							                                            		<button class="btn btn-default btn-flat btn-sm">View</button>
							                                            	</a>
							                                            	<sec:authorize access="hasRole('ROLE_TASK_EDITOR')">
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
						                                            			<a href="${contextPath}/project/edit/${task.project.id}">
								                                            		<button class="btn btn-default btn-flat btn-sm">View</button>&nbsp;&nbsp;
								                                            	</a>
								                                            	${task.project.name}
						                                            		</c:when>
						                                            		<c:when test="${empty task.project}">
						                                            			<h6>No project assigned.</h6>
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
						                                            			</c:forEach>
						                                            		</c:when>
						                                            		<c:when test="${empty task.teams}">
						                                            			<h6>No team assigned.</h6>
						                                            		</c:when>
							                                            	</c:choose>
							                                            </td>
							                                            <td>
							                                            	<c:choose>
						                                            		<c:when test="${!empty task.staff}">
						                                            			<c:forEach items="${task.staff}" var="taskStaffMember">
						                                            				<c:set var="taskStaff" value="${taskStaffMember}"/>
										                                            <c:set var="taskStaffName" value="${taskStaff.getFullName()}"/>
									                                            	<a href="${contextPath}/staff/edit/${taskStaff.id}">
									                                            		<button class="btn btn-default btn-flat btn-sm">View</button>&nbsp;&nbsp;
									                                            	</a>
									                                            	${taskStaffName}
						                                            			</c:forEach>
						                                            		</c:when>
						                                            		<c:when test="${empty task.staff}">
						                                            			<h6>No staff assigned.</h6>
						                                            		</c:when>
							                                            	</c:choose>
							                                            </td>
							                                            <td><fmt:formatDate pattern="yyyy/MM/dd" value="${task.dateStart}" /></td>
							                                            <td>${task.duration}</td>
							                                        </tr>
				                                        		</c:forEach>
				                                       		</c:if>
					                                    </tbody>
				                                        <tfoot>
				                                            <tr>
				                                            	<th>&nbsp;</th>
					                                            <th>Status</th>
					                                            <th>Content</th>
					                                            <th>Project</th>
					                                            <th>Team</th>
					                                            <th>Staff</th>
					                                            <th>Start</th>
					                                            <th>Duration</th>
				                                            </tr>
				                                        </tfoot>
				                                    </table>
				                                </div><!-- /.box-body -->
											</div><!-- /.box -->
									</div>
								</div>
							</div>
							<div class="tab-pane" id="tab_timeline">
								<div class="row">
									<div class="col-md-12">
										<div class="box box-body box-default">
											<div class="box-header">
			<!-- 													<h3 class="box-title">Staff Tasks & Schedules</h3> -->
											</div>
											<div class="box-body">
												<div id="gantt-chart" style='width:100%; height:100%;'></div>
											</div>
										</div>
									</div>
								</div>
							</div><!-- /.tab-pane -->
						</div>
					</div>
				</div>
			</div>
			</section>
        </aside>
	</div>

	<!-- Generate the data to be used by the gantt. -->
	<c:set var="ganttData" value="'data':["/>
    <c:if test="${!empty taskList}">
   		<c:forEach var="task" items="${taskList}">
    		<fmt:formatDate pattern="dd-MM-yyyy" value="${task.dateStart}" var="taskDateStart"/>
    		<c:set var="taskRow" value="{id:'${task.id}-${staff.id}', status:${task.status}, text:'${fn:escapeXml(task.title)}', content:'${fn:escapeXml(task.content)}', start_date:'${taskDateStart}', open: true, duration:${task.duration}, parent:'${staff.id}'},"/>
    		<c:set var="ganttData" value="${ganttData}${taskRow}"/>
    	</c:forEach>
    	<c:set var="ganttData" value="${fn:substring(ganttData, 0, fn:length(ganttData)-1)}"/>
    </c:if>
    <c:set var="ganttEnd" value="]"/>
   	<c:set var="ganttData" value="{${ganttData}${ganttEnd}}"/>
   	
	<!-- Javascript components -->
   	
	<script src="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/js/gantt-custom.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/js/common.js" />"type="text/javascript"></script>
	<script type="text/javascript">
	    var tasks = ${ganttData};
		$(document).ready(function() {
			gantt.init("gantt-chart");
		    gantt.parse(tasks);
			$("#example-1").dataTable();
		});
	</script>
</body>
</html>