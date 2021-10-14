package rest;

import classes.MutateResultStatus;
import d3e.core.CloneContext;
import d3e.core.CurrentUser;
import d3e.core.D3ELogger;
import d3e.core.ListExt;
import d3e.core.TransactionWrapper;
import gqltosql.schema.GraphQLDataFetcher;
import gqltosql.schema.IModelSchema;
import graphql.language.Field;
import helpers.CustomerEntityHelper;
import helpers.InventoryItemEntityHelper;
import helpers.OrderEntityHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import models.AnonymousUser;
import models.Customer;
import models.InventoryItem;
import models.Order;
import models.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import repository.jpa.AnonymousUserRepository;
import repository.jpa.CustomerRepository;
import repository.jpa.InventoryItemRepository;
import repository.jpa.OneTimePasswordRepository;
import repository.jpa.OrderRepository;
import repository.jpa.UserRepository;
import repository.jpa.UserSessionRepository;
import security.AppSessionProvider;
import store.EntityHelperService;
import store.EntityMutator;
import store.ValidationFailedException;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("api/native/")
public class NativeMutation extends AbstractQueryService {
  @Autowired private EntityMutator mutator;
  @Autowired private ObjectFactory<EntityHelperService> helperService;
  @Autowired private TransactionWrapper transactionWrapper;
  @Autowired private IModelSchema schema;
  @Autowired private AnonymousUserRepository anonymousUserRepository;
  @Autowired private CustomerRepository customerRepository;
  @Autowired private InventoryItemRepository inventoryItemRepository;
  @Autowired private OneTimePasswordRepository oneTimePasswordRepository;
  @Autowired private OrderRepository orderRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private UserSessionRepository userSessionRepository;
  @Autowired private ObjectFactory<AppSessionProvider> provider;

  @PostMapping(path = "/mutate", produces = MediaType.APPLICATION_JSON_VALUE)
  public String run(@RequestBody String query) throws Exception {
    JSONObject req = new JSONObject(query);
    List<Field> fields = parseFields(req);
    JSONObject variables = req.getJSONObject("variables");
    String queryStr = null;
    try {
      queryStr = req.getString("query");
    } catch (JSONException e) {
    }
    return executeFields(queryStr, fields, variables);
  }

  public String executeFields(String query, List<Field> fields, JSONObject variables)
      throws Exception {
    JSONObject data = new JSONObject();
    for (Field s : fields) {
      String name = s.getAlias() == null ? s.getName() : s.getAlias();
      long time = System.currentTimeMillis();
      D3ELogger.info("Started: " + time + ":" + s.getName());
      transactionWrapper.doInTransaction(
          () -> {
            try {
              try {
                Object res = executeOperation(query, s, variables);
                data.put(name, res);
              } catch (ValidationFailedException e) {
                data.put(name, createFailureResult(s, e));
              }
            } catch (Exception e2) {
              throw new RuntimeException(e2);
            }
          });
      D3ELogger.info("Completed: " + time + ":" + s.getName());
    }
    JSONObject output = new JSONObject();
    output.put("data", data);
    return output.toString();
  }

  private JSONObject createSuccessResult(Object value, Field field, String type)
      throws JSONException {
    JSONObject result = new JSONObject();
    result.put("status", MutateResultStatus.Success);
    result.put("errors", new JSONArray());
    if (value != null) {
      result.put(
          "value",
          new GraphQLDataFetcher(schema, true).fetch(inspect(field, "value"), type, value));
    }
    return result;
  }

  private JSONObject createFailureResult(Field field, ValidationFailedException e)
      throws JSONException {
    JSONObject result = new JSONObject();
    if (e.hasStatus()) {
      result.put("status", e.getStatus());
    } else {
      result.put("status", MutateResultStatus.ValidationFail);
    }
    JSONArray array = new JSONArray();
    e.getErrors().forEach(s -> array.put(s));
    result.put("errors", array);
    logErrors(result);
    return result;
  }

