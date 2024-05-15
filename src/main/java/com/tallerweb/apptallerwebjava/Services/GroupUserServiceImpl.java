package com.tallerweb.apptallerwebjava.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerweb.apptallerwebjava.DAO.GroupUserRepository;
import com.tallerweb.apptallerwebjava.DAO.UserRepository;
import com.tallerweb.apptallerwebjava.Util.dto.GroupDTO;
import com.tallerweb.apptallerwebjava.models.GroupUser;
import com.tallerweb.apptallerwebjava.models.Item;
import com.tallerweb.apptallerwebjava.models.User;

@Service
public class GroupUserServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(GroupUserServiceImpl.class);

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private GroupUserRepository groupUserRepository;

    @Autowired
    private ModelMapper modelMapper;

    public GroupDTO registrarGrupo(GroupDTO groupDTO, String token) throws Exception {
        
        try { 

            User user = userService.getUser(token);

            if(groupDTO.getNombre() == null || groupDTO.getDescripcion() == null) {
                throw new Exception("Falta el nombre o la descripcion.");
            }

            GroupUser groupUser = new GroupUser(groupDTO.getNombre(), groupDTO.getDescripcion(), user);
            groupUserRepository.save(groupUser);

            return convertToDto(groupUser);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

    }

    public GroupDTO getGrupo(String id){
        return convertToDto(getGroup(id));
    }

    public GroupUser getGroup(String id){
    
        try {
            Optional<GroupUser> optGroup = groupUserRepository.findById(id);
            
            if(optGroup.isPresent()){
                GroupUser group = optGroup.get();
                return group;
            }

            return null;

        } catch(Exception e) {
            return null;
        }

    }

    public List<GroupDTO> getGruposUsuario(String token){
        try {
            List<GroupDTO> listaDTO = new ArrayList<>();
            List<GroupDTO> response = new ArrayList<>();
            User user = userService.getUser(token);
            
            listaDTO = getAll();

            for(GroupDTO group: listaDTO){
                if(group.getIntegrantes().contains(user)){
                    response.add(group);
                }
            }

            return response;
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        
        return null;
    }

    public GroupDTO addUserGroup(String idGroup, String correo) throws Exception {
        try {
            Optional<User> optUser = userRepository.findByCorreo(correo);
            GroupUser group = getGroup(idGroup);
            
            if(optUser.isPresent()){
                User user = optUser.get();
                if(group.getIntegrantes().contains(user)){
                    throw new Exception("Ya está en el grupo.");
                }

                group.addIntegrante(user);
                groupUserRepository.save(group);
    
                return convertToDto(group);
            }

            return null;

        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        
        return null;
    }

    public GroupDTO editGroup(GroupDTO groupDTO, String idGroup) throws Exception {
       
        try {
            GroupUser group = getGroup(idGroup);

            if(groupDTO.getNombre() != null){
                group.setNombre(groupDTO.getNombre());
            }

            if(groupDTO.getDescripcion() != null){
                group.setDescripcion(groupDTO.getDescripcion());
            }

            groupUserRepository.save(group);

            GroupDTO response = convertToDto(group); 

            return response;            

        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        
        return null;
    }

    public void deleteGroup(String id) throws Exception {

        try {
            GroupUser group = getGroup(id);
            group.setEstado(false);
            groupUserRepository.save(group);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("No se pudo guardar en base de datos el eliminado del grupo");
        }

    }

    public GroupDTO deleteUserGroup(String idGroup, String correo) throws Exception {
        try {
            Optional<User> optUser = userRepository.findByCorreo(correo);
            GroupUser group = getGroup(idGroup);
            
            if(optUser.isPresent()){
                User user = optUser.get();
                if(!group.getIntegrantes().contains(user)){
                    throw new Exception("No está en el grupo, no se puede eliminar.");
                }

                group.deleteIntegrante(user);
                groupUserRepository.save(group);
    
                return convertToDto(group);
            }

            return null;

        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        
        return null;
    }

    public void deleteUserFromGroups(String token) throws Exception {
        try {
            User user = userService.getUser(token);
            List<GroupUser> lista = groupUserRepository.findAll();
            
            for(GroupUser group: lista){
                for(User userLoop: group.getIntegrantes()){
                    if(userLoop.getId().equals(user.getId())){
                        group.deleteIntegrante(userLoop);
                        groupUserRepository.save(group);
                        continue;
                    }
                }
            }

        } catch(Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void editUserGroup(User user, String token) throws Exception {
        try {
            List<GroupUser> lista = groupUserRepository.findAll();

            for(GroupUser group: lista){
                for(User userLoop: group.getIntegrantes()){
                    if(userLoop.getId().equals(user.getId())){
                        group.deleteIntegrante(userLoop);
                        group.addIntegrante(user);
                        groupUserRepository.save(group);
                        continue;
                    }
                }
                
            }
            
            
        } catch(Exception e){
            throw new Exception("No se puede editar el usuario en dicho grupo.");
        }
        
    }

    public void addItemGroup(Item item, String idGrupo) throws Exception {
        try {
            GroupUser group = getGroup(idGrupo);
            group.addItem(item);
            groupUserRepository.save(group);
        } catch(Exception e){
            throw new Exception("No se puede agregar el item al grupo.");
        }
        
    }

    public void editItemGroup(Item item, String idItem, String idGrupo) throws Exception {
        try {
            Item itemEdit = itemService.getItem(idItem);
            GroupUser group = getGroup(idGrupo);

            for(Item itemLoop: group.getItems()){
                if(itemLoop.getId().equals(itemEdit.getId())){
                    group.deleteItem(itemLoop);
                    group.addItem(itemEdit);
                    groupUserRepository.save(group);
                }
                continue;
            }

        } catch(Exception e){
            throw new Exception("No se puede agregar el item al grupo.");
        }
        
    }

    public void deleteItemGroup(Item item, String idGrupo) throws Exception {
        try {
            GroupUser group = getGroup(idGrupo);
            Boolean borrar = false;
            for(Item itemLoop: group.getItems()){
                if(itemLoop.getId().equals(item.getId())){
                    borrar = true;
                    break;
                }
            }

            if(borrar){
                group.deleteItem(item);
                groupUserRepository.save(group);
            }
        } catch(Exception e){
            throw new Exception("No se puede eliminar el item del grupo.");
        }
        
    }

    public List<GroupDTO> getAll(){
        List<GroupUser> lista = groupUserRepository.findAll();
        return lista.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public GroupDTO convertToDto(GroupUser groupUser) {
        GroupDTO groupDTO = modelMapper.map(groupUser, GroupDTO.class);
        return groupDTO;
    }
    
}
