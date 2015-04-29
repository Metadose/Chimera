<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<sec:authentication var="authUser" property="user"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>System Configuration ${action}</title>
		
	</head>
    <body class="skin-blue">
        <!-- header logo: style can be found in header.less -->
        <c:import url="/resources/header.jsp" />
        
        <div class="wrapper row-offcanvas row-offcanvas-left">
            <!-- Left side column. contains the logo and sidebar -->
            <c:import url="/resources/sidebar.jsp" />

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header no-margin">
                    <h1 class="text-center">
                        Mailbox
                    </h1>
                </section>

                <!-- Main content -->
                <section class="content">
                    <!-- MAILBOX BEGIN -->
                    <div class="mailbox row">
                        <div class="col-xs-12">
                            <div class="box box-solid">
                                <div class="box-body">
                                    <div class="row">
                                        <div class="col-md-3 col-sm-4">
                                            <!-- BOXES are complex enough to move the .box-header around.
                                                 This is an example of having the box header within the box body -->
                                            <div class="box-header">
                                                <i class="fa fa-inbox"></i>
                                                <h3 class="box-title">INBOX</h3>
                                            </div>
                                            <!-- compose message btn -->
                                            <a class="btn btn-block btn-flat btn-default" data-toggle="modal" data-target="#compose-modal"><i class="fa fa-pencil"></i> Compose Message</a>
                                            <!-- Navigation - folders-->
                                            <div style="margin-top: 15px;">
                                                <ul class="nav nav-pills nav-stacked">
<!--                                                     <li class="header">Folders</li> -->
<!--                                                     TODO Implement "active" <li class="active"><a href="#"><table> -->
													<c:forEach items="${conversations}" var="conversation">
													<c:forEach items="${conversation.contributors}" var="contributor">
													<c:if test="${contributor.id != authUser.id}">
													<c:url value="/message/view/${contributor.id}" var="urlViewMessage"/>
                                                    <li><a href="${urlViewMessage}"><table>
													<tr>
														<td>
														<div class="user-panel">
															<div>
																<c:choose>
	                                                			<c:when test="${!empty contributor.staff.thumbnailURL}">
	                                                				<img src="${contextPath}/image/display/staff/profile/?staff_id=${contributor.staff.id}" class="img-circle"/>
	                                                			</c:when>
	                                                			<c:when test="${empty contributor.staff.thumbnailURL}">
	                                                				<img src="${contextPath}/resources/img/avatar5.png" class="img-circle">
	                                                			</c:when>
		                                                		</c:choose>
															</div>
														</div>
														</td>
														<td>
															<c:choose>
			                                                <c:when test="${empty contributor.staff.getFullName()}">
			                                                	${contributor.username}
			                                                </c:when>
			                                                <c:when test="${!empty contributor.staff.getFullName()}">
				                                                ${contributor.staff.getFullName()}
			                                                </c:when>
			                                                </c:choose>
														</td>
													</tr>
													</table></a></li>
													</c:if>
													</c:forEach>
													</c:forEach>
													
<!--                                                     <li><a href="#"><i class="fa fa-pencil-square-o"></i> Drafts</a></li> -->
<!--                                                     <li><a href="#"><i class="fa fa-mail-forward"></i> Sent</a></li> -->
<!--                                                     <li><a href="#"><i class="fa fa-star"></i> Starred</a></li> -->
<!--                                                     <li><a href="#"><i class="fa fa-folder"></i> Junk</a></li> -->
                                                </ul>
                                            </div>
                                        </div><!-- /.col (LEFT) -->
                                        <div class="col-md-9 col-sm-8">
<!--                                             <div class="row pad"> -->
<!--                                                 <div class="col-sm-6 search-form"> -->
<!--                                                     <form action="#" class="text-right"> -->
<!--                                                         <div class="input-group"> -->
<!--                                                             <input type="text" class="form-control input-sm" placeholder="Search"> -->
<!--                                                             <div class="input-group-btn"> -->
<!--                                                                 <button type="submit" name="q" class="btn btn-sm btn-flat btn-primary"><i class="fa fa-search"></i></button> -->
<!--                                                             </div> -->
<!--                                                         </div> -->
<!--                                                     </form> -->
<!--                                                 </div> -->
<!--                                             </div>/.row -->

                                            <div class="table-responsive">
                                                <!-- Chat box -->
					                            <div class="box box-default">
					                                <div class="box-header">
					                                    <i class="fa fa-comments-o"></i>
					                                    <h3 class="box-title">Chat</h3>
