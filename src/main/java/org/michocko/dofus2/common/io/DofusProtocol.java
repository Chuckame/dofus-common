package org.michocko.dofus2.common.io;

import org.michocko.test.dofusdock.IDataReader;
import org.michocko.test.dofusdock.IDataWriter;

public final class DofusProtocol {
	private DofusProtocol() {
	}

	public static int STATIC_HEADER_LEN = 2;
	public static int MAX_HEADER_MESSAGE_LEN = 3;
	public static int MAX_HEADER_LEN = STATIC_HEADER_LEN + MAX_HEADER_MESSAGE_LEN;

	public static int MAX_MESSAGE_LEN = 0xFFFFFF;

	public static int readMessageLen(final IDataReader reader, final byte typeLen) {
		switch (typeLen) {
		case 0:
			return 0;
		case 1:
			return reader.readByte();
		case 2:
			return reader.readUShort();
		case 3:
			return (reader.readByte() << 16) | (reader.readByte() << 8) | reader.readByte();

		default:
			throw new IllegalStateException(
					"malformated packet header, you cannot serialize a message length on more than 3 bytes");
		}
	}

	public static void writeMessageLen(final IDataWriter writer, final byte typeLen, final int len) {
		switch (typeLen) {
		case 0:
			break;

		case 1:
			writer.writeByte((short) len);
			break;
		case 2:
			writer.writeUShort(len);
			break;
		case 3:
			writer.writeByte((short) (len >> 16 & 255));
			writer.writeUShort((short) (len & 65535));
			break;

		default:
			throw new IllegalStateException("Packet's length can't be encoded on 4 or more bytes");
		}
	}

	public static void writeHeader(final IDataWriter writer, final int protocolId, final int len) {
		byte typeLen = computeTypeLen(len);
		writer.writeUShort(subComputeStaticHeader(protocolId, typeLen));
		writeMessageLen(writer, typeLen, len);
	}

	public static final byte BIT_RIGHT_SHIFT_LEN_PACKET_ID = 2;
	public static final byte BIT_MASK = 3;

	public static byte computeTypeLen(final int len) {
		if (len > 65535)
			return 3;

		if (len > 255)
			return 2;

		if (len > 0)
			return 1;

		return 0;
	}

	public static int subComputeStaticHeader(final int id, final byte typeLen) {
		return id << BIT_RIGHT_SHIFT_LEN_PACKET_ID | typeLen;
	}

	public static int getProtocolId(final int header) {
		return header >> BIT_RIGHT_SHIFT_LEN_PACKET_ID;
	}

	public static byte getTypeLen(final int header) {
		return (byte) (header & BIT_MASK);
	}
}
