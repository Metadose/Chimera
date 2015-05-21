// Tooltip.
gantt.templates.tooltip_text = function(start, end, task){
	var startStr = start.getFullYear() + "-" + (start.getMonth() + 1) + "-" + start.getDate();
	var endStr = end.getFullYear() + "-" + (end.getMonth() + 1) + "-" + end.getDate();
	
	var template = "";
	template += "<b>("+task.type+") "+task.text+"</b><br/>";
	template += "<br/><b>Duration:</b> "+ task.duration;
	template += "<br/><b>Start Date:</b> "+ startStr;
	template += "<br/><b>End Date:</b> "+ endStr;
	
	// If undefined, then don't display.
	if(task.content !== undefined) {
		template += "<br/><br/>" + task.content;
	}
    return template;
};

// Column configurations.
gantt.config.columns = [
    {name:"text",       label:" ",  width:"200", tree:true },
    {name:"type",       label:"Type",  align: "center" },
    {name:"start_date", label:"Start", width:"100", align: "center" },
    {name:"duration",   label:"Man Days", width:"80",  align: "center" }
];

// Text for the task.
gantt.templates.task_text = function(start, end, task){
	if(typeof task.content !== "undefined"){
		return "<b>"+task.text+"</b> ("+task.content+")";	
	}
	return "<b>"+task.text+"</b>";
};

gantt.config.grid_width = 450;

// Setting the gantt scale to monthly.
gantt.config.scale_unit = "month";
gantt.config.date_scale = "%F, %Y";
gantt.config.scale_height = 50;
gantt.config.sort = true;
gantt.config.readonly = true;
gantt.config.subscales = [
	{unit:"day", step:1, date:"%j, %D" }
];

//Returned string refers to a CSS declared above.
gantt.templates.task_class = function(start, end, task){
	if(task.status == 0){
		return "gantt-info";
	} else if(task.status == 1) {
		return "gantt-primary";
	} else if(task.status == 2) {
		return "gantt-success";
	} else if(task.status == 3) {
		return "gantt-danger";
	} else if(task.status == 4) {
		return "gantt-default";
	}
};