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

import com.tallerweb.apptallerwebjava.Services.GroupUserServiceImpl;
import com.tallerweb.apptallerwebjava.Services.SecurityServiceImpl;
import com.tallerweb.apptallerwebjava.Util.dto.GroupDTO;
import com.tallerweb.apptallerwebjava.Util.dto.IdDTO;
import com.tallerweb.apptallerwebjava.Util.rest.WrapperResponse;

@RestController
@RequestMapping("/api/groupusers")
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

			return ResponseEntity.ok(new WrapperResponse<GroupDTO>(true, "", response));
		} catch (Exception e) {
			return ResponseEntity.ok(new WrapperResponse<GroupDTO>(false, e.getMessage()));
		}
	}

    // Traer todos los grupos a los que pertenece ese usuario.
    @GetMapping(path="grupos/")
	public ResponseEntity<WrapperResponse<?>> getGruposUsuario(@RequestHeader("Authorization") String token) {
		try {
            logger.info("GroupUserRest.getGruposUsuario");

            if(!securityService.isLogged(token)){
                throw new Exception("No se encuentra logueado, token invalido.");
            }

			List<GroupDTO> response = groupUserService.getGruposUsuario(token);

			return ResponseEntity.ok(new WrapperResponse<List<GroupDTO>>(true, "", response));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((new WrapperResponse<List<GroupDTO>>(false, e.getMessage())));
		}
	}

    // Traer un grupo por id
    @GetMapping(path="/{id}")
    public ResponseEntity<WrapperResponse<?>> getGrupo(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
        try {
            logger.info("GroupUserRest.getGrupo");

            if(!securityService.isLogged(token)){
                throw new Exception("No se encuentra logueado, token invalido.");
            }

            if(!securityService.perteneceGrupo(token, id)){
                throw new Exception("No pertenece al grupo que quiere ver.");
            }

            GroupDTO response = groupUserService.getGrupo(id);

            return ResponseEntity.ok(new WrapperResponse<GroupDTO>(true, "", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((new WrapperResponse<GroupDTO>(false, e.getMessage())));
        }
    }

    // Agregar un usuario a un grupo.
    @PutMapping(path="/userAdd/{id}")
    public ResponseEntity<WrapperResponse<?>> agregarUsuarioGrupo(@PathVariable("id") String idGrupo, @RequestBody IdDTO idUser, @RequestHeader("Authorization") String token) {
        try {
            logger.info("GroupUserRest.agregarUsuarioGrupo");

            if(!securityService.isLogged(token)){
                throw new Exception("No se encuentra logueado, token invalido.");
            }

            if(!securityService.adminGrupo(token, idGrupo)){
                throw new Exception("No se puede agregar personas al grupo del que no sos admin.");
            }

            GroupDTO response = groupUserService.addUserGroup(idGrupo, idUser.getId());

            return ResponseEntity.ok(new WrapperResponse<GroupDTO>(true, "", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((new WrapperResponse<GroupDTO>(false, e.getMessage())));
        }
    }

    // Editar un grupo
    @PutMapping(path="/{id}")
    public ResponseEntity<WrapperResponse<?>> editGrupo(@PathVariable("id") String id, @RequestBody GroupDTO groupDTO, @RequestHeader("Authorization") String token) {
        try {
            logger.info("GroupUserRest.editGrupo");

            if(!securityService.isLogged(token)){
                throw new Exception("No se encuentra logueado, token invalido.");
            }

            if(!securityService.adminGrupo(token, id)){
                throw new Exception("No se puede editar el grupo del que no sos admin.");
            }

            GroupDTO response = groupUserService.editGroup(groupDTO, id);

            return ResponseEntity.ok(new WrapperResponse<GroupDTO>(true, "", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((new WrapperResponse<GroupDTO>(false, e.getMessage())));
        }
    }

    // Borrar grupo
    @DeleteMapping(path="/{id}")
    public ResponseEntity<WrapperResponse<String>> deleteGroup(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
        try {
            logger.info("UserRest.deleteGroup");

            if(!securityService.isLogged(token)){
                throw new Exception("No se encuentra logueado, token invalido.");
            }

            if(!securityService.adminGrupo(token, id)){
                throw new Exception("No se puede editar el grupo del que no sos admin.");
            }

            groupUserService.deleteGroup(id);

            return ResponseEntity.ok(new WrapperResponse<String>(true, "Usuario eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.ok((new WrapperResponse<String>(false, e.getMessage())));
        }
    }

    // Borrar un usuario a un grupo.
    @PutMapping(path="/userDelete/{id}")
    public ResponseEntity<WrapperResponse<?>> deleteUsuarioGrupo(@PathVariable("id") String idGrupo, @RequestBody IdDTO idUser, @RequestHeader("Authorization") String token) {
        try {
            logger.info("GroupUserRest.deleteUsuarioGrupo");

            if(!securityService.isLogged(token)){
                throw new Exception("No se encuentra logueado, token invalido.");
            }

            if(!securityService.adminGrupo(token, idGrupo)){
                throw new Exception("No se puede eliminar personas del grupo del que no sos admin.");
            }

            GroupDTO response = groupUserService.deleteUserGroup(idGrupo, idUser.getId());

            return ResponseEntity.ok(new WrapperResponse<GroupDTO>(true, "", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((new WrapperResponse<GroupDTO>(false, e.getMessage())));
        }
    }
    
}
