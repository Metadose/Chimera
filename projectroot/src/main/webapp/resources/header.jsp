<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<sec:authentication var="authStaff" property="staff"/>
<sec:authentication var="authUser" property="user"/>
<c:choose>
	<c:when test="${!empty authStaff}">
		<c:set var="staffName" value="${authStaff.prefix} ${authStaff.firstName} ${authStaff.middleName} ${authStaff.lastName} ${authStaff.suffix}"/>
		<c:set var="companyPosition" value="${authStaff.companyPosition}"/>
	</c:when>
	<c:when test="${empty authStaff}">
		<c:set var="staffName" value="${authUser.username}"/>
		<c:set var="companyPosition" value="(No Staff)"/>
	</c:when>
</c:choose>
<!-- header logo: style can be found in header.less -->
<c:import url="/resources/js-includes.jsp" />
<c:import url="/resources/css-includes.jsp" />
<style>
	.ui-widget {
		font-family: inherit !important;
		font-size: inherit !important;
	}
</style>
<header class="header">
    <a href="index.html" class="logo">
        <!-- Add the class icon to your logo image or logo icon to add the margining -->
        AdminLTE
    </a>
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top" role="navigation">
        <!-- Sidebar toggle button-->
        <a href="#" class="navbar-btn sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </a>
        <div class="navbar-right">
            <ul class="nav navbar-nav">
			    
                <!-- Messages: style can be found in dropdown.less-->
                <li class="dropdown messages-menu">
                	<c:url value="/message/view/0" var="urlSeeAllMessages"/>
                    <a href="${urlSeeAllMessages}" class="dropdown-toggle">
                        <i class="fa fa-envelope"></i>
                        <span class="label label-success">4</span>
                    </a>
