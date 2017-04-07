package br.ufrn.lets.stacktraceminer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Represents an stack trace.
 * @author Teresa Fernandes
 */

@Entity
@Table(name="stacktrace")
public class Stacktrace implements IdEntity{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue
    @Column(name = "id_stacktrace", nullable = false)
	private Long id;
	
	@Column(name="content",columnDefinition="text")
	private String content;
	
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name="id_exception")
	private STException exception;
	
	@Column(name="id_question",columnDefinition="text")
	private String question;
	
	@Column(name="id_answer",columnDefinition="text")
	private String answer;
	
	@Column(name="creation_date_post")
	private String creationDatePost;
	
	@Column(name="tags",columnDefinition="text")
	private String tags;
	
	@Column(name="caused_by")
	private boolean causedby;
	
	@Column(name="creation_date")
	private Date creationDate;
	
	@ManyToOne
	@JoinColumn(name="id_main_stack")
	private Stacktrace mainStack;

	@Column(name="score")
	private String score;
	
	@Column(name="relevance")
	private String relevance;
	
	@Column(name="description",columnDefinition="text")
	private String description;
	
	@Column(name="mining_date")
	private Date miningDate;
	
	@Transient
	private List<STFrame> frames;
	
	public Stacktrace(){
		frames = new ArrayList<STFrame>();
	}

	public Stacktrace(Long id, String content, String question, String answer,
			String creationDatePost, String tags) {
		super();
		this.id = id;
		this.content = content;
		this.question = question;
		this.answer = answer;
		this.creationDatePost = creationDatePost;
		this.tags = tags;
	}



	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getContent() {
		if(content==null)
			return "";
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public List<STFrame> getFrames() {
		return frames;
	}

	public void setFrames(List<STFrame> frames) {
		this.frames = frames;
	}
	
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public STException getException() {
		return exception;
	}

	public void setException(STException exception) {
		this.exception = exception;
	}

	public String toString(){
		String s = "";
		if(frames != null){
			s+=exception.toString();
			for(STFrame f : frames)
				s+="\n"+f.toString();
		}
		return s;
		
		//return content;
	}

	public boolean isCausedby() {
		return causedby;
	}

	public void setCausedby(boolean causedby) {
		this.causedby = causedby;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationDatePost() {
		return creationDatePost;
	}

	public void setCreationDatePost(String creationDatePost) {
		this.creationDatePost = creationDatePost;
	}

	public Stacktrace getMainStack() {
		return mainStack;
	}

	public void setMainStack(Stacktrace mainStack) {
		this.mainStack = mainStack;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}	

}
