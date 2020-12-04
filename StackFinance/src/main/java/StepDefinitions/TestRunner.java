package StepDefinitions;

import com.vimalselvam.cucumber.listener.Reporter;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/java/cucumber/"},
        glue = {"StepDefinitions"},
        plugin = {"com.vimalselvam.cucumber.listener.ExtentCucumberFormatter:target/cucumber-reports/report.html"},
        monochrome = true
)

public class TestRunner {
    @AfterClass
    public static void writeExtentReport() throws IOException {
        Reporter.loadXMLConfig(new File("configs/extent-config.xml"));
        Reporter.setSystemInfo("user", System.getProperty("user.name"));
        Reporter.setSystemInfo("os", "Windows 10");
    }
}
