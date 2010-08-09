/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Sun Microsystems, Inc. All rights reserved.
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

package org.glassfish.admin.rest.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * @author Mitesh Meswani
 */
public class TextClassWriter implements ClassWriter {

     Writer writer;

    /**
     * @param className Name of class to be generated
     * @param generationDir Absolute location where it needs to be generated
     * @param baseClassName
     * @param resourcePath
     */
    public TextClassWriter(File generationDir, String className, String baseClassName, String resourcePath) throws IOException {
        File file = new File(generationDir, className + ".java");
        file.createNewFile();
        FileWriter fstream = new FileWriter(file);
        writer = new BufferedWriter(fstream);

        writeCopyRightHeader();
        writePackageHeader();
        writeImportStatements();

        if(resourcePath != null) {
            writer.write("@Path(\"/" + resourcePath + "/\")\n");
        }

        writer.write("public class " + className + " extends " + baseClassName + "  {\n\n");

    }

    private void writePackageHeader() throws IOException {
        writer.write("package org.glassfish.admin.rest.resources.generated;\n");
    }

    private void writeCopyRightHeader() throws IOException {
        writer.write("/*\n");
        writer.write(" * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.\n");
        writer.write(" *\n");
        writer.write(" * Copyright 2009-2010 Sun Microsystems, Inc. All rights reserved.\n");
        writer.write(" *\n");
        writer.write(" * The contents of this file are subject to the terms of either the GNU\n");
        writer.write(" * General Public License Version 2 only (\"GPL\") or the Common Development\n");
        writer.write(" * and Distribution License(\"CDDL\") (collectively, the \"License\").  You\n");
        writer.write(" * may not use this file except in compliance with the License. You can obtain\n");
        writer.write(" * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html\n");
        writer.write(" * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific\n");
        writer.write(" * language governing permissions and limitations under the License.\n");
        writer.write(" *\n");
        writer.write(" * When distributing the software, include this License Header Notice in each\n");
        writer.write(" * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.\n");
        writer.write(" * Sun designates this particular file as subject to the \"Classpath\" exception\n");
        writer.write(" * as provided by Sun in the GPL Version 2 section of the License file that\n");
        writer.write(" * accompanied this code.  If applicable, add the following below the License\n");
        writer.write(" * Header, with the fields enclosed by brackets [] replaced by your own\n");
        writer.write(" * identifying information: \"Portions Copyrighted [year]\n");
        writer.write(" * [name of copyright owner]\"\n");
        writer.write(" *\n");
        writer.write(" * Contributor(s):\n");
        writer.write(" *\n");
        writer.write(" * If you wish your version of this file to be governed by only the CDDL or\n");
        writer.write(" * only the GPL Version 2, indicate your decision by adding \"[Contributor]\n");
        writer.write(" * elects to include this software in this distribution under the [CDDL or GPL\n");
        writer.write(" * Version 2] license.\"  If you don't indicate a single choice of license, a\n");
        writer.write(" * recipient has the option to distribute your version of this file under\n");
        writer.write(" * either the CDDL, the GPL Version 2 or to extend the choice of license to\n");
        writer.write(" * its licensees as provided above.  However, if you add GPL Version 2 code\n");
        writer.write(" * and therefore, elected the GPL Version 2 license, then the option applies\n");
        writer.write(" * only if the new code is made subject to such option by the copyright\n");
        writer.write(" * holder.\n");
        writer.write(" */\n");
    }

    private void writeImportStatements() throws IOException {
        writer.write("import javax.ws.rs.Path;\n");
        writer.write("import javax.ws.rs.PathParam;\n");
        writer.write("import org.glassfish.admin.rest.resources.*;\n");
        writer.write("import org.glassfish.admin.rest.resources.custom.*;\n");
    }


    @Override
    public void createCommandResourceConstructor(String commandResourceClassName, String commandName, String httpMethod, boolean linkedToParent, CommandResourceMetaData.ParameterMetaData[] commandParams, String commandDisplayName, String commandAction) {
        try {
            writer.write("   public " + commandResourceClassName + "() {\n");
            writer.write("       super(\n");
            writer.write("          \"" + commandResourceClassName + "\",\n");
            writer.write("          \"" + commandName + "\",\n");
            writer.write("          \"" + httpMethod + "\",\n");
            if (!httpMethod.equals("GET")) {
                writer.write("          \"" + commandAction + "\",\n");
                writer.write("          \"" + commandDisplayName + "\",\n");
            }

            writer.write("          " + linkedToParent + ");\n");
            writer.write("    }\n");

            if (commandParams != null) {
                writer.write("@Override\n");
                writer.write("protected java.util.HashMap<String, String> getCommandParams() {\n");
                writer.write("\tjava.util.HashMap<String, String> hm = new java.util.HashMap<String, String>();\n");
                for (CommandResourceMetaData.ParameterMetaData commandParam : commandParams) {
                    writer.write("\thm.put(\"" + commandParam.name + "\",\"" + commandParam.value + "\");\n");
                }

                writer.write("\treturn hm;\n");
                writer.write("}\n");
            }

        } catch (IOException e) {
            throw new GeneratorException(e);
        }
    }

