package com.samples.yulya.phcollage;

import java.util.HashMap;

public class Image extends HashMap<String, String> {
    public static final String URL = "url";
    public static final String CHECK = "check";

    public Image(String url, String check) {
        super();
        super.put(URL, url);
        super.put(CHECK, check);

    }
    public String getUrl(){
        return super.get(URL);
    }
    public String getCheck(){
        return super.get(CHECK);
    }
    public void setUrl(String url){
        super.put(URL, url);
    }
    public void setCheck(String check){
        super.put(CHECK, check);
    }
}
