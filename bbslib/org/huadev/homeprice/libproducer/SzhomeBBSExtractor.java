/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.huadev.homeprice.libproducer;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.huadev.util.FileUtil;

public class SzhomeBBSExtractor {
	
	String m_strPath;
	String m_strExtractPath;
	
	SzhomeBBSExtractor(String strPath,String strExtractDir)
	{
		m_strPath = strPath;	
		m_strExtractPath = FileUtil.changeToStandardPath(strExtractDir);
		m_strExtractPath += FileUtil.getFilename(strPath);		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	


    public static void main(String[] args) throws Exception {
        // The fluent API relieves the user from having to deal with manual
        // deallocation of system resources at the cost of having to buffer
        // response content in memory in some cases.

        Content strResult = Request.Get("http://ris.szpl.gov.cn/bol/")
            .execute().returnContent();
        String str = strResult.asString();
        System.out.println(str);
        /*
        Request.Post("http://ris.szpl.gov.cn/bol/")
            .bodyForm(Form.form().add("username",  "vip").add("password",  "secret").build())
            .execute().returnContent();
            */
    }
}
