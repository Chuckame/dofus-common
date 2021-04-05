package org.michocko.dofus2.common.io;

import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.function.Supplier;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NetworkComponentReflectionFactory<T extends INetworkComponent> extends NetworkComponentFactory<T> {
	private final Class<T> tClass;

	public NetworkComponentReflectionFactory(final Class<T> tClass) {
		this.tClass = tClass;
	}

	public void registerByReflection(final Class<?> baseClass) {
		for (Class<? extends T> currentClass : new Reflections(ClasspathHelper.forClass(baseClass),
				new SubTypesScanner(false)).getSubTypesOf(this.tClass)) {
			if (!currentClass.isInterface() && !Modifier.isAbstract(currentClass.getModifiers())) {
				Supplier<Optional<T>> s = this.createInstanceSupplier(currentClass);
				Optional<? extends T> result = s.get();
				if (!result.isPresent())
					throw new NetworkComponentFactoryException(
							String.format("Unable to get instance of class '%s'.", currentClass));
				int id = result.get().getNetworkComponentId();
				log.debug(String.format("Adding message type %s -> %s...", id, currentClass.getName()));
				this.registerComponent(id, s);
			}
		}
	}
}
