package Contlo.Assignment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class test {

    public static void main(String[] args) {
        // Set the path to your ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\arkad\\eclipse-workspace\\chrome\\chromedriver-win64\\chromedriver.exe");

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();

        try {
            // Open Amazon.in
            driver.get("https://www.amazon.in");

            // Maximize the browser window
            driver.manage().window().maximize();

            // Implicit wait
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // Search for "LG soundbar"
            WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
            searchBox.sendKeys("LG soundbar");
            WebElement searchButton = driver.findElement(By.id("nav-search-submit-button"));
            searchButton.click();

            // Create a Map to hold product names and prices
            Map<Integer, String> productPriceMap = new HashMap<>();

            // Find all product elements on the page
            List<WebElement> products = driver.findElements(By.cssSelector("div.s-main-slot div.s-result-item"));

            // Loop through the products and get the product names and prices
            for (WebElement product : products) {
                try {
                    // Get the product name
                    WebElement nameElement = product.findElement(By.cssSelector("span.a-size-medium.a-color-base.a-text-normal"));
                    String productName = nameElement.getText();

                    // Get the product price (handle missing price by assigning it zero)
                    int price;
                    try {
                        WebElement priceElement = product.findElement(By.cssSelector("span.a-price-whole"));
                        price = Integer.parseInt(priceElement.getText().replace(",", ""));
                    } catch (Exception e) {
                        price = 0; // If no price is found, set it to zero
                    }

                    // Add the product and price to the map
                    productPriceMap.put(price, productName);

                } catch (Exception e) {
                    // Skip if the product name or price is not found
                    continue;
                }
            }

            // Sort the map by price
            List<Map.Entry<Integer, String>> sortedProducts = new ArrayList<>(productPriceMap.entrySet());
            sortedProducts.sort(Map.Entry.comparingByKey());

            // Write the sorted product names and prices to a report file
            try (FileWriter writer = new FileWriter("test-report.txt")) {
                for (Map.Entry<Integer, String> entry : sortedProducts) {
                    writer.write(entry.getKey() + " " + entry.getValue() + "\n");
                }
                System.out.println("Test report generated successfully: test-report.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } finally {
            // Close the browser
            driver.quit();
        }
    }
}
