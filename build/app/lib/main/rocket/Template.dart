import 'TemplateTypes.dart';
import '../classes/ConnectionStatus.dart';
import '../classes/Creatables.dart';
import '../classes/DBResultStatus.dart';
import '../classes/LoginResult.dart';
import '../models/Creatable.dart';
import '../models/Embedded.dart';
import '../models/NonCreatable.dart';

const int BOOLEAN = 0;

const int CONNECTIONSTATUS = 1;

const int CREATABLE = 2;

const int CREATABLES = 3;

const int DBRESULTSTATUS = 4;

const int DFILE = 5;

const int DATE = 6;

const int DATETIME = 7;

const int DOUBLE = 8;

const int DURATION = 9;

const int EMBEDDED = 10;

const int INTEGER = 11;

const int LOGINRESULT = 12;

const int NONCREATABLE = 13;

const int STRING = 14;

const int TIME = 15;

const int TYPE = 16;

const int USER = 17;

class UsageConstants {
  static final int
      STARTPAGE_CREATABLES_SYNCHRONISE_SUBSCRIPTION_ONCREATABLESCHANGE = 0;
  static final int
      STARTPAGE_EVENTHANDLERS_CREATEBASIC_BLOCK_QUERY_LOADCREATABLE = 1;
  static final int
      STARTPAGE_EVENTHANDLERS_CREATECHILDCOLL_BLOCK_QUERY_LOADCREATABLE = 2;
  static final int
      STARTPAGE_EVENTHANDLERS_CREATECHILD_BLOCK_QUERY_LOADCREATABLE = 3;
  static final int STARTPAGE_EVENTHANDLERS_CREATEEMB_BLOCK_QUERY_LOADCREATABLE =
      4;
  static final int
      STARTPAGE_EVENTHANDLERS_CREATEFILE_BLOCK_QUERY_LOADCREATABLE = 5;
  static final int
      STARTPAGE_EVENTHANDLERS_CREATEREFCOLL_BLOCK_QUERY_LOADCREATABLE = 6;
  static final int STARTPAGE_EVENTHANDLERS_CREATEREF_BLOCK_QUERY_LOADCREATABLE =
      7;
  static final int
      STARTPAGE_EVENTHANDLERS_UPDATEBASIC_BLOCK_QUERY_LOADCREATABLE = 8;
  static final int
      STARTPAGE_PROPERTIES_CREATABLES_COMPUTATION_QUERY_GETCREATABLES = 9;
}

class ChannelConstants {
  static const int TOTAL_CHANNEL_COUNT = 0;
  static final List<TemplateChannel> channels = [];
}

