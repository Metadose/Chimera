<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<sec:authentication var="authUser" property="user"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>User ${action}</title>
	
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
	                ${systemuser.username}
	                <small>${action} User</small>
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
                                <li><a href="#tab_data_access" data-toggle="tab">Data Access</a></li>
                                <li><a href="#tab_authority" data-toggle="tab">Authority</a></li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<c:choose>
                                		<c:when test="${!empty systemuser.staff}">
	                                	<h2 class="page-header">
	                                	<c:url value="/staff/edit/${systemuser.staff.id}/from/systemuser/${systemuser.id}" var="urlViewStaff"/>
	                                	<a href="${urlViewStaff}">
	                                	<button class="btn btn-default btn-flat btn-sm">View Staff</button>
	                                	</a>
										</h2>
                                		</c:when>
                                		<c:when test="${empty systemuser.staff}">
                                		<h2 class="page-header">
	                                	<c:url value="/staff/edit/0/from/systemuser/${systemuser.id}" var="urlViewStaff"/>
	                                	<a href="${urlViewStaff}">
	                                	<button class="btn btn-default btn-flat btn-sm">Create Staff</button>
	                                	</a>
										</h2>
                                		</c:when>
                                	</c:choose>
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Details</h3>
                   								</div>
                   								<div class="box-body">
                   									<form:form modelAttribute="systemuser" method="post" id="detailsForm" action="${contextPath}/systemuser/create">
				                                        <div class="form-group">
				                                            <label>Username</label>
				                                            <form:input type="text" class="form-control" path="username"/><br/>
				                                            <label>Password</label>
				                                            <form:password class="form-control" path="password"/><br/>
				                                            <label>Re-type Password</label>
				                                            <form:password class="form-control" path="retypePassword"/>
				                                            
				                                            <c:if test="${authUser.superAdmin == true}">
				                                            <br/>
				                                            <label>Super Admin</label>
				                                            <form:checkbox class="form-control" path="superAdmin"/><br/>
				                                            <label>Company Admin</label>
				                                            <form:checkbox class="form-control" path="companyAdmin"/><br/>
				                                            <label>Company</label>
				                                            <form:input type="text" class="form-control" path="companyID"/><br/>
<%-- 				                                            <form:select path="companyID" items="${companyList}" itemValue="id" itemLabel="name"/> --%>
				                                            </c:if>
				                                        </div>
				                                    </form:form>
				                                    <c:choose>
				                                    	<c:when test="${systemuser.id == 0}">
				                                        <button class="btn btn-default btn-flat btn-sm" onclick="submitForm('detailsForm')" id="detailsButton">Create</button>
				                                    	</c:when>
		                                            	<c:when test="${systemuser.id > 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<a href="${contextPath}/systemuser/delete/${systemuser.id}">
																<button class="btn btn-default btn-flat btn-sm">Delete This User</button>
															</a>
		                                            	</c:when>
		                                            </c:choose>
                   								</div>
                   							</div>
                   						</div>
              						</div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_data_access">
                                	<div class="box box-default">
		                                <div class="box-body table-responsive">
		                                	<table>
		                                    	<tr>
<%-- 		                                    		<sec:authorize access="hasRole('ROLE_STAFF_EDITOR')"> --%>
<!-- 		                                    		<td> -->
<%-- 		                                    			<c:url var="urlCreateStaff" value="/staff/edit/0/from/project/${project.id}"/> --%>
<%-- 		                                    			<a href="${urlCreateStaff}"> --%>
<!-- 				                                    	<button class="btn btn-default btn-flat btn-sm">Create Staff</button> -->
<!-- 		                                    			</a> -->
<!-- 		                                    		</td> -->
<!-- 		                                    		<td> -->
<!-- 		                                    			&nbsp; -->
<!-- 		                                    		</td> -->
<%-- 		                                    		</sec:authorize> --%>
 		                                    		<form:form 
 		                                    		modelAttribute="securityaccess"  
 		                                    		method="post" 
 		                                    		action="${contextPath}/systemuser/assign/securityaccess"> 
 		                                    			<td>
 		                                    			<form:select class="form-control" path="securityAccessID" items="${accessList}" itemLabel="label" itemValue="id"/>
