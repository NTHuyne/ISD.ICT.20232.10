package com.hust.ict.aims.subsystem.email.simplejavamail;

public class SimplejavamailConfigs {
    private final static SimplejavamailConfigs SIMPLE_JAVA_MAIL_CONFIGS = new SimplejavamailConfigs();

    private SimplejavamailConfigs(){

    }

    public static synchronized SimplejavamailConfigs getInstance(){
        return SIMPLE_JAVA_MAIL_CONFIGS;
    }

    private boolean auth = true;
    private boolean tls = true;
    private String username = "tronghuy230903@gmail.com";
    private String password = "tjfoclaxkycekeem";
    private String host = "smtp.gmail.com";
    private int port = 587;

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public boolean isTls() {
        return tls;
    }

    public void setTls(boolean tls) {
        this.tls = tls;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
