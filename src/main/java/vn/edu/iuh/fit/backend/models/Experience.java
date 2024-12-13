package vn.edu.iuh.fit.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "experience")
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exp_id", nullable = false)
    private Long id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "role")
    private String role;

    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    @Column(name = "work_desc", nullable = false)
    private String workDesc;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "can_id", nullable = false)
    private Candidate can;

}