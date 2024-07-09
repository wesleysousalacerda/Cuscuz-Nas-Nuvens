package dev.gorillazord.cuscuz.repository;

import org.springframework.data.repository.CrudRepository;

import dev.gorillazord.cuscuz.model.CuscuzOrder;

public interface OrderRepository 
         extends CrudRepository<CuscuzOrder, Long> {

}
