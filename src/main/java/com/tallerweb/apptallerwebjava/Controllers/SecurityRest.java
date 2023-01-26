package com.tallerweb.apptallerwebjava.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tallerweb.apptallerwebjava.Services.SecurityServiceImpl;
import com.tallerweb.apptallerwebjava.Util.dto.LoginDTO;
import com.tallerweb.apptallerwebjava.Util.dto.LoginResponseDTO;
import com.tallerweb.apptallerwebjava.Util.rest.WrapperResponse;

@RestController
public class SecurityRest {

    @Autowired
	private SecurityServiceImpl securityService;
	
	@PostMapping(path="login")
	public ResponseEntity<WrapperResponse<LoginResponseDTO>> login(@RequestBody LoginDTO loginDTO) {
		try {
			LoginResponseDTO response = this.securityService.login(loginDTO);
			return ResponseEntity.ok(new WrapperResponse<LoginResponseDTO>(true, "", response));
		} catch (Exception e) {
			return ResponseEntity.ok(new WrapperResponse<LoginResponseDTO>(false, e.getMessage()));
		}
	}
	
	@GetMapping(path="token/validate")
	public WrapperResponse<LoginResponseDTO> tokenValidate(@RequestParam("token") String token) {
		try {
			LoginResponseDTO user = securityService.decriptToken(token);
			return new WrapperResponse<LoginResponseDTO>(true, "", user);
		} catch (Exception e) {
			return new WrapperResponse<LoginResponseDTO>(false, e.getMessage());
		}
		
	}
    
}
