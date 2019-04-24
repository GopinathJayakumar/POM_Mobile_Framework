package MWrappers;


import static io.appium.java_client.touch.offset.PointOption.point;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.aventstack.extentreports.ExtentTest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.StartsActivity;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.appium.java_client.android.connection.HasNetworkConnection;
import io.appium.java_client.android.nativekey.PressesKey;
import io.appium.java_client.touch.WaitOptions;
import utils.Reporter;

public class MocMethods extends Reporter{
	public static AndroidDriver<WebElement> driver;
	public static Properties prop;
	public DesiredCapabilities dc;
	
	public MocMethods() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./config.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadObjects() throws FileNotFoundException, IOException{
		prop = new Properties();
		prop.load(new FileInputStream(new File("./object.properties")));
	}

	public boolean launchApp(String appPackage, String appActivity, String deviceName) {
		try {
			dc = new DesiredCapabilities();
			dc.setCapability("appPackage", appPackage);
			dc.setCapability("appActivity", appActivity);
			dc.setCapability("deviceName", deviceName);
			driver = new AndroidDriver<WebElement>(new URL("http://0.0.0.0:4723/wd/hub"), dc);
			reportStep("The Appication package:" + appPackage + " launched successfully", "PASS");
		} catch (MalformedURLException e) {
			reportStep("The Appication package:" + appPackage + " could not be launched", "FAIL");
		}
		return true;
	}

	public boolean launchActivity(String appPackage, String appActivity) {
		try {			
			Activity activity = new Activity (appPackage, appActivity);
			((StartsActivity) driver).startActivity(activity);
			reportStep("The Appication package:" + appPackage + " launched successfully", "PASS");
		} catch (IllegalArgumentException e) {
			reportStep("The Appication package:" + appPackage + " could not be launched", "FAIL");
		}
		return true;
	}

	public boolean launchBrowser(String browserName, String deviceName, String URL) {
		try {
			dc = new DesiredCapabilities();
			dc.setCapability("browserName", browserName);
			dc.setCapability("deviceName", deviceName);
			
			driver = new AndroidDriver<WebElement>(new URL("http://0.0.0.0:4723/wd/hub"), dc);
			driver.get(URL);		
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			reportStep("The browser: "+browserName+" launched successfully", "PASS");
		} catch (MalformedURLException e) {
			reportStep("The browser: "+browserName+" could not be launch", "FAIL");
		}
		return true;
	}

	public WebElement locateElement(String locator, String locValue) {
		try {
			switch(locator) {
			case("id"): return driver.findElementById(locValue);
			case("linkText"): return driver.findElementByLinkText(locValue);
			case("xpath"):return driver.findElementByXPath(locValue);
			case("name"): return driver.findElementByName(locValue);
			case("className"): return driver.findElementByClassName(locValue);
			case("accessID"):driver.findElementByAccessibilityId(locValue);
			}
		} catch (NoSuchElementException e) {			
			reportStep("The element with locator "+locator+" and with value "+locValue+" not found.", "FAIL");
			throw new RuntimeException();
		} catch (WebDriverException e) {
			reportStep("Unknown exception Occured", "FAIL");
		}
		return null;
	}

	public void type(WebElement ele, String data) {
		try {
			ele.clear();
			ele.sendKeys(data);			
			reportStep("PASS", "The data: "+data+" entered successfully");
		} catch (InvalidElementStateException e) {		
			reportStep("FAIL", "The data: "+data+" could not entered");	
		}
	}


	public boolean click(WebElement ele) {
		try {
			ele.click();
			reportStep("The Element "+ele+" is clicked Successfully", "PASS");
		} catch (Exception e) {
			reportStep("The Element "+ele+" is not click", "PASS");
		}
		return true;
	}


	public boolean verifyAndInstallApp(String appPackage, String appPath) {
		boolean bInstallSuccess = false;
		try {
			if (driver.isAppInstalled(appPackage))
				driver.removeApp(appPackage);
			driver.installApp(appPath);
			bInstallSuccess = true;
			reportStep("The Application:" + appPackage + " installed successfully", "PASS");
		} catch (Exception e) {
			reportStep("The Application:" + appPackage + " could not be installed", "FAIL");
		}
		return bInstallSuccess;
	}

	public void printContext() {
		try {
			Set<String> contexts = driver.getContextHandles();
			for (String string : contexts) {
				System.out.println(string);
			}
		} catch (Exception e) {
			reportStep("The Context could not be found", "FAIL");
		}
	}

