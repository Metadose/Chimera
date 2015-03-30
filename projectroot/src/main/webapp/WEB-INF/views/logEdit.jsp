<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Log Manager</title>
	<c:import url="/resources/css-includes.jsp" />
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
                                
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive">
	                                <table id="log-table" class="table table-bordered table-striped">
										<thead>
										<tr>
										<th>Timestamp</th>
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
	<c:import url="/resources/js-includes.jsp" />
	<script type="text/javascript">
	$(document).ready(function() {
		$('#log-table').dataTable();
	});
	</script>
</body>
</html>