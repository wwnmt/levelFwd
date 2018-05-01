package edu.nuaa.levelFwd;


import org.onlab.packet.MacAddress;

public enum Level {
    WHITELIST(10000,(short)1, ""),
    RELIABLE(10001, (short)2, ""),
    NORMAL(10002, (short)4, ""),
    THREAT(10003, (short)5, ""),
    BLACKLIST(10004, (short)6, "");

    private int code;
    private short port;
    private String mac;
    private Level(int code, short port, String mac) {
        this.code = code;
        this.port = port;
        this.mac = mac;
    }

    public int getCode() {
        return this.code;
    }

    public short getPort() {
        return this.port;
    }

    public MacAddress getMac(){
        return MacAddress.valueOf(this.mac);
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setPort(short port) {
        this.port = port;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public static Level getByValue(int value) {
        for (Level level : values()) {
            if (level.getCode() == value) {
                return level;
            }
        }

        return null;
    }

}