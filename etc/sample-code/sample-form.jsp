<form:form modelAttribute="estimationInput"
	action="${contextPath}/project/create/estimate"
	method="post"
	enctype="multipart/form-data">

	<div class="form-group">
	
	<label>Name</label>
	<form:input placeholder="TODO" class="form-control" path="name"/><br/>													

	<label>Excel File</label>
	<form:input type="file" class="form-control" path="estimationFile"/><br/>
	
	<label>Estimation Allowance</label>
	<form:select class="form-control" path="estimationAllowance"> 
		<c:forEach items="${allowanceList}" var="allowance"> 
			<form:option value="${allowance}" label="${allowance.getLabel()}"/> 
		</c:forEach> 
	</form:select><br/>
	
	<label>Remarks</label>
	<form:input placeholder="TODO" class="form-control" path="remarks"/><br/>
	
	<button class="btn btn-cebedo-create btn-flat btn-sm">Estimate</button>
	</div>
</form:form>