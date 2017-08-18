/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.descartes.petstore.recommender.servlet;

import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import tools.descartes.petstore.recommender.rest.TrainEndpoint;
import tools.descartes.petstore.registryclient.RegistryClient;
import tools.descartes.petstore.registryclient.Service;
import tools.descartes.petstore.registryclient.StartupCallback;

/**
 * Application Lifecycle Listener implementation class Registry Client Startup.
 * @author Simon Eismann
 *
 */
@WebListener
public class RecommenderStartup implements ServletContextListener {
	
	/**
	 * Also set this accordingly in RegistryClientStartup.
	 */
	
	/**
	 * Empty constructor.
	 */
    public RecommenderStartup() {
    	
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     * @param event The servlet context event at destruction.
     */
    public void contextDestroyed(ServletContextEvent event)  { 
		RegistryClient.CLIENT.unregister(event.getServletContext().getContextPath());
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     * @param event The servlet context event at initialization.
     */
    public void contextInitialized(ServletContextEvent event)  {
    	RegistryClient.CLIENT.runAfterServiceIsAvailable(Service.PERSISTENCE, new StartupCallback() {
			
			@Override
			public void callback() {
				TrainEndpoint.retrieveDataAndRetrain();
				RegistryClient.CLIENT.register(event.getServletContext().getContextPath());
			}
		});
    }
    
}