<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Left side column. contains the logo and sidebar -->
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<aside class="left-side sidebar-offcanvas">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- Sidebar user panel -->
        <div class="user-panel">
            <div class="pull-left image">
                <img src="<c:url value="/resources/img/avatar2.png" />" class="img-circle" alt="User Image" />
            </div>
            <div class="pull-left info">
                <p>Hello, Jane</p>

                <h6>Engineer</h6>
            </div>
        </div>
        <!-- search form -->
<!--         <form action="#" method="get" class="sidebar-form"> -->
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
            <li>
                <a href="${contextPath}/project/list/">
                    <i class="fa fa-folder"></i> <span>Projects</span>
                </a>
            </li>
            <li>
                <a href="${contextPath}/projectfile/list/">
                    <i class="fa fa-file"></i> <span>Files</span>
                </a>
            </li>
            <li>
                <a href="${contextPath}/task/list/">
                    <i class="fa fa-tasks"></i> <span>Tasks</span>
                </a>
            </li>
            <li>
                <a href="pages/calendar.html">
                    <i class="fa fa-calendar"></i> <span>Calendar</span>
<!--                     <small class="badge pull-right bg-red">3</small> -->
                </a>
            </li>
            <li>
                <a href="${contextPath}/team/list/">
                    <i class="fa fa-users"></i> <span>Teams</span>
<!--                     <small class="badge pull-right bg-green">new</small> -->
                </a>
            </li>
            <li>
                <a href="${contextPath}/staff/list/">
                    <i class="fa fa-user"></i> <span>Staff</span>
                </a>
            </li>
            <li>
                <a href="${contextPath}/systemuser/list/">
                    <i class="fa fa-user"></i> <span>Users</span>
                </a>
            </li>
            <li>
                <a href="${contextPath}/field/list/">
                    <i class="fa fa-list"></i> <span>Fields</span>
                </a>
            </li>
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
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-laptop"></i>
                    <span>Super User</span>
                    <i class="fa fa-angle-left pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li><a href="pages/UI/general.html"><i class="fa fa-angle-double-right"></i> Companies</a></li>
                    <li><a href="pages/UI/icons.html"><i class="fa fa-angle-double-right"></i> Licenses</a></li>
                    <li><a href="${contextPath}/config/list"><i class="fa fa-angle-double-right"></i> System Configuration</a></li>
                </ul>
            </li>
            <li>
                <a href="${contextPath}/auth/logout">
                    <i class="fa fa-sign-out"></i> <span>Logout</span>
<!--                     <small class="badge pull-right bg-yellow">12</small> -->
                </a>
            </li>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>