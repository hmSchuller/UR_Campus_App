package de.ur.mi.mspwddhs.campusapp.grips;

import java.util.ArrayList;

public class Course {
	
	private Link grades;
	private ArrayList<Link> listLinks;
	private String courseName;
	private String courseUrl;
	
	
	public Course(String courseName, String courseUrl){
		this.courseName = courseName;
		this.courseUrl = courseUrl;
		listLinks = new ArrayList<Link>();
	}
	
	public String getCourseName(){
		return courseName;
	}
	
	public String getCourseUrl(){
		return courseUrl;
	}
	
	public ArrayList<Link> getListLinks(){
		return listLinks;
	}
	
	public void saveForum(String name, String url){
		Link link = new Link(name, url);
		listLinks.add(link);
	}
	
	public Link getGrades(){
		return grades;
	}
	
	public void saveGrades(String name, String url){
		grades = new Link(name, url);
	}
	
	
	}
