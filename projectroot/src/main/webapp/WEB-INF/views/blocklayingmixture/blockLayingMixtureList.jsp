<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Block Laying Mixture List</title>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                Block Laying Mixture List
	                <small>Complete list of all blockLayingMixtures of measure</small>
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
													<c:url var="urlCreateBlockLayingMixture" value="/blocklayingmixture/edit/0-end"/>
				                                	<a href="${urlCreateBlockLayingMixture}">
				                                		<button class="btn btn-cebedo-create btn-flat btn-sm">Create Block Laying Mixture</button>
				                                	</a>
				                                	<br/><br/>
				                                    <table id="example-1" class="table table-bordered table-striped">
				                                        <thead>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                            	<th>Name</th>
				                                                <th>Description</th>
				                                                <th>CHB Measurement</th>
				                                                <th>Concrete Proportion</th>
				                                                <th>No. of Cement Bags</th>
				                                                <th>Sand (cu.m.)</th>
				                                            </tr>
				                                        </thead>
				                                        <tbody>
				                                        	<c:if test="${!empty blockLayingMixtureList}">
				                                        		<c:forEach items="${blockLayingMixtureList}" var="blockLayingMixture">
						                                            <tr>
						                                            	<td>
						                                            		<center>
						                                            			<c:url var="urlEditBlockLayingMixture" value="/blocklayingmixture/edit/${blockLayingMixture.getKey()}-end"/>
				                                								<a href="${urlEditBlockLayingMixture}">
																					<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
				                                								</a>
																				<c:url var="urlDeleteBlockLayingMixture" value="/blocklayingmixture/delete/${blockLayingMixture.getKey()}-end"/>
				                                								<a href="${urlDeleteBlockLayingMixture}">
																					<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
				                                								</a>
																			</center>
																		</td>
						                                                <td>${blockLayingMixture.name}</td>
						                                                <td>${blockLayingMixture.description}</td>
						                                                <td>${blockLayingMixture.chbMeasurement.getDisplayName()}</td>
						                                                <td>${blockLayingMixture.concreteProportion.getDisplayName()}</td>
						                                                <td>${blockLayingMixture.cementBags}</td>
						                                                <td>${blockLayingMixture.sand}</td>
						                                            </tr>
					                                            </c:forEach>
				                                            </c:if>
				                                        </tbody>
				                                        <tfoot>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                            	<th>Name</th>
				                                                <th>Description</th>
				                                                <th>CHB Measurement</th>
				                                                <th>No. of Cement Bags</th>
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