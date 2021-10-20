import '../utils/ObservableState.dart';
import '../classes/DBAction.dart';
import 'Button.dart';
import 'ScrollView2.dart';
import 'ThemeWrapper.dart';
import 'package:flutter/widgets.dart';

class CheckAction extends StatefulWidget {
  final DBAction action;
  CheckAction({Key key, this.action}) : super(key: key);
  @override
  _CheckActionState createState() => _CheckActionState();
}

class _CheckActionState extends ObservableState<CheckAction> {
  @override
  initState() {
    super.initState();

    initListeners();

    enableBuild = true;
  }

  void initListeners() {
    updateSyncProperty('action', widget.action);

    this.on(
        ['action.actionRun', 'action.error', 'action.success', 'action.title'],
        rebuild);
  }

  void didUpdateWidget(CheckAction oldWidget) {
    super.didUpdateWidget(oldWidget);

    if (oldWidget.action != action) {
      updateObservable('action', oldWidget.action, action);
    }
  }

  DBAction get action {
    return this.widget.action;
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
                    Button(
                        onPressed: () {
                          runAction();
                        },
                        child: Container(
                            padding: cStyle.tTextViewLabelPaddingOn,
                            child: Text(this.action.title,
                                textAlign: cStyle.tTextViewLabelTextAlignOn,
                                style: TextStyle(
                                    color: cStyle.tTextViewLabelColorOn,
                                    fontSize:
                                        cStyle.tTextViewLabelFontSizeOn)))),
                    if (this.action.actionRun && !this.action.success)
                      ScrollView2(
                          child: Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Container(
                                    padding:
                                        cStyle.tTextViewErrorMessagePaddingOn,
                                    child: Text(this.action.error,
                                        textAlign: cStyle
                                            .tTextViewErrorMessageTextAlignOn,
                                        style: TextStyle(
                                            color: cStyle
                                                .tTextViewErrorMessageColorOn)))
                              ]),
                          scrollDirection: Axis.horizontal),
                    if (this.action.actionRun && this.action.success)
                      ScrollView2(
                          child: Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Container(
                                    padding:
                                        cStyle.tTextViewSuccessMessagePaddingOn,
                                    child: Text('Success',
                                        textAlign: cStyle
                                            .tTextViewSuccessMessageTextAlignOn,
                                        style: TextStyle(
                                            color: cStyle
                                                .tTextViewSuccessMessageColorOn,
                                            fontSize: cStyle
                                                .tTextViewSuccessMessageFontSizeOn)))
                              ]),
                          scrollDirection: Axis.horizontal)
                  ]),
              scrollDirection: Axis.vertical)
        ]),
        scrollDirection: Axis.vertical);
  }

  void runAction() {
    this.action.exe(this.action);
  }
}
