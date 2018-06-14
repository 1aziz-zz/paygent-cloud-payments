package org.aziz.paygent.paymentservice.services.generics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public abstract class GenericAbstractService<T, E extends CrudRepository<T, String>> implements Service<T, String> {

    @Autowired
    private E repository;

    public T save(T entity) {
        return repository.save(entity);
    }

    public Iterable<T> getAll() {
        return repository.findAll();
    }

    public Optional<T> get(String id) {
        return repository.findById(id);
    }

    public T update(T entity) {
        return repository.save(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public void delete(T entity) {
        repository.delete(entity);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