	public boolean switchview(){
		try {
			Set<String> contexts = driver.getContextHandles();
			for (String contextName : contexts) {
				if (contextName.contains("NATIVE"))
					driver.context(contextName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The Context could not be switched", "FAIL");
		}
		return true;
	}
	
	public boolean switchWebview(){
		try {		
			Set<String> contextNames = driver.getContextHandles();
			for (String contextName : contextNames) {
				if (contextName.contains("WEBVIEW"))
					driver.context(contextName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The Webview couldnot be found", "FAIL");
		}
		return true;
	}

	public boolean verifyContentDescIsDisplayed(WebElement ele) {	
		boolean bReturn = false;
		try {
			if (ele.isDisplayed())
				reportStep("The element: "+ele+" is displayed.", "PASS");
			bReturn =  true;
		} catch (Exception e) {
			reportStep("The element: "+ele+" is not displayed.", "FAIL");			
		}
		return bReturn;
	}


	@SuppressWarnings("deprecation")
	public void pressEnter() {		
		try {
			((PressesKey) driver).pressKeyCode(66);
			reportStep("Enter button in the keyboard pressed successfully", "PASS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			reportStep("Enter button in the keyboard could not be pressed successfully", "FAIL");
		}
	}

	/*public boolean takeScreenShot(String FileName) {
		try {
			File srcFiler = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcFiler, new File(FileName));
		} catch (WebDriverException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}*/

	public boolean verifyPartialText(WebElement ele, String expectedData) {
		boolean bReturn = false;
		try {
			String name = ele.getText();
			if (name.contains(expectedData)) {
				bReturn = true;
				reportStep("The text: "+name+" matches with the value :"+expectedData, "PASS");
			}else				
				reportStep("The text: "+name+" did not match with the value :"+expectedData, "FAIL");
		} catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title", "FAIL");
		}
		return bReturn;
	}

	//for browser
	public boolean scrollDownInBrowser(int val) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.scrollBy(0," + val + "\")", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	//all
	public boolean backButton() {
		try {
			driver.navigate().back();
			reportStep("The Application screen is moved back", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The Application screen could not be moved back", "PASS");
		}
		return true;
	}

	public boolean scrollUpinApp() {
		try {
			Dimension size = driver.manage().window().getSize();
			int x0 = (int) (size.getWidth() * 0.5);
			int y0 = (int) (size.getHeight() * 0.2);
			int x1 = (int) (size.getWidth() * 0.5);
			int y1 = (int) (size.getHeight() * 0.8);
			MultiTouchAction touch = new MultiTouchAction(driver);
			touch.add(new TouchAction(driver).press(point(x1, y1))
					.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
					.moveTo(point(x0, y0)).release())
			.perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean scrollDowninApp() {
		try {
			Dimension size = driver.manage().window().getSize();
			int x0 = (int) (size.getWidth() * 0.5);
			int y0 = (int) (size.getHeight() * 0.8);
			int x1 = (int) (size.getWidth() * 0.5);
			int y1 = (int) (size.getHeight() * 0.2);
			MultiTouchAction touch = new MultiTouchAction(driver);
			touch.add(new TouchAction(driver).press(point(x1, y1))
					.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2))).moveTo(point(x0, y0)).release())
			.perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	//Tap
	public boolean clickInApp() {
		try {
			Dimension size = driver.manage().window().getSize();
			int x0 = (int) (size.getWidth() * 0.2);
			int y0 = (int) (size.getHeight() * 0.2);
			MultiTouchAction touch = new MultiTouchAction(driver);
			touch.add(new TouchAction(driver).press(point(x0, y0)).release())
			.perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean pullFile(String remotePath) {
		driver.pullFile(remotePath);
		return true;
	}

	public void moveDownInApp() {
		try {
			Dimension size = driver.manage().window().getSize();
			TouchActions touch = new TouchActions(driver);
			System.out.println(size.getWidth() / 2);
			System.out.println((int) (0.8 * (size.getHeight())));
			touch.move(size.getWidth() / 2, (int) (0.8 * (size.getHeight()))).release().perform();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

	//Delete catch cookies nd memoy
	public void resetApp() {
		driver.resetApp();
	}

	public void closeApp() {
		if (driver != null) {
			try {
				driver.closeApp();
			} catch (Exception e) {
			}
		}

	}

	public boolean WiFiOff() {
		((HasNetworkConnection) driver).setConnection(new ConnectionStateBuilder().withAirplaneModeEnabled().build());
		return true;
	}

	public boolean WiFiOn() {
		((HasNetworkConnection) driver).setConnection(new ConnectionStateBuilder().withWiFiEnabled().build());
		return true;
	}

	public boolean setPortraitOrientation() {
		driver.rotate(ScreenOrientation.PORTRAIT);
		return true;
	}

	public boolean setLanscapeOrientation() {
		driver.rotate(ScreenOrientation.LANDSCAPE);
		return true;
	}

	//wifi data
	public String getBatteryInfo() {
		return ((AndroidDriver<WebElement>) driver).getBatteryInfo().getState().toString();
	}

	public void hideKeyboard() {
		driver.hideKeyboard();
	}

	public String getOrientation() {
		return driver.getOrientation().toString();
	}

	
	public long takeScreenShot() {
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L; 
		try {
			File srcFiler = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcFiler , new File("./reports/images/"+number+".jpg"));
		} catch (WebDriverException e) {
			System.out.println("The browser has been closed.");
		} catch (IOException e) {
			System.out.println("The snapshot could not be taken");
		}
		return number;
	}


	public void sleep(int mSec) {
		try {
			Thread.sleep(mSec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}









