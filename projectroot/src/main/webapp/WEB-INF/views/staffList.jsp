<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Staff ${action}</title>
	<c:import url="/resources/css-includes.jsp" />
	<link href="<c:url value="/resources/css/gantt-custom.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.css" />"rel="stylesheet" type="text/css" />
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
			<section class="content-header">
	            <h1>
	                Staff ${action}
	                <small>Complete list of all staff members</small>
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
					                                	<a href="${contextPath}/staff/edit/0">
					                                		<button class="btn btn-default btn-flat btn-sm">Create Staff</button>
					                                	</a>
					                                	<br/><br/>
					                                    <table id="example-1" class="table table-bordered table-striped">
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
					                                        	<c:if test="${!empty staffList}">
					                                        		<c:forEach items="${staffList}" var="staff">
							                                            <tr>
							                                            	<td>
							                                            		<center>
																					<a href="${contextPath}/staff/edit/${staff.id}">
																						<button class="btn btn-default btn-flat btn-sm">View</button>
																					</a>
																					<a href="${contextPath}/staff/delete/${staff.id}">
																						<button class="btn btn-default btn-flat btn-sm">Delete</button>
																					</a>
																				</center>
																			</td>
							                                                <td>
							                                                	<div class="user-panel">
																	            <div class="pull-left image">
																	                <c:choose>
						                                                			<c:when test="${!empty staff.thumbnailURL}">
						                                                				<img src="${contextPath}/image/display/staff/profile/?staff_id=${staff.id}" class="img-circle"/>
						                                                			</c:when>
						                                                			<c:when test="${empty staff.thumbnailURL}">
						                                                				<img src="/pmsys/resources/img/avatar5.png" class="img-circle">
						                                                			</c:when>
							                                                		</c:choose>
																	            </div>
																		        </div>
							                                                </td>
							                                                <td>${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}</td>
							                                                <td>${staff.companyPosition}</td>
							                                                <td>${staff.email}</td>
							                                                <td>${staff.contactNumber}</td>
							                                            </tr>
						                                            </c:forEach>
					                                            </c:if>
					                                        </tbody>
					                                        <tfoot>
					                                            <tr>
					                                            	<th>&nbsp;</th>
					                                                <th>Photo</th>
					                                                <th>Full Name</th>
					                                                <th>Position</th>
					                                                <th>E-Mail</th>
					                                                <th>Contact Number</th>
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
    <c:if test="${!empty staffList}">
    	<c:forEach items="${staffList}" var="staff">
    		<c:set var="staffName" value="${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}"/>
    		<c:set var="staffRow" value="{id:'${staff.id}', duration:0, text:'${fn:escapeXml(staffName)}', open: true},"/>
    		<c:set var="ganttData" value="${ganttData}${staffRow}"/>
    		<c:forEach var="task" items="${staff.tasks}">
	    		<fmt:formatDate pattern="dd-MM-yyyy" value="${task.dateStart}" var="taskDateStart"/>
	    		<c:set var="taskRow" value="{id:'${task.id}-${staff.id}', status:${task.status}, text:'${fn:escapeXml(task.title)}', content:'${fn:escapeXml(task.content)}', start_date:'${taskDateStart}', open: true, duration:${task.duration}, parent:'${staff.id}'},"/>
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