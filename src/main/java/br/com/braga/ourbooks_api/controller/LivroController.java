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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.braga.ourbooks_api.model.Livro;
import br.com.braga.ourbooks_api.repository.LivroRepository;

@RestController
@RequestMapping("/livro")
public class LivroController {

	@Autowired
	private LivroRepository livroRepository;

	@GetMapping
	public List<Livro> listar(
			@PageableDefault(sort = "titulo", direction = Direction.ASC, page = 0, size = 10) Pageable paginacao) {
		Page<Livro> all = livroRepository.findAll(paginacao);
		return all.getContent();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Livro> detalhar(@PathVariable Long id) {
		Optional<Livro> optional = livroRepository.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Livro> criar(@RequestBody @Valid Livro livro) {
		livroRepository.save(livro);
		return ResponseEntity.ok(livro);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Livro> editar(@PathVariable Long id, @RequestBody @Valid Livro livro) {
		Optional<Livro> optional = livroRepository.findById(id);
		if (optional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Livro editado = optional.get();
		editado.setAutor(livro.getAutor());
		editado.setGenero(livro.getGenero());
		editado.setTitulo(livro.getTitulo());
		editado.setUrlCapa(livro.getUrlCapa());
		livroRepository.save(editado);
		return ResponseEntity.ok(editado);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		Optional<Livro> optional = livroRepository.findById(id);
		if (optional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		livroRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
