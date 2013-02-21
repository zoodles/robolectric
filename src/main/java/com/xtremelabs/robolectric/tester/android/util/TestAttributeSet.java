package com.xtremelabs.robolectric.tester.android.util;

import android.util.AttributeSet;
import android.view.View;
import com.xtremelabs.robolectric.res.AttrResourceLoader;
import com.xtremelabs.robolectric.res.ResourceExtractor;

import java.util.HashMap;
import java.util.Map;

public class TestAttributeSet implements AttributeSet {
    Map<String, String> attributes = new HashMap<String, String>();
    private ResourceExtractor resourceExtractor;
    private AttrResourceLoader attrResourceLoader;
    private Class<? extends View> viewClass;
    private boolean isSystem = false;

    public TestAttributeSet(Map<String, String> attributes, ResourceExtractor resourceExtractor,
                            AttrResourceLoader attrResourceLoader, Class<? extends View> viewClass, boolean isSystem) {
        this.attributes = attributes;
        this.resourceExtractor = resourceExtractor;
        this.attrResourceLoader = attrResourceLoader;
        this.viewClass = viewClass;
        this.isSystem = isSystem;
    }

    public TestAttributeSet() {
        this(new HashMap<String, String>());
    }

    public TestAttributeSet(Map<String, String> attributes) {
        this(attributes, new ResourceExtractor());
    }

    public TestAttributeSet(Map<String, String> attributes, ResourceExtractor resourceExtractor) {
        this(attributes, resourceExtractor, new AttrResourceLoader(resourceExtractor), null, false);
    }

    public TestAttributeSet(Map<String, String> attributes, Class<?> rFileClass) throws Exception {
        this(attributes, new ResourceExtractor());
        this.resourceExtractor.addLocalRClass(rFileClass);
    }

    public TestAttributeSet put(String name, String value) {
        attributes.put(name, value);
        return this;
    }

    @Override
    public boolean getAttributeBooleanValue(String namespace, String attribute, boolean defaultValue) {
        String value = getAttributeValueInMap(namespace, attribute);
        return (value != null) ? Boolean.valueOf(value) : defaultValue;
    }

    @Override
    public String getAttributeValue(String namespace, String attribute) {
        return getAttributeValueInMap(namespace, attribute);
    }

    @Override
    public int getAttributeIntValue(String namespace, String attribute, int defaultValue) {
        String value = getAttributeValueInMap(namespace, attribute);

        if (attrResourceLoader.hasAttributeFor(viewClass, "xxx", attribute)) {
            value = attrResourceLoader.convertValueToEnum(viewClass, "xxx", attribute, value);
        }

        return (value != null) ? Integer.valueOf(value) : defaultValue;
    }

    @Override
    public int getAttributeCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAttributeName(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAttributeValue(int resourceId) {
        String qualifiedResourceName = resourceExtractor.getResourceName(resourceId);
        if (qualifiedResourceName != null) {
            String resourceName = qualifiedResourceName.substring(qualifiedResourceName.indexOf('/') + 1);
            return getAttributeValueInMap(null, resourceName);
        } else {
            return null;
        }
    }

    @Override
    public String getPositionDescription() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAttributeNameResource(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAttributeListValue(String namespace, String attribute, String[] options, int defaultValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAttributeUnsignedIntValue(String namespace, String attribute, int defaultValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getAttributeFloatValue(String namespace, String attribute, float defaultValue) {
        String value = getAttributeValueInMap(namespace, attribute);

        if (attrResourceLoader.hasAttributeFor(viewClass, "xxx", attribute)) {
            value = attrResourceLoader.convertValueToEnum(viewClass, "xxx", attribute, value);
        }

        return (value != null) ? Float.valueOf(value) : defaultValue;
    }

    @Override
    public int getAttributeListValue(int index, String[] options, int defaultValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getAttributeBooleanValue(int resourceId, boolean defaultValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAttributeResourceValue(String namespace, String attribute, int defaultValue) {
        String value = getAttributeValueInMap(namespace, attribute);
        Integer resourceId = defaultValue;
        if (value != null) {
            resourceId = resourceExtractor.getResourceId(value);
        }
        return resourceId == null ? defaultValue : resourceId;
    }

    @Override
    public int getAttributeResourceValue(int resourceId, int defaultValue) {
        String attrName = resourceExtractor.getResourceName(resourceId);
        String value = getAttributeValueInMap(null, attrName);
        return (value == null) ? defaultValue : resourceExtractor.getResourceId(value);
    }

    @Override
    public int getAttributeIntValue(int index, int defaultValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAttributeUnsignedIntValue(int index, int defaultValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getAttributeFloatValue(int index, float defaultValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getIdAttribute() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getClassAttribute() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getIdAttributeResourceValue(int defaultValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStyleAttribute() {
        throw new UnsupportedOperationException();
    }

    private String getAttributeValueInMap(String namespace, String attribute) {
        String value = null;
        for (String key : attributes.keySet()) {
            String[] mappedKeys = {null, key};
            if (key.contains(":")) {
                mappedKeys = key.split(":");
            }

            if (mappedKeys[1].equals(attribute) && (
                    namespace == null || !namespace.equals("android") ||
                            (namespace.equals("android") && namespace.equals(mappedKeys[0])))) {
                value = attributes.get(key);
                break;
            }
        }
        if (value != null && isSystem && value.startsWith("@+id")) {
            value = value.replace("@+id", "@+android:id");
        }
        return value;
    }
}
