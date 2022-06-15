package br.com.braga.ourbooks_api.config.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.braga.ourbooks_api.model.Usuario;
import br.com.braga.ourbooks_api.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findByEmail(username);
		if (usuario.isPresent()) {
			return usuario.get();
		}

		throw new UsernameNotFoundException("Dados inválidos!");
	}

	public Usuario signup(Usuario usuario) throws Exception {
		Optional<Usuario> jaExiste = repository.findByEmail(usuario.getEmail());
		if (jaExiste.isPresent()) {
			throw new Exception("Email vinculado a uma conta pre existente");
		}
		usuario.setSenha(getEncoder().encode(usuario.getSenha()));
		repository.save(usuario);
		return usuario;
	}

	public PasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}

}