    @Override
     public void createGetCommandResource(String commandResourceClassName, String resourcePath) {
        //define method with @Path in resource- resourceName
        try {
            writer.write("@Path(\"" + resourcePath + "/\")\n");
            writer.write("public " + commandResourceClassName + " get"
                    + commandResourceClassName + "() {\n");
            writer.write(commandResourceClassName + " resource = resourceContext.getResource(" + commandResourceClassName + ".class);\n");
            writer.write("return resource;\n");
            writer.write("}\n\n");
        } catch (IOException e) {
            throw new GeneratorException(e);
        }
    }

    @Override
    public void createCustomResourceMapping(String resourceClassName, String mappingPath) {
        try {
            writer.write("\n");
            writer.write("\t@Path(\"" + mappingPath + "/\")\n");
            writer.write("\tpublic " + resourceClassName + " get" + resourceClassName + "() {\n");
            writer.write("\t\t" + resourceClassName + " resource = resourceContext.getResource(" + resourceClassName + ".class);\n");
            writer.write("\t\tresource.setEntity(getEntity());\n");
            writer.write("\t\treturn resource;\n");
            writer.write("\t}\n\n");
        } catch (IOException e) {
            throw new GeneratorException(e);
        }
    }

    @Override
    public void createGetCommandResourcePaths(List<CommandResourceMetaData> commandMetaData) {
        assert commandMetaData.size() > 0 : "It is assumed that this method is called only if commandMetaData.size() > 0";

        try {
            writer.write("@Override\n");
            writer.write("public String[][] getCommandResourcesPaths() {\n");

            StringBuilder commandResourcesPaths = new StringBuilder();
            for (CommandResourceMetaData metaData : commandMetaData) {
                if (commandResourcesPaths.length() > 0) {
                    commandResourcesPaths = commandResourcesPaths.append(", ");
                }

                commandResourcesPaths = commandResourcesPaths .append( "{")
                   .append('"').append(metaData.resourcePath).append("\", ")
                   .append('"').append(metaData.httpMethod).append("\", ")
                   .append('"').append(metaData.command).append("\"} ");
            }

            writer.write("return new String[][] {" + commandResourcesPaths + "};\n");
            writer.write("}\n\n");
        } catch (IOException e) {
            throw new GeneratorException(e);
        }
    }

    @Override
    public void createGetDeleteCommand(String commandName) {
        try {
            writer.write("@Override\n");
            writer.write("public String getDeleteCommand() {\n");
            writer.write("\treturn \"" + commandName + "\";\n");
            writer.write("}\n");
        } catch (IOException e) {
            throw new GeneratorException(e);
        }
    }

    @Override
    public void createGetPostCommand(String commandName) {
        //TODO this method and createGetDeleteCommand method may be merged to single method createGetXXXCommand(String commandName, String httpMethod)
        try {
            writer.write("@Override\n");
            writer.write("public String getPostCommand() {\n");
            writer.write("\treturn \"" + commandName + "\";\n");
            writer.write("}\n");
        } catch (IOException e) {
            throw new GeneratorException(e);
        }
    }

    @Override
    public void createGetChildResource(String path, String childResourceClassName) {
        try {
            writer.write("\t@Path(\"" + path + "/\")\n");
            writer.write("\tpublic " + childResourceClassName + " get" + childResourceClassName + "() {\n");

            writer.write("\t\t" + childResourceClassName + " resource = resourceContext.getResource(" + childResourceClassName + ".class);\n");
            writer.write("\t\tresource.setParentAndTagName(getEntity() , \"" + path + "\");\n");
            writer.write("\t\treturn resource;\n");
            writer.write("\t}\n");
        } catch (IOException e) {
            throw new GeneratorException(e);
        }
    }

    @Override
    public void createGetChildResourceForListResources(String keyAttributeName, String childResourceClassName) {
        try {
            writer.write("\n");
            writer.write("\t@Path(\"{" + keyAttributeName + "}/\")\n");
            writer.write("\tpublic " + childResourceClassName + " get" + childResourceClassName + "(@PathParam(\"" + keyAttributeName + "\") String id) {\n");
            writer.write("\t\t" + childResourceClassName + " resource = resourceContext.getResource(" + childResourceClassName + ".class);\n");
            writer.write("\t\tresource.setBeanByKey(entity, id);\n");
            writer.write("\t\treturn resource;\n");
            writer.write("\t}\n\n");
        } catch (IOException e) {
            throw new GeneratorException(e);
        }
    }

    @Override
    public void createGetPostCommandForCollectionLeafResource(String postCommandName) {
        try {
            writer.write("@Override\n");
            writer.write("protected String getPostCommand(){\n");
            writer.write("return \"" + postCommandName + "\";\n");
            writer.write("}\n");
        } catch (IOException e) {
            throw new GeneratorException(e);
        }
    }

    @Override
    public void createGetDeleteCommandForCollectionLeafResource(String deleteCommandName) {
        try {
            writer.write("@Override\n");
            writer.write("protected String getDeleteCommand(){\n");
            writer.write("return \"" + deleteCommandName + "\";\n");
            writer.write("}\n");
        } catch (IOException e) {
            throw new GeneratorException(e);
        }
    }

    @Override
    public void createGetDisplayNameForCollectionLeafResource(String displayName) {
        try {
            writer.write("@Override\n");
            writer.write("protected String getName(){\n");
            writer.write("return \"" + displayName + "\";\n");
            writer.write("}\n");
        } catch (IOException e) {
            throw new GeneratorException(e);
        }
    }

    @Override
    public void done() {
        try {
            writer.write("}\n");
            writer.close();
        } catch (IOException e) {
            throw new GeneratorException(e);
        }
    }
}
