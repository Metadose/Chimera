
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="staffWage" value="${staff.wage}"/>
<fmt:formatDate pattern="yyyy/MM/dd" value="${minDate}" var="minDateText"/>
<fmt:formatDate pattern="yyyy/MM/dd" value="${maxDate}" var="maxDateText"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Staff ${action}</title>
	
	<link href="<c:url value="/resources/css/gantt-custom.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/fullcalendar.css" />"rel="stylesheet" type="text/css" />
	
	<style>
	  ul {         
	      padding:0 0 0 0;
	      margin:0 0 0 0;
	  }
	  ul li {     
	      list-style:none;
	      margin-bottom:25px;           
	  }
	  ul li img {
	      cursor: pointer;
	  }
	</style>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<script src="<c:url value="/resources/lib/moment.min.js" />"></script>
	<script src="<c:url value="/resources/lib/fullcalendar.min.js" />"></script>
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	            	<c:choose>
	            	<c:when test="${staff.id == 0}">
	            		New Staff
	            	</c:when>
	            	<c:when test="${staff.id > 0}">
	            		${staff.getFullName()}
	            	</c:when>
	            	</c:choose>
	                <small>${action} Staff</small>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-md-12">

                    	<c:if test="${!empty project}">
                    	<c:url var="urlBack" value="/project/edit/${project.id}" />
	                    <a href="${urlBack}">
							<button class="btn btn-cebedo-back btn-flat btn-sm">Back to Project</button>
						</a><br/><br/>
                    	</c:if>

                    	${uiParamAlert}
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                                <c:if test="${staff.id != 0}">
                                <li><a href="#tab_timeline" data-toggle="tab">Timeline</a></li>
                                <li><a href="#tab_payroll" data-toggle="tab">Payroll</a></li>
                                </c:if>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Details</h3>
                   								</div>
                   								<div class="box-body">
				                                    <c:set var="detailsFormURL" value="${contextPath}/staff/create"/>
                   									<form:form modelAttribute="staff" id="detailsForm" method="post" action="${detailsFormURL}">
				                                        <div class="form-group">
				                                            <label>Prefix</label>
				                                            <form:input type="text" class="form-control" path="prefix"/><br/>
				                                            
				                                            <label>First</label>
				                                            <form:input type="text" class="form-control" path="firstName"/><br/>
				                                            
				                                            <label>Middle</label>
				                                            <form:input type="text" class="form-control" path="middleName"/><br/>
				                                            
				                                            <label>Last</label>
				                                            <form:input type="text" class="form-control" path="lastName"/><br/>
				                                            
				                                            <label>Suffix</label>
				                                            <form:input type="text" class="form-control" path="suffix"/><br/>
				                                            
				                                            <label>Position</label>
				                                            <form:input type="text" class="form-control" path="companyPosition"/><br/>
				                                            
				                                            <label>Salary (Daily)</label>
				                                            <form:input type="text" class="form-control" path="wage"/><br/>
				                                            
				                                            <label>E-Mail</label>
				                                            <form:input type="text" class="form-control" path="email"/><br/>
				                                            
				                                            <label>Contact Number</label>
				                                            <form:input type="text" class="form-control" path="contactNumber"/><br/>
				                                        </div>
				                                    </form:form>
				                                    <c:choose>
		                                            	<c:when test="${staff.id == 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${staff.id > 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<form:form action="${contextPath}/staff/delete" method="post">
																<button class="btn btn-default btn-flat btn-sm">Delete This Staff</button>
		                                            		</form:form>
		                                            	</c:when>
		                                            </c:choose>
                   								</div>
                   							</div>
                   						</div>
              						</div>
                                </div><!-- /.tab-pane -->
                                <c:if test="${staff.id != 0}">
                                <div class="tab-pane" id="tab_timeline">
                                	<div class="row">
                   						<div class="col-md-12">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Timeline</h3>
                   								</div>
                   								<div class="box-body">
				                                	<div id="gantt-chart" class="gantt-holder">
					                                </div><!-- /.box-body -->
                   								</div>
                   							</div>
                   						</div>
              						</div>
              						<div class="row">
                   						<div class="col-md-12">
                   							<div class="box box-body box-default">
                   							<div class="box-header">
                   								<table>
                   								<tr>
                   								<td><h3 class="box-title">Tasks</h3></td>
                   								<td>
                   								<a href="${contextPath}/task/assign/staff/${staff.id}">
			                                		<button class="btn btn-default btn-flat btn-sm">Create Task</button>
			                                	</a>
                   								</td>
                   								</tr>
                   								</table>
               								</div>
               								<div class="box-body">
			                                    <table id="task-table" class="table table-bordered table-striped">
			                                    	<thead>
			                                    		<tr>
				                                        	<th>&nbsp;</th>
				                                            <th>Status</th>
				                                            <th>Title</th>
				                                            <th>Content</th>
				                                            <th>Project</th>
				                                            <th>Start</th>
				                                            <th>Duration</th>
				                                        </tr>
			                                    	</thead>
			                                        <tbody>
				                                        <c:set var="taskList" value="${staff.tasks}"/>
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
								                                                <li><a href="${contextPath}/task/mark/staff/?staff_id=${staff.id}&task_id=${task.id}&status=0">New</a></li>
								                                                <li><a href="${contextPath}/task/mark/staff/?staff_id=${staff.id}&task_id=${task.id}&status=1">Ongoing</a></li>
								                                                <li><a href="${contextPath}/task/mark/staff/?staff_id=${staff.id}&task_id=${task.id}&status=2">Completed</a></li>
								                                                <li><a href="${contextPath}/task/mark/staff/?staff_id=${staff.id}&task_id=${task.id}&status=3">Failed</a></li>
								                                                <li><a href="${contextPath}/task/mark/staff/?staff_id=${staff.id}&task_id=${task.id}&status=4">Cancelled</a></li>
								                                            </ul>
								                                        </div>
								                                        <a href="${contextPath}/task/edit/${task.id}">
						                                            		<button class="btn btn-default btn-flat btn-sm">View</button>
						                                            	</a>
						                                            	<a href="${contextPath}/task/delete/${task.id}">
						                                            		<button class="btn btn-default btn-flat btn-sm">Delete</button>
						                                            	</a>
			                                        				</td>
						                                            <td style="vertical-align: middle;">
							                                            <c:set value="${task.getStatusEnum().css()}" var="css"></c:set>
																		<span class="label ${css}">${task.getStatusEnum()}</span>
						                                            </td>
						                                            <td>${task.title}</td>
						                                            <td>${task.content}</td>
						                                            <td>
						                                            	<c:choose>
					                                            		<c:when test="${!empty task.project}">
					                                            			<a class="general-link" href="${contextPath}/project/edit/from/staff/${task.project.id}">
							                                            	${task.project.name}
							                                            	</a>
					                                            		</c:when>
					                                            		<c:when test="${empty task.project}">
					                                            			<h5>No project assigned.</h5>
					                                            		</c:when>
						                                            	</c:choose>					                                            
						                                            </td>
						                                            <td>${task.dateStart}</td>
						                                            <td>${task.duration}</td>
						                                        </tr>
			                                        		</c:forEach>
		                                        		</c:if>
				                                    </tbody>
				                                </table>
			                                </div><!-- /.box-body -->
			                                </div>
                   						</div>
              						</div>
              						<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Summary</h3>
                   								</div>
                   								<div class="box-body">
                   								<!--      Map<TaskStatus, Integer> taskStatusMap = getTaskStatusMap(staff); -->
												<table id="task-status-table" class="table table-bordered table-striped">
												<thead>
		                                    		<tr>
			                                            <th>Status</th>
			                                            <th>Count</th>
			                                        </tr>
		                                    	</thead>
												<tbody>
												<c:forEach items="${taskStatusMap}" var="statusEntry">
												<c:set value="${statusEntry.key}" var="entryKey"/>
												<c:set value="${statusEntry.value}" var="entryValue"/>
													<tr>
														<td>
				                                            <span class="label ${entryKey.css()}">${entryKey}</span>
														</td>
														<td>
															${entryValue}
														</td>
													</tr>
												</c:forEach>
												</tbody>
												</table>
                   								</div>
                   							</div>
                   						</div>
              						</div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_payroll">
                                	<div class="row">
                   						<div class="col-md-12">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Attendance</h3>
                   								</div>
                   								<div class="box-body">
                   									<div id='calendar'></div>
                   								</div>
                   							</div>
                   						</div>
              						</div>
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Data Range</h3>
                   								</div>
                   								<div class="box-body">
                   									Displaying data from <b>${minDateText}</b> to <b>${maxDateText}</b>.<br/><br/>
				                                	<form:form
									                	modelAttribute="rangeDate"
														id="rangeDateForm"
														method="post"
														action="${contextPath}/project/edit/attendance/range">
														<table>
														<tr>
															<td>
								                            <label>Start Date</label>
								                            <div class='input-group date date-picker'>
									                            <form:input type="text" class="form-control" path="startDate"/>
											                    <span class="input-group-addon">
											                        <span class="glyphicon glyphicon-calendar"></span>
											                    </span>
											                </div>
															</td>
															<td>
															&nbsp;
															</td>
															<td>
								                            <label>End Date</label>
								                            <div class='input-group date date-picker'>
									                            <form:input type="text" class="form-control" path="endDate"/>
											                    <span class="input-group-addon">
											                        <span class="glyphicon glyphicon-calendar"></span>
											                    </span>
											                </div>
															</td>
															<td>
															&nbsp;
															</td>
															<td style="vertical-align: bottom; padding-bottom: 1%">
									                        <button class="btn btn-default btn-flat btn-sm" id="rangeDateButton">Load Data</button>
															</td>
														</tr>
														</table>
								                    </form:form><br/><br/>
								                    <div class="box-header">
		                   								<h3 class="box-title" style="padding-left: 0px;">Mass Attendance Editor</h3>
	                   								</div>
								                    <form:form
									                	modelAttribute="massAttendance"
														id="massAttendanceForm"
														method="post"
														action="${contextPath}/project/mass/add/attendance">
								                        <div class="form-group">
								                            <label>Start Date</label>
								                            <div class='input-group date date-picker'>
									                            <form:input type="text" class="form-control" id="massStartDate" path="startDate"/>
											                    <span class="input-group-addon">
											                        <span class="glyphicon glyphicon-calendar"></span>
											                    </span>
											                </div>
								                            <br/>
								                            <label>End Date</label>
								                            <div class='input-group date date-picker'>
									                            <form:input type="text" class="form-control" id="massEndDate" path="endDate"/>
											                    <span class="input-group-addon">
											                        <span class="glyphicon glyphicon-calendar"></span>
											                    </span>
											                </div>
								                            <br/>
								                            
								                            <label id="massStatusLabel">Status</label>
								                            <form:select class="form-control" id="massStatusValue" path="statusID"> 
								           						<c:forEach items="${calendarStatusList}" var="thisStatus"> 
								           							<form:option value="${thisStatus.get(\"id\")}" label="${thisStatus.get(\"label\")}"/> 
								           						</c:forEach>
								                 			</form:select>
								                 			<br id="massStatusBreak"/>
								                            <label id="massWageLabel">Salary</label>
								                            <form:input type="text" class="form-control" id="massWageValue" path="wage"/>
								                            <br id="massWageBreak"/>
								                            <label id="includeWeekendsLabel">Include Weekends</label>
								                            <form:checkbox class="form-control" id="includeWeekendsCheckbox" path="includeWeekends"/>
								                        </div>
								                        <button class="btn btn-default btn-flat btn-sm" id="detailsButton">Update</button>
								                    </form:form>
                   								</div>
                   							</div>
                   						</div>
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Summary</h3>
                   								</div>
                   								<div class="box-body">
