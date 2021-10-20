import '../utils/ObservableState.dart';
import '../classes/ActionUtil.dart';
import '../classes/DBAction.dart';
import '../classes/ElementUtils.dart';
import '../utils/CollectionUtils.dart';
import 'CheckAction.dart';
import 'ScrollView2.dart';
import 'ThemeWrapper.dart';
import 'package:flutter/widgets.dart';

class StartPage extends StatefulWidget {
  StartPage({Key key}) : super(key: key);
  @override
  _StartPageState createState() => _StartPageState();
}

class _StartPageState extends ObservableState<StartPage> {
  List<DBAction> _createAcctions = [];
  List<DBAction> _updateAcctions = [];
  List<DBAction> _deleteAcctions = [];
  @override
  initState() {
    super.initState();

    initListeners();

    enableBuild = true;

    onInit();
  }

  void initListeners() {
    computeCreateAcctions();

    computeUpdateAcctions();

    computeDeleteAcctions();

    this.on(['createAcctions', 'deleteAcctions', 'updateAcctions'], rebuild);
  }

  List<DBAction> get createAcctions {
    return _createAcctions;
  }

  void setCreateAcctions(List<DBAction> val) {
    bool isValChanged = CollectionUtils.isNotEquals(_createAcctions, val);

    if (!isValChanged) {
      return;
    }

    updateObservableColl('createAcctions', _createAcctions, val);

    _createAcctions.clear();

    _createAcctions.addAll(val);

    this.fire('createAcctions', this);
  }

  void addToCreateAcctions(DBAction val, [int index = -1]) {
    if (index == -1) {
      if (!_createAcctions.contains(val)) _createAcctions.add(val);
    } else {
      _createAcctions.insert(index, val);
    }

    fire('createAcctions', this, val, true);

    updateObservable('createAcctions', null, val);
  }

  void removeFromCreateAcctions(DBAction val) {
    _createAcctions.remove(val);

    fire('createAcctions', this, val, false);

    removeObservable('createAcctions', val);
  }

  void computeCreateAcctions() async {
    try {
      setCreateAcctions(List.from((await ActionUtil.createActions())));
    } catch (exception) {
      print(' exception in computeCreateAcctions : ' + exception.toString());

      setCreateAcctions([]);
    }
  }

  List<DBAction> get updateAcctions {
    return _updateAcctions;
  }

  void setUpdateAcctions(List<DBAction> val) {
    bool isValChanged = CollectionUtils.isNotEquals(_updateAcctions, val);

    if (!isValChanged) {
      return;
    }

    updateObservableColl('updateAcctions', _updateAcctions, val);

    _updateAcctions.clear();

    _updateAcctions.addAll(val);

    this.fire('updateAcctions', this);
  }

  void addToUpdateAcctions(DBAction val, [int index = -1]) {
    if (index == -1) {
      if (!_updateAcctions.contains(val)) _updateAcctions.add(val);
    } else {
      _updateAcctions.insert(index, val);
    }

    fire('updateAcctions', this, val, true);

    updateObservable('updateAcctions', null, val);
  }

  void removeFromUpdateAcctions(DBAction val) {
    _updateAcctions.remove(val);

    fire('updateAcctions', this, val, false);

    removeObservable('updateAcctions', val);
  }

  void computeUpdateAcctions() async {
    try {
      setUpdateAcctions(List.from((await ActionUtil.updateActions())));
    } catch (exception) {
      print(' exception in computeUpdateAcctions : ' + exception.toString());

      setUpdateAcctions([]);
    }
  }

  List<DBAction> get deleteAcctions {
    return _deleteAcctions;
  }

  void setDeleteAcctions(List<DBAction> val) {
    bool isValChanged = CollectionUtils.isNotEquals(_deleteAcctions, val);

    if (!isValChanged) {
      return;
    }

    updateObservableColl('deleteAcctions', _deleteAcctions, val);

    _deleteAcctions.clear();

    _deleteAcctions.addAll(val);

    this.fire('deleteAcctions', this);
  }

  void addToDeleteAcctions(DBAction val, [int index = -1]) {
    if (index == -1) {
      if (!_deleteAcctions.contains(val)) _deleteAcctions.add(val);
    } else {
      _deleteAcctions.insert(index, val);
    }

    fire('deleteAcctions', this, val, true);

    updateObservable('deleteAcctions', null, val);
  }

  void removeFromDeleteAcctions(DBAction val) {
    _deleteAcctions.remove(val);

    fire('deleteAcctions', this, val, false);

    removeObservable('deleteAcctions', val);
  }

  void computeDeleteAcctions() async {
    try {
      setDeleteAcctions(List.from((await ActionUtil.deleteActions())));
    } catch (exception) {
      print(' exception in computeDeleteAcctions : ' + exception.toString());

      setDeleteAcctions([]);
    }
  }

  @override
  Widget build(BuildContext context) {
    var cStyle = ThemeWrapper.of(context);

    return ScrollView2(
        child: Row(mainAxisAlignment: MainAxisAlignment.center, children: [
          ScrollView2(
              child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Container(
                        padding: cStyle.tTextViewHeading1PaddingOn,
                        child: Text('Create Actions',
                            style: TextStyle(
                                color: cStyle.tTextViewHeading1ColorOn,
                                fontSize: cStyle.tTextViewHeading1FontSizeOn))),
                    for (var action in this.createAcctions)
                      CheckAction(action: action)
                  ]),
              scrollDirection: Axis.vertical),
          ScrollView2(
              child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Container(
                        padding: cStyle.tTextViewHeading1PaddingOn,
                        child: Text('Update Actions',
                            style: TextStyle(
                                color: cStyle.tTextViewHeading1ColorOn,
                                fontSize: cStyle.tTextViewHeading1FontSizeOn))),
                    for (var action in this.updateAcctions)
                      CheckAction(action: action)
                  ]),
              scrollDirection: Axis.vertical),
          ScrollView2(
              child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Container(
                        padding: cStyle.tTextViewHeading1PaddingOn,
                        child: Text('Delete Actions',
                            style: TextStyle(
                                color: cStyle.tTextViewHeading1ColorOn,
                                fontSize: cStyle.tTextViewHeading1FontSizeOn))),
                    for (var action in this.deleteAcctions)
                      CheckAction(action: action)
                  ]),
              scrollDirection: Axis.vertical)
        ]),
        scrollDirection: Axis.vertical);
  }

  void onInit() {
    ElementUtils.removeInitialLoader();
  }
}
