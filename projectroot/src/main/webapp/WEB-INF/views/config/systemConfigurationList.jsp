<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>List System Configurations</title>
	
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                List System Configurations
	                <small>Complete list of all system configurations</small>
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
                                	<a href="<c:url value="/config/edit/0"/>">
                                		<button class="btn btn-cebedo-create btn-flat btn-sm">Create Configuration</button>
                                	</a>
                                	<br/><br/>
                                    <table id="example-1" class="table table-bordered table-striped">
                                        <thead>
                                            <tr>
                                            	<th>&nbsp;</th>
                                                <th>Name</th>
                                                <th>Value</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:if test="${!empty systemConfigurationList}">
                                        		<c:forEach items="${systemConfigurationList}" var="systemConfiguration">
		                                            <tr>
		                                            	<td>
		                                            		<center>
																<a href="<c:url value="/config/edit/${systemConfiguration.id}"/>">
																	<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
																</a>
																<a href="<c:url value="/config/delete/${systemConfiguration.id}"/>">
																	<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
																</a>
															</center>
														</td>
		                                                <td>${systemConfiguration.name}</td>
		                                                <td>${systemConfiguration.value}</td>
		                                            </tr>
	                                            </c:forEach>
                                            </c:if>
                                        </tbody>
                                        <tfoot>
                                            <tr>
                                            	<th>&nbsp;</th>
                                                <th>Name</th>
                                                <th>Value</th>
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