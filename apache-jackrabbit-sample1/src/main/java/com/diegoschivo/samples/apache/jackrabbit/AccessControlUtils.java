/**
 *    Copyright 2013 Diego Schivo
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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
