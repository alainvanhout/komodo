package komodo.domain;

import java.time.DateTimeException;
import java.time.LocalDateTime;

public class Endpoint {
    private String url;
    // in seconds
    private int interval = 60;
    private LocalDateTime last;

    public Endpoint(String url) {
        this.url = url;
        last = LocalDateTime.now();
    }


    public String getUrl() {
        return this.url;
    }

    public Endpoint url(String url) {
        this.url = url;
        return this;
    }

    public int getInterval() {
        return this.interval;
    }

    public Endpoint interval(int interval) {
        this.interval = interval;
        return this;
    }

    public LocalDateTime getLast() {
        return last;
    }

    public Endpoint last(LocalDateTime last) {
        this.last = last;
        return this;
    }
}
