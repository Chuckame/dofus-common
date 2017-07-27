package org.chuckame.dofus2.common.io;

public interface INetworkDeserializer {
	/**
	 * Extrait le message depuis un flux de données.
	 * 
	 * @param reader
	 *            le flux de données où extraire le message
	 */
	void deserialize(IDataReader reader);
}
