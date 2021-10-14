import 'TemplateTypes.dart';
import '../classes/ConnectionStatus.dart';
import '../classes/Customers.dart';
import '../classes/Inventory.dart';
import '../classes/LoginResult.dart';
import '../models/Customer.dart';
import '../models/InventoryItem.dart';

const int BOOLEAN = 0;

const int CONNECTIONSTATUS = 1;

const int CUSTOMER = 2;

const int CUSTOMERS = 3;

const int DFILE = 4;

const int DATE = 5;

const int DATETIME = 6;

const int DOUBLE = 7;

const int DURATION = 8;

const int INTEGER = 9;

const int INVENTORY = 10;

const int INVENTORYITEM = 11;

const int LOGINRESULT = 12;

const int STRING = 13;

const int TIME = 14;

const int TYPE = 15;

const int USER = 16;

class UsageConstants {
  static final int
      STARTPAGE_CUSTOMERS_SYNCHRONISE_SUBSCRIPTION_ONCUSTOMERSCHANGE = 0;
  static final int
      STARTPAGE_INVENTORY_SYNCHRONISE_SUBSCRIPTION_ONINVENTORYCHANGE = 1;
  static final int
      STARTPAGE_PROPERTIES_CUSTOMERS_COMPUTATION_QUERY_GETCUSTOMERS = 2;
  static final int
      STARTPAGE_PROPERTIES_INVENTORY_COMPUTATION_QUERY_GETINVENTORY = 3;
}

class ChannelConstants {
  static const int TOTAL_CHANNEL_COUNT = 0;
  static final List<TemplateChannel> channels = [];
}

class Template {
  static String HASH = '070e27bf722a024cf133f75985e466b5';
  static List<Usage> _usages = [
    Usage(
        'StartPage_customers_synchronise_Subscription_onCustomersChange',
        [
          TypeUsage(CUSTOMERS, [
            FieldUsage(0, [TypeUsage(CUSTOMER, [])])
          ])
        ],
        '8689171d842ac888f35c4222065a0c0b'),
    Usage(
        'StartPage_inventory_synchronise_Subscription_onInventoryChange',
        [
          TypeUsage(INVENTORY, [
            FieldUsage(0, [TypeUsage(INVENTORYITEM, [])])
          ])
        ],
        '02039f913e15f2db83c18225c031ca30'),
    Usage(
        'StartPage_properties_customers_computation_Query_getCustomers',
        [
          TypeUsage(CUSTOMERS, [
            FieldUsage(0, [TypeUsage(CUSTOMER, [])])
          ])
        ],
        '8689171d842ac888f35c4222065a0c0b'),
    Usage(
        'StartPage_properties_inventory_computation_Query_getInventory',
        [
          TypeUsage(INVENTORY, [
            FieldUsage(0, [TypeUsage(INVENTORYITEM, [])])
          ])
        ],
        '02039f913e15f2db83c18225c031ca30')
  ];
  static List<TemplateType> _types = [
    TemplateType('Boolean', '27226c864bac7454a8504f8edb15d95b', []),
    TemplateType(
        'ConnectionStatus',
        '8febfca144e40770ec88581be7cfece5',
        [
          TemplateField('Connecting', 0, FieldType.Enum),
          TemplateField('Connected', 0, FieldType.Enum),
          TemplateField('ConnectionFailed', 0, FieldType.Enum),
          TemplateField('RestoreFailed', 0, FieldType.Enum),
          TemplateField('AuthFailed', 0, FieldType.Enum)
        ],
        refType: RefType.Enum),
    TemplateType(
        'Customer',
        'cf0283ae628fe660644535e75fd9b827',
        [
          TemplateField('dob', DATETIME, FieldType.DateTime),
          TemplateField('name', STRING, FieldType.String)
        ],
        refType: RefType.Model,
        creator: () => Customer()),
    TemplateType('Customers', '3b7f91c6891b1101209d2f09836602c4',
        [TemplateField('items', CUSTOMER, FieldType.Ref, collection: true)],
        refType: RefType.Struct, creator: () => Customers()),
    TemplateType('DFile', 'aa71e3af30ba21b5a98feb6b0c43da81', [
      TemplateField('id', STRING, FieldType.String),
      TemplateField('name', STRING, FieldType.String),
      TemplateField('size', INTEGER, FieldType.Integer)
    ]),
    TemplateType('Date', '44749712dbec183e983dcd78a7736c41', []),
    TemplateType('DateTime', '8cf10d2341ed01492506085688270c1e', []),
    TemplateType('Double', 'd909d38d705ce75386dd86e611a82f5b', []),
    TemplateType('Duration', 'e02d2ae03de9d493df2b6b2d2813d302', []),
    TemplateType('Integer', 'a0faef0851b4294c06f2b94bb1cb2044', []),
    TemplateType(
        'Inventory',
        '5b6b778305acce71333e9ae6e1565cdb',
        [
          TemplateField('items', INVENTORYITEM, FieldType.Ref, collection: true)
        ],
        refType: RefType.Struct,
        creator: () => Inventory()),
    TemplateType('InventoryItem', '4cc9492b67a90a2544f0c9981661c26f', [],
        refType: RefType.Model, creator: () => InventoryItem()),
    TemplateType(
        'LoginResult',
        '43b15c92fa28924318ec2dd9b20d65d3',
        [
          TemplateField('failureMessage', STRING, FieldType.String),
          TemplateField('success', BOOLEAN, FieldType.Boolean),
          TemplateField('token', STRING, FieldType.String),
          TemplateField('userObject', USER, FieldType.Ref)
        ],
        refType: RefType.Struct,
        creator: () => LoginResult()),
    TemplateType('String', '27118326006d3829667a400ad23d5d98', []),
    TemplateType('Time', 'a76d4ef5f3f6a672bbfab2865563e530', []),
    TemplateType('Type', 'a1fa27779242b4902f7ae3bdd5c6d508', []),
    TemplateType('User', '8f9bfe9d1345237cb3b2b205864da075', [],
        abstract: true, refType: RefType.Model)
  ];
  static final Map<String, int> _typeMap = Map.fromIterables(
      _types.map((x) => x.name),
      List.generate(_types.length, (index) => index));
  static List<int> allFields(int type) {
    return List.generate(_types[type].fields.length, (index) => index);
  }

