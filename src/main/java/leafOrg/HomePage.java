package leafOrg;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import MWrappers.LeafOrgWrappers;

public class HomePage extends LeafOrgWrappers{
	
	public HomePage() {		
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(how = How.XPATH, using = "//Span[contains(text(),'Settings')]") WebElement Settings;

	public SettingsPage clickSettings(){
		click(Settings);		
		return new SettingsPage(driver, test);
	}


}