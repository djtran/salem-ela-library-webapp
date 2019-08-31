import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dom.Book;
import dom.Student;
import dynamodb.LibraryInitializer;
import dynamodb.LibraryManager;
import guice.LibraryModule;

import java.io.IOException;
import java.util.UUID;

public class Application {

    public static void main(String[] args) throws InterruptedException, IOException {

        String root = System.getProperty("user.dir");
        System.out.println("Root: " + root);
        Injector injector = Guice.createInjector(new LibraryModule());

        LibraryInitializer initializer = injector.getInstance(LibraryInitializer.class);
        System.out.println(initializer.init());

        LibraryManager manager = injector.getInstance(LibraryManager.class);

//        manager.addBook(Book.builder()
//                .id(UUID.randomUUID().toString())
//                .name("test-book Onibi Diary of a Yokai Ghost Hunter")
//                .tags(ImmutableList.of("graphic", "adventure"))
//                .build());

        Student s = Student.builder()
                .id("TestStudent1")
                .name("debid")
                .build();
        Book onibi = Book.builder()
                .id("09ba604c-1919-47c9-90a9-ee48ccf39c3a")
                .name("test-book Onibi Diary of a Yokai Ghost Hunter")
                .tags(ImmutableList.of("graphic", "adventure"))
                .build();
        System.out.println(manager.checkoutBook(s, onibi));


//        Blade.of().get("/", ctx -> ctx.text("Hello Blade")).start();
    }
}
