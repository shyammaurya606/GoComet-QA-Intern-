# Feature: CaseKaro Mobile Cover Shopping
# Website: https://casekaro.com/

Feature: Purchase iPhone 16 Pro Mobile Covers from CaseKaro

  Background:
    Given I navigate to the CaseKaro website

  Scenario: Add all 3 material variants of an iPhone 16 Pro case to cart
    When I click on "Mobile Covers" category
    And I search for "Apple" using the search button
    Then only Apple brand products should be visible and no other brands
    When I click on "Apple" brand
    And I select model "iPhone 16 Pro"
    And I click "Choose Options" for the first item
    And I add the "Hard" material case to cart
    And I add the "Soft" material case to cart
    And I add the "Glass" material case to cart
    And I open the cart
    Then all 3 material variants should be present in the cart
    And I print the details of all cart items to console
