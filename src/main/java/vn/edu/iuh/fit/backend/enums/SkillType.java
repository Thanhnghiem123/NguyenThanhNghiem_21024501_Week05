package vn.edu.iuh.fit.backend.enums;

public enum SkillType {
    UNSPECIFIC(0),

    TECHNICAL_SKILL(1),

    SOFT_SKILL(2);

    SkillType(int value) {
    }

    public static SkillType fromValue(int value) {
        for (SkillType type : SkillType.values()) {
            if (type.ordinal() == value) {
                return type;
            }
        }
        return UNSPECIFIC;
    }
}
