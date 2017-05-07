package br.com.aurum.astrea.filter;

import java.io.Serializable;

public class ContactFilter implements Serializable{

	private static final long serialVersionUID = 1299617019754081880L;
	private String name;
	private String email;
	private String cpf;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

}
