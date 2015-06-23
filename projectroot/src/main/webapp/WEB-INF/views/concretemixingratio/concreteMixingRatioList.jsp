<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Concrete Mixing Ratio List</title>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                Concrete Mixing Ratio List
	                <small>Complete list of all mixing ratios for concrete estimation</small>
	            </h1>
	        </section>
	        <section class="content">
			<div class="row">
				<div class="col-xs-12">
					${uiParamAlert}
					<!-- Custom Tabs -->
					<div class="nav-tabs-custom">
						<ul class="nav nav-tabs" id="navigatorTab">
							<li class="active"><a href="#tab_list" data-toggle="tab">List</a></li>
						</ul>
						<div class="tab-content">
							<div class="tab-pane active" id="tab_list">
								<div class="row">
									<div class="col-xs-12">
										<div class="box">
												<div class="box-body table-responsive">
													<c:url var="urlCreate" value="/concretemixingratio/edit/0-end"/>
				                                	<a href="${urlCreate}">
				                                		<button class="btn btn-cebedo-create btn-flat btn-sm">Create Ratio</button>
				                                	</a>
				                                	<br/><br/>
				                                    <table id="example-1" class="table table-bordered table-striped">
				                                        <thead>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                            	<th>Name</th>
				                                                <th>Description</th>
				                                                <th>Cement</th>
				                                                <th>Sand</th>
				                                                <th>Gravel</th>
				                                            </tr>
				                                        </thead>
				                                        <tbody>
				                                        	<c:if test="${!empty concreteMixingRatioList}">
				                                        		<c:forEach items="${concreteMixingRatioList}" var="ratio">
						                                            <tr>
						                                            	<td>
						                                            		<center>
						                                            			<c:url var="urlEdit" value="/concretemixingratio/edit/${ratio.getKey()}-end"/>
				                                								<a href="${urlEdit}">
																					<button class="btn btn-cebedo-view btn-flat btn-sm">View</button>
				                                								</a>
																				<c:url var="urlDelete" value="/concretemixingratio/delete/${ratio.getKey()}-end"/>
				                                								<a href="${urlDelete}">
																					<button class="btn btn-cebedo-delete btn-flat btn-sm">Delete</button>
				                                								</a>
																			</center>
																		</td>
						                                                <td>${ratio.name}</td>
						                                                <td>${ratio.description}</td>
						                                                <td>${ratio.ratioCement}</td>
						                                                <td>${ratio.ratioSand}</td>
						                                                <td>${ratio.ratioGravel}</td>
						                                            </tr>
					                                            </c:forEach>
				                                            </c:if>
				                                        </tbody>
				                                        <tfoot>
				                                            <tr>
				                                            	<th>&nbsp;</th>
				                                            	<th>Name</th>
				                                                <th>Description</th>
				                                                <th>Cement</th>
				                                                <th>Sand</th>
				                                                <th>Gravel</th>
				                                            </tr>
				                                        </tfoot>
				                                    </table>
				                                </div><!-- /.box-body -->
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