package org.aziz.paygent.paymentservice.models;

import org.aziz.paygent.paymentservice.models.entities.Payment;

import java.util.ArrayList;
import java.util.List;

public class MemberDTO {
    private List<Payment> payment = new ArrayList<>();
    private String memberId;
    private double sumPaidAmount, sumLeftAmount;

    public MemberDTO(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberId() {
        return memberId;
    }

    public List<Payment> getPayment() {
        return payment;
    }

    public void setPayment(List<Payment> payment) {
        this.payment = payment;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public double getSumPaidAmount() {
        return sumPaidAmount;
    }

    public void setSumPaidAmount(double sumPaidAmount) {
        this.sumPaidAmount = sumPaidAmount;
    }

    public double getSumLeftAmount() {
        return sumLeftAmount;
    }

    public void setSumLeftAmount(double sumLeftAmount) {
        this.sumLeftAmount = sumLeftAmount;
    }
}

