import 'dart:convert';
import 'dart:math';

import 'dart:typed_data';

class BufferReader {
  Uint8List data;
  int index = 0;
  static final Utf8Codec _utf8 = Utf8Codec();

  BufferReader(this.data);

  int _readByte() {
    return data[index++];
  }

  int _fromInts(int hi, int lo) {
    return ((hi << 32) | lo);
  }

  int _readInt() {
    int low = 0;
    for (int shift = 0; shift < 32; shift += 7) {
      final int b = readByte();
      low |= (b & 0x7F) << shift;
      if ((b & 0x80) == 0) {
        return low;
      }
    }
    int hi = 0;
    for (int shift = 0; shift < 32; shift += 7) {
      final int b = readByte();
      hi |= (b & 0x7F) << shift;
      if ((b & 0x80) == 0) {
        break;
      }
    }
    return low + hi * pow(2, 35);
  }

  void _checkLimit(int len) {}
  Uint8List readBytes() {
    var length = readInt();
    _checkLimit(length);
    index += length;
    return Uint8List.sublistView(data, index - length, index);
  }

  ByteData _readByteData(int sizeInBytes) {
    _checkLimit(sizeInBytes);
    index += sizeInBytes;
    return ByteData.sublistView(data, index - sizeInBytes, index);
  }

  int _decodeZigZag64(int value) {
    return ((value & 1) == 1 ? -(value ~/ 2) - 1 : (value ~/ 2));
  }

  bool readBool() => _readByte() == 1;
  int readByte() => _readByte();
  int readInt() => _decodeZigZag64(_readInt());
  String readString() {
    var length = readInt();
    if (length == -1) {
      return null;
    }
    _checkLimit(length);
    String str =
        _utf8.decode(Uint8List.sublistView(data, index, index + length));
    index += length;
    return str;
  }

  double readFloat() => _readByteData(4).getFloat32(0, Endian.little);
  double readDouble() => _readByteData(8).getFloat64(0, Endian.little);
}
