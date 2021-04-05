package org.michocko.dofus2.common.io;

import java.util.Optional;

public final class ProtocolTypeManager extends NetworkComponentReflectionFactory<INetworkType> {
	private static final ProtocolTypeManager INSTANCE = new ProtocolTypeManager();

	private ProtocolTypeManager() {
		super(INetworkType.class);
	}

	public static ProtocolTypeManager getInstance() {
		return INSTANCE;
	}

	public INetworkType newInstance(final int id) {
		Optional<INetworkType> result = this.create(id);
		if (!result.isPresent())
			throw new NetworkComponentFactoryException(String.format("Unable to get instance of id '%s'.", id));
		return result.get();
	}
}