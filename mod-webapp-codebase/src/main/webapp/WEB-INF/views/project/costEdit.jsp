<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>${cost.name} | Edit Cost</title>
	
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
	            	<c:out value="${cost.name}"></c:out>
	                <small>Edit Cost</small>
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
											<div class="box box-default">
												<div class="box-body">
													<form:form modelAttribute="cost"
														action="${contextPath}/project/create/cost"
														method="post"
														id="detailsForm"
														enctype="multipart/form-data">
				                                        <div class="form-group">
				                                        
														<label>Name</label>
														<form:input placeholder="Sample: Sitework, Concrete Works, Metal Works" class="form-control" path="name"/>
														<p class="help-block">Enter the name of this cost</p>
														
														<table style="width: 100%;">
															<tr>
																<td style="vertical-align: top;">
							                                        <label>Estimated Cost</label>
																	<form:input class="form-control" path="cost"/>
																	<p class="help-block">Enter the estimated cost</p>		
																</td>

																<td>&nbsp;</td>

																<td style="vertical-align: top;">
							                                        <label>Actual Cost</label>
																	<form:input class="form-control" path="actualCost"/>
																	<p class="help-block">Enter the actual cost</p>	
																</td>

																<td>&nbsp;</td>

																<td style="vertical-align: top;">
					                                                <label>Cost Type</label>
					                                                <form:select class="form-control" path="costType"> 
				                                   						<c:forEach items="${estimateCostList}" var="cost"> 
				                                   							<form:option value="${cost}" label="${cost.getLabel()}"/> 
				                                   						</c:forEach> 
					                                    			</form:select>
					                                    			<p class="help-block">Type of estimate cost</p>																				
																</td>
															</tr>
														</table>
				                                        </div>
			                                        </form:form>

			                                        <button class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton" onclick="submitForm('detailsForm')">Update</button>
				                                    
				                                    <sec:authorize access="hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_DELETE')">
				                                    <div class="btn-group">
				                                    <button type="button" class="btn btn-cebedo-delete btn-flat btn-sm dropdown-toggle" data-toggle="dropdown">Delete</button>
				                                    <ul class="dropdown-menu">
				                                    	<li>
				                                    		<a href="<c:url value="/project/delete/cost/${cost.getKey()}-end"/>" class="cebedo-dropdown-hover">
				                                        		Confirm Delete
				                                        	</a>
				                                    	</li>
				                                    </ul>
				                                    </div>
				                                    </sec:authorize>

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