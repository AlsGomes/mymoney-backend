package br.com.als.mymoney.api.domain.model.dto;

import br.com.als.mymoney.api.domain.model.Contact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ContactDTO {
	@EqualsAndHashCode.Include
	private String code;

	private String name;
	private String email;
	private String telephone;

	public ContactDTO(Contact contact) {
		this.code = contact.getCode();
		this.name = contact.getName();
		this.email = contact.getEmail();
		this.telephone = contact.getTelephone();
	}

	@Override
	public String toString() {
		return "ContactDTO [code=" + code + ", name=" + name + ", email=" + email + ", telephone=" + telephone + "]";
	}
}
