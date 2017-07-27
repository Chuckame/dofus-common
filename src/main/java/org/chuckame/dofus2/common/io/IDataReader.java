package org.chuckame.dofus2.common.io;

public interface IDataReader {

	boolean readBoolean();

	byte readSByte();

	short readByte();

	short readShort();

	int readUShort();

	int readInt();

	long readUInt();

	float readFloat();

	double readDouble();

	String readUTF();

}
