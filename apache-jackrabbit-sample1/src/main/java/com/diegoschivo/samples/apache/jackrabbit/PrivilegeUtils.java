package com.diegoschivo.samples.apache.jackrabbit;

import javax.jcr.RepositoryException;
import javax.jcr.Workspace;
import javax.jcr.security.Privilege;

import org.apache.jackrabbit.api.JackrabbitWorkspace;
import org.apache.jackrabbit.api.security.authorization.PrivilegeManager;


public final class PrivilegeUtils
{

    private PrivilegeUtils()
    {
    }

    public static PrivilegeManager getPrivilegeManager(Workspace workspace) throws RepositoryException
    {
        return ((JackrabbitWorkspace) workspace).getPrivilegeManager();
    }

    public static Privilege getPrivilege(String privilegeName, Workspace workspace) throws RepositoryException
    {
        return getPrivilegeManager(workspace).getPrivilege(privilegeName);
    }

    public static Privilege[] getPrivileges(String[] privilegeNames, Workspace workspace) throws RepositoryException
    {
        Privilege[] privileges = new Privilege[privilegeNames.length];
        for (int i = 0; i < privileges.length; i++)
        {
            privileges[i] = getPrivilege(privilegeNames[i], workspace);
        }
        return privileges;
    }
}
