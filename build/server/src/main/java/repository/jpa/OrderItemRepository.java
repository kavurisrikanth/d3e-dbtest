package repository.jpa;

import java.util.List;
import models.InventoryItem;
import models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  public List<OrderItem> getByItem(InventoryItem item);
}
