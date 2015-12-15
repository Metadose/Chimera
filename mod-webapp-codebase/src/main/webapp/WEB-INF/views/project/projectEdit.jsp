<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<sec:authentication var="authUser" property="user"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<c:choose>
    	<c:when test="${project.id == 0}">
    	<title>Create New Project</title>
    	</c:when>
    	<c:when test="${project.id > 0}">
		<title>${project.name} | Edit Project</title>
    	</c:when>
    </c:choose>
	
<%-- 	<link href="<c:url value="/resources/css/ionicons.min.css" />"rel="stylesheet" type="text/css" /> --%>
	<link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
	
<%-- 	<link href="<c:url value="/resources/css/gantt-custom.css" />"rel="stylesheet" type="text/css" /> --%>
	<link href="https://cdn.rawgit.com/VicCebedo/PracticeRepo/development/mod-webapp-codebase/src/main/webapp/resources/css/gantt-custom.css" rel="stylesheet" type="text/css" />
	
<%-- 	<link href="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.css" />"rel="stylesheet" type="text/css" /> --%>
	<link href="https://cdn.rawgit.com/VicCebedo/PracticeRepo/development/mod-webapp-codebase/src/main/webapp/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.css" rel="stylesheet" type="text/css" />
<%-- 	<link href="<c:url value="/resources/lib/fullcalendar.css" />"rel="stylesheet" type="text/css" /> --%>
	
	<style type="text/css">
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
		/*TODO Estimation tables. Needed?*/
		#concrete-estimation-summary-table, #concrete-table, #form-estimate-cost {
			font-size: 11px;
		}
		/*TODO Toggle visibility of columns. Do we still need this?*/
		.toggle-vis {
			cursor: pointer;
			font-size: 12px !important;
		}
	</style>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
