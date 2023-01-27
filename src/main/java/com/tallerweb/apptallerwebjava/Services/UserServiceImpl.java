package com.tallerweb.apptallerwebjava.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerweb.apptallerwebjava.DAO.UserRepository;
import com.tallerweb.apptallerwebjava.Util.dto.LoginDTO;
import com.tallerweb.apptallerwebjava.Util.dto.LoginResponseDTO;
import com.tallerweb.apptallerwebjava.models.User;

@Service
public class UserServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityServiceImpl securityService;

    @Autowired
    GroupUserServiceImpl groupUserService;
    
    @Autowired
    private ModelMapper modelMapper;

    public boolean validarUsuario(LoginDTO user) throws Exception {
        
        User usuario = userRepository.findByCorreo(user.getCorreo()).orElse(null);
        
        if(usuario != null){
            return false;
        }

        return true;
    }

    public LoginResponseDTO registrarUsuario(LoginDTO user) throws Exception {
        
        User usuario = new User();
        String password = securityService.encoder().encode(user.getPassword());

        try { 
            usuario.setCorreo(user.getCorreo());
            usuario.setNombre(user.getNombre());
            usuario.setPassword(password);
    
            userRepository.save(usuario);

            return convertToDto(usuario);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("No se pudo guardar en base de datos el nuevo usuario");
        }

    }

    public LoginResponseDTO editUsuario(String token, LoginDTO userDTO) throws Exception {
    
        LoginResponseDTO response = new LoginResponseDTO();

        try {
            User user = getUser(token);

            if(userDTO.getNombre() != null){
                user.setNombre(userDTO.getNombre());
            }

            if(userDTO.getPassword() != null){
                String password = securityService.encoder().encode(userDTO.getPassword());
                user.setPassword(password);
            }

            userRepository.save(user);
            groupUserService.editUserGroup(user, token);

            response = convertToDto(user); 

            return response;            

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("No se pudo guardar en base de datos la edición del usuario");
        }

    }

    public void deleteUsuario(String token) throws Exception {

        try {
            User user = getUser(token);
            user.setEstado(false);
            userRepository.save(user);
            groupUserService.deleteUserFromGroups(token);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("No se pudo guardar en base de datos el eliminado del usuario");
        }

    }

    public User getUser(String token){
    
        try {
            String id = securityService.parseJWT(token);
            Optional<User> optUser = userRepository.findById(id);
            
            if(optUser.isPresent()){
                User user = optUser.get();
                return user;
            }

            return null;

        } catch(Exception e) {
            return null;
        }

    }

    public List<LoginResponseDTO> getAll(){
        List<User> lista = userRepository.findAll();
        return lista.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private LoginResponseDTO convertToDto(User user) {
        LoginResponseDTO loginResponseDTO = modelMapper.map(user, LoginResponseDTO.class);
        return loginResponseDTO;
    }

    
}
