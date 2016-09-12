package br.com.thiengo.pagamentosapp.domain;

import android.util.Log;
import android.webkit.JavascriptInterface;

import java.util.Observable;
import java.util.Observer;

public class CreditCard extends Observable {
    private String cardNumber;
    private String name;
    private String month;
    private String year;
    private String cvv;
    private int parcels;
    private String error;
    private String token;

    public CreditCard(Observer observer){
        addObserver( observer );
    }

    @JavascriptInterface
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @JavascriptInterface
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JavascriptInterface
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @JavascriptInterface
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @JavascriptInterface
    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public int getParcels() {
        return parcels;
    }

    public void setParcels(int parcels) {
        this.parcels = parcels;
    }

    public String getError() {
        return error;
    }

    @JavascriptInterface
    public void setError(String... errors) {
        for( String e : errors ){
            if( e.equalsIgnoreCase("card_number") ){
                error += "Número do cartão, inválido; ";
            }
        }
        Log.i("log", "error: "+error);

        setChanged();
        notifyObservers();
    }

    public String getToken() {
        return token;
    }

    @JavascriptInterface
    public void setToken(String token) {
        this.token = token;
        Log.i("log", "Token: "+token);

        setChanged();
        notifyObservers();
    }
}
