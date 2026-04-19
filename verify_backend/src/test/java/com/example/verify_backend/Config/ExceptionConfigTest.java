package com.example.verify_backend.Config;

import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Exception.NoAffectException;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExceptionConfigTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new DummyController())
                .setControllerAdvice(new ExceptionConfig())
                .setValidator(validator)
                .build();
    }

    @Test
    void shouldReturnBadRequestForValidationErrors() throws Exception {
        mockMvc.perform(post("/dummy/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void shouldReturnBadRequestForBusinessLogicException() throws Exception {
        mockMvc.perform(post("/dummy/business"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("business"));
    }

    @Test
    void shouldReturnConflictForNoAffectException() throws Exception {
        mockMvc.perform(post("/dummy/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.message").value("conflict"));
    }

    @Test
    void shouldReturnInternalServerErrorForUnhandledExceptions() throws Exception {
        mockMvc.perform(post("/dummy/error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("Внутренняя ошибка сервиса"));
    }

    @RestController
    static class DummyController {

        @PostMapping("/dummy/validation")
        DummyRequest validation(@Valid @RequestBody DummyRequest request) {
            return request;
        }

        @PostMapping("/dummy/business")
        void business() {
            throw new BusinessLogicException("business");
        }

        @PostMapping("/dummy/conflict")
        void conflict() {
            throw new NoAffectException("conflict");
        }

        @PostMapping("/dummy/error")
        void error() {
            throw new RuntimeException("boom");
        }
    }

    static class DummyRequest {
        @NotBlank
        @JsonProperty("name")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
