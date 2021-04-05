package org.michocko.dofus2.common.io.test;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
		LoopBackTimeStamp ts = (LoopBackTimeStamp) msg;
		ctx.writeAndFlush(ts); // recieved message sent back directly
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}