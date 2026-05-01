# CaseKaro QA Automation – Playwright + Cucumber + Java

## Assignment Overview
Automated test suite for [casekaro.com](https://casekaro.com) covering:
1. Navigate to website
2. Open **Mobile Covers** category
3. Search for **Apple** → negative validation (no other brands visible)
4. Select **iPhone 16 Pro** model
5. Click **Choose Options** on first item
6. Add all 3 material variants (**Hard**, **Soft**, **Glass**) to cart
7. Open cart → assert all 3 items present
8. Print each item's **Material**, **Price**, and **Link** to console

---

## Tech Stack
| Tool       | Version  |
|------------|----------|
| Java       | 17+      |
| Maven      | 3.8+     |
| Playwright | 1.44.0   |
| Cucumber   | 7.18.0   |
| JUnit      | 5.10.2   |

---

## Project Structure

```
casekaro-qa/
├── pom.xml
└── src/
    └── test/
        ├── java/
        │   ├── pages/
        │   │   ├── PlaywrightManager.java   ← Browser lifecycle
        │   │   └── CaseKaroPage.java        ← Page Object Model
        │   ├── stepdefs/
        │   │   └── CaseKaroSteps.java       ← Cucumber Step Definitions
        │   └── runners/
        │       └── TestRunner.java          ← JUnit 5 Suite runner
        └── resources/
            └── features/
                └── CaseKaro.feature         ← Cucumber BDD feature file
```

---

## Prerequisites

1. **Java 17+** – `java -version`
2. **Maven 3.8+** – `mvn -version`
3. Internet access to reach casekaro.com

---

## Setup & Run

### 1. Install Playwright browsers (one-time)
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium"
```

### 2. Run all tests
```bash
mvn test
```

### 3. View HTML report
Open `target/cucumber-reports/report.html` in your browser.

---

## Key Design Decisions

| Requirement              | Implementation                                              |
|--------------------------|-------------------------------------------------------------|
| No try-catch             | Playwright throws on failures; Cucumber catches them cleanly|
| Assertions               | `PlaywrightAssertions.assertThat(...)` from Playwright API  |
| Cucumber feature file    | `src/test/resources/features/CaseKaro.feature`              |
| Console print            | `System.out.printf` in `CaseKaroPage.printCartItemDetails()`|
| Page Object Model        | `CaseKaroPage.java` encapsulates all locators & actions     |

---

## Sample Console Output

```
========== CART ITEM DETAILS ==========
--- Item 1 ---
  Material / Title : iPhone 16 Pro Hard Case
  Price            : ₹299
  Link             : https://casekaro.com/products/iphone-16-pro-hard-case
--- Item 2 ---
  Material / Title : iPhone 16 Pro Soft Case
  Price            : ₹249
  Link             : https://casekaro.com/products/iphone-16-pro-soft-case
--- Item 3 ---
  Material / Title : iPhone 16 Pro Glass Case
  Price            : ₹349
  Link             : https://casekaro.com/products/iphone-16-pro-glass-case
========================================
```
