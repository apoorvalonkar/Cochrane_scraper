package com.example.cochraneLibraryScraper.console.command;

import com.example.cochraneLibraryScraper.cochraneLibraryScraper;
import com.example.cochraneLibraryScraper.service.OutputFile;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class AppplicationCommand {

    @ShellMethod("Start scraping")
    public void cochrane(@ShellOption(defaultValue = "1") int pageNumber)
    {
        String FILE_PATH = "/Users/apoorvalonkar/Desktop/cochraneLibraryScraper/";
        String OUTPUT_FILE_NAME = "cochrane_reviews.txt";

        String reviewFile = String.valueOf(cochraneLibraryScraper.scraper(pageNumber));

        System.out.println("Writing result into the file");
        OutputFile.toFile(FILE_PATH, OUTPUT_FILE_NAME, reviewFile);
    }

}
