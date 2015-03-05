<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Task ${action}</title>
	<c:import url="/resources/css-includes.jsp" />
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
                    <div class="col-xs-12">
                        <div class="box">
                                <div class="box-header">
<!--                                     <h3 class="box-title">Data Table With Full Features</h3> -->
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive">
                                	<a href="${contextPath}/task/edit/0">
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
	                                            <th>End</th>
                                            </tr>
                                        </thead>
                                        <tbody>
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
					                                                <li><a href="${contextPath}/task/mark/?task_id=${task.id}&status=0">New</a></li>
					                                                <li><a href="${contextPath}/task/mark/?task_id=${task.id}&status=1">Ongoing</a></li>
					                                                <li><a href="${contextPath}/task/mark/?task_id=${task.id}&status=2">Completed</a></li>
					                                                <li><a href="${contextPath}/task/mark/?task_id=${task.id}&status=3">Failed</a></li>
					                                                <li><a href="${contextPath}/task/mark/?task_id=${task.id}&status=4">Cancelled</a></li>
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
				                                            		<h6>Cancelled</h6>
				                                            	</c:when>
				                                            </c:choose>
			                                            </td>
			                                            <td>${task.content}</td>
			                                            <td>
			                                            	<c:choose>
		                                            		<c:when test="${!empty task.project}">
		                                            			<a href="${contextPath}/project/edit/${task.project.id}">
				                                            		<button class="btn btn-info btn-sm">View</button>&nbsp;&nbsp;
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
				                                            		<button class="btn btn-info btn-sm">View</button>&nbsp;&nbsp;
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
						                                            <c:set var="taskStaffName" value="${taskStaff.prefix} ${taskStaff.firstName} ${taskStaff.middleName} ${taskStaff.lastName} ${taskStaff.suffix}"/>
					                                            	<a href="${contextPath}/staff/edit/${taskStaff.id}">
					                                            		<button class="btn btn-info btn-sm">View</button>&nbsp;&nbsp;
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
			                                            <td><fmt:formatDate pattern="yyyy/MM/dd" value="${task.dateEnd}" /></td>
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
	                                            <th>End</th>
                                            </tr>
                                        </tfoot>
                                    </table>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->
                    </div>
                </div>
            </section><!-- /.content -->
        </aside>
	</div>
	<c:import url="/resources/js-includes.jsp" />
	<script>
		$(document).ready(function() {
			$("#example-1").dataTable();
	    });
	</script>
</body>
</html>