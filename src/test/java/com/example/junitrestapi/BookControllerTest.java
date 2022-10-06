package com.example.junitrestapi;

import com.example.junitrestapi.entity.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @MockBean
    private BookService bookService;

    Book book = new Book(1L, "name","", 10 );
    Book book2 = new Book(1L, "name2","", 9 );
    Book book3 = new Book(1L, "name3","", 8 );


    @Test
    public void getAllBooks_success() throws Exception{
        List<Book> books = new ArrayList<>(Arrays.asList(book, book2, book3));

        Mockito.when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("name3")));
    }

    @Test
    public void getBookById_success() throws Exception{
        Mockito.when(bookService.getBookById(book.getId())).thenReturn(Optional.of(book));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", notNullValue()))
                        .andExpect(jsonPath("$.name", is("name")));
    }

    @Test
    public void createBook_success() throws Exception{
        Book record = Book.builder()
                .id(4L)
                .name("record")
                .summary("")
                .rating(10)
                .build();
        Mockito.when(bookService.saveBook(record)).thenReturn(record);
        String content = objectWriter.writeValueAsString(record);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("record")));
    }

    @Test
    public void updateBookRecord_success() throws Exception{
        Book updatedRecord = Book.builder()
                .id(1L)
                .name("updated")
                .rating(10)
                .summary("")
                .build();

        Mockito.when(bookService.getBookById(book.getId())).thenReturn(Optional.of(book));
        Mockito.when(bookService.saveBook(updatedRecord)).thenReturn(updatedRecord);

        String updatedContent = objectWriter.writeValueAsString(updatedRecord);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.put("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("updated")));
    }

    @Test
    public void deleteBookById_success() throws Exception{
        Mockito.when(bookService.getBookById(book.getId())).thenReturn(Optional.of(book));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/book/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
