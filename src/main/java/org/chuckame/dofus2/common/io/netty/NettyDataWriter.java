package org.chuckame.dofus2.common.io.netty;

import java.math.BigInteger;

import org.chuckame.dofus2.common.io.IDataWriter;

import io.netty.buffer.ByteBuf;

public final class NettyDataWriter implements IDataWriter {
	class Buf {
		final Buf parent;
		final ByteBuf buf;
		final int start;
		final int length;

		Buf(Buf parent, ByteBuf buf, int start, int length) {
			this.parent = parent;
			this.buf = buf;
			this.start = start;
			this.length = length;
		}
	}

	private Buf buf;

	private NettyDataWriter(ByteBuf buf) {
		this.buf = new Buf(null, buf, buf.readerIndex(), 0);
	}

	public static NettyDataWriter of(ByteBuf buf) {
		return new NettyDataWriter(buf);
	}

	public ByteBuf getUnderlyingBuf() {
		return buf.buf;
	}

	public void unwrap() {
		int index = 0;
		Buf root = null;
		for (Buf cur = buf; cur.parent != null; cur = cur.parent) {
			root = cur.parent;
			index += cur.start;
		}

		if (root == null)
			return;

		root.buf.writerIndex(index + buf.length);
		root.buf.readerIndex(index);

		buf = root;
	}

	public void writeBytes(byte[] bytes) {
		buf.buf.writeBytes(bytes);
	}

	@Override
	public void writeSByte(byte int8) {
		buf.buf.writeByte(int8);
	}

	@Override
	public void writeByte(short uint8) {
		buf.buf.writeByte(uint8);
	}

	@Override
	public void writeShort(short int16) {
		buf.buf.writeShort(int16);
	}

	@Override
	public void writeUShort(int uint16) {
		buf.buf.writeShort(uint16);
	}

	@Override
	public void writeInt(int int32) {
		buf.buf.writeInt(int32);
	}

	@Override
	public void writeUInt(long uint32) {
		buf.buf.writeShort((int) (uint32 >> 16));
		buf.buf.writeShort((int) uint32);
	}

	public void writeLong(long int64) {
		buf.buf.writeLong(int64);
	}

	public void writeULong(BigInteger uint64) {
		buf.buf.writeInt(uint64.shiftRight(32).intValue());
		buf.buf.writeInt(uint64.intValue());
	}

	public int getRemaining() {
		return buf.buf.writableBytes();
	}

	public int getPosition() {
		return buf.buf.writerIndex();
	}

	public void setPosition(int position) {
		if (position > buf.buf.writerIndex()) {
			buf.buf.ensureWritable(position - buf.buf.writerIndex());
		}
		buf.buf.writerIndex(position);
	}

	public void slice(int offset, int length) {
		buf = new Buf(buf, buf.buf.slice(offset, length).resetWriterIndex(), offset, length);
	}

	public NettyDataReader reader() {
		return new NettyDataReader(buf.buf.duplicate());
	}

	@Override
	public void writeBoolean(boolean value) {
		buf.buf.writeBoolean(value);
	}

	@Override
	public void writeFloat(float value) {
		buf.buf.writeFloat(value);
	}

	@Override
	public void writeDouble(double value) {
		buf.buf.writeDouble(value);
	}

	@Override
	public void writeUTF(String value) {
		writeUShort(value.length());
		buf.buf.writeBytes(value.getBytes());
	}
}
