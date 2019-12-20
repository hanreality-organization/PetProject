package com.punuo.pet.home.care.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PetNameListModel {
    @SerializedName("petname")
    public List<PetData> mPetNameList;
}
