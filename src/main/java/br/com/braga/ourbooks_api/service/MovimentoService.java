package br.com.braga.ourbooks_api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.braga.ourbooks_api.model.Leitor;
import br.com.braga.ourbooks_api.model.Livro;
import br.com.braga.ourbooks_api.model.Local;
import br.com.braga.ourbooks_api.model.Movimento;
import br.com.braga.ourbooks_api.model.Situacao;
import br.com.braga.ourbooks_api.repository.LeitorRepository;
import br.com.braga.ourbooks_api.repository.LivroRepository;
import br.com.braga.ourbooks_api.repository.LocalRepository;
import br.com.braga.ourbooks_api.repository.MovimentoRepository;

@Service
public class MovimentoService {

	@Autowired
	private LeitorRepository leitorRepository;

	@Autowired
	private LivroRepository livroRepository;

	@Autowired
	private LocalRepository localRepository;

	@Autowired
	private MovimentoRepository movimentoRepository;

	private final Map<Situacao, List<Situacao>> evolucoesPermitidas = getEvolucoesPermitidas();

	public List<Movimento> listarPorUsuarioOrigem(Long idUsuario) throws Exception {
		Leitor logado = getLeitorByIdUsuario(idUsuario);
		List<Movimento> movimentos = movimentoRepository.findByOrigem(logado);
		return movimentos;
	}

	public List<Movimento> listarPorUsuarioDestino(Long idUsuario) throws Exception {
		Leitor logado = getLeitorByIdUsuario(idUsuario);
		List<Movimento> movimentos = movimentoRepository.findByDestino(logado);
		return movimentos;
	}

	public Movimento criar(Movimento movimento, Long idUsuario) throws Exception {
		Leitor logado = getLeitorByIdUsuario(idUsuario);
		Leitor origem = getLeitorById(movimento.getOrigem().getId());
		Leitor destino = getLeitorById(movimento.getDestino().getId());
		Optional<Livro> oLivro = livroRepository.findById(movimento.getLivro().getId());
		if (oLivro.isEmpty()) {
			throw new Exception("Livro inválido.");
		}
		Livro livro = oLivro.get();
		Optional<Local> oLocal = localRepository.findById(movimento.getLocal().getId());
		if (oLocal.isEmpty()) {
			throw new Exception("Local inválido.");
		}
		Local local = oLocal.get();

		if (origem.getId().longValue() == destino.getId().longValue()) {
			throw new Exception("Origem e destino do movimento não podem ser o mesmo leitor.");
		}
		if (logado.getId().longValue() != origem.getId().longValue()
				&& logado.getId().longValue() != destino.getId().longValue()) {
			throw new Exception("Usuário deve ser origem ou destino do movimento.");
		}
		if (!origem.getDisponiveis().contains(livro) || !destino.getDesejados().contains(livro)) {
			throw new Exception("Livro movimentado não encontrado na lista de desejados / disponíveis.");
		}
		if (!origem.getLocais().contains(local)) {
			throw new Exception("Local não encontrado na origem.");
		}

		Movimento novo = new Movimento();
		novo.setOrigem(origem);
		novo.setDestino(destino);
		novo.setLivro(livro);
		novo.setSituacao(Situacao.PROPOSTA);
		novo.setCreatedAt(LocalDateTime.now());
		novo.setUpdatedAt(novo.getCreatedAt());
		movimentoRepository.save(novo);
		return novo;
	}

	public Movimento evoluir(Long idMovimento, Situacao situacao, Long idUsuario) throws Exception {
		Optional<Movimento> oMovimento = movimentoRepository.findById(idMovimento);
		if (oMovimento.isEmpty()) {
			throw new Exception("Movimento não encontrado.");
		}
		Movimento movimento = oMovimento.get();
		Leitor logado = getLeitorByIdUsuario(idUsuario);
		if (logado.getId().longValue() != movimento.getOrigem().getId().longValue()
				&& logado.getId().longValue() != movimento.getDestino().getId().longValue()) {
			throw new Exception("Usuário deve ser origem ou destino do movimento.");
		}
		if ((Situacao.ACEITA.equals(situacao) || Situacao.RECUSADA.equals(situacao))
				&& !logado.equals(movimento.getOrigem())) {
			throw new Exception("Aceitar ou recusar uma proposta é exclusividade da origem do movimento");
		}
		validarEvolucao(movimento.getSituacao(), situacao);
		return movimento;
	}

	private void validarEvolucao(Situacao atual, Situacao nova) throws Exception {
		if (!evolucoesPermitidas.get(nova).contains(atual)) {
			throw new Exception("Situação não permitida com base na situação atual.");
		}
	}

	private Leitor getLeitorByIdUsuario(Long idUsuario) throws Exception {
		Optional<Leitor> leitor = leitorRepository.findByUsuarioId(idUsuario);
		if (leitor.isEmpty()) {
			throw new Exception("Usuário não é um leitor.");
		}
		return leitor.get();
	}

	private Leitor getLeitorById(Long idLeitor) throws Exception {
		Optional<Leitor> leitor = leitorRepository.findById(idLeitor);
		if (leitor.isEmpty()) {
			throw new Exception("Leitor não encontrado.");
		}
		return leitor.get();
	}

	private static Map<Situacao, List<Situacao>> getEvolucoesPermitidas() {
		Map<Situacao, List<Situacao>> validos = new HashMap<Situacao, List<Situacao>>();
		validos.put(Situacao.PROPOSTA, new ArrayList<Situacao>());
		validos.put(Situacao.ACEITA, new ArrayList<Situacao>());
		validos.put(Situacao.RECUSADA, new ArrayList<Situacao>());
		validos.put(Situacao.FINALIZADA, new ArrayList<Situacao>());
		validos.put(Situacao.CANCELADA, new ArrayList<Situacao>());
		validos.get(Situacao.ACEITA).add(Situacao.PROPOSTA);
		validos.get(Situacao.RECUSADA).add(Situacao.PROPOSTA);
		validos.get(Situacao.FINALIZADA).add(Situacao.ACEITA);
		validos.get(Situacao.CANCELADA).addAll(Arrays.asList(Situacao.values()));
		return validos;
	}

}
