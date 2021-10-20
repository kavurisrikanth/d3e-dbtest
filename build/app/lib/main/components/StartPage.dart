import '../utils/ObservableState.dart';
import '../classes/Creatables.dart';
import '../classes/DisplayUtil.dart';
import '../classes/ElementUtils.dart';
import '../classes/EqualsUtil.dart';
import '../classes/EventUtil.dart';
import '../classes/NameUtil.dart';
import '../classes/Query.dart';
import '../models/Creatable.dart';
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
  bool _hasError = false;
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

    this.on(['allCreatables', 'hasError', 'message'], rebuild);
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

  bool get hasError {
    return _hasError;
  }

  void setHasError(bool val) {
    bool isValChanged = _hasError != val;

    if (!isValChanged) {
      return;
    }

    _hasError = val;

    this.fire('hasError', this);
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
                      Container(
                          padding: cStyle.tTextViewLabelPaddingOn,
                          child: Text(DisplayUtil.displayCreatable(item),
                              textAlign: cStyle.tTextViewLabelTextAlignOn,
                              style: TextStyle(
                                  color: cStyle.tTextViewLabelColorOn,
                                  fontSize: cStyle.tTextViewLabelFontSizeOn)))
                  ]),
              scrollDirection: Axis.vertical),
          if (this.hasError)
            ScrollView2(
                child:
                    Row(mainAxisAlignment: MainAxisAlignment.center, children: [
                  Container(
                      padding: cStyle.tTextViewErrorMessagePaddingOn,
                      child: Text('Message: ',
                          textAlign: cStyle.tTextViewErrorMessageTextAlignOn,
                          style: TextStyle(
                              color: cStyle.tTextViewErrorMessageColorOn))),
                  Container(
                      padding: cStyle.tTextViewErrorMessagePaddingOn,
                      child: Text(
                          (this.message == null || this.message.length == 0)
                              ? 'No Message'
                              : this.message,
                          textAlign: cStyle.tTextViewErrorMessageTextAlignOn,
                          style: TextStyle(
                              color: cStyle.tTextViewErrorMessageColorOn)))
                ]),
                scrollDirection: Axis.horizontal)
          else
            ScrollView2(
                child:
                    Row(mainAxisAlignment: MainAxisAlignment.center, children: [
                  Container(
                      padding: cStyle.tTextViewSuccessMessagePaddingOn,
                      child: Text('Message: ',
                          textAlign: cStyle.tTextViewSuccessMessageTextAlignOn,
                          style: TextStyle(
                              color: cStyle.tTextViewSuccessMessageColorOn,
                              fontSize:
                                  cStyle.tTextViewSuccessMessageFontSizeOn))),
                  Container(
                      padding: cStyle.tTextViewSuccessMessagePaddingOn,
                      child: Text(
                          (this.message == null || this.message.length == 0)
                              ? 'No Message'
                              : this.message,
                          textAlign: cStyle.tTextViewSuccessMessageTextAlignOn,
                          style: TextStyle(
                              color: cStyle.tTextViewSuccessMessageColorOn,
                              fontSize:
                                  cStyle.tTextViewSuccessMessageFontSizeOn)))
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
                              onPressed: () {
                                updateChildColl();
                              },
                              child: Text(
                                  'Update Creatable with Child Collection')),
                          Button(
                              onPressed: () {
                                updateEmb();
                              },
                              child: Text('Update Creatable with Embedded')),
                          Button(
                              onPressed: () {
                                updateFile();
                              },
                              child: Text('Update Creatable with File'))
                        ]),
                    scrollDirection: Axis.vertical),
                ScrollView2(
                    child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Container(
                              padding: cStyle.tTextViewHeading1PaddingOn,
                              child: Text('Delete',
                                  style: TextStyle(
                                      color: cStyle.tTextViewHeading1ColorOn,
                                      fontSize:
                                          cStyle.tTextViewHeading1FontSizeOn))),
                          Button(
                              onPressed: () {
                                deleteBasic();
                              },
                              child: Text('Delete basic Creatable')),
                          Button(
                              onPressed: () {
                                deleteRef();
                              },
                              child: Text('Delete Creatable with Ref')),
                          Button(
                              onPressed: () {
                                deleteRefColl();
                              },
                              child:
                                  Text('Delete Creatable with Ref Collection')),
                          Button(
                              onPressed: () {
                                deleteChild();
                              },
                              child: Text('Delete Creatable with Child')),
                          Button(
                              onPressed: () {
                                deleteChildColl();
                              },
                              child: Text(
                                  'Delete Creatable with Child Collection')),
                          Button(
                              onPressed: () {
                                deleteEmb();
                              },
                              child: Text('Delete Creatable with Embedded')),
                          Button(
                              onPressed: () {
                                deleteFile();
                              },
                              child: Text('Delete Creatable with File'))
                        ]),
                    scrollDirection: Axis.vertical)
              ]),
              scrollDirection: Axis.horizontal)
        ]),
        scrollDirection: Axis.vertical);
  }

  void onInit() {
    ElementUtils.removeInitialLoader();

    NameUtil.setIndex(this.allCreatables.length);

    NameUtil.getName();

    NameUtil.getFileName();

    this.allCreatables.firstWhere((c) => c.isBasic, orElse: () => null);
  }

  void reset() {
    this.setMessage(null);

    this.setHasError(false);
  }

  void createBasic() async {
    reset();

    Creatable c = (await EventUtil.createBasic());

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATEBASIC_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void createRef() async {
    reset();

    Creatable c = (await EventUtil.createRef());

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATEREF_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void createRefColl() async {
    reset();

    Creatable c = (await EventUtil.createRefColl());

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATEREFCOLL_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void createChild() async {
    reset();

    Creatable c = (await EventUtil.createChild());

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATECHILD_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void createChildColl() async {
    reset();

    Creatable c = (await EventUtil.createChildColl());

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATECHILDCOLL_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void createEmb() async {
    reset();

    Creatable c = (await EventUtil.createEmb());

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATEEMB_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void createFile() async {
    reset();

    Creatable c = (await EventUtil.createFile());

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_CREATEFILE_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void updateBasic() async {
    reset();

    Creatable c = (await EventUtil.updateBasic(this.allCreatables));

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_UPDATEBASIC_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void updateRef() async {
    reset();

    Creatable c = (await EventUtil.updateRef(this.allCreatables));

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_UPDATEREF_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void updateRefColl() async {
    reset();

    Creatable c = (await EventUtil.updateRefColl(this.allCreatables));

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_UPDATEREFCOLL_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void updateChild() async {
    reset();

    Creatable c = (await EventUtil.updateChild(this.allCreatables));

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_UPDATECHILD_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void updateChildColl() async {
    reset();

    Creatable c = (await EventUtil.updateChildColl(this.allCreatables));

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_UPDATECHILDCOLL_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void updateEmb() async {
    reset();

    Creatable c = (await EventUtil.updateEmb(this.allCreatables));

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_UPDATEEMB_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void updateFile() async {
    reset();

    Creatable c = (await EventUtil.updateFile(this.allCreatables));

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_UPDATEFILE_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void deleteBasic() async {
    reset();

    Creatable c = (await EventUtil.deleteBasic(this.allCreatables));

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_DELETEBASIC_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void deleteRef() async {
    reset();

    Creatable c = (await EventUtil.deleteRef(this.allCreatables));

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_DELETEREF_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void deleteRefColl() async {
    reset();

    Creatable c = (await EventUtil.deleteRefColl(this.allCreatables));

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_DELETEREFCOLL_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void deleteChild() async {
    reset();

    Creatable c = (await EventUtil.deleteChild(this.allCreatables));

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_DELETECHILD_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void deleteChildColl() async {
    reset();

    Creatable c = (await EventUtil.deleteChildColl(this.allCreatables));

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_DELETECHILDCOLL_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void deleteEmb() async {
    reset();

    Creatable c = (await EventUtil.deleteEmb(this.allCreatables));

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_DELETEEMB_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  void deleteFile() async {
    reset();

    Creatable c = (await EventUtil.deleteFile(this.allCreatables));

    if (c != null) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants
              .STARTPAGE_EVENTHANDLERS_DELETEFILE_BLOCK_QUERY_LOADCREATABLE,
          c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        this.setMessage('Creatable creation success');
      } else {
        this.setMessage('Equals check failed for ' +
            DisplayUtil.displayCreatable(c) +
            ' and ' +
            DisplayUtil.displayCreatable(fromDb));

        this.setHasError(true);
      }
    } else {
      this.setMessage('Creatable creation failed.');

      this.setHasError(true);
    }
  }

  @override
  void dispose() {
    MessageDispatch.get().dispose(creatables);

    super.dispose();
  }
}
