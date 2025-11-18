package desafio.itau.springboot.dto;

import java.util.DoubleSummaryStatistics;

public class StatisticsResponse {

    private long count;
    private double sum;
    private double avg;
    private double min;
    private double max;

    public StatisticsResponse(DoubleSummaryStatistics stats) {
        if (stats.getCount() == 0) {
            this.count = 0;
            this.sum = 0;
            this.avg = 0;
            this.min = 0;
            this.max = 0;
            return;
        }
        this.count = stats.getCount();
        this.sum = stats.getSum();
        this.avg = stats.getAverage();
        this.min = stats.getMin();
        this.max = stats.getMax();
    }

    public double getAvg() {
        return avg;
    }

    public long getCount() {
        return count;
    }

    public double getSum() {
        return sum;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}