<%-- 	<script src="<c:url value="/resources/lib/fullcalendar.min.js" />"></script> --%>

	<!-- Modal -->
	<div id="deleteModal" class="modal fade" role="dialog">
		<!-- Modal content-->
		<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header bg-red">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">Warning!</h4>
			</div>
			<div class="modal-body">
				<p><b>All data</b> stored in this project will <b>NOT be recoverable</b>.<br/>
					Please <b>backup</b> all your data first before deleting.</p>
			</div>
			<div class="modal-footer">

				<div class="btn-group">
				<button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
				<ul class="dropdown-menu">
					<li>
				    	<c:url var="urlProjectDelete" value="/project/delete/${project.id}"/>
						<a href="${urlProjectDelete}" class="cebedo-dropdown-hover">
				    		Confirm Delete
				    	</a>
					</li>
				</ul>
				</div>

				<button type="button" class="btn btn-cebedo-close btn-flat btn-sm" data-dismiss="modal">Close</button>
			</div>
		</div>
		</div>
	</div>

	<div class="wrapper row-offcanvas row-offcanvas-left">
		<!--  -->
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>	
	            	<c:choose>
	            		<c:when test="${project.id == 0}">
	            			New Project
			                <small>Create Project</small>
			                <c:set value="active" var="detailsVisibility"/>
			                <c:set value="hide" var="dashboardVisibility"/>
	            		</c:when>

	            		<c:when test="${project.id != 0}">
	            			${project.name}
			                <small>Edit Project</small>
			                <c:set value="active" var="dashboardVisibility"/>
	            		</c:when>
	            	</c:choose>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-md-12">
                    	${uiParamAlert}
						<c:if test="${empty project.assignedStaff && project.id > 0}">
							<div class="callout callout-warning">
								<h4>Warning!</h4>
								<p>There are <b>no staff members</b> assigned to the <b>project</b>.</p>
							</div>
	                	</c:if>
						<c:if test="${project.status == 2 && empty project.actualCompletionDate}">
							<div class="callout callout-warning">
								<h4>Warning!</h4>
								<p>Project is marked as <b>COMPLETED</b> but <b>Actual Completion Date</b> (Contract tab) is <b>not set</b>.</p>
							</div>
	                	</c:if>
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs" id="myTab">
                                <c:choose>
                                	<c:when test="${project.id != 0}">
                                		<li class="${dashboardVisibility}"><a href="#tab_dashboard" data-toggle="tab">Dashboard</a></li>
                                		<li class="${detailsVisibility}"><a href="#tab_1" data-toggle="tab">Contract</a></li>
                                		
                                		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_VIEW')">
                                		<li><a href="#tab_project_estimate" data-toggle="tab">Estimate</a></li>
                                		</sec:authorize>
                                		
                                		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'PROGRAM_OF_WORKS_VIEW')">
		                                <li><a href="#tab_timeline" data-toggle="tab">Program of Works</a></li>
										</sec:authorize>

                                		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_VIEW')">
                                		<li><a href="#tab_staff" data-toggle="tab">Staff</a></li>
                                		</sec:authorize>

                                		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'PAYROLL_VIEW')">
		                                <li><a href="#tab_payroll" data-toggle="tab">Payroll</a></li>
                                		</sec:authorize>

                                		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'INVENTORY_VIEW')">
										<li><a href="#tab_inventory" data-toggle="tab">Inventory</a></li>
										</sec:authorize>
										
                                		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'EQUIPMENT_VIEW')">
										<li><a href="#tab_equipment_expenses" data-toggle="tab">Equipment</a></li>
										</sec:authorize>

                                		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'OTHER_EXPENSES_VIEW')">
										<li><a href="#tab_other_expenses" data-toggle="tab">Other Expenses</a></li>
										</sec:authorize>

                                		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'DOWNLOADS_VIEW')">
		                                <li><a href="#tab_downloads" data-toggle="tab">Downloads</a></li>
										</sec:authorize>
		                                
		                                <!-- <li><a href="#tab_calendar" data-toggle="tab">TODO Calendar</a></li> -->
                                	</c:when>
                                	<c:when test="${project.id == 0}">
                                		<li class="${detailsVisibility}"><a href="#tab_1" data-toggle="tab">Contract</a></li>
                                	</c:when>
                                </c:choose>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane ${detailsVisibility}" id="tab_1">
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
			                                        <div class="form-group" id="detailsDivEditor">
			                                        
			                                        	<!-- Update form Input -->
                  										<form:form id="projectForm"
                  											modelAttribute="project"
                  											method="post"
                  											action="${contextPath}/project/create">

                  											<c:if test="${project.id > 0}">

																<c:set value="${project.getStatusEnum().css()}" var="css"></c:set>
																<span class="label ${css}">${project.getStatusEnum()}</span>

					                                        	<label>&nbsp;</label>
					                                            <div class="btn-group">
					                                            	
					                                            	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'CONTRACT_UPDATE')">
						                                            <button type="button" class="btn btn-cebedo-update btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">
						                                            Mark As&nbsp;<span class="caret"></span>
						                                            </button>
					                                            	</sec:authorize>
						                                            <ul class="dropdown-menu">
						                                            	<c:forEach var="projectStatus" items="${projectStatusList}">
								                                    		<li>
								                                    			<a href="<c:url value="/project/mark/?project=${project.id}&status=${projectStatus.id()}"/>" class="cebedo-dropdown-orange-hover">
								                                    				${projectStatus.label()}
								                                    			</a>
								                                    		</li>
						                                            	</c:forEach>
						                                            </ul>
						                                        </div>
					                                            <p class="help-block">Choose the status of this project</p>
				                                        	</c:if>

				                                            <label>Name</label>
				                                            <form:input type="text" placeholder="Sample: Mr. Brown Building" class="form-control" path="name"/>
				                                            <p class="help-block">Enter the name of the project</p>

				                                            <label>Physical Target</label>
				                                            <form:input type="text" placeholder="Sample: 10000, 20000, 3500" class="form-control" path="physicalTarget"/>
				                                            <p class="help-block">Enter the physical size of the project in square meters</p>

				                                            <label>Start Date</label>
					                                        <div class="input-group">
					                                            <div class="input-group-addon">
					                                                <i class="fa fa-calendar"></i>
					                                            </div>
					                                            <fmt:formatDate value="${project.dateStart}" var="dateString" pattern="yyyy/MM/dd" />
					                                            <form:input type="text" class="form-control date-picker" path="dateStart" placeholder="Sample: 2016/06/25" value="${dateString}"/>
					                                        </div>
				                                            <p class="help-block">Enter the project start date</p>

					                                            <label>Target Completion Date</label>
						                                        <div class="input-group">
						                                            <div class="input-group-addon">
						                                                <i class="fa fa-calendar"></i>
						                                            </div>
						                                            <fmt:formatDate value="${project.targetCompletionDate}" var="dateString" pattern="yyyy/MM/dd" />
						                                            <form:input type="text" class="form-control date-picker" path="targetCompletionDate" placeholder="Sample: 2016/06/25" value="${dateString}"/>
						                                        </div>
					                                            <p class="help-block">Enter the project target completion date</p>

					                                        <c:if test="${project.status == 2}"> <!-- If completed -->
					                                        	<!-- Update form Input -->
					                                            <label>Actual Completion Date</label>
					                                            
					                                            <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'CONTRACT_UPDATE')">
																		<c:url var="clearActualCompletion" value="/project/clear/actual-completion-date"/>
						                                            	(<a class="general-link" href="${clearActualCompletion}">Clear</a>)
						                                        </sec:authorize>
						                                            	
						                                        <div class="input-group">
						                                            <div class="input-group-addon">
						                                                <i class="fa fa-calendar"></i>
						                                            </div>
						                                            <fmt:formatDate value="${project.actualCompletionDate}" var="dateString" pattern="yyyy/MM/dd" />
						                                            <form:input type="text" class="form-control date-picker" path="actualCompletionDate" placeholder="Sample: 2016/06/25" value="${dateString}"/>
						                                        </div>
					                                            <p class="help-block">Enter the project actual completion date</p>
						                                    </c:if>

				                                            <label>Location</label>
				                                            <form:input type="text" placeholder="Sample: 123 Brown Avenue, New York City" class="form-control" path="location"/>
				                                            <p class="help-block">Enter the project location</p>
				                                            
				                                            <label>Notes</label>
				                                            <form:input type="text" placeholder="Sample: This is only the first phase" class="form-control" path="notes"/>
				                                            <p class="help-block">Add additional notes and remarks</p>
				                                    	</form:form>
			                                    	<c:choose>
		                                            	<c:when test="${project.id == 0}">
		                                            		<button class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton" onclick="submitForm('projectForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${project.id > 0}">
		                                            	
		                                            		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'CONTRACT_UPDATE')">
		                                            		<button class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton" onclick="submitForm('projectForm')">Update</button>
		                                            		</sec:authorize>
		                                            		
		                                            		<sec:authorize access="hasAnyRole('ADMIN_COMPANY')">
															<button class="btn btn-cebedo-delete btn-flat btn-sm" data-toggle="modal" data-target="#deleteModal">Delete</button>
															</sec:authorize>
		                                            	</c:when>
		                                            </c:choose>
			                                        </div>
                   								</div>
                   							</div>
                   						</div>
                   						<c:choose>
                   						<c:when test="${project.id != 0}">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<c:set var="projectFields" value="${project.assignedFields}"/>
                   								<c:choose>
                   								<c:when test="${empty projectFields}">
                   								<div class="box-header">
                   									<h3 class="box-title">Add More Information</h3>
                   								</div>
                   								</c:when>
                   								
                   								<c:when test="${!empty projectFields}">
                   								<div class="box-header">
                   									<h3 class="box-title">More Information</h3>
                   								</div>
                   								</c:when>
                   								</c:choose>
                   								
                   								<div class="box-body">
                   									<div class="form-group">						                                    
               												
                   											<c:if test="${!empty projectFields}">
   															<div class="form-group" id="fieldsDivViewer">
	               												<c:forEach var="field" items="${projectFields}"  varStatus="loop">
               														<!-- More Information Output -->
	       															<label><c:out value="${field.label}"/></label>
	       															<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'CONTRACT_UPDATE')">
	               														<c:url var="urlEditProjectField" value="/project/field/edit/${field.field.id}-3edc-${field.label}-3edc-${field.value}-end"/>
						                                            	(
		                                								<a class="general-link" href="${urlEditProjectField}">
						                                            	Edit
	               														</a>
						                                            	)
						                                            </sec:authorize>
						                                            <br/>
						                                            <c:out value="${field.value}"/>
	       															<br/>
	       															<br/>
																</c:forEach>
   															</div>
   															</c:if>
   															
   															<c:if test="${empty projectFields}">
   															<div class="form-group" id="fieldsDivViewer">
               													<i>No Additional Information.</i>
   															</div>
   															</c:if>
   															
															<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'CONTRACT_CREATE')">
   															<c:if test="${!empty projectFields}">
   															<h4>Add More Information</h4>
   															</c:if>
															<form:form modelAttribute="field"
																id="fieldsForm" 
																method="post" 
																action="${contextPath}/project/assign/field">
																
																<label>Label</label><br/>
																<form:input type="text" path="label" id="label" class="form-control"
																	placeholder="Sample: SSS, Building Permit No., Sub-contractor"/>
																	<p class="help-block">Add a label for this information</p>
																
																<label>Information</label><br/>
																<form:textarea class="form-control"
																	rows="3" id="value" path="value"
																	placeholder="Sample: 000-123-456, AEE-123, OneForce Construction"></form:textarea>
																	<p class="help-block">Enter the information to be added</p>
															
															</form:form>
	                                           				<button class="btn btn-cebedo-create btn-flat btn-sm" onclick="submitForm('fieldsForm')">Add Information</button>
	                                           				</sec:authorize>
	                                           				
	                                           				<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'CONTRACT_DELETE')">
	                                           				<c:if test="${!empty projectFields}">

	       														<div class="btn-group">
																<button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete All</button>
																<ul class="dropdown-menu">
																	<li>
																		<c:url var="urlProjectUnassignFieldAll" value="/project/unassign/field/all"/>
	                               										<a href="${urlProjectUnassignFieldAll}" class="cebedo-dropdown-hover">
																    		Confirm Delete All
																    	</a>
																	</li>
																</ul>
																</div>
															</c:if>
															</sec:authorize>
			                                        </div>
                   								</div>
                   							</div>
                   						</div>
                   						</c:when>
                   						</c:choose>
              						</div>
              						<c:choose>
                   					<c:when test="${project.id != 0}">
               						</c:when>
               						</c:choose>
                                </div><!-- /.tab-pane -->
                                
                                
                                <c:choose>
                   				<c:when test="${project.id != 0}">

                   				<div class="tab-pane ${dashboardVisibility}" id="tab_dashboard">
									<div class="row">
										<div class="col-md-6">
											<div class="info-box">
											<span class="info-box-icon cebedo-bg-forestry"><i class="ion ion-ios-calculator"></i></span>
											<div class="info-box-content">
											<span class="info-box-text">Current Total Expenses</span>
											<span class="info-box-number">${projectAux.getCurrentTotalProjectAsString()}</span>
											<span class="progress-description">
											Grand Total of All Expenses
											</span>
											<span class="progress-description">
											(Payroll, Inventory, Equipment and Other Expenses)
											</span>
											</div><!-- /.info-box-content -->
											</div><!-- /.info-box -->
										</div><!-- /.col -->
										<div class="col-md-6">
											<div class="info-box">
											<span class="info-box-icon cebedo-bg-forestry"><i class="ion ion-ios-briefcase"></i></span>
											<div class="info-box-content">
											<span class="info-box-text">Total Project Cost</span>
											<span class="info-box-number">${projectAux.getPlannedTotalProjectAsString()}</span>
											<span class="progress-description">
											Grand Total of Estimated Costs
											</span>
											<span class="progress-description">
											(Direct and Indirect)
											</span>
											</div><!-- /.info-box-content -->
											</div><!-- /.info-box -->
										</div><!-- /.col -->
									</div>
                   					<div class="row">

                   						<div class="col-md-3">
							              <div class="info-box">
							                <span class="info-box-icon cebedo-bg-forestry"><i class="ion ion-ios-people"></i></span>
							                <div class="info-box-content">
							                  <span class="info-box-text">Payroll</span>
							                  <span class="info-box-number">${projectAux.getGrandTotalPayrollAsString()}</span>
							                </div><!-- /.info-box-content -->
							              </div><!-- /.info-box -->
							            </div><!-- /.col -->

							            <div class="col-md-3">
							              <div class="info-box">
							                <span class="info-box-icon cebedo-bg-forestry"><i class="ion ion-settings"></i></span>
							                <div class="info-box-content">
							                  <span class="info-box-text">Inventory</span>
							                  <span class="info-box-number">${projectAux.getGrandTotalDeliveryAsString()}</span>
							                </div><!-- /.info-box-content -->
							              </div><!-- /.info-box -->
							            </div><!-- /.col -->

							            <!-- fix for small devices only -->
							            <!-- <div class="clearfix visible-sm-block"></div> -->							            

							            <div class="col-md-3">
							              <div class="info-box">
							                <span class="info-box-icon cebedo-bg-forestry"><i class="ion ion-gear-a"></i></span>
							                <div class="info-box-content">
							                  <span class="info-box-text">Equipment</span>
							                  <span class="info-box-number">${projectAux.getGrandTotalEquipmentExpensesAsString()}</span>
							                </div><!-- /.info-box-content -->
							              </div><!-- /.info-box -->
							            </div><!-- /.col -->
							            
							            <div class="col-md-3">
							              <div class="info-box">
							                <span class="info-box-icon cebedo-bg-forestry"><i class="ion ion-ios-cart"></i></span>
							                <div class="info-box-content">
							                  <span class="info-box-text">Other Expenses</span>
							                  <span class="info-box-number">${projectAux.getGrandTotalOtherExpensesAsString()}</span>
							                </div><!-- /.info-box-content -->
							              </div><!-- /.info-box -->
							            </div><!-- /.col -->
							            
							          </div>

									

              						<div class="row">

                   						<div class="col-md-6">
		                                	<c:set value="${projectAux.getCSSofOverspent().className()}" var="css"></c:set>
											<div class="info-box ${css}">
												<span class="info-box-icon"><i class="ion ion-ios-pulse-strong" style="padding-top: 20%;"></i></span>
												<div class="info-box-content">
													<span class="info-box-text">${projectAux.getCurrentTotalProjectAsString()} ${projectAux.getCurrentTotalProjectAsPercentAsString()} spent</span>
													<span class="info-box-number">
														<c:choose>
															<c:when test="${projectAux.getRemainingBudget() >= 0}">
																<c:set value="remaining" var="moneyCaption"/>
															</c:when>
															<c:when test="${projectAux.getRemainingBudget() < 0}">
																<c:set value="overspent" var="moneyCaption"/>
															</c:when>
														</c:choose>
														${projectAux.getRemainingBudgetAsString()} ${projectAux.getRemainingBudgetAsPercentAsString()} ${moneyCaption}
													</span>
													<div class="progress">
													<div class="progress-bar" style="width: ${projectAux.getRemainingBudgetAsPercent()}%"></div>
													</div>
													<span class="progress-description">
													out of project cost ${projectAux.getPlannedTotalProjectAsString()}
													</span>
												</div><!-- /.info-box-content -->
											</div>
				                        </div>

                   						<div class="col-md-6">
											<c:set value="${project.getCSSofDelay().className()}" var="css"></c:set>
											<div class="info-box ${css}">
												<span class="info-box-icon"><i class="ion ion-ios-pulse-strong" style="padding-top: 20%;"></i></span>
												<div class="info-box-content">
													<span class="info-box-text">${project.getStatusEnum()} (${project.getCSSofDelay().label()})</span>
													<span class="info-box-number">
														<c:choose>
															<c:when test="${project.getCalDaysRemaining() >= 0}">
																<c:set value="remaining" var="daysCaption"/>
															</c:when>
															<c:when test="${project.getCalDaysRemaining() < 0}">
																<c:set value="delayed" var="daysCaption"/>
															</c:when>
														</c:choose>
														${project.getCalDaysRemainingAsString()} (${project.getCalDaysRemainingAsPercentAsString()}%) calendar days ${daysCaption}
													</span>
													<div class="progress">
													<div class="progress-bar" style="width: ${project.getCalDaysProgressAsPercent()}%"></div>
													</div>
													<span class="progress-description">
													out of ${project.getCalDaysTotalAsString()} project days															
													from <fmt:formatDate value="${project.dateStart}" pattern="yyyy/MM/dd" /> to <fmt:formatDate value="${project.targetCompletionDate}" pattern="yyyy/MM/dd" />
													</span>
												</div><!-- /.info-box-content -->
											</div>
				                        </div>
				                   	</div> <!-- End of Row -->


				                   	<div class="row">
                   						<div class="col-md-6">
		                                	<div id="highcharts-dashboard-not-cumulative" style="height: 300px"></div>
				                        </div>
				                        <div class="col-md-6">
		                                	<div id="highcharts-dashboard-project-pie" style="height: 300px"></div>
				                        </div>
				                    </div>

              						<div class="row">
                   						<div class="col-md-6">
		                                	<div class="box box-body box-default">
				                                <div class="box-body">
				                                	<div id="highcharts-dashboard" style="height: 300px"></div>
				                                </div><!-- /.box-body -->
				                             </div>
				                        </div>
                   						<div class="col-md-6">
		                                	<div class="box box-body box-default">
				                                <div class="box-body">
				                                	<div id="highcharts-project" style="height: 300px"></div>
				                                </div><!-- /.box-body -->
				                             </div>
				                        </div>
				                   	</div> <!-- End of Row -->
                                </div><!-- /.tab-pane -->


								<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_VIEW')">
                                <div class="tab-pane" id="tab_project_estimate">
	                                <div class="nav-tabs-custom">
										<ul class="nav nav-tabs">
											<li class="active"><a href="#tab_estimated_cost" data-toggle="tab">Estimated Costs</a></li>
											<li><a href="#tab_calculator" data-toggle="tab">Estimator</a></li>
										</ul>
										<div class="tab-content">
											<div class="tab-pane active" id="tab_estimated_cost">
												<!-- ${directCostList}
												${indirectCostList} -->
												<div class="row">
			                   						<div class="col-md-4">
					                                	<div class="box box-body box-default">
					                                		<div class="box-header">
																<h3 class="box-title">Estimated Costs</h3>
															</div>
							                                <div class="box-body">
					                                			<div id="highcharts-costs-estimated-pie" style="height: 300px"></div>
					                                		</div>
							                             </div>
							                        </div>
			                   						<div class="col-md-4">
					                                	<div class="box box-body box-default">
					                                		<div class="box-header">
																<h3 class="box-title">Actual Costs</h3>
															</div>
															<div class="box-body">
					                                			<div id="highcharts-costs-actual-pie" style="height: 300px"></div>
					                                		</div>
					                                	</div>
							                        </div>
			                   						<div class="col-md-4">
					                                	<div class="box box-body box-default">
							                                <div class="box-body">

							                                	<table class="table table-bordered table-striped">
																<thead>
						                                    		<tr>
							                                            <th>Type</th>
							                                            <th>Estimated</th>
							                                            <th>Actual</th>
							                                            <th>Difference</th>
							                                        </tr>
						                                    	</thead>
																<tbody>
																	<tr>
																		<td>Direct</td>
																		<td class="cebedo-text-align-right">${projectAux.getGrandTotalCostsDirectAsString()}</td>
																		<td class="cebedo-text-align-right">${projectAux.getGrandTotalActualCostsDirectAsString()}</td>
																		<td class="cebedo-text-align-right">${projectAux.getDiffEstimatedActualDirectAsHTML()}</td>
																	</tr>
																	<tr>
																		<td>Indirect</td>
																		<td class="cebedo-text-align-right">${projectAux.getGrandTotalCostsIndirectAsString()}</td>
																		<td class="cebedo-text-align-right">${projectAux.getGrandTotalActualCostsIndirectAsString()}</td>
																		<td class="cebedo-text-align-right">${projectAux.getDiffEstimatedActualIndirectAsHTML()}</td>
																	</tr>
																	<tr>
																		<td>Total</td>
																		<td class="cebedo-text-align-right">${projectAux.getPlannedTotalProjectAsString()}</td>
																		<td class="cebedo-text-align-right">${projectAux.getActualTotalProjectAsString()}</td>
																		<td class="cebedo-text-align-right">${projectAux.getDiffEstimatedActualTotalAsHTML()}</td>
																	</tr>
																</tbody>
																</table>
															<br/>
																
															
															<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_CREATE')">
				                                    		<form:form modelAttribute="massUploadBean"
																action="${contextPath}/project/mass/upload/cost"
																method="post"
																enctype="multipart/form-data">
															
																<div class="form-group">
																<label>Excel File</label>
																<form:input type="file" class="form-control" path="file"/><br/>
																<button class="btn btn-cebedo-create btn-flat btn-sm">Upload</button>
																</div>
															</form:form>
															</sec:authorize>
						                                  		
							                                    <div class="form-group">
							                                    
							                                    <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_CREATE')">
																<form:form modelAttribute="cost"
																	action="${contextPath}/project/create/cost"
																	method="post"
																	id="costForm"
																	enctype="multipart/form-data">
							                                        
																	<label>Name</label>
																	<form:input placeholder="Sample: Sitework, Concrete Works, Metal Works" class="form-control" path="name"/>
																	<p class="help-block">Enter the name of this cost</p>
																	
																	<table style="width: 100%;">
																		<tr>
																			<td style="vertical-align: top;">
										                                        <label>Estimated Cost</label>
																				<form:input class="form-control" path="cost"/>
																				<p class="help-block">Enter the estimated cost</p>		
																			</td>

																			<td>&nbsp;</td>

																			<td style="vertical-align: top;">
										                                        <label>Actual Cost</label>
																				<form:input class="form-control" path="actualCost"/>
																				<p class="help-block">Enter the actual cost</p>	
																			</td>

																			<td>&nbsp;</td>

																			<td style="vertical-align: top;">
								                                                <label>Cost Type</label>
								                                                <form:select class="form-control" path="costType"> 
							                                   						<c:forEach items="${estimateCostList}" var="cost"> 
							                                   							<form:option value="${cost}" label="${cost.getLabel()}"/> 
							                                   						</c:forEach> 
								                                    			</form:select>
								                                    			<p class="help-block">Type of estimate cost</p>																				
																			</td>
																		</tr>
																	</table>
					                                    			
						                                        </form:form>
					                                    		<button class="btn btn-cebedo-create btn-flat btn-sm" onclick="submitForm('costForm')">Create</button>	
					                                    		</sec:authorize>
					                                    						                                    			
																<c:if test="${!empty directCostList || !empty indirectCostList}">
				                   									<a href="<c:url value="/project/export-xls/costs"/>">
						                                        		<button class="btn btn-cebedo-export btn-flat btn-sm">Export All</button>
						                                        	</a>
					                                        	</c:if>
							                                    </div>

							                                </div><!-- /.box-body -->
							                             </div>
							                        </div>
							                   	</div> <!-- End of Row -->
												<div class="row">
			                   						<div class="col-md-6"> <!-- Direct costs -->
					                                	<div class="box box-body box-default">
					                                		<div class="box-header">
															<h3 class="box-title">Direct Costs</h3>
															</div>
				                                        	
							                                <div class="box-body">
							                                    <table id="estimated-direct-costs" class="table table-bordered table-striped">	
							                                    	<thead>
							                                            <tr>
							                                            	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_UPDATE', 'ESTIMATE_DELETE')">
							                                            	<th>&nbsp;</th>
							                                            	</sec:authorize>
							                                            	
							                                                <th>Name</th>
							                                                <th>Estimated (&#8369;)</th>
							                                                <th>Actual (&#8369;)</th>
							                                                <th>Difference (&#8369;)</th>
							                                            </tr>
					                                        		</thead>
							                                        <tbody>
								                                		<c:forEach items="${directCostList}" var="directCost">
							                                            <tr>
					                                            			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_UPDATE', 'ESTIMATE_DELETE')">
							                                            	<td>
							                                            		<center>
							                                            		
							                                            			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_UPDATE')">
							                                            			<a href="<c:url value="/project/edit/cost/${directCost.getKey()}-end"/>">
											                                    		<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
							                                            			</a>
							                                            			</sec:authorize>

							                                            			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_DELETE')">
												                                    <div class="btn-group">
												                                    <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
												                                    <ul class="dropdown-menu">
												                                    	<li>
												                                    		<a href="<c:url value="/project/delete/cost/${directCost.getKey()}-end"/>" class="cebedo-dropdown-hover">
												                                        		Confirm Delete
												                                        	</a>
												                                    	</li>
												                                    </ul>
												                                    </div>
							                                            			</sec:authorize>

																				</center>
																			</td>
																			</sec:authorize>
						                                                	<td>${directCost.name}</td>
						                                                	<td class="cebedo-text-align-right">${directCost.cost}</td>
						                                                	<td class="cebedo-text-align-right">${directCost.actualCost}</td>
						                                                	<td class="cebedo-text-align-right">${directCost.getDiffEstimatedActualAsHTML()}</td>
							                                            </tr>
							                                            </c:forEach>
								                                    </tbody>
								                                </table>
							                                </div><!-- /.box-body -->
							                             </div>
							                        </div>
			                   						<div class="col-md-6"> <!-- Indirect costs -->
					                                	<div class="box box-body box-default">
					                                		<div class="box-header">
															<h3 class="box-title">Indirect Costs</h3>
															</div>
							                                <div class="box-body">
							                                    <table id="estimated-indirect-costs" class="table table-bordered table-striped">	
							                                    	<thead>
							                                            <tr>
							                                            	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_UPDATE', 'ESTIMATE_DELETE')">
							                                            	<th>&nbsp;</th>
							                                            	</sec:authorize>
							                                            	
							                                                <th>Name</th>
							                                                <th>Estimated (&#8369;)</th>
							                                                <th>Actual (&#8369;)</th>
							                                                <th>Difference (&#8369;)</th>
							                                            </tr>
					                                        		</thead>
							                                        <tbody>
								                                		<c:forEach items="${indirectCostList}" var="indirectCost">
							                                            <tr>
							                                            	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_UPDATE', 'ESTIMATE_DELETE')">
							                                            	<td>
							                                            		<center>
							                                            			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_UPDATE')">
							                                            			<a href="<c:url value="/project/edit/cost/${indirectCost.getKey()}-end"/>">
											                                    		<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
							                                            			</a>
							                                            			</sec:authorize>
							                                            			
							                                            			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_DELETE')">
												                                    <div class="btn-group">
												                                    <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
												                                    <ul class="dropdown-menu">
												                                    	<li>
												                                    		<a href="<c:url value="/project/delete/cost/${indirectCost.getKey()}-end"/>" class="cebedo-dropdown-hover">
												                                        		Confirm Delete
												                                        	</a>
												                                    	</li>
												                                    </ul>
												                                    </div>
												                                    </sec:authorize>
																				</center>
																			</td>
																			</sec:authorize>
						                                                	<td>${indirectCost.name}</td>
						                                                	<td class="cebedo-text-align-right">${indirectCost.cost}</td>
						                                                	<td class="cebedo-text-align-right">${indirectCost.actualCost}</td>
						                                                	<td class="cebedo-text-align-right">${indirectCost.getDiffEstimatedActualAsHTML()}</td>
							                                            </tr>
							                                            </c:forEach>
								                                    </tbody>
								                                </table>
							                                </div><!-- /.box-body -->
							                             </div>
							                        </div>
							                   	</div> <!-- End of Row -->
											</div>
											<div class="tab-pane" id="tab_calculator">
							                   	<div class="row">
							                   	
							                   		<sec:authorize access="!hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_CREATE')">
			                   						<div class="col-md-12">
			                   						</sec:authorize>
							                   	
							                   		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_CREATE')">
			                   						<div class="col-md-9">
			                   						</sec:authorize>
					                                	<div class="box box-body box-default">
			<!-- 				                                		<div class="box-header"> -->
			<!-- 			              									<h3 class="box-title">Staff Members</h3> -->
			<!-- 			              								</div> -->
							                                <div class="box-body">
							                                    <table id="estimate-output-table" class="table table-bordered table-striped">	
							                                    	<thead>
							                                            <tr>
							                                            	<th>&nbsp;</th>
							                                                <th>Name</th>
							                                                <th>Remarks</th>
							                                                <th>Allowance</th>
							                                                <th>Time Computed</th>
							                                            </tr>
					                                        		</thead>
							                                        <tbody>
								                                		<c:forEach items="${estimationOutputList}" var="estimationOutput">
							                                            <tr>
							                                            	<td>
							                                            		<center>
							                                            			<c:url var="urlView" value="/project/view/estimation/${estimationOutput.getKey()}-end"/>
							                                            			<a href="${urlView}">
											                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
							                                            			</a>

							                                            			<a href="<c:url value="/project/export-xls/estimationoutput/${estimationOutput.getKey()}-end"/>">
										                                        		<button class="btn btn-cebedo-export btn-flat btn-sm">Export</button>
										                                        	</a>
										                                        	
										                                        	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_DELETE')">
												                                    <div class="btn-group">
												                                    <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
												                                    <ul class="dropdown-menu">
												                                    	<li>
												                                    		<c:url value="/project/delete/estimation/${estimationOutput.getKey()}-end" var="urlDelete"/>
												                                    		<a href="${urlDelete}" class="cebedo-dropdown-hover">
												                                        		Confirm Delete
												                                        	</a>
												                                    	</li>
												                                    </ul>
												                                    </div>
												                                    </sec:authorize>
																				</center>
																			</td>
						                                                	<td>${estimationOutput.name}</td>
						                                                	<td>${estimationOutput.remarks}</td>
						                                                	<td>${estimationOutput.estimationAllowance.getLabel()}</td>
						                                                	<fmt:formatDate pattern="yyyy/MM/dd hh:mm:ss a" value="${estimationOutput.lastComputed}" var="timeComputed"/>
						                                                	<td>${timeComputed}</td>
							                                            </tr>
							                                            </c:forEach>
								                                    </tbody>
								                                </table>
							                                </div><!-- /.box-body -->
							                             </div>
							                        </div>
							                        
							                        <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_CREATE')">
			                   						<div class="col-md-3">
			                   							<div class="box box-body box-default">
			                   								<div class="box-header">
			                   									<h3 class="box-title">Estimation Input</h3>
			                   								</div>
			                   								<div class="box-body">
			                   								<form:form modelAttribute="estimationInput"
																action="${contextPath}/project/create/estimate"
																method="post"
																enctype="multipart/form-data">
			<!--	 allowanceList -->
			<!--     private TableEstimationAllowance estimationAllowance; -->
			<!--     private MultipartFile estimationFile; -->
						                                        <div class="form-group">
						                                        
																<label>Name</label>
																<form:input placeholder="Sample: Project estimate, Estimation" class="form-control" path="name"/>
																<p class="help-block">Enter the name of this estimate</p>
				
						                                        <label>Excel File</label>
																<form:input type="file" class="form-control" path="estimationFile"/>
																<p class="help-block">Choose the Excel file which contains the inputs</p>
																
				                                                <label>Estimation Allowance</label>
				                                                <form:select class="form-control" path="estimationAllowance"> 
			                                   						<c:forEach items="${allowanceList}" var="allowance"> 
			                                   							<form:option value="${allowance}" label="${allowance.getLabel()}"/> 
			                                   						</c:forEach> 
				                                    			</form:select>
				                                    			<p class="help-block">Alot allowance for wastage</p>
				                                    			
																<label>Remarks</label>
																<form:input placeholder="Sample: This is the official estimate" class="form-control" path="remarks"/>
																<p class="help-block">Add additional information</p>
				                                    			
				                                    			<button class="btn btn-cebedo-create btn-flat btn-sm">Estimate</button>
						                                        </div>
					                                        </form:form>
			                   								</div>
			                   							</div>
			                   						</div>
			                   						</sec:authorize>
			                   						
			              						</div>
			                                </div><!-- /.tab-pane -->
										</div>
									</div>
								</div>
								</sec:authorize>

								<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'EQUIPMENT_VIEW')">
								<div class="tab-pane" id="tab_equipment_expenses">
                                	<div class="row">
										<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
               										<div id="highcharts-equipment-expenses" style="height: 300px"></div>													
				                            	</div>
				                            </div>
				                        </div>
										<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
               										<div id="highcharts-equipment-expenses-cumulative" style="height: 300px"></div>													
				                            	</div>
				                            </div>
				                        </div>
              						</div>
                                	<div class="row">
                                		<sec:authorize access="!hasAnyRole('ADMIN_COMPANY', 'EQUIPMENT_CREATE')">
										<div class="col-md-12">
										</sec:authorize>
										
										<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'EQUIPMENT_CREATE')">
										<div class="col-md-9">
										</sec:authorize>
                   							<div class="box box-body box-default">
                   								<div class="box-body">

                   									<c:if test="${!empty equipmentExpenseList}">
	                   									<a href="<c:url value="/project/export-xls/equipment-expenses"/>">
			                                        		<button class="btn btn-cebedo-export btn-flat btn-sm">Export All</button>
			                                        	</a>
			                                        	<br/>
			                                        	<br/>
		                                        	</c:if>

			                                  		<div class="pull-right">
			                                  		<h3>Grand Total <b><u>
				                                	${projectAux.getGrandTotalEquipmentExpensesAsString()}
													</u></b></h3>
													</div>

			                                		<table id="equipment-table" class="table table-bordered table-striped">
				                                    	<thead>
				                                            <tr>
				                                            	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'EQUIPMENT_UPDATE', 'EQUIPMENT_DELETE')">
				                                            	<th>&nbsp;</th>
				                                            	</sec:authorize>
				                                                <th>Date</th>
				                                                <th>Name</th>
				                                                <th>Staff</th>
				                                                <th>Cost (&#8369;)</th>
				                                            </tr>
			                                    		</thead>
				                                        <tbody>
						                                	<c:forEach items="${equipmentExpenseList}" var="expense">
					                                            <tr>
					                                            	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'EQUIPMENT_UPDATE', 'EQUIPMENT_DELETE')">
					                                            	<td>
					                                            		<center>
					                                            			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'EQUIPMENT_UPDATE')">
					                                            			<a href="<c:url value="/project/edit/equipmentexpense/${expense.getKey()}-end"/>">
									                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
					                                            			</a>
					                                            			</sec:authorize>
					                                            			
					                                            			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'EQUIPMENT_DELETE')">
										                                    <div class="btn-group">
										                                    <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
										                                    <ul class="dropdown-menu">
										                                    	<li>
										                                    		<a href="<c:url value="/project/delete/equipmentexpense/${expense.getKey()}-end"/>" class="cebedo-dropdown-hover">
										                                        		Confirm Delete
										                                        	</a>
										                                    	</li>
										                                    </ul>
										                                    </div>
										                                    </sec:authorize>
																		</center>
																	</td>
																	</sec:authorize>
				                                                	<fmt:formatDate value="${expense.date}" var="dateString" pattern="yyyy/MM/dd" />
				                                                	<td>${dateString}</td>
				                                                	<td>${expense.name}</td>
				                                                	
				                                                	<sec:authorize access="!hasAnyRole('ADMIN_COMPANY', 'STAFF_VIEW')">
				                                                	<td>${expense.staff.getFullName()}</td>
																	</sec:authorize>
																	
																	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_VIEW')">
																	<td>
																	<c:url var="urlLink" value="/project/edit/staff/${expense.staff.id}"/>
								                                    <a href="${urlLink}" class="general-link">
																	${expense.staff.getFullName()}
								                                    </a>
																	</td>
																	</sec:authorize>
				                                                	
				                                                	<td style="text-align: right;">${expense.cost}</td>
					                                            </tr>
				                                            </c:forEach>
					                                    </tbody>
					                                </table>
				                            	</div>
				                            </div>
				                        </div>
				                        
				                        <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'EQUIPMENT_CREATE')">
                   						<div class="col-md-3">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
                   									<div class="form-group">
	                   									<form:form 
	                   										modelAttribute="equipmentexpense"
															id="equipmentExpenseForm"
															method="post"
															action="${contextPath}/project/create/equipmentexpense">
					                                        <div class="form-group">

					                                            <label>Name</label>
					                                            <form:input type="text" class="form-control" path="name"
					                                            	placeholder="Sample: Repair, Gasoline, Usage"/>
					                                            <p class="help-block">Enter the name of this equipment expenditure</p>

					                                            <label>Cost</label>
					                                            <form:input type="text" class="form-control" path="cost"
					                                            	placeholder="Sample: 350, 600, 700, 800, 950"/>
					                                            <p class="help-block">Enter the cost of the equipment expenditure</p>

					                                            <label>Staff</label>
					                                            <form:select class="form-control" path="staffID"> 
		                                     						<c:forEach items="${project.assignedStaff}" var="staff"> 
		                                     							<form:option value="${staff.id}" label="${staff.getFullName()}"/> 
		                                     						</c:forEach> 
		 		                                    			</form:select> 
		 		                                    			<p class="help-block">Choose the staff who conducted the expenditure</p>

					                                            <label>Date</label>
						                                        <div class="input-group">
						                                            <div class="input-group-addon">
						                                                <i class="fa fa-calendar"></i>
						                                            </div>
						                                            <fmt:formatDate value="${equipmentexpense.date}" var="dateString" pattern="yyyy/MM/dd" />
						                                            <form:input type="text" class="form-control date-picker" path="date" placeholder="Sample: 2016/06/25" value="${dateString}"/>
						                                        </div>
					                                            <p class="help-block">Enter the date when the equipment expenditure happened</p>

					                                        </div>
					                                    </form:form>
					                                    <c:if test="${!empty project.assignedStaff}">
	                                            		<button class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton" onclick="submitForm('equipmentExpenseForm')">Create</button>
	                                            		</c:if>	                                            		
														<c:if test="${empty project.assignedStaff}">
															<div class="callout callout-warning">
																<p>Please assign <b>staff members</b> first.</p>
															</div>
									                	</c:if>
													</div>	
												</div>	
                   							</div>
                   						</div>
                   						</sec:authorize>
              						</div>
                                </div><!-- /.tab-pane -->
                                </sec:authorize>
                                
                                <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'OTHER_EXPENSES_VIEW')">
								<div class="tab-pane" id="tab_other_expenses">
                                	<div class="row">
										<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
               										<div id="highcharts-other-expenses" style="height: 300px"></div>													
				                            	</div>
				                            </div>
				                        </div>
										<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
               										<div id="highcharts-other-expenses-cumulative" style="height: 300px"></div>													
				                            	</div>
				                            </div>
				                        </div>
              						</div>
                                	<div class="row">
                                	
                                		<sec:authorize access="!hasAnyRole('ADMIN_COMPANY', 'OTHER_EXPENSES_CREATE')">
										<div class="col-md-12">
										</sec:authorize>
                                	
                                		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'OTHER_EXPENSES_CREATE')">
										<div class="col-md-9">
										</sec:authorize>
                   							<div class="box box-body box-default">
                   								<div class="box-body">

                   									<c:if test="${!empty expenseList}">
	                   									<a href="<c:url value="/project/export-xls/expenses"/>">
			                                        		<button class="btn btn-cebedo-export btn-flat btn-sm">Export All</button>
			                                        	</a>
			                                        	<br/>
			                                        	<br/>
		                                        	</c:if>

			                                  		<div class="pull-right">
			                                  		<h3>Grand Total <b><u>
				                                	${projectAux.getGrandTotalOtherExpensesAsString()}
													</u></b></h3>
													</div>

			                                		<table id="other-expenses-table" class="table table-bordered table-striped">
				                                    	<thead>
				                                            <tr>
				                                            	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'OTHER_EXPENSES_UPDATE', 'OTHER_EXPENSES_DELETE')">
				                                            	<th>&nbsp;</th>
				                                            	</sec:authorize>
				                                            	
				                                                <th>Date</th>
				                                                <th>Name</th>
				                                                <th>Staff</th>
				                                                <th>Cost (&#8369;)</th>
				                                            </tr>
			                                    		</thead>
				                                        <tbody>
						                                	<c:forEach items="${expenseList}" var="expense">
					                                            <tr>
			                                            			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'OTHER_EXPENSES_UPDATE', 'OTHER_EXPENSES_DELETE')">
					                                            	<td>
					                                            		<center>
					                                            			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'OTHER_EXPENSES_UPDATE')">
					                                            			<a href="<c:url value="/project/edit/expense/${expense.getKey()}-end"/>">
									                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
					                                            			</a>
					                                            			</sec:authorize>
					                                            			
					                                            			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'OTHER_EXPENSES_DELETE')">
										                                    <div class="btn-group">
										                                    <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
										                                    <ul class="dropdown-menu">
										                                    	<li>
										                                    		<a href="<c:url value="/project/delete/expense/${expense.getKey()}-end"/>" class="cebedo-dropdown-hover">
										                                        		Confirm Delete
										                                        	</a>
										                                    	</li>
										                                    </ul>
										                                    </div>
										                                    </sec:authorize>
																		</center>
																	</td>
																	</sec:authorize>
				                                                	<fmt:formatDate value="${expense.date}" var="dateString" pattern="yyyy/MM/dd" />
				                                                	<td>${dateString}</td>
				                                                	<td>${expense.name}</td>
				                                                	<sec:authorize access="!hasAnyRole('ADMIN_COMPANY', 'STAFF_VIEW')">
				                                                	<td>${expense.staff.getFullName()}</td>
																	</sec:authorize>
																	
																	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_VIEW')">
																	<td>
																	<c:url var="urlLink" value="/project/edit/staff/${expense.staff.id}"/>
								                                    <a href="${urlLink}" class="general-link">
																	${expense.staff.getFullName()}
								                                    </a>
																	</td>
																	</sec:authorize>
				                                                	<td style="text-align: right;">${expense.cost}</td>
					                                            </tr>
				                                            </c:forEach>
					                                    </tbody>
					                                </table>
				                            	</div>
				                            </div>
				                        </div>
				                        
				                        <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'OTHER_EXPENSES_CREATE')">
                   						<div class="col-md-3">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
                   									<div class="form-group">
	                   									<form:form 
	                   										modelAttribute="expense"
															id="expenseForm"
															method="post"
															action="${contextPath}/project/create/expense">
					                                        <div class="form-group">

					                                            <label>Name</label>
					                                            <form:input type="text" class="form-control" path="name"
					                                            	placeholder="Sample: Signage, Legal papers, Consultants"/>
					                                            <p class="help-block">Enter the name of this expense</p>

					                                            <label>Cost</label>
					                                            <form:input type="text" class="form-control" path="cost"
					                                            	placeholder="Sample: 350, 600, 700, 800, 950"/>
					                                            <p class="help-block">Enter the cost of the expense</p>

					                                            <label>Staff</label>
					                                            <form:select class="form-control" path="staffID"> 
		                                     						<c:forEach items="${project.assignedStaff}" var="staff"> 
		                                     							<form:option value="${staff.id}" label="${staff.getFullName()}"/> 
		                                     						</c:forEach> 
		 		                                    			</form:select> 
		 		                                    			<p class="help-block">Choose the staff who conducted the expenditure</p>

					                                            <label>Date</label>
						                                        <div class="input-group">
						                                            <div class="input-group-addon">
						                                                <i class="fa fa-calendar"></i>
						                                            </div>
						                                            <fmt:formatDate value="${expense.date}" var="dateString" pattern="yyyy/MM/dd" />
						                                            <form:input type="text" class="form-control date-picker" path="date" placeholder="Sample: 2016/06/25" value="${dateString}"/>
						                                        </div>
					                                            <p class="help-block">Enter the date when the expense happened</p>

					                                        </div>
					                                    </form:form>
					                                    <c:if test="${!empty project.assignedStaff}">
	                                            		<button class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton" onclick="submitForm('expenseForm')">Create</button>
	                                            		</c:if>	                                            		
														<c:if test="${empty project.assignedStaff}">
															<div class="callout callout-warning">
																<p>Please assign <b>staff members</b> first.</p>
															</div>
									                	</c:if>
													</div>	
												</div>	
                   							</div>
                   						</div>
                   						</sec:authorize>
              						</div>
                                </div><!-- /.tab-pane -->
                                </sec:authorize>
                                
                                <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'DOWNLOADS_VIEW')">
								<div class="tab-pane" id="tab_downloads">
                                	<div class="row">
										<div class="col-md-12">
               							<div class="box box-body box-default">
               							<div class="box-header">
           									<h3 class="box-title">Reports</h3>
           								</div>
               							<div class="box-body">
                                		<table class="table table-bordered table-striped is-data-table">
                                    	<thead>
                                            <tr>
                                           	<th>&nbsp;</th>
                                               <th>Name</th>
                                               <th>Description</th>
                                            </tr>
                                   		</thead>
                                        <tbody>
                                            <tr>
                                            	<td>
                                            		<center>
                                            			<a href="<c:url value="/project/export-xls/balance-sheet"/>">
				                                    	<button class="btn btn-cebedo-export btn-flat btn-sm">Excel</button>
                                            			</a>
													</center>
												</td>
                                               	<td>Balance Sheet (Overall)</td>
                                               	<td>A summarized report of all expenses throughout the course of the project.</td>
                                            </tr>
                                            <tr>
                                            	<td>
                                            		<center>
                                            			<a href="<c:url value="/project/export-xls/analysis"/>">
				                                    	<button class="btn btn-cebedo-export btn-flat btn-sm">Excel</button>
                                            			</a>
													</center>
												</td>
                                               	<td>Analysis (Overall)</td>
                                               	<td>Analyze the project and export a file containing resulting values.</td>
                                            </tr>
                                            <tr>
                                            	<td>
                                            		<center>
                                            			<form:form 
	                   										modelAttribute="balanceSheetDateRange"
															method="post"
															action="${contextPath}/project/export-xls/balance-sheet/range">
					                                        <div class="form-group">
					                                        
					                                            <label>Start Date</label>
						                                        <div class="input-group">
						                                            <div class="input-group-addon">
						                                                <i class="fa fa-calendar"></i>
						                                            </div>
						                                            <form:input type="text" class="form-control date-picker" path="startDate" placeholder="Sample: 2016/06/25"/>
						                                        </div>
						                                        <br/>
					                                            <label>End Date</label>
						                                        <div class="input-group">
						                                            <div class="input-group-addon">
						                                                <i class="fa fa-calendar"></i>
						                                            </div>
						                                            <form:input type="text" class="form-control date-picker" path="endDate" placeholder="Sample: 2016/06/25"/>
						                                        </div>
					                                        </div>
					                                    	<button class="btn btn-cebedo-export btn-flat btn-sm">Excel</button>
					                                    </form:form>
													</center>
												</td>
                                               	<td>Balance Sheet (Date Range)</td>
                                               	<td>A summarized report of all expenses given a specific start and end date.</td>
                                            </tr>
	                                    </tbody>
		                                </table>
		                            	</div>
			                            </div>
				                        </div>
              						</div>
                                	<div class="row">
										<div class="col-md-6">
               							<div class="box box-body box-default">
               							<div class="box-header">
           									<h3 class="box-title">Project Setup</h3>
           								</div>
               							<div class="box-body">
                                		<table class="table table-bordered table-striped is-data-table">
                                    	<thead>
                                            <tr>
                                           	<th>&nbsp;</th>
                                               <th>Name</th>
                                               <th>Description</th>
                                            </tr>
                                   		</thead>
                                        <tbody>
                                        	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_VIEW')">
                                            <tr>
                                            	<td>
                                            		<center>
                                            			<a href="<c:url value="/project/export-xls/staff-members"/>">
				                                    	<button class="btn btn-cebedo-export btn-flat btn-sm">Excel</button>
                                            			</a>
													</center>
												</td>
                                               	<td>Assigned Staff</td>
                                               	<td>The list of assigned staff members.</td>
                                            </tr>
                                            </sec:authorize>
                                            
                                            <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_VIEW')">
                                            <tr>
                                            	<td>
                                            		<center>
                                            			<a href="<c:url value="/project/export-xls/costs"/>">
				                                    	<button class="btn btn-cebedo-export btn-flat btn-sm">Excel</button>
                                            			</a>
													</center>
												</td>
                                               	<td>Estimation</td>
                                               	<td>Project estimation and estimated costs.</td>
                                            </tr>
                                            </sec:authorize>
                                            
                                            <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'PROGRAM_OF_WORKS_VIEW')">
                                            <tr>
                                            	<td>
                                            		<center>
                                            			<a href="<c:url value="/project/export-xls/program-of-works"/>">
				                                    	<button class="btn btn-cebedo-export btn-flat btn-sm">Excel</button>
                                            			</a>
													</center>
												</td>
                                               	<td>Program of Works</td>
                                               	<td>Tasks defined in the program of works.</td>
                                            </tr>
                                            </sec:authorize>
	                                    </tbody>
		                                </table>
		                            	</div>
			                            </div>
				                        </div>
										<div class="col-md-6">
               							<div class="box box-body box-default">
               							<div class="box-header">
           									<h3 class="box-title">Expenses</h3>
           								</div>
               							<div class="box-body">
                                		<table class="table table-bordered table-striped is-data-table">
                                    	<thead>
                                            <tr>
                                           	<th>&nbsp;</th>
                                               <th>Name</th>
                                               <th>Description</th>
                                            </tr>
                                   		</thead>
                                        <tbody>
                                        	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'PAYROLL_VIEW')">
                                            <tr>
                                            	<td>
                                            		<center>
                                            			<a href="<c:url value="/project/export-xls/payroll/all"/>">
				                                    	<button class="btn btn-cebedo-export btn-flat btn-sm">Excel</button>
                                            			</a>
													</center>
												</td>
                                               	<td>Payrolls</td>
                                               	<td>All payrolls compiled as one file.</td>
                                            </tr>
                                            </sec:authorize>
                                            
                                            <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'INVENTORY_VIEW')">
                                            <tr>
                                            	<td>
                                            		<center>
                                            			<a href="<c:url value="/project/export-xls/inventory"/>">
				                                    	<button class="btn btn-cebedo-export btn-flat btn-sm">Excel</button>
                                            			</a>
													</center>
												</td>
                                               	<td>Inventory</td>
                                               	<td>Project deliveries, materials & pull-outs.</td>
                                            </tr>
                                            </sec:authorize>
                                            
                                            <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'EQUIPMENT_VIEW')">
                                            <tr>
                                            	<td>
                                            		<center>
                                            			<a href="<c:url value="/project/export-xls/equipment-expenses"/>">
				                                    	<button class="btn btn-cebedo-export btn-flat btn-sm">Excel</button>
                                            			</a>
													</center>
												</td>
                                               	<td>Equipment</td>
                                               	<td>Expenses related to equipments.</td>
                                            </tr>
                                            </sec:authorize>
                                            
                                            <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'OTHER_EXPENSES_VIEW')">
                                            <tr>
                                            	<td>
                                            		<center>
                                            			<a href="<c:url value="/project/export-xls/expenses"/>">
				                                    	<button class="btn btn-cebedo-export btn-flat btn-sm">Excel</button>
                                            			</a>
													</center>
												</td>
                                               	<td>Other Expenses</td>
                                               	<td>Export the other expenses.</td>
                                            </tr>
                                            </sec:authorize>
	                                    </tbody>
		                                </table>
		                            	</div>
			                            </div>
				                        </div>
              						</div>
                                </div><!-- /.tab-pane -->
                                </sec:authorize>
                                
                                <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'PROGRAM_OF_WORKS_VIEW')">
                                <div class="tab-pane" id="tab_timeline">
                                	<div class="nav-tabs-custom">
									<ul class="nav nav-tabs" id="subtabs-timeline">
										<li class="active"><a href="#subtab_chart" data-toggle="tab">Gantt Chart</a></li>
										<li><a href="#subtab_tasks" data-toggle="tab">Tasks</a></li>
									</ul>
									<div class="tab-content">

									
										<div class="tab-pane active" id="subtab_chart">
											<div class="row">
		                   						<div class="col-md-6">
		                   							<div class="box box-body box-default">
		                   								<div class="box-body">
		                   									<div id="highcharts-tasks" style="height: 300px"></div>
		                   								</div>
		                   							</div>
		                   						</div>
		                   						<div class="col-md-6">
		                   							<div class="box box-body box-default">
		                   								<div class="box-body">
														<table id="task-status-table" class="table table-bordered table-striped">
														<thead>
				                                    		<tr>
					                                            <th>Task Status</th>
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
											<div class="row">
		                   						<div class="col-md-12">
											                <c:if test="${!empty project.assignedTasks}">
							                                <table>
			               										<tr>
			           											<td>Legend:
			           											</td>
			           											<td>&nbsp;</td>
			           											<td>
																<c:forEach items="${ganttElemTypeList}" var="ganttElem">
																	<c:set value="" var="border"></c:set>
																	<c:if test="${ganttElem.className().contains(\"btn-default\")}">
																		<c:set value="border: 1px solid #999999;" var="border"></c:set>
																	</c:if>
																	<span class="label ${ganttElem.className()}"
																	style="
																	color: ${ganttElem.color()};
																	background-color: ${ganttElem.backgroundColor()};
																	${border};
																	">
																	${ganttElem.label()}
																	</span>
																	&nbsp;
																</c:forEach>
			           											</td>
			               										</tr>
			               									</table>
			               									<br/>
			               									</c:if>
						                                	<div id="gantt-chart" class="gantt-holder"></div>
					                            </div>
				                            </div>
										</div>
										
										<div class="tab-pane" id="subtab_tasks">
	              						<div class="row">
	                   						<div class="col-md-12">
	                   							<div class="box box-body box-default">
	                   								<div class="box-body">
	                   										<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'PROGRAM_OF_WORKS_CREATE')">
										                	<form:form modelAttribute="massUploadBean"
																action="${contextPath}/project/mass/upload-and-assign/task"
																method="post"
																enctype="multipart/form-data">
															
																<div class="form-group">
																<label>Excel File</label>
																<form:input type="file" class="form-control" path="file"/><br/>
																<button class="btn btn-cebedo-create btn-flat btn-sm">Upload Tasks</button>
																</div>
															</form:form>
															</sec:authorize>
															
						                                	<table>
						                                    	<tr>
						                                    		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'PROGRAM_OF_WORKS_CREATE')">
						                                    		<td>
						                                    			<c:url value="/project/edit/task/0" var="urlAddTask"/>
						                                    			<a href="${urlAddTask}">
								                                    	<button class="btn btn-cebedo-create btn-flat btn-sm">Create Task</button>
						                                    			</a>
						                                    		</td>
						                                    		</sec:authorize>
						                                    		
						                                    		<c:if test="${!empty project.assignedTasks}">
						                                    		<td>&nbsp;</td>
						                                    		<td>
							                                    		<a href="<c:url value="/project/export-xls/program-of-works"/>">
							                                        		<button class="btn btn-cebedo-export btn-flat btn-sm">Export All</button>
							                                        	</a>
						                                    		</td>
						                                    		
						                                    		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'PROGRAM_OF_WORKS_DELETE')">
						                                    		<td>&nbsp;</td>
						                                    		<td>
				               											<!-- Delete All button -->
				               											<div class="btn-group">
								                                        <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete All</button>
								                                        <ul class="dropdown-menu">
								                                        	<li>								                                        		
								                                        		<c:url value="/project/delete/task/all" var="urlButton"/>
								                                        		<a href="${urlButton}" class="cebedo-dropdown-hover">
								                                            		Confirm Delete All
								                                            	</a>
								                                        	</li>
								                                        </ul>
								                                        </div>
						                                    		</td>
						                                    		</sec:authorize>
						                                    		</c:if>
						                                    	</tr>
						                                    </table><br/>
						                                    <table id="tasks-table" class="table table-bordered table-striped">
						                                    	<thead>
						                                            <tr>
							                                        	<th>&nbsp;</th>
							                                            <th>Status</th>
							                                            <th>Planned Start & Duration</th>
							                                            <th>Planned End</th>
							                                            <th>Actual Start & Duration</th>
							                                            <th>Actual End</th>
							                                            <th>Title</th>
							                                            <th>Staff</th>
							                                        </tr>
				                                        		</thead>
						                                        <tbody>
							                                        <c:set var="taskList" value="${project.assignedTasks}"/>
								                                	<c:if test="${!empty taskList}">
						                                        		<c:forEach items="${taskList}" var="task">
						                                        			<tr>
						                                        				<td style="text-align: center;">
						                                        				
						                                        					<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'PROGRAM_OF_WORKS_UPDATE')">
						                                        					<div class="btn-group">
											                                            <button type="button" class="btn btn-cebedo-update btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">
											                                                Mark As&nbsp;
											                                                <span class="caret"></span>
											                                            </button>
											                                            <ul class="dropdown-menu">
											                                            	<c:forEach items="${taskStatusList}" var="taskStatus">
											                                                	<li><a class="cebedo-dropdown-orange-hover" href="<c:url value="/project/mark/task/?task_id=${task.id}&status=${taskStatus.id()}"/>">${taskStatus.label()}</a></li>
											                                            	</c:forEach>
											                                            </ul>
											                                        </div>
											                                        </sec:authorize>
											                                        
											                                        <!-- View button -->
											                                        <c:url value="/project/edit/task/${task.id}" var="urlViewTask"/>
											                                        <a href="${urlViewTask}">
									                                            	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
											                                        </a>
											                                        
											                                        <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'PROGRAM_OF_WORKS_DELETE')">
											                                        <!-- Delete button -->
											                                        <div class="btn-group">
											                                        <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
											                                        <ul class="dropdown-menu">
											                                        	<li>
											                                        		<a href="<c:url value="/project/delete/task/${task.id}"/>" class="cebedo-dropdown-hover">
											                                            		Confirm Delete
											                                            	</a>
											                                        	</li>
											                                        </ul>
											                                        </div>
											                                        </sec:authorize>
						                                        				</td>
									                                            <td>
										                                            <c:set value="${task.getStatusEnum().css()}" var="css"></c:set>
																					<span class="label ${css}">${task.getStatusEnum()}</span>
									                                            </td>
									                                            <fmt:formatDate pattern="yyyy/MM/dd" value="${task.dateStart}" var="taskStartDate"/>
									                                            <td>${taskStartDate} (${task.duration})</td>
									                                            <fmt:formatDate pattern="yyyy/MM/dd" value="${task.getEndDate()}" var="taskEndDate"/>
									                                            <td>${taskEndDate}</td>
									                                            <td><fmt:formatDate pattern="yyyy/MM/dd" value="${task.actualDateStart}"/> (${task.actualDuration})</td>
									                                            <td><fmt:formatDate pattern="yyyy/MM/dd" value="${task.getActualEndDate()}"/></td>
									                                            <td>${task.title}</td>
									                                            <td>
									                                            	<c:choose>
									                                            		<c:when test="${!empty task.staff}">
									                                            			<c:forEach items="${task.staff}" var="taskStaff">
									                                            			
									                                            			<c:set var="taskStaffName" value="${taskStaff.getFullName()}"/>
									                                            			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_VIEW')">
									                                            			<a class="general-link" href="<c:url value="/project/edit/staff/${taskStaff.id}"/>">
											                                            	${taskStaffName}
											                                            	</a>
											                                            	</sec:authorize>
											                                            	
									                                            			<sec:authorize access="!hasAnyRole('ADMIN_COMPANY', 'STAFF_VIEW')">
									                                            			${taskStaffName}
											                                            	</sec:authorize>
											                                            	
											                                            	<br/>
									                                            			</c:forEach>
									                                            		</c:when>
									                                            		<c:when test="${empty task.staff}">
									                                            			No staff assigned.
									                                            		</c:when>
									                                            	</c:choose>					                                            
									                                            </td>
									                                        </tr>
						                                        		</c:forEach>
					                                        		</c:if>
							                                    </tbody>
							                                </table>
						                                </div><!-- /.box-body -->
						                            </div>
					                           	</div>
				                           	</div>
										</div>
									</div>
									</div>
                                </div><!-- /.tab-pane -->
                                </sec:authorize>
                                
                                <div class="tab-pane" id="tab_calendar">
                               	<div class="row">
               						<div class="col-md-12">
               							<div class="box box-body box-default">
               								<div class="box-header">
