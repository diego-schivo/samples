package com.diegoschivo.samples.apache.jackrabbit;

import java.security.Principal;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.AccessControlPolicy;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;

import org.apache.jackrabbit.api.security.JackrabbitAccessControlList;


public final class AccessControlUtils
{

    private AccessControlUtils()
    {
    }

    public static JackrabbitAccessControlList getAccessControlList(Session session, String absPath)
        throws RepositoryException
    {
        for (AccessControlPolicyIterator it = session.getAccessControlManager().getApplicablePolicies(absPath); it
            .hasNext();)
        {
            AccessControlPolicy plc = it.nextAccessControlPolicy();
            if (plc instanceof JackrabbitAccessControlList)
            {
                return (JackrabbitAccessControlList) plc;
            }
        }
        return null;
    }

    public static boolean addAccessControlEntry(Session session, String absPath, Principal principal,
        Privilege[] privileges, boolean isAllow) throws RepositoryException
    {
        JackrabbitAccessControlList acl = AccessControlUtils.getAccessControlList(session, absPath);
        if (acl == null)
        {
            return false;
        }
        boolean modified = acl.addEntry(principal, privileges, isAllow);
        if (modified)
        {
            session.getAccessControlManager().setPolicy(absPath, acl);
        }
        return modified;
    }

    public static boolean addAccessControlEntry(Session session, String absPath, Principal principal,
        String[] privilegeNames, boolean isAllow) throws RepositoryException
    {
        return addAccessControlEntry(
            session,
            absPath,
            principal,
            PrivilegeUtils.getPrivileges(privilegeNames, session.getWorkspace()),
            isAllow);
    }

    public static void removeAccessControlEntry(Session session, String absPath, Principal principal)
        throws RepositoryException
    {
        JackrabbitAccessControlList acl = AccessControlUtils.getAccessControlList(session, absPath);
        if (acl != null)
        {
            for (AccessControlEntry ace : acl.getAccessControlEntries())
            {
                if (ace.getPrincipal().equals(principal))
                {
                    acl.removeAccessControlEntry(ace);
                }
            }
        }
    }
}
