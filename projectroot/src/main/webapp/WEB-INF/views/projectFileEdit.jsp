<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>File ${action}</title>
	
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
                    	${uiParamAlert}
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                	<div class="row">
                   						<div class="col-md-6">
                   							<div class="box box-default">
                   								<div class="box-body">
				                                    <c:choose>
		                                            	<c:when test="${projectfile.id == 0}">
		                                            		<form enctype="multipart/form-data" method="post" action="${contextPath}/projectfile/upload/file">
		                                            			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						                                        <label for="exampleInputFile">File Upload (20MB Max)</label>
						                                        <input type="file" id="file" name="file"/><br/>
						                                        <label>Description</label>
					                                            <input type="text" class="form-control" id="description" name="description"/><br/>
					                                            <button class="btn btn-default btn-flat btn-sm" id="uploadButton">Upload</button>
				                                            </form>
		                                            	</c:when>
		                                            	<c:when test="${projectfile.id > 0}">
<%-- 		                                            	   /projectfile/edit/${file.id}/from/project/${project.id} --%>
															<c:set value="${contextPath}/projectfile/update" var="urlProjectFileUpdate"/>
															<c:if test="${!empty origin && !empty originID}">
																<c:set value="${contextPath}/projectfile/update/from/${origin}/${originID}" var="urlProjectFileUpdate"/>
															</c:if>
					                                        <div class="form-group">
					                                            <form:form role="form"
					                                            	modelAttribute="projectfile" 
					                                            	name="fileForm" id="fileForm"
																	method="post"
																	action="${urlProjectFileUpdate}">
						                                            <label>Description</label>
						                                            <form:textarea class="form-control"
																		rows="3" id="description" path="description"
																		placeholder="Example: Reference spreadsheet file..."></form:textarea>
					                                            </form:form>
					                                        </div>
					                                        
		                                            		<c:set var="staff" value="${projectfile.uploader}"/>
			                                                <c:set var="staffName" value="${staff.prefix} ${staff.firstName} ${staff.middleName} ${staff.lastName} ${staff.suffix}"/>
				                                            <label>Name ${projectfile.name}</label>
					                                        <label>Size ${projectfile.size}</label><br/>
				                                            <label>Project ${projectfile.project.name}</label><br/>
				                                            <label>Uploader ${staffName}</label><br/>
				                                            <label>Date Uploaded ${projectfile.dateUploaded}</label><br/>
				                                            
		                                            		<button class="btn btn-default btn-flat btn-sm" id="detailsButton" onclick="submitForm('fileForm')">Update</button>
		                                            		<c:url value="/projectfile/delete/${projectfile.id}" var="urlDeleteProjectfile"/>
		                                            		<a href="${urlDeleteProjectfile}">
																<button class="btn btn-default btn-flat btn-sm">Delete This File</button>
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
	
		$(document).ready(function() {
			$("#example-1").dataTable();
	    });
	</script>
</body>
</html>