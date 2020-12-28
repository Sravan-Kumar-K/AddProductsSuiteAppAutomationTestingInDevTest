Feature: Testing Add Products Suite App

Scenario: Test scenario to verify the alert when we do not select the vendor
Given User is in Purchase order page
Then verify the alert

Scenario: Test scenario to verify the items added in the item sublist
Given User is in Purchase order page
Then Select Vendor "Tax Agency CA" & Add the line items using Add Products with the excel data at "C:\Users\Sravan Kumar\Desktop\Add Products Data2.xlsx,Item addition" & Verify the items in the item sublist

Scenario: Test scenario to verify the item filters
Given User is in Purchase order page
Then Verify the items in the Click to Add list based on filters using excel data at "C:\Users\Sravan Kumar\Desktop\Add Products Data2.xlsx,Filters Testing"

Scenario: Test scenario to verify the Search functionality
Given User is in Purchase order page
Then Verify the search results in the Click to Add list using excel data at "C:\Users\Sravan Kumar\Desktop\Add Products Data2.xlsx,Search Testing"

Scenario: Test scenario to verify the pop up when we add non-matrix item with matrix item exist in the Current selection list and vice versa
Given User is in Purchase order page
Then Verify the popup after item addition using excel data at "C:\Users\Sravan Kumar\Desktop\Add Products Data2.xlsx,Item Addition validations"