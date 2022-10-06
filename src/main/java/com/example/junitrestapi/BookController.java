package com.example.junitrestapi;

import com.example.junitrestapi.entity.Book;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Autowired
    BookService bookService;


    @GetMapping("")
    public List<Book> getBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("{id}")
    public Optional<Book> getBookById(@PathVariable(name = "id") Long id) {
        if(bookService.getBookById(id).isPresent()){
            Optional<Book> book = bookService.getBookById(id);
            return book;
        }
        return null;
    }

    @PostMapping
    public Book createBookRecord(@RequestBody @Valid Book book){
        return bookService.saveBook(book);
    }

    @PutMapping
    public Book updateBook(@RequestBody Book book) throws NotFoundException{
        if(book == null || book.getId() == null){
            throw new NotFoundException("BookRecord or ID must not be null");
        }

        Optional<Book> optionalBook = bookService.getBookById(book.getId());
        if(!optionalBook.isPresent()){
            throw new NotFoundException("Book with id: "+book.getId()+" does not exist");
        }
        Book updatedBook = optionalBook.get();
        updatedBook.setName(book.getName());
        updatedBook.setRating(book.getRating());
        updatedBook.setSummary(book.getSummary());
        return bookService.saveBook(updatedBook);
    }

    @DeleteMapping(value = "{id}")
    public void deleteBookById(@PathVariable Long id) throws NotFoundException{
        if(!bookService.getBookById(id).isPresent()){
            throw new NotFoundException("BookId "+id+" is not present");
        }
        bookService.deleteBook(id);

    }
}
