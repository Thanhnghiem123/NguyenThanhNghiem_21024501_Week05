package vn.edu.iuh.fit.backend.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.backend.services.CandidateServices;

@RestController("/api/vi/candidates")
public class CandidateResources {
    @Autowired
    private CandidateServices services;
}