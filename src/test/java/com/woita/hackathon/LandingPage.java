package com.woita.hackathon;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mcbrydr on 12/11/19
 */
public class LandingPage {

    private final WebDriver driver;

    public LandingPage(WebDriver driver) {
        this.driver = driver;
    }

    public void orderTransactionsByAmount(){
        WebElement amountLink = driver.findElement(By.id("amount"));
        amountLink.click();
    }

    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        WebElement tableTransactions = new WebDriverWait(driver, 5)
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@id='transactionsTable']/tbody")));

        List<WebElement> tableRows = tableTransactions.findElements(By.tagName("tr"));

        for (WebElement row: tableRows) {
            List<WebElement> tableColumns = row.findElements(By.tagName("td"));
            String status = null;
            String date = null;
            String description = null;
            String category = null;
            String amount = null;

            for (int i=0; i < tableColumns.size(); i++) {
                String value = tableColumns.get(i).getText();
                switch (i){
                    case 0:
                        status = value;
                        break;
                    case 1:
                        date = value;
                        break;
                    case 2:
                        description = value;
                        break;
                    case 3:
                        category = value;
                        break;
                    case 4:
                        amount = value;
                        break;
                }
            }
            transactions.add(new Transaction(status, date, description, category, amount));
        }
        return transactions;
    }
}
