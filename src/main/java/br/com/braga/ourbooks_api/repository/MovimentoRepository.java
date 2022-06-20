package br.com.braga.ourbooks_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.braga.ourbooks_api.model.Leitor;
import br.com.braga.ourbooks_api.model.Movimento;
import br.com.braga.ourbooks_api.model.Situacao;

@Repository
public interface MovimentoRepository extends JpaRepository<Movimento, Long> {

	long countBySituacao(Situacao situacao);

	List<Movimento> findByOrigem(Leitor origem);

	List<Movimento> findByDestino(Leitor destino);

}
