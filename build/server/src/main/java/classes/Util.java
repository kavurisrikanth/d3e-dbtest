package classes;

import java.time.LocalDateTime;
import models.CardPayMethod;
import models.Customer;

public class Util {
  public Util() {}

  public static boolean isUnderAge(Customer c) {
    LocalDateTime dob = c.getDob();
    LocalDateTime now = LocalDateTime.now();
    return now.getYear() - dob.getYear() >= 18l;
  }

  public static boolean isValidCard(CardPayMethod c) {
    LocalDateTime valid = c.getValidTill();
    LocalDateTime now = LocalDateTime.now();
    return valid.isAfter(now);
  }
}
