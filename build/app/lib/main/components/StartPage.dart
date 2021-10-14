import '../utils/ObservableState.dart';
import '../classes/Creatables.dart';
import '../classes/DBResult.dart';
import '../classes/DBResultStatus.dart';
import '../classes/DisplayUtil.dart';
import '../classes/NameUtil.dart';
import '../classes/Query.dart';
import '../models/Creatable.dart';
import '../models/NonCreatable.dart';
import '../rocket/MessageDispatch.dart';
import '../rocket/Template.dart';
import '../utils/CollectionUtils.dart';
import 'Button.dart';
import 'ScrollView2.dart';
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
  Creatables _creatables;
  List<Creatable> _allCreatables = [];
  @override
  initState() {
    super.initState();

    initListeners();

    enableBuild = true;
  }

  void initListeners() {
    computeCreatables();

    this.on(['creatables', 'creatables.items'], computeAllCreatables);

    computeAllCreatables();

    this.on([
      'allCreatables',
      'creatable',
      'creatable.child',
      'creatable.child.emb',
      'creatable.child.emb.embName',
      'creatable.child.name',
      'creatable.childColl',
      'creatable.childColl.emb',
      'creatable.childColl.emb.embName',
      'creatable.childColl.name',
      'creatable.emb',
      'creatable.emb.embName',
      'creatable.file',
      'creatable.name',
      'creatable.ref',
      'creatable.refColl',
      'nonCreatable',
      'nonCreatable.emb',
      'nonCreatable.emb.embName',
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

  Creatables get creatables {
    return _creatables;
  }

  void setCreatables(Creatables val) {
    bool isValChanged = _creatables != val;

    if (!isValChanged) {
      return;
    }

    updateObservable('creatables', _creatables, val);

    MessageDispatch.get().dispose(this._creatables);

    _creatables = val;

    this.fire('creatables', this);
  }

  void computeCreatables() async {
    try {
      setCreatables((await Query.get().getCreatables(
          UsageConstants
              .STARTPAGE_PROPERTIES_CREATABLES_COMPUTATION_QUERY_GETCREATABLES,
          synchronize: true)));
    } catch (exception) {
      print(' exception in computeCreatables : ' + exception.toString());

      setCreatables(null);
    }
  }

  List<Creatable> get allCreatables {
    return _allCreatables;
  }

  void setAllCreatables(List<Creatable> val) {
    bool isValChanged = CollectionUtils.isNotEquals(_allCreatables, val);

    if (!isValChanged) {
      return;
    }

    updateObservableColl('allCreatables', _allCreatables, val);

    _allCreatables.clear();

    _allCreatables.addAll(val);

    this.fire('allCreatables', this);
  }

  void addToAllCreatables(Creatable val, [int index = -1]) {
    if (index == -1) {
      if (!_allCreatables.contains(val)) _allCreatables.add(val);
    } else {
      _allCreatables.insert(index, val);
    }

    fire('allCreatables', this, val, true);

    updateObservable('allCreatables', null, val);
  }

  void removeFromAllCreatables(Creatable val) {
    _allCreatables.remove(val);

    fire('allCreatables', this, val, false);

    removeObservable('allCreatables', val);
  }

  void computeAllCreatables() {
    try {
      setAllCreatables(List.from(this.creatables.items));
    } catch (exception) {
      print(' exception in computeAllCreatables : ' + exception.toString());

      setAllCreatables([]);
    }
  }

  @override
  Widget build(BuildContext context) {
    return ScrollView2(
        child: Column(mainAxisAlignment: MainAxisAlignment.center, children: [
          ScrollView2(
              child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    for (var item in this.allCreatables)
                      Text(DisplayUtil.displayCreatable(item))
                  ]),
              scrollDirection: Axis.horizontal),
          ScrollView2(
              child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text('Creatable'),
                    Text(DisplayUtil.displayCreatable(this.creatable))
                  ]),
              scrollDirection: Axis.horizontal),
          ScrollView2(
              child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text('NonCreatable'),
                    Text(DisplayUtil.displayNonCreatable(this.nonCreatable))
                  ]),
              scrollDirection: Axis.horizontal),
          Button(
              onPressed: () {
                createBasic();
              },
              child: Text('Create basic Creatable')),
          Button(
              onPressed: () {
                createRef();
              },
              child: Text('Create Creatable with Ref')),
          Button(
              onPressed: () {
                createRefColl();
              },
              child: Text('Create Creatable with Ref Collection')),
          Button(
              onPressed: () {
                basicNC();
              },
              child: Text('Create basic NonCreatable'))
        ]),
        scrollDirection: Axis.vertical);
  }

  void createBasic() async {
    Creatable c = Creatable(name: NameUtil.getName());

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

  void createRef() async {
    Creatable ref = Creatable(name: NameUtil.getName());

    DBResult r = (await ref.save());

    if (r.status == DBResultStatus.Success) {
      this.setMessage('Reference creation success.');

      this.setCreatable((await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATEREF_BLOCK_QUERY_LOADCREATABLE,
          ref.id)));

      Creatable c = Creatable(name: NameUtil.getName(), ref: ref);

      r = (await c.save());

      if (r.status == DBResultStatus.Success) {
        this.setMessage('Creatable creation success.');

        this.setCreatable((await Query.get().getCreatableById(
            UsageConstants
                .STARTPAGE_EVENTHANDLERS_CREATEREF_BLOCK_QUERY_LOADCREATABLE,
            c.id)));
      } else {
        this.setMessage('Creatable creation failed.');
      }
    } else {
      this.setMessage('Reference creation failed.');
    }
  }

  void createRefColl() async {
    int i = 3;

    List<Creatable> refs = [];

    for (int x = 0; x < 3; x++) {
      Creatable ref = Creatable(name: NameUtil.getName());

      DBResult r = (await ref.save());

      if (r.status == DBResultStatus.Success) {
        this.setMessage('Reference creation success.');

        this.setCreatable((await Query.get().getCreatableById(
            UsageConstants
                .STARTPAGE_EVENTHANDLERS_CREATEREFCOLL_BLOCK_QUERY_LOADCREATABLE,
            ref.id)));
      } else {
        this.setMessage('Reference creation failed.');
      }
    }

    Creatable c = Creatable(name: NameUtil.getName(), refColl: refs);

    DBResult r = (await c.save());

    if (r.status == DBResultStatus.Success) {
      this.setMessage('Reference creation success.');

      this.setCreatable((await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATEREFCOLL_BLOCK_QUERY_LOADCREATABLE,
          c.id)));
    } else {
      this.setMessage('Reference creation failed.');
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

  @override
  void dispose() {
    MessageDispatch.get().dispose(creatables);

    super.dispose();
  }
}
