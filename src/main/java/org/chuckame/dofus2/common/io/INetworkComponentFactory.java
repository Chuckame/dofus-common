package org.chuckame.dofus2.common.io;

import java.util.Optional;

public interface INetworkComponentFactory<T extends INetworkComponent> {
	/**
	 * Tente de créer une instance avec l'id donné.
	 * 
	 * @param id
	 *            l'id correspondant à l'instance à créer
	 * @return La nouvelle instance wrappée dans un {@link Optional}, sinon
	 *         {@link Optional#empty()}
	 */
	Optional<T> create(int id);

	default INetworkComponentFactory<T> withFallback(final INetworkComponentFactory<T> after) {
		final INetworkComponentFactory<T> before = this;
		return id -> {
			Optional<T> opt = before.create(id);
			if (opt.isPresent()) {
				return opt;
			}
			return after.create(id);
		};
	}
}
