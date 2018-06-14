package org.aziz.paygent.paymentservice.services;

import org.aziz.paygent.paymentservice.datasource.ExpenseRepo;
import org.aziz.paygent.paymentservice.models.entities.Expense;
import org.aziz.paygent.paymentservice.services.generics.GenericAbstractService;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService extends GenericAbstractService<Expense, ExpenseRepo> {

}
