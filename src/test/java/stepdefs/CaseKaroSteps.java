package stepdefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import pages.CaseKaroPage;
import pages.PlaywrightManager;

public class CaseKaroSteps {

    private CaseKaroPage caseKaroPage;

    @Before
    public void setUp() {
        PlaywrightManager.initBrowser();
        caseKaroPage = new CaseKaroPage(PlaywrightManager.getPage());
    }

    @After
    public void tearDown() {
        PlaywrightManager.closeBrowser();
    }

    // ---------------------------------------------------------------
    //  Background
    // ---------------------------------------------------------------
    @Given("I navigate to the CaseKaro website")
    public void i_navigate_to_the_casekaro_website() {
        caseKaroPage.navigateToHome();
    }

    // ---------------------------------------------------------------
    //  Navigation to Mobile Covers
    // ---------------------------------------------------------------
    @When("I click on {string} category")
    public void i_click_on_category(String category) {
        caseKaroPage.clickMobileCovers();
    }

    // ---------------------------------------------------------------
    //  Search
    // ---------------------------------------------------------------
    @When("I search for {string} using the search button")
    public void i_search_for_using_the_search_button(String brand) {
        caseKaroPage.searchForBrand(brand);
    }

    // ---------------------------------------------------------------
    //  Negative validation – other brands must NOT appear
    // ---------------------------------------------------------------
    @Then("only Apple brand products should be visible and no other brands")
    public void only_apple_brand_products_should_be_visible_and_no_other_brands() {
        caseKaroPage.assertOnlyAppleVisible();
    }

    // ---------------------------------------------------------------
    //  Brand & model selection
    // ---------------------------------------------------------------
    @When("I click on {string} brand")
    public void i_click_on_brand(String brand) {
        caseKaroPage.clickAppleBrand();
    }

    @When("I select model {string}")
    public void i_select_model(String model) {
        caseKaroPage.selectIPhoneModel(model);
    }

    // ---------------------------------------------------------------
    //  Choose Options
    // ---------------------------------------------------------------
    @When("I click {string} for the first item")
    public void i_click_for_the_first_item(String buttonText) {
        caseKaroPage.clickChooseOptionsForFirstItem();
    }

    // ---------------------------------------------------------------
    //  Add materials to cart
    // ---------------------------------------------------------------
    @When("I add the {string} material case to cart")
    public void i_add_the_material_case_to_cart(String material) {
        caseKaroPage.addMaterialToCart(material);
    }

    // ---------------------------------------------------------------
    //  Cart validation
    // ---------------------------------------------------------------
    @When("I open the cart")
    public void i_open_the_cart() {
        caseKaroPage.openCart();
    }

    @Then("all 3 material variants should be present in the cart")
    public void all_3_material_variants_should_be_present_in_the_cart() {
        caseKaroPage.assertCartHasItems(3);
    }

    // ---------------------------------------------------------------
    //  Console print
    // ---------------------------------------------------------------
    @Then("I print the details of all cart items to console")
    public void i_print_the_details_of_all_cart_items_to_console() {
        caseKaroPage.printCartItemDetails();
    }
}
