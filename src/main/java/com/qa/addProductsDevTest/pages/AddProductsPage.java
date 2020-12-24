package com.qa.addProductsDevTest.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.ExtentTest;
import com.qa.addProductsDevTest.util.TestBase;

public class AddProductsPage extends TestBase{
	
	@FindBy(xpath = "//select[@id='category']")
	WebElement classDropdown;
	
	@FindBy(xpath = "//select[@id='category']//option")
	List<WebElement> classOptions;
	
	@FindBy(xpath = "//select[@id='sub_category']")
	WebElement departmentDropdown;
	
	@FindBy(xpath = "//select[@id='sub_category']//option")
	List<WebElement> departmentOptions;
	
	@FindBy(xpath = "//select[@id='sub_category1']")
	WebElement locationDropdown;
	
	@FindBy(xpath = "//select[@id='sub_category1']//option")
	List<WebElement> locationOptions;
	
	@FindBy(xpath = "//div[@id='display1']//table//tr//td//a")
	List<WebElement> clickToAddList;
	
	@FindBy(xpath = "//input[contains(@id,'qty')]")
	WebElement quantityBox;
	
	@FindBy(xpath = "//input[@value='Done']")
	WebElement doneBtn;
	
	@FindBy(xpath = "//input[@id='searchbox']")
	WebElement searchBox;
	
	@FindBy(xpath = "//input[@value='Search']")
	WebElement searchBtn;
	
	@FindBy(xpath = "//input[@id='matrixSearchbox']")
	WebElement matrixCheckBox;
	
	@FindBy(xpath = "//table[@id='selectedItemsTable']//tr//td//input")
	List<WebElement> matrixQuantityBox;
	
	public AddProductsPage() {
		PageFactory.initElements(driver, this);
	}
	
	public void selectItemsInAddProductsPage(String classFilter, String department, String location, String item, String quantity, String matrixFlag) throws InterruptedException {
		String poPageWindow = driver.getWindowHandle();
		Set<String> allWindows = driver.getWindowHandles();
		for(String child: allWindows) {
			if(!poPageWindow.equals(child)) {
				driver.switchTo().window(child);
			}
		}
		
		// Select class
		if(!classFilter.isEmpty()) {
			eleClickable(driver, By.xpath("//select[@id='category']"), 9);
			classDropdown.sendKeys(classFilter);
		}
		
		// Select Department
		if(!department.isEmpty()) {
			eleClickable(driver, By.xpath("//select[@id='sub_category']"), 9);
			departmentDropdown.sendKeys(department);
		}
		
		// Select location
		if(!location.isEmpty()) {
			eleClickable(driver, By.xpath("//select[@id='sub_category1']"), 9);
			locationDropdown.sendKeys(location);
		}
		
		// Check or uncheck matrix item checkbox
		if(matrixFlag.equals("Yes") && !matrixCheckBox.isSelected())
			matrixCheckBox.click();
		else if(matrixFlag.equals("No") && matrixCheckBox.isSelected())
			matrixCheckBox.click();
		
		// Click on Search button
		searchBtn.click();
		
		if(item.contains(",")) {
			String[] items = item.split(",");
			for (int i = 0; i < items.length; i++) {
				// Select Items in the click to add list
				for(int j=0;j<clickToAddList.size();j++) {
					String parentCustomer = clickToAddList.get(j).getText().trim();
					if(parentCustomer.equals(items[i])) {
						clickToAddList.get(j).click();
						break;
					}
				}
			}
		}else {
			// Select Item in the click to add list
			for(int j=0;j<clickToAddList.size();j++) {
				String parentCustomer = clickToAddList.get(j).getText().trim();
				if(parentCustomer.equals(item)) {
					clickToAddList.get(j).click();
					break;
				}
			}
		}
		
		
		
		// Select quantity
		eleAvailability(driver, By.xpath("//input[contains(@id,'qty')]"), 10);
		if(matrixFlag.equals("Yes")) {
			String[] quantities = quantity.split(",");
			if(quantities.length == matrixQuantityBox.size()) {
				for(int i=0;i<matrixQuantityBox.size();i++) {
					if(!quantities[i].equals("null"))
						matrixQuantityBox.get(i).sendKeys(quantities[i]);
				}
												
				// Click Done
				action.moveToElement(doneBtn).build().perform();
				doneBtn.click();
				driver.switchTo().window(poPageWindow);
				
				while(isAlertPresent()) {
					Thread.sleep(1000);
					Alert alert = driver.switchTo().alert();
					String alertMsg = alert.getText();
					System.out.println("Alert occured: "+alertMsg);
					alert.accept();
				}
			}
			else {
				System.out.println("Quantity count is wrong in the excel");
				driver.close();
				driver.switchTo().window(poPageWindow);
			}
		}
		else if(matrixFlag.equals("No")) {
			
			if(quantity.contains(",")) {
				String[] quantities = quantity.split(",");
				for(int i=0;i<matrixQuantityBox.size();i++) {
					if(!quantities[i].equals("null"))
						matrixQuantityBox.get(i).sendKeys(quantities[i]);
				}
			}
			else
				quantityBox.sendKeys(quantity);
			
			// Click Done
			doneBtn.click();
			
			Thread.sleep(2000);
			
			// Switch to PO Window
			driver.switchTo().window(poPageWindow);
		}
		
		
	}
	
