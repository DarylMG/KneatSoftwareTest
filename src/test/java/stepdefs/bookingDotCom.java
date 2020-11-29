package stepdefs;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.sun.javafx.util.Utils.isWindows;


public class bookingDotCom {

    public WebDriver driver;
    public static final String BASE_URL = "https://www.booking.com/searchresults.html?label=gen173nr-1DCAEoggI46AdIM1gEaGmIAQGYATG4ARfIAQzYAQPoAQH4AQKIAgGoAgO4Ap6ij_4FwAIB0gIkNmQ5ZTAzNGYtNmQ1OC00Y2UyLTkwMDgtNjQ3MGI0NmFkYjRi2AIE4AIB&sb=1&sb_lp=1&src=index&src_elem=sb&error_url=https%3A%2F%2Fwww.booking.com%2Findex.html%3Flabel%3Dgen173nr-1DCAEoggI46AdIM1gEaGmIAQGYATG4ARfIAQzYAQPoAQH4AQKIAgGoAgO4Ap6ij_4FwAIB0gIkNmQ5ZTAzNGYtNmQ1OC00Y2UyLTkwMDgtNjQ3MGI0NmFkYjRi2AIE4AIB%3Bsb_price_type%3Dtotal%26%3B&ss=Limerick&is_ski_area=0&ssne=Limerick&ssne_untouched=Limerick&dest_id=-1504189&dest_type=city&checkin_year=&checkin_month=&checkout_year=&checkout_month=&group_adults=2&group_children=0&no_rooms=1&b_h4u_keep_filters=&from_sf=1";
    public static final Integer TIMEOUT = 30;
    public static final Integer POLLING_MILLIS = 2000;

