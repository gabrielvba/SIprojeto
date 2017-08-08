package com.ufcg.si1.service;


import java.util.Iterator;
import java.util.List;

import com.ufcg.si1.model.Queixa;

import exceptions.ObjetoInvalidoException;
import exceptions.deleteQueixaByIdException;
import exceptions.findByIdException;

public interface QueixaService {

	List<Queixa> findAllQueixas() ;


    void saveQueixa(Queixa queixa) throws ObjetoInvalidoException;

	Queixa findById(long id) throws findByIdException;

	void updateQueixa(Queixa user);


	void deleteQueixaById(long id) throws deleteQueixaByIdException;

    int size();

	Iterator<Queixa> getIterator();


//	boolean isUserExist(Queixa user);
	
}
