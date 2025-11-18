package desafio.itau.springboot.controller;

import java.time.Clock;
import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import desafio.itau.springboot.model.Transaction;
import desafio.itau.springboot.dto.TransactionRequest;
import desafio.itau.springboot.services.TransactionService;

@RestController
@RequestMapping("/transacao")
public class TransactionController {

    private final TransactionService transactionService;
    private final Clock clock;
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    public TransactionController(TransactionService transactionsService, Clock clock) {
        this.transactionService = transactionsService;
        this.clock = clock;
    }

    @PostMapping
    public ResponseEntity<Void> createTransaction(@Valid @RequestBody TransactionRequest request) {
        if (request.dataHora().isAfter(OffsetDateTime.now(clock)) || request.valor() < 0) {
            log.warn("Transação rejeitada: valor={} dataHora={} (futura ou negativa)", request.valor(),
                    request.dataHora());
            return ResponseEntity.unprocessableEntity().build();
        }
        transactionService.addTransaction(new Transaction(request.valor(), request.dataHora()));
        log.info("Transação aceita: valor={} dataHora={}", request.valor(), request.dataHora());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearTransactions() {
        transactionService.clearTransactions();
        log.info("Todas as transações foram limpas a pedido do cliente");
        return ResponseEntity.ok().build();
    }

}
