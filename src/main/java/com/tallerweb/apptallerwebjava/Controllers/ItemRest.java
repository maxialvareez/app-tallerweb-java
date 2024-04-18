package com.tallerweb.apptallerwebjava.Controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tallerweb.apptallerwebjava.Services.ItemServiceImpl;
import com.tallerweb.apptallerwebjava.Services.SecurityServiceImpl;
import com.tallerweb.apptallerwebjava.Util.dto.IdDTO;
import com.tallerweb.apptallerwebjava.Util.dto.ItemDTO;
import com.tallerweb.apptallerwebjava.Util.rest.WrapperResponse;

@RestController
@RequestMapping("/api/items")
public class ItemRest {

    private static final Logger logger = LoggerFactory.getLogger(ItemRest.class);

    @Autowired
    private SecurityServiceImpl securityService;

    @Autowired
    private ItemServiceImpl itemService;


    // Registrar Item.
    @PostMapping(path="/{id}")
    public ResponseEntity<WrapperResponse<?>> registrarItem(@PathVariable("id") String id, @RequestBody ItemDTO itemDTO, @RequestHeader("Authorization") String token) {
        try {

        logger.info("ItemRest.registrarItem");

        if(!securityService.isLogged(token)){
            throw new Exception("No se encuentra logueado, token invalido.");
        }

        ItemDTO response = itemService.addItem(itemDTO, token, id);

        return ResponseEntity.ok(new WrapperResponse<ItemDTO>(true, "", response));
        
        } catch (Exception e) {
            return ResponseEntity.ok(new WrapperResponse<ItemDTO>(false, e.getMessage()));
        }

    }

    // Borrar item.
    @DeleteMapping(path="/{id}")
    public ResponseEntity<WrapperResponse<String>> deleteItem(@PathVariable("id") String idGrupo, @RequestBody IdDTO idItem, @RequestHeader("Authorization") String token) {
        try {
            logger.info("ItemRest.deleteItem");

            if(!securityService.isLogged(token)){
                throw new Exception("No se encuentra logueado, token invalido.");
            }
            
            itemService.deleteItem(idItem.getId(), idGrupo);

            return ResponseEntity.ok(new WrapperResponse<String>(true, "Item eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.ok((new WrapperResponse<String>(false, e.getMessage())));
        }
    }

    /*
    // Traer todos los items de un grupo.
    @GetMapping(path="/group/{id}")
	public ResponseEntity<WrapperResponse<List<ItemDTO>>> getItems(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
		try {
            logger.info("ItemRest.getItems");

            if(!securityService.isLogged(token)){
                throw new Exception("No se encuentra logueado, token invalido.");
            }
    
            if(!securityService.perteneceGrupo(token, id)){
                throw new Exception("No puede ver los items de ese grupo porque no pertenece a el.");
            }

            List<ItemDTO> response = itemService.getAllFromGroup(id);

			return ResponseEntity.ok(new WrapperResponse<List<ItemDTO>>(true, "", response));

		} catch (Exception e) {
			return ResponseEntity.ok((new WrapperResponse<List<ItemDTO>>(false, e.getMessage())));
		}
	}

    // Traer un item en particular.
    @GetMapping(path="/{id}")
    public ResponseEntity<WrapperResponse<ItemDTO>> getItem(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
        try {
            logger.info("ItemRest.getItem");

            if(!securityService.isLogged(token)){
                throw new Exception("No se encuentra logueado, token invalido.");
            }

            ItemDTO response = itemService.traerItem(id);

            return ResponseEntity.ok(new WrapperResponse<ItemDTO>(true, "", response));
        } catch (Exception e) {
            return ResponseEntity.ok((new WrapperResponse<ItemDTO>(false, e.getMessage())));
        }
    }

    // Editar item.
    @PutMapping(path="/{idGrupo}/{idItem}")
    public ResponseEntity<WrapperResponse<ItemDTO>> editItem(@PathVariable("idGrupo") String idGrupo, @PathVariable("idItem") String idItem , @RequestHeader("Authorization") String token, @RequestBody ItemDTO itemDTO) {
        try {
            logger.info("ItemRest.editItem");

            if(!securityService.isLogged(token)){
                throw new Exception("No se encuentra logueado, token invalido.");
            }

            if(!securityService.perteneceItem(token, idItem)){
                throw new Exception("Es obligatorio haber creado el Item para editarlo.");
            }

            ItemDTO response = itemService.editItem(itemDTO, idItem, idGrupo);

            return ResponseEntity.ok(new WrapperResponse<ItemDTO>(true, "", response));
        } catch (Exception e) {
            return ResponseEntity.ok((new WrapperResponse<ItemDTO>(false, e.getMessage())));
        }
    }
    */
    
}
