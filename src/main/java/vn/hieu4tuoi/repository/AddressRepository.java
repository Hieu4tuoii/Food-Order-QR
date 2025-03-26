package vn.hieu4tuoi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hieu4tuoi.model.AddressEntity;

public interface AddressRepository  extends JpaRepository<AddressEntity, Long> {
    AddressEntity findByUserIdAndAddressType(Long userId, Integer addressType);
}
