<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Task ${action}</title>
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
													<c:url value="/task/edit/0" var="urlCreateTask"/>
				                                	<a href="${urlCreateTask}">
				                                		<button class="btn btn-default btn-flat btn-sm">Create Task</button>
				                                	</a>
				                                	<br/><br/>
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
									                                        <c:url value="/task/edit/${task.id}" var="viewTaskURL"/>
									                                        <a href="${viewTaskURL}">
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
						</div>
					</div>
				</div>
			</div>
			</section>
        </aside>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#example-1").dataTable();
		});
	</script>
</body>
</html>