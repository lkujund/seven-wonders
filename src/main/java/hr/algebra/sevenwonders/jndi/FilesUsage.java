package hr.algebra.sevenwonders.jndi;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Scanner;

public class FilesUsage {

    public static void main(String[] args) {

        Hashtable<String, String> environment= new Hashtable<>();
        environment.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.fscontext.RefFSContextFactory");
        environment.put(Context.PROVIDER_URL,"file:D:/tic-tac-toe/conf");

        try (InitialDirContextCloseable context = new InitialDirContextCloseable(environment)){
            listBindings(context, "", 1, 2);
            searchBindings(context);

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private static void listBindings(Context context, String path, int level, int limit) throws NamingException {
        if(level > limit) {
            return;
        }
        NamingEnumeration<Binding> listBindings = context.listBindings(path);
        while (listBindings.hasMoreElements()) {
            Binding binding = listBindings.next();
            System.out.printf("%" + level + "s", " ");
            System.out.println(binding.getName());
        }
    }

    private static void searchBindings(Context context)  {
        System.out.print("Insert filename: ");
        String fileName = readFilename(System.in);

        try {
            Object object = context.lookup(fileName);
            Properties props = new Properties();
            props.load(new FileReader(object.toString()));
        }
        catch(NamingException | IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String readFilename(InputStream in) {
        try (Scanner scanner = new Scanner(in)) {
            return scanner.next();
        }
    }
}
