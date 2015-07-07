<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>CHB Vertical Reinforcement List</title>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                CHB Vertical Reinforcement List
	                <small>Complete list of all chbVerticalReinforcements of measure</small>
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
													<c:url var="urlCreateCHBVerticalReinforcement" value="/chbverticalreinforcement/edit/0-end"/>
				                                	<a href="${urlCreateCHBVerticalReinforcement}">
				                                		<button class="btn btn-cebedo-create btn-flat btn-sm">Create CHB Vertical Reinforcement</button>
				                                	</a>
				                                	<br/><br/>
				                                    <table id="example-1" class="table table-bordered table-striped">
				                                        <thead>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                            	<th>Name</th>
				                                                <th>Description</th>
				                                                <th>Bar Spacing</th>
				                                                <th>Bar Length (per sq.m.)</th>
				                                            </tr>
				                                        </thead>
				                                        <tbody>
				                                        	<c:if test="${!empty chbVerticalReinforcementList}">
				                                        		<c:forEach items="${chbVerticalReinforcementList}" var="chbVerticalReinforcement">
						                                            <tr>
						                                            	<td>
						                                            		<center>
						                                            			<c:url var="urlEditCHBVerticalReinforcement" value="/chbverticalreinforcement/edit/${chbVerticalReinforcement.getKey()}-end"/>
				                                								<a href="${urlEditCHBVerticalReinforcement}">
																					<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
				                                								</a>
																				<c:url var="urlDeleteCHBVerticalReinforcement" value="/chbverticalreinforcement/delete/${chbVerticalReinforcement.getKey()}-end"/>
				                                								<a href="${urlDeleteCHBVerticalReinforcement}">
																					<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
				                                								</a>
																			</center>
																		</td>
						                                                <td>${chbVerticalReinforcement.name}</td>
						                                                <td>${chbVerticalReinforcement.description}</td>
						                                                <td>${chbVerticalReinforcement.barsSpacing.label()}</td>
						                                                <td>${chbVerticalReinforcement.barLengthPerSqm}</td>
						                                            </tr>
					                                            </c:forEach>
				                                            </c:if>
				                                        </tbody>
				                                        <tfoot>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                            	<th>Name</th>
				                                                <th>Description</th>
				                                                <th>Bar Spacing</th>
				                                                <th>Bar Length (per sq.m.)</th>
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