package dev.gorillazord.cuscuz.repository;

import java.util.Optional;

import dev.gorillazord.cuscuz.model.Ingredient;

public interface IngredientRepository {
    Iterable<Ingredient> findAll();

    Optional<Ingredient> findById(String id);

    Ingredient save(Ingredient ingredient);
}