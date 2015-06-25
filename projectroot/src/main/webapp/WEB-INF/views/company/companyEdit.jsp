<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Company ${action}</title>
	
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
	                ${company.name}
	                <small>${action} Company</small>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-md-12">
                    	${uiParamAlert}
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-body box-default">
                   								<div class="box-body">
                   									<form:form modelAttribute="company" id="companyForm" role="form" method="post" action="${contextPath}/company/create/">
				                                        <div class="form-group">
				                                            <label>Name</label>
				                                            <form:input type="text" class="form-control" path="name"/><br/>
				                                            <label>Description</label>
				                                            <form:input type="text" class="form-control" path="description"/><br/>
			                                                <label>Date Started</label>
			                                                <form:input type="text" class="form-control" path="dateStarted"/><br/>
			                                                <label>Date Expiration</label>
			                                                <form:input type="text" class="form-control" path="dateExpiration"/><br/>
				                                        </div>
				                                    </form:form>
				                                    <c:choose>
				                                    	<c:when test="${company.id == 0}">
				                                        	<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('companyForm')">Create</button>
				                                    	</c:when>
		                                            	<c:when test="${company.id > 0}">
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('companyForm')">Update</button>
		                                            		<c:url value="/company/delete/${company.id}" var="urlDeleteCompany"/>
		                                            		<a href="${urlDeleteCompany}">
																<button class="btn btn-default btn-flat btn-sm">Delete This Company</button>
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
// 			$("#members-table").dataTable();
// 			$("#project-table").dataTable();
// 			$("#task-table").dataTable();
	    });
	</script>
</body>
</html>