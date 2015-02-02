<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Projects List</title>
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
	                Data Tables
	                <small>advanced tables</small>
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
                        <div class="box">
                                <div class="box-header">
                                    <h3 class="box-title">Data Table With Full Features</h3>
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive">
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
													<a href="ProjectEdit.jsp"><button class="btn btn-primary btn-sm">View</button></a>
													<button class="btn btn-warning btn-sm">Edit</button>
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
													<button class="btn btn-primary btn-sm">View</button>
													<button class="btn btn-warning btn-sm">Edit</button>
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
													<button class="btn btn-primary btn-sm">View</button>
													<button class="btn btn-warning btn-sm">Edit</button>
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
                            </div><!-- /.box -->
                    </div>
                </div>
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