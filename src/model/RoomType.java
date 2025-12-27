package model;

public enum RoomType {
    SINGLE,
    DOUBLE,
    APARTMENT;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
