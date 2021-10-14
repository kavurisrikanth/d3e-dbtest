package repository.jpa;

import java.util.List;
import models.Customer;
import models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  public List<Customer> getByGuardian(Customer guardian);

  @Query("From models.Customer customer where :orders member customer.orders")
  public List<Customer> findByOrders(@Param("orders") Order orders);
}
