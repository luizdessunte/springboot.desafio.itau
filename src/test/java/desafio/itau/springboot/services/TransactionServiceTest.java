package desafio.itau.springboot.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.DoubleSummaryStatistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import desafio.itau.springboot.config.StatisticsProperties;
import desafio.itau.springboot.model.Transaction;

class TransactionServiceTest {

    private Clock clock;
    private TransactionService service;
    private StatisticsProperties properties;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2024-04-15T12:00:00Z"), ZoneOffset.UTC);
        properties = new StatisticsProperties();
        properties.setWindowSeconds(60);
        service = new TransactionService(clock, properties);
    }

    @Test
    void shouldIncludeTransactionsExactlyOnWindowBoundary() {
        OffsetDateTime boundary = OffsetDateTime.ofInstant(clock.instant().minusSeconds(60), clock.getZone());
        service.addTransaction(new Transaction(50.0, boundary));

        DoubleSummaryStatistics stats = service.getStatistics();

        assertThat(stats.getCount()).isEqualTo(1);
        assertThat(stats.getSum()).isEqualTo(50.0);
    }

    @Test
    void shouldDiscardTransactionsOlderThanWindow() {
        OffsetDateTime old = OffsetDateTime.ofInstant(clock.instant().minusSeconds(120), clock.getZone());
        service.addTransaction(new Transaction(10.0, old));

        DoubleSummaryStatistics stats = service.getStatistics();

        assertThat(stats.getCount()).isZero();
    }

    @Test
    void shouldSummarizeTransactionsWithinWindow() {
        service.addTransaction(new Transaction(10.0, OffsetDateTime.ofInstant(clock.instant(), clock.getZone())));
        service.addTransaction(new Transaction(30.0, OffsetDateTime.ofInstant(clock.instant().minusSeconds(10), clock.getZone())));

        DoubleSummaryStatistics stats = service.getStatistics();

        assertThat(stats.getCount()).isEqualTo(2);
        assertThat(stats.getSum()).isEqualTo(40.0);
        assertThat(stats.getAverage()).isEqualTo(20.0);
        assertThat(stats.getMin()).isEqualTo(10.0);
        assertThat(stats.getMax()).isEqualTo(30.0);
    }
}
