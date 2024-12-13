package vn.edu.iuh.fit.backend.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.backend.services.CompanyServices;

@RestController("/api/vi/companies")
public class CompanyResources {
    @Autowired
    private CompanyServices services;
}
