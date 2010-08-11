package android.app;

import android.accounts.Account;
import android.accounts.AuthenticatorDescription;
import android.accounts.IAccountManager;
import android.accounts.IAccountManagerResponse;
import android.os.Bundle;
import android.os.RemoteException;

class MyAccountManagerService extends IAccountManager.Stub {
    @Override
    public String getPassword(Account account) throws RemoteException {
        return null;
    }

    @Override
    public String getUserData(Account account, String s) throws RemoteException {
        return null;
    }

    @Override
    public AuthenticatorDescription[] getAuthenticatorTypes() throws RemoteException {
        return new AuthenticatorDescription[0];
    }

    @Override
    public Account[] getAccounts(String s) throws RemoteException {
        return new Account[0];
    }

    @Override
    public void getAccountsByFeatures(IAccountManagerResponse iAccountManagerResponse, String s, String[] strings) throws RemoteException {
    }

    @Override
    public boolean addAccount(Account account, String s, Bundle bundle) throws RemoteException {
        return false;
    }

    @Override
    public void removeAccount(IAccountManagerResponse iAccountManagerResponse, Account account) throws RemoteException {
    }

    @Override
    public void invalidateAuthToken(String s, String s1) throws RemoteException {
    }

    @Override
    public String peekAuthToken(Account account, String s) throws RemoteException {
        return null;
    }

    @Override
    public void setAuthToken(Account account, String s, String s1) throws RemoteException {
    }

    @Override
    public void setPassword(Account account, String s) throws RemoteException {
    }

    @Override
    public void clearPassword(Account account) throws RemoteException {
    }

    @Override
    public void setUserData(Account account, String s, String s1) throws RemoteException {
    }

    @Override
    public void getAuthToken(IAccountManagerResponse iAccountManagerResponse, Account account, String s, boolean b, boolean b1, Bundle bundle) throws RemoteException {
    }

    @Override
    public void addAcount(IAccountManagerResponse iAccountManagerResponse, String s, String s1, String[] strings, boolean b, Bundle bundle) throws RemoteException {
    }

    @Override
    public void updateCredentials(IAccountManagerResponse iAccountManagerResponse, Account account, String s, boolean b, Bundle bundle) throws RemoteException {
    }

    @Override
    public void editProperties(IAccountManagerResponse iAccountManagerResponse, String s, boolean b) throws RemoteException {
    }

    @Override
    public void confirmCredentials(IAccountManagerResponse iAccountManagerResponse, Account account, Bundle bundle, boolean b) throws RemoteException {
    }
}
