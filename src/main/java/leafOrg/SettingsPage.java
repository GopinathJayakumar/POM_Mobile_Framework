package leafOrg;

import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.ExtentTest;

import MWrappers.LeafOrgWrappers;
import io.appium.java_client.android.AndroidDriver;

public class SettingsPage  extends LeafOrgWrappers{ 

	public SettingsPage(AndroidDriver<WebElement> driver, ExtentTest test) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		this.test = test;
	}

	public MyProfilePage clickMyProfile(){
		click(locateElement("xpath",prop.getProperty("Settings.MyProfile.Xpath")));		
		return new MyProfilePage(driver, test);
	}

	public SettingsPage clickLogout(){
		click(locateElement("xpath",prop.getProperty("Settings.Logout.Xpath")));		
		return this;
	}

	public LoginPage clickYesLogout(){
		click(locateElement("xpath",prop.getProperty("Settings.YesLogout.Xpath")));		
		return new LoginPage();
	}


}
