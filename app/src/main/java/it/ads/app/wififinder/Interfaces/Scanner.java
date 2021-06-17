package it.ads.app.wififinder.Interfaces;

public interface Scanner {

    void startScanner();

    void stopScanner();

    boolean isEnabled();

    void scannerError(String error);
}
