<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>User ${action}</title>
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
	                ${user.name}
	                <small>${action} User</small>
	            </h1>
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
                   							<div class="box box-default">
                   								<div class="box-header">
                   									<h3 class="box-title">Details</h3>
                   								</div>
                   								<div class="box-body">
                   									<form method="post" name="detailsForm" action="${contextPath}/systemuser/create">
                   										<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				                                        <div class="form-group">
				                                        	<input type="hidden" name="user_id" value="${user.id}"/>
				                                            <label>Username</label>
				                                            <input type="text" class="form-control" name="username" value="${user.username}"/><br/>
				                                            <label>Password</label>
				                                            <input type="password" class="form-control" name="password" value="${user.password}"/><br/>
				                                        </div>
				                                        <button class="btn btn-default btn-flat btn-sm" id="detailsButton">Create</button>
				                                    </form>
				                                    <c:choose>
		                                            	<c:when test="${user.id > 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<a href="${contextPath}/systemuser/delete/${user.id}">
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
	<c:import url="/resources/js-includes.jsp" />
	<script>
		function submitForm(id) {
			$('#'+id).submit();
		}
	</script>
</body>
</html>