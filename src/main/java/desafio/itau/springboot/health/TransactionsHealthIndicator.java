package desafio.itau.springboot.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import desafio.itau.springboot.services.TransactionService;

@Component
public class TransactionsHealthIndicator implements HealthIndicator {

    private final TransactionService transactionService;

    public TransactionsHealthIndicator(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Health health() {
        return Health.up()
                .withDetail("windowSeconds", transactionService.getWindowSeconds())
                .build();
    }
}
