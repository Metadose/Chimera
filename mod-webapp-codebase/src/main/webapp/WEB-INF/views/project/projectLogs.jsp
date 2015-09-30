<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>${project.name} | Projects Logs</title>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<!--  -->
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                Projects Logs
	                <small>Complete list of all project logs</small>
	            </h1>
	        </section>
	        <section class="content">
			<div class="row">
				<div class="col-md-12">
					<!-- Custom Tabs -->
					<div class="row">
						<div class="col-md-12">
							<div class="box">
									<div class="box-body">
	                                    <table id="example-1" class="table table-bordered table-striped">
	                                        <thead>
	                                            <tr>
	                                            	<th>ID</th>
	                                            	<th>Date Executed</th>
	                                            	<th>IP Address</th>
	                                                <th>User</th>
	                                                <th>Action</th>
	                                                <th>Object</th>
	                                                <th>Linked Object</th>
	                                                <th>Entry Name</th>
	                                            </tr>
	                                        </thead>
	                                        <tbody>
	                                        	<c:if test="${!empty logs}">
	                                        		<c:forEach items="${logs}" var="log">
			                                            <tr>
			                                                <td>${log.id}</td>
			                                                <td>${log.getDateExecutedAsString()}</td>
			                                                <td>${log.ipAddress}</td>
			                                                <td>${log.user.username}</td>
			                                                <td>${log.auditAction.label()}</td>
			                                                <td>${log.getObjectDetails()}</td>
			                                                <td>${log.getAssocObjectDetails()}</td>
			                                                <td>${log.entryName}</td>
			                                            </tr>
		                                            </c:forEach>
	                                            </c:if>
	                                        </tbody>
	                                        <tfoot>
	                                            <tr>
	                                            	<th>ID</th>
	                                            	<th>Date Executed</th>
	                                            	<th>IP Address</th>
	                                                <th>User</th>
	                                                <th>Action</th>
	                                                <th>Object</th>
	                                                <th>Linked Object</th>
	                                                <th>Entry Name</th>
	                                            </tr>
	                                        </tfoot>
	                                    </table>
	                                </div><!-- /.box-body -->
								</div><!-- /.box -->
						</div>
					</div>
				</div>
			</div>
			</section>
        </aside>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#example-1').DataTable({
		        "order": [[ 1, "desc" ]]
		    });
		});
	</script>
</body>
</html>