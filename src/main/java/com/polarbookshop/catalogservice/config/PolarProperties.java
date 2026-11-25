package com.polarbookshop.catalogservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// binds every property that starts with "polar." to this record
@ConfigurationProperties(prefix = "polar")
public record PolarProperties(
    String greeting // polar.greeting
) {}
