package utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class ApiUtils {
	
	/*
	 * Generic REST API Testing Methods
	 */
	public static String apiGetRequest(String uri, String authorizationKey) {
		String jsonResponse = null;
		try {
			HttpGet httpget = new HttpGet(uri);
			httpget.setHeader("Content-Type", "application/json");
			httpget.setHeader("Authorization", authorizationKey);
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(httpget);
			jsonResponse = EntityUtils.toString(response.getEntity());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return jsonResponse;
	}
	
	public static String apiPostRequest(String uri, String requestBody, String authorizationKey) {
		String jsonResponse = null;
		try {
			HttpPost httppost = new HttpPost(uri);
			httppost.setHeader("Content-Type", "application/json");
			httppost.setHeader("Authorization", authorizationKey);
			httppost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(httppost);
			jsonResponse = EntityUtils.toString(response.getEntity());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return jsonResponse;
	}
	
	public static String apiPatchRequest(String uri, String requestBody) {
		String jsonResponse = null;
		try {
			HttpPatch httpPatch = new HttpPatch(uri);
			httpPatch.setHeader("Content-Type", "application/json");
			httpPatch.setHeader("Authorization", "Basic YXpibHVlOjd0c2U3M2tnZ2c0dDduanljNXp6d3E2ZGR4YWdzcG83N2k3b3lqYzVoeTdod2xkc21hcWE=");
			httpPatch.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(httpPatch);
			jsonResponse = EntityUtils.toString(response.getEntity());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return jsonResponse;
	}
	

}
