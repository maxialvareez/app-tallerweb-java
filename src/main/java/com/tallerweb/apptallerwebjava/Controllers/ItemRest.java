package com.tallerweb.apptallerwebjava.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tallerweb.apptallerwebjava.Services.SecurityServiceImpl;
import com.tallerweb.apptallerwebjava.Services.UserServiceImpl;
import com.tallerweb.apptallerwebjava.Util.dto.ItemDTO;
import com.tallerweb.apptallerwebjava.Util.dto.LoginResponseDTO;
import com.tallerweb.apptallerwebjava.Util.rest.WrapperResponse;

@RestController
@RequestMapping("/api/items")
public class ItemRest {

    private static final Logger logger = LoggerFactory.getLogger(ItemRest.class);

    @Autowired
	private UserServiceImpl userService;

    @Autowired
    private SecurityServiceImpl securityService;

/* 
     // Registrar Item
     @PostMapping(path="/")
     public ResponseEntity<WrapperResponse<LoginResponseDTO>> registrarItem(@RequestBody ItemDTO itemDTO, @RequestHeader("Authorization") String token) {
         try {
             
            ItemDTO response = new ItemDTO();
            logger.info("ItemRest.registrarItem");
 
            //Validar que el grupo exista y crearlo ahí adentro.

            boolean valido = userService.validarUsuario(loginDTO);
 
             if(!valido){
                 throw new Exception("El correo ya se encuentra en uso.");
             }
 
             userService.registrarUsuario(loginDTO);
             response.setNombre(loginDTO.getNombre());
             response.setCorreo(loginDTO.getCorreo());
 
             return ResponseEntity.ok(new WrapperResponse(true, "", response));
         } catch (Exception e) {
             return ResponseEntity.ok(new WrapperResponse(false, e.getMessage()));
         }
     }

     */
    
}
