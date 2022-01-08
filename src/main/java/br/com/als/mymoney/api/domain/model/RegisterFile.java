package br.com.als.mymoney.api.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.als.mymoney.api.domain.model.dto.RegisterFileDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "register_files")
public class RegisterFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;
	private String fileName;
	private String contentType;
	private Long size;

	@ManyToOne
	@JoinColumn(name = "id_register")
	private Register register;

	public RegisterFile(RegisterFileDTO registerFile) {
		this.contentType = registerFile.getContentType();
		this.fileName = registerFile.getFileName();
		this.size = registerFile.getSize();
	}
}
