import com.microsoft.playwright.*;

public class ScratchTest {
    public static void main(String[] args) {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        Page page = browser.newPage();
        
        page.navigate("https://casekaro.com/");
        page.waitForLoadState();
        
        Locator searchIcon = page.locator("summary.header__search, summary[aria-label='Search'], .header__icon--search").first();
        System.out.println("Search Icon found: " + searchIcon.count());
        if (searchIcon.count() > 0) {
            searchIcon.click();
        }
        
        Locator searchInput = page.locator("input[type='search'], input[name='q']").first();
        System.out.println("Search Input found: " + searchInput.count());
        searchInput.fill("Apple");
        searchInput.press("Enter");
        
        page.waitForLoadState();
        System.out.println("URL after search: " + page.url());
        
        browser.close();
        playwright.close();
    }
}
