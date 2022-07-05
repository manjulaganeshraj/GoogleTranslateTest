package com.test.webPages;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.translate.pageObjects.HomePage;
import com.test.base.TestBase;

import org.json.simple.*;
import org.json.simple.parser.*;

public class GoogleTranslateAssignmentTest extends TestBase {

	@Test
	public void firstTest() throws IOException, InterruptedException, ParseException {

		//Open https://translate.google.com/ website
		HomePage homePage = new HomePage(driver, prop.getProperty("url"));
		homePage.load();
		
		//parse JSON test inputs with UTF encoding
		JSONObject input = parseTestInput();
		String sourceLang = (String)input.get("sourceLang");
		String destinationLang = (String)input.get("destinationLang");
		String sourceText = (String)input.get("sourceText");
		String expectedTranslation = (String)input.get("expectedTranslation");
		
		//select source language from the drop-down menu on the left 
		homePage.clickSourceLangDrpdwn();
		homePage.selectSourceLangDrpdwn(sourceLang);
		
		//select translation language from the drop-down menu on the right 
		homePage.clickTranslateLangDrpdwn();
		homePage.selectTranslateLangDrpdwn(destinationLang);
		
		//Enter text to translate
		homePage.enterInitialText(sourceText);
		//validate
		Boolean translationResult = validateTranslation(homePage, sourceText, expectedTranslation);
		Assert.assertTrue(translationResult, "Couldnot find the expected result after translation.");
		
		//SCENARIO2 - "click swap languages button and verify the result."
		homePage.clickswapLangButton();
		Boolean swapTranslationResult = validateTranslation(homePage, expectedTranslation, sourceText);
		Assert.assertTrue(swapTranslationResult, "Couldnot find the expected result after swap.");
		
		//SCENARIO3 - "clear the input field, click "select input tool" button, select "screen keyboard" and  enter "Hi!" "
		homePage.enterUsingScreenKeyboard();
		
	}
	
	private JSONObject parseTestInput() throws IOException, ParseException {
		//JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        JSONObject textInput = null;
        //with UTF encoding for special alphabets on some languages
        BufferedReader reader = new BufferedReader(new InputStreamReader(
        		new FileInputStream("./src/test/resources/testInput.json"), "UTF-8"));
        
        //Read JSON file
        Object obj = jsonParser.parse(reader);
        textInput = (JSONObject) obj;
         
		return textInput;
	}
	
	private Boolean validateTranslation(HomePage homePage, String inputText, String expectedText) throws InterruptedException{
		
		WebElement translatedOutputElement = homePage.getTranslatedOutputElement();
		String resultText = translatedOutputElement.getText();
		
		//Validate with primary display text
		if(expectedText.equalsIgnoreCase(resultText)) {
			return true;
		}
		
		//If expected is not in primary display text, try alternate values as per Google Translate by clicking result
		translatedOutputElement.click();
		
		wait.until(ExpectedConditions.visibilityOfAllElements(homePage.getTranslatedAlternateValues()));
		
		for (WebElement element : homePage.getTranslatedAlternateValues()){
			
			WebElement sourceInResult = element.findElement(By.className("swFHJd"));
			WebElement translatedTextInResult = element.findElement(By.className("NqnNQd"));
			
			if(sourceInResult.getText().equalsIgnoreCase(inputText)) {
				if(translatedTextInResult.getText().equalsIgnoreCase(expectedText)) {
					translatedTextInResult.click();
					
					return true;
				}
			}
		}
		
		translatedOutputElement.click();
		return false;
	}
}
