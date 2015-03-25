package de.ur.mi.mspwddhs.campusapp.mail;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.secruity.SecurityMain;
import android.os.AsyncTask;

public class GetMailController extends AsyncTask<String, String, String> {

	private Database db;
	Folder inbox;
	
	private boolean authenticationFlag = false;
	

	private String user;
	private String password;
	private String host = "imap.uni-regensburg.de";
	private boolean textIsHtml = false;

	private int numOfMessages;

	ArrayList<Email> emailList;

	private emailListener listener;

	Email email;

	private Message[] messages;

	public GetMailController(emailListener listener, Database db) {
		this.db = db;
		initialise(listener);
	}

	public void initialise(emailListener listener) {

		this.listener = listener;

	}

	@Override
	protected String doInBackground(String... params) {

		user = db.getLoginData().get(0);
		password = db.getLoginData().get(1);
		try {
			password = SecurityMain.decrypt(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setup();
		return null;

	}

	private void setup() {

		handleDifferentMailTexts();

		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		try {

			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect(host, user, password);

			inbox = store.getFolder("INBOX");
			numOfMessages = inbox.getMessageCount();

			inbox.open(Folder.READ_WRITE);

			if (db.isMailTableEmpty()) {
				messages = inbox.search(new FlagTerm(new Flags(Flag.SEEN),
						false));
				inbox.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
			} else {
				messages = inbox.search(new FlagTerm(new Flags(Flag.RECENT),
						true));
				inbox.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
			}

			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add(FetchProfile.Item.CONTENT_INFO);
			inbox.fetch(messages, fp);

			try {
				fillMails(messages);
				inbox.close(true);
				store.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			return;
		} catch (AuthenticationFailedException e) {
			authenticationFlag = true;
		} catch (MessagingException e) {
			System.out.println();
			e.printStackTrace();
			return;
		}

	}

	private void handleDifferentMailTexts() {

		MailcapCommandMap mc = (MailcapCommandMap) CommandMap
				.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");

	}

	@Override
	protected void onPostExecute(String result) {
		if (authenticationFlag) {
			listener.wrongPass();
		}
			listener.onUpdateCompleted();
	}

	private void fillMails(Message[] message) throws MessagingException,
			IOException {
		if (numOfMessages > 0) {
			emailList = new ArrayList<Email>();

			for (int i = 0; i < numOfMessages; i++) {
				getCompleteMail(message[i]);

			}
		}

	}

	private void getCompleteMail(Message message) throws MessagingException,
			IOException {

		Address[] param;
		String subject;

		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		String to = "";
		String from = "";

		if ((param = message.getFrom()) != null) {
			for (int j = 0; j < param.length; j++) {
				from += param[j];
			}
		}

		if ((param = message.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < param.length; j++) {
				to += param[j];
			}
		}

		if (message.getSubject() == null) {
			subject = "";
		} else {
			subject = message.getSubject();
		}
		String currentDateString = df.format(message.getReceivedDate());
		String content = getText(message);

		try {
			db.addContentMail(from, to, currentDateString, subject, content);
		} catch (Exception e) {
		}
	}

	public interface emailListener {

		public void onUpdateCompleted();

		public void wrongPass();

	}

	private String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String contentString = (String) p.getContent();
			textIsHtml = p.isMimeType("text/html");
			return contentString;
		}

		if (p.isMimeType("multipart/alternative")) {

			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}

}
