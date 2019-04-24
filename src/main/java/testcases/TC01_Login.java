package testcases;

import java.io.IOException;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import MWrappers.LeafOrgWrappers;
import leafOrg.LoginPage;

public class TC01_Login extends LeafOrgWrappers{

	@BeforeTest
	public void setValues(){
		dataSheetName 	= "TC001";
		testCaseName 	= "Login to LeafOrg App";
		testDescription = "Login to LeafOrg app (using POM framework)";
		testNodes = "Nodes";
		category = "smoke";
		authors = "Gopi";
	}

	@Test(dataProvider="fetchData")
	public void login(String deviceName,String email,String Password) throws IOException {
		new LoginPage().
		loginToLeafOrg(deviceName, email, Password)
		.clickSettings()
		.clickLogout()
		.clickYesLogout();

	}


}
