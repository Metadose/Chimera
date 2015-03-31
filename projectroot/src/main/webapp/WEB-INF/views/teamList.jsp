<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Team ${action}</title>
	<c:import url="/resources/css-includes.jsp" />
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
	                Team ${action}
	                <small>Complete list of all team members</small>
	            </h1>
	        </section>
	        <section class="content">
			<div class="row">
				<div class="col-xs-12">
					<!-- Custom Tabs -->
					<div class="nav-tabs-custom">
						<ul class="nav nav-tabs">
							<li class="active"><a href="#tab_list" data-toggle="tab">List</a></li>
							<li><a href="#tab_timeline" data-toggle="tab">Timeline</a></li>
						</ul>
						<div class="tab-content">
							<div class="tab-pane active" id="tab_list">
								<div class="row">
									<div class="col-xs-12">
										<div class="box">
												<div class="box-header">
				<!--                                     <h3 class="box-title">Data Table With Full Features</h3> -->
												</div><!-- /.box-header -->
												<div class="box-body table-responsive">	
													<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
				                                	<a href="${contextPath}/team/edit/0">
				                                		<button class="btn btn-default btn-flat btn-sm">Create Team</button>
				                                	</a>
				                                	<br/><br/>
				                                	</sec:authorize>
				                                    <table id="example-1" class="table table-bordered table-striped">
				                                        <thead>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                            	<th>#</th>
				                                                <th>Name</th>
				                                            </tr>
				                                        </thead>
				                                        <tbody>
				                                        	<c:if test="${!empty teamList}">
				                                        		<c:forEach items="${teamList}" var="team">
						                                            <tr>
						                                            	<td>
						                                            		<center>
																				<a href="${contextPath}/team/edit/${team.id}">
																					<button class="btn btn-default btn-flat btn-sm">View</button>
																				</a>
																				<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
																				<a href="${contextPath}/team/delete/${team.id}">
																					<button class="btn btn-default btn-flat btn-sm">Delete</button>
																				</a>
																				</sec:authorize>
																			</center>
																		</td>
																		<td>${team.id}</td>
						                                                <td>${team.name}</td>
						                                            </tr>
					                                            </c:forEach>
				                                            </c:if>
				                                        </tbody>
				                                        <tfoot>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                            	<th>#</th>
				                                                <th>Name</th>
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
									<div class="col-xs-12">
										<div class="box box-default">
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
    <c:if test="${!empty teamList}">
    	<c:forEach items="${teamList}" var="team">
    		<c:set var="row" value="{id:'${team.id}', duration:0, text:'${fn:escapeXml(team.name)}', open: true},"/>
    		<c:set var="ganttData" value="${ganttData}${row}"/>
    		<c:forEach var="task" items="${team.tasks}">
	    		<fmt:formatDate pattern="dd-MM-yyyy" value="${task.dateStart}" var="taskDateStart"/>
	    		<c:set var="taskRow" value="{id:'${task.id}-${team.id}', status:${task.status}, text:'${fn:escapeXml(task.title)}', content:'${fn:escapeXml(task.content)}', start_date:'${taskDateStart}', open: true, duration:${task.duration}, parent:'${team.id}'},"/>
	    		<c:set var="ganttData" value="${ganttData}${taskRow}"/>
	    	</c:forEach>
    	</c:forEach>
    	<c:set var="ganttData" value="${fn:substring(ganttData, 0, fn:length(ganttData)-1)}"/>
    </c:if>
    <c:set var="ganttEnd" value="]"/>
   	<c:set var="ganttData" value="{${ganttData}${ganttEnd}}"/>
	
	<!-- Javascript components -->
   	<c:import url="/resources/js-includes.jsp" />
	<script src="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/js/gantt-custom.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/js/common.js" />"type="text/javascript"></script>
	<script type="text/javascript">
	    var tasks = ${ganttData};
		gantt.init("gantt-chart");
	   	gantt.parse(tasks);
		$(document).ready(function() {
			$("#example-1").dataTable();
		});
	</script>
</body>
</html>