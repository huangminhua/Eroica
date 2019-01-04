package eroica.network.utility.common;

import eroica.network.utility.configuration.ApplicationConfig;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

/**
 * The prompt provider setting of this application.
 * 
 * @author Minhua HUANG
 *
 */
@Component
public class EroicaPromptProvider implements PromptProvider {

	private ApplicationConfig applicationConfig;

	@Autowired
	public EroicaPromptProvider(ApplicationConfig applicationConfig) {
		this.applicationConfig = applicationConfig;
	}

	@Override
	public AttributedString getPrompt() {
		String applicationName = applicationConfig.getApplicationName();
		return new AttributedString(applicationName + ":>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
	}

}
