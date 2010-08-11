package com.android.server;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.RemoteException;

public class PackageManagerService extends IPackageManager.Stub {
    @Override
    public PackageInfo getPackageInfo(String s, int i) throws RemoteException {
        return null;
    }

    @Override
    public int getPackageUid(String s) throws RemoteException {
        return 0;
    }

    @Override
    public int[] getPackageGids(String s) throws RemoteException {
        return new int[0];
    }

    @Override
    public PermissionInfo getPermissionInfo(String s, int i) throws RemoteException {
        return null;
    }

    @Override
    public List<PermissionInfo> queryPermissionsByGroup(String s, int i) throws RemoteException {
        return null;
    }

    @Override
    public PermissionGroupInfo getPermissionGroupInfo(String s, int i) throws RemoteException {
        return null;
    }

    @Override
    public List<PermissionGroupInfo> getAllPermissionGroups(int i) throws RemoteException {
        return null;
    }

    @Override
    public ApplicationInfo getApplicationInfo(String s, int i) throws RemoteException {
        return null;
    }

    @Override
    public ActivityInfo getActivityInfo(ComponentName componentName, int i) throws RemoteException {
        return null;
    }

    @Override
    public ActivityInfo getReceiverInfo(ComponentName componentName, int i) throws RemoteException {
        return null;
    }

    @Override
    public ServiceInfo getServiceInfo(ComponentName componentName, int i) throws RemoteException {
        return null;
    }

    @Override
    public int checkPermission(String s, String s1) throws RemoteException {
        return 0;
    }

    @Override
    public int checkUidPermission(String s, int i) throws RemoteException {
        return 0;
    }

    @Override
    public boolean addPermission(PermissionInfo permissionInfo) throws RemoteException {
        return false;
    }

    @Override
    public void removePermission(String s) throws RemoteException {
    }

    @Override
    public boolean isProtectedBroadcast(String s) throws RemoteException {
        return false;
    }

    @Override
    public int checkSignatures(String s, String s1) throws RemoteException {
        return 0;
    }

    @Override
    public int checkUidSignatures(int i, int i1) throws RemoteException {
        return 0;
    }

    @Override
    public String[] getPackagesForUid(int i) throws RemoteException {
        return new String[0];
    }

    @Override
    public String getNameForUid(int i) throws RemoteException {
        return null;
    }

    @Override
    public int getUidForSharedUser(String s) throws RemoteException {
        return 0;
    }

    @Override
    public ResolveInfo resolveIntent(Intent intent, String s, int i) throws RemoteException {
        return null;
    }

    @Override
    public List<ResolveInfo> queryIntentActivities(Intent intent, String s, int i) throws RemoteException {
        return null;
    }

    @Override
    public List<ResolveInfo> queryIntentActivityOptions(ComponentName componentName, Intent[] intents, String[] strings, Intent intent, String s, int i) throws RemoteException {
        return null;
    }

    @Override
    public List<ResolveInfo> queryIntentReceivers(Intent intent, String s, int i) throws RemoteException {
        return null;
    }

    @Override
    public ResolveInfo resolveService(Intent intent, String s, int i) throws RemoteException {
        return null;
    }

    @Override
    public List<ResolveInfo> queryIntentServices(Intent intent, String s, int i) throws RemoteException {
        return new ArrayList<ResolveInfo>();
    }

    @Override
    public List<PackageInfo> getInstalledPackages(int i) throws RemoteException {
        return null;
    }

    @Override
    public List<ApplicationInfo> getInstalledApplications(int i) throws RemoteException {
        return null;
    }

    @Override
    public List<ApplicationInfo> getPersistentApplications(int i) throws RemoteException {
        return null;
    }

    @Override
    public ProviderInfo resolveContentProvider(String s, int i) throws RemoteException {
        return null;
    }

    @Override
    public void querySyncProviders(List<String> strings, List<ProviderInfo> providerInfos) throws RemoteException {
    }

    @Override
    public List<ProviderInfo> queryContentProviders(String s, int i, int i1) throws RemoteException {
        return null;
    }

    @Override
    public InstrumentationInfo getInstrumentationInfo(ComponentName componentName, int i) throws RemoteException {
        return null;
    }

    @Override
    public List<InstrumentationInfo> queryInstrumentation(String s, int i) throws RemoteException {
        return null;
    }

    @Override
    public void installPackage(Uri uri, IPackageInstallObserver iPackageInstallObserver, int i, String s) throws RemoteException {
    }

    @Override
    public void deletePackage(String s, IPackageDeleteObserver iPackageDeleteObserver, int i) throws RemoteException {
    }

    @Override
    public String getInstallerPackageName(String s) throws RemoteException {
        return null;
    }

    @Override
    public void addPackageToPreferred(String s) throws RemoteException {
    }

    @Override
    public void removePackageFromPreferred(String s) throws RemoteException {
    }

    @Override
    public List<PackageInfo> getPreferredPackages(int i) throws RemoteException {
        return null;
    }

    @Override
    public void addPreferredActivity(IntentFilter intentFilter, int i, ComponentName[] componentNames, ComponentName componentName) throws RemoteException {
    }

    @Override
    public void replacePreferredActivity(IntentFilter intentFilter, int i, ComponentName[] componentNames, ComponentName componentName) throws RemoteException {
    }

    @Override
    public void clearPackagePreferredActivities(String s) throws RemoteException {
    }

    @Override
    public int getPreferredActivities(List<IntentFilter> intentFilters, List<ComponentName> componentNames, String s) throws RemoteException {
        return 0;
    }

    @Override
    public void setComponentEnabledSetting(ComponentName componentName, int i, int i1) throws RemoteException {
    }

    @Override
    public int getComponentEnabledSetting(ComponentName componentName) throws RemoteException {
        return 0;
    }

    @Override
    public void setApplicationEnabledSetting(String s, int i, int i1) throws RemoteException {
    }

    @Override
    public int getApplicationEnabledSetting(String s) throws RemoteException {
        return 0;
    }

    @Override
    public void freeStorageAndNotify(long l, IPackageDataObserver iPackageDataObserver) throws RemoteException {
    }

    @Override
    public void freeStorage(long l, IntentSender intentSender) throws RemoteException {
    }

    @Override
    public void deleteApplicationCacheFiles(String s, IPackageDataObserver iPackageDataObserver) throws RemoteException {
    }

    @Override
    public void clearApplicationUserData(String s, IPackageDataObserver iPackageDataObserver) throws RemoteException {
    }

    @Override
    public void getPackageSizeInfo(String s, IPackageStatsObserver iPackageStatsObserver) throws RemoteException {
    }

    @Override
    public String[] getSystemSharedLibraryNames() throws RemoteException {
        return new String[0];
    }

    @Override
    public FeatureInfo[] getSystemAvailableFeatures() throws RemoteException {
        return new FeatureInfo[0];
    }

    @Override
    public boolean hasSystemFeature(String s) throws RemoteException {
        return false;
    }

    @Override
    public void enterSafeMode() throws RemoteException {
    }

    @Override
    public boolean isSafeMode() throws RemoteException {
        return false;
    }

    @Override
    public void systemReady() throws RemoteException {
    }

    @Override
    public boolean hasSystemUidErrors() throws RemoteException {
        return false;
    }

    @Override
    public boolean performDexOpt(String s) throws RemoteException {
        return false;
    }
}
