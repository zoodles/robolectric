package com.xtremelabs.robolectric;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;

import android.app.Activity;
import android.app.Application;
import android.widget.TextView;

import com.xtremelabs.robolectric.annotation.Values;
import com.xtremelabs.robolectric.res.ResourceLoader;

@RunWith(RobolectricTestRunnerTest.RunnerForTesting.class)
public class RobolectricTestRunnerTest {
	
    @Test
    public void shouldInitializeAndBindApplicationButNotCallOnCreate() throws Exception {
        assertNotNull(Robolectric.application);
        assertEquals(MyTestApplication.class, Robolectric.application.getClass());
        assertFalse(((MyTestApplication) Robolectric.application).onCreateWasCalled);
        assertNotNull(shadowOf(Robolectric.application).getResourceLoader());
    }

    @Test
    public void setStaticValue_shouldIgnoreFinalModifier() {
        RobolectricTestRunner.setStaticValue(android.os.Build.class, "MODEL", "expected value");

        assertEquals("expected value", android.os.Build.MODEL);
    }
    
    @Test
    @Values( qualifiers="fr")
    public void internalBeforeTest_testValuesResQualifiers() {
    	assertEquals( RunnerForTesting.instance.robolectricConfig.getValuesResQualifiers(), "fr" );
    	ResourceLoader rl = shadowOf(Robolectric.application).getResourceLoader();
    	assertEquals( "Bonjour", rl.getStringValue(R.string.hello ));
    }
    
    @Test
    public void internalBeforeTest_testValuesResQualifiersNotSet() {
    	assertEquals( RunnerForTesting.instance.robolectricConfig.getValuesResQualifiers(), "" );
    	ResourceLoader rl = shadowOf(Robolectric.application).getResourceLoader();
    	assertEquals( "Hello", rl.getStringValue(R.string.hello ));
    }
    
    public static class RunnerForTesting extends WithTestDefaultsRunner {
    	public static RunnerForTesting instance;
 
        public RunnerForTesting(Class<?> testClass) throws InitializationError {
            super(testClass);
        	instance = this;
        }

        @Override protected Application createApplication() {
            return new MyTestApplication();
        }
    }

    public static class MyTestApplication extends Application {
        private boolean onCreateWasCalled;

        @Override public void onCreate() {
            this.onCreateWasCalled = true;
        }
    }
    
    public static class MyTestActivity extends Activity {
    	
    }

}
