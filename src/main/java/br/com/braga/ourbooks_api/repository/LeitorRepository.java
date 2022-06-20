package br.com.braga.ourbooks_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.braga.ourbooks_api.model.Leitor;

@Repository
public interface LeitorRepository extends JpaRepository<Leitor, Long> {

	Optional<Leitor> findByUsuarioId(Long idUsuario);

	Optional<Leitor> findByUsuarioEmail(String username);

	@Query(value = "select l from Leitor l join fetch l.disponiveis d where d.id = :idLivro")
	List<Leitor> findByDisponivelId(Long idLivro);

}
