package org.mystore.config;


import static org.mystore.constant.CommonConstant.SWAGGER_DTO_PATH;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.nimbusds.jose.shaded.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.mystore.constant.CommonConstant;
import org.mystore.constant.HttpMethodConstant;
import org.mystore.dto.RequestWrapperDTO;
import org.apache.commons.lang3.ObjectUtils;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.springframework.util.StringUtils;

@Component
public class GenericClassOpenApiCustomizer implements OpenApiCustomizer {

    private final ApplicationContext applicationContext;

    @Autowired
    public GenericClassOpenApiCustomizer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void customise(OpenAPI openApi) {
        Map<String, Object> beansOfType = applicationContext.getBeansWithAnnotation(EndPointsClass.class);

        customizeRWDComponentsObjects(beansOfType, openApi);
        customizeComponentsInheritance(openApi);
        openApi.getPaths().keySet().removeIf(k -> k.matches("^(" + SWAGGER_DTO_PATH + ").*"));
    }

    private void customizeRWDComponentsObjects(Map<String, Object> beansOfType, OpenAPI openApi) {

        Map<String, Schema<?>> schemas = new HashMap<>();
        beansOfType.forEach((key, bean) -> {

            Set<Method> methodsWithEndPointMethodAnnotation = getMethodsWithEndPointMethodAnnotation(bean);
            EndPointsClass endPointsClass = ClassUtils.getUserClass(bean.getClass()).getAnnotation(EndPointsClass.class);

            methodsWithEndPointMethodAnnotation.forEach(method -> {
                EndPointMethod endPointMethod = method.getAnnotation(EndPointMethod.class);
                Arrays.stream(endPointMethod.path()).forEach(p -> {
                    String path = validatePath(endPointsClass.path(), p);
                    ParameterizedType argParameterizedType = getArgGenericDto(method);
                    ParameterizedType returnParameterizedType = getReturnGenericDto(method);

                    PathItem pathItem = openApi.getPaths().get(path);
                    if (pathItem != null) {
                        if (argParameterizedType != null) {
                            Schema swaggerRequestSchema = addSwaggerRequestRWDContent(endPointMethod, argParameterizedType, path, pathItem);
                            schemas.put(swaggerRequestSchema.getName(), swaggerRequestSchema);
                        }
                        if (returnParameterizedType != null) {
                            Schema swaggerResponseSchema = addSwaggerResponseRWDContent(endPointMethod, returnParameterizedType, path, pathItem);
                            schemas.put(swaggerResponseSchema.getName(), swaggerResponseSchema);
                        }
                    }
                });
            });
        });
        openApi.getComponents().getSchemas().putAll(schemas);
    }

    private Set<Method> getMethodsWithEndPointMethodAnnotation(Object bean) {
        MethodFilter methodFilter = (method) -> method.getAnnotation(EndPointMethod.class) != null;
        return MethodIntrospector.selectMethods(bean.getClass(), methodFilter);
    }

    private String validatePath(String controllerPath, String methodPath) {
        controllerPath = controllerPath.strip().replaceAll("^/*", "/")
                .replaceAll("/+$", "");

        methodPath = methodPath.strip().replaceAll("^/+", "/")
                .replaceAll("/+$", "/");
        methodPath = methodPath.matches("^(?!/).+") ? "/".concat(methodPath) : methodPath;

        return (controllerPath + methodPath).replaceAll("/{2}+", "/");

    }

    private ParameterizedType getArgGenericDto(Method method) {

        for (Parameter parameter : method.getParameters()) {
            if (parameter.getType() == RequestWrapperDTO.class) {
                return (ParameterizedType) parameter.getParameterizedType();
            }
        }
        return null;
    }

    private ParameterizedType getReturnGenericDto(Method method) {
        ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
        Class<?> actualClass = (Class<?>) parameterizedType.getRawType();
        if (actualClass == RequestWrapperDTO.class) {
            return parameterizedType;
        } else if (actualClass == ResponseEntity.class) {
            for (Type type : parameterizedType.getActualTypeArguments()) {
                if (type instanceof ParameterizedType &&
                        ((ParameterizedType) type).getRawType() == RequestWrapperDTO.class) {
                    return (ParameterizedType) type;
                }
            }
        }
        return null;
    }

