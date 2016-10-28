package br.com.simplepass.cadevanaluno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;


@SpringBootApplication
public class CadeVanAlunoApplication extends SpringBootServletInitializer{

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CadeVanAlunoApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(CadeVanAlunoApplication.class, args);
	}
}
