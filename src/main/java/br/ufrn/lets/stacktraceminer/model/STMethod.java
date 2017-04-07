package br.ufrn.lets.stacktraceminer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Represents a method on the structure of stack traces .
 * @author Teresa Fernandes
 */

@Entity
@Table(name="method")
public class STMethod implements IdEntity{
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    @Column(name = "id_method", nullable = false)
	private Long id;
	
	@Column(name = "name",columnDefinition="text")
	private String name;
	
	@ManyToOne
	@JoinColumn(name="id_class")
	private STClass classs;
	
	public STMethod(){
		
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
	public STClass getClasss() {
		return classs;
	}
	public void setClasss(STClass classs) {
		this.classs = classs;
	}
	
	public String toString(){
		if(name != null){
			String s = "";
			if(classs != null)
				s+=classs.toString();
			s+="."+ this.name;
			return s;
		}
		return null;
	}
	
}