    private Schema addSwaggerRequestRWDContent(EndPointMethod endPointMethod, ParameterizedType argParameterizedType, String path,
            PathItem pathItem) {
        String httpMethod = endPointMethod.httpMethod();
        String requestName = endPointMethod.requestName();
        String responseName = endPointMethod.responseName();
        String schemaName = CommonConstant.RWD_RQ_PART + requestName +
                CommonConstant.RWD_RS_PART + responseName +
                CommonConstant.RWD_PATH_PART + path +
                CommonConstant.RWD_HTTP_METHOD_PART + httpMethod +
                CommonConstant.RWD_SWAGGER_RQ_PART;

        Schema contentSchema = getSwaggerRequestBaseSchemaProperties(requestName, responseName, schemaName, argParameterizedType);
        Schema rWDSchema = new Schema().addProperty(CommonConstant.RWD, contentSchema);

        Operation operation = getOperationFromPathItem(pathItem, httpMethod);
        updateSwaggerRequestBodyIfExist(rWDSchema, operation);

        return contentSchema;
    }

    private Schema addSwaggerResponseRWDContent(EndPointMethod endPointMethod, ParameterizedType argParameterizedType, String path,
            PathItem pathItem) {
        String httpMethod = endPointMethod.httpMethod();
        String requestName = endPointMethod.requestName();
        String responseName = endPointMethod.responseName();
        String schemaName = CommonConstant.RWD_RQ_PART + requestName +
                CommonConstant.RWD_RS_PART + responseName +
                CommonConstant.RWD_PATH_PART + path +
                CommonConstant.RWD_HTTP_METHOD_PART + httpMethod +
                CommonConstant.RWD_SWAGGER_RS_PART;

        Schema contentSchema = getSwaggerResponseBaseSchemaProperties(requestName, responseName, schemaName, argParameterizedType);
        Schema rWDSchema = new Schema().addProperty(CommonConstant.RWD, contentSchema);
        Operation operation = getOperationFromPathItem(pathItem, httpMethod);
        updateSwaggerResponseBodyIfExist(rWDSchema, operation);

        return contentSchema;
    }

    private Schema getSwaggerRequestBaseSchemaProperties(String requestName, String responseName, String schemaName,
            ParameterizedType argParameterizedType) {

        Schema contentSchema = new Schema();
        contentSchema.setName(schemaName);
        contentSchema.setType(CommonConstant.OBJECT);

        Schema requestSchema = getRWDRequestSchema(requestName, argParameterizedType.getActualTypeArguments()[0]);
        Schema responseSchema = getRWDResponseSchema(responseName, Object.class);

        String rwdRequestName = getJsonKeyName(((Class<?>) argParameterizedType.getRawType()).getDeclaredFields()[0]);
        String rwdResponseName = getJsonKeyName(((Class<?>) argParameterizedType.getRawType()).getDeclaredFields()[1]);

        contentSchema.addProperty(rwdRequestName, requestSchema);
        contentSchema.addProperty(rwdResponseName, responseSchema);

        for (Field field : ((Class<?>) argParameterizedType.getRawType()).getDeclaredFields()) {
            String fieldName = getJsonKeyName(field);
            if (!(fieldName.equals(rwdRequestName) || fieldName.equals(rwdResponseName))) {
                contentSchema.addProperty(fieldName, addSchemaDetails(field.getGenericType()));
            }
        }

        return contentSchema;
    }

