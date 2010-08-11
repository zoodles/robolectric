package android.app;

import java.util.HashMap;
import java.util.Map;

import android.accounts.AccountManagerService;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.IPermissionController;
import android.os.IServiceManager;
import android.os.RemoteException;

@SuppressWarnings({"UnusedDeclaration"})
public class FakeBinderInternal {
    private static Map<String, IBinder> serviceMap = new HashMap<String, IBinder>();

    public static void init(ApplicationContext applicationContext) {
        serviceMap.put(Context.ACCOUNT_SERVICE, new AccountManagerService(applicationContext));
    }

    public static IBinder getContextObject() {
        return new IServiceManager() {
            @Override
            public IBinder getService(String name) throws RemoteException {
                System.out.println("getSerivce " + name);
                return serviceMap.get(name);
            }

            @Override
            public IBinder checkService(String name) throws RemoteException {
                System.out.println("checkService " + name);
                return null;
            }

            @Override
            public void addService(String name, IBinder service) throws RemoteException {
                System.out.println("addService " + name);
            }

            @Override
            public String[] listServices() throws RemoteException {
                System.out.println("listServices");
                return new String[0];
            }

            @Override
            public void setPermissionController(IPermissionController controller) throws RemoteException {
            }

            @Override
            public IBinder asBinder() {
                Binder binder = new Binder();
                binder.attachInterface(this, IServiceManager.class.getName());
                return binder;
            }
        }.asBinder();
    }
}
