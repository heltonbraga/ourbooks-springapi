package br.com.braga.ourbooks_api.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import br.com.braga.ourbooks_api.model.Local;
import br.com.braga.ourbooks_api.model.Usuario;
import br.com.braga.ourbooks_api.model.View;
import br.com.braga.ourbooks_api.repository.LeitorRepository;
import br.com.braga.ourbooks_api.repository.LocalRepository;
import br.com.braga.ourbooks_api.service.LeitorService;

@RestController
@RequestMapping("/leitor")
public class LeitorController {

	@Autowired
	private LeitorRepository leitorRepository;
	
	@Autowired
	private LocalRepository localRepository;

	@Autowired
	private LeitorService service;

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
		Usuario usuario = getUsuarioLogado();
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

	@PostMapping("/quer/{id}")
	public ResponseEntity<?> incluirLivroDesejado(@PathVariable Long id) {
		Usuario usuario = getUsuarioLogado();
		try {
			Leitor leitor = service.incluirLivroDesejado(usuario.getId(), id);
			return ResponseEntity.ok(leitor.getDesejados());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/tem/{id}")
	public ResponseEntity<?> incluirLivroDisponivel(@PathVariable Long id) {
		Usuario usuario = getUsuarioLogado();
		try {
			Leitor leitor = service.incluirLivroDisponivel(usuario.getId(), id);
			return ResponseEntity.ok(leitor.getDisponiveis());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/tem/{id}")
	@JsonView(View.LeitorProposta.class)	
	public ResponseEntity<?> listarPorLivroDisponivel(@PathVariable Long id) {
		Usuario usuario = getUsuarioLogado();
		Optional<Leitor> leitor = leitorRepository.findByUsuarioId(usuario.getId());
		if (leitor.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		List<Local> locais = localRepository.listarParaMovimento(leitor.get().getId(), id);
		List<Leitor> leitores = locais.stream().map(local -> {
			Leitor l = local.getLeitor();
			l.setLocais(Arrays.asList(local));
			return l;
		}).collect(Collectors.toList());
		return ResponseEntity.ok(leitores);
	}

	private Usuario getUsuarioLogado() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (Usuario) auth.getPrincipal();
	}

}
