package dev.gorillazord.cuscuz;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import dev.gorillazord.cuscuz.model.Ingredient;
import dev.gorillazord.cuscuz.model.Ingredient.Type;
import dev.gorillazord.cuscuz.repository.IngredientRepository;

@SpringBootApplication
public class CuscuzApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuscuzApplication.class, args);
	}
	@Bean
  public CommandLineRunner dataLoader(IngredientRepository repo) {
    return args -> {
      repo.deleteAll(); // TODO: Quick hack to avoid tests from stepping on each other with constraint violations
      repo.save(new Ingredient("SOL", "Carne de Sol", Type.PROTEIN));
      repo.save(new Ingredient("MODA", "Carne Moída", Type.PROTEIN));
      repo.save(new Ingredient("FGO", "Frango Desfiado", Type.PROTEIN));
      repo.save(new Ingredient("OVO", "Ovo Mexido", Type.PROTEIN));
      repo.save(new Ingredient("QJO", "Queijo Coalho", Type.PROTEIN));
      repo.save(new Ingredient("TMTE", "Tomate", Type.SIDE));
      repo.save(new Ingredient("ALFC", "Alface", Type.SIDE));
      repo.save(new Ingredient("RQJ", "Requeijão", Type.SAUCE));
      repo.save(new Ingredient("COCO", "Leite de Coco", Type.SAUCE));
      repo.save(new Ingredient("MTG", "Manteiga da Terra", Type.SAUCE));
    };
  }

}
