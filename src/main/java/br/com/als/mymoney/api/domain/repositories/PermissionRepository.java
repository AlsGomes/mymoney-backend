package br.com.als.mymoney.api.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.als.mymoney.api.domain.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
