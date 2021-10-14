package d3e.core;

import classes.Customers;
import classes.Inventory;
import classes.LoginResult;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lists.CustomersImpl;
import lists.InventoryImpl;
import models.AnonymousUser;
import models.Customer;
import models.InventoryItem;
import models.OneTimePassword;
import models.Order;
import models.User;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.jpa.AnonymousUserRepository;
import repository.jpa.AvatarRepository;
import repository.jpa.CardPayMethodRepository;
import repository.jpa.CustomerRepository;
import repository.jpa.InventoryItemRepository;
import repository.jpa.OneTimePasswordRepository;
import repository.jpa.OrderItemRepository;
import repository.jpa.OrderRepository;
import repository.jpa.PaymentMethodRepository;
import repository.jpa.ReportConfigOptionRepository;
import repository.jpa.ReportConfigRepository;
import repository.jpa.UPIPayMethodRepository;
import repository.jpa.UserRepository;
import repository.jpa.UserSessionRepository;
import security.AppSessionProvider;
import security.JwtTokenUtil;

@Service
public class QueryProvider {
  public static QueryProvider instance;
  @Autowired private JwtTokenUtil jwtTokenUtil;
  @Autowired private AnonymousUserRepository anonymousUserRepository;
  @Autowired private AvatarRepository avatarRepository;
  @Autowired private CardPayMethodRepository cardPayMethodRepository;
  @Autowired private CustomerRepository customerRepository;
  @Autowired private InventoryItemRepository inventoryItemRepository;
  @Autowired private OneTimePasswordRepository oneTimePasswordRepository;
  @Autowired private OrderRepository orderRepository;
  @Autowired private OrderItemRepository orderItemRepository;
  @Autowired private PaymentMethodRepository paymentMethodRepository;
  @Autowired private ReportConfigRepository reportConfigRepository;
  @Autowired private ReportConfigOptionRepository reportConfigOptionRepository;
  @Autowired private UPIPayMethodRepository uPIPayMethodRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private UserSessionRepository userSessionRepository;
  @Autowired private CustomersImpl customersImpl;
  @Autowired private InventoryImpl inventoryImpl;
  @Autowired private ObjectFactory<AppSessionProvider> provider;

  @PostConstruct
  public void init() {
    instance = this;
  }

  public static QueryProvider get() {
    return instance;
  }

  public AnonymousUser getAnonymousUserById(long id) {
    Optional<AnonymousUser> findById = anonymousUserRepository.findById(id);
    return findById.orElse(null);
  }

  public Customer getCustomerById(long id) {
    Optional<Customer> findById = customerRepository.findById(id);
    return findById.orElse(null);
  }

  public InventoryItem getInventoryItemById(long id) {
    Optional<InventoryItem> findById = inventoryItemRepository.findById(id);
    return findById.orElse(null);
  }

  public OneTimePassword getOneTimePasswordById(long id) {
    Optional<OneTimePassword> findById = oneTimePasswordRepository.findById(id);
    return findById.orElse(null);
  }

  public boolean checkTokenUniqueInOneTimePassword(long oneTimePasswordId, String token) {
    return oneTimePasswordRepository.checkTokenUnique(oneTimePasswordId, token);
  }

  public Order getOrderById(long id) {
    Optional<Order> findById = orderRepository.findById(id);
    return findById.orElse(null);
  }

  public Customers getCustomers() {
    return customersImpl.get();
  }

  public Inventory getInventory() {
    return inventoryImpl.get();
  }

  public LoginResult loginWithOTP(String token, String code, String deviceToken) {
    OneTimePassword otp = oneTimePasswordRepository.getByToken(token);
    User user = otp.getUser();
    LoginResult loginResult = new LoginResult();
    if (deviceToken != null) {
      user.setDeviceToken(deviceToken);
    }
    loginResult.setSuccess(true);
    loginResult.setUserObject(otp.getUser());
    loginResult.setToken(token);
    return loginResult;
  }

  public AnonymousUser currentAnonymousUser() {
    return provider.getObject().getAnonymousUser();
  }
}
