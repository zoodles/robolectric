package com.xtremelabs.droidsugar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import android.content.res.XmlBlock;
import android.content.res.XmlResourceParser;
import com.xtremelabs.droidsugar.view.FakeActivity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

@SuppressWarnings({"UnusedDeclaration"})
public class FakeResources {
    public XmlResourceParser loadXmlResourceParser(int id, String type) {
        String layoutName = FakeActivity.viewLoader.getNameById(id);
        File file = new File("/Volumes/AndroidSource/frameworks/base/core/res/res/" + layoutName + ".xml");
        return makeXmlPullParser(file);
    }


    private static XmlResourceParser makeXmlPullParser(File file) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance(
                    System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new FileReader(file));
            return new XmlBlock(null).new Parser(xpp);
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
