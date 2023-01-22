package com.tallerweb.apptallerwebjava.Util.dto;

public class LoginResponseDTO {
	
	private String id;
	private String correo;
	private String nombre;
	private String token;

	public LoginResponseDTO(String id, String correo, String nombre, String token) {
		this.id = id;
		this.correo = correo;
		this.nombre = nombre;
		this.token = token;
	}

	public LoginResponseDTO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
