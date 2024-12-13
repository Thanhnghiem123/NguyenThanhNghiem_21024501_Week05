package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.backend.models.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