  private Object executeOperation(String query, Field field, JSONObject variables)
      throws Exception {
    GraphQLInputContext ctx =
        new ArgumentInputContext(
            field.getArguments(),
            helperService.getObject(),
            new HashMap<>(),
            new HashMap<>(),
            variables);
    D3ELogger.displayGraphQL(field.getName(), query, variables);
    switch (field.getName()) {
      case "createCustomer":
        {
          return createSuccessResult(createCustomer(ctx), field, "Customer");
        }
      case "updateCustomer":
        {
          return createSuccessResult(updateCustomer(ctx), field, "Customer");
        }
      case "deleteCustomer":
        {
          deleteCustomer(ctx);
          return createSuccessResult(null, field, "Customer");
        }
      case "createInventoryItem":
        {
          return createSuccessResult(createInventoryItem(ctx), field, "InventoryItem");
        }
      case "updateInventoryItem":
        {
          return createSuccessResult(updateInventoryItem(ctx), field, "InventoryItem");
        }
      case "deleteInventoryItem":
        {
          deleteInventoryItem(ctx);
          return createSuccessResult(null, field, "InventoryItem");
        }
      case "createOrder":
        {
          return createSuccessResult(createOrder(ctx), field, "Order");
        }
      case "updateOrder":
        {
          return createSuccessResult(updateOrder(ctx), field, "Order");
        }
      case "deleteOrder":
        {
          deleteOrder(ctx);
          return createSuccessResult(null, field, "Order");
        }
    }
    D3ELogger.info("Mutation Not found");
    return null;
  }

  private Customer createCustomer(GraphQLInputContext ctx) throws Exception {
    User currentUser = CurrentUser.get();
    if (!(currentUser instanceof AnonymousUser)) {
      throw new ValidationFailedException(
          MutateResultStatus.AuthFail,
          ListExt.asList("Current user type does not have create permissions for this model."));
    }
    Customer newCustomer = ctx.readChild("input", "Customer");
    this.mutator.save(newCustomer, false);
    return newCustomer;
  }

  private Customer updateCustomer(GraphQLInputContext ctx) throws Exception {
    User currentUser = CurrentUser.get();
    if (!(currentUser instanceof AnonymousUser)) {
      throw new ValidationFailedException(
          MutateResultStatus.AuthFail,
          ListExt.asList("Current user type does not have update permissions for this model."));
    }
    CustomerEntityHelper customerHelper = this.mutator.getHelper("Customer");
    Customer currentCustomer =
        customerRepository.findById(ctx.readLong("input", "id")).orElse(null);
    if (currentCustomer == null) {
      throw new ValidationFailedException(
          MutateResultStatus.BadRequest, ListExt.asList("Invalid ID."));
    }
    currentCustomer.recordOld(CloneContext.forCloneable(currentCustomer, false));
    Customer newCustomer = ctx.readChild("input", "Customer");
    this.mutator.update(newCustomer, false);
    return newCustomer;
  }

  private void deleteCustomer(GraphQLInputContext ctx) throws Exception {
    User currentUser = CurrentUser.get();
    if (!(currentUser instanceof AnonymousUser)) {
      throw new ValidationFailedException(
          MutateResultStatus.AuthFail,
          ListExt.asList("Current user type does not have delete permissions for this model."));
    }
    long gqlInputId = ctx.readLong("input");
    CustomerEntityHelper customerHelper = this.mutator.getHelper("Customer");
    Customer currentCustomer = customerRepository.findById(gqlInputId).orElse(null);
    if (currentCustomer == null) {
      throw new ValidationFailedException(
          MutateResultStatus.BadRequest, ListExt.asList("Invalid ID"));
    }
    this.mutator.delete(currentCustomer, false);
  }

  private InventoryItem createInventoryItem(GraphQLInputContext ctx) throws Exception {
    User currentUser = CurrentUser.get();
    if (!(currentUser instanceof AnonymousUser)) {
      throw new ValidationFailedException(
          MutateResultStatus.AuthFail,
          ListExt.asList("Current user type does not have create permissions for this model."));
    }
    InventoryItem newInventoryItem = ctx.readChild("input", "InventoryItem");
    this.mutator.save(newInventoryItem, false);
    return newInventoryItem;
  }

