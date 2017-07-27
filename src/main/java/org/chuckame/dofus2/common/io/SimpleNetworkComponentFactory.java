package org.chuckame.dofus2.common.io;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class SimpleNetworkComponentFactory<T extends INetworkComponent> implements INetworkComponentFactory<T> {
	private final Map<Integer, Supplier<T>> suppliers = new HashMap<>();

	protected void register(int protocolId, Supplier<T> supplier) {
		suppliers.put(protocolId, supplier);
	}

	@Override
	public Optional<T> create(int id) {
		Supplier<T> supplier = suppliers.get(id);
		if (supplier == null) {
			return Optional.empty();
		}
		return Optional.of(supplier.get());
	}
}