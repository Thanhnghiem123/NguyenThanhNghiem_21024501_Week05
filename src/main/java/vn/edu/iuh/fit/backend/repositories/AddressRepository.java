package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.edu.iuh.fit.backend.models.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
}