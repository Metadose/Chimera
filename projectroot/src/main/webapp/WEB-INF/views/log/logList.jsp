<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Log Manager</title>
	
	<link href="<c:url value="/resources/lib/jstree/src/themes/default/style.css" />"rel="stylesheet" type="text/css" />
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                Log Manager
	                <small>List of system logs</small>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="box">
                                <div class="box-header">
                                
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive">
	                                <div id="jstree">
	                               	</div>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->
                    </div>
                </div>
            </section><!-- /.content -->
        </aside>
	</div>
	<form id="logEditForm" action="${contextPath}/log/edit" method="post">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<input type="hidden" id="input_log_address" name="input_log_address" value=""/>
	</form>
	
	<script src="<c:url value="/resources/lib/jstree/dist/jstree.min.js" />"></script>
	<script type="text/javascript">
		var data = ${logList};
		$(document).ready(function() {
			// HOW IT WORKS:
			// Getting an attribute of the object.
			// data.instance.get_node(data.selected[0]).text
			$('#jstree').on('changed.jstree', function (e, data) {
				var address = data.instance.get_node(data.selected[0]).id;
				$('#input_log_address').val(address);
				$('#logEditForm').submit();
			}).on('loaded.jstree', function() {
				$('#jstree').jstree('open_all');
			}).jstree({'core': data});
		});
	</script>
</body>
</html>