package com.tallerweb.apptallerwebjava.Util.dto;

public class LoginDTO {
	
	private String correo;
	private String nombre;
	private String password;
	private String token;

	public LoginDTO() {
	}

	public LoginDTO(String correo, String nombre, String password, String token) {
		this.correo = correo;
		this.nombre = nombre;
		this.password = password;
		this.token = token;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}


}
