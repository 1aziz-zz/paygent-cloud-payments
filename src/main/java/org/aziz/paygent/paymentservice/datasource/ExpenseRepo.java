package org.aziz.paygent.paymentservice.datasource;

import org.aziz.paygent.paymentservice.models.entities.Expense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepo extends CrudRepository<Expense, String> {
}