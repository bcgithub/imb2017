package com.bergcomputers.domain;

import javax.validation.constraints.NotNull;

public class Beneficiary extends BaseEntity {
	
	
	@NotNull
	private String iban;
	
	@NotNull
	private String name;
	
	private String details;
	
	private String accountHolder;
	
	
	
	public String getIban(){
		return iban;
	}
	public void setIban(String iban){
		this.iban = iban;
	}
	
	
	
	public String getname(){
		return name;
	}
	public void setName(String iban){
		this.name = name;
	}
	
	
	
	
	public String getDetails(){
		return details;
	}
	
	public void setDetails(String details){
		this.details = details;
	}
	
	
	public String getAccountHolder(){
		return accountHolder;
	}
	
	public void setAccountHolder(String details){
		this.accountHolder = accountHolder;
	}
	

}
