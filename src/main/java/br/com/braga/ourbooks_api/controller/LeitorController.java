package br.com.braga.ourbooks_api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.braga.ourbooks_api.model.Leitor;
import br.com.braga.ourbooks_api.model.Livro;
import br.com.braga.ourbooks_api.model.Usuario;
import br.com.braga.ourbooks_api.model.View;
import br.com.braga.ourbooks_api.repository.LeitorRepository;
import br.com.braga.ourbooks_api.repository.LivroRepository;

@RestController
@RequestMapping("/leitor")
public class LeitorController {

	@Autowired
	private LeitorRepository leitorRepository;

	@Autowired
	private LivroRepository livroRepository;

	@GetMapping
	@JsonView(View.LeitorLista.class)
	public List<Leitor> listar(
			@PageableDefault(sort = "createdAt", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) {
		Page<Leitor> all = leitorRepository.findAll(paginacao);
		return all.getContent();
	}

	@GetMapping("/{id}")
	@JsonView(View.LeitorDetalhe.class)
	public ResponseEntity<Leitor> detalhar(@PathVariable Long id) {
		Optional<Leitor> optional = leitorRepository.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@JsonView(View.LeitorDetalhe.class)
	public ResponseEntity<Leitor> criar() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = (Usuario) auth.getPrincipal();
		Optional<Leitor> outro = leitorRepository.findByUsuarioId(usuario.getId());
		if (outro.isPresent()) {
			return ResponseEntity.badRequest().body(null);
		}
		Leitor leitor = new Leitor();
		leitor.setCreatedAt(LocalDateTime.now());
		leitor.setUsuario(usuario);
		leitorRepository.save(leitor);
		return ResponseEntity.ok(leitor);
	}

	@PostMapping("/quer")
	public ResponseEntity<?> incluirLivroDesejado(@PathVariable Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = (Usuario) auth.getPrincipal();
		Optional<Leitor> leitor = leitorRepository.findByUsuarioId(usuario.getId());
		if (leitor.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Optional<Livro> livro = livroRepository.findById(id);
		if (livro.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Leitor oleitor = leitor.get();
		if(oleitor.getDesejados().contains(livro.get())) {
			return ResponseEntity.badRequest().body(null);
		}
		oleitor.getDesejados().add(livro.get());
		leitorRepository.save(oleitor);
		return ResponseEntity.ok(oleitor);
	}

}
