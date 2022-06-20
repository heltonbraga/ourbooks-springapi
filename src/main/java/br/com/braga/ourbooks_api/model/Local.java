package br.com.braga.ourbooks_api.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "locais")
public class Local {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView({View.LocalLista.class, View.LeitorProposta.class})
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name="leitor_id")
	@JsonIgnore
	private Leitor leitor;

	@JsonIgnore
	private boolean ativo;
	
	@NotNull
	@JsonView(View.LocalDetalhe.class)
	private String nome;
	
	@NotNull
	@JsonView(View.LocalLista.class)
	private String pais;
	
	@NotNull
	@JsonView({View.LocalLista.class, View.LeitorProposta.class})
	private String regiao;
	
	@NotNull
	@JsonView({View.LocalDetalhe.class, View.LeitorProposta.class})
	@Pattern(regexp = "-?\\d+\\.\\d+")
	private String latitude;
	
	@NotNull
	@JsonView({View.LocalDetalhe.class, View.LeitorProposta.class})
	@Pattern(regexp = "-?\\d+\\.\\d+")
	private String longitude;

	public Local() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Leitor getLeitor() {
		return leitor;
	}

	public void setLeitor(Leitor leitor) {
		this.leitor = leitor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

}
