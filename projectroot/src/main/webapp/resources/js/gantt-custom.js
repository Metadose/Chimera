// Column configurations.
gantt.config.columns = [
    {name:"text",       label:" ",  width:"*", tree:true },
    {name:"start_date", label:"Start", align: "center" },
    {name:"duration",   label:"Man Days",   align: "center" },
    {name:"add",        label:"",           width:44 }
];

// Text for the task.
gantt.templates.task_text = function(start, end, task){
	if(typeof task.content !== "undefined"){
		return "<b>"+task.text+"</b> ("+task.content+")";	
	}
	return "<b>"+task.text+"</b>";
};

// Setting the gantt scale to monthly.
gantt.config.scale_unit = "month";
gantt.config.date_scale = "%F, %Y";
gantt.config.scale_height = 50;
gantt.config.subscales = [
	{unit:"day", step:1, date:"%j, %D" }
];

// Code to call as a workaround over gantt bug where chart doesn't
// render if placed in a tab.
var ganttRendered = false;
$("#tab_timeline-href").mouseout(function() {
	if(!ganttRendered){
		ganttRendered = true;
		gantt.render();
	}
});

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