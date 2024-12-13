package vn.edu.iuh.fit.backend.enums;

public enum SkillLevel {
    MASTER(5),

    PROFESSIONAL(4),

    ADVANCED(3),

    IMTERMEDIATE(2),

    BEGINER(1);

    SkillLevel(int value) {
    }

    public static SkillLevel fromValue(int value) {
        switch (value) {
            case 1:
                return BEGINER;
            case 2:
                return IMTERMEDIATE;
            case 3:
                return ADVANCED;
            case 4:
                return PROFESSIONAL;
            case 5:
                return MASTER;
            default:
                return null;
        }
    }
}