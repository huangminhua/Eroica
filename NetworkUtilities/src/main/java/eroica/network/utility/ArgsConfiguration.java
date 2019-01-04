package eroica.network.utility;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.shell.ExitRequest;
import org.springframework.shell.Input;
import org.springframework.shell.InputProvider;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;

@Configuration
public class ArgsConfiguration {

	@Autowired
	private Shell shell;

	@Bean
	public CommandLineRunner argsRunner() {
		return new ArgsRunner(shell);
	}

	@Bean
	public ExitCodeExceptionMapper exitCodeExceptionMapper() {
		return exception -> {
			Throwable e = exception;
			while (e != null && !(e instanceof ExitRequest)) {
				e = e.getCause();
			}
			return e == null ? 1 : ((ExitRequest) e).status();
		};
	}
}

@Order(InteractiveShellApplicationRunner.PRECEDENCE - 2)
class ArgsRunner implements CommandLineRunner {

	private Shell shell;

	public ArgsRunner(Shell shell) {
		this.shell = shell;
	}

	@Override
	public void run(String... args) throws Exception {
		// InteractiveShellApplicationRunner.disable(environment); //This line will let
		// the program exit.
		if (args.length > 0) {
			System.out.println("These files are to be imported:");
			System.out.println(Arrays.toString(args));
			for (int i = 0; i < args.length; i++) {
				try (BufferedReader br = Files.newBufferedReader(Paths.get(args[i]))) {
					while (true) {
						String line = br.readLine();
						if (line == null)
							break;
						shell.run(new StringInputProvider(line));
					}
				} catch (Exception e) {
					System.out.println("File [" + args[i] + "] maybe invalid. Please check it.");
					throw e;
				}
			}
		}
	}
}

class StringInputProvider implements InputProvider {
	private final String command;

	private boolean done;

	public StringInputProvider(String command) {
		this.command = command;
	}

	@Override
	public Input readInput() {
		if (!done) {
			done = true;
			return new Input() {
				@Override
				public String rawText() {
					return command;
				}
			};
		} else {
			return null;
		}
	}
}