    private Schema getSwaggerResponseBaseSchemaProperties(String requestName, String responseName, String schemaName,
            ParameterizedType returnParameterizedType) {

        Schema contentSchema = new Schema();
        contentSchema.setName(schemaName);
        contentSchema.setType(CommonConstant.OBJECT);

        Schema requestSchema = getRWDRequestSchema(requestName, Object.class);
        Schema responseSchema = getRWDResponseSchema(responseName, returnParameterizedType.getActualTypeArguments()[1]);

        String rwdRequestName = getJsonKeyName(((Class<?>) returnParameterizedType.getRawType()).getDeclaredFields()[0]);
        String rwdResponseName = getJsonKeyName(((Class<?>) returnParameterizedType.getRawType()).getDeclaredFields()[1]);

        contentSchema.addProperty(rwdRequestName, requestSchema);
        contentSchema.addProperty(rwdResponseName, responseSchema);

        for (Field field : ((Class<?>) returnParameterizedType.getRawType()).getDeclaredFields()) {
            String fieldName = getJsonKeyName(field);
            if (!(fieldName.equals(rwdRequestName) || fieldName.equals(rwdResponseName))) {
                contentSchema.addProperty(fieldName, addSchemaDetails(field.getGenericType()));
            }
        }

        return contentSchema;
    }

    private Schema getRWDRequestSchema(String requestName, Type actualTypeArgument) {
        Schema requestSchema = new Schema();
        requestSchema.addProperty(requestName, addSchemaDetails(actualTypeArgument));
        return requestSchema;
    }

    private Schema getRWDResponseSchema(String responseName, Type actualTypeArgument) {
        Schema responseSchema = new Schema();
        responseSchema.addProperty(responseName, addSchemaDetails(actualTypeArgument));
        return responseSchema;
    }

    private Operation getOperationFromPathItem(PathItem pathItem, String httpMethod) {

        if (pathItem.getPost() != null && httpMethod.equals(HttpMethodConstant.POST)) {
            return pathItem.getPost();

        }
        if (pathItem.getGet() != null && httpMethod.equals(HttpMethodConstant.GET)) {
            return pathItem.getGet();

        }
        if (pathItem.getPut() != null && httpMethod.equals(HttpMethodConstant.PUT)) {
            return pathItem.getPut();

        }
        if (pathItem.getDelete() != null && httpMethod.equals(HttpMethodConstant.DELETE)) {
            return pathItem.getDelete();

        }
        if (pathItem.getHead() != null && httpMethod.equals(HttpMethodConstant.HEAD)) {
            return pathItem.getHead();

        }
        if (pathItem.getPatch() != null && httpMethod.equals(HttpMethodConstant.PATCH)) {
            return pathItem.getPatch();

        }
        if (pathItem.getOptions() != null && httpMethod.equals(HttpMethodConstant.OPTIONS)) {
            return pathItem.getOptions();

        }

        return null;
    }

    private void updateSwaggerRequestBodyIfExist(Schema schema, Operation operation) {
        if (operation != null &&
                operation.getRequestBody() != null &&
                operation.getRequestBody().getContent() != null) {
            operation.getRequestBody().getContent().values().forEach(content -> {
                content.setSchema(schema);
            });
        }
    }

    private void updateSwaggerResponseBodyIfExist(Schema schema, Operation operation) {
        if (operation != null &&
                operation.getResponses() != null) {
            if (operation.getResponses().get(CommonConstant.STATUS_OK) != null) {
                operation.getResponses().get(CommonConstant.STATUS_OK).getContent().values().forEach(content -> {
                    content.setSchema(schema);
                });
            } else if (operation.getResponses().get(CommonConstant.STATUS_CREATED) != null) {

                operation.getResponses().get(CommonConstant.STATUS_CREATED).getContent().values().forEach(content -> {
                    content.setSchema(schema);
                });
            }
        }
    }

    private String getJsonKeyName(Field declaredField) {
        if (declaredField.getAnnotation(SchemaProperty.class) != null) {
            return declaredField.getAnnotation(SchemaProperty.class).name();
        }

        if (declaredField.getAnnotation(JsonProperty.class) != null) {
            return declaredField.getAnnotation(JsonProperty.class).value();
        }

        if (declaredField.getAnnotation(JsonAlias.class) != null) {
            return declaredField.getAnnotation(JsonAlias.class).value()[0];
        }

        if (declaredField.getAnnotation(SerializedName.class) != null) {
            return declaredField.getAnnotation(SerializedName.class).value();
        }

        if (declaredField.getAnnotation(XmlElement.class) != null) {
            return declaredField.getAnnotation(XmlElement.class).name();
        }

        return StringUtils.capitalize(declaredField.getName());
    }

