/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
// Portions Copyright [2017-2018] [Payara Foundation and/or its affiliates]
package com.sun.enterprise.security.ee.auth.login;

import static java.util.logging.Level.FINEST;

import java.util.Arrays;

import javax.security.auth.login.LoginException;

import com.sun.enterprise.security.BasePasswordLoginModule;
import com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm;

/**
 * This class implement a JDBC Login module for Payara.
 *
 * <p>
 * The work is derived from Sun's sample JDBC login module. 
 * Enhancements have been done to use the very latest features.
 *
 * @author Jean-Baptiste Bugeaud
 */
public class JDBCLoginModule extends BasePasswordLoginModule {

    /**
     * Perform JDBC authentication. Delegates to JDBCRealm.
     *
     * @throws LoginException If login fails (JAAS login() behavior).
     */
    @Override
    protected void authenticateUser() throws LoginException {
        JDBCRealm jdbcRealm = getRealm(JDBCRealm.class, "jdbclm.badrealm");

        // A JDBC user must have a name not null and non-empty.
        if (_username == null || _username.length() == 0) {
            throw new LoginException(sm.getString("jdbclm.nulluser"));
        }

        String[] groups = jdbcRealm.authenticate(_username, getPasswordChar());

        if (groups == null) { // JAAS behavior
            throw new LoginException(sm.getString("jdbclm.loginfail", _username));
        }

        if (_logger.isLoggable(FINEST)) {
            _logger.finest("JDBC login succeeded for: " + _username + " groups:" + Arrays.toString(groups));
        }

        commitUserAuthentication(groups);
    }
}
