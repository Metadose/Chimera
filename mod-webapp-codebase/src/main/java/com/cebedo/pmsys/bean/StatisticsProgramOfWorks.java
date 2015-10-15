package com.cebedo.pmsys.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.cebedo.pmsys.model.Task;

public class StatisticsProgramOfWorks extends SummaryStatistics {

    private static final long serialVersionUID = -7423281390013147949L;

    private List<Double> durations;
    private List<Double> actualDurations;
    private List<Double> differenceDurations;
    private Set<Task> tasks;

    public StatisticsProgramOfWorks() {
	;
    }

    public StatisticsProgramOfWorks(Set<Task> tasks2) {
	addValues(tasks2);
    }

    private void addValues(Set<Task> tasks) {
	for (Task task : tasks) {

	    double duration = task.getDuration();
	    this.durations.add(duration);

	    if (task.isCompleted()) {
		double actualDuration = task.getActualDuration();
		this.actualDurations.add(actualDuration);
		this.differenceDurations.add(duration - actualDuration);
	    }
	}
	this.tasks = tasks;
    }

    /**
     * Get tasks with the max duration.
     * 
     * @return
     */
    public List<Task> getMaxDuration() {
	addValuesPlanned();
	double greatestDuration = getMax();
	clear();
	return getMatchingPlannedTasks(greatestDuration);
    }

    /**
     * Get tasks with the max actual duration.
     * 
     * @return
     */
    public List<Task> getMaxActualDuration() {
	addValuesActual();
	double greatestDuration = getMax();
	clear();
	return getMatchingActualTasks(greatestDuration);
    }

    public List<Task> getMinDuration() {
	addValuesPlanned();
	double leastDuration = getMin();
	clear();
	return getMatchingPlannedTasks(leastDuration);
    }

    private List<Task> getMatchingPlannedTasks(double comparator) {
	List<Task> tasksToReturn = new ArrayList<Task>();
	for (Task task : this.tasks) {
	    if (comparator == task.getDuration()) {
		tasksToReturn.add(task);
	    }
	}
	return tasksToReturn;
    }

    private void addValuesPlanned() {
	for (double duration : this.durations) {
	    addValue(duration);
	}
    }

    public List<Task> getMinActualDuration() {
	addValuesActual();
	double leastDuration = getMin();
	clear();
	return getMatchingActualTasks(leastDuration);
    }

    private List<Task> getMatchingActualTasks(double comparator) {
	List<Task> tasksToReturn = new ArrayList<Task>();
	for (Task task : this.tasks) {
	    if (!task.isCompleted()) {
		continue;
	    }
	    if (comparator == task.getActualDuration()) {
		tasksToReturn.add(task);
	    }
	}
	return tasksToReturn;
    }

    private void addValuesActual() {
	for (double duration : this.actualDurations) {
	    addValue(duration);
	}
    }

    public List<Task> getMaxDifferenceDuration() {
	addValuesDifference();
	double greatestDiff = getMax();
	clear();
	return getMatchingDifferenceTasks(greatestDiff);
    }

    public List<Task> getMinDifferenceDuration() {
	addValuesDifference();
	double leastDiff = getMin();
	clear();
	return getMatchingDifferenceTasks(leastDiff);
    }

    private void addValuesDifference() {
	for (double diff : this.differenceDurations) {
	    addValue(diff);
	}
    }

    private List<Task> getMatchingDifferenceTasks(double comparator) {
	List<Task> tasksToReturn = new ArrayList<Task>();
	for (Task task : this.tasks) {
	    if (!task.isCompleted()) {
		continue;
	    }
	    double duration = task.getDuration();
	    double actual = task.getActualDuration();
	    if (comparator == (duration - actual)) {
		tasksToReturn.add(task);
	    }
	}
	return tasksToReturn;
    }

    public double getMeanDuration() {
	addValuesPlanned();
	double mean = getMean();
	clear();
	return mean;
    }

    public double getMeanActualDuration() {
	addValuesActual();
	double mean = getMean();
	clear();
	return mean;
    }

    public double getMeanDifference() {
	addValuesDifference();
	double mean = getMean();
	clear();
	return mean;
    }

}
