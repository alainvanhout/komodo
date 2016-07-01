package komodo.domain;

import java.time.LocalDateTime;

public class UrlCheck implements Check {
    private String url;
    // in seconds
    private double interval = 60;
    private transient LocalDateTime last;

    public UrlCheck(String url) {
        this.url = url;
        last = LocalDateTime.now();
    }

    public String getUrl() {
        return this.url;
    }

    public UrlCheck url(String url) {
        this.url = url;
        return this;
    }

    public double getInterval() {
        return this.interval;
    }

    public UrlCheck interval(int interval) {
        this.interval = interval;
        return this;
    }

    public LocalDateTime getLast() {
        return last;
    }

    public UrlCheck last(LocalDateTime last) {
        this.last = last;
        return this;
    }
}