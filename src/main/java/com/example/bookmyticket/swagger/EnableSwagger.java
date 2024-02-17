package com.example.bookmyticket.swagger;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({SwaggerConfig.class})
@Documented
public @interface EnableSwagger {
}
