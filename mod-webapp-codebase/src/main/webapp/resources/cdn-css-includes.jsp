<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="authTheme" property="theme"/>
<sec:authentication var="authCdnUrl" property="cdnUrl"/>

<%-- <link href="<c:url value="/resources/lib/bootstrap.min.css" />"rel="stylesheet" type="text/css" /> --%>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />

<%-- <link href="<c:url value="/resources/lib/font-awesome-4.3.0/css/font-awesome.css" />"rel="stylesheet" type="text/css" /> --%>
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />

<%-- <link href="<c:url value="/resources/css/datepicker/datepicker3.css" />"rel="stylesheet" type="text/css" /> --%>
<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.5.0/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css" />

<%-- <link href="<c:url value="/resources/css/fonts.css" />"rel="stylesheet" type="text/css" /> --%>
<%-- <link href="<c:url value="/resources/css/cdn-fonts.css" />"rel="stylesheet" type="text/css" /> --%>
<link href="${authCdnUrl}/resources/css/cdn-fonts.css" rel="stylesheet" type="text/css" />

<%-- <link href="<c:url value="/resources/css/datatables/dataTables.bootstrap.css" />"rel="stylesheet" type="text/css" /> --%>
<link href="${authCdnUrl}/resources/css/datatables/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />

<!-- Themes -->
<%-- <link href="<c:url value="/resources/css/themes/${authTheme}-dist.css" />"rel="stylesheet" type="text/css" /> --%>
<link href="${authCdnUrl}/resources/css/themes/${authTheme}-dist.css" rel="stylesheet" type="text/css" />

<%-- <link href="<c:url value="/resources/css/themes/${authTheme}-buttons.css" />"rel="stylesheet" type="text/css" /> --%>
<link href="${authCdnUrl}/resources/css/themes/${authTheme}-buttons.css" rel="stylesheet" type="text/css" />

<%-- <link href="<c:url value="/resources/css/cebedo-commons.css" />"rel="stylesheet" type="text/css" /> --%>
<link href="${authCdnUrl}/resources/css/cebedo-commons.css" rel="stylesheet" type="text/css" />

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
<![endif]-->
<!-- <style>
body { font-size: 120%; }
</style> -->


<!-- We're not using the JQuery UI css -->
<!-- <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/themes/smoothness/jquery-ui.css"> -->
