package com.raxrot.blog;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
        info = @Info(
                title = "Blog REST API",
                version = "1.0",
                description = "REST API for managing blog posts and comments",
                contact = @Contact(
                        name = "RaxRot",
                        email = "dasistperfektos@gmail.com",
                        url = "https://github.com/RaxRot"
                )
        )
)
@SpringBootApplication
public class SbBlogApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbBlogApiApplication.class, args);
    }

}
