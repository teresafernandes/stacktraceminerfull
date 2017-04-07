package br.ufrn.lets.stacktraceminer.model;
import java.io.Serializable;

/**
 * Interface of generic entity to implement methods to get and set the Id attribute.
 * @author Teresa Fernandes
 * */
public interface IdEntity extends Serializable {

	public Long getId();
	public void setId(Long id);
}
