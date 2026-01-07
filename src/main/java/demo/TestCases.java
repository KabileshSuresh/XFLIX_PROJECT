package demo;
import org.openqa.selenium.Alert;
import java.util.stream.Collectors;
import org.openqa.selenium.support.ui.ExpectedCondition;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.JavascriptExecutor;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;

public class TestCases {
    private static RemoteWebDriver driver;
    private WebDriverWait wait;

    public TestCases() throws MalformedURLException {
        System.out.println("Constructor: TestCases");
        WebDriverManager.chromedriver().timeout(120).setup();
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);

        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 30);
    }

    public void endTest() {
        System.out.println("End Test: TestCases");
        if (driver != null) {
            driver.quit();
        }
    }

    // TestCase01: Verify XFlix Homepage URL
    public void testCase01() {
        System.out.println("Start Test case: testCase01");
        driver.get("https://xflix-qa.vercel.app/");
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("xflix")) {
            System.out.println("Passed: URL contains 'xflix'");
        } else {
            System.out.println("Failed: URL does not contain 'xflix'");
        }
        System.out.println("End Test case: testCase01");
    }

    // TestCase02: Verify Search Functionality
    public void testCase02() {
        System.out.println("Start Test case: testCase02");
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.search-input")));
        searchBox.clear();
        searchBox.sendKeys("frameworks");
        searchBox.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("video-card")));
        List<WebElement> results = driver.findElements(By.className("video-card"));
        System.out.println("Valid keyword - Results found: " + results.size());

        searchBox = driver.findElement(By.cssSelector("input.search-input"));
        searchBox.clear();
        searchBox.sendKeys("selenium");
        searchBox.sendKeys(Keys.ENTER);

        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'No Search is found')]")));
        System.out.println("Invalid keyword - Message displayed: " + message.getText());
        System.out.println("End Test case: testCase02");
    }
    
    public void testCase03() { 
        System.out.println("Start Test case: testCase03");
        driver.get("https://xflix-qa.vercel.app/");
        List<String> beforeSort = new ArrayList<>();

        List<WebElement> keywordContent = driver.findElements(By.xpath("//div/p[1]"));

        for(WebElement elemm : keywordContent)
        {
            String eachTitle = elemm.getText();
            beforeSort.add(eachTitle);
        }

        WebElement clickSort = driver.findElement(By.id("sortBySelect"));
        Select select = new Select(clickSort);
        select.selectByVisibleText("Sort By: View Count");

        List<String> afterSort = new ArrayList<>();

        keywordContent = driver.findElements(By.xpath("//div/p[1]"));

        for(WebElement elemm : keywordContent)
        {
            String eachTitle = elemm.getText();
            beforeSort.add(eachTitle);
        }

        if(!beforeSort.equals(afterSort))
        {
            System.out.println(" Testcase03 : Sorted : PASS ");
        }
        else{
            System.out.println(" FAil ");
        }

        System.out.println("End Test case: testCase03");

        // System.out.println("Start Test case: testCase03");
        // driver.get("https://xflix-qa.vercel.app/");  // Launch XFlix
        
        // WebElement sortByDropdown = driver.findElement(By.id("sortBySelect"));
        // Select sortSelect = new Select(sortByDropdown);
        // sortSelect.selectByValue("viewCount");
        
        // try {
        //     Thread.sleep(2000);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
                
        // JavascriptExecutor js = (JavascriptExecutor) driver;
        // List<Map<String, Object>> sortedVideos = (List<Map<String, Object>>) js.executeScript(
        //     "return window.ss ? window.ss(f => f.results).sort((a, b) => b.viewCount - a.viewCount) : [];"
        // );
        
        // if (sortedVideos.isEmpty()) {
        //     System.out.println("No videos found in the state.");
        // } else {
        //     System.out.println("Videos sorted by View Count:");
        //     for (Map<String, Object> video : sortedVideos) {
        //         System.out.println("Title: " + video.get("title") + " | View Count: " + video.get("viewCount"));
        //     }
        // }
        // System.out.println("End Test case: testCase03");
    }        
    
