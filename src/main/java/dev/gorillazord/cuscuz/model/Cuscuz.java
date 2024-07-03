package dev.gorillazord.cuscuz.model;

import java.util.List;

import lombok.Data;

@Data
public class Cuscuz {
    private String name;

    private List<Ingredient> ingredients;

}