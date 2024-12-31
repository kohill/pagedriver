package com.healthedge.test.pagedriver;

import com.healthedge.common.Waiter;
import com.healthedge.config.props.PropertyReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;

public class PageDriver {

    private static final Logger LOG = LogManager.getLogger(PageDriver.class);
    private static final long pollingInterval = 400;
    private static final long pauseDelay = 500;
    private static final long explicitWaitTimeout = PropertyReader.getProperty("test.wait.timeout", 0L);
    public final String timer = "N";
    private final WebDriver webDriver;
    private final Actions actions;
    private final Waiter waiter;
    private int inputDelay = 0;

    /**
     * Constructor with Session
     */
    public PageDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.actions = new Actions(webDriver);
        this.waiter = new Waiter(webDriver);
    }

    public WebDriver getWebDriver() {
        return this.webDriver;
    }

    /**
     * @return the actions
     */
    public Actions getActions() {
        return this.actions;
    }

    /**
     * Returns waiter instance
     **/
    public Waiter getWaiter() {
        return waiter;
    }

    /**
     * Opens the specified URL in the web browser.
     *
     * @param url the URL to open
     */
    public void open(String url) {
        webDriver.get(url);
    }

    /**
     * Checks if an alert is present in the web browser.
     *
     * @return true if an alert is present, false otherwise
     */
    public boolean isAlertPresent() {
        try {
            webDriver.switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the title of the current web page.
     *
     * @return the title of the current web page
     */
    public String getBrowserTitle() {
        return webDriver.getTitle();
    }

    /**
     * Gets the current URL of the web browser.
     *
     * @return the current URL of the web browser
     */
    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }

    /**
     * Opens the specified URL in the web browser.
     *
     * @param URL the URL to open
     */
    public void openURL(String URL) {
        webDriver.navigate().to(URL);
    }

    /**
     * Gets the HTML source code of the current web page.
     *
     * @return the HTML source code of the current web page
     */
    public String getHtmlSource() {
        return webDriver.getPageSource();
    }

    /**
     * Checks if the specified text exists in the HTML source code of the current web page.
     *
     * @param text the text to search for
     * @return true if the text exists, false otherwise
     */
    public boolean textExists(String text) {
        return webDriver.getPageSource().contains(text);
    }

    /**
     * Refreshes the current web page.
     */
    public void refreshBrowser() {
        webDriver.navigate().refresh();
    }

    /**
     * Executes the specified JavaScript code in the web browser.
     *
     * @param javaScript the JavaScript code to execute
     */
    public void executeJavaScript(String javaScript) {
        ((JavascriptExecutor) webDriver)
                .executeScript(javaScript);
    }

    /**
     * Executes the specified JavaScript code in the web browser with the provided argument.
     *
     * @param javaScript the JavaScript code to execute
     * @param s          the argument to pass to the JavaScript code
     */
    public void executeJavaScript(String javaScript, Object s) {
        ((JavascriptExecutor) webDriver)
                .executeScript(javaScript, s);
    }

    /**
     * Navigates to the previous page in the web browser's history.
     */
    public void goBackInBrowser() {
        webDriver.navigate().back();
    }

    /**
     * Switches the web browser's focus to the default content (i.e., the main page).
     */
    public void switchToDefaultFrame() {
        waiter.sleep(500);
        this.webDriver.switchTo().defaultContent();
        waiter.sleep(500);
    }

    /**
     * Switches the web browser's focus to the specified frame by index.
     *
     * @param index the index of the frame to switch to
     */
    public void switchToFrame(int index) {
        waiter.sleep(500);
        this.webDriver.switchTo().frame(index);
        waiter.sleep(500);
    }

    /**
     * Switches the web browser's focus to the specified frame by locator.
     *
     * @param frameLocator the locator of the frame to switch to
     */
    public void switchToFrame(By frameLocator) {
        waiter.sleep(500);
        WebElement frameElement = this.webDriver.findElement(frameLocator);
        this.webDriver.switchTo().frame(frameElement);
        waiter.sleep(500);
    }

    /**
     * Switches the web browser's focus to the specified frame by ID.
     *
     * @param iframeId the ID of the frame to switch to
     */
    public void switchToFrame(String iframeId) {
        waiter.sleep(500);
        this.webDriver.switchTo().frame(iframeId);
        waiter.sleep(500);
    }

    /**
     * Switches the web browser's focus to the specified window.
     *
     * @param window the window handle to switch to
     */
    public void switchToWindow(String window) {
        waiter.sleep(500);
        this.webDriver.switchTo().window(window);
        waiter.sleep(500);
    }

    /**
     * Finds a list of WebElements on the page that match the given locator.
     *
     * @param objId The locator used to find the elements (e.g. By.id(), By.cssSelector(), etc.)
     * @return A list of WebElements that match the given locator
     */
    public List<WebElement> getElements(By objId) {
        return this.webDriver.findElements(objId);
    }

    /**
     * Finds a list of child WebElements within a parent WebElement on the page.
     *
     * @param by1 The locator for the parent element
     * @param by2 The locator for the child elements within the parent
     * @return A list of child WebElements that match the given locators
     */
    public List<WebElement> getElements(By by1, By by2) {
        return this.webDriver.findElement(by1).findElements(by2);
    }

    /**
     * Finds the first WebElement on the page that matches the given locator.
     *
     * @param by The locator used to find the element (e.g. By.id(), By.cssSelector(), etc.)
     * @return The first WebElement that matches the given locator
     */
    public WebElement findElement(By by) {
        WebElement element;
        return element = webDriver.findElement(by);
    }

    /**
     * Finds a child WebElement within a parent WebElement on the page.
     *
     * @param by1 The locator for the parent element
     * @param by2 The locator for the child element within the parent
     * @return The child WebElement that matches the given locators
     */
    public WebElement findingWebElements(By by1, By by2) {
        WebElement element;
        return element = webDriver.findElement(by1).findElement(by2);
    }

    /****************************************************************
     *
     * Logging methods
     *
     */

    private void logMessage(Exception e) {
        LOG.trace(e.getMessage());
    }

    private String getStackTraceInfo(String element) {
        String pageDriverInfo = "";
        String pageInfo = "";
        Exception e = new Exception(element);
        for (StackTraceElement ste : e.getStackTrace()) {
            if (ste.getClassName().contains(PageDriver.class.getSimpleName())) {
                pageDriverInfo = ste.getMethodName();
            }
        }
        pageInfo = e.getStackTrace()[4].getClassName() +
                "." + e.getStackTrace()[4].getMethodName() +
                "|" + e.getStackTrace()[3].getMethodName() +
                "|" + pageDriverInfo;
        return pageInfo;
    }

    private void entered(String element, String elementValue) {
        logMessage(new Exception("Entering|" + getStackTraceInfo(element) + "|" + element + "|" + elementValue));
    }

    private void leaving(String element) {
        logMessage(new Exception("Leaving|" + getStackTraceInfo(element) + "|" + element));
    }

    /**
     * Pauses the execution for the specified number of milliseconds.
     *
     * @param milliSeconds The number of milliseconds to pause.
     */
    public void pause(long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            logMessage(new InterruptedException("Unavailable|Pause" +
                    e.getLocalizedMessage()));
        }
    }

    /**
     * Returns the current input delay value.
     *
     * @return The input delay value in milliseconds.
     */
    public int getInputDelay() {
        return inputDelay;
    }

    /**
     * Sets the input delay value.
     *
     * @param inputDelay The new input delay value in milliseconds.
     */
    public void setInputDelay(int inputDelay) {
        this.inputDelay = inputDelay;
    }

    /**
     * Selects an option from a dropdown list by its value.
     *
     * @param by          The locator strategy to find the dropdown list.
     * @param optionValue The value of the option to be selected.
     */
    public void selectOptionBy(By by, String optionValue) {
        entered(by.toString(), optionValue);
        if (waitForElementAvailable(by)) {
            try {
                Select select = new Select(webDriver.findElement(by));
                select.selectByValue(optionValue);
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" + optionValue +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
    }

    /**
     * Selects an option from a dropdown list by its visible text.
     *
     * @param by         The locator strategy to find the dropdown list.
     * @param optionText The visible text of the option to be selected.
     */
    public void selectOptionTextBy(By by, String optionText) {
        entered(by.toString(), optionText);
        if (waitForElementAvailable(by)) {
            try {
                Select select = new Select(webDriver.findElement(by));
                select.selectByVisibleText(optionText);
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" + optionText +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
    }

    /**
     * Checks if a dropdown list contains an option with the specified visible text.
     *
     * @param by         The locator strategy to find the dropdown list.
     * @param optionText The visible text of the option to check for.
     * @return True if the option with the specified text is present, false otherwise.
     */
    public boolean selectContainsOptionWithText(By by, String optionText) {
        entered(by.toString(), optionText);
        if (waitForElementAvailable(by)) {
            try {
                Select select = new Select(webDriver.findElement(by));
                List<WebElement> options = select.getOptions();
                for (WebElement option : options) {
                    if (option.getText().trim().equals(optionText)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" + optionText +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
        return false;
    }

    /**
     * Selects first entry(other than 'Select one') in a drop-down list control.
     *
     * @param by The drop-down list identified by Name
     */
    public void selectFirstItemFromListBy(By by) {
        entered(by.toString(), "<First Item>");
        if (waitForElementAvailable(by)) {
            Select select = null;
            List<WebElement> options;
            String selectValue = null;
            int i = 1;
            try {
                select = new Select(webDriver.findElement(by));
                options = select.getOptions();
                selectValue = options.get(i).getText();
                select.selectByVisibleText(selectValue);
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
    }

    /**
     * Selects an option from a dropdown by its index.
     *
     * @param by    The locator strategy to find the dropdown element.
     * @param index The index of the option to select (zero-based).
     */
    public void selectIndexBy(By by, String index) {
        entered(by.toString(), String.valueOf(index));
        if (waitForElementAvailable(by)) {
            try {
                WebElement webElement = webDriver.findElement(by);
                Select select = new Select(webElement);
                select.selectByIndex(Integer.parseInt(index));
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
    }

    /**
     * Selects an option from a dropdown by partially matching its text.
     *
     * @param by         The locator strategy to find the dropdown element.
     * @param optionText The partial text of the option to select.
     */
    public void selectOptionPartialTextBy(By by, String optionText) {
        entered(by.toString(), optionText);
        this.waitForElementAvailable(by);
        this.waitForElementEnabled(by);
        WebElement webElement = this.webDriver.findElement(by);
        List<WebElement> allOptions = webElement.findElements(By.tagName("option"));
        Iterator var5 = allOptions.iterator();
        while (var5.hasNext()) {
            WebElement option = (WebElement) var5.next();
            String text = option.getAttribute("text");
            if (!text.isBlank() && (text.contains(optionText) || optionText.contains(text))) {
                option.click();
                break;
            }
        }
        leaving(by.toString());
    }

    /**
     * Deselects all options from a dropdown.
     *
     * @param by The locator strategy to find the dropdown element.
     */
    public void deselectDropDownValue(By by) {
        waiter.waitForAjaxToFinish();
        waitUntilClickable(by);
        WebElement element = elementLocator(by);
        Instant start = Instant.now();
        long START_TIME = Instant.now().toEpochMilli();
        Select select = new Select(element);
        waiter.waitForAjaxToFinish();
        select.deselectAll();
        waiter.waitForAjaxToFinish();
        getPageLoadTimer(by, START_TIME);
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
    }

    /**
     * Gets the value of the currently selected option in a dropdown.
     *
     * @param by The locator strategy to find the dropdown element.
     * @return The value of the selected option, or an empty string if none is selected.
     */
    public String getSelectedValueBy(By by) {
        String value = "";
        entered(by.toString(), "value");
        if (waitForElementAvailable(by)) {
            try {
                WebElement webElement = webDriver.findElement(by);
                Select select = new Select(webElement);
                value = select.getFirstSelectedOption().getAttribute("value");
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
        return value;
    }

    /**
     * Gets the index of the currently selected option in a dropdown.
     *
     * @param by The locator strategy to find the dropdown element.
     * @return The index of the selected option, or an empty string if none is selected.
     */
    public String getSelectedIndexBy(By by) {
        String value = "";
        entered(by.toString(), "index");
        if (waitForElementAvailable(by)) {
            try {
                WebElement webElement = webDriver.findElement(by);
                Select select = new Select(webElement);
                value = select.getFirstSelectedOption().getAttribute("index");
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
        return value;
    }

    /**
     * Gets the text of the currently selected option in a dropdown.
     *
     * @param by The locator strategy to find the dropdown element.
     * @return The text of the selected option, or an empty string if none is selected.
     */
    public String getSelectedTextBy(By by) {
        String value = "";
        entered(by.toString(), "");
        if (waitForElementAvailable(by)) {
            try {
                WebElement webElement = webDriver.findElement(by);
                Select select = new Select(webElement);
                value = select.getFirstSelectedOption().getText();
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
        return value;
    }

    /**
     * Gets a list of all available values in a dropdown.
     *
     * @param by The locator strategy to find the dropdown element.
     * @return A list of option values, or null if the dropdown is not available.
     */
    public List<String> getSelectValueListBy(By by) {
        List<String> optionsText = null;
        entered(by.toString(), "value");
        if (waitForElementAvailable(by)) {
            try {
                WebElement webElement = webDriver.findElement(by);
                Select select = new Select(webElement);
                List<WebElement> optionList = select.getOptions();
                optionsText = new ArrayList<String>();
                for (int i = 0; i < optionList.size(); i++) {
                    optionsText.add(optionList.get(i).getAttribute("value"));
                }
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
        return optionsText;
    }

    /**
     * Selects an option from a dropdown list by its index.
     *
     * @param by    The locator strategy to find the dropdown list element.
     * @param index The index of the option to select (zero-based).
     * @return The value of the selected option.
     */
    public String selectOptionByIndex(By by, int index) {
        String value = "";
        entered(by.toString(), String.valueOf(index));
        if (waitForElementAvailable(by)) {
            try {
                // index in DOM is one greater than the way things are indexed in java
                index = index + 1;
                WebElement selectField = webDriver.findElement(by);
                WebElement option = selectField.findElement(By.xpath("//*[@id='select-option-" + index + "']"));
                value = option.getAttribute("value");
                option.click();
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
        return value;
    }

    /**
     * Retrieves the text content of a node within another node.
     *
     * @param node The locator strategy to find the parent node.
     * @param by   The locator strategy to find the child node within the parent node.
     * @return The text content of the child node.
     */
    public String getTextInNode(By node, By by) {
        String value = "";
        entered(by.toString(), "");
        if (waitForElementAvailable(by)) {
            try {
                value = webDriver.findElement(node).findElement(by).getText();
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString() + value);
        return value;
    }

    /**
     * Retrieves the text content of an element.
     *
     * @param by The locator strategy to find the element.
     * @return The text content of the element.
     */
    public String getTextBy(By by) {
        String value = "";
        entered(by.toString(), "");
        if (waitForElementAvailable(by)) {
            try {
                value = webDriver.findElement(by).getText();
            } catch (java.util.NoSuchElementException e) {
                logMessage(new NoSuchElementException("Unavailable|" + by.toString() +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString() + value);
        return value;
    }

    /**
     * Retrieves the text content of a pop-up alert.
     *
     * @return The text content of the pop-up alert.
     */
    public String getTextFromPopUp() {
        return webDriver.switchTo().alert().getText();
    }

    /**
     * Clears the value of a field.
     *
     * @param by The locator strategy to find the field element.
     */
    public void clearValueInFieldBy(By by) {
        entered(by.toString(), "");
        if (waitForElementAvailable(by)) {
            try {
                webDriver.findElement(by).clear();
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
    }

    /**
     * Enters a value into a field using JavaScript.
     *
     * @param by         The locator strategy to find the field element.
     * @param inputValue The value to enter into the field.
     */
    public void enterValueInFieldByJS(By by, String inputValue) {
        entered(by.toString(), inputValue);
        if (waitForElementAvailable(by)) {
            try {
                WebElement webElement = webDriver.findElement(by);
                webElement.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                JavascriptExecutor executor = (JavascriptExecutor) webDriver;
                executor.executeScript("arguments[0].value='" + inputValue + "'", webElement);
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" + inputValue +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
    }

    /**
     * Enters a value into a field using JavaScript and optionally sends a tab key.
     *
     * @param by          The locator strategy to find the field element.
     * @param inputValue  The value to enter into the field.
     * @param sendTabFlag A flag indicating whether to send a tab key after entering the value.
     */
    public void enterValueInFieldByJS(By by, String inputValue, boolean sendTabFlag) {
        waiter.waitForAjaxToFinish();
        entered(by.toString(), inputValue);
        if (waitForElementAvailable(by)) {
            try {
                WebElement webElement = webDriver.findElement(by);
                webElement.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                JavascriptExecutor executor = (JavascriptExecutor) webDriver;
                executor.executeScript("arguments[0].value='" + inputValue + "'", webElement);
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" + inputValue +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
        webDriver.findElement(by).sendKeys(Keys.TAB);
        waiter.waitForAjaxToFinish();
    }

    /**
     * Enters a value into a field.
     *
     * @param by         The locator strategy to find the field element.
     * @param inputValue The value to enter into the field.
     */
    public void enterValueInFieldBy(By by, String inputValue) {
        entered(by.toString(), inputValue);
        if (waitForElementAvailable(by)) {
            try {
                WebElement webElement = webDriver.findElement(by);
                webElement.clear();
                webElement.sendKeys(inputValue);
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" + inputValue +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
    }

    /**
     * Enters a value into a field and optionally sends a tab key.
     *
     * @param by          The locator strategy to find the field element.
     * @param inputValue  The value to enter into the field.
     * @param sendTabFlag A flag indicating whether to send a tab key after entering the value.
     */
    public void enterValueInFieldBy(By by, String inputValue, boolean sendTabFlag) {
        waiter.waitForAjaxToFinish();
        entered(by.toString(), inputValue);
        if (waitForElementAvailable(by)) {
            try {
                WebElement webElement = webDriver.findElement(by);
                webElement.clear();
                webElement.sendKeys(inputValue);
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" + inputValue +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
        webDriver.findElement(by).sendKeys(Keys.TAB);
        waiter.waitForAjaxToFinish();
    }

    /**
     * Enters the given text into the specified field, first selecting all existing text in the field.
     *
     * @param by   The locator used to find the field element
     * @param text The text to enter into the field
     */
    public void enterValueInFieldSelectAllText(By by, String text) {
        entered(by.toString(), text);
        if (waitForElementAvailable(by)) {
            try {
                WebElement element = webDriver.findElement(by);
                element.click();
                element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                element.sendKeys(text);
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" + text +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
    }

    /**
     * Enters the given value into the field identified by the provided locator, one character at a time with a delay between each character.
     *
     * @param by    the locator strategy to identify the field
     * @param value the value to be entered into the field
     */
    public void enterValueInFieldSlowly(By by, String value) {
        entered(by.toString(), value);
        if (waitForElementAvailable(by)) {
            try {
                WebElement field = webDriver.findElement(by);
                field.click();
                field = webDriver.findElement(by);
                field.clear();
                for (int i = 0; i < value.length(); i++) {
                    String character = value.substring(i, i + 1);
                    field.sendKeys(character);
                    pause(pauseDelay);
                }
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" + value +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
    }

    /**
     * Checks or unchecks the checkbox identified by the provided locator based on the given boolean value.
     *
     * @param by      the locator strategy to identify the checkbox
     * @param checked true to check the checkbox, false to uncheck it
     */
    public void checkBoxBy(By by, boolean checked) {
        WebElement webElement = null;
        entered(by.toString(), Boolean.toString(checked));
        if (waitForElementAvailable(by)) {
            try {
                webElement = webDriver.findElement(by);
                String checkAttribute = webElement.getAttribute("checked");
                boolean isChecked = false;
                if (checkAttribute != null && checkAttribute.equalsIgnoreCase("true")) {
                    isChecked = true;
                } else if (checkAttribute == null) {
                    isChecked = false;
                }
                if (checked) {
                    if (!isChecked) {
                        webElement.click();
                    }
                } else {
                    if (isChecked) {
                        webElement.click();
                    }
                }
            } catch (NullPointerException e) {
                logMessage(new NullPointerException("Unavailable|" + by.toString() + "|" + String.valueOf(checked) +
                        e.getLocalizedMessage()));
            }
        }
        leaving(webElement.toString());
    }

    /**
     * Clicks the radio button identified by the provided locator.
     *
     * @param by the locator strategy to identify the radio button
     */
    public void checkRadio(By by) {
        waiter.waitForAjaxToFinish();
        waitUntilClickable(by);
        Instant start = Instant.now();
        WebElement inputElement = webDriver.findElement(by);
        try {
            long START_TIME = Instant.now().toEpochMilli();
            inputElement.click();
            waiter.waitForAjaxToFinish();
        } catch (Exception e) {
            JavascriptExecutor executor = (JavascriptExecutor) webDriver;
            long START_TIME = Instant.now().toEpochMilli();
            executor.executeScript("arguments[0].click();", inputElement);
            waiter.waitForAjaxToFinish();
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
        }
    }

    /**
     * Clicks the element identified by the provided locator.
     *
     * @param by the locator strategy to identify the element
     */
    public void clickElementBy(By by) {
        entered(by.toString(), "");
        if (waitForElementAvailable(by)) {
            try {
                WebElement element = this.waitUntilClickable(by);
                element.click();
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
    }

    /**
     * Right-clicks the element identified by the provided locator.
     *
     * @param by the locator strategy to identify the element
     */
    public void clickRightClickBy(By by) {
        if (waitForElementAvailable(by)) {
            try {
                actions.contextClick(webDriver.findElement(by)).perform();
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                        e.getLocalizedMessage()));
            }
        }
    }

    /**
     * Double-clicks the element identified by the provided locator.
     *
     * @param by the locator strategy to identify the element
     */
    public void clickDoubleClickBy(By by) {
        if (waitForElementAvailable(by)) {
            try {
                actions.doubleClick(webDriver.findElement(by)).perform();
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                        e.getLocalizedMessage()));
            }
        }
    }

    /**
     * Clicks the element identified by the provided locator using JavaScript.
     *
     * @param by the locator strategy to identify the element
     */
    public void clickElementUsingJS(By by) {
        LOG.trace("Clicking Element using JavaScript: " + by);
        entered(by.toString(), "");
        if (waitForElementAvailable(by)) {
            try {
                WebElement element = webDriver.findElement(by);
                JavascriptExecutor executor = (JavascriptExecutor) webDriver;
                executor.executeScript("arguments[0].click();", element);
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
    }


    /**
     * Clicks on a WebElement using JavaScript.
     *
     * @param webElement The WebElement to click on.
     */
    public void clickElementUsingJS(WebElement webElement) {
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("arguments[0].click();", webElement);
    }

    /**
     * Clicks on an element within a specified node.
     *
     * @param node The By locator for the node element.
     * @param by   The By locator for the element to click.
     */
    public void clickElementInNode(By node, By by) {
        entered("Node: " + by.toString(), " - Element: " + by.toString());
        if (waitForElementAvailable(by)) {
            try {
                webDriver.findElement(node).findElement(by).click();
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
    }

    /**
     * Presses the Enter key on the specified element.
     *
     * @param by The By locator for the element to press Enter on.
     */
    public void pressEnterBy(By by) {
        entered(by.toString(), "");
        if (waitForElementAvailable(by)) {
            try {
                WebElement webElement = webDriver.findElement(by);
                webElement.sendKeys(Keys.RETURN);
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
    }

    /**
     * Presses the OK button on a popup alert.
     */
    public void pressOKOnPopUp() {
        entered("Pressed OK on popup", "");
        try {
            Alert alert = webDriver.switchTo().alert();
            alert.accept();
        } catch (NoAlertPresentException e) {
            logMessage(new NoAlertPresentException("Unavailable | pressOkOnPopUp" +
                    e.getLocalizedMessage()));
        }
        leaving("Pressed OK on popup");
    }

    /**
     * Presses the Cancel button on a popup alert.
     */
    public void pressCancelOnPopUp() {
        entered("Pressed Cancel on popup", "");
        try {
            Alert alert = webDriver.switchTo().alert();
            alert.dismiss();
        } catch (Exception e) {
            logMessage(new Exception("Unavailable|pressCancelOnPopUp" +
                    e.getLocalizedMessage()));
        }
        leaving("Pressed Cancel on popup");
    }

    /**
     * Moves the mouse to the specified element, clicks and holds.
     * Good for selecting cascading menus.
     *
     * @param by The By locator for the element to move the mouse to and click.
     */
    public void moveMouseToAndClick(By by) {
        entered(by.toString(), "");
        if (waitForElementAvailable(by)) {
            try {
                Actions action = new Actions(this.webDriver);
                WebElement webElement = webDriver.findElement(by);
                action.moveToElement(webElement).clickAndHold(webElement).perform();
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
    }

    /**
     * Moves the mouse to the specified element.
     *
     * @param by The By locator for the element to move the mouse to.
     */
    public void moveMouseTo(By by) {
        entered("Move mouse to: " + by.toString(), "");
        Actions builder = new Actions(webDriver);
        Action hover = builder.moveToElement(webDriver.findElement(by)).build();
        hover.perform();
        leaving("Mouse moved");
    }

    /**
     * Moves the mouse to the specified element by its ID.
     *
     * @param by The By locator for the element to move the mouse to.
     * @deprecated Use {@link PageDriver#moveMouseTo(By)} instead.
     */
    @Deprecated
    public void moveMouseToById(By by) {
        entered("Move mouse to: " + by.toString(), "");
        Actions builder = new Actions(webDriver);
        Action hover = builder.moveToElement(webDriver.findElement(by)).build();
        hover.perform();
        leaving("Mouse moved");
    }

    /**
     * Moves the mouse to the specified element.
     *
     * @param by The By locator for the element to move the mouse to.
     * @deprecated Use {@link PageDriver#moveMouseTo(By)} instead.
     */
    @Deprecated
    public void moveToElement(By by) {
        try {
            WebElement element = webDriver.findElement(by);
            Actions builder = new Actions(webDriver);
            builder.moveToElement(element).perform();
        } catch (Exception e) {
            LOG.trace("MoveToElement -" + "Exception Message : " + e.getLocalizedMessage());
        }
    }

    /**
     * Moves the mouse to the specified WebElement.
     *
     * @param targetElement The WebElement to move the mouse to.
     * @deprecated Use {@link PageDriver#moveMouseTo(By)} instead.
     */
    @Deprecated
    public void moveToElement(WebElement targetElement) {
        try {
            JavascriptExecutor jse = (JavascriptExecutor) webDriver;
            jse.executeScript("arguments[0].scrollIntoView()", targetElement);
        } catch (StaleElementReferenceException e) {
            LOG.trace(e.toString());
        } catch (Exception e) {
            LOG.trace(e.toString());
        }
    }

    /**
     * Sends the specified key sequence to the element identified by the given locator.
     *
     * @param ObjID the locator used to identify the element
     * @param key   the key sequence to send
     */
    public void sendKeys(By ObjID, Keys key) {
        waiter.waitForAjaxToFinish();
        this.webDriver.findElement(ObjID).sendKeys(new CharSequence[]{key});
        waiter.waitForAjaxToFinish();
    }

    /**
     * Retrieves the size of the table identified by the given locator.
     *
     * @param by the locator used to identify the table
     * @return the size of the table as a string, or "-1" if the table is not available
     */
    public String getTableSize(By by) {
        int size = -1;
        entered(by.toString(), "Get Table Size");
        if (waitForElementAvailable(by)) {
            try {
                size = webDriver.findElements(by).size();
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                        e.getLocalizedMessage()));
            }
        }
        leaving("Get Table Size");
        return Integer.toString(size);
    }

    /**
     * Retrieves the row number of the table row containing the specified option text.
     *
     * @param by         the locator used to identify the table
     * @param optionText the text to search for in the table rows
     * @return the row number as a string, or "-1" if the option text is not found
     */
    public String getTableRow(By by, String optionText) {
        int row = -1;
        entered(by.toString(), optionText);
        if (waitForElementAvailable(by)) {
            try {
                WebElement webElement = webDriver.findElement(by);
                List<WebElement> allOptions = webElement.findElements(by);
                for (WebElement option : allOptions) {
                    if (option.getText().contains(optionText)) {
                        break;
                    }
                    row++;
                }
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" + optionText + "|" +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
        return Integer.toString(row);
    }

    /**
     * Retrieves the value of the specified attribute for the element identified by the given locator.
     *
     * @param by        the locator used to identify the element
     * @param attribute the name of the attribute to retrieve
     * @return the value of the specified attribute, or an empty string if the attribute is not available
     */
    public String getFieldAttribute(By by, String attribute) {
        String attributeValue = "";
        entered(by.toString(), attribute);
        if (waitForElementAvailable(by)) {
            try {
                attributeValue = webDriver.findElement(by).getAttribute(attribute);
                leaving(by.toString());
            } catch (NoSuchElementException e) {
                if (attributeValue.isEmpty()) {
                    leaving("Unavailable|" + by.toString() + "|" + attribute + "|" +
                            e.getLocalizedMessage());
                }
                return attributeValue;
            } catch (Exception e) {
                attributeValue = "";
            }
        }
        return attributeValue;
    }

    /**
     * Retrieves the text value of the element identified by the given locator.
     *
     * @param by the locator used to identify the element
     * @return the text value of the element, or an empty string if the text value is not available
     */
    public String getTextValue(By by) {
        String textValue = null;
        entered(by.toString(), "");
        if (waitForElementAvailable(by)) {
            try {
                textValue = getTextBy(by);
                if (textValue != null) {
                    if (textValue.isEmpty()) {
                        textValue = getFieldAttribute(by, "value");
                        if (textValue != null) {
                            if (textValue.isEmpty()) {
                                textValue = "";
                            }
                        }
                    }
                }
            } catch (NoSuchElementException e) {
                if (textValue != null) {
                    if (textValue.isEmpty()) {
                        textValue = "";
                    }
                }
            }
        }
        leaving(by.toString() + "|" + textValue);
        return textValue;
    }


    /**
     * Retrieves a list of text values from the elements identified by the given locator.
     *
     * @param by the locator used to identify the elements
     * @return a LinkedList containing the text values of the elements
     */
    public LinkedList<String> getTextFromElements(By by) {
        LinkedList<String> values = new LinkedList<String>();
        List<WebElement> elmList = getElements(by);
        for (WebElement elm : elmList) {
            values.add(elm.getText());
        }
        return values;
    }

    /**
     * Retrieves the inner HTML value of the element identified by the given locator.
     *
     * @param by the locator used to identify the element
     * @return the inner HTML value of the element
     */
    public String getInnerValue(By by) {
        WebElement inputElement = webDriver.findElement(by);
        return inputElement.getAttribute("innerHTML");
    }

    /**
     * Retrieves the inner text value of the element identified by the given locator.
     *
     * @param by the locator used to identify the element
     * @return the inner text value of the element, or null if the element is not available
     */
    public String getInnerText(By by) {
        String text = null;
        try {
            WebElement element = webDriver.findElement(by);
            text = element.getText();
            text = StringUtils.isEmpty(text) ? element.getAttribute("innerText") : text;
            if (text == null) {
                LOG.warn("Can't get inner text for web element with locator: {}", by);
            }
        } catch (WebDriverException e) {
            LOG.error("WebDriverException while getting inner text from web element with locator: {}", by);
        }
        return text;
    }

    /**
     * Retrieves the inner numeric value of the element identified by the given locator.
     *
     * @param by the locator used to identify the element
     * @return the inner numeric value of the element as an Integer, or null if the element does not contain a valid number
     */
    public Integer getInnerNumber(By by) {
        Integer number = null;
        String text = getInnerText(by);
        if (NumberUtils.isCreatable(text)) {
            number = Integer.parseInt(text);
        }
        if (number == null) {
            LOG.warn("Can't get number from text: '{}' found in web element by locator {}", text, by);
        }
        return number;
    }

    /**
     * Retrieves the text value of a child element within a parent element.
     *
     * @param pBy the locator used to identify the parent element
     * @param cBy the locator used to identify the child element within the parent
     * @return the text value of the child element, or an empty string if the child element is not available
     */
    public String getChildFieldTextById(By pBy, By cBy) {
        String childElementText = "";
        WebElement parent = null;
        entered("Parent: " + pBy.toString(), "- Child: " + cBy);
        if (waitForElementAvailable(pBy)) {
            try {
                parent = webDriver.findElement(pBy);
                if (waitForElementAvailable(cBy)) {
                    childElementText = parent.findElement(cBy).getText();
                }
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + pBy.toString() + "|" + cBy + "|" +
                        e.getLocalizedMessage()));
            }
        }
        leaving("Parent: " + pBy.toString() + "- Child: " + cBy);
        return childElementText;
    }

    /**
     * Retrieves a list of text values from the elements identified by the given locator.
     *
     * @param by the locator used to identify the elements
     * @return a List containing the text values of the elements
     */
    public List<String> getElementStringArray(By by) {
        List<String> allElements = new ArrayList<>();
        try {
            List<WebElement> elementList = webDriver.findElements(by);
            for (int i = 0; i < elementList.size(); i++) {
                allElements.add(elementList.get(i).getText());
            }
        } catch (Exception e) {
            logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                    e.getLocalizedMessage()));
        }
        return allElements;
    }

    /**
     * Checks if the element identified by the given locator is selected.
     *
     * @param by the locator used to identify the element
     * @return true if the element is selected, false otherwise
     */
    public boolean isElementSelectedBy(By by) {
        entered(by.toString(), "");
        boolean isElementSelected = false;
        if (waitForElementAvailable(by)) {
            try {
                WebElement element = null;
                element = webDriver.findElement(by);
                isElementSelected = element.isSelected();
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
        return isElementSelected;
    }


    /**
     * Checks if a radio button identified by the given locator is enabled.
     *
     * @param by the locator used to identify the radio button
     * @return true if the radio button is enabled, false otherwise
     */
    public boolean isRadioButtonEnabledBy(By by) {
        entered(by.toString(), "");
        boolean isElementEnabled = false;
        if (waitForElementAvailable(by)) {
            try {
                WebElement element = null;
                element = webDriver.findElement(by);
                isElementEnabled = element.isEnabled();
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
        return isElementEnabled;
    }

    /**
     * Checks if a checkbox identified by the given locator is checked.
     *
     * @param by        the locator used to identify the checkbox
     * @param attribute the attribute to check for the checkbox (e.g., "checked")
     * @return true if the checkbox is checked, false otherwise
     */
    public boolean isCheckBoxCheckedBy(By by, String attribute) {
        entered(by.toString(), "");
        boolean isElementChecked = false;
        if (waitForElementAvailable(by)) {
            WebElement element = null;
            try {
                element = webDriver.findElement(by);
                isElementChecked = element.isSelected();
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + attribute + "|" +
                        e.getLocalizedMessage()));
            }
        }
        leaving(by.toString());
        return isElementChecked;
    }

    /**
     * Waits until the element identified by the given locator is visible.
     *
     * @param locator     the locator used to identify the element
     * @param waitTimeOut the maximum time to wait for the element to be visible (in milliseconds)
     */
    public void waitTillElementVisibility(By locator, long waitTimeOut) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofMillis(waitTimeOut));
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(locator)));
    }

    /**
     * Waits until the element identified by the given locator is invisible.
     *
     * @param by          the locator used to identify the element
     * @param waitTimeOut the maximum time to wait for the element to be invisible (in milliseconds)
     */
    public void waitUntilElementInvisible(By by, long waitTimeOut) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofMillis(waitTimeOut));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    /**
     * Waits for the page to fully load.
     *
     * @param explicitWaitTimeout the maximum time to wait for the page to load (in milliseconds)
     */
    public void waitForPageToLoad(long explicitWaitTimeout) {
        new WebDriverWait(webDriver, Duration.ofMillis(explicitWaitTimeout)).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript
                        ("return document.readyState").equals("complete"));
    }

    /**
     * Waits until the element identified by the given locator is clickable.
     *
     * @param by the locator used to identify the element
     * @return the clickable WebElement
     */
    public WebElement waitUntilClickable(By by) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofMillis(explicitWaitTimeout));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(by)));
        return element;
    }


    /**
     * waitForElementAvailable
     * Set your own wait time in milliseconds
     *
     * @param by
     * @param throwException
     * @param timeToWait
     * @return
     */
    public boolean waitForElementAvailable(By by, boolean throwException, Long timeToWait) {
        WebDriverWait wait = new WebDriverWait(this.getWebDriver(), Duration.ofMillis(timeToWait));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            return true;
        } catch (TimeoutException e) {
            if (throwException) {
                LOG.debug("Element {} not visible after {} milli-seconds", by, timeToWait);
                throw e;
            }
            LOG.debug("Element {} not visible after {} milli-seconds, continuing test execution", by, timeToWait);
            return false;
        }
    }

    /**
     * waitForElementAvailable
     * Use the default wait time set in properties in test.wait.timeout
     *
     * @param by
     * @return
     */
    public boolean waitForElementAvailable(By by) {
        return waitForElementAvailable(by, false);
    }

    /**
     * waitForElementAvailable
     * Use the default wait time set in properties in test.wait.timeout and throw an exception (default false)
     *
     * @param by
     * @param throwException
     * @return
     */
    public boolean waitForElementAvailable(By by, boolean throwException) {
        WebDriverWait wait = new WebDriverWait(this.getWebDriver(), Duration.ofMillis(explicitWaitTimeout));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            return true;
        } catch (TimeoutException e) {
            if (throwException) {
                LOG.debug("Element {} not visible after {} milli-seconds", by, explicitWaitTimeout);
                throw e;
            }
            LOG.debug("Element {} not visible after {} milli-seconds, continuing test execution", by, explicitWaitTimeout);
            return false;
        }
    }

    /**
     * Checks if the element identified by the given locator is displayed on the page.
     *
     * @param by the locator used to identify the element
     * @return true if the element is displayed, false otherwise
     */
    public boolean isElementDisplayed(By by) {
        boolean isElementVisible = false;
        WebElement element = null;
        try {
            element = webDriver.findElement(by);
            isElementVisible = element.isDisplayed();
        } catch (Exception e) {
            logMessage(new Exception("Unavailable|" + getStackTraceInfo(by.toString()) + "|" + by.toString() + "|Not Displayed"));
            isElementVisible = false;
        }
        return isElementVisible;
    }

    /**
     * Waits for an element to be available within a node element, and returns when the element is found or the timeout is reached.
     *
     * @param node the locator used to identify the node element
     * @param by   the locator used to identify the element to wait for
     */
    public void waitForElementInNode(By node, By by) {
        entered("Waiting for element in node: " + by.toString(), "");
        WebElement elementLocator = null;
        if (waitForElementAvailable(by)) {
            try {
                for (int second = 0; ; second++) {
                    if (second >= (explicitWaitTimeout / 1000)) {
                        System.out.println("Element not found: " + by.toString());
                        break;
                    }
                    try {
                        elementLocator = webDriver.findElement(node).findElement(by);
                    } catch (Exception e) {
                    }
                    if (elementLocator != null)
                        break;
                    pause(pauseDelay);
                }
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + node.toString() + "|" +
                        by.toString() + "|" +
                        e.getLocalizedMessage()));
            }
        }
        leaving("Element found in node");
    }

    /**
     * Waits for an element to be gone from the page, and returns when the element is no longer present or the timeout is reached.
     *
     * @param by the locator used to identify the element
     */
    public void waitForElementGone(By by) {
        entered("Waiting for element gone: " + by.toString(), "");
        if (this.isElementDisplayed(by)) {
            try {
                for (int second = 0; ; second++) {
                    if (second >= (explicitWaitTimeout / 1000)) {
                        leaving("Warning: Element still present: " + by.toString());
                        break;
                    }
                    try {
                        if (!this.isElementDisplayed(by))
                            break;
                    } catch (Exception e) {
                    }
                    pause(pauseDelay);
                }
            } catch (Exception e) {
                logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                        e.getLocalizedMessage()));
            }
        }
        leaving("Element is gone");
    }

    /**
     * Waits for an element to be not displayed on the page, and returns when the element is not displayed or the timeout is reached.
     *
     * @param by the locator used to identify the element
     */
    public void waitForElementNotDisplayed(By by) {
        entered("Waiting for element not displayed: " + by.toString(), "");
        if (waitForElementAvailable(by)) {
            for (int second = 0; ; second++) {
                if (second >= (explicitWaitTimeout / 1000)) {
                    leaving("Element is Displayed: " + by.toString());
                    break;
                }
                try {
                    if (!waitForElementAvailable(by))
                        break;
                } catch (Exception e) {
                    logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                            e.getLocalizedMessage()));
                }
                pause(pauseDelay);
            }
        }
        leaving("Element is not displayed");
    }

    /**
     * Waits for either of two elements to be available on the page, and returns when one of the elements is found or the timeout is reached.
     *
     * @param by1 the locator used to identify the first element
     * @param by2 the locator used to identify the second element
     * @return true if either of the elements is found, false otherwise
     */
    public boolean waitForElementOrElement(By by1, By by2) {
        entered("Waiting for elementOrElement: " +
                by1.toString() + " or " + by2.toString(), "");
        for (int second = 0; ; second++) {
            if (second >= explicitWaitTimeout) {
                logMessage(new NoSuchElementException("Element not found"));
                break;
            }
            try {
                if (waitForElementAvailable(by1))
                    break;
            } catch (NoSuchElementException e) {
                logMessage(new NoSuchElementException("Unavailable|" + by1.toString() +
                        e.getLocalizedMessage()));
                return false;
            }
            try {
                if (waitForElementAvailable(by2))
                    break;
            } catch (NoSuchElementException e) {
                logMessage(new NoSuchElementException("Unavailable|" + by2.toString() +
                        e.getLocalizedMessage()));
                return false;
            }
        }
        leaving("Elements found");
        return true;
    }


    /**
     * Waits for the specified element to have the given value.
     *
     * @param by    The locator strategy to find the element
     * @param value The expected value of the element
     */
    public void waitForValue(By by, String value) {
        entered("Waiting for value: " + by.toString(), "");
        if (waitForElementAvailable(by)) {
            for (int second = 0; ; second++) {
                if (second >= (explicitWaitTimeout / 1000)) {
                    System.out.println("Expected Value: " + value);
                    System.out.println("Actual Value: " + getFieldAttribute(by, "innerText"));
                    break;
                }
                try {
                    if (getFieldAttribute(by, "value").equals(value))
                        break;
                } catch (Exception e) {
                    logMessage(new Exception("Unavailable|" + by.toString() + "|" +
                            e.getLocalizedMessage()));
                }
                pause(pauseDelay);
            }
        }
        leaving("Value found");
    }

    /**
     * Waits for the specified element to be enabled.
     *
     * @param by The locator strategy to find the element
     */
    public void waitForElementEnabled(By by) {
        int second = 0;
        while (true) {
            if (second >= (explicitWaitTimeout / 1000)) {
                leaving("Element is not enabled" + by.toString());
                break;
            }
            try {
                boolean enabled = this.webDriver.findElement(by).isEnabled();
                if (enabled) {
                    leaving("Element is enabled: " + by.toString());
                }
                break;
            } catch (Exception var4) {
                pause(pauseDelay);
                ++second;
            }
        }
    }

    /**
     * Gets the ID of the last iframe element on the page with a title attribute.
     *
     * @return The ID of the last iframe element with a title attribute, or null if none found
     */
    public String getLastFrame() {
        String frameToSwitch = null;
        try {
            this.switchToDefaultFrame();
            this.waitForElementAvailable(By.xpath("//iframe[@title]"));
            List<WebElement> elementsList = this.getElements(By.xpath("//iframe[@title]"));
            if (elementsList.size() > 0) {
                WebElement lastFrame = elementsList.get(elementsList.size() - 1);
                frameToSwitch = lastFrame.getAttribute("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return frameToSwitch;
    }

    /**
     * Enters the given input value into the specified field, handling potential StaleElementReferenceExceptions.
     *
     * @param by         The locator strategy to find the field
     * @param inputValue The value to enter into the field
     */
    public void staleEnterValueInFieldBy(By by, String inputValue) {
        entered(by.toString(), inputValue);
        int count = 0;
        while (count < 10) {
            try {
                WebElement webElement = webDriver.findElement(by);
                webElement.sendKeys(inputValue);
                break;
            } catch (StaleElementReferenceException e) {
                LOG.trace("Catching exception for " + count + "time");
            }
            count++;
        }
        leaving(by.toString());
    }

    public void clearField(By by) {
        this.webDriver.findElement(by).clear();
    }


    /**
     * Clicks an element within another element using the provided locators.
     *
     * @param sourceLocator The locator strategy to find the source element
     * @param targetLocator The locator strategy to find the target element within the source element
     */
    public void clickElementWithinElement(By sourceLocator, By targetLocator) {
        entered(sourceLocator.toString(), "");
        waitForElementVisible(sourceLocator);
        WebElement sourceElement = webDriver.findElement(sourceLocator);
        leaving(sourceLocator.toString());
        entered(targetLocator.toString(), "");
        WebElement targetElement = sourceElement.findElement(targetLocator);
        targetElement.click();
        leaving(targetLocator.toString());
    }

    /**
     * Simulates pressing the Escape key using the Robot class.
     */
    public void pressEscape() {
        Robot robo = null;
        try {
            robo = new Robot();
        } catch (NullPointerException | AWTException e) {
            logMessage(new NullPointerException("Robot error:" + e.getLocalizedMessage()));
        }
        robo.delay(100);
        robo.keyPress(KeyEvent.VK_ESCAPE);
        robo.delay(100);
    }

    /**
     * Checks if the specified element is visible on the page.
     *
     * @param by The locator strategy to find the element
     * @return true if the element is visible, false otherwise
     */
    public boolean isElementVisible(By by) {
        entered(by.toString(), "");
        boolean isElementVisible = false;
        try {
            isElementVisible = isElementDisplayed(by);
        } catch (NoSuchElementException e) {
            isElementVisible = false;
            logMessage(new NoSuchElementException(by.toString() + " is not found"));
        }
        leaving(by.toString());
        return isElementVisible;
    }

    /**
     * Checks if the specified element is enabled on the page.
     *
     * @param by The locator strategy to find the element
     * @return true if the element is enabled, false otherwise
     */
    public boolean isElementEnabled(By by) {
        entered(by.toString(), "");
        Boolean isEnabled;
        try {
            isEnabled = webDriver.findElement(by).isEnabled();
        } catch (NoSuchElementException e) {
            isEnabled = false;
            logMessage(new NoSuchElementException(by.toString() + " is not found"));
        }
        leaving(by.toString());
        return isEnabled;
    }

    /**
     * Checks if the specified checkbox element is checked by its name attribute.
     *
     * @param by        The locator strategy to find the checkbox element
     * @param attribute The name attribute of the checkbox element
     * @return true if the checkbox is checked, false otherwise
     */
    public boolean isCheckBoxCheckedByName(By by, String attribute) {
        entered(by.toString(), "");
        boolean isElementChecked = false;
        WebElement element = null;
        try {
            element = webDriver.findElement(by);
            isElementChecked = element.isSelected();
        } catch (Exception e) {
            logMessage(new NoSuchElementException(by.toString() + " is not found"));
        }
        leaving(by.toString());
        return isElementChecked;
    }

    /**
     * Checks if the specified text is present on the current page.
     *
     * @param textToVerify The text to search for on the page
     * @return true if the text is present, false otherwise
     */
    public boolean isTextPresent(String textToVerify) {
        Boolean textAvail = webDriver.getPageSource().contains(textToVerify);
        if (textAvail) {
            return textAvail;
        } else {
            textAvail = webDriver.getPageSource().contains(textToVerify);
            return textAvail;
        }
    }

    /**
     * Clicks the mouse on a specific point within an element identified by the given locator.
     *
     * @param by      The locator strategy to find the element
     * @param xOffset The horizontal offset from the top-left corner of the element
     * @param yOffset The vertical offset from the top-left corner of the element
     */
    public void clickMouseOnPointInElement(By by, int xOffset, int yOffset) {
        entered("Move mouse to: " + by.toString(), "X: " + xOffset + " - Y: " + yOffset);
        Actions builder = new Actions(webDriver);
        Action hover = builder.moveToElement(webDriver.findElement(by), xOffset, yOffset).click().build();
        hover.perform();
        leaving("Mouse moved");
    }

    /**
     * Waits for the specified element to be visible on the page.
     *
     * @param by The locator strategy to find the element
     */
    public void waitForElementVisible(By by) {
        waitForElementAvailable(by);
    }

    /**
     * Populates a dropdown element identified by the given locator with the specified index value.
     *
     * @param by    The locator strategy to find the dropdown element
     * @param value The index value to select in the dropdown
     */
    public void populateDropDownByIndex(By by, int value) {
        if (value < 0) {
            LOG.trace("Drop down value is empty");
        } else {
            waiter.waitForAjaxToFinish();
            waitUntilClickable(by);
            Instant start = Instant.now();
            long START_TIME = Instant.now().toEpochMilli();
            Select select = new Select(webDriver.findElement(by));
            select.selectByIndex(value);
            waiter.waitForAjaxToFinish();
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
        }
    }

    /**
     * Populates a dropdown element identified by the given locator with the specified value.
     *
     * @param by    The locator strategy to find the dropdown element
     * @param value The value to select in the dropdown
     */
    public void populateDropDownByValue(By by, String value) {
        if ("".equals(value) || value == null) {
            LOG.trace("Drop down value is empty");
        } else {
            waiter.waitForAjaxToFinish();
            waitUntilClickable(by);
            Instant start = Instant.now();
            long START_TIME = Instant.now().toEpochMilli();
            Select select = new Select(webDriver.findElement(by));
            select.selectByValue(value);
            waiter.waitForAjaxToFinish();
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
        }
    }

    /**
     * Waits for the page to reach the 'ready' state by checking the document.readyState property.
     */
    public void pageReadyState() {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        if (js.executeScript("return document.readyState").toString().equals("complete")) {
            LOG.trace("Page is in 'ready' state");
            return;
        }
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logMessage(new InterruptedException("Robot error:" + e.getLocalizedMessage()));
            }
            if (js.executeScript("return document.readyState").toString().equals("complete")) {
                break;
            }
        }
    }

    /**
     * Populates a listbox element identified by the given locator with the specified text value.
     *
     * @param field The locator strategy to find the listbox element
     * @param value The text value to select in the listbox
     */
    public void populateListBoxByText(By field, String value) {
        waiter.waitForAjaxToFinish();
        List<WebElement> options = null;
        String[] option_values;
        WebElement selectBox = webDriver.findElement(field);
        waiter.waitForAjaxToFinish();
        Select dropdown = new Select(selectBox);
        options = dropdown.getOptions();
        Integer len = options.size();
        option_values = new String[len];
        waiter.waitForAjaxToFinish();
        for (int i = 0; i < len; i++) {
            waiter.waitForAjaxToFinish();
            option_values[i] = options.get(i).getText();
            if (option_values[i].equalsIgnoreCase(value)) {
                options.get(i).click();
                break;
            }
        }
    }

    /**
     * Populates a dropdown element identified by the given locator with the value that partially matches the provided text.
     *
     * @param by    The locator strategy to find the dropdown element
     * @param value The partial text value to match and select in the dropdown
     */
    public void populateDropDownByPartialText(By by, String value) {
        waiter.waitForAjaxToFinish();
        if ("".equals(value) || value == null) {
            LOG.trace("Drop down value is empty");
        } else {
            waiter.waitForAjaxToFinish();
            waitUntilClickable(by);
            WebElement element = elementLocator(by);
            Instant start = Instant.now();
            long START_TIME = Instant.now().toEpochMilli();
            Select select = new Select(element);
            waiter.waitForAjaxToFinish();
            select.getOptions().parallelStream().filter(option -> option.getAttribute("textContent").toLowerCase().contains(value.toLowerCase()))
                    .findFirst().ifPresent(option -> {
                        String dropDownValue = option.getAttribute("textContent");
                        select.selectByVisibleText(dropDownValue);
                        LOG.trace(dropDownValue + " is entered for the dropdown");
                    });
            waiter.waitForAjaxToFinish();
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
        }
    }

    /**
     * Populates a dropdown element identified by the given locator with the specified text value.
     *
     * @param by    The locator strategy to find the dropdown element
     * @param value The text value to select in the dropdown
     */
    public void populateDropDownByText(By by, String value) {
        waiter.waitForAjaxToFinish();
        if ("".equals(value) || value == null) {
            LOG.trace("Drop down value is empty");
        } else {
            waiter.waitForAjaxToFinish();
            waitUntilClickable(by);
            WebElement element = elementLocator(by);
            Instant start = Instant.now();
            long START_TIME = Instant.now().toEpochMilli();
            Select select = new Select(element);
            waiter.waitForAjaxToFinish();
            select.selectByVisibleText(value);
            waiter.waitForAjaxToFinish();
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
        }
    }

    /**
     * Defines an ExpectedCondition that waits for an element to be visible and returns the element if it is displayed.
     *
     * @param locator The locator strategy to find the element
     * @return An ExpectedCondition that waits for the element to be visible and returns the element if it is displayed
     */
    public ExpectedCondition<WebElement> visibilityOfElementLocated(final By locator) {
        return new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                WebElement toReturn = webDriver.findElement(locator);
                if (toReturn.isDisplayed()) {
                    return toReturn;
                }
                return null;
            }
        };
    }

    /**
     * Attempts to find and click an element identified by the given locator, retrying up to 3 times if a StaleElementReferenceException is encountered.
     *
     * @param by The locator strategy to find the element
     * @return true if the element was successfully clicked, false otherwise
     */
    public boolean retryingFindClick(By by) {
        boolean result = false;
        int attempts = 0;
        while (attempts < 3) {
            try {
                webDriver.findElement(by).click();
                result = true;
                break;
            } catch (StaleElementReferenceException e) {
            }
            attempts++;
        }
        return result;
    }

    /**
     * Clicks a button element identified by the given locator, attempting to click it directly or using JavaScript if an exception occurs.
     *
     * @param by The locator strategy to find the button element
     */
    public void clickButton(By by) {
        waiter.waitForAjaxToFinish();
        WebElement inputElement = webDriver.findElement(by);
        try {
            inputElement.click();
        } catch (Exception e) {
            clickElementUsingJS(by);
        }
    }

    /**
     * Navigates to a specified tab on the page.
     *
     * @param by The locator strategy to find the tab element
     */
    public void goToTab(By by) {
        waiter.waitForAjaxToFinish();
        waitUntilClickable(by);
        Instant start = Instant.now();
        long START_TIME = Instant.now().toEpochMilli();
        webDriver.findElement(by).click();
        waiter.waitForAjaxToFinish();
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
    }

    /**
     * Locates a web element using the provided locator strategy.
     *
     * @param by The locator strategy to find the element
     * @return The located WebElement
     */
    public WebElement elementLocator(By by) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver)
                .withTimeout(Duration.ofMillis(explicitWaitTimeout))
                .pollingEvery(Duration.ofMillis(pollingInterval))
                .ignoring(NoSuchElementException.class);
        return webDriver.findElement(by);
    }

    /**
     * Waits for the AJAX loading modal box to become invisible, up to the specified timeout.
     *
     * @param waitTimeout The maximum time to wait for the AJAX loading modal box to disappear, in milliseconds
     */
    public void waitWhileAjaxCompletesUpToMillis(long waitTimeout) {
        new WebDriverWait(webDriver, Duration.ofMillis(waitTimeout))
                .until(ExpectedConditions.invisibilityOfElementLocated((By.id("ajaxLoadingModalBox_content"))));
    }


    /**
     * Counts the number of rows in a table on the web page using a provided JavaScript expression.
     *
     * @param row_count1 A JavaScript expression that returns the number of rows in the table
     * @return The number of rows in the table as an Integer
     */
    public Integer countSearch(String row_count1) {
        waiter.waitForAjaxToFinish();
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        String table_length1 = js.executeScript(row_count1).toString();
        Integer table_length = Integer.parseInt(table_length1);
        LOG.trace("Size:::" + table_length);
        return table_length;
    }

    /**
     * Finds and returns a list of web elements that match the given locator.
     *
     * @param by The locator strategy and value to use for finding the web elements
     * @return A list of web elements that match the given locator
     */
    public List<WebElement> getSelectValueWebElementListBy(By by) {
        return webDriver.findElements(by);
    }

    /**
     * Clicks on an element after scrolling down the page and waiting for the element to be enabled.
     *
     * @param by The locator strategy and value to use for finding the element to click
     */
    public void clickElementByScrollDown(By by) {
        waitForElementEnabled(by);
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("scroll(0, 1000);");
        leaving(by.toString());
    }

    /**
     * Gets the text of the selected option in a dropdown element by its attribute value.
     *
     * @param id        The locator strategy and value to use for finding the dropdown element
     * @param attribute The attribute name to retrieve the text from (e.g., "value", "text")
     * @return The text of the selected option in the dropdown
     */
    public String getSelectedTextByAttribute(By id, String attribute) {
        return new Select(webDriver.findElement(id)).getFirstSelectedOption().getAttribute(attribute);
    }

    /**
     * Closes all browser tabs except for the current tab.
     *
     * @param currentTab The window handle of the current tab
     */
    public void closeOtherTabsExcept(String currentTab) {
        for (String otherTabs : webDriver.getWindowHandles()) {
            if (!otherTabs.equals(currentTab)) {
                webDriver.switchTo().window(otherTabs);
                waiter.waitForAjaxToFinish();
                webDriver.close();
            }
        }
        webDriver.switchTo().window(currentTab);
    }

    /**
     * Switches the browser context to the tab at the specified index.
     *
     * @param index The index of the tab to switch to (0-based)
     */
    public void switchToTabByIndex(int index) {
        Set<String> set = webDriver.getWindowHandles();
        List<String> list = new ArrayList<>(set);
        webDriver.switchTo().window(list.get(index));
    }

    /**
     * Gets the number of rows in a table on the web page using the given locator.
     *
     * @param by The locator strategy and value to use for finding the table rows
     * @return The number of rows in the table as an Integer
     */
    public Integer getRowSize(By by) {
        waitForElementVisible(by);
        return this.webDriver.findElements(by).size();
    }

    /**
     * Gets the text values of a React dropdown box on the web page using the given locator.
     *
     * @param by The locator strategy and value to use for finding the dropdown box options
     * @return A list of strings containing the text values of the dropdown box options
     */
    public List<String> getReactDropBoxValue(By by) {
        List<WebElement> dropBoxValueElements = webDriver.findElements(by);
        List<String> dropBoxValues = new ArrayList<>();
        for (WebElement dropBoxValueElement : dropBoxValueElements) {
            dropBoxValues.add(dropBoxValueElement.getText());
        }
        return dropBoxValues;
    }

    /**
     * Gets a list of values from a web page using the given locator and options format.
     *
     * @param by      The locator strategy and value to use for finding the elements containing the values
     * @param options A format string used to construct the XPath expression for finding each value
     * @return A list of strings containing the values found on the web page
     */
    public List<String> getValuesListBy(By by, String options) {
        entered(by.toString(), "value");
        waiter.waitForAjaxToFinish();
        waitForElementAvailable(by);
        List<WebElement> webElements = webDriver.findElements(by);
        List<String> availableValues = new ArrayList<String>();
        for (int index = 0; index < webElements.size(); index++) {
            String values = webDriver.findElement(By.xpath(String.format(options, index + 1))).getText();
            availableValues.add(values);
        }
        leaving(by.toString());
        return availableValues;
    }

    /**
     * Waits for an element to become invisible on the page.
     *
     * @param by The locator strategy to find the element.
     */
    public void waitForInVisibility(By by) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofMillis(explicitWaitTimeout));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    /**
     * Waits for an element to become visible on the page with a specified timeout.
     *
     * @param by       The locator strategy to find the element.
     * @param waitTime The maximum time in seconds to wait for the element to become visible.
     * @return The visible WebElement.
     */
    public WebElement waitForVisibility(By by, int waitTime) {
        Wait<WebDriver> wait = new WebDriverWait(this.webDriver, Duration.ofSeconds(waitTime));
        WebElement divElement = wait.until(this.visibilityOfElementLocated(by));
        return divElement;
    }

    /**
     * Waits for an element to become visible on the page with the default timeout.
     *
     * @param by The locator strategy to find the element.
     * @return The visible WebElement.
     */
    public WebElement waitForVisibility(By by) {
        Wait<WebDriver> wait = new WebDriverWait(this.webDriver, Duration.ofMillis(explicitWaitTimeout));
        WebElement divElement = (WebElement) wait.until(this.visibilityOfElementLocated(by));
        return divElement;
    }

    /**
     * Clicks on an element and then shifts the cursor to the left by the specified count.
     *
     * @param by    The locator strategy to find the element.
     * @param count The number of times to shift the cursor to the left.
     */
    public void shiftLeftOnClickingLeftArrow(By by, int count) {
        waitForElementVisible(by);
        WebElement element = webDriver.findElement(by);
        element.click();
        for (int i = 1; i <= count; i++) {
            element.sendKeys(Keys.chord(Keys.ARROW_LEFT));
        }
        leaving(by.toString());
    }

    /**
     * Clicks on an element, shifts the cursor to the left by the specified count while holding the Shift key,
     * and then enters the specified text.
     *
     * @param by    The locator strategy to find the element.
     * @param text  The text to enter after selecting.
     * @param count The number of times to shift the cursor to the left while holding the Shift key.
     */
    public void pressShiftLeftArrowAndSelectText(By by, String text, int count) {
        waitForElementVisible(by);
        WebElement element = webDriver.findElement(by);
        element.click();
        for (int j = 1; j <= count; j++) {
            element.sendKeys(Keys.chord(Keys.SHIFT, Keys.ARROW_LEFT));
        }
        element.sendKeys(text);
        leaving(by.toString());
    }

    /**
     * Clicks on an element and then shifts the cursor to the right by the specified count.
     *
     * @param by    The locator strategy to find the element.
     * @param count The number of times to shift the cursor to the right.
     */
    public void shiftRightOnClickingRightArrow(By by, int count) {
        waitForElementVisible(by);
        WebElement element = webDriver.findElement(by);
        element.click();
        for (int i = 1; i <= count; i++) {
            element.sendKeys(Keys.chord(Keys.ARROW_RIGHT));
        }
        leaving(by.toString());
    }

    /**
     * Scrolls up the page by 750 pixels and clicks on an enabled element.
     *
     * @param by The locator strategy to find the element.
     */
    public void clickElementByScrollUp(By by) {
        waitForElementEnabled(by);
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("scroll(0, -750);");
        leaving(by.toString());
    }

    /**
     * Checks if an element is active by verifying if its class attribute contains the string "active".
     *
     * @param by The locator strategy to find the element.
     * @return true if the element is active, false otherwise.
     */
    public boolean isElementActive(By by) {
        boolean result = false;
        String elementClassName;
        WebElement element = null;
        try {
            elementClassName = webDriver.findElement(by).getAttribute("class");
            result = elementClassName.contains("active");
        } catch (Exception e) {
            result = false;
        }
        return result;
    }


    /**
     * Retrieves the page load timer for a given element.
     * <p>
     * This method executes JavaScript to capture various performance metrics related to page load,
     * Ajax rendering, navigation start, preparation time, fetch start, and server response time.
     * The metrics are logged to the console and returned as a formatted string.
     *
     * @param by    The locator strategy to find the element.
     * @param ST_TM The start time of the page load.
     * @return A formatted string containing the performance metrics.
     */
    public String getPageLoadTimer(By by, long ST_TM) {
        String loadTime = "";

        if (timer.equalsIgnoreCase("Y")) {
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            loadTime = (String) js.executeScript("if (window.performance === undefined) {\n" +
                    "return ;\n" +
                    "}\n" +
                    "var resources = window.performance.getEntriesByType('resource');\n" +
                    "if (resources !== undefined && resources.length > 0) {\n" +
                    "    var ajaxResponse = '';\n" +
                    "for (var i=resources.length-1; i >=0 ; i--) {\n" +
                    "var string = resources[i].name ;\n" +
                    "if(resources[i].initiatorType == 'xmlhttprequest' &&  string.indexOf('TealeafTarget') < 0 &&  string.indexOf('aaa-app') > 0 &&  string.indexOf('.js') < 0  &&(string.indexOf('.xhtml') > 0 ||  string.indexOf('.') < 0)) {\n"
                    +
                    "var  prepTimeEnd  = 0;\n" +
                    "console.log('resources[i].name' + resources[i].name);\n" +
                    "var focusoutTime = 0;\n" +
                    "if( window.performance.getEntriesByName('mark_ajax_start_BLS_focusout').length >0 ){   \n" +
                    "focusoutTime = window.performance.getEntriesByName('mark_ajax_start_BLS_focusout')[0].startTime;\n" +
                    "}\n" +
                    "var ajaxStartLength = 0;\n" +
                    "var ajaxStartLastIndexTime = 0;\n" +
                    "if( window.performance.getEntriesByName('mark_ajax_start').length >0 ){   \n" +
                    "ajaxStartLength = window.performance.getEntriesByName('mark_ajax_start').length;\n" +
                    "ajaxStartLastIndexTime = window.performance.getEntriesByName('mark_ajax_start')[(ajaxStartLength-1)].startTime;\n"
                    +
                    "}\n" +
                    "// Response time\n" +
                    "ajaxResponse =  ' |Page load Time : ' +( window.performance.timing.loadEventEnd-window.performance.timing.responseEnd ) + ' |Ajax Render Time : ' +(window.performance.getEntriesByName('mark_fully_loaded')[0].startTime - (resources[i].responseEnd)) + ' |Navigation Start : ' + window.performance.timing.navigationStart + ' |Prep Time : ' + (resources[i].requestStart - window.performance.getEntriesByName('mark_ajax_start')[0].startTime     )   +  ' |Fetch Start : ' + window.performance.timing.fetchStart  \n"
                    +
                    " + ' |Server Time : ' +( (resources[i].responseEnd)  - resources[i].requestStart);\n" +
                    "  console.log(   ajaxResponse );\n" +
                    "      window.performance.clearMarks('mark_ajax_start_BLS_focusout');\n" +
                    "  window.performance.clearMarks('mark_ajax_start_BLS_Clicked');\n" +
                    "return ajaxResponse;\n" +
                    "}\n" +
                    " }\n" +
                    "if(ajaxResponse !== '') {\n" +
                    "console.log(   ajaxResponse );\n" +
                    "  window.performance.clearMarks('mark_ajax_start_BLS_focusout');\n" +
                    "  window.performance.clearMarks('mark_ajax_start_BLS_Clicked');\n" +
                    "return ajaxResponse ;\n" +
                    "} \n" +
                    "}\n" +
                    " var fullResponseLog = ' |Page load Time : ' + ( window.performance.timing.loadEventEnd-window.performance.timing.responseEnd ) + ' |Ajax Render Time : 0 ' +  ' |Navigation Start : '+ window.performance.timing.navigationStart + ' |Prep Time : ' + (window.performance.timing.requestStart  -  window.performance.timing.navigationStart)    +  ' |Fetch Start : ' + window.performance.timing.fetchStart   + ' |Server Time : ' + (window.performance.timing.responseEnd  - window.performance.timing.requestStart );\n"
                    +
                    "console.log( fullResponseLog );\n" +
                    "   window.performance.clearMarks('mark_ajax_start_BLS_focusout');\n" +
                    "  window.performance.clearMarks('mark_ajax_start_BLS_Clicked');\n" +
                    "return  fullResponseLog;\n"
            );
        }
        return loadTime;
    }
}
