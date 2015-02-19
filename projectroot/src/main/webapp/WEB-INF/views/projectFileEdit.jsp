<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>File ${action}</title>
	<c:import url="/resources/css-includes.jsp" />
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
	                ${projectfile.name}
	                <small>${action} File</small>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<h2 class="page-header">Information</h2>
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-primary">
                   								<div class="box-header">
                   									<h3 class="box-title">Details</h3>
                   								</div>
                   								<div class="box-body">
                   									<c:set var="staff" value="${projectfile.uploader}"/>
	                                                <c:set var="staffName" value="${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}"/>
                   									
				                                        <div class="form-group">
				                                            <label>Name</label>
				                                            <input type="text" disabled class="form-control" name="name" value="${projectfile.name}"/><br/>
				                                            <form role="form" name="fileForm" id="fileForm" method="post" action="${contextPath}/projectfile/update">
					                                            <label>Description</label>
					                                            <input type="hidden" name="projectfile_id" value="${projectfile.id}"/>
					                                            <input type="text" class="form-control" name="description" value="${projectfile.description}"/><br/>
				                                            </form>
				                                            <label>Size</label>
				                                            <input type="text" disabled class="form-control" name="size" value="${projectfile.size}"/><br/>
				                                            <label>Project</label>
				                                            <input type="text" disabled class="form-control" name="project" value="${projectfile.project.name}"/><br/>
				                                            <label>Uploader</label>
				                                            <input type="text" disabled class="form-control" name="uploader" value="${staffName}"/><br/>
				                                            <label>Date Uploaded</label>
				                                            <input type="text" disabled class="form-control" name="dateUploaded" value="${projectfile.dateUploaded}"/><br/>
				                                        </div>
				                                    <c:choose>
		                                            	<c:when test="${projectfile.id == 0}">
		                                            		<button class="btn btn-success btn-sm" id="detailsButton" onclick="submitForm('fileForm')">Create</button>
		                                            	</c:when>
		                                            	<c:when test="${projectfile.id > 0}">
		                                            		<button class="btn btn-warning btn-sm" id="detailsButton" onclick="submitForm('fileForm')">Update</button>
		                                            		<a href="${contextPath}/projectfile/delete/${projectfile.id}">
																<button class="btn btn-danger btn-sm">Delete This File</button>
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
	<c:import url="/resources/js-includes.jsp" />
	<script>
		function submitForm(id) {
			$('#'+id).submit();
		}
	
		$(document).ready(function() {
			$("#example-1").dataTable();
	    });
	</script>
</body>
</html>