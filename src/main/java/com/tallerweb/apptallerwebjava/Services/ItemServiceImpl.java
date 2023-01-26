package com.tallerweb.apptallerwebjava.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerweb.apptallerwebjava.DAO.ItemRepository;
import com.tallerweb.apptallerwebjava.Util.dto.ItemDTO;
import com.tallerweb.apptallerwebjava.models.Item;
import com.tallerweb.apptallerwebjava.models.User;

@Service
public class ItemServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(GroupUserServiceImpl.class);

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ItemDTO addItem(ItemDTO itemDTO, String token) throws Exception {

        try { 

            Item item = new Item();
            User user = userService.getUser(token);

            item.setNombre(itemDTO.getNombre());
            item.setDescripcion(itemDTO.getDescripcion());
            item.setCosto(itemDTO.getCosto());
            item.setCreadoPor(user);

            itemRepository.save(item);

            return convertToDto(item);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("No se pudo guardar en base de datos el nuevo item");
        }

    }
    
    public ItemDTO editItem(String idItem, ItemDTO itemDTO) throws Exception {

        try {
            ItemDTO response = new ItemDTO();
            Item item = getItem(idItem);

            if(itemDTO.getNombre() != null){
                item.setNombre(itemDTO.getNombre());
            }

            if(itemDTO.getDescripcion() != null){
                item.setDescripcion(itemDTO.getDescripcion());
            }

            if(itemDTO.getCosto() != null){
                item.setCosto(itemDTO.getCosto());
            }

            if(itemDTO.getPago() != null){
                item.setPago(itemDTO.getPago());
            }

            itemRepository.save(item);

            response = convertToDto(item); 

            return response;            

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("No se pudo guardar en base de datos la edición del item.");
        }

    }

    public Item getItem(String id){
    
        try {
            Optional<Item> optItem = itemRepository.findById(id);
            
            if(optItem.isPresent()){
                Item item = optItem.get();
                return item;
            }

            return null;

        } catch(Exception e) {
            return null;
        }

    }

    public ItemDTO traerItem(String id) {
        try { 
            Item item = getItem(id);
            return convertToDto(item);
        } catch(Exception e){
            logger.error(e.getMessage());
        } 
        return null;
    }

    public void deleteItem(String id) throws Exception {

        try {
            Item item = getItem(id);
            item.setEstado(false);
            itemRepository.save(item);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("No se pudo guardar en base de datos el eliminado del item.");
        }

    }

    public List<ItemDTO> getAll(){
        List<Item> lista = itemRepository.findAll();
        return lista.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private ItemDTO convertToDto(Item item) {
        ItemDTO itemDTO = modelMapper.map(item, ItemDTO.class);
        return itemDTO;
    }

    
    
}
