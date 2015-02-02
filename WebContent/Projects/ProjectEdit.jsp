<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Project Edit</title>
	<%@ include file ="../Global-Constants.jsp" %>
	<%@ include file ="../CSS-Includes.jsp" %>
</head>
<body class="skin-blue">
	<%@ include file ="../Header.jsp" %>
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<%@ include file ="../Sidebar.jsp" %>
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>
	                Project Name
	                <small>Edit Project</small>
	            </h1>
	            <ol class="breadcrumb">
	                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
	                <li><a href="#">Tables</a></li>
	                <li class="active">Data tables</li>
	            </ol>
	        </section>
	        <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- Custom Tabs -->
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab_1" data-toggle="tab">Details</a></li>
                                <li><a href="#tab_2" data-toggle="tab">Tasks</a></li>
                                <li><a href="#tab_5" data-toggle="tab">Timeline</a></li>
                                <li><a href="#tab_3" data-toggle="tab">Files</a></li>
                                <li><a href="#tab_4" data-toggle="tab">Photos</a></li>
<!--                                 <li class="dropdown"> -->
<!--                                     <a class="dropdown-toggle" data-toggle="dropdown" href="#"> -->
<!--                                         Dropdown <span class="caret"></span> -->
<!--                                     </a> -->
<!--                                     <ul class="dropdown-menu"> -->
<!--                                         <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Action</a></li> -->
<!--                                         <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Another action</a></li> -->
<!--                                         <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Something else here</a></li> -->
<!--                                         <li role="presentation" class="divider"></li> -->
<!--                                         <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Separated link</a></li> -->
<!--                                     </ul> -->
<!--                                 </li> -->
<!--                                 <li class="pull-right"><a href="#" class="text-muted"><i class="fa fa-gear"></i></a></li> -->
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                    <form role="form">
                                        <!-- text input -->
                                        <div class="form-group">
                                            <label>Name</label>
                                            <input type="text" class="form-control" placeholder="Enter ..."/><br/>
                                            <label>Description</label>
                                            <input type="text" class="form-control" placeholder="Enter ..."/><br/>
                                            <label>More Details</label>
                                            <input type="text" class="form-control" placeholder="Enter ..."/><br/>
                                        </div>
										<button class="btn btn-warning btn-sm">Update</button>
										<button class="btn btn-danger btn-sm">Delete</button>
                                    </form>
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_2">
                                    <!-- TO DO List -->
		                            <div class="box">
		                                <div class="box-header">
		                                    <h3 class="box-title">Assigned Tasks</h3>
		                                    <div class="box-tools pull-right">
		                                        <ul class="pagination pagination-sm inline">
		                                            <li><a href="#">&laquo;</a></li>
		                                            <li><a href="#">1</a></li>
		                                            <li><a href="#">2</a></li>
		                                            <li><a href="#">3</a></li>
		                                            <li><a href="#">&raquo;</a></li>
		                                        </ul>
		                                    </div>
		                                </div><!-- /.box-header -->
		                                <div class="box-body">
		                                    <ul class="todo-list">
		                                        <li>
		                                            <!-- drag handle -->
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <!-- checkbox -->
		                                            <input type="checkbox" value="" name=""/>
		                                            <!-- todo text -->
		                                            <span class="text">Design a nice theme</span>
		                                            <!-- Emphasis label -->
		                                            <small class="label label-danger"><i class="fa fa-clock-o"></i> 2 mins</small>
		                                            <!-- General tools such as edit or delete-->
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Make the theme responsive</span>
		                                            <small class="label label-info"><i class="fa fa-clock-o"></i> 4 hours</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Let theme shine like a star</span>
		                                            <small class="label label-warning"><i class="fa fa-clock-o"></i> 1 day</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Let theme shine like a star</span>
		                                            <small class="label label-success"><i class="fa fa-clock-o"></i> 3 days</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Check your messages and notifications</span>
		                                            <small class="label label-primary"><i class="fa fa-clock-o"></i> 1 week</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                        <li>
		                                            <span class="handle">
		                                                <i class="fa fa-ellipsis-v"></i>
		                                                <i class="fa fa-ellipsis-v"></i>
		                                            </span>
		                                            <input type="checkbox" value="" name=""/>
		                                            <span class="text">Let theme shine like a star</span>
		                                            <small class="label label-default"><i class="fa fa-clock-o"></i> 1 month</small>
		                                            <div class="tools">
		                                                <i class="fa fa-edit"></i>
		                                                <i class="fa fa-trash-o"></i>
		                                            </div>
		                                        </li>
		                                    </ul>
		                                </div><!-- /.box-body -->
		                                <div class="box-footer clearfix no-border">
		                                    <button class="btn btn-default pull-right"><i class="fa fa-plus"></i> Add item</button>
		                                </div>
		                            </div><!-- /.box -->
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_3">
                                    <div class="box-body table-responsive">
                                    	<div class="form-group">
	                                        <label for="exampleInputFile">File Upload</label>
	                                        <input type="file" id="exampleInputFile">
	                                        <p class="help-block">Upload a file</p>
	                                    </div>
	                                    <br/>
	                                    <table id="example-1" class="table table-bordered table-striped">
	                                        <thead>
	                                            <tr>
	                                            	<th>&nbsp;</th>
	                                                <th>Rendering engine</th>
	                                                <th>Browser</th>
	                                                <th>Platform(s)</th>
	                                                <th>Engine version</th>
	                                                <th>CSS grade</th>
	                                            </tr>
	                                        </thead>
	                                        <tbody>
	                                            <tr>
	                                            	<td>
	                                            		<center>
														<button class="btn btn-primary btn-sm">Download</button>
														<button class="btn btn-danger btn-sm">Delete</button>
														</center>
													</td>
	                                                <td>Trident</td>
	                                                <td>Internet
	                                                    Explorer 4.0</td>
	                                                <td>Win 95+</td>
	                                                <td> 4</td>
	                                                <td>X</td>
	                                            </tr>
	                                            <tr>
	                                            	<td>
	                                            		<center>
														<button class="btn btn-primary btn-sm">Download</button>
														<button class="btn btn-danger btn-sm">Delete</button>
														</center>
													</td>
	                                                <td>Trident</td>
	                                                <td>Internet
	                                                    Explorer 5.0</td>
	                                                <td>Win 95+</td>
	                                                <td>5</td>
	                                                <td>C</td>
	                                            </tr>
	                                            <tr>
	                                            	<td>
	                                            		<center>
														<button class="btn btn-primary btn-sm">Download</button>
														<button class="btn btn-danger btn-sm">Delete</button>
														</center>
													</td>
	                                                <td>Trident</td>
	                                                <td>Internet
	                                                    Explorer 5.5</td>
	                                                <td>Win 95+</td>
	                                                <td>5.5</td>
	                                                <td>A</td>
	                                            </tr>
	                                        </tbody>
	                                        <tfoot>
	                                            <tr>
	                                            	<th>&nbsp;</th>
	                                                <th>Rendering engine</th>
	                                                <th>Browser</th>
	                                                <th>Platform(s)</th>
	                                                <th>Engine version</th>
	                                                <th>CSS grade</th>
	                                            </tr>
	                                        </tfoot>
	                                    </table>
	                                </div><!-- /.box-body -->
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_4">
                                    photos
                                </div><!-- /.tab-pane -->
                                <div class="tab-pane" id="tab_5">
                                    <!-- The time line -->
		                            <ul class="timeline">
		                                <!-- timeline time label -->
		                                <li class="time-label">
		                                    <span class="bg-red">
		                                        10 Feb. 2014
		                                    </span>
		                                </li>
		                                <!-- /.timeline-label -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-envelope bg-blue"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 12:05</span>
		                                        <h3 class="timeline-header"><a href="#">Support Team</a> sent you and email</h3>
		                                        <div class="timeline-body">
		                                            Etsy doostang zoodles disqus groupon greplin oooj voxy zoodles,
		                                            weebly ning heekya handango imeem plugg dopplr jibjab, movity
		                                            jajah plickers sifteo edmodo ifttt zimbra. Babblely odeo kaboodle
		                                            quora plaxo ideeli hulu weebly balihoo...
		                                        </div>
		                                        <div class='timeline-footer'>
		                                            <a class="btn btn-primary btn-xs">Read more</a>
		                                            <a class="btn btn-danger btn-xs">Delete</a>
		                                        </div>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-user bg-aqua"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 5 mins ago</span>
		                                        <h3 class="timeline-header no-border"><a href="#">Sarah Young</a> accepted your friend request</h3>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-comments bg-yellow"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 27 mins ago</span>
		                                        <h3 class="timeline-header"><a href="#">Jay White</a> commented on your post</h3>
		                                        <div class="timeline-body">
		                                            Take me to your leader!
		                                            Switzerland is small and neutral!
		                                            We are more like Germany, ambitious and misunderstood!
		                                        </div>
		                                        <div class='timeline-footer'>
		                                            <a class="btn btn-warning btn-flat btn-xs">View comment</a>
		                                        </div>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <!-- timeline time label -->
		                                <li class="time-label">
		                                    <span class="bg-green">
		                                        3 Jan. 2014
		                                    </span>
		                                </li>
		                                <!-- /.timeline-label -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-camera bg-purple"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 2 days ago</span>
		                                        <h3 class="timeline-header"><a href="#">Mina Lee</a> uploaded new photos</h3>
		                                        <div class="timeline-body">
		                                            <img src="http://placehold.it/150x100" alt="..." class='margin' />
		                                            <img src="http://placehold.it/150x100" alt="..." class='margin'/>
		                                            <img src="http://placehold.it/150x100" alt="..." class='margin'/>
		                                            <img src="http://placehold.it/150x100" alt="..." class='margin'/>
		                                        </div>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <!-- timeline item -->
		                                <li>
		                                    <i class="fa fa-video-camera bg-maroon"></i>
		                                    <div class="timeline-item">
		                                        <span class="time"><i class="fa fa-clock-o"></i> 5 days ago</span>
		                                        <h3 class="timeline-header"><a href="#">Mr. Doe</a> shared a video</h3>
		                                        <div class="timeline-body">
		                                            <iframe width="300" height="169" src="//www.youtube.com/embed/fLe_qO4AE-M" frameborder="0" allowfullscreen></iframe>
		                                        </div>
		                                        <div class="timeline-footer">
		                                            <a href="#" class="btn btn-xs bg-maroon">See comments</a>
		                                        </div>
		                                    </div>
		                                </li>
		                                <!-- END timeline item -->
		                                <li>
		                                    <i class="fa fa-clock-o"></i>
		                                </li>
		                            </ul>
                                </div><!-- /.tab-pane -->
                            </div><!-- /.tab-content -->
                        </div><!-- nav-tabs-custom -->
                    </div><!-- /.col -->
                </div> <!-- /.row -->
            </section><!-- /.content -->
        </aside>
	</div>
	<%@ include file ="../JS-Includes.jsp" %>
	<script>
		$(document).ready(function() {
			$("#example-1").dataTable();
	    });
	</script>
</body>
</html>