<%--  		                                    			<form:select class="form-control" path="securityAccessID"> --%>
<%--                                      						<c:forEach items="${accessList}" var="access">   --%>
<%--                                      							<form:option value="${access.id}" label="${access.name}"/>  --%>
<%--                                      						</c:forEach>  --%>
<%--  		                                    			</form:select>  --%>
 		                                    			</td>
 		                                    			<td>
			                                    			&nbsp;
			                                    		</td>
 														<td>
 														<button class="btn btn-default btn-flat btn-sm">Assign</button>
 		                                    			</td> 
 		                                    		</form:form> 
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
               											<c:url var="urlUnassignAllSecAccess" value="/systemuser/unassign/securityaccess/all"/>
		                                    			<a href="${urlUnassignAllSecAccess}">
                											<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
		                                    			</a>
		                                    		</td>
		                                    	</tr>
		                                    </table>
		                                    <br/>
		                                    <table id="dataaccess-table" class="table table-bordered table-striped">
		                                    	<thead>
		                                            <tr>
		                                            	<th>&nbsp;</th>
		                                                <th>ID</th>
		                                                <th>Name</th>
		                                            </tr>
                                        		</thead>
		                                        <tbody>
			                                		<c:forEach items="${systemuser.securityAccess}" var="access">
			                                            <tr>
			                                            	<td>
			                                            		<center>
<%-- 			                                            			<c:url var="urlViewStaff" value="/staff/edit/${manager.id}/from/project/${project.id}" /> --%>
<%-- 			                                            			<a href="${urlViewStaff}"> --%>
<!-- 							                                    	<button class="btn btn-default btn-flat btn-sm">View</button> -->
<!-- 			                                            			</a> -->
	                   												<c:url var="urlUnassignUserAccess" value="/systemuser/unassign/securityaccess/${access.id}"/>
	                   												<a href="${urlUnassignUserAccess}">
																		<button class="btn btn-default btn-flat btn-sm">Unassign</button>
	                   												</a>
																</center>
															</td>
			                                                <td>${access.id}</td>
			                                                <td>${access.label}</td>
			                                            </tr>
		                                            </c:forEach>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
		                            </div>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_authority">
                                	<div class="box box-default">
		                                <div class="box-body table-responsive">
		                                	<table>
		                                    	<tr>
<%-- 		                                    		<sec:authorize access="hasRole('ROLE_STAFF_EDITOR')"> --%>
<!-- 		                                    		<td> -->
<%-- 		                                    			<c:url var="urlCreateStaff" value="/staff/edit/0/from/project/${project.id}"/> --%>
<%-- 		                                    			<a href="${urlCreateStaff}"> --%>
<!-- 				                                    	<button class="btn btn-default btn-flat btn-sm">Create Staff</button> -->
<!-- 		                                    			</a> -->
<!-- 		                                    		</td> -->
<!-- 		                                    		<td> -->
<!-- 		                                    			&nbsp; -->
<!-- 		                                    		</td> -->
<%-- 		                                    		</sec:authorize> --%>
 		                                    		<form:form 
 		                                    		modelAttribute="securityrole"  
 		                                    		method="post" 
 		                                    		action="${contextPath}/systemuser/assign/securityrole"> 
 		                                    			<td>
 		                                    			<form:select class="form-control" path="securityRoleID" items="${roleList}" itemLabel="label" itemValue="id"/>
 		                                    			</td>
 		                                    			<td>
			                                    			&nbsp;
			                                    		</td>
 														<td>
 														<button class="btn btn-default btn-flat btn-sm">Assign</button>
 		                                    			</td> 
 		                                    		</form:form> 
		                                    		<td>
		                                    			&nbsp;
		                                    		</td>
		                                    		<td>
               											<c:url var="urlUnassignAllSecRole" value="/systemuser/unassign/securityrole/all"/>
		                                    			<a href="${urlUnassignAllSecRole}">
                											<button class="btn btn-default btn-flat btn-sm">Unassign All</button>
		                                    			</a>
		                                    		</td>
		                                    	</tr>
		                                    </table>
		                                    <br/>
		                                    <table id="authority-table" class="table table-bordered table-striped">
		                                    	<thead>
		                                            <tr>
		                                            	<th>&nbsp;</th>
		                                                <th>ID</th>
		                                                <th>Name</th>
		                                            </tr>
                                        		</thead>
		                                        <tbody>
			                                		<c:forEach items="${systemuser.securityRoles}" var="role">
			                                            <tr>
			                                            	<td>
			                                            		<center>
	                   												<c:url var="urlUnassignUserRole" value="/systemuser/unassign/securityrole/${role.id}"/>
	                   												<a href="${urlUnassignUserRole}">
																		<button class="btn btn-default btn-flat btn-sm">Unassign</button>
	                   												</a>
																</center>
															</td>
			                                                <td>${role.id}</td>
			                                                <td>${role.label}</td>
			                                            </tr>
		                                            </c:forEach>
			                                    </tbody>
			                                </table>
		                                </div><!-- /.box-body -->
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
			$("#dataaccess-table").dataTable();
			$("#authority-table").dataTable();
	    });
	</script>
</body>
</html>