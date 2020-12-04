package StepDefinitions;

import com.vimalselvam.cucumber.listener.Reporter;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import static StepDefinitions.WebConnector.driver;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Steps {
    WebConnector wc = new WebConnector();

    @Given("^I navigate to Library System home page in \"([^\"]*)\" browser$")
    public void iNavigateToLibrarySystemHomePageInBrowser(String browser) throws Throwable {
        wc.setUpDriver(browser);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://node-library-service.herokuapp.com");
        wc.waitForCondition("Pageload","",30);
        Reporter.addStepLog("Successfully navigated to Library System home page");
        getscreenshot();
        Reporter.addScreenCaptureFromPath(destinationPath.toString());
    }

    @When("^I login to Library Systems as \"([^\"]*)\"$")
    public void iLoginToLibrarySystemsAs(String user) throws Exception {
        String username = null, password = null;
        if (user.equalsIgnoreCase("Admin creds")){
            username = "admin";
            password = "123";
        } else if (user.equalsIgnoreCase("User creds")){
            username = "testuser123";
            password = "123";
        }
        Thread.sleep(3000L);
        wc.PerformActionOnElement("xpath,//input[@name=\"username\"]","Type",username);
        wc.PerformActionOnElement("xpath,//input[@name=\"password\"]","Type",password);
        wc.PerformActionOnElement("xpath,//button[text()='Login']","Click","");
        Reporter.addStepLog("Provided value for Username and Password field and clicked Login button");
        getscreenshot();
        Reporter.addScreenCaptureFromPath(destinationPath.toString());
    }

    @Then("^I verify that user is \"([^\"]*)\"$")
    public void iVerifyThatUserIs(String result) throws Exception {
        if (result.equalsIgnoreCase("successfully logged in")){
            wc.waitForCondition("PageLoad","",10);
            String currentURL = driver.getCurrentUrl();
            Assert.assertNotEquals(currentURL,"https://node-library-service.herokuapp.com/");
            System.out.println("Login attempt Successful");
            getscreenshot();
            Reporter.addScreenCaptureFromPath(destinationPath.toString());
        }else if (result.equalsIgnoreCase("not logged in")){
            String currentURL = driver.getCurrentUrl();
            Assert.assertEquals(currentURL,"https://node-library-service.herokuapp.com/");
            System.out.println("Login attempt not successful");
            Reporter.addStepLog("Login attempt not successful!!! Check your credentials");
            getscreenshot();
            Reporter.addScreenCaptureFromPath(destinationPath.toString());
        }
    }

    @And("^I close the automation browser$")
    public void iCloseTheAutomationBrowser() throws Exception{
        driver.close();
    }

    public static File destinationPath;

    public void getscreenshot() throws IOException
    {
//        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
        Screenshot screenshot = new AShot().coordsProvider(new WebDriverCoordsProvider()).takeScreenshot(driver);
        destinationPath = new File(System.getProperty("user.dir") + "/target/cucumber-reports/screenshot"+System.currentTimeMillis()+".jpg");
        ImageIO.write(screenshot.getImage(), "jpg", destinationPath);
    }

    @And("^I Logout of the application$")
    public void iLogoutOfTheApplication() throws Exception{
        wc.PerformActionOnElement("xpath,//button[@type='logout']","Click","");
        getscreenshot();
    }

    @And("^I click \"([^\"]*)\" button$")
    public void iClickButton(String button) throws Exception {
        if (wc.FindAnElement("xpath,//a[.//text()='"+button+"']").isDisplayed()) {
            wc.PerformActionOnElement("xpath,//a[.//text()='" + button + "']", "Click", "");
        }else if (wc.FindAnElement("xpath,//input[@value='"+button+"']").isDisplayed()) {
            wc.PerformActionOnElement("xpath,//input[@value='" + button + "']", "Click", "");
        }
        getscreenshot();
    }

    @And("^I click on \"([^\"]*)\" tab$")
    public void iClickOnTab(String tab) throws Exception {
       this.iClickButton(tab);
    }

    @Then("^I validate \"([^\"]*)\" error message is present$")
    public void iValidateErrorMessageIsPresent(String err) throws Exception {
        Assert.assertTrue(wc.FindAnElement("xpath,//*[.//text()='"+err+"']").isDisplayed());
        getscreenshot();
    }

    public static String Bookname;
    @When("^I provide values for new book record$")
    public void iProvideValuesForNewBookRecord() throws Exception{
        Bookname = "TestBook"+System.currentTimeMillis();
        wc.PerformActionOnElement("xpath,//input[@name=\"title\"]","Type",Bookname);
        wc.PerformActionOnElement("xpath,//input[@name=\"author\"]","Type","TestAuthor");
        wc.PerformActionOnElement("xpath,//input[@name=\"stock\"]","Type","10");
        getscreenshot();
    }

    @Then("^I verify that record is successfully created$")
    public void iVerifyThatRecordIsSuccessfullyCreated() throws Exception{
        Assert.assertTrue(wc.FindAnElement("xpath,//*[.//text()='"+Bookname+"']").isDisplayed());
        getscreenshot();
    }

    @When("^I click \"([^\"]*)\" button for above created book record$")
    public void iClickButtonForAboveCreatedBookRecord(String button) throws Exception {
        wc.PerformActionOnElement("xpath,//td[text()='"+Bookname+"']/following-sibling::td/a[i[@data-original-title='"+button+"']]", "Click", "");
        getscreenshot();
    }

    @Then("^I verify that \"([^\"]*)\" options are available$")
    public void iVerifyThatOptionsAreAvailable(List<String> options) throws Exception {
        for (String option:options){
            Assert.assertTrue(wc.FindAnElement("xpath,//label[text()='"+option+"']/preceding-sibling::input[@type=\"checkbox\"]").isDisplayed());
        }
        getscreenshot();
    }

    @Then("^I verify that record is successfully deleted$")
    public void iVerifyThatRecordIsSuccessfullyDeleted() throws Exception{
        Assert.assertFalse(wc.FindAnElement("xpath,//*[.//text()='"+Bookname+"']").isDisplayed());
        getscreenshot();
    }
}

