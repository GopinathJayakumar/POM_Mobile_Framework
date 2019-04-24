package leafOrg;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import MWrappers.LeafOrgWrappers;

public class LoginPage extends LeafOrgWrappers{

	public LoginPage() {		
		PageFactory.initElements(driver, this);
	}
		
	@FindBy(how = How.XPATH, using = "//input[@formcontrolname='email']") WebElement eleEmail;
	@FindBy(how = How.XPATH, using = "//input[@formcontrolname='password']") WebElement elePassword;
	@FindBy(how = How.XPATH, using = "//span[contains(text(),'Login')]") WebElement eleLogin;
	

	public LoginPage launchLeafOrgApp(String deviceName){
		launchApp("com.testleaf.leaforg", "com.testleaf.leaforg.MainActivity", deviceName);	
		switchview();
		return this;
	}


	public LoginPage enterUserName(String email){
		type(eleEmail,email);
		return this;
	}

	public LoginPage enterPassword(String password){
		type(elePassword, password);
		return this;
	}

	public HomePage clickLogin(){
		click(eleLogin);
		return new HomePage();
	}

	public  HomePage loginToLeafOrg(String deviceName,String email,String password){
		launchApp("com.testleaf.leaforg", "com.testleaf.leaforg.MainActivity", deviceName);	
		switchWebview();
		type(eleEmail,email);
		type(elePassword, password);
		click(eleLogin);
		return new HomePage();
	}




}