	public void verifyItemAdditionValidations(String classFilter, String department, String location, String matrixFlag, String item, ExtentTest logInfo) throws InterruptedException {
		// Select class
		if(!classFilter.isEmpty()) {
			eleClickable(driver, By.xpath("//select[@id='category']"), 9);
			classDropdown.click();
			for(int j=0;j<classOptions.size();j++) {
				String text = classOptions.get(j).getText().trim();
				if(text.equals(classFilter)) {
					classOptions.get(j).click();
					break;
				}
			}
		}else {
			classDropdown.sendKeys("Select A Value");
		}
		
		// Select department
		if(!department.isEmpty()) {
			eleClickable(driver, By.xpath("//select[@id='sub_category']"), 9);
			departmentDropdown.click();
			for(int j=0;j<departmentOptions.size();j++) {
				String text = departmentOptions.get(j).getText().trim();
				if(text.equals(department)) {
					departmentOptions.get(j).click();
					break;
				}
			}
		}else {
			departmentDropdown.sendKeys("Select A Value");
		}
		
		// Select location
		if(!location.isEmpty()) {
			eleClickable(driver, By.xpath("//select[@id='sub_category1']"), 9);
			locationDropdown.click();
			for(int j=0;j<locationOptions.size();j++) {
				String text = locationOptions.get(j).getText().trim();
				if(text.equals(location)) {
					locationOptions.get(j).click();
					break;
				}
			}
		}else {
			locationDropdown.sendKeys("Select A Value");
		}
		
		// Check or uncheck matrix item checkbox
		if(matrixFlag.equals("Yes") && !matrixCheckBox.isSelected())
			matrixCheckBox.click();
		else if(matrixFlag.equals("No") && matrixCheckBox.isSelected())
			matrixCheckBox.click();
		
		// Click on Search button
		searchBtn.click();
		
		// Select Items in the click to add list
		for(int j=0;j<clickToAddList.size();j++) {
			String parentCustomer = clickToAddList.get(j).getText().trim();
			if(parentCustomer.equals(item)) {
				clickToAddList.get(j).click();
				break;
			}
		}
		
		if(isAlertPresent()) {
			Thread.sleep(2000);
			Alert alert = driver.switchTo().alert();
			String alertMsg = alert.getText();
			System.out.println("Alert occured: "+alertMsg);
			logInfo.pass("Alert occured: "+alertMsg);
			alert.accept();
		}
		
	}
	
	public String switchToApPage() {
		String poPageWindow = driver.getWindowHandle();
		Set<String> allWindows = driver.getWindowHandles();
		for(String child: allWindows) {
			if(!poPageWindow.equals(child)) {
				driver.switchTo().window(child);
			}
		}
		return poPageWindow;
	}
	
	public void verifySearchResults(String itemSearch, ExtentTest logInfo) {
		itemSearch = itemSearch.replaceAll("\\.0*$", "");
		String poPageWindow = driver.getWindowHandle();
		Set<String> allWindows = driver.getWindowHandles();
		boolean flag = true;
		for(String child: allWindows) {
			if(!poPageWindow.equals(child)) {
				driver.switchTo().window(child);
				searchBox.sendKeys(itemSearch);
				searchBtn.click();
				
				for(int j=0;j<clickToAddList.size();j++) {
					String text = clickToAddList.get(j).getText().trim();
					if(!text.contains(itemSearch))
						flag = false;
				}
				
				if(flag) {
					System.out.println("Item results in the Click to Add list are displayed correctly");
					logInfo.pass("Item results in the Click to Add list are displayed correctly");
				}
				else {
					System.out.println("Item results in the Click to Add list are not displayed correctly");
					logInfo.fail("Item results in the Click to Add list are not displayed correctly");
				}
			}
		}
		driver.close();
		
		// Switch to PO Window
		driver.switchTo().window(poPageWindow);
	}
	
	public List<String> verifyClickToAddList(String classFilter, String department, String location) throws InterruptedException {
		List<String> itemsAfterFilter = new ArrayList<>();
		String poPageWindow = driver.getWindowHandle();
		Set<String> allWindows = driver.getWindowHandles();
		for(String child: allWindows) {
			if(!poPageWindow.equals(child)) {
				driver.switchTo().window(child);
				
				// Select class
				if(!classFilter.isEmpty()) {
					eleClickable(driver, By.xpath("//select[@id='category']"), 9);
					classDropdown.sendKeys(classFilter);
				}
				
				// Select department
				if(!department.isEmpty()) {
					eleClickable(driver, By.xpath("//select[@id='sub_category']"), 9);
					departmentDropdown.sendKeys(department);
				}
				
				// Select location
				if(!location.isEmpty()) {
					eleClickable(driver, By.xpath("//select[@id='sub_category1']"), 9);
					locationDropdown.sendKeys(location);
				}
				
				for(int j=0;j<clickToAddList.size();j++) {
					String text = clickToAddList.get(j).getText().trim();
					itemsAfterFilter.add(text);
				}
			}
		}
		driver.close();
		// Switch to PO Window
		driver.switchTo().window(poPageWindow);
		return itemsAfterFilter;
	}
	
}
