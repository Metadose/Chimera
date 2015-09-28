<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>List Projects</title>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<!--  -->
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                List Projects
	                <small>Complete list of all projects</small>
	            </h1>
	        </section>
	        <section class="content">
			<div class="row">
				<div class="col-md-12">
					${uiParamAlert}
					<!-- Custom Tabs -->
					<div class="row">
						<div class="col-md-12">
							<div class="box">
									<div class="box-body">
										<c:url var="urlCreateProject" value="/project/edit/0"/>
	                                	<a href="${urlCreateProject}">
	                                		<button class="btn btn-cebedo-create btn-flat btn-sm">Create Project</button>
	                                	</a>
	                                	<br/><br/>
	                                    <table id="example-1" class="table table-bordered table-striped">
	                                        <thead>
	                                            <tr>
	                                            	<th>&nbsp;</th>
	                                            	<th>Status</th>
	                                                <th>Project</th>
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
			                                            			<c:url var="urlEditProject" value="/project/edit/${project.id}"/>
	                                								<a href="${urlEditProject}">
																		<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
	                                								</a>
			                                            			
	                                								<!-- <a href="<c:url value="/project/logs/${project.id}"/>">
																		<button class="btn btn-cebedo-view btn-flat btn-sm">Logs</button>
	                                								</a> -->
																</center>
															</td>
															<td>
					                                            <c:set value="${project.getStatusEnum().css()}" var="css"></c:set>
																<span class="label ${css}">${project.getStatusEnum()}</span>
			                                                </td>
			                                                <td>${project.name}</td>
			                                                <td>${project.location}</td>
			                                                <td>${project.notes}</td>
			                                            </tr>
		                                            </c:forEach>
	                                            </c:if>
	                                        </tbody>
	                                        <tfoot>
	                                            <tr>
	                                            	<th>&nbsp;</th>
	                                                <th>Status</th>
	                                                <th>Project</th>
	                                                <th>Location</th>
	                                                <th>Notes</th>
	                                            </tr>
	                                        </tfoot>
	                                    </table>
	                                </div><!-- /.box-body -->
								</div><!-- /.box -->
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