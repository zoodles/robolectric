package android.app;

import java.io.File;
import java.lang.reflect.Field;

import com.twitter.android.FakeResources;
import com.twitter.android.FastAndroidTestRunner;
import com.twitter.android.R;

import android.accounts.AccountManager;
import android.accounts.IAccountManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.Display;
import com.android.internal.os.BinderInternal;
import com.xtremelabs.droidsugar.view.FakeActivity;
import com.xtremelabs.droidsugar.view.ViewLoader;

public class Helper {
    public static void bootstrapApp(Activity activity) {
        FastAndroidTestRunner.addProxy(AssetManager.class, FakeAssetManager.class);
        FastAndroidTestRunner.addProxy(Display.class, FakeDisplay.class);
        FastAndroidTestRunner.addProxy(BinderInternal.class, FakeBinderInternal.class);
        FastAndroidTestRunner.addProxy(Resources.class, FakeResources.class);

        initViewLoader();

        ActivityThread activityThread = ActivityThread.systemMain();
        Application application = activityThread.mInitialApplication;
        ApplicationContext appContext = activityThread.getSystemContext();

        IAccountManager.Stub accountManagerService = new MyAccountManagerService();
        setAccountManager(appContext, accountManagerService);
//        Application application = new Application();
//        ApplicationContext appContext = new ApplicationContext() {
//            @Override
//            public Resources getResources() {
//                return super.getResources();
//            }
//        };

        ApplicationInfo applicationInfo = application.getApplicationInfo();
        applicationInfo.sourceDir = "/Users/pivotal/workspace/Twitter";
        applicationInfo.publicSourceDir = "/publicSourceDir";

        Thread mainThread = new Thread() {
            @Override
            public void run() {
                ActivityThread.main(new String[0]);
            }
        };
//        mainThread.start();

//        ActivityThread.PackageInfo packageInfo = new ActivityThread.PackageInfo(
//                activityThread, applicationInfo,
//                mainThread, Helper.class.getClassLoader(),
//                false, false);

//        appContext.init(packageInfo, null, activityThread);
//        appContext.setOuterContext(activity);

//        ActivityThread.sPackageManager = new PackageManagerService();
//        HashMap<String, IBinder> map = new HashMap<String, IBinder>();
//        map.put("xxx", null);
//        ServiceManager.initServiceCache(map);
//        FakeBinderInternal.init(appContext);

        Intent intent = new Intent();
        intent.setComponent(new ComponentName("component.name.package", "ComponentNameClass"));

        activity.attach(appContext, activityThread, new Instrumentation(), null,
                application, intent, new ActivityInfo(), "title",
                null, "id", null, new Configuration());
        activity.performStart();
    }

    private static void setAccountManager(ApplicationContext appContext, IAccountManager.Stub accountManagerService) {
        try {
            Field mAccountManagerField = appContext.getClass().getDeclaredField("mAccountManager");
            mAccountManagerField.setAccessible(true);
            mAccountManagerField.set(appContext, new AccountManager(appContext, accountManagerService));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initViewLoader() {
        if (FakeActivity.viewLoader == null) {
            try {
                File sysDir = new File("/Volumes/AndroidSource/frameworks/base/core/res/res/layout");
                File appDir = new File("res/layout");

                FakeActivity.viewLoader = new ViewLoader();
                FakeActivity.viewLoader.addRClass(com.android.internal.R.class);
                FakeActivity.viewLoader.addRClass(R.class);
                FakeActivity.viewLoader.addXmlDir(sysDir);
                FakeActivity.viewLoader.addXmlDir(appDir);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
