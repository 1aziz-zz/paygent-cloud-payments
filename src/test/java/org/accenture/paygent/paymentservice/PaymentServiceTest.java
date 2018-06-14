package org.accenture.paygent.paymentservice;

import org.accenture.paygent.paymentservice.models.entities.Currency;
import org.accenture.paygent.paymentservice.models.entities.Expense;
import org.accenture.paygent.paymentservice.models.entities.Payment;
import org.accenture.paygent.paymentservice.services.PaymentService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;

    private Expense expenseJeff, expenseAziz;
    private Payment lunchPayment, dinnerPayment;
    private String memberAziz, memberJeff;
    private String groupId;

    @Before
    public void init() {

        memberAziz = RandomStringUtils.randomAlphanumeric(10);
        memberJeff = RandomStringUtils.randomAlphanumeric(10);
        groupId = RandomStringUtils.randomAlphanumeric(10);

        lunchPayment = new Payment("Lunch", 56, Currency.EUR, memberAziz, groupId);
        dinnerPayment = new Payment("Dinner", 21, Currency.EUR, memberAziz, groupId);
    }

    @Test
    public void test_if_an_expense_is_added_successfully() {
        expenseJeff = new Expense(1, memberAziz);
        expenseAziz = new Expense(20, memberJeff);

        Payment savedPayment = paymentService.save(dinnerPayment);

        paymentService.addExpenseToPayment(savedPayment, expenseAziz);
        paymentService.addExpenseToPayment(savedPayment, expenseJeff);

        savedPayment = paymentService.get(savedPayment.getId()).get();

        assertThat(savedPayment.getExpenseList()).extracting(Expense::getId).contains(expenseAziz.getId());
        assertThat(savedPayment.getExpenseList()).extracting(Expense::getId).contains(expenseJeff.getId());
        assertThat(savedPayment.getExpenseList()).size().isEqualTo(2);
    }

    @Test
    public void test_if_only_expenses_with_paid_amount_can_be_settled() {
        expenseJeff = new Expense(40, memberAziz);
        expenseAziz = new Expense(16, memberJeff);

        Payment savedPayment = paymentService.save(lunchPayment);

        paymentService.addExpenseToPayment(savedPayment, expenseJeff);
        paymentService.addExpenseToPayment(savedPayment, expenseAziz);

        savedPayment = paymentService.settlePayment(savedPayment);

        assertThat(savedPayment.isPaid()).isTrue();
        assertThat(savedPayment.getExpenseList().stream().mapToDouble(Expense::getLeftAmount).sum()).isEqualTo(0);
    }

    @Test
    public void test_if_sum_of_all_left_amounts_by_member_is_correct() {

        double expectedLeftAmountForAziz = -4.5 + 2;
        double expectedLeftAmountForJeff = -2 + 4.5;

        Payment savedDinnerPayment = paymentService.save(dinnerPayment);
        paymentService.addExpenseToPayment(savedDinnerPayment, new Expense(6, memberAziz));
        paymentService.addExpenseToPayment(savedDinnerPayment, new Expense(15, memberJeff));

        Payment savedLunchPayment = paymentService.save(lunchPayment);
        paymentService.addExpenseToPayment(savedLunchPayment, new Expense(30, memberAziz));
        paymentService.addExpenseToPayment(savedLunchPayment, new Expense(26, memberJeff));

        savedDinnerPayment = paymentService.settlePayment(savedDinnerPayment);
        savedLunchPayment = paymentService.settlePayment(savedLunchPayment);

        assertThat(savedDinnerPayment.isPaid()).isTrue();
        assertThat(savedLunchPayment.isPaid()).isTrue();

        assertThat(paymentService.getAllLeftAmountByMember(memberAziz, groupId)).isEqualTo(expectedLeftAmountForAziz);
        assertThat(paymentService.getAllLeftAmountByMember(memberJeff, groupId)).isEqualTo(expectedLeftAmountForJeff);

    }
}
