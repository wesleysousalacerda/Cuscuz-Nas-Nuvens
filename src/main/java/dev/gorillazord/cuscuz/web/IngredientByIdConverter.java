package dev.gorillazord.cuscuz.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import org.springframework.core.convert.converter.Converter;

import dev.gorillazord.cuscuz.model.Ingredient;
import dev.gorillazord.cuscuz.model.Ingredient.Type;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {
    private Map<String, Ingredient> ingredientMap = new HashMap<>();

    public IngredientByIdConverter() {
        ingredientMap.put("SOL",
                new Ingredient("SOL", "Carne de Sol", Type.PROTEIN));
        ingredientMap.put("MOIDA",
                new Ingredient("MOIDA", "Carne Moída", Type.PROTEIN));
        ingredientMap.put("FGO",
                new Ingredient("FGO", "Frango Desfiado", Type.PROTEIN));
        ingredientMap.put("OVO",
                new Ingredient("OVO", "Ovo Mexido", Type.PROTEIN));
        ingredientMap.put("QJO",
                new Ingredient("QJO", "Queijo Coalho", Type.PROTEIN));
        ingredientMap.put("TMTE",
                new Ingredient("TMTE", "Tomate", Type.SIDE));
        ingredientMap.put("ALFC",
                new Ingredient("ALFC", "Alface", Type.SIDE));
        ingredientMap.put("RQJ",
                new Ingredient("RQJ", "Requeijão", Type.SAUCE));
        ingredientMap.put("COCO",
                new Ingredient("COCO", "Leite de Coco", Type.SAUCE));
        ingredientMap.put("MTG",
                new Ingredient("MTG", "Manteiga da Terra", Type.SAUCE));
        ingredientMap.put("CLCA",
                new Ingredient("CLCA", "Caldo de Carne", Type.SAUCE));
    }

    @Override
    public Ingredient convert(String id) {
        return ingredientMap.get(id);
    }
}