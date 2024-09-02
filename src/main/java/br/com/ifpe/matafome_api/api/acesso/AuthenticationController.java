package br.com.ifpe.matafome_api.api.acesso;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.modelo.acesso.UsuarioService;
import br.com.ifpe.matafome_api.modelo.cliente.Cliente;
import br.com.ifpe.matafome_api.modelo.cliente.ClienteService;
import br.com.ifpe.matafome_api.modelo.empresa.Empresa;
import br.com.ifpe.matafome_api.modelo.empresa.EmpresaService;
import br.com.ifpe.matafome_api.modelo.seguranca.JwtService;


@RestController
@RequestMapping("/api/login")
@CrossOrigin
public class AuthenticationController {

    private final JwtService jwtService;
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmpresaService empresaService;


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
        loginResponse.put("token", jwtToken);
        loginResponse.put("tokenExpiresIn", jwtService.getExpirationTime());

        // Verifica a role do usuário
        if (authenticatedUser.getRoles().contains(Usuario.ROLE_CLIENTE)) {

            Cliente cliente = clienteService.findByUsuarioUsername(authenticatedUser.getUsername());
            loginResponse.put("ClienteData", cliente);

        } else if (authenticatedUser.getRoles().contains(Usuario.ROLE_EMPRESA_USER)) {

            Empresa empresa = empresaService.findByUsuarioUsername(authenticatedUser.getUsername());
            loginResponse.put("EmpresaData", empresa);

        }
         

        return loginResponse;
    }  

    @GetMapping("/validar/{idUser}")
    public ResponseEntity<String> validarEmail(@PathVariable Long idUser) {
        boolean isEmailValidated = usuarioService.validarEmail(idUser);

        if (isEmailValidated) {
            return ResponseEntity.ok("Email validado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao validar o email.");
        }
    }
    
    @PostMapping("/enviarCodigoDeRecuperacao")
    public ResponseEntity<String> enviarCodigoDeRecuperacao(@RequestBody Map<String, String> request) {
        String email = request.get("email");
    
        try {
            String responseMessage = usuarioService.enviarCodigo(email);
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao enviar o código de recuperação de senha: " + e.getMessage());
        }
    }

    @PostMapping("/validarCodigoDeRecuperação")
    public Boolean validarCodigo(@RequestBody TrocarSenhaRequest request ) {
        String email = request.getEmail();
        Integer codigo = request.getCodigo();

        return usuarioService.validarCodigo(codigo,email);
    }
    

    @PostMapping("/trocarSenha")
    public ResponseEntity<String> trocarSenha(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String novaSenha = request.get("novaSenha");

        try {          
                usuarioService.trocarSenha(novaSenha, email);
                return ResponseEntity.ok("Senha trocada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao trocar a senha: " + e.getMessage());
        }
    }
        
}
