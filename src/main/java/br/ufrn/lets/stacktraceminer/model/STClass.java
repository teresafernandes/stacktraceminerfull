package br.ufrn.lets.stacktraceminer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a class on the structure of stack traces.
 * 
 * @author Teresa Fernandes
 */


@Entity
@Table(name="class")
public class STClass implements IdEntity{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue
    @Column(name = "id_class", nullable = false)
	private Long id;
	
	@Column(name="name",columnDefinition="text")
	private String name;
	
	@Column(name="path",columnDefinition="text")
	private String path;
	
	public STClass(){
		
	}
	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String toString(){
		if(name != null){
			String s = "";
			if(path != null)
				s+=this.path;
			return s;
		}
		return null;
	}
	
}
