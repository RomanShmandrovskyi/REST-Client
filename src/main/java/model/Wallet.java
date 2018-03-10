package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "wallet")
public class Wallet {
    private int walletId;
    private double balance;

    public double getBalance() {
        return balance;
    }
}