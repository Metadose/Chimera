package com.cebedo.pmsys.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.cebedo.pmsys.model.Task;

public class StatisticsProgramOfWorks extends SummaryStatistics {

    private static final long serialVersionUID = -7423281390013147949L;

    private List<Double> durations = new ArrayList<Double>();
    private List<Double> actualDurations = new ArrayList<Double>();
    private List<Double> differenceDurations = new ArrayList<Double>();
    private List<Double> absoluteDurations = new ArrayList<Double>();
    private Set<Task> tasks = new HashSet<Task>();

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
		double diff = duration - actualDuration;
		this.differenceDurations.add(diff);
		this.absoluteDurations.add(Math.abs(diff));
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

    public List<Task> getMaxAbsoluteDuration() {
	addValuesAbsolute();
	double greatestDiff = getMax();
	clear();
	return getMatchingDifferenceTasks(greatestDiff, true);
    }

    public List<Task> getMinAbsoluteDuration() {
	addValuesAbsolute();
	double leastDiff = getMin();
	clear();
	return getMatchingDifferenceTasks(leastDiff, true);
    }

    private void addValuesAbsolute() {
	for (double diff : this.absoluteDurations) {
	    addValue(diff);
	}
    }

    private void addValuesDifference() {
	for (double diff : this.differenceDurations) {
	    addValue(diff);
	}
    }

    private List<Task> getMatchingDifferenceTasks(double comparator) {
	return getMatchingDifferenceTasks(comparator, false);
    }

    private List<Task> getMatchingDifferenceTasks(double comparator, boolean absolute) {
	List<Task> tasksToReturn = new ArrayList<Task>();
	for (Task task : this.tasks) {
	    if (!task.isCompleted()) {
		continue;
	    }
	    double duration = task.getDuration();
	    double actual = task.getActualDuration();
	    double diff = absolute ? Math.abs(duration - actual) : duration - actual;
	    if (comparator == diff) {
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

    public double getMeanAbsolute() {
	addValuesAbsolute();
	double mean = getMean();
	clear();
	return mean;
    }

}
