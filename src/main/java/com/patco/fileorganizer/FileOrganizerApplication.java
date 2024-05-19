package com.patco.fileorganizer;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

// import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.patco.filecontroller.FileController;
import com.patco.hashmap.HashMap;

@SpringBootApplication
@RestController
public class FileOrganizerApplication {

	public static void main(String[] args) throws IOException {
		// Runs Spring App
		// SpringApplication.run(FileOrganizerApplication.class, args);

		String unorganized_bin_path = "C:\\Users\\test\\Desktop\\test-source";
		String bin_path = "C:\\Users\\test\\Desktop\\test-destination";

		FileController file_controller = new FileController();

		HashMap<String, String> map = new HashMap<>(1000);
		File file = new File("file-properties.txt");
		Scanner sc = new Scanner(file);
		StringBuilder value_property = new StringBuilder();
		// int count = 1;

		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.startsWith("*")) {
				value_property.setLength(0);
				String[] line_tokens = line.split("\\*");
				value_property.append(line_tokens[1]);
				// System.out.println(value_property + " on line " + count);
			} else {
				// System.out.println(line);
				map.put(line, value_property.toString());

			}
			// count++;
		}
		sc.close();

		file_controller.generateEnviroment(unorganized_bin_path, bin_path);
		file_controller.setBin(bin_path);
		file_controller.setUnorganizedBin(unorganized_bin_path);
		file_controller.iterateOverFiles(unorganized_bin_path, bin_path, map);

	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
}
