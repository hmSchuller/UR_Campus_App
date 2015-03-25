package de.ur.mi.mspwddhs.campusapp.grips;

public class Forum {
	private Link link;
	private String author;
	private String date;
	
	public Forum(String name, String url, String author, String date){
		link = new Link(name, url);
		this.author = author;
		this.date = date;
	}
	
	public Link getLink(){
		return link;
	}
	
	public String getAuthor(){
		return author;
	}
	
	public String getDate(){
		return date;
	}
}
