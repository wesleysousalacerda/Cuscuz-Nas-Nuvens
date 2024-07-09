package dev.gorillazord.cuscuz.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import dev.gorillazord.cuscuz.model.Cuscuz;
import dev.gorillazord.cuscuz.model.CuscuzOrder;
import dev.gorillazord.cuscuz.model.Ingredient;
import dev.gorillazord.cuscuz.model.Ingredient.Type;
import dev.gorillazord.cuscuz.repository.IngredientRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/design")
@SessionAttributes("cuscuzOrder")
public class DesignCuscuzController {

  private final IngredientRepository ingredientRepo;

  @Autowired
  public DesignCuscuzController(
        IngredientRepository ingredientRepo) {
    this.ingredientRepo = ingredientRepo;
  }

  @ModelAttribute
  public void addIngredientsToModel(Model model) {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredientRepo.findAll().forEach(i -> ingredients.add(i));

    Type[] types = Ingredient.Type.values();
    for (Type type : types) {
      model.addAttribute(type.toString().toLowerCase(),
          filterByType(ingredients, type));
    }
  }

  @ModelAttribute(name = "cuscuzOrder")
  public CuscuzOrder order() {
    return new CuscuzOrder();
  }

  @ModelAttribute(name = "cuscuz")
  public Cuscuz cuscuz() {
    return new Cuscuz();
  }

  @GetMapping
  public String showDesignForm() {
    return "design";
  }

  @PostMapping
  public String processCuscuz(
      @Valid Cuscuz cuscuz, Errors errors,
      @ModelAttribute CuscuzOrder cuscuzOrder) {

    if (errors.hasErrors()) {
      return "design";
    }

    cuscuzOrder.addCuscuz(cuscuz);

    return "redirect:/orders/current";
  }

  private Iterable<Ingredient> filterByType(
      List<Ingredient> ingredients, Type type) {
    return ingredients
              .stream()
              .filter(x -> x.getType().equals(type))
              .collect(Collectors.toList());
  }

}
