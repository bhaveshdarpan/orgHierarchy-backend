package com.organization.orgHierarchy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.organization.orgHierarchy.exception.IllegalIDException;
import com.organization.orgHierarchy.exception.NotEmptyException;
import com.organization.orgHierarchy.model.Employee;

public class OrgHierarchyImpl implements OrgHierarchy {
    private Employee owner;
    private Map<Integer, Employee> employeeMap;

    public OrgHierarchyImpl() {
        employeeMap = new HashMap<>();
    }

    public boolean isEmpty() {
        return owner == null;
    }

    public int size() {
        return employeeMap.size();
    }

    public int level(int id) throws IllegalIDException {
        Employee employee = getEmployee(id);
        return employee.getLevel();
    }

    public void hireOwner(int id) throws NotEmptyException {
        if (!isEmpty()) {
            throw new NotEmptyException("Owner already exists.");
        }
        Employee owner = new Employee(id, 1, null);
        this.owner = owner;
        employeeMap.put(id, owner);
    }

    public void hireEmployee(int id, int bossid) throws IllegalIDException {
        if (employeeMap.containsKey(id)) {
            throw new IllegalIDException("Employee with ID " + id + " already exists.");
        }

        Employee boss = getEmployee(bossid);
        int level = boss.getLevel() + 1;
        Employee employee = new Employee(id, level, boss);
        boss.addSubordinate(employee);
        employeeMap.put(id, employee);
    }

    public void fireEmployee(int id) throws IllegalIDException {
        Employee employee = getEmployee(id);

        if (employee.isOwner()) {
            throw new IllegalIDException("Cannot fire the owner.");
        }

        if (!employee.getSubordinates().isEmpty()) {
            throw new IllegalIDException("Employee with ID " + id + " manages other employees.");
        }

        employee.getBoss().removeSubordinate(employee);
        employeeMap.remove(id);
    }

    public void fireEmployee(int id, int manageid) throws IllegalIDException {
        Employee employee = getEmployee(id);
        Employee newBoss = getEmployee(manageid);

        if (employee.isOwner()) {
            throw new IllegalIDException("Cannot fire the owner.");
        }

        if (!employee.getSubordinates().isEmpty()) {
            throw new IllegalIDException("Employee with ID " + id + " manages other employees.");
        }

        if (newBoss.getLevel() != employee.getLevel()) {
            throw new IllegalIDException("The new boss must be at the same level as the employee.");
        }

        employee.getBoss().removeSubordinate(employee);
        employee.setBoss(newBoss);
        newBoss.addSubordinate(employee);
    }

    public int boss(int id) throws IllegalIDException {
        Employee employee = getEmployee(id);

        if (employee.isOwner()) {
            return -1;
        }

        return employee.getBoss().getId();
    }

    public int lowestCommonBoss(int id1, int id2) throws IllegalIDException {
        Employee employee1 = getEmployee(id1);
        Employee employee2 = getEmployee(id2);

        if (employee1.isOwner() || employee2.isOwner()) {
            return -1;
        }

        while (employee1.getLevel() > employee2.getLevel()) {
            employee1 = employee1.getBoss();
        }

        while (employee2.getLevel() > employee1.getLevel()) {
            employee2 = employee2.getBoss();
        }

        while (employee1 != employee2) {
            employee1 = employee1.getBoss();
            employee2 = employee2.getBoss();
        }

        return employee1.getId();
    }

    public String toString(int id) throws IllegalIDException {
        Employee employee = getEmployee(id);
        StringBuilder sb = new StringBuilder();
        buildEmployeeHierarchyString(employee, sb);
        return sb.toString().trim();
    }

    public String toJson() throws IllegalIDException {
        if (isEmpty()) {
            throw new IllegalIDException("No employees in the organization.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{ \"employees\": [");

        for (Employee employee : employeeMap.values()) {
            sb.append("{")
                    .append("\"id\": ").append(employee.getId()).append(", ")
                    .append("\"level\": ").append(employee.getLevel()).append(", ")
                    .append("\"bossId\": ").append(employee.getBoss() != null ? employee.getBoss().getId() : "null")
                    .append(", \"subordinates\": [");

            List<Employee> subs = employee.getSubordinates();
            for (int i = 0; i < subs.size(); i++) {
                sb.append(subs.get(i).getId());
                if (i < subs.size() - 1) {
                    sb.append(", ");
                }
            }

            sb.append("]},");
        }

        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1); // Remove last comma
        }

        sb.append("]}");

        // Convert to tree structure
        return convertToTreeStructure(employeeMap);
    }

    private String convertToTreeStructure(Map<Integer, Employee> employeeMap) {
        Map<Integer, TreeNode> treeNodeMap = new HashMap<>();

        for (Employee employee : employeeMap.values()) {
            TreeNode node = new TreeNode(String.valueOf(employee.getId()));
            treeNodeMap.put(employee.getId(), node);
        }

        // Build tree nodes (assume that each employee has a `bossId` or `null` if they
        // are at the top level)
        for (Employee employee : employeeMap.values()) {
            TreeNode node = treeNodeMap.get(employee.getId());
            if (employee.getBoss() != null) {
                TreeNode bossNode = treeNodeMap.get(employee.getBoss().getId());
                bossNode.addChild(node);
            }
        }

        // Find the root (top-level boss)
        TreeNode root = null;
        for (Employee employee : employeeMap.values()) {
            if (employee.getBoss() == null) {
                root = treeNodeMap.get(employee.getId());
                break;
            }
        }

        // Build the tree structure as a JSON
        return buildTreeJson(root);
    }

    private String buildTreeJson(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"name\": \"").append(root.getName()).append("\", \"children\": [");

        for (TreeNode child : root.getChildren()) {
            sb.append(buildTreeJson(child)).append(",");
        }

        if (sb.charAt(sb.length() - 1) == ',') {
            sb.setLength(sb.length() - 1); // Remove the trailing comma
        }

        sb.append("]}");

        return sb.toString();
    }

    private void buildEmployeeHierarchyString(Employee employee, StringBuilder sb) {
        sb.append(employee.getId()).append(" ");

        for (Employee subordinate : employee.getSubordinates()) {
            buildEmployeeHierarchyString(subordinate, sb);
        }
    }

    private Employee getEmployee(int id) throws IllegalIDException {
        Employee employee = employeeMap.get(id);

        if (employee == null) {
            throw new IllegalIDException("Employee with ID " + id + " does not exist.");
        }

        return employee;
    }

    public void reset() throws IllegalIDException {
        if (isEmpty()) {
            throw new IllegalIDException("No employees to reset.");
        }

        owner = null;
        employeeMap.clear();
    }
}
