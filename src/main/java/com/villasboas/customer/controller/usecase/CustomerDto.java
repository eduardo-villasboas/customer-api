package com.villasboas.customer.controller.usecase;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomerDto {

	private UUID id;

	private String name;

	private String cpf;

	/*
	 * TODO: Arrumar isso, pois não é a melhor solução.
	 * Isso deveria ser uma configuração na arquitetura dos testes,
	 * pois somente os testes precisam disso. Porém, não encontrei uma forma de 
	 * adicionar um Serializer e um Deserializer no mockMvc e @ContextConfiguration não 
	 * funciona para adicionar uma configuração do jacson. 
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDate birthDate;

	private Short yearsOld;

	@Override
	public String toString() {
		return "CustomerDto [id=" + id + ", name=" + name + ", cpf=" + cpf + ", birthDate=" + birthDate
				+ ", yearsOld=" + yearsOld + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((yearsOld == null) ? 0 : yearsOld.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerDto other = (CustomerDto) obj;
		if (birthDate == null) {
			if (other.birthDate != null)
				return false;
		} else if (!birthDate.equals(other.birthDate))
			return false;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (yearsOld == null) {
			if (other.yearsOld != null)
				return false;
		} else if (!yearsOld.equals(other.yearsOld))
			return false;
		return true;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Short getYearsOld() {
		return yearsOld;
	}

	public void setYearsOld(Short yearsOld) {
		this.yearsOld = yearsOld;
	}

}
