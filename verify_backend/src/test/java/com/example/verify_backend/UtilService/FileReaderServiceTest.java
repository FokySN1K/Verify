package com.example.verify_backend.UtilService;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

class FileReaderServiceTest {

    @Test
    void shouldReadResourceFromClasspath() {
        String content = FileReaderService.readFileFromResources("/xsd/success_xsd_001.xml");

        assertThat(content).contains("xs:schema");
    }

    @Test
    void shouldThrowIllegalStateExceptionForMissingResource() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> FileReaderService.readFileFromResources("/missing-resource.txt"));

        assertThat(exception.getMessage()).contains("Не удалось прочитать ресурс");
    }
}
