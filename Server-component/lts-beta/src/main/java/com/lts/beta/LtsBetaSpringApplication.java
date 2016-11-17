package com.lts.beta;

import models.UserIdAndSensitivityLevel;
import monitor.Monitor;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;


@Configuration
@ComponentScan
@Controller
@EnableAutoConfiguration
public class LtsBetaSpringApplication {

//	@Autowired
//	public LtsBetaSpringApplication(MongoTemplate mongoTemplate) {
//		this.mongoTemplate = mongoTemplate;
//	}

//	private final MongoTemplate mongoTemplate;

	private MongoTemplate template;

	@RequestMapping(value = "/process", method = RequestMethod.POST)
	@ResponseBody
	public Result processUser(@RequestBody ProcessingRequest processingRequest){
		System.out.println("Received process request: " + processingRequest);
		String accessToken = processingRequest.getFacebookToken();
		String userId = Monitor.getUserId(accessToken);

		//call monitor to get update for userId,

		Result result = new Result();
		result.setNaughtyFriend("Magdalena");
		result.setTitle("Magdalena was naughty");
		result.setUrl("facebook.com/someUrl");

		return result;
	}

	@RequestMapping("/settings")
	public String servePage() {
		return "hellostatic.html";
	}


	@RequestMapping("/warning-message")
	public String loadMain() {
		return "warning.html";
	}

	@RequestMapping("/fblogin")
	public String facebookLogin() {
		return "facebooklogin.html";
	}

	@RequestMapping("/track")
	public void trackMe(@RequestBody ProcessingRequest processingRequest) {
		System.out.println("Received request to track user: " + processingRequest);
		String accessToken = processingRequest.getFacebookToken();
		String userId = Monitor.getUserId(accessToken);

		template.save(new UserIdAndSensitivityLevel(userId, processingRequest.getSensitivityLevel()), "userIdAndSensitivityLevel");

		System.out.println("tracking user with userId=" + userId);
	}

	@Bean
	public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
		MongoTemplate template = new MongoTemplate(mongoDbFactory);
		this.template = template;
		return template;
	}
}