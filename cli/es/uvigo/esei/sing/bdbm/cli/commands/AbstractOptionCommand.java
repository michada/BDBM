package es.uvigo.esei.sing.bdbm.cli.commands;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import es.uvigo.ei.sing.yaacli.AbstractCommand;
import es.uvigo.ei.sing.yaacli.Option;

public abstract class AbstractOptionCommand extends AbstractCommand {
	private static final String REFLECTION_OPTION_PREFIX = "OPTION_";

	@Override
	protected List<Option<?>> createOptions() {
		final Class<? extends AbstractOptionCommand> clazz = this.getClass();
		
		final Field[] fields = clazz.getFields();
		final List<Option<?>> options = new LinkedList<Option<?>>();
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()) &&
				field.getName().startsWith(AbstractOptionCommand.REFLECTION_OPTION_PREFIX) &&
				Option.class.isAssignableFrom(field.getType())
			) {
				try {
					options.add((Option<?>) field.get(null));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		return options;
	}
}
