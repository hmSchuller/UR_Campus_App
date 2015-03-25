package de.ur.mi.mspwddhs.campusapp.grips;

public class Link {
	private String name;
	private String url;
	private String author;

	public Link(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}
	
	public void setAuthor(String author){
		this.author = author;
	}
	
	public String getAuthor(){
		return author;
	}
	
}