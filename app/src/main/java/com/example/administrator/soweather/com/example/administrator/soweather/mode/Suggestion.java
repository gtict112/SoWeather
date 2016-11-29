package com.example.administrator.soweather.com.example.administrator.soweather.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/17.
 */

public class Suggestion {
    public String flubrf;
    public String flutex;
    public String drsgbrf;
    public String drsgtex;
    public String sportbrf;
    public String sporttex;
    public String travbrf;
    public String travtex;
    public String cwbrf;
    public String cwtex;
    public String uvbrf;
    public String uvtex;
    public String comfbrf;
    public String comftex;

    public static class Builder implements Serializable {
        public static Suggestion creatSuggestion() {
            return new Suggestion();
        }
    }
}
