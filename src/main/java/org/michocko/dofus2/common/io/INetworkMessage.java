package org.michocko.dofus2.common.io;

public interface INetworkMessage extends INetworkComponent {
	default boolean containsNoField() {
		return false;
	}
}
