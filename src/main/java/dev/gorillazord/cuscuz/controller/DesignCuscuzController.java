package dev.gorillazord.cuscuz.controller;

import java.util.Arrays;
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

import lombok.extern.slf4j.Slf4j;
import dev.gorillazord.cuscuz.model.Ingredient;
import dev.gorillazord.cuscuz.model.Ingredient.Type;
import dev.gorillazord.cuscuz.repository.IngredientRepository;
import jakarta.validation.Valid;
import dev.gorillazord.cuscuz.model.Cuscuz;
import dev.gorillazord.cuscuz.model.CuscuzOrder;

@Slf4j
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
        Iterable<Ingredient> ingredients = ingredientRepo.findAll();
        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType((List<Ingredient>) ingredients, type));
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

    private Iterable<Ingredient> filterByType(
            List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

    @PostMapping
    public String processCuscuz(@Valid Cuscuz cuscuz, Errors errors,
            @ModelAttribute CuscuzOrder cuscuzOrder) {
        if (errors.hasErrors()) {
            return "design";
        }
        cuscuzOrder.addCuscuz(cuscuz);
        log.info("Processing cuscuz: {}", cuscuz);
        return "redirect:/orders/current";
    }
}
