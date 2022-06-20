package br.com.braga.ourbooks_api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.braga.ourbooks_api.model.Movimento;
import br.com.braga.ourbooks_api.model.Situacao;
import br.com.braga.ourbooks_api.model.Usuario;
import br.com.braga.ourbooks_api.repository.MovimentoRepository;
import br.com.braga.ourbooks_api.service.MovimentoService;

@RestController
@RequestMapping("/movimento")
public class MovimentoController {

	@Autowired
	private MovimentoRepository movimentoRepository;

	@Autowired
	private MovimentoService service;

	@GetMapping
	public List<Movimento> listar(
			@PageableDefault(sort = "createdAt", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) {
		Page<Movimento> all = movimentoRepository.findAll(paginacao);
		return all.getContent();
	}

	@GetMapping("/origem")
	public ResponseEntity<?> listarPorUsuarioOrigem() {
		Usuario usuario = getUsuarioLogado();
		try {
			List<Movimento> propostas = service.listarPorUsuarioOrigem(usuario.getId());
			return ResponseEntity.ok(propostas);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/destino")
	public ResponseEntity<?> listarPorUsuarioDestino() {
		Usuario usuario = getUsuarioLogado();
		try {
			List<Movimento> propostas = service.listarPorUsuarioDestino(usuario.getId());
			return ResponseEntity.ok(propostas);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Movimento> detalhar(@PathVariable Long id) {
		Optional<Movimento> optional = movimentoRepository.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<?> criar(@RequestBody @Valid Movimento movimento) {
		Usuario usuario = getUsuarioLogado();
		try {
			Movimento novo = service.criar(movimento, usuario.getId());
			return ResponseEntity.ok(novo);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}/{situacao}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @PathVariable Situacao situacao) {
		Usuario usuario = getUsuarioLogado();
		try {
			Movimento atual = service.evoluir(id, situacao, usuario.getId());
			return ResponseEntity.ok(atual);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	private Usuario getUsuarioLogado() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (Usuario) auth.getPrincipal();
	}

}
