package org.chuckame.dofus2.common.io;

public interface INetworkMessage extends INetworkComponent {
	/**
	 * Indicate if this message has no field, so no data will be written to the
	 * stream. Used to speed up serialization step to skip useless
	 * {@link #serialize(IDataWriter)} call.<br>
	 * <br>
	 * Default returned value : <code>false</code>
	 * 
	 * @return <code>true</code> if this message has no written field, else
	 *         <code>false</code>.
	 */
	default boolean isAlwaysEmpty() {
		return false;
	}
}
