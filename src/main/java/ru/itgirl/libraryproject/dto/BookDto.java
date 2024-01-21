package ru.itgirl.libraryproject.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
public class BookDto {
    private Long id;
    private String name;
    private String genre;
    List<AuthorDto> author;
}