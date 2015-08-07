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
					<c:url var="urlBack" value="/project/edit/" />
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
	
	// Tree grid.
	var flatDS = ${estimationoutput.estimatesAsJson};
	$("#treegrid-chb-footing").igTreeGrid({
		dataSource: flatDS,
		width: "100%",
		features:[
			{ name: "MultiColumnHeaders" }
		],
		primaryKey: "uuid",
		columns: [
			{ headerText: "uuid", key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", key: "name", dataType: "string" },
			{ headerText: "Remarks", key: "remarks", dataType: "string", hidden: true },
			{ headerText: "Area", key: "area", dataType: "number", hidden: true },
			{ headerText: "Volume", key: "volume", dataType: "number", hidden: true },
			{ headerText: "CHB Foundation Height", key: "chbFoundationHeight", dataType: "number", hidden: true },
			{ headerText: "CHB Footing Length", key: "footingLength", dataType: "number", hidden: true },
			
			{ headerText: "Cement (40kg)", key: "concreteCement40kg", dataType: "number", hidden: true},
			{ headerText: "Cement (50kg)", key: "concreteCement50kg", dataType: "number", hidden: true},
			{ headerText: "Sand (cu.m.)", key: "concreteSand", dataType: "number", hidden: true},
			{ headerText: "Gravel (cu.m.)", key: "concreteGravel", dataType: "number", hidden: true},
			
			{ headerText: "CHB", key: "totalCHB", dataType: "number" },
			
			{ headerText: "Cement (40kg)", key: "chbLayingBags40kg", dataType: "number", hidden: true },
			{ headerText: "Cement (50kg)", key: "chbLayingBags50kg", dataType: "number", hidden: true },
			{ headerText: "Sand (cu.m.)", key: "chbLayingSand", dataType: "number", hidden: true },
			
			
			{ headerText: "plasteringCement40kg", key: "plasteringCement40kg", dataType: "number", hidden: true },
			{ headerText: "plasteringCement50kg", key: "plasteringCement50kg", dataType: "number", hidden: true },
			{ headerText: "plasteringSand", key: "plasteringSand", dataType: "number", hidden: true },
			
			{ headerText: "Footing", group: [
			{ headerText: "Cement (40kg)", key: "footingCement40kg", dataType: "number" },
			{ headerText: "Cement (50kg)", key: "footingCement50kg", dataType: "number" },
			{ headerText: "Sand (cu.m.)", key: "footingSand", dataType: "number" },
			{ headerText: "Gravel (cu.m.)", key: "footingGravel", dataType: "number" }
			]}
		]
	});
	$("#treegrid-chb-plastering").igTreeGrid({
		dataSource: flatDS,
		width: "100%",
		features:[
			{ name: "MultiColumnHeaders" }
		],
		primaryKey: "uuid",
		columns: [
			{ headerText: "uuid", key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", key: "name", dataType: "string" },
			{ headerText: "Remarks", key: "remarks", dataType: "string", hidden: true },
			{ headerText: "Area", key: "area", dataType: "number", hidden: true },
			{ headerText: "Volume", key: "volume", dataType: "number", hidden: true },
			{ headerText: "CHB Foundation Height", key: "chbFoundationHeight", dataType: "number", hidden: true },
			{ headerText: "CHB Footing Length", key: "footingLength", dataType: "number", hidden: true },
			
			{ headerText: "Cement (40kg)", key: "concreteCement40kg", dataType: "number", hidden: true},
			{ headerText: "Cement (50kg)", key: "concreteCement50kg", dataType: "number", hidden: true},
			{ headerText: "Sand (cu.m.)", key: "concreteSand", dataType: "number", hidden: true},
			{ headerText: "Gravel (cu.m.)", key: "concreteGravel", dataType: "number", hidden: true},
			
			{ headerText: "CHB", key: "totalCHB", dataType: "number" },
			
			{ headerText: "Cement (40kg)", key: "chbLayingBags40kg", dataType: "number", hidden: true },
			{ headerText: "Cement (50kg)", key: "chbLayingBags50kg", dataType: "number", hidden: true },
			{ headerText: "Sand (cu.m.)", key: "chbLayingSand", dataType: "number", hidden: true },
			
			
			{ headerText: "Plastering", group: [
			{ headerText: "Cement (40kg)", key: "plasteringCement40kg", dataType: "number" },
			{ headerText: "Cement (50kg)", key: "plasteringCement50kg", dataType: "number" },
			{ headerText: "Sand (cu.m.)", key: "plasteringSand", dataType: "number" }
			]},
			
			{ headerText: "footingCement40kg", key: "footingCement40kg", dataType: "number", hidden: true  },
			{ headerText: "footingCement50kg", key: "footingCement50kg", dataType: "number", hidden: true  },
			{ headerText: "footingSand", key: "footingSand", dataType: "number", hidden: true  },
			{ headerText: "footingGravel", key: "footingGravel", dataType: "number", hidden: true  }
		]
	});
	$("#treegrid-chb-setting").igTreeGrid({
		dataSource: flatDS,
		width: "100%",
        features:[
            { name: "MultiColumnHeaders" }
        ],
		primaryKey: "uuid",
		columns: [
			{ headerText: "uuid", key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", key: "name", dataType: "string" },
			{ headerText: "Remarks", key: "remarks", dataType: "string", hidden: true },
			{ headerText: "Area", key: "area", dataType: "number", hidden: true },
			{ headerText: "Volume", key: "volume", dataType: "number", hidden: true },
			{ headerText: "CHB Foundation Height", key: "chbFoundationHeight", dataType: "number", hidden: true },
			{ headerText: "CHB Footing Length", key: "footingLength", dataType: "number", hidden: true },
			
			{ headerText: "Cement (40kg)", key: "concreteCement40kg", dataType: "number", hidden: true},
			{ headerText: "Cement (50kg)", key: "concreteCement50kg", dataType: "number", hidden: true},
			{ headerText: "Sand (cu.m.)", key: "concreteSand", dataType: "number", hidden: true},
			{ headerText: "Gravel (cu.m.)", key: "concreteGravel", dataType: "number", hidden: true},
			
			{ headerText: "CHB", key: "totalCHB", dataType: "number" },
			
           	{ headerText: "Setting / Laying", group: [
			{ headerText: "Cement (40kg)", key: "chbLayingBags40kg", dataType: "number" },
			{ headerText: "Cement (50kg)", key: "chbLayingBags50kg", dataType: "number" },
			{ headerText: "Sand (cu.m.)", key: "chbLayingSand", dataType: "number" }
      		]},
			
			
			{ headerText: "plasteringCement40kg", key: "plasteringCement40kg", dataType: "number", hidden: true  },
			{ headerText: "plasteringCement50kg", key: "plasteringCement50kg", dataType: "number", hidden: true  },
			{ headerText: "plasteringSand", key: "plasteringSand", dataType: "number", hidden: true  },
			
			{ headerText: "footingCement40kg", key: "footingCement40kg", dataType: "number", hidden: true  },
			{ headerText: "footingCement50kg", key: "footingCement50kg", dataType: "number", hidden: true  },
			{ headerText: "footingSand", key: "footingSand", dataType: "number", hidden: true  },
			{ headerText: "footingGravel", key: "footingGravel", dataType: "number", hidden: true  }
		]
	});
	$("#treegrid-concrete").igTreeGrid({
		dataSource: flatDS,
		width: "100%",
		primaryKey: "uuid",
		columns: [
			{ headerText: "uuid", key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", key: "name", dataType: "string" },
			{ headerText: "Remarks", key: "remarks", dataType: "string", hidden: true },
			{ headerText: "Area", key: "area", dataType: "number", hidden: true },
			{ headerText: "Volume", key: "volume", dataType: "number", hidden: true },
			{ headerText: "CHB Foundation Height", key: "chbFoundationHeight", dataType: "number", hidden: true },
			{ headerText: "CHB Footing Length", key: "footingLength", dataType: "number", hidden: true },
			
			{ headerText: "Cement (40kg)", key: "concreteCement40kg", dataType: "number"},
			{ headerText: "Cement (50kg)", key: "concreteCement50kg", dataType: "number"},
			{ headerText: "Sand (cu.m.)", key: "concreteSand", dataType: "number"},
			{ headerText: "Gravel (cu.m.)", key: "concreteGravel", dataType: "number"},
			
			{ headerText: "CHB", key: "totalCHB", dataType: "number", hidden: true  },
			
			{ headerText: "chbLayingBags40kg", key: "chbLayingBags40kg", dataType: "number", hidden: true  },
			{ headerText: "chbLayingBags50kg", key: "chbLayingBags50kg", dataType: "number", hidden: true  },
			{ headerText: "chbLayingSand", key: "chbLayingSand", dataType: "number", hidden: true  },
			
			{ headerText: "plasteringCement40kg", key: "plasteringCement40kg", dataType: "number", hidden: true  },
			{ headerText: "plasteringCement50kg", key: "plasteringCement50kg", dataType: "number", hidden: true  },
			{ headerText: "plasteringSand", key: "plasteringSand", dataType: "number", hidden: true  },
			
			{ headerText: "footingCement40kg", key: "footingCement40kg", dataType: "number", hidden: true  },
			{ headerText: "footingCement50kg", key: "footingCement50kg", dataType: "number", hidden: true  },
			{ headerText: "footingSand", key: "footingSand", dataType: "number", hidden: true  },
			{ headerText: "footingGravel", key: "footingGravel", dataType: "number", hidden: true  }
		]
	});
	$("#treegrid-details").igTreeGrid({
		dataSource: flatDS,
		width: "100%",
		primaryKey: "uuid",
		columns: [
			{ headerText: "uuid", key: "uuid", dataType: "string", hidden: true },
			{ headerText: "Name", key: "name", dataType: "string" },
			{ headerText: "Remarks", key: "remarks", dataType: "string" },
			{ headerText: "Area (sq.m.)", key: "area", dataType: "number" },
			{ headerText: "Volume (cu.m.)", key: "volume", dataType: "number" },
			{ headerText: "Foundation Height (m)", key: "chbFoundationHeight", dataType: "number" },
			{ headerText: "Footing Length (m)", key: "footingLength", dataType: "number" },
			
			{ headerText: "Cement (40kg)", key: "concreteCement40kg", dataType: "number", hidden: true  },
			{ headerText: "Cement (50kg)", key: "concreteCement50kg", dataType: "number", hidden: true  },
			{ headerText: "Sand (cu.m.)", key: "concreteSand", dataType: "number", hidden: true  },
			{ headerText: "Gravel (cu.m.)", key: "concreteGravel", dataType: "number", hidden: true  },
			
			{ headerText: "CHB", key: "totalCHB", dataType: "number", hidden: true  },
			
			{ headerText: "chbLayingBags40kg", key: "chbLayingBags40kg", dataType: "number", hidden: true  },
			{ headerText: "chbLayingBags50kg", key: "chbLayingBags50kg", dataType: "number", hidden: true  },
			{ headerText: "chbLayingSand", key: "chbLayingSand", dataType: "number", hidden: true  },
			
			{ headerText: "plasteringCement40kg", key: "plasteringCement40kg", dataType: "number", hidden: true  },
			{ headerText: "plasteringCement50kg", key: "plasteringCement50kg", dataType: "number", hidden: true  },
			{ headerText: "plasteringSand", key: "plasteringSand", dataType: "number", hidden: true  },
			
			{ headerText: "footingCement40kg", key: "footingCement40kg", dataType: "number", hidden: true  },
			{ headerText: "footingCement50kg", key: "footingCement50kg", dataType: "number", hidden: true  },
			{ headerText: "footingSand", key: "footingSand", dataType: "number", hidden: true  },
			{ headerText: "footingGravel", key: "footingGravel", dataType: "number", hidden: true  }
		]
	});
});
</script>
</html>