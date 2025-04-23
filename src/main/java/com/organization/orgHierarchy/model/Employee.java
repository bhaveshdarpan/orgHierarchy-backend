package com.organization.orgHierarchy.model;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private int id;
    private int level;
    private Employee boss;
    private List<Employee> subordinates;

    public Employee(int id, int level, Employee boss) {
        this.id = id;
        this.level = level;
        this.boss = boss;
        this.subordinates = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public Employee getBoss() {
        return boss;
    }

    public void setBoss(Employee boss) {
        this.boss = boss;
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }

    public void addSubordinate(Employee subordinate) {
        subordinates.add(subordinate);
    }

    public void removeSubordinate(Employee subordinate) {
        subordinates.remove(subordinate);
    }

    public boolean isOwner() {
        return boss == null;
    }
}
