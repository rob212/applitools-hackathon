package com.woita.hackathon;

import java.util.Objects;

/**
 * @author mcbrydr on 11/11/19
 */
public class Transaction implements Comparable<Transaction> {

    private String status;
    private String date;
    private String description;
    private String category;
    private String amount;

    public Transaction(String status, String date, String description, String category, String amount) {
        this.status = status;
        this.date = date;
        this.description = description;
        this.category = category;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(date, that.date) &&
                Objects.equals(description, that.description) &&
                Objects.equals(category, that.category) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, date, description, category, amount);
    }

    private Float extractAmount(String amountLabel) {
        String[] tokenised = amountLabel.split(" ");
        Float value = Float.parseFloat(tokenised[1].replaceAll(",", ""));
        if (tokenised[0].equalsIgnoreCase("-")) {
            value = value * -1;
        }
        return value;
    }

    @Override
    public int compareTo(Transaction t) {
        return (int) (extractAmount(this.amount) - extractAmount(t.amount));
    }
}
