package org.maxwell.springboot.server;

import javax.annotation.PreDestroy;

import org.apache.catalina.connector.Connector;
import org.maxwell.springboot.controller.SampleController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@ComponentScan(basePackageClasses = SampleController.class)
public class MySpringBoot {

	private static final Logger LOGGER = LoggerFactory.getLogger(MySpringBoot.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MySpringBoot.class);
		app.setBannerMode(Mode.OFF);
		app.run(args);
		LOGGER.info("0;Started Simulator Service");
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory containerFactory = new TomcatEmbeddedServletContainerFactory();
		TomcatConnectorCustomizer connectorCustomizer = new TomcatConnectorCustomizer() {
			@Override
			public void customize(Connector connector) {
				connector.setPort(8086);
				connector.setProperty("address", "localhost");
				connector.setProperty("server", "test-server");
				connector.setProperty("connectionTimeout", "20000");
				connector.setProperty("maxHttpHeaderSize", "32676");
			}
		};
		containerFactory.addConnectorCustomizers(connectorCustomizer);
		containerFactory.setContextPath("/my-service");
		return containerFactory;
	}

	@PreDestroy
	public static void shutDownMethod() {
		LOGGER.info("0;Simulator is shutting down.");
	}
}
