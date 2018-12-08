package pl.forexat.nbpscraper.driver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory
{
  public static WebDriver getSingleDriver()
    throws MalformedURLException
  {
    WebDriver singleDriver = new RemoteWebDriver(new URL("http://localhost:9515"),new ChromeOptions());
    return singleDriver;
  }
  
  public static WebDriver getGridDriver(String browserName)
    throws MalformedURLException
  {
    RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), new ChromeOptions());
    driver.manage().timeouts().setScriptTimeout(5L, TimeUnit.SECONDS);
    return driver;
  }
}
