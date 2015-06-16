<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Project List</title>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                Project List
	                <small>Complete list of all projects</small>
	            </h1>
	        </section>
	        <section class="content">
			<div class="row">
				<div class="col-xs-12">
					${uiParamAlert}
					<!-- Custom Tabs -->
					<div class="nav-tabs-custom">
						<ul class="nav nav-tabs" id="navigatorTab">
							<li class="active"><a href="#tab_list" data-toggle="tab">List</a></li>
						</ul>
						<div class="tab-content">
							<div class="tab-pane active" id="tab_list">
								<div class="row">
									<div class="col-xs-12">
										<div class="box">
												<div class="box-body table-responsive">
													<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
													<c:url var="urlCreateProject" value="/project/edit/0"/>
				                                	<a href="${urlCreateProject}">
				                                		<button class="btn btn-cebedo-create btn-flat btn-sm">Create Project</button>
				                                	</a>
				                                	<br/><br/>
				                                	</sec:authorize>
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
																				<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
																				<c:url var="urlDeleteProject" value="/project/delete/${project.id}"/>
				                                								<a href="${urlDeleteProject}">
																					<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
				                                								</a>
																				</sec:authorize>
																				<a href="${contextPath}/project/clear/cache/${project.id}">Clear Cache</a>
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
						                                                </td>
						                                                <td>
								                                            ${project.name}<br/><br/>
						                                                	<c:choose>
						                                                		<c:when test="${!empty project.thumbnailURL}">
						                                                			<img style="width: 100%" src="${contextPath}/image/display/project/profile/?project_id=${project.id}"/>
						                                                		</c:when>
						                                                	</c:choose>
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
					</div>
				</div>
			</div>
			</section>
        </aside>
	</div>
	<script src="<c:url value="/resources/js/common.js" />"type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#example-1").dataTable();
		});
	</script>
</body>
</html>