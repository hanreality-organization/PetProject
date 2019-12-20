package com.punuo.pet.home.care.model;

import com.google.gson.annotations.SerializedName;

public class PetData {
    @SerializedName("petname")
    public String petName;

    public PetData(String petName){
        this.petName = petName;
    }

    public String getPetName(){
        return petName;
    }
}
