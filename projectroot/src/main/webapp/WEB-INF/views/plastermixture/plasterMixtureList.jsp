<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Plaster Mixture List</title>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                Plaster Mixture List
	                <small>Complete list of all mixtures of plaster</small>
	            </h1>
	        </section>
	        <section class="content">
			<div class="row">
				<div class="col-md-12">
					${uiParamAlert}
					<!-- Custom Tabs -->
					<div class="nav-tabs-custom">
						<ul class="nav nav-tabs" id="navigatorTab">
							<li class="active"><a href="#tab_list" data-toggle="tab">List</a></li>
						</ul>
						<div class="tab-content">
							<div class="tab-pane active" id="tab_list">
								<div class="row">
									<div class="col-md-12">
										<div class="box-body box-default">
													<c:url var="urlCreatePlasterMixture" value="/plastermixture/edit/0-end"/>
				                                	<a href="${urlCreatePlasterMixture}">
				                                		<button class="btn btn-cebedo-create btn-flat btn-sm">Create Plaster Mixture</button>
				                                	</a>
				                                	<br/><br/>
				                                    <table id="example-1" class="table table-bordered table-striped">
				                                        <thead>
				                                        	<tr>
				                                            	<th colspan="4">Details</th>
				                                            	<th colspan="2">Cement</th>
				                                            	<th colspan="1">Sand</th>
				                                            </tr>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                            	<th>Name</th>
				                                                <th>Description</th>
				                                                <th>Concrete Proportion</th>
				                                                <th>40kg</th>
				                                                <th>50kg</th>
				                                                <th>Sand (cu.m.)</th>
				                                            </tr>
				                                        </thead>
				                                        <tbody>
				                                        	<c:if test="${!empty plasterMixtureList}">
				                                        		<c:forEach items="${plasterMixtureList}" var="plasterMixture">
						                                            <tr>
						                                            	<td>
						                                            		<center>
						                                            			<c:url var="urlEditPlasterMixture" value="/plastermixture/edit/${plasterMixture.getKey()}-end"/>
				                                								<a href="${urlEditPlasterMixture}">
																					<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
				                                								</a>
																				<c:url var="urlDeletePlasterMixture" value="/plastermixture/delete/${plasterMixture.getKey()}-end"/>
				                                								<a href="${urlDeletePlasterMixture}">
																					<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
				                                								</a>
																			</center>
																		</td>
						                                                <td>${plasterMixture.getDisplayName()}</td>
						                                                <td>${plasterMixture.description}</td>
						                                                <td>${plasterMixture.concreteProportion.getDisplayName()}</td>
						                                                <td>${plasterMixture.cement40kg}</td>
						                                                <td>${plasterMixture.cement50kg}</td>
						                                                <td>${plasterMixture.sand}</td>
						                                            </tr>
					                                            </c:forEach>
				                                            </c:if>
				                                        </tbody>
				                                        <tfoot>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                            	<th>Name</th>
				                                                <th>Description</th>
				                                                <th>Concrete Proportion</th>
				                                                <th>40kg</th>
				                                                <th>50kg</th>
				                                                <th>Sand (cu.m.)</th>
				                                            </tr>
				                                        </tfoot>
				                                    </table>
											</div><!-- /.box -->
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			</section>
        </aside>
	</div>
	<script src="<c:url value="/resources/js/common.js" />"type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#example-1").dataTable();
		});
	</script>
</body>
</html>