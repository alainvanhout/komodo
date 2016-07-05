package komodo.urlcheck;

import komodo.domain.Check;
import komodo.domain.Effect;

import java.time.LocalDateTime;

public class UrlCheck implements Check {
    private String url;
    // in seconds
    private double interval = 60;
    private transient LocalDateTime last;
    private Effect failureEffect;
    private Boolean success;
    private String username;
    private String password;

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

    @Override
    public String getStatus() {
        return url + " : " + (success == null ? "?" : (success ? "ok" : "failure"));
    }

    @Override
    public Effect getFailureEffect() {
        return failureEffect;
    }

    public Check failureEffect(Effect failureEffect) {
        this.failureEffect = failureEffect;
        return this;
    }

    public Boolean isSuccess() {
        return success;
    }

    public Check success(Boolean success) {
        this.success = success;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UrlCheck username(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UrlCheck password(String password) {
        this.password = password;
        return this;
    }
}