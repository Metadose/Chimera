<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="staffWage" value="${staff.wage}"/>
<c:set var="taskList" value="${staff.tasks}"/>

<fmt:formatDate pattern="yyyy/MM/dd" value="${minDate}" var="minDateText"/>
<fmt:formatDate pattern="yyyy/MM/dd" value="${maxDate}" var="maxDateText"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<c:choose>
    	<c:when test="${staff.id == 0}">
    	<title>Create New Staff</title>
    	</c:when>
    	<c:when test="${staff.id > 0}">
		<title>${staff.getFullName()} | Edit Staff</title>
    	</c:when>
    </c:choose>
	
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
		<!--  -->
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	            	<c:choose>
	            	<c:when test="${staff.id == 0}">
	            		New Staff
	            		<small>Create Staff</small>
	            	</c:when>
	            	<c:when test="${staff.id > 0}">
	            		${staff.getFullName()}
	                	<small>Edit Staff</small>
	            	</c:when>
	            	</c:choose>
	            </h1>
	        </section>
	        <section class="content">
	        	<c:if test="${fromProject}">
	        	<c:url var="urlBack" value="/project/edit/${project.id}" />
                   <a href="${urlBack}">
					<button class="btn btn-cebedo-back btn-flat btn-sm">Back to Project</button>
				</a><br/><br/>
	        	</c:if>
                <div class="row">
                    <div class="col-md-12">
                    	${uiParamAlert}
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">

                            	<c:choose>
                            	<c:when test="${fromProject && staff.id > 0}">
                                <li class="active"><a href="#tab_payroll" data-toggle="tab">Payroll</a></li>
                                <li><a href="#tab_1" data-toggle="tab">Details</a></li>
                            	</c:when>

                            	<c:when test="${!fromProject || (fromProject && staff.id == 0)}">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                            	</c:when>
                            	</c:choose>

                                <c:if test="${staff.id != 0}">
                                <li><a href="#tab_timeline" data-toggle="tab">Tasks</a></li>
                                </c:if>
                            </ul>
                            <div class="tab-content">
                            	<c:choose>
                            	<c:when test="${!fromProject || (fromProject && staff.id == 0)}">
                                <div class="tab-pane active" id="tab_1">
                            	</c:when>

                            	<c:when test="${fromProject}">
                            	<div class="tab-pane" id="tab_1">
                            	</c:when>
                            	</c:choose>
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
                   									<c:choose>
					                            	<c:when test="${!fromProject}">
				                                    <c:set var="detailsFormURL" value="${contextPath}/staff/create"/>
					                            	</c:when>

					                            	<c:when test="${fromProject}">
				                                    <c:set var="detailsFormURL" value="${contextPath}/project/create/staff"/>
					                            	</c:when>
					                            	</c:choose>
                   									<form:form modelAttribute="staff" id="detailsForm" method="post" action="${detailsFormURL}">
				                                        <div class="form-group">
				                                            <label>Prefix</label>
				                                            <form:input type="text" class="form-control" path="prefix" placeholder="Sample: Engr., Hon., Atty., Dr."/>
				                                            <p class="help-block">Add the name prefix</p>
				                                            
				                                            <label>First</label>
				                                            <form:input type="text" class="form-control" path="firstName" placeholder="Sample: John, Mark, Jane"/>
				                                            <p class="help-block">Enter the first/given name</p>
				                                            
				                                            <label>Middle</label>
				                                            <form:input type="text" class="form-control" path="middleName" placeholder="Sample: Doe, Cruz, Alvarez"/>
				                                            <p class="help-block">Enter the middle name</p>
				                                            
				                                            <label>Last</label>
				                                            <form:input type="text" class="form-control" path="lastName" placeholder="Sample: Brown, Castillo, Aquino"/>
				                                            <p class="help-block">Enter the last/family name</p>
				                                            
				                                            <label>Suffix</label>
				                                            <form:input type="text" class="form-control" path="suffix" placeholder="Sample: Jr., Sr., II, III"/>
				                                            <p class="help-block">Add the name suffix</p>
				                                            
				                                            <label>Company Position</label>
				                                            <form:input type="text" class="form-control" path="companyPosition" placeholder="Sample: Skilled, Unskilled, Laborer, Mason"/>
				                                            <p class="help-block">Indicate the company position</p>
				                                            
				                                            <label>Salary (Daily)</label>
				                                            <form:input type="text" class="form-control" path="wage" placeholder="Sample: 350, 225, 250, 500"/>
				                                            <p class="help-block">Enter the staff's daily wage</p>
				                                            
				                                            <label>E-Mail</label>
				                                            <form:input type="text" class="form-control" path="email" placeholder="Sample: cruz@gmail.com, rosa@yahoo.com"/>
				                                            <p class="help-block">Add an e-mail address</p>
				                                            
				                                            <label>Contact Number</label>
				                                            <fmt:formatNumber type="number" pattern="###" value="${staff.contactNumber}" var="contactNumber"/>
				                                            <form:input type="text" class="form-control" path="contactNumber"
				                                            	value="${contactNumber}"
				                                            	placeholder="Sample: 09226110411, 09271231111"/>
				                                            <p class="help-block">Add a contact number</p>
				                                        </div>
				                                    </form:form>
				                                    <c:choose>
		                                            	<c:when test="${staff.id == 0}">
		                                            		<button class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${staff.id > 0}">
		                                            		<button class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>

															<div class="btn-group">
															<button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
															<ul class="dropdown-menu">
																<li>
															    	<a href="<c:url value="/staff/delete/${staff.id}"/>" class="cebedo-dropdown-hover">
															    		Confirm Delete
															    	</a>
																</li>
															</ul>
															</div>
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
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
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
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
                   									<div id="highcharts-tasks"></div>
                   								</div>
                   							</div>
                   						</div>
              						</div>
              						
              						<div class="row">
                   						<div class="col-md-12">
                   							<div class="box box-body box-default">
               								<div class="box-body">
			                                    <table id="task-table" class="table table-bordered table-striped">
			                                    	<thead>
			                                    		<tr>
				                                        	<th>&nbsp;</th>
				                                            <th>Status</th>
				                                            <th>Start Date</th>
				                                            <th>End Date</th>
				                                            <th>Duration</th>
				                                            <th>Title</th>
				                                            <th>Content</th>
				                                        </tr>
			                                    	</thead>
			                                        <tbody>
		                                        		<c:forEach items="${taskList}" var="task">
		                                        			<tr>
		                                        				<td style="text-align: center;">
							                                        <a href="<c:url value="/project/edit/task/${task.id}"/>">
					                                            		<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
					                                            	</a>
		                                        				</td>
					                                            <td>
						                                            <c:set value="${task.getStatusEnum().css()}" var="css"></c:set>
																	<span class="label ${css}">${task.getStatusEnum()}</span>
					                                            </td>
					                                            <fmt:formatDate pattern="yyyy/MM/dd" value="${task.dateStart}" var="taskStartDate"/>
					                                            <td>${taskStartDate}</td>
					                                            <fmt:formatDate pattern="yyyy/MM/dd" value="${task.getEndDate()}" var="taskEndDate"/>
					                                            <td>${taskEndDate}</td>
					                                            <td>${task.duration}</td>
					                                            <td>${task.title}</td>
					                                            <td>${task.content}</td>
					                                        </tr>
		                                        		</c:forEach>
				                                    </tbody>
				                                </table>
			                                </div><!-- /.box-body -->
			                                </div>
                   						</div>
              						</div>
                                </div><!-- /.tab-pane -->

                            	
                            	<c:if test="${fromProject && staff.id > 0}">
                                <div class="tab-pane active" id="tab_payroll">
                                	<div class="row">
                   						<div class="col-md-12">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
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
									                            <form:input type="text" class="form-control" path="startDate" value="${minDateText}"/>
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
									                            <form:input type="text" class="form-control" path="endDate" value="${maxDateText}"/>
											                    <span class="input-group-addon">
											                        <span class="glyphicon glyphicon-calendar"></span>
											                    </span>
											                </div>
															</td>
															<td>
															&nbsp;
															</td>
															<td style="vertical-align: bottom; padding-bottom: 1%">
									                        <button class="btn btn-cebedo-load btn-flat btn-sm" id="rangeDateButton">Load Data</button>
															</td>
														</tr>
														</table>
														<p class="help-block">Displaying data from <b>${minDateText}</b> to <b>${maxDateText}</b></p>
								                    </form:form>
                   									<div id='calendar'></div>
                   								</div>
                   							</div>
                   						</div>
              						</div>
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Mass Attendance Editor</h3>
                   								</div>
                   								<div class="box-body">
								                    <form:form
									                	modelAttribute="massAttendance"
														id="massAttendanceForm"
														method="post"
														action="${contextPath}/project/mass/add/attendance">
								                        <div class="form-group">
								                            <label>Start Date</label>
								                            <div class='input-group date date-picker'>
									                            <form:input type="text" class="form-control" id="massStartDate" path="startDate" placeholder="Sample: 2015/08/07, 2016/07/15, 2017/03/25"/>
											                    <span class="input-group-addon">
											                        <span class="glyphicon glyphicon-calendar"></span>
											                    </span>
											                </div>
								                            <p class="help-block">Enter the first date of the attendance</p>

								                            <label>End Date</label>
								                            <div class='input-group date date-picker'>
									                            <form:input type="text" class="form-control" id="massEndDate" path="endDate" placeholder="Sample: 2015/08/07, 2016/07/15, 2017/03/25"/>
											                    <span class="input-group-addon">
											                        <span class="glyphicon glyphicon-calendar"></span>
											                    </span>
											                </div>
								                            <p class="help-block">Enter the last date of the attendance</p>
								                            
								                            <label id="massStatusLabel">Status</label>
								                            <form:select class="form-control" id="massStatusValue" path="statusID"> 
								           						<c:forEach items="${calendarStatusList}" var="thisStatus"> 
								           							<form:option value="${thisStatus.get(\"id\")}" label="${thisStatus.get(\"label\")}"/> 
								           						</c:forEach>
								                 			</form:select>
								                 			<p class="help-block">Choose the status for these entries</p>

								                 			<div id="massWageDiv">
								                            <label>Salary</label>
								                            <form:input type="text" class="form-control" path="wage" placeholder="Sample: 300, 500, 550"/>
								                            <p class="help-block">Provide the salary of the staff for these entries</p>
								                 			</div>
								                            
								                            <table>
								                            	<tr>
								                            		<td>
								                            		<label id="includeWeekendsLabel">Include Weekends</label>
								                            		</td>
								                            	</tr>
								                            	<tr>
								                            		<td>
								                            		<form:checkbox class="form-control" id="includeWeekendsCheckbox" path="includeWeekends"/>
								                            		</td>								                            	
								                            	</tr>
								                            </table>

								                            <p class="help-block">Check to include weekends</p>
								                        </div>
								                        <button class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton">Update</button>
								                    </form:form>
                   								</div>
                   							</div>
                   						</div>
                   						
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-header">
													<div class="pull-right">												
					                                	<h3>Grand Total <b><u>
					                                	<fmt:formatNumber type="currency" 
					                                		currencySymbol="&#8369;"
															value="${payrollTotalWage}" />
														</u></b></h3>
				                                	</div>
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
												<c:set value="${attendanceStatusEntry.key}" var="attendanceStatus"/>
												<c:set value="${attendanceStatusEntry.value}" var="pairCountValue"/>
													<tr>
														<td>
															<c:choose>
				                                            	<c:when test="${attendanceStatus.id() == 6}">
					                                            <c:set value="border: 1px solid red" var="spanBorder"/>
				                                            	</c:when>
				                                            	<c:when test="${attendanceStatus.id() != 6}">
					                                            <c:set value="" var="spanBorder"/>
				                                            	</c:when>
				                                            </c:choose>
				                                            <span style="${spanBorder}" class="label ${attendanceStatus.css()}">${attendanceStatus}</span>
														</td>
														<td>
															<fmt:formatNumber type="number" 
															maxFractionDigits="0" 
															value="${pairCountValue.getCount()}" />
														</td>
														<td>
															<fmt:formatNumber type="currency" 
					                                		currencySymbol="&#8369;"
															value="${pairCountValue.getValue()}" />
														</td>
													</tr>
												</c:forEach>
												</tbody>
												</table>

               									<div id="highcharts-attendance"></div>
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
					                                            <td>${attendance.getFormattedDateString("yyyy/MM/dd")}</td>
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
                                </c:if>
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
	
	<c:if test="${fromProject && staff.id > 0}">
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
                            <form:input type="text" class="form-control" id="modalDate" path="date" placeholder="Sample: 2016/06/25"/>
                            <p class="help-block">The date of the attendance</p>

                            <label>Status</label>
