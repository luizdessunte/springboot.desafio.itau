package desafio.itau.springboot.controller;

import java.util.DoubleSummaryStatistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import desafio.itau.springboot.dto.StatisticsResponse;
import desafio.itau.springboot.services.TransactionService;

@RestController
@RequestMapping("/estatistica")
public class StatisticsController {

    private static final Logger log = LoggerFactory.getLogger(StatisticsController.class);
    private final TransactionService transactionService;

    public StatisticsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<StatisticsResponse> getStatistics() {
        DoubleSummaryStatistics stats = transactionService.getStatistics();
        log.debug("Estatísticas calculadas para janela de {} segundos: count={} sum={} avg={} min={} max={}",
                transactionService.getWindowSeconds(), stats.getCount(), stats.getSum(), stats.getAverage(),
                stats.getMin(), stats.getMax());
        return ResponseEntity.ok(new StatisticsResponse(stats));
    }
}