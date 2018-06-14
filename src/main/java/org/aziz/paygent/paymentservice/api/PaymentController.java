package org.aziz.paygent.paymentservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.aziz.paygent.paymentservice.exceptions.PaymentNotFoundException;
import org.aziz.paygent.paymentservice.models.MemberDTO;
import org.aziz.paygent.paymentservice.models.PaymentGroupDTO;
import org.aziz.paygent.paymentservice.models.entities.Payment;
import org.aziz.paygent.paymentservice.services.ExpenseService;
import org.aziz.paygent.paymentservice.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/payments")
@CrossOrigin
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public Iterable<Payment> getAll() {
        return paymentService.getAll();
    }

    @GetMapping(path = "{id}")
    public Payment get(@PathVariable(value = "id") String id) {
        return paymentService.get(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @PostMapping
    public Payment create(@RequestBody @Valid Payment payment) throws JsonProcessingException {
        payment.setCreatedTime(new Date());
        Payment savedPayment = paymentService.save(payment);
        paymentService.addPaymentIdToGroup(new PaymentGroupDTO(savedPayment.getId(), payment.getGroupId()));
        return savedPayment;
    }

    @PutMapping("{id}")
    public Payment update(@PathVariable(value = "id") String id, @RequestBody Payment paymentDetails) {
        Payment payment = paymentService.validatedPayment(id);
        payment.setAmount(paymentDetails.getAmount());
        payment.setDescription(paymentDetails.getDescription());
        return paymentService.update(payment);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") String id) {
        paymentService.delete(id);
    }

    @PostMapping(path = "settle/{id}")
    public Payment settlePayment(@PathVariable(value = "id") String id) {
        return paymentService.settlePayment(paymentService.validatedPayment(id));
    }

    @GetMapping(path = "info/{groupId}/{memberId}")
    public MemberDTO getMemberDTO(@PathVariable(value = "memberId") String memberId, @PathVariable(value = "groupId") String groupId) {
        MemberDTO memberDTO = new MemberDTO(memberId);
        for (Payment payment : paymentService.getPaymentsOfMember(memberId, groupId)) {
            memberDTO.getPayment().add(payment);
        }
        memberDTO.setSumLeftAmount(paymentService.getAllLeftAmountByMember(memberId, groupId));
        memberDTO.setSumPaidAmount(paymentService.getAllPaidAmountByMember(memberId, groupId));
        return memberDTO;
    }
}

