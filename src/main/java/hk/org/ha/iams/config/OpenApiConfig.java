package hk.org.ha.iams.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI termsearchOpenApi() {
        return new OpenAPI().info(new Info()
                .title("IAMS Terminology Search")
                .version("1.0.0")
                .description("REST Cloud rewrite of searchTermCode / getTermDesc"));
    }
}
