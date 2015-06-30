<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<c:set value="${false}" var="isUpdating"/>
	<c:choose>
   	<c:when test="${empty chb.uuid}">
    	<title>Concrete Hollow Blocks Create</title>
   	</c:when>
   	<c:when test="${!empty chb.uuid}">
		<title>Concrete Hollow Blocks Edit</title>
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
		            	New Concrete Hollow Block Measurement
		                <small>Create CHB Measurement</small>
	            	</c:when>
	            	<c:when test="${isUpdating}">
	            		${chb.name}
		                <small>Edit CHB Measurement</small>
	            	</c:when>
	            	</c:choose>
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
                   								<div class="box-header">
                   									<h3 class="box-title">Details</h3>
                   								</div>
                   								<div class="box-body">
                   									<div class="callout callout-info callout-cebedo">
									                    <p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
									                </div>
                   									<form:form modelAttribute="chb"
														id="detailsForm"
														method="post"
														action="${contextPath}/chb/create">
				                                        <div class="form-group">
				                                            
				                                            <label>Name</label>
				                                            <form:input type="text" placeholder="Sample: 40kg Bags, Cubic Meters, Tons" class="form-control" path="name"/>
				                                            <p class="help-block">TODO Enter the chb of measure</p>
				                                            
				                                            <label>Description</label>
				                                            <form:input type="text" placeholder="Sample: Typically used as a chb for measuring cement" class="form-control" path="description"/>
				                                            <p class="help-block">TODO Add more details about this chb</p>
				                                            
				                                            <label>Length</label>
				                                            <form:input type="text" placeholder="Sample: Typically used as a chb for measuring cement" class="form-control" path="length"/>
				                                            <form:select class="form-control" path="lengthUnit"> 
	                                     						<c:forEach items="${commonLengthUnitList}" var="commonUnit"> 
	                                     							<form:option value="${commonUnit}" label="${commonUnit.label()}"/> 
	                                     						</c:forEach> 
	 		                                    			</form:select>
				                                            <p class="help-block">TODO Add more details about this chb</p>
				                                            
				                                            <label>Height</label>
				                                            <form:input type="text" placeholder="Sample: Typically used as a chb for measuring cement" class="form-control" path="height"/>
				                                            <form:select class="form-control" path="heightUnit"> 
	                                     						<c:forEach items="${commonLengthUnitList}" var="commonUnit"> 
	                                     							<form:option value="${commonUnit}" label="${commonUnit.label()}"/> 
	                                     						</c:forEach> 
	 		                                    			</form:select>
				                                            <p class="help-block">TODO Add more details about this chb</p>
				                                            
				                                            <label>Thickness</label>
				                                            <form:input type="text" placeholder="Sample: Typically used as a chb for measuring cement" class="form-control" path="thickness"/>
				                                            <form:select class="form-control" path="thicknessUnit"> 
	                                     						<c:forEach items="${commonLengthUnitList}" var="commonUnit"> 
	                                     							<form:option value="${commonUnit}" label="${commonUnit.label()}"/> 
	                                     						</c:forEach> 
	 		                                    			</form:select>
				                                            <p class="help-block">TODO Add more details about this chb</p>
				                                            
				                                            <c:if test="${!empty chb.uuid}">
				                                            <label>CHB per Sq. Meter</label>
				                                            ${chb.getPerSqM()}
				                                            <p class="help-block">TODO Add more details about this chb</p>
				                                            </c:if>
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