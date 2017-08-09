package com.ufcg.si1.model;

import exceptions.ObjetoInvalidoException;
import org.springframework.http.ResponseEntity;

public class Queixa {

	private long id;

	private String descricao;

	private Pessoa solicitante;

	public SituacaoQueixa situacao;

	private String comentario = ""; // usado na atualizacao da queixa

	public Queixa(){
		id=0;
	}

	public Queixa(long id, String descricao, SituacaoQueixa situacao, String comentario,
                  String nome, String email,
				  String rua, String uf, String cidade) {
		this.id = id;
		this.descricao = descricao;
		this.situacao = situacao;
		this.comentario = comentario;
		this.solicitante = new Pessoa(nome, email, rua, uf, cidade);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public SituacaoQueixa getSituacao() {
		return situacao;
	}

	public void abrir() throws ObjetoInvalidoException {
		try {
			this.situacao = situacao.abrir();
		} catch (Exception e) {
			throw new ObjetoInvalidoException("Status inválido");
		}
	}

	public void fechar(String coment) throws ObjetoInvalidoException {
		try {
			this.situacao = situacao.fechar();
			this.comentario = coment;	
		} catch (Exception e) {
			throw new ObjetoInvalidoException("Status inválido");
		}
		
}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Pessoa getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Pessoa solicitante) {
		this.solicitante = solicitante;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Queixa other = (Queixa) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