<!--                    									List<Map<String, String>> getEventTypePropertyMaps -->
               									<h3 class="box-title">Calendar</h3>
               								</div>
               								<div class="box-body">
               									<table>
               										<tr>
           											<td>Legend:
           											</td>
           											<td>&nbsp;</td>
           											<td>
													<c:forEach items="${calendarEventTypes}" var="calendarEvent">
														<span class="label ${calendarEvent.css()}">
														${calendarEvent}
														</span>
														&nbsp;
													</c:forEach>
           											</td>
               										</tr>
               									</table><br/>
               									<div id='calendar'></div>
               								</div>
               							</div>
               						</div>
          						</div>
           						</div>
           						
           						<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'PAYROLL_VIEW')">
                                <div class="tab-pane" id="tab_payroll">
		                            <div class="row">
               						<div class="col-md-6">
               							<div class="box box-body box-default">
               								<div class="box-body">
               									<div id="highcharts-payroll" style="height: 300px"></div>
               								</div>
               							</div>
               						</div>
               						<div class="col-md-6">
               							<div class="box box-body box-default">
               								<div class="box-body">
               									<div id="highcharts-payroll-cumulative" style="height: 300px"></div>
               								</div>
               							</div>
               						</div>
               						</div>
                                	<div class="row">
		                            <div class="col-md-12">
               							<div class="box box-body box-default">
               								<div class="box-body">

		                                  		<div class="pull-right">
		                                  		<h3>Grand Total <b><u>
			                                	${projectAux.getGrandTotalPayrollAsString()}
												</u></b></h3>
												</div>
												
												<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'PAYROLL_CREATE')">
										  	  	<c:url var="urlCreateTeam" value="/project/edit/payroll/0-end"/>
		                                  		<a href="${urlCreateTeam}">
		                                    		<button class="btn btn-cebedo-create btn-flat btn-sm">Create Payroll</button>
		                                  		</a>
		                                  		</sec:authorize>

												<sec:authorize access="hasRole('ADMIN_SUPER')">
										  	  	<c:url var="urlCreateTeam" value="/project/compute/payroll/all"/>
		                                  		<a href="${urlCreateTeam}">
		                                    		<button class="btn btn-cebedo-create btn-flat btn-sm">Compute All</button>
		                                  		</a>
		                                  		</sec:authorize>
		                                  		
		                                  		<c:if test="${!empty payrollList}">
		                                  		<a href="<c:url value="/project/export-xls/payroll/all"/>">
	                                        		<button class="btn btn-cebedo-export btn-flat btn-sm">Export All</button>
	                                        	</a>
		                                  		</c:if>
		                                  		<br/>
		                                  		<br/>
			                                    <table id="payroll-table" class="table table-bordered table-striped">
			                                    	<thead>
			                                            <tr>
			                                            	<th>&nbsp;</th>
			                                                <th>Start Date</th>
			                                                <th>End Date</th>
			                                                <th>Creator</th>
			                                                <th>Status</th>
			                                                <th>Payroll Total (&#8369;)</th>
			                                                <th>Last Computed</th>
			                                            </tr>
	                                        		</thead>
			                                        <tbody>
				                                		<c:forEach items="${payrollList}" var="payrollRow">
														<fmt:formatDate pattern="yyyy.MM.dd" value="${payrollRow.startDate}" var="payrollStartDate"/>
														<fmt:formatDate pattern="yyyy.MM.dd" value="${payrollRow.endDate}" var="payrollEndDate"/>
				                                		
			                                            <tr>
			                                            	<td>
			                                            		<center>
			                                            			<c:url var="urlEditPayroll" value="/project/edit/payroll/${payrollRow.getKey()}-end"/>
			                                            			<a href="${urlEditPayroll}">
							                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
			                                            			</a>

			                                            			<a href="<c:url value="/project/export-xls/payroll/${payrollRow.getKey()}-end"/>">
						                                        		<button class="btn btn-cebedo-export btn-flat btn-sm">Export</button>
						                                        	</a>
						                                        	
						                                        	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'PAYROLL_DELETE')">
								                                    <div class="btn-group">
								                                    <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
								                                    <ul class="dropdown-menu">
								                                    	<li>
								                                    		<c:url value="/project/delete/payroll/${payrollRow.getKey()}-end" var="urlDeletePayroll"/>
								                                    		<a href="${urlDeletePayroll}" class="cebedo-dropdown-hover">
								                                        		Confirm Delete
								                                        	</a>
								                                    	</li>
								                                    </ul>
								                                    </div>
								                                    </sec:authorize>
								                                    
																</center>
															</td>
															<fmt:formatDate pattern="yyyy/MM/dd" value="${payrollRow.startDate}" var="payrollStartDate"/>
			                                                <td>${payrollStartDate}</td>
			                                                <fmt:formatDate pattern="yyyy/MM/dd" value="${payrollRow.endDate}" var="payrollEndDate"/>
			                                                <td>${payrollEndDate}</td>
			                                                <td>${payrollRow.creator.staff.getFullName()}</td>
			                                                
			                                                <td>
			                                                <c:set value="${payrollRow.status}" var="payrollStatus"></c:set>
			                                                <c:set value="${payrollStatus.css()}" var="css"></c:set>
															<span class="label ${css}">${payrollStatus}</span>
			                                                </td>
			                                                <td style="text-align: right">${payrollRow.payrollComputationResult.overallTotalOfStaff}</td>
			                                                <fmt:formatDate pattern="yyyy/MM/dd hh:mm:ss a" value="${payrollRow.lastComputed}" var="lastComputed"/>
			                                                <td>${lastComputed}</td>
			                                            </tr>
		                                            	</c:forEach>
				                                    </tbody>
				                                </table>
			                                </div><!-- /.box-body -->
               							</div>
               						</div>
               						</div>
                                </div><!-- /.tab-pane -->
                                </sec:authorize>
                                
                                <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'INVENTORY_VIEW')">
                                <div class="tab-pane" id="tab_inventory">
                                
                                	<div class="nav-tabs-custom">
		                            <ul class="nav nav-tabs" id="subtabs-inventory">
                                		<li class="active"><a href="#subtab_delivery" data-toggle="tab">Deliveries</a></li>
		                                <li><a href="#subtab_inventory" data-toggle="tab">Materials</a></li>
		                                <li><a href="#subtab_pullout" data-toggle="tab">Pull-Outs</a></li>
		                            </ul>
		                            <div class="tab-content">
		                                <div class="tab-pane" id="subtab_inventory">
	                                	<div class="row">
			                            <div class="col-md-12">
	               							<div class="box box-body box-default">
	               								<div class="box-body box-default">
				                                    <table id="material-table" class="table table-bordered table-striped">
				                                    	<thead>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                            	<th>Delivery</th>
				                                                <th>Specific Name</th>
				                                                <th>Unit</th>
				                                                <th>Status</th>
				                                                <th>Available</th>
				                                                <th>Pulled-Out</th>
				                                            	<th>Quantity</th>
				                                                <th>(&#8369;)/Unit</th>
				                                                <th>Total (&#8369;)</th>
				                                            </tr>
		                                        		</thead>
				                                        <tbody>
					                                		<c:forEach items="${materialList}" var="row">
				                                            <tr>
				                                            	<td>
				                                            		<center>
				                                            			<c:url var="urlEdit" value="/project/edit/material/${row.getKey()}-end"/>
				                                            			<a href="${urlEdit}">
								                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
				                                            			</a>
				                                            			
				                                            			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'INVENTORY_CREATE')">
				                                            			<c:if test="${row.available > 0}">
				                                            			<c:url var="urlPullout" value="/project/pullout/material/${row.getKey()}-end"/>
									                                    <a href="${urlPullout}">
		                   													<button class="btn btn-cebedo-pullout btn-flat btn-sm">Pull-Out</button>
									                                    </a>
									                                    </c:if>
									                                    </sec:authorize>
									                                    
									                                    <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'INVENTORY_DELETE')">
									                                    <div class="btn-group">
									                                    <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
									                                    <ul class="dropdown-menu">
									                                    	<li>
									                                    		<c:url var="urlDelete" value="/project/delete/material/${row.getKey()}-end"/>
									                                    		<a href="${urlDelete}" class="cebedo-dropdown-hover">
									                                        		Confirm Delete
									                                        	</a>
									                                    	</li>
									                                    </ul>
									                                    </div>
									                                    </sec:authorize>
									                                    
																	</center>
																</td>
																<td>
																<c:url var="urlLink" value="/project/edit/delivery/${row.delivery.getKey()}-end"/>
							                                    <a href="${urlLink}" class="general-link">
																${row.delivery.name}
							                                    </a>
																</td>

																<td>
																	<c:url var="urlLink" value="/project/edit/material/${row.getKey()}-end"/>
								                                    <a href="${urlLink}" class="general-link">
																	${row.name}
								                                    </a>
							                                	</td>

																<td>${row.getUnitName()}</td>
																
																
																<td align="center">
																<div class="progress">
																	<div class="progress-bar progress-bar-${row.getAvailableCSS()} progress-bar-striped" 
																	    role="progressbar" 
																	    aria-valuenow="${row.available}" 
																	    aria-valuemin="0" 
																	    aria-valuemax="${row.quantity}"
																	    style="width:${row.getAvailableAsPercentage()}">
																	    <c:if test="${row.available <= 0}">
																	    	Out of Stock
																	    </c:if>
																    </div>
																</div>
															    <c:if test="${row.available > 0}">
															    	<fmt:formatNumber type="number" pattern="###,##0.0###" value="${row.available}" />
															      	(${row.getAvailableAsPercentage()})
															    </c:if>
																</td>
																<td style="text-align: right">${row.available}</td>
																
																<td align="right">
																	<fmt:formatNumber type="number" pattern="###,##0.0###" value="${row.used}" />
																</td>
																
																<td align="right">
																	<fmt:formatNumber type="number" pattern="###,##0.0###" value="${row.quantity}" />
																</td>
																<td align="right">${row.costPerUnitMaterial}</td>
																<td align="right">${row.totalCostPerUnitMaterial}</td>
				                                            </tr>
			                                            	</c:forEach>
					                                    </tbody>
					                                </table>
				                                </div><!-- /.box-body -->
	               							</div>
	               						</div>
	               						</div>
		                                </div>
		                                
		                                
		                                <div class="tab-pane active" id="subtab_delivery">

	               						<div class="row">
	               						<div class="col-md-6">
	               							<div class="box box-body box-default">
	               								<div class="box-body">
	               									<div id="highcharts-inventory" style="height: 300px"></div>
	               								</div>
	               							</div>
	               						</div>
	               						<div class="col-md-6">
	               							<div class="box box-body box-default">
	               								<div class="box-body">
	               									<div id="highcharts-inventory-cumulative" style="height: 300px"></div>
	               								</div>
	               							</div>
	               						</div>
	               						</div>

		                                <div class="row">
			                            <div class="col-md-12">
	               							<div class="box box-body box-default">
	               								<div class="box-body">	               									

									                <div class="pull-right">
			                                  		<h3>Grand Total <b><u>
				                                	${projectAux.getGrandTotalDeliveryAsString()}
													</u></b></h3>
													</div>
													
													<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'INVENTORY_CREATE')">
											  	  	<c:url var="urlCreateDelivery" value="/project/edit/delivery/0-end"/>
			                                  		<a href="${urlCreateDelivery}">
			                                    		<button class="btn btn-cebedo-create btn-flat btn-sm">Create Delivery</button>
			                                  		</a>
			                                  		</sec:authorize>
			                                  		
			                                  		<c:if test="${!empty deliveryList}">
	                   									<a href="<c:url value="/project/export-xls/inventory"/>">
			                                        		<button class="btn btn-cebedo-export btn-flat btn-sm">Export All</button>
			                                        	</a>
		                                        	</c:if>
			                                  		<br/>
			                                  		<br/>
				                                    <table id="delivery-table" class="table table-bordered table-striped">
				                                    	<thead>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                                <th>Date and Time</th>
				                                                <th>Delivery</th>
				                                                <th>Description</th>
				                                                <th>Materials Cost (&#8369;)</th>
				                                            </tr>
		                                        		</thead>
				                                        <tbody>
					                                		<c:forEach items="${deliveryList}" var="deliveryRow">
															<fmt:formatDate pattern="yyyy/MM/dd hh:mm a" value="${deliveryRow.datetime}" var="deliveryDateTime"/>
				                                            <tr>
				                                            	<td>
				                                            		<center>
				                                            			<c:url var="urlEditDelivery" value="/project/edit/delivery/${deliveryRow.getKey()}-end"/>
				                                            			<a href="${urlEditDelivery}">
								                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
				                                            			</a>
				                                            			
				                                            			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'INVENTORY_DELETE')">
									                                    <div class="btn-group">
									                                    <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
									                                    <ul class="dropdown-menu">
									                                    	<li>
									                                    		<c:url value="/project/delete/delivery/${deliveryRow.getKey()}-end" var="urlDeleteDelivery"/>
									                                    		<a href="${urlDeleteDelivery}" class="cebedo-dropdown-hover">
									                                        		Confirm Delete
									                                        	</a>
									                                    	</li>
									                                    </ul>
									                                    </div>
									                                    </sec:authorize>
									                                    
																	</center>
																</td>
																<td>${deliveryDateTime}</td>
																<td>${deliveryRow.name}</td>
																<td>${deliveryRow.description}</td>
																<td align="right">${deliveryRow.grandTotalOfMaterials}</td>
				                                            </tr>
			                                            	</c:forEach>
					                                    </tbody>
					                                </table>
				                                </div><!-- /.box-body -->
	               							</div>
	               						</div>
	               						</div>


		                                </div>
		                                
		                                
		                                <div class="tab-pane ${pulloutsVisibility}" id="subtab_pullout">
		                                <div class="row">
	               						<div class="col-md-12">
	               							<div class="box box-body box-default">
	               								<div class="box-body">
				                                    <table id="pull-out-table" class="table table-bordered table-striped">
				                                    	<thead>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                                <th>Date and Time</th>
				                                            	<th>Staff</th>
				                                                <th>Delivery</th>
																<th>Specific Name</th>
				                                                <th>Unit</th>
				                                                <th>Quantity</th>
				                                            </tr>
		                                        		</thead>
				                                        <tbody>
					                                		<c:forEach items="${pullOutList}" var="row">
				                                            <tr>
				                                            	<td>
				                                            		<center>
				                                            			<c:url var="urlEdit" value="/project/edit/pullout/${row.getKey()}-end"/>
				                                            			<a href="${urlEdit}">
								                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
				                                            			</a>
				                                            			
				                                            			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'INVENTORY_DELETE')">
									                                    <div class="btn-group">
									                                    <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
									                                    <ul class="dropdown-menu">
									                                    	<li>
									                                    		<c:url value="/project/delete/pullout/${row.getKey()}-end" var="urlDelete"/>
									                                    		<a href="${urlDelete}" class="cebedo-dropdown-hover">
									                                        		Confirm Delete
									                                        	</a>
									                                    	</li>
									                                    </ul>
									                                    </div>
									                                    </sec:authorize>
																	</center>
																</td>
																<fmt:formatDate pattern="yyyy/MM/dd hh:mm a" value="${row.datetime}" var="rowDatetime"/>
																<td>${rowDatetime}</td>
																
																<sec:authorize access="!hasAnyRole('ADMIN_COMPANY', 'STAFF_VIEW')">
																<td>${row.staff.getFullName()}</td>
																</sec:authorize>
																
																<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_VIEW')">
																<td>
																<c:url var="urlLink" value="/project/edit/staff/${row.staff.id}"/>
							                                    <a href="${urlLink}" class="general-link">
																${row.staff.getFullName()}
							                                    </a>
																</td>
																</sec:authorize>

																<td>
																<c:url var="urlLink" value="/project/edit/delivery/${row.delivery.getKey()}-end"/>
							                                    <a href="${urlLink}" class="general-link">
																${row.delivery.name}
							                                    </a>
																</td>
																
																
																<td>
																<c:url var="urlLink" value="/project/edit/material/${row.material.getKey()}-end"/>
							                                    <a href="${urlLink}" class="general-link">
																${row.material.name}
							                                    </a>
																</td>
																
																<td>${row.material.getUnitName()}</td>
																
																<td align="right">
																	<fmt:formatNumber type="number" pattern="###,##0.0###" value="${row.quantity}" />
																</td>
																
				                                            </tr>
			                                            	</c:forEach>
					                                    </tbody>
					                                </table>
				                                </div><!-- /.box-body -->
	               							</div>
	               						</div>
	               						</div>
		                                </div>
		                            </div>
		                            </div>
                                </div><!-- /.tab-pane -->
                                </sec:authorize>
                                
                                <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_VIEW')">
                                <div class="tab-pane" id="tab_staff">
                                	<div class="nav-tabs-custom">
									<ul class="nav nav-tabs" id="subtabs-staff">
										<li class="active"><a href="#subtab_members" data-toggle="tab">Members</a></li>
										<li><a href="#subtab_controls" data-toggle="tab">Assign</a></li>
									</ul>
									<div class="tab-content">
										<div class="tab-pane active" id="subtab_members">
											<div class="row">
		                   						<div class="col-md-12">
				                                	<div class="box box-body box-default">
