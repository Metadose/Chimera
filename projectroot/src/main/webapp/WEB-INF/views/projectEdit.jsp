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
	<c:import url="/resources/css-includes.jsp" />
	<link href="<c:url value="/resources/css/gantt-custom.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.css" />"rel="stylesheet" type="text/css" />
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
	</style>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
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
	                <small>${action} Project</small>
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
                                		<li><a href="#tab_2" data-toggle="tab">Tasks</a></li>
		                                <li><a href="#tab_timeline" id="tab_timeline-href" data-toggle="tab">Timeline</a></li>
		                                <li><a href="#tab_3" data-toggle="tab">Files</a></li>
		                                <li><a href="#tab_4" data-toggle="tab">Photos</a></li>
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
		                   										<form id="uploadPhotoForm" action="${contextPath}/photo/upload/project/profile" method="post" enctype="multipart/form-data">	
		                   											<input type="hidden" value="${project.id}" id="project_id" name="project_id"/>
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
						                                        </form>
						                                        <form id="deletePhotoForm" action="${contextPath}/photo/delete/project/profile/?project_id=${project.id}" method="post">
						                                        </form>
						                                        <br/>
						                                        <button onclick="submitForm('uploadPhotoForm')" class="btn btn-default btn-flat btn-sm">Upload</button>
						                                        <button onclick="submitForm('deletePhotoForm')" class="btn btn-default btn-flat btn-sm">Delete Photo</button>
						                                    </div>
						                                    </sec:authorize>
                                						</c:when>
                              						</c:choose>
				                                    <br/>
				                                    <c:if test="${project.id != 0}">
				                                    <div class="form-group" id="detailsDivViewer">
			                                            <label>Name</label><br/>
			                                            ${fn:escapeXml(project.name)}<br/><br/>
			                                            <label>Status</label><br/>
			                                            ${fn:escapeXml(project.status)}<br/><br/>
			                                            <label>Location</label><br/>
			                                            ${fn:escapeXml(project.location)}<br/><br/>
			                                            <label>Notes</label><br/>
			                                            ${fn:escapeXml(project.notes)}<br/><br/>
			                                            <sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
			                                            <button class="btn btn-default btn-flat btn-sm" onclick="switchDisplay(detailsDivViewer, detailsDivEditor)">Edit</button>
			                                            </sec:authorize>
			                                        </div>
			                                        </c:if>
				                                    <sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
			                                        <div class="form-group" id="detailsDivEditor">
                  										<form role="form" name="detailsForm" id="detailsForm" method="post" action="${contextPath}/project/create">
			                                        	<input type="hidden" name="id" value="${project.id}"/>
			                                            <label>Name</label>
			                                            <input type="text" class="form-control" name="name" value="${fn:escapeXml(project.name)}"/><br/>
			                                            <label>Status</label>
			                                            <select class="form-control" id="project_status" name="status">
					                                    	<option value="0">New</option>
					                                    	<option value="1">Ongoing</option>
					                                    	<option value="2">Completed</option>
					                                    	<option value="3">Failed</option>
					                                    	<option value="4">Cancelled</option>
			                                            </select><br/>
			                                            <label>Location</label>
			                                            <input type="text" class="form-control" name="location" value="${fn:escapeXml(project.location)}"/><br/>
			                                            <label>Notes</label>
			                                            <input type="text" class="form-control" name="notes" value="${fn:escapeXml(project.notes)}"/><br/>
			                                    	</form>
			                                    	<c:choose>
		                                            	<c:when test="${project.id == 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${project.id > 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<a href="${contextPath}/project/delete/${project.id}">
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
               												<c:set var="fieldFormID" value="${0}"/>
               												
                   											<c:if test="${!empty projectFields}">
   															<div class="form-group" id="fieldsDivViewer">
	               												<c:forEach var="field" items="${projectFields}"  varStatus="loop">
		       															<label>${fn:escapeXml(field.label)}</label><br/>
		       															${fn:escapeXml(field.value)}<br/>
		       															<br/>
																</c:forEach>
																<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
					                                            	<button class="btn btn-default btn-flat btn-sm" onclick="switchDisplay(fieldsDivViewer, fieldsDivEditor)">Edit</button>
					                                            </sec:authorize>
   															</div>
   															</c:if>
   															
           													<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
               												<c:set var="formStyle" value="padding-bottom: 40px"/>
	           												<div class="form-group" id="fieldsDivEditor">
	           													<c:if test="${!empty projectFields}">
		               												<c:forEach var="field" items="${projectFields}"  varStatus="loop">
		           														<c:if test="${loop.last}">
		           															<c:set var="formStyle" value="padding-bottom: 18px"/>
		           														</c:if>
		           														<div style="${formStyle}">
		           														<form id="field_update_${fieldFormID}" method="post" action="${contextPath}/field/update/assigned/project">
																			<input type="hidden" name="project_id" value="${project.id}"/>
																			<input type="hidden" name="field_id" value="${field.field.id}"/>
																			<input type="hidden" id="old_label" name="old_label" value="${fn:escapeXml(field.label)}"/>
																			<input type="hidden" id="old_value" name="old_value" value="${fn:escapeXml(field.value)}"/>
			       															<input type="text" class="form-control" id="label" name="label" value="${fn:escapeXml(field.label)}"><br/>
			       															<textarea class="form-control" rows="3" id="value" name="value">${fn:escapeXml(field.value)}</textarea><br/>
																		</form>
		               													<form id="field_unassign_${fieldFormID}" method="post" action="${contextPath}/field/unassign/project">
																			<input type="hidden" name="project_id" value="${project.id}"/>
																			<input type="hidden" name="field_id" value="${field.field.id}"/>
			       															<input type="hidden" name="label" value="${fn:escapeXml(field.label)}">
			       															<input type="hidden" name="value" value="${fn:escapeXml(field.value)}">
																		</form>
		       															<button class="btn btn-default btn-flat btn-sm" onclick="submitForm('field_update_${fieldFormID}')">Update</button>&nbsp;
		       															<button class="btn btn-default btn-flat btn-sm" onclick="submitForm('field_unassign_${fieldFormID}')">Remove</button>
		       															</div>
																		<c:set var="fieldFormID" value="${fieldFormID + 1}"/>
																	</c:forEach>
																	<c:choose>
																		<c:when test="${!empty projectFields}">
																			<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
																			<form role="form" name="fieldsUnassignForm" id="fieldsUnassignForm" method="post" action="${contextPath}/field/unassign/project/all">
																				<input type="hidden" name="project_id" value="${project.id}"/>
																				<button class="btn btn-default btn-flat btn-sm">Remove All</button>
																			</form>
																			</sec:authorize>
																		</c:when>
																	</c:choose>
																	<br/>
																	<br/>
																</c:if>
																<h4>Add More Information</h4>
																<form role="form" name="fieldsForm" id="fieldsForm" method="post" action="${contextPath}/field/assign/project">
																	<input type="hidden" name="project_id" value="${project.id}"/>
																	<input type="hidden" name="field_id" value="1"/>
																	<label>Label</label><br/>
																	<input type="text" name="label" id="label" class="form-control" placeholder="Example: SSS, Building Permit No., Sub-contractor, etc..."><br/>
																	<label>Information</label><br/>
																	<textarea class="form-control" rows="3" id="value" name="value" placeholder="Example: 000-123-456, AEE-123, OneForce Construction, etc...">${fn:escapeXml(field.value)}</textarea>
																</form>
																<br/>
		                                           				<button class="btn btn-default btn-flat btn-sm" onclick="submitForm('fieldsForm')">Add</button><br/>
		                                           				<c:if test="${!empty projectFields}">
		                                           				<br/>
																<button class="btn btn-default btn-flat btn-sm" onclick="switchDisplay(fieldsDivEditor, fieldsDivViewer)">Done Editing</button>
																</c:if>
															</div>
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
                                <div class="tab-pane" id="tab_2">
                                	<div class="box">
		                                <div class="box-body table-responsive">
		                                	<table>
		                                    	<tr>
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/task/assign/from/project/">
		                                    			<input type="hidden" name="project_id" value="${project.id}"/>
		                                    			<input type="hidden" name="origin" value="project"/>
		                                    			<input type="hidden" name="originID" value="${project.id}"/>
				                                    	<button class="btn btn-default btn-flat btn-sm">Add Task</button>
					                                    </form>
		                                    		</td>
		                                    		<c:if test="${!empty project.assignedTasks}">
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/task/unassign/project/all">
                											<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
                											<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
               											</form>
		                                    		</td>
		                                    		</c:if>
		                                    	</tr>
		                                    </table><br/>
		                                    <table id="tasks-table" class="table table-bordered table-striped">
		                                    	<thead>
		                                            <tr>
			                                        	<th>&nbsp;</th>
			                                            <th>Status</th>
			                                            <th>Content</th>
			                                            <th>Team</th>
			                                            <th>Staff</th>
			                                            <th>Start</th>
			                                            <th>Duration</th>
			                                        </tr>
                                        		</thead>
		                                        <tbody>
			                                        <c:set var="taskList" value="${project.assignedTasks}"/>
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
							                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=0">New</a></li>
							                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=1">Ongoing</a></li>
							                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=2">Completed</a></li>
							                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=3">Failed</a></li>
							                                                <li><a href="${contextPath}/task/mark/project/?project_id=${project.id}&task_id=${task.id}&status=4">Cancelled</a></li>
