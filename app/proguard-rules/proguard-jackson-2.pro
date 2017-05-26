# Proguard configuration for Jackson 2.x (fasterxml package instead of codehaus package)

-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}

# "can't find referenced class org.w3c.dom.bootstrap.DOMImplementationRegistry" when building with OpenJDK (api23)
-dontwarn com.fasterxml.jackson.databind.ext.DOMSerializer

# "can't find referenced class java.beans.Transient" and
# "can't find referenced class java.beans.ConstructorProperties"
# when building with OpenJDK (api23)
-dontwarn com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector$*

-keepattributes Signature
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-keepattributes *Annotation*,EnclosingMethod