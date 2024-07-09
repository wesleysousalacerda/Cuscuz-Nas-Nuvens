package dev.gorillazord.cuscuz.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import dev.gorillazord.cuscuz.model.Cuscuz;
import dev.gorillazord.cuscuz.model.Ingredient;
import dev.gorillazord.cuscuz.model.Ingredient.Type;
import dev.gorillazord.cuscuz.model.IngredientRef;
import dev.gorillazord.cuscuz.repository.IngredientRepository;
import dev.gorillazord.cuscuz.repository.OrderRepository;
import dev.gorillazord.cuscuz.controller.DesignCuscuzController;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DesignCuscuzController.class)
public class DesignCuscuzControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private List<Ingredient> ingredients;

    private Cuscuz design;

    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private OrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        ingredients = Arrays.asList(
                new Ingredient("SOL", "Carne de Sol", Type.PROTEIN),
                new Ingredient("MODA", "Carne Moída", Type.PROTEIN),
                new Ingredient("FGO", "Frango Desfiado", Type.PROTEIN),
                new Ingredient("OVO", "Ovo Mexido", Type.PROTEIN),
                new Ingredient("QJO", "Queijo Coalho", Type.PROTEIN),
                new Ingredient("TMTE", "Tomate", Type.SIDE),
                new Ingredient("ALFC", "Alface", Type.SIDE),
                new Ingredient("RQJ", "Requeijão", Type.SAUCE),
                new Ingredient("COCO", "Leite de Coco", Type.SAUCE),
                new Ingredient("MTG", "Manteiga da Terra", Type.SAUCE));
        when(ingredientRepository.findAll())
                .thenReturn(ingredients);

        when(ingredientRepository.findById("SOL"))
                .thenReturn(Optional.of(new Ingredient("SOL", "Carne de Sol", Type.PROTEIN)));
        when(ingredientRepository.findById("FGO"))
                .thenReturn(Optional.of(new Ingredient("FGO", "Frango Desfiado", Type.PROTEIN)));
        when(ingredientRepository.findById("RQJ"))
                .thenReturn(Optional.of(new Ingredient("RQJ", "Requeijão", Type.SAUCE)));
        design = new Cuscuz();
        design.setName("Test Cuscuz");

        design.setIngredients(
                Arrays.asList(new IngredientRef("FLTO"), new IngredientRef("GRBF"), new IngredientRef("CHED")));
        

    }

    @Test
    public void testShowDesignForm() throws Exception {
        mockMvc.perform(get("/design"))
                .andExpect(status().isOk())
                .andExpect(view().name("design"))
                .andExpect(model().attribute("protein", ingredients.subList(0, 4)))
                .andExpect(model().attribute("side", ingredients.subList(5, 6)))
                .andExpect(model().attribute("sauce", ingredients.subList(7, 9)));
    }

    @Test
    public void processCuscuz() throws Exception {
        mockMvc.perform(post("/design")
                .content("name=Test+Cuscuz&ingredients=SOL,FGO,RQJ")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues("Location", "/orders/current"));
    }

}
