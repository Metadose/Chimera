<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<c:set value="${false}" var="isUpdating"/>
	<title>View Estimation</title>
	<link href="<c:url value="/resources/lib/igniteui/infragistics.theme.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/igniteui/infragistics.css" />"rel="stylesheet" type="text/css" />
	<link href="<c:url value="/resources/lib/igniteui/infragistics.ui.treegrid.css" />"rel="stylesheet" type="text/css" />
	<script src="<c:url value="/resources/js/accounting.min.js" />"type="text/javascript"></script>
	<script src="<c:url value="/resources/js/accounting-aux.js" />"type="text/javascript"></script>
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
	            <h1>${estimationoutput.name}
	            <small>
	            <fmt:formatDate pattern="yyyy/MM/dd hh:mm:ss a" value="${estimationoutput.lastComputed}" var="lastComputed"/>
	            ${lastComputed}
	            </small>
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
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatQuantity(${estimationoutput.quantityCHB}))</script></td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatCurrency(${estimationoutput.costCHB}))</script></td>
							</tr>
							<tr>
								<td>Cement (40kg)</td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatQuantity(${estimationoutput.quantityCement40kg}))</script></td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatCurrency(${estimationoutput.costCement40kg}))</script></td>
							</tr>
							<tr>
								<td>Cement (50kg)</td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatQuantity(${estimationoutput.quantityCement50kg}))</script></td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatCurrency(${estimationoutput.costCement50kg}))</script></td>
							</tr>
							<tr>
								<td>Sand</td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatQuantity(${estimationoutput.quantitySand}))</script></td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatCurrency(${estimationoutput.costSand}))</script></td>
							</tr>
							<tr>
								<td>Gravel</td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatQuantity(${estimationoutput.quantityGravel}))</script></td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatCurrency(${estimationoutput.costGravel}))</script></td>
							</tr>
							<tr>
								<td>Steel Bars</td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatQuantity(${estimationoutput.quantitySteelBars}))</script></td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatCurrency(${estimationoutput.costSteelBars}))</script></td>
							</tr>
							<tr>
								<td>Tie Wire (Kilo)</td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatQuantity(${estimationoutput.quantityTieWireKilos}))</script></td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatCurrency(${estimationoutput.costTieWireKilos}))</script></td>
							</tr>
							<tr>
								<td>Tie Wire (Roll)</td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatQuantity(${estimationoutput.quantityTieWireRolls}))</script></td>
								<td class="cebedo-text-align-right"><script type="text/javascript">document.write(formatCurrency(${estimationoutput.costTieWireRolls}))</script></td>
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
						
						<label>Metal Reinforcement (CHB)</label>
						<table id="treegrid-mr-chb"></table><br/>

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

<script type="text/javascript">

function submitForm(id) {
	$('#'+id).submit();
}