    private Schema addSchemaDetails(Object obj) {
        Schema schema = null;

        schema = initPropSchemaIfCollection(obj);
        if (schema == null) {
            schema = initPropSchemaIfMap(obj);
        }
        if (schema == null) {
            schema = initPropSchemaIfJavaWrapper(obj);
        }

        if (schema == null) {
            schema = initPropSchemaIfCustomClass(obj);
        }

        if (schema == null) {
            schema = new Schema();
        }
        return schema;
    }

    private Schema initPropSchemaIfCollection(Object obj) {
        Schema schema = null;
        if (obj instanceof ParameterizedType) {
            Class<?> clazz = (Class<?>) ((ParameterizedType) obj).getRawType();
            if (Collection.class.isAssignableFrom(clazz)) {
                schema = new Schema();
                schema.setType("array");
                ParameterizedType parameterizedType = (ParameterizedType) obj;
                for (Type type : parameterizedType.getActualTypeArguments()) {
                    if (type instanceof ParameterizedType) {
                        schema.setItems(addSchemaDetails((ParameterizedType) type));
                    } else if (type instanceof Class) {
                        schema.setItems(addSchemaDetails((Class<?>) type));
                    }
                }
            }
        }

        return schema;
    }

    private Schema initPropSchemaIfMap(Object obj) {
        Schema schema = null;
        if (obj instanceof ParameterizedType) {
            Class<?> clazz = (Class<?>) ((ParameterizedType) obj).getRawType();
            if (Map.class.isAssignableFrom(clazz)) {
                schema = new Schema();
                schema.setType(CommonConstant.OBJECT);
                ParameterizedType parameterizedType = (ParameterizedType) obj;
                Type type = parameterizedType.getActualTypeArguments()[1];
                if (type instanceof ParameterizedType) {
                    schema.setAdditionalProperties(addSchemaDetails((ParameterizedType) type));
                } else if (type instanceof Class) {
                    schema.setAdditionalProperties(addSchemaDetails((Class<?>) type));
                }
            }
        }

        return schema;
    }

    private Schema initPropSchemaIfJavaWrapper(Object obj) {

        Schema schema = null;

        if (obj instanceof Class) {
            Class<?> clazz = (Class<?>) obj;

            if (clazz.getSimpleName().equals(CommonConstant.OBJECT_CAPITALIZED)) {
                schema = getNewSchema(CommonConstant.OBJECT);
            }
            if (Void.class.isAssignableFrom(clazz)) {
                schema = getNewSchema("");
            }
            if (String.class.isAssignableFrom(clazz)) {
                schema = getNewSchema(CommonConstant.STRING);
            }

            if (Character.class.isAssignableFrom(clazz)) {
                schema = getNewSchema(CommonConstant.STRING);
            }

            if (Integer.class.isAssignableFrom(clazz)) {
                schema = getNewSchema(CommonConstant.INTEGER, CommonConstant.INT_32);
            }

            if (Long.class.isAssignableFrom(clazz)) {
                schema = getNewSchema(CommonConstant.INTEGER, CommonConstant.INT_64);
            }

            if (BigInteger.class.isAssignableFrom(clazz)) {
                schema = getNewSchema(CommonConstant.INTEGER, CommonConstant.INT_32);
            }

            if (Double.class.isAssignableFrom(clazz)) {
                schema = getNewSchema(CommonConstant.NUMBER, CommonConstant.DOUBLE);
            }

            if (BigDecimal.class.isAssignableFrom(clazz)) {
                schema = getNewSchema(CommonConstant.NUMBER);
            }

            if (Float.class.isAssignableFrom(clazz)) {
                schema = getNewSchema(CommonConstant.NUMBER, CommonConstant.FLOAT);
            }

            if (Boolean.class.isAssignableFrom(clazz)) {
                schema = getNewSchema(CommonConstant.BOOLEAN);
            }

            if (ChronoLocalDateTime.class.isAssignableFrom(clazz) || ChronoLocalDate.class.isAssignableFrom(clazz)) {
                schema = getNewSchema(CommonConstant.STRING, "date-time");
            }

            if (byte[].class.isAssignableFrom(clazz)) {
                schema = getNewSchema(CommonConstant.OBJECT);
                schema.addProperty("Short", getNewSchema(CommonConstant.INTEGER, CommonConstant.INT_32));
                schema.addProperty("Char", getNewSchema(CommonConstant.STRING));
                schema.addProperty("Int", getNewSchema(CommonConstant.INTEGER, CommonConstant.INT_32));
                schema.addProperty("Long", getNewSchema(CommonConstant.INTEGER, CommonConstant.INT_64));
                schema.addProperty("Float", getNewSchema(CommonConstant.NUMBER, CommonConstant.FLOAT));
                schema.addProperty("Double", getNewSchema(CommonConstant.NUMBER, CommonConstant.DOUBLE));
                schema.addProperty("Direct", getNewSchema(CommonConstant.BOOLEAN));
                schema.addProperty("ReadOnly", getNewSchema(CommonConstant.BOOLEAN));
            }
        }

        return schema;
    }

