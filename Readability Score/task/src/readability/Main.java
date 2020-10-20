package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

class Text {
    final String TEXT;
    final int WORDS;
    final int SENTENCES;
    final int CHARACTERS;
    int syllables;
    int polysyllables;
    int[] averageAge;

    public Text(String fileName) {
        this.TEXT = this.readText(fileName);
        this.WORDS = this.TEXT.split("[\\s.!?]+").length;
        this.SENTENCES = this.TEXT.split("[.!?]").length;
        this.CHARACTERS = this.TEXT.replaceAll("[\\s]", "").length();
        this.calculateSyllables();
        this.averageAge = new int[4];
    }

    /** To count the number of syllables you should use letters a, e, i, o, u, y as vowels.
     * Let's use the following 4 rules:
     * 1. Count the number of vowels in the word.
     * 2. Do not count double-vowels (for example, "rain" has 2 vowels but only 1 syllable).
     * 3. If the last letter in the word is 'e' do not count it as a vowel
     * (for example, "side" has 1 syllable).
     * 4. If at the end it turns out that the word contains 0 vowels,
     * then consider this word as a 1-syllable one.
     */
    private void calculateSyllables() {

        String[] words = this.TEXT.split("[\\s.!?]+");
        int syllables = 0;
        int polysyllables = 0;
        for (String word : words) {
            String temp;
            if (word.charAt(word.length() - 1) == 'e') {
                temp = word;
            } else {
                temp = word + 'x';
            }
            String[] consonants = temp.split("[aeiou]+");
            if (consonants.length > 1) {
                syllables += consonants.length - 1;
                if (consonants.length - 1 >= 3) {
                    polysyllables++;
                }
            } else {
                syllables++;
            }
        }
        this.syllables = syllables;
        this.polysyllables = polysyllables;
    }

    private String readText(String fileName) {
        String text = "";
        try {
            text = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            System.out.println("Cannot find the file specified");
        }
        return text;
    }

    public void printARI() {
        double score = 4.71 * (CHARACTERS / (double) WORDS) + 0.5 * (WORDS / (double) SENTENCES) - 21.43;
        int[] age = new int[]{6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 24, 24};
        averageAge[0] = age[(int) Math.ceil(score) - 1];
        System.out.printf("Automated Readability Index: %.2f (about %d year olds).%n", score, averageAge[0]);
    }

    public void printFK() {
        double score = 0.39 * (WORDS / (double) SENTENCES) + 11.8 * (syllables / (double) WORDS) - 15.59;
        int age = 6 + (int) Math.floor(score);
        averageAge[1] = age;
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d year olds).%n", score, age);
    }

    public void printSMOG() {
        double score = 1.043 * Math.sqrt(polysyllables * (30.0 / SENTENCES)) + 3.1291;
        int age = 6 + (int) Math.floor(score);
        averageAge[2] = age;
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d year olds).%n", score, age);
    }

    public void printCL() {
        double l = CHARACTERS / (double) WORDS * 100.0;
        double s = SENTENCES / (double) WORDS * 100.0;
        double score = 0.0588 * l - 0.296 * s - 15.8;
        int age = (int) Math.ceil(score);
        averageAge[3] = age;
        System.out.printf("Coleman–Liau index: %.2f (about %d year olds).%n", score, age);
    }

    public void printAverage() {
        double sum = 0;
        for (int i : averageAge) {
            sum += i;
        }
        System.out.printf("%nThis text should be understood in average by %.2f year olds.", sum / 4.0);
    }
}

public class Main {
    public static void main(String[] args) {
        Text text = new Text(args[0]);

        System.out.printf("The text is:%n%s%n%n", text.TEXT);
        System.out.printf("Words: %d%n", text.WORDS);
        System.out.printf("Sentences: %d%n", text.SENTENCES);
        System.out.printf("Characters: %d%n", text.CHARACTERS);
        System.out.printf("Syllables: %d%n", text.syllables);
        System.out.printf("Polysyllables: %d%n", text.polysyllables);
        System.out.printf("%nEnter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String choice = new Scanner(System.in).nextLine();
        System.out.println();
        if ("ARI".equals(choice)) {
            text.printARI();
        } else if ("FK".equals(choice)) {
            text.printFK();
        } else if ("SMOG".equals(choice)) {
            text.printSMOG();
        } else if ("CL".equals(choice)) {
            text.printCL();
        } else if ("all".equals(choice)) {
            text.printARI();
            text.printFK();
            text.printSMOG();
            text.printCL();
            text.printAverage();
        }
    }
}
