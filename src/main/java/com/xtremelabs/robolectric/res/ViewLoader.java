package com.xtremelabs.robolectric.res;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import com.xtremelabs.robolectric.tester.android.util.TestAttributeSet;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;

public class ViewLoader extends XmlLoader {
    protected Map<String, ViewNode> viewNodesByLayoutName = new HashMap<String, ViewNode>();
    private AttrResourceLoader attrResourceLoader;
    private List<String> qualifierSearchPath = new ArrayList<String>();

    public ViewLoader(ResourceExtractor resourceExtractor, AttrResourceLoader attrResourceLoader) {
        super(resourceExtractor);
        this.attrResourceLoader = attrResourceLoader;
    }

    @Override
    protected void processResourceXml(File xmlFile, Document document, boolean isSystem) throws Exception {
        ViewNode topLevelNode = new ViewNode("top-level", new HashMap<String, String>(), isSystem);
        processChildren(document.getChildNodes(), topLevelNode);
        String layoutName = xmlFile.getParentFile().getName() + "/" + xmlFile.getName().replace(".xml", "");
        if (isSystem) {
            layoutName = "android:" + layoutName;
        }
        viewNodesByLayoutName.put(layoutName, topLevelNode.getChildren().get(0));
    }

    private void processChildren(NodeList childNodes, ViewNode parent) {
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            processNode(node, parent);
        }
    }

    private void processNode(Node node, ViewNode parent) {
        String name = node.getNodeName();
        NamedNodeMap attributes = node.getAttributes();
        Map<String, String> attrMap = new HashMap<String, String>();
        if (attributes != null) {
            int length = attributes.getLength();
            for (int i = 0; i < length; i++) {
                Node attr = attributes.item(i);
                attrMap.put(attr.getNodeName(), attr.getNodeValue());
            }
        }

        if (name.equals("requestFocus")) {
            parent.attributes.put("android:focus", "true");
            parent.requestFocusOverride = true;
        } else if (!name.startsWith("#")) {
            ViewNode viewNode = new ViewNode(name, attrMap, parent.isSystem);
            if (parent != null) parent.addChild(viewNode);

            processChildren(node.getChildNodes(), viewNode);
        }
    }

    public View inflateView(Context context, String key) {
        return inflateView(context, key, null);
    }

    public View inflateView(Context context, String key, View parent) {
        return inflateView(context, key, null, parent);
    }

    public View inflateView(Context context, int resourceId, View parent) {
        return inflateView(context, resourceExtractor.getResourceName(resourceId), parent);
    }

    private View inflateView(Context context, String layoutName, Map<String, String> attributes, View parent) {
        ViewNode viewNode = getViewNodeByLayoutName(layoutName);
        if (viewNode == null) {
            throw new RuntimeException("Could not find layout " + layoutName);
        }
        try {
            if (attributes != null) {
                for (Map.Entry<String, String> entry : attributes.entrySet()) {
                    if (!entry.getKey().equals("layout")) {
                        viewNode.attributes.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            return viewNode.inflate(context, parent);
        } catch (Exception e) {
            throw new RuntimeException("error inflating " + layoutName, e);
        }
    }

    private ViewNode getViewNodeByLayoutName(String layoutName) {
        if (layoutName.startsWith("layout/") && !qualifierSearchPath.isEmpty()) {
            String rawLayoutName = layoutName.substring("layout/".length());
            for (String location : qualifierSearchPath) {
                ViewNode foundNode = viewNodesByLayoutName.get("layout-" + location + "/" + rawLayoutName);
                if (foundNode != null) {
                    return foundNode;
                }
            }
        }
        return viewNodesByLayoutName.get(layoutName);
    }

    public void setLayoutQualifierSearchPath(String... locations) {
        qualifierSearchPath = Arrays.asList(locations);
    }

    public class ViewNode {
        private String name;
        private final Map<String, String> attributes;

        private List<ViewNode> children = new ArrayList<ViewNode>();
        boolean requestFocusOverride = false;
        boolean isSystem = false;

        public ViewNode(String name, Map<String, String> attributes, boolean isSystem) {
            this.name = name;
            this.attributes = attributes;
            this.isSystem = isSystem;
        }

        public List<ViewNode> getChildren() {
            return children;
        }

        public void addChild(ViewNode viewNode) {
            children.add(viewNode);
        }

        public View inflate(Context context, View parent) throws Exception {
            View view = create(context, (ViewGroup) parent);

            for (ViewNode child : children) {
                child.inflate(context, view);
            }

            invokeOnFinishInflate(view);
            return view;
        }

        private void invokeOnFinishInflate(View view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            Method onFinishInflate = View.class.getDeclaredMethod("onFinishInflate");
            onFinishInflate.setAccessible(true);
            onFinishInflate.invoke(view);
        }

        private View create(Context context, ViewGroup parent) throws Exception {
            if (name.equals("include")) {
                String layout = attributes.get("layout");
                View view = inflateView(context, layout.substring(1), attributes, parent);
                return view;
            } else if (name.equals("merge")) {
                return parent;
            } else if (name.equals("fragment")) {
                View fragment = constructFragment(context);
                addToParent(parent, fragment);
                return fragment;
            } else {
                applyFocusOverride(parent);
                View view = constructView(context);
                addToParent(parent, view);
                shadowOf(view).applyFocus();
                return view;
            }
        }

        private FrameLayout constructFragment(Context context) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            TestAttributeSet attributeSet = new TestAttributeSet(attributes, resourceExtractor, attrResourceLoader, View.class, isSystem);

            Class<? extends Fragment> clazz = loadFragmentClass(attributes.get("android:name"));
            Fragment fragment = ((Constructor<? extends Fragment>) clazz.getConstructor()).newInstance();
            if (!(context instanceof FragmentActivity)) {
                throw new RuntimeException("Cannot inflate a fragment unless the activity is a FragmentActivity");
            }

            FragmentActivity activity = (FragmentActivity) context;

            String tag = attributeSet.getAttributeValue("android", "tag");
            int id = attributeSet.getAttributeResourceValue("android", "id", 0);
            // TODO: this should probably be changed to call TestFragmentManager.addFragment so that the
            // inflated fragments don't get started twice (once in the commit, and once in ShadowFragmentActivity's
            // onStart()
            activity.getSupportFragmentManager().beginTransaction().add(id, fragment, tag).commit();

            View view = fragment.getView();

            FrameLayout container = new FrameLayout(context);
            container.setId(id);
            container.addView(view);
            return container;
        }

        private void addToParent(ViewGroup parent, View view) {
            if (parent != null && parent != view) {
                parent.addView(view);
            }
        }

        private View constructView(Context context) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            Class<? extends View> clazz = pickViewClass();
            try {
                TestAttributeSet attributeSet = new TestAttributeSet(attributes, resourceExtractor, attrResourceLoader, clazz, isSystem);
                return ((Constructor<? extends View>) clazz.getConstructor(Context.class, AttributeSet.class)).newInstance(context, attributeSet);
            } catch (NoSuchMethodException e) {
                try {
                    return ((Constructor<? extends View>) clazz.getConstructor(Context.class)).newInstance(context);
                } catch (NoSuchMethodException e1) {
                    return ((Constructor<? extends View>) clazz.getConstructor(Context.class, String.class)).newInstance(context, "");
                }
            }
        }

        private Class<? extends View> pickViewClass() {
            Class<? extends View> clazz = loadViewClass(name);
            if (clazz == null) {
                clazz = loadViewClass("android.view." + name);
            }
            if (clazz == null) {
                clazz = loadViewClass("android.widget." + name);
            }
            if (clazz == null) {
                clazz = loadViewClass("android.webkit." + name);
            }
            if (clazz == null) {
                clazz = loadViewClass("com.google.android.maps." + name);
            }

            if (clazz == null) {
                throw new RuntimeException("couldn't find view class " + name);
            }
            return clazz;
        }

        private Class loadClass(String className) {
            try {
                return getClass().getClassLoader().loadClass(className);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        private Class<? extends View> loadViewClass(String className) {
            // noinspection unchecked
            return (Class<? extends View>) loadClass(className);
        }

        private Class<? extends Fragment> loadFragmentClass(String className) {
            // noinspection unchecked
            return (Class<? extends Fragment>) loadClass(className);
        }

        public void applyFocusOverride(ViewParent parent) {
            if (requestFocusOverride) {
                View ancestor = (View) parent;
                while (ancestor.getParent() != null) {
                    ancestor = (View) ancestor.getParent();
                }
                ancestor.clearFocus();
            }
        }
    }
}
