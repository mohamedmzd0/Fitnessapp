package com.example.coder.fitness;


public class FitnessModel {
    public String name, url, desc;
    public boolean played = false;

    FitnessModel() {
    }

    public FitnessModel(String desc, String name, boolean played, String url) {
        this.name = name;
        this.url = url;
        this.desc = desc;
        this.played = played;
    }
}
