package de.ur.mi.mspwddhs.campusapp.grips;

public class MyThread {
	private String author;
	private String date;
	private String message;
	
	public MyThread(String author, String date, String message){
		this.author = author;
		this.date = date;
		this.message = message;
	}
	
	public String getAuthor(){
		return author;
	}
	
	public String getDate(){
		return date;
	}
	
	public String getMessage(){
		return message;
	}
}
