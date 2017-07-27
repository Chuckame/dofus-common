package org.chuckame.dofus2.common.io;

import java.util.Optional;

public final class ProtocolTypeManager extends ReflectableNetworkComponentFactory<INetworkType> {
	private static final ProtocolTypeManager INSTANCE = new ProtocolTypeManager();

	public static ProtocolTypeManager getInstance() {
		return INSTANCE;
	}

	private ProtocolTypeManager() {
	}

	@SuppressWarnings("unchecked")
	public <T extends INetworkType> T newInstance(short networkTypeId) {
		Optional<INetworkType> supplier = create(networkTypeId);
		if (supplier.isPresent())
			return (T) supplier.get();
		throw new NetworkComponentInstantiationException(
				String.format("Unable to get non-null instance of type id '%d'.", networkTypeId));
	}
}