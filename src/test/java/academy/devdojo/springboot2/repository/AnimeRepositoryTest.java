package academy.devdojo.springboot2.repository;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.util.AnimeCreator;
import lombok.extern.log4j.Log4j2;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace=Replace.NONE)
@DisplayName("Test for Anime Repository")
@Log4j2
class AnimeRepositoryTest {
	
	@Autowired
	private AnimeRepository animeRepository; 
	
	@Test
	@DisplayName("Save persist anime whe Sucessfull")
	void save_PersistAnime_WhenSuccessful() {
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
		
		Assertions.assertThat(animeSaved).isNotNull();
		Assertions.assertThat(animeSaved.getId()).isNotNull();
		Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
	}
	
	@Test
	@DisplayName("Save update anime whe Sucessfull")
	void save_UpdateAnime_WhenSuccessful() {
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
		
		animeSaved.setName("Outro nome");
		
		Anime animeUpdate = this.animeRepository.save(animeToBeSaved);
		
		Assertions.assertThat(animeUpdate).isNotNull();
		Assertions.assertThat(animeUpdate.getId()).isNotNull();
		Assertions.assertThat(animeUpdate.getName()).isEqualTo(animeToBeSaved.getName());
	}
	
	@Test
	@DisplayName("Delete remove anime whe Sucessfull")
	void delete_RemoveAnime_WhenSuccessful() {
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
		
		animeSaved.setName("Outro nome");
		
		//soh para testar se tava funcionando o H2, não é uma boa pratica por log em test pois ja tem os assert
		//log.info(animeSaved.getId());
		
		this.animeRepository.delete(animeToBeSaved);
		
		Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());
		
		Assertions.assertThat(animeOptional).isEmpty();
	}
	
	@Test
	@DisplayName("Find by name return list anime whe Sucessfull")
	void findByName_ReturnListOfAnime_WhenSuccessful() {
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
		
		String name = animeSaved.getName();
		
		List<Anime> animes = this.animeRepository.findByName(name);
		
		Assertions.assertThat(animes).isNotEmpty();
		Assertions.assertThat(animes).contains(animeSaved);
	}
	
	@Test
	@DisplayName("Find by name return empty list anime whe no anime is found")
	void findByName_ReturnEmptyListOfAnime_WhenIsNotFound() {
		
		List<Anime> animes = this.animeRepository.findByName("sdasda");
		Assertions.assertThat(animes).isEmpty();
		
	}
	
	@Test
	@DisplayName("Save throw when name is empty")
	void save_ThrowsConstraintValidationException_WhenNameIsEmpty() {
		Anime anime = new Anime();
		//Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime)).isInstanceOf(ConstraintViolationException.class);
		
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.animeRepository.save(anime))
			.withMessageContaining("The anime name cannot be empty");
	}

}
