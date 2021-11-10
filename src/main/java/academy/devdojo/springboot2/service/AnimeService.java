package academy.devdojo.springboot2.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimeService {

	private final AnimeRepository animeRepository;
	
	public Page<Anime> listAll(Pageable pageble) {
		return animeRepository.findAll(pageble);
	}
	
	public List<Anime> listAllNonPageable() {
		return animeRepository.findAll();
	}
	
	public List<Anime> findByName(String name) {
		return animeRepository.findByName(name);
	}

	public Anime findByIdOrThrowBadRequestException(long id) {
		return animeRepository.findById(id).orElseThrow(() -> new BadRequestException("Anime not found"));
	}

	public Anime save(AnimePostRequestBody animePostRequestBody) {
		return animeRepository.save(Anime.builder().name(animePostRequestBody.getName()).build());
	}

	public void delete(long id) {
		animeRepository.delete(findByIdOrThrowBadRequestException(id));
	}

	public void replace(AnimePutRequestBody animePutRequestBody) {
		Anime animeSaved = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
		
		Anime anime = animeRepository.save(Anime.builder()
				.name(animePutRequestBody.getName())
				.id(animeSaved.getId())
				.build());
		
		animeRepository.save(anime);
	}

}
