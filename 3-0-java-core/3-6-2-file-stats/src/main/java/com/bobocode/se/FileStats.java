package com.bobocode.se;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;

/**
 * {@link FileStats} provides an API that allow to get character statistic based on text file. All whitespace characters
 * are ignored.
 */
public class FileStats {

    private final Path filePath;
    private FileStats(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Creates a new immutable {@link FileStats} objects using data from text file received as a parameter.
     *
     * @param fileName input text file name
     * @return new FileStats object created from text file
     */
    public static FileStats from(String fileName) {
        return new FileStats(getPath(fileName));
    }

    /**
     * Returns a number of occurrences of the particular character.
     *
     * @param character a specific character
     * @return a number that shows how many times this character appeared in a text file
     */
    public int getCharCount(char character) {
        return (int) getCharacterStream()
            .filter(ch -> ch == character)
            .count();
    }

    /**
     * Returns a character that appeared most often in the text.
     *
     * @return the most frequently appeared character
     */
    public char getMostPopularCharacter() {
        return getCharacterStream()
            .collect(Collectors.groupingBy(Function.identity(), counting()))
            .entrySet()
            .stream().max(Comparator.comparingLong(Map.Entry::getValue))
            .orElseThrow(() -> new FileStatsException("Invalid input"))
            .getKey();
    }

    /**
     * Returns {@code true} if this character has appeared in the text, and {@code false} otherwise
     *
     * @param character a specific character to check
     * @return {@code true} if this character has appeared in the text, and {@code false} otherwise
     */
    public boolean containsCharacter(char character) {
        return getCharacterStream()
            .anyMatch(ch -> ch == character);
    }

    private static Path getPath(String fileName) {
        Objects.requireNonNull(fileName);
        URL fileUrl = FileStats.class.getClassLoader().getResource(fileName);
        if (fileUrl == null) {
            throw new FileStatsException("Invalid file URL");
        }
        try {
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new FileStatsException("Invalid file URL", e);
        }
    }

    private Stream<Character> getCharacterStream() {
        return openFileLinesStream(filePath)
            .map(string -> string.replaceAll(" ", ""))
            .flatMap(s -> s.chars().mapToObj((ch) -> (char) ch));
    }
    private static Stream<String> openFileLinesStream(Path filePath) {
        try {
            return Files.lines(filePath);
        } catch (IOException e) {
            throw new FileStatsException("Cannot create stream of file lines", e);
        }
    }
}
