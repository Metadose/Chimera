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
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<h2 class="page-header">Information</h2>
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
				                                            <form:select path="companyID" items="${companyList}" itemValue="id" itemLabel="name"/>
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
	</script>
</body>
</html>