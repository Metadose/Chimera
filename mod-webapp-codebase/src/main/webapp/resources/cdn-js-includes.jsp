<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="authCdnUrl" property="cdnUrl"/>

<%-- <script src="<c:url value="/resources/lib/jquery.min.js" />"></script> --%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

<%-- <script src="<c:url value="/resources/lib/bootstrap.min.js" />"type="text/javascript"></script> --%>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>

<%-- <script src="<c:url value="/resources/lib/jquery-ui.min.js" />"type="text/javascript"></script> --%>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>

<%-- <script src="<c:url value="/resources/js/plugins/datepicker/bootstrap-datepicker.js" />"type="text/javascript"></script> --%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.5.0/js/bootstrap-datepicker.min.js"></script>

<%-- <script src="<c:url value="/resources/js/plugins/datatables/jquery.dataTables.js" />"type="text/javascript"></script> --%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.7/js/jquery.dataTables.min.js"></script>

<%-- <script src="<c:url value="/resources/js/plugins/datatables/dataTables.bootstrap.js" />"type="text/javascript"></script> --%>
<script src="${authCdnUrl}/resources/js/plugins/datatables/dataTables.bootstrap.js" type="text/javascript"></script>

<%-- <script src="<c:url value="/resources/lib/moment.min.js" />"></script> --%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.7.0/moment.min.js"></script>

<%-- <script src="<c:url value="/resources/js/common.js" />"type="text/javascript"></script> --%>
<script src="${authCdnUrl}/resources/js/common.js" type="text/javascript"></script>