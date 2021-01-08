package me.darkeyedragon.randomtp.common.annotationprocessor;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("co.aikar.commands.annotation.*")
public class CommandAnnotationProcessor extends AbstractProcessor {

    public static String FILE_NAME = "permissions.md";

    public FileWriter createFile() {
        try {
            File file = new File(FILE_NAME);
            file.createNewFile();
            return new FileWriter("permissions.md");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> commands = roundEnv.getElementsAnnotatedWith(CommandAlias.class);
        if (commands.size() == 0) return true;
        FileWriter writer = createFile();
        StringBuilder builder = new StringBuilder("Command|Permission\n");
        builder.append("----|----\n");
        for (Element clazzElement : commands) {
            if (clazzElement.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Not a class!", clazzElement);
                continue;
            }
            String baseCommandStr = "/" + clazzElement.getAnnotation(CommandAlias.class).value().split("\\|")[0];
            //Get the command name
            for (Element subcommandElement : roundEnv.getElementsAnnotatedWith(Subcommand.class)) {
                Subcommand subCommand = subcommandElement.getAnnotation(Subcommand.class);
                CommandPermission permission = subcommandElement.getAnnotation(CommandPermission.class);
                Element parentElement = subcommandElement.getEnclosingElement();
                String finalStr = baseCommandStr;
                if (parentElement != null) {
                    Subcommand sub = parentElement.getAnnotation(Subcommand.class);
                    if(subcommandElement.getKind() == ElementKind.CLASS){
                        continue;
                    }
                    if (sub != null) {
                        finalStr = baseCommandStr + " " + sub.value();
                    }
                }

                if (subCommand != null) {
                    builder.append(finalStr).append(" ").append(subCommand.value()).append(" | ");
                } else {
                    builder.append(finalStr).append(" ").append("<No subcommand> | ");
                }
                if (permission != null) {
                    builder.append(permission.value()).append("\n");
                } else {
                    builder.append("<No permission> \n");
                }
            }
        }
        try {
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
