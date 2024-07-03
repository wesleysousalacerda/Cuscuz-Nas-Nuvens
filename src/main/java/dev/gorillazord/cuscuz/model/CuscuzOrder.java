package dev.gorillazord.cuscuz.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CuscuzOrder {
    private String deliveryName;
    private String deliveryStreet;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryCep;
    private String ccNumber;
    private String ccExpiration;
    private String ccCVV;
    private List<Cuscuz> cuscuz = new ArrayList<>();

    public void addCuscuz(Cuscuz cuscuz) {
        this.cuscuz.add(cuscuz);
    }
}