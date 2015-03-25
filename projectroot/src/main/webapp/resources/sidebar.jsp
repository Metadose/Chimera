<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!-- Left side column. contains the logo and sidebar -->
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<sec:authentication var="authUser" property="user"/>
<sec:authentication var="authStaff" property="staff"/>
<script type="text/javascript">
function logout(){
	document.getElementById('logoutForm').submit();
}
</script>
<aside class="left-side sidebar-offcanvas">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- Sidebar user panel -->
        <div class="user-panel">
            <div class="pull-left image">
            	<c:choose>
				<c:when test="${!empty authStaff.thumbnailURL}">
					<img src="${contextPath}/image/display/staff/profile/?staff_id=${authStaff.id}" class="img-circle" alt="User Image" />
				</c:when>
				<c:when test="${empty authStaff.thumbnailURL}">
					<img src="<c:url value="/resources/img/avatar5.png" />" class="img-circle" alt="User Image" />
				</c:when>
				</c:choose>
            </div>
            <div class="pull-left info">
            	<c:choose>
            	 	<c:when test="${!empty authStaff}">
            	 		<c:set var="staffName" value="${authStaff.prefix} ${authStaff.firstName} ${authStaff.middleName} ${authStaff.lastName} ${authStaff.suffix}"/>
            	 		<p>Hello, ${staffName}</p>
            	 		<h6>${authStaff.companyPosition}</h6>
            	 	</c:when>
            	 	<c:when test="${empty authStaff}">
            	 		<p>Hello, ${authUser.username}</p>
            	 		<h6>No Staff for this User.</h6>
            	 	</c:when>
            	</c:choose>
            </div>
        </div>
        <!-- search form -->
<!--         <form action="#" method="post" class="sidebar-form"> -->
<%-- 				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> --%>
<!--             <div class="input-group"> -->
<!--                 <input type="text" name="q" class="form-control" placeholder="Search..."/> -->
<!--                 <span class="input-group-btn"> -->
<!--                     <button type='submit' name='search' id='search-btn' class="btn btn-flat"><i class="fa fa-search"></i></button> -->
<!--                 </span> -->
<!--             </div> -->
<!--         </form> -->
        <!-- /.search form -->
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">
            <li class="active">
                <a href="index.html">
                    <i class="fa fa-dashboard"></i> <span>Dashboard</span>
                </a>
            </li>
            <sec:authorize access="hasRole('ACCESS_PROJECT')">
            <li>
                <a href="${contextPath}/project/list/">
                    <i class="fa fa-folder"></i> <span>Projects</span>
                </a>
            </li>
            </sec:authorize>
            <sec:authorize access="hasRole('ACCESS_PROJECTFILE')">
            <li>
                <a href="${contextPath}/projectfile/list/">
                    <i class="fa fa-file"></i> <span>Files</span>
                </a>
            </li>
            </sec:authorize>
            <sec:authorize access="hasRole('ACCESS_TASK')">
            <li>
                <a href="${contextPath}/task/list/">
                    <i class="fa fa-tasks"></i> <span>Tasks</span>
                </a>
            </li>
            </sec:authorize>
<!--             <li> -->
<!--                 <a href="pages/calendar.html"> -->
<!--                     <i class="fa fa-calendar"></i> <span>Calendar</span> -->
<!--                 </a> -->
<!--             </li> -->
			<sec:authorize access="hasRole('ACCESS_TEAM')">
            <li>
                <a href="${contextPath}/team/list/">
                    <i class="fa fa-users"></i> <span>Teams</span>
<!--                     <small class="badge pull-right bg-green">new</small> -->
                </a>
            </li>
            </sec:authorize>
            <sec:authorize access="hasRole('ACCESS_STAFF')">
            <li>
                <a href="${contextPath}/staff/list/">
                    <i class="fa fa-user"></i> <span>Staff</span>
                </a>
            </li>
            </sec:authorize>
            <sec:authorize access="hasRole('ACCESS_SYSTEMUSER')">
            <li>
                <a href="${contextPath}/systemuser/list/">
                    <i class="fa fa-user"></i> <span>Users</span>
                </a>
            </li>
            </sec:authorize>
            <c:if test="${authUser.superAdmin == true}">
            <li>
                <a href="${contextPath}/field/list/">
                    <i class="fa fa-list"></i> <span>Fields</span>
                </a>
            </li>
            </c:if>
<!--             <li class="treeview"> -->
<!--                 <a href="#"> -->
<!--                     <i class="fa fa-edit"></i> -->
<!--                     <span>Fields</span> -->
<!--                     <i class="fa fa-angle-left pull-right"></i> -->
<!--                 </a> -->
<!--                 <ul class="treeview-menu"> -->
<!--                     <li><a href="pages/UI/general.html"><i class="fa fa-angle-double-right"></i> Fields List</a></li> -->
<!--                     <li><a href="pages/UI/icons.html"><i class="fa fa-angle-double-right"></i> Assign Fields</a></li> -->
<!--                 </ul> -->
<!--             </li> -->
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-wrench"></i>
                    <span>Settings</span>
                    <i class="fa fa-angle-left pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li><a href="pages/UI/general.html"><i class="fa fa-angle-double-right"></i> Update Profile</a></li>
                    <li><a href="pages/UI/icons.html"><i class="fa fa-angle-double-right"></i> Change Password</a></li>
                </ul>
            </li>
            <c:if test="${authUser.superAdmin == true}">
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-laptop"></i>
                    <span>Super User</span>
                    <i class="fa fa-angle-left pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li><a href="${contextPath}/company/list/"><i class="fa fa-angle-double-right"></i> Companies</a></li>
                    <li><a href="pages/UI/icons.html"><i class="fa fa-angle-double-right"></i> Licenses</a></li>
                    <li><a href="${contextPath}/config/list"><i class="fa fa-angle-double-right"></i> System Configuration</a></li>
                </ul>
            </li>
            </c:if>
            <li>	
            	<form id="logoutForm" action="${contextPath}/auth/logout" method="post">
            		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            	</form>
                <a href="#" onclick="return logout();">
                    <i class="fa fa-sign-out"></i> <span>Logout</span>
                </a>
            </li>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>