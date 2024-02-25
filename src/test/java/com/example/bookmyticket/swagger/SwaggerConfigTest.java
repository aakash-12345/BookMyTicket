package com.example.bookmyticket.swagger;

import com.example.bookmyticket.api.AdminController;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springdoc.core.GroupedOpenApi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SwaggerConfigTest {
    @Mock
    private SwaggerProperties swaggerProperties;
    @InjectMocks
    private SwaggerConfig swaggerConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPublicApi() {
        // Mock the behavior of swaggerProperties
        when(swaggerProperties.getGroup()).thenReturn("testGroup");
        when(swaggerProperties.getPackagesToScan()).thenReturn("com.example");
        when(swaggerProperties.getDisplayName()).thenReturn("Test API");

        // Create an instance of the class that contains the publicApi method
        SwaggerConfig instance = new SwaggerConfig();

        // Call the method to be tested
        GroupedOpenApi result = instance.publicApi(swaggerProperties);

        // Assert the result
        assertEquals("testGroup", result.getGroup());
        assertEquals("com.example", result.getPackagesToScan().get(0));
        assertEquals("Test API", result.getDisplayName());
    }

    @Test
    public void testOpenApi() {
        // Mock the behavior of swaggerProperties
        when(swaggerProperties.getApiTitle()).thenReturn("Test API");
        when(swaggerProperties.getApiDescription()).thenReturn("Test Description");
        when(swaggerProperties.getApiVersion()).thenReturn("1.0");
        when(swaggerProperties.getLicenseName()).thenReturn("Test License");
        when(swaggerProperties.getLicenseUrl()).thenReturn("http://test-license-url.com");
        when(swaggerProperties.getContactTeam()).thenReturn("Test Team");
        when(swaggerProperties.getContactConfluence()).thenReturn("http://test-confluence-url.com");
        when(swaggerProperties.getContactEmail()).thenReturn("test@example.com");
        when(swaggerProperties.getExternalDocDescription()).thenReturn("External Doc Description");
        when(swaggerProperties.getExternalDocUrl()).thenReturn("http://external-doc-url.com");

        // Create an instance of the class that contains the openApi method
        SwaggerConfig instance = new SwaggerConfig();

        // Call the method to be tested
        OpenAPI result = instance.openApi(swaggerProperties);

        // Assert the result
        assertEquals("Test API", result.getInfo().getTitle());
        assertEquals("Test Description", result.getInfo().getDescription());
        assertEquals("1.0", result.getInfo().getVersion());
        assertEquals("Test License", result.getInfo().getLicense().getName());
        assertEquals("http://test-license-url.com", result.getInfo().getLicense().getUrl());
        assertEquals("Test Team", result.getInfo().getContact().getName());
        assertEquals("http://test-confluence-url.com", result.getInfo().getContact().getUrl());
        assertEquals("test@example.com", result.getInfo().getContact().getEmail());
        assertEquals("External Doc Description", result.getExternalDocs().getDescription());
        assertEquals("http://external-doc-url.com", result.getExternalDocs().getUrl());
    }
}