  static List<TemplateType> get types {
    return _types;
  }

  static List<Usage> get usages {
    return _usages;
  }

  static String typeString(int val) {
    return _types[val].name;
  }

  static int typeInt(String val) {
    return _typeMap[val];
  }

  static TemplateField _getField(int type, int val) {
    TemplateType _type = _types[type];

/*
_type will have fields with index starting from _type.parentFields.
Anything less needs to be looked up in _type.parent.
*/

    if (val < _type.parentFields) {
      return _getField(_type.parent, val);
    }

/*
The field cannot be in _type's child, so subtract _type.parentField from val, and use that as index.
*/

    int adjustedIndex = val - _type.parentFields;

    return _type.fields[adjustedIndex];
  }

  static String fieldString(int type, int val) {
    return _getField(type, val).name;
  }

  static int fieldType(int type, int val) {
    return _getField(type, val).type;
  }

  static bool isChild(int type, int val) {
    return _getField(type, val).child;
  }

  static int fieldInt(int type, String val) {
    TemplateType _type = _types[type];

    if (_type.fieldMap.containsKey(val)) {
      return _type.fieldMap[val];
    }

    if (_type.parent != null) {
      return fieldInt(_type.parent, val);
    }

    return null;
  }

  static bool isEmbedded(int type) {
    return _types[type].embedded;
  }

  static bool isAbstract(int type) {
    return _types[type].abstract;
  }

  static bool hasParent(int type) {
    return _types[type].parent != null;
  }

  static int parent(int type) {
    return _types[type].parent;
  }

  static T getEnumField<T>(int type, int field) {
    switch (type) {
      case CONNECTIONSTATUS:
        {
          return ConnectionStatus.values[field] as T;
        }
    }
  }
}
