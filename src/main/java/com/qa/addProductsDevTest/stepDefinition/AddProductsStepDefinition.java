package com.qa.addProductsDevTest.stepDefinition;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.aventstack.extentreports.ExtentTest;
import com.qa.addProductsDevTest.pages.AddProductsPage;
import com.qa.addProductsDevTest.pages.AuthenticationPage;
import com.qa.addProductsDevTest.pages.HomePage;
import com.qa.addProductsDevTest.pages.LoginPage;
import com.qa.addProductsDevTest.pages.POCreationPage;
import com.qa.addProductsDevTest.util.ExcelDataToDataTable;
import com.qa.addProductsDevTest.util.TestBase;

import cucumber.api.DataTable;
import cucumber.api.Transform;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;

public class AddProductsStepDefinition extends TestBase{
	
	LoginPage loginPage;
	AuthenticationPage authPage;
	HomePage homePage;
	POCreationPage poCreationPage;
	AddProductsPage addProductsPage;
	cucumber.api.Scenario scenario;
	
	@Before
	public void login(cucumber.api.Scenario scenario){
	    this.scenario = scenario;
		ExtentTest loginfo = null;
		try {
			test = extent.createTest(scenario.getName());
			loginfo = test.createNode("Login Test");
			
			TestBase.init();
			loginPage = new LoginPage();
			authPage = loginPage.login();
			homePage = authPage.Authentication();
			homePage.changeRole(prop.getProperty("roleText"), prop.getProperty("roleType"));
			Thread.sleep(1000);
			
			loginfo.pass("Login Successful in Netsuite");
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}
	
	@After
	public void closeBrowser() {
		driver.close();
	}

	@Then("^User is in Purchase order page$")
	public void navigate_to_Purchase_order_page() throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("User is in Purchase order page");
			poCreationPage = homePage.clickOnNewPOLink();
			loginfo.pass("Navigated to Purchase order page");
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}
	
	@Then("^verify the alert$")
	public void verify_the_alert() throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verify the alert message when we do not select the vendor");
			Thread.sleep(6000);
			poCreationPage.verifyAlert(loginfo);
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}
	
	@Then("^Select Vendor \"([^\"]*)\" & Add the line items using Add Products with the excel data at \"([^\"]*)\" & Verify the items in the item sublist$")
	public void select_Vendor_Add_the_line_items_using_Add_Products_with_the_excel_data_at_Verify_the_items_in_the_item_sublist(String vendor, @Transform(ExcelDataToDataTable.class) DataTable addProductsData) throws InterruptedException {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verification of Items added from the Add Products Page");
//			String a[][] = ConvertDataTableToArray(addProductsData);
//			loginfo.pass(MarkupHelper.createTable(a));
			poCreationPage.selectVendor(vendor);
			int itemsCount=0;
			int previousItemCount = 0;
			for(Map<String,String> data: addProductsData.asMaps(String.class, String.class)) {
				String classFilter = data.get("Class");
				String department = data.get("Department");
				String location = data.get("Location");
				String item = data.get("Item");
				String quantity = data.get("Quantity");
				String matrixFlag = data.get("Matrix flag");
				if(matrixFlag.equals("No")) {
					if(item.contains(",")) {
						StringTokenizer st = new StringTokenizer(item, ",");
						int tokensCount = st.countTokens();
						previousItemCount = itemsCount;
						itemsCount = itemsCount + tokensCount;
					}
					else {
						previousItemCount = itemsCount;
						itemsCount++;
					}
				}
				addProductsPage = poCreationPage.clickOnAddProductsBtn();
				addProductsPage.selectItemsInAddProductsPage(classFilter, department, location, item, quantity, matrixFlag);
				poCreationPage.verifyItems(item, quantity, matrixFlag, itemsCount, previousItemCount, loginfo);
			}
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}

	@Then("^Verify the items in the Click to Add list based on filters using excel data at \"([^\"]*)\"$")
	public void verify_the_items_in_the_Click_to_Add_list_based_on_filters_using_excel_data_at(@Transform(ExcelDataToDataTable.class) DataTable addProductsData) throws Throwable {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verification of items in the Click to add list after applying the filters");
			
			for(Map<String,String> data: addProductsData.asMaps(String.class, String.class)) {
				List<String> itemsAfterFilterFromApPage;
				List<String> itemsAfterFilterFromSearch;
				String vendor = data.get("Vendor");
				String classFilter = data.get("Class");
				String department = data.get("Department");
				String location = data.get("Location");
				poCreationPage.selectVendor(vendor);
				addProductsPage = poCreationPage.clickOnAddProductsBtn();
				itemsAfterFilterFromApPage = addProductsPage.verifyClickToAddList(classFilter, department, location); 
				itemsAfterFilterFromSearch = poCreationPage.getItemsWithFilterInNS(classFilter, department, location);
				poCreationPage.compareItemResultsAfterFilter(classFilter, department, location, itemsAfterFilterFromApPage, itemsAfterFilterFromSearch, loginfo);
			}
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}

	}
	
	@Then("^Verify the search results in the Click to Add list using excel data at \"([^\"]*)\"$")
	public void verify_the_search_results_in_the_Click_to_Add_list_using_excel_data_at(@Transform(ExcelDataToDataTable.class) DataTable addProductsData) throws Throwable {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verification of search results in the Click to add list");

//			List<List<String>> list1 = addProductsData.asLists(String.class);
//			String[] array1;
//			String[][] array2 = null;
//			int k=0;
//			for(List<String> l: list1) {
//				array1 = l.toArray(new String[0]);
//				array2[k] = array1;
//				k++;
//			}
//			Markup m = MarkupHelper.createTable(array2);
//			loginfo.pass(m);
			
			for(Map<String,String> data: addProductsData.asMaps(String.class, String.class)) {
				String vendor = data.get("Vendor");
				String itemSearch = data.get("Item Search");
				poCreationPage.selectVendor(vendor);
				addProductsPage = poCreationPage.clickOnAddProductsBtn();
				addProductsPage.verifySearchResults(itemSearch, loginfo);
			}
			
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}
	
	@Then("^Verify the popup after item addition using excel data at \"([^\"]*)\"$")
	public void verify_the_popup_after_item_addition_using_excel_data_at(@Transform(ExcelDataToDataTable.class) DataTable addProductsData) throws Throwable {
		ExtentTest loginfo = null;
		try {
			loginfo = test.createNode("Verification of pop-up when we add matrix item with  non-matrix item exist in the Current Selection list and vice versa");

			poCreationPage.selectVendor("Tax Agency CA");
			addProductsPage = poCreationPage.clickOnAddProductsBtn();
			String poHandle = addProductsPage.switchToApPage();
			for(Map<String,String> data: addProductsData.asMaps(String.class, String.class)) {
				String classFilter = data.get("Class");
				String department = data.get("Department");
				String location = data.get("Location");
				String matrixFlag = data.get("Matrix flag");
				String item = data.get("Item name");
				addProductsPage.verifyItemAdditionValidations(classFilter, department, location, matrixFlag, item, loginfo);
			}
			driver.close();
			driver.switchTo().window(poHandle);
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL", driver, loginfo, e, scenario.getName());
		}
	}
}
