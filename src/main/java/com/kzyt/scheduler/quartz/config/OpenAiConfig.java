package com.kzyt.scheduler.quartz.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;




@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Spring Boot Swagger Demo API",
                version = "1.0",
                description = "This is a sample Spring Boot application demonstrating Swagger/OpenAPI documentation.",
                termsOfService = "http://example.com/terms/",
                contact = @Contact(
                        name = "Your Name",
                        email = "your.email@example.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Development Server"),
                @Server(url = "https://your-production-url.com", description = "Production Server")
        },
        security = @SecurityRequirement(name = "bearerAuth"),
        tags = {
                @Tag(name = "Job Management", description = "Endpoints for managing jobs in the scheduler"),
                @Tag(name = "Trigger Management", description = "Endpoints for managing triggers in the scheduler"),
                @Tag(name = "Schedule Management", description = "Endpoints for managing jobs in the scheduler"),
                @Tag(name = "Job Execution History", description = "Endpoints for retrieving job execution history and status"),
        }
)
public class OpenAiConfig {
}
