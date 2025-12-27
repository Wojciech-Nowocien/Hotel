package model;

public enum RoomType {
    SINGLE,
    DOUBLE,
    APARTMENT;

    @Override
    public String toString() {
        return switch (this){
            case SINGLE -> "pojedynczy";
            case DOUBLE -> "podwójny";
            case APARTMENT -> "apartament";
        };
    }
}
