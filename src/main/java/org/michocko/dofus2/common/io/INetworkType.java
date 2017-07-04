package org.michocko.dofus2.common.io;

public interface INetworkType {
	short getNetworkTypeId();
	void deserialize(IDataReader reader);
	void serialize(IDataWriter writer);
}
