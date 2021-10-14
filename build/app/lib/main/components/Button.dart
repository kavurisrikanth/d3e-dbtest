import '../utils/ObservableState.dart';
import '../utils/ObjectObservable.dart';
import '../classes/core.dart';
import 'CustomCursor.dart';
import 'dart:ui';
import 'package:flutter/gestures.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

typedef void _ButtonOnHoverExit();

typedef void _ButtonOnHover();

typedef void _ButtonOnPressed();

typedef void _ButtonOnLongPressed();

typedef void _ChildWrapperOnKey(FocusNode focusNode, RawKeyEvent event);

typedef void _ChildWrapperOnTap();

typedef void _ChildWrapperOnLongPress();

typedef void _ChildWrapperOnExit(PointerExitEvent event);

typedef void _ChildWrapperOnHover(PointerHoverEvent event);

class Button extends StatefulWidget {
  final bool disable;
  final Color backgroundColor;
  final double cornerRadius;
  final _ButtonOnHoverExit onHoverExit;
  final _ButtonOnHover onHover;
  final _ButtonOnPressed onPressed;
  final _ButtonOnLongPressed onLongPressed;
  final Widget child;
  Button(
      {Key key,
      this.disable = false,
      this.backgroundColor,
      this.cornerRadius = 3.0,
      this.child,
      this.onHoverExit,
      this.onHover,
      this.onPressed,
      this.onLongPressed})
      : super(key: key);
  @override
  _ButtonState createState() => _ButtonState();
}

class ButtonRefs {
  ChildWrapperState childWrapper = ChildWrapperState();
  ButtonRefs();
}

class ChildWrapperState extends ObjectObservable {
  bool _focus = false;
  ChildWrapperState();
  bool get focus {
    return _focus;
  }

  setFocus(bool val) {
    bool isValChanged = _focus != val;

    if (!isValChanged) {
      return;
    }

    _focus = val;

    fire('focus', this);
  }
}

class ChildWrapperWithState extends StatefulWidget {
  final ButtonRefs state;
  final Widget child;
  final _ChildWrapperOnLongPress onHandleLongPress;
  final _ChildWrapperOnTap onHandleTap;
  final _ChildWrapperOnExit onPointerExit;
  final _ChildWrapperOnHover onPointerHover;
  final _ChildWrapperOnKey onRawEnterKeyPressed;
  final Color backgroundColor;
  final bool childPresent;
  final double cornerRadius;
  final bool disable;
  ChildWrapperWithState(
      {Key key,
      this.state,
      this.child,
      this.onHandleLongPress,
      this.onHandleTap,
      this.onPointerExit,
      this.onPointerHover,
      this.onRawEnterKeyPressed,
      this.backgroundColor,
      this.childPresent,
      this.cornerRadius,
      this.disable})
      : super(key: key);
  @override
  _ChildWrapperWithState createState() => _ChildWrapperWithState();
}

class _ChildWrapperWithState extends ObservableState<ChildWrapperWithState> {
  @override
  initState() {
    super.initState();

    updateObservable('childWrapper', null, childWrapper);

    initListeners();

    enableBuild = true;
  }

  void initListeners() {
    this.on(['childPresent', 'childWrapper', 'childWrapper.focus'], rebuild);
  }

