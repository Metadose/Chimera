<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<sec:authentication var="authUser" property="user"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<c:choose>
    	<c:when test="${systemuser.id == 0}">
    	<title>Create User</title>
    	</c:when>
    	<c:when test="${systemuser.id > 0}">
		<title>Update User</title>
    	</c:when>
    </c:choose>
	
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

	<!-- Modal -->
	<div id="deleteModal" class="modal fade" role="dialog">
		<!-- Modal content-->
		<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">Confirmation</h4>
			</div>
			<div class="modal-body">
				<p>Do you really want to delete this user?</p>
			</div>
			<div class="modal-footer">
				<c:url value="/systemuser/delete/${systemuser.id}" var="urlDeleteUser"/>
        		<a href="${urlDeleteUser}">
					<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
				</a>
				<button type="button" class="btn btn-default btn-flat btn-sm" data-dismiss="modal">Close</button>
			</div>
		</div>
		</div>
	</div>

		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	            	<c:choose>
                    	<c:when test="${systemuser.id == 0}">
                    	New User
                    	<small>Create User</small>
                    	</c:when>
                    	<c:when test="${systemuser.id > 0}">
	                	${systemuser.username}
	                	<small>Update User</small>
                    	</c:when>
                    </c:choose>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-md-12">
                    	${uiParamAlert}
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs" id="myTab">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
                   									<form:form modelAttribute="systemuser" method="post" id="detailsForm" action="${contextPath}/systemuser/create">
				                                        <div class="form-group">
				                                            
				                                            <label>Username</label>
				                                            <c:choose>
						                                    	<c:when test="${systemuser.id == 0}">
						                                        <form:input type="text" placeholder="Sample: user_john, jane_account, etc." class="form-control" path="username"/>
				                                            	<p class="help-block">Enter the username of this account</p>
						                                    	</c:when>
				                                            	<c:when test="${systemuser.id > 0}">
				                                            	<br/>
				                                            	${systemuser.username}
				                                            	<p class="help-block">Username of this account</p>
				                                            	</c:when>
				                                            </c:choose>

				                                            <label>Password</label>
				                                            <form:password class="form-control" path="password"/>
				                                            <p class="help-block">Type the password</p>

				                                            <label>Re-type Password</label>
				                                            <form:password class="form-control" path="retypePassword"/>
				                                            <p class="help-block">Verify the password</p>
				                                            
				                                            <c:if test="${authUser.superAdmin == true}">
				                                            <label>Super Admin</label><br/>
				                                            <form:checkbox class="form-control" path="superAdmin"/>
				                                            <p class="help-block">Is this user a super admin?</p>

				                                            <label>Company Admin</label><br/>
				                                            <form:checkbox class="form-control" path="companyAdmin"/>
				                                            <p class="help-block">Is this user a company admin?</p>

				                                            <label>Company</label>
				                                            <form:select path="companyID" class="form-control" items="${companyList}" itemValue="id" itemLabel="name"/>
				                                            <p class="help-block">Select the company of this user</p>
				                                            </c:if>
				                                        </div>
				                                    </form:form>
				                                    <c:choose>
				                                    	<c:when test="${systemuser.id == 0}">
				                                        	<button class="btn btn-cebedo-create btn-flat btn-sm" onclick="submitForm('detailsForm')" id="detailsButton">Create</button>
				                                    	</c:when>

		                                            	<c:when test="${systemuser.id > 0}">
		                                            		<button class="btn btn-cebedo-update btn-flat btn-sm" onclick="submitForm('detailsForm')" id="detailsButton">Update</button>
															<button class="btn btn-cebedo-delete btn-flat btn-sm" data-toggle="modal" data-target="#deleteModal">Delete This User</button>
		                                            	</c:when>
		                                            </c:choose>

				                                	<c:choose>
				                                		<c:when test="${systemuser.id > 0 && !empty systemuser.staff}">
					                                	<c:url value="/staff/edit/${systemuser.staff.id}" var="urlViewStaff"/>
					                                	<a href="${urlViewStaff}">
					                                		<button class="btn btn-cebedo-view btn-flat btn-sm">View Staff</button>
					                                	</a>
				                                		</c:when>

				                                		<c:when test="${systemuser.id > 0 && empty systemuser.staff}">
					                                	<c:url value="/staff/edit/0/from/systemuser/${systemuser.id}" var="urlViewStaff"/>
					                                	<a href="${urlViewStaff}">
					                                		<button class="btn btn-cebedo-create btn-flat btn-sm">Create Staff</button>
					                                	</a>
				                                		</c:when>
				                                	</c:choose>

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
			$("#dataaccess-table").dataTable();
			$("#authority-table").dataTable();
	    });
	</script>
</body>
</html>