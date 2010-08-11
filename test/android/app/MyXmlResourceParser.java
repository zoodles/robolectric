package android.app;

import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.wrapper.classic.XmlPullParserDelegate;

public class MyXmlResourceParser extends XmlPullParserDelegate implements XmlResourceParser {
    private AttributeSet attributeSet;

    public MyXmlResourceParser(XmlPullParser xpp) {
        super(xpp);
        attributeSet = Xml.asAttributeSet(xpp);
    }

    @Override
    public void close() {
    }

    @Override
    public int getAttributeCount() {
        return attributeSet.getAttributeCount();
    }

    @Override
    public String getAttributeName(int index) {
        return attributeSet.getAttributeName(index);
    }

    @Override
    public String getAttributeValue(int index) {
        return attributeSet.getAttributeValue(index);
    }

    @Override
    public String getAttributeValue(String namespace, String name) {
        return attributeSet.getAttributeValue(namespace, name);
    }

    @Override
    public String getPositionDescription() {
        return attributeSet.getPositionDescription();
    }

    @Override
    public int getAttributeNameResource(int index) {
        return attributeSet.getAttributeNameResource(index);
    }

    @Override
    public int getAttributeListValue(String namespace, String attribute, String[] options, int defaultValue) {
        return attributeSet.getAttributeListValue(namespace, attribute, options, defaultValue);
    }

    @Override
    public boolean getAttributeBooleanValue(String namespace, String attribute, boolean defaultValue) {
        return attributeSet.getAttributeBooleanValue(namespace, attribute, defaultValue);
    }

    @Override
    public int getAttributeResourceValue(String namespace, String attribute, int defaultValue) {
        return attributeSet.getAttributeResourceValue(namespace, attribute, defaultValue);
    }

    @Override
    public int getAttributeIntValue(String namespace, String attribute, int defaultValue) {
        return attributeSet.getAttributeIntValue(namespace, attribute, defaultValue);
    }

    @Override
    public int getAttributeUnsignedIntValue(String namespace, String attribute, int defaultValue) {
        return attributeSet.getAttributeUnsignedIntValue(namespace, attribute, defaultValue);
    }

    @Override
    public float getAttributeFloatValue(String namespace, String attribute, float defaultValue) {
        return attributeSet.getAttributeFloatValue(namespace, attribute, defaultValue);
    }

    @Override
    public int getAttributeListValue(int index, String[] options, int defaultValue) {
        return attributeSet.getAttributeListValue(index, options, defaultValue);
    }

    @Override
    public boolean getAttributeBooleanValue(int index, boolean defaultValue) {
        return attributeSet.getAttributeBooleanValue(index, defaultValue);
    }

    @Override
    public int getAttributeResourceValue(int index, int defaultValue) {
        return attributeSet.getAttributeResourceValue(index, defaultValue);
    }

    @Override
    public int getAttributeIntValue(int index, int defaultValue) {
        return attributeSet.getAttributeIntValue(index, defaultValue);
    }

    @Override
    public int getAttributeUnsignedIntValue(int index, int defaultValue) {
        return attributeSet.getAttributeUnsignedIntValue(index, defaultValue);
    }

    @Override
    public float getAttributeFloatValue(int index, float defaultValue) {
        return attributeSet.getAttributeFloatValue(index, defaultValue);
    }

    @Override
    public String getIdAttribute() {
        return attributeSet.getIdAttribute();
    }

    @Override
    public String getClassAttribute() {
        return attributeSet.getClassAttribute();
    }

    @Override
    public int getIdAttributeResourceValue(int defaultValue) {
        return attributeSet.getIdAttributeResourceValue(defaultValue);
    }

    @Override
    public int getStyleAttribute() {
        return attributeSet.getStyleAttribute();
    }
}
