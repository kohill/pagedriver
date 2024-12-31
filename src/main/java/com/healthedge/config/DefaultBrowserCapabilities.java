package com.healthedge.config;

import com.healthedge.config.Properties;
import com.healthedge.config.props.PropertyReader;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DefaultBrowserCapabilities {

    private static final String MONITOR_NUMBER = PropertyReader.getProperty(Properties.BROWSER_MONITOR_NUMBER);
    private static final String WINDOW_WIDTH = PropertyReader.getProperty(Properties.BROWSER_WINDOW_WIDTH);
    private static final String WINDOW_HEIGHT = PropertyReader.getProperty(Properties.BROWSER_WINDOW_HEIGHT);
    private static final boolean HAS_CUSTOM_WINDOW_SIZE = !WINDOW_WIDTH.isEmpty() && !WINDOW_HEIGHT.isEmpty();
    private static final String HUB_URL = PropertyReader.getProperty(Properties.WEBDRIVER_HUB_URL);
    private static final Boolean DISABLE_WEB_SECURITY = PropertyReader.getProperty(Properties.BROWSER_DISABLE_WEB_SECURITY, true);
    private static final Boolean IS_HEADLESS = PropertyReader.getProperty(Properties.WEBDRIVER_HEADLESS, false);
    private static boolean maximizeWindow = true;

    public DefaultBrowserCapabilities() {
    }

    /**
     * Checks if the test execution is running locally or on a remote grid.
     *
     * @return true if running locally, false if running on a remote grid
     */
    public static Boolean isLocal() {
        return StringUtils.isBlank(HUB_URL);
    }

    /**
     * Configures and returns the InternetExplorerOptions for running tests on Internet Explorer.
     *
     * @return InternetExplorerOptions with desired capabilities set
     */
    public static InternetExplorerOptions ieOptions() {
        InternetExplorerOptions ieOptions = new InternetExplorerOptions();
        ieOptions.setCapability("unexpectedAlertBehaviour", UnexpectedAlertBehaviour.IGNORE);
        ieOptions.setCapability("enableVNC", true);
        ieOptions.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
        ieOptions.setCapability("javascriptEnabled", true);
        ieOptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);

        ieOptions.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        ieOptions.setCapability(InternetExplorerDriver.IE_USE_PER_PROCESS_PROXY, true);
        ieOptions.takeFullPageScreenshot();
        return ieOptions;
    }

    /**
     * Configures and returns the EdgeOptions for running tests on Edge.
     *
     * @return EdgeOptions with desired capabilities set
     */
    public static EdgeOptions eOptions() {
        EdgeOptions eOptions = new EdgeOptions();
        eOptions.addArguments("disable-infobars", "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu", "--log-level=3", "--ignore-certificate-errors");
        eOptions.addArguments("--remote-allow-origins=*");
        eOptions.setCapability("selenoid:options", Map.<String, Object>of("enableVNC", true, "enableVideo", true));
        eOptions.addArguments("--remote-allow-origins=*");
        return eOptions;
    }

    /**
     * Configures and returns the FirefoxOptions for running tests on Firefox.
     *
     * @return FirefoxOptions with desired capabilities set
     */
    public static FirefoxOptions ffOptions() {
        FirefoxOptions ffOptions = new FirefoxOptions();
        ffOptions.setCapability("unexpectedAlertBehaviour", UnexpectedAlertBehaviour.IGNORE);
        ffOptions.setCapability("enableVNC", true);
        return ffOptions;
    }

    /**
     * Configures and returns the ChromeOptions for running tests on Chrome.
     *
     * @return ChromeOptions with desired capabilities set
     */
    public static ChromeOptions chromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
        chromeOptions.addArguments("disable-infobars", "disable-renderer-backgrounding", "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu", "--ignore-certificate-errors", "--remote-allow-origins=*");
        if (DISABLE_WEB_SECURITY) {
            chromeOptions.addArguments("--disable-web-security");
        }
        // Custom local browser options
        if (isLocal() && !MONITOR_NUMBER.isEmpty()) {
            GraphicsDevice gd;
            GraphicsDevice[] graphicsDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
            gd = graphicsDevices[Integer.parseInt(MONITOR_NUMBER) - 1];
            Rectangle currentGd = gd.getDefaultConfiguration().getBounds();
            chromeOptions.addArguments(String.format("--window-position=%s,%s", currentGd.x, currentGd.y));
            if (HAS_CUSTOM_WINDOW_SIZE) {
                chromeOptions.addArguments(String.format("--window-size=%s,%s", WINDOW_WIDTH, WINDOW_HEIGHT));
                maximizeWindow = false;
            }
        }

        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("safebrowsing.enabled", true);
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.prompt_for_download", "false");
        chromePrefs.put("plugins.always_open_pdf_externally", true);
        chromePrefs.put("useAutomationExtension", false);
        chromeOptions.setExperimentalOption("prefs", chromePrefs);
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);

        if (IS_HEADLESS) {
            chromeOptions.addArguments("--headless=new");
        }
        return chromeOptions;
    }

    /**
     * Checks if the browser window should be maximized during test execution.
     *
     * @return true if the browser window should be maximized, false otherwise
     */
    public static Boolean isMaximizeWindow() {
        return maximizeWindow;
    }
}
