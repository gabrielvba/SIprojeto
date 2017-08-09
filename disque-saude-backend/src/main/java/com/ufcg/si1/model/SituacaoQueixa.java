package com.ufcg.si1.model;

import exceptions.ObjetoInvalidoException;

public interface SituacaoQueixa {
	SituacaoQueixa abrir() throws ObjetoInvalidoException;
	SituacaoQueixa fechar() throws ObjetoInvalidoException;
}
