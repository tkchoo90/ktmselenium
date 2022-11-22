package org.example;

import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static String origin;
    static String date;
    static String month;
    static String year;
    static String startTime;
    static String endTime;
    static String loginUrl;
    static String chromeDriverPath;

    static Client client;

    static List<String> availability = new ArrayList<>();

    static String ktmUrl = "https://shuttleonline.ktmb.com.my/Home/Shuttle";

    static WebDriver driver;

    static WebTarget baseTarget;
    static String telegramToken;

    static String chatId;

    public static void main(String[] args) throws IOException {

        chromeDriverPath = args[0];
        telegramToken = args[1];
        chatId = args[2];

        baseTarget = initTelegramBot();


        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        driver = new ChromeDriver();
        driver.get(ktmUrl);

//         origin = getOrigin();
//         date = getDate();
//         month = getMonth();
//         year = getYear();
//         startTime = getStartTime();
//         endTime = getEndTime();
        origin = "WOODLANDS CIQ";
        date = "22";
        month = "0";
        year = "2023";
        startTime = "1700";
        endTime = "2000";

        confirmEntry();

        checkStartLocation(origin, driver);
        pickDate(date, month, year, driver);

        // Submit
        driver.findElement(By.id("btnSubmit")).submit();
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[1]/div/div[2]/div[1]/div[3]/div/table/tbody/tr[1]/td[7]")));

        int countOfAvailableService = 0;

        do {
            availability.clear();
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                // Do nothing
            }
            driver.navigate().refresh();
            try {
                Thread.sleep(5000);
            } catch (Exception e) {

            }
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[1]/div/div[2]/div[1]/div[3]/div/table/tbody/tr[1]/td[7]")));


            int numOfTiming = driver.findElements(By.xpath("//div[@class='table-scroll']/table/tbody/tr")).size();
            for (int i = 1; i <= numOfTiming; i++) {
                String rowXpath = String.format("//div[@class='table-scroll']/table/tbody/tr[%s]/", i);
                String service = driver.findElement(By.xpath(rowXpath + "td[1]")).getText(); // name
                String timing = driver.findElement(By.xpath(rowXpath + "td[2]")).getText(); // timing
                String seats = driver.findElement(By.xpath(rowXpath + "td[5]")).getText(); // seats

                int parsedTiming = Integer.parseInt(StringUtils.replace(timing, ":", ""));
                if (parsedTiming > Integer.parseInt(startTime) && parsedTiming < Integer.parseInt(endTime)) {
                    if (Integer.parseInt(seats) != 0) {
                        String summary = service + ", Timing: " + timing + ", Seats:" + seats;
                        availability.add(summary);
                        countOfAvailableService++;
                    }
                }
            }
        } while (countOfAvailableService == 0);

        loginUrl = getLoginLink();

        System.out.println(loginUrl);
        for (String x : availability) {
            System.out.println(x);
        }
        sendMsg(loginUrl, availability);


    }

    private static void checkStartLocation(String origin, WebDriver driver) {
        String value = driver.findElement(By.id("FromStationId")).getAttribute("value");
        if (!StringUtils.equalsIgnoreCase(origin, value)) {
            WebElement swap = driver.findElement(By.xpath("/html/body/div[2]/div/form/div/div[1]/i"));
            swap.click();
        }
    }

    private static void pickDate(String day, String month, String year, WebDriver driver) {
        driver.findElement(By.xpath("//*[@id=\"OnwardDate\"]")).click();

        // Click until correct year
        String yearShown;
        do {
            Select select = new Select(driver.findElement(By.xpath("/html/body/section/div/div[1]/section/header/div[1]/select[2]")));
            WebElement option = select.getFirstSelectedOption();
            yearShown = option.getText();
            if (StringUtils.equalsIgnoreCase(yearShown, year)) {
                break;
            } else {
                driver.findElement(By.xpath("/html/body/section/div/div[1]/section/header/div[2]/button[2]")).click();
            }
        } while (true);

        // Click until correct month
        String monthShown;
        do {
            monthShown = driver.findElement(By.xpath("/html/body/section/div/div[1]/section/header/div[1]/select[1]")).getAttribute("value");
            if (StringUtils.equalsIgnoreCase(monthShown, month)) {
                break;
            } else {
                driver.findElement(By.xpath("/html/body/section/div/div[1]/section/header/div[2]/button[2]")).click();
            }
        } while (true);

        // Select correct day
        List<WebElement> days = driver.findElements(By.cssSelector(".lightpick__day.is-available:not(.is-previous-month):not(.is-next-month)"));
        for (WebElement i : days) {
            if (StringUtils.equalsIgnoreCase(i.getText(), day)) {
                i.click();
                break;
            }
        }

        // Click on button "I am travelling one way"
        driver.findElement(By.xpath("/html/body/section/div/a[2]")).click();
    }

    private static String getLoginLink() {
        driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div[2]/div[1]/div[3]/div/table/tbody/tr[1]/td[7]")).click();
        WebElement res = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/form/div[2]/div/div[2]/div[3]/div[3]/button")));

        return driver.getCurrentUrl();
    }

    private static void confirmEntry() throws IOException {
        String m = null;
        switch (month) {
            case "0":
                m = "January";
                break;
            case "1":
                m = "February";
                break;
            case "2":
                m = "March";
                break;
            case "3":
                m = "April";
                break;
            case "4":
                m = "May";
                break;
            case "5":
                m = "June";
                break;
            case "6":
                m = "July";
                break;
            case "7":
                m = "August";
                break;
            case "8":
                m = "September";
                break;
            case "9":
                m = "October";
                break;
            case "10":
                m = "November";
                break;
            case "11":
                m = "December";
        }
        System.out.println("Entry: " + date + m + year);
        System.out.println("Confirm? (y/n)");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String entry = br.readLine();
        if (entry.equalsIgnoreCase("n")) {
            System.exit(1);
        }
    }

    private static WebTarget initTelegramBot() {
        client = ClientBuilder.newClient();
        return client.target("https://api.telegram.org/bot{telegramToken}").resolveTemplate("telegramToken",  telegramToken);
    }

    private static void sendMsg(String url, List<String> availability) {
        StringBuilder sb = new StringBuilder();
        for(String s : availability) {
            sb.append(s);
            sb.append("\n");
        }

        String msg = url + sb;
        Response response = baseTarget.path("sendMessage")
                .queryParam("chat_id", chatId)
                .queryParam("text", msg)
                .request()
                .get();
        JsonObject json = response.readEntity(JsonObject.class);
        boolean ok = json.getBoolean("ok", false);
        if(!ok) {
            System.out.println("Can't send to telegram bot");
        }
    }

}