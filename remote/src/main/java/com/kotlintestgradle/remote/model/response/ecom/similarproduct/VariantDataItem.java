package com.kotlintestgradle.remote.model.response.ecom.similarproduct;

import com.google.gson.annotations.SerializedName;

public class VariantDataItem{

	@SerializedName("value")
	private String value;

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return value;
	}
}