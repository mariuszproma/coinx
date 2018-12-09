package pl.forexat.nbpscraper.operations;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import pl.forexat.nbpscraper.AccountConfiguration;
import pl.forexat.nbpscraper.model.Coin;

public class Purchase {
	private static final String CART_SUMMARY_URL = "https://kolekcjoner.nbp.pl/pl/checkout/onepage/";
	private static final Logger log = Logger.getLogger(Purchase.class);
	private static final String ADDTOCARD_BUTTON = "product-addtocart-button";
	private AccountConfiguration config;
	private WebDriver driver;
	private WebDriverWait waitDriver;
	private Coin coin;

	public void setConfig(AccountConfiguration config) {
		this.config = config;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
		this.waitDriver = new WebDriverWait(driver, 10L, 300L);
	}

	public void setCoin(Coin coin) {
		this.coin = coin;
	}

//	public Object doPurchase() throws Exception {
//		if (this.coin.isCoinPageSet()) {
//			boolean coinBuyingResult = false;
//			synchronized (this.driver) {
//				coinBuyingResult = buyCoin();
//			}
//			return Boolean.valueOf(coinBuyingResult);
//		}
//		Thread.sleep(5000L);
//		return Boolean.valueOf(true);
//	}

	public boolean buyCoin() {
//		if (this.config.getPurchaseTab() == null) {
//			return false;
//		}
//		this.driver.switchTo().window(this.config.getPurchaseTab());
			
			try{
				this.driver.get(this.coin.getCoinPage());
				//if(isCartEmpty())
					putCoinIntoCart();
			} catch(StaleElementReferenceException e){
			} catch (InterruptedException e) {
			}
			//checkCart();
			try{
			log.info("Will try to log in");
			logIn();
			} catch (WebDriverException e){
				
			}
			
		try {
			//fillOrder();
		} catch (NoSuchElementException exception) {
			log.info("Couldn't find element on page: " + exception.getMessage());
			return false;
		}
		return true;
	}
	
	private void logIn() {
		this.driver.get("https://kolekcjoner.nbp.pl/pl/checkout/onepage/");
		log.info("checkout");
		List<WebElement> helloElements = this.driver.findElements(By.className("register"));
		if (helloElements.isEmpty()) {
			log.info("hello element is empty");
			//this.soundPlayer.alarm();
			this.driver.findElement(By.id("login-email")).sendKeys(new CharSequence[] { this.config.getUsername() });
			this.driver.findElement(By.id("login-password")).sendKeys(new CharSequence[] { this.config.getPassword() });
			log.info("Waiting for password to be typed");
		}
	}
	
	private boolean isCartEmpty() {
		String cartSummary = this.driver.findElement(By.className("cart-summary")).getText();
		log.info("Cart summary: " + cartSummary);
		return cartSummary.toLowerCase().startsWith("pusty");
	}
	
	private WebElement retrieveBuyButton() {
		try{
			WebElement element = this.driver.findElement(By.id("product-addtocart-button"));
			return element;
		} catch (NoSuchElementException e){
			return null;
		}
	}
	
	private void increaseAmount() {
		List<WebElement> buttons = this.driver.findElements(By.className("quantity-change-button"));
		ExpectedCondition<WebElement> shippingButton = ExpectedConditions
				.elementToBeClickable((WebElement) buttons.get(1));
		waitAndClick(shippingButton);
	}

	private void putCoinIntoCart() throws InterruptedException {
			//Thread.sleep(300);
			//this.driver.findElement(By.id("quantity-input")).sendKeys(new CharSequence[] {QUANTITY});
			//this.driver.findElements(By.className("quantity-change-button")).get(1).click();;
			//click(element);
			//Thread.sleep(300);
				log.info("Sprawdzam dostepnosc");
				WebElement element = retrieveBuyButton();
				if(element==null) {
					return;
				}
				if(coin.getCoinLimits()>1) {
					increaseAmount();
				}
				click(element);
			//driver.close();
			//his.driver.get(this.coin.getCoinPage());
//			Thread.sleep(300);
//			click(By.id("product-addtocart-button"));
			//this.driver.findElement(By.className("add-to-basket-button")).click();;
			//this.driver.findElement(By.id("product-addtocart-button")).click();
			log.info("Coin added to cart");
	}
	
	private void checkCart() {
		this.driver.get(CART_SUMMARY_URL);
	}
	
	private void click(By condition){
		WebElement element = this.driver.findElement(condition);
		click(element);
	}
	
	private void click(WebElement webElement){
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click()", webElement);
	}

	private void fillOrder() {
		this.driver.get("https://kolekcjoner.nbp.pl/checkout/onepage/index/");
		Select billing = new Select(this.driver.findElement(By.id("billing:ssn")));
		billing.selectByValue("Receipt");
		List<WebElement> buttons = this.driver.findElements(By.className("button"));
		((WebElement) buttons.get(0)).click();
		log.info("Clicked the button for the first time ");

		ExpectedCondition<WebElement> shippingButton = ExpectedConditions
				.elementToBeClickable((WebElement) buttons.get(2));
		waitAndClick(shippingButton);

		ExpectedCondition<WebElement> przelewRadioButton = ExpectedConditions
				.elementToBeClickable(By.id("p_method_checkmo"));
		waitAndClick(przelewRadioButton);

		ExpectedCondition<WebElement> paymentButton = ExpectedConditions
				.elementToBeClickable((WebElement) buttons.get(3));
		waitAndClick(paymentButton);

		WebElement potwierdzenie = (WebElement) this.waitDriver
				.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.button.btn-checkout")));
		log.info("Potwierdzenie will be clicked: " + potwierdzenie.getTagName());
		potwierdzenie.click();
	}

	private void waitAndClick(ExpectedCondition<WebElement> buttonIsReady) {
		log.info("Waiting for button to be ready");
		WebElement element = (WebElement) this.waitDriver.until(buttonIsReady);
		element.click();
		log.info("Clicked " + element.getAttribute("name") + " button");
	}
}
