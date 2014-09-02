package org.mikezerosix.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private boolean auto;
    private String addressTelnet;
    private String addressFTP;
    private int portGame;
    private int portTelnet;
    private int portFTP;

    private String passwordTelnet;
    private String passwordFTP;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public String getAddressTelnet() {
        return addressTelnet;
    }

    public void setAddressTelnet(String addressTelnet) {
        this.addressTelnet = addressTelnet;
    }

    public String getAddressFTP() {
        return addressFTP;
    }

    public void setAddressFTP(String addressFTP) {
        this.addressFTP = addressFTP;
    }

    public int getPortGame() {
        return portGame;
    }

    public void setPortGame(int portGame) {
        this.portGame = portGame;
    }

    public int getPortTelnet() {
        return portTelnet;
    }

    public void setPortTelnet(int portTelnet) {
        this.portTelnet = portTelnet;
    }

    public int getPortFTP() {
        return portFTP;
    }

    public void setPortFTP(int portFTP) {
        this.portFTP = portFTP;
    }

    public String getPasswordTelnet() {
        return passwordTelnet;
    }

    public void setPasswordTelnet(String passwordTelnet) {
        this.passwordTelnet = passwordTelnet;
    }

    public String getPasswordFTP() {
        return passwordFTP;
    }

    public void setPasswordFTP(String passwordFTP) {
        this.passwordFTP = passwordFTP;
    }
}
