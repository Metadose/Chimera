<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Project ${action}</title>
	
	<link href="<c:url value="/resources/css/gantt-custom.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/fullcalendar.css" />"rel="stylesheet" type="text/css" />
	
	<!-- Ignite UI Required Combined CSS Files -->
	<link href="<c:url value="/resources/lib/igniteui/infragistics.theme.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/igniteui/infragistics.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/igniteui/infragistics.ui.treegrid.css" />"rel="stylesheet" type="text/css" />
    
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
		.gantt-holder {
			width:auto;
			height:auto;
		}
		#treegrid1_container {
		  outline: none;
		  border: none !important;
		  -webkit-box-shadow: none !important;
		  -moz-box-shadow: none !important;
		  box-shadow: none !important;
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
	            		<c:when test="${project.id == 0}">
	            			New Project
	            		</c:when>
	            		<c:when test="${project.id != 0}">
	            			${fn:escapeXml(project.name)}
	            		</c:when>
	            	</c:choose>
	                <small>${action} Project <a href="${contextPath}/project/clear/cache/${project.id}">Clear Cache</a> </small>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                    	${uiParamAlert}
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs" id="myTab">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                                <c:choose>
                                	<c:when test="${project.id != 0}">
                                		<li><a href="#tab_managers" data-toggle="tab">Managers</a></li>
                                		<li><a href="#tab_teams" data-toggle="tab">Teams</a></li>
		                                <li><a href="#tab_expenses" data-toggle="tab">Expenses</a></li>
		                                <li><a href="#tab_timeline" data-toggle="tab">Timeline</a></li>
		                                <li><a href="#tab_calendar" data-toggle="tab">Calendar</a></li>
		                                <li><a href="#tab_3" data-toggle="tab">Files</a></li>
		                                <li><a href="#tab_payroll" data-toggle="tab">Payroll</a></li>
                                	</c:when>
                                </c:choose>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Details</h3>
                   								</div>
                   								<div class="box-body">
                   									<c:choose>
                                						<c:when test="${project.id != 0}">
                                							<c:choose>
		                   										<c:when test="${!empty project.thumbnailURL}">
		                   											<img src="${contextPath}/image/display/project/profile/?project_id=${project.id}"/>
		                   										</c:when>
		                   										<c:when test="${empty project.thumbnailURL}">
		                   											No photo uploaded.
		                   										</c:when>
		                   									</c:choose>
		                   									<br/>
		                   									<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
		                   									<br/>
		                   									<div class="form-group">
		                   										<form:form id="uploadPhotoForm"
	                   												modelAttribute="profilePhoto"
																	action="${contextPath}/project/upload/profile"
																	method="post"
																	enctype="multipart/form-data">
			                   										<table>
			                   											<tr>
			                   												<td>
			                   													<label for="exampleInputFile">Update Photo</label>
			                   												</td>
			                   												<td>
			                   													&nbsp;&nbsp;
			                   												</td> 
			                   												<td>
			                   													<input type="file" id="file" name="file"/>
			                   												</td>
			                   											</tr>
			                   										</table>
							                                        <button class="btn btn-default btn-flat btn-sm">Upload</button>
						                                        </form:form>
						                                        <c:if test="${!empty project.thumbnailURL}">
						                                        <c:url var="urlProjectProfileDelete" value="/project/profile/delete"/>
                                								<a href="${urlProjectProfileDelete}">
						                                        <button class="btn btn-default btn-flat btn-sm">Delete Photo</button>
          														</a>
						                                        </c:if>
						                                    </div>
						                                    </sec:authorize>
                                						</c:when>
                              						</c:choose>
				                                    <br/>
				                                    <c:if test="${project.id != 0}">
				                                    <!-- Read only Output -->
				                                    <div class="form-group" id="detailsDivViewer">
			                                            <label>Name</label><br/>
			                                            <c:out value="${project.name}"/><br/><br/>
			                                            
			                                            <label>Status</label><br/>
			                                            <c:set value="${project.getStatusEnum().css()}" var="css"></c:set>
														<span class="label ${css}">${project.getStatusEnum()}</span><br/><br/>
			                                            
			                                            <label>Location</label><br/>
			                                            <c:out value="${project.location}"/><br/><br/>
			                                            
			                                            <label>Notes</label><br/>
			                                            <c:out value="${project.notes}"/><br/><br/>
			                                            
			                                            <sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
			                                            <button class="btn btn-default btn-flat btn-sm" onclick="switchDisplay(detailsDivViewer, detailsDivEditor)">Edit</button>
			                                            </sec:authorize>
			                                        </div>
			                                        </c:if>
				                                    <sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
			                                        <div class="form-group" id="detailsDivEditor">
			                                        
			                                        	<!-- Update form Input -->
                  										<form:form id="detailsForm"
                  											modelAttribute="project"
                  											method="post"
                  											action="${contextPath}/project/create">
				                                            
				                                            <label>Name</label>
				                                            <form:input type="text" class="form-control" path="name"/><br/>
				                                            
				                                            <label>Status</label>
				                                            <form:select class="form-control" id="project_status" path="status">
						                                    	<form:option value="0" label="New"/>
						                                    	<form:option value="1" label="Ongoing"/>
						                                    	<form:option value="2" label="Completed"/>
						                                    	<form:option value="3" label="Failed"/>
						                                    	<form:option value="4" label="Cancelled"/>
				                                            </form:select><br/>
				                                            
				                                            <label>Location</label>
				                                            <form:input type="text" class="form-control" path="location"/><br/>
				                                            
				                                            <label>Notes</label>
				                                            <form:input type="text" class="form-control" path="notes"/><br/>
				                                            
				                                    	</form:form>
			                                    	<c:choose>
		                                            	<c:when test="${project.id == 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${project.id > 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<c:url var="urlProjectDelete" value="/project/delete/${project.id}"/>
                               								<a href="${urlProjectDelete}">
																<button class="btn btn-default btn-flat btn-sm">Delete This Project</button>
       														</a>
		                                            	</c:when>
		                                            </c:choose>
		                                            <br/>
		                                            <c:if test="${project.id != 0}">
		                                            <br/>
		                                            <button class="btn btn-default btn-flat btn-sm" onclick="switchDisplay(detailsDivEditor, detailsDivViewer)">Done Editing</button>
		                                            </c:if>
			                                        </div>
		                                            </sec:authorize>
                   								</div>
                   							</div>
                   						</div>
                   						<c:choose>
                   						<c:when test="${project.id != 0}">
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">More Information</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="form-group">
                   											<c:set var="projectFields" value="${project.assignedFields}"/>
               												
                   											<c:if test="${!empty projectFields}">
   															<div class="form-group" id="fieldsDivViewer">
	               												<c:forEach var="field" items="${projectFields}"  varStatus="loop">
	               													<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
	               														<c:url var="urlEditProjectField" value="/project/field/edit/${field.field.id}-${field.label}-${field.value}"/>
		                                								<a href="${urlEditProjectField}">
						                                            	<button class="btn btn-default btn-flat btn-sm">Edit</button>
	               														</a>
						                                            </sec:authorize>
               														<!-- More Information Output -->
	       															<label><c:out value="${field.label}"/></label>&nbsp;<c:out value="${field.value}"/>
	       															<br/>
																</c:forEach>
       															<br/>
																<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
																	<c:url var="urlProjectUnassignFieldAll" value="/project/unassign/field/all"/>
		                               								<a href="${urlProjectUnassignFieldAll}">
																		<button class="btn btn-default btn-flat btn-sm">Remove All</button>
		       														</a>
																</sec:authorize>
   															</div>
   															</c:if>
   															<c:if test="${empty projectFields}">
   																No extra information added.
   																<br/>
   															</c:if>
   															<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
   															<br/>
   															<h4>Add More Information</h4>
															<form:form modelAttribute="field"
																id="fieldsForm" 
																method="post" 
																action="${contextPath}/project/assign/field">
																
																<label>Label</label><br/>
																<form:input type="text" path="label" id="label" class="form-control"
																	placeholder="Example: SSS, Building Permit No., Sub-contractor, etc..."/><br/>
																
																<label>Information</label><br/>
																<form:textarea class="form-control"
																	rows="3" id="value" path="value"
																	placeholder="Example: 000-123-456, AEE-123, OneForce Construction, etc..."></form:textarea>
															
															</form:form>
															<br/>
	                                           				<button class="btn btn-default btn-flat btn-sm" onclick="submitForm('fieldsForm')">Add</button><br/>
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
                                <div class="tab-pane" id="tab_3">
                                	<div class="box box-default">
                                    <div class="box-body table-responsive">
                                    	<form:form id="uploadProjectFileForm"
											modelAttribute="projectfile"
											action="${contextPath}/project/upload/projectfile"
											method="post"
											enctype="multipart/form-data">
	         									<label for="exampleInputFile">File Upload (20MB Max)</label>
												<form:input type="file" id="file" path="file"/><br/>
												<label>Description</label>
												<form:textarea class="form-control"
													rows="3" id="description" path="description"
													placeholder="Example: Reference spreadsheet file..."></form:textarea><br/>
												<button class="btn btn-default btn-flat btn-sm" id="uploadButton">Upload</button>
                                        </form:form>
	                                    <br/>
	                                    <table id="example-1" class="table table-bordered table-striped">
	                                        <thead>
	                                            <tr>
	                                            	<th>&nbsp;</th>
	                                            	<th>#</th>
	                                                <th>Name</th>
	                                                <th>Description</th>
	                                                <th>Size</th>
	                                                <th>Uploader</th>
	                                                <th>Date Uploaded</th>
	                                            </tr>
	                                        </thead>
	                                        <tbody>
	                                        	<c:if test="${!empty project.files}">
	                                        		<c:forEach items="${project.files}" var="file">
	                                        			<c:set var="uploader" value="${file.uploader}"/>
	                                               		<c:set var="uploaderName" value="${uploader.prefix} ${uploader.firstName} ${uploader.middleName} ${uploader.lastName} ${uploader.suffix}"/>
	                                        			<tr>
			                                            	<td>
			                                            		<center>
			                                            		<c:url value="/project/download/projectfile/${file.id}" var="urlDownloadProjectfile"/>
			                                            		<a href="${urlDownloadProjectfile}">
			                                            			<button class="btn btn-default btn-flat btn-sm">Download</button>
			                                            		</a>
			                                            		<c:url value="/projectfile/edit/${file.id}/from/project/${project.id}" var="urlViewProjectfile"/>
			                                            		<a href="${urlViewProjectfile}">
			                                            			<button class="btn btn-default btn-flat btn-sm">View</button>
			                                            		</a>
			                                            		<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
			                                            		<c:url value="/project/delete/projectfile/${file.id}" var="urlDeleteProjectfile"/>
			                                            		<a href="${urlDeleteProjectfile}">
																	<button class="btn btn-default btn-flat btn-sm">Delete</button>
			                                            		</a>
																</sec:authorize>
																</center>
															</td>
			                                                <td>${file.id}</td>
			                                                <td>${file.name}</td>
			                                                <td>${file.description}</td>
			                                                <c:choose>
		                                                	<c:when test="${file.size < 1000000}">
		                                                		<c:set var="fileSize" value="${file.size / 1000}"/>
		                                                		<td><fmt:formatNumber type="number" maxIntegerDigits="3" value="${fileSize}"/> KB</td>
		                                                	</c:when>
		                                                	<c:when test="${file.size >= 1000000}">
		                                                		<c:set var="fileSize" value="${file.size / 1000000}"/>
		                                                		<td><fmt:formatNumber type="number" maxIntegerDigits="3" value="${fileSize}"/> MB</td>
		                                                	</c:when>
			                                                </c:choose>
			                                                <td>${uploaderName}</td>
			                                                <td>${file.dateUploaded}</td>
			                                            </tr>
	                                        		</c:forEach>
	                                        	</c:if>
	                                        </tbody>
	                                        <tfoot>
	                                            <tr>
	                                            	<th>&nbsp;</th>
	                                            	<th>#</th>
	                                                <th>Name</th>
	                                                <th>Description</th>
	                                                <th>Size</th>
	                                                <th>Uploader</th>
	                                                <th>Date Uploaded</th>
	                                            </tr>
	                                        </tfoot>
	                                    </table>
	                                </div><!-- /.box-body -->
	                                </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_4">
                                	<div class="box box-default">
                                	<div class="box-body">
                                	<form:form id="uploadProjectFileForm"
										modelAttribute="photo"
										action="${contextPath}/project/upload/photo"
										method="post"
										enctype="multipart/form-data">
         									<label for="exampleInputFile">Upload Photo (20MB Max)</label>
											<form:input type="file" id="file" path="file"/><br/>
											<label>Description</label>
											<form:textarea class="form-control"
												rows="3" id="description" path="description"
												placeholder="Example: Image of project progress..."></form:textarea><br/>
											<button class="btn btn-default btn-flat btn-sm" id="uploadButton">Upload</button>
                                    </form:form>
                                    <br/>
                                    <c:if test="${!empty project.photos}">
                                    	<br/>
           									<ul class="row">
									     		<c:forEach items="${project.photos}" var="photo">
									     			<li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
														<img src="${contextPath}/image/display/?project_id=${project.id}&filename=${photo.name}"/><br/><br/>
														<h6>${photo.name}</h6>
														<h6>${photo.description}</h6>
														<br/>
														<h6>Uploaded ${photo.dateUploaded}</h6>
														<c:set var="photoUploader" value="${photo.uploader}"/>
														<c:set var="photoUploaderName" value="${photoUploader.prefix} ${photoUploader.firstName} ${photoUploader.middleName} ${photoUploader.lastName} ${photoUploader.suffix}"/>
														<h6>${photoUploaderName}</h6>
														
														<c:url value="/project/delete/photo/${photo.id}" var="urlDeletePhoto"/>
														<a href="#">
															<button class="btn btn-default btn-flat btn-sm">View</button>
														</a>
														<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
														<c:url value="/project/delete/photo/${photo.id}" var="urlDeletePhoto"/>
														<a href="${urlDeletePhoto}">
															<button class="btn btn-default btn-flat btn-sm">Delete</button>
														</a>
														</sec:authorize>
													</li>
									     		</c:forEach>
										     </ul>
       								</c:if>
       								</div>
       								</div>
									<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
								      <div id="modal-dialog" class="modal-dialog modal-lg">
								        <div class="modal-content">         
								          <div class="modal-body">                
								          </div>
								        </div><!-- /.modal-content -->
								      </div><!-- /.modal-dialog -->
								    </div><!-- /.modal -->
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_timeline">
                                	<div class="row">
                   						<div class="col-xs-12">
		                                	<div class="box box-default">
	              								<div class="box-header">
	              									<h3 class="box-title">Timeline</h3>
	              								</div>
				                                <div class="box-body">
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
               									</table><br/>
				                                <c:choose>
				                                	<c:when test="${!empty project.assignedTasks}">
						                                <div id="gantt-chart" class="gantt-holder">
						                                </div><!-- /.box-body -->
				                                	</c:when>
				                                	<c:when test="${empty project.assignedTasks}">
				                                		<div id="gantt-chart" class="gantt-holder">
				                                			No tasks for this project.
						                                </div><!-- /.box-body -->
				                                	</c:when>
				                                </c:choose>
				                                </div>
				                            </div>
			                            </div>
		                            </div>
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Milestones</h3>
                   								</div>
                   								<div class="box-body">
                   									<button class="btn btn-default btn-flat btn-sm" id="createMilestone">Create Milestone</button>
                   									<br/>
                   									<br/>
                   									<table id="milestones-table" class="table table-bordered table-striped">
              										<thead>
              											<tr>
              											<th>&nbsp;</th>
              											<th>Milestone</th>
              											<th>Status</th>
              											<th>New Task</th>
              											<th>Ongoing Task</th>
              											<th>Done Task</th>
              											</tr>
              										</thead>
              										<tbody>
														<tr>
															<td>
															<button class="btn btn-default btn-flat btn-sm">View</button>
															<button class="btn btn-default btn-flat btn-sm">Delete</button>
															</td>
														<c:forEach items="${milestoneSummary}" var="milestoneMap">
														<c:set value="${milestoneMap.key}" var="milestone"/>
					                                	<c:set value="${milestoneMap.value}" var="msCount"/>
															<td>${milestone.name}</td>
															<td>
															<c:set value="${msCount.get(\"Status\").css()}" var="css"></c:set>
															<span class="label ${css}">${msCount.get("Status")}</span>
															</td>
															<td>${msCount.get("New")}</td>
															<td>${msCount.get("Ongoing")}</td>
															<td>${msCount.get("Done")}</td>
														</c:forEach>
														</tr>
													</tbody>
                   									</table>
                   									<br/>
													<b>Total Tasks Assigned to Milestones:</b> ${timelineSummaryMap.get("Total Tasks Assigned to Milestones")}<br/>
													<b>Total Milestones:</b> ${timelineSummaryMap.get("Total Milestones")}<br/>
													<b>Breakdown</b> of Total Milestones by Milestone Status:<br/><br/>
													<table id="milestone-breakdown-table" class="table table-bordered table-striped">
													<thead>
			                                    		<tr>
				                                            <th>Milestone Status</th>
				                                            <th>Count</th>
				                                        </tr>
			                                    	</thead>
													<tbody>
														<tr>
															<c:set value="${idToMilestoneMap.get(\"New\").css()}" var="css"></c:set>
															<td><span class="label ${css}">${idToMilestoneMap.get("New")}</span></td>
															<td>${timelineSummaryMap.get("Total Milestones (New)")}</td>
														</tr>
														<tr>
															<c:set value="${idToMilestoneMap.get(\"Ongoing\").css()}" var="css"></c:set>
															<td><span class="label ${css}">${idToMilestoneMap.get("Ongoing")}</span></td>
															<td>${timelineSummaryMap.get("Total Milestones (Ongoing)")}</td>
														</tr>
														<tr>
															<c:set value="${idToMilestoneMap.get(\"Done\").css()}" var="css"></c:set>
															<td><span class="label ${css}">${idToMilestoneMap.get("Done")}</span></td>
															<td>${timelineSummaryMap.get("Total Milestones (Done)")}</td>
														</tr>
													</tbody>
													</table>
													
                   								</div>
                   							</div>
                   						</div>
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Summary of Tasks</h3>
                   								</div>
                   								<div class="box-body">
                   								
                   								<b>Total Tasks:</b> ${timelineSummaryMap.get("Total Tasks")}<br/>
                   								<b>Breakdown</b> of Total Tasks by Task Status:<br/><br/>
                   								
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
                   						<div class="col-xs-12">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Tasks</h3>
                   								</div>
                   								<div class="box-body">
			                                    		<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
					                                	<table>
					                                    	<tr>
					                                    		<td>
					                                    			<c:url value="/task/create/from/project" var="urlAddTask"/>
					                                    			<a href="${urlAddTask}">
							                                    	<button class="btn btn-default btn-flat btn-sm">Add Task</button>
					                                    			</a>
					                                    		</td>
					                                    		<c:if test="${!empty project.assignedTasks}">
					                                    		<td>
					                                    			&nbsp;
					                                    		</td>
					                                    		<td>
					                                    			<form method="post" action="${contextPath}/task/unassign/project/all">
					                                    				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			                											<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
			                											<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
			               											</form>
					                                    		</td>
					                                    		</c:if>
					                                    	</tr>
					                                    </table><br/>
			                                    		</sec:authorize>
					                                    <table id="tasks-table" class="table table-bordered table-striped">
					                                    	<thead>
					                                            <tr>
						                                        	<th>&nbsp;</th>
						                                            <th>Status</th>
						                                            <th>Start</th>
						                                            <th>Duration</th>
						                                            <th>Title</th>
						                                            <th>Content</th>
						                                            <th>Team</th>
						                                            <th>Staff</th>
						                                        </tr>
			                                        		</thead>
					                                        <tbody>
						                                        <c:set var="taskList" value="${project.assignedTasks}"/>
							                                	<c:if test="${!empty taskList}">
					                                        		<c:forEach items="${taskList}" var="task">
					                                        			<tr>
					                                        				<td>
					                                        					<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
					                                        					<div class="btn-group">
										                                            <button type="button" class="btn btn-default btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">
										                                                Mark As&nbsp;
										                                                <span class="caret"></span>
										                                            </button>
										                                            <ul class="dropdown-menu">
										                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=0&${_csrf.parameterName}=${_csrf.token}">New</a></li>
										                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=1&${_csrf.parameterName}=${_csrf.token}">Ongoing</a></li>
										                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=2&${_csrf.parameterName}=${_csrf.token}">Completed</a></li>
										                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=3&${_csrf.parameterName}=${_csrf.token}">Failed</a></li>
										                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=4&${_csrf.parameterName}=${_csrf.token}">Cancelled</a></li>
			<!-- 							                                                <li class="divider"></li> -->
			<!-- 							                                                <li><a href="#">Separated link</a></li> -->
										                                            </ul>
										                                        </div>
										                                        </sec:authorize>
										                                        <c:url value="/task/edit/${task.id}/from/project/${project.id}" var="urlViewTask"/>
										                                        <a href="${urlViewTask}">
								                                            	<button class="btn btn-default btn-flat btn-sm">View</button>
										                                        </a>
								                                            	<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
								                                            	<form method="post" action="${contextPath}/task/unassign/from/project">
								                                            	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
								                                            	<input type="hidden" name="task_id" value="${task.id}"/>
								                                            	<input type="hidden" name="project_id" value="${project.id}"/>
								                                            	<button class="btn btn-default btn-flat btn-sm">Unassign</button>
								                                            	</form> 
								                                            	</sec:authorize>
					                                        				</td>
								                                            <td style="vertical-align: middle;">
									                                            <c:set value="${task.getStatusEnum().css()}" var="css"></c:set>
																				<span class="label ${css}">${task.getStatusEnum()}</span>
								                                            </td>
								                                            <td>${task.dateStart}</td>
								                                            <td>${task.duration}</td>
								                                            <td>
								                                            ${task.title}
								                                            </td>
								                                            <td>
								                                            ${task.content}
								                                            </td>
								                                            <td>
								                                            	<c:choose>
								                                            		<c:when test="${!empty task.teams}">
								                                            			<c:forEach items="${task.teams}" var="taskTeam">
								                                            			<a href="${contextPath}/team/edit/${taskTeam.id}">
										                                            		<button class="btn btn-default btn-flat btn-sm">View</button>&nbsp;&nbsp;
										                                            	</a>
										                                            	${taskTeam.name}
										                                            	<br/>
								                                            			</c:forEach>
								                                            		</c:when>
								                                            		<c:when test="${empty task.teams}">
								                                            			No team assigned.
								                                            		</c:when>
								                                            	</c:choose>
								                                            </td>
								                                            <td>
								                                            	<c:choose>
								                                            		<c:when test="${!empty task.staff}">
								                                            			<c:forEach items="${task.staff}" var="taskStaff">
								                                            			<c:set var="taskStaffName" value="${taskStaff.prefix} ${taskStaff.firstName} ${taskStaff.middleName} ${taskStaff.lastName} ${taskStaff.suffix}"/>
								                                            			<a href="${contextPath}/staff/edit/from/project/?${taskStaff.id}">
										                                            		<button class="btn btn-default btn-flat btn-sm">View</button>&nbsp;&nbsp;
										                                            	</a>
										                                            	${taskStaffName}
										                                            	<br/>
								                                            			</c:forEach>
								                                            		</c:when>
								                                            		<c:when test="${empty task.staff}">
								                                            			No manager assigned.
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
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_calendar">
                               	<div class="row">
               						<div class="col-xs-12">
               							<div class="box box-default">
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
                                <div class="tab-pane" id="tab_payroll">
                                	<div class="box box-default">
                                		<div class="box-header">
          									<h3 class="box-title">Payroll</h3>
          								</div>
		                                <div class="box-body">
									  	  <table id="treegrid1"></table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_managers">
                                	<div class="box">
		                                <div class="box-body table-responsive">
		                                	<c:set var="displayBreakManager" value="${false}"/>
		                                	<table>
		                                    	<tr>
		                                    		<sec:authorize access="hasRole('ROLE_STAFF_EDITOR')">
		                                    		<td>
		                                    			<c:url var="urlCreateStaff" value="/staff/edit/0/from/project/${project.id}"/>
		                                    			<a href="${urlCreateStaff}">
				                                    	<button class="btn btn-default btn-flat btn-sm">Create Staff</button>
		                                    			</a>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:set var="displayBreakManager" value="${true}"/>
		                                    		</sec:authorize>
		                                    		<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
		                                    		<c:if test="${!empty staffList}">
 		                                    		<form:form 
 		                                    		modelAttribute="staffPosition"  
 		                                    		method="post" 
 		                                    		action="${contextPath}/project/assign/staff"> 
 		                                    			<td>
 		                                    			<form:select class="form-control" path="staffID"> 
                                     						<c:forEach items="${staffList}" var="staff"> 
                                     							<c:set var="staffName" value="${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}"/> 
                                     							<form:option value="${staff.id}" label="${staffName}"/> 
                                     						</c:forEach> 
 		                                    			</form:select> 
 		                                    			</td>
 		                                    			<td>
 		                                    				&nbsp;
 		                                    			</td>
 		                                    			<td>
 		                                    			<form:input placeholder="Example: Project Manager, Leader, etc..." 
 		                                    				type="text" 
 															class="form-control" 
 															path="position"/>
 		                                    			</td>
 		                                    			<td>
 		                                    				&nbsp;
 		                                    			</td>
 														<td>
 														<button class="btn btn-default btn-flat btn-sm">Assign</button>
 		                                    			</td> 
 		                                    		</form:form> 
		                                    		</c:if>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:if test="${!empty project.managerAssignments}">
		                                    		<td>
               											<c:url var="urlProjectUnassignStaffAll" value="/project/unassign/staff/all"/>
		                                    			<a href="${urlProjectUnassignStaffAll}">
                											<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
		                                    			</a>
		                                    		</td>
		                                    		</c:if>
		                                    		<c:set var="displayBreakManager" value="${true}"/>
		                                    		</sec:authorize>
		                                    	</tr>
		                                    </table>
		                                    <c:if test="${displayBreakManager}">
		                                    <br/>
		                                    </c:if>
		                                    <table id="managers-table" class="table table-bordered table-striped">
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
			                                        <c:set var="managerAssignments" value="${project.managerAssignments}"/>
				                                	<c:if test="${!empty managerAssignments}">
				                                		<c:forEach items="${managerAssignments}" var="assignment">
			                                			<c:set var="manager" value="${assignment.manager}"/>
			                                            <tr>
			                                            	<td>
			                                            		<center>
			                                            			<c:url var="urlViewStaff" value="/staff/edit/${manager.id}/from/project/${project.id}" />
			                                            			<a href="${urlViewStaff}">
							                                    	<button class="btn btn-default btn-flat btn-sm">View</button>
			                                            			</a>
	                   												<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
	                   												<c:url var="urlUnassignStaff" value="/project/unassign/staff/${manager.id}"/>
	                   												<a href="${urlUnassignStaff}">
																		<button class="btn btn-default btn-flat btn-sm">Unassign</button>
	                   												</a>
	                   												</sec:authorize>
																</center>
															</td>
			                                                <td>
			                                                	<div class="user-panel">
													            <div class="pull-left image">
													                <c:choose>
		                                                			<c:when test="${!empty manager.thumbnailURL}">
		                                                				<img src="${contextPath}/image/display/staff/profile/?staff_id=${manager.id}" class="img-circle"/>
		                                                			</c:when>
		                                                			<c:when test="${empty manager.thumbnailURL}">
		                                                				<img src="${contextPath}/resources/img/avatar5.png" class="img-circle">
		                                                			</c:when>
			                                                		</c:choose>
													            </div>
														        </div>
			                                                </td>
			                                                <td>${manager.prefix} ${manager.firstName} ${manager.middleName} ${manager.lastName} ${manager.suffix}</td>
			                                                <td>${manager.companyPosition}</td>
			                                                <td>${manager.email}</td>
			                                                <td>${manager.contactNumber}</td>
			                                            </tr>
		                                            </c:forEach>
	                                        		</c:if>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_expenses">
                                	<div class="box">
		                                <div class="box-body table-responsive">
		                                	<c:set var="displayBreakTeam" value="${false}"/>
		                                	<table>
		                                    	<tr>
		                                    		<sec:authorize access="hasRole('ROLE_TEAM_EDITOR')">
		                                    		<td>
		                                    			<c:url var="urlCreateTeam" value="/team/edit/0/from/project/${project.id}"/>
		                                    			<a href="${urlCreateTeam}">
				                                    	<button class="btn btn-default btn-flat btn-sm">Create Team</button>
		                                    			</a>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:set var="displayBreakTeam" value="${true}"/>
		                                    		</sec:authorize>
		                                    		<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
		                                    		<c:if test="${!empty teamList}">
		                                    		<form:form modelAttribute="teamAssignment" method="post" action="${contextPath}/project/assign/team">
		                                    		<td>
		                                    			<form:select class="form-control" path="teamID" items="${teamList}" itemLabel="name" itemValue="id"/>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
														<button class="btn btn-default btn-flat btn-sm">Assign</button>
		                                    		</td>
		                                    		</form:form>
		                                    		</c:if>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:if test="${!empty project.assignedTeams}">
		                                    		<td>
              											<c:url value="/project/unassign/team/all" var="urlUnassignTeamAll"/>
					                                    <a href="${urlUnassignTeamAll}">
              												<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
					                                    </a>
		                                    		</td>
		                                    		</c:if>
		                                    		<c:set var="displayBreakTeam" value="${true}"/>
		                                    		</sec:authorize>
		                                    	</tr>
		                                    </table>
		                                    <c:if test="${displayBreakTeam}">
		                                    <br/>
		                                    </c:if>
		                                    <table id="teams-table" class="table table-bordered table-striped">
		                                    	<thead>
		                                            <tr>
		                                            	<th>&nbsp;</th>
		                                            	<th>#</th>
		                                                <th>Name</th>
		                                            </tr>
                                        		</thead>
		                                        <tbody>
			                                        <c:set var="teams" value="${project.assignedTeams}"/>
				                                	<c:if test="${!empty teams}">
				                                		<c:forEach items="${teams}" var="team">
			                                            <tr>
			                                            	<td>
			                                            		<center>
			                                            			<c:url var="urlViewTeam" value="/team/edit/${team.id}/from/project/${project.id}"/>
			                                            			<a href="${urlViewTeam}">
							                                    	<button class="btn btn-default btn-flat btn-sm">View</button>
			                                            			</a>
								                                    <sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
								                                    <c:url value="/project/unassign/team/${team.id}" var="urlUnassignTeam"/>
								                                    <a href="${urlUnassignTeam}">
	                   													<button class="btn btn-default btn-flat btn-sm">Unassign</button>
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
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_teams">
                                	<div class="box">
		                                <div class="box-body table-responsive">
		                                	<c:set var="displayBreakTeam" value="${false}"/>
		                                	<table>
		                                    	<tr>
		                                    		<sec:authorize access="hasRole('ROLE_TEAM_EDITOR')">
		                                    		<td>
		                                    			<c:url var="urlCreateTeam" value="/team/edit/0/from/project/${project.id}"/>
		                                    			<a href="${urlCreateTeam}">
				                                    	<button class="btn btn-default btn-flat btn-sm">Create Team</button>
		                                    			</a>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:set var="displayBreakTeam" value="${true}"/>
		                                    		</sec:authorize>
		                                    		<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
		                                    		<c:if test="${!empty teamList}">
		                                    		<form:form modelAttribute="teamAssignment" method="post" action="${contextPath}/project/assign/team">
		                                    		<td>
		                                    			<form:select class="form-control" path="teamID" items="${teamList}" itemLabel="name" itemValue="id"/>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
														<button class="btn btn-default btn-flat btn-sm">Assign</button>
		                                    		</td>
		                                    		</form:form>
		                                    		</c:if>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:if test="${!empty project.assignedTeams}">
		                                    		<td>
              											<c:url value="/project/unassign/team/all" var="urlUnassignTeamAll"/>
					                                    <a href="${urlUnassignTeamAll}">
              												<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
					                                    </a>
		                                    		</td>
		                                    		</c:if>
		                                    		<c:set var="displayBreakTeam" value="${true}"/>
		                                    		</sec:authorize>
		                                    	</tr>
		                                    </table>
		                                    <c:if test="${displayBreakTeam}">
		                                    <br/>
		                                    </c:if>
		                                    <table id="teams-table" class="table table-bordered table-striped">
		                                    	<thead>
		                                            <tr>
		                                            	<th>&nbsp;</th>
		                                            	<th>#</th>
		                                                <th>Name</th>
		                                            </tr>
                                        		</thead>
		                                        <tbody>
			                                        <c:set var="teams" value="${project.assignedTeams}"/>
				                                	<c:if test="${!empty teams}">
				                                		<c:forEach items="${teams}" var="team">
			                                            <tr>
			                                            	<td>
			                                            		<center>
			                                            			<c:url var="urlViewTeam" value="/team/edit/${team.id}/from/project/${project.id}"/>
			                                            			<a href="${urlViewTeam}">
							                                    	<button class="btn btn-default btn-flat btn-sm">View</button>
			                                            			</a>
								                                    <sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
								                                    <c:url value="/project/unassign/team/${team.id}" var="urlUnassignTeam"/>
								                                    <a href="${urlUnassignTeam}">
	                   													<button class="btn btn-default btn-flat btn-sm">Unassign</button>
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
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                </c:when>
                                </c:choose>
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
	
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.date.extensions.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.extensions.js" type="text/javascript"></script>
	<script src="<c:url value="/resources/js/common.js" />"type="text/javascript"></script>

	<c:if test="${project.id != 0 && !empty project.assignedTasks}">
   	<script src="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.js" />"type="text/javascript"></script>
    <script src="${contextPath}/resources/lib/dhtmlxGantt_v3.1.1_gpl/ext/dhtmlxgantt_tooltip.js" type="text/javascript"></script>
	<script src="<c:url value="/resources/js/gantt-custom.js" />"type="text/javascript"></script>
	
	<!-- Ignite UI Required Combined JavaScript Files -->
	<script src="<c:url value="/resources/lib/modernizr.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/lib/igniteui/infragistics.core.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/lib/igniteui/infragistics.lob.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/lib/igniteui/infragistics.ui.treegrid.js" />"type="text/javascript"></script>
	
	<script type="text/javascript">
	    $(document).ready(function() {
	    	var ganttJSON = ${ganttJSON};
		    var tasks = {'data': ganttJSON};
			gantt.init("gantt-chart");
		    gantt.parse(tasks);
		    gantt.sort("start_date");
		 	
		    // TODO.
		    // On load of the page: switch to the currently selected tab.
		    var hash = window.name;
		    $('#myTab').find("[href='#"+ hash +"']").tab('show');
		});
	</script>
   	</c:if>
   	
   	
   	<c:if test="${project.id != 0}">
   	<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
   	<script type="text/javascript">
   	$(document).ready(function() {
		$('#detailsDivEditor').hide();
		
		var eventsJSON = ${calendarJSON};
		$('#calendar').fullCalendar({
			events: eventsJSON,
			dayClick: function(date, jsEvent, view) {
// 				$("#modalDate").val(date.format());
// 				$("#modalWage").val(staffWage);
// 				$("#myModal").modal('show');
		    },
		    eventClick: function(calEvent, jsEvent, view) {
// 		    	$("#modalDate").val(calEvent.start.format());
// 				$("#modalWage").val(staffWage);
// 				$("#myModal").modal('show');
				
// 				var statusValue = calEvent.attendanceStatus;
// 				$('#attendanceStatus').val(statusValue);
				
// 				if(statusValue == 2 || this.value == -1) {
// 					$('#modalWage').hide();
// 					$('#modalWageLabel').hide();
// 					$('#modalWageBreak').hide();
// 				} else {
// 					$('#modalWage').val(calEvent.attendanceWage);
// 					$('#modalWage').show();
// 					$('#modalWageLabel').show();
// 					$('#modalWageBreak').show();
// 				}
		    }
	    });
	});
   	</script>
	</sec:authorize>
	</c:if>
	
	<c:if test="${project.id != 0 && !empty project.assignedFields}">
   	<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
   	<script type="text/javascript">
   	$(document).ready(function() {
		$('#detailsDivEditor').hide();
		$('#fieldsDivEditor').hide();
	});
   	</script>
	</sec:authorize>
	</c:if>
	
	<script type="text/javascript">
	    // Photos event handler.
		$(document).on('click', 'a.controls', function(){
	        var index = $(this).attr('href');
	        var src = $('ul.row li:nth-child('+ index +') img').attr('src');             
	        $('.modal-body img').attr('src', src);
            
	        var newPrevIndex = parseInt(index) - 1; 
	        var newNextIndex = parseInt(newPrevIndex) + 2; 
	        
	        if($(this).hasClass('previous')){               
	            $(this).attr('href', newPrevIndex); 
	            $('a.next').attr('href', newNextIndex);
	        }else{
	            $(this).attr('href', newNextIndex); 
	            $('a.previous').attr('href', newPrevIndex);
	        }
	        
	        var total = $('ul.row li').length + 1; 
	        // Hide next button.
	        if(total === newNextIndex){
	            $('a.next').hide();
	        }else{
	            $('a.next').show()
	        }
	        
	        // Hide previous button.
	        if(newPrevIndex === 0){
	            $('a.previous').hide();
	        }else{
	            $('a.previous').show()
	        }
	        
	        return false;
	    });
		
		$(document).ready(function() {
			$("#example-1").dataTable();
			$("#managers-table").dataTable();
			$("#teams-table").dataTable();
			$("#tasks-table").dataTable();
			$("#date-mask").inputmask("yyyy/mm/dd", {"placeholder": "yyyy/mm/dd"});
// 			$("#project_status").val("${fn:escapeXml(project.status)}");
			
			// Event handler for photos.
			$('li img').on('click',function(){
                var src = $(this).attr('src');
                var img = '<img id="popupImage" src="' + src + '" class="img-responsive"/>';
                var index = $(this).parent('li').index();
                var html = '';
                html += img;                
                html += '<br/><div>';
               
                // Previous button.
                html += '<a class="controls previous" href="' + (index) + '">';
                html += '<button class="btn btn-default btn-flat btn-sm">Previous</button>';
                html += '</a>';
                html += '&nbsp;';
               
                // Next button.
                html += '<a class="controls next" href="'+ (index+2) + '">';
                html += '<button class="btn btn-default btn-flat btn-sm">Next</button>';
                html += '</a>';
                html += '</div>';
                $('#myModal').modal();
                $('#myModal').on('shown.bs.modal', function(){
                    $('#myModal .modal-body').html(html);
                    $('a.controls').trigger('click');
                })
                $('#myModal').on('hidden.bs.modal', function(){
                    $('#myModal .modal-body').html('');
                });
           });
			
			// Tree grid.
			var flatDS = ${payrollJSON};
            $("#treegrid1").igTreeGrid({
                dataSource: flatDS,
                primaryKey: "primaryKey",
                foreignKey: "foreignKey",
                columns: [
					{ headerText: "primaryKey", key: "primaryKey", dataType: "number", hidden: true },
					{ headerText: "foreignKey", key: "foreignKey", dataType: "number", hidden: true },
                    { headerText: "Name", key: "name", dataType: "string" },
                    { headerText: "Payroll", key: "value", dataType: "string" }
                ]
            });
            
	    });
	</script>
</body>
</html>