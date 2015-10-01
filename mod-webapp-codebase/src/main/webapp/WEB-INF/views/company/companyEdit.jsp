<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<sec:authentication var="authUser" property="user"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<c:choose>
		<c:when test="${company.id == 0}">
		<title>Create New Company</title>
		</c:when>
		<c:when test="${company.id > 0}">
		<title>${company.name} | Edit Company</title>
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
		<!--  -->
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	            	<c:choose>
                    	<c:when test="${company.id == 0}">
                    	New Company
		                <small>Create Company</small>
                    	</c:when>
                    	<c:when test="${company.id > 0}">
		                ${company.name}
		                <small>Edit Company</small>
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
				                                            <form:input type="text" class="form-control" placeholder="Sample: ABC Construction, XYZ Builders, City Contractors" path="name"/>
				                                            <p class="help-block">Enter the name of this company</p>

				                                            <label>Description</label>
				                                            <form:input type="text" class="form-control" placeholder="Sample: A construction company specialized in real estates" path="description"/>
				                                            <p class="help-block">Enter a company description</p>

															<c:if test="${authUser.superAdmin}">
				                                                <label>Date Started</label>
				                                                <div class='input-group date date-picker'>
				                                                	<fmt:formatDate value="${company.dateStarted}" var="dateString" pattern="yyyy/MM/dd" />
										                            <form:input type="text" class="form-control" placeholder="Sample: 2016/06/25" path="dateStarted" value="${dateString}"/>
												                    <span class="input-group-addon">
												                        <span class="glyphicon glyphicon-calendar"></span>
												                    </span>
												                </div>
					                                            <p class="help-block">Choose the company start date</p>
	
				                                                <label>Date Expiration</label>
				                                                <div class='input-group date date-picker'>
				                                                	<fmt:formatDate value="${company.dateExpiration}" var="dateString" pattern="yyyy/MM/dd" />
				                                                	<form:input type="text" class="form-control" placeholder="Sample: 2016/12/25" path="dateExpiration" value="${dateString}"/>
												                    <span class="input-group-addon">
												                        <span class="glyphicon glyphicon-calendar"></span>
												                    </span>
												                </div>
					                                            <p class="help-block">Choose the company expiration date</p>
	
					                                            <label>Beta Tester</label><br/>
					                                            <form:checkbox class="form-control" path="betaTester" style="margin-left: -48%;"/>
					                                            <p class="help-block">Is this company a beta tester?</p>
															</c:if>
															
				                                        </div>
				                                    </form:form>
				                                    <c:choose>
				                                    	<c:when test="${company.id == 0}">
				                                        	<button class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton" onclick="submitForm('companyForm')">Create</button>
				                                    	</c:when>
		                                            	<c:when test="${company.id > 0}">
		                                            		<button class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton" onclick="submitForm('companyForm')">Update</button>
															
															<c:if test="${authUser.superAdmin}">
	                                                            <div class="btn-group">
	                                                            <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
	                                                            <ul class="dropdown-menu">
	                                                                <li>
	                                                                    <c:url value="/company/delete/${company.id}" var="urlDeleteCompany"/>
			                                            				<a href="${urlDeleteCompany}" class="cebedo-dropdown-hover">
	                                                                        Confirm Delete
	                                                                    </a>
	                                                                </li>
	                                                            </ul>
	                                                            </div>
															</c:if>
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
			$('.date-picker').datepicker({
			    format: 'yyyy/mm/dd'
			})
	    });
	</script>
</body>
</html>