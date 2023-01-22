package com.tallerweb.apptallerwebjava.Services;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.tallerweb.apptallerwebjava.DAO.GroupUserRepository;
import com.tallerweb.apptallerwebjava.DAO.UserRepository;
import com.tallerweb.apptallerwebjava.Util.dto.GroupDTO;
import com.tallerweb.apptallerwebjava.Util.dto.LoginResponseDTO;
import com.tallerweb.apptallerwebjava.models.GroupUser;
import com.tallerweb.apptallerwebjava.models.User;

@Service
public class GroupUserServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(GroupUserServiceImpl.class);

    @Autowired
    private SecurityServiceImpl securityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupUserRepository groupUserRepository;

    @Autowired
    private ModelMapper modelMapper;

    public GroupDTO registrarGrupo(GroupDTO groupDTO, String token) throws Exception {
        
        try { 
            String id = securityService.parseJWT(token);
            Optional<User> optUser = userRepository.findById(id);

            if(!optUser.isPresent()){
                throw new Exception("No hay usuario con ese token.");
            }

            if(groupDTO.getNombre() == null || groupDTO.getDescripcion() == null) {
                throw new Exception("Falta el nombre o la descripcion.");
            }

            GroupUser groupUser = new GroupUser(groupDTO.getNombre(), groupDTO.getDescripcion(), optUser.get());
            groupUserRepository.save(groupUser);
            return convertToDto(groupUser);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

    }

    private GroupDTO convertToDto(GroupUser groupUser) {
        GroupDTO groupDTO = modelMapper.map(groupUser, GroupDTO.class);
        return groupDTO;
    }
    
}