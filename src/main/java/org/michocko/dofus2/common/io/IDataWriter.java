package org.michocko.dofus2.common.io;

public interface IDataWriter {

	void writeSByte(byte completion);

	void writeBoolean(boolean isSelectable);

	void writeUShort(int id);

	void writeDouble(double date);

	void writeShort(short id);

	void writeInt(int id);

	void writeUTF(String value);

	void writeByte(short position);

	void writeUInt(long nbWaves);

	void writeFloat(float taxModificationPercentage);

}
