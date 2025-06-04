package red_social_academica.red_social_academica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RedSocialAcademicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedSocialAcademicaApplication.class, args);
	}

}
