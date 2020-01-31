package com.rajcevic.tea.DiaryWebApp;

import com.rajcevic.tea.DiaryWebApp.misc.chain.DebugLogger;
import com.rajcevic.tea.DiaryWebApp.misc.chain.ErrorLogger;
import com.rajcevic.tea.DiaryWebApp.misc.chain.InfoLogger;
import com.rajcevic.tea.DiaryWebApp.misc.chain.SystemLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;

@SpringBootApplication
public class DiaryWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiaryWebAppApplication.class, args);
	}

	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(DataSize.ofBytes(512000000L));
		factory.setMaxRequestSize(DataSize.ofBytes(512000000L));
		return factory.createMultipartConfig();
	}

	public static SystemLogger getChainOfLoggers() throws IOException {

		SystemLogger errorLogger = new ErrorLogger(SystemLogger.ERROR);
		SystemLogger debugLogger = new DebugLogger(SystemLogger.DEBUG);
		SystemLogger infoLogger = new InfoLogger(SystemLogger.INFO);

		errorLogger.setNextLogger(debugLogger);
		debugLogger.setNextLogger(infoLogger);

		return errorLogger;
	}
}