<!--      Map<Status, Integer> attendanceStatusMap = new HashMap<Status, Integer>(); -->
												<table id="status-table" class="table table-bordered table-striped">
												<thead>
		                                    		<tr>
			                                            <th>Status</th>
			                                            <th>Count</th>
			                                            <th>Total</th>
			                                        </tr>
		                                    	</thead>
												<tbody>
												<c:forEach items="${attendanceStatusMap}" var="attendanceStatusEntry">
												<c:set value="${attendanceStatusEntry.key}" var="entryKey"/>
												<c:set value="${attendanceStatusEntry.value}" var="entryValue"/>
													<tr>
														<td>
															<c:choose>
				                                            	<c:when test="${entryKey.id() == 6}">
					                                            <c:set value="border: 1px solid red" var="spanBorder"/>
				                                            	</c:when>
				                                            	<c:when test="${entryKey.id() != 6}">
					                                            <c:set value="" var="spanBorder"/>
				                                            	</c:when>
				                                            </c:choose>
				                                            <span style="${spanBorder}" class="label ${entryKey.css()}">${entryKey}</span>
														</td>
														<td>
															<fmt:formatNumber type="number" 
															maxFractionDigits="0" 
															value="${entryValue.get(\"statusCount\")}" />
														</td>
														<td>
															<fmt:formatNumber type="currency" 
					                                		currencySymbol="&#8369;"
															value="${entryValue.get(\"equivalentWage\")}" />
														</td>
													</tr>
												</c:forEach>
												</tbody>
												</table>
												<div class="pull-right">
												
			                                	<h3>Grand Total <b><u>
			                                	<fmt:formatNumber type="currency" 
			                                		currencySymbol="&#8369;"
													value="${payrollTotalWage}" />
												</u></b></h3>
			                                	</div>
                   								</div>
                   							</div>
                   						</div>
              						</div>
              						<div class="row">
                   						<div class="col-md-12">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Breakdown</h3>
                   								</div>
                   								<div class="box-body">
				                                    <table id="attendance-table" class="table table-bordered table-striped">
				                                    	<thead>
				                                    		<tr>
					                                            <th>Date</th>
					                                            <th>Status</th>
					                                            <th>Salary</th>
					                                        </tr>
				                                    	</thead>
				                                        <tbody>
		                                        		<c:forEach items="${attendanceList}" var="attendance">
		                                        			<tr>
					                                            <td>${attendance.getFormattedDateString("yyyy-MM-dd")}</td>
					                                            <c:choose>
					                                            	<c:when test="${attendance.getStatus().id() == 6}">
						                                            <c:set value="border: 1px solid red" var="spanBorder"/>
					                                            	</c:when>
					                                            	<c:when test="${attendance.getStatus().id() != 6}">
						                                            <c:set value="" var="spanBorder"/>
					                                            	</c:when>
					                                            </c:choose>
					                                            <td><span style="${spanBorder}" class="label ${attendance.getStatus().css()}">${attendance.getStatus()}</span></td>
					                                            <td>
					                                            <fmt:formatNumber type="currency" 
						                                		currencySymbol="&#8369;"
																value="${attendance.wage}" />
					                                            </td>
					                                        </tr>
		                                        		</c:forEach>
					                                    </tbody>
					                                </table>
                   								</div>
                   							</div>
                   						</div>
                   					</div>
                                </div><!-- /.tab-pane -->
                                </c:if>
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
	
	<c:if test="${staff.id != 0}">
	<div id="myModal" class="modal fade">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title">Attendance</h4>
	            </div>
	            <div class="modal-body">
	                <form:form
	                	modelAttribute="attendance"
						id="attendanceForm"
						method="post"
						action="${contextPath}/project/add/attendance">
                        <div class="form-group">
                            <label>Date</label>
                            <form:input type="text" class="form-control" id="modalDate" path="date"/>
                            
                            <br/>
                            <label>Status</label>