<!-- 					                                    <div class="box-tools pull-right" data-toggle="tooltip" title="Status"> -->
<!-- 					                                        <div class="btn-group" data-toggle="btn-toggle" > -->
<!-- 					                                            <button type="button" class="btn btn-default btn-sm active"><i class="fa fa-square text-green"></i></button> -->
<!-- 					                                            <button type="button" class="btn btn-default btn-sm"><i class="fa fa-square text-red"></i></button> -->
<!-- 					                                        </div> -->
<!-- 					                                    </div> -->
					                                </div>
					                                <div class="box-body chat" id="chat-box">
					                                	<c:forEach items="${messages}" var="message">
					                                    <!-- chat item -->
					                                    <div class="item">
					                                        <img src="<c:url value="/resources/img/avatar5.png" />" class="img-circle" alt="User Image" />
					                                        <p class="message">
					                                        	<c:choose>
				                                                <c:when test="${!empty message.sender.staff.id}">
				                                                	<c:url value="/staff/edit/${message.sender.staff.id}" var="urlReference"/>
				                                                </c:when>
				                                                <c:when test="${empty message.sender.staff.id}">
					                                                <c:url value="/systemuser/edit/${message.sender.id}" var="urlReference"/>
				                                                </c:when>
				                                                </c:choose>
					                                            <a href="${urlReference}" class="name">
					                                                <small class="text-muted pull-right"><i class="fa fa-clock-o"></i> ${message.timestamp}</small>
					                                                <c:choose>
					                                                <c:when test="${empty message.sender.staff.getFullName()}">
					                                                	${message.sender.username}
					                                                </c:when>
					                                                <c:when test="${!empty message.sender.staff.getFullName()}">
						                                                ${message.sender.staff.getFullName()}
					                                                </c:when>
					                                                </c:choose>
					                                            </a>
					                                            ${message.content}
					                                        </p>
					                                    </div><!-- /.item -->
					                                	</c:forEach>
					                                </div><!-- /.chat -->
					                                <div class="box-footer">
					                                    	
			                                    	<form:form modelAttribute="message"
														id="detailsForm"
														method="post"
														action="${contextPath}/message/send">
														<table style="width: 100%">
															<tr>
															<td style="width: 100%">
					                                        <form:input class="form-control" placeholder="Type message..." path="content"/>
															</td>
															<td style="padding-left: 1%; vertical-align: top">
					                                        <div class="input-group-btn" style="padding-left: 1%;">
					                                            <button class="btn btn-flat btn-default"><i class="fa fa-paper-plane"></i>&nbsp;Send</button>
					                                        </div>
															</td>
															</tr>
														</table>
				                                    </form:form>
					                                    	
					                                </div>
					                            </div><!-- /.box (chat box) -->
                                            </div><!-- /.table-responsive -->
                                        </div><!-- /.col (RIGHT) -->
                                    </div><!-- /.row -->
                                </div><!-- /.box-body -->
                                <div class="box-footer clearfix">
                                    <div class="pull-right">
                                        <small>Showing 1-12/1,240</small>
                                        <button class="btn btn-xs btn-flat btn-default"><i class="fa fa-caret-left"></i></button>
                                        <button class="btn btn-xs btn-flat btn-default"><i class="fa fa-caret-right"></i></button>
                                    </div>
                                </div><!-- box-footer -->
                            </div><!-- /.box -->
                        </div><!-- /.col (MAIN) -->
                    </div>
                    <!-- MAILBOX END -->

                </section><!-- /.content -->
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- COMPOSE MESSAGE MODAL -->
        <div class="modal fade" id="compose-modal" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title"><i class="fa fa-envelope-o"></i> Compose New Message</h4>
                    </div>
                    <form action="#" method="post">
                        <div class="modal-body">
                            <div class="form-group">
                                <div class="input-group">
                                    <span class="input-group-addon">TO:</span>
                                    <input name="email_to" type="email" class="form-control" placeholder="Email TO">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="input-group">
                                    <span class="input-group-addon">CC:</span>
                                    <input name="email_to" type="email" class="form-control" placeholder="Email CC">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="input-group">
                                    <span class="input-group-addon">BCC:</span>
                                    <input name="email_to" type="email" class="form-control" placeholder="Email BCC">
                                </div>
                            </div>
                            <div class="form-group">
                                <textarea name="message" id="email_message" class="form-control" placeholder="Message" style="height: 120px;"></textarea>
                            </div>
                            <div class="form-group">
                                <div class="btn btn-success btn-file">
                                    <i class="fa fa-paperclip"></i> Attachment
                                    <input type="file" name="attachment"/>
                                </div>
                                <p class="help-block">Max. 32MB</p>
                            </div>

                        </div>
                        <div class="modal-footer clearfix">

                            <button type="button" class="btn btn-danger" data-dismiss="modal"><i class="fa fa-times"></i> Discard</button>

                            <button type="submit" class="btn btn-primary pull-left"><i class="fa fa-envelope"></i> Send Message</button>
                        </div>
                    </form>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
        
        
        <!-- Bootstrap WYSIHTML5 -->
        <script src="${contextPath}/resources/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
        <!-- iCheck -->
        <script src="${contextPath}/resources/js/plugins/iCheck/icheck.min.js" type="text/javascript"></script>
        <!-- Page script -->
        <script type="text/javascript">
            $(function() {

                "use strict";

                //iCheck for checkbox and radio inputs
                $('input[type="checkbox"]').iCheck({
                    checkboxClass: 'icheckbox_minimal-blue',
                    radioClass: 'iradio_minimal-blue'
                });

                //When unchecking the checkbox
                $("#check-all").on('ifUnchecked', function(event) {
                    //Uncheck all checkboxes
                    $("input[type='checkbox']", ".table-mailbox").iCheck("uncheck");
                });
                //When checking the checkbox
                $("#check-all").on('ifChecked', function(event) {
                    //Check all checkboxes
                    $("input[type='checkbox']", ".table-mailbox").iCheck("check");
                });
                //Handle starring for glyphicon and font awesome
                $(".fa-star, .fa-star-o, .glyphicon-star, .glyphicon-star-empty").click(function(e) {
                    e.preventDefault();
                    //detect type
                    var glyph = $(this).hasClass("glyphicon");
                    var fa = $(this).hasClass("fa");

                    //Switch states
                    if (glyph) {
                        $(this).toggleClass("glyphicon-star");
                        $(this).toggleClass("glyphicon-star-empty");
                    }

                    if (fa) {
                        $(this).toggleClass("fa-star");
                        $(this).toggleClass("fa-star-o");
                    }
                });

                //Initialize WYSIHTML5 - text editor
                $("#email_message").wysihtml5();
            });
        </script>

    </body>
</html>
