package pl.forexat.nbpscraper.operations;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import pl.forexat.nbpscraper.AccountConfiguration;
import pl.forexat.nbpscraper.sound.SoundPlayer;

public class Authorization {
	private static final Logger log = Logger.getLogger(Authorization.class);
	private static final String LOGIN_PAGE_URL = "https://kolekcjoner.nbp.pl/customer/account/login";
	private static final String SIMPLE_PAGE = "https://google.com";
	private AccountConfiguration config;
	private WebDriver driver;
	private long timeInterval;
	private Date lastRun;
	private SoundPlayer soundPlayer;

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public void setConfig(AccountConfiguration config) {
		this.config = config;
	}

	// wymagane
	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	public void setSoundPlayer(SoundPlayer soundPlayer) {
		this.soundPlayer = soundPlayer;
	}

	public Object doAuthorization() throws Exception {
		if (filterFrequentRequests()) {
			synchronized (this.driver) {
				log.info(this.config);
				switchToAuthorizationTab();
				log.info("switched to Authorization tab");
				logIn();
				log.info("Finished creating login page");
			}
			this.lastRun = new Date();
		}
		return null;
	}

	private boolean filterFrequentRequests() {
		Date currentTime = new Date();
		if ((this.lastRun == null) || (this.lastRun.getTime() + this.timeInterval < currentTime.getTime())) {
			return true;
		}
		return false;
	}

	private void logIn() {
		this.driver.get("https://kolekcjoner.nbp.pl/customer/account/login");
		List<WebElement> helloElements = this.driver.findElements(By.className("hello"));
		if (helloElements.isEmpty()) {
			this.soundPlayer.alarm();
			this.driver.findElement(By.id("email")).sendKeys(new CharSequence[] { this.config.getUsername() });
			this.driver.findElement(By.id("pass")).sendKeys(new CharSequence[] { this.config.getPassword() });
			log.info("Waiting for password to be typed");
		}
	}

	private void switchToAuthorizationTab() {
		Set<String> windowHandles = this.driver.getWindowHandles();
		if (windowHandles.size() == 1) {
			String originalWindowHandle = this.driver.getWindowHandle();
			log.info("Original window handle: " + originalWindowHandle);
			log.info("Number of open tabs: " + windowHandles.size());
			openNewTab();
			switchToSecondTab();
		}
		this.driver.switchTo().window(this.config.getAuthorizationTab());
	}

	private String switchToSecondTab() {
		String originalWindowHandle = this.driver.getWindowHandle();
		this.config.setPurchaseTab(originalWindowHandle);
		Set<String> windowHandles = this.driver.getWindowHandles();
		for (String windowHandle : windowHandles) {
			if (!windowHandle.equals(originalWindowHandle)) {
				log.info("Switching to window handle: " + windowHandle);
				this.config.setAuthorizationTab(windowHandle);
				break;
			}
		}
		return originalWindowHandle;
	}

	private void openNewTab() {
		log.info("Opening new tab");
		this.driver.get("https://google.com");
		this.driver.findElement(By.cssSelector("body")).sendKeys(new CharSequence[] { Keys.CONTROL + "t" });
		log.info("New tab opened");
	}
}
