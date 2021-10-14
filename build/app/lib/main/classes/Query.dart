import 'LoginResult.dart';
import 'dart:convert';
import 'package:http/http.dart' as rest;
import 'Env.dart';
import 'package:graphql_flutter/graphql_flutter.dart' hide Subscription;
import '../models/Customer.dart';
import '../models/InventoryItem.dart';
import '../models/User.dart';
import '../rocket/MessageDispatch.dart';
import '../rocket/Template.dart';
import '../utils/GraphQLClientInit.dart';
import '../utils/LocalDataStore.dart';
import '../utils/ReferenceCatch.dart';
import 'Customers.dart';
import 'Inventory.dart';

class Query {
  GraphQLClient _client;
  static Query _queryObject;
  ReferenceCatch _referenceCatch;
  Query._init() {
    this._client = GraphQLClientInit.get();

    this._referenceCatch = ReferenceCatch.get();
  }
  factory Query.get() {
    if (_queryObject == null) {
      _queryObject = Query._init();
    }

    return _queryObject;
  }
  Future<User> currentUser() async {
    GraphQLClientInit.token = (await LocalDataStore.get().getToken());

    return (await LocalDataStore.get().currentUser());
  }

  void logout() async {
    LocalDataStore.get().setUser(null, null);
  }

  Future<Customers> getCustomers(int usage,
      {bool optimized = false, bool synchronize = false}) async {
    return MessageDispatch.get()
        .dataQuery('Customers', usage, false, null, synchronize: synchronize);
  }

  Future<Inventory> getInventory(int usage,
      {bool optimized = false, bool synchronize = false}) async {
    return MessageDispatch.get()
        .dataQuery('Inventory', usage, false, null, synchronize: synchronize);
  }
}
