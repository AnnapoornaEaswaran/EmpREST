package employeerest;

import java.util.Map;

import org.json.JSONObject;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.rxjava.ext.web.RoutingContext;
import shaded.org.apache.http.HttpResponse;

public class EmpREST extends AbstractVerticle {
	HttpServer httpServer;
	JSONObject emp,temp;
	Map<String, JSONObject> empInfo;
	
	public void start() {
		 Router router = Router.router(vertx);

		    router.route().handler(BodyHandler.create());
		    router.get("/allemployees").handler(this::getAllEmpDetails);
		    router.get("/employee").handler(this::getEmpDetails);
		    router.put("/employee").handler(this::add_or_updateEmpDetails);
		    
		    httpServer=vertx.createHttpServer();
		    httpServer.requestHandler(router::accept;
		    httpServer.listen(9000);
		//start server
		//initialize router/handler
		//set port 
		//server is now listening
	}
	// relevant methods
	public void getEmpDetails(RoutingContext ctx) {
		// details of a particular employee
		HttpRequest request= ctx.request();
		String eid=request.getParam("id");
		if(empInfo.containsKey(eid)) {
			HttpResponse response=request.response();
			response.setStatusCode(200);
			response.getAllHeaders().add("Content-type","application/json");
			emp=empInfo.get(eid);
			response.write(emp);
			response.end();
		
		}
		else
			sendErrorMessage();
		
	}
	public void getAllEmpDetails(RoutingContext ctx) {
		// details of all employees
		HttpRequest request= ctx.request();
		JsonArray empArray= new JSONArray();
		
		if(!empInfo.isEmpty()) {
			for(String t:empInfo.keySet())
				empArray.add(empInfo.get(t));
			HttpResponse response=request.response();
			response.setStatusCode(200);
			response.getAllHeaders().add("Content-type","application/json");
			response.write(empArray);
			response.end();
		
		}
		else
			sendErrorMessage();
		
	}
	public void add_or_updateEmpDetails(RoutingContext ctx) {
		// add a new employee in the database
		// if id already present, record the changed parameters' values
		HttpRequest request= ctx.request();
		HttpResponse response=request.response();
		response.setStatusCode(200);
		response.getAllHeaders().add("Content-type","text/html").add("Content-length","60");
		
		String eid=request.getParam("id");
		String name=request.getParam("ename");
		String designation=request.getParam("desig");
		
		if(empInfo.containsKey(eid)) {
			emp=empInfo.get(eid);
			emp.put("name", name);
			emp.put("designation", designation);
			empInfo.put(eid,emp);
			response.write("<html><h1>Record Updated </h1></html>");
		
		}
		else {
			emp=new JSONObject();
			emp.put("id", eid);
			emp.put("name", name);
			emp.put("designation", designation);
			empInfo.put(eid,emp);
			response.write("<html><h1>Record Added </h1></html>");

		}
		response.end();
	
	}
	public void initEmployees() {
		// initialize HashMap 
		empInfo=new HashMap<>();
		empInfo.put("e1", employee("e1","ABC","Clerk"));
		empInfo.put("e2", employee("e2","DEF","Clerk"));
		empInfo.put("e3", employee("e3","JKL","officer"));
		empInfo.put("e4", employee("e4","GHI","officer"));
		empInfo.put("e5", employee("e5","ADA","MTS"));
		
	}
	public JSONObject employee(String id, String name, String desig) {
		JSONObject e=new JSONObject();
		e.put("id", id);
		e.put("name", name);
		e.put("designation", desig);
		return e;
	}
	public void sendErrorMessage(HttpRequest request) {
		HttpResponse response=request.response();
		response.setStatusCode(404);
		response.add("Content-Length","60")
	    .add("Content-Type", "text/html");
		response.write("<html><h1>Sorry No such id found! </h1></html>");
		response.end();
	}
	public void stop() {
		httpServer.close();
	}
}
