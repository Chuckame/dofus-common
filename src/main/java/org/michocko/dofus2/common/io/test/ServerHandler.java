package org.michocko.dofus2.common.io.test;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
		LoopBackTimeStamp ts = (LoopBackTimeStamp) msg;
		ts.setRecvTimeStamp(System.nanoTime());
		System.out.println("loop delay in ms : " + 1.0 * ts.timeLapseInNanoSecond() / 1000000L);
	}

	// Here is how we send out heart beat for idle to long
	@Override
	public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.ALL_IDLE) { // idle for no read and write
				ctx.writeAndFlush(new LoopBackTimeStamp());
			}
		}
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}
