<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Team ${action}</title>
	<c:import url="/resources/css-includes.jsp" />
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
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                ${team.name}
	                <small>${action} Team</small>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                                <li><a href="#tab_3" data-toggle="tab">Members</a></li>
                                <li><a href="#tab_2" data-toggle="tab">Tasks</a></li>
                                <li><a href="#tab_7" data-toggle="tab">Projects</a></li>
                                <li><a href="#tab_6" data-toggle="tab">Calendar</a></li>
                                <li><a href="#tab_5" data-toggle="tab">Timeline</a></li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<h2 class="page-header">Information</h2>
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-primary">
                   								<div class="box-header">
                   									<h3 class="box-title">Details</h3>
                   								</div>
                   								<div class="box-body">
                   									<form role="form" name="detailsForm" id="detailsForm" method="post" action="${contextPath}/team/create">
				                                        <div class="form-group">
				                                        	<input type="hidden" name="id" value="${team.id}"/>
				                                            <label>Name</label>
				                                            <input type="text" class="form-control" name="name" value="${team.name}"/><br/>
				                                        </div>
				                                    </form>
				                                    <c:choose>
		                                            	<c:when test="${team.id == 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${team.id > 0}">
		                                            		<button class="btn btn-warning btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<a href="${contextPath}/team/delete/${team.id}">
																<button class="btn btn-danger btn-sm">Delete This Team</button>
															</a>
		                                            	</c:when>
		                                            </c:choose>
                   								</div>
                   							</div>
                   						</div>
                   						<div class="col-md-6">
                   							<div class="box box-primary">
                   								<div class="box-header">
                   									<h3 class="box-title">More Info</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="form-group">
                   										<table>
															<tr>
																<td style="padding-bottom: 3px;">
																	<input type="text" class="form-control" value="BIR Number">
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<input type="text" class="form-control" value="202-123-345-123">
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<button class="btn btn-warning btn-sm">Remove</button>
																</td>
															</tr>
															<tr>
																<td style="padding-bottom: 3px;">
																	<input type="text" class="form-control" value="Manpower">
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<input type="text" class="form-control" value="ABC Services Inc.">
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<button class="btn btn-warning btn-sm">Remove</button>
																</td>
															</tr>
														</table>
														<br/>
														<button class="btn btn-danger btn-sm">Clear All</button>
														<br/>
														<br/>
														<h4>Assign More Fields</h4>
														<table>
															<tr>
																<td style="padding-right: 3px;">
																	<label>Label</label>
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<input type="text" class="form-control" placeholder="Example: SSS, Building Permit No., Sub-contractor, etc...">
																</td>
															</tr>
															<tr>
																<td style="padding-right: 3px;">
																	<label>Value</label>
																</td>
																<td style="padding-bottom: 3px;">
																	&nbsp;
																</td>
																<td style="padding-bottom: 3px;">
																	<input type="text" class="form-control" placeholder="Example: 000-123-456, AEE-123, OneForce Construction, etc...">
																</td>
															</tr>
														</table>
                                            			<button class="btn btn-primary btn-sm">Assign</button>
			                                        </div>
                   								</div>
                   							</div>
                   						</div>
              						</div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_6">
                                	<div class="row">
				                        <div class="col-md-3">
				                            <div class="box box-primary">
				                                <div class="box-header">
				                                    <h4 class="box-title">Draggable Events</h4>
				                                </div>
				                                <div class="box-body">
				                                    <!-- the events -->
				                                    <div id='external-events'>
				                                        <div class='external-event bg-green'>Lunch</div>
				                                        <div class='external-event bg-red'>Go home</div>
				                                        <div class='external-event bg-aqua'>Do homework</div>
				                                        <div class='external-event bg-yellow'>Work on UI design</div>
				                                        <div class='external-event bg-navy'>Sleep tight</div>
				                                        <p>
				                                            <input type='checkbox' id='drop-remove' /> <label for='drop-remove'>remove after drop</label>
				                                        </p>
				                                    </div>
				                                </div><!-- /.box-body -->
				                            </div><!-- /. box -->
				                            <div class="box box-primary">
				                                <div class="box-header">
				                                    <h3 class="box-title">Create Event</h3>
				                                </div>
				                                <div class="box-body">
				                                    <div class="btn-group" style="width: 100%; margin-bottom: 10px;">
				                                        <button type="button" id="color-chooser-btn" class="btn btn-danger btn-block btn-sm dropdown-toggle" data-toggle="dropdown">Color <span class="caret"></span></button>
				                                        <ul class="dropdown-menu" id="color-chooser">
				                                            <li><a class="text-green" href="#"><i class="fa fa-square"></i> Green</a></li>
				                                            <li><a class="text-blue" href="#"><i class="fa fa-square"></i> Blue</a></li>
				                                            <li><a class="text-navy" href="#"><i class="fa fa-square"></i> Navy</a></li>
				                                            <li><a class="text-yellow" href="#"><i class="fa fa-square"></i> Yellow</a></li>
				                                            <li><a class="text-orange" href="#"><i class="fa fa-square"></i> Orange</a></li>
				                                            <li><a class="text-aqua" href="#"><i class="fa fa-square"></i> Aqua</a></li>
				                                            <li><a class="text-red" href="#"><i class="fa fa-square"></i> Red</a></li>
				                                            <li><a class="text-fuchsia" href="#"><i class="fa fa-square"></i> Fuchsia</a></li>
				                                            <li><a class="text-purple" href="#"><i class="fa fa-square"></i> Purple</a></li>
				                                        </ul>
				                                    </div><!-- /btn-group -->
				                                    <div class="input-group">
				                                        <input id="new-event" type="text" class="form-control" placeholder="Event Title">
				                                        <div class="input-group-btn">
				                                            <button id="add-new-event" type="button" class="btn btn-default btn-flat">Add</button>
				                                        </div><!-- /btn-group -->
				                                    </div><!-- /input-group -->
				                                </div>
				                            </div>
				                        </div><!-- /.col -->
				                        <div class="col-md-9">
				                            <div class="box box-primary">
				                                <div class="box-body no-padding">
				                                    <!-- THE CALENDAR -->
				                                    <div id="calendar"></div>
				                                </div><!-- /.box-body -->
				                            </div><!-- /. box -->
				                        </div><!-- /.col -->
				                    </div><!-- /.row -->
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_3">
                                	<div class="box">
		                                <div class="box-header">
		                                	<h3 class="box-title">Members&nbsp;
		                                    <table>
		                                    	<tr>
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/staff/edit/0">
				                                    	<button class="btn btn-default btn-flat btn-sm">Create Staff</button>
					                                    </form>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<form method="post" action="${contextPath}/staff/assign/team">
		                                    		<td>
		                                    			<select class="form-control" name="staff_id">
		                                    				<c:choose>
		                                    					<c:when test="${!empty staffList}">
		                                    						<c:forEach items="${staffList}" var="staff">
		                                    							<c:set var="staffName" value="${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}"/>
		                                    							<option value="${staff.id}">${staffName}</option>
		                                    						</c:forEach>
		                                    					</c:when>
		                                    				</c:choose>
		                                    			</select>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<input type="hidden" name="origin" value="team"/>
		                                    			<input type="hidden" name="originID" value="${team.id}"/>
		                                    			<input type="hidden" name="team_id" value="${team.id}"/>
														<button class="btn btn-primary btn-sm">Assign</button>
		                                    		</td>
		                                    		</form>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/team/unassign/staff/all">
														<input type="hidden" name="team_id" value="${team.id}"/>
														<button class="btn btn-danger btn-sm">Unassign All</button>
														</form>
		                                    		</td>
		                                    	</tr>
		                                    </table>
		                                    </h3>
		                                </div><!-- /.box-header -->
		                                <div class="box-body table-responsive">
		                                    <table id="members-table" class="table table-striped">
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
			                                        <c:set var="assignmentList" value="${team.members}"/>
				                                	<c:if test="${!empty assignmentList}">
				                                		<c:forEach items="${assignmentList}" var="member">
			                                            <tr>
			                                            	<td>
			                                            		<center>
																	<a href="${contextPath}/staff/edit/${member.id}">
																		<button class="btn btn-primary btn-sm">View</button>
																	</a>
																	<form method="post" action="${contextPath}/staff/unassign/team">
																		<input type="hidden" name="staff_id" value="${member.id}"/>
																		<input type="hidden" name="team_id" value="${team.id}"/>
																		<input type="hidden" name="origin" value="team"/>
																		<button class="btn btn-danger btn-sm">Unassign</button>
																	</form>
																</center>
															</td>
			                                                <td>
			                                                	<div class="user-panel">
													            <div class="pull-left image">
													                <c:choose>
		                                                			<c:when test="${!empty member.thumbnailURL}">
		                                                				<img src="${contextPath}/image/display/staff/profile/?staff_id=${member.id}" class="img-circle"/>
		                                                			</c:when>
		                                                			<c:when test="${empty member.thumbnailURL}">
		                                                				<img src="/pmsys/resources/img/avatar5.png" class="img-circle">
		                                                			</c:when>
			                                                		</c:choose>
													            </div>
														        </div>
			                                                </td>
			                                                <td>${member.prefix} ${member.firstName} ${member.middleName} ${member.lastName} ${member.suffix}</td>
			                                                <td>${member.companyPosition}</td>
			                                                <td>${member.email}</td>
			                                                <td>${member.contactNumber}</td>
			                                            </tr>
		                                            </c:forEach>
	                                        		</c:if>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_7">
                                	<div class="box">
		                                <div class="box-header">
		                                	<h3 class="box-title">Assigned Projects&nbsp;
		                                    <table>
		                                    	<tr>
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/project/edit/0">
				                                    	<button class="btn btn-default btn-flat btn-sm">Create Project</button>
					                                    </form>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<form method="post" action="${contextPath}/team/assign/project">
		                                    		<td>
		                                    			<select class="form-control" name="project_id">
		                                    				<c:choose>
		                                    					<c:when test="${!empty projectList}">
		                                    						<c:forEach items="${projectList}" var="project">
		                                    							<option value="${project.id}">${project.name}</option>
		                                    						</c:forEach>
		                                    					</c:when>
		                                    				</c:choose>
		                                    			</select>
		                                    		</td>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<input type="hidden" name="origin" value="team"/>
		                                    			<input type="hidden" name="originID" value="${team.id}"/>
		                                    			<input type="hidden" name="team_id" value="${team.id}"/>
														<button class="btn btn-primary btn-sm">Assign</button>
		                                    		</td>
		                                    		</form>
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
		                                    			<form method="post" action="${contextPath}/team/unassign/all/project">
														<input type="hidden" name="team_id" value="${team.id}"/>
														<button class="btn btn-danger btn-sm">Unassign All</button>
														</form>
		                                    		</td>
		                                    	</tr>
		                                    </table>
		                                    </h3>
		                                </div><!-- /.box-header -->
		                                <div class="box-body no-padding">
		                                    <table id="project-table" class="table table-striped">
		                                    	<thead>
		                                    		<tr>
			                                        	<th>&nbsp;</th>
		                                                <th>Project</th>
		                                                <th>Location</th>
		                                                <th>Notes</th>
			                                        </tr>
		                                    	</thead>
		                                        <tbody>
			                                        <c:set var="assignmentList" value="${team.projects}"/>
				                                	<c:if test="${!empty assignmentList}">
				                                		<c:forEach items="${assignmentList}" var="project">
			                                            <tr>
			                                            	<td>
			                                            		<center>
																	<a href="${contextPath}/project/edit/${member.id}">
																		<button class="btn btn-primary btn-sm">View</button>
																	</a>
																	<form method="post" action="${contextPath}/team/unassign/project">
																		<input type="hidden" name="project_id" value="${project.id}"/>
																		<input type="hidden" name="team_id" value="${team.id}"/>
																		<input type="hidden" name="origin" value="team"/>
																		<input type="hidden" name="originID" value="${team.id}"/>
																		<button class="btn btn-danger btn-sm">Unassign</button>
																	</form>
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
					                                            ${project.name}<br/><br/>
			                                                	<c:choose>
			                                                		<c:when test="${!empty project.thumbnailURL}">
			                                                			<img style="width: 100%" src="${contextPath}/image/display/project/profile/?project_id=${project.id}"/>
			                                                		</c:when>
			                                                		<c:when test="${empty project.thumbnailURL}">
			                                                			<h5>No photo uploaded.</h5>
			                                                		</c:when>
			                                                	</c:choose>
			                                                </td>
			                                                <td>${project.location}</td>
			                                                <td>${project.notes}</td>
			                                            </tr>
		                                            </c:forEach>
	                                        		</c:if>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_2">
                                	<div class="box">
		                                <div class="box-header">
		                                    <h3 class="box-title">Assigned Tasks&nbsp;
		                                    <a href="${contextPath}/task/assign/team/${team.id}">
		                                		<button class="btn btn-default btn-flat btn-sm">Create Task</button>
		                                	</a>
		                                	<a href="${contextPath}/project/edit/0">
		                                		<button class="btn btn-danger btn-sm">Unassign All</button>
		                                	</a>
		                                    </h3>
		                                </div><!-- /.box-header -->
		                                <div class="box-body no-padding">
		                                    <table id="task-table" class="table table-striped">
		                                    	<thead>
		                                    		<tr>
			                                        	<th>&nbsp;</th>
			                                            <th>Status</th>
			                                            <th>Content</th>
			                                            <th>Project</th>
			                                            <th>Start</th>
			                                            <th>End</th>
			                                        </tr>
		                                    	</thead>
		                                        <tbody>
			                                        <c:set var="taskList" value="${team.tasks}"/>
				                                	<c:if test="${!empty taskList}">
		                                        		<c:forEach items="${taskList}" var="task">
		                                        			<tr>
		                                        				<td>
		                                        					<div class="btn-group">
							                                            <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">
							                                                Mark As&nbsp;
							                                                <span class="caret"></span>
							                                            </button>
							                                            <ul class="dropdown-menu">
							                                                <li><a href="${contextPath}/task/mark/team/?team_id=${team.id}&task_id=${task.id}&status=0">New</a></li>
							                                                <li><a href="${contextPath}/task/mark/team/?team_id=${team.id}&task_id=${task.id}&status=1">Ongoing</a></li>
							                                                <li><a href="${contextPath}/task/mark/team/?team_id=${team.id}&task_id=${task.id}&status=2">Completed</a></li>
							                                                <li><a href="${contextPath}/task/mark/team/?team_id=${team.id}&task_id=${task.id}&status=3">Failed</a></li>
							                                                <li><a href="${contextPath}/task/mark/team/?team_id=${team.id}&task_id=${task.id}&status=4">Cancelled</a></li>
							                                            </ul>
							                                        </div>
							                                        <a href="${contextPath}/task/edit/${task.id}">
					                                            		<button class="btn btn-primary btn-sm">View</button>
					                                            	</a>
					                                            	<a href="${contextPath}/task/delete/${task.id}">
					                                            		<button class="btn btn-danger btn-sm">Delete</button>
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
				                                            		<c:when test="${!empty task.project}">
				                                            			<a href="${contextPath}/project/edit/from/team/?${task.project.id}">
						                                            		<button class="btn btn-info btn-sm">View</button>&nbsp;&nbsp;
						                                            	</a>
						                                            	${task.project.name}
						                                            	<br/>
				                                            		</c:when>
				                                            		<c:when test="${empty task.project}">
				                                            			<h5>No project assigned.</h5>
				                                            		</c:when>
					                                            	</c:choose>					                                            
					                                            </td>
					                                            <td>${task.dateStart}</td>
					                                            <td>${task.dateEnd}</td>
					                                        </tr>
		                                        		</c:forEach>
	                                        		</c:if>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_5">
                                    <!-- The time line -->
		                            <ul class="timeline">
		                                <!-- timeline time label -->
		                                <li class="time-label">
		                                    <span class="bg-red">
		                                        10 Feb. 2014
		                                    </span>
		                                </li>
		                                <!-- /.timeline-label -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-envelope bg-blue"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 12:05</span>
		                                        <h3 class="timeline-header"><a href="#">Support Team</a> sent you and email</h3>
		                                        <div class="timeline-body">
		                                            Etsy doostang zoodles disqus groupon greplin oooj voxy zoodles,
		                                            weebly ning heekya handango imeem plugg dopplr jibjab, movity
		                                            jajah plickers sifteo edmodo ifttt zimbra. Babblely odeo kaboodle
		                                            quora plaxo ideeli hulu weebly balihoo...
		                                        </div>
		                                        <div class='timeline-footer'>
		                                            <a class="btn btn-primary btn-xs">Read more</a>
		                                            <a class="btn btn-danger btn-xs">Delete</a>
		                                        </div>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-user bg-aqua"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 5 mins ago</span>
		                                        <h3 class="timeline-header no-border"><a href="#">Sarah Young</a> accepted your friend request</h3>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-comments bg-yellow"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 27 mins ago</span>
		                                        <h3 class="timeline-header"><a href="#">Jay White</a> commented on your post</h3>
		                                        <div class="timeline-body">
		                                            Take me to your leader!
		                                            Switzerland is small and neutral!
		                                            We are more like Germany, ambitious and misunderstood!
		                                        </div>
		                                        <div class='timeline-footer'>
		                                            <a class="btn btn-warning btn-flat btn-xs">View comment</a>
		                                        </div>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <!-- timeline time label -->
		                                <li class="time-label">
		                                    <span class="bg-green">
		                                        3 Jan. 2014
		                                    </span>
		                                </li>
		                                <!-- /.timeline-label -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-camera bg-purple"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 2 days ago</span>
		                                        <h3 class="timeline-header"><a href="#">Mina Lee</a> uploaded new photos</h3>
		                                        <div class="timeline-body">
		                                            <img src="http://placehold.it/150x100" alt="..." class='margin' />
		                                            <img src="http://placehold.it/150x100" alt="..." class='margin'/>
		                                            <img src="http://placehold.it/150x100" alt="..." class='margin'/>
		                                            <img src="http://placehold.it/150x100" alt="..." class='margin'/>
		                                        </div>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-video-camera bg-maroon"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 5 days ago</span>
		                                        <h3 class="timeline-header"><a href="#">Mr. Doe</a> shared a video</h3>
		                                        <div class="timeline-body">
		                                            <iframe width="300" height="169" src="//www.youtube.com/embed/fLe_qO4AE-M" frameborder="0" allowfullscreen></iframe>
		                                        </div>
		                                        <div class="timeline-footer">
		                                            <a href="#" class="btn btn-xs bg-maroon">See comments</a>
		                                        </div>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <li>
		                                    <i class="fa fa-clock-o"></i>
		                                </li>
		                            </ul>
                                </div><!-- /.tab-pane -->
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
	<c:import url="/resources/js-includes.jsp" />
	<script>
		function submitForm(id) {
			$('#'+id).submit();
		}
	
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
	        //hide next button
	        if(total === newNextIndex){
	            $('a.next').hide();
	        }else{
	            $('a.next').show()
	        }            
	        //hide previous button
	        if(newPrevIndex === 0){
	            $('a.previous').hide();
	        }else{
	            $('a.previous').show()
	        }
	        
	        
	        return false;
	    });
		
		$(document).ready(function() {
			$("#members-table").dataTable();
			$("#project-table").dataTable();
			$("#task-table").dataTable();
			
			$('li img').on('click',function(){
                var src = $(this).attr('src');
                var img = '<img src="' + src + '" class="img-responsive"/>';
                
                //start of new code new code
                var index = $(this).parent('li').index();   
                
                var html = '';
                html += img;                
                html += '<div style="height:25px;clear:both;display:block;">';
                html += '<a class="controls next" href="'+ (index+2) + '">next &raquo;</a>';
                html += '<a class="controls previous" href="' + (index) + '">&laquo; prev</a>';
                html += '</div>';
                html += '<button class="btn btn-danger btn-sm">Delete</button>';
                
                $('#myModal').modal();
                $('#myModal').on('shown.bs.modal', function(){
                    $('#myModal .modal-body').html(html);
                    //new code
                    $('a.controls').trigger('click');
                })
                $('#myModal').on('hidden.bs.modal', function(){
                    $('#myModal .modal-body').html('');
                });
                
                
                
                
           });
	    });
	</script>
	<script type="text/javascript">
        $(function() {

            /* initialize the external events
             -----------------------------------------------------------------*/
            function ini_events(ele) {
                ele.each(function() {

                    // create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
                    // it doesn't need to have a start or end
                    var eventObject = {
                        title: $.trim($(this).text()) // use the element's text as the event title
                    };

                    // store the Event Object in the DOM element so we can get to it later
                    $(this).data('eventObject', eventObject);

                    // make the event draggable using jQuery UI
                    $(this).draggable({
                        zIndex: 1070,
                        revert: true, // will cause the event to go back to its
                        revertDuration: 0  //  original position after the drag
                    });

                });
            }
            ini_events($('#external-events div.external-event'));

            /* initialize the calendar
             -----------------------------------------------------------------*/
            //Date for the calendar events (dummy data)
            var date = new Date();
            var d = date.getDate(),
                    m = date.getMonth(),
                    y = date.getFullYear();
            $('#calendar').fullCalendar({
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                buttonText: {
                    today: 'today',
                    month: 'month',
                    week: 'week',
                    day: 'day'
                },
                //Random default events
                events: [
                    {
                        title: 'All Day Event',
                        start: new Date(y, m, 1),
                        backgroundColor: "#f56954", //red
                        borderColor: "#f56954" //red
                    },
                    {
                        title: 'Long Event',
                        start: new Date(y, m, d - 5),
                        end: new Date(y, m, d - 2),
                        backgroundColor: "#f39c12", //yellow
                        borderColor: "#f39c12" //yellow
                    },
                    {
                        title: 'Meeting',
                        start: new Date(y, m, d, 10, 30),
                        allDay: false,
                        backgroundColor: "#0073b7", //Blue
                        borderColor: "#0073b7" //Blue
                    },
                    {
                        title: 'Lunch',
                        start: new Date(y, m, d, 12, 0),
                        end: new Date(y, m, d, 14, 0),
                        allDay: false,
                        backgroundColor: "#00c0ef", //Info (aqua)
                        borderColor: "#00c0ef" //Info (aqua)
                    },
                    {
                        title: 'Birthday Party',
                        start: new Date(y, m, d + 1, 19, 0),
                        end: new Date(y, m, d + 1, 22, 30),
                        allDay: false,
                        backgroundColor: "#00a65a", //Success (green)
                        borderColor: "#00a65a" //Success (green)
                    },
                    {
                        title: 'Click for Google',
                        start: new Date(y, m, 28),
                        end: new Date(y, m, 29),
                        url: 'http://google.com/',
                        backgroundColor: "#3c8dbc", //Primary (light-blue)
                        borderColor: "#3c8dbc" //Primary (light-blue)
                    }
                ],
                editable: true,
                droppable: true, // this allows things to be dropped onto the calendar !!!
                drop: function(date, allDay) { // this function is called when something is dropped

                    // retrieve the dropped element's stored Event Object
                    var originalEventObject = $(this).data('eventObject');

                    // we need to copy it, so that multiple events don't have a reference to the same object
                    var copiedEventObject = $.extend({}, originalEventObject);

                    // assign it the date that was reported
                    copiedEventObject.start = date;
                    copiedEventObject.allDay = allDay;
                    copiedEventObject.backgroundColor = $(this).css("background-color");
                    copiedEventObject.borderColor = $(this).css("border-color");

                    // render the event on the calendar
                    // the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
                    $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

                    // is the "remove after drop" checkbox checked?
                    if ($('#drop-remove').is(':checked')) {
                        // if so, remove the element from the "Draggable Events" list
                        $(this).remove();
                    }

                }
            });

            /* ADDING EVENTS */
            var currColor = "#f56954"; //Red by default
            //Color chooser button
            var colorChooser = $("#color-chooser-btn");
            $("#color-chooser > li > a").click(function(e) {
                e.preventDefault();
                //Save color
                currColor = $(this).css("color");
                //Add color effect to button
                colorChooser
                        .css({"background-color": currColor, "border-color": currColor})
                        .html($(this).text()+' <span class="caret"></span>');
            });
            $("#add-new-event").click(function(e) {
                e.preventDefault();
                //Get value and make sure it is not null
                var val = $("#new-event").val();
                if (val.length == 0) {
                    return;
                }

                //Create events
                var event = $("<div />");
                event.css({"background-color": currColor, "border-color": currColor, "color": "#fff"}).addClass("external-event");
                event.html(val);
                $('#external-events').prepend(event);

                //Add draggable funtionality
                ini_events(event);

                //Remove event from text input
                $("#new-event").val("");
            });
        });
    </script>
</body>
</html>