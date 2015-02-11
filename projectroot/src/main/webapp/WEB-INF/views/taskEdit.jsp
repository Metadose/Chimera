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
				                                        	<label>Status</label>
				                                            <input type="text" class="form-control" name="status" value="${task.status}"/><br/>
				                                            <label>Content</label>
				                                            <input type="text" class="form-control" name="content" value="${task.content}"/><br/>
				                                            
				                                            <label>Start</label>
					                                        <div class="input-group">
					                                            <div class="input-group-addon">
					                                                <i class="fa fa-calendar"></i>
					                                            </div>
					                                            <input type="text" id="date-mask" class="form-control" name="dateStart" data-inputmask="'alias': 'yyyy/mm/dd'" data-mask" value="${task.dateStart}"/>
					                                        </div>
					                                        <label>End</label>
					                                        <div class="input-group">
					                                            <div class="input-group-addon">
					                                                <i class="fa fa-calendar"></i>
					                                            </div>
					                                            <input type="text" id="date-mask2" class="form-control" name="dateEnd" data-inputmask="'alias': 'yyyy/mm/dd'" data-mask value="${task.dateEnd}"/>
					                                        </div>
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
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
	<c:import url="/resources/js-includes.jsp" />
	<!-- InputMask -->
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.date.extensions.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/plugins/input-mask/jquery.inputmask.extensions.js" type="text/javascript"></script>
	
	<script>
		function submitForm(id) {
			$('#'+id).submit();
		}
	
		$(document).ready(function() {
			$("#date-mask").inputmask("yyyy/mm/dd", {"placeholder": "yyyy/mm/dd"});
			$("#date-mask2").inputmask("yyyy/mm/dd", {"placeholder": "yyyy/mm/dd"});
	    });
	</script>
</body>
</html>