package org.michocko.dofus2.common.io;

import java.util.Optional;

public interface INetworkComponentFactory<T extends INetworkComponent> {
	Optional<T> create(int id);
}
