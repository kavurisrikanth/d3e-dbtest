import '../utils/ObservableState.dart';
import '../classes/Customers.dart';
import '../classes/Inventory.dart';
import '../classes/Query.dart';
import '../classes/Util2.dart';
import '../models/Customer.dart';
import '../models/InventoryItem.dart';
import '../rocket/MessageDispatch.dart';
import '../rocket/Template.dart';
import '../utils/CollectionUtils.dart';
import 'Button.dart';
import 'package:flutter/widgets.dart';

class StartPage extends StatefulWidget {
  StartPage({Key key}) : super(key: key);
  @override
  _StartPageState createState() => _StartPageState();
}

class _StartPageState extends ObservableState<StartPage> {
  Customers _customers;
  List<Customer> _allCustomers = [];
  Inventory _inventory;
  List<InventoryItem> _allItems = [];
  @override
  initState() {
    super.initState();

    initListeners();

    enableBuild = true;

    onInit();
  }

  void initListeners() {
    computeCustomers();

    this.on(['customers', 'customers.items'], computeAllCustomers);

    computeAllCustomers();

    computeInventory();

    this.on(['inventory', 'inventory.items'], computeAllItems);

    computeAllItems();

    this.on(['allCustomers'], rebuild);
  }

  Customers get customers {
    return _customers;
  }

  void setCustomers(Customers val) {
    bool isValChanged = _customers != val;

    if (!isValChanged) {
      return;
    }

    updateObservable('customers', _customers, val);

    MessageDispatch.get().dispose(this._customers);

    _customers = val;

    this.fire('customers', this);
  }

  void computeCustomers() async {
    try {
      setCustomers((await Query.get().getCustomers(
          UsageConstants
              .STARTPAGE_PROPERTIES_CUSTOMERS_COMPUTATION_QUERY_GETCUSTOMERS,
          synchronize: true)));
    } catch (exception) {
      print(' exception in computeCustomers : ' + exception.toString());

      setCustomers(null);
    }
  }

  List<Customer> get allCustomers {
    return _allCustomers;
  }

  void setAllCustomers(List<Customer> val) {
    bool isValChanged = CollectionUtils.isNotEquals(_allCustomers, val);

    if (!isValChanged) {
      return;
    }

    updateObservableColl('allCustomers', _allCustomers, val);

    _allCustomers.clear();

    _allCustomers.addAll(val);

    this.fire('allCustomers', this);
  }

  void addToAllCustomers(Customer val, [int index = -1]) {
    if (index == -1) {
      if (!_allCustomers.contains(val)) _allCustomers.add(val);
    } else {
      _allCustomers.insert(index, val);
    }

    fire('allCustomers', this, val, true);

    updateObservable('allCustomers', null, val);
  }

  void removeFromAllCustomers(Customer val) {
    _allCustomers.remove(val);

    fire('allCustomers', this, val, false);

    removeObservable('allCustomers', val);
  }

  void computeAllCustomers() {
    try {
      setAllCustomers(List.from(this.customers.items));
    } catch (exception) {
      print(' exception in computeAllCustomers : ' + exception.toString());

      setAllCustomers([]);
    }
  }

  Inventory get inventory {
    return _inventory;
  }

  void setInventory(Inventory val) {
    bool isValChanged = _inventory != val;

    if (!isValChanged) {
      return;
    }

    updateObservable('inventory', _inventory, val);

    MessageDispatch.get().dispose(this._inventory);

    _inventory = val;

    this.fire('inventory', this);
  }

  void computeInventory() async {
    try {
      setInventory((await Query.get().getInventory(
          UsageConstants
              .STARTPAGE_PROPERTIES_INVENTORY_COMPUTATION_QUERY_GETINVENTORY,
          synchronize: true)));
    } catch (exception) {
      print(' exception in computeInventory : ' + exception.toString());

      setInventory(null);
    }
  }

  List<InventoryItem> get allItems {
    return _allItems;
  }

  void setAllItems(List<InventoryItem> val) {
    bool isValChanged = CollectionUtils.isNotEquals(_allItems, val);

    if (!isValChanged) {
      return;
    }

    updateObservableColl('allItems', _allItems, val);

    _allItems.clear();

    _allItems.addAll(val);

    this.fire('allItems', this);
  }

  void addToAllItems(InventoryItem val, [int index = -1]) {
    if (index == -1) {
      if (!_allItems.contains(val)) _allItems.add(val);
    } else {
      _allItems.insert(index, val);
    }

    fire('allItems', this, val, true);

    updateObservable('allItems', null, val);
  }

  void removeFromAllItems(InventoryItem val) {
    _allItems.remove(val);

    fire('allItems', this, val, false);

    removeObservable('allItems', val);
  }

  void computeAllItems() {
    try {
      setAllItems(List.from(this.inventory.items));
    } catch (exception) {
      print(' exception in computeAllItems : ' + exception.toString());

      setAllItems([]);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(children: [
      for (var item in this.allCustomers)
        Row(children: [Text(item.name), Text(item.dob.toString())]),
      Button(child: Text('Create Customer')),
      Button(child: Text('Create Inventory Item')),
      Button(child: Text('Create Order'))
    ]);
  }

  void onInit() async {
    if (this.allCustomers.isEmpty) {
      Util2.createCustomer();
    }

    if (this.allItems.isEmpty) {
      Util2.createInvoiceItem();
    }
  }

  @override
  void dispose() {
    MessageDispatch.get().dispose(customers);

    MessageDispatch.get().dispose(inventory);

    super.dispose();
  }
}
