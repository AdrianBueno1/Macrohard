package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.Assert.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.exceptions.DuplicateEmailException;
import es.udc.fi.dc.fd.model.exceptions.DuplicateUserException;
import es.udc.fi.dc.fd.model.exceptions.IncorrectCreditCardException;
import es.udc.fi.dc.fd.repository.AdRepository;
import io.github.bonigarcia.wdm.WebDriverManager;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/service.xml", "classpath:context/persistence.xml",
		"classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
@Transactional
@Rollback

public class ITAdSeleniumService {

	private static final String BASE_URL = "http://localhost:7080/MacroHard/";

	private static WebDriver driver;

	@Autowired
	private AdRepository adRepository;

	private ITAdSeleniumService() {
		super();
	}

	@BeforeAll
	public static void setupClass() {
		WebDriverManager.firefoxdriver().setup();

	}

	@BeforeEach
	public void setUpTest() throws DuplicateUserException, DuplicateEmailException, IncorrectCreditCardException {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

	}

	@AfterEach
	public void tearDownTest() {
		driver.close();

	}

	@Test
	public void testAdCreate() throws Exception {

		driver.get(BASE_URL + "login");

		WebElement userNameElement = driver.findElement(By.id("username"));
		userNameElement.sendKeys("test");

		WebElement passElement = driver.findElement(By.id("password"));
		passElement.sendKeys("testtest");

		WebElement submitElement = driver.findElement(By.id("login_form"));
		submitElement.submit();

		Thread.sleep(500);
		driver.get(BASE_URL + "createAd");
		Thread.sleep(500);

		WebElement adNameElement = driver.findElement(By.id("adName"));
		adNameElement.sendKeys("TEST NAME");
		WebElement descriptionElement = driver.findElement(By.id("description"));
		descriptionElement.sendKeys("TEST");

		WebElement priceElement = driver.findElement(By.id("price"));
		priceElement.sendKeys("1");

		WebElement cityElement = driver.findElement(By.id("city"));
		cityElement.sendKeys("TEST");

		WebElement imageElement = driver.findElement(By.id("image"));
		String imagePath = System.getProperty("user.dir") + "\\src\\main\\webapp\\resources\\images\\user_icon.png";
		imageElement.sendKeys(imagePath);

		WebElement submit2Element = driver.findElement(By.id("ad_form"));

		Thread.sleep(500);
		submit2Element.submit();

		Thread.sleep(500);
		driver.get(BASE_URL + "ads/1");
		Thread.sleep(500);

		assertNotNull(driver.findElement(By.id("myCarousel")).getText());

	}
}
