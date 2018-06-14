package org.aziz.paygent.paymentservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NewExpenseAmountNotValid extends RuntimeException {
    public NewExpenseAmountNotValid() {
        super("Expense amount is not valid. Please try again.");
    }

}

