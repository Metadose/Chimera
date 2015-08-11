<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<c:set value="${false}" var="isUpdating"/>
	<title>Estimation View</title>
	<link href="<c:url value="/resources/lib/igniteui/infragistics.theme.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/igniteui/infragistics.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/igniteui/infragistics.ui.treegrid.css" />"rel="stylesheet" type="text/css" />
	<style>
	  ul {         
	      padding:0 0 0 0;
	      margin:0 0 0 0;
	  }
	  ul li {     
	      list-style:none;
	      margin-bottom:25px;           
	  }
	  ul li img {
	      cursor: pointer;
	  }
	</style>
</head>
<body class="skin-blue">
	<c:import url="/resources/header.jsp" />
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<c:import url="/resources/sidebar.jsp" />
		<aside class="right-side">
		<!-- Content Header (Page header) -->
	        <section class="content-header">
	            <h1>${estimationoutput.name}<small>${estimationoutput.lastComputed}</small>
	            </h1>
	        </section>
	        <section class="content">
			<div class="row">
				<div class="col-md-12">
					<c:url var="urlBack" value="/project/edit/${estimationoutput.project.id}" />
	                <a href="${urlBack}">
						<button class="btn btn-cebedo-back btn-flat btn-sm">Back to Project</button>
					</a>
					<br/>
					<br/>
					<div class="box box-body box-default">
						<div class="box-header">
						<%--<fmt:formatDate pattern="yyyy/MM/dd hh:mm:ss a" value="${projectPayroll.lastComputed}" var="lastComputed"/> --%>
						<%--<h3 class="box-title">Computation as of ${lastComputed}</h3> --%>
						</div>
						<div class="box-body table-responsive">
						<div class="callout callout-info callout-cebedo">
						<p>Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section Instructions regarding this section .</p>
						</div>

						<table id="summary-table" class="table table-bordered table-striped">
						<thead>
                    		<tr>
                                <th>Material</th>
                                <th>Quantity</th>
                                <th>Cost (PHP/unit)</th>
                            </tr>
                    	</thead>
						<tbody>
							<tr>
								<td>Concrete Hollow Blocks (CHB)</td>
								<td>${estimationoutput.quantityCHB}</td>
								<td>${estimationoutput.costCHB}</td>
							</tr>
							<tr>
								<td>Cement (40kg)</td>
								<td>${estimationoutput.quantityCement40kg}</td>
								<td>${estimationoutput.costCement40kg}</td>
							</tr>
							<tr>
								<td>Cement (50kg)</td>
								<td>${estimationoutput.quantityCement50kg}</td>
								<td>${estimationoutput.costCement50kg}</td>
							</tr>
							<tr>
								<td>Sand</td>
								<td>${estimationoutput.quantitySand}</td>
								<td>${estimationoutput.costSand}</td>
							</tr>
							<tr>
								<td>Gravel</td>
								<td>${estimationoutput.quantityGravel}</td>
								<td>${estimationoutput.costGravel}</td>
							</tr>
						</tbody>
						</table>
						
						<div class="pull-right">
                  		<h3>Grand Total <b><u>
                    	${estimationoutput.getCostGrandTotalAsString()}
						</u></b></h3>
						</div>
						<br/>
						<br/>
						<br/>
						<br/>

						<label>Input</label>
						<table id="treegrid-details"></table><br/>
						
						<label>Concrete</label>
						<table id="treegrid-concrete"></table><br/>
						
						<label>CHB (Setting/Laying)</label>
						<table id="treegrid-chb-setting"></table><br/>
						
						<label>CHB (Plastering)</label>
						<table id="treegrid-chb-plastering"></table><br/>
						
						<label>CHB (Footing)</label>
						<table id="treegrid-chb-footing"></table><br/>
						
						</div>
					</div>
				</div>
			</div>
            </section><!-- /.content -->
        </aside>
	</div>
</body>
<!-- Ignite UI Required Combined JavaScript Files -->
<script src="<c:url value="/resources/lib/modernizr.js" />"type="text/javascript"></script>
<script src="<c:url value="/resources/lib/igniteui/infragistics.core.js" />"type="text/javascript"></script>
<script src="<c:url value="/resources/lib/igniteui/infragistics.lob.js" />"type="text/javascript"></script>
<script src="<c:url value="/resources/lib/igniteui/infragistics.ui.treegrid.js" />"type="text/javascript"></script>

<script type="text/javascript">
function submitForm(id) {
	$('#'+id).submit();
}

