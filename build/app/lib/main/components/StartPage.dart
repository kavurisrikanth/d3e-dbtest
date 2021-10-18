import '../utils/ObservableState.dart';
import '../classes/Creatables.dart';
import '../classes/DisplayUtil.dart';
import '../classes/EventUtil.dart';
import '../classes/NameUtil.dart';
import '../classes/Query.dart';
import '../models/Creatable.dart';
import '../models/NonCreatable.dart';
import '../rocket/MessageDispatch.dart';
import '../rocket/Template.dart';
import '../utils/CollectionUtils.dart';
import 'Button.dart';
import 'ScrollView2.dart';
import 'ThemeWrapper.dart';
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

    onInit();
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
    var cStyle = ThemeWrapper.of(context);

    return ScrollView2(
        child: Column(mainAxisAlignment: MainAxisAlignment.center, children: [
          ScrollView2(
              child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Container(
                        padding: cStyle.tTextViewHeading1PaddingOn,
                        child: Text('All Creatables from Database',
                            style: TextStyle(
                                color: cStyle.tTextViewHeading1ColorOn,
                                fontSize: cStyle.tTextViewHeading1FontSizeOn))),
                    for (var item in this.allCreatables)
                      Text(DisplayUtil.displayCreatable(item))
                  ]),
              scrollDirection: Axis.vertical),
          ScrollView2(
              child:
                  Row(mainAxisAlignment: MainAxisAlignment.center, children: [
                Container(
                    padding: cStyle.tTextViewHeading1PaddingOn,
                    child: Text('Creatable: ',
                        style: TextStyle(
                            color: cStyle.tTextViewHeading1ColorOn,
                            fontSize: cStyle.tTextViewHeading1FontSizeOn))),
                Text(DisplayUtil.displayCreatable(this.creatable))
              ]),
              scrollDirection: Axis.horizontal),
          ScrollView2(
              child:
                  Row(mainAxisAlignment: MainAxisAlignment.center, children: [
                Container(
                    padding: cStyle.tTextViewHeading1PaddingOn,
                    child: Text('NonCreatable: ',
                        style: TextStyle(
                            color: cStyle.tTextViewHeading1ColorOn,
                            fontSize: cStyle.tTextViewHeading1FontSizeOn))),
                Text(DisplayUtil.displayNonCreatable(this.nonCreatable))
              ]),
              scrollDirection: Axis.horizontal),
          ScrollView2(
              child:
                  Row(mainAxisAlignment: MainAxisAlignment.center, children: [
                ScrollView2(
                    child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Container(
                              padding: cStyle.tTextViewHeading1PaddingOn,
                              child: Text('Create',
                                  style: TextStyle(
                                      color: cStyle.tTextViewHeading1ColorOn,
                                      fontSize:
                                          cStyle.tTextViewHeading1FontSizeOn))),
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
                              child:
                                  Text('Create Creatable with Ref Collection')),
                          Button(
                              onPressed: () {
                                createChild();
                              },
                              child: Text('Create Creatable with Child')),
                          Button(
                              onPressed: () {
                                createChildColl();
                              },
                              child: Text(
                                  'Create Creatable with Child Collection')),
                          Button(
                              onPressed: () {
                                createEmb();
                              },
                              child: Text('Create Creatable with Embedded')),
                          Button(
                              onPressed: () {
                                createFile();
                              },
                              child: Text('Create Creatable with File'))
                        ]),
                    scrollDirection: Axis.vertical),
                ScrollView2(
                    child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Container(
                              padding: cStyle.tTextViewHeading1PaddingOn,
                              child: Text('Update',
                                  style: TextStyle(
                                      color: cStyle.tTextViewHeading1ColorOn,
                                      fontSize:
                                          cStyle.tTextViewHeading1FontSizeOn))),
                          Button(
                              onPressed: () {
                                updateBasic();
                              },
                              child: Text('Update basic Creatable')),
                          Button(
                              onPressed: () {
                                updateRef();

                                updateChildColl();

                                updateEmb();

                                updateFile();
                              },
                              child: Text('Update Creatable with Ref')),
                          Button(
                              onPressed: () {
                                updateRefColl();
                              },
                              child:
                                  Text('Update Creatable with Ref Collection')),
                          Button(
                              onPressed: () {
                                updateChild();
                              },
                              child: Text('Update Creatable with Child')),
                          Button(
                              child: Text(
                                  'Update Creatable with Child Collection')),
                          Button(child: Text('Update Creatable with Embedded')),
                          Button(child: Text('Update Creatable with File'))
                        ]),
                    scrollDirection: Axis.vertical)
              ]),
              scrollDirection: Axis.horizontal)
        ]),
        scrollDirection: Axis.vertical);
  }

  void onInit() {
    NameUtil.setIndex(this.allCreatables.length);

    NameUtil.getName();

    NameUtil.getFileName();

    this.allCreatables.firstWhere((c) => c.isBasic, orElse: () => null);
  }

  void createBasic() async {
    Creatable c = (await EventUtil.createBasic());

    if (c != null) {
      String message = 'Creatable creation success';

      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATEBASIC_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      this.setMessage(message);

      this.setCreatable(fromDb);
    } else {
      this.setMessage('Creatable creation failed.');
    }
  }

  void createRef() async {
    Creatable ref = (await EventUtil.createRef());

    if (ref != null) {
      this.setMessage('Reference creation success.');

      this.setCreatable((await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATEREF_BLOCK_QUERY_LOADCREATABLE,
          ref.id)));
    } else {
      this.setMessage('Reference creation failed.');
    }
  }

  void createRefColl() async {
    Creatable c = (await EventUtil.createRefColl());

    if (c != null) {
      this.setMessage('Reference creation success.');

      this.setCreatable((await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATEREFCOLL_BLOCK_QUERY_LOADCREATABLE,
          c.id)));
    } else {
      this.setMessage('Reference creation failed.');
    }
  }

  void createChild() async {
    Creatable ref = (await EventUtil.createChild());

    if (ref != null) {
      this.setMessage('With Child creation success.');

      this.setCreatable((await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATECHILD_BLOCK_QUERY_LOADCREATABLE,
          ref.id)));
    } else {
      this.setMessage('With Child creation failed.');
    }
  }

  void createChildColl() async {
    Creatable c = (await EventUtil.createChildColl());

    if (c != null) {
      this.setMessage('With child collection creation success.');

      this.setCreatable((await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATECHILDCOLL_BLOCK_QUERY_LOADCREATABLE,
          c.id)));
    } else {
      this.setMessage('With child collection creation failed.');
    }
  }

  void createEmb() async {
    Creatable ref = (await EventUtil.createEmb());

    if (ref != null) {
      this.setMessage('With Embedded creation success.');

      this.setCreatable((await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATEEMB_BLOCK_QUERY_LOADCREATABLE,
          ref.id)));
    } else {
      this.setMessage('With Embedded creation failed.');
    }
  }

  void createFile() async {
    Creatable ref = (await EventUtil.createFile());

    if (ref != null) {
      this.setMessage('With File creation success.');

      this.setCreatable((await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATEFILE_BLOCK_QUERY_LOADCREATABLE,
          ref.id)));
    } else {
      this.setMessage('With File creation failed.');
    }
  }

  void updateBasic() async {
    Creatable obj = (await EventUtil.updateBasic(this.allCreatables));

    if (obj != null) {
      String message = 'Creatable update success';

      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_UPDATEBASIC_BLOCK_QUERY_LOADCREATABLE,
          obj.id));

      this.setMessage(message);

      this.setCreatable(fromDb);
    } else {
      this.setMessage('Creatable update failed.');
    }
  }

  void updateRef() async {
    Creatable ref = (await EventUtil.updateRef(this.allCreatables));

    if (ref != null) {
      this.setMessage('Creatable update with ref success.');

      this.setCreatable((await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_UPDATEREF_BLOCK_QUERY_LOADCREATABLE,
          ref.id)));
    } else {
      this.setMessage('Creatable update with ref failed.');
    }
  }

  void updateRefColl() async {
    Creatable ref = (await EventUtil.updateRefColl(this.allCreatables));

    if (ref != null) {
      this.setMessage('Creatable update with ref collection success.');

      this.setCreatable((await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_UPDATEREFCOLL_BLOCK_QUERY_LOADCREATABLE,
          ref.id)));
    } else {
      this.setMessage('Creatable update with ref collection failed.');
    }
  }

  void updateChild() async {
    Creatable ref = (await EventUtil.updateChild(this.allCreatables));

    if (ref != null) {
      this.setMessage('Creatable update with child success.');

      this.setCreatable((await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_UPDATECHILD_BLOCK_QUERY_LOADCREATABLE,
          ref.id)));
    } else {
      this.setMessage('Creatable update with child failed.');
    }
  }

  void updateChildColl() async {
    Creatable ref = (await EventUtil.updateChildColl(this.allCreatables));

    if (ref != null) {
      this.setMessage('Creatable update with child collection success.');

      this.setCreatable((await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_UPDATECHILDCOLL_BLOCK_QUERY_LOADCREATABLE,
          ref.id)));
    } else {
      this.setMessage('Creatable update with child collection failed.');
    }
  }

  void updateEmb() async {
    Creatable ref = (await EventUtil.updateEmb(this.allCreatables));

    if (ref != null) {
      this.setMessage('Creatable update with embedded success.');

      this.setCreatable((await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_UPDATEEMB_BLOCK_QUERY_LOADCREATABLE,
          ref.id)));
    } else {
      this.setMessage('Creatable update with embedded failed.');
    }
  }

  void updateFile() async {
    Creatable ref = (await EventUtil.updateFile(this.allCreatables));

    if (ref != null) {
      this.setMessage('Creatable update with file success.');

      this.setCreatable((await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_UPDATEFILE_BLOCK_QUERY_LOADCREATABLE,
          ref.id)));
    } else {
      this.setMessage('Creatable update with file failed.');
    }
  }

  @override
  void dispose() {
    MessageDispatch.get().dispose(creatables);

    super.dispose();
  }
}
