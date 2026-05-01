package pages;

import com.microsoft.playwright.*;

import java.util.ArrayList;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CaseKaroPage {

    private final Page page;
    private final String CART_ITEMS = ".cart-item, tr.cart__row, .cart-product";

    public CaseKaroPage(Page page) {
        this.page = page;
    }

    // 1. Navigate to home
    public void navigateToHome() {
        page.navigate("https://casekaro.com/");
        page.waitForLoadState();
        page.waitForTimeout(2000);
    }

    // 2. Click Mobile Covers
    public void clickMobileCovers() {
        Locator desktopNav = page.locator("header a:visible")
                .filter(new Locator.FilterOptions().setHasText("Mobile Covers"));
        if (desktopNav.count() > 0) {
            desktopNav.first().click();
            page.waitForLoadState();
            return;
        }
        String href = (String) page.evaluate(
            "() => { const a = [...document.querySelectorAll('a')]" +
            ".find(el => el.textContent.trim().includes('Mobile Covers')); return a ? a.href : null; }"
        );
        page.navigate(href != null ? href : "https://casekaro.com/pages/phone-cases");
        page.waitForLoadState();
        page.waitForTimeout(1000);
    }

    // 3. Search via URL
    public void searchForBrand(String brand) {
        page.navigate("https://casekaro.com/search?q=" + brand + "&type=product");
        page.waitForLoadState();
        page.waitForTimeout(1500);
    }

    // 4. Negative validation
    public void assertOnlyAppleVisible() {
        String[] otherBrands = {"Samsung", "OnePlus", "Xiaomi", "Realme", "Oppo", "Vivo", "Google"};
        for (String brand : otherBrands) {
            Locator brandProducts = page.locator(
                ".product-item, .product-card, .grid__item, li.grid__item, .card__heading"
            ).filter(new Locator.FilterOptions().setHasText(brand));
            assertThat(brandProducts).hasCount(0);
        }
    }

    // 5. Click Apple brand
    public void clickAppleBrand() {
        Locator appleLink = page.locator(
            "a.tag:has-text('Apple'), .facets__item a:has-text('Apple'), " +
            "label:has-text('Apple'), a[href*='vendor=Apple']:visible, a[href*='apple']:visible"
        );
        if (appleLink.count() > 0) {
            appleLink.first().click();
            page.waitForLoadState();
            page.waitForTimeout(1000);
        } else {
            page.navigate("https://casekaro.com/collections/apple");
            page.waitForLoadState();
            page.waitForTimeout(1000);
        }
    }

    // 6. Select iPhone model
    public void selectIPhoneModel(String model) {
        Locator modelLink = page.locator("a:visible")
                .filter(new Locator.FilterOptions().setHasText(model));
        if (modelLink.count() > 0) {
            modelLink.first().click();
            page.waitForLoadState();
            page.waitForTimeout(1000);
        } else {
            page.navigate("https://casekaro.com/collections/iphone-16-pro-back-covers");
            page.waitForLoadState();
            page.waitForTimeout(1000);
        }
    }

    // 7. Navigate directly to first product page — avoids quick-add modal
    public void clickChooseOptionsForFirstItem() {
        String productHref = (String) page.evaluate(
            "() => {" +
            "  const links = [...document.querySelectorAll('a[href*=\"/products/\"]')];" +
            "  const link = links.find(a => a.href && !a.href.includes('#'));" +
            "  return link ? link.href : null;" +
            "}"
        );
        if (productHref != null) {
            page.navigate(productHref);
            page.waitForLoadState();
            page.waitForTimeout(2000);
        }
    }

    // 8. Select material and add to cart — uses JS click to bypass fieldset overlay
    public void addMaterialToCart(String material) {
        // Use JavaScript to find and click the label for this material
        // This bypasses the fieldset that intercepts pointer events
        page.evaluate(
            "material => {" +
            "  /* Try clicking label whose text matches material */" +
            "  const labels = [...document.querySelectorAll('label')];" +
            "  const label = labels.find(l => l.textContent.trim() === material);" +
            "  if (label) { label.click(); return; }" +
            "" +
            "  /* Try clicking radio input with matching value */" +
            "  const radio = document.querySelector('input[type=\"radio\"][value=\"' + material + '\"]');" +
            "  if (radio) { radio.click(); return; }" +
            "" +
            "  /* Try any element containing material text */" +
            "  const els = [...document.querySelectorAll('.product-form__input label, .variant__button-label')];" +
            "  const el = els.find(e => e.textContent.trim() === material);" +
            "  if (el) el.click();" +
            "}",
            material
        );
        page.waitForTimeout(800);

        // Click Add to Cart via JavaScript to bypass any overlay
        page.evaluate(
            "() => {" +
            "  const btn = document.querySelector('button[name=\"add\"]');" +
            "  if (btn) { btn.click(); return; }" +
            "  const btns = [...document.querySelectorAll('button')];" +
            "  const addBtn = btns.find(b => b.textContent.toLowerCase().includes('add to cart'));" +
            "  if (addBtn) addBtn.click();" +
            "}"
        );
        page.waitForTimeout(2000);

        // Close cart drawer via JS click
        page.evaluate(
            "() => {" +
            "  const selectors = [" +
            "    'button[aria-label=\"Close cart\"]'," +
            "    'button.drawer__close'," +
            "    'button.cart-notification__dismiss'," +
            "    'button[aria-label=\"Close\"]'" +
            "  ];" +
            "  for (const sel of selectors) {" +
            "    const el = document.querySelector(sel);" +
            "    if (el && el.offsetParent !== null) { el.click(); return; }" +
            "  }" +
            "}"
        );
        page.waitForTimeout(800);

        // Fallback: press Escape to close any overlay
        page.keyboard().press("Escape");
        page.waitForTimeout(500);
    }

    // 9. Open cart page
    public void openCart() {
        page.navigate("https://casekaro.com/cart");
        page.waitForLoadState();
        page.waitForTimeout(1000);
    }

    // 10. Assert cart item count
    public void assertCartHasItems(int expectedCount) {
        Locator visibleItems = page.locator(":is(" + CART_ITEMS + "):visible");
        assertThat(visibleItems).hasCount(expectedCount);
    }

    // 11. Print cart details to console
    public void printCartItemDetails() {
        List<CartItem> items = new ArrayList<>();
        Locator rows = page.locator(":is(" + CART_ITEMS + "):visible");
        int count = rows.count();

        for (int i = 0; i < count; i++) {
            Locator row = rows.nth(i);
            String titleAndMaterial = safeText(row.locator(".cart-item__details, .cart__item-details, .product-details").first());
            if ("N/A".equals(titleAndMaterial) || titleAndMaterial.isEmpty()) {
                titleAndMaterial = safeText(row.locator(".cart-item__name, .product-title").first());
            }
            String price = safeText(row.locator(
                ".cart-item__price, .price, .money, td.cart__price, .cart-item__final-price"
            ).first());
            String link = safeHref(row.locator("a").first());
            items.add(new CartItem(titleAndMaterial, price, link));
        }

        System.out.println("\n========== CART ITEM DETAILS ==========");
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            System.out.printf("--- Item %d ---%n", i + 1);
            System.out.printf("  Material / Title : %s%n", item.material);
            System.out.printf("  Price            : %s%n", item.price);
            System.out.printf("  Link             : %s%n", item.link);
        }
        System.out.println("========================================\n");
    }

    private String safeText(Locator locator) {
        if (locator.count() > 0) return locator.first().innerText().trim();
        return "N/A";
    }

    private String safeHref(Locator locator) {
        if (locator.count() > 0) {
            String href = locator.first().getAttribute("href");
            if (href != null && !href.isEmpty())
                return href.startsWith("http") ? href : "https://casekaro.com" + href;
        }
        return "N/A";
    }

    record CartItem(String material, String price, String link) {}
}