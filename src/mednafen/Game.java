package mednafen;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Game {
	private ArrayList<String> games;
	private Map<String, String> paths;

	public Game(List<String> games, Map<String, String> paths) {
		super();
		this.games = new ArrayList<>(games);
		this.paths = paths;
	}

	public static Game parseGames(String game_root_dir) throws IOException {
		List<Path> files = Files.walk(Paths.get(game_root_dir))
				.filter(Files::isRegularFile)
				.filter(f -> f.toString().toLowerCase().endsWith(".cue"))
				.collect(Collectors.toList());
		
		Map<String, String> paths = new HashMap<>();
		ArrayList<String> games = new ArrayList<>();
		for (Path file: files) {
			String name = file.getFileName().toString();
			String base_name = name.substring(0, name.lastIndexOf('.'));
			
			paths.put(base_name, file.toAbsolutePath().toString());
			games.add(base_name);
		}
		return new Game(games, paths);
	}
	
	public Object[][] search(String name) {
		ArrayList<String> result = new ArrayList<>();
		for (String game: games) {
			if (name.equals("") || game.toLowerCase().contains(name.toLowerCase())) {
				result.add(game);
			}
		}
		
		Object[][] data = new Object[result.size()][3];
		
		for (int i = 0; i < result.size(); i++) {
			data[i][0] = result.get(i);
		}
		
		return data;
	}
	
	public String getPath(String name) {
		return paths.get(name);
	}
}
