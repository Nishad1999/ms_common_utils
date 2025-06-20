package org.mystore.context;

import lombok.NoArgsConstructor;
 import org.mystore.dto.ContextMetadata;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ContextMetadataHolder {

    private static final ThreadLocal<ContextMetadata> CONTEXT = new ThreadLocal<>();

    public static ContextMetadata getContext() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static void initialize() {
        CONTEXT.set(new ContextMetadata());
    }

    public static void setContext(ContextMetadata context) {
        CONTEXT.set(context);
    }
}
