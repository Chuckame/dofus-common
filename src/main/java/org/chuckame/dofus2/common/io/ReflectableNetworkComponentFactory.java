package org.chuckame.dofus2.common.io;

import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReflectableNetworkComponentFactory<T extends INetworkComponent> extends SimpleNetworkComponentFactory<T> {
	public void registerByReflection(Class<?> baseClass, Class<T> parentClass) {
		for (Class<? extends T> clazz : new Reflections(ClasspathHelper.forClass(baseClass), new SubTypesScanner(false))
				.getSubTypesOf(parentClass)) {
			if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
				Supplier<T> supplier = () -> {
					try {
						return clazz.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						throw new NetworkComponentInstantiationException(
								String.format("Unable to create instance of %s", clazz.getName()), e);
					}
				};
				int id;
				try {
					id = supplier.get().getProtocolId();
				} catch (NetworkComponentInstantiationException e) {
					log.error("Unable to register network component {}.", clazz.getName());
					continue;
				}
				log.debug("Register network component {} -> {}...", id, clazz.getName());
				register(id, supplier);
			}
		}
	}
}