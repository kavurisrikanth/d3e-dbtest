import '../utils/DBObject.dart';
import 'core.dart';

class DBAction extends DBObject {
  int _id = 0;
  String _title = '';
  bool _actionRun = false;
  bool _success = false;
  String _error = '';
  Consumer _exe;
  DBAction(
      {bool actionRun,
      String error,
      Consumer exe,
      bool success,
      String title}) {
    if (actionRun != null) {
      this.setActionRun(actionRun);
    }

    if (error != null) {
      this.setError(error);
    }

    if (exe != null) {
      this.setExe(exe);
    }

    if (success != null) {
      this.setSuccess(success);
    }

    if (title != null) {
      this.setTitle(title);
    }
  }
  int get id {
    return _id;
  }

  set id(int id) {
    this._id = id;
  }

  String get d3eType {
    return 'DBAction';
  }

  void clear() {}
  @override
  void initListeners() {
    super.initListeners();
  }

  String get title {
    return _title;
  }

  void setTitle(String val) {
    bool isValChanged = _title != val;

    if (!isValChanged) {
      return;
    }

    _title = val;

    fire('title', this);
  }

  bool get actionRun {
    return _actionRun;
  }

  void setActionRun(bool val) {
    bool isValChanged = _actionRun != val;

    if (!isValChanged) {
      return;
    }

    _actionRun = val;

    fire('actionRun', this);
  }

  bool get success {
    return _success;
  }

  void setSuccess(bool val) {
    bool isValChanged = _success != val;

    if (!isValChanged) {
      return;
    }

    _success = val;

    fire('success', this);
  }

  String get error {
    return _error;
  }

  void setError(String val) {
    bool isValChanged = _error != val;

    if (!isValChanged) {
      return;
    }

    _error = val;

    fire('error', this);
  }

  Consumer get exe {
    return _exe;
  }

  void setExe(Consumer val) {
    bool isValChanged = _exe != val;

    if (!isValChanged) {
      return;
    }

    _exe = val;

    fire('exe', this);
  }

  bool operator ==(Object other) {
    return identical(this, other) ||
        other is DBAction &&
            _title == other._title &&
            _actionRun == other._actionRun &&
            _success == other._success &&
            _error == other._error &&
            _exe == other._exe;
  }

  @override
  int get hashCode {
    return _title?.hashCode ??
        0 + _actionRun?.hashCode ??
        0 + _success?.hashCode ??
        0 + _error?.hashCode ??
        0 + _exe?.hashCode ??
        0;
  }
}