$(document).ready(function() {

	/* Footing */
	var flatDS = ${estimationoutput.estimatesAsJson};
	$("#treegrid-chb-footing").igGrid({
		dataSource: flatDS,
		width: "100%",
		features:[
			{ name: "MultiColumnHeaders" },
			{ name: "Summaries" }
		],
		primaryKey: "uuid",
		columns: [

			/* Details */
			{ headerText: "uuid", 				   key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", 				   key: "name", dataType: "string" },
			{ headerText: "Remarks", 			   key: "remarks", dataType: "string", hidden: true },
			{ headerText: "Area (sq.m.)", 				   key: "area", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Volume (cu.m.)", 			   key: "volume", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Area Below Ground<br/>(sq.m.)", key: "areaBelowGround", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			/* Concrete */
			{ headerText: "Cement<br/>(40kg)",  key: "concreteCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(50kg)",  key: "concreteCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(cu.m.)",   key: "concreteSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(cu.m.)", key: "concreteGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "concreteCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "concreteCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "concreteCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "concreteCostGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			
			/* CHB Laying */
			{ headerText: "Cement<br/>(40kg)", key: "chbLayingBags40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", key: "chbLayingBags50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",  key: "chbLayingSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },			
			{ headerText: "Cement<br/>(PHP/40kg)", key: "chbLayingCostBags40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "chbLayingCostBags50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",  key: "chbLayingCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },

			/* Plastering */
			{ headerText: "Cement<br/>(40kg)",	  key: "plasteringCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", 	  key: "plasteringCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)", 	  key: "plasteringSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)", key: "plasteringCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "plasteringCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)", 	  key: "plasteringCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			
			/* CHB */
			{ headerText: "CHB<br/>(Pieces)", 	key: "chbTotal", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true  },
			{ headerText: "CHB<br/>(PHP/Piece)", 	key: "chbCostTotal", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			/* Footing */
			{ headerText: "Quantity", group: [
			{ headerText: "Cement<br/>(40kg)",  key: "footingCement40kg", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Cement<br/>(50kg)",  key: "footingCement50kg", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Sand<br/>(cu.m.)",   key: "footingSand", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Gravel<br/>(cu.m.)", key: "footingGravel", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" }
			]},
			{ headerText: "Cost", group: [
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "footingCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "footingCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "footingCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "footingCostGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number" }
			]},

			/* METAL REINFORCEMENT CHB */

			{ key: "mrCHBSteelBar", dataType: "number", hidden: true },
			{ key: "mrCHBTieWireKg", dataType: "number", hidden: true },
			{ key: "mrCHBTieWireRoll", dataType: "number", hidden: true },
			{ key: "mrCHBCostSteelBar", dataType: "number", hidden: true },
			{ key: "mrCHBCostTieWireKg", dataType: "number", hidden: true },
			{ key: "mrCHBCostTieWireRoll", dataType: "number", hidden: true },
			{ key: "footingLength", dataType: "number", hidden: true },
			{ key: "footingWidth", dataType: "number", hidden: true },
			{ key: "footingHeight", dataType: "number", hidden: true }
		]
	});

	/* Plastering */
	$("#treegrid-chb-plastering").igGrid({
		dataSource: flatDS,
		width: "100%",
		features:[
			{ name: "MultiColumnHeaders" },
			{ name: "Summaries" }
		],
		primaryKey: "uuid",
		columns: [

			/* Details */
			{ headerText: "uuid", 				   key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", 				   key: "name", dataType: "string" },
			{ headerText: "Remarks", 			   key: "remarks", dataType: "string", hidden: true },
			{ headerText: "Area (sq.m.)", 				   key: "area", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Volume (cu.m.)", 			   key: "volume", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Area Below Ground<br/>(sq.m.)", key: "areaBelowGround", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			/* Concrete */
			{ headerText: "Cement<br/>(40kg)",  key: "concreteCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(50kg)",  key: "concreteCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(cu.m.)",   key: "concreteSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(cu.m.)", key: "concreteGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "concreteCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "concreteCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "concreteCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "concreteCostGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			
			/* CHB */
			{ headerText: "CHB<br/>(Pieces)", 	 key: "chbTotal", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "CHB<br/>(PHP/Piece)", key: "chbCostTotal", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			/* CHB Laying */
			{ headerText: "Cement<br/>(40kg)", key: "chbLayingBags40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", key: "chbLayingBags50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",  key: "chbLayingSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },			
			{ headerText: "Cement<br/>(PHP/40kg)", key: "chbLayingCostBags40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "chbLayingCostBags50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",  key: "chbLayingCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },

			/* Plastering */
			{ headerText: "Quantity", group: [
			{ headerText: "Cement<br/>(40kg)",	  key: "plasteringCement40kg", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Cement<br/>(50kg)", 	  key: "plasteringCement50kg", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Sand<br/>(cu.m.)", 	  key: "plasteringSand", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" }
			]},
			{ headerText: "Cost", group: [
			{ headerText: "Cement<br/>(PHP/40kg)", key: "plasteringCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "plasteringCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Sand<br/>(PHP/cu.m.)", 	  key: "plasteringCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number" }
			]},
			
			
			/* Footing */
			{ headerText: "Cement<br/>(40kg)",  key: "footingCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)",  key: "footingCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",   key: "footingSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(cu.m.)", key: "footingGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "footingCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "footingCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "footingCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "footingCostGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },

			/* METAL REINFORCEMENT CHB */

			{ key: "mrCHBSteelBar", dataType: "number", hidden: true },
			{ key: "mrCHBTieWireKg", dataType: "number", hidden: true },
			{ key: "mrCHBTieWireRoll", dataType: "number", hidden: true },
			{ key: "mrCHBCostSteelBar", dataType: "number", hidden: true },
			{ key: "mrCHBCostTieWireKg", dataType: "number", hidden: true },
			{ key: "mrCHBCostTieWireRoll", dataType: "number", hidden: true },
			{ key: "footingLength", dataType: "number", hidden: true },
			{ key: "footingWidth", dataType: "number", hidden: true },
			{ key: "footingHeight", dataType: "number", hidden: true }
		]
	});

	/* CHB Laying */
	$("#treegrid-chb-setting").igGrid({
		dataSource: flatDS,
		width: "100%",
        features:[
            { name: "MultiColumnHeaders" },
			{ name: "Summaries" }
        ],
		primaryKey: "uuid",
		columns: [

			/* Details */
			{ headerText: "uuid", 				   key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", 				   key: "name", dataType: "string" },
			{ headerText: "Remarks", 			   key: "remarks", dataType: "string", hidden: true },
			{ headerText: "Area (sq.m.)", 				   key: "area", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Volume (cu.m.)", 			   key: "volume", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Area Below Ground<br/>(sq.m.)", key: "areaBelowGround", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			/* Concrete */
			{ headerText: "Cement<br/>(40kg)",  key: "concreteCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(50kg)",  key: "concreteCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(cu.m.)",   key: "concreteSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(cu.m.)", key: "concreteGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "concreteCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "concreteCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "concreteCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "concreteCostGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			
			/* CHB Laying */
			/* CHB */
			{ headerText: "Quantity", group: [
			{ headerText: "CHB<br/>(Pieces)", 	key: "chbTotal", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Cement<br/>(40kg)", key: "chbLayingBags40kg", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Cement<br/>(50kg)", key: "chbLayingBags50kg", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Sand<br/>(cu.m.)",  key: "chbLayingSand", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" }
			]},
			{ headerText: "Cost", group: [
			{ headerText: "CHB<br/>(PHP/Piece)", 	key: "chbCostTotal", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Cement<br/>(PHP/40kg)", key: "chbLayingCostBags40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "chbLayingCostBags50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Sand<br/>(PHP/cu.m.)",  key: "chbLayingCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number" }
			]},

			/* Plastering */
			{ headerText: "Cement<br/>(40kg)",	  key: "plasteringCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", 	  key: "plasteringCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)", 	  key: "plasteringSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)", key: "plasteringCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "plasteringCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)", 	  key: "plasteringCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			
			/* Footing */
			{ headerText: "Cement<br/>(40kg)",  key: "footingCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)",  key: "footingCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",   key: "footingSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(cu.m.)", key: "footingGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "footingCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "footingCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "footingCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "footingCostGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },

			/* METAL REINFORCEMENT CHB */

			{ key: "mrCHBSteelBar", dataType: "number", hidden: true },
			{ key: "mrCHBTieWireKg", dataType: "number", hidden: true },
			{ key: "mrCHBTieWireRoll", dataType: "number", hidden: true },
			{ key: "mrCHBCostSteelBar", dataType: "number", hidden: true },
			{ key: "mrCHBCostTieWireKg", dataType: "number", hidden: true },
			{ key: "mrCHBCostTieWireRoll", dataType: "number", hidden: true },
			{ key: "footingLength", dataType: "number", hidden: true },
			{ key: "footingWidth", dataType: "number", hidden: true },
			{ key: "footingHeight", dataType: "number", hidden: true }
		]
	});

	/* Concrete */
	$("#treegrid-concrete").igGrid({
		dataSource: flatDS,
		width: "100%",
		features:[
			{ name: "MultiColumnHeaders" },
			{ name: "Summaries" }
		],
		primaryKey: "uuid",
		columns: [

			/* Details */
			{ headerText: "uuid", 				   key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", 				   key: "name", dataType: "string" },
			{ headerText: "Remarks", 			   key: "remarks", dataType: "string", hidden: true },
			{ headerText: "Area (sq.m.)", 				   key: "area", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Volume (cu.m.)", 			   key: "volume", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Area Below Ground<br/>(sq.m.)", key: "areaBelowGround", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			/* Concrete */
			{ headerText: "Quantity", group: [
				{ headerText: "Cement<br/>(40kg)",  key: "concreteCement40kg", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number"},
				{ headerText: "Cement<br/>(50kg)",  key: "concreteCement50kg", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number"},
				{ headerText: "Sand<br/>(cu.m.)",   key: "concreteSand", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number"},
				{ headerText: "Gravel<br/>(cu.m.)", key: "concreteGravel", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number"}
			]},
			{ headerText: "Cost", group: [
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "concreteCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number"},
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "concreteCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number"},
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "concreteCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number"},
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "concreteCostGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number"}
			]},
			
			/* CHB */
			{ headerText: "CHB<br/>(Pieces)", 	 key: "chbTotal", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "CHB<br/>(PHP/Piece)", key: "chbCostTotal", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			/* CHB Laying */
			{ headerText: "Cement<br/>(40kg)", key: "chbLayingBags40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", key: "chbLayingBags50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",  key: "chbLayingSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },			
			{ headerText: "Cement<br/>(PHP/40kg)", key: "chbLayingCostBags40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "chbLayingCostBags50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",  key: "chbLayingCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },

			/* Plastering */
			{ headerText: "Cement<br/>(40kg)",	  key: "plasteringCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", 	  key: "plasteringCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)", 	  key: "plasteringSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)", key: "plasteringCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "plasteringCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)", 	  key: "plasteringCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			/* Footing */
			{ headerText: "Cement<br/>(40kg)",  key: "footingCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)",  key: "footingCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",   key: "footingSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(cu.m.)", key: "footingGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "footingCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "footingCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "footingCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "footingCostGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },

			/* METAL REINFORCEMENT CHB */

			{ key: "mrCHBSteelBar", dataType: "number", hidden: true },
			{ key: "mrCHBTieWireKg", dataType: "number", hidden: true },
			{ key: "mrCHBTieWireRoll", dataType: "number", hidden: true },
			{ key: "mrCHBCostSteelBar", dataType: "number", hidden: true },
			{ key: "mrCHBCostTieWireKg", dataType: "number", hidden: true },
			{ key: "mrCHBCostTieWireRoll", dataType: "number", hidden: true },
			{ key: "footingLength", dataType: "number", hidden: true },
			{ key: "footingWidth", dataType: "number", hidden: true },
			{ key: "footingHeight", dataType: "number", hidden: true }
		]
	});

	/* Details */
	$("#treegrid-details").igGrid({
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
			{ headerText: "Area (sq.m.)", 				   key: "area", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Volume (cu.m.)", 			   key: "volume", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Area Below<br/>Ground (sq.m.)", key: "areaBelowGround", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Footing", group: [
			{ headerText: "Length (m)",	key: "footingLength", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Width (m)", 	key: "footingWidth", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Height (m)",	key: "footingHeight", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number" }
			]},
			
			/* Concrete */
			{ headerText: "Cement<br/>(40kg)",  key: "concreteCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(50kg)",  key: "concreteCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(cu.m.)",   key: "concreteSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(cu.m.)", key: "concreteGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "concreteCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "concreteCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "concreteCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "concreteCostGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			
			/* CHB */
			{ headerText: "CHB<br/>(Pieces)", 	 key: "chbTotal", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "CHB<br/>(PHP/Piece)", key: "chbCostTotal", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			/* CHB Laying */
			{ headerText: "Cement<br/>(40kg)", key: "chbLayingBags40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", key: "chbLayingBags50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",  key: "chbLayingSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },			
			{ headerText: "Cement<br/>(PHP/40kg)", key: "chbLayingCostBags40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "chbLayingCostBags50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",  key: "chbLayingCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },

			/* Plastering */
			{ headerText: "Cement<br/>(40kg)",	  key: "plasteringCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", 	  key: "plasteringCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)", 	  key: "plasteringSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)", key: "plasteringCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "plasteringCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)", 	  key: "plasteringCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			/* Footing */
			{ headerText: "Cement<br/>(40kg)",  key: "footingCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)",  key: "footingCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",   key: "footingSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(cu.m.)", key: "footingGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "footingCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "footingCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "footingCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "footingCostGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number",  hidden: true },

			/* METAL REINFORCEMENT CHB */

			{ key: "mrCHBSteelBar", dataType: "number", hidden: true },
			{ key: "mrCHBTieWireKg", dataType: "number", hidden: true },
			{ key: "mrCHBTieWireRoll", dataType: "number", hidden: true },
			{ key: "mrCHBCostSteelBar", dataType: "number", hidden: true },
			{ key: "mrCHBCostTieWireKg", dataType: "number", hidden: true },
			{ key: "mrCHBCostTieWireRoll", dataType: "number", hidden: true }
		]
	});

	/* Details */
	$("#treegrid-mr-chb").igGrid({
		dataSource: flatDS,
		width: "100%",
		features:[
			{ name: "MultiColumnHeaders" },
			{ name: "Summaries" }
		],
		primaryKey: "uuid",
		columns: [

			/* Details */
			{ headerText: "uuid", 				   key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", 				   key: "name", dataType: "string" },
			{ headerText: "Remarks", 			   key: "remarks", dataType: "string", hidden: true },
			{ headerText: "Area (sq.m.)", 				   key: "area", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Volume (cu.m.)", 			   key: "volume", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Area Below Ground<br/>(sq.m.)", key: "areaBelowGround", format: "0", columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			/* Concrete */
			{ headerText: "Cement<br/>(40kg)",  key: "concreteCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(50kg)",  key: "concreteCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(cu.m.)",   key: "concreteSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(cu.m.)", key: "concreteGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "concreteCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "concreteCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "concreteCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "concreteCostGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true},
			
			/* CHB */
			{ headerText: "CHB<br/>(Pieces)", 	 key: "chbTotal", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "CHB<br/>(PHP/Piece)", key: "chbCostTotal", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			/* CHB Laying */
			{ headerText: "Cement<br/>(40kg)", key: "chbLayingBags40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", key: "chbLayingBags50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",  key: "chbLayingSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },			
			{ headerText: "Cement<br/>(PHP/40kg)", key: "chbLayingCostBags40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "chbLayingCostBags50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",  key: "chbLayingCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },

			/* Plastering */
			{ headerText: "Cement<br/>(40kg)",	  key: "plasteringCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)", 	  key: "plasteringCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)", 	  key: "plasteringSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)", key: "plasteringCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)", key: "plasteringCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)", 	  key: "plasteringCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			
			/* Footing */
			{ headerText: "Cement<br/>(40kg)",  key: "footingCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(50kg)",  key: "footingCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(cu.m.)",   key: "footingSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(cu.m.)", key: "footingGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/40kg)",  key: "footingCostCement40kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Cement<br/>(PHP/50kg)",  key: "footingCostCement50kg", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Sand<br/>(PHP/cu.m.)",   key: "footingCostSand", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number", hidden: true },
			{ headerText: "Gravel<br/>(PHP/cu.m.)", key: "footingCostGravel", formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number",  hidden: true },

			/* METAL REINFORCEMENT CHB */
			{ headerText: "Quantity", group: [
			{ headerText: "Steel Bar<br/>(Pieces)", key: "mrCHBSteelBar",					columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Tie Wire<br/>(Kilos)", key: "mrCHBTieWireKg",				columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Tie Wire<br/>(Rolls)", key: "mrCHBTieWireRoll",			columnCssClass: "cebedo-text-align-right", dataType: "number" }
			]},
			{ headerText: "Cost", group: [
			{ headerText: "Steel Bar<br/>(PHP/Piece)", key: "mrCHBCostSteelBar", 			formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Tie Wire<br/>(PHP/Kilo)", key: "mrCHBCostTieWireKg",		formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number" },
			{ headerText: "Tie Wire<br/>(PHP/Roll)", key: "mrCHBCostTieWireRoll",	formatter: formatCurrency, columnCssClass: "cebedo-text-align-right", dataType: "number" }
			]},

			{ key: "footingLength", dataType: "number", hidden: true },
			{ key: "footingWidth", dataType: "number", hidden: true },
			{ key: "footingHeight", dataType: "number", hidden: true }
		]
	});

});

$(document).ready(function() {

	$('#treegrid-concrete_summaries_footer_row_count').hide();
	$('#treegrid-concrete_summaries_footer_row_min').hide();
	$('#treegrid-concrete_summaries_footer_row_max').hide();
	$('#treegrid-concrete_summaries_footer_row_avg').hide();
	
	$('#treegrid-chb-footing_summaries_footer_row_count').hide();
	$('#treegrid-chb-footing_summaries_footer_row_min').hide();
	$('#treegrid-chb-footing_summaries_footer_row_max').hide();
	$('#treegrid-chb-footing_summaries_footer_row_avg').hide();
	
	$('#treegrid-chb-setting_summaries_footer_row_count').hide();
	$('#treegrid-chb-setting_summaries_footer_row_min').hide();
	$('#treegrid-chb-setting_summaries_footer_row_max').hide();
	$('#treegrid-chb-setting_summaries_footer_row_avg').hide();
	
	$('#treegrid-chb-plastering_summaries_footer_row_count').hide();
	$('#treegrid-chb-plastering_summaries_footer_row_min').hide();
	$('#treegrid-chb-plastering_summaries_footer_row_max').hide();
	$('#treegrid-chb-plastering_summaries_footer_row_avg').hide();

	$('#treegrid-chb-footing_summaries_footer_row_count').hide();
	$('#treegrid-chb-footing_summaries_footer_row_min').hide();
	$('#treegrid-chb-footing_summaries_footer_row_max').hide();
	$('#treegrid-chb-footing_summaries_footer_row_avg').hide();

	$('#treegrid-mr-chb_summaries_footer_row_count').hide();
	$('#treegrid-mr-chb_summaries_footer_row_min').hide();
	$('#treegrid-mr-chb_summaries_footer_row_max').hide();
	$('#treegrid-mr-chb_summaries_footer_row_avg').hide();
});
</script>
</html>