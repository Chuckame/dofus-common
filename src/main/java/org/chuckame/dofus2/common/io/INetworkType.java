package org.chuckame.dofus2.common.io;

public interface INetworkType extends INetworkComponent {
	/**
	 * Obtient l'identifiant du type.
	 * 
	 * @return l'identifiant
	 */
	short getProtocolTypeId();

	@Override
	default int getProtocolId() {
		return getProtocolTypeId();
	}
}
