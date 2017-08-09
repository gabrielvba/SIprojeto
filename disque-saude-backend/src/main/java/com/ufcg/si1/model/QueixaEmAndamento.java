package com.ufcg.si1.model;

import exceptions.ObjetoInvalidoException;

public class QueixaEmAndamento implements SituacaoQueixa{

	@Override
	public SituacaoQueixa abrir() throws ObjetoInvalidoException {
		throw new ObjetoInvalidoException("Status inválido");
	}

	@Override
	public SituacaoQueixa fechar() throws ObjetoInvalidoException {
		 return new QueixaFechada();
	}

}
