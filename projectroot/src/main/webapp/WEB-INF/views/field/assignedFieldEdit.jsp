<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Field Edit</title>
	
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
	            	<c:out value="${field.label}"></c:out>
	                <small>Edit Field</small>
	            </h1>
	        </section>
	        <section class="content">
	        	<c:url var="urlBack" value="/project/edit/${project.id}" />
                   <a href="${urlBack}">
					<button class="btn btn-cebedo-back btn-flat btn-sm">Back to Project</button>
				</a><br/><br/>
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
                   							<div class="box-body box-default">
                   								<div class="callout callout-info callout-cebedo">
								                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
								                </div>
                   									<form:form modelAttribute="field"
														id="detailsForm"
														method="post"
														action="${contextPath}/project/field/update">
				                                        <div class="form-group">
				                                            <label>Label</label>
				                                            <form:input type="text" class="form-control" path="label"/><br/>
				                                            <label>Value</label>
				                                            <form:input type="text" class="form-control" path="value"/>
				                                        </div>
				                                    </form:form>
                                            		<button class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
                                            		<c:url var="urlDeleteField" value="/project/field/delete" />
                                            		<a href="${urlDeleteField}">
														<button class="btn btn-cebedo-delete btn-flat btn-sm">Remove This Field</button>
													</a>
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