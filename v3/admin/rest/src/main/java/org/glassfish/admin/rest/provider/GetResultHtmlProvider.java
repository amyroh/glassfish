/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009-2010 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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
package org.glassfish.admin.rest.provider;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.glassfish.admin.rest.results.GetResult;
import org.jvnet.hk2.config.ConfigBean;
import org.jvnet.hk2.config.Dom;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.Set;

import static org.glassfish.admin.rest.Util.*;
import static org.glassfish.admin.rest.provider.ProviderUtil.getElementLink;

/**
 *
 * @author Rajeshwar Patil
 * @author Ludovic Champenois ludo@dev.java.net
 * @author Jason Lee
 */
@Provider
@Produces(MediaType.TEXT_HTML)
public class GetResultHtmlProvider extends BaseProvider<GetResult> {

    public GetResultHtmlProvider() {
        super(GetResult.class, MediaType.TEXT_HTML_TYPE);
    }

    @Override
    protected String getContent(GetResult proxy) {
        String result = ProviderUtil.getHtmlHeader();
        final String typeKey = upperCaseFirstLetter((decode(getName(uriInfo.getAbsolutePath().toString(), '/'))));
        result = result + "<h1>" + typeKey + "</h1>";

        String attributes = ProviderUtil.getHtmlRespresentationForAttributes((ConfigBean)proxy.getDom(), uriInfo);
        result = ProviderUtil.getHtmlForComponent(attributes, "Attributes", result);

        String deleteCommand = ProviderUtil.getHtmlRespresentationsForCommand(
                proxy.getMetaData().getMethodMetaData("DELETE"), "DELETE", "Delete", uriInfo);
        result = ProviderUtil.getHtmlForComponent(deleteCommand, "Delete " + typeKey, result);

        String childResourceLinks = getResourcesLinks(proxy);
        result = ProviderUtil.getHtmlForComponent(childResourceLinks, "Child Resources", result);

        String commandLinks = getCommandLinks(proxy.getCommandResourcesPaths());
        result = ProviderUtil.getHtmlForComponent(commandLinks, "Commands", result);

        result = result + "</body></html>";
        return result;
    }

    private String getResourcesLinks(GetResult getResult) {
        StringBuilder links = new StringBuilder("<div>");
        for (Map.Entry<String, String> link : getResourceLinks(getResult.getDom()).entrySet()) {
            links.append("<a href=\"")
                .append(link.getValue())
                .append("\">")
                .append(link.getKey())
                .append("</a><br>");

        }

        return links.append("</div><br>").toString();
    }

    private String getCommandLinks(String[][] commandResourcesPaths) {
        StringBuilder result = new StringBuilder("<div>");

        for (String[] commandResourcePath : commandResourcesPaths) {
            if (commandResourcePath[2].startsWith("_")){//hidden cli command name
                result.append("<!--");//hide the link in a comment
            }
                result.append("<a href=\"")
                                    .append(ProviderUtil.getElementLink(uriInfo, commandResourcePath[0]))
                                    .append("\">")
                                    .append(commandResourcePath[0])
                                    .append("</a><br>");
            if (commandResourcePath[2].startsWith("_")){//hidden cli
                result.append("-->");
            }
        }

        return result.append("</div><br>").toString();
    }
}