<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<sec:authentication var="authUser" property="user"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>${company.name} | Clone Company</title>
	
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
		<!--  -->
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                ${company.name}
	                <small>Clone Company</small>
	            </h1>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-md-12">
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
                   							<div class="box box-body box-default">
                   								<div class="box-body">
                   								
                   									<form:form modelAttribute="company" 
                   										id="companyForm" 
                   										role="form" 
                   										method="post" 
                   										action="${contextPath}/company/do-clone/">
                   										
				                                        <div class="form-group">
				                                            <label>Clone Name</label>
				                                            <form:input type="text" 
				                                            	class="form-control"
				                                            	placeholder="Sample: ABC Construction, XYZ Builders, City Contractors" 
				                                            	path="name"/>
				                                            <p class="help-block">Enter the name of the clone</p>
				                                            
				                                            <label>Randomize Staff Names</label>
				                                            <form:checkbox class="form-control" path="randomizeNames"/>
				                                            <p class="help-block">Check if you want to randomize staff names</p>
				                                        </div>
				                                        
				                                    </form:form>
				                                    
		                                        	<button class="btn btn-cebedo-create btn-flat btn-sm"
		                                        		id="detailsButton" 
		                                        		onclick="submitForm('companyForm')">
		                                        		Clone
		                                        	</button>
		                                        	
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