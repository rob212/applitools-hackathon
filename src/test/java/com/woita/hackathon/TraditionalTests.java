package com.woita.hackathon;

import com.google.common.collect.Comparators;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author mcbrydr on 07/11/19
 */
@RunWith(JUnitParamsRunner.class)
public class TraditionalTests {

    WebDriver driver =  new ChromeDriver();

    @Before
    public void setUp() throws Exception {
        driver.get("https://demo.applitools.com/hackathon.html");
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void verifyPageHeading() {
        String heading = driver.findElement(By.className("auth-header")).getText();
        assertEquals("Login Form", heading);
    }

    @Test
    public void verifyLoginFormLabels() {
        // verify the Username label is correct
        String usernameLabel = driver.findElement(By.xpath("//input[@id='username']/preceding-sibling::label")).getText();
        assertEquals("Username", usernameLabel);

        // verify there is a username input field
        String usernameInputFieldType = driver.findElement(By.id("username")).getTagName();
        assertEquals("input", usernameInputFieldType);

        // verify that the username field icon is set correctly
        WebElement usernameIconElement = driver.findElement(By.xpath("//input[@id='username']/following-sibling::div"));
        assertTrue(elementHasClasses(usernameIconElement, "pre-icon os-icon os-icon-user-male-circle"));

        // verify the username placeholder text is correct
        String usernameFieldPlaceholder = driver.findElement(By.id("username")).getAttribute("placeholder");
        assertEquals("Enter your username", usernameFieldPlaceholder);

        // verify the Password label is correct
        String passwordLabel = driver.findElement(By.xpath("//input[@id='password']/preceding-sibling::label")).getText();
        assertEquals("Password", passwordLabel);

        // verify there is a password input field
        String passwordInputFieldType = driver.findElement(By.id("password")).getTagName();
        assertEquals("input", passwordInputFieldType);

        // verify that the password field icon is set correctly
        WebElement passwordIconElement = driver.findElement(By.xpath("//input[@id='password']/following-sibling::div"));
        assertTrue(elementHasClasses(passwordIconElement, "pre-icon os-icon os-icon-fingerprint"));

        // verify the password placeholder text is correct
        String passwordFieldPlaceholder = driver.findElement(By.id("password")).getAttribute("placeholder");
        assertEquals("Enter your password", passwordFieldPlaceholder);
    }

    @Test
    public void verifyLoginButtonIsDisplayed() {
        WebElement loginButton = driver.findElement(By.id("log-in"));
        assertTrue(loginButton.isDisplayed());
        assertEquals("button", loginButton.getTagName());
    }

    @Test
    public void verifySocialMediaLinksExist() {
        String[] desiredSocialMedia = {"facebook", "twitter", "linkedin"};
        List<WebElement> links = driver.findElements(By.xpath("//div[@class='buttons-w']/div[@style='text-align:center']/child::a/child::img"));
        for(String platform : desiredSocialMedia) {
        boolean linkExists = false;
            for (WebElement link: links) {
                String url = link.getAttribute("src");
                 if (url.contains("https://demo.applitools.com/img/social-icons/" + platform +".png")) {
                     linkExists = true;
                 }
            }
            assertTrue(linkExists);
        }
    }

    @Test
    public void verifyRememberMeCheckboxExists() {
        // verify the text of the Remember Me checkbox is correct
        WebElement rememberMeCheckboxDiv = driver.findElement(By.xpath("//button[@id='log-in']/following-sibling::div[@class='form-check-inline']"));
        String rememberMeText = rememberMeCheckboxDiv.getText();
        assertEquals("Remember Me", rememberMeText);

        // verify that the checkbox is present
        String type = rememberMeCheckboxDiv.findElement(By.className("form-check-input")).getAttribute("type");
        assertEquals("checkbox", type);
    }


    @Test
    @Parameters({
            "password, Username must be present",
            "username, Password must be present",
            "null, Both Username and Password must be present"
    })
    public void verifyLoginForm(String elementToPopulate, String expectedErrorMessage) {
        // populate specific fields based upon parameterized input
        if (!elementToPopulate.equals("null")) {
            WebElement usernameField = driver.findElement(By.id(elementToPopulate));
            usernameField.sendKeys("words");
        }
        WebElement loginButton = driver.findElement(By.id("log-in"));
        loginButton.click();

        // verify that the empty alert div is no longer displayed
        WebElement alertEmpty = driver.findElement(By.className("alert"));
        String displayStatus = alertEmpty.getAttribute("style");
        assertTrue(displayStatus.contains("display: none"));

        // verify that an error message is displayed
        String errorMessage = driver.findElement(By.className("alert-warning")).getText();
        assertEquals(expectedErrorMessage, errorMessage);
    }

    @Test
    public void tableSortTest() {
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.sendKeys("susan");
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("opensesame");

        WebElement loginButton = driver.findElement(By.id("log-in"));
        loginButton.click();

        List<Float> orderedTransactionsBefore = new ArrayList<>();

        orderedTransactionsBefore = getOrderedAmounts();

        WebElement amountLink = driver.findElement(By.id("amount"));
        amountLink.click();

        List<Float> orderedTransactionsAfter = new ArrayList<>();

        orderedTransactionsAfter = getOrderedAmounts();


//        // verify the same number of transactions still remain
        assertEquals(orderedTransactionsBefore.size(), orderedTransactionsAfter.size());
//        // verify that by clicking the Amount link the order of the transactions are now ordered in ascending order
        boolean isInAscendingOrder = Comparators.isInOrder(orderedTransactionsAfter, Comparator.naturalOrder());
        System.out.println(isInAscendingOrder);

    }

    private List<Float> getOrderedAmounts() {
        List<Float> orderedTransactions = new ArrayList<>();

        WebElement tableTransactions = new WebDriverWait(driver, 5)
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@id='transactionsTable']/tbody")));

        List<WebElement> tableRows = tableTransactions.findElements(By.tagName("tr"));


        for (WebElement row : tableRows) {
            String amountLabel = row.findElements(By.tagName("td")).get(4).getText();
            Float value = extractAmount(amountLabel);
            orderedTransactions.add(value);
        }
        return orderedTransactions;
    }

    private Float extractAmount(String amountLabel) {
        String[] tokenised = amountLabel.split(" ");
        return Float.parseFloat(tokenised[1].replaceAll(",", ""));
    }

    private boolean elementHasClasses(WebElement element, String classNames) {
        String classes = element.getAttribute("class");
        if (classes.equals(classNames)) {
            return true;
        }
        return false;
    }
}
