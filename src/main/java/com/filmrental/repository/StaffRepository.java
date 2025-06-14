    package com.filmrental.repository;

    import com.filmrental.model.entity.Staff;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.stereotype.Repository;

    import java.util.List;
    import java.util.Optional;

    @Repository
    public interface StaffRepository extends JpaRepository<Staff, Integer> {
        Optional<Staff> findByEmail(String email);

        List<Staff> findByLastNameContainingIgnoreCase(String lastName);

        @Query("SELECT s FROM Staff s WHERE s.address.phone = :phone")
        Optional<Staff> findByAddressPhone(@Param("phone") String phone);

        @Query("SELECT s FROM Staff s WHERE LOWER(s.address.city.country.country) LIKE LOWER(CONCAT('%', :country, '%'))")
        List<Staff> findByAddressCityCountryCountryContainingIgnoreCase(@Param("country") String country);
    }