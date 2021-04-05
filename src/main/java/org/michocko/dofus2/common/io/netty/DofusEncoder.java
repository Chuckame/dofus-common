package org.michocko.dofus2.common.io.netty;

import org.michocko.dofus2.common.io.DofusProtocol;
import org.michocko.dofus2.common.io.INetworkMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public final class DofusEncoder extends ChannelHandlerAdapter {
	@Override
	public void write(final ChannelHandlerContext ctx, final Object o, final ChannelPromise promise) throws Exception {
		if (!(o instanceof INetworkMessage)) {
			ctx.write(o, promise);
			return;
		}

		INetworkMessage msg = (INetworkMessage) o;

		ByteBuf body = this.writeBody(ctx, msg);
		ByteBuf header = this.writeHeader(ctx, msg.getNetworkComponentId(), body.readableBytes());

		// NOTE(Blackrush): cheap operation, just glueing header and body together
		CompositeByteBuf buffer = ctx.alloc().compositeBuffer(2);
		buffer.addComponents(header.slice(), body.slice());
		buffer.writerIndex(header.readableBytes() + body.readableBytes());

		// kick off serialized buffer
		ctx.write(buffer, promise);
	}

	private ByteBuf writeBody(final ChannelHandlerContext ctx, final INetworkMessage msg) {
		if (!msg.containsNoField()) {
			// NOTE(Blackrush): costly operation,
			// we now the message isn't empty so we can undoubtebly 2^4-1 bytes
			// this might take up to 2^24-1 bytes

			ByteBuf buf = ctx.alloc().buffer(0xF, DofusProtocol.MAX_MESSAGE_LEN);
			msg.serialize(ByteBufWriter.of(buf));
			return buf;
		} else
			// NOTE(Blackrush): cheap operation, no alloc at all actually
			return Unpooled.EMPTY_BUFFER;
	}

	private ByteBuf writeHeader(final ChannelHandlerContext ctx, final int protocolId, final int len) {
		// NOTE(Blackrush): cheap operation, a header cannot exceed a specific number of
		// bytes
		ByteBuf buf = ctx.alloc().buffer(DofusProtocol.MAX_HEADER_LEN, DofusProtocol.MAX_HEADER_LEN);
		DofusProtocol.writeHeader(ByteBufWriter.of(buf), protocolId, len);
		return buf;
	}
}