<!--                     <ul class="dropdown-menu"> -->
<!--                         <li class="header">You have 4 messages</li> -->
<!--                         <li> -->
<!--                             inner menu: contains the actual data -->
<!--                             <ul class="menu"> -->
<!--                                 <li>start message -->
<!--                                     <a href="#"> -->
<!--                                         <div class="pull-left"> -->
<%--                                             <img src="<c:url value="/resources/img/avatar2.png" />" class="img-circle" alt="User Image"/> --%>
<!--                                         </div> -->
<!--                                         <h4> -->
<!--                                             Support Team -->
<!--                                             <small><i class="fa fa-clock-o"></i> 5 mins</small> -->
<!--                                         </h4> -->
<!--                                         <p>Why not buy a new awesome theme?</p> -->
<!--                                     </a> -->
<!--                                 </li>end message -->
<!--                             </ul> -->
<!--                         </li> -->
<%--                         <c:url value="/message/view/0" var="urlSeeAllMessages"/> --%>
<%--                         <li class="footer"><a href="${urlSeeAllMessages}">See All Messages</a></li> --%>
<!--                     </ul> -->
                </li>
                <!-- Notifications: style can be found in dropdown.less -->
                <li class="dropdown notifications-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-warning"></i>
                        <span class="label label-warning">10</span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="header">You have 10 notifications</li>
                        <li>
                            <!-- inner menu: contains the actual data -->
                            <ul class="menu">
                                <li>
                                    <a href="#">
                                        <i class="ion ion-ios7-people info"></i> 5 new members joined today
                                    </a>
                                </li>
                                <li>
                                    <a href="#">
                                        <i class="fa fa-warning danger"></i> Very long description here that may not fit into the page and may cause design problems
                                    </a>
                                </li>
                                <li>
                                    <a href="#">
                                        <i class="fa fa-users warning"></i> 5 new members joined
                                    </a>
                                </li>

                                <li>
                                    <a href="#">
                                        <i class="ion ion-ios7-cart success"></i> 25 sales made
                                    </a>
                                </li>
                                <li>
                                    <a href="#">
                                        <i class="ion ion-ios7-person danger"></i> You changed your username
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li class="footer"><a href="#">View all</a></li>
                    </ul>
                </li>
                <!-- Tasks: style can be found in dropdown.less -->
                <li class="dropdown tasks-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-tasks"></i>
                        <span class="label label-danger">9</span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="header">You have 9 tasks</li>
                        <li>
                            <!-- inner menu: contains the actual data -->
                            <ul class="menu">
                                <li><!-- Task item -->
                                    <a href="#">
                                        <h3>
                                            Design some buttons
                                            <small class="pull-right">20%</small>
                                        </h3>
                                        <div class="progress xs">
                                            <div class="progress-bar progress-bar-aqua" style="width: 20%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                <span class="sr-only">20% Complete</span>
                                            </div>
                                        </div>
                                    </a>
                                </li><!-- end task item -->
                                <li><!-- Task item -->
                                    <a href="#">
                                        <h3>
                                            Create a nice theme
                                            <small class="pull-right">40%</small>
                                        </h3>
                                        <div class="progress xs">
                                            <div class="progress-bar progress-bar-green" style="width: 40%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                <span class="sr-only">40% Complete</span>
                                            </div>
                                        </div>
                                    </a>
                                </li><!-- end task item -->
                                <li><!-- Task item -->
                                    <a href="#">
                                        <h3>
                                            Some task I need to do
                                            <small class="pull-right">60%</small>
                                        </h3>
                                        <div class="progress xs">
                                            <div class="progress-bar progress-bar-red" style="width: 60%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                <span class="sr-only">60% Complete</span>
                                            </div>
                                        </div>
                                    </a>
                                </li><!-- end task item -->
                                <li><!-- Task item -->
                                    <a href="#">
                                        <h3>
                                            Make beautiful transitions
                                            <small class="pull-right">80%</small>
                                        </h3>
                                        <div class="progress xs">
                                            <div class="progress-bar progress-bar-yellow" style="width: 80%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                <span class="sr-only">80% Complete</span>
                                            </div>
                                        </div>
                                    </a>
                                </li><!-- end task item -->
                            </ul>
                        </li>
                        <li class="footer">
                            <a href="#">View all tasks</a>
                        </li>
                    </ul>
                </li>
                <!-- User Account: style can be found in dropdown.less -->
                <li class="dropdown user user-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="glyphicon glyphicon-user"></i>
            	 		<span>${staffName} <i class="caret"></i></span>
                    </a>
                    <ul class="dropdown-menu">
                        <!-- User image -->
                        <li class="user-header bg-light-blue">
                        	<c:choose>
							<c:when test="${!empty authStaff.thumbnailURL}">
								<img src="${contextPath}/image/display/staff/profile/?staff_id=${authStaff.id}" class="img-circle" alt="User Image" />
							</c:when>
							<c:when test="${empty authStaff.thumbnailURL}">
								<img src="<c:url value="/resources/img/avatar5.png" />" class="img-circle" alt="User Image" />
							</c:when>
							</c:choose>
                            <p>
		            	 		${staffName} - ${companyPosition}
                                <small>Member since Nov. 2012</small>
                            </p>
                        </li>
                        <!-- Menu Body -->
                        <li class="user-body">
                            <div class="col-xs-4 text-center">
                                <a href="${contextPath}/systemuser/changepassword">Change Password</a>
                            </div>
                            <div class="col-xs-4 text-center">
                                <a href="#">Sales</a>
                            </div>
                            <div class="col-xs-4 text-center">
                                <a href="#">Friends</a>
                            </div>
                        </li>
                        <!-- Menu Footer-->
                        <li class="user-footer">
                            <div class="pull-left">
                                <a href="#" class="btn btn-default btn-flat">Profile</a>
                            </div>
                            <div class="pull-right">
				            	<c:url var="urlLogout" value="/auth/logout"/> 
                                <a href="${urlLogout}" onclick="return logout();" class="btn btn-default btn-flat">Logout</a>
                            </div>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </nav>
</header>