package org.michocko.dofus2.common.io.netty;

import org.michocko.test.dofusdock.IDataReader;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class ByteBufReader implements IDataReader {
	private final ByteBuf buffer;

	@Override
	public boolean readBoolean() {
		return this.buffer.readBoolean();
	}

	@Override
	public byte readSByte() {
		return this.buffer.readByte();
	}

	@Override
	public short readByte() {
		return (short) (this.buffer.readByte() & 0xFF);
	}

	@Override
	public short readShort() {
		return this.buffer.readShort();
	}

	@Override
	public int readUShort() {
		return this.buffer.readShort() & 0xFFFF;
	}

	@Override
	public int readInt() {
		return this.buffer.readInt();
	}

	@Override
	public long readUInt() {
		return this.buffer.readInt() & 0xFFFFFFFFL;
	}

	@Override
	public float readFloat() {
		return this.buffer.readFloat();
	}

	@Override
	public double readDouble() {
		return this.buffer.readDouble();
	}

	public char readChar() {
		return (char) this.readByte();
	}

	public String readUTF(final int len) {
		char[] chars = new char[len];

		for (int i = 0; i < len; i++) {
			chars[i] = this.readChar();
		}

		return new String(chars);
	}

	@Override
	public String readUTF() {
		return this.readUTF(this.readUShort());
	}
}
