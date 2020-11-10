import java.time.Duration;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class CRUDFbUi {

	private WebDriver driver;
	private WebDriverWait wait;
	private String message;
	private String firstPostTextPath = "/html/body/div[1]/div/div[1]/div[1]/div[3]/div/div/div[1]/div[1]/div[2]/div/div/div[4]/div[2]/div/div[2]/div[3]/div/div/div[1]/div/div/div/div/div/div/div/div/div/div[2]/div/div[3]/div/div/div/div";
	private String postActionsSelector = "div.dp1hu0rb:nth-child(3) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(3) > div:nth-child(1)";
	private String notNowSelector = "/html/body/div[1]/div/div[1]/div[1]/div[4]/div/div/div[2]/div/div/div[1]/div/div[2]/div/div/div/div[4]/div/div[2]/div[2]/div[1]/div/span/span";
	private String writePostSelector = "/html/body/div[1]/div/div[1]/div[1]/div[4]/div/div/div[1]/div/div[2]/div/div/div/form/div/div[1]/div/div[2]/div[2]/div[1]/div/div[1]/div[1]/div/div/div/div/div[2]/div";
	private String editPostSelector = "/html/body/div[1]/div/div[1]/div[1]/div[4]/div/div/div[1]/div/div[2]/div/div/div/form/div/div[1]/div/div[2]/div[2]/div[1]/div/div[1]/div[1]/div/div/div/div/div/div";

	public void login(String email, String password, String userId) {
        this.driver = new FirefoxDriver();
        this.driver.get("https://www.facebook.com/Test-page-109572100959612");
        System.out.println("Successfully opened facebook");
        this.driver.manage().window().maximize();
        this.driver.findElement(By.id("email")).sendKeys(email);
        this.driver.findElement(By.id("pass")).sendKeys(password);
        this.driver.findElement(By.id("pass")).sendKeys(Keys.ENTER);
        System.out.println("Successful logged in");
	}
	
	public void createPost() throws InterruptedException {
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\"Crear publicación\"]")));
     
		this.driver.findElement(By.xpath("//span[text()=\"Crear publicación\"]")).click();		
		this.message = "test message " + new Random().nextInt(100);

		this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(this.writePostSelector)));
		this.driver.findElement(By.xpath(this.writePostSelector)).sendKeys(this.message);
		Thread.sleep(1000);
		this.driver.findElement(By.xpath("//div[text()=\"Publicar\"]")).click();  

		//checks that the message posted has been posted correctly
		Thread.sleep(3000);
		String text = this.driver.findElement(By.xpath(this.firstPostTextPath)).getText();  
        Assert.assertEquals(text, this.message);

		System.out.println("Post completed");
	}
	
	public void editPost() throws InterruptedException {
        System.out.println("Post edition started");

        WebElement element = this.driver.findElement(By.cssSelector(this.postActionsSelector));
        if (!element.getAttribute("aria-expanded").equals("true")) {
            element.click();
        }
        
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\"Editar publicación\"]")));
		this.driver.findElement(By.xpath("//span[text()=\"Editar publicación\"]")).click();
		
		this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(this.editPostSelector)));
		this.driver.findElement(By.xpath(this.editPostSelector)).sendKeys(this.message);
		this.driver.findElement(By.xpath("//div[text()=\"Guardar\"]")).click();  
		Thread.sleep(1000);
		
		if (this.driver.findElements(By.xpath(this.notNowSelector)).size() != 0) {
			this.driver.findElement(By.xpath(this.notNowSelector)).click();
		}
		
		//checks that the message edited has been edited correctly
		Thread.sleep(2000);
		String text = this.driver.findElement(By.xpath(this.firstPostTextPath)).getText();  
        Assert.assertEquals(text, this.message + this.message);
        System.out.println("Post edition completed");
	}
	
	public void deletePost() throws InterruptedException {
        System.out.println("Post deletion started");

        WebElement element = this.driver.findElement(By.cssSelector(this.postActionsSelector));
        if (!element.getAttribute("aria-expanded").equals("true")) {
            element.click();
        }
        
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()=\"Eliminar publicación\"]")));
		this.driver.findElement(By.xpath("//span[text()=\"Eliminar publicación\"]")).click();
		this.driver.findElement(By.xpath("//span[text()=\"Eliminar\"]")).click();
        
		//checks that the message deleted has been deleted correctly (get function should get another post different than the deleted one)
		Thread.sleep(2000);
		String text = this.driver.findElement(By.xpath(this.firstPostTextPath)).getText();  
        Assert.assertNotEquals(text, this.message + this.message);
		System.out.println("Post deletion ended");
	}
}