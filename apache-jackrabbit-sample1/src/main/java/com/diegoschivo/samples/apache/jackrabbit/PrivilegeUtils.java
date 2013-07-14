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
