package br.com.vanderlei.api.dto;

public class AtualizaStatusDTO {
	private String status;


	public AtualizaStatusDTO() {
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public AtualizaStatusDTO(String status) {
		super();
		this.status = status;
	}
	
}
