package steve.framework.extensions;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.extension.*;
import steve.framework.annotations.UserName;
import steve.framework.utils.constants.TestFrameworkConstants;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;

public class SystemPropertyExtension implements ParameterResolver, TestInstancePostProcessor {

    private static final ExtensionContext.Namespace STORE_NAMESPACE =
            ExtensionContext.Namespace.create(SystemPropertyExtension.class);

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) {
        Optional.of(FieldUtils.getFieldsListWithAnnotation(testInstance.getClass(), UserName.class).get(0))
                .ifPresent(field -> {
                            String userName = extensionContext.getStore(STORE_NAMESPACE)
                                    .getOrComputeIfAbsent(TestFrameworkConstants.USER_NAME, key ->
                                            getSystemProperty(TestFrameworkConstants.USER_NAME), String.class);
                            try {
                                field.set(testInstance, userName);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                );
    }

    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Annotation[] annotations = parameterContext.getParameter().getAnnotations();
        return annotations[0].annotationType() == UserName.class;
    }

    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(STORE_NAMESPACE)
                .getOrComputeIfAbsent(extensionContext.getUniqueId() + parameterContext.getIndex(), key -> getSystemProperty(TestFrameworkConstants.USER_NAME));
    }

    private String getSystemProperty(String key) {
        return System.getProperty(key);
    }

    public static Optional<Field> getAnnotatedField(Object object, Class type, Class<? extends Annotation> annotation) {
        return Optional.of(FieldUtils.getFieldsListWithAnnotation(object.getClass(), annotation).get(0));
    }
}
