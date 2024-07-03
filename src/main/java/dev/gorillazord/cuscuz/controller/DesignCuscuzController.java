package dev.gorillazord.cuscuz.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import dev.gorillazord.cuscuz.model.Ingredient;
import dev.gorillazord.cuscuz.model.Ingredient.Type;
import dev.gorillazord.cuscuz.model.Cuscuz;
import dev.gorillazord.cuscuz.model.CuscuzOrder;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("cuscuzOrder")
public class DesignCuscuzController {

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("SOL", "Carne de Sol", Type.PROTEIN),
                new Ingredient("MOIDA", "Carne Moída", Type.PROTEIN),
                new Ingredient("FGO", "Frango Desfiado", Type.PROTEIN),
                new Ingredient("OVO", "Ovo Mexido", Type.PROTEIN),
                new Ingredient("QJO", "Queijo Coalho", Type.PROTEIN),
                new Ingredient("TMTE", "Tomate", Type.SIDE),
                new Ingredient("ALFC", "Alface", Type.SIDE),
                new Ingredient("RQJ", "Requeijão", Type.SAUCE),
                new Ingredient("COCO", "Leite de Coco", Type.SAUCE),
                new Ingredient("MTG", "Manteiga da Terra", Type.SAUCE),
                new Ingredient("CLCA", "Caldo de Carne", Type.SAUCE));
        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
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
    public String processCuscuz(Cuscuz cuscuz,
            @ModelAttribute CuscuzOrder cuscuzOrder) {
        cuscuzOrder.addCuscuz(cuscuz);
        log.info("Processing cuscuz: {}", cuscuz);
        return "redirect:/orders/current";
    }
}
