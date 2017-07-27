package org.chuckame.dofus2.common.io.netty;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.chuckame.dofus2.common.io.DofusProtocolHelper;
import org.chuckame.dofus2.common.io.INetworkComponentFactory;
import org.chuckame.dofus2.common.io.INetworkMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DofusDecoder extends ByteToMessageDecoder {
	private final INetworkComponentFactory<INetworkMessage> factory;

	public DofusDecoder(INetworkComponentFactory<INetworkMessage> factory) {
		this.factory = Objects.requireNonNull(factory, "factory");
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		while (in.isReadable()) {
			if (!in.isReadable(2)) {
				break;
			}
			// mark message origin
			// location where we'll reset if there is not enough data to
			// actually deserialize
			in.markReaderIndex();

			int staticHeader = in.readUnsignedShort();

			int protocolId = DofusProtocolHelper.getProtocolId(staticHeader);
			byte typeLen = DofusProtocolHelper.getTypeLen(staticHeader);

			if (!in.isReadable(typeLen)) {
				// some bytes has been read
				// we might need them later
				// so get back where we were
				in.resetReaderIndex();
				break;
			}

			int len = DofusProtocolHelper.readMessageLen(NettyDataReader.of(in), typeLen);

			if (!in.isReadable(len)) {
				in.resetReaderIndex();
				break;
			}

			Optional<INetworkMessage> opt = factory.create(protocolId);

			if (!opt.isPresent()) {
				log.warn("receive a message with id={}, but factory couldn't create it", protocolId);
				in.skipBytes(len);
				continue;
			}

			INetworkMessage msg = opt.get();
			if (!msg.isAlwaysEmpty()) {
				msg.deserialize(NettyDataReader.of(in));
			}
			out.add(msg);
		}
	}
}