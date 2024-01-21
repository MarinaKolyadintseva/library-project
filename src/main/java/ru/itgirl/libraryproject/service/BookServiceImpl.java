package ru.itgirl.libraryproject.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itgirl.libraryproject.dto.AuthorDto;
import ru.itgirl.libraryproject.dto.BookCreateDto;
import ru.itgirl.libraryproject.dto.BookDto;
import ru.itgirl.libraryproject.dto.BookUpdateDto;
import ru.itgirl.libraryproject.model.Author;
import ru.itgirl.libraryproject.model.Book;
import ru.itgirl.libraryproject.model.Genre;
import ru.itgirl.libraryproject.repository.BookRepository;
import ru.itgirl.libraryproject.repository.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public BookDto getByNameV1(String name) {
        Book book = bookRepository.findBookByName(name).orElseThrow();
        return convertEntityToDto(book);
    }


    @Override
    public BookDto getByNameV2(String name) {
        Book book = bookRepository.findBookByNameBySql(name).orElseThrow();
        return convertEntityToDto(book);
    }

    @Override
    public BookDto getByNameV3(String name) {
        Specification<Book> specification = Specification.where(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("name"), name);
            }
        });
        Book book = bookRepository.findOne(specification).orElseThrow();
        return convertEntityToDto(book);
    }

    @Override
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public BookDto createBook(BookCreateDto bookCreateDto) {
        Book book = bookRepository.save(convertDtoToEntity(bookCreateDto));
        Genre genre = genreRepository.findById(bookCreateDto.getGenreId()).orElseThrow();
        book.setGenre(genre);
        book  = bookRepository.save(book);
        BookDto bookDto = convertEntityToDto(book);
        return bookDto;
    }

    private Book convertDtoToEntity(BookCreateDto bookCreateDto) {
        Genre genre = genreRepository.findById(bookCreateDto.getGenreId()).orElseThrow();
        return Book.builder()
                .genre(genre)
                .name(bookCreateDto.getName())
                .build();
    }


    @Override
    public BookDto updateBook(BookUpdateDto bookUpdateDto) {
        Book book = bookRepository.findById(bookUpdateDto.getId()).orElseThrow();
        Genre genre = genreRepository.findById(bookUpdateDto.getGenreId()).orElseThrow();
        book.setGenre(genre);
        book.setName(bookUpdateDto.getName());
        Book Book = bookRepository.save(book);
        BookDto bookDto = convertEntityToDto(Book);
        return bookDto;
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    private BookDto convertEntityToDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .genre(book.getGenre().getName())
                .name(book.getName())
                .build();
    }
}


    


