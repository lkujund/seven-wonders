package hr.algebra.sevenwonders.utils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectionUtils {
    public static void createDocumentation(){
        Path target = Path.of("target");
        String title = target
                .toAbsolutePath()
                .toString();
        title = title.substring(0, title.lastIndexOf("\\"));
        title = title.substring(title.lastIndexOf("\\") + 1);

        try (Stream<Path> paths = Files.walk(target)) {
            List<String> classFiles = findPaths(paths);


            String documentationHtml = String.format("""
                    <!DOCTYPE html>
                    <html>
                    <head>
                    <title>%s</title>
                    </head>
                    <body>
                    """, title);

            for(String classFile : classFiles) {
                String[] classFileTokens = classFile.split("classes");
                String classFilePath = classFileTokens[1];
                String reducedClassFilePath =
                        classFilePath.substring(1, classFilePath.lastIndexOf('.'));
                String fullyQualifiedName = reducedClassFilePath.replace('\\', '.');
                String className = fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf('.') + 1);

                documentationHtml += "\n<h1>" + className + "</h1>\n";
                documentationHtml += "\t<h2>" + fullyQualifiedName + "</h2>\n";

                Class<?> deserializedClass = Class.forName(fullyQualifiedName);

                Field[] classFields = deserializedClass.getDeclaredFields();
                documentationHtml += "\t\t<h2>Fields</h2>\n";


                for(Field field : classFields) {

                    documentationHtml += "\t\t\t<h3>";

                    int modifiers = field.getModifiers();
                    appendModifiers(modifiers, documentationHtml);

                    documentationHtml += field.getType().getTypeName() + " ";

                    documentationHtml += field.getName() + "</h3>\n";
                }

                Method[] classMethods = deserializedClass.getDeclaredMethods();
                documentationHtml += "\t\t<h2>Methods</h2>\n";
                for(Method method : classMethods) {
                    documentationHtml += "\t\t\t<h3>";

                    int modifiers = method.getModifiers();
                    appendModifiers(modifiers, documentationHtml);

                    documentationHtml += method.getReturnType().getTypeName() + " ";

                    documentationHtml += method.getName();
                    String parameters = Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.joining(", "));
                    documentationHtml +="(" + parameters + ")</h3>\n";
                }

                Constructor[] classConstructors = deserializedClass.getDeclaredConstructors();
                documentationHtml += "\t\t<h2>Constructors</h2>\n";
                for(Constructor constructor : classConstructors) {
                    documentationHtml += "\t\t\t<h3>";

                    int modifiers = constructor.getModifiers();
                    appendModifiers(modifiers, documentationHtml);


                    documentationHtml += constructor.getName();
                    String parameters = Arrays.stream(constructor.getParameterTypes()).map(Class::getName).collect(Collectors.joining(", "));
                    documentationHtml +="(" + parameters + ")</h3>\n";
                }
            }


            String footerHtml = """
                    </body>
                    </html>
                    """;

            Path documentationFilePath = Path.of("files/documentation.html");
            Path filesFolderPath = documentationFilePath.toAbsolutePath().getParent();
            if (!Files.exists(filesFolderPath))
            {
                Files.createDirectories(filesFolderPath.toAbsolutePath());
            }
            String fullHtml = documentationHtml + footerHtml;

            Files.write(documentationFilePath, fullHtml.getBytes());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void appendModifiers(int modifiers, String documentationHtml) {
        switch (modifiers){
            case Modifier.PRIVATE: documentationHtml += "private ";
            case Modifier.PROTECTED: documentationHtml += "protected ";
            case Modifier.PUBLIC: documentationHtml += "public ";
        }

        if(Modifier.isStatic(modifiers)) {
            documentationHtml += "static ";
        }

        if(Modifier.isFinal(modifiers)) {
            documentationHtml += "final ";
        }
    }

    private static List<String> findPaths(Stream<Path> paths) {
        return paths
                .map(Path::toString)
                .filter(file -> file.endsWith(".class"))
                .filter(file -> !file.endsWith("module-info.class"))
                .toList();
    }
}
