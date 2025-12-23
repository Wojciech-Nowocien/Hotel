package screens;

public class WelcomeScreen extends Screen {

    @Override
    public void render() {
        System.out.println("Witaj w Hotelu!\n");
        System.out.println("Napisz 1, aby się zalogować.");
        System.out.println("Napisz 2, aby się zarejestrować.");
        System.out.println("Napisz 3, aby zamknąć program.");
        switch (INPUT.nextInt()) {
            case 1 -> changeScreen(new LoginScreen());
            case 2 -> changeScreen(new RegisterScreen());
            case 3 -> System.exit(0);
        }
    }
}
