package com.space000;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class RestaurantReviewSiteApplication {

	public static void main(String[] args) {
		String dir = "review";
		File file = new File(dir);

		if (!file.exists()) {
			file.mkdir();
		}

		File bannerDir = new File(dir, "banner");
		File memberDir = new File(dir, "member");
		File noticeDir = new File(dir, "notice");
		File reviewDir = new File(dir, "review");

		bannerDir.mkdir();
		memberDir.mkdir();
		noticeDir.mkdir();
		reviewDir.mkdir();

		SpringApplication.run(RestaurantReviewSiteApplication.class, args);
	}

}
