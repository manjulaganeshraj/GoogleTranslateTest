package com.google.translate.driver;


import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;


/**
 * DriverFactory class to create WebDriver instance to run automated tests against desktop browsers, device browser and android app.
 **/
public class DriverFactory {
	protected WebDriver driver;
	
	/* Properties to trigger Chrome browser*/
	protected void chromeDriver() {
		System.setProperty("webdriver.chrome.driver", "./src/main/resources/chromedriver/chromedriver.exe");
		
		ChromeOptions opt = new ChromeOptions();
	      
	      opt.addArguments("--lang=en_US");
	       //configure options parameter to Chrome driver
	      driver = new ChromeDriver(opt);
	      
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}

	/* Properties to trigger Firefox browser*/
	protected void firefoxDriver() {
		System.setProperty("webdriver.gecko.driver", "./src/main/resources/geckodriver/geckodriver.exe");
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
	}

	
	 
	protected WebDriver invokeBrowser(String browser) throws MalformedURLException {
		if (browser.equalsIgnoreCase("firefox")) {
			firefoxDriver();
			return driver;
		}

		else if (browser.equalsIgnoreCase("chrome")) {
			chromeDriver();
			return driver;
		}

		
		return driver;
	}

	/**
	 * This method closes the browser
	 **/
	protected void closeBrowser() throws IOException {
		driver.quit();
	}
	
	
}
