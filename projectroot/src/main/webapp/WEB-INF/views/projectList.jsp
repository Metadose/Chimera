<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Projects List</title>
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
	                Data Tables ${message}
	                <small>advanced tables</small>
	            </h1>
	            <ol class="breadcrumb">
	                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
	                <li><a href="#">Tables</a></li>
	                <li class="active">Data tables</li>
	            </ol>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="box">
                                <div class="box-header">
                                    <h3 class="box-title">Data Table With Full Features</h3>
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive">
                                	<button class="btn btn-success btn-sm">Create</button><br/>
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
																<a href="${contextPath}/project/edit/${project.id}"><button class="btn btn-primary btn-sm">View</button></a>
																<button class="btn btn-warning btn-sm">Edit</button>
																<button class="btn btn-danger btn-sm">Delete</button>
															</center>
														</td>
		                                                <td>
		                                                	${project.thumbnailURL}<br/>
		                                                	${project.status}<br/>
		                                                	${project.name}<br/>
		                                                </td>
		                                                <td>
		                                                	<c:forEach items="${project.managerAssignments}" var="managerAssignment">
		                                                		<c:set var="man" value="${managerAssignment.manager}"/>
			                                                	${man.thumbnailURL}<br/>
			                                                	${managerAssignment.projectPosition}<br/>
			                                                	${man.prefix} ${man.firstName} ${man.middleName} ${man.lastName} ${man.suffix}<br/>
			                                                	${man.companyPosition}<br/>
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