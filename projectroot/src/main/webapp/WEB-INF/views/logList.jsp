<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Log Manager</title>
	<c:import url="/resources/css-includes.jsp" />
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
	                                <div id="jstree">
	                               	</div>
                                </div><!-- /.box-body -->
                            </div><!-- /.box -->
                    </div>
                </div>
            </section><!-- /.content -->
        </aside>
	</div>
	<c:import url="/resources/js-includes.jsp" />
	<script src="<c:url value="/resources/lib/jstree/dist/jstree.min.js" />"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			// HOW IT WORKS:
			// Getting an attribute of the object.
			// data.instance.get_node(data.selected[0]).text
			$('#jstree').on('changed.jstree', function (e, data) {
				var i, j, r = [];
			    for(i = 0, j = data.selected.length; i < j; i++) {
			      r.push(data.instance.get_node(data.selected[i]).text);
			    }
			    console.log(r.join(', '));
			  }).jstree({'core':{
				${logList}
			} });
		});
	</script>
</body>
</html>