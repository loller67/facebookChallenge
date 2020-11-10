
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;

import facebook4j.FacebookException;
public class Main {
	public static void main(String args[]) throws FacebookException, ClientProtocolException, IOException, InterruptedException, URISyntaxException, HttpException {
	    Properties properties = new Properties();
        properties.load(new FileInputStream("src/fbData"));
	    
		CRUDFbAPI api = new CRUDFbAPI();
		api.loginToFb(properties.getProperty("token"), properties.getProperty("appId"), properties.getProperty("appSecret"));
		api.createPost();
		api.editPost();
		api.deletePost();
		System.out.println("API TESTS COMPLETED SUCCESSFULLY");
		
	    CRUDFbUi ui = new CRUDFbUi();
		ui.login(properties.getProperty("email"), properties.getProperty("password"), properties.getProperty("pageId"));
		ui.createPost();
		ui.editPost();
		ui.deletePost();
		System.out.println("UI TESTS COMPLETED SUCCESSFULLY");
	}
}