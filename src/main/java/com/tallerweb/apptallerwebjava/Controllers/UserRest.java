package com.tallerweb.apptallerwebjava.Controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tallerweb.apptallerwebjava.Services.SecurityServiceImpl;
import com.tallerweb.apptallerwebjava.Services.UserServiceImpl;
import com.tallerweb.apptallerwebjava.Util.dto.LoginDTO;
import com.tallerweb.apptallerwebjava.Util.dto.LoginResponseDTO;
import com.tallerweb.apptallerwebjava.Util.rest.WrapperResponse;

@RestController()
@RequestMapping("/api/users")
public class UserRest {

    private static final Logger logger = LoggerFactory.getLogger(UserRest.class);

    @Autowired
	private UserServiceImpl userService;

    @Autowired
    private SecurityServiceImpl securityService;

    // Registrar usuario
    @PostMapping(path="/")
	public ResponseEntity<WrapperResponse<LoginResponseDTO>> registrarUsuario(@RequestBody LoginDTO loginDTO) {
		try {
            LoginResponseDTO response = new LoginResponseDTO();
            logger.info("UserRest.registrarUsuario");

            // TODO Validar formato email

            boolean valido = userService.validarUsuario(loginDTO);

            if(!valido){
                throw new Exception("El correo ya se encuentra en uso.");
            }

			response = userService.registrarUsuario(loginDTO);
            
			return ResponseEntity.ok(new WrapperResponse(true, "", response));
		} catch (Exception e) {
			return ResponseEntity.ok(new WrapperResponse(false, e.getMessage()));
		}
	}

    // Traer todos los usuarios (no se usa), faltaría filtrar por los true (no eliminados)
    @GetMapping(path="/")
	public ResponseEntity<WrapperResponse<List<LoginResponseDTO>>> getUsuarios(@RequestHeader("Authorization") String token) {
		try {
            logger.info("UserRest.getUsuarios");

			List<LoginResponseDTO> response = userService.getAll();

			return ResponseEntity.ok(new WrapperResponse<List<LoginResponseDTO>>(true, "", response));
		} catch (Exception e) {
			return ResponseEntity.ok((new WrapperResponse<List<LoginResponseDTO>>(false, e.getMessage())));
		}
	}

    // Editar usuario
    @PutMapping(path="/{id}")
    public ResponseEntity<WrapperResponse<LoginResponseDTO>> editUsuario(@PathVariable("id") String id, @RequestHeader("Authorization") String token,@RequestBody LoginDTO loginDTO) {
        try {
            logger.info("UserRest.editUsuario");

            if(!securityService.authenticateUser(token, id)){
                throw new Exception("No tiene autorizacion para editar este usuario.");
            }

            LoginResponseDTO response = userService.editUsuario(id, loginDTO);

            return ResponseEntity.ok(new WrapperResponse<LoginResponseDTO>(true, "", response));
        } catch (Exception e) {
            return ResponseEntity.ok((new WrapperResponse<LoginResponseDTO>(false, e.getMessage())));
        }
    }

    // Borrar usuario
    @DeleteMapping(path="/{id}")
    public ResponseEntity<WrapperResponse<String>> deleteUsuario(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
        try {
            logger.info("UserRest.deleteUsuario");

            if(!securityService.authenticateUser(token, id)){
                throw new Exception("No tiene autorizacion para borrar este usuario.");
            }

            userService.deleteUsuario(id);

            return ResponseEntity.ok(new WrapperResponse<String>(true, "Usuario eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.ok((new WrapperResponse<String>(false, e.getMessage())));
        }
    }

    // Traer todos los grupos a los que pertenece ese usuario.
    @GetMapping(path="/grupo/{id}")
	public ResponseEntity<WrapperResponse<?>> getGruposUsuario(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
		try {
            logger.info("UserRest.getGruposUsuario");

            if(!securityService.authenticateUser(token, id)){
                throw new Exception("No tiene autorizacion.");
            }

			List<LoginResponseDTO> response = userService.getAll();

			return ResponseEntity.ok(new WrapperResponse(true, "", response));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((new WrapperResponse(false, e.getMessage())));
		}
	}


    
}
