package desafio.itau.springboot.services;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
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
    private final Clock clock;

    public TransactionService(Clock clock) {
        this.clock = clock;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        log.debug("Transação registrada: valor={} dataHora={}", transaction.getValor(), transaction.getDataHora());
    }

    public void clearTransactions() {
        transactions.clear();
        log.info("Fila de transações limpa");
    }

    public DoubleSummaryStatistics getStatistics() {
        OffsetDateTime now = OffsetDateTime.now(clock);
        OffsetDateTime cutoff = now.minusSeconds(getWindowSeconds());

        transactions.removeIf(t -> t.getDataHora().isBefore(cutoff));

        DoubleSummaryStatistics statistics = transactions.stream()
                .filter(t -> !t.getDataHora().isBefore(cutoff) && !t.getDataHora().isAfter(now))
                .mapToDouble(Transaction::getValor)
                .summaryStatistics();
        log.trace("Estatísticas atualizadas: {} elementos considerados", statistics.getCount());
        return statistics;
    }

    public long getWindowSeconds() {
        return 60;
    }
}
