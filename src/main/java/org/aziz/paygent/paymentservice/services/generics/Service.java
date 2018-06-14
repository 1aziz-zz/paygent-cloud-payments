package org.aziz.paygent.paymentservice.services.generics;
import java.io.Serializable;
import java.util.Optional;

public interface Service<T, ID extends Serializable>  {
    T save(T entity);

    Optional<T> get(String id);

    Iterable<T> getAll();

    T update(T entity);

    void delete(String id);

    void delete(T entity);

    void deleteAll();
}
