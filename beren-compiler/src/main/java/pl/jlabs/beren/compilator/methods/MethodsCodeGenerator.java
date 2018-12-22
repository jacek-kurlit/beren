package pl.jlabs.beren.compilator.methods;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import pl.jlabs.beren.compilator.configuration.BerenConfig;
import pl.jlabs.beren.compilator.definitions.ValidationDefinition;
import pl.jlabs.beren.compilator.methods.internal.InternalMethodGenerator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MethodsCodeGenerator {
    private ProcessingEnvironment processingEnv;
    private BerenConfig berenConfig;

    public MethodsCodeGenerator(ProcessingEnvironment processingEnv, BerenConfig berenConfig) {
        this.processingEnv = processingEnv;
        this.berenConfig = berenConfig;
    }

    public List<MethodSpec> generateMethodsCode(TypeMetadata typeMetadata) {
        List<MethodSpec> methods = new ArrayList<>();

        for (ExecutableElement constructor : typeMetadata.getConstructors()) {
            if(!constructor.getModifiers().contains(Modifier.PRIVATE)) {
                methods.add(createConstructor(constructor));
            }
        }
        InternalMethodGenerator internalMethodGenerator = new InternalMethodGenerator(processingEnv, berenConfig, typeMetadata.getBreakingStrategy());
        for (ValidationDefinition validationDefinition : typeMetadata.getValidationDefinitions()) {
            methods.add(internalMethodGenerator.createValidationMethod(validationDefinition));
            methods.add(internalMethodGenerator.createInternalValidationMethod(validationDefinition, typeMetadata));
        }

        return methods;
    }

    private MethodSpec createConstructor(ExecutableElement constructor) {
        return MethodSpec.constructorBuilder()
                .addAnnotations(rewriteAnnotations(constructor.getAnnotationMirrors()))
                .addParameters(getParameters(constructor.getParameters()))
                .addStatement(createSuperConstructorCall((List<VariableElement>) constructor.getParameters()))
                .addModifiers(constructor.getModifiers())
                .build();
    }

    private List<AnnotationSpec> rewriteAnnotations(List<? extends AnnotationMirror> annotationMirrors) {
        return annotationMirrors.stream().map(annotation -> AnnotationSpec.get(annotation)).collect(toList());
    }

    private List<ParameterSpec> getParameters(List<? extends VariableElement> parameters) {
        return parameters.stream().map(param -> ParameterSpec.get(param)).collect(toList());
    }

    private CodeBlock createSuperConstructorCall(List<VariableElement> parameters) {
        String paramsStatement = parameters.stream().map(param -> param.getSimpleName().toString()).collect(Collectors.joining(","));
        return CodeBlock.builder().add("super(" + paramsStatement + ")").build();
    }
}