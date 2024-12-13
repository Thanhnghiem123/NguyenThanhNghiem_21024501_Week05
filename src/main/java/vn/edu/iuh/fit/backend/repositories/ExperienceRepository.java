package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.backend.models.Experience;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
}