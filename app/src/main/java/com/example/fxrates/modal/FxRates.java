package com.example.fxrates.modal;

public class FxRates {
  private String base;
  private String date;
  private String rates;

    public FxRates(String base, String date, String rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
    }
    public FxRates(){}
    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }
}
