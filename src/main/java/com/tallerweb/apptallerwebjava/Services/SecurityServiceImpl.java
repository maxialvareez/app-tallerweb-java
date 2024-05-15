package com.tallerweb.apptallerwebjava.Services;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tallerweb.apptallerwebjava.DAO.UserRepository;
import com.tallerweb.apptallerwebjava.Util.dto.LoginDTO;
import com.tallerweb.apptallerwebjava.Util.dto.LoginResponseDTO;
import com.tallerweb.apptallerwebjava.models.GroupUser;
import com.tallerweb.apptallerwebjava.models.Item;
import com.tallerweb.apptallerwebjava.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class SecurityServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

	private static final String TOKEN = "t4113rw3b-t4113rw3b-t4113rw3b-t4113rw3b-t4113rw3b";
	private static final int TOKEN_VALIDA_TIME = 86_400_000;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private ItemServiceImpl itemService;

	@Autowired
	private GroupUserServiceImpl groupUserService;

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(4);
	}
	
	public LoginResponseDTO login(LoginDTO request) throws Exception {
		logger.info("Login " + request.getCorreo());
		try {
			LoginResponseDTO response = null;

			if(request.getToken() != null) {
				response = decriptToken(request.getToken());
				Optional<User> optUser = userRepository.findByCorreo(request.getCorreo());
				if (optUser.isPresent())
					response.setToken(createJWT(optUser.get()));
					response.setId(optUser.get().getId().toHexString());
				return response;
			} else {
				String correo = request.getCorreo();

				Optional<User> userOpt = userRepository.findByCorreo(correo);
				if (!userOpt.isPresent()) {
					throw new Exception("Usuario / Password no son correctos - Correo equivocado");
				}
				
				User user = userOpt.get();

				if (!user.getEstado()) {
					throw new Exception("Usuario / Password no son correctos - Cuenta borrada.");
				}

				if(!encoder().matches(request.getPassword(), user.getPassword())) {
					throw new Exception("Usuario / Password no son correctos - Contraseña equivocada.");
				}

				String token = createJWT(user);
				token = token.split(" ")[1];
				
				response = new LoginResponseDTO();
				response.setId(user.getId().toHexString());
				response.setNombre(user.getNombre());
				response.setCorreo(user.getCorreo());
				response.setToken(token);
				
				logger.info(user.getCorreo() + " autenticado.");
			}
			
			return response;

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("Error al autenticar al usuario");
		}

	}
	
	public LoginResponseDTO decriptToken(String token) throws Exception {
		logger.info("Token decript ==> ");
        
		if (token == null || !token.startsWith("Bearer ")) {
        	throw new Exception("Token inválido");
        }

        // Se extrae el token del Header Authorization HTTP.
        String decodeToken = token.substring("Bearer".length()).trim();
        try {
        	String correo =  parseJWT(decodeToken);
        	
        	Optional<User> userOpt = userRepository.findByCorreo(correo);
        	User user = userOpt.get();
        	
        	LoginResponseDTO response = new LoginResponseDTO();
        	response.setToken(token);
        	response.setCorreo(user.getCorreo());
        	return response;
        } catch (Exception e) {
        	throw new Exception("Error al validar el token");
        }
	}

	
	private String createJWT(User user) {
	 
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	 
	    //Let's set the JWT Claims
	    JwtBuilder builder = Jwts.builder().setId(UUID.randomUUID().toString())
	                                .setIssuedAt(now)
	                                .setSubject("SecurityService")
	                                .setIssuer(user.getCorreo())
									.claim("uid", user.getId().toHexString())
	                                .signWith(Keys.hmacShaKeyFor(TOKEN.getBytes()));
	 
	    //if it has been specified, let's add the expiration
	    long expMillis = nowMillis + TOKEN_VALIDA_TIME;
	        Date exp = new Date(expMillis);
	        builder.setExpiration(exp);
	 
	    //Builds the JWT and serializes it to a compact, URL-safe string
	    return "Bearer " + builder.compact();
	}

	 
	public String parseJWT(String jwt)throws Exception {
		try {
			//This line will throw an exception if it is not a signed JWS (as expected)
		    Claims claims = Jwts.parserBuilder()         
		       .setSigningKey(TOKEN.getBytes())
			   .build()
		       .parseClaimsJws(jwt).getBody();

		    return claims.get("uid").toString();
		} catch (Exception e) {
			throw new Exception("Invalid Token");
		}
	    
	}

	public boolean isLogged(String token) throws Exception {

        try {
            parseJWT(token);
			return true;
        } catch(Exception e){
            logger.error(e.getMessage());
        }

		return false;

    }
	
	public boolean authenticateUser(String token, String id) {

        try {
            String uid = parseJWT(token);

            Optional<User> user = userRepository.findById(id);
            if(user.isPresent()){
                if(user.get().getId().toHexString().equals(uid)){
                    return true;
                }
            }

        } catch(Exception e){
            logger.error(e.getMessage());
        }

        return false;

    }

	public boolean perteneceGrupo(String token, String idGrupo) {

        try {
            User user = userService.getUser(token);
			GroupUser group = groupUserService.getGroup(idGrupo);
            if(group.getIntegrantes().contains(user)){
                return true;
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return false;
		
    }

	public boolean perteneceItem(String token, String idItem) {

        try {
            User user = userService.getUser(token);
			Item item = itemService.getItem(idItem);
            if(item.getCreadoPor().equals(user)){
                return true;
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return false;
		
    }

	public boolean adminGrupo(String token, String idGrupo) {

        try {
            User user = userService.getUser(token);
			GroupUser group = groupUserService.getGroup(idGrupo);
            if(group.getCreadoPor().equals(user)){
                return true;
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        return false;
		
    }
}
