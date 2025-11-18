package desafio.itau.springboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "statistics")
public class StatisticsProperties {

    /**
     * Janela móvel em segundos para o cálculo das estatísticas.
     */
    private long windowSeconds = 60;

    public long getWindowSeconds() {
        return windowSeconds;
    }

    public void setWindowSeconds(long windowSeconds) {
        if (windowSeconds <= 0) {
            throw new IllegalArgumentException("windowSeconds must be positive");
        }
        this.windowSeconds = windowSeconds;
    }
}
