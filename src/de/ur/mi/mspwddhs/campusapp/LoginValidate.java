package de.ur.mi.mspwddhs.campusapp;

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

import android.content.Context;
import android.os.AsyncTask;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.grips.GripsParseController;
import de.ur.mi.mspwddhs.campusapp.secruity.SecurityMain;

public class LoginValidate {
	
	private LoginListener listener;
	private boolean response;
	private Context context;
	
	public LoginValidate(LoginListener listener, Context context){
		this.listener = listener;
		this.context = context;
		response = false;
	}
	
	
	private class Login extends AsyncTask<String, String, String>{
		
		private CookieStore cookieStore;
		private Database db;

		@Override
		protected String doInBackground(String... params) {
			String user = params[0];
			String pass = params[1];
			String profileUrl = "https://elearning.uni-regensburg.de/user/edit.php";
			loginToGrips(GripsParseController.domainName,
					user, pass);
			String html = getHttpFromUrl(profileUrl);
			if(html.contains("Benutzername oder Kennwort vergessen?")){
				response = false;
			} else {
				String email = parseProfile(html);
				db = new Database(context);
				db.open();
				try {
					pass = SecurityMain.encrypt(pass);
				} catch (Exception e) {
					e.printStackTrace();
				}
				db.saveLoginData(user, pass, email);
				db.close();
				response = true;
				
			}
			
			return "";
		}
		
		private String parseProfile(String html) {
			Document doc = Jsoup.parse(html);
			Element e = doc.select("[name*=email]").first();
			return e.attr("value");
		}

		@Override
		protected void onPostExecute(String result) {
		listener.onFinish(response);
		}
		
		private void loginToGrips(String url, String username, String password) {
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
	}
	
	public interface LoginListener{
		public void onFinish(boolean response);
	}

	public void initiate(String user, String pass) {
		new Login().execute(user, pass);
	}
}
