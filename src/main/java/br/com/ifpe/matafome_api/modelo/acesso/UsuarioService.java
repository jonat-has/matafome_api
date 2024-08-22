package br.com.ifpe.matafome_api.modelo.acesso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.ifpe.matafome_api.modelo.seguranca.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private JwtService jwtService;


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

    public Integer reedemCode() {
        return (int) (Math.random() * 9000) + 1000;  
    }

    @Transactional
    public Usuario save(Usuario user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setHabilitado(Boolean.TRUE);
        user.setEmailvalidado(Boolean.FALSE);
        user.setActiveCode(reedemCode());
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

    public void encontrarDadosUser(Long idUser) {

        

    }

}
