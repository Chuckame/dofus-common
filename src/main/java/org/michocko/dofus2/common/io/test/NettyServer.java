package org.michocko.dofus2.common.io.test;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.michocko.dofus2.common.io.test.codec.TimeStampDecoder;
import org.michocko.dofus2.common.io.test.codec.TimeStampEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class NettyServer {

	public static void main(final String[] args) throws IOException, InterruptedException {
		BasicConfigurator.configure();
		NioEventLoopGroup boosGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(boosGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);

		// ===========================================================
		// 1. define a separate thread pool to execute handlers with
		// slow business logic. e.g database operation
		// ===========================================================
		final EventExecutorGroup group = new DefaultEventExecutorGroup(1500); // thread pool of 1500

		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(final SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("idleStateHandler", new IdleStateHandler(0, 0, 5)); // add with name
				pipeline.addLast(new TimeStampEncoder()); // add without name, name auto generated
				pipeline.addLast(new TimeStampDecoder()); // add without name, name auto generated

				// ===========================================================
				// 2. run handler with slow business logic
				// in separate thread from I/O thread
				// ===========================================================
				pipeline.addLast(group, "serverHandler", new ServerHandler());
			}
		});

		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.bind(19000).sync();

		while (true) {
			Thread.sleep(1000);
		}
	}
}
