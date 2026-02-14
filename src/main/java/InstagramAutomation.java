import java.io.FileReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InstagramAutomation {

    static String USERNAME = "Your username";   // Enter Instagram username
    static String PASSWORD = "Your password";   // Enter Instagram password

    // HUMAN TYPING
    public static void humanTyping(WebElement element, String text) throws InterruptedException {
        element.clear();
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            Thread.sleep(120);
        }
    }

    public static void main(String[] args) {

        // It Will Disable Selenium CDP warnings
        Logger.getLogger("org.openqa.selenium.devtools").setLevel(Level.OFF);
        Logger.getLogger("org.openqa.selenium.chromium").setLevel(Level.OFF);

        List<String> pendingUsers = new ArrayList<>();
        WebDriver driver = null;

        try {
            // It Will Read JSON ( Pending Follow Requests) 
            String jsonPath = "D:\\Saurabh Singh\\GitHub-Projects\\Instagram-Automation\\data\\pending_follow_requests.json"; // Pleasse update the path with where your pending_follow_requests.json existed

            JSONObject json = new JSONObject(new org.json.JSONTokener(new FileReader(jsonPath)));
            JSONArray arr = json.getJSONArray("relationships_follow_requests_sent");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String url = obj
                        .getJSONArray("string_list_data")
                        .getJSONObject(0)
                        .getString("href");
                pendingUsers.add(url);
            }

            System.out.println("Total pending follow requests found: " + pendingUsers.size());

            driver = new ChromeDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            driver.get("https://www.instagram.com/accounts/login/");
            Thread.sleep(4000);

            
            if (!driver.findElements(By.name("username")).isEmpty()) {

                System.out.println("OLD login UI detected");

                humanTyping(driver.findElement(By.name("username")), USERNAME);
                humanTyping(driver.findElement(By.name("password")), PASSWORD);
                driver.findElement(By.xpath("//button[@type='submit']")).click();

            } else {

                System.out.println("NEW login UI detected");

                WebElement username = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//input[@autocomplete='username' or @type='text']")
                        )
                );

                WebElement password = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//input[@autocomplete='current-password' or @type='password']")
                        )
                );

                humanTyping(username, USERNAME);
                humanTyping(password, PASSWORD);

                WebElement loginBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.xpath("//div[@role='button']//span[text()='Log in']/.. | //button[@type='submit']")
                        )
                );
                loginBtn.click();
            }

            Thread.sleep(6000);


            String[] buttons = {"Save Info", "Not Now", "Not now", "Save info"};
            for (String text : buttons) {
                try {
                    WebElement btn = new WebDriverWait(driver, Duration.ofSeconds(1))
                            .until(ExpectedConditions.elementToBeClickable(
                                    By.xpath("//button[contains(text(),'" + text + "')]")
                            ));
                    btn.click();
                    break;
                } catch (Exception ignored) {}
            }

          
            int count = 1;

            for (String profile : pendingUsers) {
                driver.get(profile);
                Thread.sleep(4000);

                try {
                    WebElement requestedBtn = wait.until(
                            ExpectedConditions.elementToBeClickable(
                                    By.xpath("//*[text()='Requested' or contains(text(),'Requested')]")
                            )
                    );

                    ((JavascriptExecutor) driver)
                            .executeScript("arguments[0].scrollIntoView(true);", requestedBtn);

                    Thread.sleep(1000);
                    requestedBtn.click();

                    System.out.println("[" + count + "/" + pendingUsers.size() + "] Clicked Requested: " + profile);

                    WebElement unfollow = wait.until(
                            ExpectedConditions.elementToBeClickable(
                                    By.xpath("//button[contains(text(),'Unfollow')]")
                            )
                    );
                    Thread.sleep(1000);
                    unfollow.click();

                    System.out.println("[" + count + "/" + pendingUsers.size() + "] Unfollowed: " + profile);

                } catch (Exception e) {
                    System.out.println("[" + count + "/" + pendingUsers.size() + "] Failed for: " + profile);
                }

                count++;
                Thread.sleep(2000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
            System.out.println("Automation finished");
        }
    }
}
