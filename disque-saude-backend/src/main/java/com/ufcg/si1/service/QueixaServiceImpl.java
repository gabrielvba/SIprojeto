package com.ufcg.si1.service;

import com.ufcg.si1.model.Queixa;
import com.ufcg.si1.util.CustomErrorType;

import exceptions.ObjetoInvalidoException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@Service("queixaService")
public class QueixaServiceImpl implements QueixaService {

    private static final AtomicLong counter = new AtomicLong();

    private static List<Queixa> queixas;

    static {
        queixas = populateDummyQueixas();
    }

    private static List<Queixa> populateDummyQueixas() {
        List<Queixa> queixas = new ArrayList<Queixa>();

        queixas.add(new Queixa(counter.incrementAndGet(), "Passei mal com uma coxinha",
                Queixa.FECHADA, "", "Jose Silva",
                "jose@gmail.com", "rua dos tolos", "PE", "Recife"));


        queixas.add(new Queixa(counter.incrementAndGet(),
                "Bacalhau estragado, passamos mal!", Queixa.FECHADA, "",
                "Ailton Sousa", "ailton@gmail.com", "rua dos bobos", "PB",
                "Joao Pessoa"));

        queixas.add(new Queixa(counter.incrementAndGet(), "Nossa rua estah muito suja", Queixa.FECHADA, "",
                "Jose Silva", "jose@gmail.com", "rua dos tolos", "PE", "Recife"));


        queixas.add(new Queixa(counter.incrementAndGet(), "iluminacao horrivel, muitos assaltos", Queixa.FECHADA, "",
                "Ailton Sousa", "ailton@gmail.com", "rua dos bobos", "PB",
                "Joao Pessoa"));

        return queixas;
    }

    //mexi mas ta errado, fazer pra listar as queixas
    public List<Queixa> findAllQueixas() throws ObjetoInvalidoException {
    	
		if (queixas.isEmpty()) {
			throw new ObjetoInvalidoException("Vazio"); 
			
		}	return queixas;
    }
    


    public Queixa salvarQueixa(Queixa queixa) throws ObjetoInvalidoException {
    	if (queixa == null) {
			throw new ObjetoInvalidoException("Impossivel");
		}else{

	        queixa.setId(counter.incrementAndGet());
	        queixas.add(queixa);
	        return queixa;
		}
    }

    public void updateQueixa(Queixa queixa) {
        int index = queixas.indexOf(queixa);
        queixas.set(index, queixa);
    }

    public void deleteQueixaById(long id) throws ObjetoInvalidoException {
    	
    	Queixa queixaExcluida = getQueixaId(id);
    	
		if (queixaExcluida == null) {
			throw new ObjetoInvalidoException("Unable to delete. Queixa with id " + id + " not found.");
		}else{
			deleteQueixaById(id);
		}
		
		
    }

    @Override
    //este metodo nunca eh chamado, mas se precisar estah aqui
    public int size() {
        return queixas.size();
    }

    @Override
    public Iterator<Queixa> getIterator() {
        return queixas.iterator();
    }

    public void deleteAllUsers() {
        queixas.clear();
    }

    public Queixa getQueixaId(long id) {
        for (Queixa queixa : queixas) {
            if (queixa.getId() == id) {
                return queixa;
            }
        }
        return null;
    }



}