<!-- 							                                                <li class="divider"></li> -->
<!-- 							                                                <li><a href="#">Separated link</a></li> -->
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
					                                            	<c:choose>
						                                            	<c:when test="${task.status == 0}">
						                                            		<span class="label label-info">New</span>
						                                            	</c:when>
						                                            	<c:when test="${task.status == 1}">
						                                            		<span class="label label-primary">Ongoing</span>
						                                            	</c:when>
						                                            	<c:when test="${task.status == 2}">
						                                            		<span class="label label-success">Completed</span>
						                                            	</c:when>
						                                            	<c:when test="${task.status == 3}">
						                                            		<span class="label label-danger">Failed</span>
						                                            	</c:when>
						                                            	<c:when test="${task.status == 4}">
						                                            		<h6>Cancelled</h6>
						                                            	</c:when>
						                                            </c:choose>
					                                            </td>
					                                            <td>${task.content}</td>
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
					                                            			<h5>No team assigned.</h5>
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
					                                            			<h5>No manager assigned.</h5>
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
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_3">
                                	<div class="box box-default">
                                    <div class="box-body table-responsive">
                                    	<form enctype="multipart/form-data" method="post" action="${contextPath}/projectfile/upload/file/project">
											<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
											<label for="exampleInputFile">File Upload (20MB Max)</label>
											<input type="file" id="file" name="file"/><br/>
											<label>Description</label>
											<input type="text" class="form-control" id="description" name="description"/><br/>
											<button class="btn btn-default btn-flat btn-sm" id="uploadButton">Upload</button>
										</form>
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
			                                            		<form action="${contextPath}/projectfile/download/from/project/" method="post">
			                                            			<input type="hidden" name="project_id" value="${project.id}"/>
			                                            			<input type="hidden" name="projectfile_id" value="${file.id}"/>
			                                            			<button class="btn btn-default btn-flat btn-sm">Download</button>
			                                            		</form>
																<button class="btn btn-default btn-flat btn-sm">View Details</button>
																<form name="deleteFileForm" id="deleteFileForm" method="post" action="${contextPath}/projectfile/delete/from/project/">
																	<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
																	<input type="hidden" id="projectfile_id" name="projectfile_id" value="${file.id}"/>
																	<button class="btn btn-default btn-flat btn-sm">Delete</button>
																</form>
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
                                    <form enctype="multipart/form-data" method="post" action="${contextPath}/photo/upload/project">
										<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
										<label for="exampleInputFile">Upload Photo (20MB Max)</label>
										<input type="file" id="file" name="file"/><br/>
										<label>Description</label>
										<input type="text" class="form-control" id="description" name="description"/><br/>
										<button class="btn btn-default btn-flat btn-sm" id="uploadButton">Upload</button>
									</form>
                                    <br/>
                                    <c:if test="${!empty project.photos}">
                                    	<br/>
           									<ul class="row">
									     		<c:forEach items="${project.photos}" var="photo">
									     			<li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
														<img src="${contextPath}/image/display/?project_id=${project.id}&filename=${photo.name}"/><br/><br/>
														<form action="${contextPath}/photo/delete">
															<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
															<input type="hidden" id="photo_id" name="photo_id" value="${photo.id}"/>
															<h6>${photo.name}</h6>
															<h6>${photo.description}</h6>
															<br/>
															<h6>Uploaded ${photo.dateUploaded}</h6>
															
															<c:set var="photoUploader" value="${photo.uploader}"/>
															<c:set var="photoUploaderName" value="${photoUploader.prefix} ${photoUploader.firstName} ${photoUploader.middleName} ${photoUploader.lastName} ${photoUploader.suffix}"/>
															<h6>${photoUploaderName}</h6>
															<button class="btn btn-default btn-flat btn-sm" id="photoDeleteButton">Delete</button>
														</form>
													</li>
									     		</c:forEach>
										     </ul>
       								</c:if>
       								</div>
       								</div>
									<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
								      <div class="modal-dialog">
								        <div class="modal-content">         
								          <div class="modal-body">                
								          </div>
								        </div><!-- /.modal-content -->
								      </div><!-- /.modal-dialog -->
								    </div><!-- /.modal -->
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_timeline">
                                	<div class="box box-default">
		                                <div class="box-body">
		                                <div id="gantt-chart" style='width:1000px; height:400px;'>
