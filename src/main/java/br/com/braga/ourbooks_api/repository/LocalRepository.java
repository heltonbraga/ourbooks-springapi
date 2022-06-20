package br.com.braga.ourbooks_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.braga.ourbooks_api.model.Local;

@Repository
@Transactional
public interface LocalRepository extends JpaRepository<Local, Long> {

	List<Local> findByLeitorUsuarioId(Long id);

	List<Local> findByAtivoAndLeitorUsuarioId(boolean b, Long id);
	
	@Query(value = "select distinct loc.* from (select lo.* from leitores origem "
			+ "inner join livros_disponiveis disp on disp.leitor_id = origem.id and disp.livro_id = ?2 "
			+ "inner join locais lo on lo.leitor_id = origem.id "
			+ "inner join leitores destino on destino.id = ?1 "
			+ "inner join locais ld on destino.id = ld.leitor_id "
			+ "order by ACOS( SIN( CAST(lo.latitude as numeric) )*SIN( CAST(ld.latitude as numeric)) + COS( CAST(lo.latitude as numeric))*COS( CAST(ld.latitude as numeric))*COS( CAST(ld.longitude as numeric)-CAST(lo.longitude as numeric)) ) "
			+ ") loc", nativeQuery = true)
	List<Local> listarParaMovimento(Long idLeitorDestino, Long idLivro);

}
