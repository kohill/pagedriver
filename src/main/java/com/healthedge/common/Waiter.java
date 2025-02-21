package com.healthedge.common;

import com.healthedge.common.exception.PageDriverException;
import com.healthedge.config.Browser;
import com.healthedge.config.Properties;
import com.healthedge.config.props.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Waiter {

    public static final long WAIT_TIMEOUT = PropertyReader.getProperty(Properties.TEST_WAIT_TIMEOUT, 0L);
    private static final Logger LOG = LogManager.getLogger(Waiter.class);
    private WebDriver webDriver;
    private static Waiter WAITER_INSTANCE;

    private static final String AJAX_JAVASCRIPT = "try {  if (!(document.readyState === 'complete' && ((typeof jQuery == 'undefined') || (!$('#ajaxLoadingModalBox').is(':visible') && ($('#ajaxStatus').text() != 'on') && (jQuery.active == 0))) && ((typeof jsf == 'undefined') || !(window.wdap = window.wdap || (function() {var a;  jsf.ajax.addOnEvent(function(e) {a = e.status !== 'success';});  return { ajax : function() { return a; } };})()).ajax()))) {return false;}  if (window.angular) {if (!window.qa) {window.qa = {doneRendering: false};} var injector = window.angular.element(document.body).injector();    var $rootScope = injector.get('$rootScope');    var $http = injector.get('$http');    var $timeout = injector.get('$timeout');    if ($rootScope.$$phase === '$apply' || $rootScope.$$phase === '$digest' || $http.pendingRequests.length !== 0) {window.qa.doneRendering = false; return false;} if (!window.qa.doneRendering) {$timeout(function() {window.qa.doneRendering = true;}, 0); return false; }  }  return true;} catch (ex) {  return false;}";

    private Waiter() {
    }

    public Waiter(WebDriver webdriver) {
        this.webDriver = webdriver;
    }

    public static Waiter get() {
        if (WAITER_INSTANCE == null) {
            WAITER_INSTANCE = new Waiter();
        }
        return WAITER_INSTANCE;
    }

    protected WebDriver getWebDriver() {
        if (this.webDriver != null) {
            return this.webDriver;
        } else {
            try {
                return Browser.getDriver();
            } catch (PageDriverException e) {
                throw new PageDriverException("Session has not been started with static browser class, please use proper initialization From Browser.java class or instantiate Waiter class by providing active WebDriver session", e);
            }

        }
    }

    public void waitForAjaxToFinish() {
        LOG.trace("Waiting for AJAX call to finish....");
        try {
            WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(30));
            wait.until((ExpectedCondition<Boolean>) wdriver -> {
                assert wdriver != null;
                return (Boolean) ((JavascriptExecutor) wdriver).executeScript(AJAX_JAVASCRIPT);
            });
        } catch (Exception e) {
            LOG.trace("Page still loading or AJAX call did not complete successfully...");
        }
    }

    /**
     * Waits for the page fade overlay to disappear, indicating that the page has finished loading.
     * This method waits for the presence of an element with the class '[page]-click-overlay [page]-disable-click',
     * and then waits for that element to disappear, indicating that the page has finished loading.
     */
    public void waitForPageFade() {
        doWait(By.xpath("//*[contains(@class, '<<<OVERLAY NAME>>>')]"));
    }

    /**
     * Waits for a React page to finish loading by checking for the presence of specific loading indicators.
     * This method waits for the presence of elements with classes 'MuiCircularProgress', 'create-report-spinner',
     * 'ant-spin ant-spin-spinning', or a video element with a specific style, and then waits for those elements
     * to disappear, indicating that the page has finished loading.
     */
    public void waitForReactPageToLoad() {
        doWait(By.xpath("//*[contains(@class, '') or contains(@class, '') or contains(@class, '')] | //video[contains(@style, '')]"));
    }

    /**
     * Waits for an element to be present and then disappear, indicating that the page has finished loading.
     *
     * @param locator The locator strategy to use to find the element to wait for.
     */
    private void doWait(By locator) {
        try {
            WebDriverWait waitToAppear = new WebDriverWait(getWebDriver(), Duration.ofMillis(800), Duration.ofMillis(100));
            waitToAppear.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            LOG.debug("Waiting element with locator {} was not found", locator);
        }
        WebDriverWait waitToFinish = new WebDriverWait(getWebDriver(), Duration.ofSeconds(120), Duration.ofMillis(100));
        waitToFinish.until(ExpectedConditions.numberOfElementsToBe(locator, 0));
        LOG.debug("Page finished loading");
    }

    /**
     * Waits for an element to be found using a manual polling approach.
     * This method can be used if waitForElementVisible() and waitForElementPresence() are not working.
     *
     * @param locator The locator strategy to use to find the element.
     */
    public void waitForElementToBeFound(By locator) {
        waitForElementToBeFound(locator, WAIT_TIMEOUT, false);
    }

    /**
     * Waits for an element to be found using a manual polling approach with a specified timeout.
     * This method can be used if waitForElementVisible() and waitForElementPresence() are not working.
     *
     * @param locator           The locator strategy to use to find the element.
     * @param explicitTimeoutMs The explicit timeout in milliseconds to wait for the element to be found.
     */
    public void waitForElementToBeFound(By locator, long explicitTimeoutMs) {
        waitForElementToBeFound(locator, explicitTimeoutMs, false);
    }

    /**
     * Waits for an element to be found using a manual polling approach, with an option to throw an exception if not found.
     * This method can be used if waitForElementVisible() and waitForElementPresence() are not working.
     *
     * @param locator        The locator strategy to use to find the element.
     * @param throwException A boolean flag indicating whether to throw an exception if the element is not found.
     */
    public void waitForElementToBeFound(By locator, boolean throwException) {
        waitForElementToBeFound(locator, WAIT_TIMEOUT, throwException);
    }

    /**
     * Waits for an element to be found using a manual polling approach with a specified timeout and an option to throw an exception if not found.
     * This method can be used if waitForElementVisible() and waitForElementPresence() are not working.
     *
     * @param locator        The locator strategy to use to find the element.
     * @param explicitWaitMs The explicit timeout in milliseconds to wait for the element to be found.
     * @param throwException A boolean flag indicating whether to throw an exception if the element is not found.
     */
    public void waitForElementToBeFound(By locator, long explicitWaitMs, boolean throwException) {
        long startTime = System.currentTimeMillis();
        long pollTime = startTime;
        boolean isFound = false;
        while (pollTime - startTime <= explicitWaitMs) {
            try {
                getWebDriver().findElement(locator);
                isFound = true;
                break;
            } catch (NoSuchElementException e) {
                LOG.debug("Did not find element with locator {}, continuing polling...", locator);
            }
            pollTime = System.currentTimeMillis();
        }
        if (throwException && !isFound) {
            throw new PageDriverException(String.format("Could not find element with locator '%s' after %s ms", locator, explicitWaitMs));
        } else {
            LOG.debug("Could not find element with locator '{}' after '{}' ms", locator, explicitWaitMs);
        }
    }


    /**
     * Waits for an element to be visible using the provided By locator.
     * This method uses the default wait timeout and does not throw an exception if the element is not visible.
     *
     * @param by The By locator to find the element
     */
    public void waitForElementVisible(By by) {
        waitForElementVisible(by, WAIT_TIMEOUT, false);
    }

    /**
     * Waits for an element to be visible using the provided By locator.
     * This method uses the default wait timeout and allows specifying whether to throw an exception if the element is not visible.
     *
     * @param by             The By locator to find the element
     * @param throwException A boolean flag indicating whether to throw an exception if the element is not visible
     */
    public void waitForElementVisible(By by, boolean throwException) {
        waitForElementVisible(by, WAIT_TIMEOUT, throwException);
    }

    /**
     * Waits for an element to be visible using the provided By locator and explicit wait time.
     * This method does not throw an exception if the element is not visible.
     *
     * @param by               The By locator to find the element
     * @param explicitWaitTime The explicit wait time in milliseconds
     */
    public void waitForElementVisible(By by, long explicitWaitTime) {
        waitForElementVisible(by, explicitWaitTime, false);
    }

    /**
     * Waits for an element to be visible using the provided By locator, explicit wait time, and option to throw an exception.
     *
     * @param by               The By locator to find the element
     * @param explicitWaitTime The explicit wait time in milliseconds
     * @param throwException   A boolean flag indicating whether to throw an exception if the element is not visible
     */
    public void waitForElementVisible(By by, long explicitWaitTime, boolean throwException) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofMillis(explicitWaitTime));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (TimeoutException e) {
            if (throwException) {
                LOG.debug("Element {} not visible after {} ms", by, explicitWaitTime);
                throw e;
            }
            LOG.debug("Element {} not visible after {} ms, continuing test execution", by, explicitWaitTime);
        }
    }

    /**
     * Waits for a WebElement to be visible.
     * This method uses the default wait timeout and does not throw an exception if the element is not visible.
     *
     * @param ele The WebElement to wait for visibility
     */
    public void waitForElementVisible(WebElement ele) {
        waitForElementVisible(ele, WAIT_TIMEOUT, false);
    }

    /**
     * Waits for a WebElement to be visible.
     * This method uses the default wait timeout and allows specifying whether to throw an exception if the element is not visible.
     *
     * @param ele            The WebElement to wait for visibility
     * @param throwException A boolean flag indicating whether to throw an exception if the element is not visible
     */
    public void waitForElementVisible(WebElement ele, boolean throwException) {
        waitForElementVisible(ele, WAIT_TIMEOUT, throwException);
    }

    /**
     * Waits for a WebElement to be visible using the provided explicit wait time.
     * This method does not throw an exception if the element is not visible.
     *
     * @param ele              The WebElement to wait for visibility
     * @param explicitWaitTime The explicit wait time in milliseconds
     */
    public void waitForElementVisible(WebElement ele, long explicitWaitTime) {
        waitForElementVisible(ele, explicitWaitTime, false);
    }

    /**
     * Waits for a WebElement to be visible using the provided explicit wait time and option to throw an exception.
     *
     * @param ele              The WebElement to wait for visibility
     * @param explicitWaitTime The explicit wait time in milliseconds
     * @param throwException   A boolean flag indicating whether to throw an exception if the element is not visible
     */
    public void waitForElementVisible(WebElement ele, long explicitWaitTime, boolean throwException) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofMillis(explicitWaitTime));
        try {
            wait.until(ExpectedConditions.visibilityOf(ele));
        } catch (TimeoutException e) {
            if (throwException) {
                LOG.debug("Element {} not visible after {} ms", ele, explicitWaitTime);
                throw e;
            }
            LOG.debug("Element {} not visible after {} ms, continuing test execution", ele, explicitWaitTime);
        }
    }

    /**
     * Waits for an element to be invisible using the provided By locator.
     * This method uses the default wait timeout and does not throw an exception if the element is not invisible.
     *
     * @param locator The By locator to find the element
     */
    public void waitForElementInvisible(By locator) {
        waitForElementVisible(locator, WAIT_TIMEOUT, false);
    }

    /**
     * Waits for an element to be invisible using the provided By locator.
     * This method uses the default wait timeout and allows specifying whether to throw an exception if the element is not invisible.
     *
     * @param locator        The By locator to find the element
     * @param throwException A boolean flag indicating whether to throw an exception if the element is not invisible
     */
    public void waitForElementInvisible(By locator, boolean throwException) {
        waitForElementInvisible(locator, WAIT_TIMEOUT, throwException);
    }

    /**
     * Waits for an element to be invisible using the provided By locator and explicit wait time.
     * This method does not throw an exception if the element is not invisible.
     *
     * @param locator          The By locator to find the element
     * @param explicitWaitTime The explicit wait time in milliseconds
     */
    public void waitForElementInvisible(By locator, long explicitWaitTime) {
        waitForElementInvisible(locator, explicitWaitTime, false);
    }

    /**
     * Waits for an element to be invisible using the provided By locator, explicit wait time, and option to throw an exception.
     *
     * @param locator          The By locator to find the element
     * @param explicitWaitTime The explicit wait time in milliseconds
     * @param throwException   A boolean flag indicating whether to throw an exception if the element is not invisible
     */
    public void waitForElementInvisible(By locator, long explicitWaitTime, boolean throwException) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofMillis(explicitWaitTime));
        WebElement ele = getWebDriver().findElement(locator);
        try {
            wait.until(ExpectedConditions.invisibilityOf(ele));
        } catch (TimeoutException e) {
            if (throwException) {
                LOG.debug("Element {} not invisible after {} ms", ele, explicitWaitTime);
                throw e;
            }
            LOG.debug("Element {} not invisible after {} ms, continuing test execution", ele, explicitWaitTime);
        }
    }


    /**
     * Waits for the given WebElement to become invisible on the page.
     * Uses the default wait timeout value.
     * If the element is not invisible after the timeout, it will continue test execution without throwing an exception.
     *
     * @param ele The WebElement to wait for to become invisible
     */
    public void waitForElementInvisible(WebElement ele) {
        waitForElementVisible(ele, WAIT_TIMEOUT, false);
    }

    /**
     * Waits for the given WebElement to become invisible on the page.
     * Uses the default wait timeout value.
     *
     * @param ele            The WebElement to wait for to become invisible
     * @param throwException Whether to throw a TimeoutException if the element is not invisible after the timeout
     */
    public void waitForElementInvisible(WebElement ele, boolean throwException) {
        waitForElementInvisible(ele, WAIT_TIMEOUT, throwException);
    }

    /**
     * Waits for the given WebElement to become invisible on the page.
     *
     * @param ele              The WebElement to wait for to become invisible
     * @param explicitWaitTime The explicit wait time in milliseconds
     */
    public void waitForElementInvisible(WebElement ele, long explicitWaitTime) {
        waitForElementInvisible(ele, explicitWaitTime, false);
    }

    /**
     * Waits for the given WebElement to become invisible on the page.
     *
     * @param ele              The WebElement to wait for to become invisible
     * @param explicitWaitTime The explicit wait time in milliseconds
     * @param throwException   Whether to throw a TimeoutException if the element is not invisible after the timeout
     */
    public void waitForElementInvisible(WebElement ele, long explicitWaitTime, boolean throwException) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofMillis(explicitWaitTime));
        try {
            wait.until(ExpectedConditions.invisibilityOf(ele));
        } catch (TimeoutException e) {
            if (throwException) {
                LOG.debug("Element {} not invisible after {} ms", ele, explicitWaitTime);
                throw e;
            }
            LOG.debug("Element {} not invisible after {} ms, continuing test execution", ele, explicitWaitTime);
        }
    }

    /**
     * Waits for an element to be present on the page using the given By locator.
     * Uses the default wait timeout value.
     * If the element is not present after the timeout, it will continue test execution without throwing an exception.
     *
     * @param by The By locator to find the element
     */
    public void waitForElementPresence(By by) {
        waitForElementPresence(by, WAIT_TIMEOUT, false);
    }

    /**
     * Waits for an element to be present on the page using the given By locator.
     * Uses the default wait timeout value.
     *
     * @param by             The By locator to find the element
     * @param throwException Whether to throw a TimeoutException if the element is not present after the timeout
     */
    public void waitForElementPresence(By by, boolean throwException) {
        waitForElementPresence(by, WAIT_TIMEOUT, throwException);
    }

    /**
     * Waits for an element to be present on the page using the given By locator.
     *
     * @param by               The By locator to find the element
     * @param explicitWaitTime The explicit wait time in milliseconds
     */
    public void waitForElementPresence(By by, long explicitWaitTime) {
        waitForElementPresence(by, explicitWaitTime, false);
    }

    /**
     * Waits for an element to be present on the page using the given By locator.
     *
     * @param by               The By locator to find the element
     * @param explicitWaitTime The explicit wait time in milliseconds
     * @param throwException   Whether to throw a TimeoutException if the element is not present after the timeout
     */
    public void waitForElementPresence(By by, long explicitWaitTime, boolean throwException) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofMillis(explicitWaitTime));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (TimeoutException e) {
            if (throwException) {
                LOG.debug("Element {} not present after {} ms", by, explicitWaitTime);
                throw e;
            }
            LOG.debug("Element {} not present after {} ms, continuing test execution", by, explicitWaitTime);
        }
    }

    /**
     * Waits for an element to be clickable on the page using the given By locator.
     * Uses the default wait timeout value.
     * If the element is not clickable after the timeout, it will continue test execution without throwing an exception.
     *
     * @param by The By locator to find the element
     */
    public void waitForElementClickable(By by) {
        waitForElementClickable(by, WAIT_TIMEOUT, false);
    }

    /**
     * Waits for an element to be clickable on the page using the given By locator.
     * Uses the default wait timeout value.
     *
     * @param by             The By locator to find the element
     * @param throwException Whether to throw a TimeoutException if the element is not clickable after the timeout
     */
    public void waitForElementClickable(By by, boolean throwException) {
        waitForElementClickable(by, WAIT_TIMEOUT, throwException);
    }

    /**
     * Waits for an element to be clickable on the page using the given By locator.
     *
     * @param by               The By locator to find the element
     * @param explicitWaitTime The explicit wait time in milliseconds
     */
    public void waitForElementClickable(By by, long explicitWaitTime) {
        waitForElementClickable(by, explicitWaitTime, false);
    }

    /**
     * Waits for an element to be clickable on the page using the given By locator.
     *
     * @param by               The By locator to find the element
     * @param explicitWaitTime The explicit wait time in milliseconds
     * @param throwException   Whether to throw a TimeoutException if the element is not clickable after the timeout
     */
    public void waitForElementClickable(By by, long explicitWaitTime, boolean throwException) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofMillis(explicitWaitTime));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (TimeoutException e) {
            if (throwException) {
                LOG.debug("Element {} not clickable after {} ms", by, explicitWaitTime);
                throw e;
            }
            LOG.debug("Element {} not clickable after {} ms, continuing test execution", by, explicitWaitTime);
        }
    }

    /**
     * Waits for the given WebElement to be clickable on the page.
     * Uses the default wait timeout value.
     * If the element is not clickable after the timeout, it will continue test execution without throwing an exception.
     *
     * @param ele The WebElement to wait for to become clickable
     */
    public void waitForElementClickable(WebElement ele) {
        waitForElementClickable(ele, WAIT_TIMEOUT, false);
    }

    /**
     * Waits for the given WebElement to be clickable on the page.
     * Uses the default wait timeout value.
     *
     * @param ele            The WebElement to wait for to become clickable
     * @param throwException Whether to throw a TimeoutException if the element is not clickable after the timeout
     */
    public void waitForElementClickable(WebElement ele, boolean throwException) {
        waitForElementClickable(ele, WAIT_TIMEOUT, throwException);
    }

    /**
     * Waits for the given WebElement to be clickable on the page.
     *
     * @param ele              The WebElement to wait for to become clickable
     * @param explicitWaitTime The explicit wait time in milliseconds
     */
    public void waitForElementClickable(WebElement ele, long explicitWaitTime) {
        waitForElementClickable(ele, explicitWaitTime, false);
    }

    /**
     * Waits for the given WebElement to be clickable on the page.
     *
     * @param ele              The WebElement to wait for to become clickable
     * @param explicitWaitTime The explicit wait time in milliseconds
     * @param throwException   Whether to throw a TimeoutException if the element is not clickable after the timeout
     */
    public void waitForElementClickable(WebElement ele, long explicitWaitTime, boolean throwException) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofMillis(explicitWaitTime));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(ele));
        } catch (TimeoutException e) {
            if (throwException) {
                LOG.debug("Element {} not clickable after {} ms", ele, explicitWaitTime);
                throw e;
            }
            LOG.debug("Element {} not clickable after {} ms, continuing test execution", ele, explicitWaitTime);
        }
    }


    /**
     * Waits for the specified element's attribute to contain the given value.
     *
     * @param locator   The locator used to find the element
     * @param attribute The attribute to check
     * @param value     The value to check for in the attribute
     */
    public void waitForAttributeToContain(By locator, String attribute, String value) {
        waitForAttributeToContain(locator, attribute, value, false);
    }

    /**
     * Waits for the specified element's attribute to contain the given value.
     *
     * @param locator        The locator used to find the element
     * @param attribute      The attribute to check
     * @param value          The value to check for in the attribute
     * @param throwException Whether to throw a TimeoutException if the condition is not met
     */
    public void waitForAttributeToContain(By locator, String attribute, String value, boolean throwException) {
        waitForAttributeToContain(locator, attribute, value, WAIT_TIMEOUT, throwException);
    }

    /**
     * Waits for the specified element's attribute to contain the given value.
     *
     * @param locator          The locator used to find the element
     * @param attribute        The attribute to check
     * @param value            The value to check for in the attribute
     * @param explicitWaitTime The maximum time to wait for the condition to be true (in milliseconds)
     */
    public void waitForAttributeToContain(By locator, String attribute, String value, long explicitWaitTime) {
        waitForAttributeToContain(locator, attribute, value, explicitWaitTime, false);
    }

    /**
     * Waits for the specified element's attribute to contain the given value.
     *
     * @param locator          The locator used to find the element
     * @param attribute        The attribute to check
     * @param value            The value to check for in the attribute
     * @param explicitWaitTime The maximum time to wait for the condition to be true (in milliseconds)
     * @param throwException   Whether to throw a TimeoutException if the condition is not met
     */
    public void waitForAttributeToContain(By locator, String attribute, String value, long explicitWaitTime, boolean throwException) {
        waitForAttributeToContain(getWebDriver().findElement(locator), attribute, value, explicitWaitTime, throwException);
    }

    /**
     * Waits for the specified element's attribute to contain the given value.
     *
     * @param ele       The element to check
     * @param attribute The attribute to check
     * @param value     The value to check for in the attribute
     */
    public void waitForAttributeToContain(WebElement ele, String attribute, String value) {
        waitForAttributeToContain(ele, attribute, value, false);
    }

    /**
     * Waits for the specified element's attribute to contain the given value.
     *
     * @param ele            The element to check
     * @param attribute      The attribute to check
     * @param value          The value to check for in the attribute
     * @param throwException Whether to throw a TimeoutException if the condition is not met
     */
    public void waitForAttributeToContain(WebElement ele, String attribute, String value, boolean throwException) {
        waitForAttributeToContain(ele, attribute, value, WAIT_TIMEOUT, throwException);
    }

    /**
     * Waits for the specified element's attribute to contain the given value.
     *
     * @param ele              The element to check
     * @param attribute        The attribute to check
     * @param value            The value to check for in the attribute
     * @param explicitWaitTime The maximum time to wait for the condition to be true (in milliseconds)
     */
    public void waitForAttributeToContain(WebElement ele, String attribute, String value, long explicitWaitTime) {
        waitForAttributeToContain(ele, attribute, value, explicitWaitTime, false);
    }

    /**
     * Waits for the specified element's attribute to contain the given value.
     *
     * @param ele              The element to check
     * @param attribute        The attribute to check
     * @param value            The value to check for in the attribute
     * @param explicitWaitTime The maximum time to wait for the condition to be true (in milliseconds)
     * @param throwException   Whether to throw a TimeoutException if the condition is not met
     */
    public void waitForAttributeToContain(WebElement ele, String attribute, String value, long explicitWaitTime, boolean throwException) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofMillis(explicitWaitTime));
        try {
            wait.until(ExpectedConditions.attributeContains(ele, attribute, value));
        } catch (TimeoutException e) {
            if (throwException) {
                LOG.debug("Element {} does not contain {} in attribute {} after {} ms", ele, value, attribute, explicitWaitTime);
                throw e;
            }
            LOG.debug("Element {} not clickable after {} ms, continuing test execution", ele, explicitWaitTime);
        }
    }

    /**
     * Waits for the specified element's attribute to not contain the given value.
     *
     * @param locator   The locator used to find the element
     * @param attribute The attribute to check
     * @param value     The value to check for in the attribute
     */
    public void waitForAttributeNotToContain(By locator, String attribute, String value) {
        waitForAttributeNotToContain(locator, attribute, value, false);
    }

    /**
     * Waits for the specified element's attribute to not contain the given value.
     *
     * @param locator        The locator used to find the element
     * @param attribute      The attribute to check
     * @param value          The value to check for in the attribute
     * @param throwException Whether to throw a TimeoutException if the condition is not met
     */
    public void waitForAttributeNotToContain(By locator, String attribute, String value, boolean throwException) {
        waitForAttributeNotToContain(locator, attribute, value, WAIT_TIMEOUT, throwException);
    }

    /**
     * Waits for the specified element's attribute to not contain the given value.
     *
     * @param locator          The locator used to find the element
     * @param attribute        The attribute to check
     * @param value            The value to check for in the attribute
     * @param explicitWaitTime The maximum time to wait for the condition to be true (in milliseconds)
     */
    public void waitForAttributeNotToContain(By locator, String attribute, String value, long explicitWaitTime) {
        waitForAttributeNotToContain(locator, attribute, value, explicitWaitTime, false);
    }

    /**
     * Waits for the specified element's attribute to not contain the given value.
     *
     * @param locator          The locator used to find the element
     * @param attribute        The attribute to check
     * @param value            The value to check for in the attribute
     * @param explicitWaitTime The maximum time to wait for the condition to be true (in milliseconds)
     * @param throwException   Whether to throw a TimeoutException if the condition is not met
     */
    public void waitForAttributeNotToContain(By locator, String attribute, String value, long explicitWaitTime, boolean throwException) {
        waitForAttributeNotToContain(getWebDriver().findElement(locator), attribute, value, explicitWaitTime, throwException);
    }

    /**
     * Waits for the specified element's attribute to not contain the given value.
     *
     * @param ele       The element to check
     * @param attribute The attribute to check
     * @param value     The value to check for in the attribute
     */
    public void waitForAttributeNotToContain(WebElement ele, String attribute, String value) {
        waitForAttributeNotToContain(ele, attribute, value, false);
    }

    /**
     * Waits for the specified element's attribute to not contain the given value.
     *
     * @param ele            The element to check
     * @param attribute      The attribute to check
     * @param value          The value to check for in the attribute
     * @param throwException Whether to throw a TimeoutException if the condition is not met
     */
    public void waitForAttributeNotToContain(WebElement ele, String attribute, String value, boolean throwException) {
        waitForAttributeNotToContain(ele, attribute, value, WAIT_TIMEOUT, throwException);
    }

    /**
     * Waits for the specified element's attribute to not contain the given value.
     *
     * @param ele              The element to check
     * @param attribute        The attribute to check
     * @param value            The value to check for in the attribute
     * @param explicitWaitTime The maximum time to wait for the condition to be true (in milliseconds)
     */
    public void waitForAttributeNotToContain(WebElement ele, String attribute, String value, long explicitWaitTime) {
        waitForAttributeNotToContain(ele, attribute, value, explicitWaitTime, false);
    }

    /**
     * Waits for the specified element's attribute to not contain the given value.
     *
     * @param ele              The element to check
     * @param attribute        The attribute to check
     * @param value            The value to check for in the attribute
     * @param explicitWaitTime The maximum time to wait for the condition to be true (in milliseconds)
     * @param throwException   Whether to throw a TimeoutException if the condition is not met
     */
    public void waitForAttributeNotToContain(WebElement ele, String attribute, String value, long explicitWaitTime, boolean throwException) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofMillis(explicitWaitTime));
        try {
            wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(ele, attribute, value)));
        } catch (TimeoutException e) {
            if (throwException) {
                LOG.debug("Element {} does not contain {} in attribute {} after {} ms", ele, value, attribute, explicitWaitTime);
                throw e;
            }
            LOG.debug("Element {} not clickable after {} ms, continuing test execution", ele, explicitWaitTime);
        }
    }

    /**
     * Sleeps for the specified number of milliseconds.
     *
     * @param ms The number of milliseconds to sleep
     */
    public void sleep(int ms) {
        sleep((long) ms);
    }

    /**
     * Sleeps for the specified number of milliseconds.
     *
     * @param ms The number of milliseconds to sleep
     */
    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException var2) {
            LOG.debug("Thread interrupted while sleeping");
        }
    }


}
