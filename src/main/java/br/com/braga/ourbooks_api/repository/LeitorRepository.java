package br.com.braga.ourbooks_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.braga.ourbooks_api.model.Leitor;

@Repository
public interface LeitorRepository extends JpaRepository<Leitor, Long> {

	Optional<Leitor> findByUsuarioId(Long idUsuario);

	Optional<Leitor> findByUsuarioEmail(String username);

}
