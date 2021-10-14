import '../models/Customer.dart';
import 'DBResult.dart';

class Util2 {
  Util2();
  static void createCustomer() async {
    //  Create a randomized customer

    Customer c =
        Customer(name: 'FirstCustomer', dob: DateTime.utc(1992, 3, 23));

    DBResult s = (await c.save());
  }

  static void createInvoiceItem() {
    //  Create a randomized customer
  }
}
