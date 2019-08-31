import com.google.inject.Guice;
import com.google.inject.Injector;
import dynamodb.LibraryInitializer;
import guice.LibraryModule;

public class Application {

    public static void main(String[] args) throws InterruptedException {

        String root = System.getProperty("user.dir");
        System.out.println("Root: " + root);
        Injector injector = Guice.createInjector(new LibraryModule());

        LibraryInitializer initializer = injector.getInstance(LibraryInitializer.class);
        System.out.println(initializer.init());

//        Blade.of().get("/", ctx -> ctx.text("Hello Blade")).start();
    }
}
