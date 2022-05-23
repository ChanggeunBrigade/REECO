package com.example.reeco;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "servers", primaryKeys = {"ip", "port"},
        indices = {@Index(value = "name", unique = true)})
public class Server {
    @NonNull
    private String name;
    @NonNull
    private String ip;
    private int port;
    @NonNull
    private String user;
    @NonNull
    private String password;

    public Server(@NonNull String name, @NonNull String ip,
                  int port, @NonNull String user, @NonNull String password) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getIp() {
        return ip;
    }

    public void setIp(@NonNull String ip) throws IllegalArgumentException {
        // 인자가 IPv4 규칙과 일치하지 않으면 예외를 던집니다.
        // 출처: https://stackoverflow.com/questions/5284147/validating-ipv4-addresses-with-regexp
        if (!ip.matches("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)(\\.(?!$)|$)){4}$")) {
            throw new IllegalArgumentException("invalid ip address");
        }

        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) throws IllegalArgumentException {
        // 1~65535 이외의 범위가 들어올 경우 에러를 던집니다.
        if (port < 1 || 65535 < port) {
            throw new IllegalArgumentException("invalid port number");
        }
        this.port = port;
    }

    @NonNull
    public String getUser() {
        return user;
    }

    public void setUser(@NonNull String user) {
        this.user = user;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }
}