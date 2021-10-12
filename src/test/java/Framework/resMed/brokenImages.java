package Framework.resMed;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

public class brokenImages implements ITestListener{
	WebDriver driver;
	@Test
	public void findBrokenImages() throws Exception
	{
		
		String strReportPath = System.getProperty("user.dir")+"/Reports/ExecutionReport.html";
		ExtentSparkReporter sparkReport = new ExtentSparkReporter(strReportPath);
		ExtentReports extent = new ExtentReports();

		extent.attachReporter(sparkReport);
		sparkReport.config().setTheme(Theme.DARK);
		sparkReport.config().setReportName("Release 1.01");
		sparkReport.config().setDocumentTitle("ResMed Run results");
		ExtentTest test = extent.createTest("BrokenImages").assignAuthor("Tina").assignDevice("Windows - Chrome");

		test.log(Status.INFO, "Test Case BrokenImages created");
		test.pass("PASS");	
		
		String strBrowser = "Chrome";
		String strURL = "https://www.resmed.com.au/";
		Thread.sleep(3000);
		if (strBrowser.equalsIgnoreCase("Chrome"))
		{
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
							
		}
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get(strURL);
				
		for (WebElement all_images : driver.findElements(By.cssSelector("img")))
		{
		   isImageBroken(all_images);
			
		}	
		test.log(Status.PASS, "Broken Image test case passed");
		File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String screenshotpath = System.getProperty("user.dir")+"/Reports/Screenshots/scr1.jpeg";
		File destination = new File(screenshotpath);
		try {
			FileUtils.copyFile(src, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
		test.addScreenCaptureFromPath(screenshotpath, "Test case screenshot");
		extent.flush();
		driver.quit();
	}
	
	public void isImageBroken(WebElement image)
	{
		if (image.getAttribute("naturalWidth").equals("0"))
	    {
	        System.out.println("Image :"+image.getAttribute("outerHTML") + " is broken.");
	    }
	}
	
	
}
