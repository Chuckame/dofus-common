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
public final class ProtocolMessageManager {
	private static final ProtocolMessageManager INSTANCE = new ProtocolMessageManager();

	public static ProtocolMessageManager getInstance() {
		return INSTANCE;
	}

	private final Map<Integer, Class<? extends INetworkMessage>> messages = new HashMap<>();

	private ProtocolMessageManager() {
	}

	public void registerByReflection(Class<?> baseClass) {
		for (Class<? extends INetworkMessage> messageClass : new Reflections(ClasspathHelper.forClass(baseClass),
				new SubTypesScanner(false)).getSubTypesOf(INetworkMessage.class)) {
			if (!messageClass.isInterface() && !Modifier.isAbstract(messageClass.getModifiers())) {
				int id;
				try {
					id = messageClass.newInstance().getNetworkMessageId();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				log.debug(String.format("Adding network message %s -> %s...", id, messageClass.getName()));
				messages.put(id, messageClass);
			}
		}
	}

	public void register(int networkTypeId, Class<? extends INetworkMessage> typeClass) {
		messages.put(networkTypeId, typeClass);
	}

	@SuppressWarnings("unchecked")
	public <T extends INetworkMessage> T newInstance(int networkTypeId) {
		Class<? extends INetworkMessage> messageClass = messages.get(networkTypeId);
		Objects.requireNonNull(messageClass, String.format("invalid message id '%d'", networkTypeId));

		try {
			return (T) messageClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}