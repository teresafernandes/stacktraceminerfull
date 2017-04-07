package br.ufrn.lets.stacktraceminer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a exception on the structure of stack traces.
 * @author Teresa Fernandes
 */

@Entity
@Table(name="exception")
public class STException implements IdEntity{
	
	private static final long serialVersionUID = 1L;


	@Id
    @GeneratedValue
    @Column(name = "id_exception", nullable = false)
	private Long id;
	
	@Column(name="name",columnDefinition="text")
	private String name;
	
	@Column(name="description",columnDefinition="text")
	private String description;
	
	@Column(name="count")
	private Long count;
	
	public STException(){
		
	}

	public STException(Long id, String name, String description, Long count) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.count = count;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String toString(){
		if(name != null){
			String s = "";
			s+= this.name;
			if(description != null)
				s+="\n"+ this.description;
			return s;
		}
		return null;
	}

	public STException clone(){
		STException exp = new STException();
		exp.setId(this.id);
		exp.setName(this.name);
		exp.setDescription(this.description);
		return exp;
	}
	
	public boolean isEmpty(){
		return this.getName() == null || this.getName().isEmpty() || this.getDescription() == null || this.getDescription().isEmpty();
	}
}