<!-- 				                                		<div class="box-header"> -->
<!-- 			              									<h3 class="box-title">Staff Members</h3> -->
<!-- 			              								</div> -->
						                                <div class="box-body">
											                <c:if test="${!empty project.assignedStaff}">
																
																<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_UPDATE')">
						                                  		<a href="<c:url value="/project/add/attendance/all"/>">
					                                        		<button class="btn btn-cebedo-export btn-flat btn-sm">Create Attendance</button>
					                                        	</a>
					                                        	</sec:authorize>
					                                        	
						                                  		<a href="<c:url value="/project/export-xls/staff-members"/>">
					                                        		<button class="btn btn-cebedo-export btn-flat btn-sm">Export All</button>
					                                        	</a>
					                                        	
					                                        	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_UPDATE')">
							                                    <div class="btn-group">
							                                    <button type="button" class="btn btn-cebedo-unassign-all btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Unassign All</button>
							                                    <ul class="dropdown-menu">
							                                    	<li>
				              											<c:url value="/project/unassign/staff-member/all" var="urlUnassignStaffAll"/>
									                                    <a href="${urlUnassignStaffAll}" class="cebedo-dropdown-hover">
							                                        		Confirm Unassign All
							                                        	</a>
							                                    	</li>
							                                    </ul>
							                                    </div>
							                                    </sec:authorize>

																<br/>
																<br/>
				                                    		</c:if>

						                                    <table id="assigned-staff-table" class="table table-bordered table-striped">
						                                    	<thead>
						                                            <tr>
						                                            	<th>&nbsp;</th>
						                                                <th>Full Name</th>
						                                                <th>Company Position</th>
						                                                <th>Salary (&#8369; Daily)</th>
						                                                <th>E-Mail</th>
						                                                <th>Contact Number</th>
						                                            </tr>
				                                        		</thead>
						                                        <tbody>
							                                        <c:set var="assignedStaff" value="${project.assignedStaff}"/>
								                                	<c:if test="${!empty assignedStaff}">
								                                		<c:forEach items="${assignedStaff}" var="assignedStaffMember">
							                                            <tr>
							                                            	<td>
							                                            		<center>
							                                            			<c:url var="urlViewStaff" value="/project/edit/staff/${assignedStaffMember.id}"/>
							                                            			<a href="${urlViewStaff}">
											                                    	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
							                                            			</a>
							                                            			
									                                            	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_UPDATE')">
												                                    <div class="btn-group">
												                                    <button type="button" class="btn btn-cebedo-unassign btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Unassign</button>
												                                    <ul class="dropdown-menu">
												                                    	<li>
												                                    		<c:url value="/project/unassign/staff-member/${assignedStaffMember.id}" var="urlUnassignStaff"/>
												                                    		<a href="${urlUnassignStaff}" class="cebedo-dropdown-hover">
												                                        		Confirm Unassign
												                                        	</a>
												                                    	</li>
												                                    </ul>
												                                    </div>
														                            </sec:authorize>
																				</center>
																			</td>
						                                                	<td>${assignedStaffMember.getFullName()} (${assignedStaffMember.getUsername()})</td>
						                                                	<td>${assignedStaffMember.companyPosition}</td>
						                                                	<td style="text-align: right;">${assignedStaffMember.wage}</td>
						                                                	<td>${assignedStaffMember.email}</td>
						                                                	<td>${assignedStaffMember.getContactNumberAsString()}</td>
							                                            </tr>
						                                            </c:forEach>
					                                        		</c:if>
							                                    </tbody>
							                                </table>
						                                </div><!-- /.box-body -->
						                             </div>
						                        </div>
						                   	</div>
										</div>
										<div class="tab-pane" id="subtab_controls">
						                   	<div class="row">
		                   						<div class="col-md-12">
				                                	<div class="box box-body box-default">
<!-- 			              								<div class="box-header"> -->
<!-- 			              									<h3 class="box-title">Staff Assignment Controls</h3> -->
<!-- 			              								</div> -->
						                                <div class="box-body">
						                                
						                                	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_CREATE')">
				                                    		<form:form modelAttribute="massUploadBean"
																action="${contextPath}/project/mass/upload-and-assign/staff"
																method="post"
																enctype="multipart/form-data">
															
																<div class="form-group">
																<label>Excel File</label>
																<form:input type="file" class="form-control" path="file"/><br/>
																<button class="btn btn-cebedo-create btn-flat btn-sm">Upload and Assign</button>
																</div>
															</form:form>
															</sec:authorize>
															
															<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_CREATE')">
			                                    			<a href="<c:url value="/project/edit/staff/0"/>">
					                                    	<button class="btn btn-cebedo-create btn-flat btn-sm">Create Staff</button>
			                                    			</a>
			                                    			</sec:authorize>
				                                    		
				                                    		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_UPDATE')">
				                                    		<c:if test="${!empty availableStaffToAssign}">
				                                    		<button onclick="submitForm('assignStaffForm')" class="btn btn-cebedo-assign btn-flat btn-sm">Assign</button>
				                                    		</c:if>
				                                    		</sec:authorize>

				                                    		<c:if test="${!empty availableStaffToAssign}">
				                                    		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_UPDATE')">
				                                    		&nbsp;&nbsp;
				                                    		<a href="#" onclick="checkAll('include-checkbox')" class="general-link">Check All</a>&nbsp;
															<a href="#" onclick="uncheckAll('include-checkbox')" class="general-link">Uncheck All</a>
							                                </sec:authorize>
							                                
							                                <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_CREATE', 'STAFF_UPDATE')">
							                                <br/>
							                                <br/>
							                                </sec:authorize>

					                                    	<form:form modelAttribute="project" 
							                                    method="post" 
							                                    id="assignStaffForm"
							                                    action="${contextPath}/project/assign/staff/mass">
				                                    		<div class="form-group">
				                                    		<table id="staff-assign-table" class="table table-bordered table-striped">
				                                    			<thead>
				                                    			<tr>
				                                    			
				                                    			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_UPDATE')">
				                                    			<th>Check/Uncheck</th>
				                                    			</sec:authorize>
				                                    			
				                                                <th>Full Name</th>
				                                                <th>Company Position</th>
				                                                <th>Salary (&#8369; Daily)</th>
				                                                <th>E-Mail</th>
				                                                <th>Contact Number</th>
				                                    			</tr>
				                                    			</thead>

				                                    			<tbody>
					                                    			<c:forEach items="${availableStaffToAssign}" var="staff">
						                                    			<tr>
						                                    			
						                                    			<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'STAFF_UPDATE')">
						                                    			<td align="center">
							                                    			<form:checkbox class="form-control include-checkbox" path="staffIDs" value="${staff.id}"/><br/>
						                                    			</td>
						                                    			</sec:authorize>
						                                    			
						                                    			<td>${staff.getFullName()}</td>
						                                    			<td>${staff.companyPosition}</td>
					                                                	<td class="cebedo-text-align-right">${staff.wage}</td>
					                                                	<td>${staff.email}</td>
					                                                	<td>${staff.getContactNumberAsString()}</td>
						                                    			</tr>
						                                    		</c:forEach>
				                                    			</tbody>
				                                    		</table>
				                                    		</div>
						                                    </form:form>

				                                    		</c:if>

						                                </div>
						                             </div>
						                        </div>
						                   	</div>
										</div>
									</div>
									</div>	
				                </div>
				                </sec:authorize>
				                
                                </c:when>
                                </c:choose>
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
	
<%-- 	<script src="<c:url value="/resources/lib/highcharts/js/highcharts.js" />"type="text/javascript"></script> --%>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/highcharts/4.1.9.1/highcharts.js" type="text/javascript"></script>

<%-- 	<script src="<c:url value="/resources/lib/highcharts/js/themes/grid-light.js" />"type="text/javascript"></script> --%>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/highcharts/4.1.9.1/themes/grid-light.js" type="text/javascript"></script>

<%-- 	<script src="<c:url value="/resources/lib/highcharts/js/modules/no-data-to-display.js" />"type="text/javascript"></script> --%>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/highcharts/4.1.9.1/modules/no-data-to-display.js" type="text/javascript"></script>
	
<%-- 	<script src="<c:url value="/resources/lib/highcharts/js/modules/exporting.js" />"type="text/javascript"></script> --%>
<%-- 	<script src="<c:url value="/resources/lib/highcharts/js/highcharts-3d.js" />"type="text/javascript"></script> --%>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/highcharts/4.1.9.1/highcharts-3d.js" type="text/javascript"></script>
	
	<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'PROGRAM_OF_WORKS_VIEW')">
	<c:if test="${project.id != 0 && !empty project.assignedTasks}">
