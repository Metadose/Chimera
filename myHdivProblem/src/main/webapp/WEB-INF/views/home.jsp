<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
	<title>Home</title>
</head>
<body>
<form:form
	modelAttribute="myBean" 
	method="post"
	action="/demo/sendForm">
	
	
	<form:select path="staffID">
		<c:forEach items="${optionList}" var="staff">
		<form:option value="${staff.id}" label="${staff.name}"/>
		</c:forEach>
	</form:select>

	<form:input type="text" path="position"/>

	<button class="btn btn-default btn-flat btn-sm">Assign</button>
</form:form>

</body>
</html>