    public void openBrowser() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        maximiseWindow();
    }

    public void navigateToBooking(){
        driver.get(BASE_URL);
        driver.manage().timeouts().implicitlyWait(TIMEOUT, TimeUnit.SECONDS);
    }

    public void maximiseWindow() {
        if(isWindows()) {
            driver.manage().window().maximize();
        } else {
            driver.manage().window().setSize(new Dimension(1920, 1200));
        }
    }


    @Given("^User has accessed Booking\\.com website$")
    public void openBookingWebsite(){
        openBrowser();
        navigateToBooking();
    }

    private final String bookingSearchPageDestinationTextbox = "//*[@id=\"ss\"]";
    private final String bookingSearchPageCheckInDateTextbox = "//*[@id=\"frm\"]/div[3]/div/div[1]/div[1]/div/div";
    private final String bookingSearchPageCheckOutDateTextbox = "//*[@id=\"frm\"]/div[3]/div/div[1]/div[2]/div/div";
    private final String bookingHomePageWhereCheckInCheckOutNextMonthIcon = "//*[@id=\"frm\"]/div[3]/div/div[2]/div/div/div[2]";
    private final String bookingSearchPageSearchButton = "//*[@id=\"frm\"]/div[5]/div[2]";
    private final String bookingSearchPage3StarsCheckbox = "//*[@id=\"filter_class\"]/div[2]/a[1]";
    private final String bookingSearchPage4StarsCheckbox = "//*[@id=\"filter_class\"]/div[2]/a[2]";
    private final String bookingSearchPage5StarsCheckbox = "//*[@id=\"filter_class\"]/div[2]/a[3]";
    private final String bookingSearchPageUnratedCheckbox = "//*[@id=\"filter_class\"]/div[2]/a[4]";
    private final String bookingSearchPageFacilitiesShowMoreOption = "//*[@id=\"filter_facilities\"]/div[2]/button[1]";
    private final String bookingSearchPageFacilitiesSpaCheckbox = "//*[@id=\"filter_facilities\"]/div[2]/a[10]";
    private final String bookingSearchPageFacilitiesFitnessCheckbox = "//*[@id=\"filter_facilities\"]/div[2]/a[8]";
    private final String bookingHomePageWhereCheckInCheckOutPopupCurrentMonthValue = "//*[@id=\"frm\"]/div[3]/div/div[2]/div/div/div[3]/div[1]";


    @And("^I have entered a location of \"([^\"]*)\"$")
    public void enterBookingSearchPageDestinationValue(String location){
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bookingSearchPageDestinationTextbox)));
        driver.findElement(By.xpath(bookingSearchPageDestinationTextbox)).click();
        driver.findElement(By.xpath(bookingSearchPageDestinationTextbox)).clear();
        driver.findElement(By.xpath(bookingSearchPageDestinationTextbox)).sendKeys(location);
    }

    @Given("^I have entered a month of \"([^\"]*)\" and a date of \"([^\"]*)\" and a duration of \"([^\"]*)\" nights stay$")
    public void selectBookingHomePageDateValue(String month, String date, String dateLeaving){
        driver.findElement(By.xpath(bookingSearchPageCheckInDateTextbox)).click();
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bookingHomePageWhereCheckInCheckOutNextMonthIcon)));
        String currentMonth = driver.findElement(By.xpath(bookingHomePageWhereCheckInCheckOutPopupCurrentMonthValue)).getText();
        while(!currentMonth.contains(month)){
            driver.findElement(By.xpath(bookingHomePageWhereCheckInCheckOutNextMonthIcon)).click();
            currentMonth = driver.findElement(By.xpath(bookingHomePageWhereCheckInCheckOutPopupCurrentMonthValue)).getText();
        }
        List<WebElement> dates = driver.findElement(By.xpath(bookingHomePageWhereCheckInCheckOutPopupCurrentMonthValue)).findElements(By.tagName("td"));
        for(WebElement liveDate: dates){
            if (liveDate.getText().contains(date)){
                liveDate.click();
                break;
            }
        }
        driver.findElement(By.xpath(bookingSearchPageCheckOutDateTextbox)).click();
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bookingHomePageWhereCheckInCheckOutNextMonthIcon)));
        int dateLeavingAsInt = Integer.parseInt(dateLeaving);
        int dateAsInt = Integer.parseInt(date);
        if(dateLeavingAsInt<dateAsInt){
            driver.findElement(By.xpath(bookingHomePageWhereCheckInCheckOutNextMonthIcon)).click();
        }
        dates = driver.findElement(By.xpath(bookingHomePageWhereCheckInCheckOutPopupCurrentMonthValue)).findElements(By.tagName("td"));
        for(WebElement liveDate: dates){
            if (liveDate.getText().contains(dateLeaving)){
                liveDate.click();
                break;
            }
        }
    }

    @And("^I click on the Search Button$")
    public void clickBookingSearchPageSearchButton(){
        WebElement element = driver.findElement(By.xpath(bookingSearchPageSearchButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bookingSearchPageSearchButton)));
        driver.findElement(By.xpath(bookingSearchPageSearchButton)).click();
    }

    @And("^I click on the 3 stars checkbox$")
    public void clickBookingSearchPage3StarsCheckbox(){
        WebElement element = driver.findElement(By.xpath(bookingSearchPage3StarsCheckbox));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bookingSearchPage3StarsCheckbox)));
        driver.findElement(By.xpath(bookingSearchPage3StarsCheckbox)).click();}

    @And("^I click on the 4 stars checkbox$")
    public void clickBookingSearchPage4StarsCheckbox(){
        WebElement element = driver.findElement(By.xpath(bookingSearchPage4StarsCheckbox));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bookingSearchPage4StarsCheckbox)));
        driver.findElement(By.xpath(bookingSearchPage4StarsCheckbox)).click();
    }

    @And("^I click on the 5 stars checkbox$")
    public void clickBookingSearchPage5StarsCheckbox(){
        WebElement element = driver.findElement(By.xpath(bookingSearchPage5StarsCheckbox));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bookingSearchPage5StarsCheckbox)));
        driver.findElement(By.xpath(bookingSearchPage5StarsCheckbox)).click();
    }

    @And("^I click on the Unrated checkbox$")
    public void clickBookingSearchPageUnratedCheckbox(){
        WebElement element = driver.findElement(By.xpath(bookingSearchPageUnratedCheckbox));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bookingSearchPageUnratedCheckbox)));
        driver.findElement(By.xpath(bookingSearchPageUnratedCheckbox)).click();
    }

    @And("^I click on the Show More Option on the Facilities Filter Section$")
    public void clickBookingSearchPageFacilitiesShowMoreOption(){
        WebElement element = driver.findElement(By.xpath(bookingSearchPageFacilitiesShowMoreOption));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bookingSearchPageFacilitiesShowMoreOption)));
        driver.findElement(By.xpath(bookingSearchPageFacilitiesShowMoreOption)).click();
    }

    @And("^I click on the Spa checkbox$")
    public void clickBookingSearchPageFacilitiesSpaCheckbox(){driver.findElement(By.xpath(bookingSearchPageFacilitiesSpaCheckbox)).click();}

    @And("^I click on the Fitness Checkbox$")
    public void clickBookingSearchPageFitnessCheckbox(){driver.findElement(By.xpath(bookingSearchPageFacilitiesFitnessCheckbox)).click();}

    @Then("^The search page will be returned displaying \"([^\"]*)\"$")
    public void checkBookingSearchPageReturnedProperty(String propertyName){
        String property = "//*[contains(text(), '"+propertyName+"')]";
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property)));
    }
}
