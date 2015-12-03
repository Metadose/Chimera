<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>List Users</title>
	
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<!--  -->
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                List Users
	                <small>Complete list of all system users</small>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-md-12">
                    	${uiParamAlert}
                        <div class="box">
                                <div class="box-header">
<!--                                     <h3 class="box-title">Data Table With Full Features</h3> -->
                                </div><!-- /.box-header -->
                                <div class="box-body">
                                	<c:url value="/systemuser/edit/0" var="urlCreateUser"/>
                                	<a href="${urlCreateUser}">
                                		<button class="btn btn-cebedo-create btn-flat btn-sm">Create User</button>
                                	</a>
                                	<br/><br/>
                                    <table id="example-1" class="table table-bordered table-striped">
                                        <thead>
                                            <tr>
                                            	<th>&nbsp;</th>
                                                <th>Username</th>
                                                <th>Staff</th>
                                                <th>Company</th>
                                                <th>Authorization</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:if test="${!empty systemUserList}">
                                        		<c:forEach items="${systemUserList}" var="systemUser">
		                                            <tr>
		                                            	<td>
		                                            		<center>
		                                            			<c:url value="/systemuser/edit/${systemUser.id}" var="urlViewUser"/>
																<a href="${urlViewUser}">
																	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
																</a>
                                                                
                                                                <div class="btn-group">
                                                                <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
                                                                <ul class="dropdown-menu">
                                                                    <li>
                                                                        <a href="<c:url value="/systemuser/delete/${systemUser.id}"/>" class="cebedo-dropdown-hover">
                                                                            Confirm Delete
                                                                        </a>
                                                                    </li>
                                                                </ul>
                                                                </div>
															</center>
														</td>
		                                                <td>${systemUser.username}</td>
		                                                <td>
                                                            <a href="<c:url value="/staff/edit/${systemUser.staff.id}"/>" class="general-link">
                                                            ${systemUser.staff.getFullName()}
                                                            </a>
                                                        </td>
		                                                <td>${systemUser.company.name}</td>
		                                                <td>
		                                                <c:if test="${systemUser.companyAdmin}">
		                                                	<b>Company Administrator</b>
		                                                </c:if>
		                                                <c:if test="${systemUser.superAdmin}">
		                                                	<b>System Administrator</b>
		                                                </c:if>
		                                                
		                                                <c:forEach items="${systemUser.userAux.authorization}" var="authEntry">
															<c:set value="${authEntry.key}" var="authModule"/>
															<c:set value="${authEntry.value}" var="authActions"/>
																<b>${authModule.getLabel()}</b>
																(
																<c:forEach items="${authActions}" var="authAction" varStatus="loop">
																${authAction.getLabel()}${!loop.last ? ', ' : ''}
																</c:forEach>
																)
															<br/>
														</c:forEach>
		                                                
		                                                </td>
		                                            </tr>
	                                            </c:forEach>
                                            </c:if>
                                        </tbody>
                                        <tfoot>
                                            <tr>
                                            	<th>&nbsp;</th>
                                                <th>Name</th>
                                                <th>Staff</th>
                                                <th>Company</th>
                                                <th>Authorization</th>
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
	
	<script>
		$(document).ready(function() {
			$("#example-1").dataTable();
	    });
	</script>
</body>
</html>