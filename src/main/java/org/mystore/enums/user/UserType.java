package org.mystore.enums.user;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum UserType {

    //IFG,
    IFG_ADMIN(0),
    IFG_USER(0),
    IFG_AR_ADMIN(0),
    IFG_CORPORATE_ADMIN(1),
    IFG_CORPORATE_USER(1),
    IFG_AIRLINE_ADMIN(1),
    IFG_AIRLINE_USER(1),
    IFG_AIRLINE_SUB_SITE_USER,
    IFG_AGENT_ADMIN(1),
    IFG_AGENT_USER(1),

    //AR
    AR_ADMIN(0),
    AR_USER(0),
    CORPORATE_ADMIN(1),
    TMC_ADMIN(1),
    GLOBAL_TMC_PARTNER_ADMIN(1),
    TMC_PARTNER,
    AIRLINE_ADMIN(1),
    ADMIN,
    AGENT,
    CONSULTANT,
    TRAVEL_ARRANGER,
    TRAVEL_MANAGER,
    USER,
    TRAVELLER;

    private int level;

    public static Set<UserType> getStateUpdatableTypes() {
        Set<UserType> statusList = new HashSet<>();
        statusList.add(UserType.ADMIN);
        statusList.add(UserType.AGENT);
        statusList.add(UserType.CONSULTANT);
        statusList.add(UserType.TRAVEL_ARRANGER);
        statusList.add(UserType.TRAVEL_MANAGER);
        statusList.add(UserType.USER);
        statusList.add(UserType.TRAVELLER);
        statusList.add(UserType.TMC_PARTNER);
        statusList.add(UserType.IFG_USER);
        statusList.add(UserType.IFG_AGENT_USER);
        statusList.add(UserType.IFG_AIRLINE_USER);
        statusList.add(UserType.IFG_CORPORATE_USER);
        return statusList;
    }

    public static Set<UserType> getIfgAgentTypes() {
        Set<UserType> statusList = new HashSet<>();
        statusList.add(UserType.IFG_AGENT_ADMIN);
        statusList.add(UserType.IFG_AGENT_USER);
        return statusList;
    }

    public static Set<UserType> getIfgUserTypes() {
        Set<UserType> statusList = new HashSet<>();
        statusList.add(UserType.IFG_ADMIN);
        statusList.add(UserType.IFG_USER);
        statusList.add(UserType.IFG_AIRLINE_ADMIN);
        statusList.add(UserType.IFG_AIRLINE_USER);
        statusList.add(UserType.IFG_AGENT_ADMIN);
        statusList.add(UserType.IFG_AGENT_USER);
        statusList.add(UserType.IFG_CORPORATE_ADMIN);
        statusList.add(UserType.IFG_CORPORATE_USER);
        return statusList;
    }

    public static Set<UserType> getCbtUserTypes() {
        Set<UserType> statusList = new HashSet<>();
        statusList.add(UserType.AR_ADMIN);
        statusList.add(UserType.AR_USER);
        statusList.add(UserType.CORPORATE_ADMIN);
        statusList.add(UserType.TMC_ADMIN);
        statusList.add(UserType.GLOBAL_TMC_PARTNER_ADMIN);
        statusList.add(UserType.TMC_PARTNER);
        statusList.add(UserType.AIRLINE_ADMIN);
        statusList.add(UserType.ADMIN);
        statusList.add(UserType.AGENT);
        statusList.add(UserType.CONSULTANT);
        statusList.add(UserType.TRAVEL_ARRANGER);
        statusList.add(UserType.TRAVEL_MANAGER);
        statusList.add(UserType.USER);
        statusList.add(UserType.TRAVELLER);
        return statusList;
    }

}
