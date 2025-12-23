package screens;

import model.Hotel;
import util.BetterScanner;

public abstract class Screen {
    public static final BetterScanner INPUT = new BetterScanner();
    protected final Hotel hotel;

    public Screen(Hotel hotel) {
        this.hotel = hotel;
    }

    public Screen(){
        this.hotel = Hotel.HOTEL;
    }

    void changeScreen(Screen screen){
        hotel.setScreen(screen);
    }

    public abstract void render();
}
