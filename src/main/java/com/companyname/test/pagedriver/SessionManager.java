package com.companyname.test.pagedriver;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class SessionManager {
    public final static Logger LOG = LogManager.getLogger(SessionManager.class);
    public static final String BROWSER_IE = "internet explorer";
    public static final String BROWSER_EDGE = "MicrosoftEdge";
    public static final String BROWSER_FIREFOX = "firefox";
    public static final String BROWSER_CHROME = "chrome";

    public WebDriver webDriver;

    /**
     * Constructs a new SessionManager instance with the given AbstractDriverOptions.
     * This constructor will create a local WebDriver session based on the browser specified in the options.
     *
     * @param options The AbstractDriverOptions containing the desired browser configuration
     */
    public SessionManager(AbstractDriverOptions<?> options) {
        this(options, null);
    }

    /**
     * Constructs a new SessionManager instance with the given AbstractDriverOptions and grid URL.
     * If the gridUrl is null or blank, it will create a local WebDriver session based on the browser specified in the options.
     * If the gridUrl is provided, it will create a remote WebDriver session on the specified grid.
     *
     * @param options The AbstractDriverOptions containing the desired browser configuration
     * @param gridUrl The URL of the remote grid to create the WebDriver session on, or null/blank for a local session
     */
    public SessionManager(AbstractDriverOptions<?> options, String gridUrl) {

        if (StringUtils.isBlank(gridUrl)) {
            LOG.info("Starting Local Session: {}", options.getBrowserName());
            switch (options.getBrowserName()) {
                case BROWSER_FIREFOX:
                    webDriver = new FirefoxDriver((FirefoxOptions) options);
                    break;
                case BROWSER_IE:
                    webDriver = new InternetExplorerDriver((InternetExplorerOptions) options);
                    break;
                case BROWSER_EDGE:
                    webDriver = new EdgeDriver((EdgeOptions) options);
                    break;
                case BROWSER_CHROME:
                    webDriver = new ChromeDriver((ChromeOptions) options);
                    break;
                default:
                    throw new RuntimeException("Unknown browser type found!");
            }
            LOG.info("Local Session Initialized: {}", options.getBrowserName());
        } else {

            LOG.info("Starting Remote Session: {} @ {}", options.getBrowserName(), gridUrl);
            try {
                webDriver = new RemoteWebDriver(new URL(gridUrl), options);
                LOG.info("Remote Session Initialized: {} @ {}", options.getBrowserName(), gridUrl);

            } catch (MalformedURLException e) {
                LOG.error("Error starting RemoteWebDriver: {}", Arrays.toString(e.getStackTrace()));
            }
        }
    }

    /**
     * Returns the WebDriver instance.
     *
     * @return The WebDriver instance
     */
    public WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * Returns the session ID of the WebDriver instance if it is a RemoteWebDriver.
     *
     * @return The session ID as a string, or null if the WebDriver is not a RemoteWebDriver
     */
    public String getSessionId() {
        if (getWebDriver() instanceof RemoteWebDriver) {
            SessionId sid = ((RemoteWebDriver) getWebDriver()).getSessionId();
            return sid == null ? null : sid.toString();
        } else {
            return null;
        }
    }

    /**
     * Stops the WebDriver session.
     * Closes the browser window, quits the WebDriver instance, and performs any necessary cleanup.
     * If running on SauceLabs and SET_SUCCESS_ON_CLOSURE is true, it updates the job status to passed.
     */
    public void stop() {
        if (null != webDriver) {
            try {
                LOG.info("Closing browser session");
                webDriver.quit();
                LOG.info("Browser session closed and destroyed");
            } catch (Exception e) {
                LOG.error("*** Error closing Session *** ", e);
            } finally {
                webDriver = null;
            }
        }
    }
}
