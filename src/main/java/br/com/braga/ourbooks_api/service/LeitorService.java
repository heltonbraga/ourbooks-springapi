package br.com.braga.ourbooks_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.braga.ourbooks_api.model.Leitor;
import br.com.braga.ourbooks_api.model.Livro;
import br.com.braga.ourbooks_api.repository.LeitorRepository;
import br.com.braga.ourbooks_api.repository.LivroRepository;

@Service
public class LeitorService {

	@Autowired
	private LeitorRepository leitorRepository;

	@Autowired
	private LivroRepository livroRepository;

	public Leitor incluirLivroDesejado(Long idUsuario, Long idLivro) throws Exception {
		return incluirLivro(idUsuario, idLivro, true);
	}

	public Leitor incluirLivroDisponivel(Long idUsuario, Long idLivro) throws Exception {
		return incluirLivro(idUsuario, idLivro, false);
	}

	private Leitor incluirLivro(Long idUsuario, Long idLivro, boolean desejado) throws Exception {
		Optional<Leitor> oLeitor = leitorRepository.findByUsuarioId(idUsuario);
		if (oLeitor.isEmpty()) {
			throw new Exception("Usuário não é um leitor.");
		}
		Optional<Livro> oLivro = livroRepository.findById(idLivro);
		if (oLivro.isEmpty()) {
			throw new Exception("Livro não encontrado.");
		}
		Livro livro = oLivro.get();
		Leitor leitor = oLeitor.get();
		if ((desejado && leitor.getDesejados().contains(livro))
				|| (!desejado && leitor.getDisponiveis().contains(livro))) {
			throw new Exception("Livro já está na lista.");
		}
		if (desejado) {
			leitor.getDesejados().add(livro);
		} else {
			leitor.getDisponiveis().add(livro);
		}
		leitorRepository.save(leitor);
		return leitor;
	}

}
