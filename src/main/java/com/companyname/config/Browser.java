package com.companyname.config;

import com.companyname.common.exception.PageDriverException;
import com.companyname.test.pagedriver.PageDriver;
import com.companyname.test.pagedriver.SessionManager;
import com.companyname.config.props.PropertyReader;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.AbstractDriverOptions;

public final class Browser {

    private static final String BROWSER = PropertyReader.getProperty(Properties.TEST_BROWSER);
    private static final String HUB_URL = PropertyReader.getProperty(Properties.WEBDRIVER_HUB_URL);
    private static final Boolean TAKE_SCREENSHOT = PropertyReader.getProperty(Properties.TAKE_SCREENSHOT, false);
    private static final ThreadLocal<ImmutablePair<SessionManager, PageDriver>> SESSION_INSTANCE = new ThreadLocal<>();
    private static AbstractDriverOptions<?> options = null;

    // Locked constructor
    private Browser() {
    }

    /**
     * Returns the singleton instance of PageDriver.
     *
     * @return The PageDriver instance
     * @throws PageDriverException If the browser has not been initialized
     */
    public static PageDriver get() {
        if (isInitialized()) {
            return SESSION_INSTANCE.get().getValue();
        } else {
            throw new PageDriverException("Browser has not been initialized! Please run initialize method first");
        }
    }

    /**
     * Returns the SessionManager instance associated with the current session.
     *
     * @return The SessionManager instance
     * @throws PageDriverException If the session has not been initialized
     */
    public static SessionManager getSession() {
        if (isInitialized()) {
            return SESSION_INSTANCE.get().getKey();
        } else {
            throw new PageDriverException("Session has not been initialized! Please run initialize method first");
        }
    }

    /**
     * Returns the WebDriver instance associated with the current session.
     *
     * @return The WebDriver instance
     */
    public static WebDriver getDriver() {
        return get().getWebDriver();
    }

    /**
     * Opens the specified URL in the browser and maximizes the window.
     *
     * @param url The URL to open
     */
    public static void open(String url) {
        get().open(url);
        maximize();
    }

    /**
     * Closes the current browser session.
     */
    public static void close() {
        if (isInitialized()) {
            SESSION_INSTANCE.get().getKey().stop();
            SESSION_INSTANCE.remove();
        }
    }

    /**
     * Sets the driver options for the browser session.
     *
     * @param capabilities The driver options to set
     */
    public static void setOptions(AbstractDriverOptions<?> capabilities) {
        options = capabilities;
    }

    /**
     * Checks if the browser session is initialized and active.
     *
     * @return true if the session is initialized and active, false otherwise
     */
    public static boolean isInitialized() {
        boolean isActiveSession = false;
        try {
            if (SESSION_INSTANCE.get() != null && SESSION_INSTANCE.get().getKey().getWebDriver() != null) {
                SESSION_INSTANCE.get().getValue().getWebDriver().getWindowHandles();  // If there are no open browser windows, this line throws Exception
                isActiveSession = true;
            }
        } catch (Exception ignore) {
            // Session is not active - ignore
        }
        return isActiveSession;
    }

    /**
     * Checks if screenshot capturing is enabled for the current session.
     *
     * @return true if screenshot capturing is enabled, false otherwise
     */
    public static boolean isScreenshotEnabled() {
        return TAKE_SCREENSHOT;
    }

    /**
     * Initializes a vanilla (non-healed) browser session.
     */
    public static void initializeVanillaDriver() {
        applyOptions();
        SessionManager session = new SessionManager(options, HUB_URL);
        SESSION_INSTANCE.set(new ImmutablePair<>(session, new PageDriver(session.getWebDriver())));
    }

    /**
     * Initializes the browser session with the provided SessionManager instance.
     *
     * @param session The SessionManager instance to use for the browser session
     */
    public static void initialize(SessionManager session) {
        SESSION_INSTANCE.set(new ImmutablePair<>(session, new PageDriver(session.getWebDriver())));
    }

    /**
     * Applies the appropriate driver options based on the specified browser.
     * Sets the system properties for the respective browser driver executables if running locally.
     */
    private static void applyOptions() {
        switch (BROWSER.toLowerCase()) {
            case "iexplore":
                if (DefaultBrowserCapabilities.isLocal() && !PropertyReader.getProperty(Properties.WEBDRIVER_IE).isEmpty()) {
                    System.setProperty("webdriver.ie.driver", PropertyReader.getProperty(Properties.WEBDRIVER_IE));
                }
                options = options == null ? DefaultBrowserCapabilities.ieOptions() : options;
                break;
            case "edge":
                if (DefaultBrowserCapabilities.isLocal() && !PropertyReader.getProperty(Properties.WEBDRIVER_EDGE).isEmpty()) {
                    System.setProperty("webdriver.edge.driver", PropertyReader.getProperty(Properties.WEBDRIVER_EDGE));
                }
                options = options == null ? DefaultBrowserCapabilities.eOptions() : options;
                break;
            case "firefox":
                if (DefaultBrowserCapabilities.isLocal() && !PropertyReader.getProperty(Properties.WEBDRIVER_FF).isEmpty()) {
                    System.setProperty("webdriver.gecko.driver", PropertyReader.getProperty(Properties.WEBDRIVER_FF));
                }
                options = options == null ? DefaultBrowserCapabilities.ffOptions() : options;
                break;
            case "chrome":
                if (DefaultBrowserCapabilities.isLocal() && !PropertyReader.getProperty(Properties.WEBDRIVER_CHROME).isEmpty()) {
                    System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, PropertyReader.getProperty(Properties.WEBDRIVER_CHROME));
                }
                options = options == null ? DefaultBrowserCapabilities.chromeOptions() : options;
                break;
            default:
                throw new RuntimeException("Browser is not specified. Make sure 'test.browser' property is set in property files. Available values 'chrome, firefox, iexplore'.");
        }
    }

    /**
     * Maximizes the browser window if the configuration allows it.
     */
    public static void maximize() {
        if (DefaultBrowserCapabilities.isMaximizeWindow()) {
            get().getWebDriver().manage().window().maximize();
        }
    }
}