<!--                             TODO List<Map<String, String>> statusMap = new ArrayList<Map<String, String>>(); -->
                            <form:select class="form-control" id="attendanceStatus" path="statusID"> 
           						<c:forEach items="${calendarStatusList}" var="thisStatus"> 
           							<form:option value="${thisStatus.get(\"id\")}" label="${thisStatus.get(\"label\")}"/> 
           						</c:forEach>
                 			</form:select>
							<p class="help-block">Set the status of this attendace</p>

							<div id="modalWageDiv">
                            <label>Salary</label>
                            <form:input type="text" class="form-control" id="modalWage" path="wage" placeholder="Sample: 250, 350, 500"/>
                            <p class="help-block">Set the staff wage for this date</p>
                        	</div>
                        </div>
                    </form:form>
	            </div>
	            <div class="modal-footer">
	            	<button type="button" class="btn btn-cebedo-close btn-flat btn-sm" data-dismiss="modal">Close</button>
	                <button type="button" onclick="submitForm('attendanceForm')" class="btn btn-cebedo-update btn-flat btn-sm">Update</button>
	            </div>
	        </div>
	    </div>
	</div>
	</c:if>
	
	<!-- InputMask -->
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.date.extensions.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.extensions.js" type="text/javascript"></script>

    <script src="<c:url value="/resources/lib/highcharts/js/highcharts.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/lib/highcharts/js/modules/no-data-to-display.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/lib/highcharts/js/modules/exporting.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/lib/highcharts/js/highcharts-3d.js" />"type="text/javascript"></script>
	
   	<c:if test="${staff.id != 0 && fromProject}">
   	<script type="text/javascript">
   	$(document).ready(function() {
		var eventsJSON = ${calendarJSON};
		var staffWage = ${staffWage};
		
		$("#attendance-table").dataTable();
		
		$('#attendanceStatus').on('change', function() {
			// If selected value is ABSENT.
			// Hide the salary field.
			if(this.value == 2 || this.value == -1) {
				$('#modalWage').hide();
				$('#modalWageDiv').hide();
			} else {
				$('#modalWage').show();
				$('#modalWageDiv').show();
			}
		});
			
		$('#massStatusValue').on('change', function() {
			// If selected value is ABSENT.
			// Hide the salary field.
			if(this.value == 2 || this.value == -1) {
				$('#massWageDiv').hide();
			} else {
				$('#massWageDiv').show();
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
				
				// TODO Broken here: var statusValue = calEvent.attendanceStatus;
				// Value is the enum like "OVERTIME", not id like 1, 2, 3.
				var statusValue = calEvent.attendanceStatus;
				$('#attendanceStatus').val(statusValue);
				
				if(statusValue == 2 || this.value == -1) {
					$('#modalWage').hide();
					$('#modalWageDiv').hide();
				} else {
					$('#modalWage').val(calEvent.attendanceWage);
					$('#modalWage').show();
					$('#modalWageDiv').show();
				}
		    }
	    });
		var dateAsVal = '${maxDateStr}';
		var minDate = moment(dateAsVal);
		$('#calendar').fullCalendar('gotoDate', minDate);
   	});
	</script>
	</c:if>
	
	<script type="text/javascript">
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
			$("#task-table").dataTable();
	    });

	    $(function () {
		    $('#highcharts-tasks').highcharts({
		        chart: {
		            type: 'pie',
		            options3d: {
		                enabled: true,
		                alpha: 45
		            }
		        },
		        credits: {
		        	enabled: false
		        },
				title: {
		            text: ''
		        },
		        tooltip: {
		            pointFormat: '<b>{point.y} ({point.percentage:.2f}%)</b>'
		        },
		        plotOptions: {
		            pie: {
		                innerSize: 100,
		                depth: 45,
		                allowPointSelect: true,
		                cursor: 'pointer',
		                dataLabels: {
		                    enabled: true,
		                    format: '<b>{point.name}</b><br/>{point.y} ({point.percentage:.2f}) %',
		                    style: {
		                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
		                    }
		                }
		            }
		        },
		        series: [{
		            name: "Tasks",
		            colorByPoint: true,
		            data: ${dataSeriesTasks}
		        }]
		    });
		});

	    $(function () {
		    $('#highcharts-attendance').highcharts({
		        chart: {
		            type: 'pie',
		            options3d: {
		                enabled: true,
		                alpha: 45
		            }
		        },
		        credits: {
		        	enabled: false
		        },
				title: {
		            text: ''
		        },
		        tooltip: {
		            pointFormat: '<b>{point.y} ({point.percentage:.2f}%)</b>'
		        },
		        plotOptions: {
		            pie: {
		                innerSize: 100,
		                depth: 45,
		                allowPointSelect: true,
		                cursor: 'pointer',
		                dataLabels: {
		                    enabled: true,
		                    format: '<b>{point.name}</b><br/>{point.y} ({point.percentage:.2f}) %',
		                    style: {
		                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
		                    }
		                }
		            }
		        },
		        series: [{
		            name: "Attendance",
		            colorByPoint: true,
		            data: ${dataSeriesAttendance}
		        }]
		    });
		});
	</script>
</body>
</html>