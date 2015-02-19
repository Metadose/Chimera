<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>File ${action}</title>
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
	                File ${action}
	                <small>Complete list of all files</small>
	            </h1>
	            <ol class="breadcrumb">
	                <li><a href="${contextPath}/dashboard/">Home</a></li>
	                <li class="active"><a href="${contextPath}/projectfile/list">File</a></li>
	            </ol>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="box">
                                <div class="box-header">
<!--                                     <h3 class="box-title">Data Table With Full Features</h3> -->
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive">
                                	<a href="${contextPath}/projectfile/edit/0">
                                		<button class="btn btn-success btn-sm">Upload File</button>
                                	</a>
                                	<br/><br/>
                                    <table id="example-1" class="table table-bordered table-striped">
                                        <thead>
                                            <tr>
                                            	<th>&nbsp;</th>
                                            	<th>#</th>
                                                <th>Name</th>
                                                <th>Description</th>
                                                <th>Size</th>
                                                <th>Project</th>
                                                <th>Uploader</th>
                                                <th>Date Uploaded</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:if test="${!empty projectFileList}">
                                        		<c:forEach items="${projectFileList}" var="file">
                                        			<c:set var="staff" value="${file.uploader}"/>
	                                                <c:set var="staffName" value="${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}"/>
		                                            <tr>
		                                            	<td>
		                                            		<center>
																<a href="${contextPath}/projectfile/edit/${file.id}">
																	<button class="btn btn-primary btn-sm">View</button>
																</a>
																<a href="${contextPath}/projectfile/delete/${file.id}">
																	<button class="btn btn-danger btn-sm">Delete</button>
																</a>
															</center>
														</td>
														<td>${file.id}</td>
		                                                <td>${file.name}</td>
		                                                <td>${file.description}</td>
		                                                <td>${file.size}</td>
		                                                <td>
		                                                	<c:choose>
		                                                		<c:when test="${!empty file.project}">
		                                                			<a href="${contextPath}/project/edit/${file.project.id}">
																		<button class="btn btn-info btn-sm">View</button>
																	</a>
					                                                ${file.project.name}
		                                                		</c:when>
		                                                		<c:when test="${empty file.project}">
		                                                			<h5>No project assigned.</h5>
		                                                		</c:when>
		                                                	</c:choose>
		                                                </td>
		                                                <td>
		                                                <a href="${contextPath}/staff/edit/${file.uploader.id}">
															<button class="btn btn-info btn-sm">View</button>
														</a>
		                                                ${staffName}
		                                                </td>
		                                                <td>${file.dateUploaded}</td>
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
                                                <th>Size</th>
                                                <th>Project</th>
                                                <th>Uploader</th>
                                                <th>Date Uploaded</th>
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
	<c:import url="/resources/js-includes.jsp" />
	<script>
		$(document).ready(function() {
			$("#example-1").dataTable();
	    });
	</script>
</body>
</html>