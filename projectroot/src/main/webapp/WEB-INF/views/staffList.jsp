<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Staff ${action}</title>
	<c:import url="/resources/css-includes.jsp" />
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                Staff ${action}
	                <small>Complete list of all staff members</small>
	            </h1>
	        </section>  
	        <section class="content"> 
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
																	<button class="btn btn-primary btn-sm">View</button>
																</a>
																<a href="${contextPath}/staff/delete/${staff.id}">
																	<button class="btn btn-danger btn-sm">Delete</button>
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
            </section><!-- /.content -->
        </aside>
	</div>
	<c:import url="/resources/js-includes.jsp" />
	<script>
		$(document).ready(function() {
			$("#example-1").dataTable();
	    });
	</script>
</body>
</html>