    private Schema initPropSchemaIfCustomClass(Object obj) {
        Schema schema = null;
        if (obj instanceof Class) {
            Class<?> clazz = (Class<?>) obj;
            schema = new Schema();

            if (clazz.getAnnotation(JsonRootName.class) != null) {
                schema.set$ref(CommonConstant.SWAGGER_COMPONENT_SCHEMA_PATH + clazz.getAnnotation(JsonRootName.class).value());
            } else if (clazz.getAnnotation(XmlRootElement.class) != null) {
                schema.set$ref(CommonConstant.SWAGGER_COMPONENT_SCHEMA_PATH + clazz.getAnnotation(XmlRootElement.class).name());
            } else {
                schema.set$ref(CommonConstant.SWAGGER_COMPONENT_SCHEMA_PATH + clazz.getSimpleName());
            }

        }
        return schema;
    }

    private Schema getNewSchema(String type) {
        Schema schema = new Schema();
        schema.setType(type);
        return schema;
    }

    private Schema getNewSchema(String type, String format) {
        Schema schema = new Schema();
        schema.setType(type);
        schema.setFormat(format);
        return schema;
    }

    private void customizeComponentsInheritance(OpenAPI openApi) {
        Set<String> inheritance = getAbstractionsComponents(openApi);
        removeAllOfFromConcreteClasses(openApi, inheritance);
    }

    private Set<String> getAbstractionsComponents(OpenAPI openApi) {
        Set<String> inheritance = new HashSet<>();
        openApi.getComponents().getSchemas().values().forEach(schema -> {
            if (isAbstractionSchema(schema)) {
                inheritance.add(schema.getName());
            }
        });
        return inheritance;
    }

    private boolean isAbstractionSchema(Schema schema) {
        return ObjectUtils.isNotEmpty(schema.getOneOf()) ||
                ObjectUtils.isNotEmpty(schema.getAnyOf());
    }

    private void removeAllOfFromConcreteClasses(OpenAPI openApi, Set<String> inheritance) {
        openApi.getComponents().getSchemas().values().forEach(schema -> {
            if (isConcreteSchema(schema)) {
                removeRefFromConcreteClass(schema, inheritance);
            }
        });

    }

    private boolean isConcreteSchema(Schema schema) {
        return ObjectUtils.isNotEmpty(schema.getAllOf()) &&
                schema.getAllOf().size() > 0;
    }

    private void removeRefFromConcreteClass(Schema schema, Set<String> inheritance) {

        schema.setAllOf((List<Schema>) schema.getAllOf().stream()
                .filter(property -> isPropertyAbstract((Schema) property, inheritance))
                .collect(Collectors.toList()));

    }


    private boolean isPropertyAbstract(Schema schema, Set<String> inheritance) {
        if (schema.get$ref() == null) {
            return true;
        }

        return !inheritance.contains(
                schema.get$ref().substring(schema.get$ref().lastIndexOf("/") + 1)
        );
    }

}