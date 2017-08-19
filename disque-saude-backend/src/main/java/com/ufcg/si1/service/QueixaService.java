package com.ufcg.si1.service;


import java.util.Iterator;
import java.util.List;

import com.ufcg.si1.model.Queixa;

import exceptions.ObjetoInvalidoException;

public interface QueixaService {
	
	Queixa salvarQueixa(Queixa queixa) throws ObjetoInvalidoException;
	
	Queixa getQueixaId(long id) throws ObjetoInvalidoException;
	
	void deleteQueixaById(long id) throws ObjetoInvalidoException;

	List<Queixa> findAllQueixas() throws ObjetoInvalidoException;


 


	

	void updateQueixa(Queixa user);


	

    int size();

	Iterator<Queixa> getIterator();


//	boolean isUserExist(Queixa user);
	
}
