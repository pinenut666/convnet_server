package net.convnet.server.config;

import net.convnet.server.protocol.bin.CoderMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public abstract class AbstractConfig {
    @Bean
    public abstract List<CoderMapping> coderMappings();
}
