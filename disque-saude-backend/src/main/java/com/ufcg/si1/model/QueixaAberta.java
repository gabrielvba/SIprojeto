package com.ufcg.si1.model;

import exceptions.ObjetoInvalidoException;

public class QueixaAberta implements SituacaoQueixa{

	@Override
	public SituacaoQueixa abrir() throws ObjetoInvalidoException {
		return new QueixaAberta();
		
	}

	@Override
	public SituacaoQueixa fechar() throws ObjetoInvalidoException {
		return new QueixaFechada();
		
	}

}
