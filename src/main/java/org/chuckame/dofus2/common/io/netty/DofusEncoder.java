package org.chuckame.dofus2.common.io.netty;

import org.chuckame.dofus2.common.io.DofusProtocolHelper;
import org.chuckame.dofus2.common.io.INetworkMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public final class DofusEncoder extends ChannelHandlerAdapter {
	@Override
	public void write(ChannelHandlerContext ctx, Object o, ChannelPromise promise) throws Exception {
		if (!(o instanceof INetworkMessage)) {
			ctx.write(o, promise);
			return;
		}

		INetworkMessage msg = (INetworkMessage) o;

		ByteBuf body = writeBody(ctx, msg);
		ByteBuf header = writeHeader(ctx, msg.getProtocolId(), body.readableBytes());

		// cheap operation, just glueing header and body together
		CompositeByteBuf buffer = ctx.alloc().compositeBuffer(2);
		buffer.addComponents(header.slice(), body.slice());
		buffer.writerIndex(header.readableBytes() + body.readableBytes());

		// kick off serialized buffer
		ctx.write(buffer, promise);
	}

	private ByteBuf writeBody(ChannelHandlerContext ctx, INetworkMessage msg) {
		if (!msg.isAlwaysEmpty()) {
			// costly operation,
			// we now the message isn't empty so we can undoubtebly 2^4-1 bytes
			// this might take up to 2^24-1 bytes

			ByteBuf buf = ctx.alloc().buffer(0xF, DofusProtocolHelper.MAX_MESSAGE_LEN);
			msg.serialize(NettyDataWriter.of(buf));
			return buf;
		} else {
			// NOTE(Blackrush): cheap operation, no alloc at all actually
			return Unpooled.EMPTY_BUFFER;
		}
	}

	private ByteBuf writeHeader(ChannelHandlerContext ctx, int protocolId, int len) {
		// NOTE(Blackrush): cheap operation, a header cannot exceed a specific
		// number of bytes
		ByteBuf buf = ctx.alloc().buffer(DofusProtocolHelper.MAX_HEADER_LEN, DofusProtocolHelper.MAX_HEADER_LEN);
		DofusProtocolHelper.writeHeader(NettyDataWriter.of(buf), protocolId, len);
		return buf;
	}
}
