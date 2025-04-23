package com.organization.orgHierarchy.service;

import com.organization.orgHierarchy.exception.IllegalIDException;
import com.organization.orgHierarchy.exception.NotEmptyException;

public interface OrgHierarchy {
    boolean isEmpty();

    int size();

    int level(int id) throws IllegalIDException;

    void hireOwner(int id) throws NotEmptyException;

    void hireEmployee(int id, int bossid) throws IllegalIDException;

    void fireEmployee(int id) throws IllegalIDException;

    void fireEmployee(int id, int manageid) throws IllegalIDException;

    int boss(int id) throws IllegalIDException;

    int lowestCommonBoss(int id1, int id2) throws IllegalIDException;

    String toString(int id) throws IllegalIDException;

    String toJson() throws IllegalIDException;

    void reset() throws IllegalIDException;
}
