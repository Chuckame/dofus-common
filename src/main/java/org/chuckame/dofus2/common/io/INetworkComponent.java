package org.chuckame.dofus2.common.io;

public interface INetworkComponent extends INetworkDeserializer, INetworkSerializer {
	/**
	 * Obtient l'identifiant du type.
	 * 
	 * @return l'identifiant
	 */
	int getProtocolId();
}