<%--    	<script src="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.js" />"type="text/javascript"></script> --%>
	<script src="https://cdn.rawgit.com/VicCebedo/PracticeRepo/development/mod-webapp-codebase/src/main/webapp/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.js" type="text/javascript"></script>

<%--     <script src="${contextPath}/resources/lib/dhtmlxGantt_v3.1.1_gpl/ext/dhtmlxgantt_tooltip.js" type="text/javascript"></script> --%>
	<script src="https://cdn.rawgit.com/VicCebedo/PracticeRepo/development/mod-webapp-codebase/src/main/webapp/resources/lib/dhtmlxGantt_v3.1.1_gpl/ext/dhtmlxgantt_tooltip.js" type="text/javascript"></script>
    
<%-- 	<script src="<c:url value="/resources/js/gantt-custom.js" />"type="text/javascript"></script> --%>
	<script src="https://cdn.rawgit.com/VicCebedo/PracticeRepo/development/mod-webapp-codebase/src/main/webapp/resources/js/gantt-custom.js" type="text/javascript"></script>
	
	<script type="text/javascript">
	    $(document).ready(function() {
	    	var ganttJSON = ${ganttJSON};
		    var tasks = {'data': ganttJSON};
			gantt.init("gantt-chart");
		    gantt.parse(tasks);
		    gantt.sort("start_date");
		});
	</script>
   	</c:if>
   	</sec:authorize>
   	
   	
   	<c:if test="${project.id != 0}">
   	<script type="text/javascript">
