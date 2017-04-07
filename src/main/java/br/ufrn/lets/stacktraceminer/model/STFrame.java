package br.ufrn.lets.stacktraceminer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Represents a frame on the structure of stack traces.
 * @author Teresa Fernandes
 */

@Entity
@Table(name="frame")
public class STFrame implements IdEntity{

	private static final long serialVersionUID = 1L;
	

	@Id
    @GeneratedValue
    @Column(name = "id_frame", nullable = false)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="id_method")
	private STMethod method;
	
	@Column(name="file_line",columnDefinition="text")
	private String fileline;

	@ManyToOne
	@JoinColumn(name="id_stacktrace")
	private Stacktrace stacktrace;
	
	@Column(name="signaler")
	private boolean signaler;
	
	@ManyToOne
	@JoinColumn(name="id_call_frame")
	private STFrame callFrame;
	
	public STFrame(){
		
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public STMethod getMethod() {
		return method;
	}
	public void setMethod(STMethod method) {
		this.method = method;
	}
	public String getFileline() {
		return fileline;
	}
	public void setFileline(String fileline) {
		this.fileline = fileline;
	}
	
	public Stacktrace getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(Stacktrace stacktrace) {
		this.stacktrace = stacktrace;
	}

	public String toString(){
		if(method != null){
			String s = "at ";
			if(method != null)
				s+=method.toString();
			s+="("+method.getClasss().getName();
			if(fileline != null)
				s+=":"+fileline;
			s+=")";				
			return s;
		}
		return null;
	}

	public boolean isSignaler() {
		return signaler;
	}

	public void setSignaler(boolean signal) {
		this.signaler = signal;
	}

	public STFrame getCallFrame() {
		return callFrame;
	}

	public void setCallFrame(STFrame callFrame) {
		this.callFrame = callFrame;
	}

	
}
