package org.michocko.dofus2.common.io.test;

import org.apache.log4j.BasicConfigurator;
import org.michocko.dofus2.common.io.test.codec.TimeStampDecoder;
import org.michocko.dofus2.common.io.test.codec.TimeStampEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

	public static void main(final String[] args) throws InterruptedException {
		BasicConfigurator.configure();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(workerGroup);
		b.channel(NioSocketChannel.class);

		b.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(final SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new TimeStampEncoder(), new TimeStampDecoder(), new ClientHandler());
			}
		});

		String serverIp = "127.0.0.1";
		b.connect(serverIp, 19000);
		while (true) {
			Thread.sleep(1000);
		}
	}
}
