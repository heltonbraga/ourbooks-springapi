package br.com.braga.ourbooks_api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.braga.ourbooks_api.model.Leitor;
import br.com.braga.ourbooks_api.model.Local;
import br.com.braga.ourbooks_api.model.Usuario;
import br.com.braga.ourbooks_api.model.View;
import br.com.braga.ourbooks_api.repository.LeitorRepository;
import br.com.braga.ourbooks_api.repository.LocalRepository;

@RestController
@RequestMapping("/local")
public class LocalController {

	@Autowired
	private LocalRepository localRepository;

	@Autowired
	private LeitorRepository leitorRepository;

	private Usuario getUsuarioLogado() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (Usuario) auth.getPrincipal();
	}

	@GetMapping
	@JsonView(View.LocalLista.class)
	public List<Local> listar() {
		Usuario usuario = getUsuarioLogado();
		List<Local> all = localRepository.findByAtivoAndLeitorUsuarioId(true, usuario.getId());
		return all;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Local> detalhar(@PathVariable Long id) {
		Optional<Local> optional = localRepository.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Local> criar(@RequestBody @Valid Local local) {
		Optional<Leitor> leitor = leitorRepository.findByUsuarioId(getUsuarioLogado().getId());
		if (leitor.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		local.setAtivo(true);
		local.setLeitor(leitor.get());
		localRepository.save(local);
		return ResponseEntity.ok(local);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		Optional<Local> optional = localRepository.findById(id);
		if (optional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Usuario usuario = getUsuarioLogado();
		Local local = optional.get();
		if(local.getLeitor().getUsuario().getId().longValue() != usuario.getId().longValue()) {
			return ResponseEntity.badRequest().body(null);
		}
		local.setAtivo(false);
		localRepository.save(local);
		return ResponseEntity.ok().build();
	}

}
