<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Company ${action}</title>
	
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                Company ${action}
	                <small>Complete list of all company members</small>
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
                                <div class="box-body table-responsive">
                                	<c:url value="/company/edit/0" var="urlCreateCompany"/>
                                	<a href="${urlCreateCompany}">
                                		<button class="btn btn-default btn-flat btn-sm">Create Company</button>
                                	</a>
                                	<br/><br/>
                                    <table id="example-1" class="table table-bordered table-striped">
                                        <thead>
                                            <tr>
                                            	<th>&nbsp;</th>
                                            	<th>#</th>
                                                <th>Name</th>
                                                <th>Description</th>
                                                <th>Date Started</th>
                                                <th>Date Expiration</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:if test="${!empty companyList}">
                                        		<c:forEach items="${companyList}" var="company">
		                                            <tr>
		                                            	<td>
		                                            		<center>
		                                            			<c:url value="/company/edit/${company.id}" var="urlEditCompany"/>
																<a href="${urlEditCompany}">
																	<button class="btn btn-default btn-flat btn-sm">View</button>
																</a>
																<c:url value="/company/delete/${company.id}" var="urlDeleteCompany"/>
																<a href="${urlDeleteCompany}">
																	<button class="btn btn-default btn-flat btn-sm">Delete</button>
																</a>
															</center>
														</td>
														<td>${company.id}</td>
		                                                <td>${company.name}</td>
		                                                <td>${company.description}</td>
		                                                <td>${company.dateStarted}</td>
		                                                <td>${company.dateExpiration}</td>
		                                            </tr>
	                                            </c:forEach>
                                            </c:if>
                                        </tbody>
                                        <tfoot>
                                            <tr>
                                            	<th>&nbsp;</th>
                                            	<th>#</th>
                                                <th>Name</th>
                                                <th>Description</th>
                                                <th>Date Started</th>
                                                <th>Date Expiration</th>
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