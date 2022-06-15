package br.com.braga.ourbooks_api.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "leitores")
public class Leitor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(View.LeitorLista.class)
	private Long id;
	
	@Column(updatable = false)
	@JsonView(View.LeitorLista.class)
	private LocalDateTime createdAt;
	
	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, optional = false)
	@JsonView(View.LeitorDetalhe.class)
	private Usuario usuario;
	
	@ManyToMany
	@JoinTable(
	  name = "livros_disponiveis", 
	  joinColumns = @JoinColumn(name = "leitor_id"), 
	  inverseJoinColumns = @JoinColumn(name = "livro_id"))
	@JsonView(View.LeitorDetalhe.class)
	private List<Livro> disponiveis;
	
	@ManyToMany
	@JoinTable(
	  name = "livros_desejados", 
	  joinColumns = @JoinColumn(name = "leitor_id"), 
	  inverseJoinColumns = @JoinColumn(name = "livro_id"))
	@JsonView(View.LeitorDetalhe.class)
	private List<Livro> desejados;
	
	public Leitor() {
		//
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<Livro> getDisponiveis() {
		return disponiveis;
	}

	public void setDisponiveis(List<Livro> disponiveis) {
		this.disponiveis = disponiveis;
	}

	public List<Livro> getDesejados() {
		return desejados;
	}

	public void setDesejados(List<Livro> desejados) {
		this.desejados = desejados;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
