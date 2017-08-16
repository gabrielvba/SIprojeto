package com.ufcg.si1.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import com.ufcg.si1.model.Especialidade;
import com.ufcg.si1.model.UnidadeSaude;
import com.ufcg.si1.service.EspecialidadeService;
import com.ufcg.si1.service.EspecialidadeServiceImpl;
import com.ufcg.si1.service.UnidadeSaudeService;
import com.ufcg.si1.service.UnidadeSaudeServiceImpl;
import com.ufcg.si1.util.CustomErrorType;

import exceptions.ObjetoInexistenteException;
import exceptions.ObjetoJaExistenteException;
import exceptions.Rep;

public class EspecialidadeController {

	 EspecialidadeService especialidadeService = new EspecialidadeServiceImpl();
	 UnidadeSaudeService unidadeSaudeService = new UnidadeSaudeServiceImpl();
	 
	//Especialidade

	  @RequestMapping(value = "/especialidade/unidades", method = RequestMethod.GET)
	    public ResponseEntity<?> consultaEspecialidadeporUnidadeSaude(@RequestBody int codigoUnidadeSaude) {

	        Object us = null;
	        try {
	            us = unidadeSaudeService.procura(codigoUnidadeSaude);
	        } catch (Rep e) {
	            return new ResponseEntity<List>(HttpStatus.NOT_FOUND);
	        } catch (ObjetoInexistenteException e) {
	            return new ResponseEntity<List>(HttpStatus.NOT_FOUND);
	        }
	        if (us instanceof UnidadeSaude){
	            UnidadeSaude us1 = (UnidadeSaude) us;
	            return new ResponseEntity<>(us1.getEspecialidades(), HttpStatus.OK);
	        }

	        return new ResponseEntity<List>(HttpStatus.NOT_FOUND);
	    }

	    @RequestMapping(value = "/especialidade/", method = RequestMethod.POST)
	    public ResponseEntity<String> incluirEspecialidade(@RequestBody Especialidade esp, UriComponentsBuilder ucBuilder) {
	        try {
	            especialidadeService.insere(esp);
	        } catch (Rep e) {
	            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	        } catch (ObjetoJaExistenteException e) {
	            return new ResponseEntity<String>(HttpStatus.CONFLICT);
	        }

	        HttpHeaders headers = new HttpHeaders();
	        headers.setLocation(ucBuilder.path("/api/especialidade/{id}").buildAndExpand(esp.getCodigo()).toUri());
	        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	    }

	    @RequestMapping(value = "/especialidade/{id}", method = RequestMethod.GET)
	    public ResponseEntity<?> consultarEspecialidade(@PathVariable("id") long id) {

	        Especialidade q = especialidadeService.findById(id);
	        if (q == null) {
	            return new ResponseEntity(new CustomErrorType("Especialidade with id " + id
	                    + " not found"), HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<Especialidade>(q, HttpStatus.OK);
	    }

}
