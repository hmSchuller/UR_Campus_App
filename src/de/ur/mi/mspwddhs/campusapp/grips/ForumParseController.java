package de.ur.mi.mspwddhs.campusapp.grips;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;

public class ForumParseController {

	private ForumListener listener;
	private CookieStore cookieStore;
	private ArrayList<Forum> myForum;
	public boolean authentificationFlag = false;

	public ForumParseController(ForumListener listener) {
		this.listener = listener;
	}

	public void initialize(String url) {
		myForum = new ArrayList<Forum>();
		new ParseForum().execute(url);
	}

	public interface ForumListener {
		public String getPass();

		public String getUser();

		public void cancel();

		public void onForumDownloadCompleted(ArrayList<Forum> result);

		public void passwordChange();

	}

	private class ParseForum extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String username = listener.getUser();
			String password = listener.getPass();
			String url = params[0];
			String loginHtml = loginToGrips(GripsParseController.domainName,
					username, password);

			String html = getHttpFromUrl(url);
			parseForum(html);
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if(authentificationFlag){
				listener.passwordChange();
			} else {
			listener.onForumDownloadCompleted(myForum);
			}
		}

		@Override
		protected void onCancelled() {
			listener.cancel();
		}

		private String loginToGrips(String url, String username, String password) {
			String result = "";
			cookieStore = new BasicCookieStore();
			AbstractHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			try {
				final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("realm", "ur"));
				nameValuePairs
						.add(new BasicNameValuePair("username", username));
				nameValuePairs
						.add(new BasicNameValuePair("password", password));
				nameValuePairs.add(new BasicNameValuePair("rememberusername",
						"1"));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = client.execute(post);
				cookieStore = client.getCookieStore();
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return "";
		}

		private String getHttpFromUrl(String url) {
			String result = "";
			@SuppressWarnings("resource")
			AbstractHttpClient client = new DefaultHttpClient();
			client.setCookieStore(cookieStore);
			HttpPost post = new HttpPost(url);
			try {
				HttpResponse response = client.execute(post);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					result += line;
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
			if(result.contains("Benutzername oder Kennwort vergessen?")){
				authentificationFlag  = true;
			}
			return result;
		}

		private void parseForum(String html) {

			Document doc = Jsoup.parse(html);
			try {
				Element forum = doc.getElementsByClass("forumheaderlist")
						.first();
				Elements rows = forum.getElementsByTag("tr");

				for (Element row : rows) {
					Element thread = row.select("td").select(".topic")
							.select("a[href]").first();
					String author = row.select("td").select(".author")
							.select("a[href]").text();
					Element date = row.select("td").select(".lastpost")
							.select("a[href]").last();
					if (thread != null) {
						myForum.add(new Forum(thread.text(), thread
								.attr("abs:href"), author, date.text()));
					}
				}
			} catch (Exception e) {
				listener.cancel();
			}

		}
	}
}
