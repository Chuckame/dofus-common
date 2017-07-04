package org.michocko.dofus2.common.io;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ProtocolTypeManager {
	private static final ProtocolTypeManager INSTANCE = new ProtocolTypeManager();

	public static ProtocolTypeManager getInstance() {
		return INSTANCE;
	}

	private final Map<Short, Class<? extends INetworkType>> types = new HashMap<>();

	private ProtocolTypeManager() {
	}

	public void registerByReflection(Class<?> baseClass) {
		for (Class<? extends INetworkType> clazz : new Reflections(ClasspathHelper.forClass(baseClass),
				new SubTypesScanner(false)).getSubTypesOf(INetworkType.class)) {
			if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
				short id;
				try {
					id = clazz.newInstance().getNetworkTypeId();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				log.debug(String.format("Adding message type %s -> %s...", id, clazz.getName()));
				types.put(id, clazz);
			}
		}
	}

	public void register(short networkTypeId, Class<? extends INetworkType> typeClass) {
		types.put(networkTypeId, typeClass);
	}

	@SuppressWarnings("unchecked")
	public <T extends INetworkType> T newInstance(short networkTypeId) {
		Class<? extends INetworkType> typeClass = types.get(networkTypeId);
		Objects.requireNonNull(typeClass, String.format("invalid type id '%d'", networkTypeId));

		try {
			return (T) typeClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}