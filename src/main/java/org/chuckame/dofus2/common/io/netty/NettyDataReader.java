package org.chuckame.dofus2.common.io.netty;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

import org.chuckame.dofus2.common.io.IDataReader;

import io.netty.buffer.ByteBuf;

public final class NettyDataReader implements IDataReader {
	private final ByteBuf buf;

	public NettyDataReader(ByteBuf buf) {
		this.buf = Objects.requireNonNull(buf, "buf").order(ByteOrder.BIG_ENDIAN);
	}

	public static NettyDataReader of(ByteBuf buf) {
		return new NettyDataReader(buf);
	}

	public long getPosition() {
		return buf.readerIndex();
	}

	public void setPosition(long position) {
		buf.readerIndex((int) position);
	}

	public boolean readable(int n) {
		return buf.isReadable(n);
	}

	public byte[] readBytes(int n) {
		byte[] bytes = new byte[n];
		buf.readBytes(bytes);
		return bytes;
	}

	public IDataReader read(int n) {
		return new NettyDataReader(buf.readSlice(n));
	}

	public ByteBuffer all() {
		return buf.nioBuffer();
	}

	public byte[] allBytes() {
		ByteBuf dup = buf.duplicate().resetReaderIndex();
		byte[] res = new byte[dup.readableBytes()];
		dup.readBytes(res);
		return res;
	}

	@Override
	public byte readSByte() {
		return buf.readByte();
	}

	@Override
	public short readByte() {
		return buf.readUnsignedByte();
	}

	@Override
	public short readShort() {
		return buf.readShort();
	}

	@Override
	public int readUShort() {
		return buf.readUnsignedShort();
	}

	@Override
	public int readInt() {
		return buf.readInt();
	}

	@Override
	public long readUInt() {
		return buf.readUnsignedInt();
	}

	public long readInt64() {
		return buf.readLong();
	}

	public BigInteger readUInt64() {
		return BigInteger.valueOf(readUInt()).shiftLeft(32).or(BigInteger.valueOf(readUInt()));
	}

	@Override
	public boolean readBoolean() {
		return buf.readBoolean();
	}

	@Override
	public float readFloat() {
		return buf.readFloat();
	}

	@Override
	public double readDouble() {
		return buf.readDouble();
	}

	@Override
	public String readUTF() {
		byte[] bytes = new byte[readUShort()];
		buf.readBytes(bytes);
		return new String(bytes);
	}
}