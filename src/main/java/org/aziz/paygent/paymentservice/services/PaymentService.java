package org.aziz.paygent.paymentservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aziz.paygent.paymentservice.datasource.PaymentRepo;
import org.aziz.paygent.paymentservice.exceptions.NewExpenseAmountNotValid;
import org.aziz.paygent.paymentservice.exceptions.NewExpenseMemberAlreadyExists;
import org.aziz.paygent.paymentservice.exceptions.PaymentNotFoundException;
import org.aziz.paygent.paymentservice.models.PaymentGroupDTO;
import org.aziz.paygent.paymentservice.models.entities.Expense;
import org.aziz.paygent.paymentservice.models.entities.Payment;
import org.aziz.paygent.paymentservice.services.generics.GenericAbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class PaymentService extends GenericAbstractService<Payment, PaymentRepo> {

    @Autowired
    private JmsTemplate jmsTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    // add expense to a PAYMENT
    public Payment addExpenseToPayment(Payment payment, Expense expense) {
        if (!newExpenseAmountIsCorrect(expense, payment)) {
            throw new NewExpenseAmountNotValid();
        }
        if (ExpenseMemberExists(expense.getAssignedMember(), payment)) {
            throw new NewExpenseMemberAlreadyExists();
        }
        payment.addExpense(expense);
        return this.update(payment);
    }

    // view PAYMENTS of a member per GROUP
    public List<Payment> getPaymentsOfMember(String memberId, String groupId) {
        List<Payment> payments = new ArrayList<>();
        for (Payment payment : this.getAll()) {
            if (payment.getGroupId().equals(groupId)) {
                if (memberExistsInAPayment(payment.getId(), memberId)) {
                    payments.add(payment);
                }
            }
        }
        return payments;
    }

    // settle a PAYMENT
    public Payment settlePayment(Payment payment) {
        long divisive = payment.getExpenseList()
                .stream()
                .filter(t -> t.getPaidAmount() != 0)
                .count();
        if (this.paymentAmountsAreCorrect(payment)) {
            payment.getExpenseList()
                    .stream()
                    .filter(t -> t.getPaidAmount() != 0)
                    .forEach(expense -> expense.setLeftAmount(expense.getPaidAmount() - payment.getAmount() / divisive)
                    );
            payment.setPaid(true);
            return update(payment);
        }
        return null;
    }

    // view all members in a single PAYMENT
    private List<String> getAllMembersInAPayment(String paymentId) {
        Optional<Payment> payment = this.get(paymentId);
        if (payment.isPresent()) {
            return payment.get().getExpenseList().stream().map(Expense::getAssignedMember).collect(toList());
        }
        return new ArrayList<>();
    }

    // get all left amounts of a MEMBER per GROUP
    public double getAllLeftAmountByMember(String memberId, String groupId) {
        double sumLeftAmount = 0;
        for (Payment payment : this.getAll()) {
            if (payment.getGroupId().equals(groupId)) {
                sumLeftAmount += payment.getExpenseList()
                        .stream()
                        .filter(expense -> expense.getAssignedMember().equals(memberId))
                        .mapToDouble(Expense::getLeftAmount).sum();
            }
        }
        return sumLeftAmount;
    }

    // get all paid amounts of a MEMBER per GROUP
    public double getAllPaidAmountByMember(String memberId, String groupId) {
        double sumPaidAmount = 0;
        for (Payment payment : this.getAll()) {
            if (payment.getGroupId().equals(groupId)) {
                sumPaidAmount += payment.getExpenseList()
                        .stream()
                        .filter(expense -> expense.getAssignedMember().equals(memberId))
                        .mapToDouble(Expense::getPaidAmount).sum();
            }
        }
        return sumPaidAmount;
    }


    private boolean memberExistsInAPayment(String paymentId, String memberId) {
        Optional<Payment> payment = this.get(paymentId);
        return payment.map(p -> p.getExpenseList()
                .stream()
                .anyMatch(expense -> expense.getAssignedMember().equals(memberId))).orElse(false);
    }

    private boolean paymentAmountsAreCorrect(Payment payment) {
        double sumAllExpenses = payment.getExpenseList().stream().mapToDouble(Expense::getPaidAmount).sum();
        return sumAllExpenses == payment.getAmount();
    }

    private boolean ExpenseMemberExists(String expenseAssignedMember, Payment payment) {
        for (Expense expense : payment.getExpenseList()) {
            if (expense.getAssignedMember().equals(expenseAssignedMember)) {
                return true;
            }
        }
        return false;
        //return payment.getExpenseList().stream().anyMatch(expense -> expense.getAssignedMember().equals(expenseAssignedMember));
    }

    private boolean newExpenseAmountIsCorrect(Expense expense, Payment payment) {
        double getSumPaidAmount = this.getSumPaidAmount(payment.getId());
        return payment.getAmount() - getSumPaidAmount >= expense.getPaidAmount();
    }

    private double getSumPaidAmount(String id) {
        return this.get(id).get().getExpenseList().stream().mapToDouble(Expense::getPaidAmount).sum();
    }


    public Payment validatedPayment(String paymentId) {
        Optional<Payment> payment = get(paymentId);
        payment.orElseThrow(
                () -> new PaymentNotFoundException(paymentId));
        return payment.get();
    }

    public void addPaymentIdToGroup(final PaymentGroupDTO paymentGroupDTO) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(paymentGroupDTO);
        jmsTemplate.convertAndSend("addPaymentIdToGroup", message);
        System.out.println("PAYMENT-SERVICE: sent: <" + message + ">");
    }
}
