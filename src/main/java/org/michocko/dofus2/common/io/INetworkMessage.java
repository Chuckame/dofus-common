package org.michocko.dofus2.common.io;

public interface INetworkMessage {
	int getNetworkMessageId();
	void deserialize(IDataReader reader);
	void serialize(IDataWriter writer);
}
