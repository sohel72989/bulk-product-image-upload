package com.product.images.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Service
public class FileStorageService {

	public Optional<String> provideFileExtension(String filename) {
		return Optional.ofNullable(filename).filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}

	public String getNextProductFolderName(Path basePath) throws IOException {

		if (!Files.exists(basePath)) {
			Files.createDirectories(basePath);
		}

		int max = 0;

		try (Stream<Path> stream = Files.list(basePath)) {

			List<Path> folderList = stream.collect(Collectors.toList());

			for (Path p : folderList) {
				String folder = p.getFileName().toString();

				if (folder.startsWith("product-")) {

					String numberPart = folder.substring("product-".length());

					if (numberPart.matches("\\d+")) {
						int num = Integer.parseInt(numberPart);
						if (num > max)
							max = num;
					}
				}
			}
		}

		return "product-" + (max + 1);
	}

}
