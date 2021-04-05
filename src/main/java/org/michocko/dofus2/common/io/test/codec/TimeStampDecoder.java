package org.michocko.dofus2.common.io.test.codec;

import java.util.List;

import org.michocko.dofus2.common.io.test.LoopBackTimeStamp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TimeStampDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
		final int messageLength = Long.SIZE / Byte.SIZE * 2;
		if (in.readableBytes() < messageLength)
			return;

		byte[] ba = new byte[messageLength];
		in.readBytes(ba, 0, messageLength); // block until read 16 bytes from sockets
		LoopBackTimeStamp loopBackTimeStamp = new LoopBackTimeStamp();
		loopBackTimeStamp.fromByteArray(ba);
		out.add(loopBackTimeStamp);
	}
}