$(document).ready(function() {
	
	/* Footing */
	var flatDS = ${estimationoutput.estimatesAsJson};
	$("#treegrid-chb-footing").igTreeGrid({
		dataSource: flatDS,
		width: "100%",
		features:[
			{ name: "MultiColumnHeaders" }
		],
		primaryKey: "uuid",
		columns: [

			/* Details */
			{ headerText: "uuid", 				   key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", 				   key: "name", dataType: "string" },
			{ headerText: "Remarks", 			   key: "remarks", dataType: "string", hidden: true },
			{ headerText: "Area", 				   key: "area", dataType: "number", hidden: true },
			{ headerText: "Volume", 			   key: "volume", dataType: "number", hidden: true },
			{ headerText: "CHB Foundation<br/>Height (m)", key: "chbFoundationHeight", dataType: "number", hidden: true },
			{ headerText: "CHB Footing<br/>Length (m)",    key: "footingLength", dataType: "number", hidden: true },
			
			/* Concrete */
			{ headerText: "Cement<br/>(40kg)",  key: "concreteCement40kg", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(50kg)",  key: "concreteCement50kg", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(cu.m.)",   key: "concreteSand", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(cu.m.)", key: "concreteGravel", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "concreteCostCement40kg", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "concreteCostCement50kg", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "concreteCostSand", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "concreteCostGravel", dataType: "number", hidden: true},
			
			/* CHB */
			{ headerText: "CHB<br/>(pieces)", 	 key: "chbTotal", dataType: "number" },
			{ headerText: "CHB<br/>(PHP/piece)", key: "chbCostTotal", dataType: "number" },
			
			/* CHB Laying */
			{ headerText: "Cement<br/>(40kg)", key: "chbLayingBags40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", key: "chbLayingBags50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",  key: "chbLayingSand", dataType: "number", hidden: true },			
			{ headerText: "Cement<br/>(PHP/40kg)", key: "chbLayingCostBags40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "chbLayingCostBags50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",  key: "chbLayingCostSand", dataType: "number", hidden: true },

			/* Plastering */
			{ headerText: "Cement<br/>(40kg)",	  key: "plasteringCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", 	  key: "plasteringCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)", 	  key: "plasteringSand", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)", key: "plasteringCostCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "plasteringCostCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)", 	  key: "plasteringCostSand", dataType: "number", hidden: true },
			
			
			/* Footing */
			{ headerText: "Quantity", group: [
			{ headerText: "Cement<br/>(40kg)",  key: "footingCement40kg", dataType: "number" },
			{ headerText: "Cement<br/>(50kg)",  key: "footingCement50kg", dataType: "number" },
			{ headerText: "Sand<br/>(cu.m.)",   key: "footingSand", dataType: "number" },
			{ headerText: "Gravel<br/>(cu.m.)", key: "footingGravel", dataType: "number" }
			]},
			{ headerText: "Cost", group: [
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "footingCostCement40kg", dataType: "number" },
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "footingCostCement50kg", dataType: "number" },
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "footingCostSand", dataType: "number" },
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "footingCostGravel", dataType: "number" }
			]}
		]
	});

	/* Plastering */
	$("#treegrid-chb-plastering").igTreeGrid({
		dataSource: flatDS,
		width: "100%",
		features:[
			{ name: "MultiColumnHeaders" }
		],
		primaryKey: "uuid",
		columns: [

			/* Details */
			{ headerText: "uuid", 				   key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", 				   key: "name", dataType: "string" },
			{ headerText: "Remarks", 			   key: "remarks", dataType: "string", hidden: true },
			{ headerText: "Area", 				   key: "area", dataType: "number", hidden: true },
			{ headerText: "Volume", 			   key: "volume", dataType: "number", hidden: true },
			{ headerText: "CHB Foundation<br/>Height (m)", key: "chbFoundationHeight", dataType: "number", hidden: true },
			{ headerText: "CHB Footing<br/>Length (m)",    key: "footingLength", dataType: "number", hidden: true },
			
			/* Concrete */
			{ headerText: "Cement<br/>(40kg)",  key: "concreteCement40kg", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(50kg)",  key: "concreteCement50kg", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(cu.m.)",   key: "concreteSand", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(cu.m.)", key: "concreteGravel", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "concreteCostCement40kg", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "concreteCostCement50kg", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "concreteCostSand", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "concreteCostGravel", dataType: "number", hidden: true},
			
			/* CHB */
			{ headerText: "CHB<br/>(pieces)", 	 key: "chbTotal", dataType: "number", hidden: true },
			{ headerText: "CHB<br/>(PHP/piece)", key: "chbCostTotal", dataType: "number", hidden: true },
			
			/* CHB Laying */
			{ headerText: "Cement<br/>(40kg)", key: "chbLayingBags40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", key: "chbLayingBags50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",  key: "chbLayingSand", dataType: "number", hidden: true },			
			{ headerText: "Cement<br/>(PHP/40kg)", key: "chbLayingCostBags40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "chbLayingCostBags50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",  key: "chbLayingCostSand", dataType: "number", hidden: true },

			/* Plastering */
			{ headerText: "Quantity", group: [
			{ headerText: "Cement<br/>(40kg)",	  key: "plasteringCement40kg", dataType: "number" },
			{ headerText: "Cement<br/>(50kg)", 	  key: "plasteringCement50kg", dataType: "number" },
			{ headerText: "Sand<br/>(cu.m.)", 	  key: "plasteringSand", dataType: "number" }
			]},
			{ headerText: "Cost", group: [
			{ headerText: "Cement<br/>(PHP/40kg)", key: "plasteringCostCement40kg", dataType: "number" },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "plasteringCostCement50kg", dataType: "number" },
			{ headerText: "Sand<br/>(PHP/cu.m.)", 	  key: "plasteringCostSand", dataType: "number" }
			]},
			
			
			/* Footing */
			{ headerText: "Cement<br/>(40kg)",  key: "footingCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)",  key: "footingCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",   key: "footingSand", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(cu.m.)", key: "footingGravel", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "footingCostCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "footingCostCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "footingCostSand", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "footingCostGravel", dataType: "number", hidden: true }
		]
	});

	/* CHB Laying */
	$("#treegrid-chb-setting").igTreeGrid({
		dataSource: flatDS,
		width: "100%",
        features:[
            { name: "MultiColumnHeaders" }
        ],
		primaryKey: "uuid",
		columns: [

			/* Details */
			{ headerText: "uuid", 				   key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", 				   key: "name", dataType: "string" },
			{ headerText: "Remarks", 			   key: "remarks", dataType: "string", hidden: true },
			{ headerText: "Area", 				   key: "area", dataType: "number", hidden: true },
			{ headerText: "Volume", 			   key: "volume", dataType: "number", hidden: true },
			{ headerText: "CHB Foundation<br/>Height (m)", key: "chbFoundationHeight", dataType: "number", hidden: true },
			{ headerText: "CHB Footing<br/>Length (m)",    key: "footingLength", dataType: "number", hidden: true },
			
			/* Concrete */
			{ headerText: "Cement<br/>(40kg)",  key: "concreteCement40kg", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(50kg)",  key: "concreteCement50kg", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(cu.m.)",   key: "concreteSand", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(cu.m.)", key: "concreteGravel", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "concreteCostCement40kg", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "concreteCostCement50kg", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "concreteCostSand", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "concreteCostGravel", dataType: "number", hidden: true},
			
			/* CHB */
			{ headerText: "CHB<br/>(pieces)", 	 key: "chbTotal", dataType: "number", hidden: true },
			{ headerText: "CHB<br/>(PHP/piece)", key: "chbCostTotal", dataType: "number", hidden: true },
			
			/* CHB Laying */
			{ headerText: "Quantity", group: [
			{ headerText: "Cement<br/>(40kg)", key: "chbLayingBags40kg", dataType: "number" },
			{ headerText: "Cement<br/>(50kg)", key: "chbLayingBags50kg", dataType: "number" },
			{ headerText: "Sand<br/>(cu.m.)",  key: "chbLayingSand", dataType: "number" }
			]},
			{ headerText: "Cost", group: [
			{ headerText: "Cement<br/>(PHP/40kg)", key: "chbLayingCostBags40kg", dataType: "number" },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "chbLayingCostBags50kg", dataType: "number" },
			{ headerText: "Sand<br/>(PHP/cu.m.)",  key: "chbLayingCostSand", dataType: "number" }
			]},

			/* Plastering */
			{ headerText: "Cement<br/>(40kg)",	  key: "plasteringCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", 	  key: "plasteringCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)", 	  key: "plasteringSand", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)", key: "plasteringCostCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "plasteringCostCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)", 	  key: "plasteringCostSand", dataType: "number", hidden: true },
			
			
			/* Footing */
			{ headerText: "Cement<br/>(40kg)",  key: "footingCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)",  key: "footingCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",   key: "footingSand", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(cu.m.)", key: "footingGravel", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "footingCostCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "footingCostCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "footingCostSand", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "footingCostGravel", dataType: "number", hidden: true }
		]
	});

	/* Concrete */
	$("#treegrid-concrete").igTreeGrid({
		dataSource: flatDS,
		width: "100%",
		features:[
			{ name: "MultiColumnHeaders" }
		],
		primaryKey: "uuid",
		columns: [

			/* Details */
			{ headerText: "uuid", 				   key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", 				   key: "name", dataType: "string" },
			{ headerText: "Remarks", 			   key: "remarks", dataType: "string", hidden: true },
			{ headerText: "Area", 				   key: "area", dataType: "number", hidden: true },
			{ headerText: "Volume", 			   key: "volume", dataType: "number", hidden: true },
			{ headerText: "CHB Foundation<br/>Height (m)", key: "chbFoundationHeight", dataType: "number", hidden: true },
			{ headerText: "CHB Footing<br/>Length (m)",    key: "footingLength", dataType: "number", hidden: true },
			
			/* Concrete */
			{ headerText: "Quantity", group: [
				{ headerText: "Cement<br/>(40kg)",  key: "concreteCement40kg", dataType: "number"},
				{ headerText: "Cement<br/>(50kg)",  key: "concreteCement50kg", dataType: "number"},
				{ headerText: "Sand<br/>(cu.m.)",   key: "concreteSand", dataType: "number"},
				{ headerText: "Gravel<br/>(cu.m.)", key: "concreteGravel", dataType: "number"}
			]},
			{ headerText: "Cost", group: [
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "concreteCostCement40kg", dataType: "number"},
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "concreteCostCement50kg", dataType: "number"},
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "concreteCostSand", dataType: "number"},
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "concreteCostGravel", dataType: "number"}
			]},
			
			/* CHB */
			{ headerText: "CHB<br/>(pieces)", 	 key: "chbTotal", dataType: "number", hidden: true },
			{ headerText: "CHB<br/>(PHP/piece)", key: "chbCostTotal", dataType: "number", hidden: true },
			
			/* CHB Laying */
			{ headerText: "Cement<br/>(40kg)", key: "chbLayingBags40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", key: "chbLayingBags50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",  key: "chbLayingSand", dataType: "number", hidden: true },			
			{ headerText: "Cement<br/>(PHP/40kg)", key: "chbLayingCostBags40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "chbLayingCostBags50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",  key: "chbLayingCostSand", dataType: "number", hidden: true },

			/* Plastering */
			{ headerText: "Cement<br/>(40kg)",	  key: "plasteringCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", 	  key: "plasteringCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)", 	  key: "plasteringSand", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)", key: "plasteringCostCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "plasteringCostCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)", 	  key: "plasteringCostSand", dataType: "number", hidden: true },
			
			/* Footing */
			{ headerText: "Cement<br/>(40kg)",  key: "footingCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)",  key: "footingCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",   key: "footingSand", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(cu.m.)", key: "footingGravel", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "footingCostCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "footingCostCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "footingCostSand", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "footingCostGravel", dataType: "number", hidden: true }
		]
	});

	/* Details */
	$("#treegrid-details").igTreeGrid({
		dataSource: flatDS,
		width: "100%",
		features:[
			{ name: "MultiColumnHeaders" }
		],
		primaryKey: "uuid",
		columns: [

			/* Details */
			{ headerText: "uuid", 				   key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", 				   key: "name", dataType: "string" },
			{ headerText: "Remarks", 			   key: "remarks", dataType: "string" },
			{ headerText: "Area", 				   key: "area", dataType: "number" },
			{ headerText: "Volume", 			   key: "volume", dataType: "number" },
			{ headerText: "CHB Foundation<br/>Height (m)", key: "chbFoundationHeight", dataType: "number" },
			{ headerText: "CHB Footing<br/>Length (m)",    key: "footingLength", dataType: "number" },
			
			/* Concrete */
			{ headerText: "Cement<br/>(40kg)",  key: "concreteCement40kg", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(50kg)",  key: "concreteCement50kg", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(cu.m.)",   key: "concreteSand", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(cu.m.)", key: "concreteGravel", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "concreteCostCement40kg", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "concreteCostCement50kg", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "concreteCostSand", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "concreteCostGravel", dataType: "number", hidden: true},
			
			/* CHB */
			{ headerText: "CHB<br/>(pieces)", 	 key: "chbTotal", dataType: "number", hidden: true },
			{ headerText: "CHB<br/>(PHP/piece)", key: "chbCostTotal", dataType: "number", hidden: true },
			
			/* CHB Laying */
			{ headerText: "Cement<br/>(40kg)", key: "chbLayingBags40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", key: "chbLayingBags50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",  key: "chbLayingSand", dataType: "number", hidden: true },			
			{ headerText: "Cement<br/>(PHP/40kg)", key: "chbLayingCostBags40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "chbLayingCostBags50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",  key: "chbLayingCostSand", dataType: "number", hidden: true },

			/* Plastering */
			{ headerText: "Cement<br/>(40kg)",	  key: "plasteringCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", 	  key: "plasteringCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)", 	  key: "plasteringSand", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)", key: "plasteringCostCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "plasteringCostCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)", 	  key: "plasteringCostSand", dataType: "number", hidden: true },
			
			/* Footing */
			{ headerText: "Cement<br/>(40kg)",  key: "footingCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)",  key: "footingCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",   key: "footingSand", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(cu.m.)", key: "footingGravel", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "footingCostCement40kg", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "footingCostCement50kg", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "footingCostSand", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "footingCostGravel", dataType: "number",  hidden: true }
		]
	});
});
</script>
</html>