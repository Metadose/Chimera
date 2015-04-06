<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="bg-black">
<head>
	<meta charset="UTF-8">
	<title>PMSys | Log In</title>
	<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
	<link href="${contextPath}/resources/lib/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="${contextPath}/resources/lib/font-awesome-4.3.0/css/font-awesome.css" rel="stylesheet" type="text/css" />
	<!-- Theme style -->
	<link href="<c:url value="/resources/css/AdminLTE.css" />"rel="stylesheet" type="text/css" />
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
	  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	  <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
	<![endif]-->
</head>
<body class="bg-black">
        <div class="form-box" id="login-box">
            <div class="header">Log In</div>
            <form:form action="${contextPath}/j_spring_security_check" method="post">
                <div class="body bg-gray">
                    <div class="form-group">
                        <input type="text" name="j_username" id="j_username" class="form-control" placeholder="Username"/>
                    </div>
                    <div class="form-group">
                        <input type="password" name="j_password" id="j_password" class="form-control" placeholder="Password"/>
                    </div>
                    <div class="form-group">
                        <input type="checkbox" name="remember_me"/> Remember me
                    </div>
                </div>
                <div class="footer">
                    <button type="submit" class="btn bg-olive btn-block">Log me in</button>

                    <p><a href="#">I forgot my password</a></p>

                    <a href="register.html" class="text-center">Register a new membership</a>
                </div>
            </form:form>
<!--             <div class="margin text-center"> -->
<!--                 <span>Sign in using social networks</span> -->
<!--                 <br/> -->
<!--                 <button class="btn bg-light-blue btn-circle"><i class="fa fa-facebook"></i></button> -->
<!--                 <button class="btn bg-aqua btn-circle"><i class="fa fa-twitter"></i></button> -->
<!--                 <button class="btn bg-red btn-circle"><i class="fa fa-google-plus"></i></button> -->

<!--             </div> -->
        </div>
        <script src="${contextPath}/resources/lib/jquery.min.js"/>
		<script src="${contextPath}/resources/lib/bootstrap.min.js" type="text/javascript"/>
    </body>
</html>