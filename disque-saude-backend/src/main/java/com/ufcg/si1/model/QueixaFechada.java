package com.ufcg.si1.model;

import exceptions.ObjetoInvalidoException;

public class QueixaFechada implements SituacaoQueixa{

	@Override
	public SituacaoQueixa abrir() throws ObjetoInvalidoException {
		return new QueixaAberta();
		
	}

	@Override
	public SituacaoQueixa fechar() throws ObjetoInvalidoException {
		throw new ObjetoInvalidoException("Status inválido");
		
	}

}
