/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.archive.common.engine.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csstudio.archive.common.engine.Messages;
import org.csstudio.archive.common.engine.model.ArchiveChannel;
import org.csstudio.archive.common.engine.model.ArchiveGroup;
import org.csstudio.archive.common.engine.model.EngineModel;

/** Provide web page with list of disconnected channels
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
class DisconnectedResponse extends AbstractResponse
{
    /** Avoid serialization errors */
    private static final long serialVersionUID = 1L;
    
    DisconnectedResponse(final EngineModel model)
    {
        super(model);
    }
    
    @Override
    protected void fillResponse(final HttpServletRequest req,
                    final HttpServletResponse resp) throws Exception
    {
        final HTMLWriter html = new HTMLWriter(resp, Messages.HTTP_DisconnectedTitle);
        html.openTable(1, new String[] { "#", Messages.HTTP_Channel, Messages.HTTP_Group });

        
        
        final int group_count = model.getGroupCount();
        int disconnected = 0;
        for (ArchiveGroup group : model.getGroups()) {
            for (ArchiveChannel<?> channel : group.getChannels()) {
                if (channel.isConnected())
                    continue;
                ++disconnected;
                html.tableLine(new String[]
                                          {
                                           Integer.toString(disconnected),
                                           HTMLWriter.makeLink("channel?name=" + channel.getName(), channel.getName()),
                                           HTMLWriter.makeLink("group?name=" + group.getName(), group.getName()),
                                          } );
            }
        }
        html.closeTable();

        if (disconnected == 0)
            html.h2("All channels are connected");            
        
        html.close();
    }
}
