<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Field ${action}</title>
	
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                Field ${action}
	                <small>Complete list of all fields</small>
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
                                	<sec:authorize access="hasRole('ROLE_FIELD_EDITOR')">
                                	<a href="${contextPath}/field/edit/0">
                                		<button class="btn btn-default btn-flat btn-sm">Create Field</button>
                                	</a>
                                	<br/><br/>
                                	</sec:authorize>
                                    <table id="example-1" class="table table-bordered table-striped">
                                        <thead>
                                            <tr>
                                            	<th>&nbsp;</th>
                                            	<th>#</th>
                                                <th>Name</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:if test="${!empty fieldList}">
                                        		<c:forEach items="${fieldList}" var="field">
		                                            <tr>
		                                            	<td>
		                                            		<center>
																<a href="${contextPath}/field/edit/${field.id}">
																	<button class="btn btn-default btn-flat btn-sm">View</button>
																</a>
																<sec:authorize access="hasRole('ROLE_FIELD_EDITOR')">
																<a href="${contextPath}/field/delete/${field.id}">
																	<button class="btn btn-default btn-flat btn-sm">Delete</button>
																</a>
																</sec:authorize>
															</center>
														</td>
														<td>${field.id}</td>
		                                                <td>${field.name}</td>
		                                            </tr>
	                                            </c:forEach>
                                            </c:if>
                                        </tbody>
                                        <tfoot>
                                            <tr>
                                            	<th>&nbsp;</th>
                                            	<th>#</th>
                                                <th>Name</th>
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