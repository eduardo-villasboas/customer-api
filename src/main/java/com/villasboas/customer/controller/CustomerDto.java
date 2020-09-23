package com.villasboas.customer.controller;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomerDto {
	
	@NotNull(message = "id cannot be null")
	private UUID id;

	@NotNull(message = "name cannot be null")
	private String name;

	@NotNull(message = "cpf cannot be null")
	private String cpf;
	/*
	 * TODO: Arrumar isso, pois não é a melhor solução. Isso deveria ser uma
	 * configuração na arquitetura dos testes, pois somente os testes precisam
	 * disso. Porém, não encontrei uma forma de adicionar um Serializer e um
	 * Deserializer no mockMvc e @ContextConfiguration não funciona para adicionar
	 * uma configuração do jacson.
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@NotNull(message = "birthDate cannot be null")
	private LocalDate birthDate;

	private Short yearsOld;

	@Override
	public String toString() {
		return "CustomerDto [id=" + id + ", name=" + name + ", cpf=" + cpf + ", birthDate=" + birthDate + ", yearsOld="
				+ yearsOld + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(birthDate, cpf, id, name, yearsOld);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CustomerDto other = (CustomerDto) obj;
		return Objects.equals(birthDate, other.birthDate) && Objects.equals(cpf, other.cpf)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(yearsOld, other.yearsOld);
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
