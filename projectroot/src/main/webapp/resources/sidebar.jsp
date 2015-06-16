<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!-- Left side column. contains the logo and sidebar -->
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<sec:authentication var="authUser" property="user"/>
<sec:authentication var="authStaff" property="staff"/>
<style>
.autocomplete-suggestions { background: #FFF; overflow: auto; }
.autocomplete-suggestion { padding: 5px 5px; white-space: nowrap; overflow: hidden;}
.autocomplete-no-suggestion { display: block; text-align:center; font-size: medium; background: #F0F0F0; }
.autocomplete-selected { background: #F0F0F0; }
.autocomplete-suggestions strong { font-weight: bold; color: #3c8dbc; }
.autocomplete-group strong { display: block; text-align:center; font-size: medium; background: #F0F0F0; }
</style>
<script type="text/javascript">
String.prototype.capitalize = function() {
    return this.charAt(0).toUpperCase() + this.slice(1);
}

function logout(){
	document.getElementById('logoutForm').submit();
}

$(document).ready(function() {
	$('#searchField').autocomplete({
		serviceUrl: '${contextPath}/search/',
		paramName: "searchInput",
		delimiter: ",",
		forceFixPosition: true,
		showNoSuggestionNotice: true,
		noSuggestionNotice: "<h5><i>No results</i></h5>",
 		minChars: 3,
		groupBy: 'objectName',
		transformResult: function(response) {
			return {
				// Must convert json to javascript object before process.
				suggestions: $.map($.parseJSON(response), function(item) {
					return { 
						value: item.text,
						href: '${contextPath}/' + item.objectName + '/edit/' + item.objectID,
						data: { objectName : item.objectName.capitalize(), id : item.id }
					};
				})
			};
		},
		onSelect: function (suggestion) {
	        window.location.href = suggestion.href;
	    }
	});
});
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
            	 		<p>Hello, ${authStaff.getFullName()}</p>
            	 		<h6>(${authUser.username})</h6>
            	 	</c:when>
            	 	<c:when test="${empty authStaff}">
            	 		<p>Hello, ${authUser.username}</p>
            	 		<h6>No Staff for this User.</h6>
            	 	</c:when>
            	</c:choose>
            </div>
        </div>
        <!-- search form -->
<!--         <form action="#" id="sidebar-search-form" method="post" class="sidebar-form"> -->
<%-- 			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> --%>
<!--             <div class="input-group"> -->
<!--                 <input type="text" id="searchField" name="search" class='form-control' placeholder="Search..."/> -->
<!--                 <span class="input-group-btn"> -->
<!--                     <button type='submit' name='search' id='search-btn' class="btn btn-flat"><i class="fa fa-search"></i></button> -->
<!--                 </span> -->
<!--             </div> -->
<!--         </form> -->
        <!-- /.search form -->
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">
            <li class="active">
            	<c:url var="urlDashboard" value="/dashboard/"/>
                <a href="${urlDashboard}">
                    <i class="fa fa-dashboard"></i> <span>Dashboard</span>
                </a>
            </li>
            <sec:authorize access="hasRole('ACCESS_PROJECT')">
            <li>
            	<c:url var="urlProjectList" value="/project/list/"/>
                <a href="${urlProjectList}">
                    <i class="fa fa-folder"></i> <span>Projects</span>
                </a>
            </li>
            </sec:authorize>
            <sec:authorize access="hasRole('ACCESS_STAFF')">
            <li>
            	<c:url var="urlStaffList" value="/staff/list/"/>
                <a href="${urlStaffList}">
                    <i class="fa fa-user"></i> <span>Staff</span>
                </a>
            </li>
            </sec:authorize>
            <sec:authorize access="hasRole('ROLE_SYSTEMUSER_EDITOR')">
            <li>
            	<c:url var="urlSystemUserList" value="/systemuser/list/"/>
                <a href="${urlSystemUserList}">
                    <i class="fa fa-male"></i> <span>Users</span>
                </a>
            </li>
            </sec:authorize>
<%--             <sec:authorize access="hasRole('ACCESS_TEAM')"> --%>
<!--             <li> -->
<%--             	<c:url var="urlTeamList" value="/team/list/"/> --%>
<%--                 <a href="${urlTeamList}"> --%>
<!--                     <i class="fa fa-users"></i> <span>Teams</span> -->
<!--                 </a>  -->
<!--             </li> -->
<%--             </sec:authorize> --%>
            <sec:authorize access="hasRole('ACCESS_TASK')">
            <li>
            	<c:url var="urlTaskList" value="/task/list/"/>
                <a href="${urlTaskList}">
                    <i class="fa fa-tasks"></i> <span>Tasks</span>
                </a>
            </li>
            </sec:authorize>
            <li>
            	<c:url var="urlFormulaList" value="/formula/list/"/>
                <a href="${urlFormulaList}">
                    <i class="fa fa-connectdevelop"></i> <span>Formulas</span>
                </a>
            </li>
<%--             <sec:authorize access="hasRole('ACCESS_PROJECTFILE')"> --%>
<!--             <li> -->
<%--             	<c:url var="urlProjFileList" value="/projectfile/list/"/> --%>
<%--                 <a href="${urlProjFileList}"> --%>
<!--                     <i class="fa fa-file"></i> <span>Files</span> -->
<!--                 </a> -->
<!--             </li> -->
<%--             </sec:authorize> --%>
<!--             <li> -->
<!--                 <a href="pages/calendar.html"> -->
<!--                     <i class="fa fa-calendar"></i> <span>Calendar</span> -->
<!--                 </a> -->
<!--             </li> -->
            <c:if test="${authUser.superAdmin == true}">
            <li>
            	<c:url var="urlFieldList" value="/field/list/"/>
                <a href="${urlFieldList}">
                    <i class="fa fa-list"></i> <span>Fields</span>
                </a>
            </li>
            </c:if>
            <c:if test="${authUser.superAdmin == true}">
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-laptop"></i>
                    <span>Super User</span>
                    <i class="fa fa-angle-left pull-right"></i>
                </a>
                <ul class="treeview-menu">
                	<c:url var="urlCompanyList" value="/company/list/"/>
                    <li><a href="${urlCompanyList}"><i class="fa fa-angle-double-right"></i> Companies</a></li>
                    
                    <c:url var="urlLogList" value="/log/list"/>
                    <li><a href="${urlLogList}"><i class="fa fa-angle-double-right"></i> Logs</a></li>
                    
                    <li><a href="pages/UI/icons.html"><i class="fa fa-angle-double-right"></i> Licenses</a></li>
                    
                    <c:url var="urlConfigList" value="/config/list"/>
                    <li><a href="${urlConfigList}"><i class="fa fa-angle-double-right"></i> System Configuration</a></li>
                </ul>
            </li>
            </c:if>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>