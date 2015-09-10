<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<sec:authentication var="authStaff" property="staff"/>
<sec:authentication var="authUser" property="user"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:choose>
	<c:when test="${!empty authStaff}">
		<c:set var="staffName" value="${authStaff.getFullName()}"/>
		<c:set var="companyPosition" value="${authStaff.companyPosition}"/>
		<c:if test="${empty companyPosition}">
			<c:set var="companyPosition" value="(Position Not Set)"/>
		</c:if>
	</c:when>
	<c:when test="${empty authStaff}"> 
		<c:set var="staffName" value="${authUser.username}"/>
		<c:set var="companyPosition" value="(No Staff)"/>
	</c:when>
</c:choose>
<!-- header logo: style can be found in header.less -->
<c:import url="/resources/js-includes.jsp" />
<c:import url="/resources/css-includes.jsp" />

<style type="text/css">
#cover {position: fixed; height: 100%; width: 100%; top:0; left: 0; background: #FFFFFF; z-index:10;}
ul li {     
    list-style:none;
    margin-bottom: 0px !important;
}
.navbar-nav > .user-menu > .dropdown-menu > li.user-header {
    height: 80px;
} 
</style>
<div id="cover"></div>
<script type="text/javascript">
$(window).on('load', function() {
   $("#cover").hide();
});
</script>

<header class="header">
    <a href="<c:url value="/dashboard/"/>" class="logo">
        <!-- Add the class icon to your logo image or logo icon to add the margining -->
        ART PROJECTS
    </a>
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top" role="navigation">
        <!-- Sidebar toggle button-->
<!--         <a href="#" class="navbar-btn sidebar-toggle" data-toggle="offcanvas" role="button"> -->
<!--             <span class="sr-only">Toggle navigation</span> -->
<!--             <span class="icon-bar"></span> -->
<!--             <span class="icon-bar"></span> -->
<!--             <span class="icon-bar"></span> -->
<!--         </a> -->
        <div class="navbar-right">
            <ul class="nav navbar-nav">
    <!--             <li class="active"> -->
    <%--                <c:url var="urlDashboard" value="/dashboard/"/> --%>
    <%--                 <a href="${urlDashboard}"> --%>
    <!--                     <i class="fa fa-dashboard"></i> <span>Dashboard</span> -->
    <!--                 </a> -->
    <!--             </li> -->
                <li class="active">
                    <c:url var="urlProjectList" value="/project/list/"/>
                    <a href="${urlProjectList}">
                        <i class="fa fa-building"></i> <span>Projects</span>
                    </a>
                </li>
                <c:if test="${authUser.superAdmin || authUser.companyAdmin}">
                <li>
                    <c:url var="urlSystemUserList" value="/systemuser/list/"/>
                    <a href="${urlSystemUserList}">
                        <i class="fa fa-male"></i> <span>User Accounts</span>
                    </a>
                </li>
                </c:if>
                <li>
                    <c:url var="urlStaffList" value="/staff/list/"/>
                    <a href="${urlStaffList}">
                        <i class="fa fa-user"></i> <span>Company Staff</span>
                    </a>
                </li>
                <c:if test="${authUser.superAdmin}">
                    <c:url var="urlCompanyList" value="/company/list/"/>
                    <li><a href="${urlCompanyList}"><i class="fa fa-angle-double-right"></i> Companies</a></li>
                    <c:url var="urlConfigList" value="/config/list"/>
                    <li><a href="${urlConfigList}"><i class="fa fa-angle-double-right"></i> System Configuration</a></li>
                </c:if>
			    
                <!-- User Account: style can be found in dropdown.less -->
                <li class="dropdown user user-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="glyphicon glyphicon-user"></i>
            	 		<span>${staffName} <i class="caret"></i></span>
                    </a>
                    <ul class="dropdown-menu">
                        <!-- User image -->
                        <li class="user-header bg-light-blue">
                            <p>
		            	 		${staffName}<br/>
                                <small>${companyPosition}</small>
                            </p>
                        </li>
                        <!-- Menu Body -->
                        <li class="user-body">
                            <div class="col-md-4 text-center">
                                <c:url value="/systemuser/edit/${authUser.id}" var="urlViewUser"/>
                                <a href="${urlViewUser}">
                                    User Account
                                </a>
                            </div>
                            <div class="col-md-4 text-center">
                            	<c:if test="${empty authStaff}">
                            		<c:url value="/staff/edit/0" var="urlViewStaff"/>
	                                <a href="${urlViewStaff}">
	                                    Create Staff
	                                </a>
                            	</c:if>
                            	<c:if test="${!empty authStaff}">
	                                <c:url value="/staff/edit/${authStaff.id}" var="urlViewStaff"/>
	                                <a href="${urlViewStaff}">
	                                    Staff Profile
	                                </a>
                            	</c:if>
                            </div>
                        </li>
                        <!-- Menu Footer-->
                        <li class="user-footer">
                            <div class="pull-right">
				            	<c:url var="urlLogout" value="/auth/logout"/> 
                                <a href="${urlLogout}" class="btn btn-default btn-flat">Logout</a>
                            </div>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </nav>
</header>