  private InventoryItem updateInventoryItem(GraphQLInputContext ctx) throws Exception {
    User currentUser = CurrentUser.get();
    if (!(currentUser instanceof AnonymousUser)) {
      throw new ValidationFailedException(
          MutateResultStatus.AuthFail,
          ListExt.asList("Current user type does not have update permissions for this model."));
    }
    InventoryItemEntityHelper inventoryItemHelper = this.mutator.getHelper("InventoryItem");
    InventoryItem currentInventoryItem =
        inventoryItemRepository.findById(ctx.readLong("input", "id")).orElse(null);
    if (currentInventoryItem == null) {
      throw new ValidationFailedException(
          MutateResultStatus.BadRequest, ListExt.asList("Invalid ID."));
    }
    currentInventoryItem.recordOld(CloneContext.forCloneable(currentInventoryItem, false));
    InventoryItem newInventoryItem = ctx.readChild("input", "InventoryItem");
    this.mutator.update(newInventoryItem, false);
    return newInventoryItem;
  }

  private void deleteInventoryItem(GraphQLInputContext ctx) throws Exception {
    User currentUser = CurrentUser.get();
    if (!(currentUser instanceof AnonymousUser)) {
      throw new ValidationFailedException(
          MutateResultStatus.AuthFail,
          ListExt.asList("Current user type does not have delete permissions for this model."));
    }
    long gqlInputId = ctx.readLong("input");
    InventoryItemEntityHelper inventoryItemHelper = this.mutator.getHelper("InventoryItem");
    InventoryItem currentInventoryItem = inventoryItemRepository.findById(gqlInputId).orElse(null);
    if (currentInventoryItem == null) {
      throw new ValidationFailedException(
          MutateResultStatus.BadRequest, ListExt.asList("Invalid ID"));
    }
    this.mutator.delete(currentInventoryItem, false);
  }

  private Order createOrder(GraphQLInputContext ctx) throws Exception {
    User currentUser = CurrentUser.get();
    if (!(currentUser instanceof AnonymousUser)) {
      throw new ValidationFailedException(
          MutateResultStatus.AuthFail,
          ListExt.asList("Current user type does not have create permissions for this model."));
    }
    Order newOrder = ctx.readChild("input", "Order");
    this.mutator.save(newOrder, false);
    return newOrder;
  }

  private Order updateOrder(GraphQLInputContext ctx) throws Exception {
    User currentUser = CurrentUser.get();
    if (!(currentUser instanceof AnonymousUser)) {
      throw new ValidationFailedException(
          MutateResultStatus.AuthFail,
          ListExt.asList("Current user type does not have update permissions for this model."));
    }
    OrderEntityHelper orderHelper = this.mutator.getHelper("Order");
    Order currentOrder = orderRepository.findById(ctx.readLong("input", "id")).orElse(null);
    if (currentOrder == null) {
      throw new ValidationFailedException(
          MutateResultStatus.BadRequest, ListExt.asList("Invalid ID."));
    }
    currentOrder.recordOld(CloneContext.forCloneable(currentOrder, false));
    Order newOrder = ctx.readChild("input", "Order");
    this.mutator.update(newOrder, false);
    return newOrder;
  }

  private void deleteOrder(GraphQLInputContext ctx) throws Exception {
    User currentUser = CurrentUser.get();
    if (!(currentUser instanceof AnonymousUser)) {
      throw new ValidationFailedException(
          MutateResultStatus.AuthFail,
          ListExt.asList("Current user type does not have delete permissions for this model."));
    }
    long gqlInputId = ctx.readLong("input");
    OrderEntityHelper orderHelper = this.mutator.getHelper("Order");
    Order currentOrder = orderRepository.findById(gqlInputId).orElse(null);
    if (currentOrder == null) {
      throw new ValidationFailedException(
          MutateResultStatus.BadRequest, ListExt.asList("Invalid ID"));
    }
    this.mutator.delete(currentOrder, false);
  }

  private String generateToken() {
    char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    return generateRandomString(chars, 32);
  }

  private String generateCode() {
    char[] digits = "1234567890".toCharArray();
    return generateRandomString(digits, 4);
  }

  private String generateRandomString(char[] array, int length) {
    StringBuilder sb = new StringBuilder(length);
    Random rnd = new Random();
    for (int i = 0; i < length; i++) {
      char c = array[rnd.nextInt(array.length)];
      sb.append(c);
    }
    return sb.toString();
  }
}
