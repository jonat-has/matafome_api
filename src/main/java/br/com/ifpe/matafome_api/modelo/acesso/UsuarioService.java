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


import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

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
        return repository.save(user);
    }
}
