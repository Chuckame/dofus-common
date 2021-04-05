package org.michocko.dofus2.common.io;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NetworkComponentFactory<T extends INetworkComponent> implements INetworkComponentFactory<T> {
	private final Map<Integer, Supplier<Optional<T>>> componentsTypes = new HashMap<>();

	public NetworkComponentFactory(final Map<Integer, Supplier<Optional<T>>> componentsTypes) {
		this.componentsTypes.putAll(componentsTypes);
	}

	public void registerComponent(final int id, final Supplier<Optional<T>> instanceSupplier) {
		this.componentsTypes.put(id, instanceSupplier);
	}

	@Override
	public Optional<T> create(final int id) {
		return this.componentsTypes.get(id).get();
	}

	protected Supplier<Optional<T>> createInstanceSupplier(final Class<? extends T> instanceClass) {
		return () -> {
			try {
				return Optional.of(instanceClass.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				return Optional.empty();
			}
		};
	}

	static class NetworkComponentFactoryException extends RuntimeException {
		private static final long serialVersionUID = 3762437065779091914L;

		public NetworkComponentFactoryException() {
			super();
		}

		public NetworkComponentFactoryException(final String message, final Throwable cause) {
			super(message, cause);
		}

		public NetworkComponentFactoryException(final String message) {
			super(message);
		}

		public NetworkComponentFactoryException(final Throwable cause) {
			super(cause);
		}
	}
}
