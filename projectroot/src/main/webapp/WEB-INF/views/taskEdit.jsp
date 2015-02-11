<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Task ${action}</title>
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
	                ${task.content}
	                <small>${action} Task</small>
	            </h1>
	            <ol class="breadcrumb">
	                <li><a href="${contextPath}/dashboard/">Home</a></li>
	                <li><a href="${contextPath}/task/list">Task</a></li>
	                <li class="active">${task.content}</li>
	            </ol>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
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
                   									<form role="form" name="detailsForm" id="detailsForm" method="post" action="${contextPath}/task/create">
				                                        <div class="form-group">
				                                        	<input type="hidden" name="id" value="${task.id}"/>
				                                            <label>Content</label>
				                                            <input type="text" class="form-control" name="content" value="${task.content}"/><br/>
				                                        </div>
				                                    </form>
				                                    <c:choose>
		                                            	<c:when test="${task.id == 0}">
		                                            		<button class="btn btn-success btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${task.id > 0}">
		                                            		<button class="btn btn-warning btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<a href="${contextPath}/task/delete/${task.id}">
																<button class="btn btn-danger btn-sm">Delete This Task</button>
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
              						<h2 class="page-header">Assignments</h2>
              						<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-primary">
                   								<div class="box-header">
                   									<h3 class="box-title">Tasks</h3>
                   								</div>
                   								<div class="box-body">
                   									<table>
                   										<tr style="padding-bottom: 5px">
                   											<td>
                   												<div class="user-panel">
                   													<div class="pull-left info">
														                <p>Costing 1</p>
														                <h6>Maya Villanueva</h6>
														                <h6>(+63) 922 062 2345</h6>
														                <h6>5 Members</h6>
														            </div>
                   												</div>
                   											</td>
                   											<td style="padding-right: 50px">
                   												&nbsp;
                   											</td>
                   											<td>
                   												<button class="btn btn-warning btn-sm" style="padding: 3px; margin-bottom: 3px">Unassign</button>
                   												<button class="btn btn-info btn-sm" style="padding: 3px; margin-bottom: 3px">View Task</button>
                   											</td>
                   										</tr>
                   										<tr>
                   											<td>
                   												<div class="user-panel">
                   													<div class="pull-left info">
														                <p>Building Task</p>
														                <h6>Lennin Cruz</h6>
														                <h6>(+63) 922 062 2345</h6>
														                <h6>15 Members</h6>
														            </div>
                   												</div>
                   											</td>
                   											<td style="padding-right: 50px">
                   												&nbsp;
                   											</td>
                   											<td>
                   												<button class="btn btn-warning btn-sm" style="padding: 3px; margin-bottom: 3px">Unassign</button>
                   												<button class="btn btn-info btn-sm" style="padding: 3px; margin-bottom: 3px">View Task</button>
                   											</td>
                   										</tr>
                   									</table>
                   									<button class="btn btn-danger btn-sm">Clear All</button>
													<br/>
													<br/>
													<h4>Assign More Tasks</h4>
													<table>
														<tr>
															<td style="padding-right: 3px;">
																<label>Tasks </label>
															</td>
															<td style="padding-bottom: 3px;">
																&nbsp;
															</td>
															<td style="padding-bottom: 3px;">
																<select class="form-control">
					                                                <option>Banilad Builders</option>
					                                                <option>Manpower 1</option>
					                                                <option>Costing Task</option>
					                                            </select>
															</td>
														</tr>
													</table>
                                           			<button class="btn btn-primary btn-sm">Assign</button>
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
                                <div class="tab-pane" id="tab_7">
                                    <!-- TO DO List -->
		                            <div class="box">
		                                <div class="box-header">
		                                    <h3 class="box-title">Projects</h3>
		                                    <div class="box-tools pull-right">
		                                        <ul class="pagination pagination-sm inline">
		                                            <li><a href="#">&laquo;</a></li>
		                                            <li><a href="#">1</a></li>
		                                            <li><a href="#">2</a></li>
		                                            <li><a href="#">3</a></li>
		                                            <li><a href="#">&raquo;</a></li>
		                                        </ul>
		                                    </div>
		                                </div><!-- /.box-header -->
		                                <div class="box-body">
		                                    <ul class="todo-list">
		                                        <li>
		                                            <!-- drag handle -->
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <!-- checkbox -->
		                                            <input type="checkbox" value="" name=""/>
		                                            <!-- todo text -->
		                                            <span class="text">Design a nice theme</span>
		                                            <!-- Emphasis label -->
		                                            <small class="label label-danger"><i class="fa fa-clock-o"></i> 2 mins</small>
		                                            <!-- General tools such as edit or delete-->
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Make the theme responsive</span>
		                                            <small class="label label-info"><i class="fa fa-clock-o"></i> 4 hours</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Let theme shine like a star</span>
		                                            <small class="label label-warning"><i class="fa fa-clock-o"></i> 1 day</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Let theme shine like a star</span>
		                                            <small class="label label-success"><i class="fa fa-clock-o"></i> 3 days</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Check your messages and notifications</span>
		                                            <small class="label label-primary"><i class="fa fa-clock-o"></i> 1 week</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Let theme shine like a star</span>
		                                            <small class="label label-default"><i class="fa fa-clock-o"></i> 1 month</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                    </ul>
		                                </div><!-- /.box-body -->
		                                <div class="box-footer clearfix no-border">
		                                    <button class="btn btn-default pull-right"><i class="fa fa-plus"></i> Add item</button>
		                                </div>
		                            </div><!-- /.box -->
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_2">
                                    <!-- TO DO List -->
		                            <div class="box">
		                                <div class="box-header">
		                                    <h3 class="box-title">Assigned Tasks</h3>
		                                    <div class="box-tools pull-right">
		                                        <ul class="pagination pagination-sm inline">
		                                            <li><a href="#">&laquo;</a></li>
		                                            <li><a href="#">1</a></li>
		                                            <li><a href="#">2</a></li>
		                                            <li><a href="#">3</a></li>
		                                            <li><a href="#">&raquo;</a></li>
		                                        </ul>
		                                    </div>
		                                </div><!-- /.box-header -->
		                                <div class="box-body">
		                                    <ul class="todo-list">
		                                        <li>
		                                            <!-- drag handle -->
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <!-- checkbox -->
		                                            <input type="checkbox" value="" name=""/>
		                                            <!-- todo text -->
		                                            <span class="text">Design a nice theme</span>
		                                            <!-- Emphasis label -->
		                                            <small class="label label-danger"><i class="fa fa-clock-o"></i> 2 mins</small>
		                                            <!-- General tools such as edit or delete-->
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Make the theme responsive</span>
		                                            <small class="label label-info"><i class="fa fa-clock-o"></i> 4 hours</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Let theme shine like a star</span>
		                                            <small class="label label-warning"><i class="fa fa-clock-o"></i> 1 day</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Let theme shine like a star</span>
		                                            <small class="label label-success"><i class="fa fa-clock-o"></i> 3 days</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Check your messages and notifications</span>
		                                            <small class="label label-primary"><i class="fa fa-clock-o"></i> 1 week</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Let theme shine like a star</span>
		                                            <small class="label label-default"><i class="fa fa-clock-o"></i> 1 month</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                    </ul>
		                                </div><!-- /.box-body -->
		                                <div class="box-footer clearfix no-border">
		                                    <button class="btn btn-default pull-right"><i class="fa fa-plus"></i> Add item</button>
		                                </div>
		                            </div><!-- /.box -->
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_3">
                                    <div class="box-body table-responsive">
                                    	<div class="form-group">
	                                        <label for="exampleInputFile">File Upload</label>
	                                        <input type="file" id="exampleInputFile">
	                                        <p class="help-block">Upload a file</p>
	                                    </div>
	                                    <br/>
	                                    <table id="example-1" class="table table-bordered table-striped">
	                                        <thead>
	                                            <tr>
	                                            	<th>&nbsp;</th>
	                                                <th>Rendering engine</th>
	                                                <th>Browser</th>
	                                                <th>Platform(s)</th>
	                                                <th>Engine version</th>
	                                                <th>CSS grade</th>
	                                            </tr>
	                                        </thead>
	                                        <tbody>
	                                            <tr>
	                                            	<td>
	                                            		<center>
														<button class="btn btn-primary btn-sm">Download</button>
														<button class="btn btn-danger btn-sm">Delete</button>
														</center>
													</td>
	                                                <td>Trident</td>
	                                                <td>Internet
	                                                    Explorer 4.0</td>
	                                                <td>Win 95+</td>
	                                                <td> 4</td>
	                                                <td>X</td>
	                                            </tr>
	                                            <tr>
	                                            	<td>
	                                            		<center>
														<button class="btn btn-primary btn-sm">Download</button>
														<button class="btn btn-danger btn-sm">Delete</button>
														</center>
													</td>
	                                                <td>Trident</td>
	                                                <td>Internet
	                                                    Explorer 5.0</td>
	                                                <td>Win 95+</td>
	                                                <td>5</td>
	                                                <td>C</td>
	                                            </tr>
	                                        </tbody>
	                                        <tfoot>
	                                            <tr>
	                                            	<th>&nbsp;</th>
	                                                <th>Rendering engine</th>
	                                                <th>Browser</th>
	                                                <th>Platform(s)</th>
	                                                <th>Engine version</th>
	                                                <th>CSS grade</th>
	                                            </tr>
	                                        </tfoot>
	                                    </table>
	                                </div><!-- /.box-body -->
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_4">
                                    <div class="form-group">
                                        <label for="exampleInputFile">Upload Photo</label>
                                        <input type="file" id="exampleInputFile"><br/>
                                            <label>Title</label>
                                            <input type="text" class="form-control" placeholder="Enter ..."/><br/>
                                            <label>Description</label>
                                            <input type="text" class="form-control" placeholder="Enter ..."/><br/>
										<button class="btn btn-primary btn-sm">Upload</button>
                                    </div>
                                    <br/>
                                    <div class="box">
                                    	 <br/>
									     <ul class="row">
									          <li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
									          	<img src="<c:url value="/temp/1.jpg"/> "/>
									          </li>
									          <li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
									          	<img src="<c:url value="/temp/1 (1).jpg"/> "/>
									          </li>
									          <li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
									          	<img src="<c:url value="/temp/1 (2).jpg"/> "/>
									          </li>
									          <li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
									          	<img src="<c:url value="/temp/1 (3).jpg"/> "/>
									          </li>
									          <li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
									          	<img src="<c:url value="/temp/1 (4).jpg"/> "/>
									          </li>
									          <li class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
									          	<img src="<c:url value="/temp/1 (5).jpg"/> "/>
									          </li>
									     </ul>
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
		                                        <h3 class="timeline-header"><a href="#">Support Task</a> sent you and email</h3>
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
			$("#example-1").dataTable();
			
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