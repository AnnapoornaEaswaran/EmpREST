package employeerest;

import io.vertx.rxjava.ext.web.RoutingContext;

@FunctionalInterface
public interface RouteInterface {
	void getEmpDetails(RoutingContext ctx);
	//void getAllEmpDetails(RoutingContext ctx);
	//void add_or_updateEmpDetails(RoutingContext ctx);

}
