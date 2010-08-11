package android.app;

import java.lang.reflect.Field;

import android.view.Display;

@SuppressWarnings({"UnusedDeclaration"})
public class FakeDisplay {
    private Display realDisplay;

    public FakeDisplay(Display realDisplay) {
        this.realDisplay = realDisplay;
    }

    public void init(int display) {
        System.out.println("realDisplay = " + realDisplay);
        if (realDisplay.getClass() == Display.class) {
            setField("mDensity", 160f);
            setField("mDpiX", 160f);
            setField("mDpiY", 160f);
        }
    }

    private void setField(String name, Object value) {
        try {
            Field field = realDisplay.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(realDisplay, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