class Template {
  static String HASH = 'b10f063aee31eb28498015d7119b636d';
  static List<Usage> _usages = [
    Usage(
        'StartPage_creatables_synchronise_Subscription_onCreatablesChange',
        [
          TypeUsage(CREATABLES, [
            FieldUsage(1, [TypeUsage(CREATABLE, [])])
          ])
        ],
        'ce3faeb2c73f4133a2ace22f5fe1c97d'),
    Usage(
        'StartPage_eventHandlers_createBasic_block_Query_loadCreatable',
        [
          TypeUsage(CREATABLE, [
            FieldUsage(0, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(1, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(2, [
              TypeUsage(EMBEDDED, [FieldUsage(0, [])])
            ]),
            FieldUsage(3, [
              TypeUsage(DFILE, [
                FieldUsage(0, []),
                FieldUsage(3, []),
                FieldUsage(1, []),
                FieldUsage(2, [])
              ])
            ]),
            FieldUsage(4, []),
            FieldUsage(5, []),
            FieldUsage(6, [TypeUsage(CREATABLE, [])]),
            FieldUsage(7, [TypeUsage(CREATABLE, [])])
          ])
        ],
        'de5c9dd79f093bbe6ed4f8487ba015c5'),
    Usage(
        'StartPage_eventHandlers_createChildColl_block_Query_loadCreatable',
        [
          TypeUsage(CREATABLE, [
            FieldUsage(0, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(1, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(2, [
              TypeUsage(EMBEDDED, [FieldUsage(0, [])])
            ]),
            FieldUsage(3, [
              TypeUsage(DFILE, [
                FieldUsage(0, []),
                FieldUsage(3, []),
                FieldUsage(1, []),
                FieldUsage(2, [])
              ])
            ]),
            FieldUsage(4, []),
            FieldUsage(5, []),
            FieldUsage(6, [TypeUsage(CREATABLE, [])]),
            FieldUsage(7, [TypeUsage(CREATABLE, [])])
          ])
        ],
        'de5c9dd79f093bbe6ed4f8487ba015c5'),
    Usage(
        'StartPage_eventHandlers_createChild_block_Query_loadCreatable',
        [
          TypeUsage(CREATABLE, [
            FieldUsage(0, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(1, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(2, [
              TypeUsage(EMBEDDED, [FieldUsage(0, [])])
            ]),
            FieldUsage(3, [
              TypeUsage(DFILE, [
                FieldUsage(0, []),
                FieldUsage(3, []),
                FieldUsage(1, []),
                FieldUsage(2, [])
              ])
            ]),
            FieldUsage(4, []),
            FieldUsage(5, []),
            FieldUsage(6, [TypeUsage(CREATABLE, [])]),
            FieldUsage(7, [TypeUsage(CREATABLE, [])])
          ])
        ],
        'de5c9dd79f093bbe6ed4f8487ba015c5'),
    Usage(
        'StartPage_eventHandlers_createEmb_block_Query_loadCreatable',
        [
          TypeUsage(CREATABLE, [
            FieldUsage(0, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(1, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(2, [
              TypeUsage(EMBEDDED, [FieldUsage(0, [])])
            ]),
            FieldUsage(3, [
              TypeUsage(DFILE, [
                FieldUsage(0, []),
                FieldUsage(3, []),
                FieldUsage(1, []),
                FieldUsage(2, [])
              ])
            ]),
            FieldUsage(4, []),
            FieldUsage(5, []),
            FieldUsage(6, [TypeUsage(CREATABLE, [])]),
            FieldUsage(7, [TypeUsage(CREATABLE, [])])
          ])
        ],
        'de5c9dd79f093bbe6ed4f8487ba015c5'),
    Usage(
        'StartPage_eventHandlers_createFile_block_Query_loadCreatable',
        [
          TypeUsage(CREATABLE, [
            FieldUsage(0, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(1, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(2, [
              TypeUsage(EMBEDDED, [FieldUsage(0, [])])
            ]),
            FieldUsage(3, [
              TypeUsage(DFILE, [
                FieldUsage(0, []),
                FieldUsage(3, []),
                FieldUsage(1, []),
                FieldUsage(2, [])
              ])
            ]),
            FieldUsage(4, []),
            FieldUsage(5, []),
            FieldUsage(6, [TypeUsage(CREATABLE, [])]),
            FieldUsage(7, [TypeUsage(CREATABLE, [])])
          ])
        ],
        'de5c9dd79f093bbe6ed4f8487ba015c5'),
    Usage(
        'StartPage_eventHandlers_createRefColl_block_Query_loadCreatable',
        [
          TypeUsage(CREATABLE, [
            FieldUsage(0, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(1, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(2, [
              TypeUsage(EMBEDDED, [FieldUsage(0, [])])
            ]),
            FieldUsage(3, [
              TypeUsage(DFILE, [
                FieldUsage(0, []),
                FieldUsage(3, []),
                FieldUsage(1, []),
                FieldUsage(2, [])
              ])
            ]),
            FieldUsage(4, []),
            FieldUsage(5, []),
            FieldUsage(6, [TypeUsage(CREATABLE, [])]),
            FieldUsage(7, [TypeUsage(CREATABLE, [])])
          ])
        ],
        'de5c9dd79f093bbe6ed4f8487ba015c5'),
    Usage(
        'StartPage_eventHandlers_createRef_block_Query_loadCreatable',
        [
          TypeUsage(CREATABLE, [
            FieldUsage(0, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(1, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(2, [
              TypeUsage(EMBEDDED, [FieldUsage(0, [])])
            ]),
            FieldUsage(3, [
              TypeUsage(DFILE, [
                FieldUsage(0, []),
                FieldUsage(3, []),
                FieldUsage(1, []),
                FieldUsage(2, [])
              ])
            ]),
            FieldUsage(4, []),
            FieldUsage(5, []),
            FieldUsage(6, [TypeUsage(CREATABLE, [])]),
            FieldUsage(7, [TypeUsage(CREATABLE, [])])
          ])
        ],
        'de5c9dd79f093bbe6ed4f8487ba015c5'),
    Usage(
        'StartPage_eventHandlers_updateBasic_block_Query_loadCreatable',
        [
          TypeUsage(CREATABLE, [
            FieldUsage(0, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(1, [
              TypeUsage(NONCREATABLE, [
                FieldUsage(0, [
                  TypeUsage(EMBEDDED, [FieldUsage(0, [])])
                ]),
                FieldUsage(1, [])
              ])
            ]),
            FieldUsage(2, [
              TypeUsage(EMBEDDED, [FieldUsage(0, [])])
            ]),
            FieldUsage(3, [
              TypeUsage(DFILE, [
                FieldUsage(0, []),
                FieldUsage(3, []),
                FieldUsage(1, []),
                FieldUsage(2, [])
              ])
            ]),
            FieldUsage(4, []),
            FieldUsage(5, []),
            FieldUsage(6, [TypeUsage(CREATABLE, [])]),
            FieldUsage(7, [TypeUsage(CREATABLE, [])])
          ])
        ],
        'de5c9dd79f093bbe6ed4f8487ba015c5'),
    Usage(
        'StartPage_properties_creatables_computation_Query_getCreatables',
        [
          TypeUsage(CREATABLES, [
            FieldUsage(1, [TypeUsage(CREATABLE, [])])
          ])
        ],
        'ce3faeb2c73f4133a2ace22f5fe1c97d')
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
        'Creatable',
        'b1a485d74233df68c29f46f5997e3fdb',
        [
          TemplateField('child', NONCREATABLE, FieldType.Ref, child: true),
          TemplateField('childColl', NONCREATABLE, FieldType.Ref,
              child: true, collection: true),
          TemplateField('emb', EMBEDDED, FieldType.Ref, child: true),
          TemplateField('file', DFILE, FieldType.DFile),
          TemplateField('isBasic', BOOLEAN, FieldType.Boolean),
          TemplateField('name', STRING, FieldType.String),
          TemplateField('ref', CREATABLE, FieldType.Ref),
          TemplateField('refColl', CREATABLE, FieldType.Ref, collection: true)
        ],
        refType: RefType.Model,
        creator: () => Creatable()),
    TemplateType(
        'Creatables',
        'ea47dcd50be8f52cedcc136ae32aff4a',
        [
          TemplateField('errors', STRING, FieldType.String, collection: true),
          TemplateField('items', CREATABLE, FieldType.Ref, collection: true),
          TemplateField('status', DBRESULTSTATUS, FieldType.Enum)
        ],
        refType: RefType.Struct,
        creator: () => Creatables()),
    TemplateType(
        'DBResultStatus',
        'b7ade0f723488459cca2566a1b4959b5',
        [
          TemplateField('Success', 0, FieldType.Enum),
          TemplateField('Errors', 0, FieldType.Enum)
        ],
        refType: RefType.Enum),
    TemplateType('DFile', '71a781845a8ebe8adf67352a573af199', [
      TemplateField('id', STRING, FieldType.String),
      TemplateField('name', STRING, FieldType.String),
      TemplateField('size', INTEGER, FieldType.Integer),
      TemplateField('mimeType', STRING, FieldType.String)
    ]),
    TemplateType('Date', '44749712dbec183e983dcd78a7736c41', []),
    TemplateType('DateTime', '8cf10d2341ed01492506085688270c1e', []),
    TemplateType('Double', 'd909d38d705ce75386dd86e611a82f5b', []),
    TemplateType('Duration', 'e02d2ae03de9d493df2b6b2d2813d302', []),
    TemplateType('Embedded', '87d928bca2046f10bdcc74e683623d09',
        [TemplateField('embName', STRING, FieldType.String)],
        embedded: true, refType: RefType.Model, creator: () => Embedded()),
    TemplateType('Integer', 'a0faef0851b4294c06f2b94bb1cb2044', []),
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
    TemplateType(
        'NonCreatable',
        '1a76d6e9ff4a78aa8a45a5b75288f144',
        [
          TemplateField('emb', EMBEDDED, FieldType.Ref, child: true),
          TemplateField('name', STRING, FieldType.String)
        ],
        refType: RefType.Model,
        creator: () => NonCreatable()),
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

      case DBRESULTSTATUS:
        {
          return DBResultStatus.values[field] as T;
        }
    }
  }
}
