package com.gkp.onlinebookstore.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import jakarta.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookCategory {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    private String name;

	    @OneToMany(mappedBy = "category")
	  
	    private Set<Book> books;
	    
	    // One-to-Many relationship with Author
	    @OneToMany(mappedBy = "category")
	    private Set<Author> authors;
	    
	    
}
