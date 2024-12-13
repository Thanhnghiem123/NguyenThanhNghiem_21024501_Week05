package vn.edu.iuh.fit.backend.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.backend.services.JobServices;

@RestController("/api/vi/jobs")
public class JobResources {
    @Autowired
    private JobServices services;
}