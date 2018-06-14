package org.aziz.paygent.paymentservice.models.entities;

import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
public class Payment extends DBEntity {
    @NotNull
    private String description;
    @NotNull
    private double amount;
    private boolean isPaid;
    private Currency usedCurrency;
    @Reference
    private List<Expense> expenseList = new ArrayList<>();
    @NotNull
    private String groupId;
    private String createdBy;
    private Date createdTime;

    public Payment() {
    }

    public Payment(String description, double amount, Currency usedCurrency, String createdBy, String groupId) {
        this.amount = amount;
        this.usedCurrency = usedCurrency;
        this.description = description;
        this.groupId = groupId;
        this.createdTime = new Date();
        this.createdBy = createdBy;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Currency getUsedCurrency() {
        return usedCurrency;
    }

    public void setUsedCurrency(Currency usedCurrency) {
        this.usedCurrency = usedCurrency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Expense> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void addExpense(Expense expense) {
        this.expenseList.add(expense);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
