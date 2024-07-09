package dev.gorillazord.cuscuz.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table
// Exclude createdAt from equals() method so that tests won't fail trying to
// compare java.util.Date with java.sql.Timestamp (even though they're essentially
// equal). Need to figure out a better way than this, but excluding this property
// for now.
@EqualsAndHashCode(exclude = "createdAt")
public class Cuscuz {

  @Id
  private Long id;

  private Date createdAt = new Date();

  @NotNull
  @Size(min=5, message="Name must be at least 5 characters long")
  private String name;

  @Size(min=1, message="You must choose at least 1 ingredient")
  private List<IngredientRef> ingredients = new ArrayList<>();

  public void addIngredient(Ingredient cuscuz) {
    this.ingredients.add(new IngredientRef(cuscuz.getId()));
  }

}
