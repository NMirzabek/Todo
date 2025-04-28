package com.example.springdb.servlets;

import com.example.springdb.DB.UserData;
import com.example.springdb.JSON.Restore;
import com.example.springdb.entity.UserReport;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CabinetServlet {
    private final UserData userData;
    private final EntityManagerFactory emf;
    @GetMapping("/cabinet")
    public String cabinet() {
        new Thread(new Restore(emf)).start();
        return "cabinet";
    }
    @GetMapping("/report")
    public String report(Model model) {
        List<UserReport> report = userData.getReport();
        System.out.println(report);
        model.addAttribute("report", report);
        return "report";
    }
}
