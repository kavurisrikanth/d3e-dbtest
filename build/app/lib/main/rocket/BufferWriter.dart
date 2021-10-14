import 'dart:convert';

import 'dart:typed_data';

class BufferWriter {
  BytesBuilder buffer = BytesBuilder();
  Uint8List data = Uint8List(1024);
  int index = 0;
  static final Utf8Codec _utf8 = Utf8Codec();

  BufferWriter();

  void _writeByte(int val) {
    _ensureBytes(1);
    data[index++] = val;
  }

  void _writeInt(int value) {
    _ensureBytes(10);
    var i = index;
    var lo = value.toUnsigned(32).toInt();
    var hi = (value >> 32).toUnsigned(32).toInt();
    while (hi > 0 || lo >= 0x80) {
      data[i++] = 0x80 | (lo & 0x7f);
      lo = (lo >> 7) | ((hi & 0x7f) << 25);
      hi >>= 7;
    }
    data[i++] = lo;
    index = i;
  }

  void writeBool(bool val) {
    return _writeByte(val ? 1 : 0);
  }

  void _ensureBytes(int len) {
    if (index + len > data.length) {
      buffer.add(Uint8List.sublistView(data, 0, index));
      index = 0;
    }
  }

  Uint8List takeBytes() {
    if (index > 0) {
      buffer.add(Uint8List.sublistView(data, 0, index));
      index = 0;
    }
    return buffer.takeBytes();
  }

  void writeBytes(Uint8List val) {
    writeInt(val.length);
    _ensureBytes(val.length);
    data.setAll(index, val);
    index += val.length;
  }

  ByteData _readByteData(int sizeInBytes) {
    _ensureBytes(sizeInBytes);
    index += sizeInBytes;
    return ByteData.sublistView(data, index, index + sizeInBytes);
  }

  int _encodeZigZag64(int value) => (value << 1) ^ (value >> 63);
  void writeByte(int value) => _writeByte(value);
  void writeInt(int value) => _writeInt(_encodeZigZag64(value));
  void writeString(String str) {
    if (str == null || str.isEmpty) {
      writeInt(-1);
    } else {
      writeBytes(_utf8.encode(str));
    }
  }

  void writeFloat(double value) => _readByteData(4).setFloat32(0, value);
  void writeDouble(double value) => _readByteData(8).setFloat64(0, value);
}
