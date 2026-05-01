package pages;

import com.microsoft.playwright.*;

public class PlaywrightManager {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    public static void initBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1400, 900));
        page = context.newPage();
    }

    public static Page getPage() {
        return page;
    }

    public static void closeBrowser() {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
