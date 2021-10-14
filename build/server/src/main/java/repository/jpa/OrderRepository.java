package repository.jpa;

import java.util.List;
import models.Customer;
import models.Order;
import models.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  public List<Order> getByCustomer(Customer customer);

  public List<Order> getByPaymentMethod(PaymentMethod paymentMethod);
}
