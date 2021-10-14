import '../utils/ObservableState.dart';
import '../classes/DBResult.dart';
import '../classes/DBResultStatus.dart';
import '../classes/DisplayUtil.dart';
import '../classes/Query.dart';
import '../models/Creatable.dart';
import '../models/NonCreatable.dart';
import '../rocket/Template.dart';
import 'Button.dart';
import 'package:flutter/widgets.dart';

class StartPage extends StatefulWidget {
  StartPage({Key key}) : super(key: key);
  @override
  _StartPageState createState() => _StartPageState();
}

class _StartPageState extends ObservableState<StartPage> {
  String _message = '';
  Creatable _creatable;
  NonCreatable _nonCreatable;
  @override
  initState() {
    super.initState();

    initListeners();

    enableBuild = true;
  }

  void initListeners() {
    this.on([
      'creatable',
      'creatable.child',
      'creatable.child.emb',
      'creatable.child.name',
      'creatable.childColl',
      'creatable.childColl.emb',
      'creatable.childColl.name',
      'creatable.emb',
      'creatable.file',
      'creatable.name',
      'creatable.ref',
      'creatable.refColl',
      'nonCreatable',
      'nonCreatable.name'
    ], rebuild);
  }

  String get message {
    return _message;
  }

  void setMessage(String val) {
    bool isValChanged = _message != val;

    if (!isValChanged) {
      return;
    }

    _message = val;

    this.fire('message', this);
  }

  Creatable get creatable {
    return _creatable;
  }

  void setCreatable(Creatable val) {
    bool isValChanged = _creatable != val;

    if (!isValChanged) {
      return;
    }

    updateObservable('creatable', _creatable, val);

    _creatable = val;

    this.fire('creatable', this);
  }

  NonCreatable get nonCreatable {
    return _nonCreatable;
  }

  void setNonCreatable(NonCreatable val) {
    bool isValChanged = _nonCreatable != val;

    if (!isValChanged) {
      return;
    }

    updateObservable('nonCreatable', _nonCreatable, val);

    _nonCreatable = val;

    this.fire('nonCreatable', this);
  }

  @override
  Widget build(BuildContext context) {
    return Column(children: [
      Row(children: [
        Text('Creatable'),
        Text(DisplayUtil.displayCreatable(this.creatable))
      ]),
      Row(children: [
        Text('NonCreatable'),
        Text(this.creatable == null
            ? 'Unnamed NonCreatable'
            : this.nonCreatable.name)
      ]),
      Button(
          onPressed: () {
            createBasic();
          },
          child: Text('Create basic Creatable')),
      Button(
          onPressed: () {
            basicNC();
          },
          child: Text('Create basic NonCreatable'))
    ]);
  }

  void createBasic() async {
    Creatable c = Creatable();

    DBResult r = (await c.save());

    if (r.status == DBResultStatus.Success) {
      String message = 'Creatable creation success';

      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATEBASIC_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      this.setMessage(message);

      this.setCreatable(c);
    } else {
      this.setMessage('Creatable creation failed.');
    }
  }

  void basicNC() async {
    NonCreatable c = NonCreatable();

    DBResult r = (await c.save());

    if (r.status == DBResultStatus.Success) {
      String message = 'NonCreatable creation success';

      this.setMessage(message);
    } else {
      this.setMessage('NonCreatable creation failed.');
    }
  }
}
