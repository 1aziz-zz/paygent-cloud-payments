package org.aziz.paygent.paymentservice.api;

import org.aziz.paygent.paymentservice.models.entities.Expense;
import org.aziz.paygent.paymentservice.models.entities.Payment;
import org.aziz.paygent.paymentservice.services.ExpenseService;
import org.aziz.paygent.paymentservice.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/payments/{paymentId}/expenses")
@CrossOrigin
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public Iterable<Expense> getAll(@PathVariable String paymentId) {
        return paymentService.validatedPayment(paymentId).getExpenseList();
    }

    @GetMapping(path = "{id}")
    public Expense get(@PathVariable String id) {
        return expenseService.get(id).get();
    }

    @PostMapping
    public Payment create(@PathVariable String paymentId, @RequestBody @Valid Expense expense) {
        Payment payment = paymentService.validatedPayment(paymentId);
        return paymentService.addExpenseToPayment(payment, expense);
    }

    @PutMapping
    public Expense update(@RequestBody @Valid Expense expense) {
        return expenseService.update(expense);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        expenseService.delete(id);
    }
}

