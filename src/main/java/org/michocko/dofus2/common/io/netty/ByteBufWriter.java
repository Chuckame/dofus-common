package org.michocko.dofus2.common.io.netty;

import org.michocko.test.dofusdock.IDataWriter;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class ByteBufWriter implements IDataWriter {
	private final ByteBuf buffer;

	@Override
	public void writeBoolean(final boolean value) {
		this.buffer.writeBoolean(value);
	}

	@Override
	public void writeByte(final short value) {
		this.buffer.writeByte(value);
	}

	@Override
	public void writeSByte(final byte value) {
		this.buffer.writeByte(value);
	}

	@Override
	public void writeShort(final short value) {
		this.buffer.writeShort(value);
	}

	@Override
	public void writeUShort(final int value) {
		this.buffer.writeShort(value);
	}

	@Override
	public void writeInt(final int value) {
		this.buffer.writeInt(value);
	}

	@Override
	public void writeUInt(final long value) {
		this.buffer.writeShort((int) (value >> 16));
		this.buffer.writeShort((int) value);
	}

	@Override
	public void writeFloat(final float value) {
		this.buffer.writeFloat(value);
	}

	@Override
	public void writeDouble(final double value) {
		this.buffer.writeDouble(value);
	}

	@Override
	public void writeUTF(final String value) {
		this.writeUShort(value.length());
		this.writeUTFBytes(value);
	}

	public void writeUTFBytes(final String utf) {
		for (int i = 0; i < utf.length(); i++) {
			this.writeChar(utf.charAt(i));
		}
	}

	public void writeChar(final char c) {
		this.writeByte((short) c);
	}
}
