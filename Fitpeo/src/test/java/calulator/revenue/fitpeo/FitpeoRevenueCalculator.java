package calulator.revenue.fitpeo;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class FitpeoRevenueCalculator {
	private WebDriver driver;
	private JavascriptExecutor js;
	private Actions actions;
	SoftAssert softassert = new SoftAssert();

	@BeforeClass
	public void setUp() {
		// SetUp the ChromeDriver executable

		driver = new ChromeDriver();
		js = (JavascriptExecutor) driver;
		actions = new Actions(driver);
		driver.manage().window().maximize();

	}

	@Test
	public void testRevenueCalculator() {
		try {
			// Step 1: Navigate to the FitPeo Homepage
			driver.get("https://fitpeo.com");

			// Step 2: Navigate to the Revenue Calculator Page
			WebElement revenueCalculatorLink = driver
					.findElement(By.xpath("//div[contains(text(),'Revenue Calculator')]"));
			revenueCalculatorLink.click();

			// Using explicit wait for synchronization
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); 
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
					"(//span[@class='MuiSlider-root MuiSlider-colorPrimary MuiSlider-sizeMedium css-duk49p'])[1]")));

			
			  // Step 3: Scroll Down to the Slider section 
			  WebElement sliderSection = driver .findElement(By.xpath("(//span[@class='MuiSlider-root MuiSlider-colorPrimary MuiSlider-sizeMedium css-duk49p'])[1]")); 
			  js.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", sliderSection);  // Using javascript executor to perform browser actions
			 // js.executeScript("window.scrollBy(0, -180);");
			  	  
			  // Step 4: Adjust the Slider to 820 
			  WebElement slider = driver.findElement(By.xpath("(//h4[normalize-space()='Medicare Eligible Patients']/../div//span)[4]/input")); 
			  actions.clickAndHold(slider).moveByOffset(94, 0).release().perform(); // Using actions class to perform advanced interactions with webelements
			  WebElement slidertxtboxvalue = driver.findElement(By.xpath("//input[@type='number']")); 
			  String Sliderboxvalue = slidertxtboxvalue.getAttribute("value");
			  softassert.assertEquals(Sliderboxvalue, "822",
			  "Slider value is not set to 822.");
			  
			 		  
			  // Step 5: Update the Text Field to 560 
			  WebElement textField = driver.findElement(By.xpath("//input[@type='number']"));
			  js.executeScript("arguments[0].value='';", textField);
			  actions.click(textField)  // Using actions class to perform some keyboard actions
			  .keyDown(Keys.CONTROL) 
			  .sendKeys("a")
			  .keyUp(Keys.CONTROL) 
			  .sendKeys(Keys.DELETE) 
			  .perform();
			  textField.sendKeys("560"); 
			  WebDriverWait wait1 = new WebDriverWait(driver,Duration.ofSeconds(10));
			  wait1.until(ExpectedConditions.domAttributeToBe(slider, "aria-valuenow","560"));
			  softassert.assertEquals(slider.getAttribute("aria-valuenow"), "560","Slider value did not update to 560.");
			  
			  
			 
			// Step 6: Select CPT Codes

			String[] CPTcodesToSelect = { "CPT-99091", "CPT-99453", "CPT-99454", "CPT-99474" };

			for (String CPTcode : CPTcodesToSelect) {
				
				// Locate elements that contain the specific text
				
				List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), '" + CPTcode + "')]"));

				for (WebElement element : elements) {
					js.executeScript("arguments[0].scrollIntoView();", element);
					WebElement checkbox = element
							.findElement(By.xpath("//*[contains(text(), '" + CPTcode + "')]/../label/span/input"));
					if (!checkbox.isSelected()) {
						checkbox.click();
					}
				}

			}

			// Step 7: Validate Total Recurring Reimbursement
			WebElement reimbursementHeader = driver.findElement(By.xpath("//p[contains(text(),'Total Recurring Reimbursement for all Patients Per')]/following-sibling::p"));
			String expectedReimbursement = "$75600";
		//	js.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", reimbursementHeader);
			//js.executeScript("window.scrollBy(0, -180);");
			softassert.assertEquals(reimbursementHeader.getText(), expectedReimbursement, "Reimbursement value is incorrect.");
		} catch (Exception e) {
			e.printStackTrace();
			softassert.fail("Test encountered an exception: " + e.getMessage());
		}
		softassert.assertAll();
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
