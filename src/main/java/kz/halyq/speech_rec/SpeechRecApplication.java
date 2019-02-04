package kz.halyq.speech_rec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class SpeechRecApplication
extends SpringBootServletInitializer
{

	public static void main(String[] args) throws Exception{
		SpringApplication.run(SpeechRecApplication.class, args);
	}

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpeechRecApplication.class);
    }

}

