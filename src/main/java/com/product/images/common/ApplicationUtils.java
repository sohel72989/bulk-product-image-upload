package com.product.images.common;

import java.io.InputStream;
import java.util.Properties;

public class ApplicationUtils {

	public static String getFilePath(String imageFolder) {
		Properties prop = new Properties();
		InputStream inputStream = ApplicationUtils.class.getClassLoader()
				.getResourceAsStream("imagepath/image-path.properties");
		try {
			prop.load(inputStream);
			return prop.getProperty(imageFolder);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}
}
