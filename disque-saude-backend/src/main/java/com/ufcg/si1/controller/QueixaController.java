package com.ufcg.si1.controller;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ufcg.si1.model.Queixa;
import com.ufcg.si1.service.QueixaService;
import com.ufcg.si1.service.QueixaServiceImpl;
import com.ufcg.si1.util.CustomErrorType;
import com.ufcg.si1.util.ObjWrapper;

import exceptions.ObjetoInvalidoException;

@RestController
@CrossOrigin
public class QueixaController {
	
	/*
	 * situação normal =0 situação extra =1
	 */
	private int situacaoAtualPrefeitura = 0;
	
	@Autowired
	QueixaService queixaService;

	public QueixaController(QueixaService queixaService) {
		this.queixaService = queixaService;
	}

	//mexi
	@RequestMapping(value = "/queixa/", method = RequestMethod.POST)
	public ResponseEntity<Queixa> salvarQueixa(@RequestBody Queixa queixa) throws ObjetoInvalidoException {

		Queixa queixaRegistrada = queixaService.salvarQueixa(queixa);
		return new ResponseEntity<>(queixaRegistrada, HttpStatus.CREATED);

	}
	
	//mexi
	@RequestMapping(value = "/queixa/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteQUeixa(@PathVariable("id") Long id) {
		try {
			queixaService.deleteQueixaById(id);
			return new ResponseEntity<Queixa>(HttpStatus.OK);
		} catch (ObjetoInvalidoException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	
	@RequestMapping(value = "/queixa/", method = RequestMethod.GET)
	public ResponseEntity<Queixa> findAllQuaixas() {
		try{
			queixaService.findAllQueixas();
			return new ResponseEntity<Queixa>(HttpStatus.OK);
		} catch (ObjetoInvalidoException e) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
	}

	// -------------------Abrir uma Queixa-------------------------------------------

	@RequestMapping(value = "/queixa/", method = RequestMethod.POST)
	public ResponseEntity<?> abrirQueixa(@RequestBody Queixa queixa, UriComponentsBuilder ucBuilder) throws ObjetoInvalidoException {
		

		try {
			queixa.abrir();
		} catch (ObjetoInvalidoException e) {
			return new ResponseEntity<List>(HttpStatus.BAD_REQUEST);
		}
		queixaService.salvarQueixa(queixa);

		// HttpHeaders headers = new HttpHeaders();
		// headers.setLocation(ucBuilder.path("/api/queixa/{id}").buildAndExpand(queixa.getId()).toUri());

		return new ResponseEntity<Queixa>(queixa, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/queixa/{id}", method = RequestMethod.GET)
	public ResponseEntity<Queixa> consultarQueixa(@PathVariable("id") Long id) {
		try {
			Queixa queixaEncontrada = queixaService.getQueixaId(id);
			return new ResponseEntity<>(queixaEncontrada, HttpStatus.OK);
		} catch (ObjetoInvalidoException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
	@RequestMapping(value = "/queixa/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateQueixa(@PathVariable("id") long id, @RequestBody Queixa queixa) throws ObjetoInvalidoException {

		Queixa currentQueixa = queixaService.getQueixaId(id);

		if (currentQueixa == null) {
			return new ResponseEntity(new CustomErrorType("Unable to upate. Queixa with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentQueixa.setDescricao(queixa.getDescricao());
		currentQueixa.setComentario(queixa.getComentario());

		queixaService.updateQueixa(currentQueixa);
		return new ResponseEntity<Queixa>(currentQueixa, HttpStatus.OK);
	}
	

	

	@RequestMapping(value = "/queixa/fechamento", method = RequestMethod.POST)
	public ResponseEntity<?> fecharQueixa(@RequestBody Queixa queixaAFechar) {
		queixaAFechar.situacao = Queixa.FECHADA;
		queixaService.updateQueixa(queixaAFechar);
		return new ResponseEntity<Queixa>(queixaAFechar, HttpStatus.OK);
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

		// situacao retornada
		// 0: RUIM
		// 1: REGULAR
		// 2: BOM
		return new ResponseEntity<ObjWrapper<Integer>>(new ObjWrapper<Integer>(2), HttpStatus.OK);
	}

	private double numeroQueixasAbertas() {
		int contador = 0;
		Iterator<Queixa> it = queixaService.getIterator();
		for (Iterator<Queixa> it1 = it; it1.hasNext();) {
			Queixa q = it1.next();
			if (q.getSituacao() == Queixa.ABERTA)
				contador++;
		}

		return contador;
	}

}
