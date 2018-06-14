package org.aziz.paygent.paymentservice.datasource;

import org.aziz.paygent.paymentservice.models.entities.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends CrudRepository<Payment, String> {
}

