package de.ur.mi.mspwddhs.campusapp.grips;

public class Grades {
	private String taskName;
	private String points;
	private String range;
	private String percentile;
	private String feedback;
	
	public Grades(String taskName, String points, String range, String percentile, String feedback){
		this.taskName = taskName;
		this.points = points;
		this.range = range;
		this.percentile = percentile;
		this.feedback = feedback;
	}
	
	public String getTaskName(){
		return taskName;
	}
	public String getPoints(){
		return points;
	}
	public String getRange(){
		return range;
	}
	public String getPercentile(){
		return percentile;
	}
	public String getFeedback(){
		return feedback;
	}
}