//    	$(document).ready(function() {
// 		var eventsJSON = ${calendarJSON};
// 		$('#calendar').fullCalendar({
// 			events: eventsJSON,
// 			dayClick: function(date, jsEvent, view) {
// // 				$("#modalDate").val(date.format());
// // 				$("#modalWage").val(staffWage);
// // 				$("#myModal").modal('show');
// 		    },
// 		    eventClick: function(calEvent, jsEvent, view) {
// // 		    	$("#modalDate").val(calEvent.start.format());
// // 				$("#modalWage").val(staffWage);
// // 				$("#myModal").modal('show');
				
// // 				var statusValue = calEvent.attendanceStatus;
// // 				$('#attendanceStatus').val(statusValue);
				
// // 				if(statusValue == 2 || this.value == -1) {
// // 					$('#modalWage').hide();
// // 					$('#modalWageLabel').hide();
// // 					$('#modalWageBreak').hide();
// // 				} else {
// // 					$('#modalWage').val(calEvent.attendanceWage);
// // 					$('#modalWage').show();
// // 					$('#modalWageLabel').show();
// // 					$('#modalWageBreak').show();
// // 				}
// 		    }
// 	    });
// 	});

	$(function () {
	    $('#highcharts-inventory').highcharts({
	        chart: {
	            type: 'column',
            	zoomType: 'x'
	        },
	        credits: {
	        	enabled: false
	        },
	        title: {
	            text: 'Comparison of Inventory Expenses'
	        },
	        xAxis: {
	            type: 'datetime',
	            dateTimeLabelFormats: {
					millisecond: '%e. %b',
					second: '%e. %b',
					minute: '%e. %b',
					hour: '%e. %b',
					day: '%e. %b',
					week: '%e. %b',
					month: '%b \'%y',
					year: '%Y'
				},
				title: {
	                text: 'Date'
	            }
	        },
	        yAxis: {
	            title: {
	                text: 'Inventory Expenses (PHP)'
	            },
	            min: 0
	        },
	        tooltip: {
	            pointFormat: '<b>{point.y}</b>'
	        },
	        series: [{
	        	name: 'Inventory Expenses',
	            data: ${dataSeriesInventory}
	        }]
	    });
	});

	$(function () {
	    $('#highcharts-project').highcharts({
	        chart: {
	            type: 'area',
            	zoomType: 'x'
	        },	        
	    	xAxis: {
	            type: 'datetime',
	            dateTimeLabelFormats: {
					millisecond: '%e. %b',
					second: '%e. %b',
					minute: '%e. %b',
					hour: '%e. %b',
					day: '%e. %b',
					week: '%e. %b',
					month: '%b \'%y',
					year: '%Y'
				},
				title: {
	                enabled: false
	            }
	        },
	        credits: {
	        	enabled: false
	        },
	        title: {
	            text: 'Accumulation of All Project Expenses'
	        },
	        yAxis: {
	            title: {
	                text: 'Project Expenses (PHP)'
	            }
	        },
	        tooltip: {
	        	shared: true,
	            pointFormat: '<b>{point.y}</b>'
	        },
	        plotOptions: {
	            area: {
	                lineColor: '#666666',
	                lineWidth: 1,
	                marker: {
	                    lineWidth: 1,
	                    lineColor: '#666666'
	                }
	            }
	        },
	        series: [{
	        	name: 'Project Cumulative',
	            data: ${dataSeriesProject}
	        }]
	    });
	});

	$(function () {
	    $('#highcharts-costs-actual-pie').highcharts({
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
	            data: ${dataSeriesCostsActual}
	        }]
	    });
	});

	$(function () {
	    $('#highcharts-costs-estimated-pie').highcharts({
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
	            data: ${dataSeriesCostsEstimated}
	        }]
	    });
	});

	$(function () {
	    $('#highcharts-dashboard-project-pie').highcharts({
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
	            text: 'Proportion Per Type of Expenditure'
	        },
	        tooltip: {
	            pointFormat: '<b>{point.y} ({point.percentage:.2f}%)</b>'
	        },
	        plotOptions: {
	            pie: {
	                
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
	            data: ${dataSeriesDashboardPie}
	        }]
	    });
	});

	$(function () {
	    $('#highcharts-dashboard-not-cumulative').highcharts({
	        chart: {
	            type: 'column',
            	zoomType: 'x'
	        },
	        credits: {
	        	enabled: false
	        },
	        title: {
	            text: 'Comparison of Expenses per Type'
	        },
	        xAxis: {
	            type: 'datetime',
	            dateTimeLabelFormats: {
					millisecond: '%e. %b',
					second: '%e. %b',
					minute: '%e. %b',
					hour: '%e. %b',
					day: '%e. %b',
					week: '%e. %b',
					month: '%b \'%y',
					year: '%Y'
				},
				title: {
	                text: 'Date'
	            }
	        },
	        yAxis: {
	            title: {
	                text: 'Project Expenses (PHP)'
	            },
	            min: 0
	        },
	        tooltip: {
	            pointFormat: '<b>{point.y}</b>'
	        },
	        plotOptions: {
	            spline: {
	                marker: {
	                    enabled: true,
	                    radius: 8
	                }
	            }
	        },
	        series: ${dataSeriesDashboardNotCumulative}
	    });
	});

	$(function () {
	    $('#highcharts-dashboard').highcharts({
	        chart: {
	            type: 'area',
            	zoomType: 'x'
	        },
	        credits: {
	        	enabled: false
	        },
	        title: {
	            text: 'Accumulation Per Type of Expenditure'
	        },
	        xAxis: {
	            type: 'datetime',
	            dateTimeLabelFormats: {
					millisecond: '%e. %b',
					second: '%e. %b',
					minute: '%e. %b',
					hour: '%e. %b',
					day: '%e. %b',
					week: '%e. %b',
					month: '%b \'%y',
					year: '%Y'
				},
				title: {
	                text: 'Date'
	            }
	        },
	        yAxis: {
	            title: {
	                text: 'Project Expenses (PHP)'
	            },
	            min: 0
	        },
	        tooltip: {
	            pointFormat: '<b>{point.y}</b>'
	        },
	        plotOptions: {
	            spline: {
	                marker: {
	                    enabled: true,
	                    radius: 8
	                }
	            }
	        },
	        series: ${dataSeriesDashboard}
	    });
	});

	$(function () {
	    $('#highcharts-equipment-expenses-cumulative').highcharts({
	        chart: {
	            type: 'area',
            	zoomType: 'x'
	        },
	        credits: {
	        	enabled: false
	        },
	        title: {
	            text: 'Accumulation of Equipment Expenses'
	        },
	        xAxis: {
	            type: 'datetime',
	            dateTimeLabelFormats: {
					millisecond: '%e. %b',
					second: '%e. %b',
					minute: '%e. %b',
					hour: '%e. %b',
					day: '%e. %b',
					week: '%e. %b',
					month: '%b \'%y',
					year: '%Y'
				},
				title: {
	                text: 'Date'
	            }
	        },
	        yAxis: {
	            title: {
	                text: 'Equipment Expenses (PHP)'
	            },
	            min: 0
	        },
	        tooltip: {
	            pointFormat: '<b>{point.y}</b>'
	        },
	        plotOptions: {
	            spline: {
	                marker: {
	                    enabled: true,
	                    radius: 8
	                }
	            }
	        },
	        series: [{
	        	name: 'Equipment Expenses Cumulative',
	            data: ${dataSeriesEquipmentCumulative}
	        }]
	    });
	});

	$(function () {
	    $('#highcharts-other-expenses-cumulative').highcharts({
	        chart: {
	            type: 'area',
            	zoomType: 'x'
	        },
	        credits: {
	        	enabled: false
	        },
	        title: {
	            text: 'Accumulation of Other Expenses'
	        },
	        xAxis: {
	            type: 'datetime',
	            dateTimeLabelFormats: {
					millisecond: '%e. %b',
					second: '%e. %b',
					minute: '%e. %b',
					hour: '%e. %b',
					day: '%e. %b',
					week: '%e. %b',
					month: '%b \'%y',
					year: '%Y'
				},
				title: {
	                text: 'Date'
	            }
	        },
	        yAxis: {
	            title: {
	                text: 'Other Expenses (PHP)'
	            },
	            min: 0
	        },
	        tooltip: {
	            pointFormat: '<b>{point.y}</b>'
	        },
	        plotOptions: {
	            spline: {
	                marker: {
	                    enabled: true,
	                    radius: 8
	                }
	            }
	        },
	        series: [{
	        	name: 'Other Expenses Cumulative',
	            data: ${dataSeriesOtherExpensesCumulative}
	        }]
	    });
	});

	$(function () {
	    $('#highcharts-inventory-cumulative').highcharts({
	        chart: {
	            type: 'area',
            	zoomType: 'x'
	        },
	        credits: {
	        	enabled: false
	        },
	        title: {
	            text: 'Accumulation of Inventory Expenses'
	        },
	        xAxis: {
	            type: 'datetime',
	            dateTimeLabelFormats: {
					millisecond: '%e. %b',
					second: '%e. %b',
					minute: '%e. %b',
					hour: '%e. %b',
					day: '%e. %b',
					week: '%e. %b',
					month: '%b \'%y',
					year: '%Y'
				},
				title: {
	                text: 'Date'
	            }
	        },
	        yAxis: {
	            title: {
	                text: 'Inventory Expenses (PHP)'
	            },
	            min: 0
	        },
	        tooltip: {
	            pointFormat: '<b>{point.y}</b>'
	        },
	        plotOptions: {
	            spline: {
	                marker: {
	                    enabled: true,
	                    radius: 8
	                }
	            }
	        },
	        series: [{
	        	name: 'Inventory Cumulative',
	            data: ${dataSeriesInventoryCumulative}
	        }]
	    });
	});

	$(function () {
	    $('#highcharts-payroll-cumulative').highcharts({
	        chart: {
	            type: 'area',
            	zoomType: 'x'
	        },
	        credits: {
	        	enabled: false
	        },
	        title: {
	            text: 'Accumulation of Payroll Expenses'
	        },
	        xAxis: {
	            type: 'datetime',
	            dateTimeLabelFormats: {
					millisecond: '%e. %b',
					second: '%e. %b',
					minute: '%e. %b',
					hour: '%e. %b',
					day: '%e. %b',
					week: '%e. %b',
					month: '%b \'%y',
					year: '%Y'
				},
				title: {
	                text: 'Date'
	            }
	        },
	        yAxis: {
	            title: {
	                text: 'Payroll Expenses (PHP)'
	            },
	            min: 0
	        },
	        tooltip: {
	            pointFormat: '<b>{point.y}</b>'
	        },
	        plotOptions: {
	            spline: {
	                marker: {
	                    enabled: true,
	                    radius: 8
	                }
	            }
	        },
	        series: [{
	        	name: 'Payroll Cumulative',
	            data: ${dataSeriesPayrollCumulative}
	        }]
	    });
	});

	$(function () {
	    $('#highcharts-other-expenses').highcharts({
	        chart: {
	            type: 'column',
            	zoomType: 'x'
	        },
	        credits: {
	        	enabled: false
	        },
	        title: {
	            text: 'Comparison of Other Expenses'
	        },
	        xAxis: {
	            type: 'datetime',
	            dateTimeLabelFormats: {
					millisecond: '%e. %b',
					second: '%e. %b',
					minute: '%e. %b',
					hour: '%e. %b',
					day: '%e. %b',
					week: '%e. %b',
					month: '%b \'%y',
					year: '%Y'
				},
				title: {
	                text: 'Date'
	            }
	        },
	        yAxis: {
	            title: {
	                text: 'Other Expenses (PHP)'
	            },
	            min: 0
	        },
	        tooltip: {
	            pointFormat: '<b>{point.y}</b>'
	        },
	        plotOptions: {
	            spline: {
	                marker: {
	                    enabled: true,
	                    radius: 8
	                }
	            }
	        },
	        series: [{
	        	name: 'Other Expenses',
	            data: ${dataSeriesOtherExpenses}
	        }]
	    });
	});

	$(function () {
	    $('#highcharts-equipment-expenses').highcharts({
	        chart: {
	            type: 'column',
            	zoomType: 'x'
	        },
	        credits: {
	        	enabled: false
	        },
	        title: {
	            text: 'Comparison of Equipment Expenses'
	        },
	        xAxis: {
	            type: 'datetime',
	            dateTimeLabelFormats: {
					millisecond: '%e. %b',
					second: '%e. %b',
					minute: '%e. %b',
					hour: '%e. %b',
					day: '%e. %b',
					week: '%e. %b',
					month: '%b \'%y',
					year: '%Y'
				},
				title: {
	                text: 'Date'
	            }
	        },
	        yAxis: {
	            title: {
	                text: 'Equipment Expenses (PHP)'
	            },
	            min: 0
	        },
	        tooltip: {
	            pointFormat: '<b>{point.y}</b>'
	        },
	        plotOptions: {
	            spline: {
	                marker: {
	                    enabled: true,
	                    radius: 8
	                }
	            }
	        },
	        series: [{
	        	name: 'Equipment Expenses',
	            data: ${dataSeriesEquipment}
	        }]
	    });
	});

	$(function () {
	    $('#highcharts-payroll').highcharts({
	        chart: {
	            type: 'column',
            	zoomType: 'x'
	        },
	        credits: {
	        	enabled: false
	        },
	        title: {
	            text: 'Comparison of Payroll Expenses'
	        },
	        xAxis: {
	            type: 'datetime',
	            dateTimeLabelFormats: {
					millisecond: '%e. %b',
					second: '%e. %b',
					minute: '%e. %b',
					hour: '%e. %b',
					day: '%e. %b',
					week: '%e. %b',
					month: '%b \'%y',
					year: '%Y'
				},
				title: {
	                text: 'Date'
	            }
	        },
	        yAxis: {
	            title: {
	                text: 'Payroll Expenses (PHP)'
	            },
	            min: 0
	        },
	        tooltip: {
	            pointFormat: '<b>{point.y}</b>'
	        },
	        plotOptions: {
	            spline: {
	                marker: {
	                    enabled: true,
	                    radius: 8
	                }
	            }
	        },
	        series: [{
	        	name: 'Payroll Expenses',
	            data: ${dataSeriesPayroll}
	        }]
	    });
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
	            text: 'Proportional Comparison per Task Status'
	        },
	        tooltip: {
	            pointFormat: '<b>{point.y} ({point.percentage:.2f}%)</b>'
	        },
	        plotOptions: {
	            pie: {
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
   	</script>
	</c:if>
	
	<script src="https://cdn.datatables.net/plug-ins/1.10.10/api/fnLengthChange.js" type="text/javascript"></script>
	<script type="text/javascript">
	    
	    // TODO Not sure if we still need the below code.
		$(document).ready(function() {
		    var estimationTable = $('#concrete-estimation-summary-table').DataTable();
		    var concreteBreakdownTable = $("#concrete-table").DataTable();
		 
		 	// TODO Hide/show data table columns.
		    $('a.toggle-vis').on( 'click', function (e) {
		        e.preventDefault();
		 
		        // Get the column API object.
		        // Loop through each ID in data-column attr.
		        // Hide each.
		        var dataColIDs = $(this).attr('data-column');
		        var colsToHide = dataColIDs.split(",");
		        var dataTable = $(this).attr('data-table');
		        var targetTable;

		        if(dataTable == "concrete-estimation-summary-table"){
		        	targetTable = estimationTable;
		        } else if(dataTable == "concrete-table"){
		        	targetTable = concreteBreakdownTable;
		        }
		        
		        for(var i = 0; i < colsToHide.length; i++){
			        var column = targetTable.column(colsToHide[i]);
			 
			        // Toggle the visibility
			        column.visible( ! column.visible() );
		        }
		        
		    });
		    
		    // Hide the column TOTAL QUANTITY / NO. OF UNITS.
		    var dataColIDs = "2,3,4,5";
	        var colsToHide = dataColIDs.split(",");
	        for(var i = 0; i < colsToHide.length; i++){
		        var column = estimationTable.column(colsToHide[i]);
		        column.visible( ! column.visible() );
	        }
		});
		
		// Data tables.
		$(document).ready(function() {
			$('.date-picker').datepicker({
			    format: 'yyyy/mm/dd'
			})
			
			$("#estimate-output-table").dataTable();
			var staffAssignTable = $("#staff-assign-table").dataTable();
			if(staffAssignTable.size() > 0){
				staffAssignTable.fnLengthChange(-1);
			}
			$("#other-expenses-table").DataTable({
				"order": [[ 1, "desc" ]]
		    });
			$("#equipment-table").DataTable({
				"order": [[ 1, "desc" ]]
		    });
			$("#pull-out-table").DataTable({
				"order": [[ 1, "desc" ]]
		    });
			$("#material-table").DataTable({
				"order": [[ 1, "asc" ]]
		    });
			$("#delivery-table").DataTable({
				"order": [[ 1, "desc" ]]
		    });
			$("#payroll-table").DataTable({
				"order": [[ 1, "desc" ]]
		    });
			$("#assigned-staff-table").DataTable({
				"order": [[ 1, "asc" ]]
		    });
			$("#tasks-table").DataTable({
		        "order": [[ 2, "desc" ]]
		    });
			$("#estimated-direct-costs").DataTable({
		        "order": [[ 1, "asc" ]]
		    });
			$("#estimated-indirect-costs").DataTable({
		        "order": [[ 1, "asc" ]]
		    });
			$(".is-data-table").dataTable();
	    });	    
		
		$("ul.nav-tabs > li > a").on("shown.bs.tab", function (e) {
			var id = $(e.target).attr("href").substr(1);
			
			// Code to call as a workaround over gantt bug where chart doesn't
			// render if placed in a tab.
			if((id == "tab_timeline" || id == "subtab_chart") && typeof gantt !== 'undefined'){
				gantt.render();
			}
			$(window).resize();
		    // TODO Some href links, if equal to #, scrolls to top.
		    return false;
		});
	</script>
</body>
</html>