package br.com.braga.ourbooks_api.model;

public class View {
	
	public interface UsuarioLogin {}
	public interface UsuarioDetalhe {}
	
	public interface LeitorLista {}
	public interface LeitorDetalhe extends LeitorLista {}
	public interface LeitorProposta extends LeitorLista {}
	
	public interface LocalLista {}
	public interface LocalDetalhe extends LocalLista {}

}