  void childWrapperOnFocusChange(val) => childWrapper.setFocus(val);
  _ChildWrapperOnLongPress get onHandleLongPress =>
      this.widget.onHandleLongPress;
  _ChildWrapperOnTap get onHandleTap => this.widget.onHandleTap;
  _ChildWrapperOnExit get onPointerExit => this.widget.onPointerExit;
  _ChildWrapperOnHover get onPointerHover => this.widget.onPointerHover;
  _ChildWrapperOnKey get onRawEnterKeyPressed =>
      this.widget.onRawEnterKeyPressed;
  Widget get child => widget.child;
  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Focus(
        child: GestureDetector(
            onTap: () {
              onHandleTap();
            },
            onLongPress: () {
              onHandleLongPress();
            },
            child: MouseRegion(
                onExit: (event) {
                  onPointerExit(event);
                },
                onHover: (event) {
                  onPointerHover(event);
                },
                child: Container(
                    padding: EdgeInsets.only(
                        left: 4.0, top: 4.0, right: 4.0, bottom: 4.0),
                    decoration: BoxDecoration(
                        border: childWrapper.focus
                            ? Border.all(
                                color: HexColor.fromHex('42000000'), width: 1.0)
                            : null,
                        borderRadius: BorderRadius.only(
                            topLeft: RadiusExt.elliptical(
                                x: this.cornerRadius, y: this.cornerRadius),
                            topRight: RadiusExt.elliptical(
                                x: this.cornerRadius, y: this.cornerRadius),
                            bottomLeft: RadiusExt.elliptical(
                                x: this.cornerRadius, y: this.cornerRadius),
                            bottomRight: RadiusExt.elliptical(
                                x: this.cornerRadius, y: this.cornerRadius)),
                        color: this.backgroundColor != null
                            ? this.backgroundColor
                            : HexColor.fromHex('00000000')),
                    foregroundDecoration: this.disable
                        ? BoxDecoration(
                            color: HexColor.fromHex('61dddddd'),
                            borderRadius: BorderRadius.only(
                                topLeft: RadiusExt.elliptical(
                                    x: this.cornerRadius, y: this.cornerRadius),
                                topRight: RadiusExt.elliptical(x: this.cornerRadius, y: this.cornerRadius),
                                bottomLeft: RadiusExt.elliptical(x: this.cornerRadius, y: this.cornerRadius),
                                bottomRight: RadiusExt.elliptical(x: this.cornerRadius, y: this.cornerRadius)))
                        : null,
                    child: Center(widthFactor: 1.0, heightFactor: 1.0, child: childPresent ? this.widget.child : Container())))),
        onKey: (focusNode, event) {
          onRawEnterKeyPressed(focusNode, event);

          return KeyEventResult.handled;
        },
        onFocusChange: (val) {
          childWrapperOnFocusChange(val);
        });
  }

  ButtonRefs get state => widget.state;
  Color get backgroundColor => widget.backgroundColor;
  bool get childPresent => widget.childPresent;
  ChildWrapperState get childWrapper => widget.state.childWrapper;
  double get cornerRadius => widget.cornerRadius;
  bool get disable => widget.disable;
}

class _ButtonState extends ObservableState<Button> {
  ButtonRefs state = ButtonRefs();
  @override
  initState() {
    super.initState();

    initListeners();

    enableBuild = true;
  }

  void initListeners() {
    this.on(['childPresent'], rebuild);
  }

  bool get disable {
    return this.widget.disable;
  }

  Color get backgroundColor {
    return this.widget.backgroundColor;
  }

  double get cornerRadius {
    return this.widget.cornerRadius;
  }

  @override
  Widget build(BuildContext context) {
    return CustomCursor(
        child: ChildWrapperWithState(
            state: state,
            child: child,
            onHandleLongPress: onHandleLongPress,
            onHandleTap: onHandleTap,
            onPointerExit: onPointerExit,
            onPointerHover: onPointerHover,
            onRawEnterKeyPressed: onRawEnterKeyPressed,
            backgroundColor: backgroundColor,
            childPresent: childPresent,
            cornerRadius: cornerRadius,
            disable: disable));
  }

  void onPointerExit(PointerExitEvent event) {
    if (this.onHoverExit != null && !this.disable) {
      this.onHoverExit();
    }
  }

  void onPointerHover(PointerHoverEvent event) {
    if (this.onHover != null && !this.disable) {
      this.onHover();
    }
  }

  void onHandleTap() {
    if (this.onPressed != null && !this.disable) {
      this.onPressed();
    }
  }

  void onHandleLongPress() {
    if (this.onLongPressed != null && !this.disable) {
      this.onLongPressed();
    }
  }

  void onRawEnterKeyPressed(FocusNode focusNode, RawKeyEvent event) {
    if (event is RawKeyDownEvent && !this.disable) {
      if (event.logicalKey == LogicalKeyboardKey.enter ||
          event.logicalKey == LogicalKeyboardKey.space) {
        if (this.onPressed != null) {
          this.onPressed();
        }
      }
    }
  }

  _ButtonOnHoverExit get onHoverExit => this.widget.onHoverExit;
  _ButtonOnHover get onHover => this.widget.onHover;
  _ButtonOnPressed get onPressed => this.widget.onPressed;
  _ButtonOnLongPressed get onLongPressed => this.widget.onLongPressed;
  bool get childPresent => this.widget.child != null;
  Widget get child => widget.child;
}
