package dev.gorillazord.cuscuz.repository;

import java.util.Optional;

import dev.gorillazord.cuscuz.model.CuscuzOrder;

public interface OrderRepository {
    CuscuzOrder save(CuscuzOrder order);
    Optional<CuscuzOrder> findById(Long id);

}
