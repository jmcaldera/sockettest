package com.example.jmcaldera.sockettest.repository.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Order{

	@SerializedName("id")
	private int id;

	@SerializedName("documents")
	private List<Document> documents = null;

	@SerializedName("subsidiary")
	private Subsidiary subsidiary;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setDocuments(List<Document> documents){
		this.documents = documents;
	}

	public List<Document> getDocuments(){
		return documents;
	}

	public void setSubsidiary(Subsidiary subsidiary){
		this.subsidiary = subsidiary;
	}

	public Subsidiary getSubsidiary(){
		return subsidiary;
	}

	@Override
 	public String toString(){
		return
			"Order{\n" +
			"\"documents:\" " + documents +
			", \"id\": " + id +
			", \"subsidiary\": " + subsidiary +
			"\n}";
		}
}