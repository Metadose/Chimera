<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>CHB Horizontal Reinforcement List</title>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                CHB Horizontal Reinforcement List
	                <small>Complete list of all chbHorizontalReinforcements of measure</small>
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
													<c:url var="urlCreateCHBHorizontalReinforcement" value="/chbhorizontalreinforcement/edit/0-end"/>
				                                	<a href="${urlCreateCHBHorizontalReinforcement}">
				                                		<button class="btn btn-cebedo-create btn-flat btn-sm">Create CHB Horizontal Reinforcement</button>
				                                	</a>
				                                	<br/><br/>
				                                    <table id="example-1" class="table table-bordered table-striped">
				                                        <thead>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                            	<th>Name</th>
				                                                <th>Description</th>
				                                                <th>Reinforcing Bars Layer TODO need better naming</th>
				                                                <th>Bar Length per Square Meter</th>
				                                            </tr>
				                                        </thead>
				                                        <tbody>
				                                        	<c:if test="${!empty chbHorizontalReinforcementList}">
				                                        		<c:forEach items="${chbHorizontalReinforcementList}" var="chbHorizontalReinforcement">
						                                            <tr>
						                                            	<td>
						                                            		<center>
						                                            			<c:url var="urlEditCHBHorizontalReinforcement" value="/chbhorizontalreinforcement/edit/${chbHorizontalReinforcement.getKey()}-end"/>
				                                								<a href="${urlEditCHBHorizontalReinforcement}">
																					<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
				                                								</a>
																				<c:url var="urlDeleteCHBHorizontalReinforcement" value="/chbhorizontalreinforcement/delete/${chbHorizontalReinforcement.getKey()}-end"/>
				                                								<a href="${urlDeleteCHBHorizontalReinforcement}">
																					<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
				                                								</a>
																			</center>
																		</td>
						                                                <td>${chbHorizontalReinforcement.name}</td>
						                                                <td>${chbHorizontalReinforcement.description}</td>
						                                                <td>${chbHorizontalReinforcement.barsLayer.layer()}</td>
						                                                <td>${chbHorizontalReinforcement.barLengthPerSqm}</td>
						                                            </tr>
					                                            </c:forEach>
				                                            </c:if>
				                                        </tbody>
				                                        <tfoot>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                            	<th>Name</th>
				                                                <th>Description</th>
				                                                <th>Reinforcing Bars Layer TODO need better naming</th>
				                                                <th>Bar Length per Square Meter</th>
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