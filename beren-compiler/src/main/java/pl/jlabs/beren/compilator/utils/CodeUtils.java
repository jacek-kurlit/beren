package pl.jlabs.beren.compilator.utils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class CodeUtils {
    private static final String GET_PREFIX = "get";
    private static final String IS_PREFIX = "is";

    private static final String INTERNAL_METHOD_PREFIX = "internal_";

    public static final String VALIDATION_RESULTS_PARAM = "validationResults";
    public static final String THIS_PARAM = "this";

    public static final String createInternalMethodName(String methodName) {
        return INTERNAL_METHOD_PREFIX + methodName;
    }

    public static VariableElement getValidationMethodParam(ExecutableElement methodToImplement) {
        //validation methods must have one param!
        return methodToImplement.getParameters().get(0);
    }

    public static boolean isGetterMethod(ExecutableElement method) {
        String methodName = method.getSimpleName().toString();
        return methodName.startsWith(GET_PREFIX) || methodName.startsWith(IS_PREFIX);
    }

    public static String normalizeGetterName(String getterName) {
        if(getterName.startsWith(GET_PREFIX)) {
            return getterName.substring(3).toLowerCase();
        }

        return getterName.substring(2).toLowerCase();
    }
}