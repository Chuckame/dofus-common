package org.michocko.dofus2.common.io.netty;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.michocko.dofus2.common.io.DofusProtocol;
import org.michocko.dofus2.common.io.INetworkMessage;
import org.michocko.dofus2.common.io.NetworkComponentFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DofusDecoder extends ByteToMessageDecoder {
	private final NetworkComponentFactory<INetworkMessage> factory;

	public DofusDecoder(final NetworkComponentFactory<INetworkMessage> factory) {
		this.factory = Objects.requireNonNull(factory, "factory");
	}

	@Override
	protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
		while (in.isReadable()) {
			if (!in.isReadable(2))
				return;

			// NOTE(Blackrush):
			// mark message origin
			// location where we'll reset if there is not enough data to actually
			// deserialize
			in.markReaderIndex();

			int staticHeader = in.readUnsignedShort();

			int protocolId = DofusProtocol.getProtocolId(staticHeader);
			byte typeLen = DofusProtocol.getTypeLen(staticHeader);

			if (!in.isReadable(typeLen)) {
				// NOTE(Blackrush):
				// some bytes has been read
				// we might need them later
				// so get back where we were
				in.resetReaderIndex();
				return;
			}

			int len = DofusProtocol.readMessageLen(ByteBufReader.of(in), typeLen);

			if (!in.isReadable(len)) {
				// see NOTE(Blackrush)
				in.resetReaderIndex();
				break;
			}

			Optional<INetworkMessage> opt = this.factory.create(protocolId);

			if (!opt.isPresent()) {
				log.warn("receive a message with id={}, but factory couldn't create it", protocolId);

				// NOTE(Blackrush): skip non-deserializable bytes
				in.skipBytes(len);
			} else {
				INetworkMessage msg = opt.get();
				msg.deserialize(ByteBufReader.of(in));

				out.add(msg);
			}
		}
	}
}
