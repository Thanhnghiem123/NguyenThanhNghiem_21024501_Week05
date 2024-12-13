package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.iuh.fit.backend.enums.SkillLevel;
import vn.edu.iuh.fit.backend.models.Candidate;

import java.util.List;


public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    @Query("""
            select c from Candidate c inner join c.candidateSkills candidateSkills
            where candidateSkills.skillLevel = ?1 and candidateSkills.skill.skillName = ?2
            """)
    List<Candidate> findBySkillLevelAndSkillName(SkillLevel skillLevel, String skillName);
}