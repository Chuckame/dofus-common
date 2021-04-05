package org.michocko.dofus2.common.io;

import org.michocko.test.dofusdock.IDataReader;

public interface IDeserializable {
	void deserialize(IDataReader reader);
}
