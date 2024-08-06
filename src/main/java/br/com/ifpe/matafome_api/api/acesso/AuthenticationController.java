package br.com.ifpe.matafome_api.api.acesso;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.modelo.acesso.UsuarioService;
import br.com.ifpe.matafome_api.modelo.seguranca.JwtService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthenticationController {

    private final JwtService jwtService;
    
    private UsuarioService usuarioService;


    public AuthenticationController(JwtService jwtService, UsuarioService usuarioService) {

        this.jwtService = jwtService;   
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Map<Object, Object> signin(@RequestBody AuthenticationRequest data) {
    
        Usuario authenticatedUser = usuarioService.authenticate(data.getUsername(), data.getPassword());

        String jwtToken = jwtService.generateToken(authenticatedUser);

        Map<Object, Object> loginResponse = new HashMap<>();
        loginResponse.put("username", authenticatedUser.getUsername());
        loginResponse.put("idUser", authenticatedUser.getId() );
        loginResponse.put("token", jwtToken);
        loginResponse.put("tokenExpiresIn", jwtService.getExpirationTime());

        return loginResponse;
    }    
}
