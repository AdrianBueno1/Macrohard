package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

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
public final class ITMainSeleniumService {

	private static final String BASE_URL = "http://localhost:7080/MacroHard/";

	private static WebDriver driver;

	private ITMainSeleniumService() {
		super();
	}

	@BeforeAll
	public static void setupClass() {
		WebDriverManager.firefoxdriver().setup();
	}

	@BeforeEach
	public void setUpTest() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@AfterEach
	public void tearDownTest() {
		driver.close();
	}

	@Test
	public void testMainPage() {
		driver.get(BASE_URL);

		WebElement mainSection = driver.findElement(By.id("main-section"));
		assertNotNull(mainSection);
	}

	@Test
	public void testMainPageWithoutAd() {
		driver.get(BASE_URL);

		List<WebElement> ads = driver.findElements(By.tagName("h3"));
		assertEquals(0, ads.size());
	}

}
