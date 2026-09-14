package com.quarkbau.monolith.planning.model;

public enum CompanyRole {

    ADMIN(true),
    BAULEITER(true),
    PROJEKTLEITER(true),
    TEAM_LEITER(true),
    PROJEKT_MANAGER(true),

    SUBCONTRACTOR_OWNER(false),
    SUBCONTRACTOR_FIELD_EXPERT(false),
    SUBCONTRACTOR_ASPHALT_EXPERT(false);

    private final boolean isInternal;

    CompanyRole(boolean isInternal) {
        this.isInternal = isInternal;
    }

    public boolean isInternal() {
        return isInternal;
    }
}
