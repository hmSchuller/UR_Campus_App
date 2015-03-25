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
import android.content.Context;
import android.os.AsyncTask;
public class ThreadParseController {
	
	private ArrayList<MyThread> myThread;
	private ThreadParseListener listener;
	private Context context;
	
	public ThreadParseController(ThreadParseListener listener, Context context){
		this.listener = listener;
		myThread = new ArrayList<MyThread>();
	}
	
	public void initialize(String url){
		new ParseThread().execute(url);
	}
	
	public interface ThreadParseListener{
		public String getUser();
		public String getPass();
		public void onThreadDownloadCompleted(ArrayList<MyThread> data);
	}
	
	private class ParseThread extends AsyncTask<String, String, String>{
		private CookieStore cookieStore;
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
			listener.onThreadDownloadCompleted(myThread);
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
			return result;
		}
		private void parseForum(String html) {
			Document doc = Jsoup.parse(html);
			Elements forum = doc.select(".forumpost");
			
			for( Element post : forum) {
				String authorDate = post.select(".author").first().text();
				authorDate = authorDate.substring(4);
				String[] ad = authorDate.split(" - ");
				String message = post.select(".posting").first().text();
				myThread.add(new MyThread(ad[0], ad[1], message));
			}
		}
	}
}