<!--                             List<Map<String, String>> statusMap = new ArrayList<Map<String, String>>(); -->
                            <form:select class="form-control" id="attendanceStatus" path="statusID"> 
           						<c:forEach items="${calendarStatusList}" var="thisStatus"> 
           							<form:option value="${thisStatus.get(\"id\")}" label="${thisStatus.get(\"label\")}"/> 
           						</c:forEach>
                 			</form:select>
                 			<br id="modalWageBreak"/>
                            <label id="modalWageLabel">Salary</label>
                            <form:input type="text" class="form-control" id="modalWage" path="wage"/><br/>
                        </div>
                    </form:form>
	            </div>
	            <div class="modal-footer">
	            	<button type="button" class="btn btn-default btn-flat btn-sm" data-dismiss="modal">Close</button>
	                <button type="button" onclick="submitForm('attendanceForm')" class="btn btn-default btn-flat btn-sm">Update</button>
	            </div>
	        </div>
	    </div>
	</div>
	</c:if>
	
	<script src="<c:url value="/resources/js/common.js" />"type="text/javascript"></script>
	
   	<script src="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.js" />"type="text/javascript"></script>
   	<script src="${contextPath}/resources/lib/dhtmlxGantt_v3.1.1_gpl/ext/dhtmlxgantt_tooltip.js" type="text/javascript"></script>
	<script src="<c:url value="/resources/js/gantt-custom.js" />"type="text/javascript"></script>
	
	<!-- InputMask -->
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.date.extensions.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.extensions.js" type="text/javascript"></script>
	
	<script type="text/javascript">
	    var ganttJSON = ${ganttJSON};
	    var tasks = {'data': ganttJSON};
	</script>
	
	<c:if test="${staff.id != 0 && !empty staff.tasks}">
	<script type="text/javascript">
		gantt.init("gantt-chart");
	    gantt.parse(tasks);
	    gantt.sort("start_date");
	</script>
	</c:if>
   	
	<script>
		function submitAjax(id) {
			var formObj = $('#'+id);
			var serializedData = formObj.serialize();
			$.ajax({
				type: "POST",
				url: '${contextPath}/field/update/assigned/staff',
				data: serializedData,
				success: function(response){
					location.reload();
				}
			});
		}
		
		$(document).ready(function() {
			$('.date-picker').datepicker({
			    format: 'yyyy/mm/dd'
			})
			$("#modalDate").inputmask("yyyy/mm/dd", {"placeholder": "yyyy/mm/dd"});
			
// 			$("#status-table").dataTable();
			$("#project-table").dataTable();
			$("#task-table").dataTable();
			$("#attendance-table").dataTable();
			
			var eventsJSON = ${calendarJSON};
			var staffWage = ${staffWage};
			
			$('#attendanceStatus').on('change', function() {
				// If selected value is ABSENT.
				// Hide the salary field.
				if(this.value == 2 || this.value == -1) {
					$('#modalWage').hide();
					$('#modalWageLabel').hide();
					$('#modalWageBreak').hide();
				} else {
					$('#modalWage').show();
					$('#modalWageLabel').show();
					$('#modalWageBreak').show();
				}
			});
			
			$('#massStatusValue').on('change', function() {
				// If selected value is ABSENT.
				// Hide the salary field.
				if(this.value == 2 || this.value == -1) {
					$('#massWageValue').hide();
					$('#massWageLabel').hide();
					$('#massWageBreak').hide();
				} else {
					$('#massWageValue').show();
					$('#massWageLabel').show();
					$('#massWageBreak').show();
				}
			});
			
			$('#calendar').fullCalendar({
				height: 450,
				events: eventsJSON,
				dayClick: function(date, jsEvent, view) {
					$("#modalDate").val(date.format());
					$("#modalWage").val(staffWage);
					$("#myModal").modal('show');
			    },
			    eventClick: function(calEvent, jsEvent, view) {
			    	$("#modalDate").val(calEvent.start.format());
					$("#modalWage").val(staffWage);
					$("#myModal").modal('show');
					
					var statusValue = calEvent.attendanceStatus;
					$('#attendanceStatus').val(statusValue);
					
					if(statusValue == 2 || this.value == -1) {
						$('#modalWage').hide();
						$('#modalWageLabel').hide();
						$('#modalWageBreak').hide();
					} else {
						$('#modalWage').val(calEvent.attendanceWage);
						$('#modalWage').show();
						$('#modalWageLabel').show();
						$('#modalWageBreak').show();
					}
			    }
		    });
			var dateAsVal = '${maxDateStr}';
			var minDate = moment(dateAsVal);
			$('#calendar').fullCalendar('gotoDate', minDate);
	    });
	</script>
</body>
</html>