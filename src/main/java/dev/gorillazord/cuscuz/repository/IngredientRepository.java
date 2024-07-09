package dev.gorillazord.cuscuz.repository;

import org.springframework.data.repository.CrudRepository;

import dev.gorillazord.cuscuz.model.Ingredient;

public interface IngredientRepository 
         extends CrudRepository<Ingredient, String> {

}