<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Left side column. contains the logo and sidebar -->
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
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
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">
            <li class="active">
            	<c:url var="urlDashboard" value="/dashboard/"/>
                <a href="${urlDashboard}">
                    <i class="fa fa-dashboard"></i> <span>Dashboard</span>
                </a>
            </li>
            <li>
            	<c:url var="urlProjectList" value="/project/list/"/>
                <a href="${urlProjectList}">
                    <i class="fa fa-building"></i> <span>Projects</span>
                </a>
            </li>
            <li>
            	<c:url var="urlStaffList" value="/staff/list/"/>
                <a href="${urlStaffList}">
                    <i class="fa fa-user"></i> <span>Company Staff</span>
                </a>
            </li>
            <li>
            	<c:url var="urlSystemUserList" value="/systemuser/list/"/>
                <a href="${urlSystemUserList}">
                    <i class="fa fa-male"></i> <span>User Accounts</span>
                </a>
            </li>
            <li>
            	<c:url var="urlList" value="/shape/list/"/>
                <a href="${urlList}">
                    <i class="fa fa-cube"></i> <span>Shapes</span>
                </a>
            </li>
            
            <c:if test="${authUser.superAdmin == true}">
            <li>
            	<c:url var="urlFieldList" value="/field/list/"/>
                <a href="${urlFieldList}">
                    <i class="fa fa-list"></i> <span>Fields</span>
                </a>
            </li>
            </c:if>
<%--             <c:if test="${authUser.superAdmin == true}"> --%>
HIDE TODO
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
<%--             </c:if> --%>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>