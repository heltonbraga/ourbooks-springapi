package br.com.braga.ourbooks_api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.braga.ourbooks_api.config.security.AutenticacaoService;
import br.com.braga.ourbooks_api.model.Usuario;

@RestController
@RequestMapping("/signup")
public class UsuarioController {

	@Autowired
	private AutenticacaoService service;

	@PostMapping
	public ResponseEntity<String> signup(@Valid @RequestBody Usuario usuario, UriComponentsBuilder uriBuilder) {
		try {
			service.signup(usuario);
			return ResponseEntity.ok(uriBuilder.path("/leitor").toString());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

}
