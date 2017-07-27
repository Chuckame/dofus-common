package org.chuckame.dofus2.common.io;

public final class ProtocolMessageManager extends ReflectableNetworkComponentFactory<INetworkMessage> {
	private static final ProtocolMessageManager INSTANCE = new ProtocolMessageManager();

	public static ProtocolMessageManager getInstance() {
		return INSTANCE;
	}

	private ProtocolMessageManager() {
	}
}