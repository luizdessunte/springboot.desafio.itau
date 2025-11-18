package desafio.itau.springboot.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import desafio.itau.springboot.services.TransactionService;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TransactionControllerTest.FixedClockConfig.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionService transactionService;

    @BeforeEach
    void cleanState() {
        transactionService.clearTransactions();
    }

    @Test
    void shouldReturnCreatedWhenTransactionIsValid() throws Exception {
        TransactionRequestPayload payload = new TransactionRequestPayload(123.45, "2024-04-15T11:59:30Z");

        mockMvc.perform(post("/transacao")
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(payload))))
                .andExpect(status().isCreated());

        assertThat(transactionService.getStatistics().getCount()).isEqualTo(1);
    }

    @Test
    void shouldReturn422WhenTransactionIsInTheFuture() throws Exception {
        TransactionRequestPayload payload = new TransactionRequestPayload(10.0, "2024-04-15T12:10:00Z");

        mockMvc.perform(post("/transacao")
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(payload))))
                .andExpect(status().isUnprocessableEntity());

        assertThat(transactionService.getStatistics().getCount()).isZero();
    }

    @Test
    void shouldReturn422WhenTransactionIsNegative() throws Exception {
        TransactionRequestPayload payload = new TransactionRequestPayload(-1.0, "2024-04-15T11:59:30Z");

        mockMvc.perform(post("/transacao")
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(payload))))
                .andExpect(status().isUnprocessableEntity());

        assertThat(transactionService.getStatistics().getCount()).isZero();
    }

    private record TransactionRequestPayload(Double valor, String dataHora) {
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class FixedClockConfig {
        @Bean
        @Primary
        Clock testClock() {
            return Clock.fixed(Instant.parse("2024-04-15T12:00:00Z"), ZoneOffset.UTC);
        }
    }
}
