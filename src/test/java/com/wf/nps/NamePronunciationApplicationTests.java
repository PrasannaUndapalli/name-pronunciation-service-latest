package com.wf.nps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.wf.nps.controller.NamePronunciationController;
import com.wf.nps.repo.NamePronunciation;
import com.wf.nps.repo.NamePronunciationJdbcRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NamePronunciationApplicationTests {

	    @Autowired
	    NamePronunciationController controller;
	    @MockBean
		private NamePronunciationJdbcRepository repository;
	    
	    @Test
	    public void getNamesListTest() {
	    	when(repository.findAll()).thenReturn(Stream.of(new NamePronunciation("U821340","Venkat",null,null,null), new NamePronunciation("U820340","Madan",null,null,null)).collect(Collectors.toList()));
	        assertEquals(HttpStatus.OK, controller.retrieveAllNames().getStatusCode());
	    }
	    
	    @Test
	    public void getNamesListEmptyTest() {
	    	when(repository.findAll()).thenReturn(new ArrayList<>());
	        assertEquals(HttpStatus.NO_CONTENT, controller.retrieveAllNames().getStatusCode());
	    }
	    
	    @Test
	    public void getNamesListExceptionTest() {
	    	when(repository.findAll()).thenThrow(NullPointerException.class);
	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.retrieveAllNames().getStatusCode());
	    }
	    
	    @Test
	    public void retrieveNameTest() {
	    	when(repository.findById(1L)).thenReturn(new NamePronunciation("U821340","Venkat",null,null, new byte[10]));
	    	assertEquals(HttpStatus.OK, controller.retrieveName(1L).getStatusCode());
	    }
	    
	    @Test
	    public void retrieveNameNotFoundTest() {
	    	when(repository.findById(1L)).thenReturn(null);
	    	assertEquals(HttpStatus.NOT_FOUND, controller.retrieveName(1L).getStatusCode());
	    }
	    
	    @Test
	    public void retrieveNameExceptionTest() {
	    	when(repository.findById(1L)).thenThrow(NullPointerException.class);
	    	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.retrieveName(1L).getStatusCode());
	    }
	    
	    @Test
	    public void deleteNameTest() {
	    	when(repository.deleteById(1L)).thenReturn(1);
	    	assertEquals(HttpStatus.OK, controller.deleteName(1L).getStatusCode());
	    }
	    
	    @Test
	    public void deleteNameNotFoundTest() {
	    	when(repository.deleteById(1L)).thenReturn(0);
	    	assertEquals(HttpStatus.NOT_FOUND, controller.deleteName(1L).getStatusCode());
	    }
	    
	    @Test
	    public void deleteNameExceptionTest() {
	    	when(repository.deleteById(1L)).thenThrow(NullPointerException.class);
	    	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.deleteName(1L).getStatusCode());
	    }
	    
	    @Test
	    public void createNameTest() {
	    	NamePronunciation np = new NamePronunciation();
	    	when(repository.insert(np)).thenReturn(1);
	    	assertEquals(HttpStatus.CREATED, controller.createName(np).getStatusCode());
	    }
	    
	    @Test
	    public void createNameBadRequestTest() {
	    	NamePronunciation np = new NamePronunciation();
	    	when(repository.insert(np)).thenReturn(0);
	    	assertEquals(HttpStatus.BAD_REQUEST, controller.createName(np).getStatusCode());
	    }
	    
	    @Test
	    public void createNameExceptionTest() {
	    	NamePronunciation np = new NamePronunciation();
	    	when(repository.insert(np)).thenThrow(NullPointerException.class);
	    	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.createName(np).getStatusCode());
	    }
	    
	    
	    @Test
	    public void updateNameTest() {
	    	NamePronunciation name = new NamePronunciation("U821340","Venkat",null,null, new byte[10]);
	    	NamePronunciation namePro = new NamePronunciation("U821340","Venkata",null,null, new byte[10]);
	    	when(repository.findById(1L)).thenReturn(namePro);
	    	when(repository.update(namePro)).thenReturn(1);
	    	assertEquals(HttpStatus.OK, controller.updateName(name,1L).getStatusCode());
	    }
	    
	    @Test
	    public void updateNameNotFoundTest() {
	    	NamePronunciation name = new NamePronunciation("U821340","Venkat",null,null, new byte[10]);
	    	NamePronunciation namePro = new NamePronunciation("U821340","Venkata",null,null, new byte[10]);
	    	when(repository.findById(1L)).thenReturn(null);
	    	when(repository.update(namePro)).thenReturn(0);
	    	assertEquals(HttpStatus.NOT_FOUND, controller.updateName(name,1L).getStatusCode());
	    }
	    
	    @Test
	    public void updateNameExceptionTest() {
	    	NamePronunciation name = new NamePronunciation("U821340","Venkat",null,null, new byte[10]);
	    	NamePronunciation namePro = new NamePronunciation("U821340","Venkata",null,null, new byte[10]);
	    	when(repository.findById(1L)).thenThrow(NullPointerException.class);
	    	when(repository.update(namePro)).thenReturn(1);
	    	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controller.updateName(name,1L).getStatusCode());
	    }
}
