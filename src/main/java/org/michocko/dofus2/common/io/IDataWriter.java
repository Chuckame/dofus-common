package org.michocko.dofus2.common.io;

public interface IDataWriter {

	void writeBoolean(boolean value);

	void writeByte(short value);

	void writeSByte(byte value);

	void writeShort(short value);

	void writeUShort(int value);

	void writeInt(int value);

	void writeUInt(long value);

	void writeFloat(float value);

	void writeDouble(double value);

	void writeUTF(String value);

}
