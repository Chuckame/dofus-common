package org.michocko.dofus2.common.io.test.codec;

import org.michocko.dofus2.common.io.test.LoopBackTimeStamp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TimeStampEncoder extends MessageToByteEncoder<LoopBackTimeStamp> {
	@Override
	protected void encode(final ChannelHandlerContext ctx, final LoopBackTimeStamp msg, final ByteBuf out)
			throws Exception {
		out.writeBytes(msg.toByteArray());
	}
}
