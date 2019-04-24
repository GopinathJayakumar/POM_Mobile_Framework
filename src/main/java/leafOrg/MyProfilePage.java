package leafOrg;

import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.ExtentTest;

import MWrappers.LeafOrgWrappers;
import io.appium.java_client.android.AndroidDriver;

public class MyProfilePage extends LeafOrgWrappers{ 

	public MyProfilePage(AndroidDriver<WebElement> driver, ExtentTest test) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		this.test = test;
	}

	public MyProfilePage clickSaveChanges(){
		click(locateElement("xpath",prop.getProperty("MyProfilePage.SaveChanges.Xpath")));		
		return this;
	}
}
