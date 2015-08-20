<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<c:choose>
    	<c:when test="${config.id == 0}">
    	<title>Create System Configuration</title>
    	</c:when>
    	<c:when test="${config.id > 0}">
		<title>Edit System Configuration</title>
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
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	            	<c:choose>
				    	<c:when test="${config.id == 0}">
				    	New Configuration
		                <small>Create System Configuration</small>
				    	</c:when>
				    	<c:when test="${config.id > 0}">
		                ${config.name}
		                <small>Edit System Configuration</small>
				    	</c:when>
				    </c:choose>
	            </h1>
	        </section>
	        <section class="content">
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
                   							<div class="box box-body box-default">
                   								<div class="box-body">
                   									<form role="form" name="detailsForm" id="detailsForm" method="post" action="${contextPath}/config/create">
				                                        <div class="form-group">
				                                        	<input type="hidden" id="id" name="id" value="${config.id}"/>
				                                            <label>Name</label>
				                                            <input type="text" class="form-control" id="name" name="name" value="${config.name}" placeholder="Sample: ROOT_INIT, SYS_HOME"/>
				                                            <p class="help-block">Enter the name of the configuration</p>

				                                            <label>Value</label>
				                                            <input type="text" class="form-control" id="value" name="value" value="${config.value}" placeholder="Sample: 1, C:/vcc/sys/"/>
				                                            <p class="help-block">Enter the value of the configuration</p>
				                                        </div>
				                                    </form>
				                                    <c:choose>
		                                            	<c:when test="${config.id == 0}">
		                                            		<button class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${config.id > 0}">
		                                            		<button class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
		                                            		<a href="${contextPath}/config/delete/${config.id}">
																<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete This Configuration</button>
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
	</script>
</body>
</html>