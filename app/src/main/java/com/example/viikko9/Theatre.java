package com.example.viikko9;

public class Theatre {

    private String name;
    private int ID;

    public Theatre(String name, int ID){
        this.name = name;
        this.ID = ID;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public int getID(){
        return this.ID;
    }
    
    public void setID(int ID){
        this.ID = ID;
    }

    /*@Override
    public String toString() {
        return this.name; // Value to be displayed in the Spinner
    }*/

}
