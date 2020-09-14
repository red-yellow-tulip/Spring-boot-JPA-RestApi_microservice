package base.serviceDB;

import base.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UniversityRepository extends JpaRepository<University, Long> {

    Optional<University> findByUniversityId(long l);
}
