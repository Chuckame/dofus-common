package org.michocko.dofus2.common.io;

import org.michocko.test.dofusdock.IDataWriter;

public interface ISerializable {
	void serialize(IDataWriter writer);
}
