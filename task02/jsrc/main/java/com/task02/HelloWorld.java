package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.syndicate.deployment.model.lambda.url.InvokeMode;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;

@LambdaHandler(
		lambdaName = "hello_world",
		roleName = "hello_world-role",
		isPublishVersion = false,
		logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaUrlConfig(
		authType = AuthType.NONE,
		invokeMode = InvokeMode.BUFFERED
)
public class HelloWorld implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

	@Override
	public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context) {
		APIGatewayV2HTTPResponse response;
		try {
			String pathName = event.getRawPath();
			String httpMethodName = (event.getRequestContext() != null && event.getRequestContext().getHttp() != null)
					? event.getRequestContext().getHttp().getMethod()
					: null;

			if ("/hello".equals(pathName) && "GET".equalsIgnoreCase(httpMethodName)) {
				response = APIGatewayV2HTTPResponse.builder()
						.withStatusCode(200)
						.withBody("{\"statusCode\": 200, \"message\": \"Hello from Lambda\"}")
						.build();
			} else {
				response = APIGatewayV2HTTPResponse.builder()
						.withStatusCode(400)
						.withBody("{\"statusCode\": 400, \"message\": \"Bad request syntax or unsupported method. Request path: " + (pathName != null ? pathName : "null") + ". HTTP method: " + (httpMethodName != null ? httpMethodName : "null") + "\"}")
						.build();
			}
		} catch (Exception e) {
			context.getLogger().log("Error occurred: " + e.getMessage());
			response = APIGatewayV2HTTPResponse.builder()
					.withStatusCode(500)
					.withBody("{\"statusCode\": 500, \"message\": \"Internal Server Error\"}")
					.build();
		}
		return response;
	}
}