public void testCase04() throws InterruptedException {
    System.out.println("Start Test case: testCase04");
    driver.get("https://xflix-qa.vercel.app/");

    WebElement uploadBtn = wait.until(ExpectedConditions.elementToBeClickable(
        By.cssSelector("button.btn-upload")));
    System.out.println("VERIFICATION: Upload button found: " + uploadBtn.isDisplayed());
    uploadBtn.click();

    WebElement uploadVideoBtn = wait.until(ExpectedConditions.elementToBeClickable(
        By.cssSelector("button.btn-modal-upload")));
    System.out.println("VERIFICATION: Upload video button found: " + uploadVideoBtn.isDisplayed());

    uploadVideoBtn.click();

    try {
        Alert validationAlert = wait.until(ExpectedConditions.alertIsPresent());
        System.out.println("Error Alert is Displayed");
        System.out.println("VERIFICATION: Validation Alert Text: " + validationAlert.getText());
        validationAlert.accept();
    } catch (org.openqa.selenium.TimeoutException e) {
        System.err.println("ERROR: Validation Alert not present within the timeout.");
    }

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("videoLink")))
        .sendKeys("https://www.youtube.com/embed/dQw4w9WgXcQ");
    System.out.println("VERIFICATION: Video Link entered: https://www.youtube.com/embed/dQw4w9WgXcQ");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("previewImage")))
        .sendKeys("https://img.youtube.com/vi/dQw4w9WgXcQ/0.jpg");
    System.out.println("VERIFICATION: Preview Image entered: https://img.youtube.com/vi/dQw4w9WgXcQ/0.jpg");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title")))
        .sendKeys("Sample Video");
    System.out.println("VERIFICATION: Title entered: Sample Video");
    Select genreDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("genreDropdown"))));
    genreDropdown.selectByValue("Education");
    System.out.println("VERIFICATION: Genre selected: Education");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("releaseDate")))
        .sendKeys("2025-05-15");
    System.out.println("VERIFICATION: Release Date entered: 2025-05-15");
    Select ageDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ageDropdown"))));
    ageDropdown.selectByValue("18+");
    System.out.println("VERIFICATION: Age selected: 18+");

    uploadVideoBtn.click();

    try {
        Alert successAlert = wait.until(ExpectedConditions.alertIsPresent());
        System.out.println("Success Alert is Displayed");
        System.out.println("VERIFICATION: Success Alert Text: " + successAlert.getText());
        successAlert.accept();
        System.out.println("Passed: Video upload success alert appeared.");
        System.out.println("RESULT: Passed - Video upload functionality.");
    } catch (org.openqa.selenium.TimeoutException e) {
        System.err.println("ERROR: Success Alert not present within the timeout.");
        System.out.println("RESULT: Failed - Video upload functionality (Success alert not found).");
    }

    System.out.println("End Test case: testCase04");
}

    // TestCase05: Verify Like State Across Tabs
    public void testCase05() throws InterruptedException {
        System.out.println("Start Test case: testCase05");
        driver.get("https://xflix-qa.vercel.app/");

        WebElement keywordContents = driver.findElement(By.xpath("//div/p[1]"));
        keywordContents.click();

        WebElement likeBtn = driver.findElement(By.xpath("//button[@class='btn btn-like']"));
        String likesBefore = likeBtn.getText();
        likeBtn.click();

        String currentUrl = driver.getCurrentUrl();

        driver.switchTo().newWindow(WindowType.TAB);
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        Set<String> windows = driver.getWindowHandles();
        List<String> windowls = new ArrayList<>(windows);

        driver.switchTo().window(windowls.get(1));
        driver.get(currentUrl);
        likeBtn = driver.findElement(By.xpath("//button[@class='btn btn-like']"));
        String likesAfter = likeBtn.getText();

        if(!likesAfter.equals(likesBefore))
        {
            System.out.println(" Testcase05 : Likes Incremented : PASS ");
        }
        else
        {
            System.out.println(" Testcase05 : FAil ");
        }
        System.out.println("End Test case: testCase05");

        // System.out.println("Start Test case: testCase05");
        // driver.get("https://xflix-qa.vercel.app/");
    
        // wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".video-card")));
    
        // WebElement videoCard = driver.findElement(By.xpath("//p[contains(text(),'Top 10 Facts - Space [Part 5]')]/ancestor::div[@class='video-card']"));
        // ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", videoCard);
        // ((JavascriptExecutor) driver).executeScript("arguments[0].click();", videoCard);
    
        // WebElement videoTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".main-video-title")));
        // String videoTitle = videoTitleElement.getText();
    
        // WebElement likeButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn-like")));
        // int initialLikeCount = Integer.parseInt(likeButton.getText().trim());
    
        // likeButton.click();
    
        // wait.until(ExpectedConditions.textToBePresentInElement(likeButton, String.valueOf(initialLikeCount + 1)));
        // int updatedLikeCount = Integer.parseInt(likeButton.getText().trim());
    
        // String currentURL = driver.getCurrentUrl();
        // ((JavascriptExecutor) driver).executeScript("window.open()");
        // ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
    
        // // Switch to new tab
        // driver.switchTo().window(tabs.get(1));
        // driver.get(currentURL);
    
        // // Step 3: Wait and verify like count in the new tab
        // WebElement newTabLikeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn-like")));
        // int newTabLikeCount = Integer.parseInt(newTabLikeButton.getText().trim());
    
        // if (newTabLikeCount == updatedLikeCount) {
        //     System.out.println("Passed: Like count is preserved across tabs. Like Count: " + newTabLikeCount);
        // } else {
        //     System.out.println("Failed: Like count not preserved in new tab. Expected: " + updatedLikeCount + ", Found: " + newTabLikeCount);
        // }
    
        // System.out.println("End Test case: testCase05");
    
        // driver.close();
        // driver.switchTo().window(tabs.get(0));
    }
    
    
}
