package employeerest;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class EmpMain  {
	public static void main(String args[]) {
		//to remove this statement at the time of deployment 
		VertxOptions vxOptions = new VertxOptions()
				.setBlockedThreadCheckInterval(200000000);
		Vertx vertx= Vertx.vertx(vxOptions);
		//deploy rest verticle 
		vertx.deployVerticle(new EmpREST());
	}
}
