package decryptionManager.components;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dictionary {


    public Dictionary() {
        wordsSet = new HashSet<>();

    }
    private final Set<String> wordsSet;
    List<Character> excludeCharsList;
    public void getValidDictionaryWords(String words, List<Character> excludeCharsList, String alphabet) {
        this.excludeCharsList=excludeCharsList;
        words = words.toUpperCase();
        String[] wordsArray = words.trim().split(" ");
        //  System.out.println("number of words before: " + wordsSet.size());
        for (String word:wordsArray) {
            // word.toUpperCase();
            word=removeExcludeCharacter(word);
            wordsSet.add(word);

            for (int i = 0; i < word.length(); i++) {
                char character=word.charAt(i);
                if(!alphabet.contains(Character.toString(character)))
                {
                    wordsSet.remove(word);
                }
            }
            //  System.out.println(word);
        }
        // System.out.println("number of words after" + wordsSet.size());

    }
    public String removeExcludeCharacter(String input)
    {


        for (Character character:excludeCharsList)
            input=input.replaceAll(character.toString(),"");

      return input;
    }
    public boolean checkIfAllLetterInDic(String sentence)
    {
        String[] wordsArray = sentence.trim().split(" ");
        for (String word:wordsArray) {
           if(!isExistWord(word))
                 return false;
            }

        return true;
    }
    public boolean isExistWord(String word)
    {
        return wordsSet.contains(word);
    }

}
