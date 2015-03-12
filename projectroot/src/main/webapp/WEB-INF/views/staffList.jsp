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
	<link href="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.css" />"rel="stylesheet" type="text/css" />
	<style type="text/css">
	/* 	Start of gantt task colors */
	.gantt-info{
		border:2px solid #00c0ef;
		color: #00c0ef;
		background: #00c0ef;
	}
	.gantt-primary{
		border:2px solid #3c8dbc;
		color: #3c8dbc;
		background: #3c8dbc;
	}
	.gantt-success{
		border:2px solid #00a65a;
		color: #00a65a;
		background: #00a65a;
	}
	.gantt-danger{
		border:2px solid #f56954;
		color: #f56954;
		background: #f56954;
	}
	.gantt-default{
		border:2px solid #666;
		color: #666;
		background: #666;
	}
	/* 	End of gantt task colors */
	</style>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
<!-- 	        <section class="content-header"> -->
<!-- 	            <h1> -->
<%-- 	                Staff ${action} --%>
<!-- 	                <small>Complete list of all staff members</small> -->
<!-- 	            </h1> -->
<!-- 	        </section>   -->
	         <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_list" data-toggle="tab">List</a></li>
<!--                                 TODO -->
                                <li><a href="#tab_timeline" id="tab_timeline-href" data-toggle="tab">Timeline</a></li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_list">
                                	<h2 class="page-header">Information</h2>
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
									<h2 class="page-header">Timeline</h2>
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
	        
	        
	        
<!-- 	        <section class="content">  -->
                
<!--             </section>/.content -->
        </aside>
	</div>
	<c:import url="/resources/js-includes.jsp" />
	<script src="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.js" />"type="text/javascript"></script>
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
   	
	<script type="text/javascript">
    var tasks = ${ganttData};
	gantt.config.scale_unit = "month";
	gantt.config.date_scale = "%F, %Y";
	gantt.config.scale_height = 50;
	gantt.config.subscales = [
		{unit:"day", step:1, date:"%j, %D" }
	];
	
	gantt.config.columns = [
        {name:"text",       label:" ",  width:"*", tree:true },
        {name:"start_date", label:"Start", align: "center" },
        {name:"duration",   label:"Man Days",   align: "center" },
        {name:"add",        label:"",           width:44 }
    ];
	
	gantt.templates.task_text = function(start, end, task){
		if(typeof task.content !== "undefined"){
			return "<b>"+task.text+"</b> ("+task.content+")";	
		}
		return "<b>"+task.text+"</b>";
	};
	
	// Returned string refers to a CSS declared above.
	gantt.templates.task_class = function(start, end, task){
		if(task.status == 0){
			return "gantt-info";
		} else if(task.status == 1) {
			return "gantt-primary";
		} else if(task.status == 2) {
			return "gantt-success";
		} else if(task.status == 3) {
			return "gantt-danger";
		} else if(task.status == 4) {
			return "gantt-default";
		}
	};
	gantt.init("gantt-chart");
    gantt.parse(tasks);
</script>
<script>
	$(document).ready(function() {
		$("#example-1").dataTable();
	});
	$(function() {
	    $('#tab_timeline-href').bind('click', function (e) {
	        gantt.render();
	    });
	});
</script>
</body>
</html>