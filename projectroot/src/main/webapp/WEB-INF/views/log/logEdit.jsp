<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Log Manager</title>
	
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                Log Viewer
	                <small>${logPath}</small>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="box">
                                <div class="box-header">
                                
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive">
                                	<a href="${contextPath}/log/list">
                                		<button class="btn btn-default btn-flat btn-sm">Back</button>
                                	</a><br/><br/>
	                                <table id="log-table" class="table table-bordered table-striped">
										<thead>
										<tr>
										<th>Timestamp</th>
										<c:choose>
											<c:when test="${isPerformance}">
												<th>Method</th>
												<th>Milliseconds</th>
											</c:when>
											<c:when test="${!isPerformance}">
												<th>Thread</th>
												<th>Level</th>
												<th>Logger</th>
												<th>Location</th>
												<th>IP Address</th>
												<th>User</th>
												<th>Staff</th>
												<th>Company</th>
												<th>Authorities</th>
												<th>Message</th>
												<c:if test="${isError}">
													<th>Exception</th>
												</c:if>
											</c:when>
										</c:choose>
										</tr>
										</thead>
										<tbody>
											${logContent}
										</tbody>
									</table>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->
                    </div>
                </div>
            </section><!-- /.content -->
        </aside>
	</div>
	
	<script type="text/javascript">
	$(document).ready(function() {
		$('#log-table').dataTable();
	});
	</script>
</body>
</html>