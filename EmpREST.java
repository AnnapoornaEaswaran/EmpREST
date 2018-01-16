package employeerest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.HttpServerRequest;
public class EmpREST extends AbstractVerticle {
	HttpServer httpServer;
	JsonObject emp,temp;
	Map<String, JsonObject> empInfo;
	
	public void start() {
		initEmployees();
		
		Router router = Router.router(vertx);

		    router.route().handler(BodyHandler.create());
		    router.get("/allemployees").handler(ctx->  {
		    	
				HttpServerRequest request= ctx.request();
				JsonArray empArray= new JsonArray();
				
				if(!empInfo.isEmpty()) {
					for(String t:empInfo.keySet())
						(empArray).add(empInfo.get(t));
					HttpServerResponse response=request.response();
					response.setStatusCode(200);
					response.putHeader("content-type", "application/json").end(empArray.toString());
					
				}
				else
					sendErrorMessage(request);
				
			});
		    router.get("/employee").handler(ctx->  {
		    	
				HttpServerRequest request= (HttpServerRequest) ctx.request();
				String eid=request.getParam("id");
				if(empInfo.containsKey(eid)) {
					HttpServerResponse response=request.response();
					response.setStatusCode(200);
					emp=empInfo.get(eid);
					response.putHeader("content-type", "application/json").end(emp.toString());
					
				}
				else
					sendErrorMessage(request);
				
			});
		    router.post("/pemployee").handler(ctx-> {
				// add a new employee in the database
				// if id already present, record the changed parameters' values
				HttpServerRequest request= (HttpServerRequest)ctx.request();
				HttpServerResponse response=request.response();
				response.setStatusCode(200);		
				String eid=request.getParam("id");
				String name=request.getParam("ename");
				String designation=request.getParam("desig");
				
				if(empInfo.containsKey(eid)) {
					emp=empInfo.get(eid);
					emp.put("name", name);
					emp.put("designation", designation);
					empInfo.put(eid,emp);
				
				}
				else {
					emp=new JsonObject();
					emp.put("id", eid);
					emp.put("name", name);			
					//response.write("<html><h1>Record Added </h1></html>");

					emp.put("designation", designation);
					empInfo.put(eid,emp);

				}
			
				response.end();
			});
		    
		    httpServer=vertx.createHttpServer();
		    httpServer.requestHandler(router::accept);
		    httpServer.listen(9991);
		//start server
		//initialize router/handler
		//set port 
		//server is now listening
	}
	// relevant methods
	/*private void getEmpDetails(RoutingContext ctx) {
	
		HttpServerRequest request= ctx.request();
		String eid=request.getParam("id");
		if(empInfo.containsKey(eid)) {
			HttpServerResponse response=request.response();
			response.setStatusCode(200);
			emp=empInfo.get(eid);
			response.putHeader("content-type", "application/json").end(emp.encodePrettily());
			
		}
		else
			sendErrorMessage(request);
		
	}
	private void getAllEmpDetails(RoutingContext ctx) {
	
		HttpServerRequest request= (HttpServerRequest) ctx.request();
		JsonArray empArray= new JsonArray();
		
		if(!empInfo.isEmpty()) {
			for(String t:empInfo.keySet())
				(empArray).add(empInfo.get(t));
			HttpServerResponse response=request.response();
			response.setStatusCode(200);
			response.putHeader("content-type", "application/json").end(empArray.encodePrettily());
			
		}
		else
			sendErrorMessage(request);
		
	}
	private void add_or_updateEmpDetails(RoutingContext ctx) {
		// add a new employee in the database
		// if id already present, record the changed parameters' values
		HttpServerRequest request= (HttpServerRequest)ctx.request();
		HttpServerResponse response=request.response();
		response.setStatusCode(200);		
		String eid=request.getParam("id");
		String name=request.getParam("ename");
		String designation=request.getParam("desig");
		
		if(empInfo.containsKey(eid)) {
			emp=empInfo.get(eid);
			emp.put("name", name);
			emp.put("designation", designation);
			empInfo.put(eid,emp);
		
		}
		else {
			emp=new JsonObject();
			emp.put("id", eid);
			emp.put("name", name);			response.write("<html><h1>Record Added </h1></html>");

			emp.put("designation", designation);
			empInfo.put(eid,emp);

		}
	
		response.end();
	}*/
	
	private void initEmployees() {
		// initialize HashMap 
		empInfo=new HashMap<>();
		empInfo.put("e1", employee("e1","ABC","Clerk"));
		empInfo.put("e2", employee("e2","DEF","Clerk"));
		empInfo.put("e3", employee("e3","JKL","officer"));
		empInfo.put("e4", employee("e4","GHI","officer"));
		empInfo.put("e5", employee("e5","ADA","MTS"));
		
	}
	public JsonObject employee(String id, String name, String desig) {
		JsonObject e=new JsonObject();
		e.put("id", id);
		e.put("name", name);
		e.put("designation", desig);
		return e;
	}
	public void sendErrorMessage(HttpServerRequest request) {
		HttpServerResponse response=request.response();
		response.setStatusCode(404).end();
		
	}
	public void stop() {
		httpServer.close();
	}
}
