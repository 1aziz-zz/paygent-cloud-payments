package org.aziz.paygent.paymentservice.models.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document
public class Expense extends DBEntity {
    @NotNull
    private double paidAmount;
    private double leftAmount;
    private String reminderId;
    @NotNull
    private String assignedMemberId;

    public Expense() {
    }

    public Expense(double paidAmount, String assignedMember) {
        this.paidAmount = paidAmount;
        this.assignedMemberId = assignedMember;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getAssignedMember() {
        return assignedMemberId;
    }

    public void setAssignedMember(String assignedMember) {
        this.assignedMemberId = assignedMember;
    }

    public String getReminder() {
        return reminderId;
    }

    public void setReminder(String reminder) {
        this.reminderId = reminder;
    }

    public double getLeftAmount() {
        return leftAmount;
    }

    public void setLeftAmount(double leftAmount) {
        this.leftAmount = leftAmount;
    }
}
