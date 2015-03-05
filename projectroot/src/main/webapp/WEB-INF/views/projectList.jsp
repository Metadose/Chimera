<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Project ${action}</title>
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
	                Project ${action}
	                <small>Complete list of all projects</small>
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
                                	<a href="${contextPath}/project/edit/0">
                                		<button class="btn btn-primary btn-flat btn-sm">Create Project</button>
                                	</a>
                                	<br/><br/>
                                    <table id="example-1" class="table table-bordered table-striped">
                                        <thead>
                                            <tr>
                                            	<th>&nbsp;</th>
                                                <th>Project</th>
                                                <th>Manager(s)</th>
                                                <th>Location</th>
                                                <th>Notes</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:if test="${!empty projectList}">
                                        		<c:forEach items="${projectList}" var="project">
		                                            <tr>
		                                            	<td>
		                                            		<center>
																<form action="${contextPath}/project/edit/${project.id}">
																	<button class="btn btn-primary btn-flat btn-sm">View</button>
																</form>&nbsp;
																<form action="${contextPath}/project/delete/${project.id}">
																	<button class="btn btn-primary btn-flat btn-sm">Delete</button>
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
		                                                <td>
		                                                	<c:forEach items="${project.managerAssignments}" var="managerAssignment">
		                                                		<c:set var="man" value="${managerAssignment.manager}"/>
		                                                		<c:choose>
		                                                			<c:when test="${!empty man.thumbnailURL}">
		                                                				<img src="${contextPath}/image/display/staff/profile/?staff_id=${man.id}"/>
		                                                			</c:when>
		                                                			<c:when test="${empty man.thumbnailURL}">
		                                                				No photo uploaded.
		                                                			</c:when>
		                                                		</c:choose>
			                                                	<br/>
			                                                	<br/>
			                                                	<form action="${contextPath}/staff/edit/${man.id}">
			                                                		<button class="btn btn-primary btn-flat btn-sm">View</button>
			                                                	</form>
			                                                	${managerAssignment.projectPosition}<br/>
			                                                	${man.prefix} ${man.firstName} ${man.middleName} ${man.lastName} ${man.suffix}<br/>
			                                                	${man.companyPosition}<br/>
			                                                	<br/>
			                                                	<br/>
		                                                	</c:forEach>
														</td>
		                                                <td>${project.location}</td>
		                                                <td>${project.notes}</td>
		                                            </tr>
	                                            </c:forEach>
                                            </c:if>
                                        </tbody>
                                        <tfoot>
                                            <tr>
                                            	<th>&nbsp;</th>
                                                <th>Project</th>
                                                <th>Manager(s)</th>
                                                <th>Location</th>
                                                <th>Notes</th>
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