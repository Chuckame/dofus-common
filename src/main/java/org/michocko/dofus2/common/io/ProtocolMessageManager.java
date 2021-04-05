package org.michocko.dofus2.common.io;

public final class ProtocolMessageManager extends NetworkComponentReflectionFactory<INetworkMessage> {
	private static final ProtocolMessageManager INSTANCE = new ProtocolMessageManager();

	private ProtocolMessageManager() {
		super(INetworkMessage.class);
	}

	public static ProtocolMessageManager getInstance() {
		return INSTANCE;
	}
}