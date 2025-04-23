package com.organization.orgHierarchy.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.organization.orgHierarchy.exception.IllegalIDException;
import com.organization.orgHierarchy.exception.NotEmptyException;
import com.organization.orgHierarchy.service.OrgHierarchyImpl;

@RestController
@RequestMapping("/api")
public class OrgHierarchyController {

    private final OrgHierarchyImpl org = new OrgHierarchyImpl();

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is running.");
    }

    @PostMapping("/hireOwner")
    public ResponseEntity<String> hireOwner(@RequestBody Map<String, Integer> body) {
        try {
            org.hireOwner(body.get("id"));
            return ResponseEntity.ok("Owner hired successfully.");
        } catch (NotEmptyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/hireEmployee")
    public ResponseEntity<String> hireEmployee(@RequestBody Map<String, Integer> body) {
        try {
            org.hireEmployee(body.get("id"), body.get("bossId"));
            return ResponseEntity.ok("Employee hired successfully.");
        } catch (IllegalIDException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/fireEmployee/{id}")
    public ResponseEntity<String> fireEmployee(@PathVariable int id) {
        try {
            org.fireEmployee(id);
            return ResponseEntity.ok("Employee fired.");
        } catch (IllegalIDException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/boss/{id}")
    public ResponseEntity<String> getBoss(@PathVariable int id) {
        try {
            int bossId = org.boss(id);
            return ResponseEntity.ok("Boss ID: " + bossId);
        } catch (IllegalIDException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/level/{id}")
    public ResponseEntity<String> getLevel(@PathVariable int id) {
        try {
            int level = org.level(id);
            return ResponseEntity.ok("Level: " + level);
        } catch (IllegalIDException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/lowestCommonBoss")
    public ResponseEntity<String> getLowestCommonBoss(@RequestParam int id1, @RequestParam int id2) {
        try {
            int lcb = org.lowestCommonBoss(id1, id2);
            return ResponseEntity.ok("Lowest Common Boss ID: " + lcb);
        } catch (IllegalIDException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/toString/{id}")
    public ResponseEntity<String> getHierarchy(@PathVariable int id) {
        try {
            String result = org.toString(id);
            return ResponseEntity.ok(result);
        } catch (IllegalIDException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/toJson")
    public ResponseEntity<String> getJson() {
        try {
            String json = org.toJson();
            return ResponseEntity.ok(json);
        } catch (IllegalIDException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<String> reset() {
        try {
            org.reset();
        } catch (IllegalIDException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Organization hierarchy reset successfully.");
    }
}
