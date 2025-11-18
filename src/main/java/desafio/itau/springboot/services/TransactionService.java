package desafio.itau.springboot.services;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import desafio.itau.springboot.model.Transaction;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    private final Queue<Transaction> transactions = new ConcurrentLinkedQueue<>();

    public TransactionService() {
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        log.debug("Transação registrada: valor={} dataHora={}", transaction.getValor(), transaction.getDataHora());
    }

    public void clearTransactions() {
        transactions.clear();
        log.info("Fila de transações limpa");
    }
}
