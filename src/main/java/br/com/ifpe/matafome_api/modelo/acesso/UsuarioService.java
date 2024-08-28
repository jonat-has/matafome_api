package br.com.ifpe.matafome_api.modelo.acesso;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.ifpe.matafome_api.modelo.mensagens.EmailService;
import br.com.ifpe.matafome_api.modelo.seguranca.JwtService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;


    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public UsuarioService(UsuarioRepository userRepository, @Lazy AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {

        this.authenticationManager = authenticationManager;
        this.repository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario authenticate(String username, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        return repository.findByUsername(username).orElseThrow();
    }

    @Transactional
    public Usuario findByEmail(String username) {

        return repository.findByUsername(username).get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return repository.findByUsername(username).get();
    }

 

    @Transactional
    public Usuario save(Usuario user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setHabilitado(Boolean.TRUE);
        user.setEmailvalidado(Boolean.FALSE);
        return repository.save(user);
    }

    public Usuario obterUsuarioLogado(HttpServletRequest request) {
    Usuario usuarioLogado = null;
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null) {
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        usuarioLogado = repository.findByUsername(userEmail).get();
        return usuarioLogado;
    }
    return usuarioLogado;
    }

    public boolean validarEmail(Long userId) {

        Usuario user = repository.findById(userId).get();
        
        user.setEmailvalidado(Boolean.TRUE);

        repository.save(user);

        return user.getEmailvalidado();
    }

    private final Random secureRandom = new SecureRandom(); 
    public Integer reedemCode() {
        return secureRandom.nextInt(9000) + 1000;
    }

    public String enviarCodigo(String emailUser) throws MessagingException {
        Usuario user = findByEmail(emailUser);

        // Gera e salva o código de recuperação de senha
        Integer codigoRecuperacao = reedemCode();
        user.setPasswordCode(codigoRecuperacao);


        repository.save(user);

        emailService.enviarEmailRecuperarSenha(user);

        return "Código enviado para o email: " + emailUser;
    }

    public Boolean validarCodigo(Integer codigo, String emailUser) {
        Usuario user = findByEmail(emailUser);
        Integer userCodigo = user.getPasswordCode();

        // Exemplo de como usar uma comparação segura
        return userCodigo != null && MessageDigest.isEqual(userCodigo.toString().getBytes(), codigo.toString().getBytes());
    }

    public Usuario trocarSenha(String novaSenha, String emailUser) {

        Usuario user = findByEmail(emailUser);
        user.setPassword(passwordEncoder.encode(novaSenha));
        
        user.setPasswordCode(null);


        return repository.save(user);
    }
}
