package de.ur.mi.mspwddhs.campusapp.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.os.AsyncTask;

public class SendMailController extends AsyncTask<String, String, String> {

	String from;
	String to;
	String user;
	String password;
	String mailText;
	String mailSubject;
	
	String host = "mail.uni-regensburg.de";

	@Override
	protected String doInBackground(String... params) {

		from = params[0];
		user = params[1];
		password = params[2];
		mailText = params[3];
		mailSubject = params[4];
		to = params[5];
							
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtps.ssl.trust", host);

		Session session = Session.getDefaultInstance(props, null);

		try {
			
			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(from));

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			message.setSubject(mailSubject);

			message.setText(mailText);

			Transport transport = session.getTransport("smtps");
			transport.connect(host,465,user,password);
			transport.sendMessage(message,message.getAllRecipients());
			transport.close();
			
		} 
		catch (MessagingException mex) 
		{
			mex.printStackTrace();
		}
		return null;
	}

}
