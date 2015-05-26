package es.uvigo.esei.sing.bdbm.cli;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.uvigo.ei.sing.yaacli.CLIApplication;
import es.uvigo.ei.sing.yaacli.Command;
import es.uvigo.esei.sing.bdbm.BDBMManager;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.controller.DefaultBDBMController;
import es.uvigo.esei.sing.bdbm.environment.DefaultBDBMEnvironment;
import es.uvigo.esei.sing.bdbm.persistence.DefaultBDBMRepositoryManager;

public class BDBMcli extends CLIApplication {
	public BDBMcli() {
		super(true, false);
		
		this.loadCommands();
	}
	
	@Override
	protected List<Command> buildCommands() {
		try {
			final BDBMManager manager = new BDBMManager(
				new DefaultBDBMEnvironment(new File("bdbm.conf")),
				new DefaultBDBMRepositoryManager(),
				new DefaultBDBMController()
			);
			
			return getCommands(manager.getController());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected String getApplicationName() {
		return "BLAST DB Manager";
	}

	@Override
	protected String getApplicationCommand() {
		return "bdbm";
	}
	
	private static List<Command> getCommands(BDBMController controller) {
		final List<Command> commands = new ArrayList<>();
		
		try {
			for (Class<? extends Command> commandClass : getCommandClasses("es.uvigo.esei.sing.bdbm.cli.commands")) {
				commands.add(commandClass.getConstructor(BDBMController.class).newInstance(controller));
			}
			
			Collections.sort(commands, new Comparator<Command>() {
				@Override
				public int compare(Command o1, Command o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return commands;
	}
	
	private static List<Class<? extends Command>> getCommandClasses(String packageName) {
		final ClassLoader classLoader = 
			Thread.currentThread().getContextClassLoader();

		final String path = packageName.replace('.', '/');
		final List<Class<? extends Command>> commandClasses = new ArrayList<>();
		final URL packageURL = classLoader.getResource(path);
		
		try {
			final File packageDir = new File(packageURL.toURI());
		
			class ClassFilenameFilter implements FilenameFilter {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".class");
				}
			}
			
			for (File resource : packageDir.listFiles(new ClassFilenameFilter())) {
				final String filename = resource.getName();
				
				if (filename.endsWith(".class")) {
					try {
						@SuppressWarnings("unchecked")
						final Class<? extends Command> clazz = 
							(Class<? extends Command>) Class.forName(packageName + "." + filename.substring(0, filename.length() - 6));
						final int modifiers = clazz.getModifiers();
						if (!Modifier.isAbstract(modifiers) 
							&& !Modifier.isInterface(modifiers)
							&& !clazz.isEnum() 
							&& Command.class.isAssignableFrom(clazz)
						) {
							commandClasses.add(clazz);
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			
			return commandClasses;
			
		} catch (URISyntaxException e1) {
			throw new RuntimeException(e1);
		}
	}
}
