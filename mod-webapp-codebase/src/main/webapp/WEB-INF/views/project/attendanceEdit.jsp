<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>${project.name} | Mass Attendance Editor</title>
	
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
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<!--  -->
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	            	<c:out value="${project.name}"></c:out>
	                <small>Mass Attendance Editor</small>
	            </h1>
	        </section>
	        <section class="content">
	        	<c:url var="urlBack" value="/project/edit/${project.id}" />
                   <a href="${urlBack}">
					<button class="btn btn-cebedo-back btn-flat btn-sm">Back to Project</button>
				</a><br/><br/>
                <div class="row">
                    <div class="col-md-12">
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Mass Attendance Editor</a></li>
                            </ul>
                            <div class="tab-content">
                            	<div class="tab-pane active" id="tab_1">
									<div class="row">
									<div class="col-md-6">
                   							<div class="box box-body box-default">
                   							<div class="box-body">
							                    <form:form
								                	modelAttribute="massAttendance"
													id="massAttendanceForm"
													method="post"
													action="${contextPath}/project/mass/add/attendance/all">
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

							                            <label>Include Saturdays</label>
							                            <form:checkbox class="form-control" path="includeSaturdays" />
							                            <p class="help-block">Check to include Saturdays</p>

							                            <label>Include Sundays</label>
							                            <form:checkbox class="form-control" path="includeSundays" />
							                            <p class="help-block">Check to include Sundays</p>
							                            
							                            <br/>
							                            <label>Do <u>NOT</u> Include</label>
							                            <table class="table table-bordered table-striped is-data-table">
			                                    			<thead>
			                                    			<tr>
			                                    			<th>Exclude</th>
			                                                <th>Full Name</th>
			                                                <th>Company Position</th>
			                                                <th>Salary (Daily)</th>
			                                    			</tr>
			                                    			</thead>

			                                    			<tbody>
			                                    			<c:forEach items="${staffList}" var="staff">
				                                    			<tr>
				                                    			<td align="center">
					                                    			<form:checkbox class="form-control include-checkbox" path="excludeList" value="${staff.id}"/><br/>
				                                    			</td>
				                                    			<td>${staff.getFullName()}</td>
				                                    			<td>${staff.companyPosition}</td>
			                                                	<td class="cebedo-text-align-right">${staff.getWageAsString()}</td>
				                                    			</tr>
				                                    		</c:forEach>
			                                    			</tbody>
			                                    		</table>
			                                    		<button class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton">Create Attendance</button>
							                        </div>
							                        
							                    </form:form>
                   							</div>
                   						</div>
                   						</div>
									</div>
								</div><!-- /.tab-pane -->
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
	
	<script>
		function submitForm(id) {
			$('#'+id).submit();
		}
		$(document).ready(function() {
			$('.date-picker').datepicker({
			    format: 'yyyy/mm/dd'
			});
			$(".is-data-table").dataTable();
	    });
	</script>
</body>
</html>