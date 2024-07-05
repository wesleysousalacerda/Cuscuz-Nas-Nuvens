package dev.gorillazord.cuscuz.repository;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.asm.Type;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dev.gorillazord.cuscuz.model.Cuscuz;
import dev.gorillazord.cuscuz.model.CuscuzOrder;
import dev.gorillazord.cuscuz.model.IngredientRef;

@Repository
public class JdbcOrderRepository implements OrderRepository {

  private JdbcOperations jdbcOperations;

  public JdbcOrderRepository(JdbcOperations jdbcOperations) {
    this.jdbcOperations = jdbcOperations;
  }

  @Override
  @Transactional
  public CuscuzOrder save(CuscuzOrder order) {
    PreparedStatementCreatorFactory pscf =
      new PreparedStatementCreatorFactory(
        "insert into Cuscuz_Order "
        + "(delivery_name, delivery_street, delivery_city, "
        + "delivery_state, delivery_zip, cc_number, "
        + "cc_expiration, cc_cvv, placed_at) "
        + "values (?,?,?,?,?,?,?,?,?)",
        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
        Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
    );
    pscf.setReturnGeneratedKeys(true);

    order.setPlacedAt(new Date());
    PreparedStatementCreator psc =
        pscf.newPreparedStatementCreator(
            Arrays.asList(
                order.getDeliveryName(),
                order.getDeliveryStreet(),
                order.getDeliveryCity(),
                order.getDeliveryState(),
                order.getDeliveryZip(),
                order.getCcNumber(),
                order.getCcExpiration(),
                order.getCcCVV(),
                order.getPlacedAt()));

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcOperations.update(psc, keyHolder);
    long orderId = keyHolder.getKey().longValue();
    order.setId(orderId);

    List<Cuscuz> cuscuzes = order.getCuscuz();
    int i=0;
    for (Cuscuz cuscuz : cuscuzes) {
      saveCuscuz(orderId, i++, cuscuz);
    }

    return order;
  }

  private long saveCuscuz(Long orderId, int orderKey, Cuscuz cuscuz) {
    cuscuz.setCreatedAt(new Date());
    PreparedStatementCreatorFactory pscf =
            new PreparedStatementCreatorFactory(
        "insert into Cuscuz "
        + "(name, created_at, cuscuz_order, cuscuz_order_key) "
        + "values (?, ?, ?, ?)",
        Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
    );
    pscf.setReturnGeneratedKeys(true);

    PreparedStatementCreator psc =
        pscf.newPreparedStatementCreator(
            Arrays.asList(
                cuscuz.getName(),
                cuscuz.getCreatedAt(),
                orderId,
                orderKey));

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcOperations.update(psc, keyHolder);
    long cuscuzId = keyHolder.getKey().longValue();
    cuscuz.setId(cuscuzId);

    saveIngredientRefs(cuscuzId, cuscuz.getIngredients());

    return cuscuzId;
  }

  private void saveIngredientRefs(
      long cuscuzId, List<IngredientRef> ingredientRefs) {
    int key = 0;
    for (IngredientRef ingredientRef : ingredientRefs) {
      jdbcOperations.update(
          "insert into Ingredient_Ref (ingredient, cuscuz, cuscuz_key) "
          + "values (?, ?, ?)",
          ingredientRef.getIngredient(), cuscuzId, key++);
    }
  }

  @Override
  public Optional<CuscuzOrder> findById(Long id) {
    try {
      CuscuzOrder order = jdbcOperations.queryForObject(
          "select id, delivery_name, delivery_street, delivery_city, "
              + "delivery_state, delivery_zip, cc_number, cc_expiration, "
              + "cc_cvv, placed_at from Cuscuz_Order where id=?",
          (row, rowNum) -> {
            CuscuzOrder cuscuzOrder = new CuscuzOrder();
            cuscuzOrder.setId(row.getLong("id"));
            cuscuzOrder.setDeliveryName(row.getString("delivery_name"));
            cuscuzOrder.setDeliveryStreet(row.getString("delivery_street"));
            cuscuzOrder.setDeliveryCity(row.getString("delivery_city"));
            cuscuzOrder.setDeliveryState(row.getString("delivery_state"));
            cuscuzOrder.setDeliveryZip(row.getString("delivery_zip"));
            cuscuzOrder.setCcNumber(row.getString("cc_number"));
            cuscuzOrder.setCcExpiration(row.getString("cc_expiration"));
            cuscuzOrder.setCcCVV(row.getString("cc_cvv"));
            cuscuzOrder.setPlacedAt(new Date(row.getTimestamp("placed_at").getTime()));
            cuscuzOrder.setCuscuz(findCuscuzByOrderId(row.getLong("id")));
            return cuscuzOrder;
          }, id);
      return Optional.of(order);
    } catch (IncorrectResultSizeDataAccessException e) {
      return Optional.empty();
    }
  }

  private List<Cuscuz> findCuscuzByOrderId(long orderId) {
    return jdbcOperations.query(
        "select id, name, created_at from Cuscuz "
        + "where cuscuz_order=? order by cuscuz_order_key",
        (row, rowNum) -> {
          Cuscuz cuscuz = new Cuscuz();
          cuscuz.setId(row.getLong("id"));
          cuscuz.setName(row.getString("name"));
          cuscuz.setCreatedAt(new Date(row.getTimestamp("created_at").getTime()));
          cuscuz.setIngredients(findIngredientsByCuscuzId(row.getLong("id")));
          return cuscuz;
        },
        orderId);
  }

  private List<IngredientRef> findIngredientsByCuscuzId(long cuscuzId) {
    return jdbcOperations.query(
        "select ingredient from Ingredient_Ref "
        + "where cuscuz = ? order by cuscuz_key",
        (row, rowNum) -> {
          return new IngredientRef(row.getString("ingredient"));
        },
        cuscuzId);
  }

}