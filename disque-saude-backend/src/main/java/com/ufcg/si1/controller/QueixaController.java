package com.ufcg.si1.controller;

import br.edu.ufcg.Hospital;
import com.ufcg.si1.model.*;
import com.ufcg.si1.service.*;
import com.ufcg.si1.util.CustomErrorType;
import com.ufcg.si1.util.ObjWrapper;
import exceptions.ObjetoInexistenteException;
import exceptions.ObjetoInvalidoException;
import exceptions.ObjetoJaExistenteException;
import exceptions.Rep;
import exceptions.findByIdException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin

public class QueixaController {

	QueixaService queixaService = new QueixaServiceImpl();

    /* situação normal =0
       situação extra =1
     */
    private int situacaoAtualPrefeitura = 0;


	// -------------------Retrieve All
	// Complaints---------------------------------------------

	@RequestMapping(value = "/queixa/", method = RequestMethod.GET)
	public ResponseEntity<List<Queixa>> listQueixas() {
		List<Queixa> queixas = queixaService.findAllQueixas();

		if (queixas.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Queixa>>(queixas, HttpStatus.OK);
	}

	// -------------------Abrir uma
	// Queixa-------------------------------------------

	@RequestMapping(value = "/queixa/", method = RequestMethod.POST)
	public ResponseEntity<?> abrirQueixa(@RequestBody Queixa queixa) {

		try {
			queixaService.saveQueixa(queixa);
			return new ResponseEntity<Queixa>(queixa, HttpStatus.CREATED);
		} catch (ObjetoInvalidoException e) {
			return new ResponseEntity<List>(HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/queixa/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> consultarQueixa(@PathVariable("id") long id) {

		try {
			Queixa q = queixaService.findById(id);
			return new ResponseEntity<Queixa>(q, HttpStatus.OK);
			
		} catch (findByIdException e) {
			
			return new ResponseEntity<List>(HttpStatus.NOT_FOUND);//aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
		}
		
		

	}

	@RequestMapping(value = "/queixa/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateQueixa(@PathVariable("id") long id, @RequestBody Queixa queixa) throws findByIdException {

		try{

			Queixa currentQueixa = queixaService.findById(id);

			currentQueixa.setDescricao(queixa.getDescricao());
			currentQueixa.setComentario(queixa.getComentario());

			queixaService.updateQueixa(currentQueixa);
			
			return new ResponseEntity<Queixa>(currentQueixa, HttpStatus.OK);

		}catch (findByIdException e) {
			
			return new ResponseEntity<List>(HttpStatus.NOT_FOUND);//aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
			/**
			 * 		if (currentQueixa == null) {
				return new ResponseEntity(new CustomErrorType("Unable to upate. Queixa with id " + id + " not found."),
						HttpStatus.NOT_FOUND);
			}
			 */
		}
		
	}

	@RequestMapping(value = "/queixa/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id)  {
		
		try{
			queixaService.deleteQueixaById(id);
			return new ResponseEntity<Queixa>(HttpStatus.NO_CONTENT);
			
		}catch (Exception e) {
			
			return new ResponseEntity<List>(HttpStatus.NOT_FOUND);//aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
			
			/**
			 * 
					if (user == null) {
						return new ResponseEntity(new CustomErrorType("Unable to delete. Queixa with id " + id + " not found."),
								HttpStatus.NOT_FOUND);
					}
			 */
		}
		
	}

	@RequestMapping(value = "/queixa/fechamento", method = RequestMethod.POST)
	public ResponseEntity<?> fecharQueixa(@RequestBody Queixa queixaAFechar) {
		queixaAFechar.situacao = new QueixaFechada();
		queixaService.updateQueixa(queixaAFechar);
		return new ResponseEntity<Queixa>(queixaAFechar, HttpStatus.OK);
	}
	
	 
	    private double numeroQueixasAbertas() {
	        int contador = 0;
	        Iterator<Queixa> it = queixaService.getIterator();
	        for (Iterator<Queixa> it1 = it; it1.hasNext(); ) {
	            Queixa q = it1.next();
	            if (q.getSituacao() instanceof QueixaAberta)
	                contador++;
	        }

	        return contador;
	    }
	    
	    @RequestMapping(value = "/geral/situacao", method = RequestMethod.GET)
	    public ResponseEntity<?> getSituacaoGeralQueixas() {

	        // dependendo da situacao da prefeitura, o criterio de avaliacao muda
	        // se normal, mais de 20% abertas eh ruim, mais de 10 eh regular
	        // se extra, mais de 10% abertas eh ruim, mais de 5% eh regular
	        if (situacaoAtualPrefeitura == 0) {
	            if ((double) numeroQueixasAbertas() / queixaService.size() > 0.2) {
	                return new ResponseEntity<ObjWrapper<Integer>>(new ObjWrapper<Integer>(0), HttpStatus.OK);
	            } else {
	                if ((double) numeroQueixasAbertas() / queixaService.size() > 0.1) {
	                    return new ResponseEntity<ObjWrapper<Integer>>(new ObjWrapper<Integer>(1), HttpStatus.OK);
	                }
	            }
	        }
	        if (this.situacaoAtualPrefeitura == 1) {
	            if ((double) numeroQueixasAbertas() / queixaService.size() > 0.1) {
	                return new ResponseEntity<ObjWrapper<Integer>>(new ObjWrapper<Integer>(0), HttpStatus.OK);
	            } else {
	                if ((double) numeroQueixasAbertas() / queixaService.size() > 0.05) {
	                    return new ResponseEntity<ObjWrapper<Integer>>(new ObjWrapper<Integer>(1), HttpStatus.OK);
	                }
	            }
	        }

	        //situacao retornada
	        //0: RUIM
	        //1: REGULAR
	        //2: BOM
	        return new ResponseEntity<ObjWrapper<Integer>>(new ObjWrapper<Integer>(2), HttpStatus.OK);
	    }
	    

}
