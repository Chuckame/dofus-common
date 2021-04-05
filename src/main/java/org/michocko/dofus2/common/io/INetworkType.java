package org.michocko.dofus2.common.io;

public interface INetworkType extends INetworkComponent {
	short getNetworkTypeId();

	@Override
	default int getNetworkComponentId() {
		return getNetworkTypeId();
	}
}
