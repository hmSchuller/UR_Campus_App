package de.ur.mi.mspwddhs.campusapp.mail;

public class Email {
	
	private String from;
	private String to;
	private String date;
	private String subject;
	private String content;
	
	
	public Email (String from, String to, String date, String subject, String content) {
		
		this.from= from;
		this.to = to;
		this.date = date;
		this.subject = subject;
		this.content = content;
				
	}

	public String getFrom() {
		return from;
	}
	
	public String getTo() {
		return to;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getContent() {
		return content;
	}

	
}
