package com.multimidia.projeto.trabalho_final.modules.config;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.collection.spi.PersistentMap;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.multimidia.projeto.trabalho_final.modules.shared.FileResponseDTO;

@Configuration
public class ModelMapperConfig {

    @Bean
    @SuppressWarnings("rawtypes")
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Converter personalizado para evitar problemas com PersistentMap
        Converter<PersistentMap, Map<String, FileResponseDTO>> mapConverter = new Converter<>() {
            @SuppressWarnings("unchecked")
            @Override
            public Map<String, FileResponseDTO> convert(
                    MappingContext<PersistentMap, Map<String, FileResponseDTO>> context) {
                PersistentMap sourceMap = context.getSource();
                if (sourceMap == null) {
                    return null;
                }
                Map<String, FileResponseDTO> targetMap = new HashMap<>();
                sourceMap.forEach((key, value) -> targetMap.put((String) key, (FileResponseDTO) value));
                return targetMap;
            }
        };

        modelMapper.addConverter(mapConverter);

        return modelMapper;
    }
}
