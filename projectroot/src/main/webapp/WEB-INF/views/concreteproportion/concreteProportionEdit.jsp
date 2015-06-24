<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<c:set value="${false}" var="isUpdating"/>
	<c:choose>
   	<c:when test="${empty concreteproportion.uuid}">
    	<title>Concrete Proportion Create</title>
   	</c:when>
   	<c:when test="${!empty concreteproportion.uuid}">
		<title>Concrete Proportion Edit</title>
		<c:set value="${true}" var="isUpdating"/>
   	</c:when>
   	</c:choose>
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
	            	<c:choose>
	            	<c:when test="${!isUpdating}">
		            	New Concrete Proportion
		                <small>Create Concrete Proportion</small>
	            	</c:when>
	            	<c:when test="${isUpdating}">
	            		${concreteproportion.name}
		                <small>Edit Concrete Proportion</small>
	            	</c:when>
	            	</c:choose>
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
                   								<div class="box-header">
                   									<h3 class="box-title">Details</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="callout callout-info callout-cebedo">
									                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
									                </div>
                   									<form:form modelAttribute="concreteproportion"
														id="detailsForm"
														method="post"
														action="${contextPath}/concreteproportion/create">
				                                        <div class="form-group">
				                                            
				                                            <h4>Basic</h4>
				                                            <label>Name</label>
				                                            <form:input type="text" placeholder="Sample: AAA, A, B, C" class="form-control" path="name"/>
				                                            <p class="help-block">Enter the name or class of this mixing proportion</p>
				                                            
				                                            <label>Description</label>
				                                            <form:input type="text" placeholder="Sample: For beams, slabs, columns, all members subjected to bending" class="form-control" path="description"/>
				                                            <p class="help-block">Add more details about this proportion</p>
				                                            <br/>
				                                            
				                                            <h4>Ratio</h4>
				                                            <label>Cement Ratio</label>
				                                            <form:input type="text" placeholder="Sample: 1" class="form-control" path="ratioCement"/>
				                                            <p class="help-block">Specify the cement part of the proportion</p>
				                                            
				                                            <label>Sand Ratio</label>
				                                            <form:input type="text" placeholder="Sample: 2" class="form-control" path="ratioSand"/>
				                                            <p class="help-block">Specify the sand part of the proportion</p>
				                                            
				                                            <label>Gravel Ratio</label>
				                                            <form:input type="text" placeholder="Sample: 4" class="form-control" path="ratioGravel"/>
				                                            <p class="help-block">Specify the gravel part of the proportion</p>
				                                            <br/>
				                                            
				                                            <h4>Value and Result Unit</h4>
				                                            <label>Cement (40kg)</label>
				                                            <form:input type="text" placeholder="Sample: 4TODO" class="form-control" path="partCement40kg"/>
				                                            <form:select class="form-control" path="unitKeyCement40kgUnit"> 
								           						<c:forEach items="${unitList}" var="unit"> 
								           							<form:option value="${unit.getKey()}" label="${unit.name}"/> 
								           						</c:forEach>
								                 			</form:select>
				                                            <p class="help-block">Specify the gravel part of the proportion</p>
				                                            
				                                            <label>Cement (50kg)</label>
				                                            <form:input type="text" placeholder="Sample: 4TODO" class="form-control" path="partCement50kg"/>
				                                            <form:select class="form-control" path="unitKeyCement50kgUnit"> 
								           						<c:forEach items="${unitList}" var="unit"> 
								           							<form:option value="${unit.getKey()}" label="${unit.name}"/> 
								           						</c:forEach>
								                 			</form:select>
				                                            <p class="help-block">Specify the gravel part of the proportion</p>
				                                            
				                                            <label>Sand</label>
				                                            <form:input type="text" placeholder="Sample: 4TODO" class="form-control" path="partSand"/>
				                                            <form:select class="form-control" path="unitKeySandUnit"> 
								           						<c:forEach items="${unitList}" var="unit"> 
								           							<form:option value="${unit.getKey()}" label="${unit.name}"/> 
								           						</c:forEach>
								                 			</form:select>
				                                            <p class="help-block">Specify the gravel part of the proportion</p>
				                                            
				                                            <label>Gravel</label>
				                                            <form:input type="text" placeholder="Sample: 4TODO" class="form-control" path="partGravel"/>
				                                            <form:select class="form-control" path="unitKeyGravelUnit"> 
								           						<c:forEach items="${unitList}" var="unit"> 
								           							<form:option value="${unit.getKey()}" label="${unit.name}"/> 
								           						</c:forEach>
								                 			</form:select>
				                                            <p class="help-block">Specify the gravel part of the proportion</p>
				                                        </div>
				                                    </form:form>
			                                        <c:if test="${isUpdating}">
                                            		<button onclick="submitForm('detailsForm')" class="btn btn-cebedo-update btn-flat btn-sm" id="detailsButton">Update</button>
			                                        </c:if>
			                                        <c:if test="${!isUpdating}">
                                            		<button onclick="submitForm('detailsForm')" class="btn btn-cebedo-create btn-flat btn-sm" id="detailsButton">Create</button>
			                                        </c:if>
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
</body>
<script>
function submitForm(id) {
	$('#'+id).submit();
}
</script>
</html>