<!-- 		                                <div id="gantt-chart" class="box-body table-responsive"> -->
		                                </div><!-- /.box-body -->
		                                </div>
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_managers">
                                	<div class="box">
		                                <div class="box-body table-responsive">
		                                	<table>
		                                    	<tr>
		                                    		<sec:authorize access="hasRole('ROLE_STAFF_EDITOR')">
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/staff/edit/from/origin">
		                                    			<input type="hidden" name="staff_id" value="0"/>
		                                    			<input type="hidden" name="origin" value="project"/>
		                                    			<input type="hidden" name="originID" value="${project.id}"/>
				                                    	<button class="btn btn-default btn-flat btn-sm">Create Staff</button>
					                                    </form>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		</sec:authorize>
		                                    		<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
		                                    		<c:if test="${!empty staffList}">
		                                    		<form role="form" method="post" action="${contextPath}/staff/assign/project">
		                                    		<td>
		                                    			<select class="form-control" name="staff_id">
                                    						<c:forEach items="${staffList}" var="staff">
                                    							<c:set var="staffName" value="${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}"/>
                                    							<option value="${staff.id}">${staffName}</option>
                                    						</c:forEach>
		                                    			</select>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<input placeholder="Example: Project Manager, Leader, etc..." type="text" class="form-control" name="project_position"/>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<input type="hidden" name="project_id" value="${project.id}"/>
														<button class="btn btn-default btn-flat btn-sm">Assign</button>
		                                    		</td>
		                                    		</form>
		                                    		</c:if>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:if test="${!empty project.managerAssignments}">
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/staff/unassign/project/all">
                											<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
                											<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
               											</form>
		                                    		</td>
		                                    		</c:if>
		                                    		</sec:authorize>
		                                    	</tr>
		                                    </table><br/>
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
			                                            			<form method="post" action="${contextPath}/staff/edit/from/origin">
					                                    			<input type="hidden" name="staff_id" value="${manager.id}"/>
					                                    			<input type="hidden" name="origin" value="project"/>
					                                    			<input type="hidden" name="originID" value="${project.id}"/>
							                                    	<button class="btn btn-default btn-flat btn-sm">View</button>
								                                    </form>
	                   												<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
																	<form name="unassignStaffForm" id="unassignStaffForm" method="post" action="${contextPath}/staff/unassign/project">
																		<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
																		<input type="hidden" id="staff_id" name="staff_id" value="${manager.id}"/>
																		<button class="btn btn-default btn-flat btn-sm">Unassign</button>
	                   												</form>
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
		                                                				<img src="/pmsys/resources/img/avatar5.png" class="img-circle">
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
                                <div class="tab-pane" id="tab_teams">
                                	<div class="box">
		                                <div class="box-body table-responsive">
		                                	<table>
		                                    	<tr>
		                                    		<sec:authorize access="hasRole('ROLE_TEAM_EDITOR')">
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/team/edit/from/origin">
		                                    			<input type="hidden" name="team_id" value="0"/>
		                                    			<input type="hidden" name="origin" value="project"/>
		                                    			<input type="hidden" name="originID" value="${project.id}"/>
				                                    	<button class="btn btn-default btn-flat btn-sm">Create Team</button>
					                                    </form>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		</sec:authorize>
		                                    		<sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
		                                    		<c:if test="${!empty teamList}">
		                                    		<form role="form" method="post" action="${contextPath}/team/assign/project">
		                                    		<td>
		                                    			<select class="form-control" name="team_id">
                                    						<c:forEach items="${teamList}" var="team">
                                    							<option value="${team.id}">${team.name}</option>
                                    						</c:forEach>
		                                    			</select>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<input type="hidden" name="project_id" value="${project.id}"/>
		                                    			<input type="hidden" name="origin" value="project"/>
		                                    			<input type="hidden" name="originID" value="${project.id}"/>
														<button class="btn btn-default btn-flat btn-sm">Assign</button>
		                                    		</td>
		                                    		</form>
		                                    		</c:if>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<c:if test="${!empty project.assignedTeams}">
		                                    		<td>
		                                    			<form role="form" method="post" action="${contextPath}/team/unassign/project/all">
              												<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
              												<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
              											</form>
		                                    		</td>
		                                    		</c:if>
		                                    		</sec:authorize>
		                                    	</tr>
		                                    </table><br/>
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
			                                            			<form method="post" action="${contextPath}/team/edit/from/origin">
					                                    			<input type="hidden" name="team_id" value="${team.id}"/>
					                                    			<input type="hidden" name="origin" value="project"/>
					                                    			<input type="hidden" name="originID" value="${project.id}"/>
							                                    	<button class="btn btn-default btn-flat btn-sm">View</button>
								                                    </form>
								                                    <sec:authorize access="hasRole('ROLE_PROJECT_EDITOR')">
																	<form role="form" method="post" action="${contextPath}/team/unassign/project">
	                   													<input type="hidden" id="project_id" name="project_id" value="${project.id}"/>
	                   													<input type="hidden" id="team_id" name="team_id" value="${team.id}"/>
	                   													<input type="hidden" name="origin" value="project"/>
		                                    							<input type="hidden" name="originID" value="${project.id}"/>
	                   													<button class="btn btn-default btn-flat btn-sm">Unassign</button>
	                   												</form>
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
	
	<c:import url="/resources/js-includes.jsp" />
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.date.extensions.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.extensions.js" type="text/javascript"></script>
	<script src="<c:url value="/resources/js/common.js" />"type="text/javascript"></script>

	<c:if test="${project.id != 0}">
	<!-- Generate the data to be used by the gantt. -->
	<c:set var="ganttData" value="'data':[{id:'${project.id}', text:'${fn:escapeXml(project.name)}', open: true, duration:0},"/>
    <c:if test="${!empty project.assignedTasks}">
    	<c:forEach var="task" items="${project.assignedTasks}">
    		<fmt:formatDate pattern="dd-MM-yyyy" value="${task.dateStart}" var="taskDateStart"/>
    		<c:set var="taskRow" value="{id:'${task.id}', status:${task.status}, text:'${fn:escapeXml(task.title)}', content:'${fn:escapeXml(task.content)}', start_date:'${taskDateStart}', open: true, duration:${task.duration}, parent:'${project.id}'},"/>
    		<c:set var="ganttData" value="${ganttData}${taskRow}"/>
    	</c:forEach>
    	<c:set var="ganttData" value="${fn:substring(ganttData, 0, fn:length(ganttData)-1)}"/>
    </c:if>
    <c:set var="ganttEnd" value="]"/>
   	<c:set var="ganttData" value="{${ganttData}${ganttEnd}}"/>
   	<script src="<c:url value="/resources/lib/dhtmlxGantt_v3.1.1_gpl/dhtmlxgantt.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/js/gantt-custom.js" />"type="text/javascript"></script>
	
	<script type="text/javascript">
	    $(document).ready(function() {
	    	var tasks = ${ganttData};
			gantt.init("gantt-chart");
		    gantt.parse(tasks);
		 	
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
			$("#project_status").val("${fn:escapeXml(project.status)}");
			
			// Event handler for photos.
			$('li img').on('click',function(){
                var src = $(this).attr('src');
                var img = '<img src="' + src + '" class="img-responsive"/>';
                //start of new code new code
                var index = $(this).parent('li').index();   
                var html = '';
                html += img;                
                html += '<div style="clear:both;padding-top: 5px;display:block;">';
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
	    });
	</script>
</body>
</html>