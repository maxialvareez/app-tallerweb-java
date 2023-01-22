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

import com.tallerweb.apptallerwebjava.Services.GroupUserServiceImpl;
import com.tallerweb.apptallerwebjava.Services.SecurityServiceImpl;
import com.tallerweb.apptallerwebjava.Util.dto.GroupDTO;
import com.tallerweb.apptallerwebjava.Util.rest.WrapperResponse;

@RestController
@RequestMapping("/api/groupuser")
public class GroupUserRest {

    private static final Logger logger = LoggerFactory.getLogger(GroupUserRest.class);

    @Autowired
    private SecurityServiceImpl securityService;

    @Autowired
    private GroupUserServiceImpl groupUserService;

    // Registrar grupo
    @PostMapping(path="/")
	public ResponseEntity<WrapperResponse<GroupDTO>> registrarGrupo(@RequestBody GroupDTO groupResponseDTO, @RequestHeader("Authorization") String token) {
		try {
            GroupDTO response = new GroupDTO();
            logger.info("GroupUserRest.registrarGrupo");

            if(!securityService.isLogged(token)){
                throw new Exception("No se encuentra logueado, token invalido.");
            }

            response = groupUserService.registrarGrupo(groupResponseDTO, token);

			return ResponseEntity.ok(new WrapperResponse(true, "", response));
		} catch (Exception e) {
			return ResponseEntity.ok(new WrapperResponse(false, e.getMessage()));
		}
	}
    
}
