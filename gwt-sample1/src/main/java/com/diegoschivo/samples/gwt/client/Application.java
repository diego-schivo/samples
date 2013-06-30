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

package com.diegoschivo.samples.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;


public class Application implements EntryPoint
{

    private final SumServiceAsync sumService = GWT.create(SumService.class);

    public void onModuleLoad()
    {
        RootPanel container = RootPanel.get("container");

        final TextBox a = new TextBox();
        container.add(a);

        final TextBox b = new TextBox();
        container.add(b);

        final Label c = new Label();
        container.add(c);

        Button button = new Button("OK", new ClickHandler()
        {

            public void onClick(ClickEvent event)
            {
                sumService.sumServer(
                    Integer.parseInt(a.getText()),
                    Integer.parseInt(b.getText()),
                    new AsyncCallback<Integer>()
                    {

                        public void onSuccess(Integer result)
                        {
                            c.setText(Integer.toString(result));
                        }

                        public void onFailure(Throwable caught)
                        {
                        }

                    });
            }
        });
        container.add(button);
    }
}
