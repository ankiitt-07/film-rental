    package com.filmrental.mapper;

    import com.filmrental.model.dto.ActorDTO;
    import com.filmrental.model.dto.FilmDTO;
    import com.filmrental.model.entity.Actor;
    import com.filmrental.model.entity.Film;
    import org.springframework.stereotype.Component;

    @Component
    public class ActorMapper {

        public ActorDTO toActorDto(Actor actor) {
            if (actor == null) return null;
            return new ActorDTO(actor.getFirstName(), actor.getLastName(), actor.getLastUpdate());
        }

        public Actor toActorEntity(ActorDTO dto) {
            if (dto == null) return null;
            Actor actor = new Actor();
            actor.setFirstName(dto.getFirstName());
            actor.setLastName(dto.getLastName());
            actor.setLastUpdate(dto.getLastUpdate());
            return actor;
        }
        public FilmDTO toFilmDTO(Film film) {
            if (film == null) return null;

            return new FilmDTO(
                    film.getFilmId(),
                    film.getTitle(),
                    film.getDescription(),
                    film.getReleaseYear(),
                    (film.getLanguage() != null) ? film.getLanguage().getLanguageId() : null,
                    (film.getOriginalLanguage() != null) ? film.getOriginalLanguage().getLanguageId() : null,
                    film.getRentalDuration(),
                    film.getRentalRate(),
                    film.getLength(),
                    film.getReplacementCost(),
                    film.getRating(),
                    film.getSpecialFeatures(),
                    film.getLastUpdate()
            );
        }


    }
