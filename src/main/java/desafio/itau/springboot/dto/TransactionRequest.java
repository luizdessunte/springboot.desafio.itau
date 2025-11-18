package desafio.itau.springboot.dto;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * DTO imutável usado para desserializar o corpo JSON de /transacao.
 */
public record TransactionRequest(
        @NotNull @DecimalMin(value = "0.0", inclusive = true) Double valor,

        @NotNull OffsetDateTime dataHora) {
}
