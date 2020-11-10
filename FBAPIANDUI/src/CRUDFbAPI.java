import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

import org.apache.http.HttpException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.testng.Assert;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;

public class CRUDFbAPI {

    private String token;
    private String postId;
	private Facebook facebook;
    	
	public void loginToFb(String token, String appId, String appSecret) {
		this.token = token;
		this.facebook = new FacebookFactory().getInstance();
		this.facebook.setOAuthAppId(appId, appSecret);
		this.facebook.setOAuthAccessToken(new AccessToken(this.token, null));
		System.out.println("Login successful");
	}
	
	public void createPost() throws FacebookException, MalformedURLException {
		String message = "test message " + new Random().nextInt(100);
		this.postId = this.facebook.postLink(new URL("https://google.com"), message);
		System.out.println("Post creation completed");
		System.out.println("Post id: " + this.postId + " Message text: " + this.facebook.getPost(this.postId).getMessage());
		
		//checks that postId is correct and that the message has been posted correctly
        Assert.assertEquals(this.facebook.getPost(this.postId).getId(), postId);
        Assert.assertEquals(this.facebook.getPost(this.postId).getMessage(), message);
	}

	public void editPost() throws IOException, FacebookException, URISyntaxException, HttpException, InterruptedException {
		String newMessage = "changed" + new Random().nextInt(100);
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("https://graph.facebook.com/v8.0/" + this.postId + "/?message=" + newMessage + "&access_token=" + this.token);
		httpclient.execute(httppost);
		System.out.println("Post modification completed");
		System.out.println("Post id: " + this.postId + " Message text: " + this.facebook.getPost(this.postId).getMessage());

		//checks that postId has not changed and that the message has been edited correctly
        Assert.assertEquals(this.facebook.getPost(this.postId).getId(), postId);
        Assert.assertEquals(this.facebook.getPost(this.postId).getMessage(), newMessage);
	}

	public void deletePost() throws FacebookException {
		boolean deletion = facebook.deletePost(this.postId);
		System.out.println("Post deletion completed");
		
		//checks that deletion has been performed correctly.
        Assert.assertEquals(deletion, true);
	}
}
