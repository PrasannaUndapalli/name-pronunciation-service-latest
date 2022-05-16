package com.wf.nps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import com.wf.nps.repo.NamePronunciation;
import com.wf.nps.repo.NamePronunciationJdbcRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NamePronunciationJdbcRepositoryTests {

	@Autowired
	NamePronunciationJdbcRepository repository;
	@MockBean
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void findAllTest() {
		when(jdbcTemplate.query(anyString(), ArgumentMatchers.any(RowMapper.class))).thenReturn(new ArrayList<>());
		assertEquals(0, repository.findAll().size());
	}
	
	
	  @Test public void findByIdTest() { NamePronunciation namePro = new
	  NamePronunciation("u821340","","","",null);
	  when(jdbcTemplate.queryForObject(anyString(),
	  any(RowMapper.class),anyLong())).thenReturn(namePro); assertEquals(null,
	  repository.findById(1L).getId()); }
	 
	
	@Test
	public void deleteByIdTest() {
		when(jdbcTemplate.update(anyString(),Mockito.any(Object[].class))).thenReturn(1);
		assertEquals(0, repository.deleteById(1L));
	}
	
	@Test
	public void insertTest() {
		NamePronunciation namePro = new NamePronunciation("u821340","","","",null);
		when(jdbcTemplate.update(anyString(),Mockito.any(Object[].class))).thenReturn(1);
		assertEquals(0, repository.insert(namePro));
	}
	
	@Test
	public void updateTest() {
		NamePronunciation namePro = new NamePronunciation("u821340","","","",null);
		when(jdbcTemplate.update(anyString(),Mockito.any(Object[].class))).thenReturn(1);
		assertEquals(0, repository.update(namePro));
	}
	
	
	
}
