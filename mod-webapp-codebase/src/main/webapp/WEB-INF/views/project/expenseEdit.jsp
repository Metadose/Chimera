<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>${expense.name} | Edit Expense</title>
	
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
	            	<c:out value="${expense.name}"></c:out>
	                <small>Edit Expense</small>
	            </h1>
	        </section>
	        <section class="content">
	        	<c:url var="urlBack" value="/project/edit/${project.id}" />
                   <a href="${urlBack}">
					<button class="btn btn-cebedo-back btn-flat btn-sm">Back to Project</button>
				</a><br/><br/>
                <div class="row">
                    <div class="col-md-12">
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                            </ul>
                            <div class="tab-content">
                            	<div class="tab-pane active" id="tab_1">
									<div class="row">
										<div class="col-md-6">
											<div class="box box-default">
												<div class="box-body">
													<div class="form-group">
	                   									<form:form 
	                   										modelAttribute="expense"
															id="expenseForm"
															method="post"
															action="${contextPath}/project/create/expense">
					                                        <div class="form-group">

					                                            <label>Name</label>
					                                            <form:input type="text" class="form-control" path="name"
					                                            	placeholder="Sample: Signage, Legal papers, Consultants"/>
					                                            <p class="help-block">Enter the name of this expense</p>

					                                            <label>Cost</label>
					                                            <form:input type="text" class="form-control" path="cost"
					                                            	placeholder="Sample: 350, 600, 700, 800, 950"/>
					                                            <p class="help-block">Enter the cost of the expense</p>

					                                            <label>Staff</label>
					                                            <form:select class="form-control" path="staffID"> 
		                                     						<c:forEach items="${project.assignedStaff}" var="staff"> 
		                                     							<form:option value="${staff.id}" label="${staff.getFullName()}"/> 
		                                     						</c:forEach> 
		 		                                    			</form:select> 
		 		                                    			<p class="help-block">Choose the staff who conducted the expenditure</p>

					                                            <label>Date</label>
						                                        <div class="input-group">
						                                            <div class="input-group-addon">
						                                                <i class="fa fa-calendar"></i>
						                                            </div>
						                                            <fmt:formatDate value="${expense.date}" var="dateString" pattern="yyyy/MM/dd" />
						                                            <form:input type="text" class="form-control date-picker" path="date" placeholder="Sample: 2016/06/25" value="${dateString}"/>
						                                        </div>
					                                            <p class="help-block">Enter the date when the expense happened</p>

					                                        </div>
					                                    </form:form>
	                                            		<button class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton" onclick="submitForm('expenseForm')">Update</button>
	                                            		
	                                            		<sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'OTHER_EXPENSES_DELETE')">
					                                    <div class="btn-group">
					                                    <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
					                                    <ul class="dropdown-menu">
					                                    	<li>
					                                    		<a href="<c:url value="/project/delete/expense/${expense.getKey()}-end"/>" class="cebedo-dropdown-hover">
					                                        		Confirm Delete
					                                        	</a>
					                                    	</li>
					                                    </ul>
					                                    </div>
					                                    </sec:authorize>
													</div>	
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